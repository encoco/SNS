package com.example.demo.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.BoardDTO;
import com.example.demo.Service.BoardService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardController {
	
	private final BoardService boardService;
	
    @PostMapping("/boardList")
    public ResponseEntity<?> writeBoard(@RequestBody BoardDTO boardDTO) {
        // 받아온 글 내용을 서비스를 통해 저장하고, 결과에 따라 적절한 응답을 반환합니다.
        try {
        	System.out.println(boardDTO);
            boardService.writeBoard(boardDTO);
            return ResponseEntity.ok("글이 성공적으로 작성되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("글 작성 중 오류가 발생했습니다.");
        }
    }

}