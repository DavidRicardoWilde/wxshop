package com.gluttongk.example.wxshop.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kevinsawicki.http.HttpRequest;
import com.gluttongk.example.wxshop.WxshopApplication;
import com.gluttongk.example.wxshop.entity.LoginResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;

import static com.gluttongk.example.wxshop.Service.TelVerificationServiceTest.VALID_PARAMETER;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_OK;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = WxshopApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application.yml")
public class AuthIntegrationTest {
    @Autowired
    Environment environment;

    private ObjectMapper objectMapper = new ObjectMapper();

    private static class HttpResponse {
        int code;
        String body;
        Map<String, List<String>> headers;

        HttpResponse(int code, String body, Map<String, List<String>> headers) {
            this.code = code;
            this.body = body;
            this.headers = headers;
        }
    }

//    private HttpResponse doHttpRequest(String api_uri, boolean isGet, Object requestBody,
//                                       String cookie) throws JsonProcessingException {
//        HttpRequest request = isGet ? HttpRequest.get(getUrl(api_uri)) : HttpRequest.post(getUrl(api_uri));
//        if (cookie != null) {
//            request.header("Cookie", cookie);
//        }
//        request.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE);
//        if (requestBody != null) {
//            request.send(objectMapper.writeValueAsString(requestBody));
//        }
//
//        return new HttpResponse(request.code(), request.body(), request.headers());
//    }

    @Test
    public void LoginLogoutTest() throws JsonProcessingException {
        // 集成测试
        // step 1: 最开始未登录状态
        String statusResponse = HttpRequest.get(getUrl("/api/status"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body();
        LoginResponse response = objectMapper.readValue(statusResponse, LoginResponse.class);
//        Assertions.assertFalse((Boolean) response.get("login"));
        Assertions.assertFalse(response.isLogin());

        //step 2: 发送验证码
        int responseCode = HttpRequest.post(getUrl("/api/code"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .send(objectMapper.writeValueAsString(TelVerificationServiceTest.VALID_PARAMETER_CODE))
                .code();
        Assertions.assertEquals(HTTP_OK, responseCode);
        // step3: 带着验证码登录
        Map<String, List<String>> responseHeaders = HttpRequest.post(getUrl("/api/login"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .send(objectMapper.writeValueAsString(TelVerificationServiceTest.VALID_PARAMETER_CODE))
                .headers();
        List<String> setCookie = responseHeaders.get("Set-Cookie");
        Assertions.assertNotNull(setCookie);
        // 是step3.5 带着cookie 访问 status api 应该处于登录状态
        String sessionId = getSessionIdFromSetCookie(setCookie.stream().filter(cookie -> cookie.contains("JSESSIONID"))
                .findFirst()
                .get());

        statusResponse = HttpRequest.get(getUrl("/api/status"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Cookie", sessionId)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body();

        response = objectMapper.readValue(statusResponse, LoginResponse.class);
        Assertions.assertTrue(response.isLogin());
        Assertions.assertEquals(VALID_PARAMETER.getTel(), response.getUser().getTel());

        //step4: 调用 logout
        System.out.println("step4");
        HttpRequest.post(getUrl("/api/logout"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Cookie", sessionId)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .headers();

        //step4.5: 再次带着Cookie 访问 status 恢复未登录状态
        System.out.println("step4.5");
        statusResponse = HttpRequest.get(getUrl("/api/status"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Cookie", sessionId)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body();

        System.out.println("issues after step4.5");
        response = objectMapper.readValue(statusResponse, LoginResponse.class);
        Assertions.assertFalse(response.isLogin());
    }

    private String getSessionIdFromSetCookie(String setCookie) {
        //Example: JSESSIONID=6f36b44a-274a-41d5-863b-39c78dd5901f; Path=/; HttpOnly; SameSite=lax
        // --> JSESSIONID=6f36b44a-274a-41d5-863b-39c78dd5901f
        int semiColonIndex = setCookie.indexOf(";");
//        int equalIndex = setCookie.indexOf("=");

//        return setCookie.substring(equalIndex+1, semiColonIndex);
        return setCookie.substring(0, semiColonIndex);
    }

    @Test
    public void returnHttpOKWhenParameterIsCorrect() throws JsonProcessingException {
        int responseCode = HttpRequest.post(getUrl("/api/code"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .send(objectMapper.writeValueAsString(VALID_PARAMETER))
                .code();

        Assertions.assertEquals(HTTP_OK, responseCode);
    }

    @Test
    public void returnBadRequestWhenParameterIsCorrect() throws JsonProcessingException {
        int responseCode = HttpRequest.post(getUrl("/api/code"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .send(objectMapper.writeValueAsString(TelVerificationServiceTest.EMPTY_TEL))
                .code();

        Assertions.assertEquals(HTTP_BAD_REQUEST, responseCode);
    }

    private String getUrl(String apiName) {
        // 获取集成测试的端口号
        return "http://localhost:" + environment.getProperty("local.server.port") + apiName;
    }

}
