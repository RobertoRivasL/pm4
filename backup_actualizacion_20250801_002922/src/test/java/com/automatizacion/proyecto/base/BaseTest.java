package com.automatizacion.proyecto.base;

import com.automatizacion.proyecto.configuracion.ConfiguracionGlobal;
import com.automatizacion.proyecto.configuracion.ConfiguracionNavegador;
import com.automatizacion.proyecto.enums.TipoMensaje;
import com.automatizacion.proyecto.enums.TipoNavegador;
import com.automatizacion.proyecto.utilidades.GestorCapturaPantalla;
import com.automatizacion.proyecto.utilidades.EsperaExplicita;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.*;

/**
 * Clase base para todas las pruebas
 * @author Roberto Rivas Lopez
 */
public abstract class BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(BaseTest.class);
    
    protected static final ThreadLocal<WebDriver> driverLocal = new ThreadLocal<>();
    protected static final ThreadLocal<GestorCapturaPantalla> gestorCapturaLocal = new ThreadLocal<>();
    protected static final ThreadLocal<EsperaExplicita> esperaExplicitaLocal = new ThreadLocal<>();
    
    protected ConfiguracionGlobal configuracion;
    
    @BeforeSuite(alwaysRun = true)
    public void configuracionSuite() {
        logger.info(TipoMensaje.CONFIGURACION.formatearMensaje("=== INICIANDO SUITE DE AUTOMATIZACIÓN ==="));
        configuracion = ConfiguracionGlobal.obtenerInstancia();
    }
    
    @BeforeMethod(alwaysRun = true)
    public void configuracionPrueba() {
        inicializarDriver();
        inicializarUtilidades();
    }
    
    @AfterMethod(alwaysRun = true)
    public void limpiezaPrueba(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            String nombrePrueba = result.getMethod().getMethodName();
            String mensaje = result.getThrowable() != null ? result.getThrowable().getMessage() : "Error desconocido";
            capturarPantallaError(nombrePrueba, mensaje);
        }
        cerrarDriver();
    }
    
    private void inicializarDriver() {
        try {
            String tipoNavegadorStr = configuracion.obtenerTipoNavegador();
            TipoNavegador tipoNavegador = TipoNavegador.desdeString(tipoNavegadorStr);
            boolean esHeadless = configuracion.esNavegadorHeadless();
            
            WebDriver driver = ConfiguracionNavegador.crearDriver(tipoNavegador, esHeadless);
            driverLocal.set(driver);
            
        } catch (Exception e) {
            logger.error(TipoMensaje.CRITICO.formatearMensaje("Error al inicializar driver: " + e.getMessage()));
            throw new RuntimeException("No se pudo inicializar el WebDriver", e);
        }
    }
    
    private void inicializarUtilidades() {
        WebDriver driver = obtenerDriver();
        gestorCapturaLocal.set(new GestorCapturaPantalla());
        esperaExplicitaLocal.set(new EsperaExplicita(driver, configuracion.obtenerTimeoutExplicito()));
    }
    
    private void cerrarDriver() {
        try {
            WebDriver driver = driverLocal.get();
            if (driver != null) {
                driver.quit();
            }
        } finally {
            driverLocal.remove();
            gestorCapturaLocal.remove();
            esperaExplicitaLocal.remove();
        }
    }
    
    private void capturarPantallaError(String nombrePrueba, String mensajeError) {
        try {
            GestorCapturaPantalla gestor = gestorCapturaLocal.get();
            WebDriver driver = driverLocal.get();
            
            if (gestor != null && driver != null) {
                gestor.capturarPantallaError(driver, nombrePrueba, mensajeError);
            }
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error al capturar pantalla: " + e.getMessage()));
        }
    }
    
    protected WebDriver obtenerDriver() {
        return driverLocal.get();
    }
    
    protected GestorCapturaPantalla obtenerGestorCaptura() {
        return gestorCapturaLocal.get();
    }
    
    protected EsperaExplicita obtenerEsperaExplicita() {
        return esperaExplicitaLocal.get();
    }
}
