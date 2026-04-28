import axios from 'axios';
import Constants from 'expo-constants';
import { getAccessToken } from '../auth/session';

// Derive the backend URL from the Metro bundler host so it works on
// both the Android emulator (10.0.2.2) and physical devices (LAN IP).
// The debuggerHost looks like "10.89.184.41:8081" or "10.0.2.2:8081".
const hostUri = Constants.expoConfig?.hostUri          // Expo SDK 49+
             ?? Constants.manifest2?.extra?.expoGo?.debuggerHost
             ?? Constants.manifest?.debuggerHost
             ?? 'localhost:8081';
const hostIp = hostUri.split(':')[0];
export const BASE_URL = `http://${hostIp}:8080`;

const api = axios.create({ baseURL: BASE_URL });

api.interceptors.request.use((config) => {
  const token = getAccessToken();
  if (token) {
    config.headers = config.headers ?? {};
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default api;
