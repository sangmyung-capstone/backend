package capstone.bapool.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserHashtagInfo {
    @JsonProperty("hashtag_id")
    Integer hashtagId;

    @JsonProperty("count")
    Long countHashtag;

    @Builder
    public UserHashtagInfo(int hashtagId, Long count){
        this.hashtagId = hashtagId;
        this.countHashtag = count;
    }
}
