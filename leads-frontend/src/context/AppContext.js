import React, {
  createContext,
  useContext,
  useState,
  useEffect,
  useRef,
} from 'react';
import { normalizeConv, normalizeMsg } from '../constants/data';
import { getConversations, getMessages, markAsRead, sendMessage as apiSend } from '../api/conversations';
import { createStompClient } from '../ws/client';

const AppContext = createContext(null);

export function AppProvider({ children }) {
  const [activeTenant,  setActiveTenant]  = useState(1);
  const [channelFilter, setChannelFilter] = useState(null);
  const [conversations, setConversations] = useState([]);
  const [messages,      setMessages]      = useState({});
  const [showTyping,    setShowTyping]    = useState(false); // kept for ChatScreen compat
  const [activeConvId,  setActiveConvId]  = useState(null);

  const activeConvIdRef = useRef(activeConvId);
  useEffect(() => { activeConvIdRef.current = activeConvId; }, [activeConvId]);

  // Load conversations when tenant changes
  useEffect(() => {
    getConversations(activeTenant)
      .then(data => {
        console.debug('[mobile] normalized conversations', { tenantId: activeTenant, count: data.length });
        setConversations(data.map(c => normalizeConv(c, activeTenant)));
      })
      .catch(error => console.error('[mobile] failed to load conversations', error));
  }, [activeTenant]);

  // WebSocket: reconnect when tenant changes
  useEffect(() => {
    const client = createStompClient(activeTenant, (dto) => {
      const convId = dto.conversationId;
      if (!convId) return;

      const normalized = { ...normalizeMsg(dto), isNew: true };

      setMessages(prev => ({
        ...prev,
        [convId]: [...(prev[convId] || []), normalized],
      }));

      setConversations(prev =>
        prev
          .map(c =>
            c.id === convId
              ? {
                  ...c,
                  preview: dto.contenido ?? '',
                  time:    'ahora',
                  unread:  activeConvIdRef.current === convId ? 0 : c.unread + 1,
                }
              : c
          )
          .sort((a, b) => (a.id === convId ? -1 : b.id === convId ? 1 : 0))
      );
    });

    client.activate();
    return () => { client.deactivate(); };
  }, [activeTenant]);

  // ── Actions ───────────────────────────────────────────────────────────────

  const selectTenant = (id) => {
    setActiveTenant(id);
    setActiveConvId(null);
    setChannelFilter(null);
  };

  const selectConv = async (convId) => {
    setActiveConvId(convId);
    setConversations(prev =>
      prev.map(c => (c.id === convId ? { ...c, unread: 0 } : c))
    );
    try {
      const [msgs] = await Promise.all([getMessages(convId), markAsRead(convId)]);
      console.debug('[mobile] normalized messages', { conversationId: convId, count: msgs.length });
      setMessages(prev => ({ ...prev, [convId]: msgs.map(normalizeMsg) }));
    } catch (e) {
      console.error('[mobile] failed to load messages', e);
    }
  };

  const sendMessage = async (convId, text) => {
    try {
      const dto = await apiSend(convId, text);
      const msg = normalizeMsg(dto);
      setMessages(prev => ({
        ...prev,
        [convId]: [...(prev[convId] || []), msg],
      }));
      setConversations(prev =>
        prev.map(c => (c.id === convId ? { ...c, preview: text, time: 'ahora' } : c))
      );
    } catch (e) {
      console.error('[mobile] failed to send message', e);
    }
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
