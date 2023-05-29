package capstone.bapool.party.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostPartyParticipantReq {
    @JsonProperty("party_id")
    @NotNull
    private Long partyId;
}
