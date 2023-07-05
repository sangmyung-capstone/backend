package capstone.bapool.user.dto;

import capstone.bapool.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class OtherUserRes {

    @JsonProperty("user_id")
    Long userId;

    @JsonProperty("profileImg")
    Integer profileImg;

    @JsonProperty("nickname")
    String name;

    @JsonProperty("is_block")
    boolean is_block;
//    이거는 타유저 프로필
//    "user_id": 1,
//            "profileImg": 2,
//            "nickname": "최부장0923",
//            "rating": 4.3,
//            "hashtag": [
//            3
//            ],
//            "is_block": false
}
