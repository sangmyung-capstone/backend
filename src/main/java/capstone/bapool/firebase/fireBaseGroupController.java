package capstone.bapool.firebase;

import capstone.bapool.config.error.BaseException;
import capstone.bapool.firebase.dto.FireBaseParty;
import capstone.bapool.firebase.dto.FireBasePartyInfo;
import capstone.bapool.firebase.dto.FireBaseUser;
import capstone.bapool.firebase.dto.PartyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/firebase/groups")
@RequiredArgsConstructor
public class fireBaseGroupController {
    private final FireBaseGroupDao firebaseGroupDao;

    @PostMapping("")
    public ResponseEntity<String> save(@RequestBody PartyDto partyDto) throws BaseException {
        firebaseGroupDao.saveGroupInfo(partyDto.getFireBaseGroupInfo(), partyDto.getUserId());
        return ResponseEntity.ok().body("성공");
    }

}
