import { Button } from "./ui/button"
import React, { useState , useEffect} from 'react';
import { Link , useNavigate} from "react-router-dom";
import { Input } from "./ui/input"
import axios from 'axios';
import { useAuth } from '../../contexts/AuthContext'; // 경로는 실제 구조에 맞게 조정해야 함
import api from "../../api";

export default function Component() {
  const [isNavExpanded, setIsNavExpanded] = useState(false);
  const [showTopBtn, setShowTopBtn] = useState(false);
  const navigate = useNavigate();
  const { logout } = useAuth();
  const [posts, setPosts] = useState([]);
  
  useEffect(() => {
    const fetchPosts = async () => {
      try {
        // api 인스턴스 사용
        const info = localStorage.getItem('userInfo');
        const response = await api.get(`/boardList`, {info});
        console.log(response.data);
        setPosts(response.data);
      } catch (error) {
        console.error("Error fetching posts", error);
        if (error.response && error.response.status === 401) {
          // 로그인 페이지로 리다이렉트하거나 로그아웃 처리
          //navigate('/');
        }
      }
    };
    fetchPosts();
  }, [logout, navigate]);


  
  
  // 스크롤 이벤트 리스너 설정 및 정리
  useEffect(() => {
    const handleScroll = () => {
     if (window.scrollY > 300) {
        setShowTopBtn(true);
      } else {
        setShowTopBtn(false);
      }
    };

    window.addEventListener("scroll", handleScroll);
    // 컴포넌트 언마운트 시 이벤트 리스너 제거
    return () => window.removeEventListener("scroll", handleScroll);
  }, []);


  // 맨 위로 스크롤하는 함수
  const scrollToTop = () => {
    window.scrollTo({
      top: 0,
      behavior: 'smooth' // 부드러운 스크롤
    });
  };
  
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
	  <div className="flex min-h-screen bg-gray-100">
	      <div className="fixed top-0 bottom-0 z-10" onMouseEnter={() => setIsNavExpanded(true)} onMouseLeave={() => setIsNavExpanded(false)}>
		        {/* 마우스를 올렸을 때 너비가 확장되는 효과를 위한 div */}
		        <div className={`w-12 h-full bg-black transition-width duration-300 ease-in-out ${isNavExpanded ? "w-60" : "w-12"}`}>
		        {/* 네비게이션 바 내용, 마우스 오버 시 가시성 조정 및 세로 중앙 정렬 */}
		        <div className={`flex flex-col justify-center h-full opacity-0 ${isNavExpanded ? "opacity-100" : ""} transition-opacity duration-300 ease-in-out`}>
		        	<div className="w-full flex justify-center mt-5 flex-col items-center">
					  <img src="/placeholder-user.jpg" alt="Profile" className="w-13 h-13 rounded-full border-2 border-white" />
					  <span className="mt-2 dark:hover:text-gray-50 text-white">유저 Nickname</span>
					</div>
		          <nav className="mt-20">
		              <Link className="block h-10 pl-16 pr-4 py-2 font-medium rounded-md hover:bg-gray-100 hover:text-gray-900 dark:hover:bg-gray-800 dark:hover:text-gray-50 text-white" to="/index">홈</Link>
		              <Link className="block h-10 pl-16 pr-4 py-2 font-medium rounded-md hover:bg-gray-100 hover:text-gray-900 dark:hover:bg-gray-800 dark:hover:text-gray-50 text-white" to="/BoardWrite">글쓰기</Link>
		              <Link className="block h-10 pl-16 pr-4 py-2 font-medium rounded-md hover:bg-gray-100 hover:text-gray-900 dark:hover:bg-gray-800 dark:hover:text-gray-50 text-white" to="/mypage">마이페이지</Link>
		              <Link className="block h-10 pl-16 pr-4 py-2 font-medium rounded-md hover:bg-gray-100 hover:text-gray-900 dark:hover:bg-gray-800 dark:hover:text-gray-50 text-white" to="#">메세지</Link>
		              <Link className="block h-10 pl-16 pr-4 py-2 font-medium rounded-md hover:bg-gray-100 hover:text-gray-900 dark:hover:bg-gray-800 dark:hover:text-gray-50 text-white" to="#">환경 설정</Link>
		              
		              <Link className="block h-10 pl-16 pr-4 py-2 font-medium rounded-md hover:bg-gray-100 hover:text-gray-900 dark:hover:bg-gray-800 dark:hover:text-gray-50 text-white" to="/test">test</Link>
		              
		              <button className="block h-10 pl-16 pr-4 py-2 font-medium 
		              						rounded-md  hover:text-gray-900 dark:hover:bg-gray-800 dark:hover:text-gray-50
		                text-white" onClick={handleLogout}>로그아웃</button>
		          </nav>
		        </div>
			</div>
	      </div>
       
      
		{/* 상단 검색, 알람표시 */}
       <div className="flex flex-col w-full ml-60 mt-16">
	    <div className="flex justify-center items-center flex-col w-full">
          <form className="flex items-center gap-2 w-full max-w-md">
	      
	      <SearchIcon className="h-5 w-5" />
	      <Input
	        className="w-full h-10 font-normal rounded-none dark:placeholder-gray-400"
	        placeholder="Search"
	        type="search"
	      />
	          <Button className="square-8" size="icon" variant="ghost">
	      <BellIcon className="h-4 w-4" />
	      <span className="sr-only">Toggle notifications</span>
	    </Button>
	
	    </form>
	  </div>
	    
    {/* 메인 메뉴 */}
    <div className="flex-grow">
      <div className="grid grid-cols-1 gap-4 p-4">
      <div className="grid gap-4 w-full max-w-4xl mx-auto">
        <div className="grid gap-2">
			{/* 글은 여기부터*/}
			    {Array.isArray(posts) && posts.map((post) => (
					  <div key={post.board_id} className="rounded-xl bg-white p-4 grid gap-4 border border-gray-100 dark:border-gray-800">
					    <div className="flex items-center space-x-2">
					      <img
					        alt="Avatar"
					        className="rounded-full"
					        src="/placeholder.svg"
					        style={{
					          aspectRatio: "40/40",
					          objectFit: "cover",
					        }}
					        width="40"
					      />
					      <div className="grid gap-1">
					        <div className="font-semibold">{post.id}</div>
					        <div className="text-xs text-gray-500 dark:text-gray-400">{post.date}</div>
					      </div>
					    </div>
					    <div className="line-clamp-3">
					      <p>{post.content}</p>
					    </div>
					    {post.img && <img src={post.img} alt="Post" />}
					    {post.video && <video src={post.video} controls />}
					    <div className="flex space-x-4 flex-wrap">
					      <button className="w-10 h-8">Like</button> 
					      <button className="w-16 h-8">Comment</button>
					      <button className="w-16 h-8">Share</button> 
					    </div>
					  </div>
					))}
		{/*여기까지*/}
		
            </div>
          </div>
        </div>
      </div>
	 </div>
	 
	 
	 
	 {/* 맨 위로 버튼 */}
      {showTopBtn && (
        <button
          className="fixed bottom-10 right-10 bg-white hover:bg-gray-100 text-gray-900 font-bold rounded-full h-12 w-12 flex justify-center items-center border-4 border-gray-900 cursor-pointer"
          onClick={scrollToTop}
          style={{ width: '50px', height: '50px', display: 'flex', justifyContent: 'center', alignItems: 'center' }}
        >
          ▲
        </button>
      )}
    </div>
  )
}

function BellIcon(props) {
  return (
    <svg
      {...props}
      xmlns="http://www.w3.org/2000/svg"
      width="24"
      height="24"
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      strokeWidth="2"
      strokeLinecap="round"
      strokeLinejoin="round"
    >
      <path d="M6 8a6 6 0 0 1 12 0c0 7 3 9 3 9H3s3-2 3-9" />
      <path d="M10.3 21a1.94 1.94 0 0 0 3.4 0" />
    </svg>
  )
}


function SearchIcon(props) {
  return (
    <svg
      {...props}
      xmlns="http://www.w3.org/2000/svg"
      width="24"
      height="24"
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      strokeWidth="2"
      strokeLinecap="round"
      strokeLinejoin="round"
    >
      <circle cx="11" cy="11" r="8" />
      <path d="m21 21-4.3-4.3" />
    </svg>
  )
}

function FlagIcon(props) {
  return (
    <svg
      {...props}
      xmlns="http://www.w3.org/2000/svg"
      width="24"
      height="24"
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      strokeWidth="2"
      strokeLinecap="round"
      strokeLinejoin="round"
    >
      <path d="M4 15s1-1 4-1 5 2 8 2 4-1 4-1V3s-1 1-4 1-5-2-8-2-4 1-4 1z" />
      <line x1="4" x2="4" y1="22" y2="15" />
    </svg>
  )
}


function HeartIcon(props) {
  return (
    <svg
      {...props}
      xmlns="http://www.w3.org/2000/svg"
      width="24"
      height="24"
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      strokeWidth="2"
      strokeLinecap="round"
      strokeLinejoin="round"
    >
      <path d="M19 14c1.49-1.46 3-3.21 3-5.5A5.5 5.5 0 0 0 16.5 3c-1.76 0-3 .5-4.5 2-1.5-1.5-2.74-2-4.5-2A5.5 5.5 0 0 0 2 8.5c0 2.3 1.5 4.05 3 5.5l7 7Z" />
    </svg>
  )
}


function MessageCircleIcon(props) {
  return (
    <svg
      {...props}
      xmlns="http://www.w3.org/2000/svg"
      width="24"
      height="24"
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      strokeWidth="2"
      strokeLinecap="round"
      strokeLinejoin="round"
    >
      <path d="m3 21 1.9-5.7a8.5 8.5 0 1 1 3.8 3.8z" />
    </svg>
  )
}


function MoreHorizontalIcon(props) {
  return (
    <svg
      {...props}
      xmlns="http://www.w3.org/2000/svg"
      width="24"
      height="24"
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      strokeWidth="2"
      strokeLinecap="round"
      strokeLinejoin="round"
    >
      <circle cx="12" cy="12" r="1" />
      <circle cx="19" cy="12" r="1" />
      <circle cx="5" cy="12" r="1" />
    </svg>
  )
}





function ShareIcon(props) {
  return (
    <svg
      {...props}
      xmlns="http://www.w3.org/2000/svg"
      width="24"
      height="24"
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      strokeWidth="2"
      strokeLinecap="round"
      strokeLinejoin="round"
    >
      <path d="M4 12v8a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2v-8" />
      <polyline points="16 6 12 2 8 6" />
      <line x1="12" x2="12" y1="2" y2="15" />
    </svg>
  )
}