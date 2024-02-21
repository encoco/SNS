import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom'; // useNavigate 훅 임포트
import { useAuth } from '../../contexts/AuthContext';
import { Label } from "./ui/label"
import { Input } from "./ui/input"
import { Button } from "./ui/button"
import axios from 'axios';

function LoginPage() {
  const [id, setId] = useState('');
  const [password, setPassword] = useState('');
  const { login } = useAuth();
  const navigate = useNavigate();
  
  const goToSignUp = () => {
    navigate('/Signup'); // '/signup'은 회원가입 페이지의 경로로, 실제 경로에 맞게 수정해야 합니다.
  };

  const handleLogin = async () => {
    try {
      const response = await axios.post('http://localhost:8080/api/Login', {
        id: id,
        password: password
      });
      alert('로그인 성공');
      await login({ id, password }); // 예시 로그인 함수
      navigate('/index'); // 회원가입 성공 후 로그인 페이지로 이동
    } catch (error) {
      alert('계정 정보를 확인해주세요.');
    }
  };
  
  const handleSubmit = async (e) => {
    e.preventDefault(); // 기본 제출 동작 방지
    await handleLogin(); // 로그인 처리
  };

  return (
    <div className="flex flex-col gap-6 p-4 sm:p-6 items-center justify-center min-h-screen">
      <div className="w-full max-w-[400px]">
        
          <div className="text-center py-6">
            <h1 className="text-3xl font-bold">Welcome</h1>
            <p className="text-gray-500 dark:text-gray-400">Enter your email to login</p>
          </div>

        
        <form onSubmit={handleSubmit}>
          <div className="space-y-4">
            <div className="space-y-2">
              <Label htmlFor="id">Username</Label>
              <Input id="id" placeholder="r___k18" value={id} onChange={(e) => setId(e.target.value)} />
            </div>
            <div className="space-y-2">
              <Label htmlFor="password">Password</Label>
              <Input id="password" type="password" value={password} onChange={(e) => setPassword(e.target.value)}/>
            </div>
            <div className="mt-4 space-y-2 text-center">
              <Link className="text-sm underline" to="#">
                Forgot your password?
              </Link>
            </div>
            <Button type="submit" className="w-full mb-2">Login</Button>
            <Button className="w-full mb-2" onClick={goToSignUp}>Join Us</Button>
          </div>
        </form>
        <br/>
        
        <div className="space-y-4">
          <Button className="w-full" style={{ backgroundColor: '#03C75A'}} variant="outline">
            Login with Naver
          </Button>
          <Button className="w-full" variant="outline">
            Login with Google
          </Button>
          <Button className="w-full" style={{ backgroundColor: '#FEE500'}} variant="outline">
            Login with Kakao
          </Button>
        </div>
      </div>
    </div>
  );
}

export default LoginPage;
