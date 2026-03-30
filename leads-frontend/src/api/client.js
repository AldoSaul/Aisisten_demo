import axios from 'axios';

// Android emulator → host machine: 10.0.2.2
// iOS simulator   → host machine: localhost
// Physical device → replace with your machine's LAN IP (e.g. 192.168.x.x)
export const BASE_URL = 'http://10.0.2.2:8080';

const api = axios.create({ baseURL: BASE_URL });

export default api;
