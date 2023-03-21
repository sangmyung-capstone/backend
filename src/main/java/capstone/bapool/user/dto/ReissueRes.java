package capstone.bapool.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class ReissueRes {
    private Long userId;
    private String accessToken;
    private String refreshToken;
}
