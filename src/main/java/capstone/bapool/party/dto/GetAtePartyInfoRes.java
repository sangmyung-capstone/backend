package capstone.bapool.party.dto;

import capstone.bapool.party.dto.PartyInfoSimple;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetAtePartyInfoRes {

    @JsonProperty("parties")
    private List<PartyInfoSimple> partyInfoList;
}
