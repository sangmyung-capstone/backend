package capstone.bapool.party;

import capstone.bapool.config.error.BaseException;
import capstone.bapool.model.Hashtag;
import capstone.bapool.model.Party;
import capstone.bapool.model.PartyAndUser;
import capstone.bapool.model.Restaurant;
import capstone.bapool.model.User;
import capstone.bapool.model.enumerate.PartyStatus;
import capstone.bapool.model.enumerate.RoleType;
import capstone.bapool.party.dto.PostPartyReq;
import capstone.bapool.restaurant.SpringDataRestaurantRepository;
import capstone.bapool.user.UserDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static capstone.bapool.config.error.StatusEnum.NOT_FOUND_RESTAURANT_FAILURE;
import static capstone.bapool.config.error.StatusEnum.NOT_FOUND_USER_FAILURE;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PartyService {
    private final UserDao userRepository;
    private final PartyRepository partyRepository;
    private final SpringDataRestaurantRepository restaurantRepository;
    private final PartyAndUserRepository partyAndUserRepository;
    private final HashtagRepository hashtagRepository;

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
            Hashtag hashtag = Hashtag.of(savedParty, value);
            hashtagRepository.save(hashtag);
        }
        PartyAndUser partyAndUser = PartyAndUser.makeMapping(user, savedParty, RoleType.LEADER);
        partyAndUserRepository.save(partyAndUser);

        return savedParty.getId();
    }




}
