package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.BoardEntity;

public interface BoardRepository extends JpaRepository<BoardEntity, Integer> {
	
	public List<BoardEntity> findByidOrderByDateDesc(int id);

	@Query(value = "SELECT b.board_id, b.id, u.nickname, u.profile_img, b.img, b.video, b.content, b.date  "
								+ "FROM board b JOIN users u ON b.id = u.id WHERE u.id IN :ids ORDER BY b.date DESC", nativeQuery = true)
	List<BoardEntity> findByIds(@Param("ids") List<Integer> ids);

}
