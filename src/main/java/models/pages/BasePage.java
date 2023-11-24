package models.pages;

import models.components.Component;
import models.components.global.footer.FooterComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class BasePage extends Component {

    //    private final WebDriver driver;
    protected final WebDriver driver;

    public BasePage(WebDriver driver) {
        super(driver, driver.findElement(By.tagName("html")));
        this.driver = driver;
    }

    //SELECTORS


    //METHODS

    public FooterComponent footerComponent() {
//        return new FooterComponent(this.driver);
        return findComponent(FooterComponent.class);
    }
}
