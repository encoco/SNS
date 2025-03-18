package com.example.demo.user.docs;

import com.example.demo.user.dto.SearchDTO;
import com.example.demo.user.dto.UsersDTO;
import com.example.demo.user.dto.UsersInfoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "유저 정보 API", description = "유저 프로필 및 로그인/로그아웃 / 토큰 refresh, ")
public interface UserControllerDocs {

    ////////////////////////아이디 중복 검사////////////////////////
    ////////////////////////아이디 중복 검사////////////////////////
    ////////////////////////아이디 중복 검사////////////////////////
    @Parameters(value = {
            @Parameter(name = "username", description = "로그인 시 사용할 아이디"),
            @Parameter(name = "nickname", description = "서비스에서 사용할 닉네임"),
    })
    @Operation(
            summary = "아이디 및 닉네임 중복 검사",
            description = "로그인에 사용할 아이디와 서비스 내에서 사용할 닉네임의 중복 여부를 확인한다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용 가능한 ID", content = @Content(schema = @Schema(implementation = UsersDTO.class))),
            @ApiResponse(responseCode = "409", description = "중복된 아이디 또는 닉네임")
    })
    public ResponseEntity<?> checkId(UsersDTO request);




    ////////////////////////회원 가입////////////////////////
    ////////////////////////회원 가입////////////////////////
    ////////////////////////회원 가입////////////////////////
    @Operation(
        summary = "회원가입",
        description = "새로운 사용자를 등록합니다."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "회원가입 성공", content = @Content(schema = @Schema(implementation = UsersDTO.class))
        ),
        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content)
    })
    public ResponseEntity<UsersDTO> signup(UsersDTO request);


    ////////////////////////로그 아웃////////////////////////
    ////////////////////////로그 아웃////////////////////////
    ////////////////////////로그 아웃////////////////////////
    @Operation(
            summary = "로그아웃",
            description = "사용자의 로그아웃을 처리하며, refresh token 쿠키를 삭제합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "성공적으로 로그아웃 처리됨",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            )
    })
    public ResponseEntity<?> logout(HttpServletResponse response);


    ////////////////////////Refresh Token////////////////////////
    ////////////////////////Refresh Token////////////////////////
    ////////////////////////Refresh Token////////////////////////
    @Operation(
            summary = "Access Token 재발급",
            description = "리프레시 토큰을 검증한 후 유효할 경우 새로운 access token을 반환합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "새로운 access token 발급 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "유효하지 않은 리프레시 토큰",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            )
    })
    public ResponseEntity<?> refreshToken(HttpServletRequest request);

    ////////////////////////유저 검색 ////////////////////////
    ////////////////////////유저 검색 ////////////////////////
    ////////////////////////유저 검색 ////////////////////////
    @Operation(
            summary = "유저 검색",
            description = "제공된 검색어를 기준으로 사용자를 검색합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "검색 결과 반환",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = SearchDTO.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content
            )
    })
    public ResponseEntity<?> searchUser(String searchTerm);


    ///////////////프로필 변경 ///////////////
    ///////////////프로필 변경 ///////////////
    ///////////////프로필 변경 ///////////////
    @Operation(
            summary = "프로필 업데이트",
            description = "사용자의 프로필을 업데이트하며, 닉네임 중복 시 에러를 반환합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "프로필 업데이트 성공",
                    content = @Content(schema = @Schema(implementation = UsersInfoDTO.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "프로필 업데이트 실패 또는 닉네임 중복",
                    content = @Content(schema = @Schema(implementation = String.class))
            )
    })
    ResponseEntity<?> updateProfile(
            @Parameter(description = "업데이트할 사용자 프로필 정보", required = true)
            @ModelAttribute UsersDTO profile,
            @Parameter(hidden = true) HttpServletRequest request
    );

    ////////////////비밀번호 확인//////////////////
    ////////////////비밀번호 확인//////////////////
    ////////////////비밀번호 확인//////////////////
    @Operation(
            summary = "비밀번호 확인",
            description = "입력된 비밀번호가 일치하는지 검증합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "비밀번호 확인 성공",
                    content = @Content(schema = @Schema(implementation = String.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "비밀번호 확인 실패",
                    content = @Content(schema = @Schema(implementation = String.class))
            )
    })
    ResponseEntity<?> checkPassword(@Parameter(description = "비밀번호 확인을 위한 사용자 정보", required = true)UsersDTO dto,
                                    @Parameter(hidden = true) HttpServletRequest request);


    ////////////////회원 탈퇴//////////////////
    ////////////////회원 탈퇴//////////////////
    ////////////////회원 탈퇴//////////////////
    @Operation(
            summary = "회원 탈퇴",
            description = "현재 로그인된 사용자를 탈퇴 처리합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "회원 탈퇴 성공",
                    content = @Content(schema = @Schema(implementation = Void.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "회원 탈퇴 실패",
                    content = @Content(schema = @Schema(implementation = String.class))
            )
    })
    public ResponseEntity<?> DeleteUser(HttpServletRequest request);
}