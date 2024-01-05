package test_flows.computer;

import models.components.cart.CartItemRowComponent;
import models.components.cart.TotalComponent;
import models.components.checkout.BillingAddressComponent;
import models.components.checkout.PaymentInformationComponent;
import models.components.checkout.PaymentMethodComponent;
import models.components.checkout.ShippingMethodComponent;
import models.components.order.ComputerEssentialComponent;
import models.pages.CheckoutOptionPage;
import models.pages.CheckoutPage;
import models.pages.ComputerItemDetailsPage;
import models.pages.ShoppingCartPage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import test_data.CreditCardType;
import test_data.DataObjectBuilder;
import test_data.PaymentMethod;
import test_data.computer.ComputerData;
import test_data.user.UserDataObject;

import java.security.SecureRandom;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderComputerFlow<T extends ComputerEssentialComponent> {

    private Class<T> computerEssentialCompClass;
    private WebDriver driver;
    private ComputerData computerData;
    private double itemTotalPrice;
    private int quantity;
    private UserDataObject defaultCheckoutUser;
    private PaymentMethod paymentMethod;
    private CreditCardType creditCardType;

    public OrderComputerFlow(WebDriver driver, Class<T> computerEssentialCompClass, ComputerData computerData) {
        this.computerEssentialCompClass = computerEssentialCompClass;
        this.driver = driver;
        this.computerData = computerData;
        this.quantity = 1;
    }

    public OrderComputerFlow(WebDriver driver, Class<T> computerEssentialCompClass, ComputerData computerData, int quantity) {
        this.computerEssentialCompClass = computerEssentialCompClass;
        this.driver = driver;
        this.computerData = computerData;
        this.quantity = quantity;
    }

    public void buildCompSpec() {
        ComputerEssentialComponent computerEssentialComponent = new ComputerItemDetailsPage(driver).computerComp(computerEssentialCompClass);
        computerEssentialComponent.unselectDefaultOptions();
        String processorFullStr = computerEssentialComponent.selectProcessorType(this.computerData.getProcessor());
        double processorAdditionalPrice = extractAdditionalPrice(processorFullStr);
        String ramFullStr = computerEssentialComponent.selectRAMType(this.computerData.getRam());
        double ramAdditionalPrice = extractAdditionalPrice(ramFullStr);
        String hddFullStr = computerEssentialComponent.selectHDD(this.computerData.getHdd());
        double hddAdditionalPrice = extractAdditionalPrice(hddFullStr);
        String softwareFullStr = computerEssentialComponent.selectSoftware(this.computerData.getSoftware());
        double softwareAdditionalPrice = extractAdditionalPrice(softwareFullStr);
        double osAdditionalPrice = 0;
        String osDataOption = this.computerData.getOs();
        if (osDataOption != null) {
            String osFullStr = computerEssentialComponent.selectOS(osDataOption);
            osAdditionalPrice = extractAdditionalPrice(osFullStr);
        }
        double additionalPrice = processorAdditionalPrice + osAdditionalPrice;

        computerEssentialComponent.setProductQuantitySel(this.quantity);

        this.itemTotalPrice = (computerEssentialComponent.productPrice() + additionalPrice) * this.quantity;
    }

    public void addItemToCart() {
        ComputerItemDetailsPage computerItemDetailsPage = new ComputerItemDetailsPage(driver);
        ComputerEssentialComponent computerEssentialComponent = computerItemDetailsPage.computerComp(computerEssentialCompClass);
        computerEssentialComponent.clickOnAddCartBtn();
        computerEssentialComponent.waitUntilItemAddedToCart();
        computerItemDetailsPage.headerComponent().clickOnShoppingCartLink();
    }

    public void verifyShoppingCartPage() {
        ShoppingCartPage shoppingCartPage = new ShoppingCartPage(driver);
        List<CartItemRowComponent> cartItemRowComponents = shoppingCartPage.cartItemRowComponents();
        Assert.assertFalse(cartItemRowComponents.isEmpty(), "[ERR] There is no items displayed in the shopping cart!");
        double currentSubTotals = 0;
        double currentTotalUnitPrices = 0;
        for (CartItemRowComponent cartItemRowComponent : cartItemRowComponents) {
            currentSubTotals = currentSubTotals + cartItemRowComponent.subTotal();
            currentTotalUnitPrices = currentTotalUnitPrices + (cartItemRowComponent.itemQuantity() * cartItemRowComponent.unitPrice());
        }
        Assert.assertEquals(currentSubTotals, currentTotalUnitPrices, "[ERR] shopping cart sub-total is incorrect!");

        TotalComponent totalComponent = shoppingCartPage.totalComponent();
        Map<String, Double> priceCategories = totalComponent.priceCategories();
        Assert.assertFalse(priceCategories.keySet().isEmpty(), "[ERR] Checkout price info is empty!");
        double checkoutSubTotal = 0;
        double checkoutTotal = 0;
        double checkoutOtherFees = 0;
        for (String priceType : priceCategories.keySet()) {
            double priceValue = priceCategories.get(priceType);
            if (priceType.startsWith("Sub-Total")) {
                checkoutSubTotal = priceValue;
            } else if (priceType.startsWith("Total")) {
                checkoutTotal = priceValue;
            } else {
                checkoutOtherFees = checkoutOtherFees + priceValue;
            }
        }
        Assert.assertEquals(currentSubTotals, checkoutSubTotal, "[ERR] Checkout sub-total is incorrect");
        Assert.assertEquals(checkoutTotal, (checkoutSubTotal + checkoutOtherFees), "[ERR] Checkout Total is incorrect");
    }

    public void agreeTOSAndCheckout() {
        ShoppingCartPage shoppingCartPage = new ShoppingCartPage(driver);
        TotalComponent totalComponent = shoppingCartPage.totalComponent();
        totalComponent.agreeTOS();
        totalComponent.clickOnCheckoutBtn();

        new CheckoutOptionPage(driver).checkoutAsGuest();
    }

    public void inputBillingAddress() {
        String defaultCheckoutUserDataLOC = "/src/main/java/test_data/user/DefaultCheckoutUser.json";
        this.defaultCheckoutUser = DataObjectBuilder.buildDataObjectFrom(defaultCheckoutUserDataLOC, UserDataObject.class);
        CheckoutPage checkoutPage = new CheckoutPage(driver);
        BillingAddressComponent billingAddressComponent = checkoutPage.billingAddressComponent();
        billingAddressComponent.selectInputNewAddress();
        billingAddressComponent.inputFirstName(defaultCheckoutUser.getFirstName());
        billingAddressComponent.inputLastName(defaultCheckoutUser.getLastName());
        billingAddressComponent.inputEmail(defaultCheckoutUser.getEmail());
        billingAddressComponent.selectCountry(defaultCheckoutUser.getCountry());
        billingAddressComponent.selectState(defaultCheckoutUser.getState());
        billingAddressComponent.inputCity(defaultCheckoutUser.getCity());
        billingAddressComponent.inputAdd1(defaultCheckoutUser.getAdd1());
        billingAddressComponent.inputZipCode(defaultCheckoutUser.getZipcode());
        billingAddressComponent.inputPhoneNo(defaultCheckoutUser.getPhoneNumber());
        billingAddressComponent.clickOnContinueBtn();
    }

    public void inputShippingAddress() {
        new CheckoutPage(driver).shippingAddressComponent().clickOnContinueBtn();
    }

    public void selectShippingMethod() {
        List<String> shippingMethods = Arrays.asList("Ground", "Next Day Air", "2nd Day Air");
        int randomShippingMethodIndex = new SecureRandom().nextInt(shippingMethods.size());
        String randomShippingMethod = shippingMethods.get(randomShippingMethodIndex);
        CheckoutPage checkoutPage = new CheckoutPage(driver);
        ShippingMethodComponent shippingMethodComponent = checkoutPage.shippingMethodComponent();
        shippingMethodComponent.selectShippingMethod(randomShippingMethod);
        shippingMethodComponent.clickOnContinueBtn();
    }

    public void selectPaymentMethod() {
        this.paymentMethod = PaymentMethod.COD;
        CheckoutPage checkoutPage = new CheckoutPage(driver);
        PaymentMethodComponent paymentMethodComponent = checkoutPage.paymentMethodComponent();
        paymentMethodComponent.selectCODMethod();
    }

    public void selectPaymentMethod(PaymentMethod paymentMethod) {
        Assert.assertNotNull(paymentMethod, "[ERR] payment method type can't be null");
        this.paymentMethod = paymentMethod;
        CheckoutPage checkoutPage = new CheckoutPage(driver);
        PaymentMethodComponent paymentMethodComponent = checkoutPage.paymentMethodComponent();
        switch (paymentMethod) {
            case CHECK_MONEY_ORDER:
                paymentMethodComponent.selectMoneyOrderMethod();
                break;
            case CREDIT_CARD:
                paymentMethodComponent.selectCreditCardMethod();
                break;
            case PURCHASE_ORDER:
                paymentMethodComponent.selectPurchaseOrderMethod();
                break;
            default:
                paymentMethodComponent.selectCODMethod();
        }
        paymentMethodComponent.clickOnContinueBtn();
    }

    public void inputPaymentInfo(CreditCardType creditCardType) {
        this.creditCardType = creditCardType;
        CheckoutPage checkoutPage = new CheckoutPage(driver);
        PaymentInformationComponent paymentInformationComponent = checkoutPage.paymentInformationComponent();
        if (this.paymentMethod.equals(PaymentMethod.PURCHASE_ORDER)) {
            paymentInformationComponent.inputPurchaseNum("123");
        } else if (this.paymentMethod.equals(PaymentMethod.CREDIT_CARD)) {
            paymentInformationComponent.selectCardType(creditCardType);
            String cardHolderFirstName = this.defaultCheckoutUser.getFirstName();
            String cardHolderLastName = this.defaultCheckoutUser.getLastName();
            paymentInformationComponent.inputCardHolderName(cardHolderFirstName + " " + cardHolderLastName);
            String cardNumber = creditCardType.equals(CreditCardType.VISA) ? "4012888888881881" : "6011000990139424";
            paymentInformationComponent.inputCardNumber(cardNumber);

            Calendar calendar = new GregorianCalendar();

//            paymentInformationComponent.inputExpireMonth(String.valueOf(calendar.get(Calendar.MONTH) + 1));
            paymentInformationComponent.inputExpireMonth("11");
//            paymentInformationComponent.inputExpireYear(String.valueOf(calendar.get(Calendar.YEAR) + 1));
            paymentInformationComponent.inputExpireYear("2026");
            paymentInformationComponent.inputCardCode("123");
            paymentInformationComponent.clickOnContinueBtn();
        } else if (this.paymentMethod.equals(PaymentMethod.COD)) {

        } else {

        }
    }

    public void confirmOrder() {
        new CheckoutPage(driver).confirmOrderComponent().clickOnConfirmBtn();
    }

    private double extractAdditionalPrice(String optionStr) {
        double price = 0;
        Pattern pattern = Pattern.compile("\\[(.*?)\\]");
        Matcher matcher = pattern.matcher(optionStr);
        if (matcher.find()) {
            price = Double.parseDouble(matcher.group(1).replaceAll("[+-]", ""));
        }
        return price;
    }
}
