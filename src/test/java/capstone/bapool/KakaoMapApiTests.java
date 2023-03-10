package capstone.bapool;

import com.google.gson.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


public class KakaoMapApiTests {

    @Test
    public void searchByKeyword(){

        System.out.println("테스트 실행");
        try{
//            String base = "https://dapi.kakao.com/v2/local/search/keyword.json?query=떡볶이";
            URL url = new URL("https://dapi.kakao.com/v2/local/search/keyword.json?query=noodle");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            System.out.println("1");

            //POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("GET");
//            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Authorization", "KakaoAK c7c0edb9f622d6932c33a50bd0a2aa07");
            conn.setDoOutput(true);

            System.out.println("2");

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
//            log.info("responseCode : {}" , responseCode);
//            log.info(conn.getResponseMessage());

            // 200 아닐경우 예외처리 필요

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
//            log.info("result ={} " + result);
            System.out.println("result = " + result);

            //Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            // 응답 바디에서 토큰값 읽어오기
//            String accessToken = element.getAsJsonObject().get("access_token").getAsString();
//            String refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();

            br.close();

            return;
        }catch (IOException e) {
            e.printStackTrace();
//            throw new BaseException(KAKAO_GET_TOKEN_FAIL);
        }
    }


}
