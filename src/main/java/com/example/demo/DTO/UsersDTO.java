package com.example.demo.DTO;

import org.springframework.web.multipart.MultipartFile;

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
    private String profile_img;
    private String state_message;
    private MultipartFile imgpath;
    private String phone;
	private String email;
	private String changePassword;
	private String original;
	@Builder.Default
    private String role = "ROLE_USER";

	public static UsersDTO toDTO(UsersEntity dto) {
        return UsersDTO.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .nickname(dto.getNickname())
                .password(dto.getPassword())
                .state_message(dto.getState_message())
                .profile_img(dto.getProfile_img())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .role(dto.getRole())
                .build();
    }
}
