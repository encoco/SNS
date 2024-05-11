function Comment({ isOpen, onClose, comments }) {
	if (!isOpen) return null;

	const handleOverlayClick = () => {
		onClose();
	};

	const handleModalContentClick = (e) => {
		e.stopPropagation(); // 모달 콘텐츠 내부의 클릭 이벤트가 부모 요소로 전파되는 것을 막음
	};

	return (
		<div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50" onClick={handleOverlayClick}>
			<div className="bg-white p-6 rounded-lg shadow-lg max-w-lg w-full m-4 z-50 relative overflow-hidden" onClick={handleModalContentClick}>
				<h2 className="text-xl font-bold mb-4">댓글</h2>
				<form onSubmit={(e) => {
					e.preventDefault();
					onClose(); // Form 제출 후 모달 닫기
				}} className="mb-4 flex">
					<input type="text" placeholder="댓글 추가..." className="border border-gray-300 p-2 flex-grow mr-2" />
					<button type="submit" className="bg-black hover:bg-green-700 text-white font-bold py-2 px-4 rounded">
						댓글 달기
					</button>
				</form>
				<div className="overflow-y-auto max-h-96">  
					<ul>
						{comments.map(comment => (
							<li key={comment.nickname} className="mt-1 flex items-center">
								<img src={comment.img} alt="Profile" className="w-10 h-10 rounded-full mr-3" />
								<div className="flex flex-col">
									<strong>{comment.nickname}:</strong>
									<span className="text-gray-600">{comment.comment}</span>
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