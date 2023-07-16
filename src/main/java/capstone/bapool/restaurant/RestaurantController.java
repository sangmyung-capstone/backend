package capstone.bapool.restaurant;

import capstone.bapool.config.response.ResponseDto;
import capstone.bapool.restaurant.dto.*;
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

        ResponseDto response = ResponseDto.create(restaurantService.findRestaurantsOnMap(rect));

        return ResponseEntity.ok(response);
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
    
    // 식당 바텀리스트
    @PostMapping("/bottomlist/{user-id}")
    public ResponseEntity<ResponseDto> restaurantBottomList(@RequestBody GetRestaurantBottomListReq getRestaurantBottomListReq){
//        System.out.println("getRestaurantBottomListReq.getRestaurantURLs().size() = " + getRestaurantBottomListReq.getRestaurantURLs().size());

        GetRestaurantBottomListRes getRestaurantBottomListRes = restaurantService.findRestaurantBottomList(getRestaurantBottomListReq);

        ResponseDto response = ResponseDto.create(getRestaurantBottomListRes);

        return ResponseEntity.ok(response);
    }

    // 식당 검색
    @GetMapping("/search/{user-id}")
    public ResponseEntity<ResponseDto> searchRestaurant(@PathVariable("user-id") Long userId, @RequestParam("q") String query, @RequestParam("longitude") Double x, @RequestParam("latitude") Double y){

        GetSearchRestaurantRes getSearchRestaurantRes = restaurantService.searchRestaurant(query, x, y);

        ResponseDto<GetSearchRestaurantRes> response = ResponseDto.create(getSearchRestaurantRes);

        return ResponseEntity.ok(response);
    }

    // 먹었던 식당정보 조회
    @GetMapping("/log/{user-id}")
    public ResponseEntity<ResponseDto> atePartyInfoList(@PathVariable("user-id") Long userId){

        GetAtePartyInfoRes getAtePartyInfoRes = restaurantService.findAtePartyInfo(userId);

        ResponseDto<GetAtePartyInfoRes> response = ResponseDto.create(getAtePartyInfoRes);

        return ResponseEntity.ok(response);
    }
}
