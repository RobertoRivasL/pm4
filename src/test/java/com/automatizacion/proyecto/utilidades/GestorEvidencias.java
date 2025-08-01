package test.java.com.automatizacion.proyecto.utilidades;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.OutputType;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestor centralizado para manejo de evidencias de pruebas.
 * Implementa el patrón Strategy para diferentes tipos de evidencias.
 * 
 * Principios aplicados:
 * - SRP: Solo se encarga del manejo de evidencias
 * - Open/Closed: Fácil extensión para nuevos tipos de evidencias
 * - Dependency Injection: Recibe WebDriver como dependencia
 */
public class GestorEvidencias {
    
    private final WebDriver driver;
    private final String rutaBaseCapturas;
    private final String formatoFecha;
    private final List<String> evidenciasGeneradas;
    private final String carpetaSesion;
    
    private static final String EXTENSION_IMAGEN = ".png";
    private static final String PREFIJO_CAPTURA = "captura_";
    private static final String PREFIJO_ERROR = "error_";
    
    /**
     * Constructor que inicializa el gestor de evidencias.
     */
    public GestorEvidencias(WebDriver driver, String rutaBaseCapturas) {
        this.driver = driver;
        this.rutaBaseCapturas = rutaBaseCapturas != null ? rutaBaseCapturas : "capturas/";
        this.formatoFecha = "yyyy-MM-dd_HH-mm-ss";
        this.evidenciasGeneradas = new ArrayList<>();
        this.carpetaSesion = crearCarpetaSesion();
        
        // Crear directorio base si no existe
        crearDirectorioSiNoExiste(this.rutaBaseCapturas);
        
        log("Gestor de evidencias inicializado - Carpeta: " + carpetaSesion);
    }
    
    /**
     * Captura pantalla con descripción personalizada.
     */
    public String capturarPantalla(String descripcion) {
        try {
            if (!(driver instanceof TakesScreenshot)) {
                throw new RuntimeException("El driver no soporta capturas de pantalla");
            }
            
            // Generar nombre de archivo único
            String nombreArchivo = generarNombreArchivo(descripcion, PREFIJO_CAPTURA);
            String rutaCompleta = carpetaSesion + File.separator + nombreArchivo;
            
            // Tomar captura
            File captura = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File archivoDestino = new File(rutaCompleta);
            
            // Copiar archivo
            FileUtils.copyFile(captura, archivoDestino);
            
            // Registrar evidencia
            evidenciasGeneradas.add(rutaCompleta);
            log("Captura guardada: " + rutaCompleta);
            
            return rutaCompleta;
            
        } catch (IOException e) {
            log("ERROR al capturar pantalla: " + e.getMessage());
            throw new RuntimeException("Fallo al capturar pantalla", e);
        }
    }
    
    /**
     * Captura pantalla con descripción por defecto.
     */
    public String capturarPantalla() {
        return capturarPantalla("captura_automatica");
    }
    
    /**
     * Captura pantalla específica para errores.
     */
    public String capturarPantallaError(String contextoError) {
        try {
            String descripcion = "error_" + contextoError;
            String nombreArchivo = generarNombreArchivo(descripcion, PREFIJO_ERROR);
            String rutaCompleta = carpetaSesion + File.separator + nombreArchivo;
            
            File captura = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File archivoDestino = new File(rutaCompleta);
            
            FileUtils.copyFile(captura, archivoDestino);
            evidenciasGeneradas.add(rutaCompleta);
            
            log("Captura de error guardada: " + rutaCompleta);
            return rutaCompleta;
            
        } catch (Exception e) {
            log("ERROR al capturar pantalla de error: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Captura pantalla final al terminar la prueba.
     */
    public String capturarPantallaFinal() {
        try {
            return capturarPantalla("estado_final");
        } catch (Exception e) {
            log("ADVERTENCIA: No se pudo capturar pantalla final: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Genera reporte simple de las evidencias capturadas.
     */
    public void generarReporteEvidencias() {
        try {
            String nombreReporte = "reporte_evidencias_" + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(formatoFecha)) + ".txt";
            String rutaReporte = carpetaSesion + File.separator + nombreReporte;
            
            StringBuilder contenido = new StringBuilder();
            contenido.append("=== REPORTE DE EVIDENCIAS ===\n");
            contenido.append("Fecha: ").append(LocalDateTime.now()).append("\n");
            contenido.append("Total de evidencias: ").append(evidenciasGeneradas.size()).append("\n\n");
            
            for (int i = 0; i < evidenciasGeneradas.size(); i++) {
                contenido.append(String.format("%d. %s\n", i + 1, evidenciasGeneradas.get(i)));
            }
            
            FileUtils.writeStringToFile(new File(rutaReporte), contenido.toString(), "UTF-8");
            log("Reporte de evidencias generado: " + rutaReporte);
            
        } catch (IOException e) {
            log("ERROR al generar reporte de evidencias: " + e.getMessage());
        }
    }
    
    /**
     * Finaliza el manejo de evidencias y genera reportes.
     */
    public void finalizarReporte() {
        try {
            if (!evidenciasGeneradas.isEmpty()) {
                generarReporteEvidencias();
                log("Sesión de evidencias finalizada. Total capturas: " + evidenciasGeneradas.size());
            } else {
                log("No se generaron evidencias en esta sesión");
            }
        } catch (Exception e) {
            log("ERROR al finalizar reporte: " + e.getMessage());
        }
    }
    
    /**
     * Obtiene la lista de evidencias generadas en la sesión actual.
     */
    public List<String> obtenerEvidenciasGeneradas() {
        return new ArrayList<>(evidenciasGeneradas);
    }
    
    /**
     * Obtiene el número total de evidencias capturadas.
     */
    public int obtenerTotalEvidencias() {
        return evidenciasGeneradas.size();
    }
    
    /**
     * Limpia todas las evidencias de la sesión actual.
     */
    public void limpiarEvidenciasSesion() {
        try {
            File carpeta = new File(carpetaSesion);
            if (carpeta.exists()) {
                FileUtils.deleteDirectory(carpeta);
                log("Evidencias de sesión eliminadas: " + carpetaSesion);
            }
            evidenciasGeneradas.clear();
        } catch (IOException e) {
            log("ERROR al limpiar evidencias: " + e.getMessage());
        }
    }
    
    /**
     * Crea carpeta específica para la sesión actual.
     */
    private String crearCarpetaSesion() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String nombreCarpeta = "sesion_" + timestamp;
        String rutaCarpeta = rutaBaseCapturas + File.separator + nombreCarpeta;
        
        crearDirectorioSiNoExiste(rutaCarpeta);
        return rutaCarpeta;
    }
    
    /**
     * Genera nombre de archivo único para capturas.
     */
    private String generarNombreArchivo(String descripcion, String prefijo) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern(formatoFecha));
        String descripcionLimpia = limpiarNombreArchivo(descripcion);
        return prefijo + timestamp + "_" + descripcionLimpia + EXTENSION_IMAGEN;
    }
    
    /**
     * Limpia el nombre de archivo de caracteres no válidos.
     */
    private String limpiarNombreArchivo(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return "sin_descripcion";
        }
        
        return nombre.trim()
                    .replaceAll("[^a-zA-Z0-9_-]", "_")
                    .replaceAll("_{2,}", "_")
                    .toLowerCase();
    }
    
    /**
     * Crea directorio si no existe.
     */
    private void crearDirectorioSiNoExiste(String ruta) {
        File directorio = new File(ruta);
        if (!directorio.exists()) {
            if (directorio.mkdirs()) {
                log("Directorio creado: " + ruta);
            } else {
                throw new RuntimeException("No se pudo crear el directorio: " + ruta);
            }
        }
    }
    
    /**
     * Verifica si el directorio de capturas es accesible.
     */
    public boolean esDirectorioAccesible() {
        File directorio = new File(carpetaSesion);
        return directorio.exists() && directorio.canWrite();
    }
    
    /**
     * Obtiene el tamaño total de las evidencias en bytes.
     */
    public long obtenerTamanoTotalEvidencias() {
        long tamanoTotal = 0;
        for (String rutaEvidencia : evidenciasGeneradas) {
            File archivo = new File(rutaEvidencia);
            if (archivo.exists()) {
                tamanoTotal += archivo.length();
            }
        }
        return tamanoTotal;
    }
    
    /**
     * Obtiene información detallada de la sesión de evidencias.
     */
    public String obtenerResumenSesion() {
        StringBuilder resumen = new StringBuilder();
        resumen.append("=== RESUMEN SESIÓN EVIDENCIAS ===\n");
        resumen.append("Carpeta sesión: ").append(carpetaSesion).append("\n");
        resumen.append("Total evidencias: ").append(evidenciasGeneradas.size()).append("\n");
        resumen.append("Tamaño total: ").append(formatearTamano(obtenerTamanoTotalEvidencias())).append("\n");
        resumen.append("Directorio accesible: ").append(esDirectorioAccesible()).append("\n");
        
        return resumen.toString();
    }
    
    /**
     * Formatea el tamaño en bytes a formato legible.
     */
    private String formatearTamano(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
    }
    
    /**
     * Logging interno del gestor.
     */
    private void log(String mensaje) {
        System.out.println("[GestorEvidencias] " + mensaje);
    }
}