package com.example.demo.chat.entity;

import com.example.demo.user.entity.UsersEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chat_member")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    private ChatRoomEntity chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UsersEntity user;

    private String date; // 참여 날짜 등
}
