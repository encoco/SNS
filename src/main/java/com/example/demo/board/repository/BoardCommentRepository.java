package com.example.demo.board.repository;

import com.example.demo.board.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardCommentRepository extends JpaRepository<CommentEntity, Integer> {

    @Query(value = "SELECT c.comment_id, c.board_id, c.id, u.nickname, u.profile_img, c.comment, c.date"
            + "	FROM comment c JOIN users u ON c.id = u.id WHERE board_id = :board_id ORDER BY c.date DESC", nativeQuery = true)
    List<CommentEntity> findByBoardIdOrderByDateDesc(@Param("board_id") int boardId);

    public Optional<CommentEntity> findByBoardIdAndId(int boardId, int id);

    @Query(value = "SELECT * FROM comment WHERE comment_id in :ids", nativeQuery = true)
    public List<CommentEntity> findByBoardIds(@Param("ids") List<Integer> ids);
}