package com.apps.pochak.login.oauth;

import com.apps.pochak.global.apiPayload.exception.GeneralException;
import com.apps.pochak.global.s3.S3Service;
import com.apps.pochak.login.dto.request.MemberInfoRequest;
import com.apps.pochak.login.dto.response.OAuthMemberResponse;
import com.apps.pochak.login.jwt.JwtService;
import com.apps.pochak.member.domain.Member;
import com.apps.pochak.member.domain.SocialType;
import com.apps.pochak.member.domain.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static com.apps.pochak.global.apiPayload.code.status.ErrorStatus.EXIST_USER;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final JwtService jwtService;
    private final MemberRepository memberRepository;
    private final AppleOAuthService appleOAuthService;
    private final S3Service awsS3Service;

    @Transactional
    public OAuthMemberResponse signup(MultipartFile profileImage, MemberInfoRequest memberInfoRequest) throws IOException {
        Optional<Member> findMember  = memberRepository.findMemberBySocialId(memberInfoRequest.getSocialId());

        if (findMember.isPresent()) {
            throw new GeneralException(EXIST_USER);
        }

        String profileImageUrl = awsS3Service.upload(profileImage, "profile");

        String refreshToken = jwtService.createRefreshToken();
        String accessToken = jwtService.createAccessToken(memberInfoRequest.getHandle());

        Member member = Member.signupMember()
                .name(memberInfoRequest.getName())
                .email(memberInfoRequest.getEmail())
                .handle(memberInfoRequest.getHandle())
                .message(memberInfoRequest.getMessage())
                .socialId(memberInfoRequest.getSocialId())
                .profileImage(profileImageUrl)
                .socialType(SocialType.of(memberInfoRequest.getSocialType()))
                .socialRefreshToken(memberInfoRequest.getSocialRefreshToken())
                .build();

        member.updateRefreshToken(refreshToken);
        memberRepository.save(member);
        return OAuthMemberResponse.builder()
                .isNewMember(false)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public void logout(final String handle) {
        final Member member = memberRepository.findByHandle(handle);
        member.updateRefreshToken(null);
        memberRepository.save(member);
    }

    @Transactional
    public void signout(final String handle) {
        final Member member = memberRepository.findByHandle(handle);
        if (member.getSocialType().equals(SocialType.APPLE)) {
            appleOAuthService.revoke(member.getRefreshToken());
        }
        memberRepository.delete(member);
    }
}
