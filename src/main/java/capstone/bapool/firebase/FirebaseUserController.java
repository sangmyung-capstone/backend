package capstone.bapool.firebase;


import capstone.bapool.config.error.BaseException;
import capstone.bapool.firebase.dto.FireBaseUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/firebase/users")
@RequiredArgsConstructor
public class FirebaseUserController {
    private final FireBaseUserRepository fireBaseUserRepository;

    @PostMapping("")
    public ResponseEntity<String> save(@RequestBody FireBaseUser fireBaseUser) throws BaseException {
        fireBaseUserRepository.save(fireBaseUser);
        return ResponseEntity.ok().body("성공");
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> delete(@PathVariable Long userId) throws BaseException {
        fireBaseUserRepository.delete(userId);
        return ResponseEntity.ok().body("성공");
    }

    @PostMapping("/bannedUser/{userId}/{bannedUser}")
    public ResponseEntity<String> update(@PathVariable Long userId, @PathVariable Long bannedUser) throws BaseException {
        fireBaseUserRepository.addBannedUser(userId, bannedUser);
        return ResponseEntity.ok().body("성공");
    }

    @DeleteMapping("/bannedUser/{userId}/{bannedUser}")
    public ResponseEntity<String> deleteBannedUser(@PathVariable Long userId, @PathVariable Long bannedUser) throws BaseException {
        fireBaseUserRepository.removeBannedUser(userId, bannedUser);
        return ResponseEntity.ok().body("성공");
    }

}
