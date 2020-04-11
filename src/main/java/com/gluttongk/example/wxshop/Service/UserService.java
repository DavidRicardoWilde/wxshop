package com.gluttongk.example.wxshop.Service;

import com.gluttongk.example.wxshop.DAO.UserDAO;
import com.gluttongk.example.wxshop.generate.User;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User createUserIfNotExist(String tel) {
        User user = new User();
        user.setTel(tel);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        try {
            userDAO.insertUser(user);
        } catch (PersistenceException exception) {
            return userDAO.getUserByTel(tel);
        }

        return user;
    }

    public User getUserByTel(String tel) {
        return userDAO.getUserByTel(tel);
    }

//    public Optional<User> getUserByTel(String tel) {
////        return Optional.ofNullable(userDAO.getUserByTel(tel));
////    } Java8 函数编程style
}
