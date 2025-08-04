package com.automatizacion.proyecto.pruebas;

import com.automatizacion.proyecto.base.BaseTest;
import com.automatizacion.proyecto.enums.TipoMensaje;
import org.testng.annotations.Test;

/**
 * Clase de pruebas simples para inspección de elementos.
 * Utilizada para validaciones básicas de la aplicación.
 * 
 * @author Roberto Rivas Lopez
 */
public class InspectorSimple extends BaseTest {
    
    @Test(description = "Inspección simple de elementos web")
    public void inspectorElementosSimple() {
        logPasoPrueba("Ejecutando inspección simple de elementos");
        
        // Verificar que el driver está funcionando
        String titulo = driver.getTitle();
        logValidacion("Título de página: " + titulo);
        
        // Verificar que la URL es correcta
        String urlActual = driver.getCurrentUrl();
        logValidacion("URL actual: " + urlActual);
        
        capturarPantalla("inspeccion_simple");
        logger.info(TipoMensaje.EXITO.formatearMensaje("Inspección simple completada"));
    }
    
    @Test(description = "Captura de pantalla de prueba")
    public void capturarPantallaPrueba() {
        logPasoPrueba("Realizando captura de pantalla de prueba");
        capturarPantalla("captura_prueba");
        logger.info(TipoMensaje.EXITO.formatearMensaje("Captura de pantalla realizada"));
    }
}