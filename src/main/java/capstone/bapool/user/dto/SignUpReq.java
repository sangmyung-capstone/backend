package capstone.bapool.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpReq {

    @JsonProperty("access_token")
    @NotBlank
    private String accessToken;

    @JsonProperty("profile_img_id")
    @NotNull
    private Integer profileImgId;


    @JsonProperty("nickname")
    @NotBlank
    private String nickName;

    @JsonProperty("firebase_token")
    private String fireBaseToken;
}
