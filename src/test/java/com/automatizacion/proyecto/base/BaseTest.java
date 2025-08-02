package com.automatizacion.proyecto.base;

import com.automatizacion.proyecto.configuracion.ConfiguracionGlobal;
import com.automatizacion.proyecto.configuracion.ConfiguracionNavegador;
import com.automatizacion.proyecto.enums.TipoNavegador;
import com.automatizacion.proyecto.utilidades.EsperaExplicita;
import com.automatizacion.proyecto.utilidades.GestorCapturaPantalla;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Clase base para todas las pruebas de automatización
 * Cumple con requerimientos ABP: Estructura común y gestión de recursos
 * 
 * Características principales:
 * - Gestión automática de WebDriver con ThreadLocal para ejecución paralela
 * - Configuración centralizada a través de ConfiguracionGlobal
 * - Utilidades integradas: EsperaExplicita y GestorCapturaPantalla
 * - Métodos de navegación y validación comunes
 * - Logging estructurado para trazabilidad
 * 
 * @author Roberto Rivas Lopez
 */
public abstract class BaseTest {
    protected static final Logger logger = LoggerFactory.getLogger(BaseTest.class);
    
    // ThreadLocal para soporte de ejecución paralela segura
    private static final ThreadLocal<WebDriver> driverLocal = new ThreadLocal<>();
    private static final ThreadLocal<EsperaExplicita> esperaExplicitaLocal = new ThreadLocal<>();
    private static final ThreadLocal<GestorCapturaPantalla> gestorCapturaLocal = new ThreadLocal<>();
    private static final ThreadLocal<String> nombrePruebaActual = new ThreadLocal<>();
    
    // ===== CONFIGURACIÓN DE PRUEBAS =====
    
    @BeforeEach
    public void configurarPrueba(TestInfo testInfo) {
        try {
            // Almacenar nombre de la prueba para uso posterior
            nombrePruebaActual.set(testInfo.getDisplayName());
            
            logger.info("🚀 Iniciando configuración para prueba: {}", testInfo.getDisplayName());
            
            ConfiguracionGlobal config = ConfiguracionGlobal.obtenerInstancia();
            
            // Configurar WebDriver
            TipoNavegador tipoNavegador = TipoNavegador.desdeString(config.obtenerTipoNavegador());
            WebDriver driver = ConfiguracionNavegador.crearDriver(tipoNavegador, config.esNavegadorHeadless());
            driverLocal.set(driver);
            
            // Configurar utilidades
            esperaExplicitaLocal.set(new EsperaExplicita(driver, config.obtenerTimeoutExplicito()));
            gestorCapturaLocal.set(new GestorCapturaPantalla());
            
            logger.info("✅ Configuración de prueba completada exitosamente");
            
        } catch (Exception e) {
            logger.error("❌ Error en configuración de prueba: {}", e.getMessage(), e);
            throw new RuntimeException("Fallo en la configuración inicial de la prueba", e);
        }
    }
    
    @AfterEach
    public void limpiarPrueba(TestInfo testInfo) {
        logger.info("🧹 Iniciando limpieza para prueba: {}", testInfo.getDisplayName());
        cerrarDriver();
        logger.info("✅ Limpieza de prueba completada");
    }
    
    // ===== MÉTODOS DE ACCESO A RECURSOS =====
    
    /**
     * Obtiene el driver del hilo actual
     * @return WebDriver instancia del hilo actual
     * @throws IllegalStateException si el driver no está inicializado
     */
    protected WebDriver obtenerDriver() {
        WebDriver driver = driverLocal.get();
        if (driver == null) {
            throw new IllegalStateException("Driver no inicializado. ¿Se ejecutó @BeforeEach correctamente?");
        }
        return driver;
    }
    
    /**
     * Obtiene la instancia de EsperaExplicita del hilo actual
     * @return EsperaExplicita para manejo de esperas
     * @throws IllegalStateException si no está inicializada
     */
    protected EsperaExplicita obtenerEsperaExplicita() {
        EsperaExplicita espera = esperaExplicitaLocal.get();
        if (espera == null) {
            throw new IllegalStateException("EsperaExplicita no inicializada");
        }
        return espera;
    }
    
    /**
     * Obtiene el gestor de capturas del hilo actual
     * @return GestorCapturaPantalla para manejo de screenshots
     * @throws IllegalStateException si no está inicializado
     */
    protected GestorCapturaPantalla obtenerGestorCaptura() {
        GestorCapturaPantalla gestor = gestorCapturaLocal.get();
        if (gestor == null) {
            throw new IllegalStateException("GestorCapturaPantalla no inicializado");
        }
        return gestor;
    }
    
    // ===== MÉTODOS DE NAVEGACIÓN =====
    
    /**
     * Navega a una URL específica
     * @param url URL de destino
     */
    protected void navegarA(String url) {
        try {
            logger.info("🌐 Navegando a: {}", url);
            obtenerDriver().get(url);
            logger.debug("✅ Navegación exitosa a: {}", url);
        } catch (Exception e) {
            logger.error("❌ Error navegando a {}: {}", url, e.getMessage());
            capturarPantallaError("navegacion_error");
            throw new RuntimeException("Error en navegación a " + url, e);
        }
    }
    
    /**
     * Navega a la URL base configurada en el sistema
     */
    protected void navegarAUrlBase() {
        String urlBase = ConfiguracionGlobal.obtenerInstancia().obtenerUrlBase();
        navegarA(urlBase);
    }
    
    /**
     * Actualiza/refresca la página actual
     */
    protected void refrescarPagina() {
        try {
            logger.info("🔄 Refrescando página actual...");
            obtenerDriver().navigate().refresh();
            logger.debug("✅ Página refrescada exitosamente");
        } catch (Exception e) {
            logger.error("❌ Error refrescando página: {}", e.getMessage());
            throw new RuntimeException("Error al refrescar página", e);
        }
    }
    
    /**
     * Navega hacia atrás en el historial del navegador
     */
    protected void navegarAtras() {
        try {
            logger.info("⬅️ Navegando hacia atrás...");
            obtenerDriver().navigate().back();
            logger.debug("✅ Navegación hacia atrás exitosa");
        } catch (Exception e) {
            logger.error("❌ Error navegando hacia atrás: {}", e.getMessage());
            throw new RuntimeException("Error navegando hacia atrás", e);
        }
    }
    
    /**
     * Navega hacia adelante en el historial del navegador
     */
    protected void navegarAdelante() {
        try {
            logger.info("➡️ Navegando hacia adelante...");
            obtenerDriver().navigate().forward();
            logger.debug("✅ Navegación hacia adelante exitosa");
        } catch (Exception e) {
            logger.error("❌ Error navegando hacia adelante: {}", e.getMessage());
            throw new RuntimeException("Error navegando hacia adelante", e);
        }
    }
    
    // ===== MÉTODOS DE INFORMACIÓN DE PÁGINA =====
    
    /**
     * Obtiene el título de la página actual
     * @return String título de la página
     */
    protected String obtenerTituloPagina() {
        try {
            String titulo = obtenerDriver().getTitle();
            logger.debug("📄 Título de página obtenido: {}", titulo);
            return titulo != null ? titulo : "";
        } catch (Exception e) {
            logger.error("❌ Error obteniendo título de página: {}", e.getMessage());
            return "";
        }
    }
    
    /**
     * Obtiene la URL actual del navegador
     * @return String URL actual
     */
    protected String obtenerUrlActual() {
        try {
            String url = obtenerDriver().getCurrentUrl();
            logger.debug("🔗 URL actual obtenida: {}", url);
            return url != null ? url : "";
        } catch (Exception e) {
            logger.error("❌ Error obteniendo URL actual: {}", e.getMessage());
            return "";
        }
    }
    
    /**
     * Verifica si la URL actual contiene el texto esperado
     * @param textoEsperado texto que debe contener la URL
     * @return boolean true si la URL contiene el texto
     */
    protected boolean urlContiene(String textoEsperado) {
        String urlActual = obtenerUrlActual();
        boolean contiene = urlActual.toLowerCase().contains(textoEsperado.toLowerCase());
        logger.debug("🔍 URL '{}' contiene '{}': {}", urlActual, textoEsperado, contiene);
        return contiene;
    }
    
    /**
     * Verifica si el título de la página contiene el texto esperado
     * @param textoEsperado texto que debe contener el título
     * @return boolean true si el título contiene el texto
     */
    protected boolean tituloContiene(String textoEsperado) {
        String titulo = obtenerTituloPagina();
        boolean contiene = titulo.toLowerCase().contains(textoEsperado.toLowerCase());
        logger.debug("📄 Título '{}' contiene '{}': {}", titulo, textoEsperado, contiene);
        return contiene;
    }
    
    // ===== MÉTODOS DE ESPERA Y SINCRONIZACIÓN =====
    
    /**
     * Espera hasta que la URL cambie de la actual
     * @param timeoutSegundos tiempo máximo de espera
     * @return boolean true si la URL cambió
     */
    protected boolean esperarCambioUrl(int timeoutSegundos) {
        String urlInicial = obtenerUrlActual();
        return esperarCambioUrl(urlInicial, timeoutSegundos);
    }
    
    /**
     * Espera hasta que la URL cambie de una específica
     * @param urlAnterior URL desde la cual se espera el cambio
     * @param timeoutSegundos tiempo máximo de espera
     * @return boolean true si la URL cambió
     */
    protected boolean esperarCambioUrl(String urlAnterior, int timeoutSegundos) {
        try {
            logger.debug("⏳ Esperando cambio de URL desde: {}", urlAnterior);
            WebDriverWait wait = new WebDriverWait(obtenerDriver(), Duration.ofSeconds(timeoutSegundos));
            
            boolean cambio = wait.until(driver -> !driver.getCurrentUrl().equals(urlAnterior));
            
            if (cambio) {
                logger.debug("✅ URL cambió exitosamente a: {}", obtenerUrlActual());
            }
            
            return cambio;
            
        } catch (Exception e) {
            logger.warn("⚠️ Timeout esperando cambio de URL desde: {}", urlAnterior);
            return false;
        }
    }
    
    /**
     * Espera hasta que el título de la página cambie
     * @param tituloAnterior título desde el cual se espera el cambio
     * @param timeoutSegundos tiempo máximo de espera
     * @return boolean true si el título cambió
     */
    protected boolean esperarCambioTitulo(String tituloAnterior, int timeoutSegundos) {
        try {
            logger.debug("⏳ Esperando cambio de título desde: {}", tituloAnterior);
            WebDriverWait wait = new WebDriverWait(obtenerDriver(), Duration.ofSeconds(timeoutSegundos));
            
            boolean cambio = wait.until(driver -> !driver.getTitle().equals(tituloAnterior));
            
            if (cambio) {
                logger.debug("✅ Título cambió exitosamente a: {}", obtenerTituloPagina());
            }
            
            return cambio;
            
        } catch (Exception e) {
            logger.warn("⚠️ Timeout esperando cambio de título desde: {}", tituloAnterior);
            return false;
        }
    }
    
    // ===== MÉTODOS DE CAPTURA DE PANTALLA =====
    
    /**
     * Captura pantalla de la prueba actual
     * @return String ruta del archivo de captura o null si falla
     */
    protected String capturarPantallaPrueba() {
        return capturarPantallaConDescripcion("prueba");
    }
    
    /**
     * Captura pantalla con descripción personalizada
     * @param descripcion descripción para el nombre del archivo
     * @return String ruta del archivo de captura o null si falla
     */
    protected String capturarPantallaConDescripcion(String descripcion) {
        try {
            GestorCapturaPantalla gestor = obtenerGestorCaptura();
            WebDriver driver = obtenerDriver();
            String nombrePrueba = nombrePruebaActual.get();
            
            String nombreArchivo = String.format("%s_%s", nombrePrueba, descripcion);
            String rutaCaptura = gestor.capturarPantalla(driver, nombreArchivo);
            
            logger.info("📸 Captura guardada: {}", rutaCaptura);
            return rutaCaptura;
            
        } catch (Exception e) {
            logger.error("❌ Error capturando pantalla: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * Captura pantalla cuando ocurre un error
     * @param contextoError contexto del error para el nombre del archivo
     */
    protected void capturarPantallaError(String contextoError) {
        try {
            GestorCapturaPantalla gestor = obtenerGestorCaptura();
            WebDriver driver = obtenerDriver();
            String nombrePrueba = nombrePruebaActual.get();
            
            if (gestor != null && driver != null) {
                String rutaCaptura = gestor.capturarPantallaFallo(driver, 
                    String.format("%s_%s", nombrePrueba, contextoError));
                logger.error("📸 Captura de error guardada: {}", rutaCaptura);
            }
        } catch (Exception e) {
            logger.error("💥 Error capturando pantalla de fallo: {}", e.getMessage());
        }
    }
    
    // ===== MÉTODOS DE GESTIÓN INTERNA =====
    
    /**
     * Cierra el driver y limpia los recursos del hilo actual
     */
    private void cerrarDriver() {
        try {
            WebDriver driver = driverLocal.get();
            if (driver != null) {
                driver.quit();
                logger.debug("🔄 Driver cerrado correctamente");
            }
        } catch (Exception e) {
            logger.warn("⚠️ Error cerrando driver: {}", e.getMessage());
        } finally {
            // Limpiar ThreadLocal para evitar memory leaks
            driverLocal.remove();
            esperaExplicitaLocal.remove();
            gestorCapturaLocal.remove();
            nombrePruebaActual.remove();
            logger.debug("🧹 ThreadLocal limpiado");
        }
    }
    
    /**
     * Obtiene información del navegador actual
     * @return String información del navegador y versión
     */
    protected String obtenerInformacionNavegador() {
        try {
            WebDriver driver = obtenerDriver();
            return String.format("Navegador: %s", driver.getClass().getSimpleName());
        } catch (Exception e) {
            logger.debug("No se pudo obtener información del navegador: {}", e.getMessage());
            return "Información no disponible";
        }
    }
    
    /**
     * Pausa la ejecución por un tiempo determinado
     * NOTA: Usar solo cuando sea absolutamente necesario, 
     * preferir esperas explícitas
     * @param milisegundos tiempo de pausa en milisegundos
     */
    protected void pausar(long milisegundos) {
        try {
            logger.debug("⏸️ Pausando ejecución por {} ms", milisegundos);
            Thread.sleep(milisegundos);
        } catch (InterruptedException e) {
            logger.warn("Pausa interrumpida: {}", e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}