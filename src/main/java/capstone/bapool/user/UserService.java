package capstone.bapool.user;

import capstone.bapool.config.error.BaseException;
import capstone.bapool.config.response.ResponseDto;
import capstone.bapool.firebase.FireBaseUserRepository;
import capstone.bapool.model.*;
import capstone.bapool.model.enumerate.BlockStatus;
import capstone.bapool.model.enumerate.PartyStatus;
import capstone.bapool.party.PartyParticipantRepository;
import capstone.bapool.party.PartyService;
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
    private final UserRatingRepository userRatingRepository;
    private final PartyParticipantRepository partyParticipantRepository;
    private final PartyService partyService;

    private final FireBaseUserRepository fireBaseUserDao;

    @Transactional(readOnly=true)
    public UserRes findById(Long userId) throws BaseException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));

        // gana-수정 필요해 보임.
        List<UserHashtagInfo> hashtags = userHashtagRepository.findByUserId(userId);

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

        fireBaseUserDao.delete(userId);
        return ResponseDto.create(userId+" deleted successfully");
    }

    @Transactional(readOnly=true)
    public OtherUserRes findOtherById(Long userId, Long otherUserId) throws BaseException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));
        User otherUser = userRepository.findById(otherUserId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));

        List<UserHashtagInfo> hashtags = userHashtagRepository.findByUserId(otherUserId);
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

        if(blockUser.equals(blockedUser)){
            return new BlockUserRes(BlockStatus.SAMEUSEREXCEPTION);
        }

        BlockUser is_block = blockUserRepository.findByBlockUserAndBlockedUser(blockUser, blockedUser);
        if(is_block != null){
            //차단 해제하기
            blockUserRepository.delete(is_block);
            return new BlockUserRes(BlockStatus.UNBLOCK);
        }
        //차단하기
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
        }
        user.update(newName, newProfileImg);
        fireBaseUserDao.updateUserInfo(userId, userInfoReq);
        return false;
    }

    /**
     * 유저 평가하기
     * @exception BaseException NOT_FOUND_USER_FAILURE, NOT_FOUND_PARTY_FAILURE, PARTY_NOT_DONE, NOT_PARTY_PARTICIPANT
     */
    @Transactional
    public void userRating(Long userId, Long partyId, PostUserRatingReq postUserRatingReq){
        User user = findUser(userId);

        Party party = partyService.findParty(partyId);

        PartyParticipant partyParticipant = partyParticipantRepository.findPartyParticipantByUserAndParty(user, party)
            .orElseThrow(() -> {
                log.error("유저 평가하기: 파티에 참여하지 않은 유저입니다. userId={}, partyId={}", userId, partyId);
                throw new BaseException(NOT_FOUND_PARTY_PARTICIPANT_FAILURE);
        });

        // 다 먹은 파티가 아니면
        if(!party.isDone()){
            log.error("유저 평가하기: partyStatus가 Done이 아닙니다. partyId={}", partyId);
            throw new BaseException(PARTY_NOT_DONE);
        }

        // 해당 파티에 내가 참여하지 않았으면
        if(!party.isMeParticipate(user)){
            log.error("유저 평가하기: 해당 유저가 참여하지 않은 파티입니다. userId={}, partyId={}", userId, partyId);
            throw new BaseException(NOT_PARTY_PARTICIPANT);
        }

        // 이미 평가를 완료한 유저라면
        if(partyParticipant.getRatingComplete()){
            log.error("유저 평가하기: 이미 유저평가를 완료했습니다. userId={}, partyId={}", userId, partyId);
            throw new BaseException(ALREADY_RATING_COMPLETE);
        }

        // 모든 유저에 대해 평가한 것이 아니면
        if(postUserRatingReq.getRatedUserList().size() != party.getCurPartyMember()-1){
            log.error("유저 평가하기: 모든 유저에 대해 평가하지 않았습니다. userId={}, partyId={}", userId, partyId);
            throw new BaseException(NOT_SUFFICIENT_NUM_OF_USER);
        }

        // 유저 평가 반영
        for(RatedUser ratedUser : postUserRatingReq.getRatedUserList()){
            rateUser(ratedUser, user, party, userId, partyId);
        }

        // 유저 평가 완료로 status 변경
        partyParticipant.setRatingComplete();
    }

    public User findUser(Long userId){
        return userRepository.findById(userId).orElseThrow(() -> {
            log.error("유저 평가하기: 존재하지 않는 유저입니다. userId-{}", userId);
            throw new BaseException(NOT_FOUND_USER_FAILURE);
        });
    }


    private void rateUser(RatedUser ratedUser, User user, Party party, Long userId, Long partyId){
        User evaluatedUser = findUser(ratedUser.getUserId());

        // 해당 파티에 참여하지 않았으면
        if(!party.isMeParticipate(evaluatedUser)){
            log.error("유저 평가하기: 해당 유저가 참여하지 않은 파티입니다. userId={}, partyId={}", evaluatedUser.getId(), partyId);
            throw new BaseException(NOT_PARTY_PARTICIPANT);
        }

        // 자기 자신에 대해 평가할 수 없음
        if(userId.equals(ratedUser.getUserId())){
            log.error("유저 평가하기: 자기 자신에 대해 평가할 수 없습니다. userId={}, partyId={}", evaluatedUser.getId(), partyId);
            throw new BaseException(CAN_NOT_RATING_MYSELF);
        }

        // 유저 평점 반영
        UserRating userRating = new UserRating(user, evaluatedUser, party, ratedUser.getRating());
        userRatingRepository.save(userRating);
        log.info("유저 평가하기-유저 평점 반영: partyId={}, 평가한 유저Id={}, 평가당한 유저Id={}, 평점={}", partyId, userId, evaluatedUser.getId(), ratedUser.getRating());

        // 유저 해시태그 반영
        for(int hashtag : ratedUser.getHashtag()){
            UserHashtag userHashtag = UserHashtag.builder()
                    .party(party)
                    .evaluateUser(user)
                    .evaluatedUser(evaluatedUser)
                    .hashtagId(hashtag)
                    .build();
            userHashtagRepository.save(userHashtag);
            log.info("유저 평가하기-유저 해시태그 반영: partyId={}, 평가한 유저={}, 평가당한 유저={}, 해시태그={}", partyId, userId, evaluatedUser.getId(), hashtag);
        }
    }
}