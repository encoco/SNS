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
import com.example.demo.entity.Users;

@RestController
@RequestMapping("/api")
public class TestController {
	private final UsersRepository repository;

	@Autowired
    public TestController(UsersRepository repository) {
        this.repository = repository;
    }

	@GetMapping("/")
	public String index() {
		return "index.html";
	}

	@PostMapping("/Signup")
    public ResponseEntity<Users> signup(@RequestBody Users request) {
        Users savedUser = repository.save(request); // 사용자 정보를 저장
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED); // 저장된 사용자 정보와 함께 201 Created 상태 코드 반환
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
}

