package test_flows.global;

import models.components.global.TopMenuComponent;
import models.components.global.footer.*;
import models.pages.BasePage;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FooterTestFlow {

    private final WebDriver driver;

    public FooterTestFlow(WebDriver driver) {
        this.driver = driver;
    }

    public void verifyFooterComponent() {
        BasePage basePage = new BasePage(this.driver);
        InformationColumnComponent informationColumnComponent = basePage.footerComponent().informationColumnComponent();
        CustomerServiceColumnComponent customerServiceColumnComponent = basePage.footerComponent().customerServiceColumnComponent();
        MyAccountColumnComponent myAccountColumnComponent = basePage.footerComponent().myAccountColumnComponent();
        FollowUsColumnComponent followUsColumnComponent = basePage.footerComponent().followUsColumnComponent();
        verifyInformationColumn(informationColumnComponent);
//        verifyCustomerServiceColumn();
//        verifyMyAccountColumn();
//        verifyFollowUsColumn();
    }

    public void verifyProductCatFooterComponent() {
        //Random pickup MainItem from TopMenuComponent
        BasePage basePage = new BasePage(driver);
        TopMenuComponent topMenuComponent = basePage.topMenuComponent();
        List<TopMenuComponent.MainCatItem> mainCatsElem = topMenuComponent.mainItemsElem();
        Assert.assertFalse(mainCatsElem.isEmpty(), "[ERR] There is no item on top menu");
        int randomMainItemIndex = new SecureRandom().nextInt(mainCatsElem.size());
        TopMenuComponent.MainCatItem randomMainItemElem = mainCatsElem.get(randomMainItemIndex);
        String randomCatHref = randomMainItemElem.catItemLinkElem().getAttribute("href");
        randomMainItemElem.catItemLinkElem().click();

        //Make sure we are on the right page | Wait until navigation is done
        try {
            WebDriverWait wait = randomMainItemElem.componentWait();
            wait.until(ExpectedConditions.urlContains(randomCatHref));
        }catch (TimeoutException ignored) {
            Assert.fail("[ERR] Target page is not matched");
        }

        //Call common verify method
        verifyFooterComponent();
    }

    private void verifyInformationColumn(FooterColumnComponent informationColumnComponent) {
//        testFooterColumn(informationColumnComponent);
        List<String> expectedLinkTexts = Arrays.asList("Sitemap", "Shipping & Return", "Privacy Notice", "Conditions of Use", "About us", "Contact us");
        List<String> expectedHrefs = Arrays.asList("/sitemap", "/shipping-returns", "/privacy-policy", "/conditions-of-use", "/about-us", "/contactus");
        testFooterColumn(informationColumnComponent, expectedLinkTexts, expectedHrefs);
    }

    private void verifyCustomerServiceColumn(FooterColumnComponent customerServiceColumn) {
        List<String> expectedLinkTexts = new ArrayList<>();
        List<String> expectedHrefs = new ArrayList<>();
        testFooterColumn(customerServiceColumn, expectedLinkTexts, expectedHrefs);
    }

    private void verifyMyAccountColumn(FooterColumnComponent myAccountColumn) {
        List<String> expectedLinkTexts = new ArrayList<>();
        List<String> expectedHrefs = new ArrayList<>();
        testFooterColumn(myAccountColumn, expectedLinkTexts, expectedHrefs);
    }

    private void verifyFollowUsColumn(FooterColumnComponent followUsColumn) {
        List<String> expectedLinkTexts = new ArrayList<>();
        List<String> expectedHrefs = new ArrayList<>();
        testFooterColumn(followUsColumn, expectedLinkTexts, expectedHrefs);
    }

    //    private void testFooterColumn(FooterColumnComponent footerColumnComponent) {
//        System.out.println(footerColumnComponent.headerEle().getText());
//        for (WebElement linkEle : footerColumnComponent.linksEle()) {
//            System.out.println(linkEle.getText() + ": " + linkEle.getAttribute("href"));
//        }
//        System.out.println("=====");
//    }
    private void testFooterColumn(FooterColumnComponent footerColumnComponent, List<String> expectedLinkTexts, List<String> expectedHrefs) {
        List<String> actualLinkTexts = new ArrayList<>();
        List<String> actualHrefs = new ArrayList<>();
        expectedHrefs.replaceAll(originHref -> "https://demowebshop.tricentis.com" + originHref);
        footerColumnComponent.linksEle().forEach(columnItem -> {
            actualLinkTexts.add(columnItem.getText());
            actualHrefs.add(columnItem.getAttribute("href"));
        });
        if (actualLinkTexts.isEmpty() || actualHrefs.isEmpty()) {
            Assert.fail("Footer column texts OR hyperlinks is empty!");
        }
        Assert.assertEquals(actualLinkTexts, expectedLinkTexts, "[ERR] Footer column link texts are different");
        Assert.assertEquals(actualHrefs, expectedHrefs, "[ERR] Footer column hrefs are different");

    }
}
