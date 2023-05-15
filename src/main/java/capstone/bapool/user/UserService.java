package capstone.bapool.user;

import capstone.bapool.config.error.BaseException;
import capstone.bapool.entity.User;
import capstone.bapool.firebase.FireBaseUserDao;
import capstone.bapool.firebase.dto.FireBaseUser;
import capstone.bapool.user.dto.ReissueReq;
import capstone.bapool.user.dto.ReissueRes;
import capstone.bapool.user.dto.SignUpReq;
import capstone.bapool.user.dto.SocialAccessToken;
import capstone.bapool.utils.JwtUtils;
import capstone.bapool.utils.SocialUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;

import static capstone.bapool.config.error.StatusEnum.EXIST_NAME;
import static capstone.bapool.config.error.StatusEnum.NOT_FOUND_USER_FAILURE;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;
    private final JwtUtils jwtUtils;
    private final SocialUtils socialUtils;

    private final FireBaseUserDao fireBaseUserDao;

    public ReissueRes signInKakao(SocialAccessToken socialAccessToken) throws BaseException, IOException {
        String email = socialUtils.makeUserInfoByKakao(socialAccessToken.getAccessToken()).get("email");
        User user = userDao.findUserByEmail(email).orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));
        ReissueRes reissueRes = jwtUtils.generateTokens(user.getId());
        user.updateRefreshToken(reissueRes.getRefreshToken());
        return reissueRes;
    }

    @Transactional(readOnly = false)
    public ReissueRes signUpKakao(SignUpReq signUpReq) throws IOException {
        if (userDao.findUserByName(signUpReq.getNickName()).isPresent()) {
            throw new BaseException(EXIST_NAME);
        }
        Map<String, String> response = socialUtils.makeUserInfoByKakao(signUpReq.getAccessToken());
        User user = User.builder()
                .email((String) response.get("email"))
                .name((String) response.get("name"))
                .profileImgId(signUpReq.getProfileImgId())
                .name(signUpReq.getNickName())
                .build();
        User savedUser = userDao.save(user);
        ReissueRes reissueRes = jwtUtils.generateTokens(savedUser.getId());
        savedUser.updateRefreshToken(reissueRes.getRefreshToken());
        return reissueRes;
    }

    public Boolean signInKakaoAready(SocialAccessToken socialAccessToken) throws IOException, BaseException{
        String email = socialUtils.makeUserInfoByKakao(socialAccessToken.getAccessToken()).get("email");
        return !userDao.findUserByEmail(email).isEmpty();
    }

    public ReissueRes signInNaver(SocialAccessToken socialAccessToken) throws BaseException,IOException {
        String email = socialUtils.makeUserInfoByNaver(socialAccessToken.getAccessToken()).get("email");
        User user = userDao.findUserByEmail(email).orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));
        ReissueRes reissueRes = jwtUtils.generateTokens(user.getId());
        user.updateRefreshToken(reissueRes.getRefreshToken());
        return reissueRes;
    }

    @Transactional(readOnly = false)
    public ReissueRes signUpNaver(SignUpReq signUpReq) throws IOException{
        if (userDao.findUserByName(signUpReq.getNickName()).isPresent()) {
            throw new BaseException(EXIST_NAME);
        }
        Map<String, String> response = socialUtils.makeUserInfoByNaver(signUpReq.getAccessToken());
        User user = User.builder()
                .email((String) response.get("email"))
                .name((String) response.get("name"))
                .profileImgId(signUpReq.getProfileImgId())
                .name(signUpReq.getNickName())
                .build();
        User savedUser = saveUser(user);
        ReissueRes reissueRes = jwtUtils.generateTokens(savedUser.getId());
        savedUser.updateRefreshToken(reissueRes.getRefreshToken());
        return reissueRes;
    }

    private User saveUser(User user) {
        User savedUser = userDao.save(user);
        fireBaseUserDao.save(new FireBaseUser(savedUser.getId(), savedUser.getName(), savedUser.getProfileImgId()));
        return savedUser;
    }

    public Boolean signInNaverAready(SocialAccessToken socialAccessToken) throws IOException, BaseException{
        String email = socialUtils.makeUserInfoByNaver(socialAccessToken.getAccessToken()).get("email");
        return !userDao.findUserByEmail(email).isEmpty();
    }

    @Transactional(readOnly = false)
    public ReissueRes reissueAccessToken(ReissueReq reissueReq) throws BaseException {
        // validate를 함수로
        jwtUtils.validate(reissueReq.getRefreshToken());

        User user = userDao.findUserByRefreshToken(reissueReq.getRefreshToken())
                .orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));

        ReissueRes reissueRes = jwtUtils.generateTokens(user.getId());
        user.updateRefreshToken(reissueRes.getRefreshToken());
        return reissueRes;
    }
}