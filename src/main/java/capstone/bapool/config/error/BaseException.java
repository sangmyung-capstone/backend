package capstone.bapool.config.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class BaseException extends RuntimeException {
    private final StatusEnum statusEnum;

    public BaseException(StatusEnum statusEnum){
        super(statusEnum.getMessage() + "-" + statusEnum.getCode());
        this.statusEnum = statusEnum;
    }
}
