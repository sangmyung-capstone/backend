package capstone.bapool.config.error;

import lombok.Getter;

@Getter
public enum StatusEnum {
    /**
     * 200 : 요청 성공
     */
    SUCCESS(200, "OK"),

    DUPLICATE_NICKNAME(300, "중복된 닉네임이 있습니다."),

    /**
     * 중복 오류
     * 300 오류
     */
    ALREADY_EXIST_NAME_FAILURE(300, "이미 존재하는 이름입니다."),

    /**
     * 인증 오류
     * 401 오류
     */

    SOCIAL_LOGIN_FAILURE(401, "소셜 로그인에 실패하였습니다."),
    EMPTY_ACCESS_KEY(401, "EMPTY ACCESS TOKEN"),
    INVALID_TOKEN(401, "INVALID TOKEN"),
    EXPIRATION_TOKEN(401, "EXPIRATION TOKEN"),

    /**
     * 잘못 권리 에러
     * 403 오류
     */
    PARTY_STATUS_IS_NOT_RECRUITING(403,"파티가 모집중인 상태가 아닙니다."),
    PARTY_IS_FULL(403,"더 이상 파티에 들어 갈 수 없습니다."),
    NOT_PARTY_LEADER_(403,"파티 리더가 아닙니다."),
    NOT_PARTY_PARTICIPANT(403, "해당 파티의 참여자가 아닙니다."),


    /**
     * NOT FOUND 오류
     * 404 오류
     */
    NOT_FOUND_USER_FAILURE( 404 , "USER NOT FOUND"),
    NOT_FOUND_RESTAURANT_FAILURE(404, "RESTAURANT NOT FOUND"),
    NOT_FOUND_PARTY_FAILURE(404, "PARTY NOT FOUND"),
    NOT_FOUND_PARTY_PARTICIPANT_FAILURE(404, "파티에 참여해있지 않은 참가자입니다."),

    /**
     * Too Many Request 너무 많은 요청 오류
     * 429 오류
     */
    TOO_MANY_REQUEST(429, "TOO MANY REQUEST"),

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
