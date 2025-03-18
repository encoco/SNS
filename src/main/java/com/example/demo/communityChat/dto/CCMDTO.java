package com.example.demo.communityChat.dto;

import com.example.demo.communityChat.entity.CCMEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

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
@Schema(description = "커뮤니티 채팅 메시지 DTO")
public class CCMDTO {
    @Schema(description = "커뮤니티 채팅 메시지 ID",example = "123")
    int commessage_id;

    @Schema(description = "커뮤니티 채팅방 ID",example = " 13")
    int communitychat_id;

    @Schema(description = "메세지 입력한 사람의 ID",example = "12")
    int id;

    @Schema(description = "입력한 사람의 nickName",example = "nick")
    String nickname;

    @Schema(description = "채팅 메세지",example = "안녕하세요 반가워요.")
    String content;

    @Schema(description = "입력한 사람의 프로필 사진",example = "www.s3dasd.jpg")
    String profile_img;

    @Builder.Default
    private String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));

    public static List<CCMDTO> ToDtoList(List<CCMEntity> entities) {
        List<CCMDTO> dtos = new ArrayList<>();
        for (CCMEntity entity : entities) {
            CCMDTO dto = new CCMDTO();
            dto.setCommessage_id(entity.getCommessageId());
            dto.setCommunitychat_id(entity.getCommunitychatId());
            dto.setId(entity.getId());
            dto.setProfile_img(entity.getProfile_img());
            dto.setNickname(entity.getNickname());
            dto.setContent(entity.getContent());
            dto.setDate(entity.getDate());
            dtos.add(dto);
        }
        return dtos;
    }

    public static CCMDTO toDTO(CCMEntity dto) {
        return CCMDTO.builder()
                .commessage_id(dto.getCommessageId())
                .communitychat_id(dto.getCommunitychatId())
                .id(dto.getId())
                .nickname(dto.getNickname())
                .profile_img(dto.getProfile_img())
                .content(dto.getContent())
                .date(dto.getDate())
                .build();
    }

}
