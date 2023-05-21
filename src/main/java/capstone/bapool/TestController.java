package capstone.bapool;


import lombok.RequiredArgsConstructor;
import capstone.bapool.config.error.BaseException;
import capstone.bapool.config.error.StatusEnum;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {


    @GetMapping("/log")
    public String test() {
        return "밥풀 테스트 요청 성공";
    }

    @GetMapping("/info")
    public ResponseEntity<String> testinfo() throws Exception {
        return ResponseEntity.ok().body("test");
    }

}
