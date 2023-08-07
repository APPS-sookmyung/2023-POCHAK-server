package com.apps.pochak.user.service;

import com.apps.pochak.common.BaseException;
import com.apps.pochak.user.domain.User;
import com.apps.pochak.user.dto.UserFollowersResDto;
import com.apps.pochak.user.dto.UserUpdateRequestDto;
import com.apps.pochak.user.dto.UserUpdateResDto;
import com.apps.pochak.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.apps.pochak.common.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User saveUser(User user) {
        return userRepository.saveUser(user);
    }

    public UserFollowersResDto getUserFollowers(String handle) throws BaseException {
        try {
            User userByUserPK = findUserByUserHandle(handle);
            List<User> users = userByUserPK.getFollowerList().stream().map(
                            userId -> {
                                try {
                                    return userRepository.findUserWithUserId(userId);
                                } catch (Exception e) {
                                    throw new RuntimeException("해당 User의 Follower List에 더미 userID 데이터가 없는지 확인해주세요");
                                }
                            })
                    .collect(Collectors.toList());
            return new UserFollowersResDto(users);
        } catch (BaseException e) {
            throw e;
        }
    }

    public User findUserByUserHandle(String userHandle) throws BaseException {
        try {
            User userByHandle = userRepository.findUserWithUserHandle(userHandle);
            return userByHandle;
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public UserUpdateResDto updateUserProfile(String updatedUserHandle, UserUpdateRequestDto requestDto) throws BaseException {
        try {
            User userWithUserHandle = userRepository.findUserWithUserHandle(updatedUserHandle);

            // profileImage와 한줄 소개는 null 값 가능하게 설정.
            if (requestDto.getName().isBlank()) {
                throw new BaseException(NULL_USER_NAME);
            } else if (requestDto.getHandle().isBlank()) {
                throw new BaseException(NULL_USER_HANDLE);
            }

            userWithUserHandle.updateUser(
                    requestDto.getProfileImgUrl(),
                    requestDto.getName(),
                    requestDto.getHandle(),
                    requestDto.getMessage());
            userRepository.saveUser(userWithUserHandle);

            return UserUpdateResDto.builder()
                    .profileImgUrl(userWithUserHandle.getProfileImage())
                    .name(userWithUserHandle.getName())
                    .handle(userWithUserHandle.getHandle())
                    .message(userWithUserHandle.getMessage())
                    .build();
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public Boolean checkHandleDuplicate(String handle) throws BaseException {
        try {
            userRepository.findUserWithUserHandle(handle);
            return true; // 중복됨
        } catch (BaseException e) {
            if (e.getStatus().equals(INVALID_USER_ID)) {
                return false; // 중복되지 않음
            }
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
