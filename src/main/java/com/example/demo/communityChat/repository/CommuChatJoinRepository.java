package com.example.demo.communityChat.repository;

import com.example.demo.communityChat.entity.CommunityChatJoinEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommuChatJoinRepository extends JpaRepository<CommunityChatJoinEntity, Integer> {
    List<CommunityChatJoinEntity> findAllById(int userId);
}
