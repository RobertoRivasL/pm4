package com.robertorivas.automatizacion.pruebas;

import com.robertorivas.automatizacion.configuracion.ConfiguracionNavegador;
import com.robertorivas.automatizacion.configuracion.ConfiguracionPruebas;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Clase base para todas las pruebas automatizadas.
 * Contiene configuración común, gestión del WebDriver y utilidades para pruebas.
 * 
 * Principios aplicados:
 * - Template Method Pattern: Define estructura común para todas las pruebas
 * - Single Responsibility: Solo maneja configuración base de pruebas
 * - DRY: Evita duplicación de configuración entre clases de pruebas
 * - Resource Management: Gestión segura de WebDriver
 * 
 * @author Roberto Rivas Lopez
 */
public abstract class PruebasBase {
    
    protected static final Logger logger = LoggerFactory.getLogger(PruebasBase.class);
    
    // Driver e infraestructura
    private WebDriver driver;
    protected ConfiguracionPruebas config;
    
    // Control de ejecución
    private String nombrePrueba;
    private LocalDateTime inicioEjecucion;
    
    // Rutas para evidencias
    private Path rutaCapturas;
    private Path rutaLogs;
    
    /**
     * Configuración a nivel de suite (ejecuta una vez por suite completa).
     */
    @BeforeSuite(alwaysRun = true)
    public void configuracionSuite() {
        logger.info("=".repeat(80));
        logger.info("INICIANDO SUITE DE AUTOMATIZACIÓN FUNCIONAL");
        logger.info("Desarrollado por: Roberto Rivas Lopez");
        logger.info("Tecnologías: Java 17 + Selenium 4 + TestNG + Maven");
        logger.info("=".repeat(80));
        
        // Inicializar configuración
        config = ConfiguracionPruebas.obtenerInstancia();
        config.registrarConfiguracionesActuales();
        
        // Crear directorios para evidencias
        crearDirectoriosEvidencias();
        
        logger.info("Configuración de suite completada exitosamente");
    }
    
    /**
     * Configuración a nivel de clase (ejecuta una vez por clase de pruebas).
     */
    @BeforeClass(alwaysRun = true)
    public void configuracionClase() {
        logger.info("Iniciando configuración para clase: {}", this.getClass().getSimpleName());
        
        // Reinicializar configuración por si hay cambios
        config = ConfiguracionPruebas.obtenerInstancia();
        
        logger.info("Configuración de clase completada: {}", this.getClass().getSimpleName());
    }
    
    /**
     * Configuración antes de cada método de prueba.
     */
    @BeforeMethod(alwaysRun = true)
    public void configuracionPrueba(ITestResult result) {
        // Capturar información de la prueba
        nombrePrueba = result.getMethod().getMethodName();
        inicioEjecucion = LocalDateTime.now();
        
        logger.info("\n" + "=".repeat(60));
        logger.info("INICIANDO PRUEBA: {}", nombrePrueba);
        logger.info("Clase: {}", result.getTestClass().getName());
        logger.info("Hora inicio: {}", inicioEjecucion.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        logger.info("=".repeat(60));
        
        // Crear nueva instancia del driver
        crearDriver();
        
        logger.info("WebDriver creado exitosamente para: {}", nombrePrueba);
    }
    
    /**
     * Limpieza después de cada método de prueba.
     */
    @AfterMethod(alwaysRun = true)
    public void limpiezaPrueba(ITestResult result) {
        LocalDateTime finEjecucion = LocalDateTime.now();
        
        // Tomar captura si la prueba falló
        if (!result.isSuccess()) {
            tomarCapturaFallo(result);
        }
        
        // Cerrar el driver de forma segura
        cerrarDriver();
        
        // Log de finalización
        logger.info("\n" + "-".repeat(60));
        logger.info("FINALIZANDO PRUEBA: {}", nombrePrueba);
        logger.info("Estado: {}", result.isSuccess() ? "✅ EXITOSA" : "❌ FALLIDA");
        logger.info("Hora fin: {}", finEjecucion.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        
        if (result.getThrowable() != null) {
            logger.error("Error: {}", result.getThrowable().getMessage());
        }
        
        logger.info("-".repeat(60) + "\n");
    }
    
    /**
     * Limpieza después de toda la clase.
     */
    @AfterClass(alwaysRun = true)
    public void limpiezaClase() {
        logger.info("Limpieza completada para clase: {}", this.getClass().getSimpleName());
    }
    
    /**
     * Limpieza después de toda la suite.
     */
    @AfterSuite(alwaysRun = true)
    public void limpiezaSuite() {
        logger.info("=".repeat(80));
        logger.info("FINALIZANDO SUITE DE AUTOMATIZACIÓN FUNCIONAL");
        logger.info("Evidencias generadas en: {}", config.obtenerRutaReportes());
        logger.info("Capturas guardadas en: {}", config.obtenerRutaCapturas());
        logger.info("Suite completada exitosamente");
        logger.info("=".repeat(80));
    }
    
    // ===== MÉTODOS DE GESTIÓN DEL DRIVER =====
    
    /**
     * Crea una nueva instancia del WebDriver.
     */
    private void crearDriver() {
        try {
            String navegador = obtenerNavegadorConfigurado();
            logger.info("Creando driver para navegador: {}", navegador);
            
            this.driver = ConfiguracionNavegador.crearDriver(navegador);
            
            if (this.driver == null) {
                throw new RuntimeException("No se pudo crear el WebDriver para: " + navegador);
            }
            
            logger.debug("Driver creado exitosamente: {}", this.driver.getClass().getSimpleName());
            
        } catch (Exception e) {
            logger.error("Error creando WebDriver: {}", e.getMessage());
            throw new RuntimeException("Fallo la creación del WebDriver", e);
        }
    }
    
    /**
     * Cierra el driver de forma segura.
     */
    private void cerrarDriver() {
        if (this.driver != null) {
            try {
                logger.debug("Cerrando WebDriver para prueba: {}", nombrePrueba);
                this.driver.quit();
                this.driver = null;
                logger.debug("WebDriver cerrado exitosamente");
            } catch (Exception e) {
                logger.warn("Error cerrando WebDriver: {}", e.getMessage());
            }
        }
    }
    
    /**
     * Obtiene la instancia actual del WebDriver.
     */
    protected WebDriver obtenerDriver() {
        if (this.driver == null) {
            throw new IllegalStateException("WebDriver no ha sido inicializado para la prueba: " + nombrePrueba);
        }
        return this.driver;
    }
    
    // ===== MÉTODOS DE CONFIGURACIÓN =====
    
    /**
     * Obtiene el navegador configurado para las pruebas.
     */
    private String obtenerNavegadorConfigurado() {
        // Prioridad: 1. Propiedad del sistema, 2. Configuración, 3. Por defecto
        String navegador = System.getProperty("navegador");
        if (navegador == null || navegador.isEmpty()) {
            navegador = config.obtenerNavegadorDefecto();
        }
        
        logger.debug("Navegador configurado: {}", navegador);
        return navegador;
    }
    
    /**
     * Crea los directorios necesarios para evidencias.
     */
    private void crearDirectoriosEvidencias() {
        try {
            rutaCapturas = config.obtenerRutaCapturas();
            rutaLogs = config.obtenerRutaLogs();
            
            Files.createDirectories(rutaCapturas);
            Files.createDirectories(rutaLogs);
            Files.createDirectories(config.obtenerRutaReportes());
            
            logger.info("Directorios de evidencias creados:");
            logger.info("- Capturas: {}", rutaCapturas.toAbsolutePath());
            logger.info("- Logs: {}", rutaLogs.toAbsolutePath());
            logger.info("- Reportes: {}", config.obtenerRutaReportes().toAbsolutePath());
            
        } catch (IOException e) {
            logger.error("Error creando directorios de evidencias: {}", e.getMessage());
            throw new RuntimeException("No se pudieron crear directorios de evidencias", e);
        }
    }
    
    // ===== MÉTODOS DE EVIDENCIAS =====
    
    /**
     * Toma una captura de pantalla en caso de fallo.
     */
    private void tomarCapturaFallo(ITestResult result) {
        try {
            if (this.driver instanceof TakesScreenshot) {
                byte[] captura = ((TakesScreenshot) this.driver).getScreenshotAs(OutputType.BYTES);
                
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
                String nombreArchivo = String.format("FALLO_%s_%s_%s.png", 
                    result.getTestClass().getName().substring(result.getTestClass().getName().lastIndexOf('.') + 1),
                    nombrePrueba, 
                    timestamp);
                
                Path archivoCaptura = rutaCapturas.resolve(nombreArchivo);
                Files.write(archivoCaptura, captura);
                
                logger.info("Captura de fallo guardada: {}", archivoCaptura.getFileName());
                
            } else {
                logger.warn("El driver no soporta capturas de pantalla");
            }
        } catch (Exception e) {
            logger.error("Error tomando captura de fallo: {}", e.getMessage());
        }
    }
    
    /**
     * Toma una captura de pantalla general.
     */
    protected void tomarCaptura() {
        tomarCapturaPaso("captura_general");
    }
    
    /**
     * Toma una captura de pantalla con descripción específica.
     */
    protected void tomarCapturaPaso(String descripcion) {
        try {
            if (this.driver instanceof TakesScreenshot) {
                byte[] captura = ((TakesScreenshot) this.driver).getScreenshotAs(OutputType.BYTES);
                
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
                String nombreArchivo = String.format("%s_%s_%s.png", nombrePrueba, descripcion, timestamp);
                
                Path archivoCaptura = rutaCapturas.resolve(nombreArchivo);
                Files.write(archivoCaptura, captura);
                
                logger.debug("Captura guardada: {}", nombreArchivo);
                
            }
        } catch (Exception e) {
            logger.warn("Error tomando captura '{}': {}", descripcion, e.getMessage());
        }
    }
    
    // ===== MÉTODOS DE LOGGING Y REPORTES =====
    
    /**
     * Registra un separador visual en los logs.
     */
    protected void registrarSeparador(String titulo) {
        logger.info("\n" + "=".repeat(80));
        logger.info(titulo);
        logger.info("=".repeat(80));
    }
    
    /**
     * Registra información general.
     */
    protected void registrarInfo(String mensaje) {
        logger.info(mensaje);
    }
    
    /**
     * Registra información de debug.
     */
    protected void registrarDebug(String mensaje) {
        logger.debug(mensaje);
    }
    
    /**
     * Registra advertencias.
     */
    protected void registrarAdvertencia(String mensaje) {
        logger.warn("⚠️ ADVERTENCIA: {}", mensaje);
    }
    
    /**
     * Registra un paso exitoso.
     */
    protected void registrarPasoExitoso(String mensaje) {
        logger.info("✅ {}", mensaje);
    }
    
    /**
     * Registra un paso fallido.
     */
    protected void registrarPasoFallido(String mensaje, Exception excepcion) {
        logger.error("❌ {}", mensaje);
        if (excepcion != null) {
            logger.error("Excepción: {}", excepcion.getMessage());
        }
    }
    
    /**
     * Registra datos de entrada para la prueba.
     */
    protected void registrarDatosEntrada(Map<String, Object> datos) {
        logger.info("--- DATOS DE ENTRADA ---");
        datos.forEach((clave, valor) -> logger.info("{}: {}", clave, valor));
        logger.info("-".repeat(25));
    }
    
    // ===== MÉTODOS DE UTILIDAD =====
    
    /**
     * Pausa la ejecución por un tiempo determinado.
     */
    protected void pausar(int milisegundos) {
        try {
            Thread.sleep(milisegundos);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Pausa interrumpida");
        }
    }
    
    /**
     * Obtiene el timestamp actual formateado.
     */
    protected String obtenerTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
    }
    
    /**
     * Obtiene información del entorno de ejecución.
     */
    protected String obtenerInformacionEntorno() {
        StringBuilder info = new StringBuilder();
        info.append("Sistema Operativo: ").append(System.getProperty("os.name")).append("\n");
        info.append("Versión Java: ").append(System.getProperty("java.version")).append("\n");
        info.append("Usuario: ").append(System.getProperty("user.name")).append("\n");
        info.append("Directorio de trabajo: ").append(System.getProperty("user.dir")).append("\n");
        info.append("Navegador configurado: ").append(config.obtenerNavegadorDefecto()).append("\n");
        info.append("URL base: ").append(config.obtenerUrlBase()).append("\n");
        
        return info.toString();
    }
    
    /**
     * Limpia recursos específicos de la prueba (para casos especiales).
     */
    protected void limpiarRecursosPrueba() {
        // Método que las clases hijas pueden sobrescribir para limpieza específica
        logger.debug("Limpieza de recursos específicos completada");
    }
    
    /**
     * Valida que los requisitos previos de la prueba estén cumplidos.
     */
    protected boolean validarRequisitosPrePrueba() {
        // Verificar que el driver está disponible
        if (this.driver == null) {
            logger.error("WebDriver no está inicializado");
            return false;
        }
        
        // Verificar que la configuración está cargada
        if (this.config == null) {
            logger.error("Configuración no está inicializada");
            return false;
        }
        
        return true;
    }
}