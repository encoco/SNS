import React, { useState } from 'react';
import { useNavigate } from "react-router-dom";
import api from "../../../api";

function DropdownMenu({ post }) {
	const [isOpen, setIsOpen] = useState(false);
	const navigate = useNavigate();
	const userNickname = localStorage.getItem("nickname");
	const toggleDropdown = () => setIsOpen(!isOpen);
	const [showModal, setShowModal] = useState(false);

	const editPost = (post) => {
		navigate('/boardWrite', { state: { post } });
	};

	const deletePost = (post) => {
		setShowModal(true);
	};

	const confirmDelete = async () => {
		try {
			const board_id = post.board_id;
			const response = await api.post(`/boardDelete`, board_id,{
				withCredentials: true,
			});
			alert("게시물이 삭제되었습니다.");
			setShowModal(false);
			window.location.reload();
		} catch (error) {
			console.log(error);
			alert('게시물 삭제에 실패했습니다.');
			window.location.reload();
		}
	};
	
	if (post.nickname !== userNickname) {
		return null;
	}
	return (
		
		<div className="relative">
			<button onClick={toggleDropdown} className="p-2">
				<MoreHorizontalIcon className="h-5 w-5 text-gray-700" />
			</button>
			{isOpen && (
				<div className="absolute right-0 mt-2 py-2 w-48 bg-white rounded-md shadow-xl z-20">
					<button onClick={() => editPost(post)}
						className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 w-full text-left">수정</button>
					<button onClick={() => deletePost(post)}
						className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 w-full text-left">삭제</button>
				</div>
			)}
			{showModal && (
				<div className="fixed inset-0 flex items-center justify-center z-50">
					<div className="absolute inset-0 bg-gray-500 opacity-50"></div>
					<div className="bg-white p-8 rounded-md shadow-md z-50">
						<p className="text-lg font-semibold">게시물 삭제</p>
						<p className="text-gray-700 mt-2">정말로 이 게시물을 삭제하시겠습니까?</p>
						<div className="mt-4 flex justify-end">
							<button onClick={() => setShowModal(false)} className="text-gray-500 mr-4">취소</button>
							<button onClick={confirmDelete} className="bg-red-500 text-white px-4 py-2 rounded-md">삭제</button>
						</div>
					</div>
				</div>
			)}
		</div>
	);

}

export default DropdownMenu;

function FlagIcon(props) {
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
			<path d="M4 15s1-1 4-1 5 2 8 2 4-1 4-1V3s-1 1-4 1-5-2-8-2-4 1-4 1z" />
			<line x1="4" x2="4" y1="22" y2="15" />
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


function MoreHorizontalIcon(props) {
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
			<circle cx="12" cy="12" r="1" />
			<circle cx="19" cy="12" r="1" />
			<circle cx="5" cy="12" r="1" />
		</svg>
	)
}