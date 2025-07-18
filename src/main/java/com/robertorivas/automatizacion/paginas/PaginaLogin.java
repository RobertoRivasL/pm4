package com.robertorivas.automatizacion.paginas;

import com.robertorivas.automatizacion.modelos.Usuario;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.List;

/**
 * Page Object para la página de login/inicio de sesión.
 * Implementa el patrón POM para encapsular la interacción con el formulario de login.
 * 
 * Principios aplicados:
 * - Page Object Model: Encapsula elementos y acciones de la página
 * - Single Responsibility: Solo maneja operaciones de login
 * - Encapsulation: Elementos privados con métodos públicos de interacción
 * - DRY: Reutiliza funcionalidad de la clase base
 * 
 * @author Roberto Rivas Lopez
 */
public class PaginaLogin extends PaginaBase {
    
    // ===== LOCALIZADORES ESPECÍFICOS PARA PRACTICE EXPAND TESTING =====
    
    // Campos del formulario de login según test cases
    @FindBy(id = "username")
    private WebElement campoEmail;
    
    @FindBy(id = "password")
    private WebElement campoPassword;
    
    // Botón de login específico
    @FindBy(xpath = "//button[@type='submit' and contains(text(), 'Login')]")
    private WebElement botonLogin;
    
    // Botón de logout (aparece después del login exitoso)
    @FindBy(xpath = "//a[contains(text(), 'Logout') or @href='/logout']")
    private WebElement botonLogout;
    
    // Enlaces de navegación
    @FindBy(xpath = "//a[@href='/register' or contains(text(), 'Register')]")
    private WebElement enlaceRegistro;
    
    // Mensajes específicos según test cases
    @FindBy(xpath = "//div[contains(text(), 'You logged into a secure area!')]")
    private WebElement mensajeExitoLogin;
    
    @FindBy(xpath = "//div[contains(text(), 'Invalid username.')]")
    private WebElement mensajeErrorUsername;
    
    @FindBy(xpath = "//div[contains(text(), 'Invalid password.')]")
    private WebElement mensajeErrorPassword;
    
    @FindBy(xpath = "//div[contains(text(), 'Successfully registered')]")
    private WebElement mensajeRegistroExitoso;
    
    // Mensajes y validaciones generales
    @FindBy(xpath = "//div[contains(@class, 'alert') or contains(@class, 'message')]")
    private List<WebElement> mensajesError;
    
    @FindBy(css = ".success-message, .alert-success, #success")
    private WebElement mensajeExito;
    
    // Localizadores dinámicos
    private static final By LOADER = By.cssSelector(".loader, .spinner, .loading");
    private static final By FORM_LOGIN = By.cssSelector("form, .login-form, #loginForm");
    
    /**
     * Constructor de la página de login.
     */
    public PaginaLogin(WebDriver driver) {
        super(driver);
        logger.info("Inicializando página de login");
    }
    
    // ===== MÉTODOS ABSTRACTOS IMPLEMENTADOS =====
    
    @Override
    public boolean estaPaginaCargada() {
        try {
            return estaElementoVisible(By.cssSelector("#username")) &&
                   estaElementoVisible(By.cssSelector("#password")) &&
                   estaElementoVisible(By.cssSelector("#submit"));
        } catch (Exception e) {
            logger.debug("Error verificando si la página de login está cargada: {}", e.getMessage());
            return false;
        }
    }
    
    @Override
    public void esperarCargaPagina() {
        logger.debug("Esperando carga completa de la página de login");
        
        // Esperar elementos principales del formulario
        buscarElementoVisible(By.cssSelector("#username"));
        buscarElementoVisible(By.cssSelector("#password"));
        buscarElementoVisible(By.cssSelector("#submit"));
        
        // Esperar que desaparezca el loader si existe
        esperarQueElementoDesaparezca(LOADER, TIMEOUT_MEDIO);
        
        logger.info("Página de login cargada completamente");
    }
    
    @Override
    public String obtenerTituloPagina() {
        return "Login";
    }
    
    // ===== MÉTODOS DE NAVEGACIÓN =====
    
    /**
     * Navega a la página de login.
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
        if (estaElementoPresente(By.cssSelector("a[href*='register'], .link-register, #registerLink"))) {
            hacerClic(By.cssSelector("a[href*='register'], .link-register, #registerLink"));
        } else {
            logger.warn("No se encontró enlace a página de registro");
        }
    }
    
    /**
     * Navega a la página de recuperar contraseña.
     */
    public void irARecuperarPassword() {
        logger.info("Navegando a página de recuperar contraseña");
        if (estaElementoPresente(By.cssSelector("a[href*='forgot'], .link-forgot, #forgotLink"))) {
            hacerClic(By.cssSelector("a[href*='forgot'], .link-forgot, #forgotLink"));
        } else {
            logger.warn("No se encontró enlace a recuperar contraseña");
        }
    }
    
    // ===== MÉTODOS DE INTERACCIÓN CON FORMULARIO =====
    
    /**
     * Realiza el proceso completo de login con un usuario.
     */
    public boolean iniciarSesion(Usuario usuario) {
        return iniciarSesion(usuario.getEmail(), usuario.getPassword(), false);
    }
    
    /**
     * Realiza el proceso completo de login con credenciales específicas.
     */
    public boolean iniciarSesion(String email, String password) {
        return iniciarSesion(email, password, false);
    }
    
    /**
     * Realiza el proceso completo de login con opción de recordar.
     */
    public boolean iniciarSesion(String email, String password, boolean recordar) {
        logger.info("Iniciando sesión para usuario: {}", email);
        
        try {
            // Limpiar campos antes de llenar
            limpiarCamposLogin();
            
            // Llenar formulario
            introducirEmail(email);
            introducirPassword(password);
            
            if (recordar && estaElementoPresente(By.cssSelector("input[type='checkbox'], .remember-me"))) {
                configurarRecordarCredenciales(recordar);
            }
            
            // Enviar formulario
            enviarFormularioLogin();
            
            // Esperar resultado
            esperarResultadoLogin();
            
            // Verificar si el login fue exitoso
            boolean loginExitoso = verificarLoginExitoso();
            
            if (loginExitoso) {
                logger.info("Login exitoso para usuario: {}", email);
            } else {
                logger.warn("Login falló para usuario: {}. Errores: {}", email, obtenerErroresLogin());
            }
            
            return loginExitoso;
            
        } catch (Exception e) {
            logger.error("Error durante el proceso de login para {}: {}", email, e.getMessage());
            return false;
        }
    }
    
    /**
     * Introduce el email en el campo correspondiente.
     */
    public PaginaLogin introducirEmail(String email) {
        logger.debug("Introduciendo email: {}", email);
        introducirTexto(By.cssSelector("#username"), email);
        return this;
    }
    
    /**
     * Introduce la contraseña.
     */
    public PaginaLogin introducirPassword(String password) {
        logger.debug("Introduciendo contraseña");
        introducirTexto(By.cssSelector("#password"), password);
        return this;
    }
    
    /**
     * Configura la opción de recordar credenciales.
     */
    public PaginaLogin configurarRecordarCredenciales(boolean recordar) {
        logger.debug("Configurando recordar credenciales: {}", recordar);
        if (estaElementoPresente(By.cssSelector("input[type='checkbox'], .remember-me"))) {
            WebElement checkbox = buscarElementoClickeable(By.cssSelector("input[type='checkbox'], .remember-me"));
            if (checkbox.isSelected() != recordar) {
                checkbox.click();
            }
        }
        return this;
    }
    
    /**
     * Envía el formulario de login.
     */
    public void enviarFormularioLogin() {
        logger.info("Enviando formulario de login");
        hacerClic(By.cssSelector("#submit"));
    }
    
    /**
     * Limpia todos los campos del formulario de login.
     */
    public PaginaLogin limpiarCamposLogin() {
        logger.debug("Limpiando campos de login");
        
        if (estaElementoPresente(By.cssSelector("#username"))) {
            WebElement campoEmail = driver.findElement(By.cssSelector("#username"));
            campoEmail.clear();
        }
        
        if (estaElementoPresente(By.cssSelector("#password"))) {
            WebElement campoPassword = driver.findElement(By.cssSelector("#password"));
            campoPassword.clear();
        }
        
        return this;
    }
    
    // ===== MÉTODOS DE VALIDACIÓN =====
    
    /**
     * Verifica si hay errores de login.
     */
    public boolean hayErroresLogin() {
        return !obtenerErroresLogin().isEmpty();
    }
    
    /**
     * Obtiene todos los errores de login visibles.
     */
    public List<String> obtenerErroresLogin() {
        List<String> errores = new ArrayList<>();
        
        // Buscar mensajes de error
        List<WebElement> elementosError = driver.findElements(By.cssSelector(".error-message, .alert-danger, #error, .flash-error"));
        for (WebElement elemento : elementosError) {
            if (elemento.isDisplayed()) {
                String textoError = elemento.getText().trim();
                if (!textoError.isEmpty()) {
                    errores.add(textoError);
                }
            }
        }
        
        // También buscar errores comunes por texto
        String paginaTexto = driver.getPageSource().toLowerCase();
        if (paginaTexto.contains("invalid") && paginaTexto.contains("credentials")) {
            errores.add("Credenciales inválidas detectadas en página");
        }
        if (paginaTexto.contains("username") && paginaTexto.contains("password") && paginaTexto.contains("incorrect")) {
            errores.add("Usuario o contraseña incorrectos");
        }
        
        return errores;
    }
    
    /**
     * Verifica si se detectaron credenciales inválidas.
     */
    public boolean credencialesInvalidas() {
        List<String> errores = obtenerErroresLogin();
        String paginaTexto = driver.getPageSource().toLowerCase();
        
        return errores.stream().anyMatch(error -> 
            error.toLowerCase().contains("invalid") || 
            error.toLowerCase().contains("incorrect") ||
            error.toLowerCase().contains("wrong")
        ) || paginaTexto.contains("your username is invalid");
    }
    
    /**
     * Verifica si el login fue exitoso.
     */
    private boolean verificarLoginExitoso() {
        // Verificar si estamos en una página diferente (exitosa)
        String urlActual = obtenerUrlActual();
        
        // URLs que indican login exitoso
        boolean urlExitosa = urlActual.contains("logged-in") || 
                            urlActual.contains("dashboard") ||
                            urlActual.contains("home") ||
                            urlActual.contains("welcome") ||
                            urlActual.contains("main");
        
        // Verificar si hay elementos que indican login exitoso
        boolean elementosExitosos = estaElementoPresente(By.cssSelector(".logout, #logout, .welcome")) ||
                                   estaElementoPresente(By.cssSelector(".user-menu, .profile")) ||
                                   estaElementoPresente(By.cssSelector("h1"));
        
        // Verificar contenido de la página
        String contenidoPagina = driver.getPageSource().toLowerCase();
        boolean contenidoExitoso = contenidoPagina.contains("logged in successfully") ||
                                  contenidoPagina.contains("congratulations");
        
        // Verificar que no hay errores
        boolean sinErrores = !hayErroresLogin();
        
        // El login es exitoso si cumple al menos una condición positiva y no hay errores
        boolean loginExitoso = (urlExitosa || elementosExitosos || contenidoExitoso) && sinErrores;
        
        logger.debug("Verificación de login - URL exitosa: {}, Elementos exitosos: {}, Contenido exitoso: {}, Sin errores: {}, Resultado: {}", 
                    urlExitosa, elementosExitosos, contenidoExitoso, sinErrores, loginExitoso);
        
        return loginExitoso;
    }
    
    /**
     * Espera el resultado del login.
     */
    private void esperarResultadoLogin() {
        // Esperar que desaparezca el loader
        esperarQueElementoDesaparezca(LOADER, TIMEOUT_MEDIO);
        
        // Esperar que aparezcan errores o que cambie la URL
        try {
            espera.until(driver -> {
                String urlActual = obtenerUrlActual();
                boolean hayErrores = hayErroresLogin();
                boolean urlCambio = !urlActual.contains("login") || urlActual.contains("logged-in");
                
                return hayErrores || urlCambio;
            });
        } catch (Exception e) {
            logger.debug("Tiempo de espera agotado para resultado de login");
        }
        
        // Pausa adicional para estabilizar
        pausar(TIMEOUT_CORTO.dividedBy(2));
    }
    
    // ===== MÉTODOS DE VALIDACIÓN ESPECÍFICOS =====
    
    /**
     * Verifica el comportamiento con campos vacíos.
     */
    public boolean verificarCamposVacios() {
        logger.debug("Verificando comportamiento con campos vacíos");
        
        limpiarCamposLogin();
        enviarFormularioLogin();
        esperarResultadoLogin();
        
        return hayErroresLogin();
    }
    
    /**
     * Verifica el comportamiento con email válido y password vacío.
     */
    public boolean verificarPasswordVacio(String email) {
        logger.debug("Verificando comportamiento con password vacío");
        
        limpiarCamposLogin();
        introducirEmail(email);
        enviarFormularioLogin();
        esperarResultadoLogin();
        
        return hayErroresLogin();
    }
    
    /**
     * Verifica el comportamiento con email vacío y password válido.
     */
    public boolean verificarEmailVacio(String password) {
        logger.debug("Verificando comportamiento con email vacío");
        
        limpiarCamposLogin();
        introducirPassword(password);
        enviarFormularioLogin();
        esperarResultadoLogin();
        
        return hayErroresLogin();
    }
    
    /**
     * Intenta múltiples logins hasta activar bloqueo de cuenta.
     */
    public boolean intentarHastaBloqueoCuenta(String email, String passwordIncorrecto) {
        logger.info("Intentando múltiples logins para activar bloqueo");
        
        int maxIntentos = 5;
        boolean cuentaBloqueada = false;
        
        for (int intento = 1; intento <= maxIntentos; intento++) {
            logger.debug("Intento de login #{}", intento);
            
            limpiarCamposLogin();
            introducirEmail(email);
            introducirPassword(passwordIncorrecto);
            enviarFormularioLogin();
            esperarResultadoLogin();
            
            List<String> errores = obtenerErroresLogin();
            String paginaTexto = driver.getPageSource().toLowerCase();
            
            // Buscar indicadores de bloqueo
            boolean indicadoresBloqueo = errores.stream().anyMatch(error -> 
                error.toLowerCase().contains("blocked") ||
                error.toLowerCase().contains("locked") ||
                error.toLowerCase().contains("suspended") ||
                error.toLowerCase().contains("too many attempts")
            ) || paginaTexto.contains("account has been locked") ||
                 paginaTexto.contains("too many failed attempts");
            
            if (indicadoresBloqueo) {
                logger.info("Cuenta bloqueada detectada en intento #{}", intento);
                cuentaBloqueada = true;
                break;
            }
            
            // Pausa entre intentos
            pausar(TIMEOUT_CORTO.dividedBy(2));
        }
        
        return cuentaBloqueada;
    }
    
    // ===== MÉTODOS DE UTILIDAD =====
    
    /**
     * Obtiene los valores actuales del formulario.
     */
    public String[] obtenerValoresFormulario() {
        String email = "";
        String password = "";
        
        try {
            if (estaElementoPresente(By.cssSelector("#username"))) {
                email = obtenerAtributo(By.cssSelector("#username"), "value");
            }
            
            if (estaElementoPresente(By.cssSelector("#password"))) {
                password = obtenerAtributo(By.cssSelector("#password"), "value");
            }
        } catch (Exception e) {
            logger.warn("Error obteniendo valores del formulario: {}", e.getMessage());
        }
        
        return new String[]{email != null ? email : "", password != null ? password : ""};
    }
    
    /**
     * Verifica si el formulario de login está disponible.
     */
    public boolean formularioLoginDisponible() {
        return estaElementoPresente(By.cssSelector("#username")) &&
               estaElementoPresente(By.cssSelector("#password")) &&
               estaElementoPresente(By.cssSelector("#submit"));
    }
    
    /**
     * Obtiene información de la página de login.
     */
    public void logearInformacionLogin() {
        logger.debug("=== INFORMACIÓN DE LOGIN ===");
        logger.debug("Título: {}", obtenerTituloActual());
        logger.debug("URL: {}", obtenerUrlActual());
        logger.debug("Formulario disponible: {}", formularioLoginDisponible());
        logger.debug("Errores presentes: {}", hayErroresLogin());
        if (hayErroresLogin()) {
            logger.debug("Errores: {}", obtenerErroresLogin());
        }
        logger.debug("=============================");
    }
}