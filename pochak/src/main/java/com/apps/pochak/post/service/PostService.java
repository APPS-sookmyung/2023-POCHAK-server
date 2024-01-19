package com.apps.pochak.post.service;

import com.apps.pochak.comment.domain.repository.CommentRepository;
import com.apps.pochak.global.apiPayload.exception.GeneralException;
import com.apps.pochak.login.jwt.JwtService;
import com.apps.pochak.member.domain.Member;
import com.apps.pochak.post.domain.Post;
import com.apps.pochak.post.domain.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.apps.pochak.global.apiPayload.code.status.ErrorStatus.INVALID_POST_ID;
import static com.apps.pochak.global.apiPayload.code.status.ErrorStatus.NOT_YOUR_POST;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final JwtService jwtService;

    @Transactional
    public void deletePost(final Long postId) {
        final Member loginMember = jwtService.getLoginMember();
        final Post post = postRepository.findById(postId).orElseThrow(() -> new GeneralException(INVALID_POST_ID));
        if (!post.getOwner().getId().equals(loginMember.getId())) {
            throw new GeneralException(NOT_YOUR_POST);
        }
        postRepository.delete(post);
        commentRepository.bulkDeleteByPost(post);
    }
}
