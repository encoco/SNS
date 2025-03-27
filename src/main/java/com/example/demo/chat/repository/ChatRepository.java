package com.example.demo.chat.repository;

import com.example.demo.chat.entity.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<ChatRoomEntity, Integer> {

    //boolean existsByRoomNumber(String roomNumber);
}


