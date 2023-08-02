package capstone.bapool.utils;

import capstone.bapool.config.error.BaseException;
import capstone.bapool.utils.dto.KakaoRestaurant;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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

    private static final String CATEGORY_BASE_URL = "https://dapi.kakao.com/v2/local/search/category.json?category_group_code=FD6";
    private static final String KEYWORD_BASE_URL = "https://dapi.kakao.com/v2/local/search/keyword.json?category_group_code=FD6";

    /**
     * 특정범위 식당 조회
     * @param rect 식당 조회 범위
     */
    public List<KakaoRestaurant> searchPlaceWithRestaurantCategory(String rect){
        String totalURL;
        List<KakaoRestaurant> kakaoRestaurantList = new ArrayList<>();

        for(int page=1; page<=3; page++){
            try{
                totalURL = CATEGORY_BASE_URL + "&rect=" + rect + "&page=" + page;
                URL url = new URL(totalURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", "KakaoAK c7c0edb9f622d6932c33a50bd0a2aa07");

                // 결과 코드가 200이라면 성공
                log.info("식당 조회: rect={}, page={}, statusCode={}", rect, page, conn.getResponseCode());

                // 요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                log.info("식당 조회 결과: rect={}, page={}, 결과={}", rect, page, sb.toString());

                //Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
                JsonElement element = JsonParser.parseString(sb.toString());

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

            }catch (IOException e) {
                e.printStackTrace();
                log.error("카카오 로컬 API 사용 에러: 식당조회. rect={}, page={}", rect, page);
            }
        }

        return kakaoRestaurantList;
    }

    /**
     * 특정 식당 조회
     * @param restaurantId 식당 id
     * @param x 식당-x
     * @param y 식당-y
     */
    public KakaoRestaurant findRestaurant(Long restaurantId, double x, double y){
        String totalURL;

        for(int page=1; page<=3; page++){
            try{
                totalURL = CATEGORY_BASE_URL + "&radius=10" + "&x=" + x + "&y=" + y + "&page=" + page;
                URL url = new URL(totalURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", "KakaoAK c7c0edb9f622d6932c33a50bd0a2aa07");

                // 결과 코드가 200이라면 성공
                log.info("특정 식당 조회: restaurantId={}, x={}, y={}, page={}, status_code={}", restaurantId, x, y, page, conn.getResponseCode());

                // 요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                log.info("특정 식당 조회 결과: restaurantId={}, x={}, y={}, page={}, 결과={}", restaurantId, x, y, page, sb.toString());

                //Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
                JsonElement element = JsonParser.parseString(sb.toString());

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
            }catch (IOException e) {
                e.printStackTrace();
                log.error("카카오 로컬 API 사용 에러: 특정 식당 조회. restaurantId={}, x={}, y={}, page={}", restaurantId, x, y, page);
            }
        }

        return null;
    }


    /**
     * 키워드로 식당 검색
     * @param query 키워드
     * @param x 중심 x
     * @param y 중심 y
     */
    public List<KakaoRestaurant> searchRestaurantByKeyword(String query, Double x, Double y){
        String totalURL;
        List<KakaoRestaurant> kakaoRestaurantList = new ArrayList<>();

        // 키워드 한글 인코딩
        try {
            query = URLEncoder.encode(query, "UTF-8");
        }
        catch (Exception e){
            log.error("키워드로 식당 검색: 한글 인토딩 실패. query={}", query);
            e.printStackTrace();
            return null;
        }

        for(int page=1; page<=3; page++){
            try{
                totalURL = KEYWORD_BASE_URL + "&x=" + x + "&y=" + y + "&query=" + query + "&page=" + page;
                URL url = new URL(totalURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", "KakaoAK c7c0edb9f622d6932c33a50bd0a2aa07");


                // 결과 코드가 200이라면 성공
                log.info("키워드로 식당 검색: query={}, x={}, y={}, page={}, status_code={}", query, x, y, page, conn.getResponseCode());

                // 요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                log.info("키워드로 식당 검색 결과: query={}, x={}, y={}, page={}, 결과={}", query, x, y, page, sb.toString());

                //Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
                JsonElement element = JsonParser.parseString(sb.toString());

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
            }catch (IOException e) {
                e.printStackTrace();
                log.error("카카오 로컬 API 사용 에러: 키워드로 식당 검색. query={}, x={}, y={}, page={}", query, x, y, page);
            }
        }


        return kakaoRestaurantList;
    }
}
