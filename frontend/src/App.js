import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { useAuth } from './contexts/AuthContext';

import LoginPage from './components/component/LoginPage';
import SignupPage from './components/component/Signup';
import Component from './components/component/component';
import BoardWrite from './components/component/BoardWrite';
import Mypage from './components/component/my-page';
import Test from './components/component/Test';



// 로그인 상태에 따라 접근 권한을 조절하는 컴포넌트
const PrivateRoute = ({ children }) => {
  const userInfo = localStorage.getItem('userInfo'); // localStorage에서 userInfo 가져오기
  return userInfo ? children : <Navigate to="/" />; // userInfo가 있으면 자식 컴포넌트를, 없으면 로그인 페이지로 리다이렉트
};

const PublicRoute = ({ children }) => {
  const userInfo = localStorage.getItem('userInfo');
  return userInfo ? <Navigate to="/index" /> : children; // userInfo가 있으면 메인 페이지로, 없으면 자식 컴포넌트를 리턴
};

function App() {
  return (
    <div className="App">
      <Routes>
        <Route path="/" element={<PublicRoute><LoginPage /></PublicRoute>} />
        <Route path="/signup" element={<PublicRoute><SignupPage /></PublicRoute>} />
        <Route path="/index" element={<PrivateRoute><Component /></PrivateRoute>} />
        <Route path="/BoardWrite" element={<PrivateRoute><BoardWrite /></PrivateRoute>} />
        <Route path="/mypage" element={<PrivateRoute><Mypage/></PrivateRoute>} />
        
        <Route path="/Test" element={<PrivateRoute><Test/></PrivateRoute>} />
      </Routes>
    </div>
  );
}

export default App;
