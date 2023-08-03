package capstone.bapool.firebase;

import capstone.bapool.firebase.dto.FireBaseParty;
import capstone.bapool.firebase.dto.FireBasePartyInfo;
import capstone.bapool.model.enumerate.PartyStatus;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Repository
public class FireBasePartyRepository {

    public static final String DEADLINE = "DEADLINE";
    public static final String DONE = "DONE";
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Autowired
    public FireBasePartyRepository(FirebaseApp firebaseApp) {
        this.firebaseDatabase = FirebaseDatabase.getInstance(firebaseApp);
        this.databaseReference = firebaseDatabase.getReference("/test/Groups");
    }

    public void save(FireBasePartyInfo fireBasePartyInfo, Long userId, Long groupId) {
        FireBaseParty fireBaseParty = new FireBaseParty(fireBasePartyInfo, new HashMap<String, Boolean>() {{
            put(userId.toString(), true);
        }});
        databaseReference.child(groupId.toString())
                .setValueAsync(fireBaseParty);
    }

    public void update(FireBasePartyInfo fireBasePartyInfo, Long groupId) {
        databaseReference.child(groupId.toString())
                .child("groupInfo")
                .child("groupName")
                .setValueAsync(fireBasePartyInfo.getGroupName());
        databaseReference.child(groupId.toString())
                .child("groupInfo")
                .child("groupDetail")
                .setValueAsync(fireBasePartyInfo.getGroupDetail());
        databaseReference.child(groupId.toString())
                .child("groupInfo")
                .child("maxNumberOfPeople")
                .setValueAsync(fireBasePartyInfo.getMaxNumberOfPeople());
        databaseReference.child(groupId.toString())
                .child("groupInfo")
                .child("startDate")
                .setValueAsync(fireBasePartyInfo.getStartDate());
        databaseReference.child(groupId.toString())
                .child("groupInfo")
                .child("endDate")
                .setValueAsync(fireBasePartyInfo.getEndDate());
        databaseReference.child(groupId.toString())
                .child("groupInfo")
                .child("hashTag")
                .setValueAsync(fireBasePartyInfo.getHashTag());
        databaseReference.child(groupId.toString())
                .child("groupInfo")
                .child("menu")
                .setValueAsync(fireBasePartyInfo.getMenu());
    }

    public void participate(Long partyId, Long userId, int curNumberOfPeople) {
        databaseReference.child(partyId.toString())
                .child("groupInfo")
                .child("curNumberOfPeople")
                .setValueAsync(curNumberOfPeople + 1);
        databaseReference.child(partyId.toString())
                .child("groupUsers")
                .updateChildrenAsync(Map.of(userId.toString(), true));
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

    public void becomeMember(Long partyId, Long userId) {
        databaseReference.child(partyId.toString())
                .child("groupUsers")
                .child(userId.toString())
                .setValueAsync(false);
    }

    public void closeParty(Long partyId) {
        databaseReference.child(partyId.toString())
                .child("groupInfo")
                .child("status")
                .setValueAsync(DEADLINE);
    }

    public void doneParty(Long partyId) {
        databaseReference.child(partyId.toString())
                .child("groupInfo")
                .child("status")
                .setValueAsync(DONE);
    }
}
