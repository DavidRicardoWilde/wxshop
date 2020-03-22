package com.gluttongk.example.wxshop.Service;

import com.gluttongk.example.wxshop.generate.User;

public class UserContext {
    private static ThreadLocal<User> currentUser = new ThreadLocal<>();

    // Java中 通常加了static获得的返回是一样的
    //但是，TreadLocal是个特殊的存在，它能获取不同线程里数据 它的返回结果是这些数据 返回的结果是不一样的
    public static User getCurrentUser() {
        return currentUser.get();
    }

    public static void setCurrentUser(User user) {
        currentUser.set(user);
    }
}
