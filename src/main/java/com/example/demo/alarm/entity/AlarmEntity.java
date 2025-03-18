package com.example.demo.alarm.entity;

import com.example.demo.alarm.dto.AlarmDTO;
import com.example.demo.user.entity.UsersEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@Table(name = "alarm")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class AlarmEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int alarm_id;

    private int recipientId;
    @ManyToOne
    @JoinColumn(name = "sender_id") // 외래 키 지정
    private UsersEntity sender;

    private int board_id;
    private String content;
    private boolean isread;

    @Builder.Default
    private String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));


    public static AlarmEntity toEntity(AlarmDTO dto) {
        return AlarmEntity.builder()
                .alarm_id(dto.getAlarm_id())
                .recipientId(dto.getRecipient_id())
                .sender(UsersEntity.builder().id(dto.getRecipient_id()).build())
                .board_id(dto.getBoard_id())
                .content(dto.getContent())
                .isread(dto.isIsread())
                .date(dto.getDate())
                .build();
    }
}
