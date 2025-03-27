package com.example.demo.openChat.service;

import com.example.demo.board.service.BoardService;
import com.example.demo.openChat.dto.OpenChatDTO;
import com.example.demo.openChat.dto.OpenChatMemberDTO;
import com.example.demo.openChat.dto.OpenChatMessageDTO;
import com.example.demo.openChat.entity.OpenChatEntity;
import com.example.demo.openChat.entity.OpenChatMemberEntity;
import com.example.demo.openChat.entity.OpenChatMessageEntity;
import com.example.demo.openChat.repository.OpenChatMemberRepository;
import com.example.demo.openChat.repository.OpenChatMessageRepository;
import com.example.demo.openChat.repository.OpenChatRepository;
import com.example.demo.user.entity.UsersEntity;
import com.example.demo.user.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class OpenChatService {
    private final UsersRepository usersRepository;
    private final OpenChatRepository openChatRepository;
    private final OpenChatMemberRepository openMemberRepository;
    private final OpenChatMessageRepository openMessageRepository;
    private final BoardService boardService;

    public void CreateCommChat(OpenChatDTO dto) {
        OpenChatEntity entity = OpenChatEntity.toEntity(dto);
        if (dto.getImg() != null)
            entity.setImg(boardService.uploadFile(dto.getImg(), "CommunityChat"));
        openChatRepository.save(entity);
    }

    public List<OpenChatDTO> selectAllCommuRoom() {
        List<OpenChatEntity> entity = openChatRepository.findAll();
        return OpenChatDTO.toDTOList(entity);
    }

    public void joinCommunity(OpenChatDTO dto) {
        OpenChatMemberEntity entity = OpenChatMemberEntity.toEntity(dto);
        System.out.println("엔티티는 이거다 : " + entity);
        openMemberRepository.save(entity);
    }

    public List<OpenChatMemberDTO> selectCommuRoom(int userIdFromToken) {
        return OpenChatMemberDTO.toDTOList(openMemberRepository.findAllById(userIdFromToken));
    }

    public OpenChatMessageDTO saveCommChat(OpenChatMessageDTO message) {
        message.setNickname(usersRepository.findNicknameById(message.getId()));
        OpenChatMessageEntity entity = OpenChatMessageEntity.toEntity(message);
        openMessageRepository.save(entity);

        UsersEntity user = usersRepository.findById(entity.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        message.setProfile_img(user.getProfile_img());
        return message;
    }

    public List<OpenChatMessageDTO> getCommMessage(int openChatid) {
        List<OpenChatMessageEntity> entity = openMessageRepository.findByOpenChatId(openChatid);
        return OpenChatMessageDTO.ToDtoList(entity);
    }
}
