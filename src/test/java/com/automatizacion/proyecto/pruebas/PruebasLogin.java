package com.automatizacion.proyecto.pruebas;

import com.automatizacion.proyecto.base.BaseTest;
import com.automatizacion.proyecto.datos.ModeloDatosPrueba;
import com.automatizacion.proyecto.datos.ProveedorDatos;
import com.automatizacion.proyecto.enums.TipoMensaje;
import com.automatizacion.proyecto.paginas.PaginaLogin;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Epic("Gestión de Usuarios")
@Feature("Inicio de Sesión")
public class PruebasLogin extends BaseTest {
    
    private PaginaLogin paginaLogin;
    
    @Override
    protected void navegarAUrlBase() {
        try {
            String urlLogin = configuracion.obtenerUrlLogin();
            driver.get(urlLogin);
            logger.info(TipoMensaje.INFORMATIVO.formatearMensaje("Navegando a: " + urlLogin));
            Thread.sleep(2000);
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error navegando a URL login: " + e.getMessage()));
            throw new RuntimeException("No se pudo navegar a URL login", e);
        }
    }
    
    @BeforeMethod(alwaysRun = true)
    public void configuracionEspecificaLogin() {
        paginaLogin = new PaginaLogin(obtenerDriver());
        
        logPasoPrueba("Verificando que la página de login está visible");
        boolean paginaVisible = paginaLogin.esPaginaVisible();
        Assert.assertTrue(paginaVisible, "La página de login debería estar visible");
        
        if (paginaVisible) {
            logValidacion("Página de login cargada correctamente");
            paginaLogin.validarElementosPagina();
        }
    }
    
    @Test(priority = 1,
          description = "Verificar login exitoso con credenciales válidas",
          groups = {"smoke", "login", "positivo"})
    @Story("Login Exitoso")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verifica que un usuario puede iniciar sesión con credenciales válidas")
    public void testLoginExitoso() {
        
        logPasoPrueba("Preparando credenciales válidas para login exitoso");
        
        ModeloDatosPrueba datos = ModeloDatosPrueba.builder()
                .casoPrueba("LOGIN_001")
                .descripcion("Login exitoso con credenciales válidas")
                .email("practice")
                .password("SuperSecretPassword!")
                .esValido(true)
                .resultadoEsperado("Usuario autenticado exitosamente")
                .build();
        
        logPasoPrueba("Ejecutando proceso de login con datos: " + datos.getCasoPrueba());
        
        boolean loginExitoso = paginaLogin.iniciarSesion(datos);
        
        logValidacion("Verificando resultado del login");
        Assert.assertTrue(loginExitoso, "El login debería ser exitoso con credenciales válidas");
        
        capturarPantalla("login_exitoso");
        logger.info(TipoMensaje.EXITO.formatearMensaje("Test de login exitoso completado"));
    }
    
    @Test(priority = 2,
          description = "Verificar validación con credenciales inválidas",
          groups = {"regression", "login", "negativo", "validacion"})
    @Story("Validación de Credenciales")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifica que el sistema rechaza credenciales inválidas")
    public void testValidacionCredencialesInvalidas() {
        
        logPasoPrueba("Preparando credenciales inválidas");
        
        ModeloDatosPrueba datos = ModeloDatosPrueba.builder()
                .casoPrueba("LOGIN_002")
                .descripcion("Validación de credenciales inválidas")
                .email("usuario.inexistente@test.com")
                .password("PasswordIncorrecto!")
                .esValido(false)
                .mensajeError("Invalid credentials")
                .build();
        
        logPasoPrueba("Ejecutando login con credenciales inválidas");
        
        boolean loginFallido = paginaLogin.iniciarSesion(datos);
        
        logValidacion("Verificando que el login falló como esperado");
        Assert.assertTrue(loginFallido, "El login debería fallar con credenciales inválidas");
        
        String mensajeError = paginaLogin.obtenerMensajeError();
        logValidacion("Mensaje de error obtenido: " + mensajeError);
        
        capturarPantalla("validacion_credenciales_invalidas");
        logger.info(TipoMensaje.EXITO.formatearMensaje("Test de validación de credenciales inválidas completado"));
    }
    
    @Test(priority = 3,
          dataProvider = "datosLoginValidos", 
          dataProviderClass = ProveedorDatos.class,
          description = "Verificar login con múltiples credenciales válidas",
          groups = {"regression", "login", "positivo", "datadriven"})
    @Story("Login con Datos Múltiples")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifica login con diferentes conjuntos de credenciales válidas")
    public void testLoginMultiplesCredencialesValidas(ModeloDatosPrueba datos) {
        
        logPasoPrueba("Ejecutando login con datos: " + datos.getCasoPrueba());
        
        boolean loginExitoso = paginaLogin.iniciarSesion(datos);
        
        logValidacion("Verificando resultado del login para: " + datos.getCasoPrueba());
        Assert.assertTrue(loginExitoso, "El login debería ser exitoso para: " + datos.getCasoPrueba());
        
        capturarPantalla("login_multiple_" + datos.getCasoPrueba());
    }
    
    @Test(priority = 4,
          dataProvider = "datosLoginInvalidos", 
          dataProviderClass = ProveedorDatos.class,
          description = "Verificar validación con múltiples credenciales inválidas",
          groups = {"regression", "login", "negativo", "datadriven"})
    @Story("Validación con Datos Múltiples")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifica validación con diferentes conjuntos de credenciales inválidas")
    public void testValidacionMultiplesCredencialesInvalidas(ModeloDatosPrueba datos) {
        
        logPasoPrueba("Ejecutando validación con datos: " + datos.getCasoPrueba());
        
        boolean loginFallido = paginaLogin.iniciarSesion(datos);
        
        logValidacion("Verificando que falló como esperado para: " + datos.getCasoPrueba());
        Assert.assertTrue(loginFallido, "El login debería fallar para: " + datos.getCasoPrueba());
        
        capturarPantalla("validacion_multiple_" + datos.getCasoPrueba());
    }
}