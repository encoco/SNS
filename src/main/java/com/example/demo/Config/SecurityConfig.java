package com.example.demo.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import com.example.demo.Config.auth.CustomAuthenticationFailureHandler;
import com.example.demo.Config.auth.CustomAuthenticationSuccessHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final PrincipalOauth2UserService principalOauth2UserService;

	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors().and() // CORS 설정 을 활성화합니다.
            .csrf().disable() // CSRF 보호 기능을 비활성화합니다.
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).and()
            .authorizeRequests(authorizeRequests ->
                authorizeRequests
                    .requestMatchers("/",  "/login/**",  "/oauth2/**",  "/api/**",  "/user/**").permitAll()
                    .anyRequest().authenticated() // 위에 지정된 경로를 제외한 모든 요청은 인증이 필요합니다. user, admin,
            )
            .formLogin(formLogin ->
                formLogin
                    .loginProcessingUrl("/api/Login") // 로그인 처리 경로를 '/api/Login'으로 지정합니다.
                    .successHandler(new CustomAuthenticationSuccessHandler()) //성공
                    .failureHandler(new CustomAuthenticationFailureHandler()) //실패
            )
            .oauth2Login(oauth2Login ->
	            oauth2Login
	                .loginPage("/") // 사용자 정의 로그인 페이지 URL을 루트 경로로 설정합니다.
	                .userInfoEndpoint() // OAuth2 사용자 정보를 처리합니다.
	                    .userService(principalOauth2UserService) // OAuth2 사용자 서비스를 설정합니다.
	                    .and()
	                .successHandler(new CustomAuthenticationSuccessHandler()) // 로그인 성공 핸들러를 설정합니다.
	                .failureHandler(new CustomAuthenticationFailureHandler()) //실패
	        )
            .logout(logout ->
                logout.permitAll() // 로그아웃 허용
            );
        return http.build(); // 구성된 HttpSecurity 객체를 반환합니다.
    }

}