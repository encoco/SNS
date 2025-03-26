package com.example.demo.chat.repository;

import com.example.demo.chat.entity.ChatEntity;
import com.example.demo.chat.entity.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<ChatEntity, Integer> {

    //boolean existsByRoomNumber(String roomNumber);
}


