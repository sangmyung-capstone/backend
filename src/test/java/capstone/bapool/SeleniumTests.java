package capstone.bapool;

import capstone.bapool.utils.SeleniumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SeleniumTests {
    
    @Autowired
    private SeleniumUtils seleniumUtils;
    
//    @Test
//    public void testCrawlingImgUrl(){
//        System.out.println("hello");
//
//        String imgUrl = seleniumService.testCrawling("https://place.map.kakao.com/1470337852");
//        seleniumService.testCrawling("https://place.map.kakao.com/7943191");
//        seleniumService.testCrawling("https://place.map.kakao.com/1470337852");
//        seleniumService.testCrawling("https://place.map.kakao.com/7943191");
//        seleniumService.testCrawling("https://place.map.kakao.com/1470337852");
//        seleniumService.testCrawling("https://place.map.kakao.com/7943191");
//        seleniumService.testCrawling("https://place.map.kakao.com/1470337852");
//        seleniumService.testCrawling("https://place.map.kakao.com/7943191");
//
//        System.out.println("imgUrl = " + imgUrl);
//    }
//
//    @Test
//    public void testCrawlingMenu(){
//        seleniumService.findMenu("https://place.map.kakao.com/1470337852");
//    }
}
