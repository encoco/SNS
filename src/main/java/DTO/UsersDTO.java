package DTO;

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
    private String password;
    private String phone;
	private String email;

	@Builder.Default
    private String role = "ROLE_USER";



	public static UsersDTO toDTO(UsersEntity dto) {
        return UsersDTO.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .role(dto.getRole())
                .build();
    }



	@Override
	public String toString() {
		return "UsersDTO [id=" + id + ", username=" + username + ", password=" + password + ", phone=" + phone
				+ ", email=" + email + ", role=" + role + "]";
	}



}
