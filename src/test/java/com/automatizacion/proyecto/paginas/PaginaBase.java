package com.automatizacion.proyecto.paginas;

import com.automatizacion.proyecto.enums.TipoMensaje;
import com.automatizacion.proyecto.paginas.interfaces.IPaginaBase;
import com.automatizacion.proyecto.utilidades.EsperaExplicita;
import com.automatizacion.proyecto.utilidades.GestorCapturaPantalla;
import com.automatizacion.proyecto.utilidades.ManejadorScrollPagina;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;

public abstract class PaginaBase implements IPaginaBase {
    
    protected static final Logger logger = LoggerFactory.getLogger(PaginaBase.class);
    protected final WebDriver driver;
    protected final WebDriverWait wait;
    protected final EsperaExplicita esperaExplicita;
    protected final GestorCapturaPantalla gestorCaptura;
    protected final ManejadorScrollPagina manejadorScroll;
    protected final JavascriptExecutor jsExecutor;
    protected static final int TIMEOUT_DEFECTO = 10;
    
    protected PaginaBase(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_DEFECTO));
        this.esperaExplicita = new EsperaExplicita(driver);
        this.gestorCaptura = new GestorCapturaPantalla(driver);
        this.manejadorScroll = new ManejadorScrollPagina(driver);
        this.jsExecutor = (JavascriptExecutor) driver;
        PageFactory.initElements(driver, this);
    }
    
    @Override
    public String obtenerTituloPagina() {
        try {
            return driver.getTitle();
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error obteniendo título: " + e.getMessage()));
            return "";
        }
    }
    
    @Override
    public boolean esperarCargaPagina(int timeoutSegundos) {
        try {
            WebDriverWait waitCustom = new WebDriverWait(driver, Duration.ofSeconds(timeoutSegundos));
            waitCustom.until(webDriver -> jsExecutor.executeScript("return document.readyState").equals("complete"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public void navegarAtras() {
        driver.navigate().back();
        esperarCargaPagina(TIMEOUT_DEFECTO);
    }
    
    @Override
    public void actualizarPagina() {
        driver.navigate().refresh();
        esperarCargaPagina(TIMEOUT_DEFECTO);
    }
    
    protected boolean esperarElementoVisible(WebElement elemento, int timeoutSegundos) {
        try {
            WebDriverWait waitCustom = new WebDriverWait(driver, Duration.ofSeconds(timeoutSegundos));
            waitCustom.until(ExpectedConditions.visibilityOf(elemento));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    protected boolean esperarElementoClickeable(WebElement elemento, int timeoutSegundos) {
        try {
            WebDriverWait waitCustom = new WebDriverWait(driver, Duration.ofSeconds(timeoutSegundos));
            waitCustom.until(ExpectedConditions.elementToBeClickable(elemento));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    protected void ingresarTextoSeguro(WebElement elemento, String texto, boolean limpiarAntes) {
        try {
            if (esperarElementoVisible(elemento, TIMEOUT_DEFECTO)) {
                if (limpiarAntes) {
                    elemento.clear();
                }
                elemento.sendKeys(texto);
            }
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error ingresando texto: " + e.getMessage()));
            throw new RuntimeException("No se pudo ingresar texto", e);
        }
    }
    
    protected void clickSeguro(WebElement elemento) {
        try {
            if (esperarElementoClickeable(elemento, TIMEOUT_DEFECTO)) {
                manejadorScroll.scrollHastaElemento(elemento);
                elemento.click();
            }
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error haciendo click: " + e.getMessage()));
            throw new RuntimeException("No se pudo hacer click", e);
        }
    }
    
    protected String obtenerTextoSeguro(WebElement elemento) {
        try {
            if (esperarElementoVisible(elemento, 5)) {
                return elemento.getText().trim();
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }
    
    protected boolean esElementoVisible(WebElement elemento) {
        try {
            return elemento.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    protected boolean hayErroresValidacion() {
        try {
            return driver.findElements(org.openqa.selenium.By.cssSelector(".error-message, .field-error, .alert-danger"))
                        .stream().anyMatch(WebElement::isDisplayed);
        } catch (Exception e) {
            return false;
        }
    }
    
    protected void capturarPantalla(String nombreArchivo) {
        gestorCaptura.capturarPantalla(nombreArchivo);
    }
    
    public abstract void validarElementosPagina();
}