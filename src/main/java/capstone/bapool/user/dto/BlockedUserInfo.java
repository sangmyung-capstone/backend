package capstone.bapool.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;

import java.time.LocalDateTime;
@JsonPropertyOrder({"user_id", "nickname", "block_date"})
public class BlockedUserInfo {

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("nickname")
    private String name;

    @JsonProperty("block_date")
    private LocalDateTime blockDate;

    @Builder
    public BlockedUserInfo(Long userId, String name, LocalDateTime blockDate){
        this.userId = userId;
        this.name = name;
        this.blockDate = blockDate;
    }
}
