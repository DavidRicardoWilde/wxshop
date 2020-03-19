package com.gluttongk.example.wxshop.generate;

import com.gluttongk.example.wxshop.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserService userService;

    @Autowired
    public AuthService(UserService userService) {
        this.userService = userService;
    }

    public void sendVerificationCode(String tel) {
        userService.createUserIfNotExist(tel);
    }
}
