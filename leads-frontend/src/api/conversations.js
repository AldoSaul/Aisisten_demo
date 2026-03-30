import api from './client';

export const getConversations = (tenantId) =>
  api
    .get(`/api/v1/tenants/${tenantId}/conversations`, { params: { size: 100 } })
    .then(r => r.data.content ?? []);

export const getMessages = (conversationId) =>
  api
    .get(`/api/v1/conversations/${conversationId}/messages`, { params: { size: 200 } })
    .then(r => r.data.content ?? []);

export const markAsRead = (conversationId) =>
  api.patch(`/api/v1/conversations/${conversationId}/read`).then(() => {});

export const sendMessage = (conversationId, contenido) =>
  api
    .post(`/api/v1/conversations/${conversationId}/messages`, { contenido })
    .then(r => r.data);
