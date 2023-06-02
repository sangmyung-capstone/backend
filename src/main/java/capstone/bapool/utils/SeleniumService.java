package capstone.bapool.utils;

import capstone.bapool.restaurant.dto.Menu;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class SeleniumService {

    private WebDriver driver;
    public static String WEB_DRIVER_ID = "webdriver.chrome.driver"; // Properties 설정
    public static String WEB_DRIVER_PATH; // WebDriver 경로
    WebDriverWait webDriverWait;

    public SeleniumService(@Value("${selenium.path}") String path){
        WEB_DRIVER_PATH = path;
        setDriver();
    }

    private void setDriver() {
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);

        // webDriver 옵션 설정.
        ChromeOptions options = new ChromeOptions();
        options.setHeadless(true);
        options.addArguments("--lang=ko");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.setCapability("ignoreProtectedModeSettings", true);
        options.addArguments("--disable-popup-blocking"); //팝업안띄움
        options.addArguments("headless"); //브라우저 안띄움
        options.addArguments("--disable-gpu"); //gpu 비활성화
        options.addArguments("--remote-allow-origins=*"); //cors문제

        // weDriver 생성.
        driver = new ChromeDriver(options);
//        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        webDriverWait = new WebDriverWait(driver, Duration.ofMillis(500));
    }

    public String testCrawling(String url) {
        driver.get(url) ;

        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);  // 페이지 불러오는 여유시간.
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

    // 이미지url 크롤링
    public String findImgUrl(String url){
        driver.get(url) ;
        System.out.println("driver.getTitle() = " + driver.getTitle());

        //여기서 이미지 크롤링한다
        try{
            webDriverWait.until( // 해당 요소가 나올때까지 기다림
                    ExpectedConditions.presenceOfElementLocated(By.className("bg_present"))
            );
            WebElement searchLabel = driver.findElement(By.className("bg_present"));
//            System.out.println("searchLabel.getAttribute(\"style\") = " + searchLabel.getAttribute("style"));
            String imgUrlBefore = searchLabel.getAttribute("style");
            String imgUrlAfter = imgUrlBefore.substring(imgUrlBefore.indexOf('"')+1, imgUrlBefore.lastIndexOf('"'));
            System.out.println("imgUrlAfter = " + imgUrlAfter);

            return imgUrlAfter;
        }catch (Exception e){
            System.out.println("no image");
            return null;
        }
    }

    public List<Menu> findMenu(String url){
        driver.get(url) ;
        System.out.println("driver.getTitle() = " + driver.getTitle());

        //여기서 이미지 크롤링한다
        try{
            webDriverWait.until( // 해당 요소가 나올때까지 기다림
                    ExpectedConditions.presenceOfElementLocated(By.className("list_menu"))
            );

            List<Menu> menus = new ArrayList<>();

            WebElement searchLabel = driver.findElement(By.className("list_menu"));
            List<WebElement> menuList = searchLabel.findElements(By.className("photo_type"));
            System.out.println("menuList.size() = " + menuList.size());
            for(WebElement menu : menuList){
                String menuName = menu.findElement(By.className("loss_word")).getText();
                String menuPrice = menu.findElement(By.className("price_menu")).getText();

                if(menuName.isBlank()){
                    break;
                }
                System.out.println("menuPrice = " + menuPrice);
                System.out.println("menuName = " + menuName);

                menus.add(new Menu(menuName, menuPrice));
            }

            return menus;
        }catch (Exception e){
            System.out.println("no image");
            return null;
        }
    }

    private void quitDriver() {
        driver.quit(); // webDriver 종료
    }
}
