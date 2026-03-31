import api from './client';

export const getConversations = (tenantId) =>
  api
    .get(`/api/v1/tenants/${tenantId}/conversations`, { params: { size: 100 } })
    .then(r => {
      const items = r.data.content ?? [];
      console.debug('[mobile] conversations response', {
        tenantId,
        isArray: Array.isArray(items),
        count: items.length,
      });
      return items;
    });

export const getMessages = (conversationId) =>
  api
    .get(`/api/v1/conversations/${conversationId}/messages`, { params: { size: 200 } })
    .then(r => {
      const items = r.data.content ?? [];
      console.debug('[mobile] messages response', {
        conversationId,
        isArray: Array.isArray(items),
        count: items.length,
      });
      return items;
    });

export const markAsRead = (conversationId) =>
  api.patch(`/api/v1/conversations/${conversationId}/read`).then(() => {});

export const sendMessage = (conversationId, contenido) =>
  api
    .post(`/api/v1/conversations/${conversationId}/messages`, { contenido })
    .then(r => r.data);
