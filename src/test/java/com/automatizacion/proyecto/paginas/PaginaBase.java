package com.automatizacion.proyecto.paginas;

import com.automatizacion.proyecto.configuracion.ConfiguracionGlobal;
import com.automatizacion.proyecto.enums.TipoMensaje;
import com.automatizacion.proyecto.utilidades.GestorCapturaPantalla;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Set;

/**
 * Clase base para implementación del patrón Page Object Model.
 * Proporciona funcionalidades comunes para todas las páginas de la aplicación.
 * 
 * MEJORAS ESPECÍFICAS PARA LOGIN:
 * - Manejo correcto de ventanas múltiples
 * - Esperas inteligentes para formularios
 * - Gestión automática de capturas
 * - Validaciones robustas de elementos
 * 
 * Principios aplicados:
 * - Template Method: Define estructura común para páginas
 * - Don't Repeat Yourself: Funcionalidades comunes centralizadas
 * - Single Responsibility: Cada método tiene una responsabilidad específica
 * 
 * @author Antonio B. Arriagada LL. (anarriag@gmail.com)
 * @author Dante Escalona Bustos (Jacobo.bustos.22@gmail.com)  
 * @author Roberto Rivas Lopez (umancl@gmail.com)
 * @version 2.0 - Versión mejorada con funcionalidades adicionales
 */
public abstract class PaginaBase {
    
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    protected final WebDriver driver;
    protected final WebDriverWait espera;
    protected final ConfiguracionGlobal config;
    protected final JavascriptExecutor jsExecutor;
    
    // Constantes para timeouts específicos
    protected static final int TIMEOUT_ELEMENTO_CORTO = 5;
    protected static final int TIMEOUT_ELEMENTO_LARGO = 15;
    protected static final int TIMEOUT_CARGA_PAGINA = 30;
    
    /**
     * Constructor base que inicializa componentes comunes
     * 
     * @param driver WebDriver activo
     */
    protected PaginaBase(WebDriver driver) {
        this.driver = driver;
        this.config = ConfiguracionGlobal.obtenerInstancia();
        this.espera = new WebDriverWait(driver, Duration.ofSeconds(config.obtenerTimeoutExplicito()));
        this.jsExecutor = (JavascriptExecutor) driver;
        
        // Inicializar elementos de la página usando PageFactory
        PageFactory.initElements(driver, this);
        
        logger.debug(TipoMensaje.DEBUG.formatearMensaje(
            "Inicializada página: " + this.getClass().getSimpleName()));
    }
    
    // === MÉTODOS ABSTRACTOS QUE DEBEN IMPLEMENTAR LAS PÁGINAS ===
    
    /**
     * Verifica si la página está cargada correctamente
     * @return true si la página está lista
     */
    public abstract boolean esPaginaVisible();
    
    /**
     * Obtiene la URL esperada de la página
     * @return URL de la página
     */
    public abstract String obtenerUrlEsperada();
    
    // === MÉTODOS DE NAVEGACIÓN Y CARGA ===
    
    /**
     * Navega a la página específica
     * MEJORADO: Manejo de ventanas múltiples para login
     */
    public void navegarAPagina() {
        String url = obtenerUrlEsperada();
        logger.info(TipoMensaje.PASO_PRUEBA.formatearMensaje("Navegando a: " + url));
        
        try {
            // Guardar handle de ventana actual
            String ventanaOriginal = driver.getWindowHandle();
            
            driver.get(url);
            
            // Verificar si se abrieron ventanas adicionales y cerrarlas
            gestionarVentanasMultiples(ventanaOriginal);
            
            // Esperar a que la página se cargue
            esperarCargaCompletaPagina();
            
            // Validar que estamos en la página correcta
            if (!esPaginaVisible()) {
                throw new RuntimeException("La página no se cargó correctamente: " + url);
            }
            
            logger.info(TipoMensaje.EXITO.formatearMensaje("Página cargada exitosamente"));
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensajeConExcepcion(
                "Error al navegar a la página", e));
            GestorCapturaPantalla.capturarPantallaError(driver, "navegacion_error", e);
            throw new RuntimeException("No se pudo cargar la página: " + url, e);
        }
    }
    
    /**
     * Espera a que la página se cargue completamente
     * CRÍTICO: Esperas específicas para formularios de login
     */
    protected void esperarCargaCompletaPagina() {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Esperando carga completa de página"));
            
            // Esperar a que el estado de la página sea 'complete'
            espera.until(driver -> 
                jsExecutor.executeScript("return document.readyState").equals("complete"));
            
            // Esperar a que jQuery termine (si está presente)
            try {
                espera.until(driver -> 
                    (Boolean) jsExecutor.executeScript("return jQuery.active == 0"));
                logger.debug(TipoMensaje.DEBUG.formatearMensaje("jQuery completado"));
            } catch (Exception e) {
                // jQuery no está presente, continuar
                logger.debug(TipoMensaje.DEBUG.formatearMensaje("jQuery no detectado"));
            }
            
            // Espera adicional para formularios complejos
            Thread.sleep(1000);
            
            logger.debug(TipoMensaje.EXITO.formatearMensaje("Página cargada completamente"));
            
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                "Timeout esperando carga completa, continuando: " + e.getMessage()));
        }
    }
    
    /**
     * Gestiona ventanas múltiples cerrando las no deseadas
     * SOLUCIÓN: Evita problemas con ventanas de login que no se cierran
     */
    private void gestionarVentanasMultiples(String ventanaOriginal) {
        try {
            Set<String> todasLasVentanas = driver.getWindowHandles();
            
            if (todasLasVentanas.size() > 1) {
                logger.info(TipoMensaje.INFORMATIVO.formatearMensaje(
                    "Detectadas " + todasLasVentanas.size() + " ventanas, cerrando extras"));
                
                for (String ventana : todasLasVentanas) {
                    if (!ventana.equals(ventanaOriginal)) {
                        driver.switchTo().window(ventana);
                        driver.close();
                        logger.debug(TipoMensaje.DEBUG.formatearMensaje("Ventana extra cerrada"));
                    }
                }
                
                // Volver a la ventana original
                driver.switchTo().window(ventanaOriginal);
                logger.debug(TipoMensaje.DEBUG.formatearMensaje("Regresado a ventana original"));
            }
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensajeConExcepcion(
                "Error gestionando ventanas múltiples", e));
        }
    }
    
    // === MÉTODOS DE INTERACCIÓN CON ELEMENTOS ===
    
    /**
     * Busca un elemento con espera explícita
     * MEJORADO: Mejor manejo de timeouts para formularios
     */
    protected WebElement buscarElemento(By localizador) {
        return buscarElemento(localizador, TIMEOUT_ELEMENTO_CORTO);
    }
    
    /**
     * Busca un elemento con timeout personalizado
     */
    protected WebElement buscarElemento(By localizador, int timeoutSegundos) {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "Buscando elemento: " + localizador.toString() + " (timeout: " + timeoutSegundos + "s)"));
            
            WebDriverWait esperaPersonalizada = new WebDriverWait(driver, Duration.ofSeconds(timeoutSegundos));
            WebElement elemento = esperaPersonalizada.until(
                ExpectedConditions.presenceOfElementLocated(localizador));
            
            logger.debug(TipoMensaje.EXITO.formatearMensaje(
                "Elemento encontrado: " + localizador.toString()));
            
            return elemento;
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensajeConExcepcion(
                "Elemento no encontrado: " + localizador.toString(), e));
            throw e;
        }
    }
    
    /**
     * Espera a que un elemento sea visible y clickeable
     * ESPECÍFICO: Para botones de formularios de login
     */
    protected WebElement esperarElementoClickeable(By localizador) {
        return esperarElementoClickeable(localizador, TIMEOUT_ELEMENTO_CORTO);
    }
    
    /**
     * Espera a que un elemento sea clickeable con timeout personalizado
     */
    protected WebElement esperarElementoClickeable(By localizador, int timeoutSegundos) {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "Esperando elemento clickeable: " + localizador.toString() + " (" + timeoutSegundos + "s)"));
            
            WebDriverWait esperaPersonalizada = new WebDriverWait(driver, Duration.ofSeconds(timeoutSegundos));
            WebElement elemento = esperaPersonalizada.until(
                ExpectedConditions.elementToBeClickable(localizador));
            
            // Scroll al elemento si es necesario
            scrollAElemento(elemento);
            
            logger.debug(TipoMensaje.EXITO.formatearMensaje(
                "Elemento clickeable: " + localizador.toString()));
            
            return elemento;
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensajeConExcepcion(
                "Elemento no clickeable: " + localizador.toString(), e));
            throw e;
        }
    }
    
    /**
     * Click seguro en elemento con reintentos
     * SOLUCIÓN: Para botones de login que a veces no responden
     */
    protected void clickSeguro(WebElement elemento) {
        clickSeguro(elemento, 3);
    }
    
    /**
     * Click seguro con número específico de reintentos
     */
    protected void clickSeguro(WebElement elemento, int maxIntentos) {
        for (int intento = 1; intento <= maxIntentos; intento++) {
            try {
                logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                    "Intento de click " + intento + "/" + maxIntentos));
                
                // Asegurar que el elemento esté visible
                if (!elemento.isDisplayed()) {
                    scrollAElemento(elemento);
                    Thread.sleep(500);
                }
                
                // Hacer click
                elemento.click();
                
                logger.debug(TipoMensaje.EXITO.formatearMensaje(
                    "Click exitoso en intento " + intento));
                
                return;
                
            } catch (Exception e) {
                logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                    "Click falló en intento " + intento + "/" + maxIntentos + ": " + e.getMessage()));
                
                if (intento == maxIntentos) {
                    // Último intento: usar JavaScript click
                    try {
                        jsExecutor.executeScript("arguments[0].click();", elemento);
                        logger.info(TipoMensaje.INFORMATIVO.formatearMensaje(
                            "Click realizado con JavaScript como último recurso"));
                        return;
                    } catch (Exception jsException) {
                        throw new RuntimeException("No se pudo hacer click después de " + maxIntentos + " intentos", e);
                    }
                }
                
                // Esperar antes del siguiente intento
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Interrompido durante reintentos de click", ie);
                }
            }
        }
    }
    
    /**
     * Ingresa texto en un campo de forma segura
     * MEJORADO: Para campos de login (usuario/password)
     */
    protected void ingresarTextoSeguro(WebElement elemento, String texto) {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "Ingresando texto en campo (longitud: " + texto.length() + ")"));
            
            // Limpiar el campo primero
            elemento.clear();
            
            // Esperar un momento
            Thread.sleep(300);
            
            // Ingresar el texto
            elemento.sendKeys(texto);
            
            // Verificar que el texto se ingresó correctamente
            String valorActual = elemento.getAttribute("value");
            if (!texto.equals(valorActual)) {
                logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                    "Texto ingresado no coincide. Esperado longitud: " + texto.length() + 
                    ", Actual longitud: " + valorActual.length()));
                
                // Reintentar con JavaScript
                jsExecutor.executeScript("arguments[0].value = arguments[1];", elemento, texto);
            }
            
            logger.debug(TipoMensaje.EXITO.formatearMensaje("Texto ingresado correctamente"));
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensajeConExcepcion(
                "Error al ingresar texto", e));
            throw new RuntimeException("No se pudo ingresar texto en el elemento", e);
        }
    }
    
    /**
     * Hace scroll a un elemento específico
     */
    protected void scrollAElemento(WebElement elemento) {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Haciendo scroll a elemento"));
            jsExecutor.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", elemento);
            Thread.sleep(500); // Esperar que termine el scroll
            
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensajeConExcepcion(
                "Error al hacer scroll", e));
        }
    }
    
    // === MÉTODOS DE VALIDACIÓN ===
    
    /**
     * Verifica si un elemento está presente en la página
     */
    protected boolean estaPresente(By localizador) {
        try {
            driver.findElement(localizador);
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "Elemento presente: " + localizador.toString()));
            return true;
        } catch (Exception e) {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "Elemento no presente: " + localizador.toString()));
            return false;
        }
    }
    
    /**
     * Verifica si un elemento está visible
     */
    protected boolean estaVisible(By localizador) {
        try {
            WebElement elemento = driver.findElement(localizador);
            boolean visible = elemento.isDisplayed();
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "Elemento visible: " + localizador.toString() + " -> " + visible));
            return visible;
        } catch (Exception e) {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "Elemento no visible: " + localizador.toString()));
            return false;
        }
    }
    
    /**
     * Obtiene el texto de un elemento de forma segura
     */
    protected String obtenerTextoSeguro(By localizador) {
        try {
            WebElement elemento = buscarElemento(localizador, TIMEOUT_ELEMENTO_CORTO);
            String texto = elemento.getText().trim();
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "Texto obtenido: '" + texto + "' de " + localizador.toString()));
            return texto;
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                "No se pudo obtener texto del elemento: " + localizador.toString()));
            return "";
        }
    }
    
    // === MÉTODOS DE UTILIDAD ===
    
    /**
     * Obtiene la URL actual de la página
     */
    protected String obtenerUrlActual() {
        String url = driver.getCurrentUrl();
        logger.debug(TipoMensaje.DEBUG.formatearMensaje("URL actual: " + url));
        return url;
    }
    
    /**
     * Obtiene el título de la página
     */
    protected String obtenerTituloPagina() {
        String titulo = driver.getTitle();
        logger.debug(TipoMensaje.DEBUG.formatearMensaje("Título de página: " + titulo));
        return titulo;
    }
    
    /**
     * Refresca la página actual
     */
    protected void refrescarPagina() {
        logger.info(TipoMensaje.PASO_PRUEBA.formatearMensaje("Refrescando página"));
        driver.navigate().refresh();
        esperarCargaCompletaPagina();
    }
    
    /**
     * Captura pantalla de la página actual
     */
    protected String capturarPantalla(String nombreArchivo) {
        String nombreCompleto = this.getClass().getSimpleName() + "_" + nombreArchivo;
        logger.debug(TipoMensaje.DEBUG.formatearMensaje("Capturando pantalla: " + nombreCompleto));
        return GestorCapturaPantalla.capturarPantallaCompleta(driver, nombreCompleto);
    }
    
    // === MÉTODOS ADICIONALES PARA FORMULARIOS ===
    
    /**
     * Espera a que un formulario esté completamente cargado
     * ESPECÍFICO: Para formularios de login/registro
     */
    protected boolean esperarFormularioListo(By selectorFormulario) {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "Esperando formulario listo: " + selectorFormulario.toString()));
            
            // Esperar que el formulario esté presente
            WebElement formulario = espera.until(ExpectedConditions.presenceOfElementLocated(selectorFormulario));
            
            // Esperar que esté visible
            espera.until(ExpectedConditions.visibilityOf(formulario));
            
            // Espera adicional para que se carguen todos los campos
            Thread.sleep(500);
            
            logger.debug(TipoMensaje.EXITO.formatearMensaje("Formulario listo"));
            return true;
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensajeConExcepcion(
                "Error esperando formulario", e));
            return false;
        }
    }
    
    /**
     * Valida que los campos obligatorios de un formulario estén presentes
     */
    protected boolean validarCamposFormulario(By... selectoresCampos) {
        logger.debug(TipoMensaje.VALIDACION.formatearMensaje(
            "Validando " + selectoresCampos.length + " campos del formulario"));
        
        for (By selector : selectoresCampos) {
            if (!estaPresente(selector)) {
                logger.error(TipoMensaje.ERROR.formatearMensaje(
                    "Campo obligatorio faltante: " + selector.toString()));
                return false;
            }
        }
        
        logger.info(TipoMensaje.EXITO.formatearMensaje(
            "Todos los campos obligatorios están presentes"));
        return true;
    }
    
    /**
     * Limpia y llena un campo de formulario
     */
    protected void llenarCampo(By selector, String valor) {
        try {
            WebElement campo = esperarElementoClickeable(selector);
            ingresarTextoSeguro(campo, valor);
            
            // Log sin mostrar datos sensibles completos
            String valorLog = valor.length() > 10 ? valor.substring(0, 3) + "..." : valor;
            logger.debug(TipoMensaje.PASO_PRUEBA.formatearMensaje(
                "Campo llenado: " + selector.toString() + " = '" + valorLog + "'"));
                
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensajeConExcepcion(
                "Error llenando campo " + selector.toString(), e));
            throw e;
        }
    }
    
    /**
     * Envía un formulario usando el botón especificado
     */
    protected void enviarFormulario(By selectorBoton) {
        try {
            logger.info(TipoMensaje.PASO_PRUEBA.formatearMensaje("Enviando formulario"));
            
            WebElement boton = esperarElementoClickeable(selectorBoton);
            clickSeguro(boton);
            
            // Esperar un momento para que se procese el envío
            Thread.sleep(1000);
            
            logger.info(TipoMensaje.EXITO.formatearMensaje("Formulario enviado"));
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensajeConExcepcion(
                "Error enviando formulario", e));
            throw e;
        }
    }
    
    /**
     * Espera a que aparezca un mensaje específico (éxito o error)
     */
    protected boolean esperarMensajeResultado(By selectorMensaje, int timeoutSegundos) {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "Esperando mensaje de resultado: " + selectorMensaje.toString()));
            
            WebDriverWait esperaPersonalizada = new WebDriverWait(driver, Duration.ofSeconds(timeoutSegundos));
            esperaPersonalizada.until(ExpectedConditions.visibilityOfElementLocated(selectorMensaje));
            
            logger.debug(TipoMensaje.EXITO.formatearMensaje("Mensaje de resultado apareció"));
            return true;
            
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                "Timeout esperando mensaje de resultado: " + e.getMessage()));
            return false;
        }
    }
    
    /**
     * Verifica que la página esté lista para interacción
     */
    protected boolean esPaginaListaParaInteraccion() {
        try {
            // Verificar que la página esté visible
            if (!esPaginaVisible()) {
                logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("Página no visible"));
                return false;
            }
            
            // Verificar que no haya overlays o loaders activos
            if (hayElementosBloqueo()) {
                logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("Elementos de bloqueo presentes"));
                return false;
            }
            
            logger.debug(TipoMensaje.EXITO.formatearMensaje("Página lista para interacción"));
            return true;
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensajeConExcepcion(
                "Error verificando si página está lista", e));
            return false;
        }
    }
    
    /**
     * Verifica si hay elementos que bloquean la interacción (loaders, overlays)
     */
    private boolean hayElementosBloqueo() {
        // Selectores comunes de elementos de bloqueo
        String[] selectoresBloqueo = {
            ".loading", ".loader", ".spinner", 
            ".overlay", ".modal-backdrop",
            "[style*='display: block'][style*='position: fixed']"
        };
        
        for (String selector : selectoresBloqueo) {
            try {
                if (estaVisible(By.cssSelector(selector))) {
                    logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                        "Elemento de bloqueo encontrado: " + selector));
                    return true;
                }
            } catch (Exception e) {
                // Continuar con el siguiente selector
            }
        }
        
        return false;
    }
    
    /**
     * Espera a que desaparezcan los elementos de bloqueo
     */
    protected boolean esperarDesaparicionElementosBloqueo(int timeoutSegundos) {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Esperando desaparición de elementos de bloqueo"));
            
            WebDriverWait esperaPersonalizada = new WebDriverWait(driver, Duration.ofSeconds(timeoutSegundos));
            esperaPersonalizada.until(driver -> !hayElementosBloqueo());
            
            logger.debug(TipoMensaje.EXITO.formatearMensaje("Elementos de bloqueo han desaparecido"));
            return true;
            
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                "Timeout esperando desaparición de elementos de bloqueo: " + e.getMessage()));
            return false;
        }
    }
    
    /**
     * Obtiene información de debug de la página actual
     */
    protected String obtenerInfoDebugPagina() {
        StringBuilder info = new StringBuilder();
        info.append("=== DEBUG INFO PÁGINA ===\n");
        info.append("Clase: ").append(this.getClass().getSimpleName()).append("\n");
        info.append("URL Actual: ").append(obtenerUrlActual()).append("\n");
        info.append("Título: ").append(obtenerTituloPagina()).append("\n");
        info.append("Visible: ").append(esPaginaVisible()).append("\n");
        info.append("Lista para interacción: ").append(esPaginaListaParaInteraccion()).append("\n");
        info.append("Timestamp: ").append(System.currentTimeMillis()).append("\n");
        info.append("========================");
        
        logger.debug(TipoMensaje.DEBUG.formatearMensaje("Info debug generada"));
        return info.toString();
    }
    
    /**
     * Método de utilidad para pausas controladas en pruebas
     * NOTA: Usar con moderación, preferir esperas explícitas
     */
    protected void esperarTiempo(int milisegundos) {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "Pausa controlada: " + milisegundos + "ms"));
            Thread.sleep(milisegundos);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                "Pausa interrumpida: " + e.getMessage()));
        }
    }
    
    /**
     * Ejecuta JavaScript en el contexto de la página
     */
    protected Object ejecutarJavaScript(String script, Object... argumentos) {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "Ejecutando JavaScript: " + script.substring(0, Math.min(50, script.length())) + "..."));
            return jsExecutor.executeScript(script, argumentos);
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensajeConExcepcion(
                "Error ejecutando JavaScript", e));
            throw e;
        }
    }
    
    /**
     * Resalta un elemento visualmente (útil para debugging)
     */
    protected void resaltarElemento(WebElement elemento) {
        try {
            String scriptResaltar = 
                "arguments[0].style.border='3px solid red';" +
                "arguments[0].style.backgroundColor='yellow';" +
                "setTimeout(function(){" +
                "arguments[0].style.border='';" +
                "arguments[0].style.backgroundColor='';" +
                "}, 2000);";
            
            jsExecutor.executeScript(scriptResaltar, elemento);
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Elemento resaltado"));
            
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                "No se pudo resaltar elemento: " + e.getMessage()));
        }
    }
}