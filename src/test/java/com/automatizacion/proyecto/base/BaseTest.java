package com.automatizacion.proyecto.base;

import com.automatizacion.proyecto.configuracion.ConfiguracionGlobal;
import com.automatizacion.proyecto.configuracion.ConfiguracionNavegador;
import com.automatizacion.proyecto.enums.TipoMensaje;
import com.automatizacion.proyecto.utilidades.GestorCapturaPantalla;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.*;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;

public abstract class BaseTest {
    
    protected static final Logger logger = LoggerFactory.getLogger(BaseTest.class);
    protected WebDriver driver;
    protected ConfiguracionGlobal configuracion;
    protected GestorCapturaPantalla gestorCaptura;
    protected String nombrePruebaActual;
    protected LocalDateTime inicioEjecucion;
    protected boolean pruebaExitosa;
    
    @BeforeSuite(alwaysRun = true)
    public void configuracionSuite() {
        logger.info(TipoMensaje.CONFIGURACION.formatearMensaje("INICIANDO SUITE DE AUTOMATIZACIÓN"));
        configuracion = ConfiguracionGlobal.obtenerInstancia();
        logger.info(TipoMensaje.INFORMATIVO.formatearMensaje("Configuración cargada: " + configuracion.obtenerInformacionEstado()));
    }
    
    @BeforeMethod(alwaysRun = true)
    public void configuracionBasePrueba(Method metodo) {
        nombrePruebaActual = metodo.getName();
        inicioEjecucion = LocalDateTime.now();
        pruebaExitosa = false;
        
        logger.info(TipoMensaje.PRUEBA.formatearMensaje("INICIANDO PRUEBA: " + nombrePruebaActual));
        
        try {
            inicializarWebDriver();
            gestorCaptura = new GestorCapturaPantalla(driver);
            navegarAUrlBase();
            configuracionEspecificaPrueba();
            
            logger.info(TipoMensaje.EXITO.formatearMensaje("Configuración base completada para: " + nombrePruebaActual));
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error en configuración base: " + e.getMessage()));
            if (driver != null && gestorCaptura != null) {
                gestorCaptura.capturarPantalla("ConfiguracionBase_" + nombrePruebaActual);
            }
            limpiarRecursos();
            throw new RuntimeException("Fallo en configuración base", e);
        }
    }
    
    @AfterMethod(alwaysRun = true)
    public void limpiezaBasePrueba(ITestResult resultado) {
        try {
            pruebaExitosa = resultado.getStatus() == ITestResult.SUCCESS;
            Duration duracion = Duration.between(inicioEjecucion, LocalDateTime.now());
            
            if (pruebaExitosa) {
                logger.info(TipoMensaje.EXITO.formatearMensaje("PRUEBA EXITOSA: " + nombrePruebaActual + " - Duración: " + duracion.toSeconds() + "s"));
            } else {
                logger.error(TipoMensaje.ERROR.formatearMensaje("PRUEBA FALLIDA: " + nombrePruebaActual + " - Duración: " + duracion.toSeconds() + "s"));
                if (gestorCaptura != null) {
                    gestorCaptura.capturarPantalla("FALLO_" + nombrePruebaActual);
                }
            }
            
            limpiezaEspecificaPrueba();
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error en limpieza: " + e.getMessage()));
        } finally {
            limpiarRecursos();
        }
    }
    
    @AfterSuite(alwaysRun = true)
    public void limpiezaSuite() {
        logger.info(TipoMensaje.CONFIGURACION.formatearMensaje("FINALIZANDO SUITE DE AUTOMATIZACIÓN"));
    }
    
    protected void inicializarWebDriver() {
        try {
            ConfiguracionNavegador configuradorNavegador = new ConfiguracionNavegador();
            driver = configuradorNavegador.crearWebDriver();
            logger.info(TipoMensaje.EXITO.formatearMensaje("WebDriver inicializado correctamente"));
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error inicializando WebDriver: " + e.getMessage()));
            throw new RuntimeException("No se pudo inicializar WebDriver", e);
        }
    }
    
    protected void navegarAUrlBase() {
        try {
            String urlBase = configuracion.obtenerUrlRegistro(); // Para registro por defecto
            driver.get(urlBase);
            logger.info(TipoMensaje.INFORMATIVO.formatearMensaje("Navegando a: " + urlBase));
            Thread.sleep(2000); // Espera para carga
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error navegando a URL base: " + e.getMessage()));
            throw new RuntimeException("No se pudo navegar a URL base", e);
        }
    }
    
    protected void limpiarRecursos() {
        if (driver != null) {
            try {
                driver.quit();
                logger.debug(TipoMensaje.DEBUG.formatearMensaje("WebDriver cerrado correctamente"));
            } catch (Exception e) {
                logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("Error cerrando WebDriver: " + e.getMessage()));
            } finally {
                driver = null;
            }
        }
    }
    
    // Métodos template para ser implementados por subclases
    protected void configuracionEspecificaPrueba() {
        // Implementación opcional en subclases
    }
    
    protected void limpiezaEspecificaPrueba() {
        // Implementación opcional en subclases
    }
    
    // Métodos de utilidad para pruebas
    @Step("Paso de prueba: {mensaje}")
    protected void logPasoPrueba(String mensaje) {
        logger.info(TipoMensaje.PASO_PRUEBA.formatearMensaje(mensaje));
    }
    
    @Step("Validación: {mensaje}")
    protected void logValidacion(String mensaje) {
        logger.info(TipoMensaje.VALIDACION.formatearMensaje(mensaje));
    }
    
    protected void esperarSegundos(int segundos) {
        try {
            Thread.sleep(segundos * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    protected WebDriver obtenerDriver() {
        return driver;
    }
    
    protected void capturarPantalla(String nombreArchivo) {
        if (gestorCaptura != null) {
            gestorCaptura.capturarPantalla(nombreArchivo);
        }
    }

    
    
}