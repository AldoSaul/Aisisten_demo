import { useState } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import LoginScreen from './Front/LoginScreen';
import LeadsApp from './Front/LeadsApp';

export default function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  return (
    <Router>
      <Routes>
        <Route
          path="/login"
          element={!isAuthenticated ? <LoginScreen onLogin={() => setIsAuthenticated(true)} /> : <Navigate to="/" />}
        />
        <Route
          path="/"
          element={isAuthenticated ? <LeadsApp /> : <Navigate to="/login" />}
        />
      </Routes>
    </Router>
  );
}