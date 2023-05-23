package capstone.bapool.firebase;

import capstone.bapool.config.error.BaseException;
import capstone.bapool.firebase.dto.FireBasePartyInfo;
import capstone.bapool.firebase.dto.PartyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/firebase/groups")
@RequiredArgsConstructor
public class fireBaseGroupController {
    private final FireBaseGroupRepository firebaseGroupRepository;

    @PostMapping("")
    public ResponseEntity<String> save(@RequestBody PartyDto partyDto) throws BaseException {
        firebaseGroupRepository.saveGroupInfo(partyDto.getFireBaseGroupInfo(), partyDto.getUserId(), partyDto.getGroupId());
        return ResponseEntity.ok().body("성공");
    }

    @PatchMapping("/{groupId}")
    public ResponseEntity<String> update(@RequestBody FireBasePartyInfo fireBasePartyInfo, @PathVariable Long groupId) {
        firebaseGroupRepository.updateGroupInfo(fireBasePartyInfo, groupId);
        return ResponseEntity.ok().body("성공");
    }

    @PatchMapping("/{partyId}/{userId}/{curNumberOfPeople}")
    public ResponseEntity<String> participate(@PathVariable Long partyId, @PathVariable Long userId, @PathVariable Integer curNumberOfPeople) {
        firebaseGroupRepository.participate(partyId, userId, curNumberOfPeople);
        return ResponseEntity.ok().body("성공");
    }

    @DeleteMapping("/{partyId}")
    public ResponseEntity<String> delete(@PathVariable Long partyId) {
        firebaseGroupRepository.delete(partyId);
        return ResponseEntity.ok().body("성공");
    }

    @DeleteMapping("/{partyId}/{userId1}/{userId2}")
    public ResponseEntity<String> secessionLeader(@PathVariable Long partyId, @PathVariable Long userId1, @PathVariable Long userId2) {
        firebaseGroupRepository.secession(partyId, userId1, 3);
        firebaseGroupRepository.becomeLeader(partyId, userId2);
        return ResponseEntity.ok().body("성공");
    }


}
