package com.apps.pochak.user.controller;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.apps.pochak.common.BaseException;
import com.apps.pochak.common.BaseResponse;
import com.apps.pochak.login.jwt.JwtHeaderUtil;
import com.apps.pochak.login.jwt.JwtService;
import com.apps.pochak.user.dto.*;
import com.apps.pochak.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.apps.pochak.common.BaseResponseStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/profile")
public class UserProfileController {
    private final UserService userService;
    private final JwtService jwtService;

    /**
     * 기본 user profile 가져오기
     */
    @GetMapping("/{handle}")
    public BaseResponse<UserProfileResDto> getUserProfile(@PathVariable("handle") String userHandle,
                                                          @RequestParam(value = "PartitionKey", required = false) String partitionKey,
                                                          @RequestParam(value = "SortKey", required = false) String sortKey) {
        // TODO: RequestBody 부분 RequestParam으로 변경해주기
        try {
            // login
            String accessToken = JwtHeaderUtil.getAccessToken();
            String loginUserHandle = jwtService.getHandle(accessToken);

            Map<String, AttributeValue> exclusiveStartKey;
            if (partitionKey == null) {
                exclusiveStartKey = null;
            } else {
                exclusiveStartKey = new HashMap<>();
                exclusiveStartKey.put("PartitionKey", new AttributeValue().withS(partitionKey));
                exclusiveStartKey.put("SortKey", new AttributeValue().withS(sortKey));
            }

            UserProfileResDto resDto = userService.getUserProfile(userHandle, loginUserHandle, exclusiveStartKey);
            if (userHandle.equals(loginUserHandle)) {
                return new BaseResponse<>(resDto, NULL_FOLLOW_STATUS);
            }
            if (resDto.getExclusiveStartKey() == null) {
                return new BaseResponse<>(resDto, LAST_TAG_PAGE);
            }
            return new BaseResponse<>(resDto);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PatchMapping("/{handle}")
    public BaseResponse<UserUpdateResDto> updateUserProfile(@PathVariable("handle") String updatedUserHandle,
                                                            @RequestBody UserUpdateRequestDto requestDto) {
        try {
            // login
            String accessToken = JwtHeaderUtil.getAccessToken();
            String loginUserHandle = jwtService.getHandle(accessToken);

            if (!updatedUserHandle.equals(loginUserHandle)) throw new BaseException(INVALID_UPDATE_REQUEST);
            return new BaseResponse<>(userService.updateUserProfile(updatedUserHandle, requestDto));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/{handle}/pochak")
    public BaseResponse<UserPublishResDto> getUploadPosts(@PathVariable("handle") String userHandle,
                                                          @RequestBody Map<String, AttributeValue> exclusiveStartKey) {
        // TODO: RequestBody 부분 RequestParam으로 변경해주기
        try {
            // login
            String accessToken = JwtHeaderUtil.getAccessToken();
            String loginUserHandle = jwtService.getHandle(accessToken);

            UserPublishResDto resDto = userService.getUploadPosts(userHandle, loginUserHandle, exclusiveStartKey);
            if (resDto.getUploadPosts().isEmpty()) {
                return new BaseResponse<>(resDto, NULL_UPLOAD_POST);
            }
            if (resDto.getExclusiveStartKey() == null) {
                return new BaseResponse<>(resDto, LAST_PUBLISH_PAGE);
            }
            return new BaseResponse<>(resDto);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PostMapping("/{handle}")
    public BaseResponse<String> followUser(@PathVariable("handle") String userHandle) {
        try {
            // login
            String accessToken = JwtHeaderUtil.getAccessToken();
            String loginUserHandle = jwtService.getHandle(accessToken);

            if (userHandle.equals(loginUserHandle)) {
                return new BaseResponse<>(FOLLOW_ONESELF);
            }
            return new BaseResponse<>(userService.followUser(userHandle, loginUserHandle));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @DeleteMapping("/{handle}")
    public BaseResponse<String> deleteFollower(@PathVariable("handle") String userHandle) {
        try {
            // login
            String accessToken = JwtHeaderUtil.getAccessToken();
            String loginUserHandle = jwtService.getHandle(accessToken);

            if (userHandle.equals(loginUserHandle)) {
                return new BaseResponse<>(INVALID_FOLLOWER);
            }
            return new BaseResponse<>(userService.deleteFollower(userHandle, loginUserHandle));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/{handle}/follower")
    public BaseResponse<UserFollowersResDto> findFollowers(@PathVariable("handle") String userHandle) {
        try {
            return new BaseResponse<>(userService.getUserFollowers(userHandle));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/{handle}/following")
    public BaseResponse<UserFollowingsResDto> findFollowings(@PathVariable("handle") String userHandle) {
        try {
            return new BaseResponse<>(userService.getUserFollowings(userHandle));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
