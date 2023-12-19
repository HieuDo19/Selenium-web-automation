package models.pages;

import models.components.Component;
import models.components.global.CategoryItemComponent;
import models.components.global.footer.FooterComponent;
import models.components.global.footer.HeaderComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

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

    public HeaderComponent headerComponent() {
        return findComponent(HeaderComponent.class);
    }

    public List<CategoryItemComponent> categoryItemComponents() {
        return findComponents(CategoryItemComponent.class);
    }
}
