package com.example.demo.Config.auth;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

	@Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        System.out.println("실패");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 상태 코드 설정
		response.sendRedirect("http://localhost:3000");
        response.getWriter().append("Authentication failed");
    }
}