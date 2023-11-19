package models.pages;

import models.components.FooterComponent;
import org.openqa.selenium.WebDriver;

public class BasePage {

    public final WebDriver driver;

    public BasePage(WebDriver driver) {
        this.driver = driver;
    }

    //SELECTORS



    //METHODS

    public FooterComponent footerComponent() {
        return new FooterComponent(this.driver);
    }
}
