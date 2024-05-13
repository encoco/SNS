import React, { useState } from 'react';

function Together() {
   const [hoveredPost, setHoveredPost] = useState(null); // State to track hovered post
   const [showModal, setShowModal] = useState(false); // State to track modal visibility
   const [selectedPostId, setSelectedPostId] = useState(null); // State to store the ID of the post the user clicked

   const posts = [
      { id: 1, title: '깨끗!', People: 20, image: 'https://s3.ap-northeast-2.amazonaws.com/sygrsns/image/5181233d-c4aa-4f8c-befd-e0beb52c74a1.jpg', hashtags: ['#노을', '#노을지다', '#봄', '#아이린리그', '#봄도둑'] },
      { id: 2, title: '깨끗!', People: 17, image: 'https://s3.ap-northeast-2.amazonaws.com/sygrsns/image/adc71b8e-76e8-42bb-ac1f-70f16729d7d5.png', hashtags: ['#노을', '#노을지다', '#봄', '#아이린리그', '#봄도둑'] },
      // Add more posts here if needed
   ];

   const handleJoinChat = (postId) => {
      setSelectedPostId(postId);
      setShowModal(true);
   };

	const handleModalConfirm = () => {
      console.log(`Joining chat for post with ID: ${selectedPostId}`);
      setShowModal(false);
   };
   const handleModalCancel = () => {
      setShowModal(false);
   };

   return (
      <div className="Together bg-gray-100/40 dark:bg-gray-800/40 p-4 flex flex-wrap">
         <div className="w-full flex justify-end mb-4 px-2">
            <button className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
               오픈채팅 만들기
            </button>
         </div>

         {posts.map(post => (
            <div key={post.id} className="w-full md:w-1/2 mb-4 px-2">
               <div className="post bg-white rounded-lg overflow-hidden shadow-md flex">
                  <div
                     className={`w-3/5 flex flex-col justify-between ${hoveredPost === post.id ? 'hover:bg-gray-200' : ''}`}
                     onMouseEnter={() => setHoveredPost(post.id)}
                     onMouseLeave={() => setHoveredPost(null)}
                  >
                     <div className="p-3">
                        <p className="font-semibold text-lg mb-2">{post.title}</p>
                        <div className="hashtags text-blue-500 text-sm">
                           {post.hashtags.map(tag => (
                              <span key={tag} className="mr-1">#{tag}</span>
                           ))}
                        </div>
                     </div>
                     <div className="flex items-center justify-between p-3">
                        <div className="flex items-center">
                           <svg className="h-5 w-5 rounded-full fill-current text-gray-400 mr-1" viewBox="0 0 24 24">
                              <path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z" />
                           </svg>
                           <span className="text-gray-600">{post.People}</span>
                        </div>
                        <button onClick={() => handleJoinChat(post.id)} className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
                           참여
                        </button>
                     </div>
                  </div>
                  <div className="w-2/5 flex justify-center items-center">
                     <img
                        src={post.image}
                        alt="Post Image"
                        className="w-32 h-32 object-cover cursor-pointer"
                        onClick={() => {
                           setSelectedPostId(post.id);
                           setShowModal(true);
                        }}
                     />
                  </div>
               </div>
            </div>
         ))}

         {/* Modal */}
         {showModal && (
            <div className="fixed z-10 inset-0 overflow-y-auto">
               <div className="flex items-center justify-center min-h-screen">
                  <div className="bg-white rounded-lg shadow-lg p-4 mx-2" style={{ width: "45%", maxHeight: "45vh" }}>
                     {/* 이미지를 모달 내부에 크게 보여줍니다 */}
                     <img
                        src={posts.find(post => post.id === selectedPostId)?.image}
                        alt="Post Image"
                        className="mx-auto max-h-screen"
                     />
                     <div className="mt-4 flex justify-center">
                        <button onClick={handleModalConfirm} className="bg-gray-500 hover:bg-gray-400 text-white font-bold py-2 px-4 mr-4 rounded">
                           확인
                        </button>
                     </div>
                  </div>
               </div>
            </div>
         )}
      </div>
   );
}

export default Together;