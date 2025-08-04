package com.automatizacion.proyecto.configuracion;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;

/**
 * Factory para la creación y configuración de WebDrivers.
 * Implementa el patrón Factory Method para crear instancias de WebDriver
 * configuradas según los requerimientos específicos.
 * 
 * SOLUCIÓN A PROBLEMAS ESPECÍFICOS:
 * - Manejo correcto de ventanas y pestañas
 * - Configuración optimizada para formularios de login
 * - Gestión automática de drivers
 * - Timeouts específicos para login
 * 
 * Principios aplicados:
 * - Factory Method: Creación de objetos WebDriver
 * - Single Responsibility: Solo maneja creación de drivers
 * - Open/Closed: Extensible para nuevos navegadores
 * 
 * @author Antonio B. Arriagada LL. (anarriag@gmail.com)
 * @author Dante Escalona Bustos (Jacobo.bustos.22@gmail.com)  
 * @author Roberto Rivas Lopez (umancl@gmail.com)
 * @version 1.0
 */
public final class ConfiguradorNavegador {
    
    private static final Logger logger = LoggerFactory.getLogger(ConfiguradorNavegador.class);
    
    // Configuraciones por defecto (sin dependencia de ConfiguracionGlobal)
    private static final int TIMEOUT_IMPLICITO = 10;
    private static final int TIMEOUT_EXPLICITO = 15;
    private static final int TIMEOUT_CARGA_PAGINA = 30;
    private static final int ANCHO_VENTANA = 1920;
    private static final int ALTO_VENTANA = 1080;
    private static final boolean HEADLESS_DEFAULT = false;
    private static final boolean MAXIMIZAR_DEFAULT = true;
    
    /**
     * Enum para tipos de navegador (definido internamente)
     */
    public enum TipoNavegador {
        CHROME("Chrome", "chrome"),
        FIREFOX("Firefox", "firefox"),
        EDGE("Edge", "edge");
        
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
    
    // Constructor privado para clase utilitaria
    private ConfiguradorNavegador() {
        throw new UnsupportedOperationException("Clase utilitaria - no debe ser instanciada");
    }
    
    /**
     * Crea y configura un WebDriver según el tipo especificado
     * MEJORADO: Soluciona problemas de login y capturas
     * 
     * @param tipoNavegador tipo de navegador a crear
     * @return WebDriver configurado y listo para usar
     * @throws IllegalArgumentException si el tipo de navegador no es soportado
     */
    public static WebDriver crearNavegador(TipoNavegador tipoNavegador) {
        logger.info("Creando navegador: {}", tipoNavegador.getNombreCompleto());
        
        WebDriver driver = null;
        
        try {
            switch (tipoNavegador) {
                case CHROME -> driver = crearChrome();
                case FIREFOX -> driver = crearFirefox();
                case EDGE -> driver = crearEdge();
                default -> throw new IllegalArgumentException(
                    "Tipo de navegador no soportado: " + tipoNavegador);
            }
            
            configurarNavegadorGeneral(driver);
            
            logger.info("Navegador {} creado exitosamente", tipoNavegador.getNombreCompleto());
            
            return driver;
            
        } catch (Exception e) {
            logger.error("Error al crear navegador {}: {}", tipoNavegador, e.getMessage());
            
            if (driver != null) {
                try {
                    driver.quit();
                } catch (Exception ex) {
                    logger.warn("Error al cerrar driver fallido: {}", ex.getMessage());
                }
            }
            throw new RuntimeException("No se pudo crear el navegador", e);
        }
    }
    
    /**
     * Crea un WebDriver por defecto (Chrome)
     * @return WebDriver configurado
     */
    public static WebDriver crearNavegadorPorDefecto() {
        return crearNavegador(TipoNavegador.CHROME);
    }
    
    /**
     * Crea navegador basado en configuración del sistema
     * @param navegadorConfig nombre del navegador ("chrome", "firefox", "edge")
     * @return WebDriver configurado
     */
    public static WebDriver crearNavegadorDesdeConfiguracion(String navegadorConfig) {
        if (navegadorConfig == null || navegadorConfig.trim().isEmpty()) {
            return crearNavegadorPorDefecto();
        }
        
        String navegadorLower = navegadorConfig.toLowerCase().trim();
        
        return switch (navegadorLower) {
            case "chrome" -> crearNavegador(TipoNavegador.CHROME);
            case "firefox" -> crearNavegador(TipoNavegador.FIREFOX);
            case "edge" -> crearNavegador(TipoNavegador.EDGE);
            default -> {
                logger.warn("Navegador no reconocido '{}', usando Chrome por defecto", navegadorConfig);
                yield crearNavegadorPorDefecto();
            }
        };
    }
    
    // === MÉTODOS PRIVADOS DE CREACIÓN ===
    
    /**
     * Crea y configura ChromeDriver con opciones optimizadas para login
     */
    private static WebDriver crearChrome() {
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions opciones = new ChromeOptions();
        
        // Configuraciones básicas
        opciones.addArguments("--no-sandbox");
        opciones.addArguments("--disable-dev-shm-usage");
        opciones.addArguments("--disable-gpu");
        opciones.addArguments("--disable-extensions");
        
        // CRÍTICO: Configuraciones para evitar problemas de login
        opciones.addArguments("--disable-blink-features=AutomationControlled");
        opciones.setExperimentalOption("excludeSwitches", Arrays.asList("enable-automation"));
        opciones.addArguments("--disable-web-security");
        opciones.addArguments("--allow-running-insecure-content");
        
        // Configuración de ventana para capturas correctas
        if (obtenerConfiguracionHeadless()) {
            opciones.addArguments("--headless=new");
            opciones.addArguments("--window-size=" + ANCHO_VENTANA + "," + ALTO_VENTANA);
        } else {
            opciones.addArguments("--start-maximized");
        }
        
        // Configuraciones específicas para formularios
        opciones.addArguments("--disable-popup-blocking");
        opciones.addArguments("--disable-default-apps");
        opciones.addArguments("--disable-background-timer-throttling");
        
        // Configuración de user agent para evitar detección
        opciones.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
        
        logger.debug("Configurando ChromeDriver con opciones optimizadas");
        
        return new ChromeDriver(opciones);
    }
    
    /**
     * Crea y configura FirefoxDriver
     */
    private static WebDriver crearFirefox() {
        WebDriverManager.firefoxdriver().setup();
        
        FirefoxOptions opciones = new FirefoxOptions();
        
        // Configuraciones básicas
        if (obtenerConfiguracionHeadless()) {
            opciones.addArguments("--headless");
        }
        
        // Configuraciones específicas para Firefox
        opciones.addArguments("--width=" + ANCHO_VENTANA);
        opciones.addArguments("--height=" + ALTO_VENTANA);
        opciones.addArguments("--disable-blink-features=AutomationControlled");
        
        // Preferencias para formularios
        opciones.addPreference("dom.webnotifications.enabled", false);
        opciones.addPreference("media.volume_scale", "0.0");
        
        logger.debug("Configurando FirefoxDriver");
        
        return new FirefoxDriver(opciones);
    }
    
    /**
     * Crea y configura EdgeDriver
     */
    private static WebDriver crearEdge() {
        WebDriverManager.edgedriver().setup();
        
        EdgeOptions opciones = new EdgeOptions();
        
        // Configuraciones similares a Chrome (basado en Chromium)
        opciones.addArguments("--no-sandbox");
        opciones.addArguments("--disable-dev-shm-usage");
        opciones.addArguments("--disable-gpu");
        opciones.addArguments("--disable-blink-features=AutomationControlled");
        
        if (obtenerConfiguracionHeadless()) {
            opciones.addArguments("--headless=new");
            opciones.addArguments("--window-size=" + ANCHO_VENTANA + "," + ALTO_VENTANA);
        } else {
            opciones.addArguments("--start-maximized");
        }
        
        logger.debug("Configurando EdgeDriver");
        
        return new EdgeDriver(opciones);
    }
    
    /**
     * Aplica configuraciones generales a cualquier WebDriver
     * CRÍTICO: Configuraciones específicas para login
     */
    private static void configurarNavegadorGeneral(WebDriver driver) {
        // Timeouts específicos para formularios de login
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(TIMEOUT_IMPLICITO));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(TIMEOUT_CARGA_PAGINA));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(TIMEOUT_EXPLICITO));
        
        // Configuración de ventana para capturas correctas
        if (!obtenerConfiguracionHeadless() && MAXIMIZAR_DEFAULT) {
            driver.manage().window().maximize();
            logger.debug("Ventana maximizada");
        }
        
        // IMPORTANTE: Eliminamos todas las cookies y storage para login limpio
        driver.manage().deleteAllCookies();
        
        logger.info("Navegador configurado - Timeouts: {}s", TIMEOUT_EXPLICITO);
    }
    
    /**
     * Crea un WebDriverWait configurado para el driver
     * @param driver WebDriver base
     * @return WebDriverWait configurado
     */
    public static WebDriverWait crearEsperaExplicita(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_EXPLICITO));
    }
    
    /**
     * Crea un WebDriverWait con timeout personalizado
     * @param driver WebDriver base
     * @param timeoutSegundos timeout en segundos
     * @return WebDriverWait configurado
     */
    public static WebDriverWait crearEsperaPersonalizada(WebDriver driver, int timeoutSegundos) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSegundos));
    }
    
    /**
     * Cierra correctamente el navegador y libera recursos
     * SOLUCIÓN: Cierre correcto de ventanas de login
     * 
     * @param driver WebDriver a cerrar
     */
    public static void cerrarNavegador(WebDriver driver) {
        if (driver != null) {
            try {
                // Cerrar todas las ventanas/pestañas abiertas
                if (driver.getWindowHandles().size() > 1) {
                    for (String windowHandle : driver.getWindowHandles()) {
                        driver.switchTo().window(windowHandle);
                        driver.close();
                    }
                    logger.debug("Cerradas múltiples ventanas");
                }
                
                // Cerrar el navegador completamente
                driver.quit();
                logger.info("Navegador cerrado correctamente");
                
            } catch (Exception e) {
                logger.warn("Error al cerrar navegador: {}", e.getMessage());
                
                // Forzar cierre en caso de error
                try {
                    driver.quit();
                } catch (Exception ex) {
                    logger.error("Error crítico al forzar cierre: {}", ex.getMessage());
                }
            }
        }
    }
    
    /**
     * Verifica si el navegador está funcionando correctamente
     * @param driver WebDriver a verificar
     * @return true si está funcionando
     */
    public static boolean verificarNavegador(WebDriver driver) {
        try {
            driver.getCurrentUrl();
            return true;
        } catch (Exception e) {
            logger.error("Navegador no responde: {}", e.getMessage());
            return false;
        }
    }
    
    // === MÉTODOS DE CONFIGURACIÓN ===
    
    /**
     * Obtiene la configuración de headless desde properties del sistema
     * @return true si debe ejecutar en modo headless
     */
    private static boolean obtenerConfiguracionHeadless() {
        String headlessProperty = System.getProperty("headless");
        if (headlessProperty != null) {
            return Boolean.parseBoolean(headlessProperty);
        }
        return HEADLESS_DEFAULT;
    }
    
    /**
     * Obtiene el tipo de navegador desde properties del sistema
     * @return TipoNavegador configurado
     */
    public static TipoNavegador obtenerTipoNavegadorDesdeConfiguracion() {
        String browserProperty = System.getProperty("browser");
        if (browserProperty == null) {
            return TipoNavegador.CHROME;
        }
        
        return switch (browserProperty.toLowerCase()) {
            case "firefox" -> TipoNavegador.FIREFOX;
            case "edge" -> TipoNavegador.EDGE;
            default -> TipoNavegador.CHROME;
        };
    }
    
    /**
     * Configura el navegador basado en parámetros del sistema
     * Útil para ejecución desde Maven o línea de comandos
     * 
     * @return WebDriver configurado según parámetros del sistema
     */
    public static WebDriver crearNavegadorDesdeParametrosSistema() {
        TipoNavegador tipo = obtenerTipoNavegadorDesdeConfiguracion();
        logger.info("Creando navegador desde configuración del sistema: {}", tipo);
        return crearNavegador(tipo);
    }
}