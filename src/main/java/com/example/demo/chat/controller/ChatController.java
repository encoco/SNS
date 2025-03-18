package com.example.demo.chat.controller;

import com.example.demo.chat.ChatDocs;
import com.example.demo.chat.dto.ChatDTO;
import com.example.demo.chat.dto.ChatMessageDTO;
import com.example.demo.chat.service.ChatService;
import com.example.demo.communityChat.dto.CCJDTO;
import com.example.demo.communityChat.dto.CCMDTO;
import com.example.demo.communityChat.dto.CommunityChatDTO;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatController implements ChatDocs {
    private final JwtUtil jwtUtil;
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    // 채팅 리스트 반환
    @MessageMapping("/chat/{roomNumber}")
    @SendTo("/api/sub/chat/{roomNumber}")
    public ChatMessageDTO handleChatMessage(@DestinationVariable("roomNumber") String roomNumber,
                                            @Payload ChatMessageDTO message) {
        String token = message.getNickname();

        message.setRoom_number(roomNumber);
        message.setId(jwtUtil.getUserIdFromToken(token));
        return chatService.saveChat(message);
    }

    @MessageMapping("/commChat/{roomNumber}")
    @SendTo("/api/sub/commChat/{roomNumber}")
    public CCMDTO handleCommChatMessage(@DestinationVariable("roomNumber") int roomNumber, @Payload CCMDTO message) {
        String token = message.getNickname();
        message.setCommunitychat_id(roomNumber);
        message.setId(jwtUtil.getUserIdFromToken(token));
        return chatService.saveCommChat(message);
    }

    @GetMapping("/getMessage")
    public ResponseEntity<?> getMessage(@RequestParam(value = "roomNumber") String roomNumber,
                                        HttpServletRequest request) {
        List<ChatMessageDTO> dto = chatService.getMessage(roomNumber);
        System.out.println("userchat : " + dto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/getCommMessage")
    public ResponseEntity<?> getCommMessage(@RequestParam(value = "communitychat_id") String communitychat_id) {
        List<CCMDTO> dto = chatService.getCommMessage(Integer.parseInt(communitychat_id));
        System.out.println("Commmessage : " + dto);
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
    public ResponseEntity<?> joinCommuRoom(@RequestBody CommunityChatDTO dto, HttpServletRequest request) {
        String token = jwtUtil.token(request.getHeader("Authorization"));
        dto.setId(jwtUtil.getUserIdFromToken(token));
        chatService.joinCommunity(dto);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/selectCommuRoom")
    public ResponseEntity<?> selectCommuRoom(HttpServletRequest request) {
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

        if (cdto != null) { // 찾아서 있으면 이미 있으니까 그 방으로 넘기기
            dto.setRoom_number(cdto.getRoomNumber());
            chatService.saveChat(dto);
            messagingTemplate.convertAndSend("/api/sub/chat/" + cdto.getRoomNumber(), dto);
            return ResponseEntity.ok(null);
        }

        // 위에서 return 안되면 없으니까 생성하고 생성한 정보 넘기기
        ChatDTO Chat_DTO = chatService.CreateRoom(userIds, userId);
        dto.setRoom_number(Chat_DTO.getRoomNumber());
        chatService.saveChat(dto);
        messagingTemplate.convertAndSend("/api/sub/chat/" + Chat_DTO.getRoomNumber(), dto);

        return ResponseEntity.ok(null);
    }

    @GetMapping("/findRoom")
    public ResponseEntity<?> findRoom(HttpServletRequest request, @RequestParam("id") int id) {
        String token = jwtUtil.token(request.getHeader("Authorization"));
        int myId = jwtUtil.getUserIdFromToken(token);
        return ResponseEntity.ok(chatService.findRoom(id, myId));
    }
}