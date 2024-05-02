package com.example.demo.Repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.BoardLikeEntity;


public interface BoardLikeRepository extends JpaRepository<BoardLikeEntity, Integer> {
	public List<BoardLikeEntity> findByid(int id);
}