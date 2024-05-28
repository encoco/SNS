package com.example.demo.Repository;


import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.ChatEntity;

public interface ChatRepository extends JpaRepository<ChatEntity, Integer> {

	@Query(value = "SELECT uc.userchat_id, uc.room_number, uc.roomname, u.profile_img,"
			+ " uc.id,uc.join_id,uc.date FROM userchat uc JOIN users u ON uc.id = u.id WHERE u.id = :id", nativeQuery = true)
	public List<ChatEntity> CustomfindById(@Param("id")int id); // 커스텀 메서드 이름 수정
	
	public boolean existsByRoomNumber(String roomNumber);
	public ChatEntity findByJoinIdAndId(String joinId,int id);
	ChatEntity findJoinIdByroomNumberAndId(String roomNumber,int id);
}
