package tests;

import driver.DriverFactory;
import models.pages.LoginPageModel01;
import org.openqa.selenium.WebDriver;

public class POMModel01Tests {

    public static void main(String[] args) {

        WebDriver driver = DriverFactory.getWebDriver();
        driver.get("https://the-internet.herokuapp.com/login");
        LoginPageModel01 loginPage = new LoginPageModel01(driver);
        loginPage.username().sendKeys("abc@gmail.com");
        loginPage.password().sendKeys("abc@123");
        loginPage.loginBtn().click();
    }
}
