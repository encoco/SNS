package com.example.demo.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Config.auth.PrincipalDetails;
import com.example.demo.DTO.UsersDTO;
import com.example.demo.Repository.UsersRepository;
import com.example.demo.Service.UsersService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api")
public class TestController {
	private final UsersRepository repository;
	private final UsersService Uservice;

	@Autowired
    public TestController(UsersRepository repository, UsersService Uservice) {
        this.repository = repository;
        this.Uservice = Uservice;
    }


	@PostMapping("/checkId")
    public ResponseEntity<?> checkId(@RequestBody UsersDTO request) {
		System.out.println("CheckId in");
		boolean exists = repository.existsByUsername(request.getUsername());
        if (exists) {
            // ID가 이미 존재하는 경우, 클라이언트에게 중복임을 알리는 응답을 보냅니다.
        	return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("isDuplicate", true, "message", "이미 사용 중인 ID입니다."));
        } else {
            // ID가 중복되지 않는 경우, 사용 가능하다는 응답을 보냅니다.
            return ResponseEntity.ok(Map.of("isDuplicate", false, "message", "사용 가능한 ID입니다."));
        }
    }


	@PostMapping("/Signup")
	public ResponseEntity<UsersDTO> signup(@RequestBody UsersDTO request) {
	    String userId = Uservice.registerUser(request);
	    UsersDTO savedUser = new UsersDTO();
	    savedUser.setUsername(userId);
	    return new ResponseEntity<>(savedUser, HttpStatus.CREATED); // ID가 설정된 기본 DTO와 함께 반환
	}

	// 8080/user/info 접근 시 : {"id":14,"username":"t","password":"대충암호화","phone":"01012311234","email":"wdjw@naf.com","role":"ROLE_USER"}
	@CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/user/info")
    public ResponseEntity<UsersDTO> userInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
        if (authentication != null && authentication.getPrincipal() instanceof PrincipalDetails) {
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            UsersDTO userDTO = principalDetails.getUsersDTO();
            return ResponseEntity.ok(userDTO); // 사용자 정보를 JSON 형태로 반환
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null); // 인증되지 않은 접근 처리
    }

	@CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/auth/test")
    public String testSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            // 인증 객체의 주요 정보 로그로 출력
            return "Authentication: " + authentication.toString() + "\n" +
                    "Principal: " + authentication.getPrincipal().toString() + "\n" +
                    "Authorities: " + authentication.getAuthorities().toString();
        }
        return "No authentication information found.";
    }

	@GetMapping("/Logout")
    public ResponseEntity<?> logout(HttpSession session) {
        // 세션 무효화
        session.invalidate();
        return new ResponseEntity<>("You've been logged out successfully.", HttpStatus.OK);
    }
}

