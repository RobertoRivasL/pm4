package com.automatizacion.proyecto.pruebas;

import com.automatizacion.proyecto.base.BaseTest;
import com.automatizacion.proyecto.paginas.PaginaLogin;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Pruebas de funcionalidad de Login para practice.expandtesting.com
 * @author Roberto Rivas Lopez
 */
public class PruebasLogin extends BaseTest {
    
    private PaginaLogin paginaLogin;
    
    @BeforeMethod
    public void configurarPagina() {
        String urlLogin = configuracion.obtenerUrlBase() + "login";
        obtenerDriver().get(urlLogin);
        paginaLogin = new PaginaLogin(obtenerDriver(), obtenerEsperaExplicita());
        
        // Verificar que la página cargó correctamente
        Assert.assertTrue(paginaLogin.esPaginaVisible(), 
            "La página de login no cargó correctamente");
    }
    
    @Test(description = "Verificar login exitoso con credenciales válidas")
    public void testLoginExitoso() {
        // Usar credenciales válidas de practice.expandtesting.com
        paginaLogin.realizarLogin("practice", "SuperSecretPassword!");
        
        // Verificar que se redirigió a página segura
        String urlActual = obtenerDriver().getCurrentUrl();
        Assert.assertTrue(urlActual.contains("secure"), 
            "No se redirigió a la página segura después del login exitoso");
    }
    
    @Test(description = "Verificar mensaje de error con credenciales inválidas")
    public void testLoginCredencialesInvalidas() {
        paginaLogin.realizarLogin("usuario_invalido", "password_invalido");
        
        String mensajeError = paginaLogin.obtenerMensajeError();
        Assert.assertFalse(mensajeError.isEmpty(), 
            "Debería mostrar mensaje de error con credenciales inválidas");
        Assert.assertTrue(mensajeError.contains("invalid") || mensajeError.contains("incorrect"),
            "El mensaje de error debería indicar credenciales inválidas");
    }
    
    @Test(description = "Verificar campos obligatorios vacíos")
    public void testCamposVacios() {
        // Intentar login sin credenciales
        paginaLogin.realizarLogin("", "");
        
        // Verificar que permanece en la página de login
        Assert.assertTrue(paginaLogin.esPaginaVisible(), 
            "Debería permanecer en la página de login con campos vacíos");
    }
    
    @Test(description = "Verificar login solo con usuario")
    public void testSoloUsuario() {
        paginaLogin.realizarLogin("practice", "");
        
        // Verificar que permanece en login o muestra error
        String urlActual = obtenerDriver().getCurrentUrl();
        Assert.assertTrue(urlActual.contains("login"), 
            "Debería permanecer en página de login");
    }
    
    @Test(description = "Verificar login solo con password")
    public void testSoloPassword() {
        paginaLogin.realizarLogin("", "SuperSecretPassword!");
        
        // Verificar que permanece en login
        String urlActual = obtenerDriver().getCurrentUrl();
        Assert.assertTrue(urlActual.contains("login"), 
            "Debería permanecer en página de login");
    }
    
    @Test(description = "Verificar elementos de la interfaz")
    public void testElementosInterfaz() {
        Assert.assertTrue(paginaLogin.validarElementosPagina(), 
            "Todos los elementos de la página deberían estar presentes");
        
        String titulo = paginaLogin.obtenerTitulo();
        Assert.assertTrue(titulo.contains("Login"), 
            "El título debería contener 'Login'");
    }
}
