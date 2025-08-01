package com.automatizacion.proyecto.utilidades;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

/**
 * Clase utilitaria para manejar esperas explícitas en Selenium
 * Implementa el principio de Single Responsibility
 * 
 * @author Roberto Rivas Lopez
 */
public class EsperaExplicita {
    
    private static final Logger logger = LoggerFactory.getLogger(EsperaExplicita.class);
    private static final int TIEMPO_ESPERA_DEFECTO = 10;
    
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
     */
    public void esperarCargaCompleta() {
        try {
            espera.until(driver -> ((JavascriptExecutor) driver)
                .executeScript("return document.readyState").equals("complete"));
            logger.debug("Página cargada completamente");
        } catch (Exception e) {
            logger.warn("Timeout esperando carga completa de página: {}", e.getMessage());
        }
    }

    /**
     * Sube la página para evitar propaganda
     */
    public void subirPaginaParaEvitarPropaganda() {
        try {
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
            Thread.sleep(1000); // Pausa para que la propaganda desaparezca
            logger.debug("Página subida para evitar propaganda");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            logger.debug("Error subiendo página: {}", e.getMessage());
        }
    }

    /**
     * Espera a que aparezca un mensaje flash y lo retorna
     * @param localizadorMensaje By del elemento de mensaje
     * @return texto del mensaje
     */
    public String esperarMensajeFlash(By localizadorMensaje) {
        try {
            WebElement mensaje = esperarElementoVisible(localizadorMensaje);
            String texto = mensaje.getText().trim();
            logger.debug("Mensaje flash obtenido: {}", texto);
            return texto;
        } catch (Exception e) {
            logger.debug("No se pudo obtener mensaje flash: {}", e.getMessage());
            return "";
        }
    }

    /**
     * Espera elemento con reintentos
     * @param localizador localizador del elemento
     * @param maxReintentos número máximo de reintentos
     * @return WebElement encontrado
     */
    public WebElement esperarElementoConReintentos(By localizador, int maxReintentos) {
        Exception ultimaExcepcion = null;
        for (int intento = 1; intento <= maxReintentos; intento++) {
            try {
                return esperarElementoVisible(localizador);
            } catch (Exception e) {
                ultimaExcepcion = e;
                logger.debug("Intento {} fallido para elemento {}: {}", intento, localizador, e.getMessage());
                if (intento < maxReintentos) {
                    try {
                        Thread.sleep(1000); // Pausa entre reintentos
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }
       logger.error("Elemento no encontrado después de {} intentos: {}", maxReintentos, localizador);
        throw new RuntimeException("Elemento no encontrado después de " + maxReintentos + " intentos: " + localizador, ultimaExcepcion);
    }
