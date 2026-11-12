package ru.praktikum.qa.scooter;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

final class DriverFactory {
    private DriverFactory() {
    }

    static WebDriver create() {
        String browser = System.getProperty("browser", "chrome").toLowerCase();
        if ("firefox".equals(browser)) {
            FirefoxOptions options = new FirefoxOptions();
            if (Boolean.parseBoolean(System.getProperty("headless", "true"))) {
                options.addArguments("-headless");
            }
            WebDriver driver = new FirefoxDriver(options);
            driver.manage().window().setSize(new Dimension(1440, 1000));
            return driver;
        }
        ChromeOptions options = new ChromeOptions();
        if (Boolean.parseBoolean(System.getProperty("headless", "true"))) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--window-size=1440,1000", "--disable-gpu", "--no-sandbox");
        return new ChromeDriver(options);
    }
}
