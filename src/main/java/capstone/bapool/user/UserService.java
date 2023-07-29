package capstone.bapool.user;

import capstone.bapool.config.error.BaseException;
import capstone.bapool.config.error.StatusEnum;
import capstone.bapool.config.response.ResponseDto;
import capstone.bapool.firebase.FireBaseUserRepository;
import capstone.bapool.model.BlockUser;
import capstone.bapool.model.User;
import capstone.bapool.model.enumerate.BlockStatus;
import capstone.bapool.user.dto.BlockUserListRes;
import capstone.bapool.party.PartyRepository;
import capstone.bapool.user.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static capstone.bapool.config.error.StatusEnum.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BlockUserRepository blockUserRepository;
    private final UserHashtagRepository userHashtagRepository;
    private final PartyRepository partyRepository;

    private final FireBaseUserRepository fireBaseUserDao;

    @Transactional(readOnly=true)
    public UserRes findById(Long userId) throws BaseException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));    //db에 사용자id가 없다면 처리
        List<Integer> hashtags = userHashtagRepository.findByUserId(userId)
                .stream().map((userHashtag -> userHashtag.getHashtagId()))
                .collect(Collectors.toList());
        return UserRes.builder()
                .name(user.getName())
                .profileImgId(user.getProfileImgId())
                .rating(user.getRating())
                .hashtag(hashtags)
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
        List<Integer> hashtags = userHashtagRepository.findByUserId(otherUserId)
                .stream().map((userHashtag -> userHashtag.getHashtagId()))
                .collect(Collectors.toList());
        boolean is_block = blockUserRepository.findByBlockUserAndBlockedUser(user, otherUser)!=null;

        return OtherUserRes.builder()
                .userId(otherUser.getId())
                .profileImg(otherUser.getProfileImgId())
                .name(otherUser.getName())
                .rating(otherUser.getRating())
                .hashtag(hashtags)
                .is_block(is_block)
                .build();
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


    @Transactional(readOnly = true)
    public BlockUserListRes blockList(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));
        List<BlockedUserInfo> blockedUserInfoList = blockUserRepository.findByBlockUserOrderByUpdatedDate(user)
                .stream().map(BlockUser ->BlockedUserInfo.builder()
                        .userId(BlockUser.getBlockedUser().getId())
                        .name(BlockUser.getBlockedUser().getName())
                        .blockDate(BlockUser.getUpdatedDate())
                        .build())
                .collect(Collectors.toList());
        BlockUserListRes blockUserListRes = new BlockUserListRes(blockedUserInfoList);
        return blockUserListRes;
    }

    @Transactional
    public boolean updateUserInfo(Long userId, UserInfoReq userInfoReq){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));
        String newName = userInfoReq.getName();
        int newProfileImg = userInfoReq.getProfileImg();
        if(userRepository.existsUserByName(newName)){
            throw new BaseException(ALREADY_EXIST_NAME_FAILURE);
        }else{
            user.update(newName, newProfileImg);
        }
        return false;
    }

    // 유저 평가하기
    public void userRating(Long userId, PostUserRatingReq postUserRatingReq){
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new BaseException(NOT_FOUND_USER_FAILURE);
        });

    }
}