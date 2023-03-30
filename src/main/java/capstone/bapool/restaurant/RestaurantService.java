package capstone.bapool.restaurant;

import capstone.bapool.restaurant.dto.GetRestaurantInfoRes;
import capstone.bapool.restaurant.dto.RestaurantInfo;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Service
public class RestaurantService {
    public void searchByKeyword(){

        System.out.println("테스트 실행");
        try{
            String food = "떡볶이";
            String encodeFood = URLEncoder.encode(food, "UTF-8");
            URL url = new URL("https://dapi.kakao.com/v2/local/search/keyword.json?query=" + encodeFood);
            System.out.println("base = " + food);
            System.out.println("encodeUrl = " + encodeFood);
            System.out.println("url = " + url.getPath());
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
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
//            new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(),"UTF-8"))
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

            JsonArray documents = element.getAsJsonObject().get("documents").getAsJsonArray();
            System.out.println("documents.get(0) = " + documents.get(0).toString());
            JsonObject restaurant = documents.get(0).getAsJsonObject();
            String placeName = restaurant.get("place_name").toString();
            System.out.println("placeName = " + placeName);

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

    public GetRestaurantInfoRes getRestaurantInfo(){
        RestaurantInfo restaurants  = new RestaurantInfo(1l,"a","b","c","d",1,1d,1d);
        GetRestaurantInfoRes abc = new GetRestaurantInfoRes();
        abc.addrestaurant(restaurants);
        RestaurantInfo restaurantss  = new RestaurantInfo(5l,"a","fdsad","c","d",1,1d,1d);
        abc.addrestaurant(restaurantss);

        return abc;
    }
}
