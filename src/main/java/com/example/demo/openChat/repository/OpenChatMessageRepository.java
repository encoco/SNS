package com.example.demo.openChat.repository;

import com.example.demo.openChat.entity.OpenChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OpenChatMessageRepository extends JpaRepository<OpenChatMessageEntity, Integer> {

    List<OpenChatMessageEntity> findByOpenChatId(int openChatId);

}
