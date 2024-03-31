/*BoardWrite*/

import React, { useState } from 'react';
import { useNavigate,Link } from 'react-router-dom'; // useNavigate 훅 임포트
import styled from 'styled-components'; // styled-components import 추가
import { Label } from "./ui/label"
import { Input } from "./ui/input"
import { Button } from "./ui/button"
import axios from 'axios';

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

const TextArea = styled.textarea`
  width: 100%;
  height: 200px;
  padding: 10px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 5px;
  resize: none;
`;

	function BoardWrite() {
		const navigate = useNavigate();
		const [content, setContent] = useState('');
		 const [img, setimg] = useState(null);

		  // 파일이 선택되었을 때 호출되는 함수
		  const handleFileChange = (event) => {
		    // 사용자가 선택한 첫 번째 파일을 상태로 설정
		    setimg(event.target.files[0]);
		  };
		
		  // 선택된 파일 정보를 출력하는 함수 (예시)
		  const displayFileInfo = () => {
		    if (img) {
		      return <p>파일 이름: {img.name}</p>;
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
		  try {
			const id = JSON.parse(sessionStorage.getItem('userInfo') || '{}').id;
			console.log(id);
		    const response = await axios.post('http://localhost:8080/api/boardList', {
		      id, // 여기에 id 값을 추가
		      content
		    });
		   	console.log(response);  // 서버로부터 받은 응답을 콘솔에 출력
		   	navigate('/'); // 글 작성이 완료되면 홈페이지로 이동
		   	
		  } catch (error) {
			  console.log(error);
		    alert('글쓰기에 실패했습니다. 다시 시도해주세요.');
		  }
	   };
	  return (
	    <Container>
		      <Title>게시글 작성</Title>
		      <TextArea value={content} onChange={handleContentChange} placeholder="내용을 입력하세요..." />
		      <input type="file" name="img" id="img" onChange={handleFileChange} />
		      <Button onClick={Write}>업로드하기</Button>
		</Container>
	  );
	}

export default BoardWrite;