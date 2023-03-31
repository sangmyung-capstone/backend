package capstone.bapool.config.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class BaseException extends RuntimeException {
    private final StatusEnum statusEnum;
}
