package capstone.bapool.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@JsonPropertyOrder({"user_id", "nickname", "block_date"})
public class BlockUserListRes {

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("nickname")
    private String name;

    @JsonProperty("blcok_date")
    private LocalDateTime blockDate;

    @Builder
    public BlockUserListRes(Long userId, String name, LocalDateTime blockDate){
        this.userId = userId;
        this.name = name;
        this.blockDate = blockDate;
    }
}
