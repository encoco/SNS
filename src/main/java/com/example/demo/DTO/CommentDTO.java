package com.example.demo.DTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.BoardEntity;
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
   
   @Builder.Default
    private String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));

   public static CommentDTO toDTO(CommentEntity entity) {
      return CommentDTO.builder()
            .comment_id(entity.getComment_id())
            .board_id(entity.getBoardId())
            .id(entity.getUserId())
            .date(entity.getDate())
            .build();
   }
}