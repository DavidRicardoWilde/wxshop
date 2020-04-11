package com.gluttongk.example.wxshop.entity;

import com.gluttongk.example.wxshop.generate.User;

public class LoginResponse {
    private boolean login;
    private User user;

    public static LoginResponse notLogin() {
        return new LoginResponse(false, null);
    }

    public LoginResponse() {
    }

    public static LoginResponse login(User user) {
        return new LoginResponse(true, user);
    }

    private LoginResponse(boolean login, User user) {
        this.login = login;
        this.user = user;
    }

    public boolean isLogin() {
        return login;
    }

    public User getUser() {
        return user;
    }
}
