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
import Comment from './ui/Comment';
import { BrowserView, MobileView } from 'react-device-detect';

export default function Component() {
	const [showTopBtn, setShowTopBtn] = useState(false);
	const navigate = useNavigate();
	const { logout } = useAuth();
	const [posts, setPosts] = useState([]);
	const [searchTerm, setSearchTerm] = useState('');
	const [searchResults, setSearchResults] = useState([]);
	const [likesCount, setLikesCount] = useState([]);
	const [showModal, setShowModal] = useState(false);
	const [currentComments, setCurrentComments] = useState([]);
	const [selectedPostId, setSelectedPostId] = useState(null);
	const [userLikes, setUserLikes] = useState(new Set()); 
	const [profile, setProfile] = useState('');
	
	
	useEffect(() => {
		// userInfo에서 nickname을 추출하여 상태에 저장
		const userInfoJSON = localStorage.getItem('nickname');
		if (userInfoJSON) {
			const userInfo = JSON.parse(userInfoJSON);
			setProfile(userInfo);
		}
	}, []); 
	
	// 글 목록 받아오기
	useEffect(() => {
		const fetchPosts = async () => {
			try {
				// userId를 사용하여 사용자의 게시물을 가져옴
				const response = await api.get(`/mainboardList`, {
					withCredentials: true,
				});
				setPosts(response.data.posts);

				const likesCount = response.data.likes.reduce((acc, like) => {
					acc[like.board_id] = (acc[like.board_id] || 0) + 1;
					return acc;
				}, {});

				setLikesCount(likesCount);

				// 사용자가 좋아요를 누른 게시물 ID를 Set으로 저장
				const userLikes = new Set(response.data.likes.filter(like => like.id === parseInt(profile.id)).map(like => like.board_id));
				setUserLikes(userLikes);

			} catch (error) {
				console.log(error);
			}
		};
		fetchPosts();
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
				console.log(response.data);
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
	
	// 댓글 버튼 클릭 시 해당 게시물의 board_id 설정
	const handleCommentButtonClick = async (boardId) => {
		setSelectedPostId(boardId);
		try {
			const response = await api.get(`/getComments`, {
				params: { boardId },
				withCredentials: true,
			});
			console.log(response.data);
			setCurrentComments(response.data);
		} catch (error) {
			console.error('Error fetching comments:', error);
		}
	};

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
	
	const handleChange = (event) => {
		setSearchTerm(event.target.value);
	};
	const handleSubmit = (event) => {
		event.preventDefault(); // 폼 제출을 방지하여 엔터키로 검색이 실행되지 않도록 합니다.
	};

	const LikeHandler = async (boardId) => {
		try {
			const response = await api.get(`/boardLike?boardId=${boardId}`, {
				withCredentials: true,
			});
			if (response.data === "success") {
				alert('좋아요 추가');
				setLikesCount(prevLikesCount => ({
					...prevLikesCount,
					[boardId]: Math.max((prevLikesCount[boardId] || 0) + 1, 0)
				}));
				setUserLikes(prevLikes => new Set(prevLikes).add(boardId)); // 좋아요 추가
			}
			else if (response.data === "fail") {
				alert('좋아요 삭제');
				setLikesCount(prevLikesCount => ({
					...prevLikesCount,
					[boardId]: Math.max((prevLikesCount[boardId] || 0) - 1, 0)
				}));
				setUserLikes(prevLikes => {
					const newLikes = new Set(prevLikes);
					newLikes.delete(boardId); // 좋아요 삭제
					return newLikes;
				});
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
		<div className="app">
			{/* 데스크톱 화면 */}
			<BrowserView>
				<div className="grid min-h-screen w-full grid-cols-[280px_1fr] flex min-h-screen bg-gray-100">
					<Sidebar />
					<div className="flex flex-col w-full relative">
						<div className="flex justify-center items-center flex-col w-full relative">
							<form className="flex items-center gap-2 w-full max-w-md mt-5 relative" onSubmit={handleSubmit}>
								<SearchIcon className="h-5 w-5" />
								<input
									className="w-full h-10 px-1 font-normal rounded-none dark:placeholder-gray-400 "
									type="search"
									value={searchTerm}
									onChange={handleChange}
									placeholder="검색어를 입력하세요..."
								/>
							</form>
							{searchResults.length > 0 && (
								<div className="absolute top-full left-1/2 w-full max-w-md bg-white shadow-md rounded-md z-10 transform -translate-x-1/2">
									{searchResults.map(user => (
										<Link to={`/UserPage/${user.id}`} key={user.id} className="block">
											<div className="flex items-center space-x-4 p-2 hover:bg-gray-200 cursor-pointer">
												<img
													alt="s"
													className="w-8 h-8 rounded-full"
													src={user.profile_img || "/placeholder.svg"}
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
														  src={post.profile_img ? post.profile_img : "/placeholder.svg"} 
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
												<button
													className="w-10 h-8"
													style={{ color: userLikes.has(post.board_id) ? "red" : "inherit" }} // 좋아요 상태에 따라 색상 변경
													onClick={() => LikeHandler(post.board_id)}
												>
													{likesCount[post.board_id] > 0 ? `${likesCount[post.board_id]} ❤` : '0 ❤'}
												</button>
												<button
													className="w-16 h-8"
													onClick={() => {
														handleCommentButtonClick(post.board_id);
														setShowModal(true); // 모달 보이기
													}}>
													댓글
												</button>
												<button className="w-16 h-8">공유하기</button>
											</div>
										</div>
									))}
								</div>
							</div>
						</div>

						<Comment isOpen={showModal} onClose={() => setShowModal(false)} comments={currentComments} boardId={selectedPostId} />
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
			</BrowserView >


			{/* 모바일 화면 */}
			<MobileView>
				<div className="flex flex-col w-full relative">
					<Sidebar />
					<div className="flex justify-center items-center flex-col w-full relative">
						{/* 상단 목록 및 검색 폼 */}
						<form className="flex items-center justify-center w-full max-w-md mt-5 relative" onSubmit={handleSubmit}>
							<input
								className="w-3/4 md:w-full h-8 md:h-10 px-1 font-normal rounded-none dark:placeholder-gray-400 bg-gray-100"
								type="search"
								value={searchTerm}
								onChange={handleChange}
								placeholder="검색어를 입력하세요..."
								style={{ textAlign: "center" }}
							/>
						</form>
						{searchResults.length > 0 && (
							<div className="absolute top-full left-1/2 w-full max-w-md bg-white shadow-md rounded-md z-10 transform -translate-x-1/2">
								{/* 검색 결과 목록 */}
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


					{/* 글 목록 부분 */}
					<div className="flex-grow mb-20">
						<div className="p-4">
							{Array.isArray(posts) && posts.map((post) => (
								<div key={post.board_id} className="rounded-xl bg-white p-4 border border-gray-100 dark:border-gray-800 mb-4 relative">
									<div className="flex justify-between items-start mb-2">
										<div className="flex items-center space-x-2">
											<img
												alt="Avatar"
												className="rounded-full"
												src="/placeholder.svg"
												style={{
													aspectRatio: "1 / 1",
													objectFit: "cover",
													zIndex: "1" // 이미지의 z-index 설정
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
										<button
											className="w-10 h-8"
											style={{ color: likesCount[post.board_id] > 0 ? "red" : "inherit" }}
											onClick={() => LikeHandler(post.board_id)}
										>
											{likesCount[post.board_id] > 0 ? `${likesCount[post.board_id]} ❤` : '0 ❤'}
										</button>
										<button
											className="w-16 h-8"
											onClick={() => {
												handleCommentButtonClick(post.board_id);
												setShowModal(true); // 모달 보이기
											}}>
											댓글
										</button>
										<button className="w-16 h-8">공유하기</button>
									</div>
								</div>
							))}
						</div>
					</div>
					{/* 댓글 모달과 최상단으로 이동하는 버튼 */}
					<Comment isOpen={showModal} onClose={() => setShowModal(false)} comments={currentComments} boardId={selectedPostId} />
				</div>
			</MobileView>
		</div >
	);
};


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