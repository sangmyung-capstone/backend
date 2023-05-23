package capstone.bapool.restaurant;

import capstone.bapool.config.error.BaseException;
import capstone.bapool.model.Restaurant;
import capstone.bapool.party.PartyRepository;
import capstone.bapool.restaurant.dto.GetRestaurantMarkerInfoRes;
import capstone.bapool.restaurant.dto.GetRestaurantsOnMapRes;
import capstone.bapool.restaurant.dto.Menu;
import capstone.bapool.restaurant.dto.RestaurantInfo;
import capstone.bapool.utils.KakaoLocalApiService;
import capstone.bapool.utils.dto.KakaoRestaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static capstone.bapool.config.error.StatusEnum.NOT_FOUND_RESTAURANT_FAILURE;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantService {

    private final KakaoLocalApiService kakaoLocalApiService;
    private final RestaurantRepository restaurantRepository;
    private final PartyRepository partyRepository;

    /**
     * 지도화면을 위한 식당리스트 조회
     * @param rect 화면의 꼭짓점 값
     * @return
     */
    public GetRestaurantsOnMapRes findRestaurantsOnMap(String rect){

        // 카카오 로컬 api 통신
        List<KakaoRestaurant> kakaoRestaurantList = kakaoLocalApiService.searchByCategory(rect);

        List<RestaurantInfo> restaurantInfoList = new ArrayList<>();

        // 카카오와 통신한 응답값으로부터 원하는 값만 추출해 리스트에 추가하기
        for(KakaoRestaurant kakaoRestaurant : kakaoRestaurantList){
            // 식당 안의 파티개수
            int partyNum = 0;
            Restaurant restaurant = restaurantRepository.findById(kakaoRestaurant.getId()).orElse(null);
            if(restaurant != null){ // 식당이 db에 저장되어 있으면
                partyNum = partyRepository.countByRestaurant(restaurant).intValue();
            }

            RestaurantInfo restaurantInfo2 = RestaurantInfo.builder()
                    .restaurantId(kakaoRestaurant.getId())
                    .restaurantName(kakaoRestaurant.getName())
                    .restaurantAddress(kakaoRestaurant.getAddress())
                    .category(kakaoRestaurant.getCategory())
                    .numOfParty(partyNum)
                    .restaurantLongitude(kakaoRestaurant.getX())
                    .restaurantLatitude(kakaoRestaurant.getY())
                    .build();

            restaurantInfoList.add(restaurantInfo2);
        }

        return new GetRestaurantsOnMapRes(restaurantInfoList);
    }

    public GetRestaurantMarkerInfoRes findRestaurantMakerInfo(Long userId, Long restaurantId, double restaurantX, double restaurantY){

        KakaoRestaurant kakaoRestaurant = kakaoLocalApiService.findRestaurant(restaurantId, restaurantX, restaurantY);
        if(kakaoRestaurant == null){ // 해당하는 식당정보를 찾을 수 없으면
            throw new BaseException(NOT_FOUND_RESTAURANT_FAILURE);
        }

        // 식당 안의 파티개수
        int partyNum = 0;
        Restaurant restaurant = restaurantRepository.findById(kakaoRestaurant.getId()).orElse(null);
        if(restaurant != null){ // 식당이 db에 저장되어 있으면
            partyNum = partyRepository.countByRestaurant(restaurant).intValue();
        }

        // 임시값
        Menu menu = new Menu("피자", "17,000");
        List<Menu> menus = new ArrayList<>();
        menus.add(menu);

        GetRestaurantMarkerInfoRes getRestaurantMarkerInfoRes = GetRestaurantMarkerInfoRes.builder()
                .restaurantId(kakaoRestaurant.getId())
                .restaurantName(kakaoRestaurant.getName())
                .restaurantX(kakaoRestaurant.getX())
                .restaurantY(kakaoRestaurant.getY())
                .restaurantAddress(kakaoRestaurant.getAddress())
                .numOfParty(partyNum)
                .category(kakaoRestaurant.getCategory())
                .link(kakaoRestaurant.getSiteUrl())
                .phone(kakaoRestaurant.getPhone())
                // 수정 필요!!
                .imgUrl("https://i.namu.wiki/i/umI-heVYVS9miQNqXM13FRUOHHL4l1nzsZgN9XRLFG7nI_7Dyf-Myr6HmiWf9Qd7SAZQz3WYSQHPXXtGAwLTag.webp")
                .menu(menus) // 수정 필요!!
                .build();

        return getRestaurantMarkerInfoRes;
    }
}
