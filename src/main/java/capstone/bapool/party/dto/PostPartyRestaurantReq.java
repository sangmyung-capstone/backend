package capstone.bapool.party.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostPartyRestaurantReq {
    @JsonProperty(value = "restaurant_id")
    @NotNull
    private Long restaurantId;

    @NotBlank
    private String name;

    @NotBlank
    private String address;

    @NotBlank
    @JsonProperty("img_url")
    private String imgUrl;

    @NotBlank
    @JsonProperty("site_url")
    private String siteUrl;

    @NotBlank
    private String category;

    @NotBlank
    private String phone;
}
