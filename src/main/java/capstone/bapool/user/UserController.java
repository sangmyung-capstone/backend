package capstone.bapool.user;

import capstone.bapool.config.error.BaseException;
import capstone.bapool.config.response.ResponseDto;
import capstone.bapool.user.dto.ReissueReq;
import capstone.bapool.user.dto.ReissueRes;
import capstone.bapool.user.dto.SignUpReq;
import capstone.bapool.user.dto.SignupRes;
import capstone.bapool.user.dto.SocialAccessToken;
import capstone.bapool.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Object> SignInKakao(@RequestBody SocialAccessToken socialAccessToken) throws BaseException, IOException {
        ReissueRes signupRes = userService.signInKakao(socialAccessToken);
        return ResponseEntity.ok().body(ResponseDto.res(signupRes));
    }

    @PostMapping(value = "/kakao/signup")
    public ResponseEntity<ResponseDto> SignUpKakao(@RequestBody SignUpReq signUpReq) throws BaseException, IOException {
        ReissueRes signupRes = userService.signUpKakao(signUpReq);
        return ResponseEntity.ok().body(ResponseDto.res(signupRes));
    }

    @PostMapping(value = "/kakao/signin/already")
    public ResponseEntity<Object> SignInKakaoAlready(@RequestBody SocialAccessToken socialAccessToken) throws BaseException, IOException {
        return ResponseEntity.ok().body(ResponseDto.res(userService.signInKakaoAready(socialAccessToken)));
    }

    @PostMapping(value = "/naver/signin")
    public ResponseEntity<ResponseDto> SignInNaver(@RequestBody SocialAccessToken socialAccessToken) throws BaseException, IOException {
        ReissueRes signupRes = userService.signInNaver(socialAccessToken);
        return ResponseEntity.ok().body(ResponseDto.res(signupRes));
    }

    @PostMapping(value = "/naver/signup")
    public ResponseEntity<ResponseDto> SignUpNaver(@RequestBody SignUpReq signUpReq) throws BaseException, IOException {
        ReissueRes signupRes = userService.signUpNaver(signUpReq);
        return ResponseEntity.ok().body(ResponseDto.res(signupRes));
    }

    @PostMapping(value = "/naver/signin/already")
    public ResponseEntity<Object> SignInNaverAlready(@RequestBody SocialAccessToken socialAccessToken) throws BaseException, IOException {
        return ResponseEntity.ok().body(ResponseDto.res(userService.signInNaverAready(socialAccessToken)));
    }

    @PostMapping(value = "/reissuance")
    public ResponseEntity<Object> reissueAccessToken(@RequestBody ReissueReq reissueReq) throws BaseException {
        ReissueRes signupRes = userService.reissueAccessToken(reissueReq);
        return ResponseEntity.ok().body(ResponseDto.res(signupRes));
    }
}
