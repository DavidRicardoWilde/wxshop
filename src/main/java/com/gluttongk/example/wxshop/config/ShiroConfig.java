package com.gluttongk.example.wxshop.config;

import com.gluttongk.example.wxshop.service.ShiroRealm;
import com.gluttongk.example.wxshop.service.UserContext;
import com.gluttongk.example.wxshop.service.UserService;
import com.gluttongk.example.wxshop.service.VerificationCodeCheckService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig implements WebMvcConfigurer {
    @Autowired
    UserService userService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                Object tel = SecurityUtils.getSubject().getPrincipal();
                if (tel != null) {
                    userService.getUserByTel(tel.toString()).ifPresent(UserContext::setCurrentUser);
                }
                return true;
            }

            @Override
            public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
                UserContext.clearCurrentUser();
            }
        });
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager, ShiroLoginFilter shiroLoginFilter) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        Map<String, String> pattern = new HashMap<>();
        pattern.put("/api/v1/code", "anon");
        pattern.put("/api/v1/login", "anon");
        pattern.put("/api/v1/status", "anon");
        pattern.put("/api/v1/logout", "anon");
        pattern.put("/**", "authc");

        Map<String, Filter> filtersMap = new LinkedHashMap<>();
        filtersMap.put("shiroLoginFilter", shiroLoginFilter);
        shiroFilterFactoryBean.setFilters(filtersMap);

        shiroFilterFactoryBean.setFilterChainDefinitionMap(pattern);
        return shiroFilterFactoryBean;
    }

    @Bean
    public SecurityManager securityManager(ShiroRealm shiroRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        securityManager.setRealm(shiroRealm);
        securityManager.setCacheManager(new MemoryConstrainedCacheManager());
        securityManager.setSessionManager(new DefaultWebSessionManager());
        SecurityUtils.setSecurityManager(securityManager);
        return securityManager;
    }

    @Bean
    public ShiroRealm myShiroRealm(VerificationCodeCheckService verificationCodeCheckService) {
        return new ShiroRealm(verificationCodeCheckService);
    }
}
