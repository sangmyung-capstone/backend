package capstone.bapool.utils.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class KakaoRestaurant {

    private Long id;
    private String name;
    private String address;
    private String category;
    private String phone;
    private String siteUrl;
    private double x; //x
    private double y; //y

    @Builder
    public KakaoRestaurant(String name, String address, String category, Long id, String phone, String siteUrl, double longitude, double latitude) {
        this.name = name;
        this.address = address;
        this.category = category;
        this.id = id;
        this.phone = phone;
        this.siteUrl = siteUrl;
        this.x = longitude;
        this.y = latitude;
    }
}
