package capstone.bapool.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpReq {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("profile_img_id")
    private Integer profileImgId;
    @JsonProperty("nickname")
    private String nickName;
}
