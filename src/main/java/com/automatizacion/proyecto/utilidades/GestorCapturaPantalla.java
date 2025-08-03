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
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase responsable de la gestión de capturas de pantalla durante la ejecución de pruebas.
 * Proporciona funcionalidades para capturar, guardar y gestionar screenshots.
 * 
 * Sigue el principio de Responsabilidad Única y proporciona abstracción
 * para el manejo de capturas de pantalla.
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
public class GestorCapturaPantalla {
    
    private static final Logger logger = LoggerFactory.getLogger(GestorCapturaPantalla.class);
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    private static final String EXTENSION_IMAGEN = ".png";
    
    private final ConfiguracionGlobal configuracion;
    private final String directorioBase;
    private final ThreadLocal<WebDriver> driverLocal = new ThreadLocal<>();
    
    /**
     * Constructor que inicializa el gestor con la configuración global
     */
    public GestorCapturaPantalla() {
        this.configuracion = ConfiguracionGlobal.obtenerInstancia();
        this.directorioBase = configuracion.obtenerDirectorioCapturas();
        crearDirectorioSiNoExiste();
    }
    
    /**
     * Constructor que permite especificar un directorio personalizado
     * @param directorioPersonalizado directorio donde guardar las capturas
     */
    public GestorCapturaPantalla(String directorioPersonalizado) {
        this.configuracion = ConfiguracionGlobal.obtenerInstancia();
        this.directorioBase = directorioPersonalizado;
        crearDirectorioSiNoExiste();
    }
    
    /**
     * Establece el driver para el hilo actual
     * @param driver instancia de WebDriver
     */
    public void establecerDriver(WebDriver driver) {
        driverLocal.set(driver);
    }
    
    /**
     * Obtiene el driver del hilo actual
     * @return WebDriver del hilo actual
     */
    private WebDriver obtenerDriver() {
        WebDriver driver = driverLocal.get();
        if (driver == null) {
            throw new IllegalStateException("Driver no establecido para el hilo actual");
        }
        return driver;
    }
    
    /**
     * Crea el directorio de capturas si no existe
     */
    private void crearDirectorioSiNoExiste() {
        try {
            File directorio = new File(directorioBase);
            if (!directorio.exists()) {
                boolean creado = directorio.mkdirs();
                if (creado) {
                    logger.info(TipoMensaje.CONFIGURACION.formatearMensaje(
                        "Directorio de capturas creado: " + directorio.getAbsolutePath()));
                } else {
                    logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                        "No se pudo crear el directorio de capturas: " + directorio.getAbsolutePath()));
                }
            }
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error creando directorio de capturas: " + e.getMessage()));
        }
    }
    
    /**
     * Captura pantalla y la guarda con nombre específico
     * @param nombreArchivo nombre para el archivo de captura
     * @return ruta completa del archivo guardado
     */
    public String capturarPantalla(String nombreArchivo) {
        try {
            WebDriver driver = obtenerDriver();
            if (!(driver instanceof TakesScreenshot)) {
                throw new IllegalStateException("Driver no soporta capturas de pantalla");
            }
            
            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            File archivoOrigen = takesScreenshot.getScreenshotAs(OutputType.FILE);
            
            String nombreCompleto = generarNombreArchivo(nombreArchivo);
            File archivoDestino = new File(directorioBase, nombreCompleto);
            
            FileUtils.copyFile(archivoOrigen, archivoDestino);
            
            logger.info(TipoMensaje.INFORMATIVO.formatearMensaje(
                "Captura guardada: " + archivoDestino.getAbsolutePath()));
            
            return archivoDestino.getAbsolutePath();
            
        } catch (IOException e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error guardando captura: " + e.getMessage()));
            throw new RuntimeException("Error en captura de pantalla", e);
        }
    }
    
    /**
     * Captura pantalla como array de bytes
     * @return bytes de la captura
     */
    public byte[] capturarPantallaComoBytes() {
        try {
            WebDriver driver = obtenerDriver();
            if (!(driver instanceof TakesScreenshot)) {
                logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("Driver no soporta capturas"));
                return null;
            }
            
            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            return takesScreenshot.getScreenshotAs(OutputType.BYTES);
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error capturando pantalla como bytes: " + e.getMessage()));
            return null;
        }
    }
    
    /**
     * Genera nombre único para archivo de captura
     * @param nombreBase nombre base del archivo
     * @return nombre único con timestamp
     */
    private String generarNombreArchivo(String nombreBase) {
        String timestamp = LocalDateTime.now().format(FORMATO_FECHA);
        String nombreLimpio = limpiarNombreArchivo(nombreBase);
        
        return String.format("%s_%s%s", nombreLimpio, timestamp, EXTENSION_IMAGEN);
    }
    
    /**
     * Limpia el nombre del archivo de caracteres no válidos
     * @param nombre nombre a limpiar
     * @return nombre limpio
     */
    private String limpiarNombreArchivo(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return "captura";
        }
        
        return nombre.trim()
                    .replaceAll("[^a-zA-Z0-9._-]", "_")
                    .replaceAll("_{2,}", "_")
                    .toLowerCase();
    }
    
    /**
     * Limpia capturas antiguas del directorio
     * @param diasAntiguedad días de antigüedad para considerar archivos antiguos
     * @return número de archivos eliminados
     */
    public int limpiarCapturasAntiguas(int diasAntiguedad) {
        try {
            File directorio = new File(directorioBase);
            if (!directorio.exists()) {
                return 0;
            }
            
            File[] archivos = directorio.listFiles((dir, name) -> name.endsWith(EXTENSION_IMAGEN));
            if (archivos == null) {
                return 0;
            }
            
            long tiempoLimite = System.currentTimeMillis() - (diasAntiguedad * 24 * 60 * 60 * 1000L);
            int archivosEliminados = 0;
            
            for (File archivo : archivos) {
                if (archivo.lastModified() < tiempoLimite) {
                    if (archivo.delete()) {
                        archivosEliminados++;
                        logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                            "Captura antigua eliminada: " + archivo.getName()));
                    }
                }
            }
            
            if (archivosEliminados > 0) {
                logger.info(TipoMensaje.INFORMATIVO.formatearMensaje(
                    "Eliminadas " + archivosEliminados + " capturas antiguas"));
            }
            
            return archivosEliminados;
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error limpiando capturas antiguas: " + e.getMessage()));
            return 0;
        }
    }
}