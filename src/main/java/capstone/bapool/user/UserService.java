package capstone.bapool.user;

import capstone.bapool.config.error.BaseException;
import capstone.bapool.entity.User;
import capstone.bapool.user.dto.SignUpReq;
import capstone.bapool.user.dto.SignUpRes;
import capstone.bapool.utils.JwtUtils;
import capstone.bapool.utils.SocialUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;
import java.util.Optional;

import static capstone.bapool.config.error.StatusEnum.EXPIRATION_TOKEN;
import static capstone.bapool.config.error.StatusEnum.NOT_FOUND_USER_FAILURE;
import static capstone.bapool.config.error.StatusEnum.SOCIAL_LOGIN_FAILURE;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;
    private final JwtUtils jwtUtils;
    private final SocialUtils socialUtils;

    public SignUpRes signupNaver(SignUpReq signUpReq) throws BaseException,IOException {
        Map<String, String> response = socialUtils.makeUserInfoByNaver(signUpReq);
        User user = User.builder()
                .email((String) response.get("email"))
                .name((String) response.get("name"))
                .birthDay((String) response.getOrDefault("birthday", null))
                .build();
        User savedUser = saveOrFind(user);
        SignUpRes signUpRes = jwtUtils.generateTokens(savedUser.getId());
        updateRefreshToken(savedUser.getId(), signUpRes.getRefreshToken());
        return signUpRes;
    }

    public SignUpRes signupKakao(SignUpReq signUpReq) throws BaseException, IOException {
        Map<String, String> response = socialUtils.makeUserInfoByKakao(signUpReq);
        User user = User.builder()
                .email((String) response.get("email"))
                .name((String) response.get("nickname"))
                .birthDay((String) response.getOrDefault("birthday", null))
                .build();
        User savedUser = saveOrFind(user);
        SignUpRes signUpRes = jwtUtils.generateTokens(savedUser.getId());
        updateRefreshToken(savedUser.getId(), signUpRes.getRefreshToken());
        return signUpRes;
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

    public SignUpRes reissueAccessToken(SignUpReq signUpReq) throws BaseException {
        // validate를 함수로
        jwtUtils.validate(signUpReq.getRefreshToken());

        User user = userDao.findUserByRefreshToken(signUpReq.getRefreshToken())
                .orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));

        SignUpRes signUpRes = jwtUtils.generateTokens(user.getId());
        user.updateRefreshToken(signUpRes.getRefreshToken());
        return signUpRes;
    }
}