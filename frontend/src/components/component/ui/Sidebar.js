import { AvatarImage, AvatarFallback, Avatar } from "./avatar"
import { Link, useNavigate, useLocation } from "react-router-dom";
import { useAuth } from '../../../contexts/AuthContext'; // 경로는 실제 구조에 맞게 조정해야 함
import React, { useState, useEffect } from 'react';
import { BrowserView, MobileView } from "react-device-detect";
import BoardWrite from '../BoardWrite'; 


// Define the Sidebar component
function Sidebar() {
   const navigate = useNavigate();
   const location = useLocation();
   const { logout } = useAuth();
   const [nickname, setNickname] = useState(''); // 초기 상태를 빈 문자열로 설정
   const [img,setImg] = useState('');
   const [id,setId] = useState('');
   const [isModalOpen, setIsModalOpen] = useState(false);

   
    useEffect(() => {
        // userInfo에서 nickname을 추출하여 상태에 저장
        const userInfoJSON = localStorage.getItem('nickname');
        if (userInfoJSON) {
            const userInfo = JSON.parse(userInfoJSON);
            setNickname(userInfo.nickname); // nickname 상태 업데이트
            setImg(userInfo.img);
            setId(userInfo.id);
        }
    }, []); // 의존성 배열을 빈 배열로 설정하여 컴포넌트 마운트 시에만 실행

   const handleLogout = async () => {
      try {
         logout();
         navigate('/');
      } catch (error) {
         console.log(error);
         alert('다시 시도해주세요.');
      }
   };

   const openModal = () => setIsModalOpen(true);
    const closeModal = () => setIsModalOpen(false);

   return (
      <>
         <BrowserView>
            <div className="w-70 h-full bg-white p-5">
               <div className="flex flex-col items-center">
                  <Avatar>
                     <div className="w-25 h-25 rounded-full overflow-hidden">
                        <AvatarImage alt="User Avatar" className="w-full h-full object-cover" src={img || "/placeholder.svg?height=100&width=100"} />
                     </div>
                  </Avatar>
                  <h2 className="mt-4 font-semibold text-xl">{nickname}</h2>
                  <p className="text-sm text-gray-600">팔로워: 123 · 팔로잉: 123</p>
               </div>
               <div className="mt-10 flex flex-col">
                  <Link className="text-gray-600 hover:bg-gray-200 p-2 rounded" to="/index">홈</Link>
                  <Link className="text-gray-600 hover:bg-gray-200 p-2 rounded" onClick={openModal}>글쓰기</Link>
                  <Link className="text-gray-600 hover:bg-gray-200 p-2 rounded" to={`/UserPage/${id}`} >마이페이지</Link>
                  <Link className="text-gray-600 hover:bg-gray-200 p-2 rounded" to="/Message">메세지</Link>
                  <Link className="text-gray-600 hover:bg-gray-200 p-2 rounded">환경 설정</Link>
                  <button className="text-gray-600 hover:bg-gray-200 p-2 rounded" onClick={handleLogout}>로그아웃</button>
               </div>
            </div>
            <BoardWrite isOpen={isModalOpen} onRequestClose={closeModal} />
         </BrowserView>
         <MobileView>
            { /* 모바일 화면에서는 네비게이션 바를 렌더링 */}
            <div className="fixed bottom-0 left-0 w-full bg-white border-t border-gray-200 flex justify-around p-4" style={{ zIndex: '2' }}>
               <Link className="text-gray-600 hover:bg-gray-200 p-2 rounded text-sm" to="/index">홈</Link>
               <Link className="text-gray-600 hover:bg-gray-200 p-2 rounded text-sm" state={{ from: location.pathname }} to="/BoardWrite">글쓰기</Link>
               <Link className="text-gray-600 hover:bg-gray-200 p-2 rounded text-sm" to="/mypage">마이페이지</Link>
               <Link className="text-gray-600 hover:bg-gray-200 p-2 rounded text-sm" to="/Message">메세지</Link>
               <Link className="text-gray-600 hover:bg-gray-200 p-2 rounded text-sm">환경 설정</Link>
               <button className="text-gray-600 hover:bg-gray-200 p-2 rounded text-sm" onClick={handleLogout}>로그아웃</button>
            </div>
         </MobileView>
      </>
   );

}

// Export the Sidebar component
export default Sidebar;