package com.example.demo.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.example.demo.DTO.CommunityChatDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@Entity
@Table(name = "communitychat_join")
@AllArgsConstructor
@NoArgsConstructor
public class CommunityChatJoinEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ccj_id")
	private int ccjId;
	
	@Column(name="communitychat_id")
	private int communitychatId;
	private String title;
	private String imgpath;
	private int id;
	
	@Builder.Default
	private String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
	
	public static CommunityChatJoinEntity toEntity(CommunityChatDTO entity) {
        return CommunityChatJoinEntity.builder()
                .communitychatId(entity.getCommunitychatId())
                .title(entity.getTitle())
                .imgpath(entity.getImgpath())
                .id(entity.getId())
                .date(entity.getDate())
                .build();
    }
	
	
	
}
