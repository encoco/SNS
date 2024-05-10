import React, { useState } from 'react';

function Together() {
	const [hoveredPost, setHoveredPost] = useState(null); // State to track hovered post

	const posts = [
		{ id: 1, title: '깨끗!', People: 20, image: '/path/to/image1.jpg', hashtags: ['#노을', '#노을지다', '#봄', '#아이린리그', '#봄도둑'] },
		{ id: 2, title: '깨끗!', People: 17, image: '/path/to/image2.jpg', hashtags: ['#노을', '#노을지다', '#봄', '#아이린리그', '#봄도둑'] }
	];

	return (
		<div className="Together bg-gray-100/40 dark:bg-gray-800/40 p-4">
			{posts.map(post => (
				<div
					key={post.id}
					className={`post bg-white rounded-lg overflow-hidden shadow-md mb-4 flex flex-col md:flex-row ${hoveredPost === post.id ? 'hover:bg-gray-200' : ''}`}
					onMouseEnter={() => setHoveredPost(post.id)}
					onMouseLeave={() => setHoveredPost(null)}
				>
					<div className="text-wrapper w-full md:w-3/5 p-3">
						<div className="header flex items-center ml-4 mt-4">
							<div className="title">
								<p className="font-semibold">{post.title}</p>
								<div className="hashtags text-blue-500 text-sm">
									{post.hashtags.map(tag => (
										<span key={tag}> {tag} </span>
									))}
								</div>
							</div>
						</div>

						<div className="footer p-3 flex items-center">
							<svg className="h-5 w-5 rounded-full fill-current text-gray-400" viewBox="0 0 24 24">
								<path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z" />
							</svg>
							<span>{post.People}</span>
						</div>
					</div>
					<div className="image-wrapper w-full md:w-2/5 flex justify-center md:justify-end">
						<img src={post.image} alt="Post Image" className="w-32 h-32 object-cover mr-4" />
					</div>
				</div>
			))}
		</div>
	);
}

export default Together;