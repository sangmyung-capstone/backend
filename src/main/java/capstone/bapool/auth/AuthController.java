package capstone.bapool.auth;

import capstone.bapool.config.error.BaseException;
import capstone.bapool.config.response.ResponseDto;
import capstone.bapool.user.UserService;
import capstone.bapool.user.dto.*;
import capstone.bapool.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtUtils jwtUtils;

    public AuthController(AuthService authService, JwtUtils jwtUtils) {
        this.authService = authService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping(value = "/kakao/signin")
    public ResponseEntity<Object> SignInKakao(@Valid @RequestBody SocialAccessToken socialAccessToken) throws BaseException, IOException {
        SignInRes signupRes = authService.signInKakao(socialAccessToken);
        return ResponseEntity.ok().body(ResponseDto.create(signupRes));
    }

    @PostMapping(value = "/kakao/signup")
    public ResponseEntity<ResponseDto> SignUpKakao(@Valid @RequestBody SignUpReq signUpReq) throws BaseException, IOException {
        ReissueRes signupRes = authService.signUpKakao(signUpReq);
        return ResponseEntity.ok().body(ResponseDto.create(signupRes));
    }

    @PostMapping(value = "/kakao/signin/already")
    public ResponseEntity<Object> SignInKakaoAlready(@Valid @RequestBody SocialAccessToken socialAccessToken) throws BaseException, IOException {
        return ResponseEntity.ok().body(ResponseDto.create(authService.signInKakaoAready(socialAccessToken)));
    }

    @PostMapping(value = "/naver/signin")
    public ResponseEntity<ResponseDto> SignInNaver(@Valid @RequestBody SocialAccessToken socialAccessToken) throws BaseException, IOException {
        SignInRes signupRes = authService.signInNaver(socialAccessToken);
        return ResponseEntity.ok().body(ResponseDto.create(signupRes));
    }

    @PostMapping(value = "/naver/signup")
    public ResponseEntity<ResponseDto> SignUpNaver(@Valid @RequestBody SignUpReq signUpReq) throws BaseException, IOException {
        ReissueRes signupRes = authService.signUpNaver(signUpReq);
        return ResponseEntity.ok().body(ResponseDto.create(signupRes));
    }

    @PostMapping(value = "/naver/signin/already")
    public ResponseEntity<Object> SignInNaverAlready(@Valid @RequestBody SocialAccessToken socialAccessToken) throws BaseException, IOException {
        return ResponseEntity.ok().body(ResponseDto.create(authService.signInNaverAready(socialAccessToken)));
    }

    @PostMapping(value = "/reissuance")
    public ResponseEntity<Object> reissueAccessToken(@Valid @RequestBody ReissueReq reissueReq) throws BaseException {
        ReissueRes signupRes = authService.reissueAccessToken(reissueReq);
        return ResponseEntity.ok().body(ResponseDto.create(signupRes));
    }
}
