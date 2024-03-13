import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { useAuth } from './contexts/AuthContext';

import LoginPage from './components/component/LoginPage';
import SignupPage from './components/component/Signup';
import Component from './components/component/Component';

function App() {

  const checkSession = () => sessionStorage.getItem("userInfo") !== null;
  return (
    <div className="App">
      <Routes>
		<Route path="/" element={checkSession() ? <Navigate to="/index" /> : <LoginPage />} />
        <Route path="/signup" element={checkSession() ? <Navigate to="/index" /> : <SignupPage />} />
        <Route path="/index" element={!checkSession() ? <Navigate to="/" /> : <Component />} />
      </Routes>
    </div>
  );
}

export default App;