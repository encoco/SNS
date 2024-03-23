package com.example.demo.Config.auth;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import DTO.UsersDTO;
import DTO.UsersInfoDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
    	Object principal = authentication.getPrincipal();
        
        if (principal instanceof PrincipalDetails) {
            UsersDTO userDTO = ((PrincipalDetails) principal).getUsersDTO();
            UsersInfoDTO info = new UsersInfoDTO();
            info.setNickname("");
            info.setEmail(userDTO.getEmail());
            info.setPhone(userDTO.getPhone());
            info.setRole(userDTO.getRole());
            System.out.println(info);
            response.setContentType("application/json;charset=UTF-8");
            if(userDTO.getRole().equals("ROLE_USER")) response.getWriter().write(new ObjectMapper().writeValueAsString(info));	//form 로그인
            else response.sendRedirect("http://localhost:3000?user=" + info);	
        }
    }
}