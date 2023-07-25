package capstone.bapool.user;

import capstone.bapool.config.response.ResponseDto;
import capstone.bapool.party.PartyService;
import capstone.bapool.user.dto.*;
import capstone.bapool.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = {"/users", "/test/users"})
public class UserController {

    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final PartyService partyService;


    //마이페이지 로딩
    @GetMapping("/mypage/{user-id}")
    public ResponseEntity<ResponseDto> mypage(@Valid @PathVariable("user-id") Long userId){
        UserRes user = userService.findById(userId);
        //dto를 만들고 entity반환
        return ResponseEntity.ok().body(ResponseDto.create(user));
    }

    //회원 탈퇴
    //프론트랑 상의해서 service에서 반환을 null로 해도괜찮은지 물어보기
    @DeleteMapping("delete/{user-id}")
    public ResponseEntity<ResponseDto> userRemove(@Valid @PathVariable("user-id") Long userId){
        ResponseDto deleteresult = userService.deleteById(userId);
        return ResponseEntity.ok().body(deleteresult);
    }

    @GetMapping("/profilewithno/{user-id}")
    public ResponseEntity<ResponseDto> otherUser(
            @PathVariable("user-id") Long otherUserId){
        Long userId = 2L;
        OtherUserRes otherUserRes = userService.findOtherById(userId, otherUserId);
        return ResponseEntity.ok().body(ResponseDto.create(otherUserRes));
    }

    @GetMapping("/profile/{user-id}")
    public ResponseEntity<ResponseDto> otherUser(
            @PathVariable("user-id") Long otherUserId, HttpServletRequest request){
        Long userId = jwtUtils.resolveRequest(request);
        OtherUserRes otherUserRes = userService.findOtherById(userId, otherUserId);
        return ResponseEntity.ok().body(ResponseDto.create(otherUserRes));
    }

    @PostMapping("/block/{user-id}")
    public ResponseEntity<ResponseDto> blockUserWithReqBody(
            @PathVariable("user-id") Long blockUserId, @Valid @RequestBody BlockUserReq blockUserReq){
        BlockUserRes blockUserRes = userService.blockWithReqBody(blockUserReq.getBlockedUserId(), blockUserId);
        return ResponseEntity.ok().body(ResponseDto.create(blockUserRes));
    }

    // 사용자 평가 화면을 띄우기 위한 정보 조회
    @GetMapping("/rating/{user-id}")
    public ResponseEntity<ResponseDto> userRatingViewDetails(@PathVariable("user-id")Long userId, @RequestParam("party_id") Long partyId) {

        GetUserRatingVeiwRes getUserRatingVeiwRes = partyService.findPartyParticipantForRating(userId, partyId);

        ResponseDto<GetUserRatingVeiwRes> response = ResponseDto.create(getUserRatingVeiwRes);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/block/{user-id}")
    public ResponseEntity<ResponseDto> blockUserList(@PathVariable("user-id") Long userId){
        List<BlockUserRes> blockList = userService.blockList(userId);
        return ResponseEntity.ok().body(ResponseDto.create(blockList));
    }
}
