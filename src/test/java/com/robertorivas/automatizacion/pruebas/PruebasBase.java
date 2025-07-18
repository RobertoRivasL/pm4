package com.robertorivas.automatizacion.pruebas;

import com.robertorivas.automatizacion.configuracion.ConfiguracionNavegador;
import com.robertorivas.automatizacion.configuracion.ConfiguracionPruebas;
import com.robertorivas.automatizacion.utilidades.GestorEvidencias;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

import java.util.Map;

/**
 * Clase base para todas las pruebas automatizadas.
 * Contiene la configuración común y métodos de utilidad.
 * 
 * Principios aplicados:
 * - Template Method: Estructura base para todas las pruebas
 * - Single Responsibility: Solo maneja configuración común
 * - DRY: Evita duplicación de código entre clases de prueba
 * 
 * @author Roberto Rivas Lopez
 */
public abstract class PruebasBase {
    
    protected static final Logger logger = LoggerFactory.getLogger(PruebasBase.class);
    
    // Configuración y driver
    protected static ConfiguracionPruebas config;
    private static WebDriver driver;
    
    // Variables de control
    private static String navegadorActual;
    private static boolean driverInicializado = false;
    
    @BeforeSuite
    public void configuracionSuite() {
        logger.info("=== INICIANDO SUITE DE AUTOMATIZACIÓN ===");
        config = ConfiguracionPruebas.obtenerInstancia();
        config.registrarConfiguracionesActuales();
        
        // Crear directorios de evidencias
        GestorEvidencias.crearDirectoriosEvidencias();
        
        logger.info("Suite configurada exitosamente");
    }
    
    @BeforeClass
    public void configuracionClase() {
        logger.info("Configurando clase de pruebas: {}", this.getClass().getSimpleName());
    }
    
    @BeforeMethod
    @Parameters({"navegador"})
    public void configuracionPrueba(@Optional("chrome") String navegador) {
        logger.info("=== INICIANDO PRUEBA ===");
        logger.info("Navegador solicitado: {}", navegador);
        
        try {
            // Crear driver si no existe o si cambió el navegador
            if (driver == null || !navegador.equals(navegadorActual)) {
                if (driver != null) {
                    cerrarDriver();
                }
                
                driver = ConfiguracionNavegador.crearDriver(navegador);
                navegadorActual = navegador;
                driverInicializado = true;
                
                logger.info("Driver creado exitosamente: {}", navegador);
            }
            
        } catch (Exception e) {
            logger.error("Error configurando driver: {}", e.getMessage());
            throw new RuntimeException("No se pudo inicializar el driver", e);
        }
    }
    
    @AfterMethod
    public void limpiezaPrueba() {
        if (driver != null) {
            try {
                // Opcional: limpiar cookies y storage
                driver.manage().deleteAllCookies();
            } catch (Exception e) {
                logger.warn("Error limpiando datos de navegador: {}", e.getMessage());
            }
        }
        
        logger.info("=== PRUEBA FINALIZADA ===");
    }
    
    @AfterClass
    public void limpiezaClase() {
        logger.info("Finalizando clase de pruebas: {}", this.getClass().getSimpleName());
    }
    
    @AfterSuite
    public void limpiezaSuite() {
        cerrarDriver();
        logger.info("=== SUITE FINALIZADA ===");
    }
    
    /**
     * Obtiene el driver actual.
     */
    protected WebDriver obtenerDriver() {
        if (driver == null || !driverInicializado) {
            throw new IllegalStateException("Driver no inicializado. Verifica la configuración de TestNG.");
        }
        return driver;
    }
    
    /**
     * Cierra el driver actual.
     */
    private void cerrarDriver() {
        if (driver != null) {
            try {
                driver.quit();
                logger.info("Driver cerrado exitosamente");
            } catch (Exception e) {
                logger.warn("Error cerrando driver: {}", e.getMessage());
            } finally {
                driver = null;
                driverInicializado = false;
                navegadorActual = null;
            }
        }
    }
    
    /**
     * Toma una captura de pantalla para evidencias.
     */
    protected String tomarCapturaPaso(String descripcion) {
        if (driver != null) {
            String nombrePrueba = obtenerNombrePruebaActual();
            return GestorEvidencias.tomarCaptura(driver, nombrePrueba, descripcion);
        }
        return null;
    }
    
    /**
     * Toma una captura de pantalla simple.
     */
    protected String tomarCaptura() {
        return tomarCapturaPaso("captura");
    }
    
    /**
     * Obtiene el nombre de la prueba actual.
     */
    private String obtenerNombrePruebaActual() {
        // Intentar obtener el nombre del método actual
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stack) {
            if (element.getClassName().contains("Pruebas") && 
                !element.getClassName().contains("PruebasBase")) {
                return element.getClassName().substring(element.getClassName().lastIndexOf('.') + 1) 
                       + "_" + element.getMethodName();
            }
        }
        return this.getClass().getSimpleName();
    }
    
    /**
     * Registra información de debug.
     */
    protected void registrarInfo(String mensaje) {
        logger.info("📋 {}", mensaje);
    }
    
    /**
     * Registra advertencia.
     */
    protected void registrarAdvertencia(String mensaje) {
        logger.warn("⚠️ {}", mensaje);
    }
    
    /**
     * Registra error.
     */
    protected void registrarError(String mensaje, Throwable excepcion) {
        logger.error("❌ {}", mensaje, excepcion);
    }
    
    /**
     * Registra paso exitoso.
     */
    protected void registrarPasoExitoso(String mensaje) {
        logger.info("✅ {}", mensaje);
    }
    
    /**
     * Registra paso fallido.
     */
    protected void registrarPasoFallido(String mensaje, Throwable excepcion) {
        logger.error("❌ {}", mensaje);
        if (excepcion != null) {
            logger.error("Detalle del error:", excepcion);
        }
        tomarCapturaPaso("fallo_" + mensaje.replaceAll("[^a-zA-Z0-9]", "_"));
    }
    
    /**
     * Registra separador visual en los logs.
     */
    protected void registrarSeparador(String titulo) {
        String separador = "=".repeat(titulo.length() + 10);
        logger.info(separador);
        logger.info("     {}", titulo);
        logger.info(separador);
    }
    
    /**
     * Registra datos de entrada de la prueba.
     */
    protected void registrarDatosEntrada(Map<String, Object> datos) {
        logger.info("📥 DATOS DE ENTRADA:");
        datos.forEach((clave, valor) -> logger.info("   • {}: {}", clave, valor));
    }
    
    /**
     * Pausa la ejecución por un tiempo determinado.
     */
    protected void pausar(int segundos) {
        try {
            Thread.sleep(segundos * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Pausa interrumpida");
        }
    }
    
    /**
     * Verifica si estamos en modo debug.
     */
    protected boolean esModoDebug() {
        return "true".equalsIgnoreCase(System.getProperty("debug.activar", "false"));
    }
    
    /**
     * Pausa para debug si está habilitado.
     */
    protected void pausaDebug(String razon) {
        if (esModoDebug()) {
            logger.info("🔍 PAUSA DEBUG: {}", razon);
            logger.info("Presiona ENTER para continuar...");
            try {
                System.in.read();
            } catch (Exception e) {
                // Ignorar error de entrada
            }
        }
    }
}