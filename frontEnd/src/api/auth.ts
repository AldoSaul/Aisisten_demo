import api from "./client";

export type UserRole = "ADMIN" | "AGENT";

export type UserSession = {
  id: number;
  email: string;
  role: UserRole;
  tenantId: number;
  tenantName: string;
};

export interface LoginResponse {
  accessToken: string;
  tokenType: string;
  user: UserSession;
}

export type AuthSession = {
  token: string;
  tokenType: string;
  user: UserSession;
};

export async function login(email: string, password: string): Promise<LoginResponse> {
  const response = await api.post<LoginResponse>("/api/v1/auth/login", {
    email,
    password,
  });

  return response.data;
}

export async function getMe(): Promise<UserSession> {
  const response = await api.get<UserSession>("/api/v1/auth/me");
  return response.data;
}
