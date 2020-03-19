package com.gluttongk.example.wxshop.Service.ServiceInterface;

public interface SmsCodeServiceInterface {
    /**
     * 向一个指定手机号发验证码，返回正确Code
     * @param tel 目标手机号
     * @return 正确答案
     */
    String sendSmsCode(String tel);
}
