package com.apps.pochak.follow.service;

import com.apps.pochak.follow.domain.Follow;
import com.apps.pochak.follow.domain.repository.FollowRepository;
import com.apps.pochak.global.apiPayload.ApiResponse;
import com.apps.pochak.global.apiPayload.exception.GeneralException;
import com.apps.pochak.login.jwt.JwtService;
import com.apps.pochak.member.domain.Member;
import com.apps.pochak.member.domain.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.apps.pochak.global.apiPayload.code.status.ErrorStatus.NOT_FOLLOW;
import static com.apps.pochak.global.apiPayload.code.status.ErrorStatus._UNAUTHORIZED;
import static com.apps.pochak.global.apiPayload.code.status.SuccessStatus.*;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;
    private final JwtService jwtService;

    @Transactional
    public ApiResponse<Void> follow(final String handle) {
        final Member loginMember = jwtService.getLoginMember();
        final Member member = memberRepository.findByHandle(handle);
        final Optional<Follow> followOptional = followRepository.findFollowBySenderAndReceiver(loginMember, member);
        if (followOptional.isPresent()) {
            final Follow follow = followOptional.get();
            follow.toggleCurrentStatus();
            if (!follow.isFollow()) {
                return ApiResponse.of(SUCCESS_UNFOLLOW, null);
            }
        } else {
            createAndSaveFollow(loginMember, member);
        }
        return ApiResponse.of(SUCCESS_FOLLOW, null);
    }

    @Transactional
    public ApiResponse<Void> deleteFollower(final String handle,
                                            final String followerHandle
    ) {
        // TODO: 권한 확인하는 부분 애노테이션으로 리팩토링하기
        final Member loginMember = jwtService.getLoginMember();
        if (!loginMember.getHandle().equals(handle)) {
            throw new GeneralException(_UNAUTHORIZED);
        }

        final Member follower = memberRepository.findByHandle(followerHandle);
        final Follow follow = followRepository.findBySenderAndReceiver(follower, loginMember);
        if (!follow.isFollow()) {
            return ApiResponse.of(NOT_FOLLOW, null);
        }
        follow.toggleCurrentStatus();
        return ApiResponse.of(SUCCESS_DELETE_FOLLOWER, null);
    }

    private void createAndSaveFollow(final Member sender,
                                     final Member receiver
    ) {
        final Follow newFollow = Follow.of()
                .sender(sender)
                .receiver(receiver)
                .build();
        followRepository.save(newFollow);
    }
}
