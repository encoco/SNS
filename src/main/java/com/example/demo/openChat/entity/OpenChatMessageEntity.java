package com.example.demo.openChat.entity;

import com.example.demo.openChat.dto.OpenChatMessageDTO;
import com.example.demo.user.entity.UsersEntity;
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
@Table(name = "open_chat_message")
@AllArgsConstructor
@NoArgsConstructor
public class OpenChatMessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "open_message_id")
    int openMessageId;
    @Column(name = "open_chat_id")
    int openChatId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private UsersEntity user;

    String content;

    @Builder.Default
    private String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));

    public static OpenChatMessageEntity toEntity(OpenChatMessageDTO dto) {
        UsersEntity userEntity = UsersEntity.builder().id(dto.getId()).build();
        return OpenChatMessageEntity.builder()
                .openMessageId(dto.getOpenMessageId())
                .openChatId(dto.getOpenChatId())
                .user(userEntity)
                .content(dto.getContent())
                .date(dto.getDate())
                .build();
    }
}
