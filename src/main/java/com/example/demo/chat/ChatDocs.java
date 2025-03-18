package com.example.demo.chat;

import com.example.demo.chat.dto.ChatDTO;
import com.example.demo.chat.dto.ChatMessageDTO;
import com.example.demo.communityChat.dto.CCJDTO;
import com.example.demo.communityChat.dto.CCMDTO;
import com.example.demo.communityChat.dto.CommunityChatDTO;
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
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name="채팅 API", description = "채팅방 생성/검색 || 채팅 관련 REST API")
public interface ChatDocs {
    @Operation(summary = "채팅 메시지 조회",
            description = "특정 방 번호에 해당하는 채팅 메시지 목록을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "채팅 메시지 목록 반환",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ChatMessageDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "메시지 조회 중 오류",
                    content = @Content)
    })
    @GetMapping("/getMessage")
    ResponseEntity<?> getMessage(
            @Parameter(description = "방 번호", required = true, example = "room1")
            @RequestParam("roomNumber") String roomNumber,
            @Parameter(hidden = true) HttpServletRequest request
    );

    @Operation(summary = "커뮤니티 채팅 메시지 조회",
            description = "특정 커뮤니티 채팅 ID에 해당하는 메시지 목록을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "커뮤니티 채팅 메시지 목록 반환",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CCMDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "메시지 조회 중 오류",
                    content = @Content)
    })
    @GetMapping("/getCommMessage")
    ResponseEntity<?> getCommMessage(
            @Parameter(description = "커뮤니티 채팅 ID", required = true, example = "1")
            @RequestParam("communitychat_id") String communitychat_id
    );

    @Operation(summary = "채팅방 조회",
            description = "현재 사용자가 참여 중인 채팅방 목록을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "채팅방 목록 또는 '채팅방 없음' 메시지 반환",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ChatDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "401",
                    description = "인증 실패",
                    content = @Content)
    })
    @GetMapping("/selectRoom")
    ResponseEntity<?> selectRoom(
            @Parameter(hidden = true) HttpServletRequest request
    );

    @Operation(summary = "채팅방 생성",
            description = "채팅 참여자를 포함하는 채팅방을 생성하거나 기존 채팅방을 반환합니다. 반환값은 키 '1' 또는 '0'으로 구분된 Map 형태입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "채팅방 정보 반환",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Map.class,
                                    example = "{\n" +
                                            "  \"1\": { \"roomNumber\": \"room1\", \"members\": [1, 2, 3] }\n" +
                                            "}")
                    )
            ),
            @ApiResponse(responseCode = "401",
                    description = "인증 실패",
                    content = @Content)
    })
    @PostMapping("/CreateRoom")
    ResponseEntity<?> createChatRoom(
            @Parameter(description = "채팅방 생성 요청 데이터", required = true)
            @RequestBody Map<String, Object> requestData,
            @Parameter(hidden = true) HttpServletRequest request
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
            @ModelAttribute CommunityChatDTO dto,
            @Parameter(hidden = true) HttpServletRequest request
    );

    @Operation(summary = "전체 커뮤니티 채팅방 조회",
            description = "전체 커뮤니티 채팅방 목록을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "커뮤니티 채팅방 목록 반환 또는 '채팅방 없음'",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CommunityChatDTO.class)
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
            @RequestBody CommunityChatDTO dto,
            @Parameter(hidden = true) HttpServletRequest request
    );

    @Operation(summary = "참여 중인 커뮤니티 채팅방 조회",
            description = "현재 사용자가 참여 중인 커뮤니티 채팅방 목록을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "커뮤니티 채팅방 목록 반환 또는 '채팅방 없음'",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CCJDTO.class)
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

    @Operation(summary = "게시글 공유",
            description = "게시글 공유를 위해 채팅방을 생성하거나 기존 채팅방에 메시지를 전송합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "공유 처리 완료",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class)
                    )
            ),
            @ApiResponse(responseCode = "401",
                    description = "인증 실패",
                    content = @Content)
    })
    @PostMapping("/SharePost")
    ResponseEntity<?> sharePost(
            @Parameter(description = "게시글 공유 요청 데이터", required = true)
            @RequestBody Map<String, Object> requestData,
            @Parameter(hidden = true) HttpServletRequest request
    );

    @Operation(summary = "채팅방 찾기",
            description = "특정 사용자의 채팅방 정보를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "채팅방 정보 반환",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ChatDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "401",
                    description = "인증 실패",
                    content = @Content)
    })
    @GetMapping("/findRoom")
    ResponseEntity<?> findRoom(
            @Parameter(hidden = true) HttpServletRequest request,
            @Parameter(description = "찾을 사용자 ID", required = true, example = "1")
            @RequestParam("id") int id
    );
}