package com.example.demo.Config.auth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.demo.Config.JwtUtil;
import com.example.demo.DTO.UsersDTO;
import com.example.demo.DTO.UsersInfoDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private final JwtUtil jwtUtil;
	@Value("${jwt.expirationTime.access}")
    private long accessExpirationTime;
	@Value("${jwt.expirationTime.refresh}")
    private long refreshExpirationTime;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
    	Object principal = authentication.getPrincipal();
        if (principal instanceof PrincipalDetails) {
        	PrincipalDetails userDetails = (PrincipalDetails) principal;
        	String accessToken = jwtUtil.generateToken(userDetails,accessExpirationTime); // Access Token 생성
            String refreshToken = jwtUtil.generateToken(userDetails,refreshExpirationTime); // Refresh Token 생성
            UsersDTO userDTO = userDetails.getUsersDTO();
            UsersInfoDTO dto = UsersInfoDTO.toInfoDTO(userDTO);
            
            ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
    	            .httpOnly(true)
    	            .path("/")
    	            .build();
    	    response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            // Access Token만 JSON 응답으로 반환
    	    Map<String, Object> tokenMap = new HashMap<>();
    	    tokenMap.put("accessToken", accessToken);
    	    tokenMap.put("nickname", dto); // UsersDTO 객체를 그대로 추가
    	    
            String tokensJson = new ObjectMapper().writeValueAsString(tokenMap);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(tokensJson); // 클라이언트로 응답 전송
            if(userDTO.getRole().equals("ROLE_USER_SNS")) {
            	response.sendRedirect("http://localhost:3000/?tokensJson=" + tokensJson);
            }
        }
    }
}