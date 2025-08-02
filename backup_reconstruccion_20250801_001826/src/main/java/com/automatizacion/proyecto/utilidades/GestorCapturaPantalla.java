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
import java.util.Base64;

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
                "Error al crear directorio de capturas: " + e.getMessage()));
        }
    }
    
    /**
     * Captura una pantalla completa y la guarda con un nombre automático
     * @param driver instancia de WebDriver
     * @return ruta del archivo de captura o null si hubo error
     */
    public String capturarPantalla(WebDriver driver) {
        return capturarPantalla(driver, generarNombreArchivoAutomatico());
    }
    
    /**
     * Captura una pantalla con un nombre específico
     * @param driver instancia de WebDriver
     * @param nombreArchivo nombre del archivo (sin extensión)
     * @return ruta del archivo de captura o null si hubo error
     */
    public String capturarPantalla(WebDriver driver, String nombreArchivo) {
        if (driver == null) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Driver es null, no se puede capturar pantalla"));
            return null;
        }
        
        try {
            // Validar que el driver soporte capturas
            if (!(driver instanceof TakesScreenshot)) {
                logger.error(TipoMensaje.ERROR.formatearMensaje(
                    "El driver no soporta capturas de pantalla"));
                return null;
            }
            
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File archivoTemporal = screenshot.getScreenshotAs(OutputType.FILE);
            
            String rutaCompleta = construirRutaArchivo(nombreArchivo);
            File archivoDestino = new File(rutaCompleta);
            
            FileUtils.copyFile(archivoTemporal, archivoDestino);
            
            logger.info(TipoMensaje.EXITO.formatearMensaje(
                "Captura guardada exitosamente: " + rutaCompleta));
            
            return rutaCompleta;
            
        } catch (IOException e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error al guardar captura: " + e.getMessage()));
            return null;
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error inesperado al capturar pantalla: " + e.getMessage()));
            return null;
        }
    }
    
    /**
     * Captura una pantalla para una prueba específica
     * @param driver instancia de WebDriver
     * @param nombrePrueba nombre de la prueba
     * @param descripcion descripción adicional
     * @return ruta del archivo de captura o null si hubo error
     */
    public String capturarPantallaPrueba(WebDriver driver, String nombrePrueba, String descripcion) {
        String nombreArchivo = String.format("%s_%s_%s", 
                                            nombrePrueba, 
                                            descripcion.replaceAll("[^a-zA-Z0-9]", "_"),
                                            LocalDateTime.now().format(FORMATO_FECHA));
        return capturarPantalla(driver, nombreArchivo);
    }
    
    /**
     * Captura una pantalla en caso de error en una prueba
     * @param driver instancia de WebDriver
     * @param nombrePrueba nombre de la prueba que falló
     * @param mensajeError mensaje de error
     * @return ruta del archivo de captura o null si hubo error
     */
    public String capturarPantallaError(WebDriver driver, String nombrePrueba, String mensajeError) {
        String descripcion = "ERROR_" + mensajeError.replaceAll("[^a-zA-Z0-9]", "_");
        if (descripcion.length() > 50) {
            descripcion = descripcion.substring(0, 50);
        }
        
        return capturarPantallaPrueba(driver, nombrePrueba, descripcion);
    }
    
    /**
     * Obtiene una captura como string base64
     * @param driver instancia de WebDriver
     * @return captura en formato base64 o null si hubo error
     */
    public String obtenerCapturaBase64(WebDriver driver) {
        if (driver == null) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Driver es null"));
            return null;
        }
        
        try {
            if (!(driver instanceof TakesScreenshot)) {
                logger.error(TipoMensaje.ERROR.formatearMensaje(
                    "El driver no soporta capturas de pantalla"));
                return null;
            }
            
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            String base64 = screenshot.getScreenshotAs(OutputType.BASE64);
            
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "Captura generada en formato base64"));
            
            return base64;
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error al generar captura base64: " + e.getMessage()));
            return null;
        }
    }
    
    /**
     * Limpiar capturas antiguas del directorio
     * @param diasAntiguedad días de antigüedad para eliminar archivos
     * @return número de archivos eliminados
     */
    public int limpiarCapturasAntiguas(int diasAntiguedad) {
        int archivosEliminados = 0;
        
        try {
            File directorio = new File(directorioBase);
            if (!directorio.exists() || !directorio.isDirectory()) {
                return 0;
            }
            
            File[] archivos = directorio.listFiles((dir, name) -> 
                name.toLowerCase().endsWith(EXTENSION_IMAGEN));
            
            if (archivos == null) {
                return 0;
            }
            
            long tiempoLimite = System.currentTimeMillis() - (diasAntiguedad * 24L * 60L * 60L * 1000L);
            
            for (File archivo : archivos) {
                if (archivo.lastModified() < tiempoLimite) {
                    if (archivo.delete()) {
                        archivosEliminados++;
                        logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                            "Archivo eliminado: " + archivo.getName()));
                    }
                }
            }
            
            if (archivosEliminados > 0) {
                logger.info(TipoMensaje.INFORMATIVO.formatearMensaje(
                    "Archivos de captura eliminados: " + archivosEliminados));
            }
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error al limpiar capturas antiguas: " + e.getMessage()));
        }
        
        return archivosEliminados;
    }
    
    /**
     * Obtiene la ruta completa del directorio de capturas
     * @return ruta del directorio
     */
    public String obtenerDirectorioCapturas() {
        return directorioBase;
    }
    
    /**
     * Verifica si el directorio de capturas existe y es escribible
     * @return true si el directorio está disponible
     */
    public boolean verificarDirectorioDisponible() {
        File directorio = new File(directorioBase);
        return directorio.exists() && directorio.isDirectory() && directorio.canWrite();
    }
    
    // === MÉTODOS PRIVADOS DE UTILIDAD ===
    
    /**
     * Genera un nombre de archivo automático basado en timestamp
     * @return nombre del archivo sin extensión
     */
    private String generarNombreArchivoAutomatico() {
        return "captura_" + LocalDateTime.now().format(FORMATO_FECHA);
    }
    
    /**
     * Construye la ruta completa del archivo de captura
     * @param nombreArchivo nombre del archivo sin extensión
     * @return ruta completa del archivo
     */
    private String construirRutaArchivo(String nombreArchivo) {
        // Limpiar el nombre del archivo
        String nombreLimpio = nombreArchivo.replaceAll("[^a-zA-Z0-9_-]", "_");
        
        // Asegurar que no sea demasiado largo
        if (nombreLimpio.length() > 100) {
            nombreLimpio = nombreLimpio.substring(0, 100);
        }
        
        return directorioBase + File.separator + nombreLimpio + EXTENSION_IMAGEN;
    }
}
