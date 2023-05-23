package capstone.bapool.firebase;

import capstone.bapool.firebase.dto.FireBaseParty;
import capstone.bapool.firebase.dto.FireBasePartyInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class FireBaseGroupRepository {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public FireBaseGroupRepository() {
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.databaseReference = firebaseDatabase.getReference("/test/Groups");
    }

    public void saveGroupInfo(FireBasePartyInfo fireBasePartyInfo, Long userId, Long groupId) {
        FireBaseParty fireBaseParty = new FireBaseParty(fireBasePartyInfo, new HashMap<String, Boolean>() {{
            put(userId.toString(), true);
        }});
        databaseReference.child(groupId.toString())
                .setValueAsync(fireBaseParty);
    }

    public void updateGroupInfo(FireBasePartyInfo fireBasePartyInfo, Long groupId) {
        databaseReference.child(groupId.toString())
                .child("groupInfo")
                .setValueAsync(fireBasePartyInfo);
    }

    public void participate(Long partyId, Long userId, int curNumberOfPeople) {
        databaseReference.child(partyId.toString())
                .child("groupInfo")
                .child("curNumberOfPeople")
                .setValueAsync(curNumberOfPeople + 1);
        databaseReference.child(partyId.toString())
                .child("groupUsers")
                .updateChildrenAsync(Map.of(userId.toString(), false));
    }
}
