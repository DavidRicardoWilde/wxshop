package com.gluttongk.example.Service;

import com.gluttongk.example.Service.ServiceInterface.SmsCodeServiceInterface;
import org.springframework.stereotype.Service;

@Service
public class MockSmsCodeService implements SmsCodeServiceInterface {
    @Override
    public String sendSmsCode(String tel) {
        return "000000";
    }
}
