package com.automatizacion.proyecto.utilidades;

import com.automatizacion.proyecto.configuracion.ConfiguracionGlobal;
import com.automatizacion.proyecto.enums.TipoMensaje;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Gestor para capturas de pantalla optimizado para formularios de login.
 * 
 * SOLUCIONES IMPLEMENTADAS:
 * - Esperas inteligentes antes de capturar
 * - Validación de elementos visibles en formularios
 * - Capturas específicas para login exitoso/fallido
 * - Integración con Allure Reports
 * 
 * Principios aplicados:
 * - Single Responsibility: Solo maneja capturas
 * - Open/Closed: Extensible para nuevos tipos de captura
 * - Dependency Inversion: Depende de abstracciones (WebDriver)
 * 
 * @author Antonio B. Arriagada LL. (anarriag@gmail.com)
 * @author Dante Escalona Bustos (Jacobo.bustos.22@gmail.com)  
 * @author Roberto Rivas Lopez (umancl@gmail.com)
 * @version 1.0
 */
public final class GestorCapturaPantalla {
    
    private static final Logger logger = LoggerFactory.getLogger(GestorCapturaPantalla.class);
    private static final ConfiguracionGlobal config = ConfiguracionGlobal.obtenerInstancia();
    
    // Constantes para timing de capturas
    private static final int ESPERA_ANTES_CAPTURA_MS = 1000;
    private static final int ESPERA_FORMULARIO_MS = 2000;
    private static final int TIMEOUT_ELEMENTO_SEG = 5;
    
    // Constructor privado para clase utilitaria
    private GestorCapturaPantalla() {
        throw new UnsupportedOperationException("Clase utilitaria - no debe ser instanciada");
    }
    
    /**
     * Captura pantalla para formulario de login ANTES de enviar
     * SOLUCIÓN ESPECÍFICA: Espera a que el formulario esté lleno y visible
     * 
     * @param driver WebDriver activo
     * @param nombreArchivo nombre base del archivo
     * @param formularioSelector selector del formulario de login
     * @return ruta del archivo guardado
     */
    public static String capturarFormularioLogin(WebDriver driver, String nombreArchivo, String formularioSelector) {
        logger.info(TipoMensaje.PASO_PRUEBA.formatearMensaje(
            "Capturando formulario de login lleno: " + nombreArchivo));
        
        try {
            // CRÍTICO: Esperar a que el formulario esté presente y visible
            WebDriverWait espera = new WebDriverWait(driver, java.time.Duration.ofSeconds(TIMEOUT_ELEMENTO_SEG));
            
            if (formularioSelector != null && !formularioSelector.isEmpty()) {
                WebElement formulario = espera.until(
                    ExpectedConditions.visibilityOfElementLocated(
                        org.openqa.selenium.By.cssSelector(formularioSelector)));
                
                // Esperar un momento para que se rendericen los datos
                Thread.sleep(ESPERA_FORMULARIO_MS);
                
                logger.debug(TipoMensaje.DEBUG.formatearMensaje("Formulario visible y datos cargados"));
            } else {
                // Espera genérica si no hay selector específico
                Thread.sleep(ESPERA_ANTES_CAPTURA_MS);
            }
            
            // Realizar la captura
            return capturarPantallaCompleta(driver, nombreArchivo + "_formulario_lleno");
            
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                "Error al capturar formulario específico, usando captura general: " + e.getMessage()));
            return capturarPantallaCompleta(driver, nombreArchivo + "_formulario_error");
        }
    }
    
    /**
     * Captura pantalla para resultado de login (exitoso o fallido)
     * SOLUCIÓN ESPECÍFICA: Espera a elementos de confirmación
     * 
     * @param driver WebDriver activo
     * @param nombreArchivo nombre base del archivo
     * @param esExitoso true si el login fue exitoso
     * @param selectorResultado selector del elemento que confirma el resultado
     * @return ruta del archivo guardado
     */
    public static String capturarResultadoLogin(WebDriver driver, String nombreArchivo, boolean esExitoso, String selectorResultado) {
        String sufijo = esExitoso ? "_login_exitoso" : "_login_fallido";
        
        logger.info(TipoMensaje.PASO_PRUEBA.formatearMensaje(
            "Capturando resultado de login " + (esExitoso ? "exitoso" : "fallido") + ": " + nombreArchivo));
        
        try {
            // Esperar a que aparezca el elemento de resultado
            if (selectorResultado != null && !selectorResultado.isEmpty()) {
                WebDriverWait espera = new WebDriverWait(driver, java.time.Duration.ofSeconds(TIMEOUT_ELEMENTO_SEG));
                
                try {
                    espera.until(ExpectedConditions.presenceOfElementLocated(
                        org.openqa.selenium.By.cssSelector(selectorResultado)));
                    
                    // Espera adicional para animaciones/transiciones
                    Thread.sleep(ESPERA_ANTES_CAPTURA_MS);
                    
                    logger.debug(TipoMensaje.DEBUG.formatearMensaje("Elemento de resultado encontrado"));
                } catch (Exception e) {
                    logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                        "No se encontró elemento de resultado específico, capturando estado actual"));
                }
            }
            
            return capturarPantallaCompleta(driver, nombreArchivo + sufijo);
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error al capturar resultado de login: " + e.getMessage()));
            return capturarPantallaCompleta(driver, nombreArchivo + sufijo + "_error");
        }
    }
    
    /**
     * Captura pantalla completa con manejo de errores robusto
     * MEJORADO: Mejor manejo de archivos y directorios
     * 
     * @param driver WebDriver activo
     * @param nombreArchivo nombre del archivo sin extensión
     * @return ruta completa del archivo guardado
     */
    public static String capturarPantallaCompleta(WebDriver driver, String nombreArchivo) {
        try {
            // Validar que el driver esté activo
            if (driver == null) {
                logger.error(TipoMensaje.ERROR.formatearMensaje("Driver es null, no se puede capturar"));
                return null;
            }
            
            // Generar nombre único con timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));
            String nombreCompleto = String.format("%s_%s.%s", 
                limpiarNombreArchivo(nombreArchivo), 
                timestamp, 
                config.obtenerFormatoCaptura().toLowerCase());
            
            // Crear directorio si no existe
            Path directorioBase = Paths.get(config.obtenerRutaCapturas());
            Files.createDirectories(directorioBase);
            
            // Ruta completa del archivo
            Path rutaCompleta = directorioBase.resolve(nombreCompleto);
            
            // Tomar la captura
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            byte[] screenshotBytes = screenshot.getScreenshotAs(OutputType.BYTES);
            
            // Guardar archivo
            Files.write(rutaCompleta, screenshotBytes);
            
            // Adjuntar a Allure Report
            adjuntarAAllure(screenshotBytes, nombreCompleto);
            
            logger.info(TipoMensaje.EXITO.formatearMensaje(
                "Captura guardada: " + rutaCompleta.toAbsolutePath()));
            
            return rutaCompleta.toAbsolutePath().toString();
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error al capturar pantalla: " + e.getMessage()), e);
            return null;
        }
    }
    
    /**
     * Captura pantalla en caso de error/excepción
     * 
     * @param driver WebDriver activo
     * @param nombrePrueba nombre de la prueba que falló
     * @param excepcion excepción que causó el error
     * @return ruta del archivo de captura
     */
    public static String capturarPantallaError(WebDriver driver, String nombrePrueba, Exception excepcion) {
        String nombreArchivo = String.format("ERROR_%s_%s", 
            limpiarNombreArchivo(nombrePrueba),
            excepcion.getClass().getSimpleName());
        
        logger.error(TipoMensaje.ERROR.formatearMensaje(
            "Capturando pantalla de error para: " + nombrePrueba));
        
        String ruta = capturarPantallaCompleta(driver, nombreArchivo);
        
        // Log adicional del error
        logger.error(TipoMensaje.ERROR.formatearMensaje(
            "Detalles del error: " + excepcion.getMessage()));
        
        return ruta;
    }
    
    /**
     * Adjunta captura de pantalla a Allure Report
     * 
     * @param screenshotBytes bytes de la captura
     * @param nombreArchivo nombre del archivo
     */
    @Attachment(value = "Captura de Pantalla: {nombreArchivo}", type = "image/png")
    private static byte[] adjuntarAAllure(byte[] screenshotBytes, String nombreArchivo) {
        try {
            // Adjuntar como attachment de Allure
            Allure.addAttachment(nombreArchivo, "image/png", new ByteArrayInputStream(screenshotBytes), "png");
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Captura adjuntada a Allure: " + nombreArchivo));
            return screenshotBytes;
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                "Error al adjuntar a Allure: " + e.getMessage()));
            return screenshotBytes;
        }
    }
    
    /**
     * Limpia el nombre del archivo removiendo caracteres no válidos
     * 
     * @param nombre nombre original
     * @return nombre limpio para archivo
     */
    private static String limpiarNombreArchivo(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return "captura_sin_nombre";
        }
        
        // Remover caracteres no válidos para nombres de archivo
        return nombre.replaceAll("[^a-zA-Z0-9._-]", "_")
                   .replaceAll("_{2,}", "_")  // Múltiples underscores a uno solo
                   .replaceAll("^_|_$", ""); // Remover underscores al inicio/final
    }
    
    /**
     * Captura específica para elementos de formulario
     * Útil para capturar solo el formulario de login lleno
     * 
     * @param driver WebDriver activo
     * @param elemento WebElement del formulario
     * @param nombreArchivo nombre del archivo
     * @return ruta del archivo guardado
     */
    public static String capturarElemento(WebDriver driver, WebElement elemento, String nombreArchivo) {
        try {
            if (elemento == null) {
                logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                    "Elemento es null, capturando pantalla completa"));
                return capturarPantallaCompleta(driver, nombreArchivo + "_elemento_null");
            }
            
            // Esperar a que el elemento sea visible
            if (!elemento.isDisplayed()) {
                logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                    "Elemento no visible, capturando pantalla completa"));
                return capturarPantallaCompleta(driver, nombreArchivo + "_elemento_no_visible");
            }
            
            // Scroll al elemento si es necesario
            ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView(true);", elemento);
            
            Thread.sleep(500); // Esperar a que termine el scroll
            
            // Capturar el elemento específico
            byte[] screenshotBytes = elemento.getScreenshotAs(OutputType.BYTES);
            
            // Generar nombre y ruta
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String nombreCompleto = String.format("%s_elemento_%s.%s", 
                limpiarNombreArchivo(nombreArchivo), 
                timestamp, 
                config.obtenerFormatoCaptura().toLowerCase());
            
            // Crear directorio y guardar
            Path directorioBase = Paths.get(config.obtenerRutaCapturas());
            Files.createDirectories(directorioBase);
            Path rutaCompleta = directorioBase.resolve(nombreCompleto);
            Files.write(rutaCompleta, screenshotBytes);
            
            // Adjuntar a Allure
            adjuntarAAllure(screenshotBytes, nombreCompleto);
            
            logger.info(TipoMensaje.EXITO.formatearMensaje(
                "Captura de elemento guardada: " + rutaCompleta.toAbsolutePath()));
            
            return rutaCompleta.toAbsolutePath().toString();
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error al capturar elemento: " + e.getMessage()));
            // Fallback a captura completa
            return capturarPantallaCompleta(driver, nombreArchivo + "_elemento_error");
        }
    }
    
    /**
     * Secuencia completa de capturas para login
     * SOLUCIÓN INTEGRAL: Captura todo el flujo de login correctamente
     * 
     * @param driver WebDriver activo
     * @param nombreCaso nombre del caso de prueba
     * @param formularioSelector selector del formulario
     * @param resultadoSelector selector del resultado
     * @param loginExitoso si el login fue exitoso
     * @return objeto con las rutas de todas las capturas
     */
    public static ResultadoCapturas capturarFlujoLoginCompleto(
            WebDriver driver, 
            String nombreCaso, 
            String formularioSelector, 
            String resultadoSelector, 
            boolean loginExitoso) {
        
        logger.info(TipoMensaje.PASO_PRUEBA.formatearMensaje(
            "Iniciando secuencia de capturas para: " + nombreCaso));
        
        ResultadoCapturas resultado = new ResultadoCapturas();
        
        try {
            // 1. Capturar formulario lleno (ANTES de enviar)
            resultado.rutaFormulario = capturarFormularioLogin(driver, nombreCaso, formularioSelector);
            
            // 2. Pequeña pausa para procesar el envío
            Thread.sleep(1000);
            
            // 3. Capturar resultado (DESPUÉS de enviar)
            resultado.rutaResultado = capturarResultadoLogin(driver, nombreCaso, loginExitoso, resultadoSelector);
            
            // 4. Captura adicional de estado final
            resultado.rutaEstadoFinal = capturarPantallaCompleta(driver, nombreCaso + "_estado_final");
            
            logger.info(TipoMensaje.EXITO.formatearMensaje(
                "Secuencia de capturas completada para: " + nombreCaso));
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error en secuencia de capturas: " + e.getMessage()));
            resultado.rutaError = capturarPantallaError(driver, nombreCaso, e);
        }
        
        return resultado;
    }
    
    /**
     * Limpia capturas antiguas para mantener el espacio en disco
     * 
     * @param diasAntiguedad días de antigüedad para eliminar
     * @return número de archivos eliminados
     */
    public static int limpiarCapturasAntiguas(int diasAntiguedad) {
        try {
            Path directorioBase = Paths.get(config.obtenerRutaCapturas());
            if (!Files.exists(directorioBase)) {
                return 0;
            }
            
            long tiempoLimite = System.currentTimeMillis() - (diasAntiguedad * 24L * 60L * 60L * 1000L);
            int archivosEliminados = 0;
            
            try (var stream = Files.walk(directorioBase)) {
                var archivosAntiguos = stream
                    .filter(Files::isRegularFile)
                    .filter(path -> {
                        try {
                            return Files.getLastModifiedTime(path).toMillis() < tiempoLimite;
                        } catch (IOException e) {
                            return false;
                        }
                    })
                    .toList();
                
                for (Path archivo : archivosAntiguos) {
                    try {
                        Files.delete(archivo);
                        archivosEliminados++;
                    } catch (IOException e) {
                        logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                            "No se pudo eliminar archivo: " + archivo));
                    }
                }
            }
            
            logger.info(TipoMensaje.INFORMATIVO.formatearMensaje(
                "Limpieza completada. Archivos eliminados: " + archivosEliminados));
            
            return archivosEliminados;
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error al limpiar capturas antiguas: " + e.getMessage()));
            return 0;
        }
    }
    
    /**
     * Clase interna para el resultado de capturas múltiples
     */
    public static class ResultadoCapturas {
        public String rutaFormulario;
        public String rutaResultado;
        public String rutaEstadoFinal;
        public String rutaError;
        
        public boolean todasExitosas() {
            return rutaFormulario != null && rutaResultado != null && rutaEstadoFinal != null;
        }
        
        public String obtenerResumen() {
            StringBuilder resumen = new StringBuilder("Capturas realizadas:\n");
            if (rutaFormulario != null) resumen.append("- Formulario: ").append(rutaFormulario).append("\n");
            if (rutaResultado != null) resumen.append("- Resultado: ").append(rutaResultado).append("\n");
            if (rutaEstadoFinal != null) resumen.append("- Estado final: ").append(rutaEstadoFinal).append("\n");
            if (rutaError != null) resumen.append("- Error: ").append(rutaError).append("\n");
            return resumen.toString();
        }
    }
} // ← ESTA ES LA LLAVE QUE FALTABA