import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { useAuth } from './contexts/AuthContext';

import LoginPage from './components/component/LoginPage';
import SignupPage from './components/component/Signup';
import Component from './components/component/Component';

function App() {
  const auth = useAuth(); // useAuth 호출
  const users = auth && auth.users; // users가 있는지 확인

  return (
    <div className="App">
      <Routes>
        <Route path="/" element={<LoginPage />} />
        <Route path="/Signup" element={<SignupPage />} />
        <Route path="/index" element={users ? <Component /> : <Navigate to="/" replace />} />
      </Routes>
    </div>
  );
}

export default App;