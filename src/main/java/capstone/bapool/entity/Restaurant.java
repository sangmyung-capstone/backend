package capstone.bapool.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Restaurant {

    @Id
    @Column(name = "restaurant_id")
    private Long id;

    private String name;

    private String address;

    private String imgUrl;

    private String siteUrl;

    private String category;

    private String phone;

    @Builder
    public Restaurant(Long id, String name, String address, String imgUrl, String siteUrl, String category, String phone) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.imgUrl = imgUrl;
        this.siteUrl = siteUrl;
        this.category = category;
        this.phone = phone;
    }
}
