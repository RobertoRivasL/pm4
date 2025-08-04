package com.automatizacion.proyecto.utilidades;

import com.automatizacion.proyecto.enums.TipoMensaje;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManejadorScrollPagina {
    
    private static final Logger logger = LoggerFactory.getLogger(ManejadorScrollPagina.class);
    private final WebDriver driver;
    private final JavascriptExecutor jsExecutor;
    
    public ManejadorScrollPagina(WebDriver driver) {
        this.driver = driver;
        this.jsExecutor = (JavascriptExecutor) driver;
    }
    
    public void scrollHastaElemento(WebElement elemento) {
        try {
            jsExecutor.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", elemento);
            Thread.sleep(1000); // Pausa para el scroll suave
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Scroll realizado hasta elemento"));
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("Error en scroll: " + e.getMessage()));
        }
    }
    
    public void scrollAlInicio() {
        try {
            jsExecutor.executeScript("window.scrollTo({top: 0, behavior: 'smooth'});");
            Thread.sleep(1000);
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Scroll al inicio realizado"));
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("Error scroll al inicio: " + e.getMessage()));
        }
    }
    
    public void scrollAlFinal() {
        try {
            jsExecutor.executeScript("window.scrollTo({top: document.body.scrollHeight, behavior: 'smooth'});");
            Thread.sleep(1000);
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Scroll al final realizado"));
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("Error scroll al final: " + e.getMessage()));
        }
    }
    
    // NUEVO: Scroll para mostrar formulario completo en capturas
    public void scrollParaCaptura() {
        try {
            // Buscar el formulario de registro
            WebElement formulario = driver.findElement(org.openqa.selenium.By.tagName("form"));
            if (formulario != null) {
                scrollHastaElemento(formulario);
            } else {
                // Si no encuentra form, scroll al primer input
                WebElement primerInput = driver.findElement(org.openqa.selenium.By.tagName("input"));
                scrollHastaElemento(primerInput);
            }
        } catch (Exception e) {
            scrollAlInicio(); // Fallback
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Scroll para captura - fallback al inicio"));
        }
    }
}