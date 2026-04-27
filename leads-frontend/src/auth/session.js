let currentSession = null;

export function saveSession(session) {
  currentSession = session;
}

export function clearSession() {
  currentSession = null;
}

export function getAccessToken() {
  return currentSession?.token ?? null;
}

export function getCurrentUser() {
  return currentSession?.user ?? null;
}

