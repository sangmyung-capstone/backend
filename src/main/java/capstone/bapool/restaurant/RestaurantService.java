package capstone.bapool.restaurant;

import capstone.bapool.config.error.BaseException;
import capstone.bapool.model.Restaurant;
import capstone.bapool.party.PartyRepository;
import capstone.bapool.restaurant.dto.*;
import capstone.bapool.user.UserRepository;
import capstone.bapool.utils.KakaoLocalApiUtils;
import capstone.bapool.utils.RequestsUtils;
import capstone.bapool.utils.dto.ImgURLAndMenu;
import capstone.bapool.utils.dto.KakaoRestaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static capstone.bapool.config.error.StatusEnum.NOT_FOUND_RESTAURANT_FAILURE;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantService {

    private final KakaoLocalApiUtils kakaoLocalApiUtils;
    private final RestaurantRepository restaurantRepository;
    private final PartyRepository partyRepository;
    private final RequestsUtils requestsUtils;
    private final UserRepository userRepository;

    /**
     * 지도화면을 위한 식당리스트 조회, 현위치에서 식당 재검색
     * @param rect 화면의 꼭짓점 값
     */
    public GetRestaurantsOnMapRes findRestaurantsOnMap(String rect){

        // 카카오 로컬 api 통신
        List<KakaoRestaurant> kakaoRestaurantList = kakaoLocalApiUtils.searchPlaceWithRestaurantCategory(rect);

        List<RestaurantInfo> restaurantInfoList = new ArrayList<>();
        // 카카오와 통신한 응답값으로부터 원하는 값만 추출해 리스트에 추가하기
        for(KakaoRestaurant kakaoRestaurant : kakaoRestaurantList){

            int partyNum = 0; // 식당 안의 파티개수
            Restaurant restaurant = restaurantRepository.findById(kakaoRestaurant.getId()).orElse(null);
            if(restaurant != null){ // 식당이 db에 저장되어 있으면
                partyNum = partyRepository.countByRestaurantAndStartDateAfter(restaurant, LocalDateTime.now()).intValue();
            }

            RestaurantInfo restaurantInfo = RestaurantInfo.builder()
                    .restaurantId(kakaoRestaurant.getId())
                    .restaurantName(kakaoRestaurant.getName())
                    .restaurantAddress(kakaoRestaurant.getAddress())
                    .category(kakaoRestaurant.getCategory())
                    .numOfParty(partyNum)
                    .restaurantLongitude(kakaoRestaurant.getX())
                    .restaurantLatitude(kakaoRestaurant.getY())
                    .build();

            restaurantInfoList.add(restaurantInfo);
        }

        return new GetRestaurantsOnMapRes(restaurantInfoList);
    }

    /**
     * 식당 마커정보 조회
     * @param userId 유저 id
     * @param restaurantId 식당 id
     * @param restaurantX 식당-x
     * @param restaurantY 식당-y
     */
    public GetRestaurantMarkerInfoRes findRestaurantMakerInfo(Long userId, Long restaurantId, double restaurantX, double restaurantY){

        KakaoRestaurant kakaoRestaurant = kakaoLocalApiUtils.findRestaurant(restaurantId, restaurantX, restaurantY);
        if(kakaoRestaurant == null){ // 해당하는 식당정보를 찾을 수 없으면
            throw new BaseException(NOT_FOUND_RESTAURANT_FAILURE);
        }

        int partyNum = 0; // 식당 안의 파티개수
        Restaurant restaurant = restaurantRepository.findById(kakaoRestaurant.getId()).orElse(null);
        if(restaurant != null){ // 식당이 db에 저장되어 있으면
            partyNum = partyRepository.countByRestaurantAndStartDateAfter(restaurant, LocalDateTime.now()).intValue();
        }

        // 식당의 이미지, 메뉴 크롤링
        ImgURLAndMenu imgUrlAndMenu = requestsUtils.crawlingImgURLAndMenu(kakaoRestaurant.getId());

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
                .imgUrl(imgUrlAndMenu.getImgUrl())
                .menu(imgUrlAndMenu.getMenus())
                .build();

        return getRestaurantMarkerInfoRes;
    }

    // 식당 바텀리스트
    public GetRestaurantBottomListRes findRestaurantBottomList(GetRestaurantBottomListReq request){

        List<String> restaurantImgURLs = new ArrayList<>();

        for(Long restaurantId : request.getRestaurantIdList()){
            restaurantImgURLs.add(requestsUtils.crawlingImgURL(restaurantId));
        }

        return new GetRestaurantBottomListRes(restaurantImgURLs);
    }

    /**
     * 식당 검색
     * @param x 이 x좌표 주위의 식당을 위주로 검색
     * @param y 이 y좌표 주위의 식당을 위주로 검색
     */
    public GetSearchRestaurantRes searchRestaurant(String query, Double x, Double y){

        List<RestaurantInfo> restaurantInfoList = new ArrayList<>();

        List<KakaoRestaurant> kakaoRestaurantList = kakaoLocalApiUtils.searchRestaurantByKeyword(query, x, y);

        for(KakaoRestaurant kakaoRestaurant : kakaoRestaurantList){
            int partyNum = 0; // 식당 안의 파티개수
            Restaurant restaurant = restaurantRepository.findById(kakaoRestaurant.getId()).orElse(null);
            if(restaurant != null){ // 식당이 db에 저장되어 있으면
                partyNum = partyRepository.countByRestaurantAndStartDateAfter(restaurant, LocalDateTime.now()).intValue();
            }

            RestaurantInfo restaurantInfo = RestaurantInfo.builder()
                    .restaurantId(kakaoRestaurant.getId())
                    .restaurantName(kakaoRestaurant.getName())
                    .restaurantAddress(kakaoRestaurant.getAddress())
                    .category(kakaoRestaurant.getCategory())
                    .numOfParty(partyNum)
                    .restaurantLongitude(kakaoRestaurant.getX())
                    .restaurantLatitude(kakaoRestaurant.getY())
                    .build();

            restaurantInfoList.add(restaurantInfo);
        }

        return new GetSearchRestaurantRes(restaurantInfoList);
    }
}
