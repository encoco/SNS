package com.example.demo.board.dto;


import com.example.demo.board.entity.BoardEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "게시글 DTO")
public class BoardDTO {

    @Schema(description = "게시글 ID", example = "1")
    private int board_id;

    @Schema(description = "유저 ID", example = "100")
    private int id;

    @Schema(description = "닉네임", example = "홍길동")
    private String nickname;

    @Schema(description = "업로드된 이미지 파일 목록", type = "array", implementation = MultipartFile.class)
    private List<MultipartFile> img;

    @Schema(description = "이미지 경로", example = "이미지 경로")
    private String imgpath;

    @Schema(description = "사용자 프로필 이미지 경로", example = "이미지 경로")
    private String profile_img;

    @Schema(description = "게시글 동영상 URL", example = "https://example.com/video.mp4")
    private String video;

    @Schema(description = "게시글 내용", example = "이것은 게시글 내용입니다.")
    private String content;

    @Schema(description = "게시글 좋아요 수", example = "10")
    private int like;

    @Builder.Default
    private String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));

    public static BoardDTO toDTO(BoardEntity entity) {
        return BoardDTO.builder()
                .board_id(entity.getBoard_id())
                .id(entity.getId())
                .nickname(entity.getNickname())
                .imgpath(entity.getImg())
                .video(entity.getVideo())
                .content(entity.getContent())
                .date(entity.getDate())
                .build();
    }


    public static List<BoardDTO> ToDtoList(List<BoardEntity> entities) {
        List<BoardDTO> dtos = new ArrayList<>();
        for (BoardEntity entity : entities) {
            BoardDTO dto = new BoardDTO();
            dto.setBoard_id(entity.getBoard_id());
            dto.setId(entity.getId());
            dto.setNickname(entity.getNickname());
            dto.setImgpath(entity.getImg());
            dto.setProfile_img(entity.getProfile_img());
            dto.setVideo(entity.getVideo());
            dto.setDate(entity.getDate());
            dto.setContent(entity.getContent());
            dtos.add(dto);
        }
        return dtos;
    }
}