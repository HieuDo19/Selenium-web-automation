package api_learning;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class LaunchBrowser {
    public static void main(String[] args) {
        WebDriver webDriver = new ChromeDriver();

        //open browser
        webDriver.get("https://sdetpro.com");

        //quit browser
        webDriver.quit();
    }
}
