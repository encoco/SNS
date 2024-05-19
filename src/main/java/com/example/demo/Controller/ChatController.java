package com.example.demo.Controller;

import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.Config.JwtUtil;
import com.example.demo.DTO.ChatDTO;
import com.example.demo.Service.CharService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatController {
	private final SimpMessagingTemplate simpMessagingTemplate;
	private final JwtUtil jwtUtil;
	private final CharService chatService;

	@MessageMapping("/pub/chat")
	@SendTo("/sub/chat")
	public ChatDTO sendMessage(ChatDTO chatMessage) {
		System.out.println(chatMessage);
		return chatMessage; // 받은 메시지를 그대로 반환 (브로드캐스팅)
	}

	@GetMapping("/selectRoom")
	public ResponseEntity<?> selectRoom(HttpServletRequest request) {
		String token = jwtUtil.token(request.getHeader("Authorization"));
		int userId = jwtUtil.getUserIdFromToken(token);
		List<ChatDTO> dto = chatService.selectRoom(userId);
		try {
			if (dto != null) {
				System.out.println("있음");
				return ResponseEntity.ok(dto);
			}
			System.out.println("없음");
			return ResponseEntity.ok("채팅방 없음");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("selectRoom error");
		}
	}

	@PostMapping("/CreateRoom")
    public ResponseEntity<?> createChatRoom(@RequestBody Map<String, Object> requestData,HttpServletRequest request) {
		try {
			String token = jwtUtil.token(request.getHeader("Authorization"));
			int userId = jwtUtil.getUserIdFromToken(token);
			List<Integer> userIds = (List<Integer>) requestData.get("id");
			userIds.add(userId);
	        System.out.println("id : " + userIds);
	        ChatDTO dto = chatService.CreateRoom(userIds, userId);
	        return ResponseEntity.ok(dto);
		} catch(Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("selectRoom error");
		}
    }
}