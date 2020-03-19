package com.gluttongk.example.wxshop.Service;

import com.gluttongk.example.wxshop.Service.ServiceInterface.SmsCodeServiceInterface;
import org.springframework.stereotype.Service;

@Service
public class MockSmsCodeService implements SmsCodeServiceInterface {
    @Override
    public String sendSmsCode(String tel) {
        return "000000";
    }
}
