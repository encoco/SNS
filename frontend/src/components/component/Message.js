import { Link } from "react-router-dom";
import Sidebar from "./ui/Sidebar";
import Together from "./Together";
import { Button } from "./ui/button";
import { Input } from "./ui/input";
import { AvatarImage, AvatarFallback, Avatar } from "./ui/avatar";
import React, { useState, useEffect, useRef } from 'react';
import api from "../../api";
import Modal from 'react-modal';
import Search from './ui/search';
import { Stomp } from "@stomp/stompjs";
import webSocketService from '../../services/WebSocketService'; // WebSocketService 경로를 확인하세요
import { BrowserView, MobileView } from 'react-device-detect';

function Message() {
	const [activeView, setActiveView] = useState('chat');
	const [chatroom, setChatRoom] = useState([]);
	const [selectedChat, setSelectedChat] = useState('');
	const [messages, setMessages] = useState([]);
	const [inputMessage, setInputMessage] = useState('');
	const [showModal, setShowModal] = useState(false);
	const stompClient = useRef(null);
	const [selectedRoom, setSelectedRoom] = useState(null);
	const [isChatOptionsVisible, setIsChatOptionsVisible] = useState(false);
	const [showRooms, setShowRooms] = useState(false);
	const [groupRooms, setGroupRooms] = useState(false);
	const [selectedRoomName, setSelectedRoomName] = useState(false);

	const [nickname, setNickname] = useState(''); // 초기 상태를 빈 문자열로 설정
	const [img, setImg] = useState('');
	const [id, setId] = useState('')

	useEffect(() => {
		// userInfo에서 nickname을 추출하여 상태에 저장
		const userInfoJSON = localStorage.getItem('nickname');
		if (userInfoJSON) {
			const userInfo = JSON.parse(userInfoJSON);
			setNickname(userInfo.nickname); // nickname 상태 업데이트
			setImg(userInfo.img);
			setId(userInfo.id);
		}
	}, []); // 의존성 배열을 빈 배열로 설정하여 컴포넌트 마운트 시에만 실행


	useEffect(() => {
		const fetchChatrooms = async () => {
			try {
				const response = await api.get(`/selectRoom`, {
					withCredentials: true,
				});
				setChatRoom(response.data === "채팅방 없음" ? [] : response.data);
			} catch (error) {
				console.log(error);
			}
		};
		fetchChatrooms();
	}, []);

	useEffect(() => {
		const connectWebSocket = () => {
			webSocketService.connect(() => {
				console.log("Connected to WebSocket server");
				if (activeView == 'chat') {
					webSocketService.subscribe(`/api/sub/chat/${selectedChat}`, (message) => {
						setMessages(prevMessages => [...prevMessages, message]);
					});
				}
				else if ('group') {
					webSocketService.subscribe(`/api/sub/commChat/${selectedChat}`, (message) => {
						console.log("socketconnect : ",message);
						setMessages(prevMessages => [...prevMessages, message]);
					});
				}
			});
		};
		if (selectedChat) {
			connectWebSocket();
			return () => {
				if (webSocketService.client) {
					webSocketService.client.deactivate();
				}
			};
		}
	}, [selectedChat]);

	const handleSendMessage = (event) => {
		event.preventDefault();
		if (inputMessage.trim()) {
			if (activeView == 'group') {
				const message = {
					content: inputMessage,
					nickname: nickname
				};
				webSocketService.send(`/api/pub/commChat/${selectedChat}`, message, localStorage.getItem("userInfo"));
			}
			else if (activeView == 'chat') {
				const message = {
					content: inputMessage,
					nickname: nickname
				};
				webSocketService.send(`/api/pub/chat/${selectedChat}`, message, localStorage.getItem("userInfo"));

			}
			else { return; }
			setInputMessage('');
		}
	};

	const toggleChatOptions = () => {
		setIsChatOptionsVisible(!isChatOptionsVisible);
	};

	const handleCommChat = async (room) => {
		console.log('room이다',room);
		setSelectedRoomName(room.roomname);
		setSelectedRoom(room.ccjId);
		setSelectedChat(room.roomNumber);
		setActiveView('group');

		try {
			setMessages([]);// 채팅방 변경 시 메시지 초기화
			const response = await api.get(`/getCommMessage`, {
				params: { communitychat_id: room.roomNumber },
				withCredentials: true,
			});
			console.log("getCMessage",response.data);
			setMessages(response.data);
		} catch (error) {
			console.error('Error fetching comments:', error);
		}
	};

	const handleChat = async () => {
		setActiveView('chat');
		setSelectedRoomName("");
		setSelectedRoom("");
		setSelectedChat("");
		console.log(activeView);
		try {
			const response = await api.get(`/selectRoom`, {
				withCredentials: true,
			});
			setChatRoom(response.data === "채팅방 없음" ? [] : response.data);
		} catch (error) {
			console.log(error);
		}

	}

	const handleGroup = async () => {
		setActiveView('group');
		setSelectedRoomName("");
		setSelectedRoom("");
		setSelectedChat("");
		try {
			const response = await api.get(`/selectCommuRoom`, {
				withCredentials: true,
			});
			// 배열 데이터 검증 후 상태 업데이트
			if (Array.isArray(response.data)) {
				setChatRoom(response.data);
			} else {
				setChatRoom([]); // 비정상 응답 처리
			}
		} catch (error) {
			setChatRoom([]); // 오류 시 빈 배열로 초기화
		}
	}

	const handleChatSelect = async (room) => {
		try {
			setMessages([]);// 채팅방 변경 시 메시지 초기화
			const response = await api.get(`/getMessage`, {
				params: { roomNumber: room.roomNumber },
				withCredentials: true,
			});
			setMessages(response.data);
		} catch (error) {
			console.error('Error fetching comments:', error);
		}
		setSelectedChat(room.roomNumber);
	};

	const handleChange = (room) => {
		setSelectedChat(room.roomNumber);
	};

	const handleNewRoomCreated = (newRoom) => {
		handleChat();
		setSelectedChat(newRoom.roomNumber); // 새 채팅방 선택
	};

	return (
		<div className="app">
			<BrowserView>
				<div className="grid min-h-screen w-full grid-cols-[280px_1fr]">
					<div className=" border-r ">
						<Sidebar />
					</div>
					<div className="flex flex-col">
						<header className="flex h-14 lg:h-[60px] items-center gap-4 border-b bg-gray-100/40 px-6 dark:bg-gray-800/40">
							<div className="w-full flex-1">
								<Input
									className="w-full bg-white shadow-none appearance-none pl-8 md:w-2/3 lg:w-1/3 dark:bg-gray-950"
									placeholder="채팅 검색하기"
									type="search"
								/>
							</div>

							<div className="flex h-[60px] items-center border-b px-6">
								<Link className="flex items-center gap-2 font-semibold" href="#">
									<MessageCircleIcon className="h-6 w-6" />
									<span className="">채팅방</span>
								</Link>
							</div>
						</header>
						<main className="flex-1 flex flex-col">
							<div className="flex-1 flex">
								{activeView === 'chat' ? (
									// 채팅 화면 컨텐츠
									<div className="flex-1 flex flex-col">
										<div className="flex-1 overflow-y-auto py-2 max-h-[calc(100vh-135px)] p-6">
											<div className="grid gap-4">
												{/* 채팅 메시지 출력 영역 */}
												{/* 채팅 예제 */}
												{selectedChat && messages && messages.length > 0 ? messages.map(message => (
													<div key={message.message_id} className={`flex items-start gap-4 ${message.nickname === nickname ? 'justify-end' : ''}`}>
														<Avatar>
															<AvatarImage alt={message.name} src="/placeholder-user.jpg"
																style={{
																	width: '50px',
																	height: '50px',
																	objectFit: 'cover'
																}}
															/>
															<AvatarFallback>{message.room_number[0]}</AvatarFallback>
														</Avatar>
														<div className="max-w-[40%]">
															<div className={`rounded-lg p-4 text-sm ${message.nickname === nickname ? 'bg-blue-500 text-white' : 'bg-gray-100 dark:bg-gray-800'}`}>
																<p>{message.content}</p>
															</div>
															<div className="text-xs text-gray-500 dark:text-gray-400">{message.date}</div>
														</div>
													</div>
												)) : (
													<div className="flex justify-center items-center h-full">
														<p className="text-gray-500">채팅을 시작하세요</p>
													</div>
												)}

											</div>
										</div>
										<div className="border-t px-6 py-4">
											<form className="flex items-center gap-2" onSubmit={handleSendMessage}>
												<Input className="flex-1" placeholder="메세지를 입력하세요..." type="text" value={inputMessage} onChange={e => setInputMessage(e.target.value)} />
												<Button size="icon" type="submit" variant="ghost">
													<SendIcon className="h-5 w-5" />
												</Button>
											</form>
										</div>
									</div>
								) : (
									selectedChat ? (
										<div className="flex-1 flex flex-col">
											{/* 채팅 메시지 출력 영역 */}
											<div className="flex-1 overflow-y-auto py-2 max-h-[calc(100vh-135px)] p-6">
												<div className="grid gap-4">
													{messages && messages.length > 0 ? (
														messages.map(message => (
															<div key={message.commessage_id} className={`flex items-start gap-4 ${message.nickname === nickname ? 'justify-end' : ''}`}>
																<Avatar>
																	<AvatarImage alt={message.nickname} src="/placeholder-user.jpg" style={{ width: '50px', height: '50px', objectFit: 'cover' }} />
																	<AvatarFallback>{message.communitychat_id}</AvatarFallback>
																</Avatar>
																<div className="max-w-[40%]">
																	<div className={`rounded-lg p-4 text-sm ${message.nickname === nickname ? 'bg-blue-500 text-white' : 'bg-gray-100 dark:bg-gray-800'}`}>
																		<p>{message.content}</p>
																	</div>
																	<div className="text-xs text-gray-500 dark:text-gray-400">{message.date}</div>
																</div>
															</div>
														))
													) : (
														<div className="flex justify-center items-center h-full">
															<p className="text-gray-500">채팅을 시작하세요</p>
														</div>
													)}
												</div>
											</div>
											{/* 메시지 입력란 */}
											<div className="border-t px-6 py-4">
												<form className="flex items-center gap-2" onSubmit={handleSendMessage}>
													<Input className="flex-1" placeholder="메세지를 입력하세요..." type="text" value={inputMessage} onChange={e => setInputMessage(e.target.value)} />
													<Button size="icon" type="submit" variant="ghost">
														<SendIcon className="h-5 w-5" />
													</Button>
												</form>
											</div>
										</div>
									) : (
										// 'Together' 컴포넌트 렌더링
										<div className="flex-1 overflow-auto p-6">
											<div className="grid h-full gap-4">
												<Together />
											</div>
										</div>
									))}


								<div className="w-[300px] border-l bg-gray-100/40 p-6 dark:bg-gray-800/40">
									<div className="flex-1 overflow-y-auto py-2 max-h-[calc(100vh-135px)] p-6"> {/*//여기*/}
										<nav className="grid items-start px-4 text-sm font-medium">
											<Link
												className={`flex items-center gap-3 rounded-lg px-3 py-2 transition-all ${activeView === 'chat'
													? 'bg-gray-200 text-gray-900 hover:text-gray-900 dark:bg-gray-800 dark:text-gray-50 dark:hover:text-gray-50'
													: 'text-gray-500 hover:text-gray-900 dark:text-gray-400 dark:hover:text-gray-50'
													}`}
												href="#"
												onClick={() => handleChat()}
											>
												<MessageCircleIcon className="h-4 w-4" />
												개인채팅방
											</Link>
											<Link
												className={`flex items-center gap-3 rounded-lg px-3 py-2 transition-all ${activeView === 'group'
													? 'bg-gray-200 text-gray-900 hover:text-gray-900 dark:bg-gray-800 dark:text-gray-50 dark:hover:text-gray-50'
													: 'text-gray-500 hover:text-gray-900 dark:text-gray-400 dark:hover:text-gray-50'
													}`}
												href="#"
												onClick={() => handleGroup()}
											>
												<GroupIcon className="h-4 w-4" />
												함께해요
											</Link>
											<hr className="mt-6 mb-2 border-gray-300 dark:border-gray-700" />
											<div className="flex justify-center items-center h-full">
												<button onClick={() => { setShowModal(true); }}
													className="px-4 py-2 mt-2 rounded bg-black text-white" >
													채팅 추가
												</button>
											</div>
											<hr className="mt-4 mb-2 border-gray-300 dark:border-gray-700" />

											{chatroom && chatroom.length > 0 ? (
												chatroom.map(room => (
													<div key={activeView === 'group' ? room.ccjId : room.userchatId} className="flex justify-center items-center h-full">
														<button onClick={() => {
															if (activeView === 'group') {
																handleCommChat(room);
															} else {
																handleChatSelect(room);
															}
														}}
															className={`flex items-center space-x-3 p-2 rounded-lg transition-colors w-full text-left 
                											${selectedChat === room.roomNumber ? 'bg-gray-200' : 'hover:bg-gray-100 dark:hover:bg-gray-800'}`}>
															<div className="w-12 h-12 rounded-full overflow-hidden shrink-0">
																{activeView === 'chat' && (
																	<img src={room.imgpath || "/placeholder-user.jpg"} alt={room.name} className="w-full h-full object-cover" />
																)}
															</div>
															<span className="text-sm font-medium text-gray-800 dark:text-white">
																{truncateString(room.roomname, 10)}
															</span>
														</button>
													</div>
												))
											) : (
												<div className="flex justify-center items-center h-full">
													<p className="text-gray-500">채팅방이 없습니다</p>
												</div>
											)}
										</nav>
									</div>
								</div>
							</div>



						</main>
					</div>
					<Search isOpen={showModal} onClose={() => setShowModal(false)} onRoomCreated={handleNewRoomCreated} roomList={chatroom} RoomSelectChange={handleChange} />
				</div>
			</BrowserView>





			<MobileView>

				<div className="flex flex-col h-screen">
					{/* Header */}
					<div className="flex h-[60px] items-center border-b px-6">
						<Link className="flex items-center gap-2 font-semibold" href="#">
							<MessageCircleIcon className="h-6 w-6" />
							<span className="">채팅방</span>
						</Link>

						{/* 오른쪽 탭 */}
						<div className="ml-auto flex space-x-4">
							<Link
								className={`flex items-center gap-3 rounded-lg px-2 py-1 transition-all ${activeView === 'chat'
									? 'bg-gray-200 text-gray-900 hover:text-gray-900 dark:bg-gray-800 dark:text-gray-50 dark:hover:text-gray-50'
									: 'text-gray-500 hover:text-gray-900 dark:text-gray-400 dark:hover:text-gray-50'
									}`}
								href="#"
								onClick={() => handleChat()}
							>
								<MessageCircleIcon className="h-3 w-3" />
								개인채팅방
							</Link>
							<Link
								className={`flex items-center gap-3 rounded-lg px-2 py-1 transition-all ${activeView === 'group'
									? 'bg-gray-200 text-gray-900 hover:text-gray-900 dark:bg-gray-800 dark:text-gray-50 dark:hover:text-gray-50'
									: 'text-gray-500 hover:text-gray-900 dark:text-gray-400 dark:hover:text-gray-50'
									}`}
								href="#"
								onClick={() => handleGroup()}
							>
								<GroupIcon className="h-3 w-3" />
								함께해요
							</Link>
							<div className="flex justify-left items-left h-full">
								<button onClick={() => { setShowModal(true); }} className="px-2 py-1 mt-2 rounded bg-black text-white">
									+
								</button>
							</div>
							<hr className="mt-6 mb-2 border-gray-300 dark:border-gray-700" />
						</div>
					</div>





					{/* Chat messages */}
					<div className="flex items-center justify-center h-full">
						{selectedChat && messages.length > 0 ? (
							<div className="bg-white rounded-lg shadow-lg p-4 w-full max-w-lg h-full overflow-y-auto">
								{messages && messages.map(message => (
									<div key={message.message_id} className={`flex items-start gap-4 ${message.nickname === nickname ? 'justify-end' : ''}`}>
										<Avatar>
											<AvatarImage alt={message.name} src="/placeholder-user.jpg" style={{ width: '50px', height: '50px', objectFit: 'cover' }} />
											<AvatarFallback>{message.room_number[0]}</AvatarFallback>
										</Avatar>
										<div className="max-w-[40%]">
											<div className={`rounded-lg p-4 text-sm ${message.nickname === nickname ? 'bg-blue-500 text-white' : 'bg-gray-100 dark:bg-gray-800'}`}>
												<p>{message.content}</p>
											</div>
											<div className="text-xs text-gray-500 dark:text-gray-400 mb-4">{message.date}</div>
										</div>
									</div>
								))}
							</div>
						) : (
							<div className="flex justify-center items-center h-full">
								<p className="text-gray-500">채팅을 시작하세요</p>
							</div>
						)}
					</div>

					{/* Chat input */}
					{/* 채팅 입력창은 여기에 표시 */}
					<form className="flex items-center bg-gray-200 py-2 px-4" onSubmit={handleSendMessage}>
						<Input className="flex-1" placeholder="메세지를 입력하세요..." type="text" value={inputMessage} onChange={e => setInputMessage(e.target.value)} />
						<Button size="icon" type="submit" variant="ghost">
							<SendIcon className="h-5 w-5" />
						</Button>
					</form>
				</div>
			</MobileView>



		</div>
	)
}
export default Message;

function GroupIcon(props) {
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
			<path d="M3 7V5c0-1.1.9-2 2-2h2" />
			<path d="M17 3h2c1.1 0 2 .9 2 2v2" />
			<path d="M21 17v2c0 1.1-.9 2-2 2h-2" />
			<path d="M7 21H5c-1.1 0-2-.9-2-2v-2" />
			<rect width="7" height="5" x="7" y="7" rx="1" />
			<rect width="7" height="5" x="10" y="12" rx="1" />
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




function SendIcon(props) {
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
			<path d="m22 2-7 20-4-9-9-4Z" />
			<path d="M22 2 11 13" />
		</svg>
	)
}

function truncateString(str, num) {
	// 입력 값이 유효하지 않은 경우 안전한 기본값 반환
	if (typeof str !== 'string') return '';

	// 문자열 길이 확인 및 조건에 따라 처리
	if (str.length > num) {
		return str.slice(0, num) + "...";
	} else {
		return str;
	}
}