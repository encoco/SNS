package com.example.demo.Repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.demo.entity.AlarmEntity;

public interface AlarmRepository extends JpaRepository<AlarmEntity, Integer> {

	List<AlarmEntity> findByRecipientIdOrderByDateDesc(int recipientId);
	
	@Query(value = "SELECT * FROM alarm WHERE recipient_id = :recipient_id AND sender_id = :senderId " +
            "AND content = :content AND board_id = :boardId", nativeQuery = true)
Optional<AlarmEntity> findByCriteria(@Param("recipient_id") int recipientId, 
                                  @Param("senderId") int senderId, 
                                  @Param("content") String content, 
                                  @Param("boardId") int boardId);

	List<AlarmEntity> findByRecipientId(int id);
}
