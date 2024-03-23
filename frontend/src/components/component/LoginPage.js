import React, { useState,useEffect } from 'react';
import { useNavigate, Link,useLocation } from 'react-router-dom'; // useNavigate 훅 임포트
import { useAuth } from '../../contexts/AuthContext';
import { Label } from "./ui/label"
import { Input } from "./ui/input"
import { Button } from "./ui/button"
import axios from 'axios';

function LoginPage() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const { login } = useAuth();
  const { logout } = useAuth();
  const navigate = useNavigate();
	 
	 
	useEffect(() => {
	  const queryParams = new URLSearchParams(window.location.search);
	  const userParam = queryParams.get('user');
	
	  if (userParam) {
	    // 'user' 쿼리 파라미터에서 정보 추출 및 변환
	    // 예시: "UsersInfoDTO(nickname=, phone=010-7137-5740, email=wkdwlgjs111@naver.com, role=ROLE_USER_SNS)"
	    const userInfoString = userParam.slice(userParam.indexOf('(') + 1, -1); // 괄호 안의 내용 추출
	    const userInfoArray = userInfoString.split(', '); // 쉼표로 분리하여 배열로 변환
	
	    // 배열의 각 요소를 키-값 쌍으로 변환
	    const userInfoObj = userInfoArray.reduce((obj, item) => {
	      const [key, value] = item.split('=');
	      obj[key] = value;
	      return obj;
	    }, {});
	
	    // sessionStorage에 저장
	    sessionStorage.setItem('userInfo', JSON.stringify(userInfoObj));
	    alert('로그인 성공');
	    navigate('/index');
	  }
	}, []);
	
  const goToSignUp = () => {
    navigate('/Signup'); // '/signup'은 회원가입 페이지의 경로로, 실제 경로에 맞게 수정해야 합니다.
  };
  const handleLogin = async () => {
	  try {
	    const params = new URLSearchParams();
	    params.append('username', username);
	    params.append('password', password);
	
	    const response = await axios.post('http://localhost:8080/api/Login', params, {
	      headers: {
	        'Content-Type': 'application/x-www-form-urlencoded'
	      },
	      credentials: 'include' // 세션 쿠키를 클라이언트에서 서버로 전송하기 위해 필요한 옵션
	    });
	    console.log(response);
	    sessionStorage.setItem('userInfo', JSON.stringify(response.data));
	    
	    await login({ username, password }); // 예시 로그인 함수
	    alert('로그인 성공');
	    navigate('/index');
	  } catch (error) {
		  
	    if (error.response) {
	      const status = error.response.status;
	      if (status === 401 || status === 403) {
	        alert('계정 정보를 확인해주세요.');
	      } else {
	        alert('로그인 중 문제가 발생했습니다. 다시 시도해주세요.');
	      }
	    } else {
	      alert('서버와의 연결에 문제가 발생했습니다.');
	    }
	  }
	};  
	
	
	
  const handleSubmit = async (e) => {
    e.preventDefault(); // 기본 제출 동작 방지
    await handleLogin(); // 로그인 처리
  };
  
  const handleGetUserInfo = async () => {
	  try {
	    const userInfoStr = sessionStorage.getItem('userInfo');
	    if (userInfoStr) {
	      const userInfo = JSON.parse(userInfoStr);
	      console.log('저장된 사용자 정보:', userInfo);
	    } else {
	      console.log('저장된 사용자 정보가 없습니다.');
	    }
	  } catch (error) {
	    console.error('사용자 정보를 가져오는데 실패했습니다.', error);
	  }
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
              <Label htmlFor="username">Username</Label>
              <Input id="username" placeholder="r___k18" value={username} onChange={(e) => setUsername(e.target.value)} />
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
          <Button className="w-full" style={{ backgroundColor: '#03C75A'}} variant="outline"
          						onClick={() => window.location.href='http://localhost:8080/oauth2/authorization/naver'} >
            Login with Naver
          </Button>
          <Button className="w-full" style={{ backgroundColor: '#03C75A'}} variant="outline"
          						onClick={handleGetUserInfo} >          
            test
          </Button>
          <Button className="w-full" style={{ backgroundColor: '#03C75A'}} variant="outline"
          						onClick={logout} >          
            logout
          </Button>
          <Button className="w-full" variant="outline"
          							onClick={() => window.location.href='http://localhost:8080/oauth2/authorization/google'} >
            Login with Google
          </Button>
          <Button className="w-full" style={{ backgroundColor: '#FEE500'}} variant="outline"
          							 onClick={() => window.location.href='http://localhost:8080/oauth2/authorization/kakao'} >
            Login with Kakao
          </Button>
        </div>
      </div>
    </div>
  );
}

export default LoginPage;