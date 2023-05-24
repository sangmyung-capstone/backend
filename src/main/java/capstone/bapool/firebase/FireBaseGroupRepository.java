package capstone.bapool.firebase;

import capstone.bapool.firebase.dto.FireBaseParty;
import capstone.bapool.firebase.dto.FireBasePartyInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Repository
@NoArgsConstructor
public class FireBaseGroupRepository {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    @PostConstruct
    public void init() {
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

    public void delete(Long partyId) {
        databaseReference.child(partyId.toString())
                .removeValueAsync();
    }

    public void secession(Long partyId, Long userId, int curNumberOfPeople) {
        databaseReference.child(partyId.toString())
                .child("groupInfo")
                .child("curNumberOfPeople")
                .setValueAsync(curNumberOfPeople - 1);

        databaseReference.child(partyId.toString())
                .child("groupUsers")
                .child(userId.toString())
                .removeValueAsync();
    }

    public void becomeLeader(Long partyId, Long userId) {
        databaseReference.child(partyId.toString())
                .child("groupUsers")
                .child(userId.toString())
                .setValueAsync(true);
    }
}
