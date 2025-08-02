package com.automatizacion.proyecto.utilidades;

import com.automatizacion.proyecto.configuracion.ConfiguracionGlobal;
import com.automatizacion.proyecto.enums.TipoMensaje;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Gestor de capturas de pantalla
 * @author Roberto Rivas Lopez
 */
public class GestorCapturaPantalla {
    private static final Logger logger = LoggerFactory.getLogger(GestorCapturaPantalla.class);
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    
    private final String directorioBase;
    
    public GestorCapturaPantalla() {
        ConfiguracionGlobal config = ConfiguracionGlobal.obtenerInstancia();
        this.directorioBase = config.obtenerDirectorioCapturas();
        crearDirectorioSiNoExiste();
    }
    
    private void crearDirectorioSiNoExiste() {
        File directorio = new File(directorioBase);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }
    }
    
    public String capturarPantalla(WebDriver driver, String nombreArchivo) {
        if (driver == null) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Driver es null"));
            return null;
        }
        
        try {
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File archivoTemporal = screenshot.getScreenshotAs(OutputType.FILE);
            
            String timestamp = LocalDateTime.now().format(FORMATO_FECHA);
            String rutaCompleta = directorioBase + File.separator + nombreArchivo + "_" + timestamp + ".png";
            File archivoDestino = new File(rutaCompleta);
            
            FileUtils.copyFile(archivoTemporal, archivoDestino);
            
            logger.info(TipoMensaje.EXITO.formatearMensaje("Captura guardada: " + rutaCompleta));
            return rutaCompleta;
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error al capturar pantalla: " + e.getMessage()));
            return null;
        }
    }
    
    public String capturarPantallaError(WebDriver driver, String nombrePrueba, String mensajeError) {
        String nombreArchivo = "ERROR_" + nombrePrueba + "_" + mensajeError.replaceAll("[^a-zA-Z0-9]", "_");
        return capturarPantalla(driver, nombreArchivo);
    }
}
