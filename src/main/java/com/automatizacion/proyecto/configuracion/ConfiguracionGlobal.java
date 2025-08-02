package com.automatizacion.proyecto.configuracion;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase Singleton para manejo de configuración global del proyecto.
 * Implementa el patrón Singleton para asegurar una única instancia
 * de configuración en toda la aplicación.
 * 
 * Principios SOLID aplicados:
 * - SRP: Responsabilidad única de manejar configuración
 * - Singleton: Una sola instancia de configuración
 * - Encapsulación: Propiedades privadas con acceso controlado
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
public class ConfiguracionGlobal {
    
    private static final Logger logger = LoggerFactory.getLogger(ConfiguracionGlobal.class);
    private static ConfiguracionGlobal instancia;
    private final Properties propiedades;
    
    // Constantes para nombres de propiedades
    private static final String ARCHIVO_CONFIG = "config.properties";
    
    // Propiedades de navegador
    private static final String PROP_TIPO_NAVEGADOR = "navegador.tipo";
    private static final String PROP_NAVEGADOR_HEADLESS = "navegador.headless";
    private static final String PROP_VENTANA_MAXIMIZADA = "navegador.ventana.maximizada";
    private static final String PROP_ANCHO_VENTANA = "navegador.ventana.ancho";
    private static final String PROP_ALTO_VENTANA = "navegador.ventana.alto";
    
    // Propiedades de aplicación
    private static final String PROP_URL_BASE = "app.url.base";
    private static final String PROP_URL_LOGIN = "app.url.login";
    private static final String PROP_URL_REGISTRO = "app.url.registro";
    
    // Propiedades de timeouts
    private static final String PROP_TIMEOUT_EXPLICITO = "timeout.explicito";
    private static final String PROP_TIMEOUT_IMPLICITO = "timeout.implicito";
    private static final String PROP_TIMEOUT_CARGA_PAGINA = "timeout.carga.pagina";
    
    // Propiedades de reportes
    private static final String PROP_REPORTES_HABILITADOS = "reportes.habilitados";
    private static final String PROP_CAPTURAS_HABILITADAS = "capturas.habilitadas";
    private static final String PROP_CAPTURAS_SOLO_FALLOS = "capturas.solo.fallos";
    
    // Valores por defecto
    private static final String DEFAULT_TIPO_NAVEGADOR = "CHROME";
    private static final String DEFAULT_HEADLESS = "false";
    private static final String DEFAULT_VENTANA_MAXIMIZADA = "true";
    private static final String DEFAULT_TIMEOUT_EXPLICITO = "15";
    private static final String DEFAULT_TIMEOUT_IMPLICITO = "10";
    private static final String DEFAULT_URL_BASE = "https://example.com";
    
    /**
     * Constructor privado para implementar Singleton.
     */
    private ConfiguracionGlobal() {
        propiedades = new Properties();
        cargarPropiedades();
    }
    
    /**
     * Obtiene la instancia única de la configuración (Singleton).
     */
    public static synchronized ConfiguracionGlobal getInstance() {
        if (instancia == null) {
            instancia = new ConfiguracionGlobal();
        }
        return instancia;
    }
    
    /**
     * Carga las propiedades desde el archivo de configuración.
     */
    private void cargarPropiedades() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(ARCHIVO_CONFIG)) {
            if (inputStream != null) {
                propiedades.load(inputStream);
                logger.info("Archivo de configuración cargado exitosamente: {}", ARCHIVO_CONFIG);
            } else {
                logger.warn("Archivo de configuración no encontrado: {}. Usando valores por defecto.", ARCHIVO_CONFIG);
                cargarValoresPorDefecto();
            }
        } catch (IOException e) {
            logger.error("Error al cargar archivo de configuración: {}", e.getMessage());
            cargarValoresPorDefecto();
        }
    }
    
    /**
     * Carga valores por defecto cuando no se encuentra el archivo de configuración.
     */
    private void cargarValoresPorDefecto() {
        propiedades.setProperty(PROP_TIPO_NAVEGADOR, DEFAULT_TIPO_NAVEGADOR);
        propiedades.setProperty(PROP_NAVEGADOR_HEADLESS, DEFAULT_HEADLESS);
        propiedades.setProperty(PROP_VENTANA_MAXIMIZADA, DEFAULT_VENTANA_MAXIMIZADA);
        propiedades.setProperty(PROP_TIMEOUT_EXPLICITO, DEFAULT_TIMEOUT_EXPLICITO);
        propiedades.setProperty(PROP_TIMEOUT_IMPLICITO, DEFAULT_TIMEOUT_IMPLICITO);
        propiedades.setProperty(PROP_URL_BASE, DEFAULT_URL_BASE);
        
        logger.info("Valores por defecto cargados");
    }
    
    // ===== MÉTODOS GETTER PARA CONFIGURACIÓN DE NAVEGADOR =====
    
    /**
     * Obtiene el tipo de navegador configurado.
     */
    public TipoNavegador getTipoNavegador() {
        String tipo = obtenerPropiedad(PROP_TIPO_NAVEGADOR, DEFAULT_TIPO_NAVEGADOR);
        try {
            return TipoNavegador.valueOf(tipo.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.warn("Tipo de navegador inválido '{}', usando Chrome por defecto", tipo);
            return TipoNavegador.CHROME;
        }
    }
    
    /**
     * Verifica si el navegador debe ejecutarse en modo headless.
     */
    public boolean esNavegadorHeadless() {
        return obtenerPropiedadBoolean(PROP_NAVEGADOR_HEADLESS, Boolean.parseBoolean(DEFAULT_HEADLESS));
    }
    
    /**
     * Verifica si la ventana debe maximizarse.
     */
    public boolean esVentanaMaximizada() {
        return obtenerPropiedadBoolean(PROP_VENTANA_MAXIMIZADA, Boolean.parseBoolean(DEFAULT_VENTANA_MAXIMIZADA));
    }
    
    /**
     * Obtiene el ancho de ventana configurado.
     */
    public int getAnchoVentana() {
        return obtenerPropiedadEntero(PROP_ANCHO_VENTANA, 1920);
    }
    
    /**
     * Obtiene el alto de ventana configurado.
     */
    public int getAltoVentana() {
        return obtenerPropiedadEntero(PROP_ALTO_VENTANA, 1080);
    }
    
    // ===== MÉTODOS GETTER PARA URLs =====
    
    /**
     * Obtiene la URL base de la aplicación.
     */
    public String getUrlBase() {
        return obtenerPropiedad(PROP_URL_BASE, DEFAULT_URL_BASE);
    }
    
    /**
     * Obtiene la URL de login.
     */
    public String getUrlLogin() {
        String urlLogin = obtenerPropiedad(PROP_URL_LOGIN, "");
        if (urlLogin.isEmpty()) {
            return getUrlBase() + "/login";
        }
        return urlLogin;
    }
    
    /**
     * Obtiene la URL de registro.
     */
    public String getUrlRegistro() {
        String urlRegistro = obtenerPropiedad(PROP_URL_REGISTRO, "");
        if (urlRegistro.isEmpty()) {
            return getUrlBase() + "/registro";
        }
        return urlRegistro;
    }
    
    // ===== MÉTODOS GETTER PARA TIMEOUTS =====
    
    /**
     * Obtiene el timeout explícito en segundos.
     */
    public int getTimeoutExplicito() {
        return obtenerPropiedadEntero(PROP_TIMEOUT_EXPLICITO, Integer.parseInt(DEFAULT_TIMEOUT_EXPLICITO));
    }
    
    /**
     * Obtiene el timeout implícito en segundos.
     */
    public int getTimeoutImplicito() {
        return obtenerPropiedadEntero(PROP_TIMEOUT_IMPLICITO, Integer.parseInt(DEFAULT_TIMEOUT_IMPLICITO));
    }
    
    /**
     * Obtiene el timeout de carga de página en segundos.
     */
    public int getTimeoutCargaPagina() {
        return obtenerPropiedadEntero(PROP_TIMEOUT_CARGA_PAGINA, 30);
    }
    
    // ===== MÉTODOS GETTER PARA REPORTES =====
    
    /**
     * Verifica si los reportes están habilitados.
     */
    public boolean sonReportesHabilitados() {
        return obtenerPropiedadBoolean(PROP_REPORTES_HABILITADOS, true);
    }
    
    /**
     * Verifica si las capturas están habilitadas.
     */
    public boolean sonCapturasHabilitadas() {
        return obtenerPropiedadBoolean(PROP_CAPTURAS_HABILITADAS, true);
    }
    
    /**
     * Verifica si las capturas solo deben tomarse en fallos.
     */
    public boolean sonCapturasSoloFallos() {
        return obtenerPropiedadBoolean(PROP_CAPTURAS_SOLO_FALLOS, false);
    }
    
    // ===== MÉTODOS PRIVADOS DE UTILIDAD =====
    
    /**
     * Obtiene una propiedad como String con valor por defecto.
     */
    private String obtenerPropiedad(String clave, String valorDefecto) {
        String valor = propiedades.getProperty(clave, valorDefecto);
        logger.debug("Propiedad '{}': {}", clave, valor);
        return valor;
    }
    
    /**
     * Obtiene una propiedad como boolean con valor por defecto.
     */
    private boolean obtenerPropiedadBoolean(String clave, boolean valorDefecto) {
        String valor = propiedades.getProperty(clave, String.valueOf(valorDefecto));
        try {
            boolean resultado = Boolean.parseBoolean(valor);
            logger.debug("Propiedad boolean '{}': {}", clave, resultado);
            return resultado;
        } catch (Exception e) {
            logger.warn("Error al parsear propiedad boolean '{}' con valor '{}', usando defecto: {}", clave, valor, valorDefecto);
            return valorDefecto;
        }
    }
    
    /**
     * Obtiene una propiedad como entero con valor por defecto.
     */
    private int obtenerPropiedadEntero(String clave, int valorDefecto) {
        String valor = propiedades.getProperty(clave, String.valueOf(valorDefecto));
        try {
            int resultado = Integer.parseInt(valor);
            logger.debug("Propiedad entero '{}': {}", clave, resultado);
            return resultado;
        } catch (NumberFormatException e) {
            logger.warn("Error al parsear propiedad entero '{}' con valor '{}', usando defecto: {}", clave, valor, valorDefecto);
            return valorDefecto;
        }
    }
    
    // ===== MÉTODOS ADICIONALES =====
    
    /**
     * Obtiene una propiedad personalizada.
     */
    public String obtenerPropiedadPersonalizada(String clave) {
        return propiedades.getProperty(clave);
    }
    
    /**
     * Obtiene una propiedad personalizada con valor por defecto.
     */
    public String obtenerPropiedadPersonalizada(String clave, String valorDefecto) {
        return propiedades.getProperty(clave, valorDefecto);
    }
    
    /**
     * Verifica si una propiedad existe.
     */
    public boolean existePropiedad(String clave) {
        return propiedades.containsKey(clave);
    }
    
    /**
     * Obtiene información de debug de la configuración.
     */
    public String obtenerInfoConfiguracion() {
        StringBuilder info = new StringBuilder();
        info.append("=== CONFIGURACIÓN GLOBAL ===\n");
        info.append("Navegador: ").append(getTipoNavegador()).append("\n");
        info.append("Headless: ").append(esNavegadorHeadless()).append("\n");
        info.append("URL Base: ").append(getUrlBase()).append("\n");
        info.append("Timeout Explícito: ").append(getTimeoutExplicito()).append("s\n");
        info.append("Timeout Implícito: ").append(getTimeoutImplicito()).append("s\n");
        info.append("Reportes Habilitados: ").append(sonReportesHabilitados()).append("\n");
        info.append("Capturas Habilitadas: ").append(sonCapturasHabilitadas()).append("\n");
        return info.toString();
    }
    
    /**
     * Enumeración para tipos de navegador soportados.
     */
    public enum TipoNavegador {
        CHROME,
        FIREFOX,
        EDGE,
        SAFARI
    }
}