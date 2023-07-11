package capstone.bapool.user.dto;

import capstone.bapool.model.User;
import capstone.bapool.model.UserHashtag;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;

@AllArgsConstructor
@Builder
@JsonPropertyOrder({"userId", "profileImg", "name", "rating", "is_block"})
public class OtherUserRes {

    @JsonProperty("user_id")
    Long userId;

    @JsonProperty("profileImg")
    Integer profileImg;

    @JsonProperty("nickname")
    String name;

    @JsonProperty("is_block")
    boolean is_block;

    @JsonProperty("rating")
    double rating;

//    @JsonProperty("hashtag")
//    ArrayList<UserHashtag> userHashtags;
//    여기는 hashtag구하는 메소드 만들고 구현해야지
}
