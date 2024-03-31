package com.example.demo.Repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.BoardEntity;

public interface BoardRepository extends JpaRepository<BoardEntity, Integer> {
		
}


