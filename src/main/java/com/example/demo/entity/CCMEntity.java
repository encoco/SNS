package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.example.demo.DTO.CCMDTO;
import com.example.demo.DTO.ChatDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Builder
@Data
@Entity
@Table(name="communitychatmessage")
@AllArgsConstructor
@NoArgsConstructor
public class CCMEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="commessage_id")
	int commessageId;
	@Column(name="communitychat_id")
	int communitychatId;
	int id;
	String nickname;
	String content;
	String profile_img;

	@Builder.Default
	private String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
	//Entity만들고 컨트롤러에 그룹챗 넘어오는지 확인부터 다시 시작

	public static CCMEntity toEntity(CCMDTO dto) {
	        return CCMEntity.builder()
	            .commessageId(dto.getCommessage_id())
	            .communitychatId(dto.getCommunitychat_id())
	            .id(dto.getId())
	            .nickname(dto.getNickname())
	            .content(dto.getContent())
	            .date(dto.getDate())
	            .build();
	}
}
