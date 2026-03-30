# Aisisten Demo

This workspace contains four separate applications:

- `frontEnd`: React + TypeScript + Vite web app — now wired to the real backend
- `leads-frontend`: Expo / React Native mobile client — now wired to the real backend
- `leads-backend`: Spring Boot backend for leads, WebSocket, and MySQL
- `meta-integration`: Spring Boot sandbox for Meta Graph API (ignore during normal dev)

## Recommended Tooling

- Node.js 20 or newer
- npm 10 or newer
- Java 21 for `leads-backend`
- Java 25 for `meta-integration` as currently configured in its `pom.xml`
- Maven 3.9+
- Android Studio and an emulator for the Expo mobile app when testing Android locally
- MySQL 8 running locally on port 3306

## Root Repo Notes

- Run install and start commands inside the correct subfolder.
- Generated install artifacts are ignored through the root `.gitignore`.
- Commit lockfiles if they already belong to a subproject, but do not commit `node_modules`, `.expo`, or Maven `target` output.

---

## Quick Start (all three apps)

Start them in this order:

**1. Backend**
```bash
cd leads-backend
mvn spring-boot:run
```

**2. Web app**
```bash
cd frontEnd
npm install
npm run dev
```

**3. Mobile app**
```bash
cd leads-frontend
npm install
npx expo start --clear
```

---

## `leads-backend` — Spring Boot Leads Service

Path: `leads-backend`

### Requirements

- Java 21
- MySQL running locally on port 3306
  - Database: `leads_db`
  - Username: `admin` / Password: `admin`
  - Change in `src/main/resources/application.properties` if needed
- Kafka is disabled by default; no Kafka installation required

### Install / Build

```bash
cd leads-backend
mvn clean install
```

### Run

```bash
mvn spring-boot:run
```

### Notes

- Default port: `8080`
- Kafka auto-configuration is excluded via `spring.autoconfigure.exclude` — do not remove that property
- Jackson is configured to serialize dates as ISO-8601 strings (`spring.jackson.serialization.write-dates-as-timestamps=false`)
- Security: `/api/v1/**`, `/webhook/**`, `/auth/**`, and `/ws/**` are all public while JWT is not yet implemented (phase 5)
- CORS: allowed origins are `http://localhost:5173` (Vite), `http://localhost:8081` (Metro), and `http://10.0.2.2:8080` (Android emulator)

### REST API Reference

| Method | Path | Description |
|--------|------|-------------|
| `GET`   | `/api/v1/tenants/{tenantId}/conversations` | List conversations (`?channel=`, `?page=`, `?size=`) |
| `GET`   | `/api/v1/conversations/{id}/messages` | List messages (`?page=`, `?size=`) |
| `PATCH` | `/api/v1/conversations/{id}/read` | Mark conversation as read |
| `POST`  | `/api/v1/conversations/{id}/messages` | Send outgoing message `{"contenido":"..."}` |
| `GET`   | `/api/v1/tenants/{tenantId}/unread-count` | Returns `{"total": N}` |

### WebSocket

- Endpoint: `ws://localhost:8080/ws` (STOMP over SockJS, web) or `ws://localhost:8080/ws/websocket` (raw STOMP, React Native)
- Subscribe: `/topic/tenant/{tenantId}/messages`
- Payload: `MessageDTO` JSON — `{id, conversationId, contenido, esEntrante, channel, status, sentAt, createdAt}`

---

## `frontEnd` — Web App

Path: `frontEnd`

### Install

```bash
cd frontEnd
npm install
```

### Run

```bash
npm run dev
```
Opens at `http://localhost:5173`

### Other Commands

```bash
npm run build
npm run lint
npm run preview
```

### Notes

- Vite dev server proxies `/api/**` and `/ws/**` to `http://localhost:8080` — backend must be running
- API client: `src/api/client.ts` (axios, baseURL `""` — relies on Vite proxy)
- WebSocket: STOMP over SockJS via `@stomp/stompjs` + `sockjs-client`, connected in `LeadsApp.tsx`
- Data normalizers: `src/helper/data.tsx` (`normalizeConv`, `normalizeMsg`)
- All mock data removed; `TENANTS` list is still static (no `/tenants` API exists yet)
- Login is still fake (no credentials required); JWT wiring is phase 5

---

## `leads-frontend` — Expo Mobile App

Path: `leads-frontend`

### Install

```bash
cd leads-frontend
npm install
```

### Run

```bash
npx expo start
```

### Run With Cleared Cache

```bash
npx expo start --clear
```

### Native Targets

```bash
npm run android
npm run ios
```

### Backend URL Configuration

The mobile app connects to `http://10.0.2.2:8080` by default, which routes to the host machine from the Android emulator. Change `BASE_URL` in `src/api/client.js` for other targets:

| Target | BASE_URL |
|--------|----------|
| Android emulator | `http://10.0.2.2:8080` |
| iOS simulator | `http://localhost:8080` |
| Physical device | `http://<your-LAN-IP>:8080` |

### Notes

- API client: `src/api/client.js` + `src/api/conversations.js`
- WebSocket: raw STOMP (no SockJS) via `@stomp/stompjs` — see `src/ws/client.js`
- State management: `src/context/AppContext.js` (integration point for all API + WS calls)
- Data normalizers: `src/constants/data.js` (`normalizeConv`, `normalizeMsg`)
- All mock data removed; `TENANTS` list is still static
- Login is still fake; JWT wiring is phase 5

### Important Setup Notes

- Always run Expo commands from `leads-frontend`
- The entry point is `index.js` — do not change `"main"` in `package.json`
- `react-native-url-polyfill` is required; keep `import 'react-native-url-polyfill/auto'` as the first line of `App.js`
- Do not add `sockjs-client` — it causes runtime failures on React Native; raw WebSocket is used instead
- If Metro behaves strangely after dependency changes: `npx expo start --clear`

---

## Manual Testing — Backend Integration

Run these steps after starting the backend with `mvn spring-boot:run`. Replace `tenantId` and `conversationId` with real IDs from your database.

### 1. Verify the backend is up

```powershell
curl -s http://localhost:8080/actuator/health
```
Expected: `{"status":"UP"}`

### 2. List conversations

```powershell
curl -s http://localhost:8080/api/v1/tenants/1/conversations | ConvertFrom-Json | ConvertTo-Json -Depth 5
```
Expected: `200 OK`, `content` array of conversation objects.
Check that `lastMessageAt` is an ISO string, not an array — if it's an array, the Jackson property is missing from `application.properties`.

### 3. Get messages for a conversation

```powershell
curl -s http://localhost:8080/api/v1/conversations/1/messages | ConvertFrom-Json | ConvertTo-Json -Depth 5
```
Expected: `200 OK`, `content` array. `sentAt` and `createdAt` must be ISO strings.

### 4. Send a message (the new endpoint)

```powershell
curl -s -X POST http://localhost:8080/api/v1/conversations/1/messages `
  -H "Content-Type: application/json" `
  -d '{"contenido": "Hola, en que te puedo ayudar?"}' | ConvertFrom-Json | ConvertTo-Json -Depth 5
```
Expected: `201 Created`. Verify in the response body:
- `esEntrante` is `false`
- `channel` matches the conversation's channel
- `conversationId` is present

### 5. Mark as read

```powershell
curl -s -X PATCH http://localhost:8080/api/v1/conversations/1/read -w " HTTP %{http_code}"
```
Expected: ` HTTP 200`

### 6. Unread count

```powershell
curl -s http://localhost:8080/api/v1/tenants/1/unread-count
```
Expected: `{"total": N}`

### 7. Web app end-to-end

1. Start backend → `mvn spring-boot:run`
2. Start web app → `npm run dev` from `frontEnd`
3. Open `http://localhost:5173`
4. Select a tenant in the sidebar — conversation list should load from the backend
5. Click a conversation — messages should load and unread badge should clear
6. Type a message and press Enter — message should appear with right-aligned bubble and `esEntrante: false` in DB
7. Open two browser tabs on the same URL; send from one and confirm the other receives it via WebSocket without a page refresh

### 8. Mobile app end-to-end (Android emulator)

1. Start backend on host machine
2. Confirm `BASE_URL = 'http://10.0.2.2:8080'` in `leads-frontend/src/api/client.js`
3. Run `npx expo start --clear` from `leads-frontend`
4. Press `a` to open on Android emulator
5. Tap a tenant tab — conversation list should load
6. Tap a conversation — messages should load
7. Type and send a message — confirm it appears in the chat

---

## `meta-integration` — Meta Graph API Sandbox

Path: `meta-integration`

### Requirements

- Java 25 as currently defined in `pom.xml`

### Notes

- Default port is `8080` — do not run at the same time as `leads-backend`
- Update credentials in `src/main/resources/application.properties` before use
- This module is a standalone sandbox; it is not part of the main integration flow

### Test URLs

```text
http://localhost:8080/auth/meta/login
http://localhost:8080/auth/meta/test
http://localhost:8080/auth/meta/debug
http://localhost:8080/meta/ad-account?adAccountId=123456789012345
```

---

## Known Problems We Already Hit

### Expo started from the wrong folder

Symptom:
```text
ConfigError: The expected package.json path: D:\PersonalDev\Aisisten_demo\package.json does not exist
```
Fix: `cd leads-frontend` then run `npx expo start`

### Expo URL / Hermes runtime crash

Symptom:
```text
TypeError: Cannot assign to property 'protocol' which has only a getter
```
Fix: keep `react-native-url-polyfill` installed and `import 'react-native-url-polyfill/auto'` as the first line of `App.js`

### Expo root component not registered

Symptom:
```text
Invariant Violation: "main" has not been registered
```
Fix: keep `leads-frontend/index.js` and `"main": "index.js"` in `package.json`

### Web app gets no data (empty inbox)

Symptom: inbox loads but shows "Sin conversaciones"
Fix: confirm `leads-backend` is running on port 8080 and the Vite proxy is active (`vite.config.ts` must have `/api` and `/ws` proxy entries)

### Mobile app cannot reach backend

Symptom: conversations never load on emulator
Fix: confirm `BASE_URL = 'http://10.0.2.2:8080'` in `src/api/client.js`. On a physical device use your LAN IP instead.

## Recommended Tooling

- Node.js 20 or newer
- npm 10 or newer
- Java 21 for `leads-backend`
- Java 25 for `meta-integration` as currently configured in its `pom.xml`
- Maven 3.9+
- Android Studio and an emulator for the Expo mobile app when testing Android locally

## Root Repo Notes

- Run install and start commands inside the correct subfolder. Running Expo from the workspace root fails because there is no root `package.json`.
- Generated install artifacts are ignored through the root `.gitignore`.
- Commit lockfiles if they already belong to a subproject, but do not commit `node_modules`, `.expo`, or Maven `target` output.

## `frontEnd` — Web Mock

Path: `frontEnd`

### Install

```bash
cd frontEnd
npm install
```

### Run

```bash
npm run dev
```

### Other Commands

```bash
npm run build
npm run lint
npm run preview
```

### Notes

- This is the localhost web mock currently used as the visual reference for the mobile app.
- It uses Vite, React 19, and `react-router-dom`.
- Some dependencies such as `@react-navigation/*`, `expo-status-bar`, `react-native-safe-area-context`, and `react-native-screens` are present but are not part of a normal web stack. They can stay if intentionally shared for experimentation, but they are not required for the browser app.

## `leads-frontend` — Expo Mobile App

Path: `leads-frontend`

### Install

```bash
cd leads-frontend
npm install
```

### Run

```bash
npx expo start
```

### Run With Cleared Cache

```bash
npx expo start --clear
```

### Native Targets

```bash
npm run android
npm run ios
```

### Important Setup Notes

- Always run Expo commands from `leads-frontend`. Running `npx expo start` from the workspace root fails because Expo looks for `D:\PersonalDev\Aisisten_demo\package.json`.
- The mobile app now uses `index.js` as the entry point so Expo can register the root component correctly.
- `react-native-url-polyfill` is required. Without it, Expo's asset URL handling can crash Hermes with: `Cannot assign to property 'protocol' which has only a getter`.
- `expo-status-bar` must stay on a version compatible with Expo 54. The working value in this repo is `~3.0.9`.
- Do not add `sockjs-client` back unless you verify React Native compatibility. It was removed because it contributed to runtime startup failures.

### Mobile Dependency Notes

- `react-native-gesture-handler`, `react-native-safe-area-context`, and `react-native-screens` are required by the navigation stack.
- If Metro behaves strangely after dependency changes, restart from `leads-frontend` with `npx expo start --clear`.

## `leads-backend` — Spring Boot Leads Service

Path: `leads-backend`

### Requirements

- Java 21
- MySQL running locally
- Optional Kafka if you enable it later; current config disables Kafka auto-configuration

### Install / Build

```bash
cd leads-backend
mvn clean install
```

### Run

```bash
mvn spring-boot:run
```

### Notes

- Default port is `8080`.
- The app expects a MySQL database named `leads_db` with username `admin` and password `admin` unless you change `src/main/resources/application.properties`.
- `spring.kafka.enabled=false` and `KafkaAutoConfiguration` is excluded, so Kafka is not required for a basic local start.
- Review `src/main/resources/schema.sql` if you need to initialize the database manually.

## `meta-integration` — Meta Graph API Test App

Path: `meta-integration`

### Requirements

- Java 25 as currently defined in `pom.xml`

### Install / Build

```bash
cd meta-integration
mvn clean install
```

### Run

```bash
mvn spring-boot:run
```

### Notes

- Default port is `8080`, so do not run it at the same time as `leads-backend` without changing one port.
- Before using Meta login flows, update the app credentials in `src/main/resources/application.properties`.
- Recommended test URLs from the current configuration:

```text
http://localhost:8080/auth/meta/login
http://localhost:8080/auth/meta/test
http://localhost:8080/auth/meta/debug
http://localhost:8080/meta/ad-account?adAccountId=123456789012345
```

## Known Problems We Already Hit

### Expo started from the wrong folder

Symptom:

```text
ConfigError: The expected package.json path: D:\PersonalDev\Aisisten_demo\package.json does not exist
```

Fix:

- `cd leads-frontend`
- then run `npx expo start` or `npx expo start --clear`

### Expo URL / Hermes runtime crash

Symptom:

```text
TypeError: Cannot assign to property 'protocol' which has only a getter
```

Fix:

- keep `react-native-url-polyfill` installed
- keep `import 'react-native-url-polyfill/auto';` as the first line in `leads-frontend/App.js`

### Expo root component not registered

Symptom:

```text
Invariant Violation: "main" has not been registered
```

Fix:

- keep `leads-frontend/index.js`
- keep `"main": "index.js"` in `leads-frontend/package.json`
