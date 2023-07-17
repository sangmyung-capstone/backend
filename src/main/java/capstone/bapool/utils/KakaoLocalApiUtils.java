package capstone.bapool.utils;

import capstone.bapool.utils.dto.KakaoRestaurant;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class KakaoLocalApiUtils {

    // rect로 식당 조회
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
                            .phone(restaurant.get("phone").getAsString())
                            .siteUrl(restaurant.get("place_url").getAsString())
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

    /**
     * 식당 검색
     * @param restaurantId 식당 id
     * @param x 식당-x
     * @param y 식당-y
     * @return
     */
    public KakaoRestaurant findRestaurant(Long restaurantId, double x, double y){
        String baseUrl = "https://dapi.kakao.com/v2/local/search/category.json?category_group_code=FD6&radius=10";
        String resultUrl;

        for(int page=1; page<=3; page++){
            try{
                resultUrl = baseUrl + "&x=" + x + "&y=" + y;
                URL url = new URL(resultUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", "KakaoAK c7c0edb9f622d6932c33a50bd0a2aa07");

                // 결과 코드가 200이라면 성공
                // 200 아닐경우 예외처리 필요
//                System.out.println("conn.getResponseCode() = " + conn.getResponseCode());
//                System.out.println("conn.getResponseMessage() = " + conn.getResponseMessage());

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
                    // 일치하는 식당을 찾았다면
                    if(restaurant.get("id").getAsLong() == restaurantId){
                        KakaoRestaurant kakaoRestaurant = KakaoRestaurant.builder()
                                .id(restaurant.get("id").getAsLong())
                                .name(restaurant.get("place_name").getAsString())
                                .address(restaurant.get("road_address_name").getAsString())
                                .category(restaurant.get("category_name").getAsString())
                                .phone(restaurant.get("phone").getAsString())
                                .siteUrl(restaurant.get("place_url").getAsString())
                                .longitude(restaurant.get("x").getAsDouble())
                                .latitude(restaurant.get("y").getAsDouble())
                                .build();

                        return kakaoRestaurant;
                    }
                }

                br.close();

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

        return null;
    }

    // 키워드로 식당조회
    public List<KakaoRestaurant> searchRestaurantByKeyword(String query, Double x, Double y){
        String baseUrl = "https://dapi.kakao.com/v2/local/search/keyword.json?category_group_code=FD6";
        String resultUrl;
        List<KakaoRestaurant> kakaoRestaurantList = new ArrayList<>();

        try {
            query = URLEncoder.encode(query, "UTF-8"); // 한글 인코딩
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }

        for(int page=1; page<=3; page++){
            try{
                resultUrl = baseUrl + "&x=" + x + "&y=" + y + "&query=" + query + "&page=" + page;
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
                            .phone(restaurant.get("phone").getAsString())
                            .siteUrl(restaurant.get("place_url").getAsString())
                            .longitude(restaurant.get("x").getAsDouble())
                            .latitude(restaurant.get("y").getAsDouble())
                            .build();
                    kakaoRestaurantList.add(kakaoRestaurant);
                }

                br.close();

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