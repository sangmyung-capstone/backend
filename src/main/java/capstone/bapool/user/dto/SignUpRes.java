package capstone.bapool.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class SignUpRes {
    private String accessToken;
    private String refreshToken;
}
