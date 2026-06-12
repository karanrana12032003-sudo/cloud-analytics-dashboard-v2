import { useState } from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import Auth from './Auth';
import Dashboard from './Dashboard';
import AnalyticsDashboard from './AnalyticsDashboard';
import './App.css';
import './index.css';

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  // A simple way to check if the user is authenticated (e.g., by checking for a token)
  // In a real application, this would involve more robust state management and token validation
  const checkAuth = () => {
    const token = localStorage.getItem('token');
    setIsAuthenticated(!!token);
  };

  // Check auth status on initial load
  useState(() => {
    checkAuth();
  });

  return (
    <Router>
      <Routes>
        <Route path="/auth" element={<Auth />} />
        <Route
          path="/dashboard"
          element={isAuthenticated ? <Dashboard /> : <Navigate to="/auth" replace />}
        />
        <Route
          path="/analytics"
          element={isAuthenticated ? <AnalyticsDashboard /> : <Navigate to="/auth" replace />}
        />
        <Route path="*" element={<Navigate to="/auth" replace />} />
      </Routes>
    </Router>
  )
}

export default App
