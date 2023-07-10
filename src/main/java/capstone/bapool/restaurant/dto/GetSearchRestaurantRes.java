package capstone.bapool.restaurant.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetSearchRestaurantRes {

    @JsonProperty("restaurants")
    private List<RestaurantInfo> restaurantInfoList;
}
