package com.automatizacion.proyecto.configuracion;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;
import java.net.URL;
import java.net.MalformedURLException;

/**
 * Factory para creación y configuración de WebDrivers.
 * Implementa el patrón Factory Method para encapsular la creación de drivers.
 * 
 * Principios SOLID aplicados:
 * - SRP: Responsabilidad única de crear y configurar drivers
 * - OCP: Abierto para extensión (nuevos navegadores), cerrado para modificación
 * - Factory Pattern: Encapsula la creación de objetos WebDriver
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
public class ConfiguracionNavegador {
    
    private static final Logger logger = LoggerFactory.getLogger(ConfiguracionNavegador.class);
    
    // Constantes para configuración
    private static final String USER_AGENT_WINDOWS = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";
    private static final String USER_AGENT_MAC = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";
    private static final int TIMEOUT_DEFAULT = 30;
    
    // Constructor privado para clase utilitaria
    private ConfiguracionNavegador() {
        throw new IllegalStateException("Clase utilitaria - no debe instanciarse");
    }
    
    /**
     * Crea un WebDriver basado en el tipo especificado.
     * 
     * @param tipoNavegador Tipo de navegador a crear
     * @return WebDriver configurado
     */
    public static WebDriver crearDriver(ConfiguracionGlobal.TipoNavegador tipoNavegador) {
        logger.info("Creando driver para navegador: {}", tipoNavegador);
        
        WebDriver driver;
        ConfiguracionGlobal config = ConfiguracionGlobal.getInstance();
        
        try {
            switch (tipoNavegador) {
                case CHROME:
                    driver = crearDriverChrome(config);
                    break;
                case FIREFOX:
                    driver = crearDriverFirefox(config);
                    break;
                case EDGE:
                    driver = crearDriverEdge(config);
                    break;
                case SAFARI:
                    driver = crearDriverSafari(config);
                    break;
                default:
                    logger.warn("Tipo de navegador no soportado: {}. Usando Chrome por defecto.", tipoNavegador);
                    driver = crearDriverChrome(config);
            }
            
            // Configurar driver común
            configurarDriver(driver, config);
            
            logger.info("Driver creado y configurado exitosamente para: {}", tipoNavegador);
            return driver;
            
        } catch (Exception e) {
            logger.error("Error crítico al crear driver para {}: {}", tipoNavegador, e.getMessage(), e);
            throw new RuntimeException("No se pudo crear el driver para " + tipoNavegador, e);
        }
    }
    
    /**
     * Crea driver de Chrome con configuraciones específicas.
     */
    private static WebDriver crearDriverChrome(ConfiguracionGlobal config) {
        try {
            logger.debug("Configurando WebDriverManager para Chrome");
            WebDriverManager.chromedriver().setup();
            
            ChromeOptions opciones = new ChromeOptions();
            
            // Configuraciones básicas
            if (config.esNavegadorHeadless()) {
                opciones.addArguments("--headless=new"); // Nuevo modo headless de Chrome
                opciones.addArguments("--disable-gpu");
                logger.debug("Chrome configurado en modo headless");
            }
            
            // Argumentos para estabilidad y compatibilidad
            opciones.addArguments("--no-sandbox");
            opciones.addArguments("--disable-dev-shm-usage");
            opciones.addArguments("--disable-extensions");
            opciones.addArguments("--disable-plugins");
            opciones.addArguments("--disable-images");
            opciones.addArguments("--disable-javascript");
            opciones.addArguments("--remote-allow-origins=*");
            opciones.addArguments("--disable-web-security");
            opciones.addArguments("--disable-features=VizDisplayCompositor");
            opciones.addArguments("--disable-ipc-flooding-protection");
            
            // Configuraciones de rendimiento
            opciones.addArguments("--max_old_space_size=4096");
            opciones.addArguments("--disable-background-timer-throttling");
            opciones.addArguments("--disable-renderer-backgrounding");
            opciones.addArguments("--disable-backgrounding-occluded-windows");
            
            // Configuraciones de ventana
            if (!config.esVentanaMaximizada()) {
                String tamanoVentana = String.format("--window-size=%d,%d", 
                    config.getAnchoVentana(), config.getAltoVentana());
                opciones.addArguments(tamanoVentana);
                logger.debug("Chrome configurado con tamaño de ventana: {}x{}", 
                    config.getAnchoVentana(), config.getAltoVentana());
            }
            
            // Configuraciones para evitar detección de automatización
            opciones.addArguments("--disable-blink-features=AutomationControlled");
            opciones.setExperimentalOption("useAutomationExtension", false);
            opciones.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
            opciones.addArguments("--disable-automation");
            opciones.addArguments("--disable-browser-side-navigation");
            
            // User Agent
            String userAgent = obtenerUserAgent();
            opciones.addArguments("--user-agent=" + userAgent);
            
            // Configuraciones de proxy si están disponibles
            configurarProxy(opciones, config);
            
            // Preferencias adicionales
            opciones.setExperimentalOption("prefs", crearPreferenciasChrome());
            
            ChromeDriver driver = new ChromeDriver(opciones);
            
            // Ejecutar script para ocultar propiedades de WebDriver
            driver.executeScript("Object.defineProperty(navigator, 'webdriver', {get: () => undefined})");
            
            logger.debug("ChromeDriver creado exitosamente con todas las configuraciones");
            return driver;
            
        } catch (Exception e) {
            logger.error("Error al crear ChromeDriver: {}", e.getMessage(), e);
            throw new RuntimeException("No se pudo crear ChromeDriver", e);
        }
    }
    
    /**
     * Crea driver de Firefox con configuraciones específicas.
     */
    private static WebDriver crearDriverFirefox(ConfiguracionGlobal config) {
        try {
            logger.debug("Configurando WebDriverManager para Firefox");
            WebDriverManager.firefoxdriver().setup();
            
            FirefoxOptions opciones = new FirefoxOptions();
            
            // Configuraciones básicas
            if (config.esNavegadorHeadless()) {
                opciones.addArguments("--headless");
                logger.debug("Firefox configurado en modo headless");
            }
            
            // Configuraciones adicionales
            opciones.addArguments("--no-sandbox");
            opciones.addArguments("--disable-dev-shm-usage");
            opciones.addArguments("--disable-gpu");
            opciones.addArguments("--width=" + config.getAnchoVentana());
            opciones.addArguments("--height=" + config.getAltoVentana());
            
            // Configurar perfil personalizado
            FirefoxProfile perfil = crearPerfilFirefox();
            opciones.setProfile(perfil);
            
            // Configuraciones de logging
            opciones.setLogLevel(org.openqa.selenium.firefox.FirefoxDriverLogLevel.ERROR);
            
            FirefoxDriver driver = new FirefoxDriver(opciones);
            logger.debug("FirefoxDriver creado exitosamente");
            return driver;
            
        } catch (Exception e) {
            logger.error("Error al crear FirefoxDriver: {}", e.getMessage(), e);
            throw new RuntimeException("No se pudo crear FirefoxDriver", e);
        }
    }
    
    /**
     * Crea driver de Edge con configuraciones específicas.
     */
    private static WebDriver crearDriverEdge(ConfiguracionGlobal config) {
        try {
            logger.debug("Configurando WebDriverManager para Edge");
            WebDriverManager.edgedriver().setup();
            
            EdgeOptions opciones = new EdgeOptions();
            
            // Configuraciones básicas
            if (config.esNavegadorHeadless()) {
                opciones.addArguments("--headless");
                logger.debug("Edge configurado en modo headless");
            }
            
            // Argumentos similares a Chrome (Edge está basado en Chromium)
            opciones.addArguments("--no-sandbox");
            opciones.addArguments("--disable-dev-shm-usage");
            opciones.addArguments("--disable-gpu");
            opciones.addArguments("--remote-allow-origins=*");
            opciones.addArguments("--disable-web-security");
            opciones.addArguments("--disable-features=VizDisplayCompositor");
            
            // Configuraciones de ventana
            if (!config.esVentanaMaximizada()) {
                String tamanoVentana = String.format("--window-size=%d,%d", 
                    config.getAnchoVentana(), config.getAltoVentana());
                opciones.addArguments(tamanoVentana);
            }
            
            // Configuraciones para evitar detección
            opciones.addArguments("--disable-blink-features=AutomationControlled");
            opciones.setExperimentalOption("useAutomationExtension", false);
            opciones.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
            
            EdgeDriver driver = new EdgeDriver(opciones);
            logger.debug("EdgeDriver creado exitosamente");
            return driver;
            
        } catch (Exception e) {
            logger.error("Error al crear EdgeDriver: {}", e.getMessage(), e);
            throw new RuntimeException("No se pudo crear EdgeDriver", e);
        }
    }
    
    /**
     * Crea driver de Safari con configuraciones específicas.
     */
    private static WebDriver crearDriverSafari(ConfiguracionGlobal config) {
        try {
            // Verificar que estamos en macOS
            String os = System.getProperty("os.name").toLowerCase();
            if (!os.contains("mac")) {
                throw new RuntimeException("Safari solo está disponible en macOS");
            }
            
            logger.debug("Configurando SafariDriver");
            SafariOptions opciones = new SafariOptions();
            
            // Safari no soporta headless mode
            if (config.esNavegadorHeadless()) {
                logger.warn("Safari no soporta modo headless. Ejecutando en modo normal.");
            }
            
            // Configuraciones específicas de Safari
            opciones.setAutomaticInspection(false);
            opciones.setAutomaticProfiling(false);
            
            SafariDriver driver = new SafariDriver(opciones);
            logger.debug("SafariDriver creado exitosamente");
            return driver;
            
        } catch (Exception e) {
            logger.error("Error al crear SafariDriver: {}", e.getMessage(), e);
            throw new RuntimeException("No se pudo crear SafariDriver", e);
        }
    }
    
    /**
     * Configura el driver con configuraciones comunes.
     */
    private static void configurarDriver(WebDriver driver, ConfiguracionGlobal config) {
        try {
            logger.debug("Aplicando configuraciones comunes al driver");
            
            // Configurar timeouts
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(config.getTimeoutImplicito()));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(config.getTimeoutCargaPagina()));
            driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(TIMEOUT_DEFAULT));
            
            // Configurar ventana
            if (config.esVentanaMaximizada()) {
                driver.manage().window().maximize();
                logger.debug("Ventana maximizada");
            } else {
                Dimension dimension = new Dimension(config.getAnchoVentana(), config.getAltoVentana());
                driver.manage().window().setSize(dimension);
                logger.debug("Ventana configurada a {}x{}", config.getAnchoVentana(), config.getAltoVentana());
            }
            
            // Eliminar todas las cookies
            driver.manage().deleteAllCookies();
            
            logger.debug("Driver configurado exitosamente con timeouts y dimensiones");
            
        } catch (Exception e) {
            logger.warn("Error al configurar driver: {}", e.getMessage());
        }
    }
    
    /**
     * Crea perfil personalizado para Firefox.
     */
    private static FirefoxProfile crearPerfilFirefox() {
        FirefoxProfile perfil = new FirefoxProfile();
        
        try {
            // Configuraciones para automatización
            perfil.setPreference("dom.webdriver.enabled", false);
            perfil.setPreference("useAutomationExtension", false);
            
            // Configuraciones de seguridad para pruebas
            perfil.setPreference("security.tls.insecure_fallback_hosts", "localhost");
            perfil.setPreference("security.fileuri.strict_origin_policy", false);
            perfil.setPreference("security.mixed_content.block_active_content", false);
            
            // Configuraciones de rendimiento
            perfil.setPreference("browser.cache.disk.enable", false);
            perfil.setPreference("browser.cache.memory.enable", false);
            perfil.setPreference("browser.cache.offline.enable", false);
            perfil.setPreference("network.http.use-cache", false);
            
            // Configuraciones de navegación
            perfil.setPreference("browser.startup.homepage", "about:blank");
            perfil.setPreference("browser.startup.page", 0);
            
            // Deshabilitar actualizaciones automáticas
            perfil.setPreference("app.update.enabled", false);
            perfil.setPreference("app.update.auto", false);
            
            // Configuraciones de logging
            perfil.setPreference("devtools.console.stdout.content", true);
            
            logger.debug("Perfil de Firefox configurado exitosamente");
            
        } catch (Exception e) {
            logger.warn("Error al configurar perfil de Firefox: {}", e.getMessage());
        }
        
        return perfil;
    }
    
    /**
     * Crea preferencias para Chrome.
     */
    private static java.util.Map<String, Object> crearPreferenciasChrome() {
        java.util.Map<String, Object> prefs = new java.util.HashMap<>();
        
        // Deshabilitar notificaciones
        prefs.put("profile.default_content_setting_values.notifications", 2);
        
        // Deshabilitar geolocalización
        prefs.put("profile.default_content_setting_values.geolocation", 2);
        
        // Configuraciones de descarga
        prefs.put("profile.default_content_settings.popups", 0);
        prefs.put("download.prompt_for_download", false);
        prefs.put("download.directory_upgrade", true);
        
        // Deshabilitar extensiones
        prefs.put("profile.default_content_setting_values.plugins", 1);
        prefs.put("profile.content_settings.plugin_whitelist.adobe-flash-player", 1);
        prefs.put("profile.content_settings.exceptions.plugins.*,*.per_resource.adobe-flash-player", 1);
        
        // Configuraciones de privacidad
        prefs.put("profile.default_content_setting_values.media_stream_mic", 2);
        prefs.put("profile.default_content_setting_values.media_stream_camera", 2);
        
        logger.debug("Preferencias de Chrome configuradas");
        return prefs;
    }
    
    /**
     * Obtiene User Agent apropiado según el SO.
     */
    private static String obtenerUserAgent() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("mac")) {
            return USER_AGENT_MAC;
        } else {
            return USER_AGENT_WINDOWS;
        }
    }
    
    /**
     * Configura proxy si está disponible en la configuración.
     */
    private static void configurarProxy(ChromeOptions opciones, ConfiguracionGlobal config) {
        try {
            String proxyUrl = config.obtenerPropiedadPersonalizada("proxy.url");
            if (proxyUrl != null && !proxyUrl.trim().isEmpty()) {
                opciones.addArguments("--proxy-server=" + proxyUrl);
                logger.debug("Proxy configurado: {}", proxyUrl);
            }
        } catch (Exception e) {
            logger.debug("No se configuró proxy: {}", e.getMessage());
        }
    }
    
    // ===== MÉTODOS DE UTILIDAD AVANZADOS =====
    
    /**
     * Crea driver con configuración personalizada completa.
     */
    public static WebDriver crearDriverPersonalizado(ConfiguracionGlobal.TipoNavegador tipo, boolean headless, boolean maximizado, int timeoutSegundos) {
        logger.info("Creando driver personalizado avanzado: {} (headless: {}, maximizado: {}, timeout: {})", 
                   tipo, headless, maximizado, timeoutSegundos);
        
        WebDriver driver;
        
        try {
            switch (tipo) {
                case CHROME:
                    driver = crearChromePersonalizado(headless, maximizado, timeoutSegundos);
                    break;
                case FIREFOX:
                    driver = crearFirefoxPersonalizado(headless, maximizado, timeoutSegundos);
                    break;
                case EDGE:
                    driver = crearEdgePersonalizado(headless, maximizado, timeoutSegundos);
                    break;
                default:
                    logger.warn("Tipo personalizado no soportado: {}. Usando Chrome.", tipo);
                    driver = crearChromePersonalizado(headless, maximizado, timeoutSegundos);
            }
            
            // Configuración de timeouts personalizados
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeoutSegundos));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(timeoutSegundos * 2));
            
            if (maximizado) {
                driver.manage().window().maximize();
            }
            
            logger.info("Driver personalizado creado exitosamente");
            return driver;
            
        } catch (Exception e) {
            logger.error("Error al crear driver personalizado: {}", e.getMessage(), e);
            throw new RuntimeException("No se pudo crear driver personalizado", e);
        }
    }
    
    /**
     * Crea Chrome personalizado con configuraciones avanzadas.
     */
    private static WebDriver crearChromePersonalizado(boolean headless, boolean maximizado, int timeout) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions opciones = new ChromeOptions();
        
        if (headless) {
            opciones.addArguments("--headless=new");
            opciones.addArguments("--disable-gpu");
        }
        
        opciones.addArguments("--no-sandbox", "--disable-dev-shm-usage", "--disable-extensions");
        opciones.addArguments("--remote-allow-origins=*");
        opciones.setExperimentalOption("useAutomationExtension", false);
        
        return new ChromeDriver(opciones);
    }
    
    /**
     * Crea Firefox personalizado con configuraciones avanzadas.
     */
    private static WebDriver crearFirefoxPersonalizado(boolean headless, boolean maximizado, int timeout) {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions opciones = new FirefoxOptions();
        
        if (headless) {
            opciones.addArguments("--headless");
        }
        
        FirefoxProfile perfil = new FirefoxProfile();
        perfil.setPreference("dom.webdriver.enabled", false);
        opciones.setProfile(perfil);
        
        return new FirefoxDriver(opciones);
    }
    
    /**
     * Crea Edge personalizado con configuraciones avanzadas.
     */
    private static WebDriver crearEdgePersonalizado(boolean headless, boolean maximizado, int timeout) {
        WebDriverManager.edgedriver().setup();
        EdgeOptions opciones = new EdgeOptions();
        
        if (headless) {
            opciones.addArguments("--headless");
        }
        
        opciones.addArguments("--no-sandbox", "--disable-dev-shm-usage");
        opciones.setExperimentalOption("useAutomationExtension", false);
        
        return new EdgeDriver(opciones);
    }
    
    /**
     * Crea driver remoto para Selenium Grid.
     */
    public static WebDriver crearDriverRemoto(String gridUrl, ConfiguracionGlobal.TipoNavegador tipo) {
        try {
            logger.info("Creando driver remoto para Grid: {} con navegador: {}", gridUrl, tipo);
            
            DesiredCapabilities capabilities = new DesiredCapabilities();
            
            switch (tipo) {
                case CHROME:
                    capabilities.setBrowserName("chrome");
                    break;
                case FIREFOX:
                    capabilities.setBrowserName("firefox");
                    break;
                case EDGE:
                    capabilities.setBrowserName("MicrosoftEdge");
                    break;
                default:
                    capabilities.setBrowserName("chrome");
            }
            
            URL url = new URL(gridUrl);
            RemoteWebDriver driver = new RemoteWebDriver(url, capabilities);
            
            // Configuración básica
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
            driver.manage().window().maximize();
            
            logger.info("Driver remoto creado exitosamente");
            return driver;
            
        } catch (MalformedURLException e) {
            logger.error("URL de Grid inválida: {}", gridUrl, e);
            throw new RuntimeException("URL de Grid inválida: " + gridUrl, e);
        } catch (Exception e) {
            logger.error("Error al crear driver remoto: {}", e.getMessage(), e);
            throw new RuntimeException("No se pudo crear driver remoto", e);
        }
    }
    
    /**
     * Verifica si un tipo de navegador está soportado en el sistema actual.
     */
    public static boolean esNavegadorSoportado(ConfiguracionGlobal.TipoNavegador tipo) {
        try {
            switch (tipo) {
                case CHROME:
                    WebDriverManager.chromedriver().setup();
                    return true;
                case FIREFOX:
                    WebDriverManager.firefoxdriver().setup();
                    return true;
                case EDGE:
                    WebDriverManager.edgedriver().setup();
                    return true;
                case SAFARI:
                    String os = System.getProperty("os.name").toLowerCase();
                    return os.contains("mac");
                default:
                    return false;
            }
        } catch (Exception e) {
            logger.warn("Error al verificar soporte para navegador {}: {}", tipo, e.getMessage());
            return false;
        }
    }
    
    /**
     * Obtiene información detallada de navegadores disponibles.
     */
    public static String obtenerInfoNavegadores() {
        StringBuilder info = new StringBuilder();
        info.append("=== NAVEGADORES DISPONIBLES ===\n");
        
        for (ConfiguracionGlobal.TipoNavegador tipo : ConfiguracionGlobal.TipoNavegador.values()) {
            boolean soportado = esNavegadorSoportado(tipo);
            String estado = soportado ? "✅ Disponible" : "❌ No disponible";
            info.append(String.format("%-8s: %s\n", tipo, estado));
        }
        
        // Información adicional del sistema
        info.append("\n=== INFORMACIÓN DEL SISTEMA ===\n");
        info.append("SO: ").append(System.getProperty("os.name")).append("\n");
        info.append("Arquitectura: ").append(System.getProperty("os.arch")).append("\n");
        info.append("Java: ").append(System.getProperty("java.version")).append("\n");
        
        return info.toString();
    }
    
    /**
     * Limpia recursos del driver de forma segura con timeout.
     */
    public static void cerrarDriver(WebDriver driver) {
        if (driver != null) {
            try {
                // Intentar cerrar de forma limpia
                driver.quit();
                logger.info("Driver cerrado exitosamente");
            } catch (Exception e) {
                logger.warn("Error al cerrar driver normalmente, forzando cierre: {}", e.getMessage());
                try {
                    // Forzar cierre en caso de error
                    if (driver instanceof ChromeDriver) {
                        ((ChromeDriver) driver).close();
                    } else if (driver instanceof FirefoxDriver) {
                        ((FirefoxDriver) driver).close();
                    } else if (driver instanceof EdgeDriver) {
                        ((EdgeDriver) driver).close();
                    }
                } catch (Exception e2) {
                    logger.error("Error crítico al cerrar driver: {}", e2.getMessage());
                }
            }
        }
    }
    
    /**
     * Reinicia un driver manteniendo la misma configuración.
     */
    public static WebDriver reiniciarDriver(WebDriver driverActual, ConfiguracionGlobal.TipoNavegador tipo) {
        logger.info("Reiniciando driver para: {}", tipo);
        
        try {
            // Cerrar driver actual
            cerrarDriver(driverActual);
            
            // Esperar un momento
            Thread.sleep(2000);
            
            // Crear nuevo driver
            WebDriver nuevoDriver = crearDriver(tipo);
            
            logger.info("Driver reiniciado exitosamente");
            return nuevoDriver;
            
        } catch (Exception e) {
            logger.error("Error al reiniciar driver: {}", e.getMessage(), e);
            throw new RuntimeException("No se pudo reiniciar el driver", e);
        }
    }
}