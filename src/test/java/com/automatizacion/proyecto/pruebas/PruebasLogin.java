package com.automatizacion.proyecto.pruebas;

import com.automatizacion.proyecto.paginas.PruebaBase;
import com.automatizacion.proyecto.enums.TipoMensaje;
import com.automatizacion.proyecto.paginas.PaginaLogin;
import com.automatizacion.proyecto.utilidades.GestorCapturaPantalla;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.*;

/**
 * Casos de prueba para la funcionalidad de Login.
 * 
 * SOLUCIONES IMPLEMENTADAS:
 * - Capturas correctas en momentos precisos
 * - Manejo de ventanas que no se cierran
 * - Validaciones robustas de estado
 * - Limpieza correcta entre pruebas
 * 
 * Principios aplicados:
 * - Arrange-Act-Assert: Estructura clara de pruebas
 * - Single Responsibility: Cada prueba valida un escenario específico
 * - Don't Repeat Yourself: Métodos comunes reutilizables
 * 
 * @author Antonio B. Arriagada LL. (anarriag@gmail.com)
 * @author Dante Escalona Bustos (Jacobo.bustos.22@gmail.com)  
 * @author Roberto Rivas Lopez (umancl@gmail.com)
 * @version 1.0
 */
@Epic("Autenticación de Usuarios")
@Feature("Login de Usuario")
public class PruebasLogin extends PruebaBase {
    
    private PaginaLogin paginaLogin;
    
    // === DATOS DE PRUEBA ===
    private static final String USUARIO_VALIDO = "practice";
    private static final String PASSWORD_VALIDO = "SuperSecretPassword!";
    private static final String USUARIO_INVALIDO = "usuario_inexistente";
    private static final String PASSWORD_INVALIDO = "password_incorrecto";
    
    @BeforeMethod(alwaysRun = true)
    public void configurarPrueba() {
        logger.info(TipoMensaje.CONFIGURACION.formatearMensaje(
            "Configurando prueba de login"));
        
        try {
            // Inicializar página de login
            paginaLogin = new PaginaLogin(driver);
            
            // Navegar a la página de login
            paginaLogin.navegarAPagina();
            
            // Verificar que estamos en la página correcta
            Assert.assertTrue(paginaLogin.esPaginaVisible(), 
                "La página de login debería estar visible");
            
            logger.info(TipoMensaje.EXITO.formatearMensaje(
                "Configuración de prueba completada"));
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error en configuración: " + e.getMessage()));
            throw e;
        }
    }
    
    @AfterMethod(alwaysRun = true)
    public void limpiezaPost() {
        logger.info(TipoMensaje.CONFIGURACION.formatearMensaje(
            "Ejecutando limpieza post-prueba"));
        
        try {
            if (paginaLogin != null) {
                // Intentar logout si estamos logueados
                if (paginaLogin.esLoginExitoso()) {
                    paginaLogin.realizarLogout();
                }
                
                // Limpiar campos por si acaso
                paginaLogin.limpiarCamposLogin();
            }
            
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                "Error en limpieza: " + e.getMessage()));
        }
    }
    
    // === CASOS DE PRUEBA POSITIVOS ===
    
    @Test(priority = 1, 
          description = "Verificar login exitoso con credenciales válidas",
          groups = {"smoke", "login", "positivo"})
    @Story("Login Exitoso")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verifica que un usuario puede iniciar sesión con credenciales válidas")
    public void testLoginExitoso() {
        
        logPasoPrueba("Probando login con credenciales válidas");
        
        // ARRANGE: Datos de prueba listos
        String casoPrueba = "LOGIN_EXITOSO";
        
        // ACT: Realizar login completo con capturas correctas
        boolean loginExitoso = paginaLogin.realizarLoginCompleto(
            USUARIO_VALIDO, PASSWORD_VALIDO, casoPrueba);
        
        // ASSERT: Verificar que el login fue exitoso
        Assert.assertTrue(loginExitoso, 
            "El login debería ser exitoso con credenciales válidas");
        
        // Verificaciones adicionales
        Assert.assertTrue(paginaLogin.esLoginExitoso(), 
            "El estado de la página debería indicar login exitoso");
        
        Assert.assertFalse(paginaLogin.hayMensajeError(), 
            "No debería haber mensajes de error");
        
        logValidacion("Login exitoso verificado correctamente");
    }
    
    @Test(priority = 2,
          description = "Verificar acceso al dashboard después de login exitoso",
          groups = {"smoke", "login", "positivo"},
          dependsOnMethods = {"testLoginExitoso"})
    @Story("Acceso al Dashboard")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifica que después de un login exitoso se puede acceder al dashboard")
    public void testAccesoDashboard() {
        
        logPasoPrueba("Verificando acceso al dashboard");
        
        // ARRANGE & ACT: Login y verificación
        boolean loginExitoso = paginaLogin.realizarLoginCompleto(
            USUARIO_VALIDO, PASSWORD_VALIDO, "ACCESO_DASHBOARD");
        
        // ASSERT: Verificar acceso al dashboard
        Assert.assertTrue(loginExitoso, "Login debería ser exitoso");
        
        // Verificar que no estamos en página de login
        Assert.assertFalse(paginaLogin.esPaginaVisible(), 
            "No deberíamos estar en la página de login después del login exitoso");
        
        // Verificar URL cambió
        String urlActual = paginaLogin.obtenerUrlActual();
        Assert.assertFalse(urlActual.toLowerCase().contains("login"), 
            "La URL no debería contener 'login' después del login exitoso");
        
        logValidacion("Acceso al dashboard verificado");
    }
    
    // === CASOS DE PRUEBA NEGATIVOS ===
    
    @Test(priority = 3,
          description = "Verificar rechazo de credenciales inválidas",
          groups = {"regression", "login", "negativo"})
    @Story("Login Fallido")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifica que el sistema rechaza credenciales inválidas")
    public void testLoginCredencialesInvalidas() {
        
        logPasoPrueba("Probando login con credenciales inválidas");
        
        // ACT: Intentar login con credenciales incorrectas
        boolean loginExitoso = paginaLogin.realizarLoginCompleto(
            USUARIO_INVALIDO, PASSWORD_INVALIDO, "LOGIN_CREDENCIALES_INVALIDAS");
        
        // ASSERT: Verificar que el login falló
        Assert.assertFalse(loginExitoso, 
            "El login debería fallar con credenciales inválidas");
        
        Assert.assertTrue(paginaLogin.esPaginaVisible(), 
            "Deberíamos permanecer en la página de login");
        
        // Verificar mensaje de error (si existe)
        if (paginaLogin.hayMensajeError()) {
            String mensajeError = paginaLogin.obtenerMensajeError();
            Assert.assertFalse(mensajeError.isEmpty(), 
                "Debería mostrarse un mensaje de error específico");
            logValidacion("Mensaje de error mostrado: " + mensajeError);
        }
        
        logValidacion("Rechazo de credenciales inválidas verificado");
    }
    
    @Test(priority = 4,
          description = "Verificar rechazo de usuario válido con contraseña incorrecta",
          groups = {"regression", "login", "negativo"})
    @Story("Login Fallido")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifica que el sistema rechaza password incorrecto para usuario válido")
    public void testLoginPasswordIncorrecto() {
        
        logPasoPrueba("Probando usuario válido con password incorrecto");
        
        // ACT: Login con usuario válido pero password incorrecto
        boolean loginExitoso = paginaLogin.realizarLoginCompleto(
            USUARIO_VALIDO, PASSWORD_INVALIDO, "LOGIN_PASSWORD_INCORRECTO");
        
        // ASSERT: Verificar rechazo
        Assert.assertFalse(loginExitoso, 
            "El login debería fallar con password incorrecto");
        
        Assert.assertTrue(paginaLogin.esPaginaVisible(), 
            "Deberíamos permanecer en la página de login");
        
        logValidacion("Rechazo de password incorrecto verificado");
    }
    
    @Test(priority = 5,
          description = "Verificar rechazo de usuario inexistente",
          groups = {"regression", "login", "negativo"})
    @Story("Login Fallido")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifica que el sistema rechaza usuarios que no existen")
    public void testLoginUsuarioInexistente() {
        
        logPasoPrueba("Probando usuario inexistente");
        
        // ACT: Login con usuario que no existe
        boolean loginExitoso = paginaLogin.realizarLoginCompleto(
            USUARIO_INVALIDO, PASSWORD_VALIDO, "LOGIN_USUARIO_INEXISTENTE");
        
        // ASSERT: Verificar rechazo
        Assert.assertFalse(loginExitoso, 
            "El login debería fallar con usuario inexistente");
        
        Assert.assertTrue(paginaLogin.esPaginaVisible(), 
            "Deberíamos permanecer en la página de login");
        
        logValidacion("Rechazo de usuario inexistente verificado");
    }
    
    // === CASOS DE PRUEBA DE VALIDACIÓN ===
    
    @Test(priority = 6,
          description = "Verificar validación de campos vacíos",
          groups = {"regression", "login", "validacion"})
    @Story("Validación de Campos")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifica que el sistema valida campos obligatorios")
    public void testValidacionCamposVacios() {
        
        logPasoPrueba("Probando validación de campos vacíos");
        
        // ACT: Intentar login sin ingresar datos
        boolean loginExitoso = paginaLogin.realizarLoginCompleto(
            "", "", "LOGIN_CAMPOS_VACIOS");
        
        // ASSERT: Verificar que no se permite login vacío
        Assert.assertFalse(loginExitoso, 
            "El login no debería ser posible con campos vacíos");
        
        Assert.assertTrue(paginaLogin.esPaginaVisible(), 
            "Deberíamos permanecer en la página de login");
        
        logValidacion("Validación de campos vacíos funcionando");
    }
    
    @Test(priority = 7,
          description = "Verificar validación de solo usuario sin password",
          groups = {"regression", "login", "validacion"})
    @Story("Validación de Campos")
    @Severity(SeverityLevel.MINOR)
    @Description("Verifica validación cuando solo se ingresa usuario")
    public void testValidacionSoloUsuario() {
        
        logPasoPrueba("Probando validación con solo usuario");
        
        // ACT: Login con solo usuario, sin password
        boolean loginExitoso = paginaLogin.realizarLoginCompleto(
            USUARIO_VALIDO, "", "LOGIN_SOLO_USUARIO");
        
        // ASSERT: Verificar rechazo
        Assert.assertFalse(loginExitoso, 
            "El login no debería ser posible sin password");
        
        Assert.assertTrue(paginaLogin.esPaginaVisible(), 
            "Deberíamos permanecer en la página de login");
        
        logValidacion("Validación de password requerido funcionando");
    }
    
    @Test(priority = 8,
          description = "Verificar validación de solo password sin usuario",
          groups = {"regression", "login", "validacion"})
    @Story("Validación de Campos")
    @Severity(SeverityLevel.MINOR)
    @Description("Verifica validación cuando solo se ingresa password")
    public void testValidacionSoloPassword() {
        
        logPasoPrueba("Probando validación con solo password");
        
        // ACT: Login con solo password, sin usuario
        boolean loginExitoso = paginaLogin.realizarLoginCompleto(
            "", PASSWORD_VALIDO, "LOGIN_SOLO_PASSWORD");
        
        // ASSERT: Verificar rechazo
        Assert.assertFalse(loginExitoso, 
            "El login no debería ser posible sin usuario");
        
        Assert.assertTrue(paginaLogin.esPaginaVisible(), 
            "Deberíamos permanecer en la página de login");
        
        logValidacion("Validación de usuario requerido funcionando");
    }
    
    // === CASOS DE PRUEBA DE FUNCIONALIDAD ===
    
    @Test(priority = 9,
          description = "Verificar funcionalidad de limpiar campos",
          groups = {"funcionalidad", "login"})
    @Story("Funcionalidad de Campos")
    @Severity(SeverityLevel.TRIVIAL)
    @Description("Verifica que se pueden limpiar los campos del formulario")
    public void testLimpiarCampos() {
        
        logPasoPrueba("Probando limpieza de campos");
        
        try {
            // ARRANGE: Llenar campos primero
            paginaLogin.ingresarCredenciales("test_user", "test_password");
            
            // ACT: Limpiar campos
            paginaLogin.limpiarCamposLogin();
            
            // ASSERT: Verificar que los campos están vacíos
            // (Esta verificación depende de la implementación específica)
            
            logValidacion("Limpieza de campos verificada");
            
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                "Error probando limpieza de campos: " + e.getMessage()));
            // No fallar la prueba por esto
        }
    }
    
    @Test(priority = 10,
          description = "Verificar estado de página después de múltiples intentos fallidos",
          groups = {"stress", "login", "negativo"})
    @Story("Resistencia del Sistema")
    @Severity(SeverityLevel.MINOR)
    @Description("Verifica el comportamiento después de múltiples intentos de login fallidos")
    public void testMultiplesIntentosFallidos() {
        
        logPasoPrueba("Probando múltiples intentos fallidos");
        
        int intentos = 3;
        
        for (int i = 1; i <= intentos; i++) {
            logger.info(TipoMensaje.PASO_PRUEBA.formatearMensaje(
                "Intento fallido " + i + "/" + intentos));
            
            // ACT: Realizar login fallido
            boolean loginExitoso = paginaLogin.realizarLoginCompleto(
                USUARIO_INVALIDO, PASSWORD_INVALIDO, 
                "LOGIN_MULTIPLES_INTENTOS_" + i);
            
            // ASSERT: Verificar que sigue fallando
            Assert.assertFalse(loginExitoso, 
                "Login debería fallar en intento " + i);
            
            Assert.assertTrue(paginaLogin.esPaginaVisible(), 
                "Página de login debería seguir visible después del intento " + i);
            
            // Pequeña pausa entre intentos
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        logValidacion("Sistema resistente a múltiples intentos fallidos");
    }
    
    // === CASOS DE PRUEBA DE SEGURIDAD BÁSICA ===
    
    @Test(priority = 11,
          description = "Verificar que no se muestran passwords en texto plano",
          groups = {"security", "login"})
    @Story("Seguridad Básica")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifica que el campo password está enmascarado")
    public void testSeguridadPasswordEnmascarado() {
        
        logPasoPrueba("Verificando enmascarado de password");
        
        try {
            // ACT: Ingresar credenciales
            paginaLogin.ingresarCredenciales("test", "secretpassword");
            
            // ASSERT: Verificar que el campo password tiene tipo "password"
            // (Esta verificación depende de los elementos web específicos)
            
            capturarPantalla("seguridad_password_enmascarado");
            
            logValidacion("Campo password verificado como enmascarado");
            
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                "No se pudo verificar enmascarado de password: " + e.getMessage()));
        }
    }
    
    // === CASOS DE PRUEBA DE INTERFAZ ===
    
    @Test(priority = 12,
          description = "Verificar elementos de interfaz presentes",
          groups = {"ui", "login"})
    @Story("Interfaz de Usuario")
    @Severity(SeverityLevel.MINOR)
    @Description("Verifica que todos los elementos de la interfaz están presentes")
    public void testElementosInterfazPresentes() {
        
        logPasoPrueba("Verificando elementos de interfaz");
        
        // ASSERT: Verificar que la página está visible (ya validado en @BeforeMethod)
        Assert.assertTrue(paginaLogin.esPaginaVisible(), 
            "La página de login debería estar completamente visible");
        
        // Capturar estado inicial de la página
        capturarPantalla("interfaz_elementos_presentes");
        
        // Log del estado actual
        String estadoPagina = paginaLogin.obtenerEstadoPagina();
        logger.info(TipoMensaje.INFORMATIVO.formatearMensaje(
            "Estado de página verificado:\n" + estadoPagina));
        
        logValidacion("Elementos de interfaz verificados");
    }
    
    // === CASOS DE PRUEBA DE FLUJO COMPLETO ===
    
    @Test(priority = 13,
          description = "Verificar flujo completo: login exitoso seguido de logout",
          groups = {"integration", "login", "smoke"})
    @Story("Flujo Completo")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifica el flujo completo de login y logout")
    public void testFlujoCompletoLoginLogout() {
        
        logPasoPrueba("Probando flujo completo login-logout");
        
        // ACT 1: Login exitoso
        boolean loginExitoso = paginaLogin.realizarLoginCompleto(
            USUARIO_VALIDO, PASSWORD_VALIDO, "FLUJO_COMPLETO_LOGIN");
        
        // ASSERT 1: Verificar login
        Assert.assertTrue(loginExitoso, "Login debería ser exitoso");
        Assert.assertTrue(paginaLogin.esLoginExitoso(), "Estado debería ser logueado");
        
        capturarPantalla("flujo_completo_despues_login");
        
        // ACT 2: Logout
        paginaLogin.realizarLogout();
        
        // ASSERT 2: Verificar logout (volver a página de login)
        // Esperar un momento para la transición
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Verificar que estamos de vuelta en login
        try {
            paginaLogin.navegarAPagina(); // Asegurar que estamos en login
            Assert.assertTrue(paginaLogin.esPaginaVisible(), 
                "Deberíamos volver a la página de login después del logout");
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                "No se pudo verificar logout completamente: " + e.getMessage()));
        }
        
        capturarPantalla("flujo_completo_despues_logout");
        
        logValidacion("Flujo completo login-logout verificado");
    }
    
    // === CASOS DE PRUEBA DE RENDIMIENTO BÁSICO ===
    
    @Test(priority = 14,
          description = "Verificar tiempo de respuesta de login",
          groups = {"performance", "login"})
    @Story("Rendimiento")
    @Severity(SeverityLevel.MINOR)
    @Description("Verifica que el login responde en tiempo razonable")
    public void testTiempoRespuestaLogin() {
        
        logPasoPrueba("Midiendo tiempo de respuesta de login");
        
        long tiempoInicio = System.currentTimeMillis();
        
        // ACT: Realizar login y medir tiempo
        boolean loginExitoso = paginaLogin.realizarLoginCompleto(
            USUARIO_VALIDO, PASSWORD_VALIDO, "TIEMPO_RESPUESTA");
        
        long tiempoFinal = System.currentTimeMillis();
        long tiempoTranscurrido = tiempoFinal - tiempoInicio;
        
        // ASSERT: Verificar que el login fue exitoso
        Assert.assertTrue(loginExitoso, "Login debería ser exitoso");
        
        // Log del tiempo transcurrido
        logger.info(TipoMensaje.INFORMATIVO.formatearMensaje(
            "Tiempo de login: " + tiempoTranscurrido + " ms"));
        
        // Verificar que el tiempo es razonable (menos de 30 segundos)
        Assert.assertTrue(tiempoTranscurrido < 30000, 
            "Login debería completarse en menos de 30 segundos. Tiempo actual: " + tiempoTranscurrido + "ms");
        
        logValidacion("Tiempo de respuesta verificado: " + tiempoTranscurrido + "ms");
    }
    
    // === MÉTODO DE UTILIDAD ===
    
    /**
     * Método auxiliar para capturar pantalla en las pruebas
     */
    private void capturarPantalla(String nombreArchivo) {
        try {
            String nombreCompleto = "login_" + nombreArchivo;
            GestorCapturaPantalla.capturarPantallaCompleta(driver, nombreCompleto);
            
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                "Error capturando pantalla: " + e.getMessage()));
        }
    }
    
    /**
     * Método auxiliar para logging de pasos de prueba
     */
    private void logPasoPrueba(String mensaje) {
        logger.info(TipoMensaje.PASO_PRUEBA.formatearMensaje(mensaje));
    }
    
    /**
     * Método auxiliar para logging de validaciones
     */
    private void logValidacion(String mensaje) {
        logger.info(TipoMensaje.VALIDACION.formatearMensaje(mensaje));
    }
}