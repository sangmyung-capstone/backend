package capstone.bapool.restaurant;

import capstone.bapool.config.response.ResponseDto;
import capstone.bapool.restaurant.dto.GetRestaurantMarkerInfoRes;
import capstone.bapool.restaurant.dto.GetRestaurantsOnMapRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = {"/restaurants", "/test/restaurants"})
@Slf4j
public class RestaurantController {

    RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService){
        this.restaurantService = restaurantService;
    }

    // 홈화면
    @GetMapping("/{user-id}")
    public ResponseEntity<ResponseDto> restaurantOnMapList(@PathVariable(name = "user-id")Long userId, @RequestParam String rect){

//        System.out.println("userId = " + userId);
//        System.out.println("rect = " + rect);

        ResponseDto responseDto = new ResponseDto<GetRestaurantsOnMapRes>(200,"요청성공",restaurantService.findRestaurantsOnMap(rect));

        return ResponseEntity.ok().body(ResponseDto.create(restaurantService.findRestaurantsOnMap(rect)));
    }


    /**
     * [GET] /restaurants/{user-id}/{restaurant-id}?longitude=&latitude=
     * 식당 마커정보 조회
     * @param userId 유저 id
     * @param restaurantId 식당 id
     * @param restaurantX 식당-x
     * @param restaurantY 식당-y
     * @return
     */
    @GetMapping("/{user-id}/{restaurant-id}")
    public ResponseEntity<ResponseDto> restaurantMarkerInfoDetails(@PathVariable("user-id")Long userId, @PathVariable("restaurant-id")Long restaurantId,
                                            @RequestParam("longitude") double restaurantX, @RequestParam("latitude") double restaurantY){

        GetRestaurantMarkerInfoRes getRestaurantMarkerInfoRes = restaurantService.findRestaurantMakerInfo(userId, restaurantId, restaurantX, restaurantY);

        ResponseDto response = ResponseDto.create(getRestaurantMarkerInfoRes);
        log.info("식당 마커정보 조회 완료: {}-{}", getRestaurantMarkerInfoRes.getRestaurantName(), restaurantId);

        return ResponseEntity.ok().body(response);
    }
}
