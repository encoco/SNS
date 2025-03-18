package com.example.demo.follow.controller;

import com.example.demo.config.JwtUtil;
import com.example.demo.follow.docs.FollowDocs;
import com.example.demo.follow.service.FollowService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FollowController implements FollowDocs {
    private final JwtUtil jwtutil;
    private final FollowService followservice;

    @GetMapping("/userFollow")
    public ResponseEntity<?> followUser(@RequestParam("userId") int userId, HttpServletRequest request) {
        try {
            //프런트에서 던져주는거보다 토큰 인증하고 보는게 보안상 좋지 않을까?
            String token = jwtutil.token(request.getHeader("Authorization"));
            int myId = jwtutil.getUserIdFromToken(token);

            if (followservice.followUser(myId,userId).equals("del")) {
                return ResponseEntity.ok("삭제");
            } else {
                return ResponseEntity.ok("추가");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("error");
        }
    }
}
