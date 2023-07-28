package capstone.bapool.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class BlockUserListRes {

    @JsonProperty("users")
    private List<BlockedUserInfo> blockedUserList;
}
