package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.CommunityChatEntity;
import com.example.demo.entity.CommunityChatJoinEntity;

public interface CommuChatRepository extends JpaRepository<CommunityChatEntity, Integer> {
	
}
