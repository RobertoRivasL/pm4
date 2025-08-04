package com.automatizacion.proyecto.utilidades;

import com.automatizacion.proyecto.configuracion.ConfiguracionGlobal;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class EsperaExplicita {
    
    private final WebDriver driver;
    private final WebDriverWait wait;
    
    public EsperaExplicita(WebDriver driver) {
        this.driver = driver;
        int timeout = ConfiguracionGlobal.obtenerInstancia().obtenerPropiedadInt(ConfiguracionGlobal.TIMEOUT_EXPLICITO, 10);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
    }
    
    public boolean esperarElementoVisible(WebElement elemento) {
        try {
            wait.until(ExpectedConditions.visibilityOf(elemento));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean esperarElementoClickeable(WebElement elemento) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(elemento));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}