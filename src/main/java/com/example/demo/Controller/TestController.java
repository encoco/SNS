package com.example.demo.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Config.JwtUtil;
import com.example.demo.DTO.SearchDTO;
import com.example.demo.DTO.UsersDTO;
import com.example.demo.DTO.UsersInfoDTO;
import com.example.demo.Repository.UsersRepository;
import com.example.demo.Service.UsersService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
		boolean username = repository.existsByUsername(request.getUsername());
		boolean nickname = repository.existsByNickname(request.getNickname());
		if (username) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(Map.of("isDuplicate", true, "message", "이미 사용 중인 아이디입니다."));
		} else if (nickname) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(Map.of("isDuplicate", true, "message", "이미 사용 중인 닉네임입니다."));
		} else {
			// ID가 중복되지 않는 경우, 사용 가능하다는 응답을 보냅니다.
			return ResponseEntity.ok(Map.of("isDuplicate", false, "message", "사용 가능한 ID입니다."));
		}
	}

	@PostMapping("/Signup")
	public ResponseEntity<UsersDTO> signup(@RequestBody UsersDTO request) {
		System.out.println("signup");
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

	@PostMapping("/refresh")
	public ResponseEntity<?> refreshToken(HttpServletRequest request) {
		// 리프레시 토큰 검증 로직
		String refreshToken = (String) request.getAttribute("refreshToken");
		if (refreshToken != null && jwtutil.validateToken(refreshToken)) {
			String newAccessToken = jwtutil.newAccessToken(refreshToken);

			return ResponseEntity.ok().body(newAccessToken);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Refresh Token");
		}
	}

	@GetMapping("/search")
	public ResponseEntity<?> searchUser(@RequestParam("searchTerm") String searchTerm) {
		List<SearchDTO> searchResults = Uservice.searchUsers(searchTerm);
		return ResponseEntity.ok(searchResults);
	}

	@GetMapping("/userFollow")
	public ResponseEntity<?> followUser(@RequestParam("userId") int userId, HttpServletRequest request) {
		try {
			String token = jwtutil.token(request.getHeader("Authorization"));
			int myId = jwtutil.getUserIdFromToken(token);
			if (Uservice.followUser(userId, myId).equals("del")) {
				return ResponseEntity.ok("삭제");
			} else
				return ResponseEntity.ok("추가");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("error");
		}
	}

	@PostMapping("/WriteProfile")
	public ResponseEntity<?> updateProfile(@ModelAttribute UsersDTO profile, HttpServletRequest request) {
		try {
			String token = jwtutil.token(request.getHeader("Authorization"));
			profile.setId(jwtutil.getUserIdFromToken(token));
			String original = jwtutil.getNickFromToken(token); 
			UsersInfoDTO updatedProfile = Uservice.updateUserProfile(profile, original);
			return ResponseEntity.ok(updatedProfile);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("프로필 업데이트 실패: " + e.getMessage());
		}
	}
}