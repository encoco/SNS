import React, { useState, useEffect } from 'react';
import ImageSlider from './ImageSlider'; // ImageSlider 컴포넌트를 import
import api from "../../api";
import { Link, useNavigate, useParams } from "react-router-dom";
import { Button } from "./ui/button"
import { CardTitle, CardHeader, CardContent, CardFooter, Card } from "./ui/card"
import axios from 'axios';
import { useAuth } from '../../contexts/AuthContext'; // 경로는 실제 구조에 맞게 조정해야 함
import Sidebar from "./ui/Sidebar";
import DropdownMenu from './ui/DropdownMenu';
import Comment from './ui/Comment';
function UserPage() {
	const [showTopBtn, setShowTopBtn] = useState(false);
	const [posts, setPosts] = useState([]);
	const [likesCount, setLikesCount] = useState([]);
	const navigate = useNavigate();
	const { userId } = useParams(); // URL에서 userId 추출
	const { logout } = useAuth();
	const [userInfo, setUserInfo] = useState(null);
	const [isFollowing, setIsFollowing] = useState(false);
	const [showModal, setShowModal] = useState(false);
	const [currentComments, setCurrentComments] = useState([]);
	const dummyComments = [
		{ nickname: "aaa", img: "/placeholder.svg", comment: "Great post!" },
		{ nickname: "bbb", img: "/placeholder.svg", comment: "Thanks for sharing!" },
		{ nickname: "bbb", img: "/placeholder.svg", comment: "Thanks for sharing!" },
		{ nickname: "bbb", img: "/placeholder.svg", comment: "Thanks for sharing!" },
		{ nickname: "bbb", img: "/placeholder.svg", comment: "Thanks for sharing!" },
		{ nickname: "bbb", img: "/placeholder.svg", comment: "Thanks for sharing!" },
		{ nickname: "bbb", img: "/placeholder.svg", comment: "Thanks for sharing!" },
		{ nickname: "bbb", img: "/placeholder.svg", comment: "Thanks for sharing!" },
		{ nickname: "bbb", img: "/placeholder.svg", comment: "Thanks for sharing!" },
		{ nickname: "bbb", img: "/placeholder.svg", comment: "Thanks for sharing!" },
		{ nickname: "bbb", img: "/placeholder.svg", comment: "Thanks for sharing!" },
		{ nickname: "bbb", img: "/placeholder.svg", comment: "Thanks for sharing!" },
		{ nickname: "bbb", img: "/placeholder.svg", comment: "Thanks for sharing!" },
		{ nickname: "bbb", img: "/placeholder.svg", comment: "Thanks for sharing!" },
		{ nickname: "bbb", img: "/placeholder.svg", comment: "Thanks for sharing!" },
		{ nickname: "bbb", img: "/placeholder.svg", comment: "Thanks for sharing!" },
		{ nickname: "bbb", img: "/placeholder.svg", comment: "Thanks for sharing!" }
	];

	useEffect(() => {
		const fetchPosts = async () => {
			try {
				// userId를 사용하여 사용자의 게시물을 가져옴
				const response = await api.get(`/userPosts?userId=${userId}`, {
					withCredentials: true,
				});
				setUserInfo(response.data.userInfo);
				setPosts(response.data.posts);

				const likesCount = response.data.likes.reduce((acc, like) => {
					acc[like.board_id] = (acc[like.board_id] || 0) + 1;
					return acc;
				}, {});

				setLikesCount(likesCount);
			} catch (error) {
				console.log(error);
			}
		};
		fetchPosts();
	}, [userId]);

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

	const handleFollow = async () => {
		try {
			const method = isFollowing ? 'delete' : 'post';
			const response = await api.get(`/userFollow?userId=${userId}`, {
				withCredentials: true,
			});
			if (response.status === 200) {
				setIsFollowing(!isFollowing); // 상태 토글
			} else {
				console.error('Follow action failed:', response);
			}
		} catch (error) {
			console.error('Follow action error:', error);
		}
	};

	// 맨 위로 스크롤하는 함수
	const scrollToTop = () => {
		window.scrollTo({
			top: 0,
			behavior: 'smooth' // 부드러운 스크롤
		});
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
				setLikesCount(prevLikesCount => ({
					...prevLikesCount,
					[boardId]: Math.max((prevLikesCount[boardId] || 0) + 1, 0)
				}));
			}
			else if (response.data === "fail") {
				alert('좋아요 삭제');
				setLikesCount(prevLikesCount => ({
					...prevLikesCount,
					[boardId]: Math.max((prevLikesCount[boardId] || 0) - 1, 0)
				}));
			}
		} catch (error) {
			console.log(error);
			alert('다시 시도해주세요');
		}
	};

	return (
		<div className="flex min-h-screen bg-gray-100">
			<Sidebar />
			<div className="flex flex-col w-full ml-10">
				{userInfo && (
					<div className="flex items-center mb-5">
						<img
							alt="Profile"
							className="w-8 h-8 rounded-full mr-2"
							src={userInfo.img || "/placeholder.svg"}
						/>
						<h2 className="text-2xl">{userInfo.nickname}</h2>
						{localStorage.getItem('nickname') !== userInfo.nickname && (
							<button
								onClick={handleFollow}
								className={`ml-5 px-3 py-1 text-sm font-medium rounded transition duration-150 ease-in-out ${isFollowing ? 'bg-red-500 hover:bg-red-700 text-white' : 'bg-black hover:bg-green-600 text-white'
									}`}
							>
								{isFollowing ? '언팔로우' : '팔로우'}
							</button>
						)}
					</div>
				)}

				{posts.length === 0 ? ( // 글이 없는 경우 메시지를 표시합니다
					<div className="col-span-2 flex justify-center items-center h-full">
						<p className="text-xl">작성한 글이 없습니다.</p>
					</div>
				) : ( // 글이 있는 경우 카드를 표시합니다
					<div className="grid grid-cols-2 gap-4">
						{posts.map((post) => (
							<Card key={post.board_id} className="w-full">
								<CardHeader>
									<div className="flex items-center">
										<img
											alt="Profile"
											className="w-8 h-8 rounded-full mr-2"
											src={userInfo.img || "/placeholder.svg"}
										/>
										<div className="flex-grow"> {/* 이 부분이 추가됨 */}
											<CardTitle>{post.nickname}</CardTitle>
										</div>
										<DropdownMenu post={post} />
									</div>
								</CardHeader>
								<CardContent>
									{post.imgpath && <ImageSlider imgpath={post.imgpath} />}
									{post.video && <video src={post.video} controls />}
									<p className="mt-2">{post.content}</p>
								</CardContent>
								<CardFooter className="flex justify-between text-sm">
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
												setCurrentComments(dummyComments); // 현재 댓글 설정
												setShowModal(true); // 모달 보이기
											}}
										>
											댓글
										</button>
										<button className="w-16 h-8">공유</button>
									</div>
								</CardFooter>
							</Card>
						))}
					</div>
				)}
			</div>
			
			<Comment isOpen={showModal} onClose={() => setShowModal(false)} comments={currentComments} />
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

export default UserPage;