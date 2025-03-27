package com.example.demo.chat.controller;

import com.example.demo.chat.docs.ChatDocs;
import com.example.demo.chat.dto.ChatDTO;
import com.example.demo.chat.dto.ChatMessageDTO;
import com.example.demo.chat.service.ChatService;
import com.example.demo.config.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatController implements ChatDocs {
    private final JwtUtil jwtUtil;
    private final ChatService chatService;

    // 채팅 리스트 반환
    @MessageMapping("/chat/{roomNumber}")
    @SendTo("/api/sub/chat/{roomNumber}")
    public ChatMessageDTO handleChatMessage(@DestinationVariable("roomNumber") int chat_id,
                                            @Payload ChatMessageDTO message) {
        String token = message.getNickname();

        message.setChat_id(chat_id);
        message.setId(jwtUtil.getUserIdFromToken(token));
        return chatService.saveChat(message);
    }

    @GetMapping("/UserMessage")
    public ResponseEntity<?> getMessage(@RequestParam(value = "userChatId") int userChatId, HttpServletRequest request) {
        List<ChatMessageDTO> dto = chatService.getMessage(userChatId);
        System.out.println("선택한  메시지는~~ : " + dto);
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

            ChatDTO dto = chatService.findOrCreateRoom(userIds, userId);
            System.out.println("findOrCreateRoom 결과 : " + dto);
            if (dto.isNew_room()) {//생성하고 생성한 정보 넘기기
                response.put("0", dto);
                return ResponseEntity.ok(response);
            }
            // 찾아서 있으면 이미 있으니까 그 방으로 넘기기
            response.put("1", dto);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("selectRoom error");
        }
    }




    @PostMapping("/SharePost")
    public ResponseEntity<?> sharePost(@RequestBody Map<String, Object> requestData, HttpServletRequest request) {
        String token = jwtUtil.token(request.getHeader("Authorization"));
        int userId = jwtUtil.getUserIdFromToken(token);
        List<Integer> userIds = (List<Integer>) requestData.get("Ids");
        int board_id = (Integer) requestData.get("board_id");

        userIds.add(userId);
        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setId(userId);
        dto.setShare_board_id(board_id);
        ChatDTO cdto = chatService.findRoom(userIds, userId);

//        if (cdto != null) { // 찾아서 있으면 이미 있으니까 그 방으로 넘기기
//            dto.setRoom_number(cdto.getRoomNumber());
//            chatService.saveChat(dto);
//            messagingTemplate.convertAndSend("/api/sub/chat/" + cdto.getRoomNumber(), dto);
//            return ResponseEntity.ok(null);
//        }
//
//        // 위에서 return 안되면 없으니까 생성하고 생성한 정보 넘기기
//        ChatDTO Chat_DTO = chatService.createRoom(userIds, userId);
//        dto.setRoom_number(Chat_DTO.getRoomNumber());
//        chatService.saveChat(dto);
//        messagingTemplate.convertAndSend("/api/sub/chat/" + Chat_DTO.getRoomNumber(), dto);

        return ResponseEntity.ok(null);
    }

    @GetMapping("/findRoom")
    public ResponseEntity<?> findRoom(HttpServletRequest request, @RequestParam("id") int id) {
        String token = jwtUtil.token(request.getHeader("Authorization"));
        int myId = jwtUtil.getUserIdFromToken(token);
        return ResponseEntity.ok(chatService.findOrCreateRoom(Collections.singletonList(id), myId));
    }
}