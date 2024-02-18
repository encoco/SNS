import React from 'react';
import { Routes, Route } from 'react-router-dom'; // BrowserRouter 제거
import LoginPage from './components/component/LoginPage'; // 로그인 페이지 컴포넌트 임포트
import Component from './components/component/Component'; // 기존에 사용하던 다른 컴포넌트 임포트
import SignupPage from './components/component/Signup'; // 기존에 사용하던 다른 컴포넌트 임포트

function App() {
  return (
    <div className="App">
      <Routes>
        <Route path="/Login" element={<LoginPage />} />
        <Route path="/Signup" element={<SignupPage />} /> {/* 수정된 부분 */}
        {/* 다른 경로와 컴포넌트 설정 */}
        <Route path="/" element={<Component />} />
      </Routes>
    </div>
  );
}

export default App;