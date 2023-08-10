package com.apps.pochak.comment.service;

import com.apps.pochak.comment.domain.Comment;
import com.apps.pochak.comment.dto.ChildCommentDto;
import com.apps.pochak.comment.dto.CommentResDto;
import com.apps.pochak.comment.dto.CommentUploadRequestDto;
import com.apps.pochak.comment.dto.ParentCommentDto;
import com.apps.pochak.comment.repository.CommentRepository;
import com.apps.pochak.common.BaseException;
import com.apps.pochak.post.domain.Post;
import com.apps.pochak.post.repository.PostRepository;
import com.apps.pochak.publish.repository.PublishRepository;
import com.apps.pochak.tag.repository.TagRepository;
import com.apps.pochak.user.domain.User;
import com.apps.pochak.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.apps.pochak.common.BaseResponseStatus.DATABASE_ERROR;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository repository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final PublishRepository publishRepository;

    @Transactional
    public CommentResDto parentcommentUpload(String postPK, CommentUploadRequestDto requestDto, String loginUserHandle) throws BaseException {
        try{
            // comment Entity 생성

            User loginUser=userRepository.findUserByUserHandle(loginUserHandle);
            Comment comment= requestDto.toEntity(postPK,loginUser);
            commentRepository.saveComment(comment);

            // Post에 ParentComment 저장하는 리스트에도 업데이트 코드 추가
            Post commentedPost=postRepository.findPostByPostPK(postPK);
            commentedPost.getParentCommentSKs().add(comment.getUploadedDate());


            // ParentCommentDto 생성
            ParentCommentDto parentCommentDto=new ParentCommentDto(loginUser,comment);

            // List<ParentCommentDto> 생성

            List<ParentCommentDto> parentCommentDtoList=commentedPost.getParentCommentSKs().stream().map(
                    parentCommentSK ->{
                        try {
                            Comment eachComment=commentRepository.findCommentByCommentSK(postPK,parentCommentSK);
                            return new ParentCommentDto(loginUser,eachComment);
                        }catch (BaseException e){
                            throw new RuntimeException(e);
                        }
                    }
            ).collect(Collectors.toList());

            // 새로운 ParentCommentDto 넣어주기
            parentCommentDtoList.add(parentCommentDto);
            return new CommentResDto(parentCommentDtoList);

        } catch(BaseException e){
            throw e;
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public CommentResDto childcommentUpload(String postPK, CommentUploadRequestDto requestDto, String loginUserHandle) throws BaseException {
        try{
            // childComment Entity 생성
            User loginUser=userRepository.findUserByUserHandle(loginUserHandle);
            Comment childComment= requestDto.toEntity(postPK,loginUser);
            commentRepository.saveComment(childComment);

            // 부모의 Comment 객체에 childCommentSKs add
            Comment parentComment=commentRepository.findCommentByCommentSK(postPK,requestDto.getParentCommentSK());
            parentComment.getChildCommentSKs().add(childComment.getUploadedDate());

            // ChildCommentDto 생성
            ChildCommentDto childCommentDto=new ChildCommentDto(loginUser,childComment);
            
            // create Response :  List<ParentCommentDto> 생성
            Post commentedPost=postRepository.findPostByPostPK(postPK);
            List<ParentCommentDto> parentCommentDtoList=commentedPost.getParentCommentSKs().stream().map(
                    parentCommentSK ->{
                        try {
                            Comment eachComment=commentRepository.findCommentByCommentSK(postPK,parentCommentSK);
                            // 부모 dto의 경우 childDto를 add하여 반환
                            if(parentCommentSK==requestDto.getParentCommentSK()){
                                ParentCommentDto parentCommentDto=new ParentCommentDto(loginUser,eachComment);
                                parentCommentDto.getChildComments().add(childCommentDto);
                                return parentCommentDto;
                            }
                            return new ParentCommentDto(loginUser,eachComment);
                        }catch (BaseException e){
                            throw new RuntimeException(e);
                        }
                    }
            ).collect(Collectors.toList());

            return new CommentResDto(parentCommentDtoList);

        } catch(BaseException e){
            throw e;
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
