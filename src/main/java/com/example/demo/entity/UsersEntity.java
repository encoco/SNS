package com.example.demo.entity;

import DTO.UsersDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Users")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsersEntity {
	@Override
	public String toString() {
		return "UsersEntity [id=" + id + ", username=" + username + ", password=" + password + ", phone=" + phone
				+ ", email=" + email + ", role=" + role + "]";
	}


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id; //DB가서 pk id로 바꾸기
	private String username;
    private String password;
    private String phone;
	private String email;

	@Builder.Default
	private String role = "ROLE_USER";

	public int getId() {
		return id;
	}
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	public String getPhone() {
		return phone;
	}
	public String getEmail() {
		return email;
	}
	public String getRole() {
		return role;
	}


	public static UsersEntity toEntity(UsersDTO dto) {
        return UsersEntity.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .role(dto.getRole())
                .build();
    }


}
