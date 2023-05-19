package capstone.bapool.restaurant;

import capstone.bapool.entity.Restaurant;
import capstone.bapool.party.PartyRepository;
import capstone.bapool.restaurant.dto.RestaurantsOnMapRes;
import capstone.bapool.restaurant.dto.RestaurantInfo;
import capstone.bapool.utils.KakaoLocalApiService;
import capstone.bapool.utils.dto.KakaoRestaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
    public RestaurantsOnMapRes findRestaurantsOnMap(String rect){

        // 카카오 로컬 api 통신
        List<KakaoRestaurant> kakaoRestaurantList = kakaoLocalApiService.searchByCategory(rect);

        List<RestaurantInfo> restaurantInfoList = new ArrayList<>();

        // 카카오와 통신한 응답값으로부터 원하는 값만 추출해 리스트에 추가하기
        for(KakaoRestaurant kakaoRestaurant : kakaoRestaurantList){
            // 식당 안의 파티개수
            int partyNum = 0;
            Restaurant restaurant = restaurantRepository.findOne(kakaoRestaurant.getId());
            if(restaurant != null){ // 식당이 db에 저장되어 있으면
                partyNum = partyRepository.countParty(restaurant).intValue();
            }

            RestaurantInfo restaurantInfo2 = RestaurantInfo.builder()
                    .restaurantId(kakaoRestaurant.getId())
                    .restaurantName(kakaoRestaurant.getName())
                    .restaurantAddress(kakaoRestaurant.getAddress())
                    .category(kakaoRestaurant.getCategory())
                    .numOfParty(partyNum)
                    .restaurantLongitude(kakaoRestaurant.getLongitude())
                    .restaurantLatitude(kakaoRestaurant.getLatitude())
                    .build();

            restaurantInfoList.add(restaurantInfo2);
        }

        return new RestaurantsOnMapRes(restaurantInfoList);
    }
}
