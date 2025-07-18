package com.robertorivas.automatizacion.pruebas;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

/**
 * Pruebas automatizadas para el formulario de registro en ExpandTesting.
 * URL: https://practice.expandtesting.com/register
 * 
 * Casos de prueba basados en la documentación oficial de ExpandTesting:
 * - Test Case 1: Successful Registration → "Successfully registered, you can log in now."
 * - Test Case 2: Missing Username → "All fields are required."
 * - Test Case 3: Missing Password → "All fields are required."  
 * - Test Case 4: Non-matching Passwords → "Passwords do not match."
 * 
 * @author Roberto Rivas Lopez
 */
public class RegistroExpandTestingTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private final String URL_REGISTRO = "https://practice.expandtesting.com/register";

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        // Comentar la línea siguiente para ver el navegador en acción (útil para debugging)
        options.addArguments("--headless=new");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        System.out.println("✅ Driver configurado para ExpandTesting");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("✅ Driver cerrado correctamente");
        }
    }

    /**
     * Navega a la página de registro y espera a que cargue.
     */
    private void openRegisterPage() {
        driver.get(URL_REGISTRO);
        
        // Esperar que la página cargue completamente
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("username")));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("password")));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("confirmPassword")));
        
        System.out.println("📄 Página de registro cargada: " + driver.getCurrentUrl());
    }

    /**
     * Llena y envía el formulario de registro.
     * Permite valores null para probar campos faltantes.
     */
    private void submitForm(String username, String password, String confirm) {
        System.out.println("📝 Llenando formulario - Username: " + username + ", Password: " + (password != null ? "***" : "null"));
        
        // Limpiar campos antes de llenar
        WebElement userField = driver.findElement(By.id("username"));
        WebElement passField = driver.findElement(By.id("password"));
        WebElement confirmField = driver.findElement(By.id("confirmPassword"));
        
        userField.clear();
        passField.clear();
        confirmField.clear();
        
        // Llenar campos solo si no son null
        if (username != null) {
            userField.sendKeys(username);
        }
        if (password != null) {
            passField.sendKeys(password);
        }
        if (confirm != null) {
            confirmField.sendKeys(confirm);
        }
        
        // Hacer clic en el botón de envío
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();
        
        System.out.println("🚀 Formulario enviado");
    }

    /**
     * Obtiene el mensaje de respuesta del formulario.
     * ExpandTesting usa el elemento #flash para mostrar mensajes.
     */
    private String getMessage() {
        try {
            // Esperar que aparezca el mensaje
            WebElement messageElement = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.id("flash"))
            );
            
            String message = messageElement.getText().trim();
            System.out.println("💬 Mensaje obtenido: " + message);
            return message;
            
        } catch (Exception e) {
            // Fallback: buscar otros posibles selectores de mensaje
            try {
                WebElement altMessage = driver.findElement(By.cssSelector("div[role='alert']"));
                String message = altMessage.getText().trim();
                System.out.println("💬 Mensaje obtenido (alternativo): " + message);
                return message;
            } catch (Exception e2) {
                System.out.println("❌ No se pudo obtener mensaje de error");
                return "";
            }
        }
    }

    /**
     * Verifica si hubo redirección a la página de login.
     */
    private boolean isRedirectedToLogin() {
        // Esperar un poco para la redirección
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        boolean redirected = driver.getCurrentUrl().contains("/login");
        System.out.println("🔄 Redirigido a login: " + redirected + " (URL: " + driver.getCurrentUrl() + ")");
        return redirected;
    }

    // ===== CASOS DE PRUEBA =====

    @Test(description = "Test Case 1: Successful Registration", 
          priority = 1)
    public void testRegistroExitoso() {
        System.out.println("\n🧪 === TEST CASE 1: REGISTRO EXITOSO ===");
        
        openRegisterPage();
        
        // Usar un username único para evitar conflictos
        String uniqueUsername = "usuarioDemo" + System.currentTimeMillis();
        submitForm(uniqueUsername, "Password123", "Password123");
        
        // Verificar mensaje de éxito
        String message = getMessage();
        Assert.assertEquals(message, "Successfully registered, you can log in now.", 
                          "El mensaje de éxito no es el esperado");
        
        // Verificar redirección a login
        Assert.assertTrue(isRedirectedToLogin(), 
                         "Debería redirigir a la página de login después del registro exitoso");
        
        System.out.println("✅ Test Case 1: PASÓ - Registro exitoso");
    }

    @Test(description = "Test Case 2: Registration with Missing Username", 
          priority = 2)
    public void testRegistroSinUsuario() {
        System.out.println("\n🧪 === TEST CASE 2: REGISTRO SIN USERNAME ===");
        
        openRegisterPage();
        submitForm("", "Password123", "Password123"); // Username vacío
        
        String message = getMessage();
        Assert.assertEquals(message, "All fields are required.", 
                          "El mensaje de error para username faltante no es el esperado");
        
        System.out.println("✅ Test Case 2: PASÓ - Error detectado sin username");
    }

    @Test(description = "Test Case 3: Registration with Missing Password", 
          priority = 3)
    public void testRegistroSinPassword() {
        System.out.println("\n🧪 === TEST CASE 3: REGISTRO SIN PASSWORD ===");
        
        openRegisterPage();
        submitForm("usuarioDemo", "", ""); // Passwords vacíos
        
        String message = getMessage();
        Assert.assertEquals(message, "All fields are required.", 
                          "El mensaje de error para password faltante no es el esperado");
        
        System.out.println("✅ Test Case 3: PASÓ - Error detectado sin password");
    }

    @Test(description = "Test Case 4: Registration with Non-matching Passwords", 
          priority = 4)
    public void testRegistroPasswordsNoCoinciden() {
        System.out.println("\n🧪 === TEST CASE 4: PASSWORDS NO COINCIDEN ===");
        
        openRegisterPage();
        submitForm("usuarioDemo", "Password123", "PasswordXYZ");
        
        String message = getMessage();
        Assert.assertEquals(message, "Passwords do not match.", 
                          "El mensaje de error para passwords no coincidentes no es el esperado");
        
        System.out.println("✅ Test Case 4: PASÓ - Error detectado con passwords diferentes");
    }

    // ===== CASOS DE PRUEBA ADICIONALES =====

    @Test(description = "Test Case 5: Registration with all fields empty", 
          priority = 5)
    public void testRegistroTodosCamposVacios() {
        System.out.println("\n🧪 === TEST CASE 5: TODOS LOS CAMPOS VACÍOS ===");
        
        openRegisterPage();
        submitForm("", "", ""); // Todos los campos vacíos
        
        String message = getMessage();
        Assert.assertEquals(message, "All fields are required.", 
                          "El mensaje de error para todos los campos vacíos no es el esperado");
        
        System.out.println("✅ Test Case 5: PASÓ - Error detectado con todos los campos vacíos");
    }

    @Test(description = "Test Case 6: Registration with username only", 
          priority = 6)
    public void testRegistroSoloUsername() {
        System.out.println("\n🧪 === TEST CASE 6: SOLO USERNAME ===");
        
        openRegisterPage();
        submitForm("usuarioDemo", "", ""); // Solo username, passwords vacíos
        
        String message = getMessage();
        Assert.assertEquals(message, "All fields are required.", 
                          "El mensaje de error para passwords faltantes no es el esperado");
        
        System.out.println("✅ Test Case 6: PASÓ - Error detectado con passwords faltantes");
    }

    @Test(description = "Test Case 7: Registration with different password lengths", 
          priority = 7)
    public void testRegistroPasswordsDiferentes() {
        System.out.println("\n🧪 === TEST CASE 7: PASSWORDS DE DIFERENTES LONGITUDES ===");
        
        openRegisterPage();
        submitForm("usuarioDemo", "Short1", "VeryLongPassword123");
        
        String message = getMessage();
        Assert.assertEquals(message, "Passwords do not match.", 
                          "El mensaje de error para passwords de diferentes longitudes no es el esperado");
        
        System.out.println("✅ Test Case 7: PASÓ - Error detectado con passwords de diferentes longitudes");
    }
}