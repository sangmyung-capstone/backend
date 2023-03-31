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

    public GetRestaurantInfoRes getRestaurantInfo(){
        RestaurantInfo restaurants  = new RestaurantInfo(1l,"a","b","c","d",1,1d,1d);
        GetRestaurantInfoRes restaurantInfos = new GetRestaurantInfoRes();
        restaurantInfos.addrestaurant(restaurants);
        RestaurantInfo restaurantss  = new RestaurantInfo(5l,"a","fdsad","c","d",1,1d,1d);
        restaurantInfos.addrestaurant(restaurantss);

        return restaurantInfos;
    }
}
