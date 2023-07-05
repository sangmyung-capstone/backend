package capstone.bapool.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

//    @JsonProperty("hashtag")
//    ArrayList<UserHashtag> userHashtags;
//    여기는 hashtag구하는 메소드 만들고 구현해야지
}
