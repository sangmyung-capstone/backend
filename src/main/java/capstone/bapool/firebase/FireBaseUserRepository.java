package capstone.bapool.firebase;

import capstone.bapool.config.error.BaseException;
import capstone.bapool.config.error.StatusEnum;
import capstone.bapool.firebase.dto.FireBaseUser;
import capstone.bapool.firebase.dto.UserDto;
import capstone.bapool.user.dto.UserInfoReq;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Repository
public class FireBaseUserRepository {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference tempDatabaseReference;

    @Autowired
    public FireBaseUserRepository(FirebaseApp firebaseApp) {
        this.firebaseDatabase = FirebaseDatabase.getInstance(firebaseApp);
        this.databaseReference = firebaseDatabase.getReference("/test/Users");
        this.tempDatabaseReference = firebaseDatabase.getReference("Groups");
    }

    // 저장
    public void save(FireBaseUser fireBaseUser) throws BaseException {
        databaseReference.child(String.valueOf(fireBaseUser.getUserId()))
                .setValueAsync(new UserDto(fireBaseUser.getName(), fireBaseUser.getImgUrl(), fireBaseUser.getFirebaseToken()));
        existUser(fireBaseUser.getUserId());
    }

    public void delete(Long userId) {
        existUser(userId);
        databaseReference.child(String.valueOf(userId))
                .removeValueAsync();
    }

    private void existUser(Long userId) {
        databaseReference.child(userId.toString())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if(!snapshot.exists()) {
                            System.out.println(snapshot.getValue());
                            throw new BaseException(StatusEnum.NOT_FOUND_USER_FAILURE);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                    }
                });
    }

    // 수정
    public void addBannedUser(Long userId, Long bannedUserId) {
        Map<String, Object> users = new HashMap<>();
        users.put(bannedUserId.toString(),true);
        databaseReference.child(String.valueOf(userId))
                .child("bannedUser")
                .updateChildrenAsync(users);
    }

    public void removeBannedUser(Long userId, Long bannedUserId) {
        databaseReference.child(String.valueOf(userId))
                .child("bannedUser")
                .child(bannedUserId.toString())
                .removeValueAsync();
    }

    public void chattingFcmTest(){
        tempDatabaseReference.child("groupId1").child("groupMessages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        log.info(snapshot.getKey());
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
    }

    public void updateUserInfo(Long userId, UserInfoReq userInfoReq) {
        databaseReference.child(String.valueOf(userId))
                .child("imgUrl")
                .setValueAsync(userInfoReq.getProfileImg());
        databaseReference.child(String.valueOf(userId))
                .child("nickName")
                .setValueAsync(userInfoReq.getName());
    }
}
