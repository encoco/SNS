package com.example.demo.user.controller;

import com.example.demo.config.JwtUtil;
import com.example.demo.user.docs.UserControllerDocs;
import com.example.demo.user.dto.SearchDTO;
import com.example.demo.user.dto.UsersDTO;
import com.example.demo.user.dto.UsersInfoDTO;
import com.example.demo.user.repository.UsersRepository;
import com.example.demo.user.service.UsersService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController implements UserControllerDocs {
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
        Uservice.registerUser(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
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


    @PostMapping("/WriteProfile")
    public ResponseEntity<?> updateProfile(@ModelAttribute UsersDTO profile, HttpServletRequest request) {
        try {
            String token = jwtutil.token(request.getHeader("Authorization"));
            profile.setId(jwtutil.getUserIdFromToken(token));
            System.out.println(profile);
            UsersInfoDTO updatedProfile = Uservice.updateUserProfile(profile);
            if(updatedProfile != null) {
                return ResponseEntity.internalServerError().body("닉네임 중복");
            }
            return ResponseEntity.ok(updatedProfile);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("프로필 업데이트 실패: " + e.getMessage());
        }
    }

    @PostMapping("/checkPassword")
    public ResponseEntity<?> checkPassword(@RequestBody UsersDTO dto, HttpServletRequest request) {
        String token = jwtutil.token(request.getHeader("Authorization"));
        boolean checked = Uservice.updatePassword(dto, jwtutil.getUserIdFromToken(token));
        if (checked) {
            return ResponseEntity.ok("완료");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호 확인");
        }
    }


    @PostMapping("/DeleteUser")
    public ResponseEntity<?> DeleteUser(HttpServletRequest request) {
        try {
            String token = jwtutil.token(request.getHeader("Authorization"));
            Uservice.DeleteUser(jwtutil.getUserIdFromToken(token));
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("탈퇴 실패");
        }
    }


    //WebController에 있던건데 혹시 몰라서 여기 짱박아두기.
    @GetMapping(value = {"", "/", "/Signup", "/index", "/BoarddWrite", "/mypage", "/UserPage/**", "/Message", "/Participate", "/Setting", "/Together"})
    public String forward() {
        return "forward:/index.html";
    }
}

