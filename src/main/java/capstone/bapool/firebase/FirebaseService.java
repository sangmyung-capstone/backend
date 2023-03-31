package capstone.bapool.firebase;

import capstone.bapool.entity.User;
import capstone.bapool.firebase.dto.FireBaseUser;
import capstone.bapool.firebase.dto.UserDto;
import capstone.bapool.user.dto.ReissueRes;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FirebaseService {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public FirebaseService() {
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.databaseReference = firebaseDatabase.getReference("server/users");
    }

    // 저장
    public void save(FireBaseUser fireBaseUser) {
        databaseReference.child(String.valueOf(fireBaseUser.getUserId()))
                .setValueAsync(new UserDto(fireBaseUser.getName()));
    }

    // 수정
    public void update(FireBaseUser fireBaseUser) {
        Map<String, Object> users = new HashMap<>();
        users.put("name", fireBaseUser.getName());
        databaseReference.child(String.valueOf(fireBaseUser.getUserId()))
                .updateChildrenAsync(users);
    }

    public void read(Long userId) {

    }
}
