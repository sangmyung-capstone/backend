package capstone.bapool.party;


import capstone.bapool.config.response.ResponseDto;
import capstone.bapool.party.dto.PartiesInRestaurantRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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

    // 식당안의 파티리스트 조회
    @GetMapping("/{user-id}/{restaurant-id}")
    public ResponseDto<PartiesInRestaurantRes> partyInRestaurantList(@PathVariable("user-id")Long userId, @PathVariable("restaurant-id")Long restaurantId) {

        PartiesInRestaurantRes partiesInRestaurantRes = partyService.findPartiesInRestaurant(userId, restaurantId);

        System.out.println("partiesInRestaurantRes = " + partiesInRestaurantRes.getPartyInfos().size());

        return ResponseDto.res(partiesInRestaurantRes);
    }

    @PostMapping(value = "/{user-id}")
    public ResponseEntity<PostPartyRes> create(
            @PathVariable("user-id") Long userId,
            @Valid @RequestBody PostPartyReq postPartyReq
    )
    {
        Long partyId = partyService.save(postPartyReq, userId);
        return ResponseEntity.ok(new PostPartyRes(partyId));
    }
}
