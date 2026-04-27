import { useEffect, useMemo, useState } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import LoginScreen from './Front/LoginScreen';
import LeadsApp from './Front/LeadsApp';
import ChannelsScreen from './Front/ChannelsScreen';
import { clearSession, loadSession, saveSession } from './auth/session';
import { getMe, type LoginResponse, type UserSession } from './api/auth';

export default function App() {
  const [user, setUser] = useState<UserSession | null>(null);
  const [authReady, setAuthReady] = useState(false);

  useEffect(() => {
    const bootstrapAuth = async () => {
      const storedSession = loadSession();
      if (!storedSession) {
        setAuthReady(true);
        return;
      }

      try {
        const currentUser = await getMe();
        saveSession({ ...storedSession, user: currentUser });
        setUser(currentUser);
      } catch {
        clearSession();
        setUser(null);
      } finally {
        setAuthReady(true);
      }
    };

    void bootstrapAuth();
  }, []);

  const isAuthenticated = !!user;

  const authActions = useMemo(() => ({
    login(loginResponse: LoginResponse) {
      saveSession({
        token: loginResponse.accessToken,
        tokenType: loginResponse.tokenType,
        user: loginResponse.user,
      });
      setUser(loginResponse.user);
    },
    logout() {
      clearSession();
      setUser(null);
    },
  }), []);

  if (!authReady) {
    return null;
  }

  return (
    <Router>
      <Routes>
        <Route
          path="/login"
          element={
            !isAuthenticated
              ? <LoginScreen onLogin={authActions.login} />
              : <Navigate to="/" replace />
          }
        />
        <Route
          path="/"
          element={isAuthenticated ? <LeadsApp onLogout={authActions.logout} /> : <Navigate to="/login" replace />}
        />
        <Route
          path="/channels"
          element={isAuthenticated && user ? <ChannelsScreen user={user} onLogout={authActions.logout} /> : <Navigate to="/login" replace />}
        />
      </Routes>
    </Router>
  );
}
