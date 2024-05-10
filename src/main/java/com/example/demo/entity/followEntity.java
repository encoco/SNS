package com.example.demo.entity;

import com.example.demo.DTO.BoardDTO;
import com.example.demo.DTO.followDTO;

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

@Entity
@Builder
@Table(name = "Follow")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class followEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int follow_id;
	
	@Column(name = "follower_id")
	private int followerId;
	
	@Column(name = "following_id")
	private int followingId;
	
	public static followEntity toEntity(followDTO dto) {
        return followEntity.builder()
        		.followerId(dto.getFollowerId())
        		.followingId(dto.getFollowingId())
                .build();
    }

}
