package capstone.bapool.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class SignUpReq {
    private String accessToken;
    private String refreshToken;
}