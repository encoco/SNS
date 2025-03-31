package com.example.demo.board.controller;

import com.example.demo.board.docs.BoardDocs;
import com.example.demo.board.dto.BoardDTO;
import com.example.demo.board.dto.BoardLikeDTO;
import com.example.demo.board.dto.CommentDTO;
import com.example.demo.board.service.BoardService;
import com.example.demo.config.JwtUtil;
import com.example.demo.user.dto.SearchDTO;
import com.example.demo.user.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardController implements BoardDocs {

    private final BoardService boardService;
    private final JwtUtil jwtUtil;
    private final UsersService userService;

    @GetMapping("/mainboardList")
    public ResponseEntity<?> mainboardList(HttpServletRequest request) {
        try {
            String token = jwtUtil.token(request.getHeader("Authorization"));
            int userId = jwtUtil.getUserIdFromToken(token);
            List<BoardDTO> posts = boardService.getfollowPost(userId);
            if (posts == null || posts.isEmpty()) {
                posts = boardService.getRandomPost();
            }
            List<BoardLikeDTO> like = boardService.getfollowLike(posts);
            Map<String, Object> response = new HashMap<>();
            response.put("posts", posts);
            response.put("likes", like);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("mainboardList error" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("/boardList");
        }
    }

    @GetMapping("/userPosts")
    public ResponseEntity<?> userPosts(@RequestParam("userId") int userId, HttpServletRequest request) {
        try {
            String token = jwtUtil.token(request.getHeader("Authorization"));
            int myId = jwtUtil.getUserIdFromToken(token);
            System.out.println("어어그래");
            List<BoardDTO> posts = boardService.getPost(userId);
            System.out.println(posts);
            List<BoardLikeDTO> like = boardService.getLike(userId);

            boolean follow = boardService.existFollow(userId, myId);
            SearchDTO user = userService.userInfo(userId);
            Map<String, Object> response = new HashMap<>();

            response.put("posts", posts);
            response.put("likes", like);
            response.put("userInfo", user);
            response.put("follow", follow);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("userPosts error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("/userPosts");
        }
    }

    @PostMapping("/boardWrite")
    public ResponseEntity<?> writeBoard(@RequestParam("content") String content,
                                        @RequestParam(value = "img", required = false) List<MultipartFile> imgs, HttpServletRequest request) {
        try {
            String token = jwtUtil.token(request.getHeader("Authorization"));
            int userId = jwtUtil.getUserIdFromToken(token);
            String nickname = jwtUtil.getNickFromToken(token);

            BoardDTO boardDTO = new BoardDTO();
            boardDTO.setId(userId);
            boardDTO.setNickname(nickname);
            boardDTO.setContent(content);
            System.out.println(boardDTO);
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
    public ResponseEntity<?> boardLike(@RequestParam(value = "boardId") int boardId,
                                       @RequestParam(value = "writerId") int writerId, HttpServletRequest request) {
        try {
            String token = jwtUtil.token(request.getHeader("Authorization"));
            int userId = jwtUtil.getUserIdFromToken(token);
            BoardLikeDTO dto = new BoardLikeDTO();
            dto.setBoard_id(boardId);
            dto.setId(userId);
            int result = boardService.boardLike(dto, writerId);

            if (result == 0) {
                return ResponseEntity.ok("fail");
            } else if (result == 1) {
                return ResponseEntity.ok("success");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected result");
            }
        } catch (Exception e) {
            System.out.println("boardLike error : " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("boardLIke");
        }
    }

    @GetMapping("/getComments")
    public ResponseEntity<?> getComments(@RequestParam(value = "boardId") int boardId) {
        try {
            List<CommentDTO> comments = boardService.getComments(boardId);
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 불러오기 중 오류가 발생했습니다.");
        }
    }

    @PostMapping("/CommentWrite")
    public ResponseEntity<?> writeComment(@RequestBody CommentDTO dto, HttpServletRequest request) {
        try {
            String token = jwtUtil.token(request.getHeader("Authorization"));
            int userId = jwtUtil.getUserIdFromToken(token);
            dto.setId(userId);

            String nickname = jwtUtil.getNickFromToken(token);
            dto.setId(userId);
            dto.setNickname(nickname);
            boardService.writeComment(dto);
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 작성 중 오류가 발생했습니다.");
        }
    }

    @PostMapping("/boardUpdate")
    public ResponseEntity<?> updateBoard(@RequestParam("content") String content,
                                         @RequestParam(value = "img", required = false) List<MultipartFile> imgs,
                                         @RequestParam(value = "imgpath", required = false) String imgpath,
                                         @RequestParam("board_id") int board_id,
                                         HttpServletRequest request) {
        try {
            BoardDTO boardDTO = new BoardDTO();
            boardDTO.setBoard_id(board_id);
            boardDTO.setContent(content);

            if (imgpath != null && !imgpath.isEmpty()) {
                boardDTO.setImgpath(imgpath);
            } else if (imgs != null && !imgs.isEmpty()) {
                // 새로운 이미지가 첨부된 경우 기존 imgpath가 없더라도 처리
                boardDTO.setImgpath(""); // 새 이미지 경로를 설정할 수 있도록 빈 문자열 할당
            } else {
                boardDTO.setImgpath(null); // 기존 이미지 경로를 null로 설정
            }

            if (imgs != null && !imgs.isEmpty()) {
                boardDTO.setImg(imgs); // 새로운 이미지 파일 설정
            }

            boardService.updatePost(boardDTO);
            return ResponseEntity.ok("수정 완료");
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("글 update 실패.");
        }
    }


    @PostMapping("/EditComment")
    public ResponseEntity<?> updateComment(@RequestBody CommentDTO commentDTO) {
        try {
            boardService.updateComment(commentDTO);
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 update.");
        }
    }

    @PostMapping("/DeleteComment")
    public ResponseEntity<?> deleteComment(@RequestBody CommentDTO commentDTO) {
        try {
            boardService.DeleteComment(commentDTO);
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 update.");
        }
    }

    @PostMapping("/boardDelete")
    public ResponseEntity<?> deleteBoard(@RequestBody int board_id) {
        try {
            boardService.DeleteBoard(board_id);
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("글 삭제 중 오류가 발생했습니다.");
        }
    }

    @GetMapping("/findPost")
    public ResponseEntity<?> findPost(@RequestParam(value = "boardId") int boardId, HttpServletRequest request) {
        try {
            String token = jwtUtil.token(request.getHeader("Authorization"));
            int id = jwtUtil.getUserIdFromToken(token);

            BoardDTO post = boardService.getSharePost(boardId);
            List<BoardLikeDTO> like = boardService.getShareLike(boardId);

            Map<String, Object> response = new HashMap<>();
            response.put("posts", post);
            response.put("likes", like);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 불러오기 중 오류가 발생했습니다.");
        }
    }
}