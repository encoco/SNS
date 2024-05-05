package com.example.demo.Repository;



import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.BoardLikeEntity;


public interface BoardLikeRepository extends JpaRepository<BoardLikeEntity, Integer> {
	public List<BoardLikeEntity> findByboardId(int userId);
	Optional<BoardLikeEntity> findByBoardIdAndUserId(int boardId, int userId);
	
}