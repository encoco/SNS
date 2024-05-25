package com.example.demo.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Config.JwtUtil;
import com.example.demo.DTO.ChatDTO;
import com.example.demo.DTO.ChatMessageDTO;
import com.example.demo.Service.ChatService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatController {
	private final JwtUtil jwtUtil;
	private final ChatService chatService;
	private final SimpMessageSendingOperations template;

	// 채팅 리스트 반환
	@MessageMapping("/chat/{roomNumber}")
    @SendTo("/api/sub/chat/{roomNumber}")
    public ChatMessageDTO handleChatMessage(@DestinationVariable("roomNumber") String roomNumber, @Payload ChatMessageDTO message) {
		String token = message.getNickname();
        message.setRoom_number(roomNumber);
        message.setId(jwtUtil.getUserIdFromToken(token));
        message.setNickname(jwtUtil.getNickFromToken(token));
        System.out.println("ChatController : " + message);
        chatService.saveChat(message);
        return message; // 클라이언트로 메시지 반환
    }

	@GetMapping("/getMessage")
	public ResponseEntity<?> getMessage(@RequestParam(value="roomNumber") String roomNumber){
		System.out.println(roomNumber);
		List<ChatMessageDTO> dto = chatService.getMessage(roomNumber);
		return ResponseEntity.ok(dto);
	}

	// 메시지 송신 및 수신, /pub가 생략된 모습. 클라이언트 단에선 /pub/message로 요청
	@MessageMapping("/message")
	public ResponseEntity<Void> receiveMessage(@RequestBody ChatMessageDTO chat) {
		// 메시지를 해당 채팅방 구독자들에게 전송
		System.out.println("CM : " + chat);
		template.convertAndSend("/sub/chatroom/1", chat);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/selectRoom")
	public ResponseEntity<?> selectRoom(HttpServletRequest request) {
		String token = jwtUtil.token(request.getHeader("Authorization"));
		int userId = jwtUtil.getUserIdFromToken(token);
		List<ChatDTO> dto = chatService.selectRoom(userId);
		try {
			if (dto != null) {
				return ResponseEntity.ok(dto);
			}
			return ResponseEntity.ok("채팅방 없음");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("selectRoom error");
		}
	}

	@PostMapping("/CreateRoom")
	public ResponseEntity<?> createChatRoom(@RequestBody Map<String, Object> requestData, HttpServletRequest request) {
		try {
			String token = jwtUtil.token(request.getHeader("Authorization"));
			int userId = jwtUtil.getUserIdFromToken(token);
			List<Integer> userIds = (List<Integer>) requestData.get("id");
			ChatDTO dto;
			userIds.add(userId);
			Map<String, Object> response = new HashMap<>();

			dto = chatService.findRoom(userIds, userId);
			if (dto != null) { // 찾아서 있으면 이미 있으니까 그 방으로 넘기기
				response.put("1", dto);
				return ResponseEntity.ok(response);
			}
			// 위에서 return 안되면 없으니까 생성하고 생성한 정보 넘기기
			dto = chatService.CreateRoom(userIds, userId);
			response.put("0", dto);
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			System.out.println(e);
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("selectRoom error");
		}
	}


}