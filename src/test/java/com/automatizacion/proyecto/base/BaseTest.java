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
import io.qameta.allure.Allure;

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
    
    // ThreadLocal para manejar ejecución paralela de forma segura
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
        logger.info(TipoMensaje.CONFIGURACION.crearSeparador("INICIANDO SUITE DE AUTOMATIZACIÓN FUNCIONAL"));
        
        configuracion = ConfiguracionGlobal.obtenerInstancia();
        
        logger.info(TipoMensaje.INFORMATIVO.formatearMensaje("URL Base: " + configuracion.obtenerUrlBase()));
        logger.info(TipoMensaje.INFORMATIVO.formatearMensaje("Navegador: " + configuracion.obtenerTipoNavegador()));
        logger.info(TipoMensaje.INFORMATIVO.formatearMensaje("Directorio capturas: " + configuracion.obtenerDirectorioCapturas()));
        
        // Inicializar directorios necesarios
        inicializarDirectorios();
    }
    
    /**
     * Configuración que se ejecuta antes de cada clase de prueba
     */
    @BeforeClass(alwaysRun = true)
    public void configuracionClase() {
        String nombreClase = this.getClass().getSimpleName();
        logger.info(TipoMensaje.PRUEBA.formatearMensaje("Iniciando clase de prueba: " + nombreClase));
    }
    
    /**
     * Configuración que se ejecuta antes de cada método de prueba
     * @param method método de prueba que se va a ejecutar
     */
    @BeforeMethod(alwaysRun = true)
    public void configuracionMetodo(Method method) {
        String nombrePrueba = method.getName();
        logger.info(TipoMensaje.PASO_PRUEBA.formatearMensaje("=== INICIANDO PRUEBA: " + nombrePrueba + " ==="));
        
        try {
            // Crear driver
            WebDriver driver = ConfiguracionNavegador.crearNavegador();
            driverLocal.set(driver);
            
            // Inicializar utilidades
            inicializarUtilidades(driver);
            
            // Navegar a URL base
            driver.get(configuracion.obtenerUrlBase());
            
            logger.info(TipoMensaje.EXITO.formatearMensaje("Prueba " + nombrePrueba + " iniciada correctamente"));
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error en configuración de prueba: " + e.getMessage()));
            throw new RuntimeException("Fallo en configuración de prueba", e);
        }
    }
    
    /**
     * Configuración que se ejecuta después de cada método de prueba
     * @param result resultado de la prueba
     */
    @AfterMethod(alwaysRun = true)
    public void limpiezaMetodo(ITestResult result) {
        String nombrePrueba = result.getMethod().getMethodName();
        
        try {
            // Procesar resultado de la prueba
            procesarResultadoPrueba(result);
            
            // Limpiar recursos
            limpiarRecursos();
            
            logger.info(TipoMensaje.INFORMATIVO.formatearMensaje("=== FINALIZANDO PRUEBA: " + nombrePrueba + " ==="));
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error en limpieza de prueba: " + e.getMessage()));
        }
    }
    
    /**
     * Configuración que se ejecuta después de cada clase de prueba
     */
    @AfterClass(alwaysRun = true)
    public void limpiezaClase() {
        String nombreClase = this.getClass().getSimpleName();
        logger.info(TipoMensaje.INFORMATIVO.formatearMensaje("Finalizando clase de prueba: " + nombreClase));
    }
    
    /**
     * Configuración que se ejecuta una vez después de todas las pruebas de la suite
     */
    @AfterSuite(alwaysRun = true)
    public void limpiezaSuite() {
        logger.info(TipoMensaje.CONFIGURACION.crearSeparador("FINALIZANDO SUITE DE AUTOMATIZACIÓN FUNCIONAL"));
        logger.info(TipoMensaje.EXITO.formatearMensaje("Suite de pruebas completada"));
    }
    
    // === MÉTODOS PÚBLICOS PARA ACCESO A RECURSOS ===
    
    /**
     * Obtiene el driver actual del hilo
     * @return instancia de WebDriver
     */
    protected WebDriver obtenerDriver() {
        WebDriver driver = driverLocal.get();
        if (driver == null) {
            throw new IllegalStateException("Driver no inicializado para el hilo actual");
        }
        return driver;
    }
    
    /**
     * Obtiene el gestor de capturas del hilo actual
     * @return instancia de GestorCapturaPantalla
     */
    protected GestorCapturaPantalla obtenerGestorCaptura() {
        GestorCapturaPantalla gestor = gestorCapturaLocal.get();
        if (gestor == null) {
            throw new IllegalStateException("GestorCapturaPantalla no inicializado para el hilo actual");
        }
        return gestor;
    }
    
    /**
     * Obtiene la utilidad de esperas del hilo actual
     * @return instancia de EsperaExplicita
     */
    protected EsperaExplicita obtenerEsperaExplicita() {
        EsperaExplicita espera = esperaExplicitaLocal.get();
        if (espera == null) {
            throw new IllegalStateException("EsperaExplicita no inicializada para el hilo actual");
        }
        return espera;
    }
    
    /**
     * Obtiene el manejador de scroll del hilo actual
     * @return instancia de ManejadorScrollPagina
     */
    protected ManejadorScrollPagina obtenerManejadorScroll() {
        ManejadorScrollPagina manejador = manejadorScrollLocal.get();
        if (manejador == null) {
            throw new IllegalStateException("ManejadorScrollPagina no inicializado para el hilo actual");
        }
        return manejador;
    }
    
    // === MÉTODOS DE UTILIDAD PARA PRUEBAS ===
    
    /**
     * Captura pantalla con nombre personalizado
     * @param nombreCaptura nombre para la captura
     */
    protected void capturarPantalla(String nombreCaptura) {
        try {
            String rutaCaptura = obtenerGestorCaptura().capturarPantalla(nombreCaptura);
            logger.info(TipoMensaje.INFORMATIVO.formatearMensaje("Captura guardada: " + rutaCaptura));
            
            // Adjuntar a Allure si está disponible
            adjuntarCapturaAllure(rutaCaptura, nombreCaptura);
            
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("Error capturando pantalla: " + e.getMessage()));
        }
    }
    
    /**
     * Registra un paso de prueba en logs
     * @param descripcionPaso descripción del paso
     */
    protected void logPasoPrueba(String descripcionPaso) {
        logger.info(TipoMensaje.PASO_PRUEBA.formatearMensaje(descripcionPaso));
        
        // Agregar step a Allure si está disponible
        try {
            Allure.step(descripcionPaso);
        } catch (Exception e) {
            // Allure no disponible, continuar sin error
        }
    }
    
    /**
     * Registra una validación exitosa
     * @param descripcionValidacion descripción de la validación
     */
    protected void logValidacion(String descripcionValidacion) {
        logger.info(TipoMensaje.VALIDACION.formatearMensaje(descripcionValidacion));
    }
    
    /**
     * Registra información general
     * @param mensaje mensaje informativo
     */
    protected void logInformacion(String mensaje) {
        logger.info(TipoMensaje.INFORMATIVO.formatearMensaje(mensaje));
    }
    
    /**
     * Registra un error
     * @param mensaje mensaje de error
     */
    protected void logError(String mensaje) {
        logger.error(TipoMensaje.ERROR.formatearMensaje(mensaje));
    }
    
    /**
     * Registra una advertencia
     * @param mensaje mensaje de advertencia
     */
    protected void logAdvertencia(String mensaje) {
        logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(mensaje));
    }
    
    // === MÉTODOS PRIVADOS DE APOYO ===
    
    /**
     * Inicializa las utilidades para el driver actual
     * @param driver instancia de WebDriver
     */
    private void inicializarUtilidades(WebDriver driver) {
        try {
            // Inicializar gestor de capturas
            GestorCapturaPantalla gestorCaptura = new GestorCapturaPantalla();
            gestorCapturaLocal.set(gestorCaptura);
            
            // Inicializar espera explícita
            EsperaExplicita esperaExplicita = new EsperaExplicita(driver, configuracion.obtenerTimeoutExplicito());
            esperaExplicitaLocal.set(esperaExplicita);
            
            // Inicializar manejador de scroll
            ManejadorScrollPagina manejadorScroll = new ManejadorScrollPagina(driver);
            manejadorScrollLocal.set(manejadorScroll);
            
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Utilidades inicializadas correctamente"));
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error inicializando utilidades: " + e.getMessage()));
            throw new RuntimeException("Fallo en inicialización de utilidades", e);
        }
    }
    
    /**
     * Procesa el resultado de la prueba
     * @param result resultado de la prueba
     */
    private void procesarResultadoPrueba(ITestResult result) {
        String nombrePrueba = result.getMethod().getMethodName();
        
        switch (result.getStatus()) {
            case ITestResult.SUCCESS:
                logger.info(TipoMensaje.EXITO.formatearMensaje("PRUEBA EXITOSA: " + nombrePrueba));
                capturarPantalla(nombrePrueba + "_exitosa");
                break;
                
            case ITestResult.FAILURE:
                logger.error(TipoMensaje.ERROR.formatearMensaje("PRUEBA FALLIDA: " + nombrePrueba));
                logger.error(TipoMensaje.ERROR.formatearMensaje("Error: " + result.getThrowable().getMessage()));
                capturarPantalla(nombrePrueba + "_fallida");
                break;
                
            case ITestResult.SKIP:
                logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("PRUEBA OMITIDA: " + nombrePrueba));
                break;
                
            default:
                logger.info(TipoMensaje.INFORMATIVO.formatearMensaje("Estado desconocido para prueba: " + nombrePrueba));
        }
    }
    
    /**
     * Limpia los recursos del hilo actual
     */
    private void limpiarRecursos() {
        try {
            // Cerrar driver
            WebDriver driver = driverLocal.get();
            if (driver != null) {
                ConfiguracionNavegador.cerrarNavegador(driver);
                driverLocal.remove();
            }
            
            // Limpiar ThreadLocals
            gestorCapturaLocal.remove();
            esperaExplicitaLocal.remove();
            manejadorScrollLocal.remove();
            
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Recursos limpiados correctamente"));
            
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("Error limpiando recursos: " + e.getMessage()));
        }
    }
    
    /**
     * Inicializa los directorios necesarios para las pruebas
     */
    private void inicializarDirectorios() {
        try {
            // Los directorios se crean automáticamente por las utilidades cuando es necesario
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Directorios inicializados"));
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("Error inicializando directorios: " + e.getMessage()));
        }
    }
    
    /**
     * Adjunta captura a Allure si está disponible
     * @param rutaCaptura ruta de la captura
     * @param nombreCaptura nombre de la captura
     */
    private void adjuntarCapturaAllure(String rutaCaptura, String nombreCaptura) {
        try {
            byte[] captura = obtenerGestorCaptura().capturarPantallaComoBytes();
            if (captura != null) {
                Allure.addAttachment(nombreCaptura, "image/png", 
                    new java.io.ByteArrayInputStream(captura), "png");
            }
        } catch (Exception e) {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Allure no disponible o error adjuntando captura"));
        }
    }
}