package com.gluttongk.example.wxshop.Service;

import com.gluttongk.example.wxshop.generate.User;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserLoginInterceptor implements HandlerInterceptor {
    private UserService userService;

    public UserLoginInterceptor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//            System.out.println("pre");
        System.out.println(SecurityUtils.getSubject().getPrincipal());
        Object tel = SecurityUtils.getSubject().getPrincipal();
        if (tel != null) {
            //说明已经登陆了
//                userService.getUserByTel(tel.toString()).ifPresent(UserContext::setCurrentUser); Java 8方式
            User user =  userService.getUserByTel(tel.toString());
            UserContext.setCurrentUser(user);
        }

        return true; // 返回true才会进去，返沪false就会被拦截并停止(不会进行下一步)
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//            System.out.println("Post");
        //非常重要，因为线程会被费用
        //如果 在线程1中保存了用户A的信息， 且没有清理的话
        //下次线程1再用来处理别的请求的时候， 就会出现"串号"的情况
        UserContext.setCurrentUser(null);
    }
}
