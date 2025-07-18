package com.robertorivas.automatizacion.pruebas;

import com.robertorivas.automatizacion.configuracion.ConfiguracionNavegador;
import com.robertorivas.automatizacion.configuracion.ConfiguracionPruebas;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
