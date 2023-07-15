package capstone.bapool.user.dto;

import capstone.bapool.model.UserHashtag;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRes {

    @JsonProperty("nickname")
    String name;

    @JsonProperty("profileImg")
    Integer profileImgId;

    @JsonProperty("rating")
    double rating;

    @JsonProperty("hashtag")
    List<UserHashtag> userHashtags;
}
