package com.example.demo.Repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.BoardEntity;

public interface BoardRepository extends JpaRepository<BoardEntity, Integer> {
	public List<BoardEntity> findByid(int id);
}


