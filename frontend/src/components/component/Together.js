import React, { useState, useEffect } from 'react';
import AddCommuChat from './ui/AddCommuChat.js';
import api from "../../api";

function Together({ onJoinRoom }) {
   const [showModal, setShowModal] = useState(false); 
   const [selectedPostId, setSelectedPostId] = useState(null);
   const [addCommu, setAddCommu] = useState(false);
   const [commuRoom, setCommuRoom] = useState(null);

   useEffect(() => {
      const fetchCommuRooms = async () => {
         try {
            const response = await api.get(`/selectAllCommuRoom`, {
               withCredentials: true,
            });
            setCommuRoom(response.data === "채팅방 없음" ? [] : response.data);
         } catch (error) {
            console.log(error);
         }
      };
      fetchCommuRooms();
   }, []);


   const handleJoin = async (room) => {
      const confirmJoin = window.confirm("정말 참여하시겠습니까?");
      if (confirmJoin) {
         try {
            const response = await api.post(`/JoinCommuRoom`, {
               communitychatId: room.communitychatId,
               id: room.id,
               title: room.title,
               description: room.description,
               imgpath: room.imgpath
            }, {
               withCredentials: true,
            });
            console.log("hi");
            onJoinRoom(room); // Join 후 상위 컴포넌트에 알림
            
         } catch (error) {
            console.log(error);
         }
         console.log('사용자가 참여를 확정했습니다.', room);
      } else {
         return;
      }
   };

   return (
      <div className="Together bg-gray-100/40 dark:bg-gray-800/40 p-4 flex flex-wrap">
         <div className="w-full flex justify-end mb-4 px-2">
            <button onClick={() => setAddCommu(true)} className="bg-black text-white font-bold py-2 px-4 rounded">
               오픈채팅 만들기
            </button>
         </div>

         {commuRoom && commuRoom.map(room => (
            <div key={room.communitychatId} className="w-full md:w-1/2 mb-4 px-2">
               <div className="post bg-white rounded-lg overflow-hidden shadow-md flex" style={{ minHeight: '250px', maxHeight: '300px' }}>
                  <div className="w-3/5 p-3 flex flex-col justify-between">
                     <div>
                        <p className="font-semibold text-lg mb-2">{room.title}</p>
                        {room.description}
                     </div>
                     <div className="flex items-center justify-between">
                        <div className="flex items-center">
                           <svg className="h-5 w-5 rounded-full fill-current text-gray-400 mr-1" viewBox="0 0 24 24">
                              <path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z" />
                           </svg>
                           <span className="text-gray-600">123</span>
                        </div>
                        <button onClick={() => handleJoin(room)} className="bg-black text-white font-bold py-1 px-2 rounded">
                           참여
                        </button>
                     </div>
                  </div>
                  <div className="w-2/5 flex justify-center items-center p-3">
                     <img
                        onClick={() => {
                           setSelectedPostId(room.communitychatId);
                           setShowModal(true);
                        }}
                        src={room.imgpath || "/placeholder.svg"}
                        alt="Chat Room"
                        className="h-full w-full object-cover"
                     />
                  </div>
               </div>
            </div>
         ))}

         {showModal && (
            <div className="fixed inset-0 z-10 overflow-y-auto flex items-center justify-center p-4">
               <div className="bg-white shadow-lg rounded-lg overflow-hidden">
                  <div className="p-4">
                     <img
                        src={commuRoom.find(room => room.communitychatId === selectedPostId)?.imgpath || "/placeholder.svg"}
                        alt="Post Image"
                        className="max-w-full max-h-screen"
                     />
                  </div>
                  <div className="flex justify-center p-4">
                     <button onClick={() => setShowModal(false)} className="bg-gray-500 hover:bg-gray-400 text-white font-bold py-2 px-4 rounded">
                        Close
                     </button>
                  </div>
               </div>
            </div>
         )}
         <AddCommuChat Open={addCommu} Close={() => setAddCommu(false)} />
      </div>
   );
}

export default Together;