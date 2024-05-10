import { Button } from "./ui/button"
import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from "react-router-dom";
import { Input } from "./ui/input"
import axios from 'axios';
import { useAuth } from '../../contexts/AuthContext'; // 경로는 실제 구조에 맞게 조정해야 함
import api from "../../api";
import ImageSlider from './ImageSlider'; // ImageSlider 컴포넌트를 import
import { AvatarImage, AvatarFallback, Avatar } from "./ui/avatar"
import Sidebar from "./ui/Sidebar";
import DropdownMenu from './ui/DropdownMenu';

export default function Component() {
	const [showTopBtn, setShowTopBtn] = useState(false);
	const navigate = useNavigate();
	const { logout } = useAuth();
	const [posts, setPosts] = useState([]);
	const [searchTerm, setSearchTerm] = useState('');
	const [searchResults, setSearchResults] = useState([]);
	// 글 목록 받아오기
	useEffect(() => {
		const fetchPosts = async () => {
			try {
				const response = await api.get(`/boardList`, {
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

	useEffect(() => {
		const fetchSearchResults = async () => {
			try {
				const response = await api.get(`/search`, {
					params: {
						searchTerm: searchTerm
					},
					withCredentials: true
				});
				setSearchResults(response.data);
			} catch (error) {
				console.error('Error fetching search results:', error);
			}
		};

		// 검색어가 변경될 때만 검색 요청을 보냅니다.
		if (searchTerm.trim() !== '') {
			fetchSearchResults();
		} else {
			// 검색어가 없으면 검색 결과를 초기화합니다.
			setSearchResults([]);
		}
	}, [searchTerm]);
	const handleChange = (event) => {
		setSearchTerm(event.target.value);
	};
	const handleSubmit = (event) => {
		event.preventDefault(); // 폼 제출을 방지하여 엔터키로 검색이 실행되지 않도록 합니다.
	};
	
	const LikeHandler = async (boardId) => {
		try {
			const formData = new FormData();
			formData.append('boardId', boardId);
			const response = await api.get(`/boardLike?boardId=${boardId}`, {
				withCredentials: true,
			});
			if (response.data === "success") {
				alert('좋아요 추가');
			}
			else if (response.data === "fail") {
				alert('좋아요 삭제');
			}
		} catch (error) {
			console.log(error);
			alert('다시 시도해주세요');
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
		/*상단목록*/
		<div className="flex min-h-screen bg-gray-100">
			<Sidebar />
			<div className="flex flex-col w-full ml-10 relative">
				<div className="flex justify-center items-center flex-col w-full relative">
					<form className="flex items-center gap-2 w-full max-w-md mt-5 relative" onSubmit={handleSubmit}>
						<SearchIcon className="h-5 w-5" />
						<input
							className="w-full h-10 px-1 font-normal rounded-none dark:placeholder-gray-400"
							type="search"
							value={searchTerm}
							onChange={handleChange}
							placeholder="검색어를 입력하세요..."
						/>
						<Button className="square-8" size="icon" variant="ghost">
							<BellIcon className="h-4 w-4" />
							<span className="sr-only">알림 토글</span>
						</Button>
					</form>
					{searchResults.length > 0 && (
						<div className="absolute top-full left-1/2 w-full max-w-md bg-white shadow-md rounded-md z-10 transform -translate-x-1/2">
							{searchResults.map(user => (
								<Link to={`/UserPage/${user.id}`} key={user.id} className="block">
									<div className="flex items-center space-x-4 p-2 hover:bg-gray-200 cursor-pointer">
										<img
											alt="s"
											className="w-8 h-8 rounded-full"
											src={user.img || "/placeholder.svg"}
										/>
										<span>{user.nickname}</span>
									</div>
								</Link>
							))}
						</div>
					)}
				</div>



				{/*여기부터 글*/}
				<div className="flex-grow">
					<div className="grid grid-cols-1 gap-4 p-4">
						<div className="grid gap-4 w-full max-w-4xl mx-auto">
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
										<button className="w-10 h-8" onClick={() => LikeHandler(post.board_id)}>Like</button>
										<button className="w-16 h-8">Comment</button>
										<button className="w-16 h-8">Share</button>
									</div>
								</div>
							))}
						</div>
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
		</div>
	);
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