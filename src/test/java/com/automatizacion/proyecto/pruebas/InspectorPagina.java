package com.automatizacion.proyecto.pruebas;

import com.automatizacion.proyecto.base.BaseTest;
import com.automatizacion.proyecto.enums.TipoMensaje;
import com.automatizacion.proyecto.paginas.PaginaRegistro;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.List;

/**
 * Clase para inspección avanzada de páginas y elementos.
 * Realiza validaciones detalladas de la estructura de las páginas.
 * 
 * @author Roberto Rivas Lopez
 */
public class InspectorPagina extends BaseTest {
    
    @Test(description = "Inspeccionar elementos de la página de registro")
    public void inspeccionarPaginaRegistro() {
        logPasoPrueba("Iniciando inspección de página de registro");
        
        PaginaRegistro paginaRegistro = new PaginaRegistro(obtenerDriver());
        
        // Validar que la página está visible
        Assert.assertTrue(paginaRegistro.esPaginaVisible(), 
                         "La página de registro debe estar visible");
        
        // Inspeccionar elementos del formulario
        inspeccionarElementosFormulario();
        
        // Validar estructura de la página
        paginaRegistro.validarElementosPagina();
        
        capturarPantalla("inspeccion_registro");
        logger.info(TipoMensaje.EXITO.formatearMensaje("Inspección de página de registro completada"));
    }
    
    @Test(description = "Contar elementos en la página")
    public void contarElementosPagina() {
        logPasoPrueba("Contando elementos en la página");
        
        // Contar inputs
        List<WebElement> inputs = driver.findElements(By.tagName("input"));
        logValidacion("Número de inputs encontrados: " + inputs.size());
        
        // Contar botones
        List<WebElement> botones = driver.findElements(By.tagName("button"));
        logValidacion("Número de botones encontrados: " + botones.size());
        
        // Contar links
        List<WebElement> links = driver.findElements(By.tagName("a"));
        logValidacion("Número de links encontrados: " + links.size());
        
        capturarPantalla("conteo_elementos");
        logger.info(TipoMensaje.EXITO.formatearMensaje("Conteo de elementos completado"));
    }
    
    private void inspeccionarElementosFormulario() {
        logPasoPrueba("Inspeccionando elementos del formulario");
        
        try {
            // Verificar campo username
            WebElement campoUsername = driver.findElement(By.id("username"));
            Assert.assertTrue(campoUsername.isDisplayed(), "Campo username debe estar visible");
            logValidacion("Campo username encontrado y visible");
            
            // Verificar campo password
            WebElement campoPassword = driver.findElement(By.id("password"));
            Assert.assertTrue(campoPassword.isDisplayed(), "Campo password debe estar visible");
            logValidacion("Campo password encontrado y visible");
            
            // Verificar botón register
            List<WebElement> botonesRegister = driver.findElements(By.xpath("//button[text()='Register']"));
            Assert.assertFalse(botonesRegister.isEmpty(), "Debe existir botón Register");
            logValidacion("Botón Register encontrado");
            
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("Algunos elementos no fueron encontrados: " + e.getMessage()));
        }
    }
}