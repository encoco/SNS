package com.example.demo.alarm.docs;


import com.example.demo.alarm.dto.AlarmDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Tag(name = "알람 API", description = "알람 조회 / 확인 / 삭제  ")
public interface AlarmDocs {
    @Operation(summary = "알람 조회", description = "사용자의 알람 리스트를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "알람 리스트 반환", content = @Content(
                        mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = AlarmDTO.class))
                )
            ),
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content)
    })
    @GetMapping("/getAlarm")
    ResponseEntity<?> getAlarm(@Parameter(hidden = true) HttpServletRequest request);

    @Operation(
            summary = "알람 읽음 처리",
            description = "안읽은 알람들을 읽은 상태로 변환합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "새 알람 존재 여부 반환 (true/false)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Boolean.class)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content)
    })
    @GetMapping("/CheckNewAlarm")
    ResponseEntity<?> checkAlarm(@Parameter(hidden = true) HttpServletRequest request);

    @Operation(
            summary = "전체 알람 삭제",
            description = "사용자의 모든 알람을 삭제합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "전체 알람 삭제 완료",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content)
    })
    @PostMapping("/delAllAlarm")
    ResponseEntity<?> deleteAllAlarm(@Parameter(hidden = true) HttpServletRequest request);
}