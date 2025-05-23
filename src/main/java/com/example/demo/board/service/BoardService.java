package com.example.demo.board.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.demo.alarm.entity.AlarmEntity;
import com.example.demo.alarm.repository.AlarmRepository;
import com.example.demo.board.dto.BoardDTO;
import com.example.demo.board.dto.BoardLikeDTO;
import com.example.demo.board.dto.CommentDTO;
import com.example.demo.board.entity.BoardEntity;
import com.example.demo.board.entity.BoardLikeEntity;
import com.example.demo.board.entity.CommentEntity;
import com.example.demo.board.event.BoardEvent;
import com.example.demo.board.event.BoardEventListener;
import com.example.demo.board.repository.BoardCommentRepository;
import com.example.demo.board.repository.BoardLikeRepository;
import com.example.demo.board.repository.BoardRepository;
import com.example.demo.config.S3Config;
import com.example.demo.follow.repository.FollowRepository;
import com.example.demo.user.entity.UsersEntity;
import io.jsonwebtoken.io.IOException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    public final BoardRepository boardRepository;
    public final BoardCommentRepository boardCommentRepository;
    public final AlarmRepository alarmRepository;
    public final BoardLikeRepository boardlike;
    public final FollowRepository fRepository;
    private final S3Config s3Config;
    private final BoardEventListener boardEventListener;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadFile(MultipartFile file, String path) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = UUID.randomUUID().toString() + extension; // 고유번호 + 확장자로 이름 리네임

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize()); // 파일 크기 설정

        // S3에 파일 업로드
        try {
            s3Config.amazonS3Client()
                    .putObject(new PutObjectRequest(bucket + "/" + path, newFilename, file.getInputStream(), metadata));
        } catch (SdkClientException | java.io.IOException e) {
            e.printStackTrace();
        }

        // 올바른 경로의 URL 주소를 생성하여 저장
        String s3Url = s3Config.amazonS3Client().getUrl(bucket + "/" + path, newFilename).toString();
        return s3Url;
    }

    public List<BoardDTO> getPost(int id) {
        if (boardRepository.existsByUserId(id)) {
            List<BoardEntity> entity = boardRepository.findByUser_IdOrderByDateDesc(id);
            return BoardDTO.ToDtoList(entity);
        }
        return null;
    }

    public List<BoardLikeDTO> getLike(int userId) {
        List<BoardEntity> userPosts = boardRepository.findByUser_IdOrderByDateDesc(userId);
        if (!userPosts.isEmpty()) {
            List<BoardLikeDTO> dtos = new ArrayList<>();
            for (BoardEntity post : userPosts) {
                List<BoardLikeEntity> likesForPost = boardlike.findByboardId(post.getBoardId());
                dtos.addAll(BoardLikeDTO.ToDtoList(likesForPost));
            }

            return dtos;
        }
        return null;
    }

    public void writeBoard(BoardDTO boardDTO) {
        String imgPath = "";

        if (boardDTO.getImg() != null) {
            for (MultipartFile img : boardDTO.getImg()) {
                if (imgPath.isEmpty() || imgPath == "") {
                    imgPath += uploadFile(img, "image");
                } else {
                    imgPath += "|" + uploadFile(img, "image");
                }
            }
            boardDTO.setImgpath(imgPath);
        }
        BoardEntity board = BoardEntity.toEntity(boardDTO);
        boardRepository.save(board);
    }

    @Transactional
    public void writeComment(CommentDTO commentDTO) {
        CommentEntity commentEntity = CommentEntity.toEntity(commentDTO);
        BoardEntity entity = boardRepository.findByBoardId(commentDTO.getBoard_id());
        int writerId = entity.getUser().getId();

        Optional<AlarmEntity> existingAlarm = alarmRepository.findByCriteria(writerId, commentDTO.getId(),
                "", commentDTO.getBoard_id());
        if (!existingAlarm.isPresent() && commentDTO.getId() != writerId) {
            BoardEvent event = new BoardEvent(this,commentDTO.getId(), writerId, commentDTO.getBoard_id());
            boardEventListener.publishEventComment(event);
        }
        boardCommentRepository.save(commentEntity);
    }

    @Transactional
    public int boardLike(BoardLikeDTO dto, int writerId) {
        BoardLikeEntity entity = BoardLikeEntity.toEntity(dto);
        Optional<BoardLikeEntity> existingLike = boardlike.findByBoardIdAndUserId(dto.getBoard_id(), dto.getId());

        if (existingLike.isPresent()) {
            // 이미 "좋아요"가 존재한다면 삭제
            boardlike.delete(existingLike.get());
            return 0;
        } else {
            Optional<AlarmEntity> existingAlarm = alarmRepository.findByCriteria(writerId, dto.getId(), "님이 글을 좋아합니다.", dto.getBoard_id());
            if (!existingAlarm.isPresent() && dto.getId() != writerId) {
                BoardEvent event = new BoardEvent(this,dto.getId(), writerId, dto.getBoard_id());
                boardEventListener.publishEventBoardLike(event);
            }
            boardlike.save(entity);
            return 1;
        }
    }

    @Transactional // 트랜잭션을 사용하여 업데이트를 보장
    public void updatePost(BoardDTO dto) { // findById == select * from board where board_id = dto.getBoard_id();
        BoardEntity post = boardRepository.findById(dto.getBoard_id()).orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        String imgpath = dto.getImgpath();
        if (dto.getImg() != null) {
            for (MultipartFile img : dto.getImg()) {
                if (imgpath.isEmpty() || imgpath == "") {
                    imgpath += uploadFile(img, "image");
                } else {
                    imgpath += "|" + uploadFile(img, "image");
                }
            }
        }
        post.setUpdateContent(dto.getContent(), imgpath);
    }

    @Transactional
    public void updateComment(CommentDTO commentDTO) {
        CommentEntity board = CommentEntity.toEntity(commentDTO);

        boardCommentRepository.save(board);
    }

    @Transactional
    public void DeleteBoard(int board_id) {
        boardRepository.deleteById(board_id);
    }

    public List<BoardDTO> getfollowPost(int userId) {
        List<Integer> followIds = fRepository.findFollowingIdByFollowerId(userId);

        List<BoardEntity> entity = boardRepository.findByUser_IdInOrderByDateDesc(followIds);

        return BoardDTO.ToDtoList(entity);
    }

    public List<BoardLikeDTO> getfollowLike(List<BoardDTO> posts) {
        List<Integer> postIds = posts.stream().map(BoardDTO::getBoard_id).collect(Collectors.toList());
        List<BoardLikeEntity> likeEntities = boardlike.findByBoardIds(postIds);

        return BoardLikeDTO.ToDtoList(likeEntities);
    }

    // 댓글 조회
    public List<CommentDTO> getComments(int boardId) {
        List<CommentEntity> commentEntities = boardCommentRepository.findByBoardIdOrderByDateDesc(boardId);
        return CommentEntity.ToDtoList(commentEntities);
    }

    @Transactional
    public void DeleteComment(CommentDTO commentDTO) {
        CommentEntity board = CommentEntity.toEntity(commentDTO);
        boardCommentRepository.delete(board);
    }

    public BoardDTO getSharePost(int board_id) {
        BoardEntity entity = boardRepository.findByBoardId(board_id);
        return BoardDTO.toDTO(entity);
    }

    public List<BoardLikeDTO> getShareLike(int boardId) {
        // 해당 userId에 해당하는 글을 찾습니다.
        List<BoardLikeEntity> likesForPost = boardlike.findByboardId(boardId);

        return BoardLikeDTO.ToDtoList(likesForPost);

    }

    public boolean existFollow(int userId, int myId) {
        return fRepository.existsByFollowerIdAndFollowingId(myId, userId);
    }

    public List<BoardDTO> getRandomPost() {
        try {
            List<BoardEntity> test = boardRepository.findRandomBoards();
            return BoardDTO.ToDtoList(test);

        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}