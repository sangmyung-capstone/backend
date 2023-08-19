package capstone.bapool.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostUserRatingReq {

    @Valid
    @NotNull(message = "users는 null이어선 안됩니다")
    @JsonProperty("users")
    private List<RatedUser> ratedUserList;
}
