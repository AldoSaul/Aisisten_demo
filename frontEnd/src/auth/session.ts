import type { AuthSession } from "../api/auth";

const STORAGE_KEY = "leads_auth_session";

export function saveSession(session: AuthSession): void {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(session));
}

export function loadSession(): AuthSession | null {
  const raw = localStorage.getItem(STORAGE_KEY);
  if (!raw) return null;

  try {
    const parsed = JSON.parse(raw) as Partial<AuthSession>;
    if (!parsed || typeof parsed !== "object") {
      throw new Error("Invalid session payload");
    }

    if (typeof parsed.token !== "string" || !parsed.token) {
      throw new Error("Missing token");
    }

    if (!parsed.user || typeof parsed.user !== "object") {
      throw new Error("Missing user");
    }

    return {
      token: parsed.token,
      tokenType: typeof parsed.tokenType === "string" && parsed.tokenType ? parsed.tokenType : "Bearer",
      user: parsed.user,
    };
  } catch {
    localStorage.removeItem(STORAGE_KEY);
    return null;
  }
}

export function getAccessToken(): string | null {
  const session = loadSession();
  return session?.token ?? null;
}

export function clearSession(): void {
  localStorage.removeItem(STORAGE_KEY);
}
