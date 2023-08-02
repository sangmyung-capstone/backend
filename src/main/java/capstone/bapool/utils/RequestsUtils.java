package capstone.bapool.utils;

import capstone.bapool.utils.dto.ImgURLAndMenu;
import capstone.bapool.utils.dto.Menu;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class RequestsUtils {

    private static final String BASE_URL = "https://place.map.kakao.com/main/v/";

    /**
     * 식당 이미지, 메뉴 크롤링
     * @param restaurantId 식당id
     */
    public ImgURLAndMenu crawlingImgURLAndMenu(Long restaurantId){
        String totalURL = BASE_URL + restaurantId;

        try{
            URL url = new URL(totalURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // 결과 코드가 200이라면 성공
            log.debug("식당 이미지, 메뉴 크롤링: restaurantId={}, status_code={}", restaurantId, conn.getResponseCode());

            // 요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            StringBuilder sb = new StringBuilder(); // string의 많은 덧셈연산은 성능상 좋지 않아서
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            //Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonElement element = JsonParser.parseString(sb.toString());

            return new ImgURLAndMenu(getImgURL(element), getMenus(element));
        }catch (IOException e) {
            log.error("식당, 이미지 크롤링 에러: restaurantId={}", restaurantId);
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 식당 이미지 크롤링
     * @param restaurantId 식당id
     */
    public String crawlingImgURL(Long restaurantId){

        String resultUrl = BASE_URL + restaurantId;

        try{
            URL url = new URL(resultUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            // 결과 코드가 200이라면 성공
            log.debug("식당 이미지 크롤링: restaurantId-{}, status_code-{}", restaurantId, conn.getResponseCode());

            // 요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            //Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonElement element = JsonParser.parseString(sb.toString());

            return getImgURL(element);
        }catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    // 이미지 크롤링
    private String getImgURL(JsonElement element){
        JsonElement imgURL = element.getAsJsonObject().get("basicInfo").getAsJsonObject().get("mainphotourl");

        if(imgURL != null){
            String imgURLString = imgURL.toString();
            log.info("이미지 크롤링: imgURL={}", imgURLString);
            return imgURLString.substring(imgURLString.indexOf('"')+1, imgURLString.lastIndexOf('"'));
        }
        else{
            return null;
        }
    }

    // 메뉴 크롤링
    private List<Menu> getMenus(JsonElement element){
        JsonElement menuInfo = element.getAsJsonObject().get("menuInfo");
        List<Menu> menus = new ArrayList<>();
        if(menuInfo != null){
            JsonArray menuList = menuInfo.getAsJsonObject().get("menuList").getAsJsonArray();

            String price;
            String menuName;
            for(JsonElement menu : menuList){
                price = menu.getAsJsonObject().get("price")==null ? null : menu.getAsJsonObject().get("price").getAsString();
                menuName = menu.getAsJsonObject().get("menu").getAsString();

                menus.add(new Menu(menuName, price));
                log.info("메뉴 크롤링: menu={}, price={}", menuName, price);
            }
        }

        return menus;
    }
}
