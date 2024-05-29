package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.ChatMessageEntity;

public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Integer>{

	@Query(value = "SELECT um.message_id, um.room_number, um.id, um.nickname, um.content, um.date, u.profile_img "
			+ "FROM userchatmessage um JOIN users u ON um.id = u.id WHERE room_number = :room_number", nativeQuery = true)
	List<ChatMessageEntity> findByroomNumber(@Param("room_number")String roomNumber);

}
