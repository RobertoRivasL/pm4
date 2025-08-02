package main.java.com.automatizacion.proyecto.configuracion;

import main.java.com.automatizacion.proyecto.enums.TipoNavegador;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
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
    public static WebDriver crearDriver(TipoNavegador tipoNavegador, boolean modoSinCabeza) {
        WebDriver driver;
        
        logger.info("Creando driver para navegador: {} (Headless: {})", 
                   tipoNavegador.obtenerNombreCompleto(), modoSinCabeza);
        
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
        
        if (!modoSinCabeza) {
            driver.manage().window().maximize();
        }
        
        logger.info("Driver {} creado exitosamente", tipoNavegador.obtenerNombreCompleto());
        return driver;
    }
    
    /**
     * Configura Chrome con las opciones especificadas.
     */
    private static WebDriver configurarChrome(boolean modoSinCabeza) {
        try {
            WebDriverManager.chromedriver().setup();
            ChromeOptions opciones = new ChromeOptions();
            
            // Opciones básicas de rendimiento y estabilidad
            opciones.addArguments("--disable-blink-features=AutomationControlled");
            opciones.addArguments("--disable-extensions");
            opciones.addArguments("--no-sandbox");
            opciones.addArguments("--disable-dev-shm-usage");
            opciones.addArguments("--disable-gpu");
            opciones.addArguments("--remote-allow-origins=*");
            opciones.addArguments("--disable-web-security");
            opciones.addArguments("--allow-running-insecure-content");
            
            // Configuración para evitar detección de automatización
            opciones.setExperimentalOption("useAutomationExtension", false);
            opciones.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
            
            // Configuración para manejo de notificaciones y popups
            opciones.addArguments("--disable-popup-blocking");
            opciones.addArguments("--disable-notifications");
            opciones.addArguments("--disable-infobars");
            
            // Configuración de User-Agent personalizado
            opciones.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
            
            if (modoSinCabeza) {
                opciones.addArguments("--headless=new");
                opciones.addArguments("--window-size=1920,1080");
            }
            
            return new ChromeDriver(opciones);
            
        } catch (Exception e) {
            logger.error("Error configurando Chrome: {}", e.getMessage());
            throw new RuntimeException("No se pudo configurar Chrome", e);
        }
    }
    
    /**
     * Configura Firefox con las opciones especificadas.
     */
    private static WebDriver configurarFirefox(boolean modoSinCabeza) {
        try {
            WebDriverManager.firefoxdriver().setup();
            FirefoxOptions opciones = new FirefoxOptions();
            
            // Configuraciones básicas de Firefox
            opciones.addPreference("dom.webnotifications.enabled", false);
            opciones.addPreference("dom.push.enabled", false);
            opciones.addPreference("dom.popup_allowed_events", "");
            opciones.addPreference("media.navigator.permission.disabled", true);
            opciones.addPreference("geo.enabled", false);
            
            // Configuración de rendimiento
            opciones.addPreference("browser.download.folderList", 2);
            opciones.addPreference("browser.helperApps.neverAsk.saveToDisk", "application/octet-stream");
            
            if (modoSinCabeza) {
                opciones.addArguments("--headless");
                opciones.addArguments("--width=1920");
                opciones.addArguments("--height=1080");
            }
            
            return new FirefoxDriver(opciones);
            
        } catch (Exception e) {
            logger.error("Error configurando Firefox: {}", e.getMessage());
            throw new RuntimeException("No se pudo configurar Firefox", e);
        }
    }
    
    /**
     * Configura Edge con las opciones especificadas.
     */
    private static WebDriver configurarEdge(boolean modoSinCabeza) {
        try {
            WebDriverManager.edgedriver().setup();
            EdgeOptions opciones = new EdgeOptions();
            
            // Opciones similares a Chrome ya que Edge usa Chromium
            opciones.addArguments("--disable-blink-features=AutomationControlled");
            opciones.addArguments("--disable-extensions");
            opciones.addArguments("--no-sandbox");
            opciones.addArguments("--disable-dev-shm-usage");
            opciones.addArguments("--remote-allow-origins=*");
            opciones.addArguments("--disable-popup-blocking");
            opciones.addArguments("--disable-notifications");
            
            if (modoSinCabeza) {
                opciones.addArguments("--headless=new");
                opciones.addArguments("--window-size=1920,1080");
            }
            
            return new EdgeDriver(opciones);
            
        } catch (Exception e) {
            logger.error("Error configurando Edge: {}", e.getMessage());
            throw new RuntimeException("No se pudo configurar Edge", e);
        }
    }
    
    /**
     * Configura Safari (solo disponible en macOS).
     */
    private static WebDriver configurarSafari(boolean modoSinCabeza) {
        try {
            String sistemaOperativo = System.getProperty("os.name").toLowerCase();
            if (!sistemaOperativo.contains("mac")) {
                throw new UnsupportedOperationException("Safari solo está disponible en macOS");
            }
            
            if (modoSinCabeza) {
                logger.warn("Safari no soporta modo headless, ejecutándose en modo normal");
            }
            
            // Safari no requiere WebDriverManager ya que viene preinstalado en macOS
            return new org.openqa.selenium.safari.SafariDriver();
            
        } catch (Exception e) {
            logger.error("Error configurando Safari: {}", e.getMessage());
            throw new RuntimeException("No se pudo configurar Safari", e);
        }
    }
    
    /**
     * Configura los tiempos de espera para el driver.
     */
    private static void configurarTiemposEspera(WebDriver driver) {
        try {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(TIEMPO_ESPERA_IMPLICITA));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(TIEMPO_CARGA_PAGINA));
            driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(TIEMPO_CARGA_PAGINA));
            
            logger.debug("Tiempos de espera configurados - Implícito: {}s, Carga: {}s", 
                        TIEMPO_ESPERA_IMPLICITA, TIEMPO_CARGA_PAGINA);
                        
        } catch (Exception e) {
            logger.warn("Error configurando tiempos de espera: {}", e.getMessage());
        }
    }
    
    /**
     * Verifica si un navegador está disponible en el sistema.
     */
    public static boolean esNavegadorDisponible(TipoNavegador tipoNavegador) {
        try {
            // Verificar si el navegador está soportado en el sistema actual
            if (!tipoNavegador.esSoportadoEnSistemaActual()) {
                return false;
            }
            
            // Intentar configurar el WebDriverManager sin crear el driver
            switch (tipoNavegador) {
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
                    // Safari solo está disponible en macOS y no requiere setup adicional
                    return System.getProperty("os.name").toLowerCase().contains("mac");
                default:
                    return false;
            }
            
        } catch (Exception e) {
            logger.debug("Navegador {} no disponible: {}", tipoNavegador.obtenerNombreCompleto(), e.getMessage());
            return false;
        }
    }
    
    /**
     * Obtiene una lista de navegadores disponibles en el sistema.
     */
    public static java.util.List<TipoNavegador> obtenerNavegadoresDisponibles() {
        java.util.List<TipoNavegador> navegadoresDisponibles = new java.util.ArrayList<>();
        
        for (TipoNavegador navegador : TipoNavegador.values()) {
            if (esNavegadorDisponible(navegador)) {
                navegadoresDisponibles.add(navegador);
                logger.debug("Navegador disponible: {}", navegador.obtenerNombreCompleto());
            }
        }
        
        return navegadoresDisponibles;
    }
    
    /**
     * Crea un driver con configuración por defecto.
     */
    public static WebDriver crearDriverPorDefecto() {
        return crearDriver(TipoNavegador.CHROME, false);
    }
    
    /**
     * Cierra un driver de forma segura.
     */
    public static void cerrarDriver(WebDriver driver) {
        if (driver != null) {
            try {
                driver.quit();
                logger.debug("Driver cerrado exitosamente");
            } catch (Exception e) {
                logger.warn("Error al cerrar driver: {}", e.getMessage());
            }
        }
    }
}