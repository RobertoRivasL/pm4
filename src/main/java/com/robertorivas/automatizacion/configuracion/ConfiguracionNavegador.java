package com.robertorivas.automatizacion.configuracion;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase responsable de la configuración y creación de instancias de WebDriver
 * para diferentes navegadores.
 * 
 * Principios aplicados:
 * - Single Responsibility: Solo maneja la configuración de navegadores
 * - Open/Closed: Abierto para extensión (nuevos navegadores)
 * - Factory Pattern: Para la creación de drivers
 * 
 * @author Roberto Rivas Lopez
 */
public class ConfiguracionNavegador {
    
    private static final Logger logger = LoggerFactory.getLogger(ConfiguracionNavegador.class);
    
    // Enum para tipos de navegador (principio de encapsulación)
    public enum TipoNavegador {
        CHROME("chrome"),
        FIREFOX("firefox"),
        EDGE("edge"),
        CHROME_HEADLESS("chrome-headless"),
        FIREFOX_HEADLESS("firefox-headless");
        
        private final String nombre;
        
        TipoNavegador(String nombre) {
            this.nombre = nombre;
        }
        
        public String getNombre() {
            return nombre;
        }
        
        public static TipoNavegador obtenerPorNombre(String nombre) {
            for (TipoNavegador tipo : values()) {
                if (tipo.nombre.equalsIgnoreCase(nombre)) {
                    return tipo;
                }
            }
            throw new IllegalArgumentException("Navegador no soportado: " + nombre);
        }
    }
    
    private static final int TIEMPO_ESPERA_IMPLICITA = 10;
    private static final int TIEMPO_ESPERA_PAGINA = 30;
    
    /**
     * Crea una instancia de WebDriver basada en el tipo de navegador especificado.
     * 
     * @param tipoNavegador El tipo de navegador a crear
     * @return Instancia configurada de WebDriver
     */
    public static WebDriver crearDriver(TipoNavegador tipoNavegador) {
        logger.info("Iniciando configuración del navegador: {}", tipoNavegador.getNombre());
        
        WebDriver driver = switch (tipoNavegador) {
            case CHROME -> crearDriverChrome(false);
            case CHROME_HEADLESS -> crearDriverChrome(true);
            case FIREFOX -> crearDriverFirefox(false);
            case FIREFOX_HEADLESS -> crearDriverFirefox(true);
            case EDGE -> crearDriverEdge();
        };
        
        configurarDriver(driver);
        logger.info("Navegador {} configurado exitosamente", tipoNavegador.getNombre());
        
        return driver;
    }
    
    /**
     * Crea una instancia de WebDriver basada en el nombre del navegador.
     * 
     * @param nombreNavegador Nombre del navegador como string
     * @return Instancia configurada de WebDriver
     */
    public static WebDriver crearDriver(String nombreNavegador) {
        TipoNavegador tipo = TipoNavegador.obtenerPorNombre(nombreNavegador);
        return crearDriver(tipo);
    }
    
    /**
     * Crea y configura ChromeDriver.
     */
    private static WebDriver crearDriverChrome(boolean headless) {
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions opciones = new ChromeOptions();
        
        if (headless) {
            opciones.addArguments("--headless");
        }
        
        // Configuraciones adicionales para Chrome
        opciones.addArguments("--no-sandbox");
        opciones.addArguments("--disable-dev-shm-usage");
        opciones.addArguments("--disable-gpu");
        opciones.addArguments("--window-size=1920,1080");
        opciones.addArguments("--disable-extensions");
        opciones.addArguments("--disable-web-security");
        opciones.addArguments("--allow-running-insecure-content");
        
        // Configurar descargas
        Map<String, Object> prefsDescargas = new HashMap<>();
        prefsDescargas.put("profile.default_content_settings.popups", 0);
        prefsDescargas.put("download.default_directory", System.getProperty("user.dir") + "/descargas");
        opciones.setExperimentalOption("prefs", prefsDescargas);
        
        return new ChromeDriver(opciones);
    }
    
    /**
     * Crea y configura FirefoxDriver.
     */
    private static WebDriver crearDriverFirefox(boolean headless) {
        WebDriverManager.firefoxdriver().setup();
        
        FirefoxOptions opciones = new FirefoxOptions();
        
        if (headless) {
            opciones.addArguments("--headless");
        }
        
        // Configuraciones adicionales para Firefox
        opciones.addArguments("--width=1920");
        opciones.addArguments("--height=1080");
        
        // Deshabilitar notificaciones
        opciones.addPreference("dom.webnotifications.enabled", false);
        opciones.addPreference("dom.push.enabled", false);
        
        return new FirefoxDriver(opciones);
    }
    
    /**
     * Crea y configura EdgeDriver.
     */
    private static WebDriver crearDriverEdge() {
        WebDriverManager.edgedriver().setup();
        
        EdgeOptions opciones = new EdgeOptions();
        opciones.addArguments("--no-sandbox");
        opciones.addArguments("--disable-dev-shm-usage");
        opciones.addArguments("--window-size=1920,1080");
        
        return new EdgeDriver(opciones);
    }
    
    /**
     * Aplica configuraciones comunes a todos los drivers.
     */
    private static void configurarDriver(WebDriver driver) {
        // Configurar timeouts
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(TIEMPO_ESPERA_IMPLICITA));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(TIEMPO_ESPERA_PAGINA));
        
        // Maximizar ventana
        driver.manage().window().maximize();
        
        logger.debug("Configuraciones aplicadas: timeout implícito = {}s, timeout página = {}s", 
                    TIEMPO_ESPERA_IMPLICITA, TIEMPO_ESPERA_PAGINA);
    }
    
    /**
     * Obtiene información del navegador y versión.
     */
    public static String obtenerInformacionNavegador(WebDriver driver) {
        try {
            String userAgent = (String) ((org.openqa.selenium.JavascriptExecutor) driver)
                    .executeScript("return navigator.userAgent;");
            return userAgent;
        } catch (Exception e) {
            logger.warn("No se pudo obtener información del navegador", e);
            return "Información no disponible";
        }
    }
    
    /**
     * Verifica si el driver está activo y funcional.
     */
    public static boolean verificarDriver(WebDriver driver) {
        try {
            driver.getTitle();
            return true;
        } catch (Exception e) {
            logger.error("El driver no está funcional", e);
            return false;
        }
    }
}