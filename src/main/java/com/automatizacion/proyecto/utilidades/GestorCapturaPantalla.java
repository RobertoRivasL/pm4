package com.automatizacion.proyecto.utilidades;

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

public class GestorCapturaPantalla {
    
    private static final Logger logger = LoggerFactory.getLogger(GestorCapturaPantalla.class);
    private final WebDriver driver;
    private final String directorioBase = "capturas";
    
    public GestorCapturaPantalla(WebDriver driver) {
        this.driver = driver;
        crearDirectorioSiNoExiste();
    }
    
    public String capturarPantalla(String nombreArchivo) {
        try {
            if (!(driver instanceof TakesScreenshot)) {
                logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("Driver no soporta capturas"));
                return null;
            }
            
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String nombreCompleto = nombreArchivo + "_" + timestamp + ".png";
            String rutaCompleta = directorioBase + File.separator + nombreCompleto;
            
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            FileUtils.writeByteArrayToFile(new File(rutaCompleta), screenshot);
            
            logger.info(TipoMensaje.EXITO.formatearMensaje("Captura guardada: " + nombreCompleto));
            return rutaCompleta;
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error capturando pantalla: " + e.getMessage()));
            return null;
        }
    }
    
    private void crearDirectorioSiNoExiste() {
        File directorio = new File(directorioBase);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }
    }
}