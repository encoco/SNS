package com.example.demo.board.entity;

import com.example.demo.board.dto.CommentDTO;
import com.example.demo.user.entity.UsersEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comment")
@Entity
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int comment_id;

    @Column(name = "board_id")
    private int boardId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private UsersEntity user;

    private String comment;

    @Builder.Default
    private String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));



    public static CommentEntity toEntity(CommentDTO dto) {
        UsersEntity userEntity = UsersEntity.builder()
                .id(dto.getId())
                .build();

        return CommentEntity.builder()
                .comment_id(dto.getComment_id())
                .boardId(dto.getBoard_id())
                .user(userEntity)
                .comment(dto.getComment())
                .date(dto.getDate())
                .build();
    }

    public static List<CommentDTO> ToDtoList(List<CommentEntity> entities) {
        List<CommentDTO> dtos = new ArrayList<>();
        for (CommentEntity entity : entities) {
            CommentDTO dto = new CommentDTO();
            dto.setComment_id(entity.getComment_id());
            dto.setBoard_id(entity.getBoardId());
            dto.setId(entity.getUser().getId());
            dto.setComment(entity.getComment());
            dto.setProfile_img(entity.getUser().getProfile_img());
            dto.setNickname(entity.getUser().getNickname());
            dto.setDate(entity.getDate());

            dtos.add(dto);
        }
        return dtos;
    }
}

