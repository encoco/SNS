package com.example.demo.DTO;

import com.example.demo.entity.UsersEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsersDTO {
	private int id;
	private String username;
	private String nickname;
    private String password;
    private String img;
    private String state_message;
    private String phone;
	private String email;

	@Builder.Default
    private String role = "ROLE_USER";

	public static UsersDTO toDTO(UsersEntity dto) {
        return UsersDTO.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .nickname(dto.getNickname())
                .password(dto.getPassword())
                .state_message(dto.getState_message())
                .img(dto.getImg())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .role(dto.getRole())
                .build();
    }
}
