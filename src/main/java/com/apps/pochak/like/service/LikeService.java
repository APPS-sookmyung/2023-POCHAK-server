package com.apps.pochak.like.service;

import com.apps.pochak.alarm.domain.Alarm;
import com.apps.pochak.alarm.domain.repository.AlarmRepository;
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
import com.apps.pochak.tag.domain.Tag;
import com.apps.pochak.tag.domain.repository.TagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.apps.pochak.global.BaseEntityStatus.ACTIVE;
import static com.apps.pochak.global.BaseEntityStatus.DELETED;
import static com.apps.pochak.global.apiPayload.code.status.ErrorStatus.POST_OWNER_LIKE;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final FollowRepository followRepository;
    private final TagRepository tagRepository;
    private final AlarmRepository alarmRepository;
    private final JwtService jwtService;

    @Transactional
    public void likePost(final Long postId) {
        final Member loginMember = jwtService.getLoginMember();
        final Post post = postRepository.findPostById(postId);

        if (post.isOwner(loginMember))
            throw new GeneralException(POST_OWNER_LIKE);

        final Optional<LikeEntity> optionalLike = likeRepository.findByLikeMemberAndLikedPost(loginMember, post);
        if (optionalLike.isPresent()) {
            final LikeEntity postLike = optionalLike.get();
            toggleLikeStatus(postLike);
        } else {
            saveNewLikeEntity(
                    loginMember,
                    post
            );
        }
    }

    private void toggleLikeStatus(LikeEntity like) {
        if (like.getStatus().equals(ACTIVE)) {
            like.setStatus(DELETED);
            deleteAlarm(like);
        }
        else {
            like.setStatus(ACTIVE);
            sendLikeAlarm(like);
        }
    }

    private void saveNewLikeEntity(
            final Member loginMember,
            final Post post
    ) {
        final LikeEntity like = LikeEntity.builder()
                .likeMember(loginMember)
                .likedPost(post)
                .build();
        likeRepository.save(like);
        sendLikeAlarm(like);
    }

    private void sendLikeAlarm(final LikeEntity like) {
        final Alarm likeAlarm = Alarm.getLikeAlarm(like, like.getLikedPost().getOwner());
        alarmRepository.save(likeAlarm);

        final List<Tag> tagList = tagRepository.findTagByPost(like.getLikedPost());
        final List<Alarm> alarmList = tagList.stream().map(
                tag -> Alarm.getLikeAlarm(like, tag.getMember())
        ).collect(Collectors.toList());
        alarmRepository.saveAll(alarmList);
    }

    private void deleteAlarm(LikeEntity like) {
        final List<Alarm> alarmList = alarmRepository.findAlarmByLike(like);
        alarmRepository.deleteAll(alarmList);
    }

    @Transactional
    public LikeElements getMemberLikedPost(final Long postId) {
        final Member loginMember = jwtService.getLoginMember();
        final Post likedPost = postRepository.findPostById(postId);
        List<LikeEntity> likes = likeRepository.findByLikedPost(likedPost);

        final List<LikeElement> likeElements = likes.stream().map(
                like -> new LikeElement(
                        like.getLikeMember(),
                        followRepository.existsBySenderAndReceiver(loginMember, like.getLikeMember())
                )).toList();
        return new LikeElements(likeElements);
    }
}
