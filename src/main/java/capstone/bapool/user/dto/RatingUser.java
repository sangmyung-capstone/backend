package capstone.bapool.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RatingUser {

    @JsonProperty("user_id")
    private Long userId;

    private Double rating;

    private List<Integer> hashtag;
}
