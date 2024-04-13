package com.example.demo.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.DTO.BoardDTO;
import com.example.demo.Repository.BoardRepository;
import com.example.demo.entity.BoardEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

	public final BoardRepository boardRepository;

	public List<BoardDTO> getPost(int id){
		if(boardRepository.findByid(id) != null) {
			List<BoardEntity> entity = boardRepository.findByid(id);
			List<BoardDTO> dto = BoardDTO.ToDtoList(entity);
			return dto;
		}
		return null;
	}

	public void writeBoard(BoardDTO boardDTO) {
		BoardEntity board = BoardEntity.toEntity(boardDTO);
		System.out.println(board);
		boardRepository.save(board);
	}

}
