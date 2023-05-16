package capstone.bapool.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User{
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

    @Column(name="created_at")
    private LocalDateTime atCreateTime;

    @Builder
    public User(Long id, String name, String email, Integer profileImgId, String refreshToken, LocalDateTime atCreateTime) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.profileImgId = profileImgId;
        this.refreshToken = refreshToken;
        this.atCreateTime = atCreateTime;
    }


    // 변경 감지
    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
