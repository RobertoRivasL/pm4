package com.automatizacion.proyecto.paginas;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.PageFactory;
import com.automatizacion.proyecto.utilidades.EsperaExplicita;
import com.automatizacion.proyecto.utilidades.GestorCapturaPantalla;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase base para todos los Page Objects.
 * Implementa funcionalidad común y patrones de diseño.
 * 
 * Principios aplicados:
 * - SRP: Funcionalidad base común para todas las páginas
 * - DRY: Evita repetición de código en Page Objects
 * - Template Method: Define estructura común para páginas
 * - Encapsulación: Métodos protegidos para uso de subclases
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
public abstract class PaginaBase {
    protected WebDriver driver;
    protected EsperaExplicita espera;
    protected GestorCapturaPantalla gestorCaptura;

    public PaginaBase(WebDriver driver, EsperaExplicita espera, GestorCapturaPantalla gestorCaptura) {
        this.driver = driver;
        this.espera = espera;
        this.gestorCaptura = gestorCaptura;
    }
    
    /**
     * Constructor base que inicializa el driver y las esperas.
     */
    protected PaginaBase(WebDriver driver, int tiempoEsperaSegundos) {
        this.driver = driver;
        this.espera = new EsperaExplicita(driver, tiempoEsperaSegundos);
        PageFactory.initElements(driver, this);
        logger.debug("Página base inicializada con timeout: {} segundos", tiempoEsperaSegundos);
    }
    
    /**
     * Constructor con tiempo de espera por defecto.
     */
    protected PaginaBase(WebDriver driver) {
        this(driver, TIEMPO_ESPERA_ELEMENTOS);
    }
    
    // ===== MÉTODOS DE NAVEGACIÓN =====
    
    /**
     * Navega a la URL especificada.
     */
    public void navegarA(String url) {
        try {
            logger.info("Navegando a: {}", url);
            driver.get(url);
            espera.esperarCargaCompleta();
            
            // Esperar un momento adicional para elementos dinámicos
            Thread.sleep(1000);
            
            logger.debug("Navegación completada exitosamente");
        } catch (Exception e) {
            logger.error("Error al navegar a la URL: {}", url, e);
            throw new RuntimeException("Error al navegar a la URL: " + url, e);
        }
    }
    
    /**
     * Actualiza la página actual.
     */
    public void actualizarPagina() {
        try {
            logger.debug("Actualizando página");
            driver.navigate().refresh();
            espera.esperarCargaCompleta();
        } catch (Exception e) {
            logger.error("Error al actualizar página", e);
            throw new RuntimeException("Error al actualizar página", e);
        }
    }
    
    // ===== MÉTODOS DE INFORMACIÓN =====
    
    /**
     * Obtiene el título de la página actual.
     */
    public String obtenerTituloPagina() {
        try {
            String titulo = driver.getTitle();
            logger.debug("Título de página: {}", titulo);
            return titulo;
        } catch (Exception e) {
            logger.warn("Error al obtener título de página", e);
            return "";
        }
    }
    
    /**
     * Obtiene la URL actual.
     */
    public String obtenerUrlActual() {
        try {
            String url = driver.getCurrentUrl();
            logger.debug("URL actual: {}", url);
            return url;
        } catch (Exception e) {
            logger.warn("Error al obtener URL actual", e);
            return "";
        }
    }
    
    // ===== MÉTODOS DE VERIFICACIÓN =====
    
    /**
     * Verifica si un elemento está visible usando localizador.
     */
    protected boolean esElementoVisible(By localizador) {
        try {
            espera.esperarElementoVisible(localizador, TIEMPO_ESPERA_CORTO);
            return true;
        } catch (TimeoutException e) {
            logger.debug("Elemento no visible: {}", localizador);
            return false;
        } catch (Exception e) {
            logger.warn("Error al verificar visibilidad del elemento: {}", localizador, e);
            return false;
        }
    }
    
    /**
     * Verifica si un elemento está presente en el DOM.
     */
    protected boolean esElementoPresente(By localizador) {
        try {
            driver.findElement(localizador);
            return true;
        } catch (Exception e) {
            logger.debug("Elemento no presente: {}", localizador);
            return false;
        }
    }
    
    /**
     * Verifica si un WebElement está visible.
     */
    protected boolean esElementoVisible(WebElement elemento) {
        try {
            return elemento != null && elemento.isDisplayed();
        } catch (Exception e) {
            logger.debug("WebElement no visible", e);
            return false;
        }
    }
    
    // ===== MÉTODOS DE INTERACCIÓN =====
    
    /**
     * Hace clic en un elemento con manejo robusto.
     */
    protected void hacerClicRobusto(WebElement elemento) {
        for (int intento = 1; intento <= MAX_REINTENTOS; intento++) {
            try {
                // Scroll al elemento si es necesario
                scrollAlElemento(elemento);
                
                // Esperar que sea clickeable
                espera.esperarElementoClickeable(elemento);
                
                // Hacer clic
                elemento.click();
                
                logger.debug("Click exitoso en intento {}", intento);
                return;
                
            } catch (Exception e) {
                logger.warn("Error en click, intento {}: {}", intento, e.getMessage());
                
                if (intento == MAX_REINTENTOS) {
                    // Último intento con JavaScript
                    try {
                        hacerClicConJavaScript(elemento);
                        logger.debug("Click con JavaScript exitoso");
                        return;
                    } catch (Exception jsException) {
                        logger.error("Falló click después de {} intentos", MAX_REINTENTOS, jsException);
                        throw new RuntimeException("No se pudo hacer clic en el elemento después de " + MAX_REINTENTOS + " intentos", jsException);
                    }
                }
                
                // Esperar antes del siguiente intento
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Interrumpido durante reintentos de click", ie);
                }
            }
        }
    }
    
    /**
     * Hace clic usando JavaScript como alternativa.
     */
    protected void hacerClicConJavaScript(WebElement elemento) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", elemento);
            logger.debug("Click con JavaScript ejecutado");
        } catch (Exception e) {
            logger.error("Error al hacer click con JavaScript", e);
            throw new RuntimeException("Error al hacer click con JavaScript", e);
        }
    }
    
    /**
     * Limpia un campo de texto e ingresa nuevo texto.
     */
    protected void limpiarEIngresarTexto(WebElement elemento, String texto) {
        try {
            // Scroll al elemento
            scrollAlElemento(elemento);
            
            // Esperar que sea visible
            espera.esperarElementoVisible(elemento);
            
            // Limpiar campo
            elemento.clear();
            
            // Limpiar con Ctrl+A y Delete como respaldo
            elemento.sendKeys(Keys.CONTROL + "a");
            elemento.sendKeys(Keys.DELETE);
            
            // Ingresar nuevo texto
            if (texto != null && !texto.isEmpty()) {
                elemento.sendKeys(texto);
            }
            
            logger.debug("Texto ingresado exitosamente: {}", texto != null ? texto.substring(0, Math.min(texto.length(), 20)) + "..." : "");
            
        } catch (Exception e) {
            logger.error("Error al ingresar texto: {}", e.getMessage());
            throw new RuntimeException("Error al ingresar texto", e);
        }
    }
    
    /**
     * Obtiene el texto de un elemento de forma robusta.
     */
    protected String obtenerTextoElemento(WebElement elemento) {
        try {
            espera.esperarElementoVisible(elemento);
            String texto = elemento.getText().trim();
            
            // Si getText() está vacío, intentar con getAttribute
            if (texto.isEmpty()) {
                texto = elemento.getAttribute("textContent");
                if (texto != null) {
                    texto = texto.trim();
                }
            }
            
            logger.debug("Texto obtenido: {}", texto);
            return texto != null ? texto : "";
            
        } catch (Exception e) {
            logger.warn("Error al obtener texto del elemento", e);
            return "";
        }
    }
    
    /**
     * Obtiene el valor de un atributo de forma robusta.
     */
    protected String obtenerAtributoElemento(WebElement elemento, String atributo) {
        try {
            espera.esperarElementoVisible(elemento);
            String valor = elemento.getAttribute(atributo);
            logger.debug("Atributo '{}' obtenido: {}", atributo, valor);
            return valor != null ? valor.trim() : "";
        } catch (Exception e) {
            logger.warn("Error al obtener atributo '{}' del elemento", atributo, e);
            return "";
        }
    }
    
    // ===== MÉTODOS DE SCROLL =====
    
    /**
     * Hace scroll hasta un elemento específico.
     */
    protected void scrollAlElemento(WebElement elemento) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", elemento);
            
            // Esperar que el scroll se complete
            Thread.sleep(500);
            
            logger.debug("Scroll al elemento completado");
        } catch (Exception e) {
            logger.warn("Error al hacer scroll al elemento", e);
        }
    }
    
    /**
     * Hace scroll hasta el top de la página.
     */
    protected void scrollAlInicio() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollTo(0, 0);");
            Thread.sleep(500);
            logger.debug("Scroll al inicio completado");
        } catch (Exception e) {
            logger.warn("Error al hacer scroll al inicio", e);
        }
    }
    
    /**
     * Hace scroll hasta el final de la página.
     */
    protected void scrollAlFinal() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
            Thread.sleep(500);
            logger.debug("Scroll al final completado");
        } catch (Exception e) {
            logger.warn("Error al hacer scroll al final", e);
        }
    }
    
    // ===== MÉTODOS DE ESPERA =====
    
    /**
     * Espera explícita personalizada.
     */
    protected void esperarTiempo(long milisegundos) {
        try {
            Thread.sleep(milisegundos);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Espera interrumpida", e);
        }
    }
    
    /**
     * Espera hasta que un elemento sea clickeable.
     */
    protected void esperarElementoClickeable(By localizador) {
        try {
            espera.esperarElementoClickeable(localizador);
        } catch (Exception e) {
            logger.error("Timeout esperando elemento clickeable: {}", localizador);
            throw new RuntimeException("Elemento no clickeable: " + localizador, e);
        }
    }
    
    /**
     * Espera hasta que un elemento sea visible.
     */
    protected void esperarElementoVisible(By localizador) {
        try {
            espera.esperarElementoVisible(localizador);
        } catch (Exception e) {
            logger.error("Timeout esperando elemento visible: {}", localizador);
            throw new RuntimeException("Elemento no visible: " + localizador, e);
        }
    }
    
    // ===== MÉTODOS DE JAVASCRIPT =====
    
    /**
     * Ejecuta JavaScript personalizado.
     */
    protected Object ejecutarJavaScript(String script, Object... argumentos) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            Object resultado = js.executeScript(script, argumentos);
            logger.debug("JavaScript ejecutado exitosamente");
            return resultado;
        } catch (Exception e) {
            logger.error("Error al ejecutar JavaScript: {}", script, e);
            throw new RuntimeException("Error al ejecutar JavaScript", e);
        }
    }
    
    /**
     * Verifica si la página está completamente cargada.
     */
    protected boolean esPaginaCargada() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            String readyState = js.executeScript("return document.readyState").toString();
            return "complete".equals(readyState);
        } catch (Exception e) {
            logger.warn("Error al verificar estado de carga de página", e);
            return false;
        }
    }
    
    /**
     * Resalta un elemento para debugging visual.
     */
    protected void resaltarElemento(WebElement elemento) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].style.border='3px solid red'", elemento);
            Thread.sleep(1000);
            js.executeScript("arguments[0].style.border=''", elemento);
        } catch (Exception e) {
            logger.debug("Error al resaltar elemento", e);
        }
    }
    
    // ===== MÉTODOS ABSTRACTOS =====
    
    /**
     * Método abstracto que debe implementar cada página concreta.
     * Verifica si la página específica está visible y cargada.
     */
    public abstract boolean esPaginaVisible();
    
    // ===== MÉTODOS DE UTILIDAD =====
    
    /**
     * Obtiene información de depuración de la página actual.
     */
    protected String obtenerInfoDepuracion() {
        try {
            return String.format("URL: %s | Título: %s | ReadyState: %s", 
                obtenerUrlActual(), 
                obtenerTituloPagina(),
                esPaginaCargada() ? "complete" : "loading");
        } catch (Exception e) {
            return "Error al obtener información de depuración: " + e.getMessage();
        }
    }
    
    /**
     * Valida que el driver esté disponible y la página cargada.
     */
    protected void validarEstadoPagina() {
        if (driver == null) {
            throw new IllegalStateException("WebDriver no está inicializado");
        }
        
        if (!esPaginaCargada()) {
            logger.warn("La página puede no estar completamente cargada");
        }
    }
}