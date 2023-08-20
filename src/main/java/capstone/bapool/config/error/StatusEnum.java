package capstone.bapool.config.error;

import lombok.Getter;

@Getter
public enum StatusEnum {
    /**
     * 200 : 요청 성공
     */
    SUCCESS(200, "OK"),

    /**
     * 중복 오류
     * 300 오류
     */
    ALREADY_EXIST_NAME_FAILURE(300, "이미 존재하는 이름입니다."),

    /**
     * 잘못된 요청
     * 400 오류
     */
    PARTY_NOT_DONE(400, "파티가 끝나지 않았습니다"),
    NOT_SUFFICIENT_NUM_OF_USER(400, "모든 유저에 대해 평가해 주세요."),
    OUT_OF_RATING_RANGE(400, "평점이 범위(0.0~5.0)를 벗어났습니다."),
    ALREADY_RATING_COMPLETE(400, "이미 유저평가를 완료했습니다."),
    CAN_NOT_RATING_MYSELF(400, "자기 자신에 대해선 평가할 수 없습니다."),

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
    IS_PARTY_LEADER(403, "파티 리더입니다."),
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
     * Payload Too Large 너무 많은 요청 오류
     * 413 오류
     */
    PAYLOAD_TOO_LARGE(413, "PAYLOAD TOO LARGE"),

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
