package com.example.demo.entity;


import com.example.demo.DTO.UsersDTO;
import com.example.demo.DTO.UsersInfoDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "Users")
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UsersEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id; //DB가서 pk id로 바꾸기
	private String username;
	private String nickname;
    private String password;
    private String profile_img;
    String state_message;
    private String phone;
	private String email;

	@Builder.Default
	private String role = "ROLE_USER";

	public static UsersEntity toEntity(UsersDTO dto) {
        return UsersEntity.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .nickname(dto.getNickname())
                .password(dto.getPassword())
                .profile_img(dto.getProfile_img())
                .state_message(dto.getState_message())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .role(dto.getRole())
                .build();
    }

}
