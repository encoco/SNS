package com.example.demo.communityChat.repository;

import com.example.demo.communityChat.entity.CommunityChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommuChatRepository extends JpaRepository<CommunityChatEntity, Integer> {

}
