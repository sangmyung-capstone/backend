package capstone.bapool.user;

import capstone.bapool.config.error.BaseException;
import capstone.bapool.config.response.ResponseDto;
import capstone.bapool.user.dto.SignUpReq;
import capstone.bapool.user.dto.SignUpRes;
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

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/kakao/signup")
    @ApiOperation(value = "카카오 회원가입")
    public ResponseEntity<Object> kakaoSignup(@RequestBody SignUpReq signUpReq) throws BaseException, IOException {
        SignUpRes signupRes = userService.signupKakao(signUpReq);
        return ResponseEntity.ok().body(ResponseDto.res(signupRes));
    }

    @PostMapping(value = "/naver/signup")
    @ApiOperation(value = "네이버 회원가입")
    public ResponseEntity<ResponseDto> naverSignup(@RequestBody SignUpReq signUpReq) throws BaseException, IOException {
        SignUpRes signupRes = userService.signupNaver(signUpReq);
        return ResponseEntity.ok().body(ResponseDto.res(signupRes));
    }

    @PostMapping(value = "/reissuance")
    @ApiOperation(value = "토큰 재발급")
    public ResponseEntity<Object> reissueAccessToken(@RequestBody SignUpReq signUpReq) throws BaseException {
        SignUpRes signupRes = userService.reissueAccessToken(signUpReq);
        return ResponseEntity.ok().body(ResponseDto.res(signupRes));
    }

}
