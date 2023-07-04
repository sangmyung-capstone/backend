package capstone.bapool.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class SignInRes {
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("nickname")
    private String nickname;

    public SignInRes(ReissueRes reissueRes, String nickname) {
        this.userId = reissueRes.getUserId();
        this.accessToken = reissueRes.getAccessToken();
        this.refreshToken = reissueRes.getRefreshToken();
        this.nickname = nickname;
    }
}
