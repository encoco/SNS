package com.example.demo.DTO;

import java.util.ArrayList;
import java.util.List;
import com.example.demo.entity.CommunityChatJoinEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CCJDTO {
	private int ccjId;
	private int roomNumber;
	private String roomname;
	private int id;
	private String imgpath;
	private String date;
	
	
	public static List<CCJDTO> toDTOList(List<CommunityChatJoinEntity> entitis) {
	    List<CCJDTO> dtos = new ArrayList<>();
	    for (CommunityChatJoinEntity entity : entitis) {
	    	CCJDTO dto = new CCJDTO();
	        dto.setCcjId(entity.getCcjId());
	        dto.setRoomNumber(entity.getCommunitychatId());
	        dto.setId(entity.getId());
	        dto.setImgpath(entity.getImgpath());
	        dto.setRoomname(entity.getTitle());
	        dto.setDate(entity.getDate());
	        dtos.add(dto);
	    }
	    return dtos;
	}
}
