package com.example.demo.alarm.repository;

import com.example.demo.alarm.entity.AlarmEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AlarmRepository extends JpaRepository<AlarmEntity, Integer> {

    List<AlarmEntity> findByRecipientIdOrderByDateDesc(int recipientId);

    @Query(value = "SELECT * FROM alarm WHERE recipient_id = :recipient_id AND sender_id = :senderId " +
            "AND content = :content AND board_id = :boardId", nativeQuery = true)
    Optional<AlarmEntity> findByCriteria(@Param("recipient_id") int recipientId,
                                         @Param("senderId") int senderId,
                                         @Param("content") String content,
                                         @Param("boardId") int boardId);

    List<AlarmEntity> findByRecipientId(int id);

    @Query(value = "SELECT COUNT(*) > 0 FROM alarm WHERE recipient_id = :recipient_id AND isread = 0", nativeQuery = true)
    int existsByRecipientIdAndReadFalse(@Param("recipient_id") int recipientId);
}
