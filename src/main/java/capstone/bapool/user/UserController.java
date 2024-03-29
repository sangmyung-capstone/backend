package capstone.bapool.user;

import capstone.bapool.config.error.BaseException;
import capstone.bapool.config.response.ResponseDto;
import capstone.bapool.party.PartyService;
import capstone.bapool.user.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static capstone.bapool.config.error.StatusEnum.OUT_OF_RATING_RANGE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = {"/users", "/test/users"})
public class UserController {

    private final UserService userService;
    private final PartyService partyService;


    /**
     * [GET] /users/mypage/{user-id}
     * 마이페이지
     */
    @GetMapping("/mypage/{user-id}")
    public ResponseEntity<ResponseDto> mypage(@Valid @PathVariable("user-id") Long userId){
        UserRes user = userService.findById(userId);
        //dto를 만들고 entity반환
        return ResponseEntity.ok().body(ResponseDto.create(user));
    }

    /**
     * [DELETE] /users/delete/{user-id}
     * 탈퇴하기
     */
    @DeleteMapping("delete/{user-id}")
    public ResponseEntity<ResponseDto> userRemove(@Valid @PathVariable("user-id") Long userId){
        ResponseDto deleteresult = userService.deleteById(userId);
        return ResponseEntity.ok().body(deleteresult);
    }

    /**
     * [GET] /users/profile/{user-id}/{other-user-id}
     * 타유저 조회하기
     */
    @GetMapping("/profile/{user-id}/{other-user-id}")
    public ResponseEntity<ResponseDto> otherUser(
            @PathVariable("user-id") Long userId,
            @PathVariable("other-user-id") Long otherUserId){
        OtherUserRes otherUserRes = userService.findOtherById(userId, otherUserId);
        return ResponseEntity.ok().body(ResponseDto.create(otherUserRes));
    }

    /**
     * [POST] /users/block/{user-id}
     * 유저 차단하기
     */
    @PostMapping("/block/{user-id}")
    public ResponseEntity<ResponseDto> blockUserWithReqBody(
            @PathVariable("user-id") Long blockUserId, @Valid @RequestBody BlockUserReq blockUserReq){
        BlockUserRes blockUserRes = userService.blockWithReqBody(blockUserReq.getBlockedUserId(), blockUserId);
        return ResponseEntity.ok().body(ResponseDto.create(blockUserRes));
    }

    /**
     * [GET] /users/rating/{user-id}?party_id=
     * 사용자 평가 화면을 띄우기 위한 정보 조회
     */
    @GetMapping("/rating/{user-id}")
    public ResponseEntity<ResponseDto> userRatingViewDetails(@PathVariable("user-id")Long userId, @RequestParam("party_id") Long partyId) {

        log.info("사용차 평가화면 정보 요청: userId={}, partyId={}", userId, partyId);

        GetUserRatingVeiwRes getUserRatingVeiwRes = partyService.findPartyParticipantForRating(userId, partyId);

        log.info("사용차 평가화면 정보 요청처리 완료: userId={}, partyId={}", userId, partyId);

        ResponseDto<GetUserRatingVeiwRes> response = ResponseDto.create(getUserRatingVeiwRes);

        return ResponseEntity.ok(response);
    }

    /**
     * [GET] /users/block/{user-id}
     * 차단리스트
     */
    @GetMapping("/block/{user-id}")
    public ResponseEntity<ResponseDto> blockUserList(@PathVariable("user-id") Long userId){
        BlockUserListRes blockList = userService.blockList(userId);
        return ResponseEntity.ok().body(ResponseDto.create(blockList));
    }

    /**
     * [PATCH] /users/info/{user-id}
     * 유저 정보 변경
     */
    @PatchMapping("/info/{user-id}")
    public ResponseEntity<ResponseDto> patchUserInfo(
            @PathVariable("user-id") Long userId, @Valid @RequestBody UserInfoReq userInfoReq){
        return ResponseEntity.ok().body(ResponseDto.create(userService.updateUserInfo(userId, userInfoReq)));
    }

    /**
     * [POST] /users/rating/{user-id}?party_id=
     * 유저 평가하기
     */
    @PostMapping("/rating/{user-id}")
    public ResponseEntity<ResponseDto> userRating(@PathVariable("user-id")Long userId, @RequestParam("party_id") Long partyId, @RequestBody @Validated PostUserRatingReq postUserRatingReq){

        log.info("유저 평가하기 요청: userId={}, partyId={}", userId, partyId);

        // 평점이 0.0~5.0 사이가 아닌 경우
        for(RatedUser user : postUserRatingReq.getRatedUserList()){
            if(user.getRating() < 0.0 || 5.0 < user.getRating()){
                throw new BaseException(OUT_OF_RATING_RANGE);
            }
        }

        userService.userRating(userId, partyId, postUserRatingReq);

        log.info("유저 평가하기 요청처리 완료: userId={}, partyId={}", userId, partyId);

        ResponseDto response = ResponseDto.create(null);
        return ResponseEntity.ok(response);
    }
}
