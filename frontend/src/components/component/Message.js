import { Link } from "react-router-dom";
import Sidebar from "./ui/Sidebar";
import Together from "./Together";
import { Button } from "./ui/button"
import { Input } from "./ui/input"
import { AvatarImage, AvatarFallback, Avatar } from "./ui/avatar"
import React, { useState, useEffect } from 'react';
import api from "../../api";
import Modal from 'react-modal';
import Search from './ui/search';
import webSocketService from '../../services/WebSocketService';

function Message() {
	const [activeView, setActiveView] = useState('chat');
	const [chatroom, setChatRoom] = useState([]);
	const [selectedChat, setSelectedChat] = useState('');
	//const [message,setMessage] = useState([]);
	const [showModal, setShowModal] = useState(false);

	useEffect(() => {
		const chatroom = async () => {
			try {
				const response = await api.get(`/selectRoom`, {
					withCredentials: true,
				});
				setChatRoom(response.data === "채팅방 없음" ? [] : response.data);

			} catch (error) {
				console.log(error);
			}
		};
		chatroom();
	}, []);

	const message = [
		{ message_id: 1, name: "채팅1", nickname: "rgr", content: "채팅1.", date: "2024-05-01 17:32:00", roomNumber: "1" },
		{ message_id: 2, name: "채팅2", nickname: "lll", content: "채팅2ㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱ?", date: "2024-05-01 17:33:00", roomNumber: "1" },
		{ message_id: 3, name: "채팅3", nickname: "rgr", content: "채팅3ㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱ?", date: "2024-05-01 17:34:00", roomNumber: "1" },
		{ message_id: 4, name: "채팅4", nickname: "lll", content: "채팅4.", date: "2024-05-01 17:35:00", roomNumber: "1" }
	];
	const handleClick = () => {
		console.log("123 click");
		console.log(chatroom);
	}

	const handleChatSelect = (room) => {
		console.log("Selected:", room.roomNumber);
		setSelectedChat(room.roomNumber);
		//여기서 message도 수정 디비에서 해당 채팅방에서 찾아서
	};

	const handleNewRoomCreated = (newRoom) => {
		setChatRoom(prevRooms => [...prevRooms, newRoom]); // 새 채팅방 추가
		setSelectedChat(newRoom.roomNumber); // 새 채팅방 선택
		console.log(newRoom);
	};

	return (
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
								<div className="flex-1 overflow-auto p-6">
									<div className="grid gap-4">
										{/* 채팅 메시지 출력 영역 */}
										{/* 채팅 예제 */}
										{selectedChat && message.length > 0 ? message.map(message => (
											<div key={message.message_id} className={`flex items-start gap-4 ${message.nickname === localStorage.getItem('nickname') ? 'justify-end' : ''}`}>
												<Avatar>
													<AvatarImage alt={message.name} src="/placeholder-user.jpg"
														style={{
															width: '50px',
															height: '50px',
															objectFit: 'cover'
														}}
													/>
													<AvatarFallback>{message.name[0]}</AvatarFallback>
												</Avatar>
												<div className="max-w-[40%]">
													<div className={`rounded-lg p-4 text-sm ${message.nickname === localStorage.getItem('nickname') ? 'bg-blue-500 text-white' : 'bg-gray-100 dark:bg-gray-800'}`}>
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
									<form className="flex items-center gap-2">
										<Input className="flex-1" placeholder="메세지를 입력하세요..." type="text" />
										<Button size="icon" type="submit" variant="ghost">
											<SendIcon className="h-5 w-5" />
										</Button>
									</form>
								</div>
							</div>
						) : (
							// 함께해요 화면 컨텐츠
							<div className="flex-1 flex flex-col">
								<div className="flex-1 overflow-auto p-6">
									<div className="grid h-full gap-4">
										{/* 함께해요 활동 목록 */}
										<div className="flex flex-col border-r bg-gray-100/40 dark:bg-gray-800/40">
											<Together />
										</div>
									</div>
								</div>
							</div>
						)}





						<div className="w-[300px] border-l bg-gray-100/40 p-6 dark:bg-gray-800/40">
							<div className="flex-1 overflow-y-auto py-2 max-h-[calc(100vh-110px)]">
								<nav className="grid items-start px-4 text-sm font-medium">
									<Link
										className={`flex items-center gap-3 rounded-lg px-3 py-2 transition-all ${activeView === 'chat'
											? 'bg-gray-200 text-gray-900 hover:text-gray-900 dark:bg-gray-800 dark:text-gray-50 dark:hover:text-gray-50'
											: 'text-gray-500 hover:text-gray-900 dark:text-gray-400 dark:hover:text-gray-50'
											}`}
										href="#"
										onClick={() => setActiveView('chat')}
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
										onClick={() => setActiveView('group')}
									>
										<GroupIcon className="h-4 w-4" />
										함께해요
									</Link>
									<hr className="mt-4 mb-2 border-gray-300 dark:border-gray-700" />
									<div className="flex justify-center items-center h-full">
										<button onClick={() => { setShowModal(true); }}
											className="px-4 py-2 rounded bg-black text-white" >
											새로운 채팅
										</button>
									</div>
									<hr className="mt-4 mb-2 border-gray-300 dark:border-gray-700" />

									{chatroom && chatroom.length > 0 ? (
										chatroom.map(room => (
											<div key={room.userchatId} className="flex justify-center items-center h-full">
												<button onClick={() => handleChatSelect(room)}
													className={`flex items-center space-x-3 p-2 rounded-lg transition-colors w-full text-left 
													${selectedChat === room.roomNumber ?
															'bg-gray-200' : 'hover:bg-gray-20 dark:hover:bg-gray-100'}`
													}>
													<div className="w-12 h-12 rounded-full overflow-hidden shrink-0">
														<img src="/placeholder-user.jpg" alt={room.name} className="w-full h-full object-cover" />
													</div>
													<span className="text-sm font-medium text-gray-800 dark:text-white"> {truncateString(room.nickname, 10)} </span>
												</button>
											</div>
										))
									) : (<></>)}
								</nav>
							</div>
						</div>
					</div>
				</main>
			</div>
			<Search isOpen={showModal} onClose={() => setShowModal(false)} onRoomCreated={handleNewRoomCreated} />
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