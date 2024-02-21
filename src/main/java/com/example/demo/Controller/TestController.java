package com.example.demo.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.Repository.UsersRepository;
import com.example.demo.Service.UsersService;
import com.example.demo.entity.Users;

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

	@GetMapping("/")
	public String index() {
		return "index.html";
	}

	@PostMapping("/checkId")
    public ResponseEntity<?> checkId(@RequestBody Users request) {
		boolean exists = repository.existsById(request.getId());
        if (exists) {
            // ID가 이미 존재하는 경우, 클라이언트에게 중복임을 알리는 응답을 보냅니다.
        	return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("isDuplicate", true, "message", "이미 사용 중인 ID입니다."));
        } else {
            // ID가 중복되지 않는 경우, 사용 가능하다는 응답을 보냅니다.
            return ResponseEntity.ok(Map.of("isDuplicate", false, "message", "사용 가능한 ID입니다."));
        }
    }

	@PostMapping("/Signup")
    public ResponseEntity<Users> signup(@RequestBody Users request) {
        Users savedUser = Uservice.registerUser(request); // 서비스 계층을 통해 사용자 정보 저장
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED); // 저장된 사용자 정보와 함께 201 Created 상태 코드 반환
    }
	
	@PostMapping("/Login")
	public ResponseEntity<Users> login(@RequestBody Users request, HttpSession session) {
		Users auth = Uservice.findbyId(request); // 0: 비밀번호 틀림, 1: 로그인 성공, 2: 계정 없음
	    if (auth != null) { // 로그인 성공
	        session.setAttribute("user", auth); // 세션에 사용자 정보 저장
	        return new ResponseEntity<>(auth, HttpStatus.OK); // 성공적으로 로그인 처리 후 사용자 정보와 함께 200 OK 상태 코드 반환
	    } else { // 계정 없거나 틀림
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found 상태 코드 반환
	    }
	}
	@GetMapping("/Logout")
    public ResponseEntity<?> logout(HttpSession session) {
        // 세션 무효화
        session.invalidate();
        return new ResponseEntity<>("You've been logged out successfully.", HttpStatus.OK);
    }
}

