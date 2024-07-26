package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.DTO.UsersInfoDTO;
import com.example.demo.entity.CCMEntity;

public interface CCMRepository extends JpaRepository<CCMEntity, Integer>{

	@Query(value = "SELECT cm.commessage_id, cm.communitychat_id, cm.id, u.nickname, u.profile_img, cm.content, cm.date "
			+ "FROM communitychatmessage cm JOIN users u ON cm.id = u.id WHERE communitychat_id = :communitychat_id", nativeQuery = true)
	List<CCMEntity> findBycommunitychatId(@Param("communitychat_id")int communitychatId);
	
}
