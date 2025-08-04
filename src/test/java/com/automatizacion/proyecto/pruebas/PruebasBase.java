package com.automatizacion.proyecto.pruebas;

import com.automatizacion.proyecto.base.BaseTest;
import com.automatizacion.proyecto.configuracion.ConfiguracionGlobal;
import com.automatizacion.proyecto.enums.TipoMensaje;
import org.openqa.selenium.By;  // ← ESTA IMPORTACIÓN FALTABA
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Clase base de pruebas para funcionalidades generales.
 * Contiene pruebas de configuración y setup básico.
 * 
 * @author Roberto Rivas Lopez
 */
public class PruebasBase extends BaseTest {
    
    @Test(description = "Verificar configuración global", priority = 1)
    public void verificarConfiguracionGlobal() {
        logPasoPrueba("Verificando configuración global del sistema");
        
        ConfiguracionGlobal config = ConfiguracionGlobal.obtenerInstancia();
        
        // Verificar URL base
        String urlBase = config.obtenerUrlBase();
        Assert.assertNotNull(urlBase, "URL base no debe ser null");
        Assert.assertFalse(urlBase.isEmpty(), "URL base no debe estar vacía");
        logValidacion("URL base configurada: " + urlBase);
        
        // Verificar tipo de navegador
        String tipoNavegador = config.obtenerPropiedad(ConfiguracionGlobal.NAVEGADOR_TIPO);
        Assert.assertNotNull(tipoNavegador, "Tipo de navegador no debe ser null");
        logValidacion("Navegador configurado: " + tipoNavegador);
        
        // Verificar timeouts
        int timeoutExplicito = config.obtenerPropiedadInt(ConfiguracionGlobal.TIMEOUT_EXPLICITO, 10);
        Assert.assertTrue(timeoutExplicito > 0, "Timeout explícito debe ser mayor a 0");
        logValidacion("Timeout explícito: " + timeoutExplicito + " segundos");
        
        logger.info(TipoMensaje.EXITO.formatearMensaje("Configuración global verificada correctamente"));
    }
    
    @Test(description = "Verificar inicialización de WebDriver", priority = 2)
    public void verificarWebDriver() {
        logPasoPrueba("Verificando inicialización de WebDriver");
        
        // Verificar que el driver no es null
        Assert.assertNotNull(driver, "WebDriver no debe ser null");
        
        // Verificar que se puede obtener la URL actual
        String urlActual = driver.getCurrentUrl();
        Assert.assertNotNull(urlActual, "URL actual no debe ser null");
        logValidacion("URL actual: " + urlActual);
        
        // Verificar que se puede obtener el título
        String titulo = driver.getTitle();
        Assert.assertNotNull(titulo, "Título de página no debe ser null");
        logValidacion("Título de página: " + titulo);
        
        logger.info(TipoMensaje.EXITO.formatearMensaje("WebDriver verificado correctamente"));
    }
    
    @Test(description = "Verificar navegación básica", priority = 3)
    public void verificarNavegacionBasica() {
        logPasoPrueba("Verificando navegación básica");
        
        // Obtener URL inicial
        String urlInicial = driver.getCurrentUrl();
        logValidacion("URL inicial: " + urlInicial);
        
        // Verificar que podemos refrescar la página
        driver.navigate().refresh();
        esperarSegundos(2);
        
        String urlDespuesRefresh = driver.getCurrentUrl();
        logValidacion("URL después de refresh: " + urlDespuesRefresh);
        
        // Las URLs deberían ser similares (pueden diferir en parámetros)
        Assert.assertTrue(urlDespuesRefresh.contains(ConfiguracionGlobal.obtenerInstancia().obtenerUrlBase()),
                        "La URL debe contener la URL base configurada");
        
        capturarPantalla("navegacion_basica");
        logger.info(TipoMensaje.EXITO.formatearMensaje("Navegación básica verificada correctamente"));
    }
    
    @Test(description = "Verificar captura de pantallas", priority = 4)
    public void verificarCapturaPantalla() {
        logPasoPrueba("Verificando funcionalidad de captura de pantalla");
        
        // Realizar varias capturas de prueba
        String archivo1 = gestorCaptura.capturarPantalla("prueba_captura_1");
        Assert.assertNotNull(archivo1, "Primera captura debe generar archivo");
        logValidacion("Primera captura realizada: " + archivo1);
        
        esperarSegundos(1);
        
        String archivo2 = gestorCaptura.capturarPantalla("prueba_captura_2");
        Assert.assertNotNull(archivo2, "Segunda captura debe generar archivo");
        logValidacion("Segunda captura realizada: " + archivo2);
        
        // Verificar que los archivos son diferentes (por timestamp)
        Assert.assertNotEquals(archivo1, archivo2, "Los archivos de captura deben ser diferentes");
        
        logger.info(TipoMensaje.EXITO.formatearMensaje("Funcionalidad de captura verificada correctamente"));
    }
    
    @Test(description = "Verificar gestión de errores", priority = 5)
    public void verificarGestionErrores() {
        logPasoPrueba("Verificando gestión de errores del framework");
        
        try {
            // Intentar encontrar un elemento que no existe
            driver.findElement(By.id("elemento-que-no-existe"));
            Assert.fail("Debería haber lanzado excepción por elemento no encontrado");
            
        } catch (Exception e) {
            logValidacion("Excepción capturada correctamente: " + e.getClass().getSimpleName());
            
            // Capturar pantalla del error
            capturarPantalla("test_gestion_errores");
            
            logger.info(TipoMensaje.EXITO.formatearMensaje("Gestión de errores verificada correctamente"));
        }
    }
}