package com.automatizacion.proyecto.utilidades;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

/**
 * Utilidad para esperas explícitas
 * @author Roberto Rivas Lopez
 */
public class EsperaExplicita {
    private final WebDriverWait espera;
    
    public EsperaExplicita(WebDriver driver, int timeoutSegundos) {
        this.espera = new WebDriverWait(driver, Duration.ofSeconds(timeoutSegundos));
    }
    
    public WebElement esperarElementoVisible(By localizador) {
        return espera.until(ExpectedConditions.visibilityOfElementLocated(localizador));
    }
    
    public WebElement esperarElementoClickeable(By localizador) {
        return espera.until(ExpectedConditions.elementToBeClickable(localizador));
    }
    
    public WebElement esperarElementoPresente(By localizador) {
        return espera.until(ExpectedConditions.presenceOfElementLocated(localizador));
    }
    
    public boolean esperarUrlContiene(String fragmentoUrl) {
        return espera.until(ExpectedConditions.urlContains(fragmentoUrl));
    }
}
