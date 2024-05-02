import { Button } from "./ui/button"
import React, { useState , useEffect} from 'react';
import { Link , useNavigate} from "react-router-dom";
import { Input } from "./ui/input"
import axios from 'axios';
import { useAuth } from '../../contexts/AuthContext'; // 경로는 실제 구조에 맞게 조정해야 함
import api from "../../api";
import ImageSlider from './ImageSlider'; // ImageSlider 컴포넌트를 import
import { AvatarImage, AvatarFallback, Avatar } from "./ui/avatar"
import  Sidebar from "./ui/Sidebar";
import DropdownMenu from './ui/DropdownMenu';

export default function Component() {
  const [showTopBtn, setShowTopBtn] = useState(false);
  const navigate = useNavigate();
  const { logout } = useAuth();
  const [posts, setPosts] = useState([]);
  
  useEffect(() => {
	 const fetchPosts = async () => {
	  try {
	    const response = await api.get(`/boardList`,{
		  withCredentials: true,
		});
	    setPosts(response.data.posts);
	  } catch (error) {
	    console.log(error);
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


   const LikeHandler = async (boardId) => {
		  	try {
				const formData = new FormData();
        		formData.append('boardId', boardId);
		    	const response = await api.get(`/boardLike?boardId=${boardId}`,{
				  withCredentials: true,
				});
				alert('좋아요');
			  	} catch (error) {
			    console.log(error);
			    alert('글쓰기에 실패했습니다. 다시 시도해주세요.');
			}
	   };
	
  // 맨 위로 스크롤하는 함수
  const scrollToTop = () => {
    window.scrollTo({
      top: 0,
      behavior: 'smooth' // 부드러운 스크롤
    });
  };
   
  return (
	  <div className="flex min-h-screen bg-gray-100">
	  
	  <Sidebar />

		{/* 상단 검색, 알람표시 */}
       <div className="flex flex-col w-full ml-10">
	    <div className="flex justify-center items-center flex-col w-full">
          <form className="flex items-center gap-2 w-full max-w-md mt-5">
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
				  <div key={post.board_id} className="rounded-xl bg-white p-4 grid gap-4 border border-gray-100 dark:border-gray-800 relative">
				    <div className="flex justify-between items-start">
				      <div className="flex items-center space-x-2">
				        <img
				          alt="Avatar"
				          className="rounded-full"
				          src="/placeholder.svg"
				          style={{
				            aspectRatio: "1 / 1",
				            objectFit: "cover",
				          }}
				          width="40"
				        />
				        <div className="grid gap-1">
				          <div className="font-semibold">{post.nickname}</div>
				          <div className="text-xs text-gray-500 dark:text-gray-400">{post.date}</div>
				        </div>
				      </div>
				      <DropdownMenu post={post} />
				    </div>
				    {post.imgpath && <ImageSlider imgpath={post.imgpath} />}
				    {post.video && <video src={post.video} controls />}
				    <div className="line-clamp-3">
				      <p>{post.content}</p>
				    </div>
				    <div className="flex space-x-4 flex-wrap">
				      <button className="w-10 h-8"  onClick={() => LikeHandler(post.board_id)}>Like</button>
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
