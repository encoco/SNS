package com.example.demo.Config;

import java.io.IOException;

import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtFilter extends GenericFilterBean {
	private JwtUtil jwtUtil;

	public JwtFilter(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String requestURI = httpRequest.getRequestURI();
		if (requestURI.startsWith("/api/Login") || requestURI.startsWith("/api/Logout") || 
											requestURI.startsWith("/api/checkId") ||   requestURI.startsWith("/api/Signup")) {
			chain.doFilter(request, response);
			return;
		}
		String refreshToken = null;
		String accessToken = getTokenFromRequest(httpRequest);
		Cookie[] cookies = httpRequest.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("refreshToken".equals(cookie.getName())) {
					refreshToken = cookie.getValue();
					break;
				}
			}
		}

		if (requestURI.startsWith("/api/refresh")) {
            if (refreshToken != null) {
                httpRequest.setAttribute("refreshToken", refreshToken);
                chain.doFilter(httpRequest, response);
            } else {
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Refresh token is missing");
            }
            return;
        }

		 if (accessToken != null && jwtUtil.validateToken(accessToken)) {
		        chain.doFilter(request, response);
		 } else {
		        System.out.println("토큰 만료");
		        httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		 }
	}

	private String getTokenFromRequest(HttpServletRequest request) {
		String authorizationHeader = request.getHeader("Authorization");
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring(7, authorizationHeader.length()); // "Bearer " 이후의 문자열을
																								// 토큰으로 추출
			return token;
		}
		return null;
	}

}
