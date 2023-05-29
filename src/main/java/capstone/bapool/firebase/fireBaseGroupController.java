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
    private final FireBasePartyRepository firebasePartyRepository;

    @PostMapping("")
    public ResponseEntity<String> save(@RequestBody PartyDto partyDto) throws BaseException {
        firebasePartyRepository.save(partyDto.getFireBaseGroupInfo(), partyDto.getUserId(), partyDto.getGroupId());
        return ResponseEntity.ok().body("성공");
    }

    @PatchMapping("/{groupId}")
    public ResponseEntity<String> update(@RequestBody FireBasePartyInfo fireBasePartyInfo, @PathVariable Long groupId) {
        firebasePartyRepository.update(fireBasePartyInfo, groupId);
        return ResponseEntity.ok().body("성공");
    }

    @PatchMapping("/{partyId}/{userId}/{curNumberOfPeople}")
    public ResponseEntity<String> participate(@PathVariable Long partyId, @PathVariable Long userId, @PathVariable Integer curNumberOfPeople) {
        firebasePartyRepository.participate(partyId, userId, curNumberOfPeople);
        return ResponseEntity.ok().body("성공");
    }

    @DeleteMapping("/{partyId}")
    public ResponseEntity<String> delete(@PathVariable Long partyId) {
        firebasePartyRepository.delete(partyId);
        return ResponseEntity.ok().body("성공");
    }

    @DeleteMapping("/{partyId}/{userId1}/{userId2}")
    public ResponseEntity<String> secessionLeader(@PathVariable Long partyId, @PathVariable Long userId1, @PathVariable Long userId2) {
        firebasePartyRepository.secession(partyId, userId1, 3);
        firebasePartyRepository.becomeLeader(partyId, userId2);
        return ResponseEntity.ok().body("성공");
    }


}
