package com.automatizacion.proyecto.utilidades;

import com.automatizacion.proyecto.enums.TipoMensaje;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase responsable del manejo de scroll en páginas web.
 * Proporciona métodos para realizar diferentes tipos de scroll de forma robusta.
 * 
 * Principios aplicados:
 * - SRP: Solo maneja operaciones de scroll
 * - Abstracción: Oculta complejidad de JavaScript
 * - Reutilización: Métodos genéricos para diferentes necesidades
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
public class ManejadorScrollPagina {
    
    private static final Logger logger = LoggerFactory.getLogger(ManejadorScrollPagina.class);
    
    private final WebDriver driver;
    private final JavascriptExecutor jsExecutor;
    private final EsperaExplicita espera;
    
    // Constantes para configuración de scroll
    private static final int PAUSA_DESPUES_SCROLL = 500; // milisegundos
    private static final int PIXELS_SCROLL_SUAVE = 100;
    private static final String SCROLL_BEHAVIOR_SMOOTH = "smooth";
    private static final String SCROLL_BEHAVIOR_AUTO = "auto";
    
    /**
     * Constructor que inicializa el manejador de scroll
     * @param driver instancia de WebDriver
     */
    public ManejadorScrollPagina(WebDriver driver) {
        this.driver = driver;
        this.jsExecutor = (JavascriptExecutor) driver;
        this.espera = new EsperaExplicita(driver);
        
        logger.debug(TipoMensaje.DEBUG.formatearMensaje("ManejadorScrollPagina inicializado"));
    }
    
    // === MÉTODOS DE SCROLL BÁSICOS ===
    
    /**
     * Hace scroll hacia arriba de la página
     */
    public void scrollHaciaArriba() {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Realizando scroll hacia arriba"));
            
            jsExecutor.executeScript("window.scrollTo(0, 0);");
            esperarDespuesDelScroll();
            
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Scroll hacia arriba completado"));
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error en scroll hacia arriba: " + e.getMessage()));
        }
    }
    
    /**
     * Hace scroll hacia abajo de la página
     */
    public void scrollHaciaAbajo() {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Realizando scroll hacia abajo"));
            
            jsExecutor.executeScript("window.scrollTo(0, document.body.scrollHeight);");
            esperarDespuesDelScroll();
            
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Scroll hacia abajo completado"));
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error en scroll hacia abajo: " + e.getMessage()));
        }
    }
    
    /**
     * Hace scroll a una posición específica
     * @param x coordenada horizontal
     * @param y coordenada vertical
     */
    public void scrollAPosicion(int x, int y) {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "Realizando scroll a posición: (" + x + ", " + y + ")"));
            
            jsExecutor.executeScript("window.scrollTo(" + x + ", " + y + ");");
            esperarDespuesDelScroll();
            
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Scroll a posición completado"));
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error en scroll a posición: " + e.getMessage()));
        }
    }
    
    // === SCROLL HACIA ELEMENTOS ===
    
    /**
     * Hace scroll hasta que un elemento sea visible
     * @param elemento elemento objetivo
     */
    public void scrollHastaElemento(WebElement elemento) {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Realizando scroll hasta elemento"));
            
            jsExecutor.executeScript("arguments[0].scrollIntoView({behavior: '" + 
                SCROLL_BEHAVIOR_SMOOTH + "', block: 'center'});", elemento);
            esperarDespuesDelScroll();
            
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Scroll hasta elemento completado"));
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error en scroll hasta elemento: " + e.getMessage()));
        }
    }
    
    /**
     * Hace scroll hasta un elemento localizado por By
     * @param localizador localizador del elemento
     */
    public void scrollHastaElemento(By localizador) {
        try {
            WebElement elemento = espera.esperarElementoPresente(localizador);
            scrollHastaElemento(elemento);
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error en scroll hasta elemento por localizador: " + e.getMessage()));
        }
    }
    
    /**
     * Centra un elemento en la pantalla
     * @param elemento elemento a centrar
     */
    public void centrarElemento(WebElement elemento) {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Centrando elemento en pantalla"));
            
            jsExecutor.executeScript(
                "arguments[0].scrollIntoView({behavior: '" + SCROLL_BEHAVIOR_SMOOTH + 
                "', block: 'center', inline: 'center'});", elemento);
            esperarDespuesDelScroll();
            
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Elemento centrado"));
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error al centrar elemento: " + e.getMessage()));
        }
    }
    
    // === SCROLL INCREMENTAL ===
    
    /**
     * Hace scroll hacia abajo de forma incremental
     * @param pixels cantidad de pixels para hacer scroll
     */
    public void scrollIncremental(int pixels) {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "Realizando scroll incremental: " + pixels + " pixels"));
            
            jsExecutor.executeScript("window.scrollBy(0, " + pixels + ");");
            esperarDespuesDelScroll();
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error en scroll incremental: " + e.getMessage()));
        }
    }
    
    /**
     * Hace scroll suave hacia abajo
     */
    public void scrollSuaveHaciaAbajo() {
        scrollIncremental(PIXELS_SCROLL_SUAVE);
    }
    
    /**
     * Hace scroll suave hacia arriba
     */
    public void scrollSuaveHaciaArriba() {
        scrollIncremental(-PIXELS_SCROLL_SUAVE);
    }
    
    // === SCROLL ESPECÍFICO PARA PRUEBAS ===
    
    /**
     * Sube la página para evitar elementos que interfieren (como propaganda)
     */
    public void subirPaginaParaEvitarPropaganda() {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "Subiendo página para evitar elementos que interfieren"));
            
            jsExecutor.executeScript("window.scrollBy(0, -100);");
            esperarDespuesDelScroll();
            
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                "Error al subir página: " + e.getMessage()));
        }
    }
    
    /**
     * Hace scroll hasta que un elemento esté completamente visible
     * @param elemento elemento objetivo
     * @return true si el elemento está completamente visible
     */
    public boolean scrollHastaElementoCompletamenteVisible(WebElement elemento) {
        try {
            // Primero hacer scroll básico hasta el elemento
            scrollHastaElemento(elemento);
            
            // Verificar si está completamente visible
            Boolean completamenteVisible = (Boolean) jsExecutor.executeScript(
                "var elem = arguments[0];" +
                "var rect = elem.getBoundingClientRect();" +
                "return (rect.top >= 0 && rect.left >= 0 && " +
                "rect.bottom <= window.innerHeight && " +
                "rect.right <= window.innerWidth);", elemento);
            
            if (completamenteVisible != null && completamenteVisible) {
                logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                    "Elemento completamente visible"));
                return true;
            } else {
                // Ajustar posición si no está completamente visible
                centrarElemento(elemento);
                return true;
            }
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error verificando visibilidad completa: " + e.getMessage()));
            return false;
        }
    }
    
    // === SCROLL EN CONTENEDORES ===
    
    /**
     * Hace scroll dentro de un contenedor específico
     * @param contenedor elemento contenedor
     * @param pixels cantidad de pixels para scroll
     */
    public void scrollEnContenedor(WebElement contenedor, int pixels) {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "Realizando scroll en contenedor: " + pixels + " pixels"));
            
            jsExecutor.executeScript("arguments[0].scrollTop += " + pixels + ";", contenedor);
            esperarDespuesDelScroll();
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error en scroll de contenedor: " + e.getMessage()));
        }
    }
    
    /**
     * Hace scroll horizontal
     * @param pixels cantidad de pixels para scroll horizontal
     */
    public void scrollHorizontal(int pixels) {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "Realizando scroll horizontal: " + pixels + " pixels"));
            
            jsExecutor.executeScript("window.scrollBy(" + pixels + ", 0);");
            esperarDespuesDelScroll();
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error en scroll horizontal: " + e.getMessage()));
        }
    }
    
    // === UTILIDADES DE SCROLL ===
    
    /**
     * Obtiene la posición actual de scroll
     * @return array con posición [x, y]
     */
    public long[] obtenerPosicionScroll() {
        try {
            Long scrollX = (Long) jsExecutor.executeScript("return window.pageXOffset;");
            Long scrollY = (Long) jsExecutor.executeScript("return window.pageYOffset;");
            
            return new long[]{
                scrollX != null ? scrollX : 0,
                scrollY != null ? scrollY : 0
            };
            
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                "Error obteniendo posición de scroll: " + e.getMessage()));
            return new long[]{0, 0};
        }
    }
    
    /**
     * Obtiene la altura total de la página
     * @return altura total en pixels
     */
    public long obtenerAlturaTotalPagina() {
        try {
            Long altura = (Long) jsExecutor.executeScript("return document.body.scrollHeight;");
            return altura != null ? altura : 0;
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                "Error obteniendo altura de página: " + e.getMessage()));
            return 0;
        }
    }
    
    /**
     * Verifica si se puede hacer más scroll hacia abajo
     * @return true si se puede hacer más scroll
     */
    public boolean pudeHacerMasScrollAbajo() {
        try {
            Boolean resultado = (Boolean) jsExecutor.executeScript(
                "return (window.innerHeight + window.pageYOffset) < document.body.scrollHeight;");
            return resultado != null && resultado;
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                "Error verificando scroll disponible: " + e.getMessage()));
            return false;
        }
    }
    
    /**
     * Espera después del scroll para permitir que se complete la animación
     */
    private void esperarDespuesDelScroll() {
        espera.pausar(PAUSA_DESPUES_SCROLL);
    }
}