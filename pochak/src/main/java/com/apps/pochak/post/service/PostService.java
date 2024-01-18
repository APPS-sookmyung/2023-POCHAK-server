package com.apps.pochak.post.service;

import com.apps.pochak.comment.domain.Comment;
import com.apps.pochak.comment.domain.repository.CommentRepository;
import com.apps.pochak.follow.domain.repository.FollowRepository;
import com.apps.pochak.global.apiPayload.exception.GeneralException;
import com.apps.pochak.likes.domain.repository.LikeRepository;
import com.apps.pochak.login.jwt.JwtService;
import com.apps.pochak.member.domain.Member;
import com.apps.pochak.post.domain.Post;
import com.apps.pochak.post.domain.repository.PostRepository;
import com.apps.pochak.post.dto.response.PostDetailResponse;
import com.apps.pochak.tag.domain.Tag;
import com.apps.pochak.tag.domain.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.apps.pochak.global.apiPayload.code.status.ErrorStatus.PRIVATE_POST;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final FollowRepository followRepository;
    private final TagRepository tagRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final JwtService jwtService;

    public PostDetailResponse getPostDetail(final Long postId) {
        final Member loginMember = jwtService.getLoginMember();
        final Post post = postRepository.findPostById(postId);
        final List<Tag> tagList = tagRepository.findTagByPost(post);
        if (post.isPrivate() && !isAccessAuthorized(post, tagList, loginMember)) {
            throw new GeneralException(PRIVATE_POST);
        }
        final Boolean isFollow = isMyPost(post, loginMember) ?
                null : followRepository.existsBySenderAndReceiver(loginMember, post.getOwner());
        final Boolean isLike = likeRepository.existsByLikeMemberAndLikedPost(loginMember, post);
        final int likeCount = likeRepository.countByLikedPost(post);
        final Comment comment = commentRepository.findFirstByPost(post).orElse(null);

        return PostDetailResponse.of()
                .post(post)
                .tagList(tagList)
                .isFollow(isFollow)
                .isLike(isLike)
                .likeCount(likeCount)
                .recentComment(comment)
                .build();
    }

    private Boolean isAccessAuthorized(final Post post,
                                       final List<Tag> tagList,
                                       final Member loginMember) {
        final List<String> taggedMemberHandleList = tagList.stream()
                .map(
                        tag -> tag.getMember().getHandle()
                ).collect(Collectors.toList());
        return isMyPost(post, loginMember) || taggedMemberHandleList.contains(loginMember.getHandle());
    }

    private Boolean isMyPost(final Post post,
                             final Member loginMember) {
        return post.getOwner().getId().equals(loginMember.getId());
    }
}
