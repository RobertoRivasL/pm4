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
import io.qameta.allure.Attachment;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Clase base para todas las pruebas automatizadas.
 * Proporciona configuración común, setup y teardown.
 * 
 * @author Roberto Rivas Lopez
 */
public abstract class BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(BaseTest.class);

    // ThreadLocal para ejecución paralela
    private static final ThreadLocal<WebDriver> driverLocal = new ThreadLocal<>();
    private static final ThreadLocal<GestorCapturaPantalla> gestorCapturaLocal = new ThreadLocal<>();
    private static final ThreadLocal<EsperaExplicita> esperaExplicitaLocal = new ThreadLocal<>();
    private static final ThreadLocal<ManejadorScrollPagina> manejadorScrollLocal = new ThreadLocal<>();

    protected ConfiguracionGlobal configuracion;

    @BeforeSuite(alwaysRun = true)
    public void configuracionSuite() {
        logger.info(TipoMensaje.CONFIGURACION.formatearMensaje(
                "=== INICIANDO SUITE DE AUTOMATIZACIÓN ==="));

        configuracion = ConfiguracionGlobal.obtenerInstancia();

        logger.info(TipoMensaje.INFORMATIVO.formatearMensaje(
                "URL Base: " + configuracion.obtenerUrlBase()));
        logger.info(TipoMensaje.INFORMATIVO.formatearMensaje(
                "Navegador: " + configuracion.obtenerTipoNavegador()));
    }

    @BeforeClass(alwaysRun = true)
    public void configuracionClase() {
        String nombreClase = this.getClass().getSimpleName();
        logger.info(TipoMensaje.PRUEBA.formatearMensaje(
                "Iniciando clase de prueba: " + nombreClase));
    }

    @BeforeMethod(alwaysRun = true)
    public void configuracionMetodo(Method method) {
        String nombreMetodo = method.getName();
        logger.info(TipoMensaje.PASO_PRUEBA.formatearMensaje(
                "Ejecutando prueba: " + nombreMetodo));

        try {
            inicializarDriver();

            String urlBase = configuracion.obtenerUrlBase();
            if (urlBase != null && !urlBase.isEmpty()) {
                obtenerDriver().get(urlBase);
                logger.info(TipoMensaje.INFORMATIVO.formatearMensaje(
                        "Navegando a: " + urlBase));
            }

            inicializarUtilidades();

        } catch (Exception e) {
            logger.error(TipoMensaje.CRITICO.formatearMensaje(
                    "Error en configuración: " + e.getMessage()));
            throw new RuntimeException("Fallo en configuración inicial", e);
        }
    }

    @AfterMethod(alwaysRun = true)
    public void limpiezaMetodo(ITestResult result) {
        String nombreMetodo = result.getMethod().getMethodName();

        try {
            if (result.getStatus() == ITestResult.FAILURE) {
                logger.error(TipoMensaje.ERROR.formatearMensaje(
                        "Prueba FALLIDA: " + nombreMetodo));

                capturarPantallaError(nombreMetodo, result.getThrowable().getMessage());
            } else if (result.getStatus() == ITestResult.SUCCESS) {
                logger.info(TipoMensaje.VALIDACION.formatearMensaje(
                        "Prueba EXITOSA: " + nombreMetodo));
            }

        } catch (Exception e) {
            logger.warn("Error durante limpieza: " + e.getMessage());
        } finally {
            cerrarDriver();
        }
    }

    @AfterClass(alwaysRun = true)
    public void limpiezaClase() {
        String nombreClase = this.getClass().getSimpleName();
        logger.info(TipoMensaje.PRUEBA.formatearMensaje(
                "Finalizando clase de prueba: " + nombreClase));
    }

    @AfterSuite(alwaysRun = true)
    public void limpiezaSuite() {
        logger.info(TipoMensaje.CONFIGURACION.formatearMensaje(
                "=== FINALIZANDO SUITE DE AUTOMATIZACIÓN ==="));
    }

    /**
     * Inicializa el WebDriver según configuración
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
            throw new RuntimeException("No se pudo inicializar WebDriver", e);
        }
    }

    /**
     * Inicializa utilidades necesarias
     */
    private void inicializarUtilidades() {
        WebDriver driver = obtenerDriver();

        gestorCapturaLocal.set(new GestorCapturaPantalla());
        esperaExplicitaLocal.set(new EsperaExplicita(driver, configuracion.obtenerTimeoutExplicito()));
        manejadorScrollLocal.set(new ManejadorScrollPagina(driver));

        logger.debug(TipoMensaje.DEBUG.formatearMensaje("Utilidades inicializadas"));
    }

    /**
     * Cierra el WebDriver y limpia ThreadLocal
     */
    private void cerrarDriver() {
        try {
            WebDriver driver = driverLocal.get();
            if (driver != null) {
                driver.quit();
                logger.debug(TipoMensaje.DEBUG.formatearMensaje("Driver cerrado"));
            }
        } catch (Exception e) {
            logger.warn("Error al cerrar driver: " + e.getMessage());
        } finally {
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
        String rutaCaptura = capturarPantalla("ERROR_" + nombrePrueba);
        if (rutaCaptura != null) {
            // Adjuntar a Allure
            adjuntarCaptura(rutaCaptura, "Error en " + nombrePrueba);
            
            logger.info(TipoMensaje.INFORMATIVO.formatearMensaje(
                "Captura de error guardada: " + rutaCaptura));
        }
    } catch (Exception e) {
        logger.warn("No se pudo capturar pantalla de error: " + e.getMessage());
    }

    /**
 * Adjunta captura a reporte Allure
 * @param rutaArchivo ruta del archivo de captura
 * @param descripcion descripción de la captura
 * @return bytes del archivo o array vacío si hay error
 */
private byte[] adjuntarCaptura(String rutaArchivo, String descripcion) {
    try {
        byte[] imageBytes = Files.readAllBytes(Paths.get(rutaArchivo));
        Allure.addAttachment(descripcion, "image/png", new ByteArrayInputStream(imageBytes), "png");
        return imageBytes;
    } catch (Exception e) {
        logger.warn("Error al adjuntar captura: " + e.getMessage());
        return new byte[0];
    }

    // === MÉTODOS PÚBLICOS PARA CLASES HEREDADAS ===

    /**
     * @return WebDriver actual del hilo
     */
    protected WebDriver obtenerDriver() {
        WebDriver driver = driverLocal.get();
        if (driver == null) {
            throw new IllegalStateException("WebDriver no inicializado. " +
                    "Verifique la configuración del test.");
        }
        return driver;
    }

    /**
     * @return GestorCapturaPantalla actual
     */
    protected GestorCapturaPantalla obtenerGestorCaptura() {
        return gestorCapturaLocal.get();
    }

    /**
     * @return EsperaExplicita actual
     */
    protected EsperaExplicita obtenerEsperaExplicita() {
        return esperaExplicitaLocal.get();
    }

    /**
     * @return ManejadorScrollPagina actual
     */
    protected ManejadorScrollPagina obtenerManejadorScroll() {
        return manejadorScrollLocal.get();
    }

    /**
     * Captura pantalla con descripción
     */
    protected String capturarPantalla(String descripcion) {
        GestorCapturaPantalla gestor = obtenerGestorCaptura();
        if (gestor != null) {
            // CORRECCIÓN: Usar capturarPantalla(driver, descripcion)
            return gestor.capturarPantalla(obtenerDriver(), descripcion);
        }
        return null;
    }

    /**
     * Log de paso de prueba
     */
    protected void logPasoPrueba(String mensaje) {
        logger.info(TipoMensaje.PASO_PRUEBA.formatearMensaje(mensaje));
        Allure.step(mensaje);
    }

    /**
     * Log de validación
     */
    protected void logValidacion(String mensaje) {
        logger.info(TipoMensaje.VALIDACION.formatearMensaje(mensaje));
        Allure.step("✓ " + mensaje);
    }
}