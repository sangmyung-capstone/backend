package capstone.bapool;

import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/info")
    public ResponseEntity<String> test() throws Exception {
//        return ResponseEntity.ok().body("성공");
        throw new Exception();
    }
}
