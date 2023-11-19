package tests;

import driver.DriverFactory;
import models.pages.LoginPageModel03;
import org.openqa.selenium.WebDriver;

public class POMModel03Tests {

    public static void main(String[] args) {

        WebDriver driver = DriverFactory.getWebDriver();
        driver.get("https://the-internet.herokuapp.com/login");
        LoginPageModel03 loginPage = new LoginPageModel03(driver);
        loginPage
                .inputUsername("abc@gmail.com")
                .inputPassword("abc@123")
                .clickOnLoginBtn();
    }
}
