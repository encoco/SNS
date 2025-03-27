package com.example.demo.openChat.docs;

import com.example.demo.openChat.dto.OpenChatDTO;
import com.example.demo.openChat.dto.OpenChatMemberDTO;
import com.example.demo.openChat.dto.OpenChatMessageDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface OpenDocs {
    @Operation(summary = "커뮤니티 채팅 메시지 조회",
            description = "특정 커뮤니티 채팅 ID에 해당하는 메시지 목록을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "커뮤니티 채팅 메시지 목록 반환",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = OpenChatMessageDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "메시지 조회 중 오류",
                    content = @Content)
    })

    @GetMapping("/getCommMessage")
    ResponseEntity<?> getCommMessage(
            @Parameter(description = "커뮤니티 채팅 ID", required = true, example = "1")
            @RequestParam("openChatId") int openChatId
    );

    @Operation(summary = "커뮤니티 채팅방 생성",
            description = "커뮤니티 채팅방을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "생성 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class)
                    )
            ),
            @ApiResponse(responseCode = "401",
                    description = "인증 실패",
                    content = @Content)
    })
    @PostMapping("/CreateCommChat")
    ResponseEntity<?> createCommChat(
            @Parameter(description = "커뮤니티 채팅방 정보", required = true)
            @ModelAttribute OpenChatDTO dto,
            @Parameter(hidden = true) HttpServletRequest request
    );


    @Operation(summary = "커뮤니티 채팅방 참여",
            description = "커뮤니티 채팅방에 참여합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "참여 완료",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Void.class)
                    )
            ),
            @ApiResponse(responseCode = "401",
                    description = "인증 실패",
                    content = @Content)
    })
    @PostMapping("/JoinCommuRoom")
    ResponseEntity<?> joinCommuRoom(
            @Parameter(description = "참여할 커뮤니티 채팅방 정보", required = true)
            @RequestBody OpenChatDTO dto,
            @Parameter(hidden = true) HttpServletRequest request
    );

    @Operation(summary = "전체 커뮤니티 채팅방 조회",
            description = "전체 커뮤니티 채팅방 목록을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "커뮤니티 채팅방 목록 반환 또는 '채팅방 없음'",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OpenChatDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "401",
                    description = "인증 실패",
                    content = @Content)
    })
    @GetMapping("/selectAllCommuRoom")
    ResponseEntity<?> selectAllCommuRoom(
            @Parameter(hidden = true) HttpServletRequest request
    );

    @Operation(summary = "참여 중인 커뮤니티 채팅방 조회",
            description = "현재 사용자가 참여 중인 커뮤니티 채팅방 목록을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "커뮤니티 채팅방 목록 반환 또는 '채팅방 없음'",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OpenChatMemberDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "401",
                    description = "인증 실패",
                    content = @Content)
    })
    @GetMapping("/selectCommuRoom")
    ResponseEntity<?> selectCommuRoom(
            @Parameter(hidden = true) HttpServletRequest request
    );

}
