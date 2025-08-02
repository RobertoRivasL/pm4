package com.automatizacion.proyecto.base;

import org.testng.annotations.*;
import org.openqa.selenium.WebDriver;
import com.automatizacion.proyecto.configuracion.ConfiguracionNavegador;
import com.automatizacion.proyecto.configuracion.ConfiguracionGlobal;
import com.automatizacion.proyecto.enums.TipoNavegador;
import com.automatizacion.proyecto.utilidades.GestorCapturaPantalla;
import com.automatizacion.proyecto.utilidades.EsperaExplicita;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.qameta.allure.Step;

/**
 * Clase base corregida para todas las pruebas de automatización.
 * Corrige todos los errores de nombres de métodos y configuración.
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
public abstract class BaseTest {
    
    protected WebDriver driver;
    protected ConfiguracionGlobal config;
    protected EsperaExplicita espera;
    private static final Logger logger = LoggerFactory.getLogger(BaseTest.class);
    
    // Variables para manejo de configuración
    protected String urlBase;
    protected String urlLogin;
    protected String urlRegistro;
    protected TipoNavegador tipoNavegador;
    
    /**
     * Configuración que se ejecuta antes de cada método de prueba.
     * CORREGIDO: Todos los nombres de métodos actualizados.
     */
    @BeforeMethod(alwaysRun = true)
    @Step("Configurar entorno de prueba")
    public void configurarPrueba() {
        try {
            logger.info("=== INICIANDO CONFIGURACIÓN DE PRUEBA ===");
            
            // ✅ CORRECCIÓN 1: getInstance() en lugar de obtenerInstancia()
            config = ConfiguracionGlobal.getInstance();
            
            // ✅ CORRECCIÓN 2: Usar métodos getter correctos
            urlBase = config.getUrlBase();
            urlLogin = config.getUrlLogin();
            urlRegistro = config.getUrlRegistro();
            tipoNavegador = config.getTipoNavegador();
            
            logger.info("URLs configuradas - Base: {}, Login: {}, Registro: {}", urlBase, urlLogin, urlRegistro);
            
            // ✅ CORRECCIÓN 3: ConfiguracionNavegador.crearDriver() con clase completa
            driver = ConfiguracionNavegador.crearDriver(tipoNavegador);
            
            // ✅ CORRECCIÓN 4: Inicializar EsperaExplicita con timeout correcto
            espera = new EsperaExplicita(driver, config.getTimeoutExplicito());
            
            logger.info("Driver creado exitosamente para: {}", tipoNavegador);
            logger.info("EsperaExplicita configurada con timeout: {} segundos", config.getTimeoutExplicito());
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
    
    // ===== MÉTODOS DE UTILIDAD CORREGIDOS =====
    
    /**
     * ✅ CORRECCIÓN 5: Método capturarPantalla NO estático.
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
     * ✅ NAVEGACIÓN CORREGIDA: Navega a la página de login.
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
     * ✅ NAVEGACIÓN CORREGIDA: Navega a la página de registro.
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
     * ✅ NAVEGACIÓN CORREGIDA: Navega a la página base/home.
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
            try {
                return String.format("Navegador: %s | URL: %s | Título: %s", 
                    tipoNavegador, 
                    driver.getCurrentUrl(), 
                    driver.getTitle());
            } catch (Exception e) {
                return "Navegador: " + tipoNavegador + " | Error obteniendo info: " + e.getMessage();
            }
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
        
        // Log adicional de configuración
        logger.debug("Configuración actual: {}", obtenerInfoNavegador());
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
    
    /**
     * Valida el estado del entorno antes de ejecutar pruebas.
     */
    protected void validarEntornoPrueba() {
        if (driver == null) {
            throw new IllegalStateException("WebDriver no está inicializado");
        }
        
        if (config == null) {
            throw new IllegalStateException("ConfiguracionGlobal no está inicializada");
        }
        
        if (espera == null) {
            throw new IllegalStateException("EsperaExplicita no está inicializada");
        }
        
        logger.debug("Entorno de prueba validado correctamente");
    }
    
    /**
     * Obtiene configuración actual formateada para logs.
     */
    protected String obtenerConfiguracionActual() {
        if (config == null) {
            return "Configuración no disponible";
        }
        
        return String.format(
            "Configuración: Navegador=%s, Timeout=%ds, URLs=[Base=%s, Login=%s, Registro=%s]",
            tipoNavegador,
            config.getTimeoutExplicito(),
            urlBase,
            urlLogin,
            urlRegistro
        );
    }
}