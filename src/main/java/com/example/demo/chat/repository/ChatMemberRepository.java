package com.example.demo.chat.repository;

import com.example.demo.chat.entity.ChatMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMemberRepository extends JpaRepository<ChatMemberEntity, Integer> {
    List<ChatMemberEntity> findByUser_Id(int userId);
    List<ChatMemberEntity> findByChatRoom_Id(int chatRoomId);
}

