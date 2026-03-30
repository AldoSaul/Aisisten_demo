import { Client } from '@stomp/stompjs';
import { BASE_URL } from '../api/client';

// React Native has native WebSocket — no SockJS needed.
// Spring SockJS endpoint also accepts raw WebSocket at /ws/websocket.
const WS_URL = BASE_URL.replace(/^http/, 'ws') + '/ws/websocket';

/**
 * Creates and returns an active STOMP client subscribed to a tenant's message topic.
 * Call client.deactivate() in the useEffect cleanup.
 *
 * @param {number} tenantId
 * @param {(dto: object) => void} onMessage  Called with parsed MessageDTO on each push
 */
export function createStompClient(tenantId, onMessage) {
  const client = new Client({
    brokerURL: WS_URL,
    reconnectDelay: 5000,
    onConnect: () => {
      client.subscribe(`/topic/tenant/${tenantId}/messages`, (frame) => {
        try {
          onMessage(JSON.parse(frame.body));
        } catch (e) {
          console.error('[WS] parse error', e);
        }
      });
    },
    onStompError: (frame) => {
      console.error('[WS] STOMP error', frame.headers?.message);
    },
  });
  return client;
}
