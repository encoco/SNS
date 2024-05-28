package com.example.demo.DTO;

import com.example.demo.entity.UsersEntity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsersInfoDTO {
	int id;
	String nickname;
	String img;
	String state_message;
	String phone;
	String email;
	String role;

	public static UsersInfoDTO toInfoDTO(UsersEntity entity) {
		return UsersInfoDTO.builder().id(entity.getId()).nickname(entity.getNickname()).img(entity.getImg())
				.state_message(entity.getState_message()).phone(entity.getPhone()).email(entity.getEmail()).build();
	}
	
	public static UsersInfoDTO toInfoDTO(UsersDTO entity) {
		return UsersInfoDTO.builder()
				.id(entity.getId())
				.nickname(entity.getNickname())
				.img(entity.getImg())
				.state_message(entity.getState_message())
				.phone(entity.getPhone())
				.email(entity.getEmail()).build();
	}

}
