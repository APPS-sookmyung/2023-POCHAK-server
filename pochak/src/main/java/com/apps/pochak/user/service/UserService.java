package com.apps.pochak.user.service;

import com.apps.pochak.common.BaseException;
import com.apps.pochak.user.domain.User;
import com.apps.pochak.user.dto.UserFollowersResDto;
import com.apps.pochak.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.apps.pochak.common.BaseResponseStatus.DATABASE_ERROR;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User saveUser(User user) {
        return userRepository.saveUser(user);
    }

    public UserFollowersResDto getUserFollowers(String userPK) throws BaseException {
        try {
            User userByUserPK = findUserByUserPK(userPK);
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

    public User findUserByUserPK(String userPK) throws BaseException {
        try {
            User userByUserPK = userRepository.findUserWithUserPK(userPK);
            return userByUserPK;
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
