package capstone.bapool.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class RequestsService {

    public String getImgURl(Long restaurantId){
        System.out.println("크롤링 테스트");

        String resultUrl = "https://place.map.kakao.com/main/v/" + restaurantId;

        try{
            URL url = new URL(resultUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            
            // 결과 코드가 200이라면 성공
            // 200 아닐경우 예외처리 필요
            System.out.println("conn.getResponseCode() = " + conn.getResponseCode());
            System.out.println("conn.getResponseMessage() = " + conn.getResponseMessage());

            // 요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
//            System.out.println("result = " + result);

            //Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonElement element = JsonParser.parseString(result);

            // 응답 바디에서 필요한 값 뽑아내기
            JsonElement basicInfo = element.getAsJsonObject().get("basicInfo").getAsJsonObject().get("mainphotourl");

            System.out.println("basicInfo = " + basicInfo);

            return basicInfo.getAsString();
        }catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
