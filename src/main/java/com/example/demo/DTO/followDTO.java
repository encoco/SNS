package com.example.demo.DTO;

import com.example.demo.entity.followEntity;

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
public class followDTO {
	private int follow_id;
	private int followerId;
	private int followingId;

	public static followDTO toDTO(followEntity entity) {
        return followDTO.builder()
        		.followerId(entity.getFollowerId())
        		.followingId(entity.getFollowingId())
                .build();
    }
}
