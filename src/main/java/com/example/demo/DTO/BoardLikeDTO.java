package com.example.demo.DTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.entity.BoardLikeEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BoardLikeDTO {
	private int boardlike_id;
	private int id;
	private int board_id;
	@Builder.Default
    private String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));

	public static BoardLikeDTO toDTO(BoardLikeEntity entity) {
        return BoardLikeDTO.builder()
                .boardlike_id(entity.getBoardlike_id())
        		.board_id(entity.getBoardId())
                .id(entity.getUserId())
                .date(entity.getDate())
                .build();
    }
	public static List<BoardLikeDTO> ToDtoList(List<BoardLikeEntity> entities) {
	    List<BoardLikeDTO> dtos = new ArrayList<>();
	    for (BoardLikeEntity entity : entities) {
	        BoardLikeDTO dto = new BoardLikeDTO();
	        dto.setBoard_id(entity.getBoardId());
	        dto.setId(entity.getUserId());
	        dto.setDate(entity.getDate());
	        dtos.add(dto);
	    }
	    return dtos;
	}
}
