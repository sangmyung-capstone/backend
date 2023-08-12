package capstone.bapool.user.dto;

import capstone.bapool.model.UserHashtag;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;


@JsonPropertyOrder({"userId", "profileImg", "name", "rating", "hashtag", "is_block"})
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

    @JsonProperty("hashtag")
    List<UserHashtagInfo> hashtag;

    @Builder
    public OtherUserRes(Long userId, Integer profileImg, String name, boolean is_block, double rating, List<UserHashtagInfo> hashtag) {
        this.userId = userId;
        this.profileImg = profileImg;
        this.name = name;
        this.is_block = is_block;
        this.rating = rating;
        this.hashtag = hashtag;
    }
}
