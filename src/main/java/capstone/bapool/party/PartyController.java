package capstone.bapool.party;


import capstone.bapool.config.response.ResponseDto;
import capstone.bapool.party.dto.PartiesInRestaurantRes;
import capstone.bapool.party.dto.PatchPartyReq;
import capstone.bapool.party.dto.PostPartyParticipantReq;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import capstone.bapool.party.dto.PostPartyReq;
import capstone.bapool.party.dto.PostPartyRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(path = {"/parties", "/test/parties"})
@RequiredArgsConstructor
public class PartyController {

    private final PartyService partyService;


    /**
     * [GET] /parties/{user-id}/{restaurant-id}
     * 식당안의 파티리스트 조회
     * @param userId
     * @param restaurantId
     * @return
     */
    @GetMapping("/{user-id}/{restaurant-id}")
    public ResponseEntity<ResponseDto> partyInRestaurantList(@PathVariable("user-id")Long userId, @PathVariable("restaurant-id")Long restaurantId) {

        PartiesInRestaurantRes partiesInRestaurantRes = partyService.findPartiesInRestaurant(userId, restaurantId);

        System.out.println("partiesInRestaurantRes = " + partiesInRestaurantRes.getPartyInfos().size());

        ResponseDto<PartiesInRestaurantRes> response = ResponseDto.create(partiesInRestaurantRes);

        log.info("식당 '{}-{}'의 파티리스트 조회 요청", partiesInRestaurantRes.getRestaurantName(), restaurantId);

        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/{user-id}")
    public ResponseEntity<ResponseDto> create(
            @PathVariable("user-id") Long userId,
            @Valid @RequestBody PostPartyReq postPartyReq
    ) {
        Long partyId = partyService.save(postPartyReq, userId);
        return ResponseEntity.ok(ResponseDto.create(new PostPartyRes(partyId)));
    }

    @PatchMapping(value = "/{user-id}")
    public ResponseEntity<ResponseDto> update(
            @PathVariable("user-id") Long userId,
            @Valid @RequestBody PatchPartyReq patchPartyReq
    ) {
        partyService.update(patchPartyReq);
        return ResponseEntity.ok(ResponseDto.create(null));
    }

    @DeleteMapping(value = "/{user-id}/{party-id}")
    public ResponseEntity<ResponseDto> delete(
            @PathVariable("user-id") Long userId,
            @PathVariable("party-id") Long partyId
    ) {
        partyService.delete(userId, partyId);
        return ResponseEntity.ok(ResponseDto.create(null));
    }

    @PatchMapping(value = "/close/{user-id}/{party-id}")
    public ResponseEntity<ResponseDto> close(
            @PathVariable("user-id") Long userId,
            @PathVariable("party-id") Long partyId
    ) {
        partyService.close(partyId);
        return ResponseEntity.ok(ResponseDto.create(null));
    }

    @PostMapping("/participant/{user-id}")
    public ResponseEntity<Object> participate(
            @PathVariable("user-id") Long userId,
            @Valid @RequestBody PostPartyParticipantReq postPartyParticipantReq) {
        partyService.participate(userId, postPartyParticipantReq.getPartyId());
        return ResponseEntity.ok(ResponseDto.create(null));
    }
}
