import React, {
  createContext,
  useContext,
  useState,
  useEffect,
  useRef,
} from 'react';
import { ALL_CONVS, ALL_MSGS, FAKE_INCOMING } from '../constants/data';

const AppContext = createContext(null);

export function AppProvider({ children }) {
  const [activeTenant,  setActiveTenant]  = useState(1);
  const [channelFilter, setChannelFilter] = useState(null);
  const [conversations, setConversations] = useState(ALL_CONVS);
  const [messages,      setMessages]      = useState({ ...ALL_MSGS });
  const [showTyping,    setShowTyping]    = useState(false);
  const [activeConvId,  setActiveConvId]  = useState(null);

  const fakeIdx        = useRef(0);
  const activeTenantRef = useRef(activeTenant);
  const activeConvIdRef = useRef(activeConvId);

  // Keep refs in sync so the interval closure always sees the latest values
  useEffect(() => { activeTenantRef.current = activeTenant; }, [activeTenant]);
  useEffect(() => { activeConvIdRef.current = activeConvId; }, [activeConvId]);

  // Simulate incoming messages every 12 s
  useEffect(() => {
    const timer = setInterval(() => {
      const item = FAKE_INCOMING[fakeIdx.current % FAKE_INCOMING.length];
      fakeIdx.current += 1;

      const conv = ALL_CONVS.find(c => c.id === item.convId);
      if (!conv || conv.tenantId !== activeTenantRef.current) return;

      setShowTyping(item.convId);

      setTimeout(() => {
        setShowTyping(false);

        const newMsg = {
          id:    Date.now(),
          txt:   item.txt,
          out:   false,
          time:  new Date().toLocaleTimeString('es-MX', { hour: '2-digit', minute: '2-digit' }),
          isNew: true,
        };

        setMessages(prev => ({
          ...prev,
          [item.convId]: [...(prev[item.convId] || []), newMsg],
        }));

        setConversations(prev =>
          prev
            .map(c =>
              c.id === item.convId
                ? {
                    ...c,
                    preview: item.txt,
                    time:    'ahora',
                    unread:  c.unread + (activeConvIdRef.current === item.convId ? 0 : 1),
                  }
                : c
            )
            .sort((a, b) => {
              if (a.id === item.convId) return -1;
              if (b.id === item.convId) return  1;
              return 0;
            })
        );
      }, 2200);
    }, 12000);

    return () => clearInterval(timer);
  }, []); // intentionally empty — we use refs for dynamic values

  // ── Actions ───────────────────────────────────────────────────────────────

  const selectTenant = (id) => {
    setActiveTenant(id);
    setActiveConvId(null);
    setChannelFilter(null);
  };

  const selectConv = (convId) => {
    setActiveConvId(convId);
    setConversations(prev =>
      prev.map(c => (c.id === convId ? { ...c, unread: 0 } : c))
    );
  };

  const sendMessage = (convId, text) => {
    const msg = {
      id:   Date.now(),
      txt:  text,
      out:  true,
      time: new Date().toLocaleTimeString('es-MX', { hour: '2-digit', minute: '2-digit' }),
    };
    setMessages(prev => ({
      ...prev,
      [convId]: [...(prev[convId] || []), msg],
    }));
    setConversations(prev =>
      prev.map(c => (c.id === convId ? { ...c, preview: text, time: 'ahora' } : c))
    );
  };

  // ── Derived state ─────────────────────────────────────────────────────────

  const tenantConvs = conversations.filter(
    c => c.tenantId === activeTenant && (!channelFilter || c.channel === channelFilter)
  );

  const totalUnread = conversations
    .filter(c => c.tenantId === activeTenant)
    .reduce((sum, c) => sum + c.unread, 0);

  return (
    <AppContext.Provider
      value={{
        activeTenant,  selectTenant,
        channelFilter, setChannelFilter,
        conversations, tenantConvs,
        messages,      showTyping,
        totalUnread,
        activeConvId,  selectConv,
        sendMessage,
      }}
    >
      {children}
    </AppContext.Provider>
  );
}

export const useApp = () => useContext(AppContext);
