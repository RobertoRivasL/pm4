package com.robertorivas.automatizacion.configuracion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Properties;

/**
 * Clase singleton responsable de manejar todas las configuraciones del proyecto.
 * 
 * Principios aplicados:
 * - Singleton Pattern: Una sola instancia de configuración
 * - Single Responsibility: Solo maneja configuraciones
 * - Encapsulación: Configuraciones encapsuladas con métodos de acceso
 * - Separation of Concerns: Configuración separada de la lógica de negocio
 * 
 * @author Roberto Rivas Lopez
 */
public class ConfiguracionPruebas {
    
    private static final Logger logger = LoggerFactory.getLogger(ConfiguracionPruebas.class);
    private static ConfiguracionPruebas instancia;
    private final Properties propiedades;
    
    // Rutas de archivos
    private static final String ARCHIVO_CONFIG = "/configuracion/config.properties";
    
    // Constructor privado (patrón Singleton)
    private ConfiguracionPruebas() {
        this.propiedades = cargarPropiedades();
    }
    
    /**
     * Obtiene la instancia única de ConfiguracionPruebas (Singleton).
     */
    public static ConfiguracionPruebas obtenerInstancia() {
        if (instancia == null) {
            synchronized (ConfiguracionPruebas.class) {
                if (instancia == null) {
                    instancia = new ConfiguracionPruebas();
                }
            }
        }
        return instancia;
    }
    
    /**
     * Carga las propiedades desde el archivo de configuración.
     */
    private Properties cargarPropiedades() {
        Properties props = new Properties();
        
        try (InputStream inputStream = getClass().getResourceAsStream(ARCHIVO_CONFIG)) {
            if (inputStream != null) {
                props.load(inputStream);
                logger.info("Configuración cargada exitosamente desde: {}", ARCHIVO_CONFIG);

                if (props.isEmpty()) {
                    logger.warn("Archivo de configuración vacío: {}. Usando valores por defecto.", ARCHIVO_CONFIG);
                    cargarConfiguracionPorDefecto(props);
                }
            } else {
                logger.warn("Archivo de configuración no encontrado: {}. Usando valores por defecto.", ARCHIVO_CONFIG);
                cargarConfiguracionPorDefecto(props);
            }
        } catch (IOException e) {
            logger.error("Error al cargar configuración: {}", e.getMessage());
            cargarConfiguracionPorDefecto(props);
        }
        
        // Sobrescribir con propiedades del sistema si existen
        sobrescribirConPropiedadesSistema(props);
        
        return props;
    }
    
    /**
     * Carga configuración por defecto cuando no se encuentra el archivo.
     */
    private void cargarConfiguracionPorDefecto(Properties props) {
        // URLs de aplicación
        props.setProperty("app.url.base", "https://practicetestautomation.com");
        props.setProperty("app.url.registro", "/practice-test-login");
        props.setProperty("app.url.login", "/practice-test-login");
        
        // Configuraciones de navegador
        props.setProperty("navegador.defecto", "chrome");
        props.setProperty("navegador.headless", "false");
        
        // Timeouts
        props.setProperty("timeout.implicito", "10");
        props.setProperty("timeout.explicito", "15");
        props.setProperty("timeout.pagina", "30");
        
        // Rutas de archivos
        props.setProperty("ruta.datos", "src/test/resources/datos");
        props.setProperty("ruta.reportes", "reportes");
        props.setProperty("ruta.capturas", "reportes/capturas");
        props.setProperty("ruta.logs", "reportes/logs");
        
        // Configuraciones de datos
        props.setProperty("datos.usuarios.registro", "usuarios_registro.csv");
        props.setProperty("datos.usuarios.login", "usuarios_login.csv");
        props.setProperty("datos.credenciales.invalidas", "credenciales_invalidas.csv");
        
        logger.info("Configuración por defecto cargada");
    }
    
    /**
     * Sobrescribe propiedades con variables del sistema (útil para CI/CD).
     */
    private void sobrescribirConPropiedadesSistema(Properties props) {
        // Navegador
        String navegadorSistema = System.getProperty("navegador");
        if (navegadorSistema != null) {
            props.setProperty("navegador.defecto", navegadorSistema);
            logger.info("Navegador sobrescrito por propiedad del sistema: {}", navegadorSistema);
        }
        
        // Entorno
        String entornoSistema = System.getProperty("entorno");
        if (entornoSistema != null) {
            props.setProperty("entorno", entornoSistema);
            logger.info("Entorno sobrescrito por propiedad del sistema: {}", entornoSistema);
        }
        
        // Headless
        String headlessSistema = System.getProperty("headless");
        if (headlessSistema != null) {
            props.setProperty("navegador.headless", headlessSistema);
            logger.info("Modo headless sobrescrito por propiedad del sistema: {}", headlessSistema);
        }
    }
    
    // Métodos de acceso a configuraciones de URLs
    
    public String obtenerUrlBase() {
        return propiedades.getProperty("app.url.base");
    }
    
    public String obtenerUrlRegistro() {
        return obtenerUrlBase() + propiedades.getProperty("app.url.registro");
    }
    
    public String obtenerUrlLogin() {
        return obtenerUrlBase() + propiedades.getProperty("app.url.login");
    }
    
    // Métodos de acceso a configuraciones de navegador
    
    public String obtenerNavegadorDefecto() {
        return propiedades.getProperty("navegador.defecto", "chrome");
    }
    
    public boolean esNavegadorHeadless() {
        return Boolean.parseBoolean(propiedades.getProperty("navegador.headless", "false"));
    }
    
    // Métodos de acceso a timeouts
    
    public Duration obtenerTimeoutImplicito() {
        int segundos = Integer.parseInt(propiedades.getProperty("timeout.implicito", "10"));
        return Duration.ofSeconds(segundos);
    }
    
    public Duration obtenerTimeoutExplicito() {
        int segundos = Integer.parseInt(propiedades.getProperty("timeout.explicito", "15"));
        return Duration.ofSeconds(segundos);
    }
    
    public Duration obtenerTimeoutPagina() {
        int segundos = Integer.parseInt(propiedades.getProperty("timeout.pagina", "30"));
        return Duration.ofSeconds(segundos);
    }
    
    // Métodos de acceso a rutas
    
    public Path obtenerRutaDatos() {
        return Paths.get(propiedades.getProperty("ruta.datos", "src/test/resources/datos"));
    }
    
    public Path obtenerRutaReportes() {
        return Paths.get(propiedades.getProperty("ruta.reportes", "reportes"));
    }
    
    public Path obtenerRutaCapturas() {
        return Paths.get(propiedades.getProperty("ruta.capturas", "reportes/capturas"));
    }
    
    public Path obtenerRutaLogs() {
        return Paths.get(propiedades.getProperty("ruta.logs", "reportes/logs"));
    }
    
    // Métodos de acceso a archivos de datos
    
    public String obtenerArchivoUsuariosRegistro() {
        return propiedades.getProperty("datos.usuarios.registro", "usuarios_registro.csv");
    }
    
    public String obtenerArchivoUsuariosLogin() {
        return propiedades.getProperty("datos.usuarios.login", "usuarios_login.csv");
    }
    
    public String obtenerArchivoCredencialesInvalidas() {
        return propiedades.getProperty("datos.credenciales.invalidas", "credenciales_invalidas.csv");
    }
    
    // Métodos utilitarios
    
    public String obtenerEntorno() {
        return propiedades.getProperty("entorno", "desarrollo");
    }
    
    public boolean esEntornoCI() {
        return "ci".equalsIgnoreCase(obtenerEntorno());
    }
    
    public boolean esEntornoDesarrollo() {
        return "desarrollo".equalsIgnoreCase(obtenerEntorno());
    }
    
    /**
     * Obtiene una propiedad personalizada.
     */
    public String obtenerPropiedad(String clave) {
        return propiedades.getProperty(clave);
    }
    
    /**
     * Obtiene una propiedad personalizada con valor por defecto.
     */
    public String obtenerPropiedad(String clave, String valorPorDefecto) {
        return propiedades.getProperty(clave, valorPorDefecto);
    }
    
    /**
     * Registra todas las configuraciones actuales (útil para debugging).
     */
    public void registrarConfiguracionesActuales() {
        logger.info("=== CONFIGURACIONES ACTUALES ===");
        logger.info("URL Base: {}", obtenerUrlBase());
        logger.info("URL Registro: {}", obtenerUrlRegistro());
        logger.info("URL Login: {}", obtenerUrlLogin());
        logger.info("Navegador: {}", obtenerNavegadorDefecto());
        logger.info("Headless: {}", esNavegadorHeadless());
        logger.info("Entorno: {}", obtenerEntorno());
        logger.info("Timeout Implícito: {}s", obtenerTimeoutImplicito().getSeconds());
        logger.info("Timeout Explícito: {}s", obtenerTimeoutExplicito().getSeconds());
        logger.info("Timeout Página: {}s", obtenerTimeoutPagina().getSeconds());
        logger.info("Ruta Datos: {}", obtenerRutaDatos());
        logger.info("Ruta Reportes: {}", obtenerRutaReportes());
        logger.info("================================");
    }
}