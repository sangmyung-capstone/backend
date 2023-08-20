package capstone.bapool.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_hashtag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserHashtag extends BaseTimeEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_hashtag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id")
    private Party party;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluate_user")
    private User evaluateUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluated_user")
    private User evaluatedUser;

    @Column(name = "hashtag_id")
    private int hashtagId;


    @Builder
    private UserHashtag(Party party, User evaluateUser, User evaluatedUser, int hashtagId) {
        this.party = party;
        this.evaluateUser = evaluateUser;
        this.evaluatedUser = evaluatedUser;
        this.hashtagId = hashtagId;

        this.evaluatedUser.getUserHashtags().add(this);
    }

    public static UserHashtag create(Party party, User evaluateUser, User evaluatedUser, int hashtagId) {
        return new UserHashtag(party, evaluateUser, evaluatedUser, hashtagId);
    }

}
