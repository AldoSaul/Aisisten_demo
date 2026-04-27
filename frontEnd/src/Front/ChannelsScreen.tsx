import { useEffect, useMemo, useState, type CSSProperties } from "react";
import { Link } from "react-router-dom";

import type { UserSession } from "../api/auth";
import {
  disconnectIntegration,
  getIntegrations,
  getProviders,
  reconnectIntegration,
  startConnect,
  syncProviderAssets,
  type IntegrationConnectionSummary,
  type ProviderDescriptor,
} from "../api/integrations";

type ChannelsScreenProps = {
  user: UserSession;
  onLogout: () => void;
};

function statusLabel(status: string): string {
  switch (status) {
    case "CONNECTED":
      return "Connected";
    case "ERROR":
      return "Error";
    case "RECONNECT_REQUIRED":
      return "Reconnect required";
    case "PENDING":
      return "Connecting";
    default:
      return "Not connected";
  }
}

function statusColor(status: string): string {
  switch (status) {
    case "CONNECTED":
      return "#12805c";
    case "ERROR":
      return "#b42318";
    case "RECONNECT_REQUIRED":
      return "#b54708";
    case "PENDING":
      return "#6941c6";
    default:
      return "#475467";
  }
}

export default function ChannelsScreen({ user, onLogout }: ChannelsScreenProps) {
  const [providers, setProviders] = useState<ProviderDescriptor[]>([]);
  const [connections, setConnections] = useState<IntegrationConnectionSummary[]>([]);
  const [busyProvider, setBusyProvider] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);

  const tenantId = user.tenantId;

  const connectionByProvider = useMemo(() => {
    return new Map(connections.map(connection => [connection.provider, connection]));
  }, [connections]);

  const loadAll = async () => {
    const [providerItems, connectionItems] = await Promise.all([
      getProviders(),
      getIntegrations(tenantId),
    ]);
    setProviders(providerItems);
    setConnections(connectionItems);
  };

  useEffect(() => {
    void (async () => {
      try {
        setError(null);
        await loadAll();
      } catch {
        setError("Unable to load integration channels");
      }
    })();
  }, [tenantId]);

  const handleConnect = async (provider: string) => {
    try {
      setBusyProvider(provider);
      setError(null);
      await startConnect(provider, tenantId);
      await loadAll();
    } catch {
      setError("Failed to start channel connection");
    } finally {
      setBusyProvider(null);
    }
  };

  const handleDisconnect = async (connectionId: number, provider: string) => {
    try {
      setBusyProvider(provider);
      setError(null);
      await disconnectIntegration(connectionId, tenantId);
      await loadAll();
    } catch {
      setError("Failed to disconnect channel");
    } finally {
      setBusyProvider(null);
    }
  };

  const handleReconnect = async (connectionId: number, provider: string) => {
    try {
      setBusyProvider(provider);
      setError(null);
      await reconnectIntegration(connectionId, tenantId);
      await loadAll();
    } catch {
      setError("Failed to reconnect channel");
    } finally {
      setBusyProvider(null);
    }
  };

  const handleSyncAssets = async (provider: string) => {
    try {
      setBusyProvider(provider);
      setError(null);
      await syncProviderAssets(provider, tenantId);
      await loadAll();
    } catch {
      setError("Failed to sync channel assets");
    } finally {
      setBusyProvider(null);
    }
  };

  return (
    <div style={{ minHeight: "100vh", background: "#f8f7f4", padding: "24px" }}>
      <div style={{ maxWidth: 980, margin: "0 auto" }}>
        <header style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: 20 }}>
          <div>
            <h1 style={{ margin: 0, fontSize: 24, color: "#1f2937" }}>Channel Connections</h1>
            <p style={{ margin: "6px 0 0", color: "#667085", fontSize: 14 }}>
              Tenant: <strong>{user.tenantName}</strong>
            </p>
          </div>
          <div style={{ display: "flex", gap: 10 }}>
            <Link
              to="/"
              style={{
                textDecoration: "none",
                padding: "10px 14px",
                borderRadius: 8,
                border: "1px solid #d0d5dd",
                color: "#1d2939",
                background: "white",
                fontWeight: 600,
                fontSize: 13,
              }}
            >
              Inbox
            </Link>
            <button
              type="button"
              onClick={onLogout}
              style={{
                padding: "10px 14px",
                borderRadius: 8,
                border: "1px solid #d0d5dd",
                color: "#1d2939",
                background: "white",
                fontWeight: 600,
                fontSize: 13,
                cursor: "pointer",
              }}
            >
              Sign out
            </button>
          </div>
        </header>

        {error && (
          <div
            style={{
              marginBottom: 14,
              padding: "10px 12px",
              borderRadius: 8,
              border: "1px solid #fecaca",
              background: "#fef2f2",
              color: "#b42318",
              fontSize: 13,
            }}
          >
            {error}
          </div>
        )}

        <div style={{ display: "grid", gap: 12 }}>
          {providers.map((provider) => {
            const connection = connectionByProvider.get(provider.provider);
            const state = connection?.status ?? "NOT_CONNECTED";
            const isBusy = busyProvider === provider.provider;

            return (
              <div
                key={provider.provider}
                style={{
                  background: "white",
                  border: "1px solid #eaecf0",
                  borderRadius: 10,
                  padding: "14px 16px",
                  display: "flex",
                  alignItems: "center",
                  justifyContent: "space-between",
                  gap: 10,
                }}
              >
                <div style={{ minWidth: 0 }}>
                  <div style={{ display: "flex", alignItems: "center", gap: 8 }}>
                    <strong style={{ fontSize: 15, color: "#101828" }}>{provider.displayName}</strong>
                    <span
                      style={{
                        fontSize: 12,
                        color: statusColor(state),
                        background: "#f2f4f7",
                        padding: "2px 8px",
                        borderRadius: 999,
                      }}
                    >
                      {statusLabel(state)}
                    </span>
                  </div>
                  <div style={{ marginTop: 4, color: "#667085", fontSize: 12 }}>
                    Provider key: {provider.provider}
                  </div>
                </div>

                <div style={{ display: "flex", alignItems: "center", gap: 8, flexWrap: "wrap", justifyContent: "flex-end" }}>
                  {state === "PENDING" ? (
                    <button
                      type="button"
                      disabled
                      style={buttonStyle("secondary")}
                    >
                      Connecting...
                    </button>
                  ) : state === "CONNECTED" ? (
                    <>
                      <button
                        type="button"
                        disabled={isBusy}
                        onClick={() => handleSyncAssets(provider.provider)}
                        style={buttonStyle("secondary")}
                      >
                        Sync assets
                      </button>
                      <button
                        type="button"
                        disabled={isBusy || !connection}
                        onClick={() => connection && handleDisconnect(connection.id, provider.provider)}
                        style={buttonStyle("danger")}
                      >
                        Disconnect
                      </button>
                    </>
                  ) : state === "RECONNECT_REQUIRED" || state === "ERROR" || state === "DISCONNECTED" ? (
                    <button
                      type="button"
                      disabled={isBusy}
                      onClick={() =>
                        connection
                          ? handleReconnect(connection.id, provider.provider)
                          : handleConnect(provider.provider)
                      }
                      style={buttonStyle("primary")}
                    >
                      Reconnect
                    </button>
                  ) : (
                    <button
                      type="button"
                      disabled={isBusy}
                      onClick={() => handleConnect(provider.provider)}
                      style={buttonStyle("primary")}
                    >
                      Connect
                    </button>
                  )}
                </div>
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
}

function buttonStyle(kind: "primary" | "secondary" | "danger"): CSSProperties {
  if (kind === "primary") {
    return {
      padding: "8px 12px",
      borderRadius: 8,
      border: "1px solid #1366d6",
      background: "#1570ef",
      color: "white",
      fontWeight: 600,
      fontSize: 12,
      cursor: "pointer",
    };
  }
  if (kind === "danger") {
    return {
      padding: "8px 12px",
      borderRadius: 8,
      border: "1px solid #fecdca",
      background: "#fff5f4",
      color: "#b42318",
      fontWeight: 600,
      fontSize: 12,
      cursor: "pointer",
    };
  }
  return {
    padding: "8px 12px",
    borderRadius: 8,
    border: "1px solid #d0d5dd",
    background: "white",
    color: "#344054",
    fontWeight: 600,
    fontSize: 12,
    cursor: "pointer",
  };
}

