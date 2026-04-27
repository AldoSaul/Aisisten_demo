import { useState, useEffect, useRef } from "react";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import { Link } from "react-router-dom";
import { css } from "../CSS/leadCss.tsx";
import { TENANTS, CH_COLOR, CH_LABEL, CH_LIGHT, normalizeConv, normalizeMsg } from "../helper/data.tsx";
import { getConversations, getMessages, markAsRead, sendMessage } from "../api/client.ts";

type LeadsAppProps = {
  onLogout: () => void;
};

export default function LeadsApp({ onLogout }: LeadsAppProps) {
  const [activeTenant,  setActiveTenant]  = useState<number>(1);
  const [channelFilter, setChannelFilter] = useState<string | null>(null);
  const [activeConv,    setActiveConv]    = useState<any>(null);
  const [conversations, setConversations] = useState<any[]>([]);
  const [messages,      setMessages]      = useState<Record<number, any[]>>({});
  const [inputValue,    setInputValue]    = useState<string>("");
  const msgsEndRef     = useRef<HTMLDivElement>(null);
  const activeConvRef  = useRef<any>(null);
  const stompClientRef = useRef<Client | null>(null);

  // Keep activeConv accessible inside WS callback without stale closure
  useEffect(() => { activeConvRef.current = activeConv; }, [activeConv]);

  // Scroll to bottom on new message
  useEffect(() => {
    msgsEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages, activeConv]);

  // Load conversations when tenant changes
  useEffect(() => {
    getConversations(activeTenant)
      .then((data: any[]) => {
        console.debug("[web] normalized conversations", { tenantId: activeTenant, count: data.length });
        setConversations(data.map(c => normalizeConv(c, activeTenant)));
      })
      .catch((error) => console.error("[web] failed to load conversations", error));
  }, [activeTenant]);

  // WebSocket: subscribe to real-time messages for active tenant
  // Note: In a production app, consider using a more robust state management and WebSocket handling approach (e.g., Redux + middleware, context provider, or a custom hook) to avoid potential issues with component re-renders and state updates.
  // For this demo, the direct useEffect with refs is a simpler solution to ensure the latest state is used in the WebSocket callback without needing to manage complex dependencies or risk stale closures.  
  // Also, remember to handle WebSocket connection errors and edge cases in a real application.
  //SockJS doesn't support native WebSocket features like automatic reconnection or heartbeats, so using @stomp/stompjs with SockJS provides a more robust solution for real-time updates in this demo.
  useEffect(() => {
    if (stompClientRef.current?.active) return; // StrictMode guard: skip second mount

    const client = new Client({
      webSocketFactory: () => new (SockJS as any)("/ws"),
      reconnectDelay: 5000,
      onConnect: () => {
        client.subscribe(`/topic/tenant/${activeTenant}/messages`, (frame) => {
          const dto = JSON.parse(frame.body);
          const convId: number = dto.conversationId;
          if (!convId) return;

          const normalized = { ...normalizeMsg(dto), isNew: true };

          setMessages(prev => {
            const existing = prev[convId] || [];
            if (existing.some(m => m.id === normalized.id)) return prev; // deduplicate: already added by handleSend
            return { ...prev, [convId]: [...existing, normalized] };
          });

          setConversations(prev =>
            prev
              .map(c =>
                c.id === convId
                  ? {
                      ...c,
                      preview: dto.contenido ?? "",
                      time: "ahora",
                      unread: activeConvRef.current?.id === convId ? 0 : c.unread + 1,
                    }
                  : c
              )
              .sort((a, b) => (a.id === convId ? -1 : b.id === convId ? 1 : 0))
          );
        });
      },
    });

    stompClientRef.current = client;
    client.activate();
    return () => {
      client.deactivate();
      stompClientRef.current = null;
    };
  }, [activeTenant]);

  const tenantConvs = conversations.filter((c: { tenantId: number; channel: any }) =>
    c.tenantId === activeTenant && (!channelFilter || c.channel === channelFilter)
  );

  const totalUnread = conversations
    .filter((c: { tenantId: number }) => c.tenantId === activeTenant)
    .reduce((s: number, c: { unread: number }) => s + c.unread, 0);

  const handleSelectConv = async (conv: any) => {
    setActiveConv(conv);
    setConversations(prev => prev.map(c => c.id === conv.id ? { ...c, unread: 0 } : c));
    try {
      const [msgs] = await Promise.all([getMessages(conv.id), markAsRead(conv.id)]);
      console.debug("[web] normalized messages", { conversationId: conv.id, count: msgs.length });
      setMessages(prev => ({ ...prev, [conv.id]: msgs.map(normalizeMsg) }));
    } catch (e) {
      console.error("[web] failed to load messages", e);
    }
  };

  const handleSend = async () => {
    if (!inputValue.trim() || !activeConv) return;
    const text = inputValue.trim();
    setInputValue("");
    try {
      const dto = await sendMessage(activeConv.id, text);
      const msg = { ...normalizeMsg(dto), isNew: false };
      setMessages(prev => {
        const existing = prev[activeConv.id] || [];
        if (existing.some(m => m.id === msg.id)) return prev; // deduplicate: WS may have arrived first
        return { ...prev, [activeConv.id]: [...existing, msg] };
      });
      setConversations(prev =>
        prev.map(c => c.id === activeConv.id ? { ...c, preview: text, time: "ahora" } : c)
      );
    } catch (e) {
      console.error("[web] failed to send message", e);
    }
  };

  const handleKey = (e: { key: string; shiftKey: any; preventDefault: () => void }) => {
    if (e.key === "Enter" && !e.shiftKey) { e.preventDefault(); handleSend(); }
  };

  return (
    <>
      <style>{css}</style>
      <div className="app">

        {/* ── Sidebar ── */}
        <aside className="sidebar">
          <div className="sidebar-brand">
            <h1>Leads<span>Hub</span></h1>
            <Link
              to="/channels"
              style={{
                marginTop: 12,
                width: "100%",
                border: "1px solid #5a5248",
                borderRadius: 8,
                background: "transparent",
                color: "#f0ece4",
                padding: "8px 10px",
                fontSize: 12,
                fontWeight: 600,
                cursor: "pointer",
                display: "inline-block",
                textDecoration: "none",
                textAlign: "center",
              }}
            >
              Channel connections
            </Link>
            <button
              type="button"
              onClick={onLogout}
              style={{
                marginTop: 12,
                width: "100%",
                border: "1px solid #5a5248",
                borderRadius: 8,
                background: "transparent",
                color: "#f0ece4",
                padding: "8px 10px",
                fontSize: 12,
                fontWeight: 600,
                cursor: "pointer",
              }}
            >
              Sign out
            </button>
          </div>
          <div className="sidebar-section">Cuentas</div>
          {TENANTS.map((t: any) => (
            <div
              key={t.id}
              className={`tenant-item ${activeTenant === t.id ? "active" : ""}`}
              onClick={() => { setActiveTenant(t.id); setActiveConv(null); setChannelFilter(null); }}
            >
              <div className="tenant-dot" />
              <span className="tenant-name">{t.nombre}</span>
            </div>
          ))}
          <div className="sidebar-stats">
            <div className="stat-card">
              <div className="n">{tenantConvs.length}</div>
              <div className="l">Chats</div>
            </div>
            <div className="stat-card">
              <div className="n" style={{ color: totalUnread > 0 ? "#C84B2F" : undefined }}>{totalUnread}</div>
              <div className="l">Sin leer</div>
            </div>
            <div className="stat-card" style={{ gridColumn: "span 2" }}>
              <div className="n" style={{ fontSize: 14, color: "#7DC97A" }}>● Conectado</div>
              <div className="l">WebSocket activo</div>
            </div>
          </div>
        </aside>

        {/* ── Inbox ── */}
        <section className="inbox">
          <div className="inbox-header">
            <div className="inbox-header-row">
              <h2>Bandeja</h2>
              {totalUnread > 0 && <span className="unread-total">{totalUnread} nuevos</span>}
            </div>
          </div>

          <div className="filters">
            {[null, "WHATSAPP", "INSTAGRAM", "FACEBOOK"].map((ch: string | null) => (
              <button
                key={ch || "all"}
                className={`filter-pill ${channelFilter === ch ? "active" : ""}`}
                style={channelFilter === ch ? { background: ch ? (CH_COLOR as any)[ch] : "#1C1814" } : {}}
                onClick={() => setChannelFilter(ch as string | null)}
              >
                {ch ? (CH_LABEL as any)[ch] : "Todos"}
              </button>
            ))}
          </div>

          <div className="conv-list">
            {tenantConvs.length === 0 && (
              <div style={{ padding: 24, textAlign: "center", color: "var(--sub)", fontSize: 13 }}>
                Sin conversaciones
              </div>
            )}
            {tenantConvs.map((conv: any) => (
              <div
                key={conv?.id}
                className={`conv-item ${activeConv?.id === conv?.id ? "active" : ""} ${conv?.unread > 0 ? "unread" : ""}`}
                onClick={() => handleSelectConv(conv)}
              >
                <div className="avatar" style={{ background: (CH_COLOR as any)[conv.channel] }}>
                  {conv.initials}
                </div>
                <div className="conv-body">
                  <div className="conv-row">
                    <span className={`conv-name ${conv.unread > 0 ? "bold" : ""}`}>{conv.leadNombre}</span>
                    <span className="conv-time">{conv.time}</span>
                  </div>
                  <div style={{ display: "flex", alignItems: "center", gap: 6 }}>
                    <span
                      className="ch-badge"
                      style={{
                        background: (CH_LIGHT as any)[conv.channel],
                        borderColor: (CH_COLOR as any)[conv.channel] + "55",
                        color: (CH_COLOR as any)[conv.channel],
                      }}
                    >
                      {(CH_LABEL as any)[conv.channel]}
                    </span>
                    {conv.unread > 0 && <span className="unread-badge">{conv.unread}</span>}
                  </div>
                  <div className={`conv-preview ${conv.unread > 0 ? "bold" : ""}`}>
                    {conv.preview}
                  </div>
                </div>
              </div>
            ))}
          </div>
        </section>

        {/* ── Chat ── */}
        <section className="chat">
          {!activeConv ? (
            <div className="chat-empty">
              <div className="chat-empty-icon">💬</div>
              <p>Selecciona una conversación</p>
            </div>
          ) : (
            <>
              <div className="chat-header" style={{ borderBottomColor: (CH_COLOR as any)[activeConv.channel] }}>
                <div className="avatar" style={{ background: (CH_COLOR as any)[activeConv.channel] }}>
                  {activeConv.initials}
                </div>
                <div className="chat-header-info">
                  <div className="chat-header-name">{activeConv.leadNombre}</div>
                  <div className="chat-header-sub">
                    vía {(CH_LABEL as any)[activeConv.channel]} · ID {activeConv.id}
                  </div>
                </div>
                <div className="live-dot" title="WebSocket conectado" />
              </div>

              <div className="msgs">
                {(messages[activeConv.id] || []).map((msg: any) => (
                  <div key={msg.id} className={`msg-row ${msg.out ? "out" : ""}`}>
                    <div className={`bubble ${msg.out ? "bubble-out" : "bubble-in"} ${msg.isNew ? "new-msg-flash" : ""}`}>
                      {msg.txt}
                      <div className="bubble-time">{msg.time}</div>
                    </div>
                  </div>
                ))}

                <div ref={msgsEndRef} />
              </div>

              <div className="input-area">
                <input
                  className="msg-input"
                  placeholder={`Responder a ${activeConv.leadNombre}...`}
                  value={inputValue}
                  onChange={e => setInputValue(e.target.value)}
                  onKeyDown={handleKey}
                />
                <button className="send-btn" onClick={handleSend}>
                  <svg viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                    <path d="M2.01 21L23 12 2.01 3 2 10l15 2-15 2z"/>
                  </svg>
                </button>
              </div>
            </>
          )}
        </section>

      </div>
    </>
  );
}
