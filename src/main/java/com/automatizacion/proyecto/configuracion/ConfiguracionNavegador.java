package com.automatizacion.proyecto.configuracion;

import com.automatizacion.proyecto.enums.TipoMensaje;
import com.automatizacion.proyecto.enums.TipoNavegador;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase responsable de la configuración y creación de instancias de WebDriver.
 * Implementa el patrón Factory para crear diferentes tipos de navegadores.
 * 
 * Principios aplicados:
 * - SRP: Solo se encarga de la configuración de navegadores
 * - OCP: Fácil extensión para nuevos navegadores
 * - DIP: Devuelve la abstracción WebDriver
 * - Factory Pattern: Centraliza la creación de drivers
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
public class ConfiguracionNavegador {
    
    private static final Logger logger = LoggerFactory.getLogger(ConfiguracionNavegador.class);
    private static final ConfiguracionGlobal configuracion = ConfiguracionGlobal.obtenerInstancia();
    
    // Configuraciones por defecto
    private static final int TIEMPO_ESPERA_IMPLICITA_DEFECTO = 10;
    private static final int TIEMPO_CARGA_PAGINA_DEFECTO = 30;
    private static final String USER_AGENT_DEFECTO = "Mozilla/5.0 (Test Automation)";
    
    /**
     * Crea una instancia de WebDriver basada en la configuración global
     * @return instancia configurada de WebDriver
     */
    public static WebDriver crearNavegador() {
        TipoNavegador tipo = configuracion.obtenerTipoNavegador();
        boolean headless = configuracion.esNavegadorHeadless();
        return crearNavegador(tipo, headless);
    }
    
    /**
     * Crea una instancia de WebDriver basada en el tipo especificado
     * @param tipoNavegador el tipo de navegador a crear
     * @param modoSinCabeza si el navegador debe ejecutarse en modo headless
     * @return instancia configurada de WebDriver
     * @throws IllegalArgumentException si el tipo de navegador no es soportado
     */
    public static WebDriver crearNavegador(TipoNavegador tipoNavegador, boolean modoSinCabeza) {
        logger.info(TipoMensaje.CONFIGURACION.formatearMensaje(
            "Creando navegador: " + tipoNavegador + " (Headless: " + modoSinCabeza + ")"));
        
        // Verificar que el navegador sea soportado en el SO actual
        if (!tipoNavegador.esSoportadoEnSO()) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                "Navegador " + tipoNavegador + " no soportado en este SO. Usando Chrome como alternativa."));
            tipoNavegador = TipoNavegador.CHROME;
        }
        
        WebDriver driver;
        
        try {
            switch (tipoNavegador) {
                case CHROME:
                    driver = configurarChrome(modoSinCabeza);
                    break;
                case FIREFOX:
                    driver = configurarFirefox(modoSinCabeza);
                    break;
                case EDGE:
                    driver = configurarEdge(modoSinCabeza);
                    break;
                case SAFARI:
                    driver = configurarSafari(modoSinCabeza);
                    break;
                default:
                    throw new IllegalArgumentException("Tipo de navegador no soportado: " + tipoNavegador);
            }
            
            configurarTiemposEspera(driver);
            maximizarVentana(driver, modoSinCabeza);
            
            logger.info(TipoMensaje.EXITO.formatearMensaje(
                "Navegador " + tipoNavegador + " creado exitosamente"));
            
            return driver;
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error al crear navegador " + tipoNavegador + ": " + e.getMessage()));
            throw new RuntimeException("No se pudo crear el navegador " + tipoNavegador, e);
        }
    }
    
    /**
     * Configura Chrome con las opciones especificadas
     */
    private static WebDriver configurarChrome(boolean headless) {
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions opciones = new ChromeOptions();
        
        // Opciones básicas
        opciones.addArguments("--no-sandbox");
        opciones.addArguments("--disable-dev-shm-usage");
        opciones.addArguments("--disable-gpu");
        opciones.addArguments("--disable-web-security");
        opciones.addArguments("--allow-running-insecure-content");
        opciones.addArguments("--disable-extensions");
        opciones.addArguments("--disable-plugins");
        opciones.addArguments("--disable-images");
        opciones.addArguments("--disable-javascript");
        
        // Configuración de headless
        if (headless) {
            opciones.addArguments("--headless=new");
            opciones.addArguments("--window-size=1920,1080");
        }
        
        // Preferencias adicionales
        Map<String, Object> preferencias = new HashMap<>();
        preferencias.put("profile.default_content_setting_values.notifications", 2);
        preferencias.put("profile.default_content_settings.popups", 0);
        preferencias.put("profile.managed_default_content_settings.images", 2);
        opciones.setExperimentalOption("prefs", preferencias);
        
        // User agent personalizado
        opciones.addArguments("--user-agent=" + USER_AGENT_DEFECTO);
        
        logger.debug(TipoMensaje.DEBUG.formatearMensaje("Opciones Chrome configuradas"));
        
        return new ChromeDriver(opciones);
    }
    
    /**
     * Configura Firefox con las opciones especificadas
     */
    private static WebDriver configurarFirefox(boolean headless) {
        WebDriverManager.firefoxdriver().setup();
        
        FirefoxOptions opciones = new FirefoxOptions();
        
        // Configuración de headless
        if (headless) {
            opciones.addArguments("--headless");
            opciones.addArguments("--width=1920");
            opciones.addArguments("--height=1080");
        }
        
        // Preferencias de Firefox
        opciones.addPreference("dom.webnotifications.enabled", false);
        opciones.addPreference("dom.push.enabled", false);
        opciones.addPreference("media.volume_scale", "0.0");
        opciones.addPreference("permissions.default.image", 2);
        opciones.addPreference("javascript.enabled", false);
        
        logger.debug(TipoMensaje.DEBUG.formatearMensaje("Opciones Firefox configuradas"));
        
        return new FirefoxDriver(opciones);
    }
    
    /**
     * Configura Edge con las opciones especificadas
     */
    private static WebDriver configurarEdge(boolean headless) {
        WebDriverManager.edgedriver().setup();
        
        EdgeOptions opciones = new EdgeOptions();
        
        // Opciones básicas (similares a Chrome)
        opciones.addArguments("--no-sandbox");
        opciones.addArguments("--disable-dev-shm-usage");
        opciones.addArguments("--disable-gpu");
        opciones.addArguments("--disable-web-security");
        opciones.addArguments("--disable-extensions");
        
        // Configuración de headless
        if (headless) {
            opciones.addArguments("--headless=new");
            opciones.addArguments("--window-size=1920,1080");
        }
        
        // Preferencias
        Map<String, Object> preferencias = new HashMap<>();
        preferencias.put("profile.default_content_setting_values.notifications", 2);
        opciones.setExperimentalOption("prefs", preferencias);
        
        logger.debug(TipoMensaje.DEBUG.formatearMensaje("Opciones Edge configuradas"));
        
        return new EdgeDriver(opciones);
    }
    
    /**
     * Configura Safari con las opciones especificadas
     */
    private static WebDriver configurarSafari(boolean headless) {
        // Safari no soporta modo headless de forma nativa
        if (headless) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                "Safari no soporta modo headless. Ejecutándose en modo normal."));
        }
        
        SafariOptions opciones = new SafariOptions();
        opciones.setAutomaticInspection(false);
        opciones.setAutomaticProfiling(false);
        
        logger.debug(TipoMensaje.DEBUG.formatearMensaje("Opciones Safari configuradas"));
        
        return new SafariDriver(opciones);
    }
    
    /**
     * Configura los tiempos de espera del driver
     */
    private static void configurarTiemposEspera(WebDriver driver) {
        int tiempoImplicito = configuracion.obtenerTimeoutImplicito();
        int tiempoCarga = configuracion.obtenerPropiedad("timeout.carga.pagina", 
            String.valueOf(TIEMPO_CARGA_PAGINA_DEFECTO)).equals("") ? 
            TIEMPO_CARGA_PAGINA_DEFECTO : 
            Integer.parseInt(configuracion.obtenerPropiedad("timeout.carga.pagina", 
                String.valueOf(TIEMPO_CARGA_PAGINA_DEFECTO)));
        
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(tiempoImplicito));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(tiempoCarga));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(tiempoCarga));
        
        logger.debug(TipoMensaje.DEBUG.formatearMensaje(
            "Timeouts configurados - Implícito: " + tiempoImplicito + "s, Carga: " + tiempoCarga + "s"));
    }
    
    /**
     * Maximiza la ventana del navegador
     */
    private static void maximizarVentana(WebDriver driver, boolean headless) {
        try {
            if (!headless) {
                driver.manage().window().maximize();
                logger.debug(TipoMensaje.DEBUG.formatearMensaje("Ventana maximizada"));
            } else {
                // En modo headless, establecer tamaño específico
                driver.manage().window().setSize(new org.openqa.selenium.Dimension(1920, 1080));
                logger.debug(TipoMensaje.DEBUG.formatearMensaje("Ventana configurada para modo headless: 1920x1080"));
            }
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                "No se pudo configurar el tamaño de ventana: " + e.getMessage()));
        }
    }
    
    /**
     * Cierra el driver de forma segura
     * @param driver driver a cerrar
     */
    public static void cerrarNavegador(WebDriver driver) {
        if (driver != null) {
            try {
                driver.quit();
                logger.info(TipoMensaje.INFORMATIVO.formatearMensaje("Navegador cerrado correctamente"));
            } catch (Exception e) {
                logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                    "Error al cerrar navegador: " + e.getMessage()));
            }
        }
    }
    
    /**
     * Obtiene información del navegador actual
     * @param driver driver del cual obtener información
     * @return información del navegador
     */
    public static String obtenerInformacionNavegador(WebDriver driver) {
        try {
            String userAgent = (String) ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("return navigator.userAgent;");
            return userAgent;
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                "No se pudo obtener información del navegador: " + e.getMessage()));
            return "Información no disponible";
        }
    }
}