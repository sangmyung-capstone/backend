package capstone.bapool.model;


import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Getter
public class UserRating {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_rating_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluate_user")
    private User evaluateUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluated_user")
    private User evaluatedUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id")
    private Party party;

    @ColumnDefault("4.5")
    private Double rating;
}
