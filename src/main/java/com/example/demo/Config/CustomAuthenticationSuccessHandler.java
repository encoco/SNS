package com.example.demo.Config;

import com.example.demo.Config.auth.PrincipalDetails;
import com.fasterxml.jackson.databind.ObjectMapper;

import DTO.UsersDTO;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        Object principal = authentication.getPrincipal();
        if (principal instanceof PrincipalDetails) {
            UsersDTO userDTO = ((PrincipalDetails) principal).getUsersDTO();
            System.out.println(userDTO.toString());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(new ObjectMapper().writeValueAsString(userDTO));
        }
    }
}