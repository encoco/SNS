package com.example.demo.DTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.example.demo.entity.CommentEntity;

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
public class CommentDTO {
	private int comment_id;
	private int board_id;
	private int id;
	private String comment;
	private String nickname;
	private String profile_img;

	@Builder.Default
    private String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));

	public static CommentDTO toDTO(CommentEntity entity) {
		return CommentDTO.builder()
				.comment_id(entity.getComment_id())
				.board_id(entity.getBoardId())
				.id(entity.getId())
				.nickname(entity.getNickname())
				.comment(entity.getComment())
				.profile_img(entity.getProfile_img())
				.date(entity.getDate())
				.build();
	}
}
