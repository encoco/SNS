package com.example.demo.chat.entity;

import com.example.demo.chat.dto.ChatDTO;
import com.example.demo.user.entity.UsersEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@Entity
@Table(name = "chat_room")
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private int id;

    @ToString.Exclude
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<ChatMemberEntity> participants = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private UsersEntity user;

    @Builder.Default
    private String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));


    public static ChatRoomEntity toEntity(ChatDTO dto) {
        UsersEntity userEntity = UsersEntity.builder()
                .id(dto.getId())
                .build();

        return ChatRoomEntity.builder()
                .id(dto.getUserchatId())
                .user(userEntity)
                .date(dto.getDate())
                .build();
    }
}
