package com.example.demo.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.BoardDTO;

import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class testC {

	private final com.example.demo.Service.testS testS;
	
    @PostMapping("/Test")
    public ResponseEntity<?> writeBoard(@RequestBody BoardDTO boardDTO) {
    	System.out.println("POST 요청이 도착했습니다.");
        // 받아온 글 내용을 서비스를 통해 저장하고, 결과에 따라 적절한 응답을 반환합니다.
        try {
        	testS.writeBoard(boardDTO);
            return ResponseEntity.ok("글이 성공적으로 작성되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("글 작성 중 오류가 발생했습니다.");
        }
    }
}
