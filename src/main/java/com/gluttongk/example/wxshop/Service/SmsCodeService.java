package com.gluttongk.example.wxshop.Service;

import org.springframework.stereotype.Service;

@Service
public class SmsCodeService {
    /***
     * 向目标手机号发送验证码
     * @param tel
     * @return
     */
    String sendSmsCode(String tel) {
        return "000000";
    }
}
