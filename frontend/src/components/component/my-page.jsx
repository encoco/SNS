import { AvatarImage, AvatarFallback, Avatar } from "./ui/avatar"
import React, { useState , useEffect} from 'react';
import { Link ,useNavigate} from "react-router-dom";
import { Button } from "./ui/button"
import { CardHeader, CardContent, CardFooter, Card } from "./ui/card"
import axios from 'axios';
import { useAuth } from '../../contexts/AuthContext'; // 경로는 실제 구조에 맞게 조정해야 함


function Mypage() {
	const [isNavExpanded, setIsNavExpanded] = useState(false);
	const { logout } = useAuth();
	const [showTopBtn, setShowTopBtn] = useState(false);
	const navigate = useNavigate();
	const handleLogout = async () => {
	  try {
	    const response = await axios.get('http://localhost:8080/api/Logout');
	   	logout();
	   	navigate('/');
	   	
	  } catch (error) {
		  console.log(error);
	    alert('다시 시도해주세요.');
	  }
   };
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
    <div className="grid md:grid-cols-[300px_1fr] w-full max-w-6xl gap-0 md:gap-0.5 xl:gap-1 rounded-lg border overflow-hidden">
      <div className="flex min-h-screen border-r border-gray-200 dark:border-gray-800">
  		<div className="fixed top-0 bottom-0 z-10" onMouseEnter={() => setIsNavExpanded(true)} onMouseLeave={() => setIsNavExpanded(false)}>
		        {/* 마우스를 올렸을 때 너비가 확장되는 효과를 위한 div */}
		       	<div className={`w-19 h-full bg-black border-r border-gray-200 dark:border-gray-800 transition-width duration-300 ease-in-out ${isNavExpanded ? "w-60" : "w-12"}`}>
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
		              <button className="block h-10 pl-16 pr-4 py-2 font-medium 
		              						rounded-md  hover:text-gray-900 dark:hover:bg-gray-800 dark:hover:text-gray-50
		                text-white" onClick={handleLogout}>로그아웃</button>
		          </nav>
		        </div>
			</div>
	      </div>
	      
      
      
      </div>
      <div className={`flex flex-col w-full mt-16 transition-margin duration-300 ease-in-out`}>
        <header className="flex items-center p-4 border-b border-gray-200 dark:border-gray-800 ">
          <Button className="md:hidden w-10 h-10" variant="icon">
            <MenuIcon className="w-6 h-6" />
            <span className="sr-only">Toggle sidebar</span>
          </Button>
          	<div className="flex-grow">
			  <h1 className="text-right mr-11 text-lg font-semibold">마이 페이지</h1>
			</div>
          <div className="ml-auto flex items-center gap-4">
            <Button className="w-8 h-8" variant="icon">
              <SearchIcon className="w-4 h-4" />
              <span className="sr-only">Search</span>
            </Button>
            <Button className="w-8 h-8" variant="icon">
              <BellIcon className="w-4 h-4" />
              <span className="sr-only">Notifications</span>
            </Button>
            <Button className="w-8 h-8" variant="icon">
              <LogOutIcon className="w-4 h-4" />
              <span className="sr-only">Log out</span>
            </Button>
            <div>
              <div className="mt-1 w-48 py-1 text-sm">
                <div />
                <div />
                <div />
                <div />
              </div>
            </div>
          </div>
        </header>
        <main className="flex-1 overflow-y-auto">
          <div className="grid gap-4 p-4">
            <Card>
              <CardHeader>
                <div className="flex items-center space-x-4">
                  <div className="flex items-center space-x-2">
                    <Avatar className="w-10 h-10 border">
                      <AvatarImage alt="@shadcn" src="/placeholder-user.jpg" />
                      <AvatarFallback>U</AvatarFallback>
                    </Avatar>
                    <div className="text-sm font-medium">글쓴애이름 </div>
                  </div>
                </div>
              </CardHeader>
              <CardContent>
                <div className="prose dark:prose-dark">
                  <p>
                    내용내용냉ㅇ내용📸🏞️🍲
                  </p>
                </div>
              </CardContent>
              <CardFooter>
                <div className="flex items-center space-x-4">
                  <Button size="xs" variant="ghost">
                    <HeartIcon className="w-3 h-3 mr-1.5" />
                    Like
                  </Button>
                  <Button size="xs" variant="ghost">
                    <MessageCircleIcon className="w-3 h-3 mr-1.5" />
                    Comment
                  </Button>
                  <Button size="xs" variant="ghost">
                    <ShareIcon className="w-3 h-3 mr-1.5" />
                    Share
                  </Button>
                  <div className="ml-auto flex items-center space-x-2 text-xs">
                    <ClockIcon className="w-3 h-3 opacity-70" />
                    <time>2m</time>
                  </div>
                </div>
              </CardFooter>
            </Card>
            <Card>
              <CardHeader>
                <div className="flex items-center space-x-2">
                  <Avatar className="border">
                    <AvatarImage alt="@shadcn" src="/placeholder-user.jpg" />
                    <AvatarFallback>U</AvatarFallback>
                  </Avatar>
                  <div className="text-sm font-medium">Olivia Davis</div>
                </div>
              </CardHeader>
              <CardContent>
                <img
                  alt="Image"
                  className="aspect-video overflow-hidden rounded-lg object-cover"
                  height="225"
                  src="/placeholder.svg"
                  width="400"
                />
                sadiphsaoidbsaofbasofabsboi
              </CardContent>
              <CardFooter>
                <div className="flex items-center space-x-4">
                  <Button size="xs" variant="ghost">
                    <HeartIcon className="w-3 h-3 mr-1.5" />
                    Like
                  </Button>
                  <Button size="xs" variant="ghost">
                    <MessageCircleIcon className="w-3 h-3 mr-1.5" />
                    Comment
                  </Button>
                  <Button size="xs" variant="ghost">
                    <ShareIcon className="w-3 h-3 mr-1.5" />
                    Share
                  </Button>
                  <div className="ml-auto flex items-center space-x-2 text-xs">
                    <ClockIcon className="w-3 h-3 opacity-70" />
                    <time>2m</time>
                  </div>
                </div>
              </CardFooter>
            </Card>
          </div>
        </main>
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