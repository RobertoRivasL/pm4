package com.automatizacion.proyecto.configuracion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Clase Singleton que maneja la configuración global de la aplicación.
 * Implementa el patrón Singleton para garantizar una única instancia
 * y centralizar la gestión de propiedades de configuración.
 * 
 * Principios aplicados:
 * - Singleton: Una sola instancia de configuración
 * - Encapsulación: Propiedades privadas con acceso controlado
 * - Responsabilidad Única: Solo maneja configuración
 * 
 * @author Roberto Rivas Lopez
 * @author Antonio B. Arriagada LL.
 * @author Dante Escalona Bustos
 * @version 1.0
 */
public final class ConfiguracionGlobal {
    
    private static final Logger logger = LoggerFactory.getLogger(ConfiguracionGlobal.class);
    
    // === SINGLETON INSTANCE ===
    private static volatile ConfiguracionGlobal instancia;
    private static final Object LOCK = new Object();
    
    // === PROPIEDADES DE CONFIGURACIÓN ===
    private final Properties propiedades;
    
    // === CONSTANTES DE CONFIGURACIÓN ===
    private static final String ARCHIVO_CONFIG = "config.properties";
    private static final String ARCHIVO_CONFIG_BACKUP = "config-default.properties";
    
    // === ENUMS INTERNOS ===
    
    /**
     * Enum para tipos de navegador (definido internamente)
     */
    public enum TipoNavegador {
        CHROME("Chrome", "chrome"),
        FIREFOX("Firefox", "firefox"),
        EDGE("Edge", "edge"),
        SAFARI("Safari", "safari");
        
        private final String nombreCompleto;
        private final String identificador;
        
        TipoNavegador(String nombreCompleto, String identificador) {
            this.nombreCompleto = nombreCompleto;
            this.identificador = identificador;
        }
        
        public String getNombreCompleto() {
            return nombreCompleto;
        }
        
        public String getIdentificador() {
            return identificador;
        }
    }
    
    // === CLAVES DE PROPIEDADES ===
    private static final class ClavesPropiedades {
        // Navegador
        static final String NAVEGADOR_TIPO = "navegador.tipo";
        static final String NAVEGADOR_HEADLESS = "navegador.headless";
        static final String NAVEGADOR_MAXIMIZAR = "navegador.maximizar";
        static final String NAVEGADOR_VENTANA_ANCHO = "navegador.ventana.ancho";
        static final String NAVEGADOR_VENTANA_ALTO = "navegador.ventana.alto";
        
        // URLs
        static final String URL_BASE = "url.base";
        static final String URL_LOGIN = "url.login";
        static final String URL_REGISTRO = "url.registro";
        
        // Timeouts
        static final String TIMEOUT_IMPLICITO = "timeout.implicito";
        static final String TIMEOUT_EXPLICITO = "timeout.explicito";
        static final String TIMEOUT_CARGA_PAGINA = "timeout.carga.pagina";
        
        // Reportes y capturas
        static final String RUTA_CAPTURAS = "ruta.capturas";
        static final String RUTA_REPORTES = "ruta.reportes";
        static final String FORMATO_CAPTURA = "formato.captura";
        
        // Logs
        static final String LOG_NIVEL = "log.nivel";
        static final String LOG_ARCHIVO = "log.archivo";
        
        // Datos de prueba
        static final String RUTA_DATOS_CSV = "ruta.datos.csv";
        static final String RUTA_DATOS_EXCEL = "ruta.datos.excel";
        
        private ClavesPropiedades() {
            // Clase de constantes - constructor privado
        }
    }
    
    /**
     * Constructor privado para implementar Singleton
     * Carga las propiedades de configuración desde el archivo
     */
    private ConfiguracionGlobal() {
        this.propiedades = new Properties();
        cargarPropiedades();
        validarConfiguracionObligatoria();
        logConfiguracionCargada();
    }
    
    /**
     * Obtiene la instancia única de ConfiguracionGlobal (Thread-safe)
     * Implementa Double-Checked Locking para mejor rendimiento
     * 
     * @return instancia única de ConfiguracionGlobal
     */
    public static ConfiguracionGlobal obtenerInstancia() {
        if (instancia == null) {
            synchronized (LOCK) {
                if (instancia == null) {
                    instancia = new ConfiguracionGlobal();
                }
            }
        }
        return instancia;
    }
    
    // === MÉTODOS DE CONFIGURACIÓN DEL NAVEGADOR ===
    
    /**
     * Obtiene el tipo de navegador configurado
     * @return tipo de navegador (CHROME, FIREFOX, etc.)
     */
    public TipoNavegador obtenerTipoNavegador() {
        String tipoStr = obtenerPropiedad(ClavesPropiedades.NAVEGADOR_TIPO, "CHROME");
        try {
            return TipoNavegador.valueOf(tipoStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.warn("Tipo de navegador inválido: {}. Usando CHROME por defecto.", tipoStr);
            return TipoNavegador.CHROME;
        }
    }
    
    /**
     * Indica si el navegador debe ejecutarse en modo headless
     * @return true si debe ejecutarse en headless
     */
    public boolean esNavegadorHeadless() {
        return Boolean.parseBoolean(obtenerPropiedad(ClavesPropiedades.NAVEGADOR_HEADLESS, "false"));
    }
    
    /**
     * Indica si la ventana del navegador debe maximizarse
     * @return true si debe maximizarse
     */
    public boolean debeMaximizarNavegador() {
        return Boolean.parseBoolean(obtenerPropiedad(ClavesPropiedades.NAVEGADOR_MAXIMIZAR, "true"));
    }
    
    /**
     * Obtiene el ancho de ventana configurado
     * @return ancho en píxeles
     */
    public int obtenerAnchoVentana() {
        try {
            return Integer.parseInt(obtenerPropiedad(ClavesPropiedades.NAVEGADOR_VENTANA_ANCHO, "1920"));
        } catch (NumberFormatException e) {
            logger.warn("Ancho de ventana inválido, usando 1920 por defecto");
            return 1920;
        }
    }
    
    /**
     * Obtiene el alto de ventana configurado
     * @return alto en píxeles
     */
    public int obtenerAltoVentana() {
        try {
            return Integer.parseInt(obtenerPropiedad(ClavesPropiedades.NAVEGADOR_VENTANA_ALTO, "1080"));
        } catch (NumberFormatException e) {
            logger.warn("Alto de ventana inválido, usando 1080 por defecto");
            return 1080;
        }
    }
    
    // === MÉTODOS DE URLs ===
    
    /**
     * Obtiene la URL base de la aplicación
     * @return URL base
     */
    public String obtenerUrlBase() {
        return obtenerPropiedad(ClavesPropiedades.URL_BASE, "https://practice.expandtesting.com");
    }
    
    /**
     * Obtiene la URL de la página de login
     * @return URL de login
     */
    public String obtenerUrlLogin() {
        String urlLogin = obtenerPropiedad(ClavesPropiedades.URL_LOGIN, "/login");
        return construirUrlCompleta(urlLogin);
    }
    
    /**
     * Obtiene la URL de la página de registro
     * @return URL de registro
     */
    public String obtenerUrlRegistro() {
        String urlRegistro = obtenerPropiedad(ClavesPropiedades.URL_REGISTRO, "/register");
        return construirUrlCompleta(urlRegistro);
    }
    
    // === MÉTODOS DE TIMEOUTS ===
    
    /**
     * Obtiene el timeout implícito en segundos
     * @return timeout implícito
     */
    public int obtenerTimeoutImplicito() {
        try {
            return Integer.parseInt(obtenerPropiedad(ClavesPropiedades.TIMEOUT_IMPLICITO, "10"));
        } catch (NumberFormatException e) {
            logger.warn("Timeout implícito inválido, usando 10s por defecto");
            return 10;
        }
    }
    
    /**
     * Obtiene el timeout explícito en segundos
     * @return timeout explícito
     */
    public int obtenerTimeoutExplicito() {
        try {
            return Integer.parseInt(obtenerPropiedad(ClavesPropiedades.TIMEOUT_EXPLICITO, "15"));
        } catch (NumberFormatException e) {
            logger.warn("Timeout explícito inválido, usando 15s por defecto");
            return 15;
        }
    }
    
    /**
     * Obtiene el timeout de carga de página en segundos
     * @return timeout de carga
     */
    public int obtenerTimeoutCargaPagina() {
        try {
            return Integer.parseInt(obtenerPropiedad(ClavesPropiedades.TIMEOUT_CARGA_PAGINA, "30"));
        } catch (NumberFormatException e) {
            logger.warn("Timeout de carga inválido, usando 30s por defecto");
            return 30;
        }
    }
    
    // === MÉTODOS DE RUTAS ===
    
    /**
     * Obtiene la ruta donde se guardan las capturas de pantalla
     * @return ruta de capturas
     */
    public String obtenerRutaCapturas() {
        return obtenerPropiedad(ClavesPropiedades.RUTA_CAPTURAS, "target/capturas/");
    }
    
    /**
     * Obtiene la ruta donde se guardan los reportes
     * @return ruta de reportes
     */
    public String obtenerRutaReportes() {
        return obtenerPropiedad(ClavesPropiedades.RUTA_REPORTES, "target/reportes/");
    }
    
    /**
     * Obtiene el formato de las capturas de pantalla
     * @return formato de captura (PNG, JPG, etc.)
     */
    public String obtenerFormatoCaptura() {
        return obtenerPropiedad(ClavesPropiedades.FORMATO_CAPTURA, "PNG").toUpperCase();
    }
    
    /**
     * Obtiene la ruta del archivo de datos CSV
     * @return ruta del CSV
     */
    public String obtenerRutaDatosCSV() {
        return obtenerPropiedad(ClavesPropiedades.RUTA_DATOS_CSV, "src/test/resources/datos/usuarios.csv");
    }
    
    /**
     * Obtiene la ruta del archivo de datos Excel
     * @return ruta del Excel
     */
    public String obtenerRutaDatosExcel() {
        return obtenerPropiedad(ClavesPropiedades.RUTA_DATOS_EXCEL, "src/test/resources/datos/usuarios.xlsx");
    }
    
    // === MÉTODOS DE LOGS ===
    
    /**
     * Obtiene el nivel de log configurado
     * @return nivel de log
     */
    public String obtenerNivelLog() {
        return obtenerPropiedad(ClavesPropiedades.LOG_NIVEL, "INFO").toUpperCase();
    }
    
    /**
     * Obtiene el archivo de log configurado
     * @return archivo de log
     */
    public String obtenerArchivoLog() {
        return obtenerPropiedad(ClavesPropiedades.LOG_ARCHIVO, "target/logs/automation.log");
    }
    
    // === MÉTODOS PRIVADOS ===
    
    /**
     * Carga las propiedades desde el archivo de configuración
     */
    private void cargarPropiedades() {
        try {
            // Intentar cargar el archivo principal
            cargarArchivoPropiedades(ARCHIVO_CONFIG);
            logger.info("Configuración cargada desde: {}", ARCHIVO_CONFIG);
        } catch (IOException e) {
            logger.warn("No se pudo cargar {}. Intentando archivo de respaldo.", ARCHIVO_CONFIG);
            
            try {
                // Intentar cargar el archivo de respaldo
                cargarArchivoPropiedades(ARCHIVO_CONFIG_BACKUP);
                logger.info("Configuración cargada desde archivo de respaldo: {}", ARCHIVO_CONFIG_BACKUP);
            } catch (IOException e2) {
                logger.error("No se pudo cargar ningún archivo de configuración. Usando valores por defecto.");
                cargarConfiguracionPorDefecto();
            }
        }
        
        // Sobrescribir con properties del sistema si están definidas
        sobrescribirConPropiedadesSistema();
    }
    
    /**
     * Sobrescribe configuración con properties del sistema (-D)
     */
    private void sobrescribirConPropiedadesSistema() {
        // Navegador desde system property
        String browserProperty = System.getProperty("browser");
        if (browserProperty != null && !browserProperty.trim().isEmpty()) {
            propiedades.setProperty(ClavesPropiedades.NAVEGADOR_TIPO, browserProperty.toUpperCase());
            logger.info("Navegador sobrescrito por system property: {}", browserProperty);
        }
        
        // Headless desde system property
        String headlessProperty = System.getProperty("headless");
        if (headlessProperty != null && !headlessProperty.trim().isEmpty()) {
            propiedades.setProperty(ClavesPropiedades.NAVEGADOR_HEADLESS, headlessProperty);
            logger.info("Modo headless sobrescrito por system property: {}", headlessProperty);
        }
        
        // URL base desde system property
        String urlBaseProperty = System.getProperty("url.base");
        if (urlBaseProperty != null && !urlBaseProperty.trim().isEmpty()) {
            propiedades.setProperty(ClavesPropiedades.URL_BASE, urlBaseProperty);
            logger.info("URL base sobrescrita por system property: {}", urlBaseProperty);
        }
    }
    
    /**
     * Carga un archivo de propiedades específico
     * @param nombreArchivo nombre del archivo a cargar
     * @throws IOException si no se puede cargar el archivo
     */
    private void cargarArchivoPropiedades(String nombreArchivo) throws IOException {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(nombreArchivo)) {
            if (input == null) {
                throw new IOException("Archivo no encontrado: " + nombreArchivo);
            }
            propiedades.load(input);
        }
    }
    
    /**
     * Carga configuración por defecto cuando no se encuentran archivos
     */
    private void cargarConfiguracionPorDefecto() {
        // Configuración mínima por defecto
        propiedades.setProperty(ClavesPropiedades.NAVEGADOR_TIPO, "CHROME");
        propiedades.setProperty(ClavesPropiedades.NAVEGADOR_HEADLESS, "false");
        propiedades.setProperty(ClavesPropiedades.NAVEGADOR_MAXIMIZAR, "true");
        propiedades.setProperty(ClavesPropiedades.URL_BASE, "https://practice.expandtesting.com");
        propiedades.setProperty(ClavesPropiedades.URL_LOGIN, "/login");
        propiedades.setProperty(ClavesPropiedades.URL_REGISTRO, "/register");
        propiedades.setProperty(ClavesPropiedades.TIMEOUT_EXPLICITO, "15");
        propiedades.setProperty(ClavesPropiedades.TIMEOUT_IMPLICITO, "10");
        propiedades.setProperty(ClavesPropiedades.TIMEOUT_CARGA_PAGINA, "30");
        propiedades.setProperty(ClavesPropiedades.RUTA_CAPTURAS, "target/capturas/");
        propiedades.setProperty(ClavesPropiedades.RUTA_REPORTES, "target/reportes/");
        propiedades.setProperty(ClavesPropiedades.FORMATO_CAPTURA, "PNG");
        propiedades.setProperty(ClavesPropiedades.LOG_NIVEL, "INFO");
        propiedades.setProperty(ClavesPropiedades.LOG_ARCHIVO, "target/logs/automation.log");
        
        logger.info("Cargada configuración por defecto");
    }
    
    /**
     * Valida que la configuración obligatoria esté presente
     */
    private void validarConfiguracionObligatoria() {
        String[] propiedadesObligatorias = {
            ClavesPropiedades.URL_BASE,
            ClavesPropiedades.NAVEGADOR_TIPO,
            ClavesPropiedades.TIMEOUT_EXPLICITO
        };
        
        for (String propiedad : propiedadesObligatorias) {
            if (!propiedades.containsKey(propiedad) || 
                propiedades.getProperty(propiedad).trim().isEmpty()) {
                
                logger.error("Propiedad obligatoria faltante: {}", propiedad);
                throw new IllegalStateException("Configuración incompleta: " + propiedad);
            }
        }
        
        logger.debug("Validación de configuración obligatoria completada");
    }
    
    /**
     * Registra la configuración cargada en los logs
     */
    private void logConfiguracionCargada() {
        logger.info("=== CONFIGURACIÓN CARGADA ===");
        logger.info("Navegador: {}", obtenerTipoNavegador());
        logger.info("URL Base: {}", obtenerUrlBase());
        logger.info("URL Login: {}", obtenerUrlLogin());
        logger.info("Headless: {}", esNavegadorHeadless());
        logger.info("Timeout Explícito: {}s", obtenerTimeoutExplicito());
        logger.info("Ruta Capturas: {}", obtenerRutaCapturas());
        logger.info("================================");
    }
    
    /**
     * Obtiene una propiedad con valor por defecto
     * @param clave clave de la propiedad
     * @param valorPorDefecto valor si no existe la propiedad
     * @return valor de la propiedad o valor por defecto
     */
    private String obtenerPropiedad(String clave, String valorPorDefecto) {
        return propiedades.getProperty(clave, valorPorDefecto);
    }
    
    /**
     * Construye una URL completa combinando base y ruta relativa
     * @param rutaRelativa ruta relativa
     * @return URL completa
     */
    private String construirUrlCompleta(String rutaRelativa) {
        String urlBase = obtenerUrlBase();
        
        if (rutaRelativa.startsWith("http")) {
            return rutaRelativa; // Ya es una URL completa
        }
        
        // Asegurar que no hay dobles barras
        if (urlBase.endsWith("/") && rutaRelativa.startsWith("/")) {
            return urlBase + rutaRelativa.substring(1);
        } else if (!urlBase.endsWith("/") && !rutaRelativa.startsWith("/")) {
            return urlBase + "/" + rutaRelativa;
        } else {
            return urlBase + rutaRelativa;
        }
    }
    
    // === MÉTODOS PÚBLICOS ADICIONALES ===
    
    /**
     * Obtiene todas las propiedades como objeto Properties
     * @return copia de las propiedades (para evitar modificaciones externas)
     */
    public Properties obtenerTodasLasPropiedades() {
        Properties copia = new Properties();
        copia.putAll(this.propiedades);
        return copia;
    }
    
    /**
     * Verifica si una propiedad específica existe
     * @param clave clave de la propiedad
     * @return true si la propiedad existe
     */
    public boolean existePropiedad(String clave) {
        return propiedades.containsKey(clave);
    }
    
    /**
     * Obtiene una propiedad personalizada (para extensibilidad)
     * @param clave clave de la propiedad
     * @return valor de la propiedad o null si no existe
     */
    public String obtenerPropiedadPersonalizada(String clave) {
        return propiedades.getProperty(clave);
    }
    
    /**
     * Obtiene una propiedad personalizada con valor por defecto
     * @param clave clave de la propiedad
     * @param valorPorDefecto valor por defecto
     * @return valor de la propiedad o valor por defecto
     */
    public String obtenerPropiedadPersonalizada(String clave, String valorPorDefecto) {
        return propiedades.getProperty(clave, valorPorDefecto);
    }
    
    /**
     * Genera un resumen de la configuración actual
     * @return string con resumen de configuración
     */
    public String generarResumenConfiguracion() {
        StringBuilder resumen = new StringBuilder();
        resumen.append("=== RESUMEN DE CONFIGURACIÓN ===\n");
        resumen.append("Navegador: ").append(obtenerTipoNavegador()).append("\n");
        resumen.append("Modo Headless: ").append(esNavegadorHeadless()).append("\n");
        resumen.append("URL Base: ").append(obtenerUrlBase()).append("\n");
        resumen.append("URL Login: ").append(obtenerUrlLogin()).append("\n");
        resumen.append("URL Registro: ").append(obtenerUrlRegistro()).append("\n");
        resumen.append("Timeout Explícito: ").append(obtenerTimeoutExplicito()).append("s\n");
        resumen.append("Ruta Capturas: ").append(obtenerRutaCapturas()).append("\n");
        resumen.append("Nivel Log: ").append(obtenerNivelLog()).append("\n");
        resumen.append("===============================");
        return resumen.toString();
    }
    
    /**
     * Reinicia la instancia (útil para testing)
     * CUIDADO: Solo usar en entornos de desarrollo/testing
     */
    public static synchronized void reiniciarInstancia() {
        instancia = null;
        logger.warn("Instancia de ConfiguracionGlobal reiniciada");
    }
}