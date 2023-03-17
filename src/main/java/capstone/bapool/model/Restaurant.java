package capstone.bapool.model;

import javax.persistence.*;

@Entity
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="resto_id")
    private Long id;

    private String name;

    private String address;

    private String imgUrl;

    private String siteUrl;

    private String category;

    private String phone;
}
