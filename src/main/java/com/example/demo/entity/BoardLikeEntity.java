package com.example.demo.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.example.demo.DTO.BoardLikeDTO;

import jakarta.persistence.Column;
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
	@Column(name = "id")
	private int userId;

	@Column(name = "board_id")
	private int boardId;

	@Builder.Default
	private String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));

	public static BoardLikeEntity toEntity(BoardLikeDTO dto) {
		return BoardLikeEntity.builder().boardlike_id(dto.getBoardlike_id()).boardId(dto.getBoard_id()).userId(dto.getId())
				.date(dto.getDate()).build();
	}
}
