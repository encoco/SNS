package com.example.demo.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.DTO.CommentDTO;
import com.example.demo.entity.CommentEntity;

public interface BoardCommentRepository extends JpaRepository<CommentEntity, Integer>{
	public List<CommentEntity> findByboardId(int userId);
	public Optional<CommentEntity> findByBoardIdAndUserId(int boardId, int userId);
	
	@Query(value = "SELECT * FROM comment WHERE comment_id in :ids", nativeQuery = true)
    public List<CommentEntity> findByBoardIds(@Param("ids") List<Integer> ids);
	

	
}
