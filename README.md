# Aisisten Demo

This workspace contains four separate applications:

- `frontEnd`: React + TypeScript + Vite web mock used on localhost
- `leads-frontend`: Expo / React Native mobile client
- `leads-backend`: Spring Boot backend for leads, WebSocket, Kafka, and MySQL
- `meta-integration`: Spring Boot test app for Meta Graph API integration

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
