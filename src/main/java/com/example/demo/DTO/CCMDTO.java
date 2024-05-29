package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.entity.CCMEntity;
import com.example.demo.entity.ChatMessageEntity;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CCMDTO {
	int commessage_id;
	int communitychat_id;
	int id;
	String nickname;
	String content;
	String profile_img;
	
	@Builder.Default
	private String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
	// Entity만들고 컨트롤러에 그룹챗 넘어오는지 확인부터 다시 시작

	public static List<CCMDTO> ToDtoList(List<CCMEntity> entities) {
		List<CCMDTO> dtos = new ArrayList<>();
		for (CCMEntity entity : entities) {
			CCMDTO dto = new CCMDTO();
			dto.setCommessage_id(entity.getCommessageId());
			dto.setCommunitychat_id(entity.getCommunitychatId());
			dto.setId(entity.getId());
			dto.setProfile_img(entity.getProfile_img());
			dto.setNickname(entity.getNickname());
			dto.setContent(entity.getContent());
			dto.setDate(entity.getDate());
			dtos.add(dto);
		}
		return dtos;
	}
	
	public static CCMDTO toDTO(CCMEntity dto) {
        return CCMDTO.builder()
            .commessage_id(dto.getCommessageId())
            .communitychat_id(dto.getCommunitychatId())
            .id(dto.getId())
            .nickname(dto.getNickname())
            .profile_img(dto.getProfile_img())
            .content(dto.getContent())
            .date(dto.getDate())
            .build();
	}

}
