import React, { createContext, useContext, useState } from 'react';

// Create the context outside of any component just once
const AuthContext = createContext({
  users: null, // This is just for initial value; it's not used directly
  login: () => {},
  logout: () => {},
});

// Custom hook to use the auth context
export function useAuth() {
  return useContext(AuthContext);
}

// Provider component that encapsulates the state logic and provides it to the rest of the app
export const AuthProvider = ({ children }) => {
  const [users, setUser] = useState(null);

  const login = (userData) => {
    setUser(userData);
  };

  const logout = () => {
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ users, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};
