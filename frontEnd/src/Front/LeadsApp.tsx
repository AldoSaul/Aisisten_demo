import { useState, useEffect, useRef } from "react";
import {css}  from "../CSS/leadCss.tsx";
import { TENANTS, ALL_CONVS, ALL_MSGS, FAKE_INCOMING, CH_COLOR, CH_LABEL, CH_LIGHT } from "../helper/data.tsx";


export default function LeadsApp() {
  const [activeTenant,    setActiveTenant]    = useState<number>(1);
  const [channelFilter,   setChannelFilter]   = useState<string | null>(null);
  const [activeConv,      setActiveConv]      = useState<any>(null);
  const [conversations,   setConversations]   = useState<any[]>(ALL_CONVS);
  const [messages,        setMessages]        = useState<Record<number, any[]>>({});
  const [inputValue,      setInputValue]      = useState<string>("");
  const [showTyping,      setShowTyping]      = useState<boolean | number>(false);
  const msgsEndRef = useRef(null);
  const fakeIdx    = useRef(0);

  // Init messages
  useEffect(() => { setMessages({ ...ALL_MSGS }); }, []);

  // Scroll to bottom on new message
  useEffect(() => {
    (msgsEndRef.current as unknown as HTMLDivElement)?.scrollIntoView({ behavior: "smooth" });
  }, [messages, activeConv, showTyping]);

  // Simulate incoming messages every 12s
  useEffect(() => {
    const t = setInterval(() => {
      const item = FAKE_INCOMING[fakeIdx.current % FAKE_INCOMING.length];
      fakeIdx.current++;
      // Only simulate if tenant matches
      const conv = ALL_CONVS.find((c: { id: any; }) => c.id === item.convId);
      if (!conv || conv.tenantId !== activeTenant) return;

      setShowTyping(item.convId);
      setTimeout(() => {
        setShowTyping(false);
        const newMsg = { id: Date.now(), txt: item.txt, out: false, time: new Date().toLocaleTimeString("es-MX", { hour:"2-digit", minute:"2-digit" }), isNew: true };
        setMessages(prev => ({ ...prev, [item.convId]: [...(prev[item.convId] || []), newMsg] }));
        setConversations((prev: any[]) => prev.map((c: { id: any; unread: number; }) =>
          c.id === item.convId
            ? { ...c, preview: item.txt, time: "ahora", unread: c.unread + (activeConv?.id === item.convId ? 0 : 1) }
            : c
        ).sort((a: { id: any; }, b: { id: any; }) => {
          if (a.id === item.convId) return -1;
          if (b.id === item.convId) return 1;
          return 0;
        }));
      }, 2200);
    }, 12000);
    return () => clearInterval(t);
  }, [activeTenant, activeConv]);

  const tenantConvs = conversations.filter((c: { tenantId: number; channel: any; }) =>
    c.tenantId === activeTenant && (!channelFilter || c.channel === channelFilter)
  );

  const totalUnread = conversations.filter((c: { tenantId: number; }) => c.tenantId === activeTenant).reduce((s: any, c: { unread: any; }) => s + c.unread, 0);

  const handleSelectConv = (conv: any) => {
    setActiveConv(conv);
    setConversations((prev: any[]) => prev.map((c: { id: any; }) => c.id === conv.id ? { ...c, unread: 0 } : c));
  };

  const handleSend = () => {
    if (!inputValue.trim() || !activeConv) return;
    const msg = { id: Date.now(), txt: inputValue.trim(), out: true, time: new Date().toLocaleTimeString("es-MX", { hour:"2-digit", minute:"2-digit" }) };
    setMessages(prev => ({ ...prev, [activeConv.id]: [...(prev[activeConv.id] || []), msg] }));
    setConversations((prev: any[]) => prev.map((c: { id: any; }) => c.id === activeConv.id ? { ...c, preview: inputValue.trim(), time: "ahora" } : c));
    setInputValue("");
  };

  const handleKey = (e: { key: string; shiftKey: any; preventDefault: () => void; }) => { if (e.key === "Enter" && !e.shiftKey) { e.preventDefault(); handleSend(); } };

  return (
    <>
      <style>{css}</style>
      <div className="app">

        {/* ── Sidebar ── */}
        <aside className="sidebar">
          <div className="sidebar-brand">
            <h1>Leads<span>Hub</span></h1>
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
                    {showTyping === conv.id ? "Escribiendo..." : conv.preview}
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

                {showTyping === activeConv.id && (
                  <div className="msg-row">
                    <div className="typing-bubble">
                      <div className="typing">
                        <span /><span /><span />
                      </div>
                    </div>
                  </div>
                )}
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