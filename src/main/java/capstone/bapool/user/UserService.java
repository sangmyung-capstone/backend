package capstone.bapool.user;

import capstone.bapool.config.error.BaseException;
import capstone.bapool.config.interceptor.AuthenticationInterceptor;
import capstone.bapool.config.response.ResponseDto;
import capstone.bapool.firebase.FireBaseUserRepository;
import capstone.bapool.firebase.dto.FireBaseUser;
import capstone.bapool.model.BlockUser;
import capstone.bapool.model.User;
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
import java.util.Map;

import static capstone.bapool.config.error.StatusEnum.NOT_FOUND_USER_FAILURE;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BlockUserRepository blockUserRepository;

    private final FireBaseUserRepository fireBaseUserDao;

    @Transactional
    public UserRes findById(Long userId) throws BaseException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));    //db에 사용자id가 없다면 처리
        return UserRes.builder()
                .name(user.getName())
                .profileImgId(user.getProfileImgId())
                .rating(user.getRating())
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

    @Transactional
    public OtherUserRes findOtherById(Long userId, Long otherUserId) throws BaseException {
        //내id랑 userid랑 확인해야되나, 카톡은 내 결과도 그냥 뜨는데
        //내id랑 userid랑 구별을 해야 block유무를 확인할수있는데 우짜지...
//        User user = jwtUtils.resolveRequest(). 어쩌구 하면 userid뽑을수 있을거같은데....

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));
        User otherUser = userRepository.findById(otherUserId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));
        boolean is_block = blockUserRepository.findExist(user,otherUser)!=null;
        return OtherUserRes.builder()
                .userId(otherUser.getId())
                .profileImg(otherUser.getProfileImgId())
                .name(otherUser.getName())
                .rating(otherUser.getRating())
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
            if(blockUserRepository.findExist(blockUser,blockedUser)!=null){
                BlockUser blockUserTuple = blockUserRepository.findExist(blockUser,blockedUser);
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
            if(blockUserRepository.findExist(blockUser,blockedUser)!=null){
                blockUserRepository.delete(blockUserRepository.findExist(blockUser,blockedUser));
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