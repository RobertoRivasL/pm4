package com.robertorivas.automatizacion.paginas;

import com.robertorivas.automatizacion.modelos.Usuario;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.List;

/**
 * Page Object para la página de login de ExpandTesting.
 * Implementa el patrón POM para encapsular la interacción con el formulario de login.
 * 
 * URL: https://practice.expandtesting.com/login
 * 
 * Casos de prueba soportados:
 * - Login exitoso con username: practice, password: SuperSecretPassword!
 * - Login fallido con username inválido (error: "Invalid username.")
 * - Login fallido con password inválido (error: "Invalid password.")
 * 
 * @author Roberto Rivas Lopez
 */
public class PaginaLogin extends PaginaBase {
    
    // ===== LOCALIZADORES ESPECÍFICOS PARA EXPANDTESTING =====
    
    // Campos del formulario de login
    @FindBy(css = "input[name='username'], #username")
    private WebElement campoUsername;
    
    @FindBy(css = "input[name='password'], #password")
    private WebElement campoPassword;
    
    // Botones
    @FindBy(css = "button[type='submit'], .btn-primary, input[value='Login']")
    private WebElement botonLogin;
    
    @FindBy(css = ".btn-clear, #clear, button[name='clear']")
    private WebElement botonLimpiar;
    
    // Mensajes y alertas específicos de ExpandTesting
    @FindBy(css = "#flash, .alert-danger, .error")
    private WebElement mensajeError;
    
    @FindBy(css = "#flash, .alert-success, .success")
    private WebElement mensajeExito;
    
    // Enlaces de navegación
    @FindBy(css = "a[href='/register'], a[href*='register']")
    private WebElement enlaceRegistro;
    
    @FindBy(css = "a[href*='forgot'], a[href*='recover']")
    private WebElement enlaceRecuperarPassword;
    
    // Checkbox recordar (si existe)
    @FindBy(css = "input[type='checkbox'], input[name='remember']")
    private WebElement checkboxRecordar;
    
    // Localizadores dinámicos
    private static final By LOADER = By.cssSelector(".loader, .spinner, .loading");
    private static final By MENSAJE_FLASH = By.cssSelector("#flash");
    
    /**
     * Constructor de la página de login.
     */
    public PaginaLogin(WebDriver driver) {
        super(driver);
        logger.info("Inicializando página de login de ExpandTesting");
    }
    
    // ===== MÉTODOS ABSTRACTOS IMPLEMENTADOS =====
    
    @Override
    public boolean estaPaginaCargada() {
        try {
            return estaElementoVisible(By.cssSelector("input[name='username']")) &&
                   estaElementoVisible(By.cssSelector("input[name='password']")) &&
                   estaElementoVisible(By.cssSelector("button[type='submit'], .btn-primary")) &&
                   obtenerUrlActual().contains("/login");
        } catch (Exception e) {
            logger.debug("Error verificando si la página de login está cargada: {}", e.getMessage());
            return false;
        }
    }
    
    @Override
    public void esperarCargaPagina() {
        logger.debug("Esperando carga completa de la página de login de ExpandTesting");
        
        // Esperar elementos principales del formulario
        buscarElementoVisible(By.cssSelector("input[name='username']"));
        buscarElementoVisible(By.cssSelector("input[name='password']"));
        buscarElementoVisible(By.cssSelector("button[type='submit'], .btn-primary"));
        
        // Esperar que desaparezca el loader si existe
        esperarQueElementoDesaparezca(LOADER, TIMEOUT_MEDIO);
        
        logger.info("Página de login de ExpandTesting cargada completamente");
    }
    
    @Override
    public String obtenerTituloPagina() {
        return "Login - ExpandTesting";
    }
    
    // ===== MÉTODOS DE NAVEGACIÓN =====
    
    /**
     * Navega a la página de login de ExpandTesting.
     */
    public PaginaLogin navegarAPaginaLogin() {
        String urlLogin = config.obtenerUrlLogin();
        navegarA(urlLogin);
        esperarCargaPagina();
        return this;
    }
    
    /**
     * Navega a la página de registro desde el enlace.
     */
    public void irAPaginaRegistro() {
        logger.info("Navegando a página de registro desde login");
        if (estaElementoPresente(By.cssSelector("a[href='/register'], a[href*='register']"))) {
            hacerClic(By.cssSelector("a[href='/register'], a[href*='register']"));
        } else {
            // Navegación directa si no hay enlace
            navegarA(config.obtenerUrlBase() + "/register");
        }
    }
    
    /**
     * Navega a recuperar contraseña.
     */
    public void irARecuperarPassword() {
        logger.info("Navegando a recuperar contraseña");
        if (estaElementoPresente(By.cssSelector("a[href*='forgot'], a[href*='recover']"))) {
            hacerClic(By.cssSelector("a[href*='forgot'], a[href*='recover']"));
        } else {
            logger.warn("Enlace de recuperar contraseña no encontrado");
        }
    }
    
    // ===== MÉTODOS DE INTERACCIÓN CON FORMULARIO =====
    
    /**
     * Introduce el username en el campo correspondiente.
     */
    public PaginaLogin introducirUsername(String username) {
        logger.debug("Introduciendo username: {}", username);
        introducirTexto(By.cssSelector("input[name='username']"), username);
        return this;
    }
    
    /**
     * Introduce la contraseña.
     */
    public PaginaLogin introducirPassword(String password) {
        logger.debug("Introduciendo contraseña");
        introducirTexto(By.cssSelector("input[name='password']"), password);
        return this;
    }
    
    /**
     * Introduce el email (alias para username para compatibilidad).
     */
    public PaginaLogin introducirEmail(String email) {
        return introducirUsername(email);
    }
    
    /**
     * Hace clic en el checkbox de recordar credenciales.
     */
    public PaginaLogin marcarRecordarCredenciales(boolean recordar) {
        if (estaElementoPresente(By.cssSelector("input[type='checkbox'], input[name='remember']"))) {
            WebElement checkbox = buscarElementoClickeable(By.cssSelector("input[type='checkbox'], input[name='remember']"));
            if (checkbox.isSelected() != recordar) {
                checkbox.click();
                logger.debug("Checkbox recordar credenciales configurado a: {}", recordar);
            }
        }
        return this;
    }
    
    /**
     * Hace clic en el botón de login.
     */
    public void hacerClicLogin() {
        logger.info("Haciendo clic en botón de login");
        hacerClic(By.cssSelector("button[type='submit'], .btn-primary"));
        
        // Esperar respuesta del servidor
        esperarRespuestaLogin();
    }
    
    /**
     * Limpia los campos del formulario de login.
     */
    public PaginaLogin limpiarCamposLogin() {
        logger.info("Limpiando campos del formulario de login");
        
        if (estaElementoPresente(By.cssSelector("input[name='username']"))) {
            WebElement username = driver.findElement(By.cssSelector("input[name='username']"));
            username.clear();
        }
        
        if (estaElementoPresente(By.cssSelector("input[name='password']"))) {
            WebElement password = driver.findElement(By.cssSelector("input[name='password']"));
            password.clear();
        }
        
        return this;
    }
    
    // ===== MÉTODOS DE LOGIN PRINCIPAL =====
    
    /**
     * Realiza el proceso completo de login con Usuario.
     */
    public boolean iniciarSesion(Usuario usuario) {
        return iniciarSesion(usuario.getEmail(), usuario.getPassword(), false);
    }
    
    /**
     * Realiza el proceso completo de login con credenciales.
     */
    public boolean iniciarSesion(String username, String password) {
        return iniciarSesion(username, password, false);
    }
    
    /**
     * Realiza el proceso completo de login con opción de recordar.
     */
    public boolean iniciarSesion(String username, String password, boolean recordar) {
        logger.info("Iniciando sesión para usuario: {}", username);
        
        try {
            // Limpiar campos antes de llenar
            limpiarCamposLogin();
            
            // Llenar formulario
            introducirUsername(username);
            introducirPassword(password);
            
            if (recordar) {
                marcarRecordarCredenciales(true);
            }
            
            // Enviar formulario
            hacerClicLogin();
            
            // Verificar resultado
            return verificarLoginExitoso();
            
        } catch (Exception e) {
            logger.error("Error durante el proceso de login: {}", e.getMessage());
            return false;
        }
    }
    
    // ===== MÉTODOS DE VALIDACIÓN Y VERIFICACIÓN =====
    
    /**
     * Verifica si el login fue exitoso.
     */
    public boolean verificarLoginExitoso() {
        try {
            // Opción 1: Verificar redirección a página segura
            String urlActual = obtenerUrlActual();
            if (urlActual.contains("/secure")) {
                logger.info("Login exitoso - Redirigido a página segura");
                return true;
            }
            
            // Opción 2: Verificar mensaje de éxito
            if (estaElementoVisible(MENSAJE_FLASH)) {
                String mensajeFlash = obtenerTexto(MENSAJE_FLASH);
                if (mensajeFlash.contains("You logged into a secure area!")) {
                    logger.info("Login exitoso - Mensaje de éxito detectado");
                    return true;
                }
            }
            
            // Opción 3: Verificar ausencia de errores y cambio de página
            if (!hayErroresLogin() && !urlActual.contains("/login")) {
                logger.info("Login exitoso - Sin errores y fuera de página de login");
                return true;
            }
            
            return false;
            
        } catch (Exception e) {
            logger.error("Error verificando login exitoso: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Verifica si hay errores de login.
     */
    public boolean hayErroresLogin() {
        try {
            if (estaElementoVisible(MENSAJE_FLASH)) {
                String mensaje = obtenerTexto(MENSAJE_FLASH);
                return mensaje.contains("Invalid username.") || 
                       mensaje.contains("Invalid password.") ||
                       mensaje.contains("All fields are required.");
            }
            return false;
        } catch (Exception e) {
            logger.debug("Error verificando errores de login: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Obtiene los errores de login.
     */
    public List<String> obtenerErroresLogin() {
        List<String> errores = new ArrayList<>();
        
        try {
            if (estaElementoVisible(MENSAJE_FLASH)) {
                String mensaje = obtenerTexto(MENSAJE_FLASH);
                if (!mensaje.isEmpty()) {
                    errores.add(mensaje);
                }
            }
        } catch (Exception e) {
            logger.debug("Error obteniendo errores de login: {}", e.getMessage());
        }
        
        return errores;
    }
    
    /**
     * Verifica si las credenciales son inválidas.
     */
    public boolean credencialesInvalidas() {
        List<String> errores = obtenerErroresLogin();
        return errores.stream().anyMatch(error -> 
            error.contains("Invalid username.") || error.contains("Invalid password.")
        );
    }
    
    /**
     * Verifica el comportamiento con campos vacíos.
     */
    public boolean verificarCamposVacios() {
        logger.info("Verificando comportamiento con campos vacíos");
        
        limpiarCamposLogin();
        hacerClicLogin();
        
        return hayErroresLogin();
    }
    
    /**
     * Verifica el comportamiento con password vacío.
     */
    public boolean verificarPasswordVacio(String username) {
        logger.info("Verificando comportamiento con password vacío");
        
        limpiarCamposLogin();
        introducirUsername(username);
        // Dejar password vacío
        hacerClicLogin();
        
        return hayErroresLogin();
    }
    
    /**
     * Verifica el comportamiento con email vacío.
     */
    public boolean verificarEmailVacio(String password) {
        logger.info("Verificando comportamiento con username vacío");
        
        limpiarCamposLogin();
        // Dejar username vacío
        introducirPassword(password);
        hacerClicLogin();
        
        return hayErroresLogin();
    }
    
    /**
     * Intenta múltiples logins para verificar bloqueo de cuenta.
     */
    public boolean intentarHastaBloqueoCuenta(String username, String passwordIncorrecto) {
        logger.info("Intentando múltiples logins para verificar bloqueo");
        
        int maxIntentos = 5;
        
        for (int i = 1; i <= maxIntentos; i++) {
            logger.debug("Intento {} de {}", i, maxIntentos);
            
            limpiarCamposLogin();
            introducirUsername(username);
            introducirPassword(passwordIncorrecto);
            hacerClicLogin();
            
            List<String> errores = obtenerErroresLogin();
            
            // Verificar si hay mensaje de bloqueo
            boolean cuentaBloqueada = errores.stream().anyMatch(error -> 
                error.toLowerCase().contains("blocked") || 
                error.toLowerCase().contains("locked") ||
                error.toLowerCase().contains("too many") ||
                error.toLowerCase().contains("exceeded")
            );
            
            if (cuentaBloqueada) {
                logger.info("Cuenta bloqueada después de {} intentos", i);
                return true;
            }
            
            // Pausa entre intentos
            pausar(TIMEOUT_CORTO);
        }
        
        logger.info("No se detectó bloqueo después de {} intentos", maxIntentos);
        return false;
    }
    
    // ===== MÉTODOS DE UTILIDAD =====
    
    /**
     * Obtiene los valores actuales del formulario.
     */
    public String[] obtenerValoresFormulario() {
        String[] valores = new String[2];
        
        try {
            if (estaElementoPresente(By.cssSelector("input[name='username']"))) {
                valores[0] = obtenerAtributo(By.cssSelector("input[name='username']"), "value");
            }
            
            if (estaElementoPresente(By.cssSelector("input[name='password']"))) {
                valores[1] = obtenerAtributo(By.cssSelector("input[name='password']"), "value");
            }
        } catch (Exception e) {
            logger.warn("Error obteniendo valores del formulario: {}", e.getMessage());
            valores[0] = "";
            valores[1] = "";
        }
        
        return valores;
    }
    
    /**
     * Verifica si el formulario de login está disponible.
     */
    public boolean formularioLoginDisponible() {
        try {
            return estaElementoVisible(By.cssSelector("input[name='username']")) &&
                   estaElementoVisible(By.cssSelector("input[name='password']")) &&
                   estaElementoVisible(By.cssSelector("button[type='submit'], .btn-primary"));
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Espera la respuesta del servidor después del login.
     */
    private void esperarRespuestaLogin() {
        try {
            // Esperar que aparezca mensaje de éxito, error o redirección
            espera.until(driver -> {
                String urlActual = driver.getCurrentUrl();
                boolean hayMensaje = estaElementoVisible(MENSAJE_FLASH);
                boolean huboRedireccion = !urlActual.endsWith("/login");
                
                return hayMensaje || huboRedireccion;
            });
        } catch (Exception e) {
            logger.debug("Timeout esperando respuesta de login");
        }
    }
    
    /**
     * Obtiene información de debug de la página actual.
     */
    public void logearInformacionLogin() {
        logger.debug("=== INFORMACIÓN DE LOGIN ===");
        logger.debug("URL actual: {}", obtenerUrlActual());
        logger.debug("Página cargada: {}", estaPaginaCargada());
        logger.debug("Formulario disponible: {}", formularioLoginDisponible());
        logger.debug("Hay errores: {}", hayErroresLogin());
        
        if (hayErroresLogin()) {
            logger.debug("Errores: {}", obtenerErroresLogin());
        }
        
        logger.debug("============================");
    }
}