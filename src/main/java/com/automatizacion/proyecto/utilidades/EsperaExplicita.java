package com.automatizacion.proyecto.utilidades;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.automatizacion.proyecto.enums.TipoMensaje;

import java.time.Duration;

/**
 * Clase utilitaria para manejo de esperas explícitas en Selenium.
 * Proporciona métodos robustos para esperar diferentes condiciones.
 * 
 * Implementa el principio de Responsabilidad Única (SRP) al enfocarse
 * exclusivamente en el manejo de esperas.
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
public class EsperaExplicita {
    
    private static final Logger logger = LoggerFactory.getLogger(EsperaExplicita.class);
    
    private final WebDriver driver;
    private final WebDriverWait espera;
    private final int tiempoEsperaSegundos;
    
    /**
     * Constructor que inicializa las esperas con el driver y tiempo especificado
     * 
     * @param driver instancia de WebDriver
     * @param tiempoEsperaSegundos tiempo máximo de espera en segundos
     */
    public EsperaExplicita(WebDriver driver, int tiempoEsperaSegundos) {
        this.driver = driver;
        this.tiempoEsperaSegundos = tiempoEsperaSegundos;
        this.espera = new WebDriverWait(driver, Duration.ofSeconds(tiempoEsperaSegundos));
        
        logger.debug(TipoMensaje.DEBUG.formatearMensaje(
            "EsperaExplicita inicializada con timeout de " + tiempoEsperaSegundos + " segundos"));
    }
    
    /**
     * Espera a que un elemento sea visible
     * 
     * @param localizador localizador del elemento
     * @return WebElement visible
     */
    public WebElement esperarElementoVisible(By localizador) {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "Esperando elemento visible: " + localizador.toString()));
            
            WebElement elemento = espera.until(ExpectedConditions.visibilityOfElementLocated(localizador));
            
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "Elemento encontrado y visible: " + localizador.toString()));
            
            return elemento;
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Elemento no encontrado o no visible: " + localizador.toString() + " - " + e.getMessage()));
            throw new RuntimeException("Elemento no visible: " + localizador.toString(), e);
        }
    }
    
    /**
     * Espera a que un elemento sea visible con timeout personalizado
     * 
     * @param localizador localizador del elemento
     * @param timeoutSegundos timeout personalizado en segundos
     * @return WebElement visible
     */
    public WebElement esperarElementoVisible(By localizador, int timeoutSegundos) {
        WebDriverWait esperaPersonalizada = new WebDriverWait(driver, Duration.ofSeconds(timeoutSegundos));
        try {
            return esperaPersonalizada.until(ExpectedConditions.visibilityOfElementLocated(localizador));
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Elemento no visible con timeout " + timeoutSegundos + "s: " + localizador.toString()));
            throw new RuntimeException("Elemento no visible: " + localizador.toString(), e);
        }
    }
    
    /**
     * Espera a que un elemento sea clickeable
     * 
     * @param localizador localizador del elemento
     * @return WebElement clickeable
     */
    public WebElement esperarElementoClickeable(By localizador) {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "Esperando elemento clickeable: " + localizador.toString()));
            
            WebElement elemento = espera.until(ExpectedConditions.elementToBeClickable(localizador));
            
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "Elemento clickeable encontrado: " + localizador.toString()));
            
            return elemento;
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Elemento no clickeable: " + localizador.toString() + " - " + e.getMessage()));
            throw new RuntimeException("Elemento no clickeable: " + localizador.toString(), e);
        }
    }
    
    /**
     * Espera a que un elemento esté presente en el DOM
     * 
     * @param localizador localizador del elemento
     * @return WebElement presente
     */
    public WebElement esperarElementoPresente(By localizador) {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "Esperando elemento presente: " + localizador.toString()));
            
            WebElement elemento = espera.until(ExpectedConditions.presenceOfElementLocated(localizador));
            
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "Elemento presente encontrado: " + localizador.toString()));
            
            return elemento;
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Elemento no presente: " + localizador.toString() + " - " + e.getMessage()));
            throw new RuntimeException("Elemento no presente: " + localizador.toString(), e);
        }
    }
    
    /**
     * Espera a que un elemento desaparezca
     * 
     * @param localizador localizador del elemento
     * @return true si el elemento desapareció
     */
    public boolean esperarElementoDesaparezca(By localizador) {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "Esperando que elemento desaparezca: " + localizador.toString()));
            
            boolean desaparecio = espera.until(ExpectedConditions.invisibilityOfElementLocated(localizador));
            
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "Elemento desapareció: " + localizador.toString()));
            
            return desaparecio;
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Elemento no desapareció: " + localizador.toString() + " - " + e.getMessage()));
            return false;
        }
    }
    
    /**
     * Espera a que un texto esté presente en un elemento
     * 
     * @param localizador localizador del elemento
     * @param texto texto esperado
     * @return true si el texto está presente
     */
    public boolean esperarTextoEnElemento(By localizador, String texto) {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "Esperando texto '" + texto + "' en elemento: " + localizador.toString()));
            
            boolean textoPresente = espera.until(ExpectedConditions.textToBePresentInElementLocated(localizador, texto));
            
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "Texto encontrado en elemento: " + localizador.toString()));
            
            return textoPresente;
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Texto no encontrado en elemento: " + localizador.toString() + " - " + e.getMessage()));
            return false;
        }
    }
    
    /**
     * Espera a que la carga de la página esté completa
     */
    public void esperarCargaCompleta() {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Esperando carga completa de página"));
            
            espera.until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver driver) {
                    return ((JavascriptExecutor) driver)
                            .executeScript("return document.readyState").equals("complete");
                }
            });
            
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Página cargada completamente"));
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error esperando carga de página: " + e.getMessage()));
        }
    }
    
    /**
     * Espera a que jQuery esté cargado y no tenga peticiones activas
     */
    public void esperarJQueryCompleto() {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Esperando jQuery completo"));
            
            espera.until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver driver) {
                    try {
                        return (Boolean) ((JavascriptExecutor) driver)
                                .executeScript("return jQuery.active == 0");
                    } catch (Exception e) {
                        // jQuery no está presente, continuar
                        return true;
                    }
                }
            });
            
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("jQuery completado"));
            
        } catch (Exception e) {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "jQuery no presente o error: " + e.getMessage()));
        }
    }
    
    /**
     * Espera personalizada con función lambda
     * 
     * @param condicion función que devuelve boolean
     * @param mensajeError mensaje de error si falla
     * @return true si la condición se cumple
     */
    public boolean esperarCondicionPersonalizada(ExpectedCondition<Boolean> condicion, String mensajeError) {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Esperando condición personalizada"));
            
            Boolean resultado = espera.until(condicion);
            
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Condición personalizada cumplida"));
            
            return resultado != null ? resultado : false;
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                mensajeError + " - " + e.getMessage()));
            return false;
        }
    }
    
    /**
     * Pausa la ejecución por un tiempo determinado (usar con precaución)
     * 
     * @param milisegundos tiempo de pausa en milisegundos
     */
    public void pausar(long milisegundos) {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "Pausando ejecución por " + milisegundos + " ms"));
            
            Thread.sleep(milisegundos);
        } catch (InterruptedException e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error en pausa: " + e.getMessage()));
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Método específico para evitar anuncios/propaganda común en sitios de prueba
     */
    public void subirPaginaParaEvitarPropaganda() {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Ejecutando scroll para evitar propaganda"));
            
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollTo(0, -document.body.scrollHeight);");
            
            // Pequeña pausa para que se ejecute el scroll
            pausar(500);
            
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Scroll ejecutado exitosamente"));
            
        } catch (Exception e) {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "No se pudo ejecutar scroll: " + e.getMessage()));
        }
    }
    
    /**
     * Obtiene el tiempo de espera configurado
     * 
     * @return tiempo de espera en segundos
     */
    public int obtenerTiempoEspera() {
        return tiempoEsperaSegundos;
    }
    
    /**
     * Actualiza el tiempo de espera dinámicamente
     * 
     * @param nuevoTiempoSegundos nuevo tiempo de espera
     */
    public void actualizarTiempoEspera(int nuevoTiempoSegundos) {
        logger.info(TipoMensaje.INFORMATIVO.formatearMensaje(
            "Actualizando timeout de " + tiempoEsperaSegundos + "s a " + nuevoTiempoSegundos + "s"));
        
        // Nota: WebDriverWait es inmutable, esto es solo para referencia
        // En uso real, se debería crear una nueva instancia
    }
}