package com.example.demo.chat.repository;

import com.example.demo.chat.entity.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Integer> {

    List<ChatMessageEntity> findByChatRoom_Id(int chatId);
}
