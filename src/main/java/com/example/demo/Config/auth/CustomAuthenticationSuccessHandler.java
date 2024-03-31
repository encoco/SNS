package com.example.demo.Config.auth;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.example.demo.DTO.UsersDTO;
import com.example.demo.DTO.UsersInfoDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

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
            info.setId(userDTO.getId());
            info.setNickname("");
            info.setEmail(userDTO.getEmail());
            info.setPhone(userDTO.getPhone());
            info.setRole(userDTO.getRole());
            System.out.println(info);
            response.setContentType("application/json;charset=UTF-8");
            if(userDTO.getRole().equals("ROLE_USER")) {
				response.getWriter().write(new ObjectMapper().writeValueAsString(info));	//form 로그인
			} else { //form 로그인
				response.sendRedirect("http://localhost:3000?user=" + info);
			}
        }
    }
}