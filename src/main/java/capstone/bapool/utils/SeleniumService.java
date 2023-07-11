package capstone.bapool.utils;

import capstone.bapool.utils.dto.Menu;
import capstone.bapool.utils.dto.ImgURLAndMenu;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
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
        options.addArguments("--disable-gpu"); //gpu 비활성화
        options.addArguments("--remote-allow-origins=*"); //cors문제

        // weDriver 생성.
        this.driver =  new ChromeDriver(options);
//        this.driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
//        this.driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(5));
//        this.driver.manage().timeouts().implicitlyWait(Duration.ofMillis(300));
    }


    /**
     * 주어진 url로부터 이미지와 메뉴를 크롤링함.
     * 만약, 메뉴 또는 이미지가 없거나 크롤링할 수 없는 상황이면 null을 반환함.
     * @param url 식당의 url
     * @return
     */
    public ImgURLAndMenu crawlingImgURLAndMenu(String url){
        driver.get(url) ;

        log.debug("식당 마커정보: '{}'의 이미지, 메뉴 크롤링", driver.getTitle());

        this.driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
        String imgUrl = findImgURL();
        this.driver.manage().timeouts().implicitlyWait(Duration.ofMillis(0));

        return new ImgURLAndMenu(imgUrl, findMenu());
    }

    public String crawlingImgURL(String url){
        driver.get(url);

        this.driver.manage().timeouts().implicitlyWait(Duration.ofMillis(400));
        return findImgURL();
    }

    /**
     * 메뉴 크롤링.
     * 만약 메뉴가 없거나, 크롤링할 수 없는 상태면 null 반환
     * @return Menu 리스트
     */
    private List<Menu> findMenu(){

        List<Menu> menus = new ArrayList<>();
        try{
            // 메뉴 크롤링하기
            WebElement searchLabel = driver.findElement(By.className("list_menu"));
            List<WebElement> crawlingMenus = searchLabel.findElements(By.className("photo_type")); // 이미지 있는 메뉴판
            if(crawlingMenus.isEmpty()){ // 이미지 없는 메뉴판
                crawlingMenus = searchLabel.findElements(By.className("nophoto_type"));
            }
            log.info("메뉴 크롤링: 식당 '{}'의 메뉴 개수={}", driver.getTitle(), crawlingMenus.size());

            // 크롤링한 메뉴 dto클래스에 담기
            for(WebElement menu : crawlingMenus){
                String menuName = menu.findElement(By.className("loss_word")).getText();
                String menuPrice = menu.findElement(By.className("price_menu")).getText();

                if(menuName.isBlank()){
                    break;
                }

                log.debug("메뉴 크롤링: 식당 '{}'의 메뉴={}, 가격={} 크롤링", driver.getTitle(), menuName, menuPrice);

                menus.add(new Menu(menuName, menuPrice));
            }
        } catch (Exception e){
            log.info("이미지 크롤링: 식당 '{}'의 메뉴가 없음", driver.getTitle());
        }

        return menus;
    }

    /**
     * 이미지 크롤링.
     * 만약 식당에 이미지가 없으면 빈 List 반환
     * @return 이미지 url
     */
    private String findImgURL(){
        try{
            WebElement searchLabel = driver.findElement(By.className("bg_present"));
//            System.out.println("searchLabel.getAttribute(\"style\") = " + searchLabel.getAttribute("style"));
            String imgUrlBefore = searchLabel.getAttribute("style");
            String imgUrlAfter = imgUrlBefore.substring(imgUrlBefore.indexOf('"')+1, imgUrlBefore.lastIndexOf('"'));

            log.info("이미지 크롤링: '{}'의 이미지 크롤링={}", driver.getTitle(), imgUrlAfter);

            return imgUrlAfter;
        }catch (Exception e){
            log.info("이미지 크롤링: 식당 '{}'의 이미지가 없음", driver.getTitle());
            return null;
        }
    }

    private void quitDriver() {
        driver.quit(); // webDriver 종료
    }
}
