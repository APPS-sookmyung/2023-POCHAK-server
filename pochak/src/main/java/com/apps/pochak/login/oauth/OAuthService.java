package com.apps.pochak.login.oauth;

import com.apps.pochak.common.AwsS3Service;
import com.apps.pochak.common.BaseException;
import com.apps.pochak.common.BaseResponse;
import com.apps.pochak.login.dto.OAuthResponse;
import com.apps.pochak.login.dto.UserInfoRequest;
import com.apps.pochak.login.jwt.JwtService;
import com.apps.pochak.user.domain.SocialType;
import com.apps.pochak.user.domain.User;
import com.apps.pochak.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

import static com.apps.pochak.common.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AppleOAuthService appleOAuthService;
    private final AwsS3Service awsS3Service;

    public OAuthResponse signup(UserInfoRequest userInfoRequest) throws BaseException, IOException {
        Optional<User> findUser = userRepository.findUserWithSocialId(userInfoRequest.getSocialId());

        if (findUser.isPresent()) {
            throw new BaseException(EXIST_USER);
        }

        String profileImageUrl = awsS3Service.upload(userInfoRequest.getProfileImage(), "profile");

        String refreshToken = jwtService.createRefreshToken();
        String accessToken = jwtService.createAccessToken(userInfoRequest.getHandle());

        User user = User.signupUser()
                .name(userInfoRequest.getName())
                .email(userInfoRequest.getEmail())
                .handle(userInfoRequest.getHandle())
                .message(userInfoRequest.getMessage())
                .socialId(userInfoRequest.getSocialId())
                .profileImage(profileImageUrl)
                .socialType(SocialType.of(userInfoRequest.getSocialType()))
                .socialRefreshToken(userInfoRequest.getSocialRefreshToken())
                .build();

        user.updateRefreshToken(refreshToken);
        userRepository.saveUser(user);
        return OAuthResponse.builder()
                .isNewMember(false)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public BaseResponse logout(String handle) throws BaseException {
        User user = userRepository.findUserByUserHandle(handle);
        user.updateRefreshToken(null);
        userRepository.saveUser(user);
        return new BaseResponse(SUCCESS);
    }

    public BaseResponse signout(String handle) throws BaseException {
        User user = userRepository.findUserByUserHandle(handle);
        if (user.getSocialType().equals(SocialType.APPLE)) {
            appleOAuthService.revoke(user.getRefreshToken());
        }
        userRepository.deleteUser(user);
        return new BaseResponse(SUCCESS);
    }
}
