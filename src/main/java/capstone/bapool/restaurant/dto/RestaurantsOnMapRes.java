package capstone.bapool.restaurant.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class RestaurantsOnMapRes {

    List<RestaurantInfo2> restaurantInfos;

    // 생성자
    public RestaurantsOnMapRes(){
        this.restaurantInfos = new ArrayList<>();
    }

    public void addrestaurant(RestaurantInfo2 restaurantInfo){
        restaurantInfos.add(restaurantInfo);
    }

    public void setRestaurantInfos(List<RestaurantInfo2> temp){
        this.restaurantInfos = temp;
    }
}
