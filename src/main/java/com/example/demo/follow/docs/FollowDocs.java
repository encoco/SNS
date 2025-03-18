package com.example.demo.follow.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "팔로우 API", description = "팔로우 토글 ")
public interface FollowDocs {

    @Operation(summary = "사용자 팔로우 토글",
            description = "현재 사용자가 특정 사용자에 대해 팔로우를 추가하거나 취소합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "팔로우 상태 변경 결과 반환 (\"삭제\" 또는 \"추가\")",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class,
                                    example = "\"삭제\" or \"추가\"")
                    )
            ),
            @ApiResponse(responseCode = "401",
                    description = "인증 실패",
                    content = @Content)
    })
    @GetMapping("/userFollow")
    ResponseEntity<?> followUser(
            @Parameter(hidden = true)
            @RequestParam("userId") int userId,
            HttpServletRequest request);
}
