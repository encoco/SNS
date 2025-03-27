package com.example.demo.openChat.repository;

import com.example.demo.openChat.entity.OpenChatMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OpenChatMemberRepository extends JpaRepository<OpenChatMemberEntity, Integer> {
    List<OpenChatMemberEntity> findAllById(int userId);
}
