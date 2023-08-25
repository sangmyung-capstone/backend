package capstone.bapool.party;

import capstone.bapool.firebase.FireBasePartyRepository;
import capstone.bapool.firebase.dto.FireBasePartyInfo;
import capstone.bapool.model.Party;
import capstone.bapool.model.PartyHashtag;
import capstone.bapool.model.Restaurant;
import capstone.bapool.party.dto.*;
import capstone.bapool.config.error.BaseException;
import capstone.bapool.model.PartyParticipant;
import capstone.bapool.model.User;
import capstone.bapool.model.enumerate.PartyStatus;
import capstone.bapool.model.enumerate.RoleType;
import capstone.bapool.restaurant.RestaurantRepository;
import capstone.bapool.party.dto.GetAtePartyInfoRes;
import capstone.bapool.user.UserRepository;
import capstone.bapool.user.dto.GetUserRatingVeiwRes;
import capstone.bapool.user.dto.RatingUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static capstone.bapool.config.error.StatusEnum.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PartyService {

    private final RestaurantRepository restaurantRepository;
    private final PartyRepository partyRepository;
    private final PartyParticipantRepository partyParticipantRepository;
    private final PartyHashtagRepository partyHashtagRepository;
    private final FireBasePartyRepository fireBasePartyRepository;
    private final UserRepository userRepository;


    @Transactional(readOnly = false)
    public Long save(PostPartyReq postPartyReq, Long userId) {
        Restaurant restaurant = saveOrGetRestaurant(postPartyReq);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));

        Party party = Party.builder()
                .name(postPartyReq.getPartyName())
                .restaurant(restaurant)
                .startDate(postPartyReq.getStartDate())
                .menu(postPartyReq.getMenu())
                .maxPeople(postPartyReq.getMaxPeople())
                .partyStatus(PartyStatus.RECRUITING)
                .detail(postPartyReq.getDetail())
                .build();
        Party savedParty = partyRepository.save(party);

        for (Integer value : postPartyReq.getHashtag()) {
            PartyHashtag partyHashtag = PartyHashtag.create(savedParty, value);
            partyHashtagRepository.save(partyHashtag);
        }
        PartyParticipant partyParticipant = PartyParticipant.makeMapping(user, savedParty, RoleType.LEADER);
        partyParticipantRepository.save(partyParticipant);

        fireBasePartyRepository.save(new FireBasePartyInfo(postPartyReq, user.getId()), user.getId(), party.getId());
        return savedParty.getId();
    }

    private Restaurant saveOrGetRestaurant(PostPartyReq postPartyReq) {
        Optional<Restaurant> optionalRestaurant = restaurantRepository
                .findById(postPartyReq.getPostPartyRestaurantReq().getRestaurantId());
        if (!optionalRestaurant.isEmpty()) {
            return optionalRestaurant.get();
        }

        PostPartyRestaurantReq restaurantInfo = postPartyReq.getPostPartyRestaurantReq();

        Restaurant restaurant = Restaurant.builder()
                .id(restaurantInfo.getRestaurantId())
                .name(restaurantInfo.getName())
                .address(restaurantInfo.getAddress())
                .imgUrl(restaurantInfo.getImgUrl())
                .siteUrl(restaurantInfo.getSiteUrl())
                .category(restaurantInfo.getCategory())
                .phone(restaurantInfo.getPhone())
                .build();
        return restaurantRepository.save(restaurant);
    }

    /**
     * 식당안의 파티리스트 조회
     * @param userId
     * @param restaurantId 식당id
     * @return
     */
    public PartiesInRestaurantRes findPartiesInRestaurant(Long userId, Long restaurantId){

        PartiesInRestaurantRes partiesInRestaurantRes = new PartiesInRestaurantRes();

        // 식당 조회
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElse(null);
        if(restaurant == null){
            log.info("식당안의 파티리스트 조회: 식당 {}이 db에 없음", restaurantId);
            return partiesInRestaurantRes;
        }

        // 식당 이름 입력.
        partiesInRestaurantRes.setRestaurantName(restaurant.getName());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));

        // 해당 식당안의 파티리스트 조회
        List<Party> parties = partyRepository.findByRestaurantAndStartDateAfter(restaurant, LocalDateTime.now());
        for(Party party : parties){ // 파티 정보 입력.

            PartyInfo partyInfo = PartyInfo.builder()
                    .partyId(party.getId())
                    .partyName(party.getName())
                    .isParticipate(party.isMeParticipate(user))
                    .menu(party.getMenu())
                    .detail(party.getDetail())
                    .hasBlockUser(party.hasBlockUser(user))
                    .participants(party.getCurPartyMember())
                    .maxPeople(party.getMaxPeople())
                    .startDate(party.getStartDate())
                    .userRating(party.getPartyParticipantAvgRating()) // 다시 입력해줘야함!!
                    .partyHashtag(party.getPartyHashtag())
                    .isRecruiting(party.is_recruiting())
                    .build();
            partiesInRestaurantRes.addPartyInfos(partyInfo);
        }

        return partiesInRestaurantRes;
    }

    @Transactional(readOnly = false)
    public void update(PatchPartyReq patchPartyReq) {
        Party party = partyRepository.findById(patchPartyReq.getPartyId())
                .orElseThrow(() -> new BaseException(NOT_FOUND_PARTY_FAILURE));

        party.removeHashtags();
        for (Integer value : patchPartyReq.getHashtag()) {
            PartyHashtag partyHashtag = PartyHashtag.create(party, value);
            partyHashtagRepository.save(partyHashtag);
        }

        party.update(patchPartyReq.getPartyName(), patchPartyReq.getMaxPeople(),
                patchPartyReq.getStartDate(),
                patchPartyReq.getMenu(), patchPartyReq.getDetail());
        fireBasePartyRepository.update(new FireBasePartyInfo(patchPartyReq) , patchPartyReq.getPartyId());
    }


    @Transactional(readOnly = false)
    public void delete(Long userId, Long partyId) {
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_PARTY_FAILURE));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));
        PartyParticipant partyParticipant = partyParticipantRepository.findPartyParticipantByUserAndParty(user, party)
                .orElseThrow((() -> new BaseException(NOT_FOUND_PARTY_PARTICIPANT_FAILURE)));

        if (partyParticipant.isLeader() && party.isNotLastMember()) {
            throw new BaseException(IS_PARTY_LEADER);
        }

        if (party.mustRemoved()) {
            validateExists(party, user);
            partyRepository.delete(party);
            fireBasePartyRepository.delete(partyId);
            return;
        }

        int curPartyMember = party.getCurPartyMember();
        fireBasePartyRepository.secession(partyId, userId, curPartyMember);
        // DONE 인상태에서는
        if (party.isDone()) {
            partyParticipant.goOut();
            return;
        }

        // DONE 이 아닐때
        partyParticipantRepository.delete(partyParticipant);
    }

    private void validateExists(Party party, User user) {
        if (!partyParticipantRepository.existsByUserAndParty(user, party)) {
            throw new BaseException(NOT_FOUND_PARTY_PARTICIPANT_FAILURE);
        }
    }

    @Transactional(readOnly = false)
    public void close(Long partyId) {
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_PARTY_FAILURE));
        party.close();
        fireBasePartyRepository.closeParty(partyId);
    }

    @Transactional(readOnly = false)
    public void participate(Long userId, Long partyId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_PARTY_FAILURE));
        int curNumberOfPeople = party.getCurPartyMember();
        PartyParticipant partyParticipant = PartyParticipant.makeMapping(user, party, RoleType.MEMBER);
        partyParticipantRepository.save(partyParticipant);
        fireBasePartyRepository.participate(partyId, userId, curNumberOfPeople);
    }

    @Transactional(readOnly = false)
    public void changeLeader(Long userId, Long partyId, Long otherUserId) {
        Party partyReference = partyRepository.getReferenceById(partyId);
        User userReference = userRepository.getReferenceById(userId);
        User otherUserReference = userRepository.getReferenceById(otherUserId);

        PartyParticipant partyParticipant = partyParticipantRepository.findPartyParticipantByUserAndParty(userReference, partyReference)
                .orElseThrow(() -> new BaseException(NOT_FOUND_PARTY_PARTICIPANT_FAILURE));
        partyParticipant.becomeMember();
        fireBasePartyRepository.becomeMember(partyId, userId);

        PartyParticipant newLeader = partyParticipantRepository.findPartyParticipantByUserAndParty(otherUserReference, partyReference)
                .orElseThrow(() -> new BaseException(NOT_FOUND_PARTY_PARTICIPANT_FAILURE));
        newLeader.becomeLeader();
        fireBasePartyRepository.becomeLeader(partyId, otherUserId);
    }

    /**
     * 먹었던 파티정보 리스트 조회
     */
    public GetAtePartyInfoRes findAtePartyInfo(Long userId){

        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.error("먹었던 파티정보 리스트 조회: 존재하지 않는 유저입니다. userId-{}", userId);
            throw new BaseException(NOT_FOUND_USER_FAILURE);
        });

        List<AtePartyInfo> atePartyInfoList = partyRepository.findByUserAndPartyStatus(user, PartyStatus.DONE);

        return new GetAtePartyInfoRes(atePartyInfoList);
    }


    /**
     * 사용자 평가화면 정보 조회
     * @exception BaseException NOT_FOUND_USER_FAILURE, NOT_FOUND_PARTY_FAILURE, NOT_PARTY_PARTICIPANT
     */
    public GetUserRatingVeiwRes findPartyParticipantForRating(Long userId, Long partyId){

        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.error("사용자 평가화면 정보 조회: 존재하지 않는 유저입니다. userId-{}", userId);
            throw new BaseException(NOT_FOUND_USER_FAILURE);
        });

        Party party = partyRepository.findById(partyId).orElseThrow(() -> {
            log.error("사용자 평가화면 정보 조회: 존재하지 않는 파티입니다. partyId-{}", partyId);
            throw new BaseException(NOT_FOUND_PARTY_FAILURE);
        });

        // 해당 파티에 내가 참여하지 않았으면
        if(!party.isMeParticipate(user)){
            log.error("사용자 평가화면 정보 조회: 해당 사용자가 참여하지 않은 파티입니다, userId-{}, partyId-{}", userId, partyId);
            throw new BaseException(NOT_PARTY_PARTICIPANT);
        }

        List<RatingUserInfo> ratingUserInfoList = new ArrayList<>();
        for(PartyParticipant partyParticipant: party.getPartyParticipants()){
            User ratingUser = partyParticipant.getUser();
            if(ratingUser.equals(user)){ // 자기 자신은 평가에서 제외
                continue;
            }
            ratingUserInfoList.add(new RatingUserInfo(ratingUser.getId(), ratingUser.getName()));
        }

        return new GetUserRatingVeiwRes(ratingUserInfoList);
    }

    @Transactional(readOnly = false)
    public void changePartyDone(Long userId, Long partyId) {
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_PARTY_FAILURE));
        party.done();
        fireBasePartyRepository.doneParty(partyId);
    }
}
