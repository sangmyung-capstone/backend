package capstone.bapool.firebase;

import capstone.bapool.firebase.dto.FireBaseParty;
import capstone.bapool.firebase.dto.FireBasePartyInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class FireBaseGroupDao {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public FireBaseGroupDao() {
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.databaseReference = firebaseDatabase.getReference("/test/Groups");
    }

    public void saveGroupInfo(FireBasePartyInfo fireBasePartyInfo, Long userId) {
        FireBaseParty fireBaseParty = new FireBaseParty(fireBasePartyInfo, new HashMap<String, Boolean>() {{
            put(userId.toString(), true);
        }});
        databaseReference.child(fireBasePartyInfo.getGroupId().toString())
                .setValueAsync(fireBaseParty);
    }
}
