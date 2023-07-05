package capstone.bapool.user;

import capstone.bapool.config.error.BaseException;
import capstone.bapool.config.response.ResponseDto;
import capstone.bapool.user.dto.ReissueReq;
import capstone.bapool.user.dto.ReissueRes;
import capstone.bapool.user.dto.SignInRes;
import capstone.bapool.user.dto.SignUpReq;
import capstone.bapool.user.dto.SignupRes;
import capstone.bapool.user.dto.SocialAccessToken;

import capstone.bapool.model.User;
import capstone.bapool.user.dto.*;
import capstone.bapool.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(value = "/kakao/signin")
    public ResponseEntity<Object> SignInKakao(@Valid @RequestBody SocialAccessToken socialAccessToken) throws BaseException, IOException {
        SignInRes signupRes = userService.signInKakao(socialAccessToken);
        return ResponseEntity.ok().body(ResponseDto.create(signupRes));
    }

    @PostMapping(value = "/kakao/signup")
    public ResponseEntity<ResponseDto> SignUpKakao(@Valid @RequestBody SignUpReq signUpReq) throws BaseException, IOException {
        ReissueRes signupRes = userService.signUpKakao(signUpReq);
        return ResponseEntity.ok().body(ResponseDto.create(signupRes));
    }

    @PostMapping(value = "/kakao/signin/already")
    public ResponseEntity<Object> SignInKakaoAlready(@Valid @RequestBody SocialAccessToken socialAccessToken) throws BaseException, IOException {
        return ResponseEntity.ok().body(ResponseDto.create(userService.signInKakaoAready(socialAccessToken)));
    }

    @PostMapping(value = "/naver/signin")
    public ResponseEntity<ResponseDto> SignInNaver(@Valid @RequestBody SocialAccessToken socialAccessToken) throws BaseException, IOException {
        SignInRes signupRes = userService.signInNaver(socialAccessToken);
        return ResponseEntity.ok().body(ResponseDto.create(signupRes));
    }

    @PostMapping(value = "/naver/signup")
    public ResponseEntity<ResponseDto> SignUpNaver(@Valid @RequestBody SignUpReq signUpReq) throws BaseException, IOException {
        ReissueRes signupRes = userService.signUpNaver(signUpReq);
        return ResponseEntity.ok().body(ResponseDto.create(signupRes));
    }

    @PostMapping(value = "/naver/signin/already")
    public ResponseEntity<Object> SignInNaverAlready(@Valid @RequestBody SocialAccessToken socialAccessToken) throws BaseException, IOException {
        return ResponseEntity.ok().body(ResponseDto.create(userService.signInNaverAready(socialAccessToken)));
    }

    @PostMapping(value = "/reissuance")
    public ResponseEntity<Object> reissueAccessToken(@Valid @RequestBody ReissueReq reissueReq) throws BaseException {
        ReissueRes signupRes = userService.reissueAccessToken(reissueReq);
        return ResponseEntity.ok().body(ResponseDto.create(signupRes));
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
    public ResponseEntity<ResponseDto> blockUser(
            @Valid @PathVariable("user-id") Long userId, @Valid @RequestBody BlockedUserReq blockedUserReq) {
        BlockUserRes blockUserRes = userService.block(userId, blockedUserReq.getBlockedUserId());
        return ResponseEntity.ok().body(ResponseDto.create(blockUserRes));
    }
}
