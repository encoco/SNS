package com.example.demo.DTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.demo.entity.UsersEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SearchDTO {
	private int id;
	private String nickname;
	private String img;
	
	public static List<SearchDTO> toSearchDTO(List<UsersEntity> entity) {
		List<SearchDTO> dtos = new ArrayList<>();
		for(UsersEntity ent : entity) {
			SearchDTO dto = new SearchDTO();
			dto.setId(ent.getId());
			dto.setNickname(ent.getNickname());
			dto.setImg(ent.getImg());
			dtos.add(dto);
		}
		return dtos;
    }

	public static SearchDTO toDTO(UsersEntity entity) {
		SearchDTO dto = new SearchDTO();
		dto.setId(entity.getId());
		dto.setNickname(entity.getNickname());
		dto.setImg(entity.getImg());
		return dto;
	}
}
