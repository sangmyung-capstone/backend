package capstone.bapool.restaurant.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GetRestaurantBottomListReq {

    @JsonProperty("restaurant_urls")
    private List<String> restaurantURLs;

//    @Override
//    public String toString(){
//        String result = "";
//
//        for(String url : restaurantURLs){
//            result
//        }
//    }
}