package com.automatizacion.proyecto.paginas;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.PageFactory;
import com.automatizacion.proyecto.utilidades.EsperaExplicita;

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
    
    protected final WebDriver driver;
    protected final EsperaExplicita espera;
    protected static final int TIEMPO_ESPERA_ELEMENTOS = 15;
    protected static final int MAX_REINTENTOS = 3;
    
    /**
     * Constructor base que inicializa el driver y las esperas.
     */
    protected PaginaBase(WebDriver driver, int tiempoEsperaSegundos) {
        this.driver = driver;
        this.espera = new EsperaExplicita(driver, tiempoEsperaSegundos);
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
            espera.esperarCargaCompleta();
            // Subir página para evitar propaganda después de cargar
            espera.subirPaginaParaEvitarPropaganda();
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
     * Verifica si un elemento está visible.
     */
    protected boolean esElementoVisible(By localizador) {
        try {
            espera.esperarElementoVisible(localizador);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Hace clic en un elemento con manejo robusto.
     */
    protected void hacerClicElemento(By localizador) {
        try {
            WebElement elemento = espera.esperarElementoClickeable(localizador);
            elemento.click();
        } catch (Exception e) {
            // Intento alternativo con JavaScript si el clic normal falla
            try {
                WebElement elemento = espera.esperarElementoPresente(localizador);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", elemento);
            } catch (Exception ex) {
                throw new RuntimeException("No se pudo hacer clic en el elemento: " + localizador, ex);
            }
        }
    }
    
    /**
     * Ingresa texto en un campo con limpieza previa.
     */
    protected void ingresarTextoEnCampo(By localizador, String texto) {
        try {
            WebElement campo = espera.esperarElementoVisible(localizador);
            campo.clear();
            campo.sendKeys(texto);
        } catch (Exception e) {
            throw new RuntimeException("Error al ingresar texto en el campo: " + localizador, e);
        }
    }
    
    /**
     * Obtiene el texto de un elemento.
     */
    protected String obtenerTextoElemento(By localizador) {
        try {
            WebElement elemento = espera.esperarElementoVisible(localizador);
            return elemento.getText().trim();
        } catch (Exception e) {
            return "";
        }
    }
    
    /**
     * Verifica si un elemento está habilitado.
     */
    protected boolean esElementoHabilitado(By localizador) {
        try {
            WebElement elemento = espera.esperarElementoPresente(localizador);
            return elemento.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Espera a que aparezca un mensaje y lo retorna.
     */
    protected String esperarYObtenerMensaje(By localizadorMensaje) {
        return espera.esperarMensajeFlash(localizadorMensaje);
    }
    
    /**
     * Recarga la página actual.
     */
    public void recargarPagina() {
        driver.navigate().refresh();
        espera.esperarCargaCompleta();
        espera.subirPaginaParaEvitarPropaganda();
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
        espera.subirPaginaParaEvitarPropaganda();
    }
    
    /**
     * Método template que debe ser implementado por cada página
     * para verificar que está completamente cargada.
     */
    public abstract boolean esPaginaVisible();
    
    /**
     * Método template para validar elementos específicos de cada página.
     */
    protected abstract void validarElementosPagina();
    
    /**
     * Método auxiliar para logging de acciones.
     */
    protected void log(String mensaje) {
        System.out.println(String.format("[%s] %s", 
            this.getClass().getSimpleName(), mensaje));
    }
    
    /**
     * Manejo robusto de elementos con reintentos.
     * Útil para elementos que aparecen dinámicamente.
     */
    protected WebElement obtenerElementoConReintentos(By localizador) {
        return espera.esperarElementoConReintentos(localizador, MAX_REINTENTOS);
    }
    
    /**
     * Espera a que la página esté lista para interactuar.
     * Incluye subir página para evitar propaganda.
     */
    protected void prepararPaginaParaInteraccion() {
        espera.esperarCargaCompleta();
        subirPagina();
        // Pequeña pausa adicional para asegurar que la propaganda desaparece
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}