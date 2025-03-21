package com.example.demo.communityChat.entity;

import com.example.demo.communityChat.dto.CommunityChatDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@Entity
@Table(name = "communitychat")
@AllArgsConstructor
@NoArgsConstructor
public class CommunityChatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "communitychat_id")
    private int communitychatId;
    private int id;
    private String title;
    private String description;
    private String img;

    @Builder.Default
    private String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));

    public static CommunityChatEntity toEntity(CommunityChatDTO entity) {
        return CommunityChatEntity.builder()
                .communitychatId(entity.getCommunitychatId())
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .img(entity.getImgpath())
                .date(entity.getDate())
                .build();
    }

    public static List<CommunityChatEntity> toEntityList(List<CommunityChatDTO> entitis) {
        List<CommunityChatEntity> dtos = new ArrayList<>();
        for (CommunityChatDTO entity : entitis) {
            CommunityChatEntity dto = new CommunityChatEntity();
            dto.setCommunitychatId(entity.getCommunitychatId());
            dto.setId(entity.getId());
            dto.setTitle(entity.getTitle());
            dto.setDescription(entity.getDescription());
            dto.setImg(entity.getImgpath());
            dto.setDate(entity.getDate());
            dtos.add(dto);
        }
        return dtos;
    }

}
