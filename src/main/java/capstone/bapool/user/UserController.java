package capstone.bapool.user;

import capstone.bapool.config.error.BaseException;
import capstone.bapool.config.response.ResponseDto;
import capstone.bapool.user.dto.ReissueReq;
import capstone.bapool.user.dto.ReissueRes;
import capstone.bapool.user.dto.SocialAccessToken;
import capstone.bapool.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/auth")
@Api(value = "Auth")
public class UserController {

    private final UserService userService;
    private final JwtUtils jwtUtils;

    public UserController(UserService userService, JwtUtils jwtUtils) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping(value = "/kakao/signin")
    @ApiOperation(value = "카카오 로그인")
    public ResponseEntity<Object> SignInKakao(@RequestBody SocialAccessToken socialAccessToken) throws BaseException, IOException {
        ReissueRes signupRes = userService.signInKakao(socialAccessToken);
        return ResponseEntity.ok().body(ResponseDto.res(signupRes));
    }

    @PostMapping(value = "/kakao/signin/already")
    @ApiOperation(value = "카카오 회원인지 확인하는 로직 ")
    public ResponseEntity<Object> SignInKakaoAlready(@RequestBody SocialAccessToken socialAccessToken) throws BaseException, IOException {
        return ResponseEntity.ok().body(ResponseDto.res(userService.signInKakaoAready(socialAccessToken)));
    }

    @PostMapping(value = "/naver/signin")
    @ApiOperation(value = "네이버 로그인")
    public ResponseEntity<ResponseDto> SignInNaver(@RequestBody SocialAccessToken socialAccessToken) throws BaseException, IOException {
        ReissueRes signupRes = userService.signInNaver(socialAccessToken);
        return ResponseEntity.ok().body(ResponseDto.res(signupRes));
    }

    @PostMapping(value = "/naver/signin/already")
    @ApiOperation(value = "네이버 회원인지 확인하는 로직 ")
    public ResponseEntity<Object> SignInNaverAlready(@RequestBody SocialAccessToken socialAccessToken) throws BaseException, IOException {
        return ResponseEntity.ok().body(ResponseDto.res(userService.signInNaverAready(socialAccessToken)));
    }

    @PostMapping(value = "/reissuance")
    @ApiOperation(value = "토큰 재발급")
    public ResponseEntity<Object> reissueAccessToken(@RequestBody ReissueReq reissueReq) throws BaseException {
        ReissueRes signupRes = userService.reissueAccessToken(reissueReq);
        return ResponseEntity.ok().body(ResponseDto.res(signupRes));
    }

}
