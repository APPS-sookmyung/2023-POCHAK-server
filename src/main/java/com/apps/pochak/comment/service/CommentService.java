package com.apps.pochak.comment.service;

import com.apps.pochak.comment.domain.Comment;
import com.apps.pochak.comment.domain.repository.CommentRepository;
import com.apps.pochak.comment.dto.response.CommentElements;
import com.apps.pochak.global.apiPayload.exception.GeneralException;
import com.apps.pochak.login.jwt.JwtService;
import com.apps.pochak.member.domain.Member;
import com.apps.pochak.post.domain.Post;
import com.apps.pochak.post.domain.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.apps.pochak.global.apiPayload.code.status.ErrorStatus.PRIVATE_POST;

@Service
@RequiredArgsConstructor
public class CommentService {
    public static final int DEFAULT_PAGING_SIZE = 30;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final JwtService jwtService;

    @Transactional
    public CommentElements getComments(
            final Long postId,
            final Pageable pageable
    ) {
        final Member loginMember = jwtService.getLoginMember();
        final Post post = postRepository.findPostById(postId);
        if (post.isPrivate()) throw new GeneralException(PRIVATE_POST);
        final Page<Comment> commentList = commentRepository.findParentCommentByPost(post, pageable);
        return new CommentElements(loginMember, commentList);
    }
}
