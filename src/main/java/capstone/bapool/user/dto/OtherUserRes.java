package capstone.bapool.user.dto;

import capstone.bapool.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OtherUserRes {
    User user;
    boolean is_block;
}
