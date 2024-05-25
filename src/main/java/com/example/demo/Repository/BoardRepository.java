package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.BoardEntity;

public interface BoardRepository extends JpaRepository<BoardEntity, Integer> {
	public List<BoardEntity> findByid(int id);

	@Query(value = "SELECT * FROM board WHERE id in :ids ORDER BY date DESC", nativeQuery = true)
    List<BoardEntity> findByIds(@Param("ids") List<Integer> ids);

}
