package capstone.bapool.restaurant.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@Getter
@NoArgsConstructor
@JsonPropertyOrder({"restaurantId", "restaurantName", "restaurantAddress", "category", "numOfParty", "restaurantLongitude", "restaurantLatitude"})
public class RestaurantInfo {

    @JsonProperty("restaurant_id")
    private Long restaurantId;

    @JsonProperty("restaurant_name")
    private String restaurantName;

    @JsonProperty("restaurant_address")
    private String restaurantAddress;

    private String category;

    @JsonProperty("num_of_party")
    private int numOfParty;

    @JsonProperty("restaurant_longitude")
    private double restaurantLongitude; //x

    @JsonProperty("restaurant_latitude")
    private double restaurantLatitude; //y


    @Builder
    public RestaurantInfo(Long restaurantId, String restaurantName, String restaurantAddress, String category, int numOfParty, double restaurantLongitude, double restaurantLatitude) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.category = category;
        this.numOfParty = numOfParty;
        this.restaurantLongitude = restaurantLongitude;
        this.restaurantLatitude = restaurantLatitude;
    }

    @Override
    public String toString(){
        return "id="+ restaurantId +"\nname=" + restaurantName
                +"\naddress=" + restaurantAddress + "\ncategory=" + category
                + "\nnumOfParty=" + numOfParty
                +"\nlong=" + restaurantLongitude + "\nlat=" + restaurantLatitude;
    }
}
