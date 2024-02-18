/*LoginPage*/

import React, { useState } from 'react';
import { useNavigate,Link } from 'react-router-dom'; // useNavigate 훅 임포트
import { Label } from "./ui/label"
import { Input } from "./ui/input"
import { Button } from "./ui/button"
import axios from 'axios';

	function Signup() {
	  const navigate = useNavigate();
	  const [id, setId] = useState('');
	  const [password, setPassword] = useState('');
	  const [passwordCheck, setPasswordCheck] = useState('');
	  const [email, setEmail] = useState('');
	  const [phone, setPhone] = useState('');
	
	  const handleSignup = async () => {
	    // 비밀번호 확인 로직 추가
	    if (password !== passwordCheck) {
	      alert('Passwords do not match');
	      return;
	    }
	    try {
	      const response = await axios.post('http://localhost:8080/api/Signup', {
	        id: id,
	        password: password,
	        email: email,
	        phone: phone,
	      });
	      console.log(response.data);
	      navigate('/Login'); // 회원가입 성공 후 로그인 페이지로 이동
	    } catch (error) {
	      console.error('회원가입 실패', error);
	    }
	  };
	  return (
	    <div className="flex flex-col gap-6 p-4 sm:p-6 items-center justify-center min-h-screen">
	      <div className="w-full max-w-[400px]">
	        <div className="text-center py-6">
	          <h1 className="text-3xl font-bold">Join us</h1>
	          <p className="text-gray-500 dark:text-gray-400">Create your account</p>
	        </div>
	        <div className="space-y-4">
	          <div className="space-y-2">
	            <Label htmlFor="id">ID</Label>
	            <Input id="id" placeholder="r___k18" value={id} onChange={(e) => setId(e.target.value)} />
	          </div>
	          <div className="space-y-2">
	            <Label htmlFor="password">Password</Label>
	            <Input id="password" type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
	          </div>
	          <div className="space-y-2">
	            <Label htmlFor="passwordck">Password Check</Label>
	            <Input id="passwordck" type="password" value={passwordCheck} onChange={(e) => setPasswordCheck(e.target.value)} />
	          </div>
	          <div className="space-y-2">
	            <Label htmlFor="phone">Phone Number</Label>
	            <Input id="phone" value={phone} onChange={(e) => setPhone(e.target.value)} />
	          </div>
	          <div className="space-y-2">
	            <Label htmlFor="email">Email</Label>
	            <Input id="email" type="email" value={email} onChange={(e) => setEmail(e.target.value)} />
	          </div>
	          
	          <Button className="w-full" onClick={handleSignup}>Join</Button>
	        </div>
	      </div>
	    </div>
	  );
	}

export default Signup;