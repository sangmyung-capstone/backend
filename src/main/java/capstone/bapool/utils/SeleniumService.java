package capstone.bapool.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
@Service
public class SeleniumService {

    public SeleniumService(){
        chrome();
    }

    private WebDriver driver;
    public static String WEB_DRIVER_ID = "webdriver.chrome.driver"; // Properties 설정
    public static String WEB_DRIVER_PATH = "C:\\Program Files (x86)\\chromedriver_win32\\chromedriver.exe"; // WebDriver 경로

    private void chrome() {
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);

        // webDriver 옵션 설정.
        ChromeOptions options = new ChromeOptions();
        options.setHeadless(true);
        options.addArguments("--lang=ko");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.setCapability("ignoreProtectedModeSettings", true);
        options.addArguments("--remote-allow-origins=*"); //cors문제

        // weDriver 생성.
        driver = new ChromeDriver(options);
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
    }

    public String useDriver(String url) {
        driver.get(url) ;
        driver.manage().timeouts().implicitlyWait(50, TimeUnit.MILLISECONDS);  // 페이지 불러오는 여유시간.
        System.out.println("driver.getTitle() = " + driver.getTitle());

        //여기서 이미지 크롤링한다
        try{
            //        WebElement searchLabel = driver.findElement(By.id("kakaoBtnSearch"));
            WebElement searchLabel = driver.findElement(By.className("bg_present"));
            System.out.println("searchLabel.getAttribute(\"style\") = " + searchLabel.getAttribute("style"));
            String imgUrlBefore = searchLabel.getAttribute("style");
            String imgUrlAfter = imgUrlBefore.substring(imgUrlBefore.indexOf('"')+1, imgUrlBefore.lastIndexOf('"'));
            System.out.println("imgUrlAfter = " + imgUrlAfter);
//        System.out.println("searchLabel = " + searchLabel);
//        System.out.println("searchLabel.getText() = " + searchLabel.getText());

            return imgUrlAfter;
        }catch (Exception e){
            System.out.println("no image");
            return null;
        }
    }

    private void quitDriver() {
        driver.quit(); // webDriver 종료
    }
}
