package capstone.bapool.restaurant;

import capstone.bapool.restaurant.dto.TempResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = {"/restaurants", "/test/restaurants"})
public class RestaurantController {

    RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService){
        this.restaurantService = restaurantService;
    }

    @GetMapping("/{user-id}")
    public TempResponse getRestaurantInfo(@PathVariable(name = "user-id")Long userId, @RequestParam String rect){

        System.out.println("userId = " + userId);
        System.out.println("rect = " + rect);


        TempResponse tempResponse = new TempResponse(restaurantService.getRestaurantInfo(rect));
        return tempResponse;
    }
}
