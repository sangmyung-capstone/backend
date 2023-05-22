package capstone.bapool.restaurant.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GetRestaurantsOnMapRes {

    List<RestaurantInfo> restaurantInfos;

    /* 생성자 */
    public GetRestaurantsOnMapRes(){
        this.restaurantInfos = new ArrayList<>();
    }

    public GetRestaurantsOnMapRes(List<RestaurantInfo> restaurantInfos) {
        this.restaurantInfos = restaurantInfos;
    }
    /* 생성자 끝 */

    public void addrestaurant(RestaurantInfo restaurantInfo){
        restaurantInfos.add(restaurantInfo);
    }

    public void setRestaurantInfos(List<RestaurantInfo> temp){
        this.restaurantInfos = temp;
    }
}
