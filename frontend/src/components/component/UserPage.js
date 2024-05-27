import React, { useState, useEffect } from 'react';
import ImageSlider from './ImageSlider'; // ImageSlider 컴포넌트를 import
import api from "../../api";
import { useParams } from "react-router-dom";
import { CardTitle, CardHeader, CardContent, CardFooter, Card } from "./ui/card"
import { useAuth } from '../../contexts/AuthContext'; // 경로는 실제 구조에 맞게 조정해야 함
import Sidebar from "./ui/Sidebar";
import DropdownMenu from './ui/DropdownMenu';
import Comment from './ui/Comment';
import EditProfile from './ui/editProfile.js';

function UserPage() {
	const [showTopBtn, setShowTopBtn] = useState(false);
	const [posts, setPosts] = useState([]);
	const [likesCount, setLikesCount] = useState([]);
	const { userId } = useParams(); // URL에서 userId 추출
	const [userInfo, setUserInfo] = useState(null);
	const [isFollowing, setIsFollowing] = useState(false);
	const [showModal, setShowModal] = useState(false);
	const [editProfile, setEditProfile] = useState(false);
	const [currentComments, setCurrentComments] = useState([]);
	const [nickname, setNickname] = useState(''); // 초기 상태를 빈 문자열로 설정
	const [profile, setProfile] = useState('');
	const [userLikes, setUserLikes] = useState(new Set()); 
	
	useEffect(() => {
		// userInfo에서 nickname을 추출하여 상태에 저장
		const userInfoJSON = localStorage.getItem('nickname');
		if (userInfoJSON) {
			const userInfo = JSON.parse(userInfoJSON);
			setNickname(userInfo.nickname); // nickname 상태 업데이트
			setProfile(userInfo);
		}
	}, []); 
	
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

				// 사용자가 좋아요를 누른 게시물 ID를 Set으로 저장
				const userLikes = new Set(response.data.likes.filter(like => like.id === parseInt(userId)).map(like => like.board_id));
				setUserLikes(userLikes);

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

	const handleEditProfile = () => {
		setEditProfile(true);
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
						{nickname !== userInfo.nickname ? (
							<button
								onClick={handleFollow}
								className={`ml-5 px-3 py-1 text-sm font-medium rounded transition duration-150 ease-in-out ${isFollowing ? 'bg-red-500 hover:bg-red-700 text-white' : 'bg-black hover:bg-green-600 text-white'}`}
							>
								{isFollowing ? '언팔로우' : '팔로우'}
							</button>
						) : (
							<button onClick={handleEditProfile} className="ml-5 px-3 py-1 text-sm font-medium rounded transition duration-150 ease-in-out bg-black hover:bg-green-600 text-white">
								내 정보 수정
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
											style={{ color: userLikes.has(post.board_id) ? "red" : "inherit" }} // 좋아요 상태에 따라 색상 변경
											onClick={() => LikeHandler(post.board_id)}
										>
											{likesCount[post.board_id] > 0 ? `${likesCount[post.board_id]} ❤` : '0 ❤'}
										</button>
										<button
											className="w-16 h-8"
											onClick={() => {
												//setCurrentComments(dummyComments); // 현재 댓글 설정
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
			
			<EditProfile Open={editProfile} Close={() => setEditProfile(false)} userInfo={profile}/>
			
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
