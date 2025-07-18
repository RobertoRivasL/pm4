package com.robertorivas.automatizacion.pruebas;

import com.robertorivas.automatizacion.configuracion.ConfiguracionNavegador;
import com.robertorivas.automatizacion.configuracion.ConfiguracionPruebas;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
<<<<<<< HEAD
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase base para todas las pruebas automatizadas.
 * Proporciona configuración común, manejo de drivers y utilidades compartidas.
 * 
 * Principios aplicados:
 * - Template Method: Define estructura común para todas las pruebas
 * - Single Responsibility: Maneja solo configuración y utilidades base
 * - DRY: Evita duplicación de código entre clases de prueba
 * - Resource Management: Manejo adecuado de recursos como WebDriver
 * 
 * @author Roberto Rivas Lopez
 */
public abstract class PruebasBase {
    
    protected static final Logger logger = LoggerFactory.getLogger(PruebasBase.class);
    
    // Configuración y drivers
    protected ConfiguracionPruebas config;
    private WebDriver driver;
    private ThreadLocal<WebDriver> driverLocal = new ThreadLocal<>();
    
    // Configuración de reportes y evidencias
    private static final String RUTA_CAPTURAS = "reportes/capturas";
    private static final String RUTA_LOGS = "reportes/logs";
    private static final DateTimeFormatter FORMATO_TIMESTAMP = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    
    // Contadores y métricas
    private Map<String, Object> datosEjecucion;
    private long tiempoInicioSuite;
    private long tiempoInicioPrueba;
    
    // ===== CONFIGURACIÓN DE SUITE =====
    
    @BeforeSuite(alwaysRun = true)
    public void configuracionSuite() {
        tiempoInicioSuite = System.currentTimeMillis();
        
        logger.info("==========================================================");
        logger.info("INICIANDO SUITE DE AUTOMATIZACIÓN FUNCIONAL");
        logger.info("Desarrollado por: Roberto Rivas Lopez");
        logger.info("Curso: Automatización de Pruebas");
        logger.info("==========================================================");
        
        // Inicializar configuración
        config = ConfiguracionPruebas.obtenerInstancia();
        config.registrarConfiguracionesActuales();
        
        // Crear directorios necesarios
        crearDirectoriosEvidencias();
        
        // Inicializar datos de ejecución
        datosEjecucion = new HashMap<>();
        datosEjecucion.put("Suite", "Suite de Automatización Funcional");
        datosEjecucion.put("Autor", "Roberto Rivas Lopez");
        datosEjecucion.put("Inicio", LocalDateTime.now().format(FORMATO_TIMESTAMP));
        datosEjecucion.put("Navegador", config.obtenerNavegadorDefecto());
        datosEjecucion.put("Entorno", config.obtenerEntorno());
        
        logger.info("Configuración de suite completada");
    }
    
    @AfterSuite(alwaysRun = true)
    public void finalizacionSuite() {
        long tiempoEjecucion = System.currentTimeMillis() - tiempoInicioSuite;
        
        logger.info("==========================================================");
        logger.info("FINALIZANDO SUITE DE AUTOMATIZACIÓN FUNCIONAL");
        logger.info("Tiempo total de ejecución: {} ms ({} segundos)", tiempoEjecucion, tiempoEjecucion / 1000);
        logger.info("==========================================================");
        
        // Generar resumen final
        generarResumenEjecucion(tiempoEjecucion);
    }
    
    // ===== CONFIGURACIÓN DE CLASE =====
    
    @BeforeClass(alwaysRun = true)
    public void configuracionClase() {
        String nombreClase = this.getClass().getSimpleName();
        logger.info("Configurando clase de pruebas: {}", nombreClase);
        
        // Validar configuración antes de iniciar
        validarConfiguracion();
        
        logger.info("Configuración de clase {} completada", nombreClase);
    }
    
    @AfterClass(alwaysRun = true)
    public void finalizacionClase() {
        String nombreClase = this.getClass().getSimpleName();
        logger.info("Finalizando clase de pruebas: {}", nombreClase);
        
        // Cerrar driver si queda abierto a nivel de clase
        cerrarDriver();
        
        logger.info("Finalización de clase {} completada", nombreClase);
    }
    
    // ===== CONFIGURACIÓN DE MÉTODOS =====
    
    @BeforeMethod(alwaysRun = true)
    public void configuracionMetodo(ITestResult resultado) {
        tiempoInicioPrueba = System.currentTimeMillis();
        
        String nombreMetodo = resultado.getMethod().getMethodName();
        String descripcion = resultado.getMethod().getDescription();
        
        registrarSeparador("INICIANDO PRUEBA: " + nombreMetodo.toUpperCase());
        logger.info("Método: {}", nombreMetodo);
        if (descripcion != null && !descripcion.isEmpty()) {
            logger.info("Descripción: {}", descripcion);
        }
        
        // Inicializar driver para la prueba
        inicializarDriver();
        
        logger.info("Configuración de método completada para: {}", nombreMetodo);
    }
    
    @AfterMethod(alwaysRun = true)
    public void finalizacionMetodo(ITestResult resultado) {
        long tiempoEjecucion = System.currentTimeMillis() - tiempoInicioPrueba;
        
        String nombreMetodo = resultado.getMethod().getMethodName();
        String estado = obtenerEstadoPrueba(resultado);
        
        logger.info("Finalizando prueba: {}", nombreMetodo);
        logger.info("Estado: {}", estado);
        logger.info("Tiempo de ejecución: {} ms ({} segundos)", tiempoEjecucion, tiempoEjecucion / 1000);
        
        // Tomar captura si la prueba falló
        if (resultado.getStatus() == ITestResult.FAILURE) {
            tomarCapturaError(nombreMetodo, resultado.getThrowable());
        }
        
        // Cerrar driver después de cada prueba
        cerrarDriver();
        
        registrarSeparador("PRUEBA FINALIZADA: " + nombreMetodo.toUpperCase() + " - " + estado);
    }
    
    // ===== GESTIÓN DE WEBDRIVER =====
    
    /**
     * Inicializa el WebDriver para la prueba actual.
     */
    protected void inicializarDriver() {
        try {
            // Obtener navegador de parámetros TestNG o configuración
            String navegador = obtenerParametroNavegador();
            
            logger.info("Inicializando WebDriver para navegador: {}", navegador);
            
            // Crear driver usando la configuración
            ConfiguracionNavegador.TipoNavegador tipoNavegador = 
                ConfiguracionNavegador.TipoNavegador.obtenerPorNombre(navegador);
            
            driver = ConfiguracionNavegador.crearDriver(tipoNavegador);
            driverLocal.set(driver);
            
            logger.info("WebDriver inicializado exitosamente: {}", navegador);
            
            // Verificar que el driver funciona
            if (!ConfiguracionNavegador.verificarDriver(driver)) {
                throw new RuntimeException("El WebDriver no está funcional");
            }
            
        } catch (Exception e) {
            logger.error("Error inicializando WebDriver: {}", e.getMessage());
            throw new RuntimeException("No se pudo inicializar WebDriver", e);
        }
    }
    
    /**
     * Obtiene la instancia actual del WebDriver.
     */
    protected WebDriver obtenerDriver() {
        WebDriver driverActual = driverLocal.get();
        if (driverActual == null) {
            throw new IllegalStateException("WebDriver no ha sido inicializado");
        }
        return driverActual;
    }
    
    /**
     * Cierra el WebDriver actual.
     */
    protected void cerrarDriver() {
        WebDriver driverActual = driverLocal.get();
        if (driverActual != null) {
            try {
                logger.debug("Cerrando WebDriver");
                driverActual.quit();
            } catch (Exception e) {
                logger.warn("Error cerrando WebDriver: {}", e.getMessage());
            } finally {
                driverLocal.remove();
                driver = null;
            }
        }
    }
    
    // ===== UTILIDADES DE EVIDENCIAS =====
    
    /**
     * Toma una captura de pantalla con descripción.
     */
    protected void tomarCapturaPaso(String descripcion) {
        tomarCaptura(descripcion, false);
    }
    
    /**
     * Toma una captura de pantalla en caso de error.
     */
    protected void tomarCapturaError(String nombrePrueba, Throwable error) {
        String descripcion = "Error en " + nombrePrueba + ": " + 
                           (error != null ? error.getMessage() : "Error desconocido");
        tomarCaptura(descripcion, true);
    }
    
    /**
     * Toma una captura de pantalla.
     */
    private void tomarCaptura(String descripcion, boolean esError) {
        try {
            WebDriver driverActual = driverLocal.get();
            if (driverActual instanceof TakesScreenshot) {
                byte[] captura = ((TakesScreenshot) driverActual).getScreenshotAs(OutputType.BYTES);
                
                String timestamp = LocalDateTime.now().format(FORMATO_TIMESTAMP);
                String tipoCaptura = esError ? "error" : "paso";
                String nombreArchivo = String.format("captura_%s_%s_%s.png", 
                                                   this.getClass().getSimpleName(),
                                                   tipoCaptura,
                                                   timestamp);
                
                Path rutaCaptura = Paths.get(RUTA_CAPTURAS, nombreArchivo);
                Files.write(rutaCaptura, captura);
                
                logger.info("Captura guardada: {} - {}", nombreArchivo, descripcion);
            }
        } catch (Exception e) {
            logger.warn("Error tomando captura: {}", e.getMessage());
        }
    }
    
    // ===== UTILIDADES DE LOGGING =====
    
    /**
     * Registra un separador visual en los logs.
     */
    protected void registrarSeparador(String titulo) {
        String separador = "=".repeat(Math.max(60, titulo.length() + 4));
        logger.info(separador);
        logger.info("  " + titulo);
        logger.info(separador);
    }
    
    /**
     * Registra información general.
     */
    protected void registrarInfo(String mensaje) {
        logger.info(mensaje);
    }
    
    /**
     * Registra una advertencia.
     */
    protected void registrarAdvertencia(String mensaje) {
        logger.warn("⚠️ ADVERTENCIA: {}", mensaje);
    }
    
    /**
     * Registra un paso exitoso.
     */
    protected void registrarPasoExitoso(String mensaje) {
        logger.info("✅ ÉXITO: {}", mensaje);
    }
    
    /**
     * Registra un paso fallido.
     */
    protected void registrarPasoFallido(String mensaje, Throwable error) {
        logger.error("❌ FALLO: {}", mensaje);
        if (error != null) {
            logger.error("Detalle del error: {}", error.getMessage());
        }
    }
    
    /**
     * Registra datos de entrada de la prueba.
     */
    protected void registrarDatosEntrada(Map<String, Object> datos) {
        logger.info("📊 DATOS DE ENTRADA:");
        datos.forEach((clave, valor) -> logger.info("  {} = {}", clave, valor));
    }
    
    // ===== UTILIDADES DE CONFIGURACIÓN =====
    
    /**
     * Obtiene el parámetro de navegador de TestNG.
     */
    private String obtenerParametroNavegador() {
        String navegador = System.getProperty("navegador");
        if (navegador == null || navegador.isEmpty()) {
            navegador = config.obtenerNavegadorDefecto();
        }
        return navegador;
    }
    
    /**
     * Valida que la configuración esté correcta.
     */
    private void validarConfiguracion() {
        logger.debug("Validando configuración de pruebas");
        
        // Validar URLs
        String urlBase = config.obtenerUrlBase();
        if (urlBase == null || urlBase.isEmpty()) {
            throw new RuntimeException("URL base no configurada");
        }
        
        // Validar timeouts
        if (config.obtenerTimeoutExplicito().isZero() || config.obtenerTimeoutExplicito().isNegative()) {
            throw new RuntimeException("Timeout explícito no válido");
        }
        
        logger.debug("Configuración validada correctamente");
    }
    
    /**
     * Crea los directorios necesarios para evidencias.
     */
    private void crearDirectoriosEvidencias() {
        try {
            Files.createDirectories(Paths.get(RUTA_CAPTURAS));
            Files.createDirectories(Paths.get(RUTA_LOGS));
            logger.debug("Directorios de evidencias creados");
        } catch (IOException e) {
            logger.warn("Error creando directorios de evidencias: {}", e.getMessage());
        }
    }
    
    /**
     * Obtiene el estado de la prueba como string.
     */
    private String obtenerEstadoPrueba(ITestResult resultado) {
        switch (resultado.getStatus()) {
            case ITestResult.SUCCESS:
                return "EXITOSO";
            case ITestResult.FAILURE:
                return "FALLIDO";
            case ITestResult.SKIP:
                return "OMITIDO";
            default:
                return "DESCONOCIDO";
        }
    }
    
    /**
     * Genera un resumen de la ejecución.
     */
    private void generarResumenEjecucion(long tiempoTotal) {
        try {
            StringBuilder resumen = new StringBuilder();
            resumen.append("=== RESUMEN DE EJECUCIÓN ===\n");
            resumen.append("Suite: ").append(datosEjecucion.get("Suite")).append("\n");
            resumen.append("Autor: ").append(datosEjecucion.get("Autor")).append("\n");
            resumen.append("Inicio: ").append(datosEjecucion.get("Inicio")).append("\n");
            resumen.append("Fin: ").append(LocalDateTime.now().format(FORMATO_TIMESTAMP)).append("\n");
            resumen.append("Duración: ").append(tiempoTotal / 1000).append(" segundos\n");
            resumen.append("Navegador: ").append(datosEjecucion.get("Navegador")).append("\n");
            resumen.append("Entorno: ").append(datosEjecucion.get("Entorno")).append("\n");
            resumen.append("============================\n");
            
            // Guardar resumen
            String timestamp = LocalDateTime.now().format(FORMATO_TIMESTAMP);
            String nombreArchivo = "resumen_ejecucion_" + timestamp + ".txt";
            Path rutaResumen = Paths.get(RUTA_LOGS, nombreArchivo);
            Files.write(rutaResumen, resumen.toString().getBytes());
            
            logger.info("Resumen de ejecución guardado: {}", nombreArchivo);
            
        } catch (Exception e) {
            logger.warn("Error generando resumen de ejecución: {}", e.getMessage());
        }
    }
    
    // ===== MÉTODOS DE UTILIDAD ADICIONALES =====
    
    /**
     * Pausa la ejecución por un tiempo determinado.
     */
    protected void pausar(long milisegundos) {
        try {
            Thread.sleep(milisegundos);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Pausa interrumpida");
        }
    }
    
    /**
     * Obtiene información del navegador actual.
     */
    protected String obtenerInformacionNavegador() {
        try {
            WebDriver driverActual = obtenerDriver();
            return ConfiguracionNavegador.obtenerInformacionNavegador(driverActual);
        } catch (Exception e) {
            return "Información no disponible";
        }
    }
    
    /**
     * Verifica si estamos en un entorno de CI/CD.
     */
    protected boolean esEntornoCI() {
        return config.esEntornoCI() || 
               System.getenv("CI") != null || 
               System.getProperty("ci") != null;
    }
    
    /**
     * Obtiene la configuración actual.
     */
    protected ConfiguracionPruebas obtenerConfiguracion() {
        return config;
    }
}
=======
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.util.Map;

/**
 * Clase base para todas las pruebas.
 * Gestiona la creación del WebDriver y utilidades de logging.
 */
public abstract class PruebasBase {

    protected WebDriver driver;
    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected ConfiguracionPruebas config;

    @BeforeClass
    public void configuracionClase() {
        config = ConfiguracionPruebas.obtenerInstancia();
        ConfiguracionNavegador.TipoNavegador tipo =
                ConfiguracionNavegador.TipoNavegador.obtenerPorNombre(config.obtenerNavegadorDefecto());
        driver = ConfiguracionNavegador.crearDriver(tipo);
        logger.info("Driver inicializado para {}", tipo.getNombre());
    }

    @AfterClass(alwaysRun = true)
    public void cerrarDriver() {
        if (driver != null) {
            driver.quit();
            logger.info("Driver cerrado");
        }
    }

    protected WebDriver obtenerDriver() {
        return driver;
    }

    // ===== Utilidades de logging =====

    protected void registrarSeparador(String titulo) {
        logger.info("==== {} ====", titulo);
    }

    protected void registrarDatosEntrada(Map<String, Object> datos) {
        logger.info("Datos de entrada: {}", datos);
    }

    protected void registrarInfo(String mensaje) {
        logger.info(mensaje);
    }

    protected void registrarPasoExitoso(String mensaje) {
        logger.info("✔ {}", mensaje);
    }

    protected void registrarPasoFallido(String mensaje, Throwable t) {
        if (t != null) {
            logger.error("✘ {}", mensaje, t);
        } else {
            logger.error("✘ {}", mensaje);
        }
    }

    protected void registrarAdvertencia(String mensaje) {
        logger.warn(mensaje);
    }

    protected void tomarCapturaPaso(String descripcion) {
        if (driver instanceof TakesScreenshot ts) {
            ts.getScreenshotAs(OutputType.BYTES);
            logger.debug("Captura tomada: {}", descripcion);
        }
    }
}
>>>>>>> 6997292b2d22485ff45fed1f08040976dfcfd0b3
