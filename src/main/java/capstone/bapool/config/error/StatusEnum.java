package capstone.bapool.config.error;

import lombok.Getter;

@Getter
public enum StatusEnum {
    /**
     * 200 : 요청 성공
     */
    SUCCESS(200, "OK"),

    /**
     * 인증 오류
     * 401 오류
     */

    SOCIAL_LOGIN_FAILURE(401, "소셜 로그인에 실패하였습니다."),
    EMPTY_ACCESS_KEY(401, "EMPTY ACCESS TOKEN"),
    INVALID_TOKEN(401, "INVALID TOKEN"),
    EXPIRATION_TOKEN(401, "EXPIRATION TOKEN"),

    /**
     * NOT FOUND 오류
     * 404 오류
     */
    NOT_FOUND_USER_FAILURE( 404 , "USER NOT FOUND"),
    NOT_FOUND_RESTAURANT_FAILURE(404, "RESTAURANT NOT FOUND"),
    NOT_FOUND_PARTY_FAILURE(404, "PARTY NOT FOUND"),
    NOT_FOUND_PARTY_PARTICIPANT_FAILURE(404, "파티에 참여해있지 않은 참가자입니다."),

    /**
     * 통상 오류
     * 500 번대 오류
     */
    INTERNET_SERVER_ERROR( 500, "INTERNET SERVER ERROR");

    private final int code;
    private final String message;

    private StatusEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
