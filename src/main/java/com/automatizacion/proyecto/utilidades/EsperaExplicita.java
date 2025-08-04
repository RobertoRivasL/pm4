package com.automatizacion.proyecto.utilidades;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

/**
 * Utilidad para manejo de esperas explícitas en Selenium WebDriver.
 * 
 * FUNCIONALIDADES MEJORADAS:
 * - Esperas para elementos visibles/clickeables
 * - Esperas para texto y atributos
 * - Esperas para formularios de login
 * - Timeouts personalizables
 * - Logging detallado
 * 
 * Principios aplicados:
 * - Single Responsibility: Solo maneja esperas
 * - Dependency Injection: Recibe WebDriver como parámetro
 * - Fail-Safe: Manejo robusto de errores
 * 
 * @author Antonio B. Arriagada LL.
 * @author Dante Escalona Bustos
 * @author Roberto Rivas Lopez
 * @version 2.0 - Versión mejorada sin dependencias externas
 */
public class EsperaExplicita {
    
    private static final Logger logger = LoggerFactory.getLogger(EsperaExplicita.class);
    
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final int timeoutDefault;
    
    // Constantes para timeouts comunes
    private static final int TIMEOUT_DEFAULT = 15;
    private static final int TIMEOUT_CORTO = 5;
    private static final int TIMEOUT_LARGO = 30;
    
    /**
     * Constructor con timeout por defecto
     * @param driver WebDriver activo
     */
    public EsperaExplicita(WebDriver driver) {
        this(driver, TIMEOUT_DEFAULT);
    }
    
    /**
     * Constructor con timeout personalizado
     * @param driver WebDriver activo
     * @param timeoutSegundos timeout en segundos
     */
    public EsperaExplicita(WebDriver driver, int timeoutSegundos) {
        this.driver = driver;
        this.timeoutDefault = timeoutSegundos;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSegundos));
        
        logger.debug("EsperaExplicita inicializada con timeout: {}s", timeoutSegundos);
    }
    
    // === ESPERAS PARA ELEMENTOS (WebElement) ===
    
    /**
     * Espera a que un elemento sea visible
     * @param elemento WebElement a esperar
     * @return true si el elemento se vuelve visible
     */
    public boolean esperarElementoVisible(WebElement elemento) {
        try {
            wait.until(ExpectedConditions.visibilityOf(elemento));
            logger.debug("Elemento visible encontrado");
            return true;
        } catch (Exception e) {
            logger.warn("Timeout esperando elemento visible: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Espera a que un elemento sea clickeable
     * @param elemento WebElement a esperar
     * @return true si el elemento se vuelve clickeable
     */
    public boolean esperarElementoClickeable(WebElement elemento) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(elemento));
            logger.debug("Elemento clickeable encontrado");
            return true;
        } catch (Exception e) {
            logger.warn("Timeout esperando elemento clickeable: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Espera a que un elemento desaparezca
     * @param elemento WebElement a esperar que desaparezca
     * @return true si el elemento desaparece
     */
    public boolean esperarElementoInvisible(WebElement elemento) {
        try {
            wait.until(ExpectedConditions.invisibilityOf(elemento));
            logger.debug("Elemento se volvió invisible");
            return true;
        } catch (Exception e) {
            logger.warn("Timeout esperando elemento invisible: {}", e.getMessage());
            return false;
        }
    }
    
    // === ESPERAS PARA LOCALIZADORES (By) ===
    
    /**
     * Espera a que un elemento esté presente en el DOM
     * @param localizador By localizador del elemento
     * @return WebElement encontrado o null si timeout
     */
    public WebElement esperarPresenciaElemento(By localizador) {
        try {
            WebElement elemento = wait.until(ExpectedConditions.presenceOfElementLocated(localizador));
            logger.debug("Elemento presente encontrado: {}", localizador);
            return elemento;
        } catch (Exception e) {
            logger.warn("Timeout esperando presencia de elemento {}: {}", localizador, e.getMessage());
            return null;
        }
    }
    
    /**
     * Espera a que un elemento sea visible (usando localizador)
     * @param localizador By localizador del elemento
     * @return WebElement encontrado o null si timeout
     */
    public WebElement esperarVisibilidadElemento(By localizador) {
        try {
            WebElement elemento = wait.until(ExpectedConditions.visibilityOfElementLocated(localizador));
            logger.debug("Elemento visible encontrado: {}", localizador);
            return elemento;
        } catch (Exception e) {
            logger.warn("Timeout esperando visibilidad de elemento {}: {}", localizador, e.getMessage());
            return null;
        }
    }
    
    /**
     * Espera a que un elemento sea clickeable (usando localizador)
     * @param localizador By localizador del elemento
     * @return WebElement encontrado o null si timeout
     */
    public WebElement esperarElementoClickeable(By localizador) {
        try {
            WebElement elemento = wait.until(ExpectedConditions.elementToBeClickable(localizador));
            logger.debug("Elemento clickeable encontrado: {}", localizador);
            return elemento;
        } catch (Exception e) {
            logger.warn("Timeout esperando elemento clickeable {}: {}", localizador, e.getMessage());
            return null;
        }
    }
    
    // === ESPERAS PARA TEXTO Y ATRIBUTOS ===
    
    /**
     * Espera a que un elemento contenga texto específico
     * @param localizador By localizador del elemento
     * @param texto texto esperado
     * @return true si el elemento contiene el texto
     */
    public boolean esperarTextoEnElemento(By localizador, String texto) {
        try {
            wait.until(ExpectedConditions.textToBePresentInElementLocated(localizador, texto));
            logger.debug("Texto '{}' encontrado en elemento {}", texto, localizador);
            return true;
        } catch (Exception e) {
            logger.warn("Timeout esperando texto '{}' en elemento {}: {}", texto, localizador, e.getMessage());
            return false;
        }
    }
    
    /**
     * Espera a que un elemento tenga un atributo con valor específico
     * @param localizador By localizador del elemento
     * @param atributo nombre del atributo
     * @param valor valor esperado del atributo
     * @return true si el atributo tiene el valor esperado
     */
    public boolean esperarAtributoElemento(By localizador, String atributo, String valor) {
        try {
            wait.until(ExpectedConditions.attributeToBe(localizador, atributo, valor));
            logger.debug("Atributo '{}={}' encontrado en elemento {}", atributo, valor, localizador);
            return true;
        } catch (Exception e) {
            logger.warn("Timeout esperando atributo '{}={}' en elemento {}: {}", 
                atributo, valor, localizador, e.getMessage());
            return false;
        }
    }
    
    // === ESPERAS ESPECÍFICAS PARA FORMULARIOS DE LOGIN ===
    
    /**
     * Espera a que un formulario de login esté completamente cargado
     * ESPECÍFICO PARA LOGIN: Verifica campos usuario, password y botón
     * 
     * @param selectorFormulario selector del formulario
     * @param selectorUsuario selector del campo usuario
     * @param selectorPassword selector del campo password
     * @param selectorBoton selector del botón login
     * @return true si todos los elementos están presentes
     */
    public boolean esperarFormularioLoginCompleto(String selectorFormulario, 
                                                 String selectorUsuario, 
                                                 String selectorPassword, 
                                                 String selectorBoton) {
        try {
            logger.debug("Esperando formulario de login completo");
            
            // Esperar formulario principal
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(selectorFormulario)));
            
            // Esperar campos específicos
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(selectorUsuario)));
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(selectorPassword)));
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(selectorBoton)));
            
            logger.info("Formulario de login completamente cargado");
            return true;
            
        } catch (Exception e) {
            logger.error("Timeout esperando formulario de login completo: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Espera a que aparezca un mensaje de resultado de login
     * @param selectorMensaje selector del mensaje (error o éxito)
     * @return true si aparece el mensaje
     */
    public boolean esperarMensajeResultadoLogin(String selectorMensaje) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(selectorMensaje)));
            logger.debug("Mensaje de resultado de login apareció");
            return true;
        } catch (Exception e) {
            logger.warn("Timeout esperando mensaje de resultado de login: {}", e.getMessage());
            return false;
        }
    }
    
    // === ESPERAS PARA MÚLTIPLES ELEMENTOS ===
    
    /**
     * Espera a que aparezcan múltiples elementos
     * @param localizador By localizador de los elementos
     * @param cantidadMinima cantidad mínima de elementos esperados
     * @return lista de elementos encontrados o null si timeout
     */
    public List<WebElement> esperarMultiplesElementos(By localizador, int cantidadMinima) {
        try {
            List<WebElement> elementos = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(localizador, cantidadMinima - 1));
            logger.debug("Encontrados {} elementos con localizador {}", elementos.size(), localizador);
            return elementos;
        } catch (Exception e) {
            logger.warn("Timeout esperando {} elementos {}: {}", cantidadMinima, localizador, e.getMessage());
            return null;
        }
    }
    
    // === ESPERAS CON TIMEOUT PERSONALIZADO ===
    
    /**
     * Espera con timeout personalizado para elemento visible
     * @param localizador By localizador del elemento
     * @param timeoutSegundos timeout personalizado
     * @return WebElement encontrado o null si timeout
     */
    public WebElement esperarVisibilidadElementoConTimeout(By localizador, int timeoutSegundos) {
        WebDriverWait waitPersonalizado = new WebDriverWait(driver, Duration.ofSeconds(timeoutSegundos));
        try {
            WebElement elemento = waitPersonalizado.until(ExpectedConditions.visibilityOfElementLocated(localizador));
            logger.debug("Elemento visible encontrado con timeout personalizado {}s: {}", timeoutSegundos, localizador);
            return elemento;
        } catch (Exception e) {
            logger.warn("Timeout personalizado {}s esperando elemento {}: {}", timeoutSegundos, localizador, e.getMessage());
            return null;
        }
    }
    
    // === ESPERAS PARA CONDICIONES ESPECÍFICAS ===
    
    /**
     * Espera a que la página termine de cargar completamente
     * @return true si la página está completamente cargada
     */
    public boolean esperarCargaCompletaPagina() {
        try {
            wait.until(webDriver -> 
                ((org.openqa.selenium.JavascriptExecutor) webDriver)
                    .executeScript("return document.readyState").equals("complete"));
            logger.debug("Página completamente cargada");
            return true;
        } catch (Exception e) {
            logger.warn("Timeout esperando carga completa de página: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Espera a que aparezca una nueva ventana/pestaña
     * @param cantidadVentanasEsperadas número de ventanas esperadas
     * @return true si aparecen las ventanas esperadas
     */
    public boolean esperarNuevaVentana(int cantidadVentanasEsperadas) {
        try {
            wait.until(ExpectedConditions.numberOfWindowsToBe(cantidadVentanasEsperadas));
            logger.debug("Nuevas ventanas detectadas. Total: {}", cantidadVentanasEsperadas);
            return true;
        } catch (Exception e) {
            logger.warn("Timeout esperando {} ventanas: {}", cantidadVentanasEsperadas, e.getMessage());
            return false;
        }
    }
    
    // === MÉTODOS DE UTILIDAD ===
    
    /**
     * Obtiene el timeout configurado
     * @return timeout en segundos
     */
    public int obtenerTimeout() {
        return timeoutDefault;
    }
    
    /**
     * Crea una nueva instancia con timeout corto (5s)
     * @param driver WebDriver activo
     * @return nueva instancia de EsperaExplicita
     */
    public static EsperaExplicita timeoutCorto(WebDriver driver) {
        return new EsperaExplicita(driver, TIMEOUT_CORTO);
    }
    
    /**
     * Crea una nueva instancia con timeout largo (30s)
     * @param driver WebDriver activo
     * @return nueva instancia de EsperaExplicita
     */
    public static EsperaExplicita timeoutLargo(WebDriver driver) {
        return new EsperaExplicita(driver, TIMEOUT_LARGO);
    }
}