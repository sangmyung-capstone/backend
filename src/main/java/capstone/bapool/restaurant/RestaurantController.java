package capstone.bapool.restaurant;

import capstone.bapool.restaurant.dto.GetRestaurantInfoRes;
import capstone.bapool.restaurant.dto.RestaurantInfo;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {
    @GetMapping("/{user-id}")
    public GetRestaurantInfoRes getRestaurantInfo(@PathVariable(name = "user-id")Long userId, @RequestParam String rect){
        System.out.println("userId = " + userId);
        System.out.println("rect = " + rect);

        return restaurantService.getRestaurantInfo();
    }

    RestaurantService restaurantService;
    public RestaurantController(RestaurantService restaurantService){
        this.restaurantService = restaurantService;
    }

}
