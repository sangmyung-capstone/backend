package capstone.bapool.utils;

import capstone.bapool.config.error.BaseException;
import capstone.bapool.user.dto.SocialAccessToken;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static capstone.bapool.config.error.StatusEnum.SOCIAL_LOGIN_FAILURE;

@Slf4j
@Component
@RequiredArgsConstructor
public class SocialUtils {
    private static final String naverApiURL = "https://openapi.naver.com/v1/nid/me";
    private static final String kakaoApiURL = "https://kapi.kakao.com/v2/user/me";

    public Map<String, String> makeUserInfoByKakao(String socialAccessToken) throws IOException {
        HttpURLConnection con = connectKakaoResourceServer(socialAccessToken);
        validateSocialAccessToken(con);

        String result = findSocialLoginUsersInfo(con);

        Map<String, String> response = findResponseFromKakako(result);
        return response;
    }

    private HttpURLConnection connectKakaoResourceServer(String socialAccessToken ) throws IOException {
        URL url = new URL(kakaoApiURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + socialAccessToken);
        return conn;
    }

    public Map<String, String> makeUserInfoByNaver(String socialAccessToken) throws IOException {
        HttpURLConnection con = connectNaverResourceServer(socialAccessToken);
        validateSocialAccessToken(con);

        String result = findSocialLoginUsersInfo(con);

        Map<String, String> response = findResponseFromNaver(result);
        return response;
    }

    private HttpURLConnection connectNaverResourceServer(String socialAccessToken) throws IOException {
        URL url = new URL(naverApiURL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "Bearer " + socialAccessToken);
        return con;
    }

    private void validateSocialAccessToken(HttpURLConnection con) throws IOException, BaseException {
        if (con.getResponseCode() != 200) {
            throw new BaseException(SOCIAL_LOGIN_FAILURE);
        }
    }

    private String findSocialLoginUsersInfo(HttpURLConnection con) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        String line = "";
        String result = "";
        while ((line = br.readLine()) != null) {
            result += line;
        }
        return result;
    }

    private Map<String, String> findResponseFromKakako(String result) throws BaseException {
        Gson gson = new Gson();
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap = gson.fromJson(result, jsonMap.getClass());

        Map<String, String> properties = (Map<String, String>) jsonMap.get("properties");
        Map<String, String> kakao_account = (Map<String, String>) jsonMap.get("kakao_account");
        if (!(kakao_account.get("has_email") != "true")) {
            throw new BaseException(SOCIAL_LOGIN_FAILURE);
        }
        properties.put("email", kakao_account.get("email"));
        if ((kakao_account.get("has_birthday") == "true")) {
            properties.put("birthday", kakao_account.get("birthday"));
        }
        return properties;
    }

    private Map<String, String> findResponseFromNaver(String result) throws BaseException {
        Gson gson = new Gson();
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap = gson.fromJson(result, jsonMap.getClass());

        if (!jsonMap.get("resultcode").equals("00")) {
            throw new BaseException(SOCIAL_LOGIN_FAILURE);
        }
        return (Map<String, String>) jsonMap.get("response");
    }
}
