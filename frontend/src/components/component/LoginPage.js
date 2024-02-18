/*LoginPage*/

import React from 'react';
import { useNavigate,Link } from 'react-router-dom'; // useNavigate 훅 임포트
import { Label } from "./ui/label"
import { Input } from "./ui/input"
import { Button } from "./ui/button"


function LoginPage() {
  let navigate = useNavigate(); // useNavigate 훅 사용
  const goToSignUp = () => {
    navigate('/Signup'); // '/signup'은 회원가입 페이지의 경로로, 실제 경로에 맞게 수정해야 합니다.
  };

  return (
	<div className="flex flex-col gap-6 p-4 sm:p-6 items-center justify-center min-h-screen">
	      <div className="w-full max-w-[400px]">
	        
	          <div className="text-center py-6">
	            <h1 className="text-3xl font-bold">Welcome</h1>
	            <p className="text-gray-500 dark:text-gray-400">Enter your email to login</p>
	          </div>

	        
	        <div className="space-y-4">
	          <div className="space-y-2">
	            <Label htmlFor="email">Username</Label>
	            <Input id="email" placeholder="r___k18" />
	          </div>
	          <div className="space-y-2">
	            <Label htmlFor="password">Password</Label>
	            <Input id="password" type="password" />
	          </div>
	          <div className="mt-4 space-y-2 text-center">
		          <Link className="text-sm underline" href="#">
		            Forgot your password?
		          </Link>
	          </div>
	          <Button className="w-full">Login</Button>
	          <Button className="w-full" onClick={goToSignUp}>Join Us</Button>
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
	    </div>
	  );
	}

export default LoginPage;

