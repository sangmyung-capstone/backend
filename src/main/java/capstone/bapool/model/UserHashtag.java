package capstone.bapool.model;

import lombok.AccessLevel;
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
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "hashtag_id")
    private int hashtagId;

    private UserHashtag(User user, int hashtagId) {
        this.user = user;
        this.hashtagId = hashtagId;

        this.user.addUserHashtag(this);
    }

    public static UserHashtag create(User user, int hashtagId) {
        return new UserHashtag(user, hashtagId);
    }

}
