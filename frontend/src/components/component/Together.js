import React from 'react';

function Together() {
	// 예시 데이터입니다. 실제 데이터로 대체하세요.
	const posts = [
		{ id: 1, title: '깨끗!', People: 20, image: '/path/to/image1.jpg', hashtags: ['#노을', '#노을지다', '#봄', '#아이린리그', '#봄도둑'] },
		{ id: 2, title: '깨끗!', People: 17, image: '/path/to/image2.jpg', hashtags: ['#노을', '#노을지다', '#봄', '#아이린리그', '#봄도둑'] }
	];

	return (
		<div className="Together bg-gray-100/40 dark:bg-gray-800/40 p-4">
			{posts.map(post => (
				<div key={post.id} className="post bg-white rounded-lg overflow-hidden shadow-md mb-4">
					<div className="header flex items-center p-3 ">
						<div className="title ml-3">
							<p className="font-semibold">{post.title}</p>
							<div className="hashtags text-blue-500 text-sm">
								{post.hashtags.map(tag => (
									<span key={tag}> {tag} </span>
								))}
							</div>
						</div>
					</div>
					<img src={post.image} alt="게시물 이미지" className="w-full" />
					<div className="footer p-3 flex justify-between items-center">
						<div className="actions">
							<button className="comment-button flex items-center">
								<svg className="h-10 w-10 rounded-full fill-current text-gray-400 mr-3" viewBox="0 0 24 24">
									<path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z" />
								</svg>
								<span>{post.People}</span>
							</button>
						</div>
					</div>
				</div>
			))}
		</div>
	);
}


export default Together;
