package com.example.demo.Controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.Config.JwtUtil;
import com.example.demo.Config.auth.PrincipalDetails;
import com.example.demo.DTO.UsersDTO;
import com.example.demo.Repository.UsersRepository;
import com.example.demo.Service.UsersService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TestController {
	private final UsersRepository repository;
	private final UsersService Uservice;
	private final JwtUtil jwtutil;

	@PostMapping("/checkId")
    public ResponseEntity<?> checkId(@RequestBody UsersDTO request) {
		System.out.println("CheckId in");
		boolean username = repository.existsByUsername(request.getUsername());
		boolean nickname = repository.existsByNickname(request.getNickname());
        if (username) {
        	return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("isDuplicate", true, "message", "이미 사용 중인 아이디입니다."));
        } 
        else if(nickname) {
        	return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("isDuplicate", true, "message", "이미 사용 중인 닉네임입니다."));
        } else {
            // ID가 중복되지 않는 경우, 사용 가능하다는 응답을 보냅니다.
            return ResponseEntity.ok(Map.of("isDuplicate", false, "message", "사용 가능한 ID입니다."));
        }
    }

	@PostMapping("/Signup")
	public ResponseEntity<UsersDTO> signup(@RequestBody UsersDTO request) {
		Uservice.registerUser(request);
	    return new ResponseEntity<>(HttpStatus.CREATED); // ID가 설정된 기본 DTO와 함께 반환
	}

	@GetMapping("/Logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", null); // 쿠키 이름을 refreshToken으로 변경
        refreshTokenCookie.setMaxAge(0); // 쿠키의 만료 시간을 0으로 설정하여 즉시 만료
        refreshTokenCookie.setPath("/"); // 모든 경로에서 유효한 쿠키로 설정
        response.addCookie(refreshTokenCookie); // 쿠키를 응답에 추가하여 클라이언트에 전송, 삭제됨을 알림

        return new ResponseEntity<>("You've been logged out successfully.", HttpStatus.OK);
    }
	
	@PostMapping("/test")
	public ResponseEntity<?> test(@RequestParam(name="code") String code) {
		System.out.println(code);
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("완");
	}
	
	@PostMapping("/refreshToken")
	public ResponseEntity<?> refreshToken(@RequestBody String refreshToken){
	    try {
	    	String[] parts = refreshToken.split("\":\"", 2);
	        String rawToken = parts[1].substring(0, parts[1].length() - 2);
	        String token = rawToken.replace("\\\"", "");
	        System.out.println("token : " + token);
	        
	        int id = jwtutil.getUserIdFromToken(token);
	        UsersDTO dto = new UsersDTO();
	        PrincipalDetails userDetails = new PrincipalDetails(dto);
	        // 새 액세스 토큰 생성
	        String newAccessToken = jwtutil.generateToken(userDetails,300);
	        System.out.println("newtoken : " + newAccessToken);

	        return ResponseEntity.ok(newAccessToken);
	    } catch (Exception e) {
	    	e.printStackTrace(); // 이를 통해 예외의 상세 정보를 콘솔에 출력
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("다시 로그인 해주세요");
	    }
	}
}

