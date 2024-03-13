package com.example.demo.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

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
                    .requestMatchers("/", "/login", "/oauth2/**", "/api/**", "/user/**").permitAll() // 특정 경로는 인증 없이 접근이 허용됩니다.
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
            )
            .logout(logout ->
                logout.permitAll() // 로그아웃 허용
            );
        return http.build(); // 구성된 HttpSecurity 객체를 반환합니다.
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
