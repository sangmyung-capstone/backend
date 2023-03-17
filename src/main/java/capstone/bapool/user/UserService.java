package capstone.bapool.user;

import capstone.bapool.config.error.BaseException;
import capstone.bapool.entity.User;
import capstone.bapool.user.dto.ReissueReq;
import capstone.bapool.user.dto.ReissueRes;
import capstone.bapool.user.dto.SocialAccessToken;
import capstone.bapool.utils.JwtUtils;
import capstone.bapool.utils.SocialUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static capstone.bapool.config.error.StatusEnum.NOT_FOUND_USER_FAILURE;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;
    private final JwtUtils jwtUtils;
    private final SocialUtils socialUtils;

    public ReissueRes signInNaver(SocialAccessToken socialAccessToken) throws BaseException,IOException {
        Map<String, String> response = socialUtils.makeUserInfoByNaver(socialAccessToken);
        User user = User.builder()
                .email((String) response.get("email"))
                .name((String) response.get("name"))
                .birthDay((String) response.getOrDefault("birthday", null))
                .build();
        User savedUser = saveOrFind(user);
        ReissueRes reissueRes = jwtUtils.generateTokens(savedUser.getId());
        updateRefreshToken(savedUser.getId(), reissueRes.getRefreshToken());
        return reissueRes;
    }

    public ReissueRes signInKakao(SocialAccessToken socialAccessToken) throws BaseException, IOException {
        Map<String, String> response = socialUtils.makeUserInfoByKakao(socialAccessToken);
        User user = User.builder()
                .email((String) response.get("email"))
                .name((String) response.get("nickname"))
                .birthDay((String) response.getOrDefault("birthday", null))
                .build();
        User savedUser = saveOrFind(user);
        ReissueRes reissueRes = jwtUtils.generateTokens(savedUser.getId());
        updateRefreshToken(savedUser.getId(), reissueRes.getRefreshToken());
        return reissueRes;
    }

    public Boolean signInKakaoAready(SocialAccessToken socialAccessToken) throws IOException, BaseException{
        String email = socialUtils.makeUserInfoByKakao(socialAccessToken).get("email");
        return !userDao.findUserByEmail(email).isEmpty();
    }

    public Boolean signInNaverAready(SocialAccessToken socialAccessToken) throws IOException, BaseException{
        String email = socialUtils.makeUserInfoByNaver(socialAccessToken).get("email");
        return !userDao.findUserByEmail(email).isEmpty();
    }

    public User saveOrFind(User user) {
        Optional<User> userByEmail = userDao.findUserByEmail(user.getEmail());
        if (userByEmail.isEmpty()) {
            User save = userDao.save(user);
            return save;
        }
        return userByEmail.get();
    }

    public void updateRefreshToken(Long userId, String refreshToken) throws BaseException {
        User user = userDao.findById(userId).orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));
        user.updateRefreshToken(refreshToken);
    }

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