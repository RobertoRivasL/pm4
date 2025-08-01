package test.java.com.automatizacion.proyecto.paginas;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.Keys;
import com.automatizacion.proyecto.utilidades.EsperaExplicita;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Clase base para todos los Page Objects.
 * Implementa funcionalidad común y patrones de diseño.
 * 
 * Principios aplicados:
 * - SRP: Funcionalidad base común para todas las páginas
 * - DRY: Evita repetición de código en Page Objects
 * - Template Method: Define estructura común para páginas
 * 
 * @author Roberto Rivas Lopez
 */
public abstract class PaginaBase {

    protected static final Logger logger = LoggerFactory.getLogger(PaginaBase.class);
    protected final WebDriver driver;
    protected final EsperaExplicita espera;
    protected final WebDriverWait wait;
    protected static final int TIEMPO_ESPERA_ELEMENTOS = 15;
    protected static final int MAX_REINTENTOS = 3;

    private PaginaLogin paginaLogin;
    private PaginaRegistro paginaRegistro;
  

    /**
     * Constructor base que inicializa el driver y las esperas.
     */
    protected PaginaBase(WebDriver driver, int tiempoEsperaSegundos) {
        this.driver = driver;
        this.espera = new EsperaExplicita(driver, tiempoEsperaSegundos);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(tiempoEsperaSegundos));
        PageFactory.initElements(driver, this);
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
            driver.get(url);
            esperarCargaCompleta();
            subirPaginaParaEvitarPropaganda();
        } catch (Exception e) {
            throw new RuntimeException("Error al navegar a la URL: " + url, e);
        }
    }

    /**
     * Obtiene el título de la página actual.
     */
    public String obtenerTituloPagina() {
        return driver.getTitle();
    }

    /**
     * Obtiene la URL actual.
     */
    public String obtenerUrlActual() {
        return driver.getCurrentUrl();
    }

    /**
     * Verifica si un elemento está visible usando localizador.
     */
    protected boolean esElementoVisible(By localizador) {
        try {
            return espera.esperarElementoVisible(localizador) != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica si un WebElement está visible.
     */
    protected boolean esElementoVisible(WebElement elemento) {
        try {
            return elemento.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Espera que un elemento sea visible usando localizador.
     */
    protected WebElement esperarElementoVisible(By localizador, int timeoutSegundos) {
        try {
            WebDriverWait waitCustom = new WebDriverWait(driver, Duration.ofSeconds(timeoutSegundos));
            return waitCustom.until(ExpectedConditions.visibilityOfElementLocated(localizador));
        } catch (Exception e) {
            logger.error("Elemento no visible después de {} segundos: {}", timeoutSegundos, localizador);
            throw new RuntimeException("Elemento no visible: " + localizador, e);
        }
    }

    /**
     * Espera que un WebElement sea visible.
     */
    protected WebElement esperarElementoVisible(WebElement elemento, int timeoutSegundos) {
        try {
            WebDriverWait waitCustom = new WebDriverWait(driver, Duration.ofSeconds(timeoutSegundos));
            return waitCustom.until(ExpectedConditions.visibilityOf(elemento));
        } catch (Exception e) {
            logger.error("Elemento no visible después de {} segundos", timeoutSegundos);
            throw new RuntimeException("Elemento no visible", e);
        }
    }

    /**
     * Espera que un elemento sea clickeable usando localizador.
     */
    protected WebElement esperarElementoClicable(By localizador, int timeoutSegundos) {
        try {
            WebDriverWait waitCustom = new WebDriverWait(driver, Duration.ofSeconds(timeoutSegundos));
            return waitCustom.until(ExpectedConditions.elementToBeClickable(localizador));
        } catch (Exception e) {
            logger.error("Elemento no clickeable después de {} segundos: {}", timeoutSegundos, localizador);
            throw new RuntimeException("Elemento no clickeable: " + localizador, e);
        }
    }

    /**
     * Espera que un WebElement sea clickeable.
     */
    protected WebElement esperarElementoClicable(WebElement elemento, int timeoutSegundos) {
        try {
            WebDriverWait waitCustom = new WebDriverWait(driver, Duration.ofSeconds(timeoutSegundos));
            return waitCustom.until(ExpectedConditions.elementToBeClickable(elemento));
        } catch (Exception e) {
            logger.error("Elemento no clickeable después de {} segundos", timeoutSegundos);
            throw new RuntimeException("Elemento no clickeable", e);
        }
    }

    /**
     * Espera que un elemento esté presente en el DOM.
     */
    protected WebElement esperarElementoPresente(By localizador, int timeoutSegundos) {
        try {
            WebDriverWait waitCustom = new WebDriverWait(driver, Duration.ofSeconds(timeoutSegundos));
            return waitCustom.until(ExpectedConditions.presenceOfElementLocated(localizador));
        } catch (Exception e) {
            logger.error("Elemento no presente después de {} segundos: {}", timeoutSegundos, localizador);
            throw new RuntimeException("Elemento no presente: " + localizador, e);
        }
    }

    /**
     * Espera que un elemento se vuelva invisible.
     */
    protected boolean esperarElementoInvisible(By localizador, int timeoutSegundos) {
        try {
            WebDriverWait waitCustom = new WebDriverWait(driver, Duration.ofSeconds(timeoutSegundos));
            return waitCustom.until(ExpectedConditions.invisibilityOfElementLocated(localizador));
        } catch (Exception e) {
            logger.warn("Elemento sigue visible después de {} segundos: {}", timeoutSegundos, localizador);
            return false;
        }
    }

    /**
     * Espera que un WebElement se vuelva invisible.
     */
    protected boolean esperarElementoInvisible(WebElement elemento, int timeoutSegundos) {
        try {
            WebDriverWait waitCustom = new WebDriverWait(driver, Duration.ofSeconds(timeoutSegundos));
            return waitCustom.until(ExpectedConditions.invisibilityOf(elemento));
        } catch (Exception e) {
            logger.warn("Elemento sigue visible después de {} segundos", timeoutSegundos);
            return false;
        }
    }

    /**
     * Hace clic en un elemento con manejo robusto usando localizador.
     */
    protected void hacerClick(By localizador) {
        try {
            WebElement elemento = esperarElementoClicable(localizador, TIEMPO_ESPERA_ELEMENTOS);
            elemento.click();
        } catch (Exception e) {
            // Intento alternativo con JavaScript
            try {
                WebElement elemento = esperarElementoPresente(localizador, 5);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", elemento);
            } catch (Exception ex) {
                throw new RuntimeException("No se pudo hacer clic en el elemento: " + localizador, ex);
            }
        }
    }

    /**
     * Hace clic en un WebElement con manejo robusto.
     */
    protected void hacerClick(WebElement elemento) {
        try {
            esperarElementoClicable(elemento, TIEMPO_ESPERA_ELEMENTOS);
            elemento.click();
        } catch (Exception e) {
            // Intento alternativo con JavaScript
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", elemento);
            } catch (Exception ex) {
                throw new RuntimeException("No se pudo hacer clic en el elemento", ex);
            }
        }
    }

    /**
     * Limpia un campo y escribe texto.
     */
    protected void limpiarYEscribir(WebElement elemento, String texto) {
        try {
            esperarElementoVisible(elemento, TIEMPO_ESPERA_ELEMENTOS);
            elemento.clear();
            if (texto != null && !texto.isEmpty()) {
                elemento.sendKeys(texto);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al escribir texto en el elemento", e);
        }
    }

    /**
     * Obtiene el texto de un elemento usando localizador.
     */
    protected String obtenerTexto(By localizador) {
        try {
            WebElement elemento = esperarElementoVisible(localizador, TIEMPO_ESPERA_ELEMENTOS);
            return elemento.getText().trim();
        } catch (Exception e) {
            logger.warn("No se pudo obtener texto del elemento: {}", localizador);
            return "";
        }
    }

    /**
     * Obtiene el texto de un WebElement.
     */
    protected String obtenerTexto(WebElement elemento) {
        try {
            esperarElementoVisible(elemento, TIEMPO_ESPERA_ELEMENTOS);
            return elemento.getText().trim();
        } catch (Exception e) {
            logger.warn("No se pudo obtener texto del elemento");
            return "";
        }
    }

    /**
     * Verifica si un elemento está habilitado usando localizador.
     */
    protected boolean esElementoHabilitado(By localizador) {
        try {
            WebElement elemento = esperarElementoPresente(localizador, 5);
            return elemento.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica si un WebElement está habilitado.
     */
    protected boolean esElementoHabilitado(WebElement elemento) {
        try {
            return elemento.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Espera a que aparezca un mensaje flash y lo retorna.
     */
    protected String esperarMensajeFlash(By localizadorMensaje) {
        try {
            WebElement elemento = esperarElementoVisible(localizadorMensaje, 10);
            return elemento.getText().trim();
        } catch (Exception e) {
            logger.debug("No se encontró mensaje flash en: {}", localizadorMensaje);
            return "";
        }
    }

    /**
     * Recarga la página actual.
     */
    public void recargarPagina() {
        driver.navigate().refresh();
        esperarCargaCompleta();
        subirPaginaParaEvitarPropaganda();
    }

    /**
     * Ejecuta JavaScript en la página.
     */
    protected Object ejecutarJavaScript(String script, Object... argumentos) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return js.executeScript(script, argumentos);
    }

    /**
     * Scroll hacia arriba de la página (útil para evitar propaganda).
     */
    protected void subirPagina() {
        subirPaginaParaEvitarPropaganda();
    }

    /**
     * Implementación del método para subir página y evitar propaganda.
     */
    protected void subirPaginaParaEvitarPropaganda() {
        try {
            ejecutarJavaScript("window.scrollTo(0, 0);");
            Thread.sleep(500); // Pequeña pausa para permitir que se procese el scroll
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            logger.debug("Error ejecutando scroll hacia arriba: {}", e.getMessage());
        }
    }

    /**
     * Espera a que la página se cargue completamente.
     */
    protected void esperarCargaCompleta() {
        try {
            WebDriverWait waitLoad = new WebDriverWait(driver, Duration.ofSeconds(30));
            waitLoad.until(driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState")
                    .equals("complete"));
        } catch (Exception e) {
            logger.warn("Timeout esperando carga completa de página");
        }
    }

    /**
     * Manejo robusto de elementos con reintentos.
     */
    protected WebElement obtenerElementoConReintentos(By localizador) {
        for (int intento = 1; intento <= MAX_REINTENTOS; intento++) {
            try {
                return esperarElementoVisible(localizador, 5);
            } catch (Exception e) {
                if (intento == MAX_REINTENTOS) {
                    throw new RuntimeException(
                            "Elemento no encontrado después de " + MAX_REINTENTOS + " intentos: " + localizador, e);
                }
                logger.debug("Intento {} fallido para elemento: {}", intento, localizador);
                try {
                    Thread.sleep(1000); // Pausa entre reintentos
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
        throw new RuntimeException("Elemento no encontrado: " + localizador);
    }

    /**
     * Prepara la página para interacción.
     */
    protected void prepararPaginaParaInteraccion() {
        esperarCargaCompleta();
        subirPagina();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Método template que debe ser implementado por cada página
     * para verificar que está completamente cargada.
     */
    public abstract boolean esPaginaVisible();

    /**
     * Método template para validar elementos específicos de cada página.
     */
    protected void validarElementosPagina() {
        // Implementación por defecto vacía, las clases derivadas pueden sobreescribir
        logger.debug("Validación de elementos de página ejecutada para: {}", this.getClass().getSimpleName());
    }

    /**
     * Método auxiliar para logging de acciones.
     */
    protected void log(String mensaje) {
        logger.info("[{}] {}", this.getClass().getSimpleName(), mensaje);
    }
}