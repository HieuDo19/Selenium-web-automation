package tests.global.footer;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import test_flows.global.FooterTestFlow;
import tests.BaseTest;

public class FooterTest extends BaseTest {

    //    public static void main(String[] args) {
//        WebDriver driver = DriverFactory.getWebDriver();
//        try {
//            driver.get("https://demowebshop.tricentis.com/");
//            FooterTestFlow footerTestFlow = new FooterTestFlow(driver);
//            footerTestFlow.verifyFooterComponent();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            driver.quit();
//        }
    @Test
    public void testHomePageFooter() {
        WebDriver driver = getDriver();
        driver.get("https://demowebshop.tricentis.com/");
        FooterTestFlow footerTestFlow = new FooterTestFlow(driver);
        footerTestFlow.verifyFooterComponent();
    }

    @Test
    public void testCategoryPageFooter() {
        WebDriver driver = getDriver();
        driver.get("https://demowebshop.tricentis.com/");
        FooterTestFlow footerTestFlow = new FooterTestFlow(driver);
        footerTestFlow.verifyProductCatFooterComponent();
    }
}
