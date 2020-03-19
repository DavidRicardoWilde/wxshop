package com.gluttongk.example.wxshop.Service;

import com.gluttongk.example.wxshop.Controller.AuthController;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class TelVerificationService {
    private static Pattern TEL_PATTERN = Pattern.compile("1\\d{10}");

    /**
     * 验证输入的参数是否合法
     *
     * @param param 必要参数
     * @return bool
     */
    public boolean verifyTelParameter(AuthController.TelAndCode param) {
        if (param == null) {
            return false;
        } else if (param.getTel() == null) {
            return false;
        } else {
//            return param.getTel().matches("1\\d{10}");
            return TEL_PATTERN.matcher(param.getTel()).find();
        }
    }
}
