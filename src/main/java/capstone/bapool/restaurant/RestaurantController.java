package capstone.bapool.restaurant;

import capstone.bapool.restaurant.dto.GetRestaurantInfoRes;
import capstone.bapool.restaurant.dto.RestaurantInfo;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {
    @GetMapping("/{user-id}")
    public GetRestaurantInfoRes 식당정보(@PathVariable(name = "user-id")Long userId, @RequestParam String rect){
        System.out.println("userId = " + userId);
        System.out.println("rect = " + rect);
        RestaurantInfo restaurants  = new RestaurantInfo(1l,"a","b","c","d",1,1d,1d);
        GetRestaurantInfoRes abc = new GetRestaurantInfoRes();
        abc.addrestaurant(restaurants);
        RestaurantInfo restaurantss  = new RestaurantInfo(1l,"a","b","c","d",1,1d,1d);
        abc.addrestaurant(restaurantss);

        return abc;
    }
}
