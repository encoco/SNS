package com.example.demo.Config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.example.demo.Config.auth.PrincipalDetails;
import com.example.demo.DTO.UsersDTO;

import java.util.Date;

@Component
public class JwtUtil {

	@Value("${jwt.secret}")
	private String secret;
    
    // JWT 생성
 // JWT 생성 - 공통 메소드
    public String generateToken(PrincipalDetails userDetails, long expirationTime) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);
        
        UsersDTO userDTO = userDetails.getUsersDTO();
        int id = userDTO.getId(); 
        String nickname = userDTO.getNickname();
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secret.getBytes()) 
                .claim("id", id) // JWT에 사용자의 id 추가
                .claim("nickname", nickname) // JWT에 사용자의 nickname 추가
                .compact();
    }
    
    // JWT로부터 사용자 이름 추출
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // JWT 유효성 검증
    public boolean validateToken(String token) {
    	try {
    		isTokenExpired(token);
    	}catch(ExpiredJwtException e){
    		return false;
    	}catch(Exception e) {
    		return false;
    	}
        return (!isTokenExpired(token));
    }

    // 토큰 만료 확인
    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser()
                .setSigningKey(secret.getBytes())
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }
    
    public int getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                            .setSigningKey(secret.getBytes()) 
                            .parseClaimsJws(token)
                            .getBody();
        return Integer.parseInt(claims.get("id").toString()); 
    }
    
    public Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret.getBytes())
                .parseClaimsJws(token)
                .getBody();
    }
}