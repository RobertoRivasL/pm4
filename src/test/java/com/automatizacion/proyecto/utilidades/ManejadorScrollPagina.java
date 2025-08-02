package com.automatizacion.proyecto.utilidades;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.automatizacion.proyecto.enums.TipoMensaje;

/**
 * Clase utilitaria para manejo de operaciones de scroll en páginas web.
 * Proporciona métodos robustos para desplazamiento de página y elementos.
 * 
 * Implementa el principio de Responsabilidad Única (SRP) al enfocarse
 * exclusivamente en operaciones de scroll.
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
public class ManejadorScrollPagina {
    
    private static final Logger logger = LoggerFactory.getLogger(ManejadorScrollPagina.class);
    
    private final WebDriver driver;
    private final JavascriptExecutor jsExecutor;
    
    /**
     * Constructor que inicializa el manejador con el driver
     * 
     * @param driver instancia de WebDriver
     */
    public ManejadorScrollPagina(WebDriver driver) {
        this.driver = driver;
        this.jsExecutor = (JavascriptExecutor) driver;
        
        logger.debug(TipoMensaje.DEBUG.formatearMensaje("ManejadorScrollPagina inicializado"));
    }
    
    /**
     * Hace scroll hasta un elemento específico
     * 
     * @param elemento elemento hacia el cual hacer scroll
     */
    public void scrollHastaElemento(WebElement elemento) {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Haciendo scroll hacia elemento"));
            
            jsExecutor.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", elemento);
            
            // Pausa breve para que se complete el scroll
            Thread.sleep(500);
            
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Scroll hacia elemento completado"));
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error haciendo scroll hacia elemento: " + e.getMessage()));
        }
    }
    
    /**
     * Hace scroll hacia arriba de la página
     */
    public void scrollHaciaArriba() {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Haciendo scroll hacia arriba"));
            
            jsExecutor.executeScript("window.scrollTo({top: 0, behavior: 'smooth'});");
            
            Thread.sleep(500);
            
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Scroll hacia arriba completado"));
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error haciendo scroll hacia arriba: " + e.getMessage()));
        }
    }
    
    /**
     * Hace scroll hacia abajo de la página
     */
    public void scrollHaciaAbajo() {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Haciendo scroll hacia abajo"));
            
            jsExecutor.executeScript("window.scrollTo({top: document.body.scrollHeight, behavior: 'smooth'});");
            
            Thread.sleep(500);
            
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Scroll hacia abajo completado"));
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error haciendo scroll hacia abajo: " + e.getMessage()));
        }
    }
    
    /**
     * Hace scroll por una cantidad específica de píxeles
     * 
     * @param pixeles cantidad de píxeles (positivo = abajo, negativo = arriba)
     */
    public void scrollPorPixeles(int pixeles) {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "Haciendo scroll por " + pixeles + " píxeles"));
            
            jsExecutor.executeScript("window.scrollBy({top: " + pixeles + ", behavior: 'smooth'});");
            
            Thread.sleep(300);
            
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Scroll por píxeles completado"));
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error haciendo scroll por píxeles: " + e.getMessage()));
        }
    }
    
    /**
     * Obtiene la posición actual del scroll vertical
     * 
     * @return posición actual del scroll en píxeles
     */
    public long obtenerPosicionScrollVertical() {
        try {
            return (Long) jsExecutor.executeScript("return window.pageYOffset;");
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error obteniendo posición de scroll: " + e.getMessage()));
            return 0;
        }
    }
    
    /**
     * Verifica si se puede hacer scroll hacia abajo
     * 
     * @return true si se puede hacer scroll hacia abajo
     */
    public boolean puedeScrollAbajo() {
        try {
            return (Boolean) jsExecutor.executeScript(
                "return (window.innerHeight + window.scrollY) < document.body.offsetHeight;");
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error verificando scroll hacia abajo: " + e.getMessage()));
            return false;
        }
    }
}