package com.automatizacion.proyecto.utilidades;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

/**
 * Clase utilitaria para manejar esperas explícitas en Selenium.
 * Implementa el principio de Single Responsibility.
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
public class EsperaExplicita {
    
    private static final Logger logger = LoggerFactory.getLogger(EsperaExplicita.class);
    private static final int TIEMPO_ESPERA_DEFECTO = 10;
    private static final int TIEMPO_PAUSA_CORTA = 500;
    private static final int TIEMPO_PAUSA_MEDIA = 1000;
    private static final int TIEMPO_PAUSA_LARGA = 2000;
    
    private final WebDriverWait espera;
    private final WebDriver driver;
    
    /**
     * Constructor que inicializa la espera con tiempo por defecto
     * @param driver instancia del WebDriver
     */
    public EsperaExplicita(WebDriver driver) {
        this(driver, TIEMPO_ESPERA_DEFECTO);
    }
    
    /**
     * Constructor que permite especificar el tiempo de espera
     * @param driver instancia del WebDriver
     * @param tiempoEsperaSegundos tiempo de espera en segundos
     */
    public EsperaExplicita(WebDriver driver, int tiempoEsperaSegundos) {
        this.driver = driver;
        this.espera = new WebDriverWait(driver, Duration.ofSeconds(tiempoEsperaSegundos));
        logger.debug("EsperaExplicita inicializada con {} segundos", tiempoEsperaSegundos);
    }
    
    /**
     * Espera hasta que un elemento sea visible
     * @param localizador By del elemento
     * @return WebElement visible
     */
    public WebElement esperarElementoVisible(By localizador) {
        logger.debug("Esperando que el elemento sea visible: {}", localizador);
        try {
            WebElement elemento = espera.until(ExpectedConditions.visibilityOfElementLocated(localizador));
            logger.debug("Elemento visible encontrado: {}", localizador);
            return elemento;
        } catch (Exception e) {
            logger.error("Error esperando elemento visible {}: {}", localizador, e.getMessage());
            throw new RuntimeException("No se pudo encontrar el elemento visible: " + localizador, e);
        }
    }
    
    /**
     * Espera hasta que un elemento sea clickeable
     * @param localizador By del elemento
     * @return WebElement clickeable
     */
    public WebElement esperarElementoClickeable(By localizador) {
        logger.debug("Esperando que el elemento sea clickeable: {}", localizador);
        try {
            WebElement elemento = espera.until(ExpectedConditions.elementToBeClickable(localizador));
            logger.debug("Elemento clickeable encontrado: {}", localizador);
            return elemento;
        } catch (Exception e) {
            logger.error("Error esperando elemento clickeable {}: {}", localizador, e.getMessage());
            throw new RuntimeException("No se pudo encontrar el elemento clickeable: " + localizador, e);
        }
    }
    
    /**
     * Espera hasta que un elemento esté presente en el DOM
     * @param localizador By del elemento
     * @return WebElement presente
     */
    public WebElement esperarElementoPresente(By localizador) {
        logger.debug("Esperando que el elemento esté presente: {}", localizador);
        try {
            WebElement elemento = espera.until(ExpectedConditions.presenceOfElementLocated(localizador));
            logger.debug("Elemento presente encontrado: {}", localizador);
            return elemento;
        } catch (Exception e) {
            logger.error("Error esperando elemento presente {}: {}", localizador, e.getMessage());
            throw new RuntimeException("No se pudo encontrar el elemento presente: " + localizador, e);
        }
    }
    
    /**
     * Espera hasta que un texto específico esté presente en un elemento
     * @param localizador By del elemento
     * @param texto texto a esperar
     * @return true si el texto está presente
     */
    public boolean esperarTextoEnElemento(By localizador, String texto) {
        logger.debug("Esperando texto '{}' en elemento: {}", texto, localizador);
        try {
            boolean resultado = espera.until(ExpectedConditions.textToBePresentInElementLocated(localizador, texto));
            logger.debug("Texto '{}' encontrado en elemento: {}", texto, localizador);
            return resultado;
        } catch (Exception e) {
            logger.error("Error esperando texto '{}' en elemento {}: {}", texto, localizador, e.getMessage());
            return false;
        }
    }
    
    /**
     * Espera hasta que un elemento no sea visible
     * @param localizador By del elemento
     * @return true si el elemento no es visible
     */
    public boolean esperarElementoNoVisible(By localizador) {
        logger.debug("Esperando que el elemento no sea visible: {}", localizador);
        try {
            boolean resultado = espera.until(ExpectedConditions.invisibilityOfElementLocated(localizador));
            logger.debug("Elemento no visible: {}", localizador);
            return resultado;
        } catch (Exception e) {
            logger.error("Error esperando elemento no visible {}: {}", localizador, e.getMessage());
            return false;
        }
    }
    
    /**
     * Espera hasta que todos los elementos de una lista sean visibles
     * @param localizador By de los elementos
     * @return List de WebElements visibles
     */
    public List<WebElement> esperarElementosVisibles(By localizador) {
        logger.debug("Esperando que los elementos sean visibles: {}", localizador);
        try {
            List<WebElement> elementos = espera.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(localizador));
            logger.debug("Elementos visibles encontrados ({}): {}", elementos.size(), localizador);
            return elementos;
        } catch (Exception e) {
            logger.error("Error esperando elementos visibles {}: {}", localizador, e.getMessage());
            throw new RuntimeException("No se pudieron encontrar los elementos visibles: " + localizador, e);
        }
    }
    
    /**
     * Espera hasta que la URL contenga un texto específico
     * @param fragmentoUrl fragmento de URL a esperar
     * @return true si la URL contiene el fragmento
     */
    public boolean esperarUrlContiene(String fragmentoUrl) {
        logger.debug("Esperando que la URL contenga: {}", fragmentoUrl);
        try {
            boolean resultado = espera.until(ExpectedConditions.urlContains(fragmentoUrl));
            logger.debug("URL contiene el fragmento: {}", fragmentoUrl);
            return resultado;
        } catch (Exception e) {
            logger.error("Error esperando URL con fragmento '{}': {}", fragmentoUrl, e.getMessage());
            return false;
        }
    }
    
    /**
     * Espera hasta que el título de la página contenga un texto específico
     * @param tituloEsperado título esperado
     * @return true si el título contiene el texto
     */
    public boolean esperarTituloContiene(String tituloEsperado) {
        logger.debug("Esperando que el título contenga: {}", tituloEsperado);
        try {
            boolean resultado = espera.until(ExpectedConditions.titleContains(tituloEsperado));
            logger.debug("Título contiene: {}", tituloEsperado);
            return resultado;
        } catch (Exception e) {
            logger.error("Error esperando título '{}': {}", tituloEsperado, e.getMessage());
            return false;
        }
    }
    
    /**
     * Espera a que la página se cargue completamente
     * Incluye validación de document.readyState y jQuery si está disponible
     */
    public void esperarCargaCompleta() {
        logger.debug("Esperando carga completa de la página");
        
        try {
            // Esperar que document.readyState sea 'complete'
            espera.until(webDriver -> {
                JavascriptExecutor js = (JavascriptExecutor) webDriver;
                String readyState = (String) js.executeScript("return document.readyState");
                logger.debug("Document readyState: {}", readyState);
                return "complete".equals(readyState);
            });
            
            // Esperar a que jQuery termine si está disponible
            esperarJQuery();
            
            // Pausa adicional para asegurar estabilidad
            pausaCorta();
            
            logger.debug("Carga completa de página finalizada");
            
        } catch (Exception e) {
            logger.warn("Error esperando carga completa: {}", e.getMessage());
        }
    }
    
    /**
     * Sube la página para evitar propaganda y elementos flotantes
     * Utiliza JavaScript para hacer scroll hacia arriba
     */
    public void subirPaginaParaEvitarPropaganda() {
        logger.debug("Subiendo página para evitar propaganda");
        
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            
            // Scroll hacia arriba de la página
            js.executeScript("window.scrollTo(0, 0);");
            
            // Pausa corta para que el scroll surta efecto
            pausaCorta();
            
            // Si hay elementos de propaganda visibles, intentar cerrarlos
            cerrarElementosFlotantes();
            
            logger.debug("Página posicionada en la parte superior");
            
        } catch (Exception e) {
            logger.warn("Error subiendo página: {}", e.getMessage());
        }
    }
    
    /**
     * Espera y obtiene un mensaje flash/temporal de la página
     * @param localizadorMensaje localizador del mensaje
     * @return texto del mensaje o cadena vacía si no se encuentra
     */
    public String esperarMensajeFlash(By localizadorMensaje) {
        logger.debug("Esperando mensaje flash: {}", localizadorMensaje);
        
        try {
            // Esperar que el mensaje aparezca
            WebElement mensaje = esperarElementoVisible(localizadorMensaje);
            String textoMensaje = mensaje.getText().trim();
            
            logger.debug("Mensaje flash encontrado: '{}'", textoMensaje);
            return textoMensaje;
            
        } catch (Exception e) {
            logger.debug("No se encontró mensaje flash: {}", e.getMessage());
            return "";
        }
    }
    
    /**
     * Espera un elemento con reintentos automáticos
     * @param localizador localizador del elemento
     * @param maxReintentos número máximo de reintentos
     * @return WebElement encontrado
     */
    public WebElement esperarElementoConReintentos(By localizador, int maxReintentos) {
        logger.debug("Esperando elemento con reintentos (max: {}): {}", maxReintentos, localizador);
        
        for (int intento = 1; intento <= maxReintentos; intento++) {
            try {
                WebElement elemento = esperarElementoVisible(localizador);
                logger.debug("Elemento encontrado en intento {}: {}", intento, localizador);
                return elemento;
                
            } catch (Exception e) {
                logger.debug("Intento {} fallido para elemento {}: {}", intento, localizador, e.getMessage());
                
                if (intento < maxReintentos) {
                    pausaMedia();
                    // Refresh de la página si es el último intento antes del final
                    if (intento == maxReintentos - 1) {
                        logger.debug("Último intento - refrescando página");
                        driver.navigate().refresh();
                        esperarCargaCompleta();
                    }
                } else {
                    throw new RuntimeException("Elemento no encontrado después de " + maxReintentos + " intentos: " + localizador, e);
                }
            }
        }
        
        throw new RuntimeException("No se pudo encontrar el elemento: " + localizador);
    }
    
    /**
     * Espera a que jQuery termine de ejecutarse (si está disponible)
     */
    private void esperarJQuery() {
        try {
            espera.until(webDriver -> {
                JavascriptExecutor js = (JavascriptExecutor) webDriver;
                // Verificar si jQuery está disponible
                Boolean jqueryDefined = (Boolean) js.executeScript("return typeof jQuery !== 'undefined'");
                
                if (jqueryDefined) {
                    // Esperar a que jQuery.active sea 0 (no hay peticiones AJAX pendientes)
                    Long jqueryActive = (Long) js.executeScript("return jQuery.active");
                    logger.debug("jQuery activo: {}", jqueryActive);
                    return jqueryActive == 0;
                }
                
                return true; // Si jQuery no está disponible, considerar como completo
            });
        } catch (Exception e) {
            logger.debug("Error verificando jQuery: {}", e.getMessage());
        }
    }
    
    /**
     * Intenta cerrar elementos flotantes comunes (modales, propaganda, etc.)
     */
    private void cerrarElementosFlotantes() {
        // Selectores comunes para cerrar elementos flotantes
        String[] selectoresCierre = {
            "button[aria-label='Close']",
            "button[aria-label='close']",
            ".close",
            ".modal-close",
            "[class*='close']",
            "[data-dismiss='modal']"
        };
        
        for (String selector : selectoresCierre) {
            try {
                List<WebElement> elementos = driver.findElements(By.cssSelector(selector));
                for (WebElement elemento : elementos) {
                    if (elemento.isDisplayed() && elemento.isEnabled()) {
                        elemento.click();
                        pausaCorta();
                        logger.debug("Elemento flotante cerrado: {}", selector);
                    }
                }
            } catch (Exception e) {
                // Ignorar errores al cerrar elementos flotantes
                logger.debug("No se pudo cerrar elemento flotante {}: {}", selector, e.getMessage());
            }
        }
    }
    
    /**
     * Pausa corta para sincronización
     */
    private void pausaCorta() {
        try {
            Thread.sleep(TIEMPO_PAUSA_CORTA);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.debug("Pausa corta interrumpida");
        }
    }
    
    /**
     * Pausa media para sincronización
     */
    private void pausaMedia() {
        try {
            Thread.sleep(TIEMPO_PAUSA_MEDIA);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.debug("Pausa media interrumpida");
        }
    }
    
    /**
     * Pausa larga para sincronización
     */
    private void pausaLarga() {
        try {
            Thread.sleep(TIEMPO_PAUSA_LARGA);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.debug("Pausa larga interrumpida");
        }
    }
    
    /**
     * Espera personalizada con condición lambda
     * @param condicion condición a evaluar
     * @param tiempoEspera tiempo máximo de espera en segundos
     * @return true si la condición se cumple, false si timeout
     */
    public boolean esperarCondicion(java.util.function.Function<WebDriver, Boolean> condicion, int tiempoEspera) {
        try {
            WebDriverWait esperaPersonalizada = new WebDriverWait(driver, Duration.ofSeconds(tiempoEspera));
            return esperaPersonalizada.until(condicion);
        } catch (TimeoutException e) {
            logger.debug("Timeout esperando condición personalizada");
            return false;
        }
    }
    
    /**
     * Verifica si un elemento existe sin lanzar excepción
     * @param localizador localizador del elemento
     * @return true si el elemento existe, false en caso contrario
     */
    public boolean existeElemento(By localizador) {
        try {
            driver.findElement(localizador);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Espera hasta que desaparezca un elemento de loading/spinner
     * @param localizadorLoading localizador del elemento de loading
     */
    public void esperarFinalizacionLoading(By localizadorLoading) {
        logger.debug("Esperando finalización de loading: {}", localizadorLoading);
        try {
            esperarElementoNoVisible(localizadorLoading);
            logger.debug("Loading finalizado");
        } catch (Exception e) {
            logger.debug("No se encontró elemento de loading o ya finalizó");
        }
    }
}