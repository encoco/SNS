import React, { useState , useEffect} from 'react';
import ImageSlider from './ImageSlider'; // ImageSlider 컴포넌트를 import
import api from "../../api";
import { Link ,useNavigate} from "react-router-dom";
import { Button } from "./ui/button"
import { CardTitle,CardHeader, CardContent, CardFooter, Card } from "./ui/card"
import axios from 'axios';
import { useAuth } from '../../contexts/AuthContext'; // 경로는 실제 구조에 맞게 조정해야 함
import  Sidebar from "./ui/Sidebar";
import DropdownMenu from './ui/DropdownMenu';

function Mypage() {
	const [showTopBtn, setShowTopBtn] = useState(false);
	const [posts, setPosts] = useState([]);
	const navigate = useNavigate();
  	const { logout } = useAuth();
  	
	useEffect(() => {
	 const fetchPosts = async () => {
	  try {
	    const response = await api.get(`/boardList`,{
		  withCredentials: true,
		});
	    setPosts(response.data);
	    console.log(response.data);
	    
	  } catch (error) {
	    console.log(error);
	  }
	};
    fetchPosts();
  }, [logout, navigate]);
  
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
   
  return (
      <div className="flex min-h-screen bg-gray-100">

	  <Sidebar />
      
      <div className="flex-1 p-5">
        <h1 className="text-3xl font-bold mb-5">My Feed</h1>
        <div className="grid grid-cols-2 gap-4">
          
          {Array.isArray(posts) && posts.map((post) => (
          <Card key={post.board_id} className="w-full">
            <CardHeader>
              <div className="flex justify-between items-center">
	              <CardTitle>{post.nickname}</CardTitle>
	              <DropdownMenu  post={post} />
              </div>
            </CardHeader>
            <CardContent>
              {post.imgpath && <ImageSlider imgpath={post.imgpath} />}
			  {post.video && <video src={post.video} controls />}
              
              <p className="mt-2">{post.content}</p>
            </CardContent>
            <CardFooter className="flex justify-between text-sm">
              <span>123 Like</span>
              <span>Comment</span>
              <span>Share</span>
            </CardFooter>
          </Card>
          ))}
          
        </div>

      </div>
      
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

export default Mypage;