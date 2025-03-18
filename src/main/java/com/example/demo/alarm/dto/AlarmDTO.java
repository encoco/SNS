package com.example.demo.alarm.dto;

import com.example.demo.alarm.entity.AlarmEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "알림 DTO")
public class AlarmDTO {

    @Schema(description = "알림 ID", example = "1")
    private int alarm_id;

    @Schema(description = "수신자 ID", example = "100")
    private int recipient_id;

    @Schema(description = "발신자 ID", example = "200")
    private int sender_id;

    @Schema(description = "프로필 이미지 경로", example = "이미지 경로")
    private String profile_img;

    @Schema(description = "사용자 닉네임", example = "홍길동")
    private String nickname;

    @Schema(description = "게시글 ID", example = "123")
    private int board_id;

    @Schema(description = "알림 내용", example = "새 댓글이 달렸습니다.")
    private String content;

    @Schema(description = "알림 읽음 여부", example = "false")
    private boolean isread;


    @Builder.Default
    private String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));


    public static AlarmDTO fromEntity(AlarmEntity entity) {
        return AlarmDTO.builder()
                .alarm_id(entity.getAlarm_id())
                .recipient_id(entity.getRecipientId())
                .sender_id(entity.getSender().getId())
                .board_id(entity.getBoard_id())
                .content(entity.getSender().getNickname() + entity.getContent())
                .date(entity.getDate())
                .isread(entity.isIsread())
                .profile_img(entity.getSender().getProfile_img())
                .nickname(entity.getSender().getNickname())
                .build();
    }

    // Convert a list of entities to a list of DTOs
    public static List<AlarmDTO> toDtoList(List<AlarmEntity> entities) {
        List<AlarmDTO> dtos = new ArrayList<>();
        for (AlarmEntity entity : entities) {
            dtos.add(fromEntity(entity));
        }
        return dtos;
    }
}
