package capstone.bapool.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RatingUserInfo {

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("nickname")
    private String name;
}
