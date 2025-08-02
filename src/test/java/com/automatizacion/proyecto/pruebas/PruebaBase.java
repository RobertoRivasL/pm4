package com.automatizacion.proyecto.pruebas;

import org.testng.annotations.*;
import org.openqa.selenium.WebDriver;
import com.automatizacion.proyecto.configuracion.ConfiguracionNavegador;
import com.automatizacion.proyecto.configuracion.ConfiguracionGlobal;
import com.automatizacion.proyecto.configuracion.ConfiguracionGlobal.TipoNavegador;
import com.automatizacion.proyecto.utilidades.GestorCapturaPantalla;
import com.automatizacion.proyecto.utilidades.EsperaExplicita;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.qameta.allure.Step;
import org.testng.Assert;
import io.qameta.allure.*;
import com.automatizacion.proyecto.paginas.PaginaRegistro;
import com.automatizacion.proyecto.paginas.PaginaLogin;
import com.automatizacion.proyecto.datos.ModeloDatosPrueba;
import com.automatizacion.proyecto.utilidades.LectorDatosPrueba;

public abstract class PruebaBase {
    
    protected WebDriver driver;
    protected ConfiguracionGlobal config;
    protected EsperaExplicita espera;
    private static final Logger logger = LoggerFactory.getLogger(PruebaBase.class);
    
    protected String urlBase;
    protected String urlLogin;
    protected String urlRegistro;
    protected TipoNavegador tipoNavegador;
    
    @BeforeMethod(alwaysRun = true)
    @Step("Configurar entorno de prueba")
    public void configurarPrueba() {
        try {
            logger.info("=== INICIANDO CONFIGURACIÓN DE PRUEBA ===");
            
            config = ConfiguracionGlobal.getInstance();
            
            urlBase = config.getUrlBase();
            urlLogin = config.getUrlLogin();
            urlRegistro = config.getUrlRegistro();
            tipoNavegador = config.getTipoNavegador();
            
            logger.info("URLs configuradas - Base: {}, Login: {}, Registro: {}", urlBase, urlLogin, urlRegistro);
            
            driver = ConfiguracionNavegador.crearDriver(tipoNavegador);
            espera = new EsperaExplicita(driver);
            
            logger.info("Driver creado exitosamente para: {}", tipoNavegador);
            logger.info("=== CONFIGURACIÓN DE PRUEBA COMPLETADA ===");
            
        } catch (Exception e) {
            logger.error("Error en configuración de prueba: {}", e.getMessage(), e);
            throw new RuntimeException("Fallo en configuración de prueba", e);
        }
    }
    
    @AfterMethod(alwaysRun = true)
    @Step("Limpiar entorno de prueba")
    public void limpiarPrueba() {
        try {
            if (driver != null) {
                logger.info("Cerrando driver...");
                ConfiguracionNavegador.cerrarDriver(driver);
                driver = null;
                logger.info("Driver cerrado exitosamente");
            }
        } catch (Exception e) {
            logger.error("Error al cerrar driver: {}", e.getMessage(), e);
        }
    }
    
    @BeforeClass(alwaysRun = true)
    public void configurarClase() {
        logger.info("=== INICIANDO CLASE DE PRUEBA: {} ===", this.getClass().getSimpleName());
    }
    
    @AfterClass(alwaysRun = true)
    public void limpiarClase() {
        logger.info("=== FINALIZANDO CLASE DE PRUEBA: {} ===", this.getClass().getSimpleName());
    }
    
    @Step("Capturar pantalla: {nombreArchivo}")
    protected void capturarPantalla(String nombreArchivo) {
        try {
            if (driver != null) {
                GestorCapturaPantalla.capturarPantalla(driver, nombreArchivo);
                logger.debug("Captura realizada: {}", nombreArchivo);
            } else {
                logger.warn("No se puede capturar pantalla: driver es null");
            }
        } catch (Exception e) {
            logger.error("Error al capturar pantalla '{}': {}", nombreArchivo, e.getMessage());
        }
    }
    
    protected void capturarPantalla() {
        capturarPantalla("test_screenshot");
    }
    
    @Step("Paso de prueba: {mensaje}")
    protected void logPasoPrueba(String mensaje) {
        logger.info("🔄 PASO: {}", mensaje);
    }
    
    @Step("Validación: {mensaje}")
    protected void logValidacion(String mensaje) {
        logger.info("✅ VALIDACIÓN: {}", mensaje);
    }
    
    protected void logError(String mensaje, Exception e) {
        logger.error("❌ ERROR: {} - {}", mensaje, e.getMessage(), e);
    }
    
    @Step("Navegar a página de login")
    protected void navegarALogin() {
        try {
            logPasoPrueba("Navegando a página de login");
            driver.get(urlLogin);
            Thread.sleep(2000);
            logValidacion("Navegación a login completada");
        } catch (Exception e) {
            logError("Error al navegar a login", e);
            capturarPantalla("error_navegacion_login");
            throw new RuntimeException("No se pudo navegar a login", e);
        }
    }
    
    @Step("Navegar a página de registro")
    protected void navegarARegistro() {
        try {
            logPasoPrueba("Navegando a página de registro");
            driver.get(urlRegistro);
            Thread.sleep(2000);
            logValidacion("Navegación a registro completada");
        } catch (Exception e) {
            logError("Error al navegar a registro", e);
            capturarPantalla("error_navegacion_registro");
            throw new RuntimeException("No se pudo navegar a registro", e);
        }
    }
    
    @Step("Navegar a página principal")
    protected void navegarAHome() {
        try {
            logPasoPrueba("Navegando a página principal");
            driver.get(urlBase);
            Thread.sleep(2000);
            logValidacion("Navegación a home completada");
        } catch (Exception e) {
            logError("Error al navegar a home", e);
            capturarPantalla("error_navegacion_home");
            throw new RuntimeException("No se pudo navegar a home", e);
        }
    }
    
    protected void esperar(long milisegundos) {
        try {
            Thread.sleep(milisegundos);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Espera interrumpida: {}", e.getMessage());
        }
    }
    
    protected String obtenerInfoNavegador() {
        if (driver != null) {
            return String.format("Navegador: %s | URL: %s | Título: %s", 
                tipoNavegador, 
                driver.getCurrentUrl(), 
                driver.getTitle());
        }
        return "Driver no disponible";
    }
    
    protected boolean esDriverDisponible() {
        try {
            return driver != null && driver.getCurrentUrl() != null;
        } catch (Exception e) {
            logger.warn("Driver no disponible: {}", e.getMessage());
            return false;
        }
    }
    
    protected void iniciarPrueba(String nombrePrueba, String descripcion) {
        logger.info("🚀 INICIANDO PRUEBA: {}", nombrePrueba);
        logger.info("📝 DESCRIPCIÓN: {}", descripcion);
        logger.info("🌐 NAVEGADOR: {}", tipoNavegador);
        logger.info("📍 URL BASE: {}", urlBase);
    }
    
    protected void finalizarPrueba(String nombrePrueba, boolean exitosa) {
        String estado = exitosa ? "✅ EXITOSA" : "❌ FALLIDA";
        logger.info("🏁 FINALIZANDO PRUEBA: {} - {}", nombrePrueba, estado);
        
        if (!exitosa) {
            capturarPantalla("prueba_fallida_" + nombrePrueba.replaceAll("\\s+", "_"));
        }
    }
}