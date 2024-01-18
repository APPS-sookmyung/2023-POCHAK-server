package com.apps.pochak.follow.service;

import com.apps.pochak.follow.domain.Follow;
import com.apps.pochak.follow.domain.repository.FollowRepository;
import com.apps.pochak.global.BaseEntityStatus;
import com.apps.pochak.global.apiPayload.ApiResponse;
import com.apps.pochak.login.jwt.JwtService;
import com.apps.pochak.member.domain.Member;
import com.apps.pochak.member.domain.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.apps.pochak.global.BaseEntityStatus.DELETED;
import static com.apps.pochak.global.apiPayload.code.status.SuccessStatus.FOLLOW_SUCCESS;
import static com.apps.pochak.global.apiPayload.code.status.SuccessStatus.UNFOLLOW_SUCCESS;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;
    private final JwtService jwtService;

    @Transactional
    public ApiResponse<?> follow(final String handle) {
        final Member loginMember = jwtService.getLoginMember();
        final Member member = memberRepository.findByHandle(handle);
        final Optional<Follow> follow = followRepository.findFollowBySenderAndReceiver(loginMember, member);
        if (follow.isPresent()) {
            final BaseEntityStatus currentStatus = follow.get().toggleCurrentStatus();
            if (currentStatus.equals(DELETED)) {
                return ApiResponse.of(UNFOLLOW_SUCCESS, null);
            }
        } else {
            createAndSaveFollow(loginMember, member);
        }
        return ApiResponse.of(FOLLOW_SUCCESS, null);
    }

    private void createAndSaveFollow(final Member sender,
                                               final Member receiver) {
        final Follow newFollow = Follow.of()
                .sender(sender)
                .receiver(receiver)
                .build();
        followRepository.save(newFollow);
    }
}
