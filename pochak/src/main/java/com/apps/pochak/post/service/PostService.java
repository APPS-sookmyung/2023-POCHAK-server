package com.apps.pochak.post.service;

import com.apps.pochak.comment.repository.CommentRepository;
import com.apps.pochak.common.BaseException;
import com.apps.pochak.post.domain.Post;
import com.apps.pochak.post.dto.PostResDto;
import com.apps.pochak.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public Post savePost(Post post) { return postRepository.savePost(post); }

    // PostResDto에서 사용할 parentComment조회 함수
    public List getParentComments(Post post) throws BaseException {
        List parentComments = post.getParentComments().stream().map(
                commentId -> {
                    try {
                        // commentRepository에 가서 comment 가져오기
                        return commentRepository.findCommentByCommentId(commentId).getContent();
                    }
                    catch (Exception e){
                        throw new RuntimeException("해당 Post의 ParentComments List에 더미 CommentId 데이터가 없는지 확인해주세요");
                    }
                })
                .collect(Collectors.toList());
        return parentComments;
    }

    public PostResDto getPostDetail(String postPK) throws BaseException {
        // PK로 찾기
        try{
            Post postByPostPK=postRepository.findPostWithPostPK(postPK);
            return new PostResDto(postByPostPK);
        }
        catch (BaseException e){
            throw e;
        }
    }
}
