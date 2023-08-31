package capstone.bapool.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoReq {
    @NotBlank(message = "공백이 아닌 이름을 사용해야 합니다")
    @JsonProperty("nickname")
    String name;

    @JsonProperty("profileImg")
    int profileImg;
}

