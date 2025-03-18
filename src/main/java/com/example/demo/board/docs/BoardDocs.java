package com.example.demo.board.docs;

import com.example.demo.board.dto.CommentDTO;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Tag(name = "게시물 API",description = "게시물 / 댓글 관련 API")
public interface BoardDocs {

    @Operation(summary = "게시물 리스트 조회",
            description = "사용자의 팔로우 게시글을 조회하고, 게시글이 없을 경우 랜덤 게시글을 반환합니다. 또한, 게시글에 대한 좋아요 정보도 함께 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "게시글 및 좋아요 정보 반환",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(responseCode = "500", description = "내부 서버 오류", content = @Content)
    })
    @GetMapping("/mainboardList")
    ResponseEntity<?> mainboardList(@Parameter(hidden = true) HttpServletRequest request);


    @Operation(summary = "사용자 게시글 조회",
            description = "특정 사용자의 게시글, 좋아요 정보, 사용자 정보, 팔로우 상태를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "게시글 및 관련 정보 반환",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "내부 서버 오류", content = @Content)})
    @GetMapping("/userPosts")
    ResponseEntity<?> userPosts(
            @Parameter(description = "조회할 사용자의 ID", required = true, example = "1")
            @RequestParam("userId") int userId,
            @Parameter(hidden = true) HttpServletRequest request
    );


    @Operation(summary = "게시글 작성",
            description = "새로운 게시글을 작성합니다. 게시글 내용과 선택적으로 이미지 파일을 업로드할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "게시글 작성 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class)
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "게시글 작성 중 오류",
                    content = @Content)
    })
    @PostMapping("/boardWrite")
    ResponseEntity<?> writeBoard(
            @Parameter(description = "게시글 내용", required = true, example = "게시글 내용입니다.")
            @RequestParam("content") String content,
            @Parameter(description = "업로드할 이미지 파일 목록", required = false)
            @RequestParam(value = "img", required = false) List<MultipartFile> imgs,
            @Parameter(hidden = true) HttpServletRequest request
    );

    @Operation(summary = "게시글 좋아요",
            description = "특정 게시글에 좋아요를 처리합니다. 결과에 따라 'fail', 'success' 또는 오류 메시지가 반환됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "게시글 좋아요 결과 반환",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class)
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "내부 서버 오류",
                    content = @Content)
    })
    @GetMapping("/boardLike")
    ResponseEntity<?> boardLike(
            @Parameter(description = "게시글 ID", required = true, example = "123")
            @RequestParam("boardId") int boardId,
            @Parameter(description = "게시글 작성자 ID (알람 보낼 때 사용)", required = true, example = "1")
            @RequestParam("writerId") int writerId,
            @Parameter(hidden = true) HttpServletRequest request
    );

    @Operation(summary = "댓글 조회",
            description = "특정 게시글의 댓글 리스트를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "댓글 리스트 반환",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CommentDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "내부 서버 오류",
                    content = @Content)
    })
    @GetMapping("/getComments")
    ResponseEntity<?> getComments(
            @Parameter(description = "게시글 ID", required = true, example = "123")
            @RequestParam("boardId") int boardId
    );

    @Operation(summary = "댓글 작성",
            description = "댓글을 작성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "댓글 작성 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class)
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "댓글 작성 중 오류",
                    content = @Content)
    })
    @PostMapping("/CommentWrite")
    ResponseEntity<?> writeComment(
            @Parameter(description = "작성할 댓글 정보", required = true)
            @RequestBody CommentDTO dto,
            @Parameter(hidden = true) HttpServletRequest request
    );


    @Operation(summary = "게시글 수정",
            description = "게시글 내용을 수정합니다. 이미지 파일 및 이미지 경로를 선택적으로 업데이트할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "수정 완료",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class)
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "글 update 실패",
                    content = @Content)
    })
    @PostMapping("/boardUpdate")
    ResponseEntity<?> updateBoard(
            @Parameter(description = "게시글 내용", required = true, example = "게시글 내용입니다.")
            @RequestParam("content") String content,
            @Parameter(description = "업로드할 이미지 파일 목록", required = false)
            @RequestParam(value = "img", required = false) List<MultipartFile> imgs,
            @Parameter(description = "이미지 경로", required = false, example = "s3~~~dsandba.jpg")
            @RequestParam(value = "imgpath", required = false) String imgpath,
            @Parameter(hidden = true)
            @RequestParam("board_id") int board_id,
            @Parameter(hidden = true) HttpServletRequest request
    );

    @Operation(summary = "댓글 수정",
            description = "댓글 내용을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "수정 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class)
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "댓글 update 실패",
                    content = @Content)
    })
    @PostMapping("/EditComment")
    ResponseEntity<?> updateComment(
            @Parameter(description = "수정할 댓글 정보", required = true)
            @RequestBody CommentDTO commentDTO
    );

    @Operation(summary = "댓글 삭제",
            description = "댓글을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "삭제 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class)
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "댓글 삭제 실패",
                    content = @Content)
    })
    @PostMapping("/DeleteComment")
    ResponseEntity<?> deleteComment(
            @Parameter(description = "삭제할 댓글 정보", required = true)
            @RequestBody CommentDTO commentDTO
    );

    @Operation(summary = "게시글 삭제",
            description = "게시글을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "삭제 완료",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Void.class)
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "글 삭제 중 오류 발생",
                    content = @Content)
    })
    @PostMapping("/boardDelete")
    ResponseEntity<?> deleteBoard(int board_id);

    @Operation(summary = "게시글 상세 조회",
            description = "게시글과 해당 게시글의 좋아요 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "게시글 및 좋아요 정보 반환",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Map.class)
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "게시글 조회 중 오류 발생",
                    content = @Content)
    })
    @GetMapping("/findPost")
    ResponseEntity<?> findPost(
            @Parameter(hidden = true) int boardId,
            @Parameter(hidden = true) HttpServletRequest request
    );
}