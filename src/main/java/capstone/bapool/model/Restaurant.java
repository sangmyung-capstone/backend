package capstone.bapool.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="restaurant_id")
    private Long id;

    private String name;

    private String address;

    private String imgUrl;

    private String siteUrl;

    private String category;

    private String phone;

//    @OneToMany(mappedBy = "restaurant")
//    private List<Party> parties;
}
