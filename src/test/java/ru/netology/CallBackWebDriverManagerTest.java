package ru.netology;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class CallBackWebDriverManagerTest {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
//библиотека webdriver manager автоматически определяет ОС и версию браузера, скачивает и устанавливает подходящий файл драйвера
        WebDriverManager.chromedriver().setup();//Выбираем драйвер для нужного браузера и путь до него.
    }

    @BeforeEach
    void setUp() {
        //  Перед каждым тестом запускаем драйвер
//  запуск браузера в режиме headless - отключаем графический интерфейс
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        driver = new ChromeDriver(options);//
        driver.get("http://localhost:9999");
    }

    @AfterEach
//  завершение работы браузера после каждого теста
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    public void shouldTestSendForm() {//Заполнение формы
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иван Бакланов");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79999999999");
        driver.findElement(By.cssSelector("[data-test-id=agreement] .checkbox__box")).click();//Чекбокс
        driver.findElement(By.cssSelector(".form-field .button__content")).click();//кнопка продолжить
        String message = driver.findElement(By.className("Success_successBlock__2L3Cw")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", message.trim());
    }


    @Test
    public void shouldTestWarningIfFormEmpty() {//Отправка пустой формы
        driver.findElement(By.cssSelector(".form-field .button__content")).click();
        String message = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", message);
    }

    @Test
    public void shouldTestWarningIfNameFieldEmpty() {//Поле имя пустое
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+77777777777");
        driver.findElement(By.cssSelector("[data-test-id=agreement] .checkbox__box")).click();
        driver.findElement(By.cssSelector(".form-field .button__content")).click();
        String message = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", message);
    }

    @Test
    public void shouldTestWarningIfTelephoneFieldEmpty() {//Поле телефон пустое
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Василь Протонов");
        driver.findElement(By.cssSelector("[data-test-id=agreement] .checkbox__box")).click();
        driver.findElement(By.cssSelector(".form-field .button__content")).click();
        String message = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", message);
    }

    @Test
    public void shouldTestWarningIfCheckboxFieldEmpty() {//Чекбокс пустой
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Василь Протонов");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+77777777777");
        driver.findElement(By.cssSelector(".form-field .button__content")).click();
        String message = driver.findElement(By.cssSelector("[data-test-id='agreement'].input_invalid .checkbox__text")).getText();
        assertEquals("Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй", message);
    }

    @Test
    public void shouldTestWarningIfBadName() {//Имя, Фамилия на английском
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Lol Shto");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+77777777777");
        driver.findElement(By.cssSelector("[data-test-id=agreement] .checkbox__box")).click();
        driver.findElement(By.cssSelector(".form-field .button__content")).click();
        String message = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", message);
    }

    @Test
    public void shouldTestWarningIfBadTelephoneLessNumber() {//Номер телефона меньше 11 цифр
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Василь Протонов");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+12345");
        driver.findElement(By.cssSelector("[data-test-id=agreement] .checkbox__box")).click();
        driver.findElement(By.cssSelector(".form-field .button__content")).click();
        String message = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", message);
    }

    @Test
    public void shouldTestWarningIfBadTelephoneMoreNumber() {//Номер телефона больше 11 цифр
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Василь Протонов");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+723456789012");
        driver.findElement(By.cssSelector("[data-test-id=agreement] .checkbox__box")).click();
        driver.findElement(By.cssSelector(".form-field .button__content")).click();
        String message = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", message);
    }

    @Test
    public void shouldTestWarningIfBadTelephoneLetterNumber() {//В поле телефон английские буквы
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Василь Протонов");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+afornfgcjsq");
        driver.findElement(By.cssSelector("[data-test-id=agreement] .checkbox__box")).click();
        driver.findElement(By.cssSelector(".form-field .button__content")).click();
        String message = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", message);
    }

}
