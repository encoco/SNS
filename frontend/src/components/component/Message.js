import { Link } from "react-router-dom";
import Sidebar from "./ui/Sidebar";
import Together from "./Together";
import { Button } from "./ui/button"
import { CardTitle, CardDescription, CardHeader, CardContent, Card } from "./ui/card"
import { Input } from "./ui/input"
import { DropdownMenuTrigger, DropdownMenuLabel, DropdownMenuSeparator, DropdownMenuItem, DropdownMenuContent, DropdownMenu } from "./ui/dropdown-menu"
import { AvatarImage, AvatarFallback, Avatar } from "./ui/avatar"
import { Badge } from "./ui/badge"
import React, { useState } from 'react';

function Meesage() {
	const [activeView, setActiveView] = useState('chat');

	const handleClick = () => {
		console.log("123 click");
	}
	return (
		<div className="grid min-h-screen w-full grid-cols-[280px_1fr]">
			<div className="flex flex-col border-r bg-gray-100/40 dark:bg-gray-800/40">
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
										<div className="flex items-start gap-4">
											<Avatar>
												<AvatarImage alt="John Doe" src="/placeholder-user.jpg"
													style={{
														width: '50px',    // 너비 설정
														height: '50px',   // 높이 설정
														objectFit: 'cover'  // 이미지 비율 유지
													}}
												/>
												<AvatarFallback>JD</AvatarFallback>
											</Avatar>
											<div className="flex-1 space-y-2">
												<div className="rounded-lg bg-gray-100 p-4 text-sm dark:bg-gray-800">
													<p>오늘 저녁 마라탕 ㄱㄱ</p>
												</div>
												<div className="text-xs text-gray-500 dark:text-gray-400">3:49 PM</div>
											</div>
										</div>
										<div className="flex items-start gap-4 justify-end">
											<div className="flex-1 space-y-2">
												<div className="rounded-lg bg-blue-500 p-4 text-sm text-white">
													<p>오 ㄱㄱ</p>
												</div>
												<div className="text-xs text-gray-500 dark:text-gray-400">3:51 PM</div>
											</div>
											<Avatar>
												<AvatarImage alt="John Doe" src="/placeholder-user.jpg"
													style={{
														width: '50px',    // 너비 설정
														height: '50px',   // 높이 설정
														objectFit: 'cover'  // 이미지 비율 유지
													}}
												/>
												<AvatarFallback>JD</AvatarFallback>
											</Avatar>
										</div>


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
									<div className="grid gap-4">
										{/* 함께해요 활동 목록 */}
										<div className="flex flex-col border-r bg-gray-100/40 dark:bg-gray-800/40">
											<Together />
										</div>
									</div>
								</div>
							</div>
						)}

						<div className="w-[300px] border-l bg-gray-100/40 p-6 dark:bg-gray-800/40">
							<div className="flex-1 overflow-auto py-2">
								<nav className="grid items-start px-4 text-sm font-medium">
									<Link
										className="flex items-center gap-3 rounded-lg bg-gray-100 px-3 py-2 text-gray-900 transition-all hover:text-gray-900 dark:bg-gray-800 dark:text-gray-50 dark:hover:text-gray-50"
										href="#" onClick={() => setActiveView('chat')}
									>
										<MessageCircleIcon className="h-4 w-4" />
										개인채팅방
									</Link>
									<Link
										className="flex items-center gap-3 rounded-lg px-3 py-2 text-gray-500 transition-all hover:text-gray-900 dark:text-gray-400 dark:hover:text-gray-50"
										href="#" onClick={() => setActiveView('group')}
									>
										<GroupIcon className="h-4 w-4" />
										함께해요
									</Link>
									<hr className="mt-4 mb-2 border-gray-300 dark:border-gray-700" />
									<button
										onClick={handleClick}
										className="flex items-center space-x-3 p-2 rounded-lg hover:bg-gray-200 dark:hover:bg-gray-700 transition-colors w-full text-left">
										<div className="w-12 h-12 rounded-full overflow-hidden shrink-0">
											<img src="/placeholder-user.jpg" alt="name" className="w-full h-full object-cover" />
										</div>
										<span className="text-sm font-medium text-gray-800 dark:text-white">흰둥이</span>
									</button>
									<button
										onClick={handleClick}
										className="flex items-center space-x-3 p-2 rounded-lg hover:bg-gray-200 dark:hover:bg-gray-700 transition-colors w-full text-left">
										<div className="w-12 h-12 rounded-full overflow-hidden shrink-0">
											<img src="/placeholder-user.jpg" alt="name" className="w-full h-full object-cover" />
										</div>
										<span className="text-sm font-medium text-gray-800 dark:text-white">패트</span>
									</button>
									<button
										onClick={handleClick}
										className="flex items-center space-x-3 p-2 rounded-lg hover:bg-gray-200 dark:hover:bg-gray-700 transition-colors w-full text-left">
										<div className="w-12 h-12 rounded-full overflow-hidden shrink-0">
											<img src="/placeholder-user.jpg" alt="name" className="w-full h-full object-cover" />
										</div>
										<span className="text-sm font-medium text-gray-800 dark:text-white">매트</span>
									</button>
									<button
										onClick={handleClick}
										className="flex items-center space-x-3 p-2 rounded-lg hover:bg-gray-200 dark:hover:bg-gray-700 transition-colors w-full text-left">
										<div className="w-12 h-12 rounded-full overflow-hidden shrink-0">
											<img src="/placeholder-user.jpg" alt="name" className="w-full h-full object-cover" />
										</div>
										<span className="text-sm font-medium text-gray-800 dark:text-white">짱구</span>
									</button>

								</nav>
							</div>
						</div>
					</div>
				</main>
			</div>
		</div>
	)
}
export default Meesage;

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


function PlusIcon(props) {
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
			<path d="M5 12h14" />
			<path d="M12 5v14" />
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


function SettingsIcon(props) {
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
			<path d="M12.22 2h-.44a2 2 0 0 0-2 2v.18a2 2 0 0 1-1 1.73l-.43.25a2 2 0 0 1-2 0l-.15-.08a2 2 0 0 0-2.73.73l-.22.38a2 2 0 0 0 .73 2.73l.15.1a2 2 0 0 1 1 1.72v.51a2 2 0 0 1-1 1.74l-.15.09a2 2 0 0 0-.73 2.73l.22.38a2 2 0 0 0 2.73.73l.15-.08a2 2 0 0 1 2 0l.43.25a2 2 0 0 1 1 1.73V20a2 2 0 0 0 2 2h.44a2 2 0 0 0 2-2v-.18a2 2 0 0 1 1-1.73l.43-.25a2 2 0 0 1 2 0l.15.08a2 2 0 0 0 2.73-.73l.22-.39a2 2 0 0 0-.73-2.73l-.15-.08a2 2 0 0 1-1-1.74v-.5a2 2 0 0 1 1-1.74l.15-.09a2 2 0 0 0 .73-2.73l-.22-.38a2 2 0 0 0-2.73-.73l-.15.08a2 2 0 0 1-2 0l-.43-.25a2 2 0 0 1-1-1.73V4a2 2 0 0 0-2-2z" />
			<circle cx="12" cy="12" r="3" />
		</svg>
	)
}


function UsersIcon(props) {
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
			<path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2" />
			<circle cx="9" cy="7" r="4" />
			<path d="M22 21v-2a4 4 0 0 0-3-3.87" />
			<path d="M16 3.13a4 4 0 0 1 0 7.75" />
		</svg>
	)
}