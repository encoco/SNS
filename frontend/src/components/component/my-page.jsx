import React, { useState, useEffect } from 'react';
import ImageSlider from './ImageSlider'; // ImageSlider 컴포넌트를 import
import api from "../../api";
import { Link, useNavigate } from "react-router-dom";
import { Button } from "./ui/button"
import { CardTitle, CardHeader, CardContent, CardFooter, Card } from "./ui/card"
import axios from 'axios';
import { useAuth } from '../../contexts/AuthContext'; // 경로는 실제 구조에 맞게 조정해야 함
import Sidebar from "./ui/Sidebar";
import DropdownMenu from './ui/DropdownMenu';
import Comment from './ui/Comment';

function Mypage() {
	const [showTopBtn, setShowTopBtn] = useState(false);
	const [posts, setPosts] = useState([]);
	const navigate = useNavigate();
	const [likesCount, setLikesCount] = useState([]);
	const [showModal, setShowModal] = useState(false);
	const [currentComments, setCurrentComments] = useState([]);
	const [boardId, setBoardId] = useState([]);

	useEffect(() => {
		const fetchPosts = async () => {
			try {
				const response = await api.get(`/boardList`, {
					withCredentials: true,
				});
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
	}, [navigate]);

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

	// 맨 위로 스크롤하는 함수
	const scrollToTop = () => {
		window.scrollTo({
			top: 0,
			behavior: 'smooth' // 부드러운 스크롤
		});
	};

	const dummyComments = [
		{ nickname : "aaa", img : "/placeholder.svg" , comment: "Great post!" },
		{ nickname : "bbb" , img : "/placeholder.svg", comment: "Thanks for sharing!" },
		{ nickname : "bbb" , img : "/placeholder.svg", comment: "Thanks for sharing!" },
		{ nickname : "bbb" , img : "/placeholder.svg", comment: "Thanks for sharing!" },
		{ nickname : "bbb" , img : "/placeholder.svg", comment: "Thanks for sharing!" },
		{ nickname : "bbb" , img : "/placeholder.svg", comment: "Thanks for sharing!" },
		{ nickname : "bbb" , img : "/placeholder.svg", comment: "Thanks for sharing!" },
		{ nickname : "bbb" , img : "/placeholder.svg", comment: "Thanks for sharing!" },
		{ nickname : "bbb" , img : "/placeholder.svg", comment: "Thanks for sharing!" },
		{ nickname : "bbb" , img : "/placeholder.svg", comment: "Thanks for sharing!" },
		{ nickname : "bbb" , img : "/placeholder.svg", comment: "Thanks for sharing!" },
		{ nickname : "bbb" , img : "/placeholder.svg", comment: "Thanks for sharing!" },
		{ nickname : "bbb" , img : "/placeholder.svg", comment: "Thanks for sharing!" },
		{ nickname : "bbb" , img : "/placeholder.svg", comment: "Thanks for sharing!" },
		{ nickname : "bbb" , img : "/placeholder.svg", comment: "Thanks for sharing!" },
		{ nickname : "bbb" , img : "/placeholder.svg", comment: "Thanks for sharing!" },
		{ nickname : "bbb" , img : "/placeholder.svg", comment: "Thanks for sharing!" }
	];
	return (
		<div className="flex min-h-screen bg-gray-100">

			<Sidebar />

			<div className="flex flex-col w-full ml-10">
				<h1 className="text-3xl font-bold mb-5">마이페이지</h1>
				<div className="grid grid-cols-2 gap-4">

					{Array.isArray(posts) && posts.map((post) => (
						<Card key={post.board_id} className="w-full">
							<CardHeader>
								<div className="flex items-center">
									<img
										alt="Profile"
										className="w-8 h-8 rounded-full mr-2"
										src={"/placeholder.svg"}
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
											setBoardId(post.board_id);
										}}
									>
										댓글
									</button>
									<button className="w-16 h-8">공유하기</button>
								</div>
							</CardFooter>
						</Card>
					))}
				</div>
			</div>

			<Comment isOpen={showModal} onClose={() => setShowModal(false)} comments={currentComments} board_id={boardId} />
			
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