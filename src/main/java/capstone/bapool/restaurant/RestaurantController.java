package capstone.bapool.restaurant;

import capstone.bapool.config.response.ResponseDto;
import capstone.bapool.restaurant.dto.RestaurantInfoRes;
import capstone.bapool.restaurant.dto.TempResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = {"/restaurants", "/test/restaurants"})
public class RestaurantController {

    RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService){
        this.restaurantService = restaurantService;
    }

    @GetMapping("/{user-id}")
    public ResponseEntity<Object> getRestaurantInfo(@PathVariable(name = "user-id")Long userId, @RequestParam String rect){

        System.out.println("userId = " + userId);
        System.out.println("rect = " + rect);

        ResponseDto responseDto = new ResponseDto<RestaurantInfoRes>(200,"요청성공",restaurantService.getRestaurantInfo(rect));

        return ResponseEntity.ok().body(ResponseDto.res(restaurantService.getRestaurantInfo(rect)));
    }

    @GetMapping("/{user-id}/{restaurant-id}")
    public ResponseEntity<Object> getRestaurantInfo(@PathVariable(name = "user-id")Long userId, @PathVariable(name = "restaurant-id")Long restaurantId){

        System.out.println("userId = " + userId);
        System.out.println(("restaurantId = " + restaurantId));

        ResponseDto responseDto = new ResponseDto<RestaurantInfoRes>(200,"요청성공",restaurantService.getRestaurantInfo(rect));

        return ResponseEntity.ok().body(ResponseDto.res(restaurantService.getRestaurantInfo(rect)));
    }
}
