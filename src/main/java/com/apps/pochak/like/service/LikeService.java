package com.apps.pochak.like.service;

import com.apps.pochak.follow.domain.repository.FollowRepository;
import com.apps.pochak.global.apiPayload.exception.GeneralException;
import com.apps.pochak.like.domain.LikeEntity;
import com.apps.pochak.like.domain.repository.LikeRepository;
import com.apps.pochak.like.dto.response.LikeElement;
import com.apps.pochak.like.dto.response.LikeElements;
import com.apps.pochak.login.jwt.JwtService;
import com.apps.pochak.member.domain.Member;
import com.apps.pochak.post.domain.Post;
import com.apps.pochak.post.domain.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.apps.pochak.global.BaseEntityStatus.ACTIVE;
import static com.apps.pochak.global.BaseEntityStatus.DELETED;
import static com.apps.pochak.global.apiPayload.code.status.ErrorStatus.POST_OWNER_LIKE;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final FollowRepository followRepository;
    private final JwtService jwtService;

    @Transactional
    public void likePost(final Long postId) {
        final Member loginMember = jwtService.getLoginMember();
        final Post post = postRepository.findPostById(postId);

        if (loginMember == post.getOwner())
            throw new GeneralException(POST_OWNER_LIKE);

        LikeEntity postLike = likeRepository.findByLikeMemberAndLikedPost(loginMember, post);
        if (postLike != null) {
            if (postLike.getStatus().equals(ACTIVE))
                postLike.setStatus(DELETED);
            else if (postLike.getStatus().equals(DELETED))
                postLike.setStatus(ACTIVE);
        } else {
            postLike = new LikeEntity(loginMember, post);
        }
        likeRepository.save(postLike);
    }

    @Transactional
    public LikeElements getMemberLikedPost(final Long postId) {
        final Member loginMember = jwtService.getLoginMember();
        final Post likedPost = postRepository.findPostById(postId);
        List<LikeEntity> likes = likeRepository.findByLikedPost(likedPost);

        final List<LikeElement> likeElements = likes.stream().map(
                like -> new LikeElement(like.getLikeMember(), followRepository.existsBySenderAndReceiver(loginMember, like.getLikeMember())
                )).toList();

        return new LikeElements(likeElements);
    }
}
