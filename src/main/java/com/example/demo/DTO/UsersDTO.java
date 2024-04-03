package com.example.demo.DTO;

import com.example.demo.entity.UsersEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Data
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsersDTO {
	private int id;
	private String username;
	private String nickname;
    private String password;
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
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .role(dto.getRole())
                .build();
    }
}
