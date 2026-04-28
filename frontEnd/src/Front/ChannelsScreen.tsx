import { useEffect, useMemo, useState } from "react";
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
import "../CSS/channels.css";

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

function statusBadgeClass(status: string): string {
  switch (status) {
    case "CONNECTED":        return "status-badge status-badge--connected";
    case "ERROR":            return "status-badge status-badge--error";
    case "RECONNECT_REQUIRED": return "status-badge status-badge--reconnect";
    case "PENDING":          return "status-badge status-badge--pending";
    default:                 return "status-badge status-badge--default";
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
    <div className="channels-page">
      <div className="channels-content">
        <header className="channels-header">
          <div className="channels-header-title">
            <h1>Channel Connections</h1>
            <p>
              Tenant: <strong>{user.tenantName}</strong>
            </p>
          </div>
          <div className="channels-header-actions">
            <Link to="/" className="btn">Inbox</Link>
            <button type="button" onClick={onLogout} className="btn">
              Sign out
            </button>
          </div>
        </header>

        {error && <div className="channels-error">{error}</div>}

        <div className="channels-list">
          {providers.map((provider) => {
            const connection = connectionByProvider.get(provider.provider);
            const state = connection?.status ?? "NOT_CONNECTED";
            const isBusy = busyProvider === provider.provider;

            return (
              <div key={provider.provider} className="channel-card">
                <div className="channel-card-info">
                  <div className="channel-name-row">
                    <strong className="channel-display-name">{provider.displayName}</strong>
                    <span className={statusBadgeClass(state)}>{statusLabel(state)}</span>
                  </div>
                  <div className="channel-provider-key">
                    Provider key: {provider.provider}
                  </div>
                </div>

                <div className="channel-actions">
                  {state === "PENDING" ? (
                    <button type="button" disabled className="btn-action secondary">
                      Connecting...
                    </button>
                  ) : state === "CONNECTED" ? (
                    <>
                      <button
                        type="button"
                        disabled={isBusy}
                        onClick={() => handleSyncAssets(provider.provider)}
                        className="btn-action secondary"
                      >
                        Sync assets
                      </button>
                      <button
                        type="button"
                        disabled={isBusy || !connection}
                        onClick={() => connection && handleDisconnect(connection.id, provider.provider)}
                        className="btn-action danger"
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
                      className="btn-action primary"
                    >
                      Reconnect
                    </button>
                  ) : (
                    <button
                      type="button"
                      disabled={isBusy}
                      onClick={() => handleConnect(provider.provider)}
                      className="btn-action primary"
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

