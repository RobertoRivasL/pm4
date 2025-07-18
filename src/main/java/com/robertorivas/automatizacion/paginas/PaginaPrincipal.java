package com.robertorivas.automatizacion.paginas;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object para la página principal/segura de ExpandTesting.
 * Corresponde a la página /secure que se muestra después del login exitoso.
 * 
 * URL: https://practice.expandtesting.com/secure
 * 
 * Características de la página:
 * - Mensaje: "You logged into a secure area!"
 * - Botón de Logout
 * - Área segura protegida por autenticación
 * 
 * @author Roberto Rivas Lopez
 */
public class PaginaPrincipal extends PaginaBase {
    
    // ===== LOCALIZADORES ESPECÍFICOS PARA EXPANDTESTING =====
    
    // Elementos principales de la página segura
    @FindBy(css = "h2, .secure-area-text")
    private WebElement tituloAreaSegura;
    
    @FindBy(css = "#flash, .alert-success, .success")
    private WebElement mensajeBienvenida;
    
    @FindBy(css = "a[href='/logout'], .btn-secondary")
    private WebElement botonLogout;
    
    @FindBy(css = ".secure-area, .content, .container")
    private WebElement areaSegura;
    
    // Información del usuario (si está disponible)
    @FindBy(css = ".user-info, .username, .user-name")
    private WebElement informacionUsuario;
    
    // Localizadores dinámicos
    private static final By MENSAJE_FLASH = By.cssSelector("#flash");
    private static final By BOTON_LOGOUT_GENERAL = By.cssSelector("a[href='/logout'], .btn-secondary, a[href*='logout']");
    private static final By TITULO_SECURE = By.cssSelector("h1, h2, h3");
    
    /**
     * Constructor de la página principal/segura.
     */
    public PaginaPrincipal(WebDriver driver) {
        super(driver);
        logger.info("Inicializando página principal/segura de ExpandTesting");
    }
    
    // ===== MÉTODOS ABSTRACTOS IMPLEMENTADOS =====
    
    @Override
    public boolean estaPaginaCargada() {
        try {
            String urlActual = obtenerUrlActual();
            boolean enPaginaSegura = urlActual.contains("/secure");
            boolean tieneMensajeBienvenida = estaElementoVisible(MENSAJE_FLASH);
            boolean tieneBotonLogout = estaElementoVisible(BOTON_LOGOUT_GENERAL);
            
            return enPaginaSegura && (tieneMensajeBienvenida || tieneBotonLogout);
        } catch (Exception e) {
            logger.debug("Error verificando si la página principal está cargada: {}", e.getMessage());
            return false;
        }
    }
    
    @Override
    public void esperarCargaPagina() {
        logger.debug("Esperando carga completa de la página principal de ExpandTesting");
        
        // Esperar que estemos en la URL correcta
        espera.until(driver -> obtenerUrlActual().contains("/secure"));
        
        // Esperar elementos principales
        try {
            buscarElementoVisible(MENSAJE_FLASH, TIMEOUT_MEDIO);
        } catch (Exception e) {
            logger.debug("Mensaje flash no encontrado, verificando otros elementos");
        }
        
        try {
            buscarElementoVisible(BOTON_LOGOUT_GENERAL, TIMEOUT_MEDIO);
        } catch (Exception e) {
            logger.debug("Botón logout no encontrado, página podría tener estructura diferente");
        }
        
        logger.info("Página principal de ExpandTesting cargada");
    }
    
    @Override
    public String obtenerTituloPagina() {
        return "Secure Area - ExpandTesting";
    }
    
    // ===== MÉTODOS DE VERIFICACIÓN DE ESTADO =====
    
    /**
     * Verifica si el usuario está logueado correctamente.
     */
    public boolean usuarioLogueado() {
        try {
            String urlActual = obtenerUrlActual();
            
            // Verificar URL de página segura
            if (!urlActual.contains("/secure")) {
                logger.debug("No estamos en página segura. URL actual: {}", urlActual);
                return false;
            }
            
            // Verificar presencia de elementos que indican login exitoso
            boolean tieneMensajeBienvenida = estaElementoVisible(MENSAJE_FLASH);
            boolean tieneBotonLogout = estaElementoVisible(BOTON_LOGOUT_GENERAL);
            boolean tieneContenidoSeguro = estaElementoVisible(TITULO_SECURE);
            
            return tieneMensajeBienvenida || tieneBotonLogout || tieneContenidoSeguro;
            
        } catch (Exception e) {
            logger.error("Error verificando si usuario está logueado: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Verifica si la sesión está activa.
     */
    public boolean sesionActiva() {
        return usuarioLogueado();
    }
    
    // ===== MÉTODOS DE OBTENCIÓN DE INFORMACIÓN =====
    
    /**
     * Obtiene el mensaje de bienvenida.
     */
    public String obtenerMensajeBienvenida() {
        try {
            if (estaElementoVisible(MENSAJE_FLASH)) {
                String mensaje = obtenerTexto(MENSAJE_FLASH);
                if (mensaje.contains("You logged into a secure area!")) {
                    return mensaje;
                }
            }
            return null;
        } catch (Exception e) {
            logger.debug("Error obteniendo mensaje de bienvenida: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * Obtiene el nombre del usuario logueado.
     */
    public String obtenerNombreUsuarioLogueado() {
        try {
            // ExpandTesting no muestra el nombre específico del usuario
            // Pero podemos devolver información general
            if (usuarioLogueado()) {
                return "Usuario Logueado"; // Valor genérico para ExpandTesting
            }
            return null;
        } catch (Exception e) {
            logger.debug("Error obteniendo nombre de usuario: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * Obtiene el título de la página segura.
     */
    public String obtenerTituloAreaSegura() {
        try {
            if (estaElementoVisible(TITULO_SECURE)) {
                return obtenerTexto(TITULO_SECURE);
            }
            return obtenerTituloActual(); // Fallback al título de la página
        } catch (Exception e) {
            logger.debug("Error obteniendo título del área segura: {}", e.getMessage());
            return null;
        }
    }
    
    // ===== MÉTODOS DE ACCIONES =====
    
    /**
     * Cierra la sesión del usuario.
     */
    public boolean cerrarSesion() {
        logger.info("Iniciando proceso de logout");
        
        try {
            if (!usuarioLogueado()) {
                logger.warn("El usuario no parece estar logueado");
                return false;
            }
            
            // Buscar y hacer clic en el botón de logout
            if (estaElementoVisible(BOTON_LOGOUT_GENERAL)) {
                hacerClic(BOTON_LOGOUT_GENERAL);
                tomarCaptura(); // Captura después del clic
                
                // Esperar redirección
                esperarRedireccionLogout();
                
                // Verificar que el logout fue exitoso
                boolean logoutExitoso = verificarLogoutExitoso();
                
                if (logoutExitoso) {
                    logger.info("Logout completado exitosamente");
                } else {
                    logger.warn("El logout podría no haber sido completamente exitoso");
                }
                
                return logoutExitoso;
                
            } else {
                logger.error("Botón de logout no encontrado");
                return false;
            }
            
        } catch (Exception e) {
            logger.error("Error durante el proceso de logout: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Realiza logout (alias para compatibilidad).
     */
    public boolean logout() {
        return cerrarSesion();
    }
    
    // ===== MÉTODOS DE NAVEGACIÓN =====
    
    /**
     * Navega directamente a la página principal/segura.
     */
    public PaginaPrincipal navegarAPaginaPrincipal() {
        String urlPrincipal = config.obtenerUrlBase() + "/secure";
        navegarA(urlPrincipal);
        esperarCargaPagina();
        return this;
    }
    
    /**
     * Refresca la página actual.
     */
    public PaginaPrincipal refrescarPaginaPrincipal() {
        super.refrescarPagina();
        esperarCargaPagina();
        return this;
    }
    
    // ===== MÉTODOS DE VERIFICACIÓN PRIVADOS =====
    
    /**
     * Espera la redirección después del logout.
     */
    private void esperarRedireccionLogout() {
        try {
            // Esperar que la URL cambie (salir de /secure)
            espera.until(driver -> {
                String urlActual = driver.getCurrentUrl();
                return !urlActual.contains("/secure") || 
                       urlActual.contains("/login") || 
                       urlActual.equals(config.obtenerUrlBase() + "/");
            });
            
            logger.debug("Redirección después de logout detectada");
            
        } catch (Exception e) {
            logger.debug("Timeout esperando redirección de logout");
        }
    }
    
    /**
     * Verifica que el logout fue exitoso.
     */
    private boolean verificarLogoutExitoso() {
        try {
            String urlActual = obtenerUrlActual();
            
            // Verificar que ya no estamos en página segura
            boolean fueraDeAreaSegura = !urlActual.contains("/secure");
            
            // Verificar mensaje de logout exitoso si existe
            boolean mensajeLogout = false;
            if (estaElementoVisible(MENSAJE_FLASH)) {
                String mensaje = obtenerTexto(MENSAJE_FLASH);
                mensajeLogout = mensaje.contains("You logged out of the secure area!") ||
                               mensaje.contains("logged out") ||
                               mensaje.contains("logout");
            }
            
            // Verificar que estamos en página de login o home
            boolean enPaginaCorrecta = urlActual.contains("/login") || 
                                      urlActual.equals(config.obtenerUrlBase() + "/") ||
                                      urlActual.equals(config.obtenerUrlBase());
            
            return fueraDeAreaSegura && (mensajeLogout || enPaginaCorrecta);
            
        } catch (Exception e) {
            logger.debug("Error verificando logout exitoso: {}", e.getMessage());
            return false;
        }
    }
    
    // ===== MÉTODOS DE UTILIDAD =====
    
    /**
     * Verifica si hay mensajes de alerta o notificaciones.
     */
    public boolean hayMensajes() {
        return estaElementoVisible(MENSAJE_FLASH);
    }
    
    /**
     * Obtiene todos los mensajes visibles en la página.
     */
    public String obtenerMensajesActuales() {
        try {
            if (estaElementoVisible(MENSAJE_FLASH)) {
                return obtenerTexto(MENSAJE_FLASH);
            }
            return "";
        } catch (Exception e) {
            logger.debug("Error obteniendo mensajes actuales: {}", e.getMessage());
            return "";
        }
    }
    
    /**
     * Verifica si el botón de logout está disponible.
     */
    public boolean botonLogoutDisponible() {
        return estaElementoVisible(BOTON_LOGOUT_GENERAL);
    }
    
    /**
     * Obtiene información completa del estado de la página.
     */
    public String obtenerEstadoPagina() {
        StringBuilder estado = new StringBuilder();
        
        estado.append("URL: ").append(obtenerUrlActual()).append("\n");
        estado.append("Usuario logueado: ").append(usuarioLogueado()).append("\n");
        estado.append("Botón logout disponible: ").append(botonLogoutDisponible()).append("\n");
        estado.append("Hay mensajes: ").append(hayMensajes()).append("\n");
        
        String mensajeBienvenida = obtenerMensajeBienvenida();
        if (mensajeBienvenida != null) {
            estado.append("Mensaje bienvenida: ").append(mensajeBienvenida).append("\n");
        }
        
        String nombreUsuario = obtenerNombreUsuarioLogueado();
        if (nombreUsuario != null) {
            estado.append("Usuario: ").append(nombreUsuario).append("\n");
        }
        
        return estado.toString();
    }
    
    /**
     * Registra información de debug de la página actual.
     */
    public void logearInformacionPagina() {
        logger.debug("=== INFORMACIÓN DE PÁGINA PRINCIPAL ===");
        logger.debug("URL actual: {}", obtenerUrlActual());
        logger.debug("Página cargada: {}", estaPaginaCargada());
        logger.debug("Usuario logueado: {}", usuarioLogueado());
        logger.debug("Botón logout disponible: {}", botonLogoutDisponible());
        logger.debug("Hay mensajes: {}", hayMensajes());
        
        String mensajeBienvenida = obtenerMensajeBienvenida();
        if (mensajeBienvenida != null) {
            logger.debug("Mensaje bienvenida: {}", mensajeBienvenida);
        }
        
        logger.debug("=====================================");
    }
}