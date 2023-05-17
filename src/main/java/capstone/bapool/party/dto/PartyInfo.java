package capstone.bapool.party.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

//파티정보
@Getter
@JsonPropertyOrder({"partyId", "partyName", "menu", "detail", "hasBlockUser", "participants", "maxPeople", "startDate", "endDate", "rating", "hashtag"})
public class PartyInfo {

    @JsonProperty(value = "party_id")
    private Long partyId;

    @JsonProperty(value = "party_name")
    private String partyName;

    private int participants;

    @JsonProperty(value = "max_people")
    private int maxPeople;

    @JsonProperty(value = "start_date")
    private LocalDateTime startDate;

    @JsonProperty(value = "end_date")
    private LocalDateTime endDate;

    private String menu;

    private List<Integer> hastag;

    private String detail;

    @JsonProperty(value = "has_block_user")
    private boolean hasBlockUser;

    private List<Integer> rating;

    @Builder
    public PartyInfo(Long partyId, String partyName, int participants, int maxPeople, LocalDateTime startDate, LocalDateTime endDate, String menu, List<Integer> hastag, String detail, boolean hasBlockUser, List<Integer> rating) {
        this.partyId = partyId;
        this.partyName = partyName;
        this.participants = participants;
        this.maxPeople = maxPeople;
        this.startDate = startDate;
        this.endDate = endDate;
        this.menu = menu;
        this.hastag = hastag;
        this.detail = detail;
        this.hasBlockUser = hasBlockUser;
        this.rating = rating;
    }
}
