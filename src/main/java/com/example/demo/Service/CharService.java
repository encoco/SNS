package com.example.demo.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.example.demo.DTO.ChatDTO;
import com.example.demo.Repository.ChatRepository;
import com.example.demo.Repository.UsersRepository;
import com.example.demo.entity.ChatEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CharService {
	private final ChatRepository chatRepository;
	private final UsersRepository uRepository;

	
	
	public List<ChatDTO> selectRoom(int userId) {
		List<ChatEntity> entity = chatRepository.findByUserId(userId);
		if (entity != null) {
			List<ChatDTO> dto = ChatDTO.ToDtoList(entity);
			return dto;
		}
		return null;
	}

	@Transactional
	public ChatDTO CreateRoom(List<Integer> userIds, int myId) {
		String roomNumber = UUID.randomUUID().toString();
		String name = "";
		ChatDTO dto = new ChatDTO();
		String ids = makeIds(userIds);

		while (chatRepository.existsByRoomNumber(roomNumber)) {
			roomNumber = UUID.randomUUID().toString();
		}

		for (Integer userId : userIds) {
			if (userId == myId)
				continue;
			if (name.length() > 0)
				name += ",";
			name += uRepository.findNicknameById(userId);
		}
		for (Integer userId : userIds) {
			ChatEntity chatUser = new ChatEntity();
			chatUser.setRoomname(name);
			chatUser.setRoomNumber(roomNumber);
			chatUser.setUserId(userId);
			chatUser.setJoinId(ids);
			chatRepository.save(chatUser);
		}
		ChatEntity create = chatRepository.findByJoinIdAndUserId(ids, myId);
		return ChatDTO.toDTO(create);
	}

	public ChatDTO findRoom(List<Integer> userIds, int myId) {
		Collections.sort(userIds);
		String userIdsString = userIds.stream().map(String::valueOf).collect(Collectors.joining(","));
		ChatEntity selectE = chatRepository.findByJoinIdAndUserId(userIdsString, myId);
		if (selectE != null)
			return ChatDTO.toDTO(selectE);
		return null;
	}

	public String makeIds(List<Integer> userIds) {
		Collections.sort(userIds);
		String userIdsString = userIds.stream().map(String::valueOf).collect(Collectors.joining(","));
		return userIdsString;
	}
}
