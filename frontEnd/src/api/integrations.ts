import api from "./client";

export type ProviderDescriptor = {
  provider: string;
  displayName: string;
  supportsConnect: boolean;
  supportsWebhooks: boolean;
  supportsAssetSync: boolean;
};

export type IntegrationConnectionSummary = {
  id: number;
  provider: string;
  status: "NOT_CONNECTED" | "PENDING" | "CONNECTED" | "ERROR" | "RECONNECT_REQUIRED" | "DISCONNECTED";
  authFlowStatus: "NOT_STARTED" | "STARTED" | "CALLBACK_RECEIVED" | "TOKEN_EXCHANGED" | "COMPLETED" | "FAILED";
};

export type IntegrationConnectionDetail = {
  id: number;
  publicId: string;
  tenantId: number;
  provider: string;
  status: string;
  authFlowStatus: string;
  externalConnectionId: string | null;
  lastError: string | null;
  connectedAt: string | null;
  disconnectedAt: string | null;
  lastSyncedAt: string | null;
  createdAt: string;
  updatedAt: string;
};

export type IntegrationAsset = {
  id: number;
  connectionId: number;
  accountId: number;
  provider: string;
  assetType: string;
  externalAssetId: string;
  displayName: string | null;
  active: boolean;
  lastSyncedAt: string | null;
};

export type IntegrationSyncResult = {
  provider: string;
  discoveredAccounts: number;
  discoveredAssets: number;
  message: string;
};

export type ConnectStartResponse = {
  connectionId: number;
  provider: string;
  state: string;
  redirectUrl: string | null;
  status: string;
  authFlowStatus: string;
};

export async function getProviders(): Promise<ProviderDescriptor[]> {
  const response = await api.get<ProviderDescriptor[]>("/api/v1/integrations/providers");
  return response.data;
}

export async function getIntegrations(tenantId: number): Promise<IntegrationConnectionSummary[]> {
  const response = await api.get<IntegrationConnectionSummary[]>("/api/v1/integrations", {
    params: { tenantId },
  });
  return response.data;
}

export async function getIntegration(id: number, tenantId: number): Promise<IntegrationConnectionDetail> {
  const response = await api.get<IntegrationConnectionDetail>(`/api/v1/integrations/${id}`, {
    params: { tenantId },
  });
  return response.data;
}

export async function startConnect(provider: string, tenantId: number): Promise<ConnectStartResponse> {
  const response = await api.post<ConnectStartResponse>(`/api/v1/integrations/${provider}/connect/start`, {
    tenantId,
  });
  return response.data;
}

export async function disconnectIntegration(id: number, tenantId: number): Promise<IntegrationConnectionDetail> {
  const response = await api.post<IntegrationConnectionDetail>(`/api/v1/integrations/${id}/disconnect`, {
    tenantId,
  });
  return response.data;
}

export async function reconnectIntegration(id: number, tenantId: number): Promise<IntegrationConnectionDetail> {
  const response = await api.post<IntegrationConnectionDetail>(`/api/v1/integrations/${id}/reconnect`, {
    tenantId,
  });
  return response.data;
}

export async function syncProviderAssets(provider: string, tenantId: number): Promise<IntegrationSyncResult> {
  const response = await api.post<IntegrationSyncResult>(`/api/v1/integrations/${provider}/assets/sync`, {
    tenantId,
  });
  return response.data;
}

export async function getIntegrationAssets(tenantId: number): Promise<IntegrationAsset[]> {
  const response = await api.get<IntegrationAsset[]>("/api/v1/integrations/assets", {
    params: { tenantId },
  });
  return response.data;
}

