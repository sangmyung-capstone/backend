package capstone.bapool.restaurant.dto;

import lombok.*;

@Getter
@NoArgsConstructor
public class RestaurantInfo2 {
    private Long restaurant_id;
    private String restaurant_name;
    private String restaurant_address;
    private String category;
    private int num_of_party;
    private double restaurant_longitude;
    private double restaurant_latitude;

    @Builder
    public RestaurantInfo2(Long restaurant_id, String restaurant_name, String restaurant_address, String category, int num_of_party, double restaurant_longitude, double restaurant_latitude) {
        this.restaurant_id = restaurant_id;
        this.restaurant_name = restaurant_name;
        this.restaurant_address = restaurant_address;
        this.category = category;
        this.num_of_party = num_of_party;
        this.restaurant_longitude = restaurant_longitude;
        this.restaurant_latitude = restaurant_latitude;
    }

    @Override
    public String toString(){
        return "id="+ restaurant_id +"\nname=" + restaurant_name
                +"\naddress=" + restaurant_address + "\ncategory=" + category
                + "\nnumOfParty=" + num_of_party
                +"\nlong=" +restaurant_longitude + "\nlat=" + restaurant_latitude;
    }
}
