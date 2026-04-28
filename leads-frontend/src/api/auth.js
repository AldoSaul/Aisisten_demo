import api from './client';

export const login = (email, password) =>
  api.post('/api/v1/auth/login', { email, password }).then(r => r.data);

