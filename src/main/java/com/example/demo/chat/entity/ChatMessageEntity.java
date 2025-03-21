package com.example.demo.chat.entity;

import com.example.demo.chat.dto.ChatMessageDTO;
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
@Table(name = "userchatmessage")
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int message_id;
    @Column(name = "room_number")
    private String roomNumber;
    private int id;
    private String content;
    private String nickname;
    private String profile_img;
    private Integer share_board_id;

    @Builder.Default
    private String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));

    public static ChatMessageEntity toEntity(ChatMessageDTO dto) {
        return ChatMessageEntity.builder()
                .message_id(dto.getMessage_id())
                .roomNumber(dto.getRoom_number())
                .share_board_id(dto.getShare_board_id())
                .id(dto.getId())
                .nickname(dto.getNickname())
                .content(dto.getContent())
                .date(dto.getDate())
                .build();
    }
}
