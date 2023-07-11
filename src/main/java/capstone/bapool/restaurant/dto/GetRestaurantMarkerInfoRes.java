package capstone.bapool.restaurant.dto;

import capstone.bapool.utils.dto.Menu;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@JsonPropertyOrder({"restaurantId", "restaurantName", "restaurantX", "restaurantY", "restaurantAddress", "numOfParty", "category", "link", "phone", "imgUrl", "menu"})
public class GetRestaurantMarkerInfoRes {

    @JsonProperty("restaurant_id")
    private Long restaurantId;

    @JsonProperty("restaurant_name")
    private String restaurantName;

    @JsonProperty("restaurant_longitude")
    private double restaurantX;

    @JsonProperty("restaurant_latitude")
    private double restaurantY;

    @JsonProperty("restaurant_address")
    private String restaurantAddress;

    @JsonProperty("num_of_party")
    private int numOfParty;

    @JsonProperty("category")
    private String category;

    private String link;

    private String phone;

    @JsonProperty("img_url")
    private String imgUrl;

    private List<Menu> menu;

    @Builder
    public GetRestaurantMarkerInfoRes(Long restaurantId, String restaurantName, double restaurantX, double restaurantY, String restaurantAddress, int numOfParty, String category, String imgUrl, String link, String phone, List<Menu> menu) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.restaurantX = restaurantX;
        this.restaurantY = restaurantY;
        this.restaurantAddress = restaurantAddress;
        this.numOfParty = numOfParty;
        this.category = category;
        this.imgUrl = imgUrl;
        this.link = link;
        this.phone = phone;
        this.menu = menu;
    }
}
