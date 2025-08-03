package com.automatizacion.proyecto.configuracion;

import com.automatizacion.proyecto.enums.TipoNavegador;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Clase responsable de la configuración y creación de instancias de WebDriver.
 * Implementa el patrón Factory para crear diferentes tipos de navegadores.
 * 
 * Principios aplicados:
 * - SRP: Solo se encarga de la configuración de navegadores
 * - OCP: Fácil extensión para nuevos navegadores
 * - DIP: Devuelve la abstracción WebDriver
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
public class ConfiguracionNavegador {
    
    private static final Logger logger = LoggerFactory.getLogger(ConfiguracionNavegador.class);
    private static final int TIEMPO_ESPERA_IMPLICITA = 10;
    private static final int TIEMPO_CARGA_PAGINA = 30;
    
    /**
     * Crea una instancia de WebDriver basada en el tipo especificado.
     * 
     * @param tipoNavegador El tipo de navegador a crear
     * @param modoSinCabeza Si el navegador debe ejecutarse en modo headless
     * @return Instancia configurada de WebDriver
     * @throws IllegalArgumentException Si el tipo de navegador no es soportado
     */
    public static WebDriver crearNavegador(TipoNavegador tipoNavegador, boolean modoSinCabeza) {
        logger.info("Creando navegador tipo: {} (Headless: {})", 
                   tipoNavegador.obtenerNombreCompleto(), modoSinCabeza);
        
        WebDriver driver;
        
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
        driver.manage().window().maximize();
        
        logger.info("Navegador {} configurado exitosamente", tipoNavegador.obtenerNombreCompleto());
        return driver;
    }
    
    /**
     * Método de conveniencia que mantiene compatibilidad con código existente
     * @param tipoNavegador tipo de navegador
     * @param modoSinCabeza modo headless
     * @return WebDriver configurado
     */
    public static WebDriver crearDriver(TipoNavegador tipoNavegador, boolean modoSinCabeza) {
        return crearNavegador(tipoNavegador, modoSinCabeza);
    }
    
    /**
     * Crea un navegador con configuración por defecto (Chrome)
     * @return WebDriver configurado
     */
    public static WebDriver crearNavegadorPorDefecto() {
        return crearNavegador(TipoNavegador.CHROME, false);
    }
    
    /**
     * Configura Chrome con las opciones especificadas.
     * @param modoSinCabeza si debe ejecutarse en modo headless
     * @return WebDriver de Chrome configurado
     */
    private static WebDriver configurarChrome(boolean modoSinCabeza) {
        logger.debug("Configurando Chrome con WebDriverManager");
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions opciones = new ChromeOptions();
        
        // Opciones básicas para evitar detección de automatización
        opciones.addArguments("--disable-blink-features=AutomationControlled");
        opciones.addArguments("--disable-extensions");
        opciones.addArguments("--no-sandbox");
        opciones.addArguments("--disable-dev-shm-usage");
        opciones.addArguments("--disable-gpu");
        opciones.addArguments("--remote-allow-origins=*");
        opciones.addArguments("--disable-web-security");
        opciones.addArguments("--allow-running-insecure-content");
        
        // Configuración para manejo de propaganda y notificaciones
        opciones.addArguments("--disable-popup-blocking");
        opciones.addArguments("--disable-notifications");
        opciones.addArguments("--disable-infobars");
        
        // User Agent personalizado
        opciones.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
        
        // Configuración experimental para evitar detección
        opciones.setExperimentalOption("useAutomationExtension", false);
        opciones.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        
        if (modoSinCabeza) {
            String[] argumentosHeadless = TipoNavegador.CHROME.obtenerArgumentosHeadless();
            opciones.addArguments(argumentosHeadless);
        }
        
        logger.debug("Chrome configurado exitosamente");
        return new ChromeDriver(opciones);
    }
    
    /**
     * Configura Firefox con las opciones especificadas.
     * @param modoSinCabeza si debe ejecutarse en modo headless
     * @return WebDriver de Firefox configurado
     */
    private static WebDriver configurarFirefox(boolean modoSinCabeza) {
        logger.debug("Configurando Firefox con WebDriverManager");
        WebDriverManager.firefoxdriver().setup();
        
        FirefoxOptions opciones = new FirefoxOptions();
        
        // Configuraciones básicas para evitar notificaciones
        opciones.addPreference("dom.webnotifications.enabled", false);
        opciones.addPreference("dom.push.enabled", false);
        opciones.addPreference("dom.popup_allowed_events", "");
        opciones.addPreference("browser.download.folderList", 2);
        opciones.addPreference("browser.helperApps.neverAsk.saveToDisk", "application/pdf");
        
        // User Agent personalizado
        opciones.addPreference("general.useragent.override", 
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:120.0) Gecko/20100101 Firefox/120.0");
        
        if (modoSinCabeza) {
            String[] argumentosHeadless = TipoNavegador.FIREFOX.obtenerArgumentosHeadless();
            opciones.addArguments(argumentosHeadless);
        }
        
        logger.debug("Firefox configurado exitosamente");
        return new FirefoxDriver(opciones);
    }
    
    /**
     * Configura Edge con las opciones especificadas.
     * @param modoSinCabeza si debe ejecutarse en modo headless
     * @return WebDriver de Edge configurado
     */
    private static WebDriver configurarEdge(boolean modoSinCabeza) {
        logger.debug("Configurando Edge con WebDriverManager");
        WebDriverManager.edgedriver().setup();
        
        EdgeOptions opciones = new EdgeOptions();
        
        // Opciones similares a Chrome (Edge está basado en Chromium)
        opciones.addArguments("--disable-blink-features=AutomationControlled");
        opciones.addArguments("--disable-extensions");
        opciones.addArguments("--no-sandbox");
        opciones.addArguments("--disable-dev-shm-usage");
        opciones.addArguments("--remote-allow-origins=*");
        opciones.addArguments("--disable-popup-blocking");
        opciones.addArguments("--disable-notifications");
        
        if (modoSinCabeza) {
            String[] argumentosHeadless = TipoNavegador.EDGE.obtenerArgumentosHeadless();
            opciones.addArguments(argumentosHeadless);
        }
        
        logger.debug("Edge configurado exitosamente");
        return new EdgeDriver(opciones);
    }
    
    /**
     * Configura Safari con las opciones especificadas.
     * Nota: Safari solo funciona en macOS
     * @param modoSinCabeza si debe ejecutarse en modo headless (no soportado en Safari)
     * @return WebDriver de Safari configurado
     */
    private static WebDriver configurarSafari(boolean modoSinCabeza) {
        logger.debug("Configurando Safari");
        
        if (modoSinCabeza) {
            logger.warn("Safari no soporta modo headless, ejecutando en modo normal");
        }
        
        SafariOptions opciones = new SafariOptions();
        // Safari tiene opciones limitadas comparado con otros navegadores
        
        logger.debug("Safari configurado exitosamente");
        return new SafariDriver(opciones);
    }
    
    /**
     * Configura los tiempos de espera para el driver.
     * @param driver instancia del WebDriver a configurar
     */
    private static void configurarTiemposEspera(WebDriver driver) {
        logger.debug("Configurando timeouts: implícito={}s, carga={}s", 
                    TIEMPO_ESPERA_IMPLICITA, TIEMPO_CARGA_PAGINA);
        
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(TIEMPO_ESPERA_IMPLICITA));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(TIEMPO_CARGA_PAGINA));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(TIEMPO_CARGA_PAGINA));
    }
    
    /**
     * Obtiene el tipo de navegador desde una cadena de texto.
     * @param navegador nombre del navegador
     * @return TipoNavegador correspondiente
     */
    public static TipoNavegador obtenerTipoNavegador(String navegador) {
        if (navegador == null || navegador.trim().isEmpty()) {
            logger.debug("Navegador no especificado, usando Chrome por defecto");
            return TipoNavegador.CHROME;
        }
        
        try {
            TipoNavegador tipo = TipoNavegador.desdeString(navegador);
            logger.debug("Navegador '{}' convertido a: {}", navegador, tipo);
            return tipo;
        } catch (Exception e) {
            logger.warn("Navegador '{}' no reconocido, usando Chrome por defecto: {}", 
                       navegador, e.getMessage());
            return TipoNavegador.CHROME;
        }
    }
    
    /**
     * Verifica si un tipo de navegador está soportado en el sistema actual
     * @param tipo tipo de navegador a verificar
     * @return true si está soportado
     */
    public static boolean esNavegadorSoportado(TipoNavegador tipo) {
        return tipo.esSoportadoEnSistemaActual();
    }
    
    /**
     * Obtiene todos los navegadores soportados en el sistema actual
     * @return array de navegadores soportados
     */
    public static TipoNavegador[] obtenerNavegadoresSoportados() {
        return TipoNavegador.obtenerNavegadoresSoportados();
    }
    
    /**
     * Obtiene información detallada del navegador configurado
     * @param tipo tipo de navegador
     * @return información del navegador
     */
    public static String obtenerInformacionNavegador(TipoNavegador tipo) {
        return tipo.toStringDetallado();
    }
}