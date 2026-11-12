package ru.praktikum.qa.scooter.pageobject;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.time.Duration;

public class HomePage {
    public static final String URL = "https://qa-scooter.praktikum-services.ru/";

    private final WebDriver driver;

    // Логотип Яндекса
    private final By yandexLogo = By.cssSelector("a[href*='yandex.ru']");
    // Логотип Самоката
    private final By scooterLogo = By.cssSelector("a[href='/']");
    // Кнопка «Заказать» в шапке
    private final By topOrderButton = By.xpath("(//button[normalize-space()='Заказать'])[1]");
    // Кнопка «Статус заказа»
    private final By orderStatusButton = By.xpath("//button[normalize-space()='Статус заказа']");
    // Поле номера заказа
    private final By orderNumberInput = By.cssSelector("input[placeholder='Введите номер заказа']");
    // Кнопка «Go!»
    private final By goButton = By.xpath("//button[normalize-space()='Go!']");
    // Кнопка «Заказать» внизу страницы
    private final By bottomOrderButton = By.xpath("(//button[normalize-space()='Заказать'])[2]");
    // Заголовки вопросов раздела «Вопросы о важном»
    private final By faqHeadings = By.cssSelector("[id^='accordion__heading-']");

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    public void open() {
        driver.get(URL);
    }

    public void acceptCookies() {
        List<WebElement> buttons = driver.findElements(By.id("rcc-confirm-button"));
        if (!buttons.isEmpty() && buttons.get(0).isDisplayed()) {
            buttons.get(0).click();
        }
    }

    public void clickOrderButton(int buttonIndex) {
        By locator = buttonIndex == 0 ? topOrderButton : bottomOrderButton;
        WebElement button = new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.elementToBeClickable(locator));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", button);
        button.click();
    }

    public void clickFaq(int index) {
        WebElement heading = driver.findElements(faqHeadings).get(index);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", heading);
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOf(heading));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", heading);
    }

    public String faqAnswer(int index) {
        return new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("accordion__panel-" + index)))
                .getText();
    }

    public String faqHeading(int index) {
        return driver.findElements(faqHeadings).get(index).getText();
    }
}
