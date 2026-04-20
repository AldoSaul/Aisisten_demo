import axios, { type InternalAxiosRequestConfig } from "axios";

import { getAccessToken } from "../auth/session";

const api = axios.create({ baseURL: "" });

api.interceptors.request.use((config: InternalAxiosRequestConfig) => {
  const token = getAccessToken();
  if (token) {
    config.headers.set("Authorization", `Bearer ${token}`);
  }
  return config;
});

export const getConversations = (tenantId: number): Promise<any[]> =>
  api
    .get(`/api/v1/tenants/${tenantId}/conversations`, { params: { size: 100 } })
    .then(r => {
      const items = r.data.content ?? [];
      console.debug("[web] conversations response", {
        tenantId,
        isArray: Array.isArray(items),
        count: items.length,
      });
      return items;
    });

export const getMessages = (conversationId: number): Promise<any[]> =>
  api
    .get(`/api/v1/conversations/${conversationId}/messages`, { params: { size: 200 } })
    .then(r => {
      const items = r.data.content ?? [];
      console.debug("[web] messages response", {
        conversationId,
        isArray: Array.isArray(items),
        count: items.length,
      });
      return items;
    });

export const markAsRead = (conversationId: number): Promise<void> =>
  api.patch(`/api/v1/conversations/${conversationId}/read`).then(() => {});

export const sendMessage = (conversationId: number, contenido: string): Promise<any> =>
  api
    .post(`/api/v1/conversations/${conversationId}/messages`, { contenido })
    .then(r => r.data);

export default api;
