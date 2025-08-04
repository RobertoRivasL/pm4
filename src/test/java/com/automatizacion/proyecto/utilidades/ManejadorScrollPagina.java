package com.automatizacion.proyecto.utilidades;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ManejadorScrollPagina {
    
    private final WebDriver driver;
    private final JavascriptExecutor jsExecutor;
    
    public ManejadorScrollPagina(WebDriver driver) {
        this.driver = driver;
        this.jsExecutor = (JavascriptExecutor) driver;
    }
    
    public void scrollHastaElemento(WebElement elemento) {
        try {
            jsExecutor.executeScript("arguments[0].scrollIntoView(true);", elemento);
            Thread.sleep(500); // Pausa breve para el scroll
        } catch (Exception e) {
            // Log del error si es necesario
        }
    }
    
    public void scrollAlInicio() {
        jsExecutor.executeScript("window.scrollTo(0, 0);");
    }
    
    public void scrollAlFinal() {
        jsExecutor.executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }
}