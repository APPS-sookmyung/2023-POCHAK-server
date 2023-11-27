package com.apps.pochak.login.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JwtServiceTest {
    @Autowired
    private JwtService jwtService;

    @Test
    public void createTestUserToken() {
//        String accessToken = jwtService.createAccessToken("handle1");
//        System.out.println(accessToken);
    }
}