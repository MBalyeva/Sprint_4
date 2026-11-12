package ru.praktikum.qa.scooter.pageobject;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;

public class OrderPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Поле «Имя»
    private final By firstNameInput = By.cssSelector("input[placeholder='* Имя']");
    // Поле «Фамилия»
    private final By lastNameInput = By.cssSelector("input[placeholder='* Фамилия']");
    // Поле адреса
    private final By addressInput = By.cssSelector("input[placeholder='* Адрес: куда привезти заказ']");
    // Поле станции метро
    private final By metroInput = By.cssSelector("input[placeholder='* Станция метро']");
    // Поле телефона
    private final By phoneInput = By.cssSelector("input[placeholder='* Телефон: на него позвонит курьер']");
    // Кнопка «Далее»
    private final By nextButton = By.xpath("//button[normalize-space()='Далее']");
    // Поле даты доставки
    private final By deliveryDateInput = By.cssSelector("input[placeholder='* Когда привезти самокат']");
    // Выпадающий список срока аренды
    private final By rentalPeriodDropdown = By.cssSelector(".Dropdown-control");
    // Чекбокс «чёрный жемчуг»
    private final By blackColorCheckbox = By.id("black");
    // Чекбокс «серая безысходность»
    private final By greyColorCheckbox = By.id("grey");
    // Поле комментария для курьера
    private final By commentInput = By.cssSelector("input[placeholder='Комментарий для курьера']");
    // Кнопка «Назад»
    private final By backButton = By.xpath("//button[normalize-space()='Назад']");
    // Кнопка «Заказать» на втором шаге
    private final By submitButton = By.xpath("//button[normalize-space()='Заказать' and contains(@class,'Button_Middle')]");
    // Кнопка подтверждения «Да»
    private final By confirmButton = By.xpath("//button[normalize-space()='Да']");
    // Сообщение об успешном создании заказа
    private final By successMessage = By.xpath("//*[contains(normalize-space(), 'Заказ оформлен')]");

    public OrderPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void fillCustomerData(String firstName, String lastName, String address, String metro, String phone) {
        driver.findElement(firstNameInput).sendKeys(firstName);
        driver.findElement(lastNameInput).sendKeys(lastName);
        driver.findElement(addressInput).sendKeys(address);
        String metroPrefix = metro.length() > 3 ? metro.substring(0, 3) : metro;
        By metroOption = By.xpath("//li[contains(@class,'select-search__row')]//button[normalize-space()=" + quote(metro) + "]");

        for (int attempt = 0; attempt < 3; attempt++) {
            WebElement metroField = driver.findElement(metroInput);
            metroField.click();
            metroField.sendKeys(Keys.CONTROL, "a");
            metroField.sendKeys(metroPrefix);
            try {
                wait.until(ExpectedConditions.elementToBeClickable(metroOption)).click();
                break;
            } catch (org.openqa.selenium.TimeoutException exception) {
                if (attempt == 2) {
                    throw exception;
                }
                driver.navigate().refresh();
                wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameInput));
                driver.findElement(firstNameInput).sendKeys(firstName);
                driver.findElement(lastNameInput).sendKeys(lastName);
                driver.findElement(addressInput).sendKeys(address);
            }
        }
        driver.findElement(phoneInput).sendKeys(phone);
    }

    private String quote(String value) {
        return "'" + value.replace("'", "\\'") + "'";
    }

    public void clickNext() {
        wait.until(ExpectedConditions.elementToBeClickable(nextButton)).click();
    }

    public void selectDeliveryDate() {
        driver.findElement(deliveryDateInput).click();
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        String dayClass = String.format("react-datepicker__day--%03d", tomorrow.getDayOfMonth());
        By tomorrowCell = By.cssSelector("." + dayClass + ":not(.react-datepicker__day--outside-month):not(.react-datepicker__day--disabled)");
        wait.until(ExpectedConditions.elementToBeClickable(tomorrowCell)).click();
    }

    public void selectRentalPeriod(String period) {
        driver.findElement(rentalPeriodDropdown).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(@class,'Dropdown-option') and normalize-space()='" + period + "']"))).click();
    }

    public void selectColor(String color) {
        By checkbox = "чёрный жемчуг".equals(color) ? blackColorCheckbox : greyColorCheckbox;
        wait.until(ExpectedConditions.elementToBeClickable(checkbox)).click();
    }

    public void enterComment(String comment) {
        driver.findElement(commentInput).sendKeys(comment);
    }

    public void submitOrder() {
        wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
    }

    public void confirmOrder() {
        wait.until(ExpectedConditions.elementToBeClickable(confirmButton)).click();
    }

    public boolean isSuccessMessageVisible() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage)).isDisplayed();
    }

}
