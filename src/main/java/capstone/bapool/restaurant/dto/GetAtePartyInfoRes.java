package capstone.bapool.restaurant.dto;

import capstone.bapool.party.dto.PartyInfoSimple;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetAtePartyInfoRes {

    private List<PartyInfoSimple> partyInfoList;
}
