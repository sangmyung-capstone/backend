package capstone.bapool.restaurant.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class GetRestaurantBottomListRes {

    @JsonProperty("restaurant_img_urls")
    private List<String> restaurantImgURLs;
}
