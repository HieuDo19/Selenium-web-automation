package api_learning;

import driver.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class FormInteraction {
    //Declare constants
    private static final String TARGET_URL = "https://the-internet.herokuapp.com/login";
    private static final By USERNAME_SEL = By.id("username");
    private static final By PASSWORD_SEL = By.cssSelector("#password");
    private static final By LOGIN_BTN_SEL = By.cssSelector("#login [type='submit']");
    private static final String USERNAME_STR = "tomsmith";
    private static final String PASSWORD_STR = "SuperSecretPassword";

    //main method
    public static void main(String[] args) {
        WebDriver driver = DriverFactory.getWebDriver();
        driver.get(TARGET_URL);

        try {
            WebElement userNameEle = driver.findElement(USERNAME_SEL);
            WebElement passWordEle = driver.findElement(PASSWORD_SEL);

            //Refresh page || Assume this action triggers reload DOM
            driver.navigate().refresh();

            userNameEle = driver.findElement(USERNAME_SEL);
            passWordEle = driver.findElement(PASSWORD_SEL);

            WebElement loginBtn = driver.findElement(LOGIN_BTN_SEL);
            // Input username and password
            userNameEle.sendKeys(USERNAME_STR);
            passWordEle.sendKeys(PASSWORD_STR);
            //Click on login button
            loginBtn.click();

            //Navigate back
            driver.navigate().back();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
