package ru.taras_buriachenko.NauJava;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.taras_buriachenko.NauJava.entity.User;
import ru.taras_buriachenko.NauJava.repository.crud.UserRepository;
import ru.taras_buriachenko.NauJava.type.UserRole;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthUiSeleniumTests {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private WebDriver driver;
    private WebDriverWait wait;
    private String baseUrl;

    private final String testUsername = "selenium_user";
    private final String testPassword = "TestPassword123";

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;

        userRepository.findByUsernameIgnoreCase(testUsername)
                .forEach(userRepository::delete);

        User user = new User(
                null,
                testUsername,
                passwordEncoder.encode(testPassword),
                "+79990001122",
                LocalDateTime.now(),
                true,
                UserRole.USER
        );
        userRepository.save(user);

        EdgeOptions options = new EdgeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new EdgeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        userRepository.findByUsernameIgnoreCase(testUsername)
                .forEach(userRepository::delete);
    }

    @Test
    void loginAndLogout_FullUserFlow_Success() {
        driver.get(baseUrl + "/login");

        WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
        WebElement passwordInput = driver.findElement(By.name("password"));
        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));

        usernameInput.sendKeys(testUsername);
        passwordInput.sendKeys(testPassword);
        loginButton.click();

        wait.until(ExpectedConditions.urlContains("/home"));
        WebElement welcomeHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h1")));
        assertEquals("Добро пожаловать, " + testUsername + "!", welcomeHeader.getText());
        assertTrue(driver.getTitle().contains("Мой рабочий стол"));

        WebElement logoutButton = driver.findElement(By.cssSelector("form[action='/logout'] button[type='submit']"));
        logoutButton.click();

        wait.until(ExpectedConditions.urlContains("/login?logout"));
        WebElement loginHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h2")));
        assertEquals("Авторизация", loginHeader.getText());
    }
}