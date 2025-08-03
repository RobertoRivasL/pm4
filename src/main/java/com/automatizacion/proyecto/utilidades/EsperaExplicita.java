package com.automatizacion.proyecto.utilidades;

import com.automatizacion.proyecto.enums.TipoMensaje;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

/**
 * Clase responsable del manejo de esperas explícitas en Selenium.
 * Proporciona métodos robustos para esperar diferentes condiciones.
 * 
 * Principios aplicados:
 * - SRP: Solo maneja esperas explícitas
 * - DRY: Centraliza lógica de esperas
 * - Abstracción: Oculta complejidad de WebDriverWait
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
public class EsperaExplicita {
    
    private static final Logger logger = LoggerFactory.getLogger(EsperaExplicita.class);
    
    private final WebDriver driver;
    private final WebDriverWait espera;
    private final int tiempoEsperaSegundos;
    
    // Constantes para diferentes tipos de espera
    private static final int TIEMPO_ESPERA_CORTO = 5;
    private static final int TIEMPO_ESPERA_MEDIO = 15;
    private static final int TIEMPO_ESPERA_LARGO = 30;
    private static final int INTERVALO_POLLING = 500; // milisegundos
    
    /**
     * Constructor con tiempo de espera personalizado
     * @param driver instancia de WebDriver
     * @param tiempoEsperaSegundos tiempo máximo de espera en segundos
     */
    public EsperaExplicita(WebDriver driver, int tiempoEsperaSegundos) {
        this.driver = driver;
        this.tiempoEsperaSegundos = tiempoEsperaSegundos;
        this.espera = new WebDriverWait(driver, Duration.ofSeconds(tiempoEsperaSegundos));
        this.espera.pollingEvery(Duration.ofMilliseconds(INTERVALO_POLLING));
        
        logger.debug(TipoMensaje.DEBUG.formatearMensaje(
            "EsperaExplicita inicializada con timeout: " + tiempoEsperaSegundos + "s"));
    }
    
    /**
     * Constructor con tiempo de espera por defecto
     * @param driver instancia de WebDriver
     */
    public EsperaExplicita(WebDriver driver) {
        this(driver, TIEMPO_ESPERA_MEDIO);
    }
    
    // === ESPERAS PARA VISIBILIDAD DE ELEMENTOS ===
    
    /**
     * Espera hasta que un elemento sea visible
     * @param localizador localizador del elemento
     * @return elemento visible
     * @throws TimeoutException si el elemento no se vuelve visible
     */
    public WebElement esperarElementoVisible(By localizador) {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "Esperando elemento visible: " + localizador));
            
            WebElement elemento = espera.until(ExpectedConditions.visibilityOfElementLocated(localizador));
            
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "Elemento visible encontrado: " + localizador));
            
            return elemento;
        } catch (TimeoutException e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Timeout esperando elemento visible: " + localizador));
            throw e;
        }
    }
    
    /**
     * Espera hasta que un elemento específico sea visible
     * @param elemento elemento a esperar
     * @return elemento visible
     */
    public WebElement esperarElementoVisible(WebElement elemento) {
        try {
            return espera.until(ExpectedConditions.visibilityOf(elemento));
        } catch (TimeoutException e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Timeout esperando elemento visible"));
            throw e;
        }
    }
    
    /**
     * Espera hasta que todos los elementos sean visibles
     * @param localizador localizador de los elementos
     * @return lista de elementos visibles
     */
    public List<WebElement> esperarElementosVisibles(By localizador) {
        try {
            return espera.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(localizador));
        } catch (TimeoutException e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Timeout esperando elementos visibles: " + localizador));
            throw e;
        }
    }
    
    // === ESPERAS PARA PRESENCIA DE ELEMENTOS ===
    
    /**
     * Espera hasta que un elemento esté presente en el DOM
     * @param localizador localizador del elemento
     * @return elemento presente
     */
    public WebElement esperarElementoPresente(By localizador) {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "Esperando elemento presente: " + localizador));
            
            return espera.until(ExpectedConditions.presenceOfElementLocated(localizador));
        } catch (TimeoutException e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Timeout esperando elemento presente: " + localizador));
            throw e;
        }
    }
    
    /**
     * Espera hasta que todos los elementos estén presentes
     * @param localizador localizador de los elementos
     * @return lista de elementos presentes
     */
    public List<WebElement> esperarElementosPresentes(By localizador) {
        try {
            return espera.until(ExpectedConditions.presenceOfAllElementsLocatedBy(localizador));
        } catch (TimeoutException e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Timeout esperando elementos presentes: " + localizador));
            throw e;
        }
    }
    
    // === ESPERAS PARA CLICKEABILIDAD ===
    
    /**
     * Espera hasta que un elemento sea clickeable
     * @param localizador localizador del elemento
     * @return elemento clickeable
     */
    public WebElement esperarElementoClickeable(By localizador) {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "Esperando elemento clickeable: " + localizador));
            
            WebElement elemento = espera.until(ExpectedConditions.elementToBeClickable(localizador));
            
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "Elemento clickeable encontrado: " + localizador));
            
            return elemento;
        } catch (TimeoutException e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Timeout esperando elemento clickeable: " + localizador));
            throw e;
        }
    }
    
    /**
     * Espera hasta que un elemento específico sea clickeable
     * @param elemento elemento a esperar
     * @return elemento clickeable
     */
    public WebElement esperarElementoClickeable(WebElement elemento) {
        try {
            return espera.until(ExpectedConditions.elementToBeClickable(elemento));
        } catch (TimeoutException e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Timeout esperando elemento clickeable"));
            throw e;
        }
    }
    
    // === ESPERAS PARA INVISIBILIDAD ===
    
    /**
     * Espera hasta que un elemento desaparezca
     * @param localizador localizador del elemento
     * @return true si el elemento desaparece
     */
    public boolean esperarElementoInvisible(By localizador) {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "Esperando elemento invisible: " + localizador));
            
            return espera.until(ExpectedConditions.invisibilityOfElementLocated(localizador));
        } catch (TimeoutException e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                "Timeout esperando elemento invisible: " + localizador));
            return false;
        }
    }
    
    // === ESPERAS PARA TEXTO ===
    
    /**
     * Espera hasta que un elemento contenga texto específico
     * @param localizador localizador del elemento
     * @param texto texto esperado
     * @return true si el elemento contiene el texto
     */
    public boolean esperarTextoEnElemento(By localizador, String texto) {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "Esperando texto '" + texto + "' en elemento: " + localizador));
            
            return espera.until(ExpectedConditions.textToBePresentInElementLocated(localizador, texto));
        } catch (TimeoutException e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Timeout esperando texto en elemento: " + localizador));
            return false;
        }
    }
    
    /**
     * Espera hasta que el texto de un elemento cambie
     * @param elemento elemento a monitorear
     * @param textoAnterior texto anterior
     * @return true si el texto cambia
     */
    public boolean esperarCambioTexto(WebElement elemento, String textoAnterior) {
        try {
            return espera.until(driver -> !elemento.getText().equals(textoAnterior));
        } catch (TimeoutException e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                "Timeout esperando cambio de texto"));
            return false;
        }
    }
    
    // === ESPERAS PARA ALERTAS ===
    
    /**
     * Espera hasta que aparezca una alerta
     * @return alerta presente
     */
    public Alert esperarAlerta() {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Esperando alerta"));
            
            Alert alerta = espera.until(ExpectedConditions.alertIsPresent());
            
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Alerta encontrada"));
            
            return alerta;
        } catch (TimeoutException e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Timeout esperando alerta"));
            throw e;
        }
    }
    
    // === ESPERAS PARA CARGA DE PÁGINA ===
    
    /**
     * Espera hasta que la página esté completamente cargada
     */
    public void esperarCargaCompleta() {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Esperando carga completa de página"));
            
            espera.until(driver -> {
                String estado = ((JavascriptExecutor) driver)
                    .executeScript("return document.readyState").toString();
                return "complete".equals(estado);
            });
            
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Página cargada completamente"));
            
        } catch (TimeoutException e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                "Timeout esperando carga completa de página"));
        }
    }
    
    /**
     * Espera hasta que jQuery esté cargado (si está presente)
     */
    public void esperarjQuery() {
        try {
            espera.until(driver -> {
                Boolean jqueryListo = (Boolean) ((JavascriptExecutor) driver)
                    .executeScript("return jQuery.active == 0");
                return jqueryListo != null && jqueryListo;
            });
            
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("jQuery cargado completamente"));
            
        } catch (Exception e) {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("jQuery no presente o error: " + e.getMessage()));
        }
    }
    
    // === ESPERAS PERSONALIZADAS ===
    
    /**
     * Espera hasta que una condición personalizada se cumpla
     * @param condicion condición a evaluar
     * @param descripcion descripción de la condición
     * @return resultado de la condición
     */
    public <T> T esperarCondicionPersonalizada(java.util.function.Function<WebDriver, T> condicion, String descripcion) {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Esperando condición: " + descripcion));
            
            T resultado = espera.until(condicion);
            
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Condición cumplida: " + descripcion));
            
            return resultado;
        } catch (TimeoutException e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Timeout esperando condición: " + descripcion));
            throw e;
        }
    }
    
    // === UTILIDADES ADICIONALES ===
    
    /**
     * Pausa la ejecución por un tiempo específico (usar con precaución)
     * @param milisegundos tiempo a pausar
     */
    public void pausar(long milisegundos) {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Pausando ejecución por " + milisegundos + "ms"));
            Thread.sleep(milisegundos);
        } catch (InterruptedException e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("Interrupción durante pausa"));
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Sube la página para evitar propaganda (método específico del contexto)
     */
    public void subirPaginaParaEvitarPropaganda() {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Subiendo página para evitar propaganda"));
            
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, -100);");
            pausar(500); // Pequeña pausa para permitir que se complete el scroll
            
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                "Error al subir página: " + e.getMessage()));
        }
    }
    
    /**
     * Verifica si un elemento está presente sin lanzar excepción
     * @param localizador localizador del elemento
     * @return true si el elemento está presente
     */
    public boolean esElementoPresente(By localizador) {
        try {
            driver.findElement(localizador);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }
    
    /**
     * Verifica si un elemento está visible sin lanzar excepción
     * @param localizador localizador del elemento
     * @return true si el elemento está visible
     */
    public boolean esElementoVisible(By localizador) {
        try {
            WebElement elemento = driver.findElement(localizador);
            return elemento.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }
    
    /**
     * Crea una nueva instancia de WebDriverWait con tiempo personalizado
     * @param tiempoSegundos tiempo de espera en segundos
     * @return nueva instancia de WebDriverWait
     */
    public WebDriverWait crearEsperaPersonalizada(int tiempoSegundos) {
        return new WebDriverWait(driver, Duration.ofSeconds(tiempoSegundos));
    }
    
    /**
     * Obtiene el tiempo de espera configurado
     * @return tiempo de espera en segundos
     */
    public int obtenerTiempoEspera() {
        return tiempoEsperaSegundos;
    }
    
    /**
     * Espera con reintento para elementos que pueden ser inestables
     * @param localizador localizador del elemento
     * @param maxReintentos número máximo de reintentos
     * @return elemento encontrado
     * @throws TimeoutException si no se encuentra después de todos los reintentos
     */
    public WebElement esperarConReintento(By localizador, int maxReintentos) {
        TimeoutException ultimaExcepcion = null;
        
        for (int intento = 1; intento <= maxReintentos; intento++) {
            try {
                logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                    "Intento " + intento + "/" + maxReintentos + " para elemento: " + localizador));
                
                return esperarElementoVisible(localizador);
                
            } catch (TimeoutException e) {
                ultimaExcepcion = e;
                logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                    "Intento " + intento + " fallido para elemento: " + localizador));
                
                if (intento < maxReintentos) {
                    pausar(1000); // Pausa entre reintentos
                }
            }
        }
        
        logger.error(TipoMensaje.ERROR.formatearMensaje(
            "Todos los intentos fallaron para elemento: " + localizador));
        
        throw ultimaExcepcion;
    }
}