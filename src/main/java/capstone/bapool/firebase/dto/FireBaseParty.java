package capstone.bapool.firebase.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class FireBaseParty {
    private FireBasePartyInfo groupInfo;
    private Map<String, Boolean> groupUsers;
}
