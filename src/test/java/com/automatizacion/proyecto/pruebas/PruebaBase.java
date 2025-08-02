package com.automatizacion.proyecto.base;

import org.testng.annotations.*;
import org.openqa.selenium.WebDriver;
import com.automatizacion.proyecto.configuracion.ConfiguracionNavegador;
import com.automatizacion.proyecto.configuracion.ConfiguracionGlobal;
import com.automatizacion.proyecto.configuracion.ConfiguracionGlobal.TipoNavegador;
import com.automatizacion.proyecto.utilidades.GestorCapturaPantalla;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.qameta.allure.Step;

/**
 * Clase base para todas las pruebas de automatización.
 * Proporciona configuración común y utilidades para las pruebas.
 * 
 * Principios aplicados:
 * - Template Method: Define estructura común para pruebas
 * - DRY: Evita repetición de configuración en cada test
 * - SRP: Responsabilidad única de configurar entorno de pruebas
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
public abstract class PruebaBase {
    
    protected WebDriver driver;
    protected ConfiguracionGlobal config;
    private static final Logger logger = LoggerFactory.getLogger(PruebaBase.class);
    
    // Variables para manejo de configuración
    protected String urlBase;
    protected String urlLogin;
    protected String urlRegistro;
    protected TipoNavegador tipoNavegador;
    
    /**
     * Configuración que se ejecuta antes de cada método de prueba.
     */
    @BeforeMethod(alwaysRun = true)
    @Step("Configurar entorno de prueba")
    public void configurarPrueba() {
        try {
            logger.info("=== INICIANDO CONFIGURACIÓN DE PRUEBA ===");
            
            // Obtener configuración global
            config = ConfiguracionGlobal.getInstance();
            
            // Configurar URLs
            urlBase = config.getUrlBase();
            urlLogin = config.getUrlLogin();
            urlRegistro = config.getUrlRegistro();
            tipoNavegador = config.getTipoNavegador();
            
            logger.info("URLs configuradas - Base: {}, Login: {}, Registro: {}", urlBase, urlLogin, urlRegistro);
            
            // Crear driver
            driver = ConfiguracionNavegador.crearDriver(tipoNavegador);
            
            logger.info("Driver creado exitosamente para: {}", tipoNavegador);
            logger.info("=== CONFIGURACIÓN DE PRUEBA COMPLETADA ===");
            
        } catch (Exception e) {
            logger.error("Error en configuración de prueba: {}", e.getMessage(), e);
            throw new RuntimeException("Fallo en configuración de prueba", e);
        }
    }
    
    /**
     * Limpieza que se ejecuta después de cada método de prueba.
     */
    @AfterMethod(alwaysRun = true)
    @Step("Limpiar entorno de prueba")
    public void limpiarPrueba() {
        try {
            if (driver != null) {
                logger.info("Cerrando driver...");
                ConfiguracionNavegador.cerrarDriver(driver);
                driver = null;
                logger.info("Driver cerrado exitosamente");
            }
        } catch (Exception e) {
            logger.error("Error al cerrar driver: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Configuración que se ejecuta una vez al inicio de la clase.
     */
    @BeforeClass(alwaysRun = true)
    public void configurarClase() {
        logger.info("=== INICIANDO CLASE DE PRUEBA: {} ===", this.getClass().getSimpleName());
    }
    
    /**
     * Limpieza que se ejecuta una vez al final de la clase.
     */
    @AfterClass(alwaysRun = true)
    public void limpiarClase() {
        logger.info("=== FINALIZANDO CLASE DE PRUEBA: {} ===", this.getClass().getSimpleName());
    }
    
    // ===== MÉTODOS DE UTILIDAD PARA PRUEBAS =====
    
    /**
     * Captura pantalla con nombre específico.
     */
    @Step("Capturar pantalla: {nombreArchivo}")
    protected void capturarPantalla(String nombreArchivo) {
        try {
            if (driver != null) {
                GestorCapturaPantalla.capturarPantalla(driver, nombreArchivo);
                logger.debug("Captura realizada: {}", nombreArchivo);
            } else {
                logger.warn("No se puede capturar pantalla: driver es null");
            }
        } catch (Exception e) {
            logger.error("Error al capturar pantalla '{}': {}", nombreArchivo, e.getMessage());
        }
    }
    
    /**
     * Captura pantalla automática.
     */
    protected void capturarPantalla() {
        capturarPantalla("test_screenshot");
    }
    
    /**
     * Log de paso de prueba para seguimiento.
     */
    @Step("Paso de prueba: {mensaje}")
    protected void logPasoPrueba(String mensaje) {
        logger.info("🔄 PASO: {}", mensaje);
    }
    
    /**
     * Log de validación para verificaciones.
     */
    @Step("Validación: {mensaje}")
    protected void logValidacion(String mensaje) {
        logger.info("✅ VALIDACIÓN: {}", mensaje);
    }
    
    /**
     * Log de error para fallos en pruebas.
     */
    protected void logError(String mensaje, Exception e) {
        logger.error("❌ ERROR: {} - {}", mensaje, e.getMessage(), e);
    }
    
    /**
     * Navega a la página de login.
     */
    @Step("Navegar a página de login")
    protected void navegarALogin() {
        try {
            logPasoPrueba("Navegando a página de login");
            driver.get(urlLogin);
            Thread.sleep(2000); // Esperar carga de página
            logValidacion("Navegación a login completada");
        } catch (Exception e) {
            logError("Error al navegar a login", e);
            capturarPantalla("error_navegacion_login");
            throw new RuntimeException("No se pudo navegar a login", e);
        }
    }
    
    /**
     * Navega a la página de registro.
     */
    @Step("Navegar a página de registro")
    protected void navegarARegistro() {
        try {
            logPasoPrueba("Navegando a página de registro");
            driver.get(urlRegistro);
            Thread.sleep(2000); // Esperar carga de página
            logValidacion("Navegación a registro completada");
        } catch (Exception e) {
            logError("Error al navegar a registro", e);
            capturarPantalla("error_navegacion_registro");
            throw new RuntimeException("No se pudo navegar a registro", e);
        }
    }
    
    /**
     * Navega a la página base/home.
     */
    @Step("Navegar a página principal")
    protected void navegarAHome() {
        try {
            logPasoPrueba("Navegando a página principal");
            driver.get(urlBase);
            Thread.sleep(2000); // Esperar carga de página
            logValidacion("Navegación a home completada");
        } catch (Exception e) {
            logError("Error al navegar a home", e);
            capturarPantalla("error_navegacion_home");
            throw new RuntimeException("No se pudo navegar a home", e);
        }
    }
    
    /**
     * Espera genérica para sincronización.
     */
    protected void esperar(long milisegundos) {
        try {
            Thread.sleep(milisegundos);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Espera interrumpida: {}", e.getMessage());
        }
    }
    
    /**
     * Obtiene información del navegador para debugging.
     */
    protected String obtenerInfoNavegador() {
        if (driver != null) {
            return String.format("Navegador: %s | URL: %s | Título: %s", 
                tipoNavegador, 
                driver.getCurrentUrl(), 
                driver.getTitle());
        }
        return "Driver no disponible";
    }
    
    /**
     * Verifica si el driver está disponible y funcional.
     */
    protected boolean esDriverDisponible() {
        try {
            return driver != null && driver.getCurrentUrl() != null;
        } catch (Exception e) {
            logger.warn("Driver no disponible: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Método template para logging de inicio de prueba.
     */
    protected void iniciarPrueba(String nombrePrueba, String descripcion) {
        logger.info("🚀 INICIANDO PRUEBA: {}", nombrePrueba);
        logger.info("📝 DESCRIPCIÓN: {}", descripcion);
        logger.info("🌐 NAVEGADOR: {}", tipoNavegador);
        logger.info("📍 URL BASE: {}", urlBase);
    }
    
    /**
     * Método template para logging de finalización de prueba.
     */
    protected void finalizarPrueba(String nombrePrueba, boolean exitosa) {
        String estado = exitosa ? "✅ EXITOSA" : "❌ FALLIDA";
        logger.info("🏁 FINALIZANDO PRUEBA: {} - {}", nombrePrueba, estado);
        
        if (!exitosa) {
            capturarPantalla("prueba_fallida_" + nombrePrueba.replaceAll("\\s+", "_"));
        }
    }
}