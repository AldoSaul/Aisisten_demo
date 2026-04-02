import { useMemo, useState } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import LoginScreen from './Front/LoginScreen';
import LeadsApp from './Front/LeadsApp';
import { clearSession, loadSession, saveSession } from './auth/session';
import type { UserSession } from './api/auth';

export default function App() {
  const [user, setUser] = useState<UserSession | null>(() => loadSession());

  const isAuthenticated = !!user;

  const authActions = useMemo(() => ({
    login(userSession: UserSession) {
      saveSession(userSession);
      setUser(userSession);
    },
    logout() {
      clearSession();
      setUser(null);
    },
  }), []);

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
      </Routes>
    </Router>
  );
}