package capstone.bapool.utils;

import capstone.bapool.utils.dto.KakaoRestaurant;
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
import java.util.ArrayList;
import java.util.List;

@Service
public class KakaoLocalApiService {

    public List<KakaoRestaurant> searchByCategory(String rect){
        String baseUrl = "https://dapi.kakao.com/v2/local/search/category.json?category_group_code=FD6";
        String resultUrl;
        List<KakaoRestaurant> kakaoRestaurantList = new ArrayList<>();

        for(int page=1; page<=3; page++){
            try{
                resultUrl = baseUrl + "&rect=" + rect + "&page=" + page;
                URL url = new URL(resultUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", "KakaoAK c7c0edb9f622d6932c33a50bd0a2aa07");

                conn.connect();

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
                System.out.println("result = " + result);

                //Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
                JsonElement element = JsonParser.parseString(result);

                // 응답 바디에서 필요한 값 뽑아내기
                JsonArray documents = element.getAsJsonObject().get("documents").getAsJsonArray();

                for(int i=0; i<documents.size(); i++){
                    JsonObject restaurant = documents.get(i).getAsJsonObject();
                    KakaoRestaurant kakaoRestaurant = KakaoRestaurant.builder()
                            .id(restaurant.get("id").getAsLong())
                            .name(restaurant.get("place_name").getAsString())
                            .address(restaurant.get("road_address_name").getAsString())
                            .category(restaurant.get("category_name").getAsString())
                            .longitude(restaurant.get("x").getAsDouble())
                            .latitude(restaurant.get("y").getAsDouble())
                            .build();
                    kakaoRestaurantList.add(kakaoRestaurant);
                }

                br.close();

//                for(RestaurantInfo info : kakaoRestaurants){
//                    System.out.println(info);
//                    System.out.println();
//                }

                // 이게 마지막 페이지이면 끝내기
                JsonObject meta = element.getAsJsonObject().get("meta").getAsJsonObject();
                boolean isEnd = meta.getAsJsonObject().get("is_end").getAsBoolean();
                if(isEnd){
                    break;
                }

                System.out.println("isEnd = " + isEnd);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }

        return kakaoRestaurantList;
    }
}
