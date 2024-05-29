package com.example.demo.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.ChatEntity;

public interface ChatRepository extends JpaRepository<ChatEntity, Integer> {

	@Query(value = "SELECT MIN(uc.userchat_id) AS userchat_id, uc.room_number, "
			+ "GROUP_CONCAT(DISTINCT u.profile_img) AS profile_img, "
			+ "GROUP_CONCAT(DISTINCT u.nickname) AS nickname, "
			+ "MIN(uc.id) AS id, MIN(uc.join_id) AS join_id, MIN(uc.date) AS date " + "FROM userchat uc "
			+ "JOIN users u ON FIND_IN_SET(u.id, uc.join_id) > 0 AND u.id != :id "
			+ "WHERE FIND_IN_SET(:id, uc.join_id) > 0 " + "GROUP BY uc.room_number", nativeQuery = true)
	public List<ChatEntity> CustomfindById(@Param("id") int id);

	public boolean existsByRoomNumber(String roomNumber);

	public ChatEntity findByJoinIdAndId(String joinId, int id);

	ChatEntity findJoinIdByroomNumberAndId(String roomNumber, int id);
}
