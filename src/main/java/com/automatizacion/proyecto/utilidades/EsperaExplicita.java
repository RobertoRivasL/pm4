package com.automatizacion.proyecto.utilidades;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;
import java.util.List;

/**
 * Clase utilitaria para manejar esperas explícitas en Selenium.
 * Centraliza todas las estrategias de espera y proporciona métodos robustos.
 * 
 * Principios aplicados:
 * - SRP: Responsabilidad única de manejar esperas
 * - DRY: Evita repetición de código de esperas
 * - Encapsulación: Manejo interno de WebDriverWait
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
public class EsperaExplicita {
    
    private static final Logger logger = LoggerFactory.getLogger(EsperaExplicita.class);
    
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final int timeoutSegundos;
    
    // Constantes de timeouts
    private static final int TIMEOUT_CORTO = 5;
    private static final int TIMEOUT_MEDIO = 10;
    private static final int TIMEOUT_LARGO = 30;
    
    /**
     * Constructor que inicializa las esperas con timeout personalizado.
     */
    public EsperaExplicita(WebDriver driver, int timeoutSegundos) {
        this.driver = driver;
        this.timeoutSegundos = timeoutSegundos;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSegundos));
        logger.debug("EsperaExplicita inicializada con timeout: {} segundos", timeoutSegundos);
    }
    
    /**
     * Constructor con timeout por defecto.
     */
    public EsperaExplicita(WebDriver driver) {
        this(driver, TIMEOUT_MEDIO);
    }
    
    // ===== ESPERAS DE ELEMENTOS =====
    
    /**
     * Espera hasta que un elemento sea visible.
     */
    public WebElement esperarElementoVisible(By localizador) {
        try {
            logger.debug("Esperando elemento visible: {}", localizador);
            WebElement elemento = wait.until(ExpectedConditions.visibilityOfElementLocated(localizador));
            logger.debug("Elemento visible encontrado: {}", localizador);
            return elemento;
        } catch (TimeoutException e) {
            logger.error("Timeout esperando elemento visible: {}", localizador);
            throw new RuntimeException("Elemento no visible después de " + timeoutSegundos + " segundos: " + localizador, e);
        }
    }
    
    /**
     * Espera hasta que un elemento sea visible con timeout personalizado.
     */
    public WebElement esperarElementoVisible(By localizador, int timeoutPersonalizado) {
        try {
            logger.debug("Esperando elemento visible con timeout {}: {}", timeoutPersonalizado, localizador);
            WebDriverWait waitPersonalizado = new WebDriverWait(driver, Duration.ofSeconds(timeoutPersonalizado));
            WebElement elemento = waitPersonalizado.until(ExpectedConditions.visibilityOfElementLocated(localizador));
            logger.debug("Elemento visible encontrado: {}", localizador);
            return elemento;
        } catch (TimeoutException e) {
            logger.warn("Timeout esperando elemento visible: {}", localizador);
            throw new TimeoutException("Elemento no visible después de " + timeoutPersonalizado + " segundos: " + localizador, e);
        }
    }
    
    /**
     * Espera hasta que un WebElement específico sea visible.
     */
    public WebElement esperarElementoVisible(WebElement elemento) {
        try {
            logger.debug("Esperando WebElement visible");
            WebElement elementoVisible = wait.until(ExpectedConditions.visibilityOf(elemento));
            logger.debug("WebElement visible");
            return elementoVisible;
        } catch (TimeoutException e) {
            logger.error("Timeout esperando WebElement visible");
            throw new RuntimeException("WebElement no visible después de " + timeoutSegundos + " segundos", e);
        }
    }
    
    /**
     * Espera hasta que un elemento sea clickeable.
     */
    public WebElement esperarElementoClickeable(By localizador) {
        try {
            logger.debug("Esperando elemento clickeable: {}", localizador);
            WebElement elemento = wait.until(ExpectedConditions.elementToBeClickable(localizador));
            logger.debug("Elemento clickeable encontrado: {}", localizador);
            return elemento;
        } catch (TimeoutException e) {
            logger.error("Timeout esperando elemento clickeable: {}", localizador);
            throw new RuntimeException("Elemento no clickeable después de " + timeoutSegundos + " segundos: " + localizador, e);
        }
    }
    
    /**
     * Espera hasta que un WebElement específico sea clickeable.
     */
    public WebElement esperarElementoClickeable(WebElement elemento) {
        try {
            logger.debug("Esperando WebElement clickeable");
            WebElement elementoClickeable = wait.until(ExpectedConditions.elementToBeClickable(elemento));
            logger.debug("WebElement clickeable");
            return elementoClickeable;
        } catch (TimeoutException e) {
            logger.error("Timeout esperando WebElement clickeable");
            throw new RuntimeException("WebElement no clickeable después de " + timeoutSegundos + " segundos", e);
        }
    }
    
    /**
     * Espera hasta que un elemento esté presente en el DOM.
     */
    public WebElement esperarElementoPresente(By localizador) {
        try {
            logger.debug("Esperando elemento presente: {}", localizador);
            WebElement elemento = wait.until(ExpectedConditions.presenceOfElementLocated(localizador));
            logger.debug("Elemento presente encontrado: {}", localizador);
            return elemento;
        } catch (TimeoutException e) {
            logger.error("Timeout esperando elemento presente: {}", localizador);
            throw new RuntimeException("Elemento no presente después de " + timeoutSegundos + " segundos: " + localizador, e);
        }
    }
    
    /**
     * Espera hasta que múltiples elementos sean visibles.
     */
    public List<WebElement> esperarElementosVisibles(By localizador) {
        try {
            logger.debug("Esperando elementos visibles: {}", localizador);
            List<WebElement> elementos = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(localizador));
            logger.debug("Elementos visibles encontrados: {} elementos", elementos.size());
            return elementos;
        } catch (TimeoutException e) {
            logger.error("Timeout esperando elementos visibles: {}", localizador);
            throw new RuntimeException("Elementos no visibles después de " + timeoutSegundos + " segundos: " + localizador, e);
        }
    }
    
    // ===== ESPERAS DE INVISIBILIDAD =====
    
    /**
     * Espera hasta que un elemento sea invisible.
     */
    public boolean esperarInvisibilidadDelElemento(By localizador) {
        try {
            logger.debug("Esperando invisibilidad del elemento: {}", localizador);
            boolean invisible = wait.until(ExpectedConditions.invisibilityOfElementLocated(localizador));
            logger.debug("Elemento invisible: {}", localizador);
            return invisible;
        } catch (TimeoutException e) {
            logger.warn("Timeout esperando invisibilidad del elemento: {}", localizador);
            return false;
        }
    }
    
    /**
     * Espera hasta que un WebElement específico sea invisible.
     */
    public boolean esperarInvisibilidadDelElemento(WebElement elemento) {
        try {
            logger.debug("Esperando invisibilidad del WebElement");
            boolean invisible = wait.until(ExpectedConditions.invisibilityOf(elemento));
            logger.debug("WebElement invisible");
            return invisible;
        } catch (TimeoutException e) {
            logger.warn("Timeout esperando invisibilidad del WebElement");
            return false;
        }
    }
    
    // ===== ESPERAS DE TEXTO =====
    
    /**
     * Espera hasta que un elemento contenga texto específico.
     */
    public boolean esperarTextoEnElemento(By localizador, String texto) {
        try {
            logger.debug("Esperando texto '{}' en elemento: {}", texto, localizador);
            boolean textoPresente = wait.until(ExpectedConditions.textToBePresentInElementLocated(localizador, texto));
            logger.debug("Texto encontrado en elemento: {}", localizador);
            return textoPresente;
        } catch (TimeoutException e) {
            logger.warn("Timeout esperando texto '{}' en elemento: {}", texto, localizador);
            return false;
        }
    }
    
    /**
     * Espera hasta que un elemento contenga texto no vacío.
     */
    public boolean esperarTextoNoVacioEnElemento(By localizador) {
        try {
            logger.debug("Esperando texto no vacío en elemento: {}", localizador);
            boolean textoPresente = wait.until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver driver) {
                    try {
                        WebElement elemento = driver.findElement(localizador);
                        String texto = elemento.getText().trim();
                        return !texto.isEmpty();
                    } catch (Exception e) {
                        return false;
                    }
                }
            });
            logger.debug("Texto no vacío encontrado en elemento: {}", localizador);
            return textoPresente;
        } catch (TimeoutException e) {
            logger.warn("Timeout esperando texto no vacío en elemento: {}", localizador);
            return false;
        }
    }
    
    // ===== ESPERAS DE ATRIBUTOS =====
    
    /**
     * Espera hasta que un atributo de un elemento contenga un valor específico.
     */
    public boolean esperarAtributoContiene(By localizador, String atributo, String valor) {
        try {
            logger.debug("Esperando atributo '{}' contenga '{}' en elemento: {}", atributo, valor, localizador);
            boolean atributoPresente = wait.until(ExpectedConditions.attributeContains(localizador, atributo, valor));
            logger.debug("Atributo con valor encontrado en elemento: {}", localizador);
            return atributoPresente;
        } catch (TimeoutException e) {
            logger.warn("Timeout esperando atributo '{}' con valor '{}' en elemento: {}", atributo, valor, localizador);
            return false;
        }
    }
    
    // ===== ESPERAS DE PÁGINA =====
    
    /**
     * Espera hasta que la página esté completamente cargada.
     */
    public void esperarCargaCompleta() {
        try {
            logger.debug("Esperando carga completa de página");
            wait.until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver driver) {
                    JavascriptExecutor js = (JavascriptExecutor) driver;
                    String readyState = js.executeScript("return document.readyState").toString();
                    boolean jqueryInactivo = true;
                    
                    // Verificar jQuery si está disponible
                    try {
                        Long jqueryActive = (Long) js.executeScript("return jQuery.active");
                        jqueryInactivo = (jqueryActive == 0);
                    } catch (Exception e) {
                        // jQuery no disponible, continuar
                    }
                    
                    return "complete".equals(readyState) && jqueryInactivo;
                }
            });
            logger.debug("Página completamente cargada");
        } catch (TimeoutException e) {
            logger.warn("Timeout esperando carga completa de página");
        }
    }
    
    /**
     * Espera hasta que el título de la página contenga texto específico.
     */
    public boolean esperarTituloContiene(String textoTitulo) {
        try {
            logger.debug("Esperando título contenga: {}", textoTitulo);
            boolean tituloPresente = wait.until(ExpectedConditions.titleContains(textoTitulo));
            logger.debug("Título con texto encontrado");
            return tituloPresente;
        } catch (TimeoutException e) {
            logger.warn("Timeout esperando título con texto: {}", textoTitulo);
            return false;
        }
    }
    
    /**
     * Espera hasta que la URL contenga texto específico.
     */
    public boolean esperarUrlContiene(String textoUrl) {
        try {
            logger.debug("Esperando URL contenga: {}", textoUrl);
            boolean urlPresente = wait.until(ExpectedConditions.urlContains(textoUrl));
            logger.debug("URL con texto encontrado");
            return urlPresente;
        } catch (TimeoutException e) {
            logger.warn("Timeout esperando URL con texto: {}", textoUrl);
            return false;
        }
    }
    
    // ===== ESPERAS PERSONALIZADAS =====
    
    /**
     * Espera personalizada con condición lambda.
     */
    public <T> T esperarCondicionPersonalizada(ExpectedCondition<T> condicion) {
        try {
            logger.debug("Esperando condición personalizada");
            T resultado = wait.until(condicion);
            logger.debug("Condición personalizada cumplida");
            return resultado;
        } catch (TimeoutException e) {
            logger.error("Timeout en condición personalizada");
            throw new RuntimeException("Condición personalizada no cumplida después de " + timeoutSegundos + " segundos", e);
        }
    }
    
    /**
     * Espera con polling personalizado.
     */
    public <T> T esperarConPolling(ExpectedCondition<T> condicion, int timeoutSegundos, int pollingMilisegundos) {
        try {
            logger.debug("Esperando con polling personalizado: timeout={}s, polling={}ms", timeoutSegundos, pollingMilisegundos);
            WebDriverWait waitPersonalizado = new WebDriverWait(driver, Duration.ofSeconds(timeoutSegundos));
            waitPersonalizado.pollingEvery(Duration.ofMilliseconds(pollingMilisegundos));
            T resultado = waitPersonalizado.until(condicion);
            logger.debug("Condición con polling cumplida");
            return resultado;
        } catch (TimeoutException e) {
            logger.error("Timeout en espera con polling personalizado");
            throw new RuntimeException("Condición con polling no cumplida después de " + timeoutSegundos + " segundos", e);
        }
    }
    
    // ===== MÉTODOS DE UTILIDAD =====
    
    /**
     * Obtiene el timeout configurado.
     */
    public int getTimeoutSegundos() {
        return timeoutSegundos;
    }
    
    /**
     * Crea una nueva instancia con timeout diferente para casos específicos.
     */
    public EsperaExplicita conTimeout(int nuevoTimeout) {
        return new EsperaExplicita(driver, nuevoTimeout);
    }
    
    /**
     * Método de utilidad para esperas cortas.
     */
    public EsperaExplicita esperaCorta() {
        return new EsperaExplicita(driver, TIMEOUT_CORTO);
    }
    
    /**
     * Método de utilidad para esperas largas.
     */
    public EsperaExplicita esperaLarga() {
        return new EsperaExplicita(driver, TIMEOUT_LARGO);
    }
}