package capstone.bapool.party.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PartyInfoSimple {

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

    @Builder
    public PartyInfoSimple(Long partyId, String partyName, String restaurantName, String restaurantImgURL, String restaurantAddress, String category) {
        this.partyId = partyId;
        this.partyName = partyName;
        this.restaurantName = restaurantName;
        this.restaurantImgURL = restaurantImgURL;
        this.restaurantAddress = restaurantAddress;
        this.category = category;
    }
}
