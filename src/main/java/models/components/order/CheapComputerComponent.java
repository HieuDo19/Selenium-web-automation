package models.components.order;

import io.qameta.allure.Step;
import models.components.ComponentCSSSelector;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

@ComponentCSSSelector(".product-essential")
public class CheapComputerComponent extends ComputerEssentialComponent {

    public CheapComputerComponent(WebDriver driver, WebElement component) {
        super(driver, component);
    }

    @Override
    @Step("Select Processor Type: {type}")
    public String selectProcessorType(String type) {
        /*System.out.println("selectProcessorType CHEAP");
        return null;*/
        return selectComOption(type);
    }

    @Override
    @Step("Select RAM Type: {type}")
    public String selectRAMType(String type) {
        /*System.out.println("selectRAMType CHEAP");
        return null;*/
        return selectComOption(type);
    }
}
