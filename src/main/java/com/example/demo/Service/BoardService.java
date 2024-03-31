package com.example.demo.Service;

import org.springframework.stereotype.Service;

import com.example.demo.DTO.BoardDTO;
import com.example.demo.Repository.BoardRepository;
import com.example.demo.entity.BoardEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

	public final BoardRepository boardRepository;
	
	public void writeBoard(BoardDTO boardDTO) {
		BoardEntity board = BoardEntity.toEntity(boardDTO);
		boardRepository.save(board);
	}
	
}
