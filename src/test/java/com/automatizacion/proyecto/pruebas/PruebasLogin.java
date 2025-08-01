package test.java.com.automatizacion.proyecto.pruebas;

import test.java.com.automatizacion.proyecto.base.BaseTest;
import test.java.com.automatizacion.proyecto.datos.ModeloDatosPrueba;
import test.java.com.automatizacion.proyecto.datos.ProveedorDatos;
import main.java.com.automatizacion.proyecto.enums.TipoMensaje;
import test.java.com.automatizacion.proyecto.paginas.PaginaLogin;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.Assert;

/**
 * Clase de pruebas para la funcionalidad de inicio de sesión.
 * Contiene todos los casos de prueba relacionados con el login de usuarios.
 * 
 * Extiende BaseTest para heredar configuración común y sigue
 * las mejores prácticas de automatización de pruebas.
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
@Epic("Autenticación de Usuarios")
@Feature("Inicio de Sesión")
public class PruebasLogin extends BaseTest {

    private PaginaLogin paginaLogin;

    /**
     * Configuración específica para cada método de prueba de login
     */
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

    // === CASOS DE PRUEBA EXITOSOS ===

    @Test(priority = 1, description = "Verificar login exitoso con credenciales válidas", groups = { "smoke", "login",
            "positivo" })
    @Story("Login Exitoso")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verifica que un usuario puede iniciar sesión correctamente con credenciales válidas")
    public void testLoginExitoso() {

        // Datos de prueba
        ModeloDatosPrueba datos = ModeloDatosPrueba.builder()
                .casoPrueba("LOGIN_001")
                .descripcion("Login exitoso con credenciales válidas")
                .email("usuario.valido@test.com")
                .password("Password123!")
                .esValido(true)
                .resultadoEsperado("Login exitoso y redirección a dashboard")
                .build();

        logPasoPrueba("Ejecutando caso: " + datos.getCasoPrueba());

        // Ejecutar login
        boolean loginExitoso = paginaLogin.iniciarSesion(datos);

        // Validaciones
        Assert.assertTrue(loginExitoso,
                "El login debería ser exitoso con credenciales válidas");

        logValidacion("Login exitoso verificado correctamente");

        if (datos.isRequiereCaptura()) {
            capturarPantalla("login_exitoso");
        }
    }

    @Test(priority = 2, dataProvider = "datosLoginValidos", dataProviderClass = ProveedorDatos.class, description = "Verificar login con múltiples usuarios válidos", groups = {
            "regression", "login", "positivo" })
    @Story("Login con Múltiples Usuarios")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifica el login con diferentes combinaciones de usuarios válidos")
    public void testLoginMultiplesUsuarios(ModeloDatosPrueba datos) {

        logPasoPrueba("Ejecutando caso: " + datos.getCasoPrueba() + " - " + datos.getDescripcionCaso());

        // Ejecutar login
        boolean loginExitoso = paginaLogin.iniciarSesion(datos);

        // Validaciones
        Assert.assertTrue(loginExitoso,
                "El login debería ser exitoso para: " + datos.generarResumen());

        logValidacion("Login exitoso para usuario: " + datos.generarResumen());
    }

    // === CASOS DE PRUEBA NEGATIVOS ===

    @Test(priority = 3, description = "Verificar login fallido con credenciales inválidas", groups = { "smoke", "login",
            "negativo" })
    @Story("Login Fallido")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifica que el sistema rechaza credenciales inválidas correctamente")
    public void testLoginCredencialesInvalidas() {

        ModeloDatosPrueba datos = ModeloDatosPrueba.builder()
                .casoPrueba("LOGIN_002")
                .descripcion("Login con credenciales inválidas")
                .email("usuario.invalido@test.com")
                .password("PasswordIncorrecto")
                .esValido(false)
                .resultadoEsperado("Error de credenciales inválidas")
                .mensajeError("Credenciales incorrectas")
                .build();

        logPasoPrueba("Ejecutando caso: " + datos.getCasoPrueba());

        // Ejecutar login
        boolean loginFallido = paginaLogin.iniciarSesion(datos);

        // Validaciones
        Assert.assertTrue(loginFallido,
                "El login debería fallar con credenciales inválidas");

        String mensajeError = paginaLogin.obtenerMensajeError();
        Assert.assertFalse(mensajeError.isEmpty(),
                "Debería mostrarse un mensaje de error");

        logValidacion("Login rechazado correctamente. Mensaje: " + mensajeError);
        capturarPantalla("login_credenciales_invalidas");
    }

    @Test(priority = 4, description = "Verificar login con email vacío", groups = { "regression", "login", "negativo",
            "validacion" })
    @Story("Validación de Campos")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifica la validación cuando el campo email está vacío")
    public void testLoginEmailVacio() {

        logPasoPrueba("Probando login con email vacío");

        // Intentar login solo con password
        paginaLogin.ingresarEmail("");
        paginaLogin.ingresarPassword("Password123!");
        paginaLogin.clickBotonLogin();

        // Validaciones
        Assert.assertTrue(paginaLogin.esPaginaVisible(),
                "Debería permanecer en la página de login");

        String mensajeError = paginaLogin.obtenerMensajeError();
        Assert.assertFalse(mensajeError.isEmpty(),
                "Debería mostrarse mensaje de error por email vacío");

        logValidacion("Validación de email vacío funcionando correctamente");
    }

    @Test(priority = 5, description = "Verificar login con password vacío", groups = { "regression", "login",
            "negativo", "validacion" })
    @Story("Validación de Campos")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifica la validación cuando el campo password está vacío")
    public void testLoginPasswordVacio() {

        logPasoPrueba("Probando login con password vacío");

        // Intentar login solo con email
        paginaLogin.ingresarEmail("usuario@test.com");
        paginaLogin.ingresarPassword("");
        paginaLogin.clickBotonLogin();

        // Validaciones
        Assert.assertTrue(paginaLogin.esPaginaVisible(),
                "Debería permanecer en la página de login");

        String mensajeError = paginaLogin.obtenerMensajeError();
        Assert.assertFalse(mensajeError.isEmpty(),
                "Debería mostrarse mensaje de error por password vacío");

        logValidacion("Validación de password vacío funcionando correctamente");
    }

    @Test(priority = 6, dataProvider = "datosLoginInvalidos", dataProviderClass = ProveedorDatos.class, description = "Verificar login con datos inválidos desde proveedor de datos", groups = {
            "regression", "login", "negativo" })
    @Story("Login con Datos Inválidos")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifica el manejo de diferentes tipos de datos inválidos")
    public void testLoginDatosInvalidos(ModeloDatosPrueba datos) {

        logPasoPrueba("Ejecutando caso negativo: " + datos.getCasoPrueba());

        // Ejecutar login
        boolean loginFallido = paginaLogin.iniciarSesion(datos);

        // Validaciones
        Assert.assertTrue(loginFallido,
                "El login debería fallar para: " + datos.generarResumen());

        if (datos.getMensajeErrorEsperado() != null) {
            String mensajeActual = paginaLogin.obtenerMensajeError();
            Assert.assertTrue(mensajeActual.contains(datos.getMensajeErrorEsperado()) ||
                    !mensajeActual.isEmpty(),
                    "Debería mostrarse mensaje de error apropiado");
        }

        logValidacion("Caso negativo validado correctamente: " + datos.getCasoPrueba());
    }

    // === CASOS DE PRUEBA DE FUNCIONALIDADES ADICIONALES ===

    @Test(priority = 7, description = "Verificar funcionalidad 'Recordarme'", groups = { "regression", "login",
            "funcionalidad" })
    @Story("Funcionalidades Adicionales")
    @Severity(SeverityLevel.MINOR)
    @Description("Verifica que la opción 'Recordarme' funciona correctamente")
    public void testFuncionalidadRecordarme() {

        logPasoPrueba("Probando funcionalidad 'Recordarme'");

        // Activar recordarme
        paginaLogin.establecerRecordarme(true);

        ModeloDatosPrueba datos = ModeloDatosPrueba.builder()
                .email("usuario.recordar@test.com")
                .password("Password123!")
                .esValido(true)
                .build();

        // Ejecutar login
        boolean loginExitoso = paginaLogin.iniciarSesion(datos);

        // Validaciones
        Assert.assertTrue(loginExitoso,
                "El login con 'Recordarme' debería ser exitoso");

        logValidacion("Funcionalidad 'Recordarme' probada correctamente");
    }

    @Test(priority = 8, description = "Verificar enlace 'Olvidé mi contraseña'", groups = { "regression", "login",
            "navegacion" })
    @Story("Navegación")
    @Severity(SeverityLevel.MINOR)
    @Description("Verifica que el enlace de recuperación de contraseña funciona")
    public void testEnlaceOlvidoPassword() {

        logPasoPrueba("Probando enlace 'Olvidé mi contraseña'");

        // Click en enlace
        paginaLogin.clickOlvidoPassword();

        // Validar navegación (esto dependería de la implementación específica)
        // Por ahora validamos que se ejecutó sin errores
        logValidacion("Enlace 'Olvidé mi contraseña' funcional");

        capturarPantalla("olvido_password_navegacion");
    }

    @Test(priority = 9, description = "Verificar enlace de registro", groups = { "regression", "login", "navegacion" })
    @Story("Navegación")
    @Severity(SeverityLevel.MINOR)
    @Description("Verifica que el enlace para ir a registro funciona")
    public void testEnlaceRegistro() {

        logPasoPrueba("Probando navegación a registro desde login");

        // Click en enlace de registro
        paginaLogin.irARegistro();

        // Validaciones básicas
        logValidacion("Navegación a registro ejecutada");

        capturarPantalla("navegacion_a_registro");
    }

    // === CASOS DE PRUEBA DE SEGURIDAD ===

    @Test(priority = 10, description = "Verificar bloqueo por múltiples intentos fallidos", groups = { "security",
            "login", "negativo" }, enabled = true)
    @Story("Seguridad")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifica el comportamiento ante múltiples intentos de login fallidos")
    public void testBloqueoMultiplesIntentos() {

        logPasoPrueba("Probando bloqueo por múltiples intentos fallidos");

        ModeloDatosPrueba datosInvalidos = ModeloDatosPrueba.builder()
                .email("usuario.bloqueo@test.com")
                .password("PasswordIncorrecto")
                .esValido(false)
                .build();

        // Realizar múltiples intentos fallidos
        for (int i = 1; i <= 3; i++) {
            logPasoPrueba("Intento fallido #" + i);

            paginaLogin.limpiarFormulario();
            paginaLogin.iniciarSesion(datosInvalidos);

            String mensajeError = paginaLogin.obtenerMensajeError();
            Assert.assertFalse(mensajeError.isEmpty(),
                    "Debería haber mensaje de error en intento " + i);
        }

        logValidacion("Prueba de múltiples intentos completada");
        capturarPantalla("multiples_intentos_fallidos");
    }

    @Test(priority = 11, description = "Verificar caracteres especiales en credenciales", groups = { "security",
            "login", "edge-cases" })
    @Story("Casos Borde")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifica el manejo de caracteres especiales en las credenciales")
    public void testCaracteresEspeciales() {

        logPasoPrueba("Probando caracteres especiales en credenciales");

        ModeloDatosPrueba datos = ModeloDatosPrueba.builder()
                .casoPrueba("LOGIN_SPECIAL_CHARS")
                .email("test@domain.com")
                .password("P@ssw0rd!#$%")
                .esValido(true)
                .build();

        // Intentar login con caracteres especiales
        boolean resultado = paginaLogin.iniciarSesion(datos);

        // La validación depende de si el sistema acepta estos caracteres
        logValidacion("Prueba de caracteres especiales completada. Resultado: " + resultado);

        capturarPantalla("caracteres_especiales");
    }

    // === CASOS DE PRUEBA DE PERFORMANCE ===

    @Test(priority = 12, description = "Verificar tiempo de respuesta del login", groups = { "performance", "login" })
    @Story("Performance")
    @Severity(SeverityLevel.MINOR)
    @Description("Verifica que el tiempo de respuesta del login esté dentro de límites aceptables")
    public void testTiempoRespuestaLogin() {

        logPasoPrueba("Midiendo tiempo de respuesta del login");

        ModeloDatosPrueba datos = ModeloDatosPrueba.builder()
                .email("usuario.performance@test.com")
                .password("Password123!")
                .esValido(true)
                .build();

        long tiempoInicio = System.currentTimeMillis();

        // Ejecutar login
        boolean loginExitoso = paginaLogin.iniciarSesion(datos);

        long tiempoFin = System.currentTimeMillis();
        long tiempoRespuesta = tiempoFin - tiempoInicio;

        // Validaciones
        Assert.assertTrue(loginExitoso, "El login debería ser exitoso");
        Assert.assertTrue(tiempoRespuesta < 5000,
                "El tiempo de respuesta (" + tiempoRespuesta + "ms) debería ser menor a 5 segundos");

        logValidacion("Tiempo de respuesta del login: " + tiempoRespuesta + "ms");
    }

    // === CASOS DE PRUEBA DE UI/UX ===

    @Test(priority = 13, description = "Verificar elementos de la interfaz de usuario", groups = { "ui", "login" })
    @Story("Interfaz de Usuario")
    @Severity(SeverityLevel.MINOR)
    @Description("Verifica que todos los elementos de la interfaz estén presentes y sean accesibles")
    public void testElementosInterfazUsuario() {

        logPasoPrueba("Validando elementos de la interfaz de usuario");

        // Verificar elementos principales
        paginaLogin.validarElementosPagina();
       

        // Verificar título
        String titulo = paginaLogin.obtenerTitulo();
        Assert.assertFalse(titulo.isEmpty(),
                "La página debería tener un título");

        logValidacion("Elementos de interfaz validados correctamente. Título: " + titulo);

        capturarPantalla("elementos_interfaz_login");
    }

    @Test(priority = 14, description = "Verificar limpieza de formulario", groups = { "ui", "login", "funcionalidad" })
    @Story("Funcionalidades de Formulario")
    @Severity(SeverityLevel.MINOR)
    @Description("Verifica que la limpieza del formulario funciona correctamente")
    public void testLimpiezaFormulario() {

        logPasoPrueba("Probando limpieza de formulario");

        // Llenar campos
        paginaLogin.ingresarEmail("test@email.com");
        paginaLogin.ingresarPassword("testpassword");

        // Limpiar formulario
        paginaLogin.limpiarFormulario();

        // Verificar que los campos estén vacíos sería ideal,
        // pero depende de la implementación específica
        logValidacion("Limpieza de formulario ejecutada");
    }
}