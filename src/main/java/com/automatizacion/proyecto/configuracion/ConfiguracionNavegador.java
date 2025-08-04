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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;

public class ConfiguracionNavegador {
    
    private static final Logger logger = LoggerFactory.getLogger(ConfiguracionNavegador.class);
    private final ConfiguracionGlobal configuracion;
    
    public ConfiguracionNavegador() {
        this.configuracion = ConfiguracionGlobal.obtenerInstancia();
    }
    
    public WebDriver crearWebDriver() {
        String tipoNavegador = configuracion.obtenerPropiedad(ConfiguracionGlobal.NAVEGADOR_TIPO, "CHROME");
        boolean headless = configuracion.obtenerPropiedadBoolean(ConfiguracionGlobal.NAVEGADOR_HEADLESS, false);
        
        WebDriver driver = null;
        
        try {
            switch (tipoNavegador.toUpperCase()) {
                case "CHROME":
                    driver = crearChromeDriver(headless);
                    break;
                case "FIREFOX":
                    driver = crearFirefoxDriver(headless);
                    break;
                case "EDGE":
                    driver = crearEdgeDriver(headless);
                    break;
                default:
                    logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("Navegador no soportado: " + tipoNavegador + ", usando Chrome"));
                    driver = crearChromeDriver(headless);
            }
            
            configurarTimeouts(driver);
            maximizarVentana(driver);
            
            logger.info(TipoMensaje.EXITO.formatearMensaje("WebDriver creado: " + tipoNavegador + (headless ? " (headless)" : "")));
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error creando WebDriver: " + e.getMessage()));
            throw new RuntimeException("No se pudo crear el WebDriver", e);
        }
        
        return driver;
    }
    
    private WebDriver crearChromeDriver(boolean headless) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions opciones = new ChromeOptions();
        opciones.addArguments("--no-sandbox");
        opciones.addArguments("--disable-dev-shm-usage");
        opciones.addArguments("--disable-gpu");
        opciones.addArguments("--remote-allow-origins=*");
        
        if (headless) {
            opciones.addArguments("--headless");
        }
        
        return new ChromeDriver(opciones);
    }
    
    private WebDriver crearFirefoxDriver(boolean headless) {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions opciones = new FirefoxOptions();
        
        if (headless) {
            opciones.addArguments("--headless");
        }
        
        return new FirefoxDriver(opciones);
    }
    
    private WebDriver crearEdgeDriver(boolean headless) {
        WebDriverManager.edgedriver().setup();
        EdgeOptions opciones = new EdgeOptions();
        
        if (headless) {
            opciones.addArguments("--headless");
        }
        
        return new EdgeDriver(opciones);
    }
    
    private void configurarTimeouts(WebDriver driver) {
        int timeoutImplicito = configuracion.obtenerPropiedadInt(ConfiguracionGlobal.TIMEOUT_IMPLICITO, 10);
        int timeoutExplicito = configuracion.obtenerPropiedadInt(ConfiguracionGlobal.TIMEOUT_EXPLICITO, 10);
        
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeoutImplicito));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(timeoutExplicito * 2));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(timeoutExplicito));
    }
    
    private void maximizarVentana(WebDriver driver) {
        try {
            driver.manage().window().maximize();
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("No se pudo maximizar la ventana: " + e.getMessage()));
        }
    }
}