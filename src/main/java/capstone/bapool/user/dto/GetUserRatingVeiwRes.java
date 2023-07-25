package capstone.bapool.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;


@AllArgsConstructor
public class GetUserRatingVeiwRes {

    @JsonProperty("users")
    List<RatingUserInfo> ratingUserInfoList;
}
