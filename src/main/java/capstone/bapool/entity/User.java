package capstone.bapool.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User{
    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, length = 20, unique = true)
    private String name;

    @Column(nullable = false, length = 50, unique = true)
    private String email;

    @Column(nullable = false, columnDefinition = "smallint(6)")
    private Integer profileImgId;

    @Column
    private String refreshToken;

    @Column
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
