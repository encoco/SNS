package com.example.demo.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.DTO.BoardDTO;
import com.example.demo.Repository.testR;
import com.example.demo.entity.BoardEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class testS {
	public final testR testR;
	
	public List<BoardDTO> getPost(int id){
		if(testR.findByid(id) != null) {
			List<BoardEntity> entity = testR.findByid(id);
			List<BoardDTO> dto = BoardDTO.ToDtoList(entity);
			return dto;
		}
		return null;
	}
	
	public void writeBoard(BoardDTO boardDTO) {
		BoardEntity board = BoardEntity.toEntity(boardDTO);
		System.out.println(board);
		testR.save(board);
	}
}
