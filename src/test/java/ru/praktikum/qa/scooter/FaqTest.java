package ru.praktikum.qa.scooter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import ru.praktikum.qa.scooter.pageobject.HomePage;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class FaqTest {
    private final int questionIndex;
    private final String expectedAnswer;
    private WebDriver driver;

    public FaqTest(int questionIndex, String expectedAnswer) {
        this.questionIndex = questionIndex;
        this.expectedAnswer = expectedAnswer;
    }

    @Parameterized.Parameters(name = "Вопрос {0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {0, "Сутки — 400 рублей. Оплата курьеру — наличными или картой."},
                {1, "Пока что у нас так: один заказ — один самокат."},
                {2, "Допустим, вы оформляете заказ"},
                {3, "Только начиная с завтрашнего дня."},
                {4, "Пока что нет!"},
                {5, "Самокат приезжает к вам с полной зарядкой."},
                {6, "Да, пока самокат не привезли."},
                {7, "Да, обязательно."}
        });
    }

    @Before
    public void setUp() {
        driver = DriverFactory.create();
        HomePage homePage = new HomePage(driver);
        homePage.open();
        homePage.acceptCookies();
    }

    @Test
    public void clickQuestionShowsExpectedAnswer() {
        HomePage homePage = new HomePage(driver);
        homePage.clickFaq(questionIndex);
        String answer = homePage.faqAnswer(questionIndex);
        assertTrue("Текст ответа не содержит ожидаемую фразу: [" + answer + "]", answer.contains(expectedAnswer));
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
