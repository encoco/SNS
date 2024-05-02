package com.example.demo.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.example.demo.DTO.BoardLikeDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "boardlike")
@Entity
public class BoardLikeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int boardlike_id;
	private int id;
	private int board_id;
	@Builder.Default
    private String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
	
	public static BoardLikeEntity toEntity(BoardLikeDTO dto) {
        return BoardLikeEntity.builder()
                .boardlike_id(dto.getBoardlike_id())
        		.board_id(dto.getBoard_id())
                .id(dto.getId())
                .date(dto.getDate())
                .build();
    }
}
