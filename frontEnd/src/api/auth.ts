import axios from "axios";

export type UserRole = "ADMIN" | "AGENT";

export type UserSession = {
  id: number;
  email: string;
  role: UserRole;
  tenantId: number;
  tenantName: string;
};

export interface LoginResponse {
  user: UserSession;
}

export async function login(email: string, password: string): Promise<UserSession> {
  const response = await axios.post<LoginResponse>("/api/v1/auth/login", {
    email,
    password,
  });

  return response.data.user;
}
