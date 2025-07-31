package com.automatizacion.proyecto.configuracion;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;

/**
 * Clase responsable de la configuración y creación de instancias de WebDriver.
 * Implementa el patrón Factory para crear diferentes tipos de navegadores.
 * 
 * Principios aplicados:
 * - SRP: Solo se encarga de la configuración de navegadores
 * - OCP: Fácil extensión para nuevos navegadores
 * - DIP: Devuelve la abstracción WebDriver
 */
public class ConfiguracionNavegador {
    
    private static final int TIEMPO_ESPERA_IMPLICITA = 10;
    private static final int TIEMPO_CARGA_PAGINA = 30;
    
    public enum TipoNavegador {
        CHROME, FIREFOX, EDGE
    }
    
    /**
     * Crea una instancia de WebDriver basada en el tipo especificado.
     * 
     * @param tipoNavegador El tipo de navegador a crear
     * @param modoSinCabeza Si el navegador debe ejecutarse en modo headless
     * @return Instancia configurada de WebDriver
     * @throws IllegalArgumentException Si el tipo de navegador no es soportado
     */
    public static WebDriver crearNavegador(TipoNavegador tipoNavegador, boolean modoSinCabeza) {
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
            default:
                throw new IllegalArgumentException("Tipo de navegador no soportado: " + tipoNavegador);
        }
        
        configurarTiemposEspera(driver);
        driver.manage().window().maximize();
        
        return driver;
    }
    
    /**
     * Configura Chrome con las opciones especificadas.
     */
    private static WebDriver configurarChrome(boolean modoSinCabeza) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions opciones = new ChromeOptions();
        
        // Opciones básicas
        opciones.addArguments("--disable-blink-features=AutomationControlled");
        opciones.addArguments("--disable-extensions");
        opciones.addArguments("--no-sandbox");
        opciones.addArguments("--disable-dev-shm-usage");
        opciones.addArguments("--disable-gpu");
        opciones.addArguments("--remote-allow-origins=*");
        
        // Configuración para evitar detección de automatización
        opciones.setExperimentalOption("useAutomationExtension", false);
        opciones.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        
        // Configuración para manejo de propaganda
        opciones.addArguments("--disable-popup-blocking");
        opciones.addArguments("--disable-notifications");
        
        if (modoSinCabeza) {
            opciones.addArguments("--headless=new");
        }
        
        return new ChromeDriver(opciones);
    }
    
    /**
     * Configura Firefox con las opciones especificadas.
     */
    private static WebDriver configurarFirefox(boolean modoSinCabeza) {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions opciones = new FirefoxOptions();
        
        // Configuraciones básicas
        opciones.addPreference("dom.webnotifications.enabled", false);
        opciones.addPreference("dom.push.enabled", false);
        opciones.addPreference("dom.popup_allowed_events", "");
        
        if (modoSinCabeza) {
            opciones.addArguments("--headless");
        }
        
        return new FirefoxDriver(opciones);
    }
    
    /**
     * Configura Edge con las opciones especificadas.
     */
    private static WebDriver configurarEdge(boolean modoSinCabeza) {
        WebDriverManager.edgedriver().setup();
        EdgeOptions opciones = new EdgeOptions();
        
        opciones.addArguments("--disable-blink-features=AutomationControlled");
        opciones.addArguments("--disable-extensions");
        opciones.addArguments("--no-sandbox");
        opciones.addArguments("--disable-dev-shm-usage");
        opciones.addArguments("--remote-allow-origins=*");
        
        if (modoSinCabeza) {
            opciones.addArguments("--headless=new");
        }
        
        return new EdgeDriver(opciones);
    }
    
    /**
     * Configura los tiempos de espera para el driver.
     */
    private static void configurarTiemposEspera(WebDriver driver) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(TIEMPO_ESPERA_IMPLICITA));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(TIEMPO_CARGA_PAGINA));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(TIEMPO_CARGA_PAGINA));
    }
    
    /**
     * Obtiene el tipo de navegador desde una cadena de texto.
     */
    public static TipoNavegador obtenerTipoNavegador(String navegador) {
        if (navegador == null || navegador.trim().isEmpty()) {
            return TipoNavegador.CHROME; // Por defecto Chrome
        }
        
        switch (navegador.toLowerCase().trim()) {
            case "chrome":
                return TipoNavegador.CHROME;
            case "firefox":
                return TipoNavegador.FIREFOX;
            case "edge":
                return TipoNavegador.EDGE;
            default:
                throw new IllegalArgumentException("Navegador no soportado: " + navegador);
        }
    }
}