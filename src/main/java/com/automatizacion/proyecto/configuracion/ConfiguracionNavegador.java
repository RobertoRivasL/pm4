package com.automatizacion.proyecto.configuracion;

import com.automatizacion.proyecto.enums.TipoNavegador;
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
 * Factory para crear instancias de WebDriver configuradas.
 * Implementa el patrón Factory Method.
 * 
 * @author Roberto Rivas Lopez
 */
public class ConfiguracionNavegador {
    
    private static final int TIEMPO_ESPERA_IMPLICITA = 10;
    private static final int TIEMPO_CARGA_PAGINA = 30;
    
    /**
     * Crea un WebDriver según el tipo y configuración especificada.
     * 
     * @param tipoNavegador tipo de navegador a crear
     * @param esHeadless si debe ejecutarse sin interfaz gráfica
     * @return instancia configurada de WebDriver
     * @throws IllegalArgumentException si el tipo no es soportado
     */
    public static WebDriver crearDriver(TipoNavegador tipoNavegador, boolean esHeadless) {
        WebDriver driver;
        
        switch (tipoNavegador) {
            case CHROME:
                driver = configurarChrome(esHeadless);
                break;
            case FIREFOX:
                driver = configurarFirefox(esHeadless);
                break;
            case EDGE:
                driver = configurarEdge(esHeadless);
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
    private static WebDriver configurarChrome(boolean esHeadless) {
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions opciones = new ChromeOptions();
        opciones.addArguments("--no-sandbox");
        opciones.addArguments("--disable-dev-shm-usage");
        opciones.addArguments("--disable-gpu");
        opciones.addArguments("--window-size=1920,1080");
        
        if (esHeadless) {
            opciones.addArguments("--headless");
        }
        
        // Opciones adicionales para estabilidad
        opciones.addArguments("--disable-web-security");
        opciones.addArguments("--allow-running-insecure-content");
        opciones.addArguments("--disable-extensions");
        
        return new ChromeDriver(opciones);
    }
    
    /**
     * Configura Firefox con las opciones especificadas.
     */
    private static WebDriver configurarFirefox(boolean esHeadless) {
        WebDriverManager.firefoxdriver().setup();
        
        FirefoxOptions opciones = new FirefoxOptions();
        
        if (esHeadless) {
            opciones.addArguments("--headless");
        }
        
        opciones.addArguments("--width=1920");
        opciones.addArguments("--height=1080");
        
        return new FirefoxDriver(opciones);
    }
    
    /**
     * Configura Edge con las opciones especificadas.
     */
    private static WebDriver configurarEdge(boolean esHeadless) {
        WebDriverManager.edgedriver().setup();
        
        EdgeOptions opciones = new EdgeOptions();
        opciones.addArguments("--no-sandbox");
        opciones.addArguments("--disable-dev-shm-usage");
        opciones.addArguments("--window-size=1920,1080");
        
        if (esHeadless) {
            opciones.addArguments("--headless");
        }
        
        return new EdgeDriver(opciones);
    }
    
    /**
     * Configura los tiempos de espera para el driver.
     */
    private static void configurarTiemposEspera(WebDriver driver) {
        driver.manage().timeouts()
            .implicitlyWait(Duration.ofSeconds(TIEMPO_ESPERA_IMPLICITA))
            .pageLoadTimeout(Duration.ofSeconds(TIEMPO_CARGA_PAGINA));
    }
}