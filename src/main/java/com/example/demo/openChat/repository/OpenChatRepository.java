package com.example.demo.openChat.repository;

import com.example.demo.openChat.entity.OpenChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpenChatRepository extends JpaRepository<OpenChatEntity, Integer> {

}
