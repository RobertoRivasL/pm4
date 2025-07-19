package com.robertorivas.automatizacion.pruebas;

import com.robertorivas.automatizacion.configuracion.ConfiguracionNavegador;
import com.robertorivas.automatizacion.configuracion.ConfiguracionPruebas;
import com.robertorivas.automatizacion.datos.ProveedorDatos;
import com.robertorivas.automatizacion.modelos.DatosRegistro;
import com.robertorivas.automatizacion.utilidades.GestorEvidencias;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.ElementClickInterceptedException;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;

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
 * SISTEMA HÍBRIDO IMPLEMENTADO:
 * - Tests básicos con datos hardcodeados + anti-modales + capturas
 * - Tests con DataProvider para datos CSV (modo híbrido)
 * - Compatible con configuración ejecucion.modo.hibrido=true
 * 
 * @author Roberto Rivas Lopez
 */
public class RegistroExpandTestingTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private final String URL_REGISTRO = "https://practice.expandtesting.com/register";
    
    // Variables para manejo de capturas
    private Path rutaCapturas;
    private String nombrePrueba;
    
    // Variables para modo híbrido
    private static boolean modoHibrido;
    private static ConfiguracionPruebas config;

    @BeforeClass
    public void setUp() {
        // Inicializar configuración híbrida
        config = ConfiguracionPruebas.obtenerInstancia();
        modoHibrido = Boolean.parseBoolean(config.obtenerPropiedad("ejecucion.modo.hibrido", "true"));
        
        // Usar configuración anti-modales
        ConfiguracionNavegador.TipoNavegador tipo = ConfiguracionNavegador.TipoNavegador.CHROME;
        driver = ConfiguracionNavegador.crearDriver(tipo);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15)); // Aumentar timeout
        
        // Configurar capturas
        nombrePrueba = "RegistroExpandTestingTest";
        rutaCapturas = Paths.get("reportes", "capturas");
        try {
            Files.createDirectories(rutaCapturas);
        } catch (IOException e) {
            System.err.println("⚠️  Error creando directorio de capturas: " + e.getMessage());
        }
        
        System.out.println("✅ Driver configurado para ExpandTesting (modo híbrido: " + modoHibrido + ")");
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
     * Versión simplificada para diagnóstico.
     */
    private void openRegisterPage() {
        System.out.println("🌐 Navegando a: " + URL_REGISTRO);
        driver.get(URL_REGISTRO);
        
        System.out.println("📄 URL actual después de navegar: " + driver.getCurrentUrl());
        
        // Esperar que la página cargue completamente
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("username")));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("password")));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("confirmPassword")));
        
        // Captura después de cargar la página
        tomarCapturaPaso("pagina_registro_cargada");
        
        System.out.println("📄 Página de registro cargada completamente");
    }

    /**
     * Llena y envía el formulario de registro.
     * Permite valores null para probar campos faltantes.
     * Incluye estrategias anti-modales para prevenir interferencias publicitarias.
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
        
        // Captura antes del envío
        tomarCapturaPaso("formulario_lleno_antes_envio");
        
        // === ESTRATEGIAS ANTI-MODALES ANTES DEL CLIC ===
        System.out.println("🛡️  Aplicando protecciones anti-modales antes del envío...");
        
        // 1. Cerrar modales existentes
        cerrarModalesInmediatos();
        
        // 2. Intentar hacer clic con estrategias anti-interceptación
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        
        try {
            // Estrategia 1: Clic directo
            submitButton.click();
            System.out.println("🚀 Formulario enviado con clic directo");
            tomarCapturaPaso("envio_exitoso_directo");
        } catch (ElementClickInterceptedException e) {
            System.out.println("⚠️  Clic interceptado por modal - aplicando estrategias avanzadas");
            tomarCapturaPaso("modal_interceptando_clic");
            
            // Estrategia 2: Cerrar modales agresivamente y reintentar
            aplicarCorrecionesAgresivasModal();
            
            try {
                // Esperar un momento y reintentar
                Thread.sleep(500);
                submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
                submitButton.click();
                System.out.println("🚀 Formulario enviado tras corrección de modales");
                tomarCapturaPaso("envio_exitoso_tras_correccion");
            } catch (ElementClickInterceptedException e2) {
                // Estrategia 3: Clic con JavaScript
                System.out.println("🔧 Usando JavaScript para hacer clic en el botón");
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();", submitButton);
                System.out.println("🚀 Formulario enviado con JavaScript");
                tomarCapturaPaso("envio_exitoso_javascript");
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Cierra modales publicitarios de forma inmediata.
     * Adaptado de las estrategias de PaginaLogin.
     */
    private void cerrarModalesInmediatos() {
        try {
            // Presionar ESC múltiples veces para cerrar posibles modales
            for (int i = 0; i < 3; i++) {
                driver.findElement(By.tagName("body")).sendKeys(Keys.ESCAPE);
            }
            
            // Buscar y cerrar modales por selectores CSS comunes
            String[] selectoresModales = {
                "button[aria-label='Close']",
                "button[aria-label='close']",
                ".close",
                ".modal-close",
                "button.close",
                "[data-dismiss='modal']",
                "button[onclick*='close']",
                "div[id*='ad'] button",
                "iframe[src*='google'] + button"
            };
            
            for (String selector : selectoresModales) {
                try {
                    List<WebElement> botonesCerrar = driver.findElements(By.cssSelector(selector));
                    for (WebElement boton : botonesCerrar) {
                        if (boton.isDisplayed()) {
                            boton.click();
                            System.out.println("✅ Modal cerrado con selector: " + selector);
                        }
                    }
                } catch (Exception e) {
                    // Continuar con el siguiente selector
                }
            }
            
        } catch (Exception e) {
            System.out.println("⚠️  Error cerrando modales inmediatos: " + e.getMessage());
        }
    }

    /**
     * Aplica correcciones agresivas para eliminar modales persistentes.
     * Adaptado de las estrategias de PaginaLogin.
     */
    private void aplicarCorrecionesAgresivasModal() {
        try {
            System.out.println("🔧 Aplicando correcciones agresivas contra modales...");
            
            JavascriptExecutor js = (JavascriptExecutor) driver;
            
            // 1. Eliminar elementos con z-index alto (típicos de modales)
            js.executeScript(
                "document.querySelectorAll('*').forEach(el => {" +
                "  const style = window.getComputedStyle(el);" +
                "  if (parseInt(style.zIndex) > 1000) {" +
                "    el.style.display = 'none';" +
                "    console.log('Elemento modal eliminado:', el);" +
                "  }" +
                "});"
            );
            
            // 2. Eliminar iframes de Google Ads
            js.executeScript(
                "document.querySelectorAll('iframe[src*=\"google\"]').forEach(iframe => {" +
                "  iframe.remove();" +
                "  console.log('Iframe de Google eliminado');" +
                "});"
            );
            
            // 3. Eliminar overlays y modales por clase
            js.executeScript(
                "['overlay', 'modal', 'popup', 'ad-container', 'advertisement'].forEach(className => {" +
                "  document.querySelectorAll('.' + className + ', [id*=\"' + className + '\"]').forEach(el => {" +
                "    el.remove();" +
                "  });" +
                "});"
            );
            
            // 4. Limpiar fragmentos de URL que pueden indicar modales
            String currentUrl = driver.getCurrentUrl();
            if (currentUrl.contains("#google_vignette") || currentUrl.contains("#ad")) {
                String cleanUrl = currentUrl.split("#")[0];
                driver.navigate().to(cleanUrl);
                System.out.println("🔗 URL limpiada de fragmentos publicitarios");
            }
            
            System.out.println("✅ Correcciones agresivas aplicadas");
            
        } catch (Exception e) {
            System.out.println("⚠️  Error en correcciones agresivas: " + e.getMessage());
        }
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
            
            // Captura del mensaje de respuesta
            tomarCapturaPaso("mensaje_respuesta_obtenido");
            
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
        
        // Captura final del test exitoso
        tomarCapturaPaso("test_registro_exitoso_final");
        
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

    // ===== TESTS HÍBRIDOS CON DATAPROVIDER (CSV) =====
    
    /**
     * Test híbrido que usa DataProvider para datos de registro válidos desde CSV.
     * Mantiene todas las mejoras: anti-modales, capturas, logging detallado.
     */
    @Test(dataProvider = "datosRegistroValidos", 
          dataProviderClass = ProveedorDatos.class,
          description = "Test Case Híbrido 1: Registro con datos CSV válidos", 
          priority = 10,
          enabled = true) // Se puede controlar desde config
    public void testRegistroHibridoCSVValidos(DatosRegistro datos) {
        System.out.println("\n🧪 === TEST HÍBRIDO CSV 1: REGISTRO CON DATOS VÁLIDOS ===");
        System.out.println("📊 Datos desde CSV: " + datos.getEmail());
        
        openRegisterPage();
        
        // Generar username válido para ExpandTesting
        String validUsername = generarUsernameValido(datos.getEmail());
        System.out.println("🔧 Username generado: " + validUsername + " (longitud: " + validUsername.length() + ")");
        
        submitForm(validUsername, datos.getPassword(), datos.getPassword());
        
        // Verificar mensaje de éxito
        String message = getMessage();
        Assert.assertEquals(message, "Successfully registered, you can log in now.", 
                          "El mensaje de éxito no es el esperado para datos CSV: " + datos.getEmail());
        
        // Verificar redirección
        Assert.assertTrue(isRedirectedToLogin(), 
                         "Debería redirigir a login después del registro exitoso con datos CSV");
        
        tomarCapturaPaso("test_hibrido_csv_exitoso_" + datos.getEmail().replace("@", "_at_"));
        
        System.out.println("✅ Test Híbrido CSV 1: PASÓ - Registro exitoso con " + datos.getEmail());
    }

    /**
     * Test híbrido que usa DataProvider para datos de registro inválidos desde CSV.
     * Prueba diferentes escenarios de error usando datos estructurados.
     */
    @Test(dataProvider = "datosRegistroInvalidos", 
          dataProviderClass = ProveedorDatos.class,
          description = "Test Case Híbrido 2: Registro con datos CSV inválidos", 
          priority = 11,
          enabled = true)
    public void testRegistroHibridoCSVInvalidos(DatosRegistro datos) {
        System.out.println("\n🧪 === TEST HÍBRIDO CSV 2: REGISTRO CON DATOS INVÁLIDOS ===");
        System.out.println("📊 Datos desde CSV: " + datos.getEmail() + " (Esperando error)");
        
        openRegisterPage();
        
        // Para datos inválidos, usar estrategia inteligente según el tipo de error a probar
        String username;
        if (datos.getEmail() == null || datos.getEmail().trim().isEmpty()) {
            username = ""; // Probar campo vacío
        } else if (datos.getEmail().contains("invalid") || datos.getEmail().length() > 50) {
            username = datos.getEmail(); // Usar email inválido como username para probar validación de formato
        } else {
            // Generar username válido para probar otras validaciones (passwords, etc.)
            username = generarUsernameValido(datos.getEmail());
        }
        
        System.out.println("🔧 Username para test: " + username);
        
        submitForm(username, datos.getPassword(), datos.getConfirmarPassword());
        
        // El mensaje esperado debe venir del CSV o ser uno de los conocidos
        String message = getMessage();
        String expectedMessage = determinarMensajeEsperado(datos);
        
        Assert.assertEquals(message, expectedMessage, 
                          "El mensaje de error no es el esperado para datos CSV inválidos: " + datos.getEmail());
        
        tomarCapturaPaso("test_hibrido_csv_error_" + datos.getEmail().replace("@", "_at_"));
        
        System.out.println("✅ Test Híbrido CSV 2: PASÓ - Error detectado correctamente con " + datos.getEmail());
    }

    /**
     * Test híbrido específico para diferentes tipos de registro.
     * Usa el DataProvider "tiposRegistro" para probar diferentes escenarios.
     */
    @Test(dataProvider = "tiposRegistro", 
          dataProviderClass = ProveedorDatos.class,
          description = "Test Case Híbrido 3: Tipos específicos de registro", 
          priority = 12,
          enabled = true)
    public void testRegistroHibridoTipos(String tipo, String username, String password, String confirm, String mensajeEsperado) {
        System.out.println("\n🧪 === TEST HÍBRIDO CSV 3: TIPOS DE REGISTRO ===");
        System.out.println("📊 Tipo: " + tipo + " | Usuario: " + username);
        
        openRegisterPage();
        
        // Usar datos específicos del tipo
        submitForm(username, password, confirm);
        
        String message = getMessage();
        Assert.assertEquals(message, mensajeEsperado, 
                          "El mensaje para tipo '" + tipo + "' no es el esperado");
        
        tomarCapturaPaso("test_hibrido_tipo_" + tipo.replaceAll("[^a-zA-Z0-9]", "_"));
        
        System.out.println("✅ Test Híbrido CSV 3: PASÓ - Tipo '" + tipo + "' validado correctamente");
    }

    /**
     * Determina el mensaje de error esperado basado en los datos de registro.
     */
    private String determinarMensajeEsperado(DatosRegistro datos) {
        // Lógica para determinar qué tipo de error se espera
        if (datos.getEmail() == null || datos.getEmail().trim().isEmpty()) {
            return "All fields are required.";
        }
        if (datos.getPassword() == null || datos.getPassword().trim().isEmpty()) {
            return "All fields are required.";
        }
        if (datos.getConfirmarPassword() == null || datos.getConfirmarPassword().trim().isEmpty()) {
            return "All fields are required.";
        }
        if (!datos.getPassword().equals(datos.getConfirmarPassword())) {
            return "Passwords do not match.";
        }
        
        // Si todos los campos están llenos y las passwords coinciden, 
        // probablemente es un caso de datos duplicados o formato inválido
        return "All fields are required."; // fallback
    }

    /**
     * Genera un username válido para ExpandTesting basado en un email/username.
     * Reglas de ExpandTesting:
     * - Solo letras minúsculas, números y guiones simples
     * - Entre 3 y 39 caracteres
     * - No puede empezar o terminar con guión
     */
    private String generarUsernameValido(String emailOrUsername) {
        if (emailOrUsername == null || emailOrUsername.trim().isEmpty()) {
            return "";
        }
        
        // Si ya es un username válido (como los del CSV), úsalo con pequeña modificación para unicidad
        if (esUsernameValido(emailOrUsername)) {
            // Agregar solo un número pequeño para unicidad
            String timestamp = String.valueOf(System.currentTimeMillis()).substring(10); // Últimos 3 dígitos
            String result = emailOrUsername + timestamp;
            
            // Asegurar que no exceda 39 caracteres
            if (result.length() > 39) {
                result = emailOrUsername.substring(0, 36) + timestamp;
            }
            
            return result;
        }
        
        // Si es un email, extraer la parte antes del @
        String baseName = emailOrUsername.contains("@") ? 
                         emailOrUsername.split("@")[0].toLowerCase() : 
                         emailOrUsername.toLowerCase();
        
        // Limpiar caracteres no permitidos (solo a-z, 0-9, guiones)
        baseName = baseName.replaceAll("[^a-z0-9\\-]", "");
        
        // Remover guiones al inicio y final
        baseName = baseName.replaceAll("^-+|-+$", "");
        
        // Truncar si es muy largo (dejamos espacio para timestamp)
        if (baseName.length() > 25) {
            baseName = baseName.substring(0, 25);
        }
        
        // Agregar timestamp para unicidad
        String timestamp = String.valueOf(System.currentTimeMillis()).substring(8); // Últimos 5 dígitos
        String finalUsername = baseName + timestamp;
        
        // Asegurar longitud máxima
        if (finalUsername.length() > 39) {
            finalUsername = finalUsername.substring(0, 39);
        }
        
        // Asegurar longitud mínima
        if (finalUsername.length() < 3) {
            finalUsername = "usr" + timestamp;
        }
        
        return finalUsername;
    }
    
    /**
     * Verifica si un username es válido según las reglas de ExpandTesting.
     */
    private boolean esUsernameValido(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        
        // Verificar longitud
        if (username.length() < 3 || username.length() > 39) {
            return false;
        }
        
        // Verificar que no empiece o termine con guión
        if (username.startsWith("-") || username.endsWith("-")) {
            return false;
        }
        
        // Verificar que solo contenga caracteres permitidos
        return username.matches("^[a-z0-9\\-]+$");
    }

    /**
     * Toma una captura de pantalla con descripción específica.
     */
    private void tomarCapturaPaso(String descripcion) {
        try {
            if (driver instanceof TakesScreenshot) {
                // Tomar captura usando GestorEvidencias para mantener consistencia
                String rutaCaptura = GestorEvidencias.tomarCaptura(driver, nombrePrueba, descripcion);
                
                if (rutaCaptura != null) {
                    System.out.println("📸 Captura guardada: " + descripcion);
                } else {
                    System.out.println("⚠️  No se pudo guardar captura: " + descripcion);
                }
                
            } else {
                System.out.println("⚠️  Driver no soporta capturas de pantalla");
            }
        } catch (Exception e) {
            System.out.println("⚠️  Error tomando captura '" + descripcion + "': " + e.getMessage());
        }
    }
}