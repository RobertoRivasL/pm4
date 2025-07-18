package com.robertorivas.automatizacion.pruebas;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Pruebas automatizadas para el formulario de registro en
 * https://practice.expandtesting.com/register
 * Basadas en los escenarios descritos por el usuario.
 */
public class RegistroExpandTestingTest {
    private WebDriver driver;
    private final String URL_REGISTRO = "https://practice.expandtesting.com/register";

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--window-size=1920,1080");
        driver = new ChromeDriver(options);
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void openRegisterPage() {
        driver.get(URL_REGISTRO);
    }

    private void submitForm(String username, String password, String confirm) {
        if (username != null) {
            WebElement userField = driver.findElement(By.id("username"));
            userField.clear();
            userField.sendKeys(username);
        }
        if (password != null) {
            WebElement passField = driver.findElement(By.id("password"));
            passField.clear();
            passField.sendKeys(password);
        }
        if (confirm != null) {
            WebElement confirmField = driver.findElement(By.id("confirmPassword"));
            confirmField.clear();
            confirmField.sendKeys(confirm);
        }
        driver.findElement(By.cssSelector("button[type='submit']")).click();
    }

    private String getMessage() {
        WebElement msg = driver.findElement(By.cssSelector("div[role='alert']"));
        return msg.getText().trim();
    }

    @Test
    public void testRegistroExitoso() {
        openRegisterPage();
        submitForm("usuarioDemo", "Password123", "Password123");
        String message = getMessage();
        Assert.assertEquals(message, "Successfully registered, you can log in now.");
        Assert.assertTrue(driver.getCurrentUrl().contains("/login"));
    }

    @Test
    public void testRegistroSinUsuario() {
        openRegisterPage();
        submitForm(null, "Password123", "Password123");
        String message = getMessage();
        Assert.assertEquals(message, "All fields are required.");
    }

    @Test
    public void testRegistroSinPassword() {
        openRegisterPage();
        submitForm("usuarioDemo", null, "algo");
        String message = getMessage();
        Assert.assertEquals(message, "All fields are required.");
    }

    @Test
    public void testRegistroPasswordsNoCoinciden() {
        openRegisterPage();
        submitForm("usuarioDemo", "Password123", "PasswordXYZ");
        String message = getMessage();
        Assert.assertEquals(message, "Passwords do not match.");
    }
}
