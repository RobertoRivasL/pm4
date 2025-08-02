package com.automatizacion.proyecto.utilidades;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.OutputType;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
public class GestorEvidencias {
    
    private static final Logger logger = LoggerFactory.getLogger(GestorEvidencias.class);
    
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
     * @param driver WebDriver para capturar pantallas
     * @param rutaBaseCapturas ruta base donde guardar capturas
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
     * Constructor simplificado con ruta por defecto
     * @param driver WebDriver para capturar pantallas
     */
    public GestorEvidencias(WebDriver driver) {
        this(driver, "capturas/");
    }
    
    // === MÉTODOS PRINCIPALES DE CAPTURA ===
    
    /**
     * Captura pantalla con descripción personalizada.
     * @param descripcion descripción de la captura
     * @return ruta del archivo generado
     */
    public String capturarPantalla(String descripcion) {
        try {
            validarDriver();
            
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
     * @return ruta del archivo generado
     */
    public String capturarPantalla() {
        return capturarPantalla("captura_automatica");
    }
    
    /**
     * Captura pantalla específica para errores.
     * @param contextoError contexto del error
     * @return ruta del archivo generado o null si falló
     */
    public String capturarPantallaError(String contextoError) {
        return capturarPantallaError(contextoError, "Error no especificado");
    }
    
    /**
     * Captura pantalla específica para errores con mensaje detallado.
     * @param contextoError contexto del error
     * @param mensajeError mensaje detallado del error
     * @return ruta del archivo generado o null si falló
     */
    public String capturarPantallaError(String contextoError, String mensajeError) {
        try {
            validarDriver();
            
            String descripcion = "error_" + contextoError;
            String nombreArchivo = generarNombreArchivo(descripcion, PREFIJO_ERROR);
            String rutaCompleta = carpetaSesion + File.separator + nombreArchivo;
            
            File captura = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File archivoDestino = new File(rutaCompleta);
            
            FileUtils.copyFile(captura, archivoDestino);
            evidenciasGeneradas.add(rutaCompleta);
            
            log("Captura de error guardada: " + rutaCompleta);
            log("Mensaje del error: " + mensajeError);
            
            return rutaCompleta;
            
        } catch (Exception e) {
            log("ERROR al capturar pantalla de error: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Captura pantalla final al terminar la prueba.
     * @return ruta del archivo generado o null si falló
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
     * Captura pantalla para un paso específico de la prueba
     * @param nombrePaso nombre del paso
     * @return ruta del archivo generado
     */
    public String capturarPantallaPaso(String nombrePaso) {
        String descripcion = "paso_" + limpiarNombreArchivo(nombrePaso);
        return capturarPantalla(descripcion);
    }
    
    // === MÉTODOS DE GESTIÓN DE REPORTES ===
    
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
            contenido.append("Total de evidencias: ").append(evidenciasGeneradas.size()).append("\n");
            contenido.append("Carpeta de sesión: ").append(carpetaSesion).append("\n\n");
            
            for (int i = 0; i < evidenciasGeneradas.size(); i++) {
                String rutaEvidencia = evidenciasGeneradas.get(i);
                File archivo = new File(rutaEvidencia);
                contenido.append(String.format("%d. %s (%.2f KB)\n", 
                    i + 1, 
                    archivo.getName(),
                    archivo.exists() ? archivo.length() / 1024.0 : 0));
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
                mostrarEstadisticasSesion();
            } else {
                log("No se generaron evidencias en esta sesión");
            }
        } catch (Exception e) {
            log("ERROR al finalizar reporte: " + e.getMessage());
        }
    }
    
    /**
     * Muestra estadísticas de la sesión de evidencias
     */
    private void mostrarEstadisticasSesion() {
        long tamanoTotal = obtenerTamanoTotalEvidencias();
        log("Estadísticas de sesión:");
        log("- Total evidencias: " + evidenciasGeneradas.size());
        log("- Tamaño total: " + String.format("%.2f MB", tamanoTotal / (1024.0 * 1024.0)));
        log("- Carpeta: " + carpetaSesion);
    }
    
    // === MÉTODOS DE UTILIDAD Y INFORMACIÓN ===
    
    /**
     * Obtiene la lista de evidencias generadas en la sesión actual.
     * @return lista de rutas de evidencias
     */
    public List<String> obtenerEvidenciasGeneradas() {
        return new ArrayList<>(evidenciasGeneradas);
    }
    
    /**
     * Obtiene el número total de evidencias capturadas.
     * @return cantidad de evidencias
     */
    public int obtenerCantidadEvidencias() {
        return evidenciasGeneradas.size();
    }
    
    /**
     * Obtiene el tamaño total de las evidencias en bytes.
     * @return tamaño total en bytes
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
     * @return información de la sesión
     */
    public String obtenerInformacionSesion() {
        StringBuilder info = new StringBuilder();
        info.append("=== INFORMACIÓN DE SESIÓN DE EVIDENCIAS ===\n");
        info.append("Carpeta de sesión: ").append(carpetaSesion).append("\n");
        info.append("Total de evidencias: ").append(evidenciasGeneradas.size()).append("\n");
        info.append("Tamaño total: ").append(String.format("%.2f KB", obtenerTamanoTotalEvidencias() / 1024.0)).append("\n");
        info.append("Directorio accesible: ").append(esDirectorioAccesible()).append("\n");
        
        if (!evidenciasGeneradas.isEmpty()) {
            info.append("\nEvidencias generadas:\n");
            for (int i = 0; i < evidenciasGeneradas.size(); i++) {
                String ruta = evidenciasGeneradas.get(i);
                File archivo = new File(ruta);
                info.append(String.format("  %d. %s\n", i + 1, archivo.getName()));
            }
        }
        
        return info.toString();
    }
    
    /**
     * Verifica si el directorio de capturas es accesible.
     * @return true si es accesible
     */
    public boolean esDirectorioAccesible() {
        File directorio = new File(carpetaSesion);
        return directorio.exists() && directorio.canWrite();
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
    
    // === MÉTODOS PRIVADOS DE APOYO ===
    
    /**
     * Valida que el driver esté disponible y soporte capturas
     */
    private void validarDriver() {
        if (driver == null) {
            throw new RuntimeException("Driver es null, no se puede capturar pantalla");
        }
        
        if (!(driver instanceof TakesScreenshot)) {
            throw new RuntimeException("El driver no soporta capturas de pantalla");
        }
    }
    
    /**
     * Crea carpeta específica para la sesión actual.
     * @return ruta de la carpeta creada
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
     * @param descripcion descripción de la captura
     * @param prefijo prefijo del archivo
     * @return nombre de archivo único
     */
    private String generarNombreArchivo(String descripcion, String prefijo) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern(formatoFecha));
        String descripcionLimpia = limpiarNombreArchivo(descripcion);
        return prefijo + timestamp + "_" + descripcionLimpia + EXTENSION_IMAGEN;
    }
    
    /**
     * Limpia el nombre de archivo de caracteres no válidos.
     * @param nombre nombre a limpiar
     * @return nombre limpio
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
     * @param ruta ruta del directorio a crear
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
     * Método de logging simplificado
     * @param mensaje mensaje a loggear
     */
    private void log(String mensaje) {
        logger.info(mensaje);
        // También imprimir en consola para debugging inmediato
        System.out.println("[GestorEvidencias] " + mensaje);
    }
    
    // === MÉTODOS ESTÁTICOS DE UTILIDAD ===
    
    /**
     * Crea un gestor de evidencias con configuración por defecto
     * @param driver WebDriver a usar
     * @return nueva instancia del gestor
     */
    public static GestorEvidencias crearPorDefecto(WebDriver driver) {
        return new GestorEvidencias(driver, "capturas/");
    }
    
    /**
     * Crea un gestor de evidencias para una prueba específica
     * @param driver WebDriver a usar
     * @param nombrePrueba nombre de la prueba
     * @return nueva instancia del gestor
     */
    public static GestorEvidencias crearParaPrueba(WebDriver driver, String nombrePrueba) {
        String rutaEspecifica = "capturas/" + nombrePrueba.replaceAll("[^a-zA-Z0-9]", "_");
        return new GestorEvidencias(driver, rutaEspecifica);
    }
}