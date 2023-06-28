package capstone.bapool.utils;

import capstone.bapool.restaurant.dto.Menu;
import capstone.bapool.utils.dto.ImgUrlAndMenu;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
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

    public SeleniumService(@Value("${selenium.path}") String path){
        WEB_DRIVER_PATH = path;
        setDriver();
//        webDriverWait = new WebDriverWait(driver, Duration.ofMillis(500));
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
        this.driver =  new ChromeDriver(options);
//        this.driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
//        this.driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(5));
//        this.driver.manage().timeouts().implicitlyWait(Duration.ofMillis(300));
    }


    // 메뉴, 이미지 한번에 크롤링하기
    public ImgUrlAndMenu findImgUrlAndMenu(String url){
        driver.get(url) ;

        System.out.println("driver.getTitle() = " + driver.getTitle());

        this.driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
        String imgUrl = findImgUrl();
        this.driver.manage().timeouts().implicitlyWait(Duration.ofMillis(0));

        return new ImgUrlAndMenu(imgUrl, findMenu());
    }

    // 메뉴 크롤링
    private List<Menu> findMenu(){

        List<Menu> menus = new ArrayList<>();
        try{
            // 메뉴 크롤링하기
            WebElement searchLabel = driver.findElement(By.className("list_menu"));
            List<WebElement> crawlingMenus = searchLabel.findElements(By.className("photo_type")); // 이미지 있는 메뉴판
            if(crawlingMenus.isEmpty()){ // 이미지 없는 메뉴판
                crawlingMenus = searchLabel.findElements(By.className("nophoto_type"));
            }
            System.out.println("crawlingMenus.size() = " + crawlingMenus.size());

            // 크롤링한 메뉴 dto클래스에 담기
            for(WebElement menu : crawlingMenus){
                String menuName = menu.findElement(By.className("loss_word")).getText();
                String menuPrice = menu.findElement(By.className("price_menu")).getText();

                if(menuName.isBlank()){
                    break;
                }
                System.out.println("menuPrice = " + menuPrice);
                System.out.println("menuName = " + menuName);

                menus.add(new Menu(menuName, menuPrice));
            }
        } catch (Exception e){
            System.out.println("no menu");
        }

        return menus;
    }

    // 이미지 크롤링
    private String findImgUrl(){
        try{
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

    private void quitDriver() {
        driver.quit(); // webDriver 종료
    }
}
