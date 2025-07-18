package com.robertorivas.automatizacion.paginas;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

<<<<<<< HEAD
import java.util.ArrayList;
import java.util.List;

/**
 * Page Object para la página principal después del login exitoso.
 * Maneja las funcionalidades disponibles una vez que el usuario está autenticado.
 * 
 * Principios aplicados:
 * - Page Object Model: Encapsula elementos y acciones de la página principal
 * - Single Responsibility: Solo maneja operaciones de la página principal
 * - Encapsulation: Elementos privados con métodos públicos de interacción
 * - DRY: Reutiliza funcionalidad de la clase base
 * 
 * @author Roberto Rivas Lopez
 */
public class PaginaPrincipal extends PaginaBase {
    
    // ===== LOCALIZADORES DE ELEMENTOS =====
    
    // Elementos de navegación principal  
    @FindBy(css = "a[href*='logout'], .wp-block-button__link, a:contains('Log out')")
    private WebElement botonLogout;
    
    @FindBy(css = ".post-title, h1")
    private WebElement mensajePrincipal;
    
    @FindBy(css = ".post-content, .entry-content")
    private WebElement contenidoPrincipal;
    
    // Localizadores dinámicos
    private static final By INDICADORES_LOGIN = By.cssSelector(".logged-in, .authenticated, .user-session");
    private static final By CONTENIDO_AUTENTICADO = By.cssSelector(".dashboard, .main-panel, .user-content");
    private static final By LOADER = By.cssSelector(".loader, .spinner, .loading");
    
    /**
     * Constructor de la página principal.
     */
    public PaginaPrincipal(WebDriver driver) {
        super(driver);
        logger.info("Inicializando página principal");
    }
    
    // ===== MÉTODOS ABSTRACTOS IMPLEMENTADOS =====
    
    @Override
    public boolean estaPaginaCargada() {
        try {
            // Verificar URL específica de la página de éxito
            String urlActual = obtenerUrlActual();
            boolean urlValida = urlActual.contains("logged-in-successfully");
            
            // Verificar contenido específico de la página
            String paginaTexto = driver.getPageSource().toLowerCase();
            boolean contenidoValido = paginaTexto.contains("logged in successfully") ||
                                     paginaTexto.contains("congratulations");
            
            // Verificar elementos específicos de la página
            boolean tieneElementos = estaElementoVisible(By.cssSelector("h1")) ||
                                   estaElementoVisible(By.cssSelector(".post-title"));
            
            boolean paginaCargada = urlValida && (contenidoValido || tieneElementos);
            
            logger.debug("Verificación página principal - URL: {}, Contenido: {}, Elementos: {}, Resultado: {}", 
                        urlValida, contenidoValido, tieneElementos, paginaCargada);
            
            return paginaCargada;
            
        } catch (Exception e) {
            logger.debug("Error verificando si la página principal está cargada: {}", e.getMessage());
            return false;
        }
    }
    
    @Override
    public void esperarCargaPagina() {
        logger.debug("Esperando carga completa de la página principal");
        
        // Esperar que desaparezca el loader
        esperarQueElementoDesaparezca(LOADER, TIMEOUT_MEDIO);
        
        // Esperar URL específica o contenido específico
        try {
            espera.until(driver -> {
                String urlActual = obtenerUrlActual();
                boolean urlCorrecta = urlActual.contains("logged-in-successfully");
                boolean contenidoPresente = driver.getPageSource().toLowerCase().contains("logged in successfully");
                return urlCorrecta || contenidoPresente;
            });
        } catch (Exception e) {
            logger.debug("Timeout esperando elementos de página principal");
        }
        
        logger.info("Página principal cargada");
    }
    
    @Override
    public String obtenerTituloPagina() {
        return "Página Principal";
    }
    
    // ===== MÉTODOS DE VERIFICACIÓN DE ESTADO =====
    
    /**
     * Verifica si el usuario está logueado.
     */
    public boolean usuarioLogueado() {
        try {
            // Verificar URL específica
            String urlActual = obtenerUrlActual();
            boolean urlAutenticada = urlActual.contains("logged-in-successfully");
            
            // Verificar contenido específico de la página
            String paginaTexto = driver.getPageSource().toLowerCase();
            boolean contenidoAutenticado = paginaTexto.contains("logged in successfully") ||
                                         paginaTexto.contains("congratulations");
            
            // Verificar elementos específicos
            boolean tieneBotonLogout = estaElementoPresente(By.cssSelector("a[href*='logout'], .wp-block-button__link")) ||
                                     paginaTexto.contains("log out");
            
            boolean usuarioLogueado = urlAutenticada && (contenidoAutenticado || tieneBotonLogout);
            
            logger.debug("Verificación usuario logueado - URL: {}, Contenido: {}, Logout: {}, Resultado: {}", 
                        urlAutenticada, contenidoAutenticado, tieneBotonLogout, usuarioLogueado);
            
            return usuarioLogueado;
            
        } catch (Exception e) {
            logger.debug("Error verificando si usuario está logueado: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Obtiene el nombre del usuario logueado.
     */
    public String obtenerNombreUsuarioLogueado() {
        try {
            // Intentar obtener de diferentes elementos
            if (estaElementoVisible(By.cssSelector(".username, .user-name, #userName"))) {
                String nombre = obtenerTexto(By.cssSelector(".username, .user-name, #userName"));
                if (nombre != null && !nombre.trim().isEmpty()) {
                    return nombre.trim();
                }
            }
            
            // Buscar en mensajes de bienvenida
            if (estaElementoVisible(By.cssSelector(".welcome-user, .welcome-message"))) {
                String mensaje = obtenerTexto(By.cssSelector(".welcome-user, .welcome-message"));
                if (mensaje != null && !mensaje.trim().isEmpty()) {
                    return extraerNombreDelMensaje(mensaje);
                }
            }
            
            // Buscar en el texto de la página
            String paginaTexto = driver.getPageSource();
            if (paginaTexto.contains("Logged In Successfully!")) {
                return "Usuario Autenticado";
            }
            
            return "Usuario";
            
        } catch (Exception e) {
            logger.debug("Error obteniendo nombre de usuario: {}", e.getMessage());
            return "Usuario";
        }
    }
    
    /**
     * Obtiene el mensaje de bienvenida.
     */
    public String obtenerMensajeBienvenida() {
        try {
            // Buscar en diferentes elementos
            if (estaElementoVisible(By.cssSelector(".welcome-message, .alert-success"))) {
                return obtenerTexto(By.cssSelector(".welcome-message, .alert-success"));
            }
            
            if (estaElementoVisible(By.cssSelector("h1, .main-title"))) {
                String titulo = obtenerTexto(By.cssSelector("h1, .main-title"));
                if (titulo != null && (titulo.toLowerCase().contains("welcome") || titulo.toLowerCase().contains("logged"))) {
                    return titulo;
                }
            }
            
            // Buscar texto específico común
            String paginaTexto = driver.getPageSource();
            if (paginaTexto.contains("Logged In Successfully!")) {
                return "Logged In Successfully!";
            }
            if (paginaTexto.contains("Congratulations")) {
                return "Congratulations! You logged into a secure area!";
            }
            
            return null;
            
        } catch (Exception e) {
            logger.debug("Error obteniendo mensaje de bienvenida: {}", e.getMessage());
            return null;
        }
    }
    
    // ===== MÉTODOS DE ACCIONES =====
    
    /**
     * Cierra la sesión del usuario.
     */
    public boolean cerrarSesion() {
        logger.info("Cerrando sesión de usuario");
        
        try {
            // Buscar botón de logout específico de la página
            By selectorLogout = By.cssSelector("a[href*='logout'], .wp-block-button__link");
            
            // Si no encuentra por CSS, buscar por texto
            if (!estaElementoPresente(selectorLogout)) {
                // Buscar por XPath usando texto "Log out"
                selectorLogout = By.xpath("//a[contains(text(), 'Log out') or contains(text(), 'Logout')]");
            }
            
            if (estaElementoPresente(selectorLogout)) {
                hacerClic(selectorLogout);
                
                // Esperar a que se complete el logout
                esperarLogout();
                
                // Verificar que el logout fue exitoso
                boolean logoutExitoso = !usuarioLogueado();
                
                if (logoutExitoso) {
                    logger.info("Logout completado exitosamente");
                } else {
                    logger.warn("El logout no se completó correctamente");
                }
                
                return logoutExitoso;
                
            } else {
                logger.warn("No se encontró botón de logout");
                return false;
            }
            
        } catch (Exception e) {
            logger.error("Error durante el logout: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Navega al perfil del usuario.
     */
    public void irAPerfil() {
        logger.info("Navegando al perfil del usuario");
        if (estaElementoPresente(By.cssSelector("a[href*='profile'], .profile-link"))) {
            hacerClic(By.cssSelector("a[href*='profile'], .profile-link"));
        }
    }
    
    /**
     * Navega a la configuración.
     */
    public void irAConfiguracion() {
        logger.info("Navegando a configuración");
        if (estaElementoPresente(By.cssSelector("a[href*='settings'], .settings-link"))) {
            hacerClic(By.cssSelector("a[href*='settings'], .settings-link"));
        }
    }
    
    /**
     * Expande el menú de usuario si existe.
     */
    public void expandirMenuUsuario() {
        logger.debug("Expandiendo menú de usuario");
        if (estaElementoPresente(By.cssSelector(".user-menu, .profile-menu"))) {
            hacerClic(By.cssSelector(".user-menu, .profile-menu"));
        }
    }
    
    // ===== MÉTODOS DE NAVEGACIÓN =====
    
    /**
     * Navega a la página principal (home).
     */
    public void irAInicio() {
        logger.info("Navegando a página de inicio");
        if (estaElementoPresente(By.cssSelector("a[href*='home'], .home-link"))) {
            hacerClic(By.cssSelector("a[href*='home'], .home-link"));
        }
    }
    
    // ===== MÉTODOS DE UTILIDAD PRIVADOS =====
    
    /**
     * Espera a que se complete el proceso de logout.
     */
    private void esperarLogout() {
        logger.debug("Esperando completar logout");
        
        // Esperar que desaparezcan elementos de usuario autenticado
        esperarQueElementoDesaparezca(By.cssSelector(".user-menu, .profile-menu"), TIMEOUT_MEDIO);
        
        // Esperar que aparezcan elementos de login o que cambie la URL
        try {
            espera.until(driver -> {
                String urlActual = obtenerUrlActual();
                boolean enLogin = urlActual.contains("login") || 
                                urlActual.contains("home") ||
                                estaElementoPresente(By.cssSelector("input[name='username'], input[name='password']"));
                
                return enLogin || !usuarioLogueado();
            });
        } catch (Exception e) {
            logger.debug("Timeout esperando logout");
        }
        
        // Pausa adicional para estabilizar
        pausar(TIMEOUT_CORTO.dividedBy(2));
    }
    
    /**
     * Extrae el nombre del usuario de un mensaje de bienvenida.
     */
    private String extraerNombreDelMensaje(String mensaje) {
        if (mensaje == null || mensaje.trim().isEmpty()) {
            return "Usuario";
        }
        
        // Patrones comunes de mensajes de bienvenida
        String[] patrones = {
            "Welcome, (.*?)!",
            "Hello, (.*?)!",
            "Hi, (.*?)!",
            "Welcome (.*?)!",
            "Hello (.*?)!",
            "Bienvenido, (.*?)!"
        };
        
        for (String patron : patrones) {
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(patron);
            java.util.regex.Matcher matcher = pattern.matcher(mensaje);
            if (matcher.find()) {
                return matcher.group(1).trim();
            }
        }
        
        return "Usuario";
    }
    
    // ===== MÉTODOS DE INFORMACIÓN Y DEBUG =====
    
    /**
     * Obtiene información completa de la página principal.
     */
    public void logearInformacionPaginaPrincipal() {
        logger.debug("=== INFORMACIÓN PÁGINA PRINCIPAL ===");
        logger.debug("Usuario logueado: {}", usuarioLogueado());
        logger.debug("Nombre usuario: {}", obtenerNombreUsuarioLogueado());
        logger.debug("Mensaje bienvenida: {}", obtenerMensajeBienvenida());
        logger.debug("Título: {}", obtenerTituloActual());
        logger.debug("URL: {}", obtenerUrlActual());
        logger.debug("Botón logout presente: {}", estaElementoPresente(By.cssSelector(".logout, #logout")));
        logger.debug("Menú usuario presente: {}", estaElementoPresente(By.cssSelector(".user-menu, .profile-menu")));
        logger.debug("====================================");
    }
    
    /**
     * Obtiene el estado actual de la sesión.
     */
    public String obtenerEstadoSesion() {
        if (usuarioLogueado()) {
            return "Autenticado - " + obtenerNombreUsuarioLogueado();
        } else {
            return "No autenticado";
        }
    }
    
    /**
     * Verifica si hay elementos de error en la página principal.
     */
    public boolean hayErroresEnPagina() {
        return estaElementoPresente(By.cssSelector(".error, .alert-error, .error-message"));
    }
    
    /**
     * Obtiene una lista de todas las opciones de navegación disponibles.
     */
    public List<String> obtenerOpcionesNavegacion() {
        List<String> opciones = new ArrayList<>();
        
        if (estaElementoPresente(By.cssSelector("a[href*='logout'], .wp-block-button__link"))) {
            opciones.add("Logout");
        }
        
        return opciones;
    }
}
=======
/**
 * Representa la página principal que se muestra después de un login exitoso.
 */
public class PaginaPrincipal extends PaginaBase {

    @FindBy(css = "a[href*='logout'], .logout")
    private WebElement enlaceLogout;

    @FindBy(css = ".profile-name, #userName")
    private WebElement etiquetaNombre;

    public PaginaPrincipal(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean estaPaginaCargada() {
        return estaElementoVisible(By.cssSelector("a[href*='logout'], .logout"));
    }

    @Override
    public void esperarCargaPagina() {
        buscarElementoVisible(By.cssSelector("a[href*='logout'], .logout"));
    }

    @Override
    public String obtenerTituloPagina() {
        return "Principal";
    }

    /** Verifica si el usuario está logueado. */
    public boolean usuarioLogueado() {
        return estaElementoVisible(By.cssSelector("a[href*='logout'], .logout"));
    }

    /** Obtiene el nombre del usuario logueado. */
    public String obtenerNombreUsuarioLogueado() {
        if (etiquetaNombre != null && etiquetaNombre.isDisplayed()) {
            return etiquetaNombre.getText().trim();
        }
        return "";
    }

    /** Cierra la sesión si es posible. */
    public boolean cerrarSesion() {
        if (enlaceLogout != null) {
            enlaceLogout.click();
            esperarCargaPaginaCompleta();
            return true;
        }
        return false;
    }
}
>>>>>>> 6997292b2d22485ff45fed1f08040976dfcfd0b3
