package capstone.bapool.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends BaseTimeEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, length = 20, unique = true)
    private String name;

    @Column(nullable = false, length = 50, unique = true)
    private String email;

    @Column(name="profile_img_id", nullable = false, columnDefinition = "smallint(6)")
    private Integer profileImgId;

    @Column(name="refresh_token")
    private String refreshToken;

    @OneToMany(mappedBy = "user")
    private List<PartyParticipant> partyParticipants = new ArrayList<PartyParticipant>();

    // 차단한 유저
    @OneToMany(mappedBy = "blockUser", cascade = CascadeType.REMOVE)
    private List<BlockUser> blockUsers = new ArrayList<>();

    // 유저 평점
    @OneToMany(mappedBy = "evaluatedUser", cascade = CascadeType.REMOVE)
    private List<UserRating> userRatings = new ArrayList<>();

    @OneToMany(mappedBy = "evaluatedUser", cascade = CascadeType.REMOVE)
    private List<UserHashtag> userHashtags = new ArrayList<>();

    @Builder
    public User(Long id, String name, String email, Integer profileImgId, String refreshToken) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.profileImgId = profileImgId;
        this.refreshToken = refreshToken;
    }


    // 변경 감지
    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    // 유저 평점 구하기
    public double getRating(){
        if(userRatings.size() == 0){ // 아무런 평점이 없다면 기본으로 4.5 설정.
            return 4.5;
        }

        double sum = 0;
        for(UserRating userRating : userRatings){
            sum += userRating.getRating();
        }

        return sum / userRatings.size();
    }

    public void update(String newName, int newProfileImg){
        this.name = newName;
        this.profileImgId = newProfileImg;
    }
}
