package com.example.demo.DTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.entity.AlarmEntity;
import com.example.demo.entity.UsersEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlarmDTO {
    private int alarm_id;
    private int recipient_id;
    private int sender_id;
    private String profile_img;
    private String nickname;
    private int board_id;
    private String content;
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
