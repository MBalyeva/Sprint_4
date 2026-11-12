package ru.praktikum.qa.scooter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import ru.praktikum.qa.scooter.pageobject.HomePage;
import ru.praktikum.qa.scooter.pageobject.OrderPage;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class OrderTest {
    private final int orderButtonIndex;
    private final CustomerData customer;
    private WebDriver driver;

    public OrderTest(int orderButtonIndex, CustomerData customer) {
        this.orderButtonIndex = orderButtonIndex;
        this.customer = customer;
    }

    @Parameterized.Parameters(name = "Кнопка {0}, данные {1}")
    public static Collection<Object[]> data() {
        CustomerData first = new CustomerData("Иван", "Петров", "Москва, улица Тестовая, дом 1", "Сокольники", "+79991234567", "сутки", "чёрный жемчуг", "Позвонить перед доставкой");
        CustomerData second = new CustomerData("Анна", "Смирнова", "Москва, улица Учебная, дом 5", "Сокольники", "+79997654321", "двое суток", "серая безысходность", "Оставить у двери");
        return Arrays.asList(new Object[][]{{0, first}, {0, second}, {1, first}, {1, second}});
    }

    @Before
    public void setUp() {
        driver = DriverFactory.create();
        HomePage homePage = new HomePage(driver);
        homePage.open();
        homePage.acceptCookies();
    }

    @Test
    public void positiveOrderFlowShowsSuccessMessage() {
        HomePage homePage = new HomePage(driver);
        homePage.clickOrderButton(orderButtonIndex);
        OrderPage orderPage = new OrderPage(driver);
        orderPage.fillCustomerData(customer.firstName, customer.lastName, customer.address, customer.metro, customer.phone);
        orderPage.clickNext();
        orderPage.selectDeliveryDate();
        orderPage.selectRentalPeriod(customer.period);
        orderPage.selectColor(customer.color);
        orderPage.enterComment(customer.comment);
        orderPage.submitOrder();
        orderPage.confirmOrder();
        assertTrue("После подтверждения заказа не показано сообщение об успехе", orderPage.isSuccessMessageVisible());
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private static final class CustomerData {
        private final String firstName;
        private final String lastName;
        private final String address;
        private final String metro;
        private final String phone;
        private final String period;
        private final String color;
        private final String comment;

        private CustomerData(String firstName, String lastName, String address, String metro, String phone, String period, String color, String comment) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.address = address;
            this.metro = metro;
            this.phone = phone;
            this.period = period;
            this.color = color;
            this.comment = comment;
        }

        @Override
        public String toString() {
            return firstName + " " + lastName;
        }
    }
}
