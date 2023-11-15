package api_learning;

import driver.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LinkTextInteraction {
    //Declare constants
    private static final String TARGET_URL = "https://the-internet.herokuapp.com/login";

    public static void main(String[] args) {
        WebDriver driver = DriverFactory.getWebDriver();
        driver.get(TARGET_URL);
        try {
//            By powerByLinkTextSel = By.linkText("Elemental Selenium");
            By powerByPartialLinkTextSel = By.partialLinkText("Elemental");
//            WebElement powerByLinkTextEle = driver.findElement(powerByPartialLinkTextSel);
            WebElement powerByPartialLinkTextEle = driver.findElement(powerByPartialLinkTextSel);
            powerByPartialLinkTextEle.click();

            try {
                Thread.sleep(2000);
            } catch (Exception ignored) {
            }

            //Navigate back
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}