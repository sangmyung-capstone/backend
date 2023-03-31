package capstone.bapool.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
//@AllArgsConstructor
@Getter
public class TempResponse {

    private int code;

    private String message;

    private Object result;

    public TempResponse(Object result) {
        this.code = 200;
        this.message = "요청 성공";
        this.result = result;
    }
}
