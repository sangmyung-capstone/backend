package capstone.bapool.party.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

//파티정보
@Getter
@JsonPropertyOrder({"partyId", "partyName", "isParticipate", "menu", "detail", "hasBlockUser", "participants", "maxPeople", "startDate", "endDate", "rating", "hashtag"})
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

    private List<Integer> hashtag;

    private String detail;

    @JsonProperty(value = "has_block_user")
    private boolean hasBlockUser;

    private List<Double> rating;

    @Builder
    public PartyInfo(Long partyId, String partyName, boolean isParticipate, int participants, int maxPeople, LocalDateTime startDate, LocalDateTime endDate, String menu, List<Integer> hashtag, String detail, boolean hasBlockUser, List<Double> rating) {
        this.partyId = partyId;
        this.partyName = partyName;
        this.isParticipate = isParticipate;
        this.participants = participants;
        this.maxPeople = maxPeople;
        this.startDate = startDate;
        this.endDate = endDate;
        this.menu = menu;
        this.hashtag = hashtag;
        this.detail = detail;
        this.hasBlockUser = hasBlockUser;
        this.rating = rating;
    }
}
