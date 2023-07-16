package capstone.bapool.restaurant;

import capstone.bapool.config.error.BaseException;
import capstone.bapool.model.Party;
import capstone.bapool.model.Restaurant;
import capstone.bapool.model.User;
import capstone.bapool.model.enumerate.PartyStatus;
import capstone.bapool.party.PartyRepository;
import capstone.bapool.party.dto.PartyInfoSimple;
import capstone.bapool.restaurant.dto.*;
import capstone.bapool.user.UserRepository;
import capstone.bapool.user.UserService;
import capstone.bapool.utils.KakaoLocalApiService;
import capstone.bapool.utils.RequestsService;
import capstone.bapool.utils.SeleniumService;
import capstone.bapool.utils.dto.ImgURLAndMenu;
import capstone.bapool.utils.dto.KakaoRestaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static capstone.bapool.config.error.StatusEnum.NOT_FOUND_RESTAURANT_FAILURE;
import static capstone.bapool.config.error.StatusEnum.NOT_FOUND_USER_FAILURE;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantService {

    private final KakaoLocalApiService kakaoLocalApiService;
    private final RestaurantRepository restaurantRepository;
    private final PartyRepository partyRepository;
    private final SeleniumService seleniumService;
    private final RequestsService requestsService;
    private final UserRepository userRepository;

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

    /**
     * 식당 마커정보
     * @param userId 듀저 id
     * @param restaurantId 식당 id
     * @param restaurantX 식당-x
     * @param restaurantY 식당-y
     * @return
     */
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

        // 식당의 이미지와 메뉴 크롤링하기
//        ImgUrlAndMenu imgUrlAndMenu = seleniumService.crawlingImgURLAndMenu(kakaoRestaurant.getSiteUrl());

        ImgURLAndMenu imgUrlAndMenu = requestsService.crawlingImgURLAndMenu(kakaoRestaurant.getId());

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
            restaurantImgURLs.add(requestsService.crawlingImgURL(restaurantId));
        }

        return new GetRestaurantBottomListRes(restaurantImgURLs);
    }

    // 식당 검색
    public GetSearchRestaurantRes searchRestaurant(String query, Double x, Double y){

        List<RestaurantInfo> restaurantInfoList = new ArrayList<>();

        List<KakaoRestaurant> kakaoRestaurantList = kakaoLocalApiService.searchRestaurantByKeyword(query, x, y);

        for(KakaoRestaurant kakaoRestaurant : kakaoRestaurantList){
            // 식당 안의 파티개수
            int partyNum = 0;
            Restaurant restaurant = restaurantRepository.findById(kakaoRestaurant.getId()).orElse(null);
            if(restaurant != null){ // 식당이 db에 저장되어 있으면
                partyNum = partyRepository.countByRestaurant(restaurant).intValue();
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

    // 먹었던 식당정보 리스트
    public GetAtePartyInfoRes findAtePartyInfo(Long userId){

        User user = userRepository.findById(userId).orElseThrow(
                () -> {throw new BaseException(NOT_FOUND_USER_FAILURE);}
        );
        
        List<Party> parties = partyRepository.findAtePartyByUser(user.getId(), PartyStatus.DONE.toString()).orElse(null);

        System.out.println("parties.size() = " + parties.size());
        
        List<PartyInfoSimple> partyInfoSimpleList = new ArrayList<>();
        for(Party party : parties){
            Restaurant restaurant = restaurantRepository.findById(party.getRestaurant().getId()).orElseThrow(
                    () -> {throw new BaseException(NOT_FOUND_RESTAURANT_FAILURE);}
            );

            PartyInfoSimple partyInfoSimple = PartyInfoSimple.builder()
                    .partyId(party.getId())
                    .partyName(party.getName())
                    .restaurantName(restaurant.getName())
                    .restaurantImgURL(restaurant.getImgUrl())
                    .restaurantAddress(restaurant.getAddress())
                    .category(restaurant.getCategory())
                    .build();

            partyInfoSimpleList.add(partyInfoSimple);
        }
        
        return new GetAtePartyInfoRes(partyInfoSimpleList);
    }
}
