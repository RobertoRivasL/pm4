package com.automatizacion.proyecto.paginas;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatizacion.proyecto.utilidades.EsperaExplicita;
import com.automatizacion.proyecto.utilidades.GestorCapturaPantalla;
import com.automatizacion.proyecto.utilidades.ManejadorScrollPagina;
import com.automatizacion.proyecto.paginas.interfaces.IPaginaBase;
import com.automatizacion.proyecto.enums.TipoMensaje;

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
 * @version 1.0
 */
public abstract class PaginaBase implements IPaginaBase {
    
    private static final Logger logger = LoggerFactory.getLogger(PaginaBase.class);
    
    protected final WebDriver driver;
    protected final EsperaExplicita espera;
    protected final GestorCapturaPantalla gestorCaptura;
    protected final ManejadorScrollPagina manejadorScroll;
    
    protected static final int TIEMPO_ESPERA_ELEMENTOS = 15;
    protected static final int MAX_REINTENTOS = 3;
    protected static final int PAUSA_ENTRE_ACCIONES = 300;
    
    /**
     * Constructor base que inicializa el driver y las utilidades.
     * 
     * @param driver instancia de WebDriver
     * @param tiempoEsperaSegundos tiempo de espera personalizado
     */
    protected PaginaBase(WebDriver driver, int tiempoEsperaSegundos) {
        this.driver = driver;
        this.espera = new EsperaExplicita(driver, tiempoEsperaSegundos);
        this.gestorCaptura = new GestorCapturaPantalla();
        this.manejadorScroll = new ManejadorScrollPagina(driver);
        
        PageFactory.initElements(driver, this);
        
        logger.debug(TipoMensaje.DEBUG.formatearMensaje(
            "PaginaBase inicializada para " + this.getClass().getSimpleName()));
    }
    
    /**
     * Constructor con tiempo de espera por defecto.
     * 
     * @param driver instancia de WebDriver
     */
    protected PaginaBase(WebDriver driver) {
        this(driver, TIEMPO_ESPERA_ELEMENTOS);
    }
    
    // === IMPLEMENTACIÓN DE IPaginaBase ===
    
    @Override
    public String obtenerTituloPagina() {
        try {
            String titulo = driver.getTitle();
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Título de página obtenido: " + titulo));
            return titulo;
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error obteniendo título de página: " + e.getMessage()));
            return "";
        }
    }
    
    @Override
    public String obtenerUrlActual() {
        try {
            String url = driver.getCurrentUrl();
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("URL actual obtenida: " + url));
            return url;
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error obteniendo URL actual: " + e.getMessage()));
            return "";
        }
    }
    
    @Override
    public void navegarA(String url) {
        try {
            logger.info(TipoMensaje.INFORMATIVO.formatearMensaje("Navegando a: " + url));
            
            driver.get(url);
            esperarCargaCompleta();
            
            // Pequeña pausa para estabilidad
            Thread.sleep(PAUSA_ENTRE_ACCIONES);
            
            logger.info(TipoMensaje.EXITO.formatearMensaje("Navegación exitosa a: " + url));
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error navegando a URL: " + url + " - " + e.getMessage()));
            throw new RuntimeException("Error al navegar a la URL: " + url, e);
        }
    }
    
    @Override
    public WebElement esperarElementoVisible(By localizador, int timeoutSegundos) {
        try {
            return espera.esperarElementoVisible(localizador, timeoutSegundos);
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Elemento no visible: " + localizador.toString()));
            throw e;
        }
    }
    
    @Override
    public WebElement esperarElementoClickeable(By localizador, int timeoutSegundos) {
        try {
            WebDriverWait esperaPersonalizada = new WebDriverWait(driver, Duration.ofSeconds(timeoutSegundos));
            return esperaPersonalizada.until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(localizador));
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Elemento no clickeable: " + localizador.toString()));
            throw e;
        }
    }
    
    @Override
    public boolean estaElementoPresente(By localizador) {
        try {
            driver.findElement(localizador);
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "Elemento presente: " + localizador.toString()));
            return true;
        } catch (NoSuchElementException e) {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "Elemento no presente: " + localizador.toString()));
            return false;
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error verificando presencia de elemento: " + e.getMessage()));
            return false;
        }
    }
    
    @Override
    public boolean clickElementoSeguro(WebElement elemento) {
        for (int intento = 1; intento <= MAX_REINTENTOS; intento++) {
            try {
                logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                    "Intento " + intento + " de click en elemento"));
                
                // Verificar que el elemento esté habilitado
                if (!elemento.isEnabled()) {
                    logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                        "Elemento no habilitado para click"));
                    return false;
                }
                
                // Hacer scroll hasta el elemento si es necesario
                scrollHastaElemento(elemento);
                
                // Pequeña pausa para estabilidad
                Thread.sleep(PAUSA_ENTRE_ACCIONES);
                
                // Intentar click normal
                elemento.click();
                
                logger.debug(TipoMensaje.DEBUG.formatearMensaje("Click exitoso en elemento"));
                return true;
                
            } catch (Exception e) {
                logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                    "Intento " + intento + " de click falló: " + e.getMessage()));
                
                if (intento == MAX_REINTENTOS) {
                    // Último intento: usar JavaScript
                    try {
                        logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                            "Intentando click con JavaScript"));
                        
                        JavascriptExecutor js = (JavascriptExecutor) driver;
                        js.executeScript("arguments[0].click();", elemento);
                        
                        logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                            "Click con JavaScript exitoso"));
                        return true;
                        
                    } catch (Exception jsException) {
                        logger.error(TipoMensaje.ERROR.formatearMensaje(
                            "Click con JavaScript también falló: " + jsException.getMessage()));
                        return false;
                    }
                }
                
                // Pausa antes del siguiente intento
                try {
                    Thread.sleep(PAUSA_ENTRE_ACCIONES);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            }
        }
        return false;
    }
    
    @Override
    public boolean ingresarTextoSeguro(WebElement elemento, String texto) {
        if (texto == null) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                "Texto es null, no se puede ingresar"));
            return false;
        }
        
        for (int intento = 1; intento <= MAX_REINTENTOS; intento++) {
            try {
                logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                    "Intento " + intento + " de ingresar texto: " + texto));
                
                // Verificar que el elemento esté habilitado
                if (!elemento.isEnabled()) {
                    logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                        "Elemento no habilitado para ingresar texto"));
                    return false;
                }
                
                // Hacer scroll hasta el elemento
                scrollHastaElemento(elemento);
                
                // Limpiar campo primero
                elemento.clear();
                Thread.sleep(100);
                
                // Ingresar texto
                elemento.sendKeys(texto);
                
                // Verificar que el texto se ingresó correctamente
                String textoActual = elemento.getAttribute("value");
                if (texto.equals(textoActual)) {
                    logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                        "Texto ingresado exitosamente"));
                    return true;
                }
                
                logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                    "Texto no coincide. Esperado: " + texto + ", Actual: " + textoActual));
                
            } catch (Exception e) {
                logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                    "Intento " + intento + " de ingresar texto falló: " + e.getMessage()));
                
                if (intento < MAX_REINTENTOS) {
                    try {
                        Thread.sleep(PAUSA_ENTRE_ACCIONES);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        return false;
                    }
                }
            }
        }
        
        logger.error(TipoMensaje.ERROR.formatearMensaje(
            "No se pudo ingresar texto después de " + MAX_REINTENTOS + " intentos"));
        return false;
    }
    
    @Override
    public boolean limpiarCampo(WebElement elemento) {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Limpiando campo"));
            
            if (!elemento.isEnabled()) {
                logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                    "Elemento no habilitado para limpiar"));
                return false;
            }
            
            scrollHastaElemento(elemento);
            elemento.clear();
            
            // Verificar que se limpió
            String valorActual = elemento.getAttribute("value");
            boolean limpio = valorActual == null || valorActual.isEmpty();
            
            if (limpio) {
                logger.debug(TipoMensaje.DEBUG.formatearMensaje("Campo limpiado exitosamente"));
            } else {
                logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                    "Campo no se limpió completamente. Valor actual: " + valorActual));
            }
            
            return limpio;
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error limpiando campo: " + e.getMessage()));
            return false;
        }
    }
    
    @Override
    public String obtenerTextoElemento(WebElement elemento) {
        try {
            String texto = elemento.getText();
            if (texto == null || texto.trim().isEmpty()) {
                // Intentar obtener desde atributo value si el texto está vacío
                texto = elemento.getAttribute("value");
            }
            
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "Texto obtenido del elemento: " + texto));
            
            return texto != null ? texto : "";
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error obteniendo texto del elemento: " + e.getMessage()));
            return "";
        }
    }
    
    @Override
    public boolean estaElementoHabilitado(WebElement elemento) {
        try {
            boolean habilitado = elemento.isEnabled();
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "Elemento habilitado: " + habilitado));
            return habilitado;
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error verificando si elemento está habilitado: " + e.getMessage()));
            return false;
        }
    }
    
    @Override
    public boolean estaElementoSeleccionado(WebElement elemento) {
        try {
            boolean seleccionado = elemento.isSelected();
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "Elemento seleccionado: " + seleccionado));
            return seleccionado;
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error verificando si elemento está seleccionado: " + e.getMessage()));
            return false;
        }
    }
    
    @Override
    public String capturarPantalla(String nombreArchivo) {
        try {
            return gestorCaptura.capturarPantalla(driver, nombreArchivo);
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error capturando pantalla: " + e.getMessage()));
            return null;
        }
    }
    
    @Override
    public void scrollHastaElemento(WebElement elemento) {
        try {
            manejadorScroll.scrollHastaElemento(elemento);
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error haciendo scroll hacia elemento: " + e.getMessage()));
        }
    }
    
    @Override
    public void esperarCargaCompleta() {
        try {
            espera.esperarCargaCompleta();
            espera.esperarJQueryCompleto();
        } catch (Exception e) {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "Error esperando carga completa: " + e.getMessage()));
        }
    }
    
    // === MÉTODOS AUXILIARES PROTEGIDOS ===
    
    /**
     * Obtiene el gestor de capturas para uso en clases derivadas
     * 
     * @return instancia del gestor de capturas
     */
    protected GestorCapturaPantalla obtenerGestorCaptura() {
        return gestorCaptura;
    }
    
    /**
     * Obtiene el manejador de scroll para uso en clases derivadas
     * 
     * @return instancia del manejador de scroll
     */
    protected ManejadorScrollPagina obtenerManejadorScroll() {
        return manejadorScroll;
    }
    
    /**
     * Obtiene el gestor de esperas para uso en clases derivadas
     * 
     * @return instancia del gestor de esperas
     */
    protected EsperaExplicita obtenerEspera() {
        return espera;
    }
    
    /**
     * Pausa la ejecución por un tiempo específico
     * 
     * @param milisegundos tiempo de pausa
     */
    protected void pausar(long milisegundos) {
        try {
            Thread.sleep(milisegundos);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                "Pausa interrumpida: " + e.getMessage()));
        }
    }
    
    /**
     * Ejecuta JavaScript en el navegador
     * 
     * @param script script de JavaScript a ejecutar
     * @param argumentos argumentos para el script
     * @return resultado de la ejecución
     */
    protected Object ejecutarJavaScript(String script, Object... argumentos) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            return js.executeScript(script, argumentos);
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error ejecutando JavaScript: " + e.getMessage()));
            return null;
        }
    }
    
    /**
     * Verifica si un elemento es visible (está en pantalla y no oculto)
     * 
     * @param elemento elemento a verificar
     * @return true si el elemento es visible
     */
    protected boolean esElementoVisible(WebElement elemento) {
        try {
            return elemento.isDisplayed();
        } catch (Exception e) {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "Elemento no visible: " + e.getMessage()));
            return false;
        }
    }
    
    /**
     * Método template para verificar si la página está cargada.
     * Las clases derivadas deben implementar esPaginaVisible()
     * 
     * @return true si la página específica está visible
     */
    public abstract boolean esPaginaVisible();
}