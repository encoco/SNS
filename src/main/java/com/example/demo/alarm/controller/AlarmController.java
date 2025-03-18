package com.example.demo.alarm.controller;

import com.example.demo.alarm.docs.AlarmDocs;
import com.example.demo.alarm.dto.AlarmDTO;
import com.example.demo.alarm.service.AlarmService;
import com.example.demo.config.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AlarmController implements AlarmDocs {
    private final JwtUtil jwtutil;
    private final AlarmService alarmservice;

    @GetMapping("/getAlarm")
    public ResponseEntity<?> getAlarm(HttpServletRequest request) {
        String token = jwtutil.token(request.getHeader("Authorization"));
        List<AlarmDTO> dto = alarmservice.getAlarm(jwtutil.getUserIdFromToken(token));
        System.out.println("dto : " + dto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/CheckNewAlarm")
    public ResponseEntity<?> checkAlarm(HttpServletRequest request) {
        String token = jwtutil.token(request.getHeader("Authorization"));
        boolean isRead = alarmservice.getCheckAlarm(jwtutil.getUserIdFromToken(token));
        return ResponseEntity.ok(isRead);
    }

    @PostMapping("delAllAlarm")
    public ResponseEntity<?> deleteAllAlarm(HttpServletRequest request) {
        String token = jwtutil.token(request.getHeader("Authorization"));
        alarmservice.delAllAlarm(jwtutil.getUserIdFromToken(token));
        return ResponseEntity.ok("삭제완료");
    }
}
