package capstone.bapool.user;

import capstone.bapool.config.error.BaseException;
import capstone.bapool.config.response.ResponseDto;
import capstone.bapool.user.dto.SignUpReq;
import capstone.bapool.user.dto.SignUpRes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/kakao/signup")
    public ResponseEntity<Object> kakaoSignup(@RequestBody SignUpReq signUpReq) throws BaseException {
        SignUpRes signupRes = userService.signupKakao(signUpReq);
        return ResponseEntity.ok().body(ResponseDto.res(signupRes));
    }

    @PostMapping(value = "/naver/signup")
    public ResponseEntity<ResponseDto> naverSignup(@RequestBody SignUpReq signUpReq) throws BaseException {
        SignUpRes signupRes = userService.signupNaver(signUpReq);
        return ResponseEntity.ok().body(ResponseDto.res(signupRes));
    }

    @PostMapping(value = "/reissuance")
    public ResponseEntity<Object> reissueAccessToken(@RequestBody SignUpReq signUpReq) throws BaseException {
        SignUpRes signupRes = userService.reissueAccessToken(signUpReq);
        return ResponseEntity.ok().body(ResponseDto.res(signupRes));
    }

}
