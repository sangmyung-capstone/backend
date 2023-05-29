package capstone.bapool.party;

import capstone.bapool.model.Party;
import capstone.bapool.model.PartyHashtag;
import capstone.bapool.model.Restaurant;
import capstone.bapool.party.dto.PartiesInRestaurantRes;
import capstone.bapool.party.dto.PartyInfo;
import capstone.bapool.config.error.BaseException;
import capstone.bapool.model.PartyParticipant;
import capstone.bapool.model.User;
import capstone.bapool.model.enumerate.PartyStatus;
import capstone.bapool.model.enumerate.RoleType;
import capstone.bapool.party.dto.PostPartyReq;
import capstone.bapool.restaurant.RestaurantRepository;
import capstone.bapool.user.UserDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static capstone.bapool.config.error.StatusEnum.NOT_FOUND_RESTAURANT_FAILURE;
import static capstone.bapool.config.error.StatusEnum.NOT_FOUND_USER_FAILURE;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PartyService {

    private final RestaurantRepository restaurantRepository;
    private final UserDao userRepository;
    private final PartyRepository partyRepository;
    private final PartyParticipantRepository partyParticipantRepository;
    private final PartyHashtagRepository partyHashtagRepository;
    private final FireBasePartyRepository fireBasePartyRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = false)
    public Long save(PostPartyReq postPartyReq, Long userId) {
        Restaurant restaurant = restaurantRepository.findById(postPartyReq.getRestaurantId())
                .orElseThrow(() -> new BaseException(NOT_FOUND_RESTAURANT_FAILURE));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));

        Party party = Party.builder()
                .name(postPartyReq.getPartyName())
                .restaurant(restaurant)
                .startDate(postPartyReq.getStartDate())
                .endDate(postPartyReq.getEndDate())
                .menu(postPartyReq.getMenu())
                .maxPeople(postPartyReq.getMaxPeople())
                .partyStatus(PartyStatus.RECRUITING)
                .detail(postPartyReq.getDetail())
                .build();
        Party savedParty = partyRepository.save(party);

        for (Integer value : postPartyReq.getHashtag()) {
            PartyHashtag partyHashtag = PartyHashtag.create(savedParty, value);
            hashtagRepository.save(partyHashtag);
        }
        PartyParticipant partyParticipant = PartyParticipant.makeMapping(user, savedParty, RoleType.LEADER);
        partyAndUserRepository.save(partyParticipant);

        return savedParty.getId();
    }

    // 식당안의 파티리스트 조회
    public PartiesInRestaurantRes findPartiesInRestaurant(Long userId, Long restaurantId){

        PartiesInRestaurantRes partiesInRestaurantRes = new PartiesInRestaurantRes();

        // db에서 식당 조회
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElse(null);
        if(restaurant == null){
            System.out.println("restaurantId = " + restaurantId);
            System.out.println("식당 없음!!");
            return partiesInRestaurantRes;
        }
        partiesInRestaurantRes.setRestaurantName(restaurant.getName());

        // 임시값
        List<Integer> hashtag = new ArrayList<>();
        hashtag.add(1);
        List<Double> rating = new ArrayList<>();
        rating.add(4.2);

        // 해당 식당안의 파티리스트 조회
        List<Party> parties = partyRepository.findByRestaurant(restaurant);
        for(Party party : parties){
            PartyInfo partyInfo = PartyInfo.builder()
                    .partyId(party.getId())
                    .partyName(party.getName())
                    .isParticipate(false) // 다시 입력해줘야함!!
                    .menu(party.getMenu())
                    .detail(party.getDetail())
                    .hasBlockUser(false) // 다시 입력해줘야함!!
                    .participants(1) // 다시 입력해줘야함!!
                    .maxPeople(party.getMaxPeople())
                    .startDate(party.getStartDate())
                    .endDate(party.getEndDate())
                    .rating(rating) // 다시 입력해줘야함!!
                    .hastag(hashtag) // 다시 입력해줘야함!!
                    .build();
            partiesInRestaurantRes.addPartyInfos(partyInfo);
        }

        return partiesInRestaurantRes;
    }
}
