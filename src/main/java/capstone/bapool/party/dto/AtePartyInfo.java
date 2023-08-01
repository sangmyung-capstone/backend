package capstone.bapool.party.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AtePartyInfo {

    @JsonProperty("party_id")
    private Long partyId;

    @JsonProperty("party_name")
    private String partyName;

    @JsonProperty("restaurant_name")
    private String restaurantName;

    @JsonProperty("restaurant_img_url")
    private String restaurantImgURL;

    @JsonProperty("restaurant_address")
    private String restaurantAddress;

    @JsonProperty("category")
    private String category;

    @JsonProperty("rating_complete")
    private Boolean ratingComplete;

    @Builder
    public AtePartyInfo(Long partyId, String partyName, String restaurantName, String restaurantImgURL, String restaurantAddress, String category, Boolean ratingComplete) {
        this.partyId = partyId;
        this.partyName = partyName;
        this.restaurantName = restaurantName;
        this.restaurantImgURL = restaurantImgURL;
        this.restaurantAddress = restaurantAddress;
        this.category = category;
        this.ratingComplete = ratingComplete;
    }
}
