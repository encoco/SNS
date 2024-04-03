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
    public ResponseEntity<?> logout(HttpSession session) {
        // 세션 무효화
        session.invalidate();
        return new ResponseEntity<>("You've been logged out successfully.", HttpStatus.OK);
    }
}

