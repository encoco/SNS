package com.example.demo.board.repository;

import com.example.demo.board.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<BoardEntity, Integer> {

    @Query(value = "SELECT b.board_id, b.id, u.nickname, u.profile_img, b.img, b.video, b.content, b.date  "
            + "FROM board b JOIN users u ON b.id = u.id WHERE u.id = :id ORDER BY b.date DESC", nativeQuery = true)
    public List<BoardEntity> findByidOrderByDateDesc(@Param("id") int id);

    @Query(value = "SELECT b.board_id, b.id, u.nickname, u.profile_img, b.img, b.video, b.content, b.date  "
            + "FROM board b JOIN users u ON b.id = u.id WHERE u.id IN :ids ORDER BY b.date DESC", nativeQuery = true)
    List<BoardEntity> findByIds(@Param("ids") List<Integer> ids);

    @Query(value = "SELECT * FROM board WHERE board_id = :board_id", nativeQuery = true)
    public BoardEntity findByBoardId(@Param("board_id") int board_id);

    @Query(value = "SELECT id FROM board WHERE board_id = :board_id", nativeQuery = true)
    public int findIdByboardId(@Param("board_id") int board_id);

    @Query(value = "SELECT * FROM board ORDER BY RAND() LIMIT 20", nativeQuery = true)
    List<BoardEntity> findRandomBoards();

}
