package capstone.bapool.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReissueReq {
    @JsonProperty("access_token")
    @NotBlank
    private String accessToken;

    @JsonProperty("refresh_token")
    @NotBlank
    private String refreshToken;
}