import React, { useState , useEffect} from 'react';
import { Link ,useNavigate} from "react-router-dom";
import { Button } from "./ui/button"
import { CardTitle,CardHeader, CardContent, CardFooter, Card } from "./ui/card"
import axios from 'axios';
import { useAuth } from '../../contexts/AuthContext'; // 경로는 실제 구조에 맞게 조정해야 함
import  Sidebar from "./ui/Sidebar";
import DropdownMenu from './ui/DropdownMenu';

function Mypage() {
	const [isNavExpanded, setIsNavExpanded] = useState(false);
	const [showTopBtn, setShowTopBtn] = useState(false);
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
          <Card className="w-full">
            <CardHeader>
              <div className="flex justify-between items-center">
	              <CardTitle>Olivia Zabis</CardTitle>
	              <DropdownMenu 
	                onEdit={() => console.log('Edit clicked')}
	                onDelete={() => console.log('Delete clicked')}
	              />
              </div>
            </CardHeader>
            <CardContent>
              <img
                alt="Content Image"
                className="w-full h-auto bg-gray-300"
                height="200"
                src="/placeholder.svg"
                style={{
                  aspectRatio: "300/200",
                  objectFit: "cover",
                }}
                width="300"
              />
              <p className="mt-2">sdjapio2j3iodsjio</p>
            </CardContent>
            <CardFooter className="flex justify-between text-sm">
              <span>Like</span>
              <span>Comment</span>
              <span>Share</span>
            </CardFooter>
          </Card>
          
          
          <Card className="w-full">
            <CardHeader>
              <div className="flex justify-between items-center">
	              <CardTitle>Olivia Zabis</CardTitle>
	              <DropdownMenu 
	                onEdit={() => console.log('Edit clicked')}
	                onDelete={() => console.log('Delete clicked')}
	              />
              </div>
            </CardHeader>
            <CardContent>
              <img
                alt="Content Image"
                className="w-full h-auto bg-gray-300"
                height="200"
                src="/placeholder.svg"
                style={{
                  aspectRatio: "300/200",
                  objectFit: "cover",
                }}
                width="300"
              />
              <p className="mt-2">sdjapio2j3iodsjio</p>
            </CardContent>
            <CardFooter className="flex justify-between text-sm">
              <span>Like</span>
              <span>Comment</span>
              <span>Share</span>
            </CardFooter>
          </Card>
          <Card className="w-full">
            <CardHeader>
              <div className="flex justify-between items-center">
	              <CardTitle>Olivia Zabis</CardTitle>
	              <DropdownMenu 
	                onEdit={() => console.log('Edit clicked')}
	                onDelete={() => console.log('Delete clicked')}
	              />
              </div>
            </CardHeader>
            <CardContent>
              <img
                alt="Content Image"
                className="w-full h-auto bg-gray-300"
                height="200"
                src="/placeholder.svg"
                style={{
                  aspectRatio: "300/200",
                  objectFit: "cover",
                }}
                width="300"
              />
              <p className="mt-2">sdjapio2j3iodsjio</p>
            </CardContent>
            <CardFooter className="flex justify-between text-sm">
              <span>Like</span>
              <span>Comment</span>
              <span>Share</span>
            </CardFooter>
          </Card>
          <Card className="w-full">
            <CardHeader>
              <div className="flex justify-between items-center">
	              <CardTitle>Olivia Zabis</CardTitle>
	              <DropdownMenu 
	                onEdit={() => console.log('Edit clicked')}
	                onDelete={() => console.log('Delete clicked')}
	              />
              </div>
            </CardHeader>
            <CardContent>
              <img
                alt="Content Image"
                className="w-full h-auto bg-gray-300"
                height="200"
                src="/placeholder.svg"
                style={{
                  aspectRatio: "300/200",
                  objectFit: "cover",
                }}
                width="300"
              />
              <p className="mt-2">sdjapio2j3iodsjio</p>
            </CardContent>
            <CardFooter className="flex justify-between text-sm">
              <span>Like</span>
              <span>Comment</span>
              <span>Share</span>
            </CardFooter>
          </Card>
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


function ChevronDownIcon(props) {
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
      <path d="m6 9 6 6 6-6" />
    </svg>
  )
}


function ClockIcon(props) {
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
      <circle cx="12" cy="12" r="10" />
      <polyline points="12 6 12 12 16 14" />
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


function LogOutIcon(props) {
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
      <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4" />
      <polyline points="16 17 21 12 16 7" />
      <line x1="21" x2="9" y1="12" y2="12" />
    </svg>
  )
}


function MenuIcon(props) {
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
      <line x1="4" x2="20" y1="12" y2="12" />
      <line x1="4" x2="20" y1="6" y2="6" />
      <line x1="4" x2="20" y1="18" y2="18" />
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
export default Mypage;