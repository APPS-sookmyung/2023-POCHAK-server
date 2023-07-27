package com.apps.pochak.user.service;

import com.apps.pochak.common.BaseException;
import com.apps.pochak.user.domain.User;
import com.apps.pochak.user.dto.UserFollowersResDto;
import com.apps.pochak.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        } catch (BaseException e) {
            throw e;
        }
        return null;
    }

    public User findUserByUserPK(String userPK) throws BaseException {
        try {
            User userByUserPK = userRepository.getUserWithUserPK(userPK);
            return userByUserPK;
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            System.out.print(e);
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
