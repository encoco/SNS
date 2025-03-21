package com.example.demo.board.repository;

import com.example.demo.board.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<BoardEntity, Integer> {
    List<BoardEntity> findByUser_IdOrderByDateDesc(int userId);
    List<BoardEntity> findByUser_IdInOrderByDateDesc(List<Integer> userIds);
    BoardEntity findByBoardId(int boardId);
    boolean existsByUserId(int userId);

    @Query(value = "SELECT * FROM board ORDER BY RAND() LIMIT 20", nativeQuery = true)
    List<BoardEntity> findRandomBoards();
}
