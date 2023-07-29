package capstone.bapool.model;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRating extends BaseTimeEntity{

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

    public UserRating(User user, User evaluatedUser, Party party, Double rating){
        this.evaluateUser = user;
        this.evaluatedUser = evaluatedUser;
        this.party = party;
        this.rating = rating;

        this.evaluateUser.addUserRating(this);
    }
}
