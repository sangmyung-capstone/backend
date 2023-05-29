package capstone.bapool.config.response;

import capstone.bapool.config.error.StatusEnum;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ResponseDto<T> {
    private int code;
    private String message;
    private T result;

    @Builder
    public ResponseDto(int code, String message, T result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }


    public static <T> ResponseDto<T> res(T body) {
        return res(StatusEnum.SUCCESS.getCode(), StatusEnum.SUCCESS.getMessage(), body);
    }

    public static<T> ResponseDto<T> res(int code, String message, T result) {
        return ResponseDto.<T>builder()
                .code(code)
                .message(message)
                .result(result)
                .build();
    }
}
