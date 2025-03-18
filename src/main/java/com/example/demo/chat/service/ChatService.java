package com.example.demo.chat.service;

import com.example.demo.board.service.BoardService;
import com.example.demo.chat.dto.ChatDTO;
import com.example.demo.chat.dto.ChatMessageDTO;
import com.example.demo.chat.entity.ChatEntity;
import com.example.demo.chat.entity.ChatMessageEntity;
import com.example.demo.chat.repository.ChatMessageRepository;
import com.example.demo.chat.repository.ChatRepository;
import com.example.demo.communityChat.dto.CCJDTO;
import com.example.demo.communityChat.dto.CCMDTO;
import com.example.demo.communityChat.dto.CommunityChatDTO;
import com.example.demo.communityChat.entity.CCMEntity;
import com.example.demo.communityChat.entity.CommunityChatEntity;
import com.example.demo.communityChat.entity.CommunityChatJoinEntity;
import com.example.demo.communityChat.repository.CCMRepository;
import com.example.demo.communityChat.repository.CommuChatJoinRepository;
import com.example.demo.communityChat.repository.CommuChatRepository;
import com.example.demo.user.entity.UsersEntity;
import com.example.demo.user.repository.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
        message.setNickname(usersRepository.findNicknameById(message.getId()));
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
        message.setNickname(usersRepository.findNicknameById(message.getId()));
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
            ChatEntity entity = chatRepository.findRoomNumberByJoinIdAndId(ids, myId);
            if (entity == null) {
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
            } else {
                System.out.println(entity);
                return ChatDTO.toDTO(entity);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

}
