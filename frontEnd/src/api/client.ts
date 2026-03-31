import axios from "axios";

// Create an Axios instance with a base URL. The base URL is empty because Vite's proxy will handle it if not changed to http://localhost:8080  
// In development, Vite's proxy will forward API requests to the backend server at http://localhost:8080, so we can keep the base URL empty here. In production, you would set this to your actual backend URL. 
// Note: If you change the base URL here, make sure to also update the WebSocket URL in LeadsApp.tsx to match, since both need to point to the same backend server for API and WebSocket communication to work correctly.   
const api = axios.create({ baseURL: "" }); // Base URL is empty because Vite's proxy will handle it if not change to http://localhost:8080

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
