package capstone.bapool.config.error;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

import static org.springframework.http.HttpStatus.NON_AUTHORITATIVE_INFORMATION;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        StatusEnum errorStatus = StatusEnum.INTERNET_SERVER_ERROR;
        e.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(makeErrorResponse(errorStatus.getCode(), errorStatus.getMessage()));
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorResponse> handleIOException(BaseException e) {
        StatusEnum statusEnum = e.getStatusEnum();
        e.printStackTrace();
        return ResponseEntity
                .status(NON_AUTHORITATIVE_INFORMATION)
                .body(makeErrorResponse(statusEnum.getCode(), statusEnum.getMessage()));
    }

    private ErrorResponse makeErrorResponse(Integer code, String message) {
        return ErrorResponse.builder()
                .code(code)
                .message(message)
                .build();
    }

}
