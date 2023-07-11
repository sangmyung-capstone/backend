package capstone.bapool.user;

import capstone.bapool.config.error.BaseException;
import capstone.bapool.config.response.ResponseDto;
import capstone.bapool.user.dto.ReissueReq;
import capstone.bapool.user.dto.ReissueRes;
import capstone.bapool.user.dto.SignInRes;
import capstone.bapool.user.dto.SignUpReq;
import capstone.bapool.user.dto.SignupRes;
import capstone.bapool.user.dto.SocialAccessToken;

import capstone.bapool.user.dto.*;
import capstone.bapool.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final JwtUtils jwtUtils;

    public UserController(UserService userService, JwtUtils jwtUtils) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }
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

    @PostMapping("/profile/{user-id}")
    public ResponseEntity<ResponseDto> otherUser(
            @Valid @PathVariable("user-id") Long userId, @Valid @RequestBody OtherUserReq otherUserReq){
        OtherUserRes otherUserRes = userService.findOtherById(userId, otherUserReq.getOtherUserId());
        return ResponseEntity.ok().body(ResponseDto.create(otherUserRes));
    }

    @PostMapping("/block/{user-id}")
    public ResponseEntity<ResponseDto> blockUserWithReqBody(
            @Valid @PathVariable("user-id") Long blockUserId, @Valid @RequestBody BlockUserReq blockUserReq){
        BlockUserRes blockUserRes = userService.blockWithReqBody(blockUserReq.getBlockedUserId(), blockUserId);
        return ResponseEntity.ok().body(ResponseDto.create(blockUserRes));
    }

    @GetMapping("/block/{user-id}")
    public ResponseEntity<ResponseDto> blockUser(
            @Valid @PathVariable("user-id") Long blockedUserId, HttpServletRequest request) {
        Long userId = jwtUtils.resolveRequest(request);
        BlockUserRes blockUserRes = userService.block(blockedUserId, userId);
        return ResponseEntity.ok().body(ResponseDto.create(blockUserRes));
    }
}
