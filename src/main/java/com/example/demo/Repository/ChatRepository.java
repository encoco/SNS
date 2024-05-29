package com.example.demo.Repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.ChatEntity;

public interface ChatRepository extends JpaRepository<ChatEntity, Integer> {
	public List<ChatEntity> findByUserId(int userId); // 커스텀 메서드 이름 수정
	public boolean existsByRoomNumber(String roomNumber);
	public ChatEntity findByJoinIdAndUserId(String joinId,int userId);
	ChatEntity findJoinIdByroomNumberAndUserId(String roomNumber,int userId);
}
