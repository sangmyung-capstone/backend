package capstone.bapool;

import capstone.bapool.utils.SeleniumService;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@SpringBootTest
public class SeleniumTests {
    
    @Autowired
    private SeleniumService seleniumService;
    
    @Test
    public void testCrawlingImgUrl(){
        System.out.println("hello");

        String imgUrl = seleniumService.testCrawling("https://place.map.kakao.com/1470337852");
        seleniumService.testCrawling("https://place.map.kakao.com/7943191");
        seleniumService.testCrawling("https://place.map.kakao.com/1470337852");
        seleniumService.testCrawling("https://place.map.kakao.com/7943191");
        seleniumService.testCrawling("https://place.map.kakao.com/1470337852");
        seleniumService.testCrawling("https://place.map.kakao.com/7943191");
        seleniumService.testCrawling("https://place.map.kakao.com/1470337852");
        seleniumService.testCrawling("https://place.map.kakao.com/7943191");

        System.out.println("imgUrl = " + imgUrl);
    }

    @Test
    public void testCrawlingMenu(){
        seleniumService.findMenu("https://place.map.kakao.com/1470337852");
    }
}
