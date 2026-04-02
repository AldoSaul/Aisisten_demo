import type { UserSession } from "../api/auth";

const STORAGE_KEY = "leads_user_session";

export function saveSession(user: UserSession): void {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(user));
}

export function loadSession(): UserSession | null {
  const raw = localStorage.getItem(STORAGE_KEY);
  if (!raw) return null;

  try {
    return JSON.parse(raw) as UserSession;
  } catch {
    localStorage.removeItem(STORAGE_KEY);
    return null;
  }
}

export function clearSession(): void {
  localStorage.removeItem(STORAGE_KEY);
}
