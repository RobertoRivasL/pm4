package com.automatizacion.proyecto.paginas;

import com.automatizacion.proyecto.utilidades.EsperaExplicita;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Clase base para todas las páginas (Page Object Model)
 * @author Roberto Rivas Lopez
 */
public abstract class PaginaBase {
    protected WebDriver driver;
    protected EsperaExplicita espera;
    
    public PaginaBase(WebDriver driver, EsperaExplicita espera) {
        this.driver = driver;
        this.espera = espera;
    }
    
    protected void hacerClicElemento(By localizador) {
        WebElement elemento = espera.esperarElementoClickeable(localizador);
        elemento.click();
    }
    
    protected void ingresarTextoEnCampo(By localizador, String texto) {
        WebElement campo = espera.esperarElementoVisible(localizador);
        campo.clear();
        campo.sendKeys(texto);
    }
    
    protected String obtenerTextoElemento(By localizador) {
        WebElement elemento = espera.esperarElementoVisible(localizador);
        return elemento.getText().trim();
    }
    
    protected boolean esElementoVisible(By localizador) {
        try {
            espera.esperarElementoVisible(localizador);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public String obtenerTituloPagina() {
        return driver.getTitle();
    }
    
    public String obtenerUrlActual() {
        return driver.getCurrentUrl();
    }
    
    public abstract boolean esPaginaVisible();
}
