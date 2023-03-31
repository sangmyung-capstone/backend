package capstone.bapool.firebase;


import capstone.bapool.config.error.BaseException;
import capstone.bapool.config.response.ResponseDto;
import capstone.bapool.firebase.dto.FireBaseUser;
import capstone.bapool.user.dto.ReissueRes;
import capstone.bapool.user.dto.SocialAccessToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/firebase")
@RequiredArgsConstructor
public class FirebaseController {
    private final FirebaseService firebaseService;

    @PostMapping("")
    public ResponseEntity<String> saveUser(@RequestBody FireBaseUser fireBaseUser) throws BaseException, IOException {
        firebaseService.save(fireBaseUser);
        return ResponseEntity.ok().body("성공");
    }

    @PatchMapping("")
    public ResponseEntity<String> updateUser(@RequestBody FireBaseUser fireBaseUser) throws BaseException, IOException {
        firebaseService.update(fireBaseUser);
        return ResponseEntity.ok().body("성공");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<String> readUser(@PathVariable("userId") Long userId) throws BaseException, IOException {
        firebaseService.read(userId);
        return ResponseEntity.ok().body("성공");
    }
}
