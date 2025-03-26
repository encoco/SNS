package com.example.demo.chat.entity;

import com.example.demo.chat.dto.ChatMessageDTO;
import com.example.demo.user.entity.UsersEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    @JsonIgnore
    private ChatEntity chatRoom;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private UsersEntity user;

    private Integer share_board_id;

    @Builder.Default
    private String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));

    public static ChatMessageEntity toEntity(ChatMessageDTO dto) {
        UsersEntity userEntity = UsersEntity.builder().id(dto.getId()).build();
        ChatEntity chat = ChatEntity.builder().id(dto.getChat_id()).build();

        return ChatMessageEntity.builder()
                .message_id(dto.getMessage_id())
                .chatRoom(chat)
                .user(userEntity)
                .share_board_id(dto.getShare_board_id())
                .content(dto.getContent())
                .date(dto.getDate())
                .build();
    }
}
