/*BoardWrite*/

import React, { useState } from 'react';
import { useNavigate,Link,useLocation } from 'react-router-dom'; // useNavigate 훅 임포트
import styled from 'styled-components'; // styled-components import 추가
import { Label } from "./ui/label"
import { Input } from "./ui/input"
import { Button } from "./ui/button"
import axios from 'axios';
import api from "../../api";
import { CardTitle, CardDescription, CardHeader, CardContent, CardFooter, Card } from "./ui/card"
import { AvatarImage, AvatarFallback, Avatar } from "./ui/avatar"

//npm install styled-components 설치함!
const Container = styled.div`
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
`;

const Title = styled.h2`
  text-align: center;
  margin-bottom: 20px;
`;
const StyledTextarea = styled.textarea`
  /* 기본 스타일 */
  border: 1px solid #ccc; /* 얇은 테두리 스타일 */
  padding: 8px; /* 내부 여백 */
  font-size: 16px; /* 글꼴 크기 */
  border-radius: 8px;
  resize: none;
  width: 100%;
  height: 200px;
  overflow-y: auto;
  /* 원하는 추가적인 스타일을 여기에 추가하세요 */
`;


	function BoardWrite() {
		const navigate = useNavigate();
		const location = useLocation();
		const [content, setContent] = useState('');
		const [images, setImages] = useState([]);
		const fileInputRef = React.useRef(null);
		const fromPath = location.state?.from || '/';  // 이전 경로가 없다면 홈으로 설정
  		const post = location.state.post;
  		console.log(post);
		  // 파일이 선택되었을 때 호출되는 함수
		  const handleFileChange = (event) => {
		    setImages([...event.target.files]);
		  };
		
		  // 선택된 파일 정보를 출력하는 함수 (예시)
		  const displayFileInfo = () => {
		    if (images.length > 0) {
		      return images.map((file, index) => (
		        <div key={index}>
		          <p>파일 이름: {file.name}</p>
		        </div>
		      ));
		    } else {
		      return <p>선택된 파일이 없습니다.</p>;
		    }
		  };
		// 글 내용이 변경될 때마다 호출되는 함수
		const handleContentChange = (event) => {
		  setContent(event.target.value); // 입력된 글 내용을 상태 변수에 반영합니다.
		};
		
		// '업로드' 버튼을 클릭시 호출
		const Write = async () => {

			const formData = new FormData(); // FormData 객체 생성
		    formData.append('content', content); // 글 내용 추가
			images.forEach((image) => {
			    formData.append('img', image); 
			});
		  	try {
		    	const response = await api.post('/boardWrite', formData, {
			      headers: {
			        'Content-Type': 'multipart/form-data',
			      },
			      withCredentials: true,
			    });
				alert('게시물이 업로드 되었습니다.');
				navigate(fromPath);  
			  	} catch (error) {
			    console.log(error);
			    alert('글쓰기에 실패했습니다. 다시 시도해주세요.');
			}
	   };
	   const handleButtonClick = () => {
	    	fileInputRef.current.click();
	  	};
	  return (
	     <Card className="w-[75vw] max-w-sm mx-auto">
      <CardHeader>
        <CardTitle className="text-xl">새 게시물 만들기</CardTitle>
      </CardHeader>
      <CardContent className="grid gap-4">
        <StyledTextarea className="flex-1" placeholder="게시물 내용을 작성해주세요." value={content} onChange={handleContentChange}/>
        <div className="flex flex-col gap-2">
           <input type="file" name="img" id="img" ref={fileInputRef} onChange={handleFileChange} multiple style={{ display: 'none' }} /> 
		  <Button size="sm" onClick={handleButtonClick}>
		    <UploadIcon className="h-4 w-4 mr-2"  />
		    Add images
		  </Button>
          <Button size="sm">
            <UploadIcon className="h-4 w-4 mr-2" />
            Add videos
          </Button>
           {displayFileInfo()} 
        </div>
        <div className="grid gap-4">
          <Card className="rounded-none shadow-none border-0">
          </Card>
        </div>
      </CardContent>
      <CardFooter>
        <Button onClick={Write}>업로드</Button>
      </CardFooter>
    </Card>
	  );
	}
	function BookmarkIcon(props) {
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
      <path d="m19 21-7-4-7 4V5a2 2 0 0 1 2-2h10a2 2 0 0 1 2 2v16z" />
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


function UploadIcon(props) {
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
      <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4" />
      <polyline points="17 8 12 3 7 8" />
      <line x1="12" x2="12" y1="3" y2="15" />
    </svg>
  )
}

export default BoardWrite;