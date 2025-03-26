package com.example.demo.chat.repository;

import com.example.demo.chat.entity.ChatParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipantEntity, Integer> {
    List<ChatParticipantEntity> findByUser_Id(int userId);
    List<ChatParticipantEntity> findByChatRoom_Id(int chatRoomId);
}

