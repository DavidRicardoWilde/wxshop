package com.gluttongk.example.wxshop.Service;

import com.gluttongk.example.wxshop.Controller.AuthController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TelVerificationServiceTest {
    public static AuthController.TelAndCode VALID_PARAMETER =
            new AuthController.TelAndCode("13812345678", null);
    public static AuthController.TelAndCode EMPTY_TEL =
            new AuthController.TelAndCode(null, null);
    @Test
    public void returnTrueIfValid() {
        Assertions.assertTrue(new TelVerificationService().verifyTelParameter(VALID_PARAMETER));
    }

    @Test
    public void returnFalseIfNotTel() {
        Assertions.assertFalse(new TelVerificationService().verifyTelParameter(EMPTY_TEL));
        Assertions.assertFalse(new TelVerificationService().verifyTelParameter(null));
    }
}
