// ===============================================
// ARCHIVO: src/main/java/com/automatizacion/proyecto/utilidades/GestorCapturaPantalla.java
// VERSIÓN CORREGIDA COMPLETA
// ===============================================
package com.automatizacion.proyecto.utilidades;

import com.automatizacion.proyecto.enums.TipoMensaje;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Gestor centralizado para captura de pantallas en las pruebas automatizadas.
 * @author Roberto Rivas Lopez
 */
public class GestorCapturaPantalla {
    
    private static final Logger logger = LoggerFactory.getLogger(GestorCapturaPantalla.class);
    private final WebDriver driver;
    private final String directorioBase = "capturas";
    
    public GestorCapturaPantalla(WebDriver driver) {
        this.driver = driver;
        crearDirectorioSiNoExiste();
    }
    
    /**
     * Captura una screenshot con nombre personalizado
     * @param nombreArchivo nombre base del archivo (sin extensión)
     * @return ruta completa del archivo generado
     */
    public String capturarPantalla(String nombreArchivo) {
        return capturarPantalla(nombreArchivo, true);
    }
    
    /**
     * Captura una screenshot con opciones avanzadas
     * @param nombreArchivo nombre base del archivo
     * @param adjuntarAAllure si debe adjuntar a Allure
     * @return ruta completa del archivo generado
     */
    public String capturarPantalla(String nombreArchivo, boolean adjuntarAAllure) {
        try {
            if (!(driver instanceof TakesScreenshot)) {
                logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("El driver no soporta capturas"));
                return null;
            }
            
            // Asegurar que la página esté lista para captura
            try {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                Object readyState = js.executeScript("return document.readyState");
                if (!"complete".equals(readyState)) {
                    Thread.sleep(1000); // Esperar si no está completa
                }
                Thread.sleep(500); // Pausa adicional para estabilizar
            } catch (Exception e) {
                logger.debug(TipoMensaje.DEBUG.formatearMensaje("No se pudo verificar estado de página"));
            }
            
            String nombreCompleto = generarNombreArchivoUnico(nombreArchivo);
            String rutaCompleta = directorioBase + File.separator + nombreCompleto;
            
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            
            File archivoCaptura = new File(rutaCompleta);
            FileUtils.writeByteArrayToFile(archivoCaptura, screenshot);
            
            logger.info(TipoMensaje.EXITO.formatearMensaje("Captura guardada: " + nombreCompleto + " (Tamaño: " + screenshot.length + " bytes)"));
            
            if (adjuntarAAllure) {
                // Solo adjuntar a Allure si está disponible, si no, solo guardar archivo
                try {
                    adjuntarAAllure(screenshot, nombreArchivo);
                } catch (Exception e) {
                    logger.debug(TipoMensaje.DEBUG.formatearMensaje("No se pudo adjuntar a Allure: " + e.getMessage()));
                }
            }
            
            return rutaCompleta;
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error capturando pantalla: " + e.getMessage()));
            return null;
        }
    }
    
    /**
     * Captura pantalla por error en prueba
     * @param nombrePrueba nombre de la prueba que falló
     * @param excepcion excepción que causó el error
     * @return ruta del archivo generado
     */
    public String capturarPantallaError(String nombrePrueba, Throwable excepcion) {
        String nombreArchivo = "ERROR_" + nombrePrueba + "_" + excepcion.getClass().getSimpleName();
        logger.error(TipoMensaje.ERROR.formatearMensaje("Capturando pantalla por error en: " + nombrePrueba));
        return capturarPantalla(nombreArchivo, true);
    }
    
    /**
     * Genera un nombre de archivo único con timestamp
     * @param nombreBase nombre base del archivo
     * @return nombre completo con timestamp y extensión
     */
    private String generarNombreArchivoUnico(String nombreBase) {
        String nombreLimpio = nombreBase.replaceAll("[^a-zA-Z0-9_-]", "_");
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss-SSS"));
        String hiloInfo = Thread.currentThread().getName().replaceAll("[^a-zA-Z0-9]", "");
        return String.format("%s_%s_%s.png", nombreLimpio, timestamp, hiloInfo);
    }
    
    /**
     * Crea el directorio base para capturas si no existe
     */
    private void crearDirectorioSiNoExiste() {
        try {
            File directorio = new File(directorioBase);
            if (!directorio.exists()) {
                boolean creado = directorio.mkdirs();
                if (creado) {
                    logger.info(TipoMensaje.CONFIGURACION.formatearMensaje("Directorio de capturas creado: " + directorioBase));
                } else {
                    logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("No se pudo crear directorio de capturas: " + directorioBase));
                }
            }
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error creando directorio de capturas: " + e.getMessage()));
        }
    }
    
    /**
     * Adjunta la captura al reporte Allure (método privado simplificado)
     * @param screenshot bytes de la captura
     * @param nombre nombre descriptivo para Allure
     */
    private void adjuntarAAllure(byte[] screenshot, String nombre) {
        // Implementación simplificada - solo para evitar errores de compilación
        // En un entorno real con Allure, aquí iría la anotación @Attachment
        logger.debug(TipoMensaje.DEBUG.formatearMensaje("Captura preparada para Allure: " + nombre));
    }
    
    /**
     * Verifica si el driver soporta capturas de pantalla
     * @return true si soporta capturas
     */
    public boolean soportaCapturas() {
        return driver instanceof TakesScreenshot;
    }
    
    /**
     * Obtiene la ruta del directorio base de capturas
     * @return ruta del directorio base
     */
    public String obtenerDirectorioBase() {
        return directorioBase;
    }
    
    /**
     * Captura pantalla y la retorna como bytes para uso directo
     * @return array de bytes de la captura
     */
    public byte[] capturarPantallaBytes() {
        try {
            if (!(driver instanceof TakesScreenshot)) {
                logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("El driver no soporta capturas"));
                return new byte[0];
            }
            
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Captura generada como bytes. Tamaño: " + screenshot.length));
            return screenshot;
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error generando captura como bytes: " + e.getMessage()));
            return new byte[0];
        }
    }
    
    /**
     * Obtiene estadísticas del directorio de capturas
     * @return información estadística
     */
    public String obtenerEstadisticasDirectorio() {
        try {
            File directorio = new File(directorioBase);
            
            if (!directorio.exists()) {
                return "Directorio no existe";
            }
            
            File[] archivos = directorio.listFiles((dir, name) -> name.endsWith(".png"));
            
            if (archivos == null) {
                return "No se pueden leer archivos del directorio";
            }
            
            long tamanoTotal = 0;
            for (File archivo : archivos) {
                tamanoTotal += archivo.length();
            }
            
            double tamanoMB = tamanoTotal / (1024.0 * 1024.0);
            return String.format("Capturas: %d archivos, %.2f MB total", archivos.length, tamanoMB);
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error obteniendo estadísticas: " + e.getMessage()));
            return "Error obteniendo estadísticas";
        }
    }
}