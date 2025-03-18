package com.example.demo.communityChat.dto;

import com.example.demo.communityChat.entity.CommunityChatEntity;
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
@Schema(description = "커뮤니티 채팅 DTO")
public class CommunityChatDTO {
    @Schema(description = "커뮤니티 채팅 ID", example = "1")
    private int communitychatId;

    @Schema(description = "사용자 ID", example = "1")
    private int id;

    @Schema(description = "채팅방 제목", example = "놀사람 구해요")
    private String title;

    @Schema(description = "채팅방 설명", example = "자유로운 의견을 나누는 채팅방입니다.")
    private String description;

    @Schema(description = "이미지 경로", example = "www.s3sdasda.jpg")
    private String imgpath;

    @Schema(description = "업로드할 이미지 파일")
    private MultipartFile img;


    @Builder.Default
    private String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));

    public static CommunityChatDTO toDTO(CommunityChatEntity entity) {
        return CommunityChatDTO.builder()
                .communitychatId(entity.getCommunitychatId())
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .imgpath(entity.getImg())
                .date(entity.getDate())
                .build();
    }

    public static List<CommunityChatDTO> toDTOList(List<CommunityChatEntity> entitis) {
        List<CommunityChatDTO> dtos = new ArrayList<>();
        for (CommunityChatEntity entity : entitis) {
            CommunityChatDTO dto = new CommunityChatDTO();
            dto.setCommunitychatId(entity.getCommunitychatId());
            dto.setId(entity.getId());
            dto.setTitle(entity.getTitle());
            dto.setDescription(entity.getDescription());
            dto.setImgpath(entity.getImg());
            dto.setDate(entity.getDate());
            dtos.add(dto);
        }
        return dtos;
    }
}
