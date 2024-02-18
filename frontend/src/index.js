import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';

import { BrowserRouter } from 'react-router-dom'; // BrowserRouter 임포트

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <BrowserRouter> {/* BrowserRouter로 App 컴포넌트를 감싸기 */}
      <App />
    </BrowserRouter>
  </React.StrictMode>
);

reportWebVitals();