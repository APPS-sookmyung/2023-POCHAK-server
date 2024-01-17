package com.apps.pochak.member.service;

import com.apps.pochak.follow.domain.repository.FollowRepository;
import com.apps.pochak.global.apiPayload.exception.GeneralException;
import com.apps.pochak.login.jwt.JwtHeaderUtil;
import com.apps.pochak.login.jwt.JwtService;
import com.apps.pochak.member.domain.Member;
import com.apps.pochak.member.domain.repository.MemberRepository;
import com.apps.pochak.member.dto.response.ProfileResponse;
import com.apps.pochak.post.domain.Post;
import com.apps.pochak.post.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.apps.pochak.global.apiPayload.code.status.ErrorStatus.INVALID_MEMBER_HANDLE;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;
    private final PostRepository postRepository;

    private final JwtService jwtService;

    public ProfileResponse getProfileDetail(final String handle,
                                            final Pageable pageable
    ) {
        final String loginMemberHandle = getLoginMemberHandle();
        final Member loginMember = getLoginMember();
        final Member member = findMemberByHandle(handle);
        final long followerCount = followRepository.countFollowByReceiver(member);
        final long followingCount = followRepository.countFollowBySender(member);
        final Page<Post> taggedPost = postRepository.findTaggedPost(member, pageable);
        final Boolean isFollow = (handle.equals(loginMemberHandle)) ?
                null : followRepository.existsBySenderAndReceiver(loginMember, member);

        return ProfileResponse.of()
                .member(member)
                .postPage(taggedPost)
                .followerCount(followerCount)
                .followingCount(followingCount)
                .isFollow(isFollow)
                .build();
    }

    private Member getLoginMember() {
        final String loginMemberHandle = getLoginMemberHandle();
        return findMemberByHandle(loginMemberHandle);
    }

    private Member findMemberByHandle(final String handle) {
        return memberRepository.findMemberByHandle(handle)
                .orElseThrow(() -> new GeneralException(INVALID_MEMBER_HANDLE));
    }

    private String getLoginMemberHandle() {
        String accessToken = JwtHeaderUtil.getAccessToken();
        return jwtService.getHandle(accessToken);
    }
}
