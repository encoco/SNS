package com.example.demo.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Arrays;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.CCJDTO;
import com.example.demo.DTO.CCMDTO;
import com.example.demo.DTO.ChatDTO;
import com.example.demo.DTO.ChatMessageDTO;
import com.example.demo.DTO.CommunityChatDTO;
import com.example.demo.DTO.UsersDTO;
import com.example.demo.DTO.UsersInfoDTO;
import com.example.demo.Repository.CCMRepository;
import com.example.demo.Repository.ChatMessageRepository;
import com.example.demo.Repository.ChatRepository;
import com.example.demo.Repository.CommuChatJoinRepository;
import com.example.demo.Repository.CommuChatRepository;
import com.example.demo.Repository.UsersRepository;
import com.example.demo.entity.CCMEntity;
import com.example.demo.entity.ChatEntity;
import com.example.demo.entity.ChatMessageEntity;
import com.example.demo.entity.CommunityChatEntity;
import com.example.demo.entity.CommunityChatJoinEntity;
import com.example.demo.entity.UsersEntity;
import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {
	private final ChatRepository chatRepository;
	private final ChatMessageRepository messageRepository;
	private final CommuChatRepository commuRepository;
	private final CommuChatJoinRepository CCJRepository;
	private final CCMRepository ccmrepository;
	private final UsersRepository usersRepository;
	private final BoardService boardService;

	public List<ChatDTO> selectRoom(int userId) {
		List<ChatEntity> entity = chatRepository.CustomfindById(userId);
		if (entity != null) {
			List<ChatDTO> dto = ChatDTO.ToDtoList(entity);
			return dto;
		}
		return null;
	}

	@Transactional
	public ChatDTO CreateRoom(List<Integer> userIds, int myId) {
		String roomNumber = UUID.randomUUID().toString();
		String ids = makeIds(userIds);
		while (chatRepository.existsByRoomNumber(roomNumber)) {
			roomNumber = UUID.randomUUID().toString();
		}

		for (Integer userId : userIds) {
			ChatEntity chatUser = new ChatEntity();

			chatUser.setRoomNumber(roomNumber);
			chatUser.setId(userId);
			chatUser.setJoinId(ids);
			
			chatRepository.save(chatUser);
		}
		ChatEntity create = chatRepository.findByJoinIdAndId(ids, myId);
		return ChatDTO.toDTO(create);
	}

	public ChatDTO findRoom(List<Integer> userIds, int myId) {
		Collections.sort(userIds);
		String userIdsString = userIds.stream().map(String::valueOf).collect(Collectors.joining(","));
		ChatEntity selectE = chatRepository.findByJoinIdAndId(userIdsString, myId);
		if (selectE != null)
			return ChatDTO.toDTO(selectE);
		return null;
	}

	public String makeIds(List<Integer> userIds) {
		Collections.sort(userIds);
		String userIdsString = userIds.stream().map(String::valueOf).collect(Collectors.joining(","));
		return userIdsString;
	}

	public ChatMessageDTO saveChat(ChatMessageDTO message) {
		ChatMessageEntity entity = ChatMessageEntity.toEntity(message);
		messageRepository.save(entity);

		UsersEntity user = usersRepository.findById(entity.getId()).orElseThrow(() -> new RuntimeException("User not found"));
		message.setProfile_img(user.getProfile_img());
		return message;
	}

	public List<ChatMessageDTO> getMessage(String roomNumber) {
		List<ChatMessageEntity> entity = messageRepository.findByroomNumber(roomNumber);
		return ChatMessageDTO.ToDtoList(entity);
	}

	public void CreateCommChat(CommunityChatDTO dto) {
		CommunityChatEntity entity = CommunityChatEntity.toEntity(dto);
		if (dto.getImg() != null)
			entity.setImg(boardService.uploadFile(dto.getImg(), "CommunityChat"));
		commuRepository.save(entity);

	}

	public List<CommunityChatDTO> selectAllCommuRoom() {
		List<CommunityChatEntity> entity = commuRepository.findAll();
		return CommunityChatDTO.toDTOList(entity);

	}

	public void joinCommunity(CommunityChatDTO dto) {
		System.out.println("dto : " + dto);
		CommunityChatJoinEntity entity = CommunityChatJoinEntity.toEntity(dto);
		System.out.println("entity : " + entity);
		CCJRepository.save(entity);
	}

	public List<CCJDTO> selectCommuRoom(int userIdFromToken) {
		return CCJDTO.toDTOList(CCJRepository.findAllById(userIdFromToken));
	}

	public CCMDTO saveCommChat(CCMDTO message) {
		CCMEntity entity = CCMEntity.toEntity(message);
		ccmrepository.save(entity);
		
		UsersEntity user = usersRepository.findById(entity.getId()).orElseThrow(() -> new RuntimeException("User not found"));
		message.setProfile_img(user.getProfile_img());
		return message;
	}

	public List<CCMDTO> getCommMessage(int communitychat_id) {
		List<CCMEntity> entity = ccmrepository.findBycommunitychatId(communitychat_id);
		return CCMDTO.ToDtoList(entity);
	}

	public ChatDTO findRoom(int id, int myId) {
		List<Integer> userIds = new ArrayList<>(List.of(id, myId)); // 변경 가능한 리스트로 변환
		String ids = makeIds(userIds);
		try {
			ChatEntity entity = chatRepository.findRoomNumberByJoinIdAndId(ids,myId);
			if(entity == null) {
				String roomNumber = UUID.randomUUID().toString();
				while (chatRepository.existsByRoomNumber(roomNumber)) {
					roomNumber = UUID.randomUUID().toString();
				}
				for (Integer userId : userIds) {
					ChatEntity chatUser = new ChatEntity();

					chatUser.setRoomNumber(roomNumber);
					chatUser.setId(userId);
					chatUser.setJoinId(ids);
					
					chatRepository.save(chatUser);
				}
				ChatEntity create = chatRepository.findByJoinIdAndId(ids, myId);
				System.out.println(create);
				return ChatDTO.toDTO(create);
			}
			else {
				System.out.println(entity);
				return ChatDTO.toDTO(entity);
			}
		}catch(Exception e) { System.out.println(e);}
		return null;
	}

}
