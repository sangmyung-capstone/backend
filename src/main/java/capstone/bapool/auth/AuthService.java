package capstone.bapool.auth;

import capstone.bapool.config.error.BaseException;
import capstone.bapool.config.error.StatusEnum;
import capstone.bapool.firebase.FireBaseUserRepository;
import capstone.bapool.firebase.dto.FireBaseUser;
import capstone.bapool.model.User;
import capstone.bapool.user.BlockUserRepository;
import capstone.bapool.user.UserRepository;
import capstone.bapool.user.dto.*;
import capstone.bapool.utils.JwtUtils;
import capstone.bapool.utils.SocialUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;

import static capstone.bapool.config.error.StatusEnum.ALREADY_EXIST_NAME_FAILURE;
import static capstone.bapool.config.error.StatusEnum.NOT_FOUND_USER_FAILURE;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final SocialUtils socialUtils;
    private final BlockUserRepository blockUserRepository;

    private final FireBaseUserRepository fireBaseUserDao;

    @Transactional(readOnly = false)
    public SignInRes signInKakao(SocialAccessToken socialAccessToken) throws BaseException, IOException {
        String email = socialUtils.makeUserInfoByKakao(socialAccessToken.getAccessToken()).get("email");
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));
        SignInRes reissueRes = new SignInRes(jwtUtils.generateTokens(user.getId()), user.getName());
        user.updateRefreshToken(reissueRes.getRefreshToken());
        return reissueRes;
    }

    @Transactional(readOnly = false)
    public ReissueRes signUpKakao(SignUpReq signUpReq) throws IOException {
        Map<String, String> response = socialUtils.makeUserInfoByKakao(signUpReq.getAccessToken());
        User user = User.builder()
                .email((String) response.get("email"))
                .name((String) response.get("name"))
                .profileImgId(signUpReq.getProfileImgId())
                .name(signUpReq.getNickName())
                .build();
        User savedUser = saveUser(user, signUpReq.getFireBaseToken());
        ReissueRes reissueRes = jwtUtils.generateTokens(savedUser.getId());
        savedUser.updateRefreshToken(reissueRes.getRefreshToken());
        return reissueRes;
    }

    public Boolean signInKakaoAready(SocialAccessToken socialAccessToken) throws IOException, BaseException{
        String email = socialUtils.makeUserInfoByKakao(socialAccessToken.getAccessToken()).get("email");
        return !userRepository.findUserByEmail(email).isEmpty();
    }

    @Transactional(readOnly = false)
    public SignInRes signInNaver(SocialAccessToken socialAccessToken) throws BaseException,IOException {
        String email = socialUtils.makeUserInfoByNaver(socialAccessToken.getAccessToken()).get("email");
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));
        SignInRes reissueRes = new SignInRes(jwtUtils.generateTokens(user.getId()), user.getName());
        log.info(user.getName());
        user.updateRefreshToken(reissueRes.getRefreshToken());
        return reissueRes;
    }

    @Transactional(readOnly = false)
    public ReissueRes signUpNaver(SignUpReq signUpReq) throws IOException{
        Map<String, String> response = socialUtils.makeUserInfoByNaver(signUpReq.getAccessToken());
        User user = User.builder()
                .email((String) response.get("email"))
                .name((String) response.get("name"))
                .profileImgId(signUpReq.getProfileImgId())
                .name(signUpReq.getNickName())
                .build();
        User savedUser = saveUser(user, signUpReq.getFireBaseToken());
        ReissueRes reissueRes = jwtUtils.generateTokens(savedUser.getId());
        savedUser.updateRefreshToken(reissueRes.getRefreshToken());
        return reissueRes;
    }

    private User saveUser(User user, String firebaseToken) {
        if (userRepository.existsUserByName(user.getName())) {
            throw new BaseException(ALREADY_EXIST_NAME_FAILURE);
        }
        User savedUser = userRepository.save(user);
        fireBaseUserDao.save(new FireBaseUser(savedUser.getId(), savedUser.getName(), savedUser.getProfileImgId(), firebaseToken));
        return savedUser;
    }

    public Boolean signInNaverAready(SocialAccessToken socialAccessToken) throws IOException, BaseException{
        String email = socialUtils.makeUserInfoByNaver(socialAccessToken.getAccessToken()).get("email");
        return !userRepository.findUserByEmail(email).isEmpty();
    }

    @Transactional(readOnly = false)
    public ReissueRes reissueAccessToken(ReissueReq reissueReq) throws BaseException {
        // validate를 함수로
        jwtUtils.validate(reissueReq.getRefreshToken());

        User user = userRepository.findUserByRefreshToken(reissueReq.getRefreshToken())
                .orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));

        ReissueRes reissueRes = jwtUtils.generateTokens(user.getId());
        user.updateRefreshToken(reissueRes.getRefreshToken());
        return reissueRes;
    }

}
