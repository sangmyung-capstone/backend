package capstone.bapool.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GetRestaurantInfoRes {
    List<RestaurantInfo> restaurants = new ArrayList<>();

    public void addrestaurant(RestaurantInfo restaurantInfo){
        restaurants.add(restaurantInfo);
    }
}
