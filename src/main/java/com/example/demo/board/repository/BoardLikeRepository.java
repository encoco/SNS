package com.example.demo.board.repository;


import com.example.demo.board.entity.BoardLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface BoardLikeRepository extends JpaRepository<BoardLikeEntity, Integer> {
    public List<BoardLikeEntity> findByboardId(int userId);

    public Optional<BoardLikeEntity> findByBoardIdAndUserId(int boardId, int userId);

    @Query(value = "SELECT * FROM boardlike WHERE board_id in :ids", nativeQuery = true)
    public List<BoardLikeEntity> findByBoardIds(@Param("ids") List<Integer> ids);

    @Query(value = "SELECT * FROM boardlike WHERE board_id = :board_id", nativeQuery = true)
    public BoardLikeEntity findByBoardId(@Param("board_id") int boardId);

}