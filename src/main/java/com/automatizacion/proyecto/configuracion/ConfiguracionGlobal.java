package com.automatizacion.proyecto.configuracion;

import com.automatizacion.proyecto.enums.TipoMensaje;
import com.automatizacion.proyecto.enums.TipoNavegador;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Clase responsable de la gestión de configuración global del proyecto.
 * Implementa el patrón Singleton para garantizar una única instancia.
 * 
 * Principios aplicados:
 * - SRP: Solo maneja configuración
 * - Singleton: Una sola instancia de configuración
 * - DIP: Abstrae la fuente de configuración
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
public class ConfiguracionGlobal {
    
    private static final Logger logger = LoggerFactory.getLogger(ConfiguracionGlobal.class);
    private static ConfiguracionGlobal instancia;
    private static final Object lock = new Object();
    
    // Archivos de configuración
    private static final String ARCHIVO_CONFIGURACION = "config.properties";
    private static final String RUTA_CONFIGURACION_RECURSOS = "/config.properties";
    
    // Propiedades del sistema
    private final Properties propiedades;
    
    // Configuraciones por defecto
    private static final String URL_BASE_DEFECTO = "https://demo.opencart.com";
    private static final TipoNavegador NAVEGADOR_DEFECTO = TipoNavegador.CHROME;
    private static final boolean HEADLESS_DEFECTO = false;
    private static final int TIMEOUT_DEFECTO = 15;
    private static final String DIRECTORIO_CAPTURAS_DEFECTO = "capturas";
    private static final String DIRECTORIO_REPORTES_DEFECTO = "reportes";
    private static final String DIRECTORIO_LOGS_DEFECTO = "logs";
    
    /**
     * Constructor privado para implementar Singleton
     */
    private ConfiguracionGlobal() {
        this.propiedades = new Properties();
        cargarConfiguracion();
    }
    
    /**
     * Obtiene la instancia única de ConfiguracionGlobal (Singleton)
     * @return instancia de ConfiguracionGlobal
     */
    public static ConfiguracionGlobal obtenerInstancia() {
        if (instancia == null) {
            synchronized (lock) {
                if (instancia == null) {
                    instancia = new ConfiguracionGlobal();
                }
            }
        }
        return instancia;
    }
    
    /**
     * Carga la configuración desde diferentes fuentes
     */
    private void cargarConfiguracion() {
        logger.info(TipoMensaje.CONFIGURACION.formatearMensaje(
            "Iniciando carga de configuración"));
        
        // 1. Cargar propiedades del sistema
        propiedades.putAll(System.getProperties());
        
        // 2. Cargar desde archivo de recursos
        cargarDesdeRecursos();
        
        // 3. Cargar desde archivo externo
        cargarDesdeArchivoExterno();
        
        // 4. Cargar variables de entorno
        cargarVariablesEntorno();
        
        // 5. Aplicar configuraciones por defecto
        aplicarConfiguracionesPorDefecto();
        
        logger.info(TipoMensaje.CONFIGURACION.formatearMensaje(
            "Configuración cargada exitosamente"));
        
        logConfiguracionActual();
    }
    
    /**
     * Carga configuración desde recursos internos
     */
    private void cargarDesdeRecursos() {
        try (InputStream input = getClass().getResourceAsStream(RUTA_CONFIGURACION_RECURSOS)) {
            if (input != null) {
                propiedades.load(input);
                logger.info(TipoMensaje.CONFIGURACION.formatearMensaje(
                    "Configuración cargada desde recursos"));
            }
        } catch (IOException e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                "No se pudo cargar configuración desde recursos: " + e.getMessage()));
        }
    }
    
    /**
     * Carga configuración desde archivo externo
     */
    private void cargarDesdeArchivoExterno() {
        Path archivoConfig = Paths.get(ARCHIVO_CONFIGURACION);
        if (Files.exists(archivoConfig)) {
            try (InputStream input = Files.newInputStream(archivoConfig)) {
                propiedades.load(input);
                logger.info(TipoMensaje.CONFIGURACION.formatearMensaje(
                    "Configuración cargada desde archivo externo: " + archivoConfig));
            } catch (IOException e) {
                logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                    "Error al cargar archivo externo: " + e.getMessage()));
            }
        }
    }
    
    /**
     * Carga variables de entorno relevantes
     */
    private void cargarVariablesEntorno() {
        // Variables de entorno que pueden sobrescribir configuración
        String[] variablesRelevantes = {
            "SELENIUM_URL_BASE",
            "SELENIUM_NAVEGADOR",
            "SELENIUM_HEADLESS",
            "SELENIUM_TIMEOUT"
        };
        
        for (String variable : variablesRelevantes) {
            String valor = System.getenv(variable);
            if (valor != null && !valor.trim().isEmpty()) {
                String clave = variable.toLowerCase().replace("selenium_", "");
                propiedades.setProperty(clave, valor);
                logger.info(TipoMensaje.CONFIGURACION.formatearMensaje(
                    "Variable de entorno aplicada: " + variable + " = " + valor));
            }
        }
    }
    
    /**
     * Aplica configuraciones por defecto para propiedades faltantes
     */
    private void aplicarConfiguracionesPorDefecto() {
        aplicarPorDefecto("url.base", URL_BASE_DEFECTO);
        aplicarPorDefecto("navegador.tipo", NAVEGADOR_DEFECTO.n());
        aplicarPorDefecto("navegador.headless", String.valueOf(HEADLESS_DEFECTO));
        aplicarPorDefecto("timeout.explicito", String.valueOf(TIMEOUT_DEFECTO));
        aplicarPorDefecto("timeout.implicito", String.valueOf(TIMEOUT_DEFECTO));
        aplicarPorDefecto("directorio.capturas", DIRECTORIO_CAPTURAS_DEFECTO);
        aplicarPorDefecto("directorio.reportes", DIRECTORIO_REPORTES_DEFECTO);
        aplicarPorDefecto("directorio.logs", DIRECTORIO_LOGS_DEFECTO);
    }
    
    /**
     * Aplica valor por defecto si la propiedad no existe
     */
    private void aplicarPorDefecto(String clave, String valorDefecto) {
        if (!propiedades.containsKey(clave)) {
            propiedades.setProperty(clave, valorDefecto);
        }
    }
    
    /**
     * Registra en log la configuración actual
     */
    private void logConfiguracionActual() {
        logger.info(TipoMensaje.CONFIGURACION.crearSeparador("CONFIGURACIÓN ACTUAL"));
        logger.info(TipoMensaje.CONFIGURACION.formatearMensaje("URL Base: " + obtenerUrlBase()));
        logger.info(TipoMensaje.CONFIGURACION.formatearMensaje("Navegador: " + obtenerTipoNavegador()));
        logger.info(TipoMensaje.CONFIGURACION.formatearMensaje("Modo Headless: " + esNavegadorHeadless()));
        logger.info(TipoMensaje.CONFIGURACION.formatearMensaje("Timeout Explícito: " + obtenerTimeoutExplicito() + "s"));
        logger.info(TipoMensaje.CONFIGURACION.formatearMensaje("Directorio Capturas: " + obtenerDirectorioCapturas()));
    }
    
    // === MÉTODOS PÚBLICOS PARA OBTENER CONFIGURACIONES ===
    
    public String obtenerUrlBase() {
        return propiedades.getProperty("url.base", URL_BASE_DEFECTO);
    }
    
    public TipoNavegador obtenerTipoNavegador() {
        String navegador = propiedades.getProperty("navegador.tipo", NAVEGADOR_DEFECTO.n());
        return TipoNavegador.desdeString(navegador);
    }
    
    public boolean esNavegadorHeadless() {
        return Boolean.parseBoolean(propiedades.getProperty("navegador.headless", String.valueOf(HEADLESS_DEFECTO)));
    }
    
    public int obtenerTimeoutExplicito() {
        return Integer.parseInt(propiedades.getProperty("timeout.explicito", String.valueOf(TIMEOUT_DEFECTO)));
    }
    
    public int obtenerTimeoutImplicito() {
        return Integer.parseInt(propiedades.getProperty("timeout.implicito", String.valueOf(TIMEOUT_DEFECTO)));
    }
    
    public String obtenerDirectorioCapturas() {
        return propiedades.getProperty("directorio.capturas", DIRECTORIO_CAPTURAS_DEFECTO);
    }
    
    public String obtenerDirectorioReportes() {
        return propiedades.getProperty("directorio.reportes", DIRECTORIO_REPORTES_DEFECTO);
    }
    
    public String obtenerDirectorioLogs() {
        return propiedades.getProperty("directorio.logs", DIRECTORIO_LOGS_DEFECTO);
    }
    
    /**
     * Obtiene una propiedad específica con valor por defecto
     * @param clave clave de la propiedad
     * @param valorDefecto valor por defecto si no existe
     * @return valor de la propiedad
     */
    public String obtenerPropiedad(String clave, String valorDefecto) {
        return propiedades.getProperty(clave, valorDefecto);
    }
    
    /**
     * Establece una propiedad dinámicamente
     * @param clave clave de la propiedad
     * @param valor valor a establecer
     */
    public void establecerPropiedad(String clave, String valor) {
        propiedades.setProperty(clave, valor);
        logger.info(TipoMensaje.CONFIGURACION.formatearMensaje(
            "Propiedad establecida: " + clave + " = " + valor));
    }
    
    /**
     * Verifica si una propiedad existe
     * @param clave clave a verificar
     * @return true si la propiedad existe
     */
    public boolean existePropiedad(String clave) {
        return propiedades.containsKey(clave);
    }
    
    /**
     * Obtiene todas las propiedades (copia defensiva)
     * @return copia de las propiedades
     */
    public Properties obtenerTodasLasPropiedades() {
        Properties copia = new Properties();
        copia.putAll(propiedades);
        return copia;
    }
    
    /**
     * Reinicia la configuración (útil para pruebas)
     */
    public static void reiniciarInstancia() {
        synchronized (lock) {
            instancia = null;
        }
        logger.info(TipoMensaje.CONFIGURACION.formatearMensaje("Instancia de configuración reiniciada"));
    }
}
