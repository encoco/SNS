package com.example.demo.Config;

import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtFilter extends GenericFilterBean {

    private JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
    	HttpServletResponse httpResponse = (HttpServletResponse)response;
    	HttpServletRequest httpRequest = (HttpServletRequest) request;
    	
        String requestURI = httpRequest.getRequestURI();
        if (requestURI.startsWith("/api/Login") || requestURI.startsWith("/api/Logout")) {
            chain.doFilter(request, response);
            return;
        }//  여기까지
        
        String token = getTokenFromRequest((HttpServletRequest) request);
        boolean validate = jwtUtil.validateToken(token);
        if (token != null && validate) { //여기서부터 다시 시작
        	System.out.println("인증 성공");
        	chain.doFilter(request, response);
        }
        if(!validate) {
        	String newToken = "new Token";//jwtUtil.refreshToken(token); // 새로운 토큰 생성
            httpResponse.setHeader("Authorization", "Bearer " + newToken); // 응답 헤더에 새 토큰 추가
            chain.doFilter(request, response); // 요청 계속 처리
            
        }
    }

    private String getTokenFromRequest(HttpServletRequest request) {
    	String authorizationHeader = request.getHeader("Authorization");
	    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
	        String token = authorizationHeader.substring(8,authorizationHeader.length()-1); // "Bearer " 이후의 문자열을 토큰으로 추출
	        return token;
	    }
        return null;
    }
    
}
