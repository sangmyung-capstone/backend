package capstone.bapool.restaurant;

import capstone.bapool.config.response.ResponseDto;
import capstone.bapool.restaurant.dto.RestaurantsOnMapRes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = {"/restaurants", "/test/restaurants"})
public class RestaurantController {

    RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService){
        this.restaurantService = restaurantService;
    }

    // 홈화면
    @GetMapping("/{user-id}")
    public ResponseEntity<Object> restaurantOnMapList(@PathVariable(name = "user-id")Long userId, @RequestParam String rect){

        System.out.println("userId = " + userId);
        System.out.println("rect = " + rect);

        ResponseDto responseDto = new ResponseDto<RestaurantsOnMapRes>(200,"요청성공",restaurantService.findRestaurantsOnMap(rect));

        return ResponseEntity.ok().body(ResponseDto.res(restaurantService.findRestaurantsOnMap(rect)));
    }
    
}
