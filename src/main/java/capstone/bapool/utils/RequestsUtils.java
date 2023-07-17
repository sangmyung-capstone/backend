package capstone.bapool.utils;

import capstone.bapool.utils.dto.ImgURLAndMenu;
import capstone.bapool.utils.dto.Menu;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
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
public class RequestsUtils {

    public ImgURLAndMenu crawlingImgURLAndMenu(Long restaurantId){
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

            return new ImgURLAndMenu(getImgURL(element), getMenus(element));
        }catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    // 이미지 크롤링
    public String crawlingImgURL(Long restaurantId){
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

            return getImgURL(element);
        }catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    // 이미지 크롤링
    private String getImgURL(JsonElement element){
        JsonElement imgURL = element.getAsJsonObject().get("basicInfo").getAsJsonObject().get("mainphotourl");

        System.out.println("imgURL = " + imgURL);
        if(imgURL != null){
            String imgURLString = imgURL.toString();
            return imgURLString.substring(imgURLString.indexOf('"')+1, imgURLString.lastIndexOf('"'));
        }
        else{
            return null;
        }
    }

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
                System.out.println("menuName = " + menuName + ", price = " + price);
            }
        }

        return menus;
    }
}
