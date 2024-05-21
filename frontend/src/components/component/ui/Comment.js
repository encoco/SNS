import React, { useState } from 'react';
import api from "../../../api";

function Comment({ isOpen, onClose, comments, boardId }) {
	const [commentText, setCommentText] = useState('');
	const [activeDropdown, setActiveDropdown] = useState(null);
	const [editCommentId, setEditCommentId] = useState(null);
	const [originalCommentText, setOriginalCommentText] = useState('');

	if (!isOpen) {
		return null;
	}

	const handleOverlayClick = () => {
		onClose();
	};

	const handleModalContentClick = (e) => {
		e.stopPropagation();
	};

	const handleCommentWrite = async () => {
		if (isOpen) {
			console.log("현재 선택된 게시물 ID:", boardId);
			const requestData = {
				comment: commentText,
				board_id: boardId
			};

			try {
				const response = await api.post(`/CommentWrite`, requestData, {
					withCredentials: true,
				});
				if (response.data === "success") {
					alert('댓글 작성 성공');
					setCommentText('');
				} else {
					alert('댓글 작성 실패');
				}
			} catch (error) {
				console.log(error);
				alert('댓글 작성 중 오류가 발생했습니다.');
			}
		}
	};

	const toggleDropdown = (commentId) => {
		setActiveDropdown(activeDropdown === commentId ? null : commentId);
	};

	const handleEdit = (commentId, commentText) => {
		setEditCommentId(commentId);
		setOriginalCommentText(commentText);
		setCommentText(commentText); // 수정할 댓글 내용을 input에 표시
		setActiveDropdown(null);
	};

	const handleEditConfirm = async (commentId) => {
		console.log(`Confirm edit for comment with ID: ${commentId}`);
		// 수정된 댓글 내용을 서버로 전송하는 로직
		setEditCommentId(null);
	};

	const handleEditCancel = (commentId) => {
		console.log(`Cancel edit for comment with ID: ${commentId}`);
		setEditCommentId(null);
		setCommentText(originalCommentText); // 이전에 저장한 댓글 내용으로 복구
	};

	const handleDelete = (commentId) => {
		console.log(`Delete comment with ID: ${commentId}`);
		setActiveDropdown(null);
	};

	return (
		<div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50" onClick={handleOverlayClick}>
			<div className="bg-white p-6 rounded-lg shadow-lg max-w-lg w-full m-4 z-50 relative overflow-hidden" onClick={handleModalContentClick}>
				<h2 className="text-xl font-bold mb-4">댓글</h2>
				<div className="mb-4 flex">
					<input
						type="text"
						placeholder="댓글 추가..."
						className="border border-gray-300 p-2 flex-grow mr-2"
						value={commentText}
						onChange={(e) => setCommentText(e.target.value)}
					/>
					<button onClick={handleCommentWrite} className="bg-black hover:bg-green-700 text-white font-bold py-2 px-4 rounded">
						댓글 달기
					</button>
				</div>
				<div className="overflow-y-auto max-h-96">
					<ul>
						{comments && Array.isArray(comments) && comments.map(comment => (
							<li key={comment.comment_id} className="mt-1 flex items-center relative">
								<img src={comment.img} alt="Profile" className="w-10 h-10 rounded-full mr-3" />
								<div className="flex flex-col flex-grow">
									{editCommentId === comment.comment_id ? (
										<input
											type="text"
											value={commentText}
											onChange={(e) => setCommentText(e.target.value)}
											className="border border-gray-300 p-2 flex-grow mr-2"
										/>
									) : (
										<>
											<strong>{comment.nickname}:</strong>
											<span className="text-gray-600">{comment.comment}</span>
										</>
									)}
								</div>
								{editCommentId === comment.comment_id ? (
									<>
										<button onClick={() => handleEditConfirm(comment.comment_id)} className="ml-2 text-gray-500">
											확인
										</button>
										<button onClick={() => handleEditCancel(comment.comment_id)} className="ml-2 text-gray-500">
											취소
										</button>
									</>
								) : (
									<button onClick={() => toggleDropdown(comment.comment_id)} className="ml-2 text-gray-500">
										⋮
									</button>
								)}
								<div onClick={() => setActiveDropdown(null)}>
									{activeDropdown === comment.comment_id && (
										<div className="absolute right-0 mt-2 w-32 bg-white border border-gray-200 rounded shadow-lg z-10">
											<ul>
												<li className="px-4 py-2 hover:bg-gray-100 cursor-pointer" onClick={() => handleEdit(comment.comment_id, comment.comment)}>수정</li>
												<li className="px-4 py-2 hover:bg-gray-100 cursor-pointer" onClick={() => handleDelete(comment.comment_id)}>삭제</li>
											</ul>
										</div>
									)}
								</div>
							</li>
						))}
					</ul>
				</div>
				<button onClick={onClose} className="absolute top-2 right-2 text-2xl font-bold text-gray-800">&times;</button>
			</div>
		</div>
	);
}

export default Comment;