package com.automatizacion.proyecto.base;

import com.automatizacion.proyecto.configuracion.ConfiguracionGlobal;
import com.automatizacion.proyecto.configuracion.ConfiguracionNavegador;
import com.automatizacion.proyecto.enums.TipoMensaje;
import com.automatizacion.proyecto.enums.TipoNavegador;
import com.automatizacion.proyecto.utilidades.GestorCapturaPantalla;
import com.automatizacion.proyecto.utilidades.EsperaExplicita;
import com.automatizacion.proyecto.utilidades.ManejadorScrollPagina;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;

/**
 * Clase base para todas las pruebas automatizadas.
 * Proporciona configuración común, setup y teardown para las pruebas.
 * 
 * Implementa el patrón Template Method y sigue los principios SOLID
 * proporcionando una base sólida para todas las pruebas.
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
public abstract class BaseTest {
    
    private static final Logger logger = LoggerFactory.getLogger(BaseTest.class);
    
    // ThreadLocal para manejar ejecución paralela
    private static final ThreadLocal<WebDriver> driverLocal = new ThreadLocal<>();
    private static final ThreadLocal<GestorCapturaPantalla> gestorCapturaLocal = new ThreadLocal<>();
    private static final ThreadLocal<EsperaExplicita> esperaExplicitaLocal = new ThreadLocal<>();
    private static final ThreadLocal<ManejadorScrollPagina> manejadorScrollLocal = new ThreadLocal<>();
    
    protected ConfiguracionGlobal configuracion;
    
    /**
     * Configuración que se ejecuta una vez antes de todas las pruebas de la suite
     */
    @BeforeSuite(alwaysRun = true)
    public void configuracionSuite() {
        logger.info(TipoMensaje.CONFIGURACION.formatearMensaje(
            "=== INICIANDO SUITE DE AUTOMATIZACIÓN FUNCIONAL ==="));
        
        configuracion = ConfiguracionGlobal.obtenerInstancia();
        
        logger.info(TipoMensaje.INFORMATIVO.formatearMensaje(
            "URL Base: " + configuracion.obtenerUrlBase()));
        logger.info(TipoMensaje.INFORMATIVO.formatearMensaje(
            "Navegador: " + configuracion.obtenerTipoNavegador()));
        logger.info(TipoMensaje.INFORMATIVO.formatearMensaje(
            "Directorio capturas: " + configuracion.obtenerDirectorioCapturas()));
    }
    
    /**
     * Configuración que se ejecuta antes de cada clase de prueba
     */
    @BeforeClass(alwaysRun = true)
    public void configuracionClase() {
        String nombreClase = this.getClass().getSimpleName();
        logger.info(TipoMensaje.PRUEBA.formatearMensaje(
            "Iniciando clase de prueba: " + nombreClase));
    }
    
    /**
     * Configuración que se ejecuta antes de cada método de prueba
     * @param method método de prueba que se va a ejecutar
     */
    @BeforeMethod(alwaysRun = true)
    public void configuracionMetodo(Method method) {
        String nombreMetodo = method.getName();
        logger.info(TipoMensaje.PASO_PRUEBA.formatearMensaje(
            "Ejecutando prueba: " + nombreMetodo));
        
        try {
            // Inicializar WebDriver
            inicializarDriver();
            
            // Navegar a URL base
            String urlBase = configuracion.obtenerUrlBase();
            if (urlBase != null && !urlBase.isEmpty()) {
                obtenerDriver().get(urlBase);
                logger.info(TipoMensaje.INFORMATIVO.formatearMensaje(
                    "Navegando a: " + urlBase));
            }
            
            // Inicializar utilidades
            inicializarUtilidades();
            
        } catch (Exception e) {
            logger.error(TipoMensaje.CRITICO.formatearMensaje(
                "Error en configuración del método: " + e.getMessage()));
            throw new RuntimeException("Fallo en la configuración inicial de la prueba", e);
        }
    }
    
    /**
     * Limpieza que se ejecuta después de cada método de prueba
     * @param result resultado de la prueba ejecutada
     */
    @AfterMethod(alwaysRun = true)
    public void limpiezaMetodo(ITestResult result) {
        String nombreMetodo = result.getMethod().getMethodName();
        
        try {
            // Capturar pantalla en caso de fallo
            if (result.getStatus() == ITestResult.FAILURE) {
                logger.error(TipoMensaje.ERROR.formatearMensaje(
                    "Prueba FALLIDA: " + nombreMetodo));
                
                capturarPantallaError(nombreMetodo, result.getThrowable().getMessage());
            } else if (result.getStatus() == ITestResult.SUCCESS) {
                logger.info(TipoMensaje.EXITO.formatearMensaje(
                    "Prueba EXITOSA: " + nombreMetodo));
            } else if (result.getStatus() == ITestResult.SKIP) {
                logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                    "Prueba OMITIDA: " + nombreMetodo));
            }
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error al procesar resultado de prueba: " + e.getMessage()));
        } finally {
            // Cerrar driver
            cerrarDriver();
        }
    }
    
    /**
     * Limpieza que se ejecuta después de cada clase de prueba
     */
    @AfterClass(alwaysRun = true)
    public void limpiezaClase() {
        String nombreClase = this.getClass().getSimpleName();
        logger.info(TipoMensaje.INFORMATIVO.formatearMensaje(
            "Finalizando clase de prueba: " + nombreClase));
    }
    
    /**
     * Limpieza que se ejecuta una vez después de todas las pruebas de la suite
     */
    @AfterSuite(alwaysRun = true)
    public void limpiezaSuite() {
        logger.info(TipoMensaje.CONFIGURACION.formatearMensaje(
            "=== FINALIZANDO SUITE DE AUTOMATIZACIÓN FUNCIONAL ==="));
            
        // Limpiar capturas antiguas (opcional)
        try {
            GestorCapturaPantalla gestor = new GestorCapturaPantalla();
            int archivosEliminados = gestor.limpiarCapturasAntiguas(7); // 7 días
            if (archivosEliminados > 0) {
                logger.info(TipoMensaje.INFORMATIVO.formatearMensaje(
                    "Archivos de captura antiguos eliminados: " + archivosEliminados));
            }
        } catch (Exception e) {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "No se pudieron limpiar capturas antiguas: " + e.getMessage()));
        }
    }
    
    /**
     * Inicializa el WebDriver según la configuración
     */
    private void inicializarDriver() {
        try {
            String tipoNavegadorStr = configuracion.obtenerTipoNavegador();
            TipoNavegador tipoNavegador = TipoNavegador.desdeString(tipoNavegadorStr);
            boolean esHeadless = configuracion.esNavegadorHeadless();
            
            WebDriver driver = ConfiguracionNavegador.crearDriver(tipoNavegador, esHeadless);
            driverLocal.set(driver);
            
            logger.info(TipoMensaje.CONFIGURACION.formatearMensaje(
                "Driver inicializado: " + tipoNavegador + " (Headless: " + esHeadless + ")"));
                
        } catch (Exception e) {
            logger.error(TipoMensaje.CRITICO.formatearMensaje(
                "Error al inicializar driver: " + e.getMessage()));
            throw new RuntimeException("No se pudo inicializar el WebDriver", e);
        }
    }
    
    /**
     * Inicializa las utilidades necesarias para las pruebas
     */
    private void inicializarUtilidades() {
        WebDriver driver = obtenerDriver();
        
        // Inicializar gestor de capturas
        gestorCapturaLocal.set(new GestorCapturaPantalla());
        
        // Inicializar espera explícita
        esperaExplicitaLocal.set(new EsperaExplicita(driver, configuracion.obtenerTimeoutExplicito()));
        
        // Inicializar manejador de scroll
        manejadorScrollLocal.set(new ManejadorScrollPagina(driver));
        
        logger.debug(TipoMensaje.DEBUG.formatearMensaje("Utilidades inicializadas"));
    }
    
    /**
     * Cierra el WebDriver y limpia los ThreadLocal
     */
    private void cerrarDriver() {
        try {
            WebDriver driver = driverLocal.get();
            if (driver != null) {
                driver.quit();
                logger.debug(TipoMensaje.DEBUG.formatearMensaje("Driver cerrado"));
            }
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error al cerrar driver: " + e.getMessage()));
        } finally {
            // Limpiar ThreadLocal
            driverLocal.remove();
            gestorCapturaLocal.remove();
            esperaExplicitaLocal.remove();
            manejadorScrollLocal.remove();
        }
    }
    
    /**
     * Captura pantalla en caso de error
     * @param nombrePrueba nombre de la prueba que falló
     * @param mensajeError mensaje de error
     */
    private void capturarPantallaError(String nombrePrueba, String mensajeError) {
        try {
            GestorCapturaPantalla gestor = obtenerGestorCaptura();
            WebDriver driver = obtenerDriver();
            
            if (gestor != null && driver != null) {
                String rutaCaptura = gestor.capturarPantallaError(driver, nombrePrueba, mensajeError);
                if (rutaCaptura != null) {
                    logger.info(TipoMensaje.INFORMATIVO.formatearMensaje(
                        "Captura de error guardada: " + rutaCaptura));
                }
            }
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error al capturar pantalla de error: " + e.getMessage()));
        }
    }
    
    // === MÉTODOS PROTEGIDOS PARA USO EN CLASES DERIVADAS ===
    
    /**
     * Obtiene la instancia actual del WebDriver
     * @return WebDriver actual
     */
    protected WebDriver obtenerDriver() {
        WebDriver driver = driverLocal.get();
        if (driver == null) {
            throw new IllegalStateException("Driver no inicializado. Verifique la configuración del test.");
        }
        return driver;
    }
    
    /**
     * Obtiene el gestor de capturas actual
     * @return GestorCapturaPantalla actual
     */
    protected GestorCapturaPantalla obtenerGestorCaptura() {
        return gestorCapturaLocal.get();
    }
    
    /**
     * Obtiene la instancia de espera explícita
     * @return EsperaExplicita actual
     */
    protected EsperaExplicita obtenerEsperaExplicita() {
        return esperaExplicitaLocal.get();
    }
    
    /**
     * Obtiene el manejador de scroll
     * @return ManejadorScrollPagina actual
     */
    protected ManejadorScrollPagina obtenerManejadorScroll() {
        return manejadorScrollLocal.get();
    }
    
    /**
     * Método de conveniencia para capturar pantalla en las pruebas
     * @param descripcion descripción de la captura
     * @return ruta del archivo de captura
     */
    protected String capturarPantalla(String descripcion) {
        GestorCapturaPantalla gestor = obtenerGestorCaptura();
        if (gestor != null) {
            return gestor.capturarPantalla(obtenerDriver(), descripcion);
        }
        return null;
    }
    
    /**
     * Método de conveniencia para logging de pasos de prueba
     * @param mensaje mensaje del paso
     */
    protected void logPasoPrueba(String mensaje) {
        logger.info(TipoMensaje.PASO_PRUEBA.formatearMensaje(mensaje));
    }
    
    /**
     * Método de conveniencia para logging de validaciones
     * @param mensaje mensaje de validación
     */
    protected void logValidacion(String mensaje) {
        logger.info(TipoMensaje.VALIDACION.formatearMensaje(mensaje));
    }
}