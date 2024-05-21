import { AvatarImage, AvatarFallback, Avatar } from "./avatar"
import { Link, useNavigate, useLocation } from "react-router-dom";
import { useAuth } from '../../../contexts/AuthContext'; // 경로는 실제 구조에 맞게 조정해야 함
import React  from 'react';
// Define the Sidebar component
function Sidebar() {
	const navigate = useNavigate();
	const location = useLocation();
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
					<AvatarImage alt="User Avatar" src="/placeholder.svg?height=100&width=100" />
					<AvatarFallback>U</AvatarFallback>
				</Avatar>
				<h2 className="mt-4 font-semibold text-xl">{localStorage.getItem('nickname')}</h2>
				<p className="text-sm text-gray-600">팔로워: 123 · 팔로잉: 123</p>
			</div>
			<div className="mt-10 flex flex-col">
				<Link className="text-gray-600 hover:bg-gray-200 p-2 rounded" to="/index">홈</Link>
				<Link className="text-gray-600 hover:bg-gray-200 p-2 rounded" state={{ from: location.pathname }} to="/BoardWrite">글쓰기</Link>
				<Link className="text-gray-600 hover:bg-gray-200 p-2 rounded" to="/mypage" >마이페이지</Link>
				<Link className="text-gray-600 hover:bg-gray-200 p-2 rounded" to="/Message">메세지</Link>
				<Link className="text-gray-600 hover:bg-gray-200 p-2 rounded">환경 설정</Link>
				<button className="text-gray-600 hover:bg-gray-200 p-2 rounded" onClick={handleLogout}>로그아웃</button>
			</div>
		</div>
	);
}

// Export the Sidebar component
export default Sidebar;