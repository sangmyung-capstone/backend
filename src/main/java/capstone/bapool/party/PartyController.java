package capstone.bapool.party;

import capstone.bapool.config.response.ResponseDto;
import capstone.bapool.party.dto.PartiesInRestaurantRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = {"/parties", "/test/parties"})
@RequiredArgsConstructor
public class PartyController {

    private final PartyService partyService;

    @GetMapping("/{user-id}/{restaurant-id}")
    public ResponseDto<PartiesInRestaurantRes> partyInRestaurantList(@PathVariable("user-id")Long userId, @PathVariable("restaurant-id")Long restaurantId){

        PartiesInRestaurantRes partiesInRestaurantRes = partyService.findPartiesInRestaurant(userId, restaurantId);

        System.out.println("partiesInRestaurantRes = " + partiesInRestaurantRes.getPartyInfos().size());

        return ResponseDto.res(partiesInRestaurantRes);
    }
}
