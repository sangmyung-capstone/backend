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
import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurantService {

    public GetRestaurantInfoRes getRestaurantInfo(String rect){
        GetRestaurantInfoRes restaurantInfos = new GetRestaurantInfoRes();
        List<RestaurantInfo> temp = searchByCategory(rect);
        restaurantInfos.setRestaurants(temp);
//        RestaurantInfo restaurants  = new RestaurantInfo();
//        restaurantInfos.addrestaurant(restaurants);
//        RestaurantInfo restaurantss  = new RestaurantInfo(5l,"a","fdsad","c","d",1,1d,1d);
//        restaurantInfos.addrestaurant(restaurantss);

        return restaurantInfos;
    }

    public List<RestaurantInfo> searchByCategory(String rect){

        String baseUrl = "https://dapi.kakao.com/v2/local/search/category.json?category_group_code=FD6";
        String resultUrl;
        List<RestaurantInfo> restaurantInfoList = new ArrayList<>();

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
                RestaurantInfo restaurantInfo;
                JsonArray documents = element.getAsJsonObject().get("documents").getAsJsonArray();

                for(int i=0; i<documents.size(); i++){
                    JsonObject restaurant = documents.get(i).getAsJsonObject();
                    restaurantInfo = RestaurantInfo.builder()
                            .restaurant_id(restaurant.get("id").getAsLong())
                            .restaurant_name(restaurant.get("place_name").getAsString())
                            .restaurant_address(restaurant.get("road_address_name").getAsString())
                            .category(restaurant.get("category_name").getAsString())
                            .num_of_party(101)
                            .imgUrl("https://temp/imgUrl")
                            .restaurant_longitude(restaurant.get("x").getAsDouble())
                            .restaurant_latitude(restaurant.get("y").getAsDouble())
                            .build();
                    restaurantInfoList.add(restaurantInfo);
                }

                br.close();

                for(RestaurantInfo info : restaurantInfoList){
                    System.out.println(info);
                    System.out.println();
                }

            }catch (IOException e) {
                e.printStackTrace();
            }
        }

        return restaurantInfoList;
    }
}
