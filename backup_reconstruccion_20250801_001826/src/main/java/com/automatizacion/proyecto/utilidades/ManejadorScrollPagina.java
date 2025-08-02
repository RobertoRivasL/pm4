package com.automatizacion.proyecto.utilidades;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase utilitaria para manejar operaciones de scroll en páginas web.
 * Proporciona métodos para desplazarse por la página y manejar propaganda.
 * 
 * Implementa el principio de Responsabilidad Única enfocándose
 * únicamente en operaciones de scroll y desplazamiento.
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
     * @param driver instancia de WebDriver
     */
    public ManejadorScrollPagina(WebDriver driver) {
        this.driver = driver;
        this.jsExecutor = (JavascriptExecutor) driver;
        logger.debug("ManejadorScrollPagina inicializado");
    }
    
    /**
     * Hace scroll hacia arriba de la página completamente
     */
    public void subirAlTope() {
        try {
            jsExecutor.executeScript("window.scrollTo(0, 0);");
            Thread.sleep(500); // Pausa breve para que se complete el scroll
            logger.debug("Scroll al tope de la página ejecutado");
        } catch (Exception e) {
            logger.error("Error haciendo scroll al tope: " + e.getMessage());
        }
    }
    
    /**
     * Hace scroll hacia abajo de la página completamente
     */
    public void bajarAlFondo() {
        try {
            jsExecutor.executeScript("window.scrollTo(0, document.body.scrollHeight);");
            Thread.sleep(500);
            logger.debug("Scroll al fondo de la página ejecutado");
        } catch (Exception e) {
            logger.error("Error haciendo scroll al fondo: " + e.getMessage());
        }
    }
    
    /**
     * Hace scroll hasta un elemento específico
     * @param elemento elemento hacia el cual hacer scroll
     */
    public void scrollHastaElemento(WebElement elemento) {
        try {
            jsExecutor.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", elemento);
            Thread.sleep(1000); // Pausa para el smooth scroll
            logger.debug("Scroll hasta elemento ejecutado");
        } catch (Exception e) {
            logger.error("Error haciendo scroll hasta elemento: " + e.getMessage());
        }
    }
    
    /**
     * Hace scroll vertical por una cantidad específica de píxeles
     * @param pixeles cantidad de píxeles (positivo = hacia abajo, negativo = hacia arriba)
     */
    public void scrollVertical(int pixeles) {
        try {
            jsExecutor.executeScript("window.scrollBy(0, " + pixeles + ");");
            Thread.sleep(300);
            logger.debug("Scroll vertical de " + pixeles + " píxeles ejecutado");
        } catch (Exception e) {
            logger.error("Error en scroll vertical: " + e.getMessage());
        }
    }
    
    /**
     * Método específico para subir la página y evitar propaganda
     * Este método es útil cuando hay anuncios o pop-ups que interfieren
     */
    public void subirPaginaParaEvitarPropaganda() {
        try {
            // Subir completamente al tope
            subirAlTope();
            
            // Pequeña pausa adicional
            Thread.sleep(800);
            
            // Verificar si hay elementos de propaganda y ocultarlos
            ocultarElementosPropaganda();
            
            logger.debug("Página subida y propaganda manejada");
        } catch (Exception e) {
            logger.error("Error evitando propaganda: " + e.getMessage());
        }
    }
    
    /**
     * Intenta ocultar elementos comunes de propaganda
     */
    private void ocultarElementosPropaganda() {
        try {
            // Lista de selectores comunes para propaganda
            String[] selectoresPropaganda = {
                ".advertisement", ".ad-banner", ".popup", ".modal", 
                "#ad-container", "[id*='ad']", "[class*='ad-']", 
                ".google-ads", ".banner-ad"
            };
            
            for (String selector : selectoresPropaganda) {
                String script = 
                    "var elementos = document.querySelectorAll('" + selector + "'); " +
                    "for (var i = 0; i < elementos.length; i++) { " +
                    "    elementos[i].style.display = 'none'; " +
                    "}";
                
                jsExecutor.executeScript(script);
            }
            
            logger.debug("Elementos de propaganda procesados");
        } catch (Exception e) {
            logger.debug("No se pudieron ocultar elementos de propaganda: " + e.getMessage());
        }
    }
    
    /**
     * Obtiene la posición actual del scroll vertical
     * @return posición actual en píxeles
     */
    public int obtenerPosicionScrollVertical() {
        try {
            Long posicion = (Long) jsExecutor.executeScript("return window.pageYOffset;");
            return posicion.intValue();
        } catch (Exception e) {
            logger.error("Error obteniendo posición scroll: " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * Verifica si se está en el tope de la página
     * @return true si está en el tope
     */
    public boolean estaEnElTope() {
        return obtenerPosicionScrollVertical() == 0;
    }
}
