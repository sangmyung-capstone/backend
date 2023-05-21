package capstone.bapool.party.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PostPartyRes {
    @JsonProperty(value = "party_id")
    private Long partyId;
}
