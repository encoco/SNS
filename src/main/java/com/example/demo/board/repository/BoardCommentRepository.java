package com.example.demo.board.repository;

import com.example.demo.board.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardCommentRepository extends JpaRepository<CommentEntity, Integer> {
    // 특정 게시글의 댓글을 날짜 기준으로 내림차순 정렬
    List<CommentEntity> findByBoardIdOrderByDateDesc(int boardId);
}