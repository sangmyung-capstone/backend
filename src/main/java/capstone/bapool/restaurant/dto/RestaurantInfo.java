package capstone.bapool.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantInfo {
    private Long restaurant_id;
    private String restaurant_name;
    private String restaurant_address;
    private String category;
    private String imgUrl;
    private int num_of_party;
    private double restaurant_longitude;
    private double restaurant_latitude;


}
