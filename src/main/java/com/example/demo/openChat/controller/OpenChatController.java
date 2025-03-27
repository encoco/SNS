package com.example.demo.openChat.controller;

import com.example.demo.openChat.docs.OpenDocs;
import com.example.demo.openChat.dto.OpenChatDTO;
import com.example.demo.openChat.dto.OpenChatMemberDTO;
import com.example.demo.openChat.dto.OpenChatMessageDTO;
import com.example.demo.openChat.service.OpenChatService;
import com.example.demo.config.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OpenChatController implements OpenDocs {
    private final JwtUtil jwtUtil;
    private final OpenChatService openChatService;


    @MessageMapping("/commChat/{roomNumber}")
    @SendTo("/api/sub/commChat/{roomNumber}")
    public OpenChatMessageDTO handleCommChatMessage(@DestinationVariable("roomNumber") int roomNumber, @Payload OpenChatMessageDTO message) {
        String token = message.getNickname();
        message.setOpenChatId(roomNumber);
        message.setId(jwtUtil.getUserIdFromToken(token));
        return openChatService.saveCommChat(message);
    }

    @GetMapping("/selectAllCommuRoom")
    public ResponseEntity<?> selectAllCommuRoom(HttpServletRequest request) {
        List<OpenChatDTO> dto = openChatService.selectAllCommuRoom();
        try {
            if (dto != null) {
                return ResponseEntity.ok(dto);
            }
            return ResponseEntity.ok("채팅방 없음");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("selectRoom error");
        }
    }

    @PostMapping("/CreateCommChat")
    public ResponseEntity<?> createCommChat(@ModelAttribute OpenChatDTO dto, HttpServletRequest request) {
        try {
            String token = jwtUtil.token(request.getHeader("Authorization"));
            dto.setId(jwtUtil.getUserIdFromToken(token));
            openChatService.CreateCommChat(dto);
            return ResponseEntity.ok("");

        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("selectRoom error");
        }
    }


    @GetMapping("/getCommMessage")
    public ResponseEntity<?> getCommMessage(@RequestParam(value = "openChatId") int openChatId) {
        List<OpenChatMessageDTO> dto = openChatService.getCommMessage(openChatId);
        System.out.println("openMessage : " + dto);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/JoinCommuRoom")
    public ResponseEntity<?> joinCommuRoom(@RequestBody OpenChatDTO dto, HttpServletRequest request) {
        String token = jwtUtil.token(request.getHeader("Authorization"));
        dto.setId(jwtUtil.getUserIdFromToken(token));
        openChatService.joinCommunity(dto);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/OpenRoom")
    public ResponseEntity<?> selectCommuRoom(HttpServletRequest request) {
        String token = jwtUtil.token(request.getHeader("Authorization"));
        List<OpenChatMemberDTO> dto = openChatService.selectCommuRoom(jwtUtil.getUserIdFromToken(token));
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
