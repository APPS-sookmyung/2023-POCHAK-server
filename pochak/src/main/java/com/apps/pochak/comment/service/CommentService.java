package com.apps.pochak.comment.service;

import com.apps.pochak.comment.domain.Comment;
import com.apps.pochak.comment.dto.ChildCommentDto;
import com.apps.pochak.comment.dto.CommentResDto;
import com.apps.pochak.comment.dto.ParentCommentDto;
import com.apps.pochak.comment.repository.CommentRepository;
import com.apps.pochak.common.BaseException;
import com.apps.pochak.post.domain.Post;
import com.apps.pochak.post.repository.PostRepository;
import com.apps.pochak.user.domain.User;
import com.apps.pochak.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.apps.pochak.common.BaseResponseStatus.DATABASE_ERROR;
import static com.apps.pochak.common.BaseResponseStatus.INVALID_COMMENT_ID;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;


    public CommentResDto getComment(String postPK, String loginUserHandle) throws BaseException{
        try{

            Post thisPost=postRepository.findPostByPostPK(postPK);
            // repository 에서 thisParentComments 가져오기
            List<Comment> thisParentComments = commentRepository.findDescCommentsByPostPK(postPK);
            // ParentDto생성 stream,map
            List<ParentCommentDto> commentsResDto = thisParentComments.stream().map(
                    comment -> {
                        try {
                            User commentedUser=userRepository.findUserByUserHandle(comment.getCommentUserHandle());
                            String userProfileImage=commentedUser.getProfileImage();
                            String userHandle=comment.getCommentUserHandle();
                            LocalDateTime uploadedTime= LocalDateTime.parse(comment.getUploadedDate().substring(7));
                            String content=comment.getContent();
                            List<String> childCommentSKs;
                            List<ChildCommentDto> childCommentDtos =new ArrayList<>();
                            if (comment.getChildCommentSKs() != null) {
                                childCommentSKs = comment.getChildCommentSKs();
                                childCommentDtos=childCommentSKs.stream().map(
                                        childCommentSK -> {
                                            try {
                                                Comment childComment = commentRepository.findCommentByCommentSK(postPK,childCommentSK);
                                                User commentedChildUser=userRepository.findUserByUserHandle(childComment.getCommentUserHandle());
                                                String childUserProfileImage=commentedChildUser.getProfileImage();
                                                String childUserHandle=commentedChildUser.getHandle();
                                                LocalDateTime childUploadedTime=LocalDateTime.parse(childComment.getUploadedDate().substring(7));
                                                String childContent=childComment.getContent();
                                                ChildCommentDto childCommentDto=new ChildCommentDto(childUserProfileImage,
                                                        childUserHandle,
                                                        childUploadedTime,
                                                        childContent);
                                                return childCommentDto;
                                            }
                                            catch (Exception e){
                                                throw new RuntimeException("Parentcomment의 childComment가 없는지 확인해주세요");
                                            }


                                        }
                                ).collect(Collectors.toList());
                            } else {
                                childCommentSKs = new ArrayList<>();
                            }


                            ParentCommentDto parentCommentDto=new ParentCommentDto(userProfileImage,
                                    userHandle,
                                    uploadedTime,
                                    content,
                                    childCommentDtos);

                            return parentCommentDto;
                        } catch (Exception e){
                            throw new RuntimeException("해당 Post에 더미 Comment 데이터가 없는지 확인해주세요");
                        }
                    }
            ).collect(Collectors.toList());
            // ParentDto 하위에 ChildDto 채우기
            // 전체 list CommentsResDto return
            return (CommentResDto) commentsResDto;

        }
        catch (BaseException e){
            throw e;
        }catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
