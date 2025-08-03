package com.automatizacion.proyecto.paginas;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.automatizacion.proyecto.utilidades.EsperaExplicita;
import com.automatizacion.proyecto.enums.TipoMensaje;

import java.time.Duration;

/**
 * Clase base para todos los Page Objects.
 * Implementa funcionalidad común y patrones de diseño.
 * 
 * Principios aplicados:
 * - SRP: Funcionalidad base común para todas las páginas
 * - DRY: Evita repetición de código en Page Objects
 * - Template Method: Define estructura común para páginas
 */
public abstract class PaginaBase {
    
    protected static final Logger logger = LoggerFactory.getLogger(PaginaBase.class);
    protected final WebDriver driver;
    protected final EsperaExplicita espera;
    protected final WebDriverWait wait;
    protected static final int TIEMPO_ESPERA_ELEMENTOS = 15;
    protected static final int MAX_REINTENTOS = 3;
    
    /**
     * Constructor base que inicializa el driver y las esperas.
     */
    protected PaginaBase(WebDriver driver, int tiempoEsperaSegundos) {
        this.driver = driver;
        this.espera = new EsperaExplicita(driver, tiempoEsperaSegundos);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(tiempoEsperaSegundos));
        PageFactory.initElements(driver, this);
        logger.debug(TipoMensaje.DEBUG.formatearMensaje("PaginaBase inicializada"));
    }
    
    /**
     * Constructor con tiempo de espera por defecto.
     */
    protected PaginaBase(WebDriver driver) {
        this(driver, TIEMPO_ESPERA_ELEMENTOS);
    }
    
    /**
     * Navega a la URL especificada.
     */
    public void navegarA(String url) {
        try {
            logger.info(TipoMensaje.NAVEGACION.formatearMensaje("Navegando a: " + url));
            driver.get(url);
            espera.esperarCargaCompleta();
            // Subir página para evitar propaganda después de cargar
            espera.subirPaginaParaEvitarPropaganda();
            logger.debug(TipoMensaje.EXITO.formatearMensaje("Navegación completada"));
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensajeError("Error al navegar a la URL: " + url, e));
            throw new RuntimeException("Error al navegar a la URL: " + url, e);
        }
    }
    
    /**
     * Obtiene el título de la página actual.
     */
    public String obtenerTituloPagina() {
        try {
            String titulo = driver.getTitle();
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Título obtenido: " + titulo));
            return titulo;
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensajeError("Error obteniendo título", e));
            return "";
        }
    }
    
    /**
     * Obtiene la URL actual.
     */
    public String obtenerUrlActual() {
        try {
            String url = driver.getCurrentUrl();
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("URL actual: " + url));
            return url;
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensajeError("Error obteniendo URL actual", e));
            return "";
        }
    }
    
    /**
     * Verifica si un elemento está visible.
     */
    protected boolean esElementoVisible(By localizador) {
        try {
            WebElement elemento = wait.until(ExpectedConditions.visibilityOfElementLocated(localizador));
            return elemento != null;
        } catch (Exception e) {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Elemento no visible: " + localizador));
            return false;
        }
    }
    
    /**
     * Verifica si un elemento está visible (sobrecarga para WebElement)
     */
    protected boolean esElementoVisible(WebElement elemento) {
        try {
            return elemento != null && elemento.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Hace clic en un elemento con manejo robusto.
     */
    protected void hacerClicEnElemento(By localizador) {
        try {
            WebElement elemento = wait.until(ExpectedConditions.elementToBeClickable(localizador));
            elemento.click();
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Clic realizado en: " + localizador));
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensajeError("Error haciendo clic en elemento", e));
            throw new RuntimeException("Error haciendo clic en elemento: " + localizador, e);
        }
    }
    
    /**
     * Hace clic en un elemento (sobrecarga para WebElement)
     */
    protected void hacerClicEnElemento(WebElement elemento) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(elemento));
            elemento.click();
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Clic realizado en elemento"));
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensajeError("Error haciendo clic en elemento", e));
            throw new RuntimeException("Error haciendo clic en elemento", e);
        }
    }
    
    /**
     * Escribe texto en un elemento
     */
    protected void escribirTexto(WebElement elemento, String texto) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(elemento));
            elemento.clear();
            elemento.sendKeys(texto);
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Texto escrito: " + texto));
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensajeError("Error escribiendo texto", e));
            throw new RuntimeException("Error escribiendo texto: " + texto, e);
        }
    }
    
    /**
     * Limpia y escribe texto en un elemento
     */
    protected void limpiarYEscribir(WebElement elemento, String texto) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(elemento));
            elemento.clear();
            Thread.sleep(100); // Pequeña pausa para asegurar que se limpió
            elemento.sendKeys(texto);
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Texto limpiado y escrito: " + texto));
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensajeError("Error limpiando y escribiendo texto", e));
            throw new RuntimeException("Error limpiando y escribiendo texto: " + texto, e);
        }
    }
    
    /**
     * Obtiene el texto de un elemento
     */
    protected String obtenerTexto(WebElement elemento) {
        try {
            wait.until(ExpectedConditions.visibilityOf(elemento));
            String texto = elemento.getText();
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Texto obtenido: " + texto));
            return texto;
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensajeError("Error obteniendo texto", e));
            return "";
        }
    }
    
    /**
     * Espera hasta que un elemento sea clickeable
     */
    protected WebElement esperarElementoClicable(WebElement elemento, int timeoutSegundos) {
        try {
            WebDriverWait localWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSegundos));
            return localWait.until(ExpectedConditions.elementToBeClickable(elemento));
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensajeError("Elemento no clickeable", e));
            throw new RuntimeException("Elemento no se volvió clickeable", e);
        }
    }
    
    /**
     * Espera hasta que un elemento sea visible
     */
    protected WebElement esperarElementoVisible(WebElement elemento, int timeoutSegundos) {
        try {
            WebDriverWait localWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSegundos));
            return localWait.until(ExpectedConditions.visibilityOf(elemento));
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensajeError("Elemento no visible", e));
            throw new RuntimeException("Elemento no se volvió visible", e);
        }
    }
    
    /**
     * Ejecuta JavaScript
     */
    protected Object ejecutarJavaScript(String script, Object... argumentos) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            return js.executeScript(script, argumentos);
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensajeError("Error ejecutando JavaScript", e));
            return null;
        }
    }
    
    /**
     * Hace scroll hasta un elemento
     */
    protected void scrollHastaElemento(WebElement elemento) {
        try {
            ejecutarJavaScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", elemento);
            Thread.sleep(500); // Pausa para que complete el scroll
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Scroll realizado hasta elemento"));
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensajeError("Error haciendo scroll", e));
        }
    }
    
    /**
     * Actualiza la página
     */
    public void actualizarPagina() {
        try {
            driver.navigate().refresh();
            espera.esperarCargaCompleta();
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Página actualizada"));
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensajeError("Error actualizando página", e));
        }
    }
    
    /**
     * Navega hacia atrás
     */
    public void navegarAtras() {
        try {
            driver.navigate().back();
            espera.esperarCargaCompleta();
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Navegación hacia atrás completada"));
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensajeError("Error navegando hacia atrás", e));
        }
    }
    
    /**
     * Espera a que se cargue la página
     */
    public boolean esperarCargaPagina(int timeoutSegundos) {
        try {
            WebDriverWait localWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSegundos));
            localWait.until(webDriver -> ((JavascriptExecutor) webDriver)
                    .executeScript("return document.readyState").equals("complete"));
            return true;
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensajeError("Timeout esperando carga de página", e));
            return false;
        }
    }
    
    /**
     * Método abstracto que debe implementar cada página
     */
    public abstract boolean esPaginaVisible();
}