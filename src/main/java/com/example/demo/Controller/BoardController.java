package com.example.demo.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Config.JwtUtil;
import com.example.demo.DTO.BoardDTO;
import com.example.demo.DTO.BoardLikeDTO;
import com.example.demo.Service.BoardService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardController {

	private final BoardService boardService;
	private final JwtUtil jwtUtil;
	public String token(String request) {
    	String token = request.substring(7,request.length());
    	return token;
    }

	@GetMapping("/boardList")
	public ResponseEntity<?> readList(HttpServletRequest request) {
	    try {
	    	String token = token(request.getHeader("Authorization"));
	    	int userId = jwtUtil.getUserIdFromToken(token);
	        List<BoardDTO> posts = boardService.getPost(userId);
	        List<BoardLikeDTO> like = boardService.getLike(userId);
	        
	        Map<String, Object> response = new HashMap<>();
	        response.put("posts", posts);
	        response.put("likes", like);
	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        System.out.println("서버 오류");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
	    }
	}

    @PostMapping("/boardWrite")
    public ResponseEntity<?> writeBoard(@RequestParam("content") String content, 
    		 							@RequestParam(value= "img" , required = false) List<MultipartFile> imgs,
									    HttpServletRequest request){
        try {
        	String token= token(request.getHeader("Authorization"));
        	int userId = jwtUtil.getUserIdFromToken(token);
        	String nickname = jwtUtil.getNickFromToken(token);
        	BoardDTO boardDTO = new BoardDTO();
            boardDTO.setId(userId);
            boardDTO.setNickname(nickname);
            boardDTO.setContent(content);
            if (imgs != null && !imgs.isEmpty()) {
                boardDTO.setImg(imgs); // 여러 이미지 파일 설정
            } 

            boardService.writeBoard(boardDTO);
            return ResponseEntity.ok("글이 성공적으로 작성되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("글 작성 중 오류가 발생했습니다.");
        }
    }
   
    @GetMapping("/boardLike")
    public ResponseEntity<?> boardLike(@RequestParam(value = "boardId") int boardId,HttpServletRequest request){
    	try {
    		String token = token(request.getHeader("Authorization"));
    		int userId = jwtUtil.getUserIdFromToken(token);
    		BoardLikeDTO dto = new BoardLikeDTO();
    		dto.setBoard_id(boardId);
    		dto.setId(userId);
    		int result = boardService.boardLike(dto);
    		
    		if (result == 0) {
                return ResponseEntity.ok("fail");
            } else if (result == 1) {
                return ResponseEntity.ok("success");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected result");
            }
    	}catch(Exception e){
    		System.out.println("error : " + e);
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
    	}
    }

    @PostMapping("/boardUpdate")
    public ResponseEntity<?> UpdateBoard(@RequestParam("content") String content, 
				@RequestParam(value= "img" , required = false) List<MultipartFile> imgs,
				@RequestParam(value= "imgpath" , required = false) String imgpath,
				@RequestParam("board_id") int board_id,
		    HttpServletRequest request){
    	try {
        	BoardDTO boardDTO = new BoardDTO();
        	boardDTO.setBoard_id(board_id);
            boardDTO.setContent(content);
            boardDTO.setImgpath(imgpath);
            if (imgs != null && !imgs.isEmpty()) {
                boardDTO.setImg(imgs); // 여러 이미지 파일 설정
            } 
            boardService.updatePost(boardDTO);
            return ResponseEntity.ok("수정 완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("글 작성 중 오류가 발생했습니다.");
        }
    }
    
    @PostMapping("/boardDelete")
    public ResponseEntity<?> DeleteBoard(@RequestBody int board_id){
    	try{
    		boardService.DeleteBoard(board_id);
    		return ResponseEntity.ok(null);
    	}
    	catch(Exception e) {
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("글 삭제 중 오류가 발생했습니다.");
    	}
    }
    
}