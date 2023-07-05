package capstone.bapool.user.dto;

import capstone.bapool.model.enumerate.BlockStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlockUserRes {

    @JsonProperty("user_id")
    Long userId;

    @JsonProperty("block_status")
    BlockStatus blockStatus;

    @JsonProperty("nickname")
    String name;

    @JsonProperty("block_date")
    LocalDateTime blockDate;

    public BlockUserRes(BlockStatus blockStatus){
        this.blockStatus = blockStatus;
    }

//  이거는 차단 response
//    "user_id": 15,
//            "block_status": 15,
//            "nickname": "김차장",
//            "block_date": "2017-07-21T17:32:28Z"
}
