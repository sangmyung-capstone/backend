package capstone.bapool.party.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@AllArgsConstructor
public class PostPartyReq {
    @JsonProperty(value = "restaurant_id")
    @NotNull
    private Long restaurantId;

    @JsonProperty("party_name")
    @NotBlank
    private String partyName;

    @JsonProperty("max_people")
    @NotNull
    private Integer maxPeople;

    @JsonProperty("start_date")
    @NotNull
    private LocalDateTime startDate;

    @JsonProperty("end_date")
    @NotNull
    private LocalDateTime endDate;

    @NotBlank
    private String menu;

    @NotNull
    private String imgUrl;

    @NotNull
    Set<Integer> hashtag;

    private String detail;
}
