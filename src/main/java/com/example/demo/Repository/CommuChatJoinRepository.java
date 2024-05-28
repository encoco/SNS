package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.CommunityChatJoinEntity;

public interface CommuChatJoinRepository extends JpaRepository<CommunityChatJoinEntity, Integer> {
	List<CommunityChatJoinEntity> findAllById(int userId);
}
