package com.robertorivas.automatizacion.paginas;

import com.robertorivas.automatizacion.modelos.Usuario;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Page Object refactorizado para la página de login de ExpandTesting.
 * Implementa estrategias múltiples para localización robusta de elementos.
 * 
 * URL: https://practice.expandtesting.com/login
 * 
 * Casos de prueba soportados:
 * - Login exitoso con username: practice, password: SuperSecretPassword!
 * - Login fallido con username inválido (error: "Invalid username.")
 * - Login fallido con password inválido (error: "Invalid password.")
 * 
 * Mejoras implementadas:
 * - Múltiples estrategias de localización de elementos
 * - Timeouts adaptativos
 * - Mejor manejo de errores
 * - Logging detallado para debugging
 * 
 * @author Roberto Rivas Lopez
 */
public class PaginaLogin extends PaginaBase {

    // ===== LOCALIZADORES ROBUSTOS PARA EXPANDTESTING =====

    // Múltiples estrategias para campos de formulario
    private static final String[] SELECTORES_USERNAME = {
        "input[name='username']",
        "#username", 
        "input[type='text']",
        "input[placeholder*='username' i]",
        "input[placeholder*='user' i]",
        "form input:nth-of-type(1)"
    };

    private static final String[] SELECTORES_PASSWORD = {
        "input[name='password']",
        "#password",
        "input[type='password']",
        "input[placeholder*='password' i]",
        "form input[type='password']:first-of-type"
    };

    private static final String[] SELECTORES_SUBMIT = {
        "button[type='submit']",
        "input[type='submit']",
        ".btn-primary",
        ".btn",
        "button[value*='Login']",
        "button[value*='Submit']",
        "form button:first-of-type"
    };

    // @FindBy elements como fallback
    @FindBy(css = "input[name='username'], #username, input[type='text']")
    private WebElement campoUsername;

    @FindBy(css = "input[name='password'], #password, input[type='password']")
    private WebElement campoPassword;

    @FindBy(css = "button[type='submit'], input[type='submit'], .btn")
    private WebElement botonLogin;

    @FindBy(css = "#flash, .alert, .message")
    private WebElement mensajeFlash;

    // Enlaces de navegación
    @FindBy(css = "a[href*='register']")
    private WebElement enlaceRegistro;

    @FindBy(css = "a[href*='forgot'], a[href*='recover']")
    private WebElement enlaceRecuperarPassword;

    // Localizadores dinámicos
    private static final By LOADER = By.cssSelector(".loader, .spinner, .loading");
    private static final By MENSAJE_FLASH = By.cssSelector("#flash");
    private static final By ANY_ERROR_MESSAGE = By.cssSelector("#flash, .alert-danger, .error, .message");
    
    // Selectores para modales y popups después del login
    private static final String[] SELECTORES_MODAL_CIERRE = {
        "button[aria-label='Close']",
        "button[aria-label*='close' i]",
        ".close",
        ".modal-close",
        "button.close",
        "[data-dismiss='modal']",
        ".btn-close",
        "button[title='Close']",
        "button[title*='close' i]",
        ".popup-close",
        ".ad-close",
        "[id*='close']",
        "[class*='close']",
        "button[onclick*='close']",
        "div[role='button'][aria-label*='close' i]"
    };
    
    private static final String[] SELECTORES_MODAL_CONTAINER = {
        ".modal",
        ".popup",
        ".overlay",
        "[role='dialog']",
        ".advertisement",
        ".ad-modal",
        "[id*='modal']",
        "[id*='popup']",
        "[id*='ad']",
        "[class*='modal']",
        "[class*='popup']",
        "[class*='overlay']",
        "iframe[src*='google']",
        "iframe[src*='ads']",
        "div[style*='z-index']"
    };

    // Timeouts específicos
    private static final Duration TIMEOUT_ELEMENTO = Duration.ofSeconds(5);
    private static final Duration TIMEOUT_RESPUESTA = Duration.ofSeconds(10);
    private static final Duration TIMEOUT_MODAL = Duration.ofSeconds(3);

    /**
     * Constructor de la página de login.
     */
    public PaginaLogin(WebDriver driver) {
        super(driver);
        logger.info("Inicializando página de login robusta de ExpandTesting");
    }

    // ===== MÉTODOS ABSTRACTOS IMPLEMENTADOS =====

    @Override
    public boolean estaPaginaCargada() {
        try {
            // Verificación múltiple: URL + al menos un elemento clave
            boolean urlCorrecta = obtenerUrlActual().contains("/login");
            boolean formularioPresente = formularioLoginDisponible();
            
            logger.debug("Verificación página cargada - URL: {}, Formulario: {}", urlCorrecta, formularioPresente);
            return urlCorrecta && formularioPresente;
            
        } catch (Exception e) {
            logger.debug("Error verificando si la página está cargada: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public void esperarCargaPagina() {
        logger.debug("Esperando carga completa de la página de login");

        try {
            // Esperar que la URL contenga /login
            espera.until(driver -> obtenerUrlActual().contains("/login"));
            
            // Esperar al menos un campo de formulario
            boolean campoEncontrado = false;
            for (String selector : SELECTORES_USERNAME) {
                if (estaElementoPresente(By.cssSelector(selector))) {
                    campoEncontrado = true;
                    break;
                }
            }
            
            if (!campoEncontrado) {
                // Esperar un poco más y reintentar
                pausar(Duration.ofMillis(2000));
            }
            
            // Esperar que desaparezca el loader
            esperarQueElementoDesaparezca(LOADER, TIMEOUT_CORTO);
            
            logger.info("Página de login cargada completamente");
            
        } catch (Exception e) {
            logger.warn("Timeout esperando carga de página: {}", e.getMessage());
        }
    }

    @Override
    public String obtenerTituloPagina() {
        return "Login - ExpandTesting";
    }

    // ===== MÉTODOS DE NAVEGACIÓN =====

    /**
     * Navega a la página de login con verificación robusta.
     */
    public PaginaLogin navegarAPaginaLogin() {
        String urlLogin = config.obtenerUrlLogin();
        
        logger.info("Navegando a login: {}", urlLogin);
        navegarA(urlLogin);
        esperarCargaPagina();
        
        // Verificación adicional
        if (!estaPaginaCargada()) {
            logger.warn("La página no se cargó correctamente, reintentando...");
            pausar(Duration.ofMillis(2000));
            refrescarPagina();
            esperarCargaPagina();
        }
        
        return this;
    }

    /**
     * Navega a registro con múltiples estrategias.
     */
    public void irAPaginaRegistro() {
        logger.info("Navegando a página de registro");
        
        String[] selectoresRegistro = {
            "a[href*='register']",
            "a[href='/register']", 
            "a[title*='Register']",
            "a[title*='Sign up']"
        };
        
        for (String selector : selectoresRegistro) {
            if (estaElementoPresente(By.cssSelector(selector))) {
                hacerClic(By.cssSelector(selector));
                return;
            }
        }
        
        // Navegación directa como fallback
        navegarA(config.obtenerUrlBase() + "/register");
    }

    /**
     * Navega a recuperación de contraseña con múltiples estrategias.
     */
    public void irARecuperarPassword() {
        logger.info("Navegando a página de recuperación de contraseña");
        
        String[] selectoresRecuperacion = {
            "a[href*='forgot']",
            "a[href*='recover']", 
            "a[href*='reset']",
            "a[title*='Forgot']",
            "a[title*='Reset']", 
            "a[title*='Recover']",
            "#forgot-password",
            ".forgot-password"
        };
        
        for (String selector : selectoresRecuperacion) {
            if (estaElementoPresente(By.cssSelector(selector))) {
                hacerClic(By.cssSelector(selector));
                return;
            }
        }
        
        // Navegación directa como fallback
        // Practice Expand Testing puede no tener esta funcionalidad, 
        // pero mantenemos la funcionalidad para compatibilidad
        logger.warn("No se encontró enlace de recuperación de contraseña, navegación directa no disponible");
    }

    // ===== MÉTODOS DE INTERACCIÓN ROBUSTOS =====

    /**
     * Introduce username con múltiples estrategias.
     */
    public PaginaLogin introducirUsername(String username) {
        logger.debug("Introduciendo username: {}", username);
        
        for (String selector : SELECTORES_USERNAME) {
            try {
                WebElement campo = buscarElementoVisible(By.cssSelector(selector), TIMEOUT_ELEMENTO);
                campo.clear();
                campo.sendKeys(username);
                
                // Verificar que se introdujo correctamente
                String valorActual = campo.getAttribute("value");
                if (username.equals(valorActual)) {
                    logger.debug("✅ Username introducido exitosamente con selector: {}", selector);
                    return this;
                }
                
            } catch (Exception e) {
                logger.debug("❌ Selector username falló: {} - {}", selector, e.getMessage());
            }
        }
        
        throw new RuntimeException("No se pudo introducir username después de intentar todos los selectores");
    }

    /**
     * Introduce password con múltiples estrategias.
     */
    public PaginaLogin introducirPassword(String password) {
        logger.debug("Introduciendo contraseña");
        
        for (String selector : SELECTORES_PASSWORD) {
            try {
                WebElement campo = buscarElementoVisible(By.cssSelector(selector), TIMEOUT_ELEMENTO);
                campo.clear();
                campo.sendKeys(password);
                
                // Para password no podemos verificar el valor por seguridad
                // pero podemos verificar que el campo no esté vacío
                String valorAtributo = campo.getAttribute("value");
                if (valorAtributo != null && !valorAtributo.isEmpty()) {
                    logger.debug("✅ Password introducido exitosamente con selector: {}", selector);
                    return this;
                }
                
            } catch (Exception e) {
                logger.debug("❌ Selector password falló: {} - {}", selector, e.getMessage());
            }
        }
        
        throw new RuntimeException("No se pudo introducir password después de intentar todos los selectores");
    }

    /**
     * Introduce email (alias para compatibilidad).
     */
    public PaginaLogin introducirEmail(String email) {
        return introducirUsername(email);
    }

    /**
     * Hace clic en login con estrategias múltiples y verificación.
     */
    public void hacerClicLogin() {
        logger.info("Haciendo clic en botón de login con estrategias múltiples");
        
        // Estrategia 1: Selectores específicos
        for (String selector : SELECTORES_SUBMIT) {
            try {
                WebElement boton = buscarElementoClickeable(By.cssSelector(selector), TIMEOUT_ELEMENTO);
                boton.click();
                logger.debug("✅ Click exitoso con selector: {}", selector);
                esperarRespuestaLogin();
                return;
                
            } catch (Exception e) {
                logger.debug("❌ Selector submit falló: {} - {}", selector, e.getMessage());
            }
        }
        
        // Estrategia 2: Enter en campo password
        logger.debug("Intentando envío con Enter en campo password");
        try {
            for (String selector : SELECTORES_PASSWORD) {
                if (estaElementoPresente(By.cssSelector(selector))) {
                    WebElement campoPassword = driver.findElement(By.cssSelector(selector));
                    campoPassword.sendKeys(Keys.ENTER);
                    logger.debug("✅ Envío con Enter exitoso");
                    esperarRespuestaLogin();
                    return;
                }
            }
        } catch (Exception e) {
            logger.debug("❌ Envío con Enter falló: {}", e.getMessage());
        }
        
        // Estrategia 3: JavaScript click como último recurso
        logger.debug("Intentando click con JavaScript");
        try {
            for (String selector : SELECTORES_SUBMIT) {
                if (estaElementoPresente(By.cssSelector(selector))) {
                    hacerClicConJavaScript(By.cssSelector(selector));
                    logger.debug("✅ JavaScript click exitoso");
                    esperarRespuestaLogin();
                    return;
                }
            }
        } catch (Exception e) {
            logger.debug("❌ JavaScript click falló: {}", e.getMessage());
        }
        
        throw new RuntimeException("No se pudo hacer clic en submit después de intentar todas las estrategias");
    }

    /**
     * Limpia campos con verificación.
     */
    public PaginaLogin limpiarCamposLogin() {
        logger.info("Limpiando campos del formulario");
        
        // Limpiar username
        for (String selector : SELECTORES_USERNAME) {
            try {
                if (estaElementoPresente(By.cssSelector(selector))) {
                    WebElement campo = driver.findElement(By.cssSelector(selector));
                    campo.clear();
                    logger.debug("Username limpiado con selector: {}", selector);
                    break;
                }
            } catch (Exception e) {
                logger.debug("Error limpiando username con {}: {}", selector, e.getMessage());
            }
        }
        
        // Limpiar password
        for (String selector : SELECTORES_PASSWORD) {
            try {
                if (estaElementoPresente(By.cssSelector(selector))) {
                    WebElement campo = driver.findElement(By.cssSelector(selector));
                    campo.clear();
                    logger.debug("Password limpiado con selector: {}", selector);
                    break;
                }
            } catch (Exception e) {
                logger.debug("Error limpiando password con {}: {}", selector, e.getMessage());
            }
        }
        
        return this;
    }

    // ===== MÉTODOS DE LOGIN PRINCIPAL =====

    /**
     * Proceso completo de login con Usuario.
     */
    public boolean iniciarSesion(Usuario usuario) {
        return iniciarSesion(usuario.getEmail(), usuario.getPassword(), false);
    }

    /**
     * Proceso completo de login con credenciales.
     */
    public boolean iniciarSesion(String username, String password) {
        return iniciarSesion(username, password, false);
    }

    /**
     * Proceso completo de login robusto.
     */
    public boolean iniciarSesion(String username, String password, boolean recordar) {
        logger.info("Iniciando sesión robusta para usuario: {}", username);

        try {
            // Paso 1: Verificar que estamos en la página correcta
            if (!estaPaginaCargada()) {
                logger.warn("Página no cargada correctamente, navegando...");
                navegarAPaginaLogin();
            }

            // Paso 2: Limpiar campos
            limpiarCamposLogin();
            pausar(Duration.ofMillis(500)); // Breve pausa para estabilidad

            // Paso 3: Llenar formulario
            introducirUsername(username);
            pausar(Duration.ofMillis(300));
            introducirPassword(password);
            pausar(Duration.ofMillis(300));

            // Paso 4: Recordar credenciales si es necesario
            if (recordar) {
                marcarRecordarCredenciales(true);
            }

            // Paso 5: Enviar formulario
            hacerClicLogin();

            // Paso 6: Verificar resultado
            boolean resultado = verificarLoginExitoso();
            
            if (resultado) {
                logger.info("✅ Login exitoso para usuario: {}", username);
            } else {
                logger.warn("❌ Login falló para usuario: {}", username);
                List<String> errores = obtenerErroresLogin();
                if (!errores.isEmpty()) {
                    logger.warn("Errores detectados: {}", errores);
                }
            }
            
            return resultado;

        } catch (Exception e) {
            logger.error("Error crítico durante login: {}", e.getMessage(), e);
            return false;
        }
    }

    // ===== MÉTODOS DE VERIFICACIÓN ROBUSTOS =====

    /**
     * Cierra modales o popups que puedan aparecer después del login.
     * Maneja casos como publicidad, banners promocionales, etc.
     */
    public void cerrarModalesPostLogin() {
        logger.debug("Verificando y cerrando modales post-login");
        
        try {
            // Esperar un poco para que aparezcan los modales
            pausar(Duration.ofMillis(2000));
            
            // Verificar si hay algún modal visible o si la URL contiene indicadores de modal
            String urlActual = obtenerUrlActual();
            boolean tieneFragmentoModal = urlActual.contains("#google_vignette") || 
                                        urlActual.contains("#ad") || 
                                        urlActual.contains("#popup") ||
                                        urlActual.contains("#modal");
            
            boolean modalPresente = tieneFragmentoModal;
            for (String selector : SELECTORES_MODAL_CONTAINER) {
                if (estaElementoVisible(By.cssSelector(selector))) {
                    modalPresente = true;
                    logger.info("Modal detectado con selector: {}", selector);
                    break;
                }
            }
            
            if (!modalPresente) {
                logger.debug("No se detectaron modales post-login");
                return;
            }
            
            logger.info("Modal detectado - iniciando estrategias de cierre agresivas");
            
            // Intentar cerrar el modal con diferentes estrategias
            boolean modalCerrado = false;
            
            // Estrategia 1: Tecla ESC múltiples veces (muy efectiva contra ads)
            try {
                for (int i = 0; i < 3; i++) {
                    driver.switchTo().activeElement().sendKeys(Keys.ESCAPE);
                    pausar(Duration.ofMillis(500));
                }
                logger.info("✅ Enviadas teclas ESC preventivas");
            } catch (Exception e) {
                logger.debug("Error enviando ESC: {}", e.getMessage());
            }
            
            // Estrategia 2: Botón de cierre específico
            for (String selector : SELECTORES_MODAL_CIERRE) {
                try {
                    if (estaElementoPresente(By.cssSelector(selector)) && 
                        estaElementoVisible(By.cssSelector(selector))) {
                        hacerClic(By.cssSelector(selector));
                        logger.info("✅ Modal cerrado con botón: {}", selector);
                        modalCerrado = true;
                        break;
                    }
                } catch (Exception e) {
                    logger.debug("No se pudo cerrar modal con selector {}: {}", selector, e.getMessage());
                }
            }
            
            // Estrategia 3: Click fuera del modal (en el overlay)
            if (!modalCerrado) {
                try {
                    for (String selector : SELECTORES_MODAL_CONTAINER) {
                        if (estaElementoPresente(By.cssSelector(selector))) {
                            // Click en el overlay, no en el contenido del modal
                            hacerClicConJavaScript(By.cssSelector(selector));
                            logger.info("✅ Modal cerrado con click en overlay");
                            modalCerrado = true;
                            break;
                        }
                    }
                } catch (Exception e) {
                    logger.debug("No se pudo cerrar modal con click en overlay: {}", e.getMessage());
                }
            }
            
            // Estrategia 4: Navegación directa si sigue con fragmento modal
            String urlDespues = obtenerUrlActual();
            if (urlDespues.contains("#google_vignette") || urlDespues.contains("#ad")) {
                try {
                    // Navegar a la URL limpia (sin fragmento)
                    String urlLimpia = urlDespues.split("#")[0];
                    if (urlLimpia.endsWith("/login")) {
                        // Si todavía estamos en login, ir a secure
                        urlLimpia = config.obtenerUrlPrincipal();
                    }
                    logger.info("Navegando a URL limpia para evitar modal: {}", urlLimpia);
                    navegarA(urlLimpia);
                    modalCerrado = true;
                } catch (Exception e) {
                    logger.debug("Error navegando a URL limpia: {}", e.getMessage());
                }
            }
            
            // Estrategia 5: JavaScript para eliminar elementos modal
            if (!modalCerrado) {
                try {
                    String jsScript = 
                        "var modals = document.querySelectorAll('.modal, .popup, .overlay, [id*=\"modal\"], [id*=\"popup\"], [id*=\"ad\"]');" +
                        "modals.forEach(function(modal) { modal.style.display = 'none'; modal.remove(); });" +
                        "document.body.style.overflow = 'auto';" +
                        "return modals.length;";
                    
                    Object resultado = ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(jsScript);
                    if (resultado != null) {
                        logger.info("✅ Ejecutado JavaScript para eliminar modales: {}", resultado);
                        modalCerrado = true;
                    }
                } catch (Exception e) {
                    logger.debug("Error eliminando modales con JavaScript: {}", e.getMessage());
                }
            }
            
            // Esperar que el modal desaparezca
            if (modalCerrado) {
                pausar(Duration.ofMillis(1500));
                
                // Verificar que efectivamente se cerró
                String urlFinal = obtenerUrlActual();
                boolean sinFragmentoModal = !urlFinal.contains("#google_vignette") && 
                                          !urlFinal.contains("#ad") && 
                                          !urlFinal.contains("#popup");
                
                boolean sinModalVisible = true;
                for (String selector : SELECTORES_MODAL_CONTAINER) {
                    if (estaElementoVisible(By.cssSelector(selector))) {
                        sinModalVisible = false;
                        break;
                    }
                }
                
                if (sinFragmentoModal && sinModalVisible) {
                    logger.info("✅ Modal cerrado exitosamente - URL limpia: {}", urlFinal);
                } else {
                    logger.warn("❌ Modal puede seguir presente - URL: {}", urlFinal);
                }
            } else {
                logger.warn("❌ No se pudo cerrar el modal con ninguna estrategia");
            }
            
        } catch (Exception e) {
            logger.warn("Error manejando modales post-login: {}", e.getMessage());
        }
    }

    /**
     * Verifica login exitoso con múltiples validaciones.
     */
    public boolean verificarLoginExitoso() {
        logger.debug("Verificando login exitoso con múltiples estrategias");
        
        try {
            // Primero cerrar cualquier modal/popup que pueda aparecer
            cerrarModalesPostLogin();
            
            // Verificación 1: URL cambió a /secure
            String urlActual = obtenerUrlActual();
            if (urlActual.contains("/secure")) {
                logger.info("✅ Login exitoso - URL contiene /secure: {}", urlActual);
                return true;
            }
            
            // Verificación 2: Mensaje flash de éxito
            if (verificarMensajeExito()) {
                logger.info("✅ Login exitoso - Mensaje de éxito detectado");
                return true;
            }
            
            // Verificación 3: Presencia de elementos de página segura
            String[] selectoresPageSegura = {
                "a[href*='logout']",
                "button[onclick*='logout']", 
                ".logout",
                "a[href='/logout']",
                "form[action*='logout']"
            };
            
            for (String selector : selectoresPageSegura) {
                if (estaElementoPresente(By.cssSelector(selector))) {
                    logger.info("✅ Login exitoso - Elemento de página segura detectado: {}", selector);
                    return true;
                }
            }
            
            // Verificación 4: Título de página cambió
            String titulo = obtenerTituloActual().toLowerCase();
            if (titulo.contains("secure") || titulo.contains("welcome") || titulo.contains("dashboard")) {
                logger.info("✅ Login exitoso - Título indica página segura: {}", titulo);
                return true;
            }
            
            // Verificación 5: No hay errores Y salimos de /login
            boolean sinErrores = !hayErroresLogin();
            boolean fueraDeLogin = !urlActual.endsWith("/login");
            
            if (sinErrores && fueraDeLogin) {
                logger.info("✅ Login exitoso - Sin errores y fuera de página login");
                return true;
            }
            
            logger.debug("❌ Ninguna verificación de login exitoso se cumplió");
            logearEstadoActual();
            return false;
            
        } catch (Exception e) {
            logger.error("Error verificando login exitoso: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Verifica si hay mensajes de éxito específicos.
     */
    private boolean verificarMensajeExito() {
        String[] selectoresMensajeExito = {
            "#flash",
            ".alert-success",
            ".success-message",
            ".message.success"
        };
        
        for (String selector : selectoresMensajeExito) {
            try {
                if (estaElementoVisible(By.cssSelector(selector))) {
                    String mensaje = obtenerTexto(By.cssSelector(selector)).toLowerCase();
                    if (mensaje.contains("logged into a secure area") || 
                        mensaje.contains("success") || 
                        mensaje.contains("welcome")) {
                        return true;
                    }
                }
            } catch (Exception e) {
                // Continuar con el siguiente selector
            }
        }
        
        return false;
    }

    /**
     * Verifica errores con múltiples estrategias.
     */
    public boolean hayErroresLogin() {
        String[] selectoresError = {
            "#flash",
            ".alert-danger",
            ".error-message", 
            ".message.error",
            ".alert.alert-danger"
        };
        
        for (String selector : selectoresError) {
            try {
                if (estaElementoVisible(By.cssSelector(selector))) {
                    String mensaje = obtenerTexto(By.cssSelector(selector)).toLowerCase();
                    if (mensaje.contains("invalid") || 
                        mensaje.contains("error") ||
                        mensaje.contains("incorrect") || 
                        mensaje.contains("failed") ||
                        mensaje.contains("required")) {
                        return true;
                    }
                }
            } catch (Exception e) {
                // Continuar con el siguiente selector
            }
        }
        
        return false;
    }

    /**
     * Obtiene errores con búsqueda exhaustiva.
     */
    public List<String> obtenerErroresLogin() {
        List<String> errores = new ArrayList<>();
        
        String[] selectoresError = {
            "#flash",
            ".alert-danger", 
            ".error-message",
            ".message.error",
            ".alert.alert-danger",
            ".field-error"
        };
        
        for (String selector : selectoresError) {
            try {
                if (estaElementoVisible(By.cssSelector(selector))) {
                    String mensaje = obtenerTexto(By.cssSelector(selector)).trim();
                    if (!mensaje.isEmpty() && !errores.contains(mensaje)) {
                        errores.add(mensaje);
                    }
                }
            } catch (Exception e) {
                // Continuar con el siguiente selector
            }
        }
        
        return errores;
    }

    /**
     * Verifica disponibilidad del formulario con múltiples checks.
     */
    public boolean formularioLoginDisponible() {
        // Verificar que al menos tengamos username + password + submit
        boolean tieneUsername = false;
        boolean tienePassword = false;
        boolean tieneSubmit = false;
        
        // Buscar campo username
        for (String selector : SELECTORES_USERNAME) {
            if (estaElementoPresente(By.cssSelector(selector))) {
                tieneUsername = true;
                break;
            }
        }
        
        // Buscar campo password
        for (String selector : SELECTORES_PASSWORD) {
            if (estaElementoPresente(By.cssSelector(selector))) {
                tienePassword = true;
                break;
            }
        }
        
        // Buscar botón submit
        for (String selector : SELECTORES_SUBMIT) {
            if (estaElementoPresente(By.cssSelector(selector))) {
                tieneSubmit = true;
                break;
            }
        }
        
        boolean disponible = tieneUsername && tienePassword && tieneSubmit;
        logger.debug("Formulario disponible: {} (username: {}, password: {}, submit: {})", 
                    disponible, tieneUsername, tienePassword, tieneSubmit);
        
        return disponible;
    }

    // ===== MÉTODOS DE UTILIDAD MEJORADOS =====

    /**
     * Checkbox recordar credenciales con múltiples estrategias.
     */
    public PaginaLogin marcarRecordarCredenciales(boolean recordar) {
        String[] selectoresCheckbox = {
            "input[type='checkbox']",
            "input[name='remember']",
            "#remember",
            ".remember-me input"
        };
        
        for (String selector : selectoresCheckbox) {
            try {
                if (estaElementoPresente(By.cssSelector(selector))) {
                    WebElement checkbox = buscarElementoClickeable(By.cssSelector(selector));
                    if (checkbox.isSelected() != recordar) {
                        checkbox.click();
                        logger.debug("Checkbox recordar configurado a: {} con selector: {}", recordar, selector);
                    }
                    return this;
                }
            } catch (Exception e) {
                logger.debug("Error con checkbox selector {}: {}", selector, e.getMessage());
            }
        }
        
        logger.debug("No se encontró checkbox para recordar credenciales");
        return this;
    }

    /**
     * Espera respuesta del servidor con verificaciones múltiples y manejo de modales.
     */
    private void esperarRespuestaLogin() {
        logger.debug("Esperando respuesta del servidor con manejo anti-modales");
        
        try {
            // Estrategia agresiva: presionar ESC inmediatamente y múltiples veces
            for (int i = 0; i < 5; i++) {
                try {
                    driver.switchTo().activeElement().sendKeys(Keys.ESCAPE);
                    pausar(Duration.ofMillis(300));
                } catch (Exception e) {
                    // Continuar aunque falle
                }
            }
            
            // Esperar cambio de URL O mensaje flash O loader desaparece
            espera.until(driver -> {
                String urlActual = driver.getCurrentUrl();
                boolean cambioUrl = !urlActual.endsWith("/login") || urlActual.contains("/secure");
                boolean hayMensaje = estaElementoVisible(By.cssSelector("#flash, .alert, .message"));
                boolean sinLoader = !estaElementoVisible(By.cssSelector(".loader, .spinner"));
                
                // Enviar ESC cada iteración para prevenir modales
                try {
                    driver.switchTo().activeElement().sendKeys(Keys.ESCAPE);
                } catch (Exception e) {
                    // Ignorar errores
                }
                
                return cambioUrl || hayMensaje || sinLoader;
            });
            
            // Manejo inmediato de modales si aparecen
            cerrarModalesInmediatos();
            
            // Pausa adicional para estabilidad
            pausar(Duration.ofMillis(1500));
            
            // Verificación final de modales
            String urlFinal = obtenerUrlActual();
            if (urlFinal.contains("#google_vignette") || urlFinal.contains("#ad")) {
                logger.warn("Detectado fragmento de modal en URL, aplicando correcciones agresivas");
                aplicarCorrecionesAgresivasModal();
            }
            
        } catch (Exception e) {
            logger.debug("Timeout esperando respuesta de login: {}", e.getMessage());
            // Aplicar correcciones como fallback
            cerrarModalesInmediatos();
        }
    }
    
    /**
     * Cierra modales de forma inmediata sin esperas largas.
     */
    private void cerrarModalesInmediatos() {
        try {
            // ESC múltiples
            for (int i = 0; i < 3; i++) {
                driver.switchTo().activeElement().sendKeys(Keys.ESCAPE);
                pausar(Duration.ofMillis(200));
            }
            
            // Buscar y cerrar elementos modal rápidamente
            String[] selectoresRapidos = {
                "iframe[src*='google']",
                "iframe[src*='ads']",
                ".modal",
                ".popup",
                "[role='dialog']"
            };
            
            for (String selector : selectoresRapidos) {
                try {
                    if (estaElementoPresente(By.cssSelector(selector))) {
                        // Click en el elemento para cerrarlo
                        hacerClicConJavaScript(By.cssSelector(selector));
                        logger.debug("Modal cerrado inmediatamente: {}", selector);
                    }
                } catch (Exception e) {
                    // Continuar con el siguiente
                }
            }
            
        } catch (Exception e) {
            logger.debug("Error en cierre inmediato de modales: {}", e.getMessage());
        }
    }
    
    /**
     * Aplica correcciones agresivas cuando se detectan modales persistentes.
     */
    private void aplicarCorrecionesAgresivasModal() {
        try {
            logger.info("Aplicando correcciones agresivas para modales persistentes");
            
            // JavaScript para eliminar todos los overlays y modales
            String jsLimpieza = 
                "document.querySelectorAll('iframe[src*=\"google\"], iframe[src*=\"ads\"], .modal, .popup, .overlay, [role=\"dialog\"]').forEach(el => el.remove());" +
                "document.body.style.overflow = 'auto';" +
                "window.location.hash = '';";
            
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(jsLimpieza);
            
            // Forzar navegación limpia
            String urlLimpia = obtenerUrlActual().split("#")[0];
            if (urlLimpia.endsWith("/login")) {
                // Navegar directamente a /secure si estamos logueados
                String urlSegura = config.obtenerUrlPrincipal();
                logger.info("Navegación forzada a área segura: {}", urlSegura);
                navegarA(urlSegura);
            }
            
            pausar(Duration.ofMillis(1000));
            
        } catch (Exception e) {
            logger.warn("Error en correcciones agresivas: {}", e.getMessage());
        }
    }

    /**
     * Obtiene valores del formulario con verificación robusta.
     */
    public String[] obtenerValoresFormulario() {
        String[] valores = new String[2];
        valores[0] = "";
        valores[1] = "";
        
        // Obtener valor username
        for (String selector : SELECTORES_USERNAME) {
            try {
                if (estaElementoPresente(By.cssSelector(selector))) {
                    valores[0] = obtenerAtributo(By.cssSelector(selector), "value");
                    break;
                }
            } catch (Exception e) {
                // Continuar con siguiente selector
            }
        }
        
        // Obtener valor password (limitado por seguridad)
        for (String selector : SELECTORES_PASSWORD) {
            try {
                if (estaElementoPresente(By.cssSelector(selector))) {
                    String valor = obtenerAtributo(By.cssSelector(selector), "value");
                    valores[1] = (valor != null && !valor.isEmpty()) ? "***" : "";
                    break;
                }
            } catch (Exception e) {
                // Continuar con siguiente selector
            }
        }
        
        return valores;
    }

    /**
     * Logging detallado del estado actual.
     */
    public void logearInformacionLogin() {
        logger.debug("=== INFORMACIÓN DETALLADA DE LOGIN ===");
        logger.debug("URL actual: {}", obtenerUrlActual());
        logger.debug("Título: {}", obtenerTituloActual());
        logger.debug("Página cargada: {}", estaPaginaCargada());
        logger.debug("Formulario disponible: {}", formularioLoginDisponible());
        logger.debug("Hay errores: {}", hayErroresLogin());
        
        if (hayErroresLogin()) {
            logger.debug("Errores: {}", obtenerErroresLogin());
        }
        
        String[] valores = obtenerValoresFormulario();
        logger.debug("Username actual: '{}'", valores[0]);
        logger.debug("Password field populated: {}", !valores[1].isEmpty());
        
        logger.debug("====================================");
    }

    /**
     * Logging del estado actual para debugging.
     */
    private void logearEstadoActual() {
        logger.debug("Estado actual de la página:");
        logger.debug("- URL: {}", obtenerUrlActual());
        logger.debug("- Título: {}", obtenerTituloActual());
        logger.debug("- Hay errores: {}", hayErroresLogin());
        logger.debug("- Errores: {}", obtenerErroresLogin());
    }

    // ===== MÉTODOS DE VALIDACIÓN ADICIONALES =====

    /**
     * Verifica credenciales inválidas.
     */
    public boolean credencialesInvalidas() {
        List<String> errores = obtenerErroresLogin();
        return errores.stream().anyMatch(error -> 
            error.toLowerCase().contains("invalid username") || 
            error.toLowerCase().contains("invalid password")
        );
    }

    /**
     * Verificación con campos vacíos.
     */
    public boolean verificarCamposVacios() {
        logger.info("Verificando comportamiento con campos vacíos");
        limpiarCamposLogin();
        hacerClicLogin();
        return hayErroresLogin();
    }

    /**
     * Verificación con password vacío.
     */
    public boolean verificarPasswordVacio(String username) {
        logger.info("Verificando comportamiento con password vacío");
        limpiarCamposLogin();
        introducirUsername(username);
        hacerClicLogin();
        return hayErroresLogin();
    }

    /**
     * Verificación con username vacío.
     */
    public boolean verificarEmailVacio(String password) {
        logger.info("Verificando comportamiento con username vacío");
        limpiarCamposLogin();
        introducirPassword(password);
        hacerClicLogin();
        return hayErroresLogin();
    }

    /**
     * Intentos múltiples para verificar bloqueo.
     */
    public boolean intentarHastaBloqueoCuenta(String username, String passwordIncorrecto) {
        logger.info("Verificando bloqueo por intentos múltiples");
        
        int maxIntentos = 3; // Reducido para evitar problemas
        
        for (int i = 1; i <= maxIntentos; i++) {
            logger.debug("Intento {} de {} con credenciales incorrectas", i, maxIntentos);
            
            navegarAPaginaLogin(); // Asegurar que estamos en login
            iniciarSesion(username, passwordIncorrecto);
            
            List<String> errores = obtenerErroresLogin();
            boolean cuentaBloqueada = errores.stream().anyMatch(error -> 
                error.toLowerCase().contains("blocked") ||
                error.toLowerCase().contains("locked") ||
                error.toLowerCase().contains("too many") ||
                error.toLowerCase().contains("exceeded")
            );
            
            if (cuentaBloqueada) {
                logger.info("Cuenta bloqueada detectada después de {} intentos", i);
                return true;
            }
            
            pausar(Duration.ofMillis(2000)); // Pausa entre intentos
        }
        
        logger.info("No se detectó bloqueo después de {} intentos", maxIntentos);
        return false;
    }
}