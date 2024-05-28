package com.example.demo.DTO;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.entity.CommunityChatEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CommunityChatDTO {
	private int communitychatId;
	private int id;
	private String title;
	private String description;
	private String imgpath;
	private MultipartFile img;
	
	@Builder.Default
	private String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
	
	public static CommunityChatDTO toDTO(CommunityChatEntity entity) {
        return CommunityChatDTO.builder()
                .communitychatId(entity.getCommunitychatId())
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .imgpath(entity.getImg())
                .date(entity.getDate())
                .build();
    }

	public static List<CommunityChatDTO> toDTOList(List<CommunityChatEntity> entitis) {
	    List<CommunityChatDTO> dtos = new ArrayList<>();
	    for (CommunityChatEntity entity : entitis) {
	        CommunityChatDTO dto = new CommunityChatDTO();
	        dto.setCommunitychatId(entity.getCommunitychatId());
	        dto.setId(entity.getId());
	        dto.setTitle(entity.getTitle());
	        dto.setDescription(entity.getDescription());
	        dto.setImgpath(entity.getImg());
	        dto.setDate(entity.getDate());
	        dtos.add(dto);
	    }
	    return dtos;
	}
}
