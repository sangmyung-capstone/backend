package capstone.bapool.party.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

//파티정보
@Getter
@JsonPropertyOrder({"partyId", "partyName", "isParticipate", "menu", "detail", "hasBlockUser", "participants", "maxPeople", "startDate", "endDate", "userRating", "partyHashtag"})
public class PartyInfo {

    @JsonProperty(value = "party_id")
    private Long partyId;

    @JsonProperty(value = "party_name")
    private String partyName;

    @JsonProperty("is_participate")
    private boolean isParticipate; //내가 이 파티에 참여했는지 여부

    private int participants;

    @JsonProperty(value = "max_people")
    private int maxPeople;

    @JsonProperty(value = "start_date")
    private LocalDateTime startDate;

    @JsonProperty(value = "end_date")
    private LocalDateTime endDate;

    private String menu;

    @JsonProperty("party_hashtag")
    private List<Integer> partyHashtag;

    private String detail;

    @JsonProperty(value = "has_block_user")
    private boolean hasBlockUser;

    @JsonProperty("user_rating")
    private List<Double> userRating;

    @Builder
    public PartyInfo(Long partyId, String partyName, boolean isParticipate, int participants, int maxPeople, LocalDateTime startDate, LocalDateTime endDate, String menu, List<Integer> partyHashtag, String detail, boolean hasBlockUser, List<Double> userRating) {
        this.partyId = partyId;
        this.partyName = partyName;
        this.isParticipate = isParticipate;
        this.participants = participants;
        this.maxPeople = maxPeople;
        this.startDate = startDate;
        this.endDate = endDate;
        this.menu = menu;
        this.partyHashtag = partyHashtag;
        this.detail = detail;
        this.hasBlockUser = hasBlockUser;
        this.userRating = userRating;
    }
}
