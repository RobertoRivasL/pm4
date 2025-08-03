package com.automatizacion.proyecto.utilidades;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatizacion.proyecto.enums.TipoMensaje;

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
     * Espera hasta que la página se cargue completamente
     * Utiliza JavaScript para verificar el estado de carga
     */
    public void esperarCargaCompleta() {
        logger.debug("Esperando carga completa de la página");
        try {
            espera.until(driver -> {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                String readyState = (String) js.executeScript("return document.readyState");
                return "complete".equals(readyState);
            });
            logger.debug("Página cargada completamente");
        } catch (Exception e) {
            logger.error("Error esperando carga completa: {}", e.getMessage());
        }
    }
    
    /**
     * Ejecuta un scroll hacia arriba para evitar propaganda o elementos que puedan 
     * interferir con las pruebas después de cargar la página
     */
    public void subirPaginaParaEvitarPropaganda() {
        logger.debug("Ejecutando scroll hacia arriba para evitar propaganda");
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollTo(0, -document.body.scrollHeight);");
            
            // Pequeña pausa para permitir que se complete el scroll
            Thread.sleep(500);
            logger.debug("Scroll hacia arriba ejecutado exitosamente");
        } catch (Exception e) {
            logger.warn("Error al ejecutar scroll hacia arriba: {}", e.getMessage());
        }
    }
    
    /**
     * Espera un tiempo específico en segundos
     * @param segundos tiempo a esperar
     */
    public void esperarTiempo(int segundos) {
        logger.debug("Esperando {} segundos", segundos);
        try {
            Thread.sleep(segundos * 1000L);
        } catch (InterruptedException e) {
            logger.error("Error durante la espera: {}", e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    // Agregar este método a la clase EsperaExplicita existente

/**
 * Pausa la ejecución por un número específico de segundos
 * @param segundos número de segundos a pausar
 */
public void pausar(int segundos) {
    try {
        Thread.sleep(segundos * 1000L);
        logger.debug(TipoMensaje.DEBUG.formatearMensaje(
            "Pausa ejecutada por " + segundos + " segundos"));
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
            "Pausa interrumpida: " + e.getMessage()));
    }
}
}