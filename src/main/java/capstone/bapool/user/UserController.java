package capstone.bapool.user;

import capstone.bapool.config.error.BaseException;
import capstone.bapool.config.response.ResponseDto;
import capstone.bapool.model.User;
import capstone.bapool.user.dto.ReissueReq;
import capstone.bapool.user.dto.ReissueRes;
import capstone.bapool.user.dto.SignUpReq;
import capstone.bapool.user.dto.SocialAccessToken;
import capstone.bapool.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;

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
        ReissueRes signupRes = userService.signInKakao(socialAccessToken);
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
        ReissueRes signupRes = userService.signInNaver(socialAccessToken);
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
    public ResponseDto<Object> mypage(@Valid @PathVariable("user-id") Long userId){
        Optional<User> user = userService.findById(userId);
        return ResponseDto.create(user);
    }

    //회원 탈퇴
    @DeleteMapping("user/{user-id}")
    public void deleteUser(@Valid @PathVariable("user-id") Long userId){
        userService.deleteById(userId);
    }
}
