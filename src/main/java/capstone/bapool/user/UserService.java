package capstone.bapool.user;

import capstone.bapool.config.error.BaseException;
import capstone.bapool.config.interceptor.AuthenticationInterceptor;
import capstone.bapool.config.response.ResponseDto;
import capstone.bapool.firebase.FireBaseUserRepository;
import capstone.bapool.firebase.dto.FireBaseUser;
import capstone.bapool.model.BlockUser;
import capstone.bapool.model.User;
import capstone.bapool.model.UserHashtag;
import capstone.bapool.model.enumerate.BlockStatus;
import capstone.bapool.user.dto.ReissueReq;
import capstone.bapool.user.dto.ReissueRes;
import capstone.bapool.user.dto.SignInRes;
import capstone.bapool.user.dto.SignUpReq;
import capstone.bapool.user.dto.SocialAccessToken;
import capstone.bapool.user.dto.*;
import capstone.bapool.utils.JwtUtils;
import capstone.bapool.utils.SocialUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static capstone.bapool.config.error.StatusEnum.NOT_FOUND_USER_FAILURE;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BlockUserRepository blockUserRepository;
    private final UserHashtagRepository userHashtagRepository;

    private final FireBaseUserRepository fireBaseUserDao;

    @Transactional(readOnly=true)
    public UserRes findById(Long userId) throws BaseException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));    //db에 사용자id가 없다면 처리
        List<UserHashtag> userHashtags = userHashtagRepository.findByUserId(userId);
        return UserRes.builder()
                .name(user.getName())
                .profileImgId(user.getProfileImgId())
                .rating(user.getRating())
                .userHashtags(userHashtags)
                .build();
    }

    @Transactional
    public ResponseDto deleteById(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));
        userRepository.deleteById(user.getId());
        //firebase에서 채팅방도 나가야 될거같은데
        fireBaseUserDao.delete(userId);//이거는 firebase에서 삭제만
        return ResponseDto.create(userId+" deleted successfully");
    }

    @Transactional(readOnly=true)
    public OtherUserRes findOtherById(Long userId, Long otherUserId) throws BaseException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));
        User otherUser = userRepository.findById(otherUserId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));
        List<UserHashtag> userHashtags = userHashtagRepository.findByUserId(otherUserId);
        boolean is_block = blockUserRepository.findByBlockUserAndBlockedUser(user, otherUser)!=null;
        return OtherUserRes.builder()
                .userId(otherUser.getId())
                .profileImg(otherUser.getProfileImgId())
                .name(otherUser.getName())
                .rating(otherUser.getRating())
                .userHashtags(userHashtags)
                .is_block(is_block)
                .build();
    }

    @Transactional
    public BlockUserRes block(Long blockedUserId, Long blockUserId){
        User blockUser = userRepository.findById(blockUserId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));
        User blockedUser = userRepository.findById(blockedUserId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));

        if(blockUser != blockedUser){
            if(blockUserRepository.findByBlockUserAndBlockedUser(blockUser, blockedUser)!=null){
                BlockUser blockUserTuple = blockUserRepository.findByBlockUserAndBlockedUser(blockUser, blockedUser);
                blockUserRepository.delete(blockUserTuple);
                return new BlockUserRes(BlockStatus.UNBLOCK);
            }else {//else면 차단하기, if면 차단 해제
                BlockUser blockUserTuple = BlockUser.builder()
                        .blockUser(blockUser)
                        .blockedUser(blockedUser)
                        .build();
                blockUserRepository.save(blockUserTuple);

                BlockUserRes blockUserRes  = BlockUserRes.builder()
                        .userId(blockedUser.getId())
                        .blockStatus(BlockStatus.BLOCK)
                        .name(blockedUser.getName())
                        .blockDate(blockUserTuple.getUpdatedDate())
                        .build();
                return blockUserRes;
            }
        }else{
            return new BlockUserRes(BlockStatus.SAMEUSEREXCEPTION);
        }
    }

    @Transactional
    public BlockUserRes blockWithReqBody(Long blockedUserId, Long blockUserId){
        User blockUser = userRepository.findById(blockUserId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));
        User blockedUser = userRepository.findById(blockedUserId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));

        if(blockUser != blockedUser){
            if(blockUserRepository.findByBlockUserAndBlockedUser(blockUser, blockedUser)!=null){
                blockUserRepository.delete(blockUserRepository.findByBlockUserAndBlockedUser(blockUser, blockedUser));
                return new BlockUserRes(BlockStatus.UNBLOCK);
            }else {//else면 차단하기, if면 차단 해제
                BlockUser blockUserTuple = BlockUser.builder()
                        .blockUser(blockUser)
                        .blockedUser(blockedUser)
                        .build();
                blockUserRepository.save(blockUserTuple);

                BlockUserRes blockUserRes  = BlockUserRes.builder()
                        .userId(blockedUser.getId())
                        .blockStatus(BlockStatus.BLOCK)
                        .name(blockedUser.getName())
                        .blockDate(blockUserTuple.getUpdatedDate())
                        .build();
                return blockUserRes;
            }
        }else{
            return new BlockUserRes(BlockStatus.SAMEUSEREXCEPTION);
        }
    }
}