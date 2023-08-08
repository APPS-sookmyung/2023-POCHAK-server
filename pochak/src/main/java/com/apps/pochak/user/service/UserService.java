package com.apps.pochak.user.service;

import com.apps.pochak.common.BaseException;
import com.apps.pochak.user.domain.User;
import com.apps.pochak.user.dto.UserFollowersResDto;
import com.apps.pochak.user.dto.UserFollowingsResDto;
import com.apps.pochak.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.apps.pochak.common.BaseResponseStatus.DATABASE_ERROR;
import static com.apps.pochak.common.BaseResponseStatus.NULL_USER_HANDLE;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    // test
    @Transactional
    public User saveUser(User user) {
        return userRepository.saveUser(user);
    }

    public UserFollowersResDto getUserFollowers(String handle) throws BaseException {
        try {
            if (handle.isBlank()) {
                throw new BaseException(NULL_USER_HANDLE);
            }
            User userByUserPK = userRepository.findUserByUserHandle(handle);
            List<User> followers = userByUserPK.getFollowerList().stream().map(
                            userId -> {
                                try {
                                    return userRepository.findUserByUserId(userId);
                                } catch (Exception e) {
                                    throw new RuntimeException("해당 User의 Follower List에 더미 userID 데이터가 있는지 확인하세요");
                                }
                            })
                    .collect(Collectors.toList());
            return new UserFollowersResDto(followers);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public UserFollowingsResDto getUserFollowings(String userHandle) throws BaseException {
        try {
            if (userHandle.isBlank()) {
                throw new BaseException(NULL_USER_HANDLE);
            }
            List<User> followings = userRepository.findUserByUserHandle(userHandle).getFollowingList().stream().map(
                    userId -> {
                        try {
                            return userRepository.findUserByUserId(userId);
                        } catch (BaseException e) {
                            throw new RuntimeException("해당 User의 Following List에 더미 userID 데이터가 있는지 확인하세요");
                        }
                    }
            ).collect(Collectors.toList());
            return new UserFollowingsResDto(followings);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
