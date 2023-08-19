package capstone.bapool.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RatedUser {

    @NotNull(message = "user_id가 null이어선 안됩니다")
    @JsonProperty("user_id")
    private Long userId;

    @NotNull(message = "rating이 null이어선 안됩니다")
    private Double rating;

    @NotNull(message = "hashtag가 null이어선 안됩니다")
    private List<Integer> hashtag;
}
