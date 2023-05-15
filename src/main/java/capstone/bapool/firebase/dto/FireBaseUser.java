package capstone.bapool.firebase.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FireBaseUser {
    private Long userId;
    private String name;
    private Integer imgUrl;
}
