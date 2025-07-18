package com.robertorivas.automatizacion.utilidades;

import com.robertorivas.automatizacion.configuracion.ConfiguracionPruebas;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Gestor para el manejo de evidencias (capturas de pantalla, logs, etc.).
 * 
 * Principios aplicados:
 * - Single Responsibility: Solo maneja evidencias
 * - Utility Class: Métodos estáticos para fácil uso
 * - Error Handling: Manejo robusto de errores de I/O
 * 
 * @author Roberto Rivas Lopez
 */
public class GestorEvidencias {
    
    private static final Logger logger = LoggerFactory.getLogger(GestorEvidencias.class);
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    
    private static ConfiguracionPruebas config = ConfiguracionPruebas.obtenerInstancia();
    
    /**
     * Toma una captura de pantalla.
     */
    public static String tomarCaptura(WebDriver driver, String nombrePrueba, String descripcion) {
        try {
            // Crear directorio si no existe
            Path rutaCapturas = config.obtenerRutaCapturas();
            Files.createDirectories(rutaCapturas);
            
            // Generar nombre de archivo
            String timestamp = LocalDateTime.now().format(FORMATO_FECHA);
            String nombreArchivo = String.format("captura_%s_%s_%s.png", 
                                                nombrePrueba, descripcion, timestamp);
            
            // Limpiar nombre de archivo
            nombreArchivo = nombreArchivo.replaceAll("[^a-zA-Z0-9._-]", "_");
            
            Path rutaArchivo = rutaCapturas.resolve(nombreArchivo);
            
            // Tomar captura
            if (driver instanceof TakesScreenshot) {
                File archivoCaptura = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(archivoCaptura, rutaArchivo.toFile());
                
                logger.debug("Captura guardada: {}", rutaArchivo);
                return rutaArchivo.toString();
            } else {
                logger.warn("El driver no soporta capturas de pantalla");
                return null;
            }
            
        } catch (IOException e) {
            logger.error("Error tomando captura: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * Toma una captura simple.
     */
    public static String tomarCaptura(WebDriver driver, String nombrePrueba) {
        return tomarCaptura(driver, nombrePrueba, "captura");
    }
    
    /**
     * Crea el directorio de evidencias si no existe.
     */
    public static void crearDirectoriosEvidencias() {
        try {
            Files.createDirectories(config.obtenerRutaCapturas());
            Files.createDirectories(config.obtenerRutaLogs());
            Files.createDirectories(config.obtenerRutaReportes());
            
            logger.info("Directorios de evidencias creados");
        } catch (IOException e) {
            logger.error("Error creando directorios de evidencias: {}", e.getMessage());
        }
    }
    
    /**
     * Limpia archivos de evidencias antiguos.
     */
    public static void limpiarEvidenciasAntiguas(int diasAntiguedad) {
        try {
            Path rutaCapturas = config.obtenerRutaCapturas();
            if (Files.exists(rutaCapturas)) {
                Files.walk(rutaCapturas)
                     .filter(Files::isRegularFile)
                     .filter(path -> {
                         try {
                             return Files.getLastModifiedTime(path).toInstant()
                                   .isBefore(java.time.Instant.now().minus(diasAntiguedad, java.time.temporal.ChronoUnit.DAYS));
                         } catch (IOException e) {
                             return false;
                         }
                     })
                     .forEach(path -> {
                         try {
                             Files.delete(path);
                             logger.debug("Archivo eliminado: {}", path);
                         } catch (IOException e) {
                             logger.warn("No se pudo eliminar: {}", path);
                         }
                     });
            }
        } catch (IOException e) {
            logger.error("Error limpiando evidencias antiguas: {}", e.getMessage());
        }
    }
}