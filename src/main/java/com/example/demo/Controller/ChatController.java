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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Config.JwtUtil;
import com.example.demo.DTO.CCJDTO;
import com.example.demo.DTO.CCMDTO;
import com.example.demo.DTO.ChatDTO;
import com.example.demo.DTO.ChatMessageDTO;
import com.example.demo.DTO.CommunityChatDTO;
import com.example.demo.DTO.UsersInfoDTO;
import com.example.demo.Service.ChatService;

import io.jsonwebtoken.io.IOException;
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
	public ChatMessageDTO handleChatMessage(@DestinationVariable("roomNumber") String roomNumber,
			@Payload ChatMessageDTO message) {
		String token = message.getNickname();
		// 오픈채팅은 1부터 AutoIncrement 설정이고 일반채팅은 UUID를 통한 값이기 때문에 parseInt에서 에러가 안뜨면 오픈채팅.
		message.setRoom_number(roomNumber);
		message.setId(jwtUtil.getUserIdFromToken(token));
		message.setNickname(jwtUtil.getNickFromToken(token));
		return chatService.saveChat(message);
	}

	@MessageMapping("/commChat/{roomNumber}")
	@SendTo("/api/sub/commChat/{roomNumber}")
	public CCMDTO handleCommChatMessage(@DestinationVariable("roomNumber") int roomNumber,
			@Payload CCMDTO message) {
		String token = message.getNickname();
		message.setCommunitychat_id(roomNumber);
		message.setId(jwtUtil.getUserIdFromToken(token));
		message.setNickname(jwtUtil.getNickFromToken(token));
		return chatService.saveCommChat(message);
	}

	@GetMapping("/getMessage")
	public ResponseEntity<?> getMessage(@RequestParam(value = "roomNumber") String roomNumber, HttpServletRequest request) {
		List<ChatMessageDTO> dto = chatService.getMessage(roomNumber);
		return ResponseEntity.ok(dto);
	}
	
	@GetMapping("/getCommMessage")
	public ResponseEntity<?> getCommMessage(@RequestParam(value = "communitychat_id") String communitychat_id) {
		List<CCMDTO> dto = chatService.getCommMessage(Integer.parseInt(communitychat_id));
		System.out.println(dto);
		return ResponseEntity.ok(dto);
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
			userIds.add(userId);
			Map<String, Object> response = new HashMap<>();

			ChatDTO dto = chatService.findRoom(userIds, userId);
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

	@PostMapping("/CreateCommChat")
	public ResponseEntity<?> createCommChat(@ModelAttribute CommunityChatDTO dto, HttpServletRequest request) {
		try {
			String token = jwtUtil.token(request.getHeader("Authorization"));
			dto.setId(jwtUtil.getUserIdFromToken(token));
			chatService.CreateCommChat(dto);
			return ResponseEntity.ok("");

		} catch (Exception e) {
			System.out.println(e);
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("selectRoom error");
		}
	}

	@GetMapping("/selectAllCommuRoom")
	public ResponseEntity<?> selectAllCommuRoom(HttpServletRequest request) {
		List<CommunityChatDTO> dto = chatService.selectAllCommuRoom();
		try {
			if (dto != null) {
				return ResponseEntity.ok(dto);
			}
			return ResponseEntity.ok("채팅방 없음");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("selectRoom error");
		}
	}

	@PostMapping("/JoinCommuRoom")
	public ResponseEntity<?> JoinCommuRoom(@RequestBody CommunityChatDTO dto, HttpServletRequest request) {
		String token = jwtUtil.token(request.getHeader("Authorization"));
		dto.setId(jwtUtil.getUserIdFromToken(token));
		System.out.println(dto);
		chatService.joinCommunity(dto);
		return ResponseEntity.ok(null);
	}

	@GetMapping("/selectCommuRoom")
	public ResponseEntity<?> selCommuRoom(HttpServletRequest request) {
		String token = jwtUtil.token(request.getHeader("Authorization"));
		List<CCJDTO> dto = chatService.selectCommuRoom(jwtUtil.getUserIdFromToken(token));
		System.out.println(dto);
		try {
			if (dto != null) {
				return ResponseEntity.ok(dto);
			}
			return ResponseEntity.ok("채팅방 없음");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("selectRoom error");
		}
	}
}