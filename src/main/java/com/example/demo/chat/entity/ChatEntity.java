package com.example.demo.chat.entity;

import com.example.demo.chat.dto.ChatDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Builder
@Data
@Entity
@Table(name = "userchat")
@AllArgsConstructor
@NoArgsConstructor
public class ChatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userchat_id")
    private int userchatId;
    @Column(name = "room_number")
    private String roomNumber;
    private int id;
    @Column(name = "join_id")
    private String joinId;
    private String profile_img;
    private String nickname;
    @Builder.Default
    private String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));


    public static ChatEntity toEntity(ChatDTO dto) {
        return ChatEntity.builder()
                .userchatId(dto.getUserchatId())
                .roomNumber(dto.getRoomNumber())
                .id(dto.getId())
                .nickname(dto.getRoomname())
                .joinId(dto.getJoinId())
                .date(dto.getDate())
                .build();
    }
}
