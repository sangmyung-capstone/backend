package capstone.bapool.restaurant;

import capstone.bapool.config.error.BaseException;
import capstone.bapool.config.response.ResponseDto;
import capstone.bapool.restaurant.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static capstone.bapool.config.error.StatusEnum.PAYLOAD_TOO_LARGE;

@RestController
@RequestMapping(path = {"/restaurants", "/test/restaurants"})
@Slf4j
public class RestaurantController {

    RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService){
        this.restaurantService = restaurantService;
    }

    /**
     * [GET] /restaurants/{user-id}?rect=
     * 지도화면, 현위치에서 재검색
     * @param rect 화면의 꼭짓점 값
     */
    @GetMapping("/{user-id}")
    public ResponseEntity<ResponseDto> restaurantOnMapList(@PathVariable(name = "user-id")Long userId, @RequestParam String rect){

        log.info("지도화면 요청: userId={}, rect={}", userId, rect);

        GetRestaurantsOnMapRes getRestaurantsOnMapRes = restaurantService.findRestaurantsOnMap(rect);

        log.info("지도화면 요청처리 완료: userId={}, rect={}", userId, rect);

        ResponseDto<GetRestaurantsOnMapRes> response = ResponseDto.create(getRestaurantsOnMapRes);
        return ResponseEntity.ok(response);
    }


    /**
     * [GET] /restaurants/{user-id}/{restaurant-id}?longitude=&latitude=
     * 식당 마커정보 조회
     * @param userId 유저 id
     * @param restaurantId 식당 id
     * @param restaurantX 식당-x
     * @param restaurantY 식당-y
     */
    @GetMapping("/{user-id}/{restaurant-id}")
    public ResponseEntity<ResponseDto> restaurantMarkerInfoDetails(@PathVariable("user-id")Long userId, @PathVariable("restaurant-id")Long restaurantId,
                                            @RequestParam("longitude") double restaurantX, @RequestParam("latitude") double restaurantY){

        log.info("식당 마커정보 요청: restaurantId={}", restaurantId);

        GetRestaurantMarkerInfoRes getRestaurantMarkerInfoRes = restaurantService.findRestaurantMakerInfo(userId, restaurantId, restaurantX, restaurantY);

        log.info("식당 마커정보 요청처리 완료: restaurantId={}", restaurantId);

        ResponseDto<GetRestaurantMarkerInfoRes> response = ResponseDto.create(getRestaurantMarkerInfoRes);
        return ResponseEntity.ok().body(response);
    }

    /**
     * [GET] /restaurants/bottomlist/{user-id}
     * 식당 바텀리스트 이미지
     */
    @PostMapping("/bottomlist/{user-id}")
    public ResponseEntity<ResponseDto> restaurantBottomList(@RequestBody GetRestaurantBottomListReq getRestaurantBottomListReq){

        log.info("식당 바텀리스트 요청: 식당id 리스트={}", getRestaurantBottomListReq.toString());

        // 한번에 3개까지만 조회 가능
        if(getRestaurantBottomListReq.getRestaurantIdList().size() > 3){
            throw new BaseException(PAYLOAD_TOO_LARGE);
        }

        GetRestaurantBottomListRes getRestaurantBottomListRes = restaurantService.findRestaurantBottomList(getRestaurantBottomListReq);

        log.info("식당 바텀리스트 요청처리 완료: 식당id 리스트={}", getRestaurantBottomListReq.toString());

        ResponseDto response = ResponseDto.create(getRestaurantBottomListRes);
        return ResponseEntity.ok(response);
    }

    /**
     * [GET] /restaurants/search/{user-id}?query=
     * 식당 검색
     * @param query 키워드
     * @param x 이 x좌표를 중심으로 식당 검색
     * @param y 이 y좌표를 중심으로 식당 검색
     */
    @GetMapping("/search/{user-id}")
    public ResponseEntity<ResponseDto> searchRestaurant(@PathVariable("user-id") Long userId, @RequestParam("q") String query, @RequestParam("longitude") Double x, @RequestParam("latitude") Double y){

        log.info("식당 검색 요청: query={}, x={}, y={}", query, x, y);

        GetSearchRestaurantRes getSearchRestaurantRes = restaurantService.searchRestaurant(query, x, y);

        log.info("식당 검색 요청처리 완료: query={}, x={}, y={}", query, x, y);

        ResponseDto<GetSearchRestaurantRes> response = ResponseDto.create(getSearchRestaurantRes);
        return ResponseEntity.ok(response);
    }
}
