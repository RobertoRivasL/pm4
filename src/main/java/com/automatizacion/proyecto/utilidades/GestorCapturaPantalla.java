package com.automatizacion.proyecto.utilidades;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Gestor para captura de pantallas en las pruebas de automatización
 * Cumple con requerimientos ABP: Evidencia visual de pruebas
 * 
 * @author Roberto Rivas Lopez
 */
public class GestorCapturaPantalla {
    private static final Logger logger = LoggerFactory.getLogger(GestorCapturaPantalla.class);
    
    // Configuración de directorios y formatos
    private static final String CARPETA_CAPTURAS = "target/capturas-pantalla";
    private static final String CARPETA_FALLOS = "target/capturas-pantalla/fallos";
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    
    /**
     * Constructor que inicializa las carpetas necesarias
     */
    public GestorCapturaPantalla() {
        inicializarCarpetas();
    }
    
    /**
     * Crea las carpetas necesarias para las capturas
     */
    private void inicializarCarpetas() {
        try {
            File carpetaCapturas = new File(CARPETA_CAPTURAS);
            File carpetaFallos = new File(CARPETA_FALLOS);
            
            if (!carpetaCapturas.exists()) {
                carpetaCapturas.mkdirs();
                logger.debug("📁 Carpeta de capturas creada: {}", CARPETA_CAPTURAS);
            }
            
            if (!carpetaFallos.exists()) {
                carpetaFallos.mkdirs();
                logger.debug("📁 Carpeta de fallos creada: {}", CARPETA_FALLOS);
            }
            
        } catch (Exception e) {
            logger.error("❌ Error creando carpetas de capturas: {}", e.getMessage());
        }
    }
    
    /**
     * Captura pantalla con nombre personalizado
     * @param driver WebDriver instance
     * @param nombreArchivo nombre base del archivo
     * @return String ruta completa del archivo generado
     */
    public String capturarPantalla(WebDriver driver, String nombreArchivo) {
        try {
            // Validar parámetros
            if (driver == null) {
                logger.error("❌ Driver es null, no se puede capturar pantalla");
                return null;
            }
            
            if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
                nombreArchivo = "captura";
            }
            
            // Limpiar nombre de archivo
            nombreArchivo = limpiarNombreArchivo(nombreArchivo);
            
            // Capturar screenshot
            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            File archivoTemporal = takesScreenshot.getScreenshotAs(OutputType.FILE);
            
            // Generar nombre con timestamp
            String timestamp = LocalDateTime.now().format(FORMATO_FECHA);
            String nombreCompleto = String.format("%s_%s.png", nombreArchivo, timestamp);
            
            // Guardar archivo
            File archivoDestino = new File(CARPETA_CAPTURAS, nombreCompleto);
            FileUtils.copyFile(archivoTemporal, archivoDestino);
            
            String rutaCompleta = archivoDestino.getAbsolutePath();
            logger.info("📸 Captura guardada: {}", rutaCompleta);
            
            return rutaCompleta;
            
        } catch (IOException e) {
            logger.error("❌ Error capturando pantalla: {}", e.getMessage());
            return null;
        } catch (Exception e) {
            logger.error("❌ Error inesperado capturando pantalla: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * Captura pantalla automática cuando ocurre un fallo
     * @param driver WebDriver instance
     * @param nombreTest nombre de la prueba que falló
     * @return String ruta completa del archivo generado
     */
    public String capturarPantallaFallo(WebDriver driver, String nombreTest) {
        try {
            if (driver == null) {
                logger.error("❌ Driver es null, no se puede capturar pantalla de fallo");
                return null;
            }
            
            if (nombreTest == null || nombreTest.trim().isEmpty()) {
                nombreTest = "fallo_desconocido";
            }
            
            // Limpiar nombre
            nombreTest = limpiarNombreArchivo(nombreTest);
            
            // Capturar screenshot
            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            File archivoTemporal = takesScreenshot.getScreenshotAs(OutputType.FILE);
            
            // Generar nombre con prefijo FALLO y timestamp
            String timestamp = LocalDateTime.now().format(FORMATO_FECHA);
            String nombreCompleto = String.format("FALLO_%s_%s.png", nombreTest, timestamp);
            
            // Guardar en carpeta de fallos
            File archivoDestino = new File(CARPETA_FALLOS, nombreCompleto);
            FileUtils.copyFile(archivoTemporal, archivoDestino);
            
            String rutaCompleta = archivoDestino.getAbsolutePath();
            logger.error("💥 Captura de fallo guardada: {}", rutaCompleta);
            
            return rutaCompleta;
            
        } catch (IOException e) {
            logger.error("❌ Error capturando pantalla de fallo: {}", e.getMessage());
            return null;
        } catch (Exception e) {
            logger.error("❌ Error inesperado capturando pantalla de fallo: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * Captura pantalla con prefijo personalizado
     * @param driver WebDriver instance
     * @param prefijo prefijo para el nombre del archivo
     * @param descripcion descripción adicional
     * @return String ruta completa del archivo generado
     */
    public String capturarPantallaConPrefijo(WebDriver driver, String prefijo, String descripcion) {
        String nombreArchivo = String.format("%s_%s", 
            prefijo != null ? prefijo : "captura", 
            descripcion != null ? descripcion : "screenshot");
        return capturarPantalla(driver, nombreArchivo);
    }
    
    /**
     * Captura pantalla de página completa (si el navegador lo soporta)
     * @param driver WebDriver instance
     * @param nombreArchivo nombre base del archivo
     * @return String ruta completa del archivo generado
     */
    public String capturarPantallaCompleta(WebDriver driver, String nombreArchivo) {
        // Por ahora, usar el método estándar
        // En el futuro se puede implementar scroll y captura completa
        return capturarPantalla(driver, nombreArchivo + "_completa");
    }
    
    /**
     * Limpia el nombre de archivo removiendo caracteres no válidos
     * @param nombre nombre original
     * @return String nombre limpio y seguro para archivos
     */
    private String limpiarNombreArchivo(String nombre) {
        if (nombre == null) {
            return "captura";
        }
        
        // Remover caracteres no válidos para nombres de archivo
        String nombreLimpio = nombre.replaceAll("[^a-zA-Z0-9._-]", "_");
        
        // Limitar longitud
        if (nombreLimpio.length() > 50) {
            nombreLimpio = nombreLimpio.substring(0, 50);
        }
        
        // Asegurar que no esté vacío
        if (nombreLimpio.trim().isEmpty()) {
            nombreLimpio = "captura";
        }
        
        return nombreLimpio;
    }
    
    /**
     * Obtiene la carpeta de capturas configurada
     * @return String ruta de la carpeta de capturas
     */
    public String obtenerCarpetaCapturas() {
        return CARPETA_CAPTURAS;
    }
    
    /**
     * Obtiene la carpeta de fallos configurada
     * @return String ruta de la carpeta de fallos
     */
    public String obtenerCarpetaFallos() {
        return CARPETA_FALLOS;
    }
    
    /**
     * Verifica si el driver soporta capturas de pantalla
     * @param driver WebDriver instance
     * @return boolean true si soporta capturas
     */
    public boolean soportaCapturas(WebDriver driver) {
        return driver instanceof TakesScreenshot;
    }
    
    /**
     * Limpia capturas antiguas (más de X días)
     * @param diasAntiguedad días de antigüedad para limpiar
     * @return int número de archivos eliminados
     */
    public int limpiarCapturasAntiguas(int diasAntiguedad) {
        int archivosEliminados = 0;
        
        try {
            File carpeta = new File(CARPETA_CAPTURAS);
            if (!carpeta.exists()) {
                return 0;
            }
            
            long tiempoLimite = System.currentTimeMillis() - (diasAntiguedad * 24L * 60L * 60L * 1000L);
            
            File[] archivos = carpeta.listFiles((dir, name) -> name.endsWith(".png"));
            if (archivos != null) {
                for (File archivo : archivos) {
                    if (archivo.lastModified() < tiempoLimite) {
                        if (archivo.delete()) {
                            archivosEliminados++;
                            logger.debug("🗑️ Archivo antiguo eliminado: {}", archivo.getName());
                        }
                    }
                }
            }
            
            logger.info("🧹 Limpieza completada: {} archivos eliminados", archivosEliminados);
            
        } catch (Exception e) {
            logger.error("❌ Error limpiando capturas antiguas: {}", e.getMessage());
        }
        
        return archivosEliminados;
    }
}