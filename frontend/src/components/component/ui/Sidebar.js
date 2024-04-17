import React from 'react';
import { AvatarImage, AvatarFallback, Avatar } from "./avatar"
import { Link , useNavigate} from "react-router-dom";
import { useAuth } from '../../../contexts/AuthContext'; // 경로는 실제 구조에 맞게 조정해야 함

// Define the Sidebar component
function Sidebar() {
	const navigate = useNavigate();
  	const { logout } = useAuth();
	  const handleLogout = async () => {
	  try {
	   	logout();
	   	navigate('/');
	  } catch (error) {
		  console.log(error);
	    alert('다시 시도해주세요.');
	  }
   };
    return (
		
        <div className="w-70 bg-white p-5">
            <div className="flex flex-col items-center">
                <Avatar>
                    <AvatarImage alt="User Avatar" src="/placeholder.svg?height=120&width=120" />
                    <AvatarFallback>U</AvatarFallback>
                </Avatar>
                <h2 className="mt-4 font-semibold text-xl">Nickname</h2>
                <p className="text-sm text-gray-600">팔로워: 123 · 팔로잉: 123</p>
            </div>
            <div className="mt-10 flex flex-col">
                <Link className="text-gray-600 hover:bg-gray-200 p-2 rounded" to="/index">홈</Link>
                <Link className="text-gray-600 hover:bg-gray-200 p-2 rounded" to="/BoardWrite">글쓰기</Link>
                <Link className="text-gray-600 hover:bg-gray-200 p-2 rounded" to="/mypage">마이페이지</Link>
                <Link className="text-gray-600 hover:bg-gray-200 p-2 rounded">메세지</Link>
                <Link className="text-gray-600 hover:bg-gray-200 p-2 rounded">환경 설정</Link>
                <button className="text-gray-600 hover:bg-gray-200 p-2 rounded" onClick={handleLogout}>로그아웃</button>
            </div>
        </div>
    );
}

// Export the Sidebar component
export default Sidebar;