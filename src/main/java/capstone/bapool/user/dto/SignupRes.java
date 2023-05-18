package capstone.bapool.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SignupRes {
    private String accessToken;
    private String refreshToken;
}
