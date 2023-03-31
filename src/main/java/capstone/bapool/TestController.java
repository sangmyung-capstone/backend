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
    public String test(){
        return "밥풀 테스트 요청 성공";
    }

    @GetMapping("/info")
    public ResponseEntity<String> test() throws Exception {
        return ResponseEntity.ok().body("test");
    }

    @GetMapping("/list")
    public TestDto testList(){

        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        TestDto testDto = new TestDto("최테스트", "언더스코어가 적용될지", list);

        return testDto;
    }

}
