package com.automatizacion.proyecto.pruebas;

import com.automatizacion.proyecto.base.BaseTest;
import com.automatizacion.proyecto.paginas.PaginaLogin;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Pruebas de funcionalidad de Login
 * @author Roberto Rivas Lopez
 */
public class PruebasLogin extends BaseTest {
    
    private PaginaLogin paginaLogin;
    
    @BeforeMethod
    public void configurarPagina() {
        obtenerDriver().get(configuracion.obtenerUrlBase());
        paginaLogin = new PaginaLogin(obtenerDriver(), obtenerEsperaExplicita());
    }
    
    @Test(description = "Verificar login exitoso con credenciales válidas")
    public void testLoginExitoso() {
        Assert.assertTrue(paginaLogin.esPaginaVisible(), "La página de login no está visible");
        
        paginaLogin.realizarLogin("Admin", "admin123");
        
        // Verificar redirección exitosa
        Assert.assertTrue(obtenerEsperaExplicita().esperarUrlContiene("dashboard"), 
                         "No se redirigió al dashboard después del login");
    }
    
    @Test(description = "Verificar mensaje de error con credenciales inválidas")
    public void testLoginCredencialesInvalidas() {
        Assert.assertTrue(paginaLogin.esPaginaVisible(), "La página de login no está visible");
        
        paginaLogin.realizarLogin("usuario_invalido", "password_invalido");
        
        String mensajeError = paginaLogin.obtenerMensajeError();
        Assert.assertFalse(mensajeError.isEmpty(), "No se mostró mensaje de error");
    }
    
    @Test(description = "Verificar campos obligatorios vacíos")
    public void testCamposObligatorios() {
        Assert.assertTrue(paginaLogin.esPaginaVisible(), "La página de login no está visible");
        
        paginaLogin.hacerClicLogin();
        
        // Verificar que permanece en la página de login
        Assert.assertTrue(paginaLogin.esPaginaVisible(), "Debería permanecer en la página de login");
    }
}
