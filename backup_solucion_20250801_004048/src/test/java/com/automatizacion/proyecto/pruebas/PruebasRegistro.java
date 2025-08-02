package com.automatizacion.proyecto.pruebas;

import com.automatizacion.proyecto.base.BaseTest;
import com.automatizacion.proyecto.paginas.PaginaRegistro;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.util.Random;

/**
 * Pruebas de funcionalidad de Registro para practice.expandtesting.com
 * @author Roberto Rivas Lopez
 */
public class PruebasRegistro extends BaseTest {
    
    private PaginaRegistro paginaRegistro;
    private Random random = new Random();
    
    @BeforeMethod
    public void configurarPagina() {
        String urlRegistro = configuracion.obtenerUrlBase() + "register";
        obtenerDriver().get(urlRegistro);
        paginaRegistro = new PaginaRegistro(obtenerDriver(), obtenerEsperaExplicita());
        
        Assert.assertTrue(paginaRegistro.esPaginaVisible(), 
            "La página de registro no cargó correctamente");
    }
    
    @Test(description = "Verificar registro exitoso con datos válidos")
    public void testRegistroExitoso() {
        String usuarioUnico = "usuario" + System.currentTimeMillis();
        String password = "Password123!";
        
        paginaRegistro.realizarRegistro(usuarioUnico, password);
        
        // Verificar mensaje de éxito o redirección
        String urlActual = obtenerDriver().getCurrentUrl();
        String mensajeExito = paginaRegistro.obtenerMensajeExito();
        
        Assert.assertTrue(urlActual.contains("login") || !mensajeExito.isEmpty(),
            "Debería mostrar éxito o redirigir al login");
    }
    
    @Test(description = "Verificar error con contraseñas que no coinciden")
    public void testPasswordsNoCoinciden() {
        String usuario = "test" + random.nextInt(1000);
        
        paginaRegistro.ingresarUsuario(usuario);
        paginaRegistro.ingresarPassword("Password123!");
        paginaRegistro.ingresarConfirmarPassword("Password456!");
        paginaRegistro.hacerClicRegistrar();
        
        // Verificar error
        String mensajeError = paginaRegistro.obtenerMensajeError();
        Assert.assertTrue(!mensajeError.isEmpty() || 
                         obtenerDriver().getCurrentUrl().contains("register"),
            "Debería mostrar error o permanecer en registro");
    }
    
    @Test(description = "Verificar campos obligatorios vacíos")
    public void testCamposVacios() {
        paginaRegistro.realizarRegistro("", "");
        
        Assert.assertTrue(paginaRegistro.esPaginaVisible(),
            "Debería permanecer en página de registro con campos vacíos");
    }
    
    @Test(description = "Verificar elementos de la interfaz")
    public void testElementosInterfaz() {
        Assert.assertTrue(paginaRegistro.validarElementosPagina(),
            "Todos los elementos deberían estar presentes");
        
        String titulo = paginaRegistro.obtenerTitulo();
        Assert.assertTrue(titulo.contains("Register"),
            "El título debería contener 'Register'");
    }
}
