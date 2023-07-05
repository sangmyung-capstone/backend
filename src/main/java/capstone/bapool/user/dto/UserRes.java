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
//
//    "nickname": "최부장0923",
//            "profileImg": 2,
//            "rating": 4.2,
//            "hashtag": [
//            3
//            ]
}
