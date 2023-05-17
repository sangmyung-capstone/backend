package capstone.bapool.party;

import capstone.bapool.entity.Party;
import capstone.bapool.entity.Restaurant;
import capstone.bapool.party.dto.PartiesInRestaurantRes;
import capstone.bapool.party.dto.PartyInfo;
import capstone.bapool.restaurant.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PartyService {

    private final RestaurantRepository restaurantRepository;
    private final PartyRepository partyRepository;

    // 식당안의 파티리스트 조회
    public PartiesInRestaurantRes findPartiesInRestaurant(Long userId, Long restaurantId){

        PartiesInRestaurantRes partiesInRestaurantRes = new PartiesInRestaurantRes();

        // db에서 식당 조회
        Restaurant restaurant = restaurantRepository.findOne(restaurantId);
        if(restaurant == null){
            System.out.println("restaurantId = " + restaurantId);
            System.out.println("식당 없음!!");
            return partiesInRestaurantRes;
        }
        partiesInRestaurantRes.setRestaurantName(restaurant.getName());

        // 해당 식당안의 파티 조회
        List<Party> parties = partyRepository.selectPartisInRestaurant(restaurant);
        for(Party party : parties){
            PartyInfo partyInfo = PartyInfo.builder()
                    .partyId(party.getId())
                    .partyName(party.getName())
                    .menu(party.getMenu())
                    .detail(party.getDetail())
                    .hasBlockUser(false) // 다시 입력해줘야함!!
                    .participants(0) // 다시 입력해줘야함!!
                    .maxPeople(party.getMaxPeople())
                    .startDate(party.getStartDate())
                    .endDate(party.getEndDate())
                    .rating(null) // 다시 입력해줘야함!!
                    .hastag(null) // 다시 입력해줘야함!!
                    .build();
            partiesInRestaurantRes.addPartyInfos(partyInfo);
        }

        return partiesInRestaurantRes;
    }
}
