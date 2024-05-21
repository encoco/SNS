package com.example.demo.Config; 

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;

    @Autowired
    public JwtChannelInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor != null && accessor.getCommand() == StompCommand.CONNECT) {
            String authorizationHeader = accessor.getFirstNativeHeader("Authorization");
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String jwt = authorizationHeader.substring(7);
                System.out.println("jwt : " + jwt);
                if (jwtUtil.validateToken(jwt)) {
                    int userId = jwtUtil.getUserIdFromToken(jwt);
                    String nickname = jwtUtil.getNickFromToken(jwt);
                    
                    System.out.println("jwtinterceptor id : " + userId);
                    System.out.println("jwtinterceptor nn : " + nickname);
                    // 사용자 정보를 설정
                    UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(nickname, null, Collections.emptyList());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    accessor.setUser(authentication);
                } else {
                    // JWT 검증 실패 처리
                    SecurityContextHolder.clearContext();
                }
            }
        }

        return message;
    }
}