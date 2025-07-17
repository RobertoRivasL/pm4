package com.robertorivas.automatizacion.paginas;

import com.robertorivas.automatizacion.configuracion.ConfiguracionPruebas;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.Set;

/**
 * Clase base para implementar el patrón Page Object Model (POM).
 * Contiene funcionalidades comunes para todas las páginas.
 * 
 * Principios aplicados:
 * - Abstraction: Clase base abstracta con métodos comunes
 * - Encapsulation: Encapsula operaciones comunes de WebDriver
 * - DRY: Evita duplicación de código entre páginas
 * - Single Responsibility: Maneja solo operaciones base de páginas
 * 
 * @author Roberto Rivas Lopez
 */
public abstract class PaginaBase {
    
    protected static final Logger logger = LoggerFactory.getLogger(PaginaBase.class);
    
    // Driver y configuración
    protected final WebDriver driver;
    protected final WebDriverWait espera;
    protected final Actions acciones;
    protected final ConfiguracionPruebas config;
    
    // Timeouts comunes
    protected static final Duration TIMEOUT_CORTO = Duration.ofSeconds(5);
    protected static final Duration TIMEOUT_MEDIO = Duration.ofSeconds(10);
    protected static final Duration TIMEOUT_LARGO = Duration.ofSeconds(20);
    
    /**
     * Constructor base para todas las páginas.
     */
    protected PaginaBase(WebDriver driver) {
        this.driver = driver;
        this.config = ConfiguracionPruebas.obtenerInstancia();
        this.espera = new WebDriverWait(driver, config.obtenerTimeoutExplicito());
        this.acciones = new Actions(driver);
        
        // Inicializar elementos de la página usando PageFactory
        PageFactory.initElements(driver, this);
        
        logger.debug("Página inicializada: {}", this.getClass().getSimpleName());
    }
    
    // Métodos abstractos que deben implementar las páginas hijas
    
    /**
     * Verifica si la página actual es la correcta.
     */
    public abstract boolean estaPaginaCargada();
    
    /**
     * Espera a que la página esté completamente cargada.
     */
    public abstract void esperarCargaPagina();
    
    /**
     * Obtiene el título específico de la página.
     */
    public abstract String obtenerTituloPagina();
    
    // Métodos de navegación y espera
    
    /**
     * Navega a una URL específica.
     */
    protected void navegarA(String url) {
        logger.info("Navegando a: {}", url);
        driver.get(url);
        esperarCargaPaginaCompleta();
    }
    
    /**
     * Espera a que la página esté completamente cargada.
     */
    protected void esperarCargaPaginaCompleta() {
        esperarDocumentoListo();
        esperarJQueryCompleto();
        esperarCargaPagina();
    }
    
    /**
     * Espera a que el documento esté listo.
     */
    private void esperarDocumentoListo() {
        espera.until(driver -> {
            String estado = ((JavascriptExecutor) driver).executeScript("return document.readyState").toString();
            return "complete".equals(estado);
        });
    }
    
    /**
     * Espera a que jQuery termine de ejecutarse (si está disponible).
     */
    private void esperarJQueryCompleto() {
        try {
            espera.until(driver -> {
                Boolean jqueryCompleto = (Boolean) ((JavascriptExecutor) driver)
                        .executeScript("return typeof jQuery !== 'undefined' ? jQuery.active == 0 : true");
                return jqueryCompleto;
            });
        } catch (TimeoutException | JavascriptException e) {
            // jQuery no está disponible, continuar
            logger.debug("jQuery no disponible o no se pudo verificar");
        }
    }
    
    // Métodos de búsqueda y espera de elementos
    
    /**
     * Busca un elemento con espera explícita hasta que sea visible.
     */
    protected WebElement buscarElementoVisible(By localizador) {
        return buscarElementoVisible(localizador, config.obtenerTimeoutExplicito());
    }
    
    /**
     * Busca un elemento con espera explícita y timeout personalizado.
     */
    protected WebElement buscarElementoVisible(By localizador, Duration timeout) {
        logger.debug("Buscando elemento visible: {}", localizador);
        WebDriverWait esperaPersonalizada = new WebDriverWait(driver, timeout);
        return esperaPersonalizada.until(ExpectedConditions.visibilityOfElementLocated(localizador));
    }
    
    /**
     * Busca un elemento clickeable.
     */
    protected WebElement buscarElementoClickeable(By localizador) {
        return buscarElementoClickeable(localizador, config.obtenerTimeoutExplicito());
    }
    
    /**
     * Busca un elemento clickeable con timeout personalizado.
     */
    protected WebElement buscarElementoClickeable(By localizador, Duration timeout) {
        logger.debug("Buscando elemento clickeable: {}", localizador);
        WebDriverWait esperaPersonalizada = new WebDriverWait(driver, timeout);
        return esperaPersonalizada.until(ExpectedConditions.elementToBeClickable(localizador));
    }
    
    /**
     * Busca múltiples elementos con espera.
     */
    protected List<WebElement> buscarElementos(By localizador) {
        espera.until(ExpectedConditions.presenceOfElementLocated(localizador));
        return driver.findElements(localizador);
    }
    
    /**
     * Verifica si un elemento está presente.
     */
    protected boolean estaElementoPresente(By localizador) {
        try {
            driver.findElement(localizador);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }
    
    /**
     * Verifica si un elemento está visible.
     */
    protected boolean estaElementoVisible(By localizador) {
        try {
            WebElement elemento = driver.findElement(localizador);
            return elemento.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }
    
    /**
     * Espera a que un elemento desaparezca.
     */
    protected boolean esperarQueElementoDesaparezca(By localizador, Duration timeout) {
        try {
            WebDriverWait esperaPersonalizada = new WebDriverWait(driver, timeout);
            return esperaPersonalizada.until(ExpectedConditions.invisibilityOfElementLocated(localizador));
        } catch (TimeoutException e) {
            return false;
        }
    }
    
    // Métodos de interacción con elementos
    
    /**
     * Hace clic en un elemento con espera.
     */
    protected void hacerClic(By localizador) {
        logger.debug("Haciendo clic en elemento: {}", localizador);
        WebElement elemento = buscarElementoClickeable(localizador);
        elemento.click();
    }
    
    /**
     * Hace clic usando JavaScript (útil para elementos que pueden estar ocultos).
     */
    protected void hacerClicConJavaScript(By localizador) {
        logger.debug("Haciendo clic con JavaScript en: {}", localizador);
        WebElement elemento = buscarElementoVisible(localizador);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", elemento);
    }
    
    /**
     * Introduce texto en un campo de entrada.
     */
    protected void introducirTexto(By localizador, String texto) {
        logger.debug("Introduciendo texto '{}' en: {}", texto, localizador);
        WebElement elemento = buscarElementoVisible(localizador);
        elemento.clear();
        elemento.sendKeys(texto);
    }
    
    /**
     * Introduce texto de forma gradual (simula escritura humana).
     */
    protected void introducirTextoGradual(By localizador, String texto) {
        logger.debug("Introduciendo texto gradual '{}' en: {}", texto, localizador);
        WebElement elemento = buscarElementoVisible(localizador);
        elemento.clear();
        
        for (char caracter : texto.toCharArray()) {
            elemento.sendKeys(String.valueOf(caracter));
            try {
                Thread.sleep(50); // Pausa breve entre caracteres
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.warn("Interrupción durante escritura gradual");
            }
        }
    }
    
    /**
     * Obtiene el texto de un elemento.
     */
    protected String obtenerTexto(By localizador) {
        WebElement elemento = buscarElementoVisible(localizador);
        return elemento.getText().trim();
    }
    
    /**
     * Obtiene el valor de un atributo.
     */
    protected String obtenerAtributo(By localizador, String atributo) {
        WebElement elemento = buscarElementoVisible(localizador);
        return elemento.getAttribute(atributo);
    }
    
    /**
     * Selecciona opción de dropdown por texto visible.
     */
    protected void seleccionarPorTexto(By localizador, String texto) {
        logger.debug("Seleccionando opción '{}' en dropdown: {}", texto, localizador);
        WebElement elemento = buscarElementoVisible(localizador);
        Select select = new Select(elemento);
        select.selectByVisibleText(texto);
    }
    
    /**
     * Selecciona opción de dropdown por valor.
     */
    protected void seleccionarPorValor(By localizador, String valor) {
        logger.debug("Seleccionando valor '{}' en dropdown: {}", valor, localizador);
        WebElement elemento = buscarElementoVisible(localizador);
        Select select = new Select(elemento);
        select.selectByValue(valor);
    }
    
    // Métodos de navegación y ventanas
    
    /**
     * Cambia a una nueva ventana o pestaña.
     */
    protected void cambiarANuevaVentana() {
        Set<String> ventanas = driver.getWindowHandles();
        for (String ventana : ventanas) {
            driver.switchTo().window(ventana);
        }
    }
    
    /**
     * Vuelve a la ventana principal.
     */
    protected void volverAVentanaPrincipal() {
        driver.switchTo().defaultContent();
    }
    
    /**
     * Cambia a un iframe.
     */
    protected void cambiarAIframe(By localizador) {
        WebElement iframe = buscarElementoVisible(localizador);
        driver.switchTo().frame(iframe);
    }
    
    // Métodos de scroll y navegación
    
    /**
     * Hace scroll hacia un elemento.
     */
    protected void scrollHaciaElemento(By localizador) {
        WebElement elemento = buscarElementoVisible(localizador);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elemento);
    }
    
    /**
     * Hace scroll hacia arriba de la página.
     */
    protected void scrollAlInicio() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
    }
    
    /**
     * Hace scroll hacia abajo de la página.
     */
    protected void scrollAlFinal() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }
    
    // Métodos de manejo de alertas
    
    /**
     * Acepta una alerta JavaScript.
     */
    protected void aceptarAlerta() {
        try {
            Alert alerta = espera.until(ExpectedConditions.alertIsPresent());
            String textoAlerta = alerta.getText();
            logger.info("Aceptando alerta: {}", textoAlerta);
            alerta.accept();
        } catch (TimeoutException e) {
            logger.warn("No se encontró alerta para aceptar");
        }
    }
    
    /**
     * Cancela una alerta JavaScript.
     */
    protected void cancelarAlerta() {
        try {
            Alert alerta = espera.until(ExpectedConditions.alertIsPresent());
            String textoAlerta = alerta.getText();
            logger.info("Cancelando alerta: {}", textoAlerta);
            alerta.dismiss();
        } catch (TimeoutException e) {
            logger.warn("No se encontró alerta para cancelar");
        }
    }
    
    /**
     * Obtiene el texto de una alerta.
     */
    protected String obtenerTextoAlerta() {
        try {
            Alert alerta = espera.until(ExpectedConditions.alertIsPresent());
            return alerta.getText();
        } catch (TimeoutException e) {
            logger.warn("No se encontró alerta");
            return null;
        }
    }
    
    // Métodos de información de página
    
    /**
     * Obtiene el título actual de la página.
     */
    protected String obtenerTituloActual() {
        return driver.getTitle();
    }
    
    /**
     * Obtiene la URL actual.
     */
    protected String obtenerUrlActual() {
        return driver.getCurrentUrl();
    }
    
    /**
     * Verifica si estamos en la URL esperada.
     */
    protected boolean estaEnUrl(String urlEsperada) {
        String urlActual = obtenerUrlActual();
        return urlActual.contains(urlEsperada);
    }
    
    // Métodos de utilidad
    
    /**
     * Pausa la ejecución por un tiempo determinado.
     */
    protected void pausar(Duration duracion) {
        try {
            Thread.sleep(duracion.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Pausa interrumpida");
        }
    }
    
    /**
     * Refresca la página actual.
     */
    protected void refrescarPagina() {
        logger.info("Refrescando página");
        driver.navigate().refresh();
        esperarCargaPaginaCompleta();
    }
    
    /**
     * Va hacia atrás en el historial.
     */
    protected void irAtras() {
        logger.info("Navegando hacia atrás");
        driver.navigate().back();
        esperarCargaPaginaCompleta();
    }
    
    /**
     * Va hacia adelante en el historial.
     */
    protected void irAdelante() {
        logger.info("Navegando hacia adelante");
        driver.navigate().forward();
        esperarCargaPaginaCompleta();
    }
    
    /**
     * Toma una captura de pantalla.
     */
    protected byte[] tomarCaptura() {
        if (driver instanceof TakesScreenshot) {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        }
        logger.warn("El driver no soporta capturas de pantalla");
        return new byte[0];
    }
    
    /**
     * Método de utilidad para logging de información de depuración.
     */
    protected void logearInformacionPagina() {
        logger.debug("=== INFORMACIÓN DE PÁGINA ===");
        logger.debug("Título: {}", obtenerTituloActual());
        logger.debug("URL: {}", obtenerUrlActual());
        logger.debug("Página cargada: {}", estaPaginaCargada());
        logger.debug("===============================");
    }
}