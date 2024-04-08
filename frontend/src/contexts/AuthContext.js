import React, { createContext, useContext, useState } from 'react';
import axios from 'axios';

const AuthContext = createContext({
  users: null, 
  login: () => {},
  logout: () => {},
});

// Custom hook to use the auth context
export function useAuth() {
  return useContext(AuthContext);
}

export const AuthProvider = ({ children }) => {
  const [users, setUser] = useState(null);

  const login = (userData) => {
	  
    setUser(userData);
  };

  const logout = () => {
	  try {
	    axios.get('http://localhost:8080/api/Logout', {
	      withCredentials: true
	    });
	    // 여기서 응답 처리
	    localStorage.removeItem('userInfo'); // 세션 스토리지에서 사용자 정보 제거
	    alert("로그아웃 하셨습니다.");
	  } catch (error) {
	    console.error('로그아웃 오류', error);
	  }
	};

  return (
    <AuthContext.Provider value={{ users, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};
