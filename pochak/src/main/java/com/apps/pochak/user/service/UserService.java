package com.apps.pochak.user.service;

import com.apps.pochak.user.domain.User;
import com.apps.pochak.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User findUserByUserPK(String userPK) {
        List<User> userByUserPK = userRepository.findUserByUserPK(userPK);
        System.out.println(userByUserPK);
        if (userByUserPK.isEmpty()) {
            new IllegalArgumentException("해당 유저가 없습니다. userPK: " + userPK);
        }
        return userByUserPK.get(0);
    }

}
