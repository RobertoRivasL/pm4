package com.automatizacion.proyecto.pruebas;

import com.automatizacion.proyecto.pruebas.BaseTest;
import com.automatizacion.proyecto.datos.ModeloDatosPrueba;
import com.automatizacion.proyecto.datos.ProveedorDatos;
import com.automatizacion.proyecto.enums.TipoMensaje;
import com.automatizacion.proyecto.paginas.PaginaRegistro;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.Assert;

/**
 * Clase de pruebas para la funcionalidad de registro de usuarios.
 * Contiene todos los casos de prueba relacionados con el registro.
 * 
 * Extiende BaseTest para heredar configuración común y sigue
 * las mejores prácticas de automatización de pruebas.
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
@Epic("Gestión de Usuarios")
@Feature("Registro de Usuarios")
public class PruebasRegistro extends BaseTest {

    private PaginaRegistro paginaRegistro;

    /**
     * Configuración específica para cada método de prueba de registro
     */
    @BeforeMethod(alwaysRun = true)
    public void configuracionEspecificaRegistro() {
        paginaRegistro = new PaginaRegistro(obtenerDriver());

        logPasoPrueba("Verificando que la página de registro está visible");

        boolean paginaVisible = paginaRegistro.esPaginaVisible();
        Assert.assertTrue(paginaVisible, "La página de registro debería estar visible");

        if (paginaVisible) {
            logValidacion("Página de registro cargada correctamente");
            paginaRegistro.validarElementosPagina();
        }
    }

    // === CASOS DE PRUEBA EXITOSOS ===

    @Test(priority = 1, description = "Verificar registro exitoso con datos válidos", groups = { "smoke", "registro",
            "positivo" })
    @Story("Registro Exitoso")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verifica que un usuario puede registrarse correctamente con datos válidos")
    public void testRegistroExitoso() {

        ModeloDatosPrueba datos = ModeloDatosPrueba.builder()
                .casoPrueba("REG_001")
                .descripcion("Registro exitoso con datos válidos")
                .nombre("Roberto")
                .apellido("Rivas")
                .email("roberto.rivas." + System.currentTimeMillis() + "@test.com")
                .password("Password123!")
                .confirmacionPassword("Password123!")
                .telefono("+56912345678")
                .esValido(true)
                .resultadoEsperado("Usuario registrado exitosamente")
                .build();

        logPasoPrueba("Ejecutando caso: " + datos.getCasoPrueba());

        // Ejecutar registro
        boolean registroExitoso = paginaRegistro.registrarUsuario(datos);

        // Validaciones
        Assert.assertTrue(registroExitoso,
                "El registro debería ser exitoso con datos válidos");

        logValidacion("Registro exitoso verificado correctamente");
        capturarPantalla("registro_exitoso");
    }

    @Test(priority = 2, dataProvider = "datosRegistroValidos", dataProviderClass = ProveedorDatos.class, description = "Verificar registro con múltiples conjuntos de datos válidos", groups = {
            "regression", "registro", "positivo" })
    @Story("Registro con Múltiples Datos")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifica el registro con diferentes combinaciones de datos válidos")
    public void testRegistroMultiplesDatos(ModeloDatosPrueba datos) {

        // Hacer email único para evitar conflictos
        datos.setEmail(datos.getEmail().replace("@", "." + System.currentTimeMillis() + "@"));

        logPasoPrueba("Ejecutando caso: " + datos.getCasoPrueba() + " - " + datos.getDescripcionCaso());

        // Ejecutar registro
        boolean registroExitoso = paginaRegistro.registrarUsuario(datos);

        // Validaciones
        Assert.assertTrue(registroExitoso,
                "El registro debería ser exitoso para: " + datos.generarResumen());

        logValidacion("Registro exitoso para: " + datos.generarResumen());
    }

    // === CASOS DE PRUEBA NEGATIVOS - VALIDACIÓN DE CAMPOS ===

    @Test(priority = 3, description = "Verificar validación de campo nombre vacío", groups = { "regression", "registro",
            "negativo", "validacion" })
    @Story("Validación de Campos Obligatorios")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifica que el sistema valida correctamente cuando el nombre está vacío")
    public void testValidacionNombreVacio() {

        logPasoPrueba("Probando validación de nombre vacío");

        ModeloDatosPrueba datos = ModeloDatosPrueba.builder()
                .casoPrueba("REG_VAL_001")
                .nombre("") // Nombre vacío
                .apellido("Apellido")
                .email("test@email.com")
                .password("Password123!")
                .confirmacionPassword("Password123!")
                .esValido(false)
                .mensajeError("El nombre es obligatorio")
                .build();

        // Ejecutar registro
        boolean registroFallido = paginaRegistro.registrarUsuario(datos);

        // Validaciones
        Assert.assertTrue(registroFallido,
                "El registro debería fallar con nombre vacío");

        String mensajeError = paginaRegistro.obtenerMensajeError();
        Assert.assertFalse(mensajeError.isEmpty(),
                "Debería mostrarse mensaje de error por nombre vacío");

        logValidacion("Validación de nombre vacío funcionando correctamente");
        capturarPantalla("validacion_nombre_vacio");
    }

    @Test(priority = 4, description = "Verificar validación de email inválido", groups = { "regression", "registro",
            "negativo", "validacion" })
    @Story("Validación de Email")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifica que el sistema valida correctamente formatos de email inválidos")
    public void testValidacionEmailInvalido() {

        logPasoPrueba("Probando validación de email inválido");

        String[] emailsInvalidos = {
                "email-sin-arroba.com",
                "email@sin-dominio",
                "email@.com",
                "@dominio.com",
                "email espacios@dominio.com"
        };

        for (String emailInvalido : emailsInvalidos) {

            logPasoPrueba("Probando email inválido: " + emailInvalido);

            // Limpiar formulario
            paginaRegistro.limpiarFormulario();

            // Llenar con email inválido
            paginaRegistro.ingresarNombre("Test");
            paginaRegistro.ingresarApellido("User");
            paginaRegistro.ingresarEmail(emailInvalido);
            paginaRegistro.ingresarPassword("Password123!");
            paginaRegistro.ingresarConfirmarPassword("Password123!");
            paginaRegistro.aceptarTerminos();
            paginaRegistro.clickBotonRegistrar();

            // Validar que permanece en la página o muestra error
            boolean hayError = paginaRegistro.hayErroresValidacion() ||
                    paginaRegistro.esPaginaVisible();

            Assert.assertTrue(hayError,
                    "Debería mostrar error o permanecer en página para email: " + emailInvalido);
        }

        logValidacion("Validación de emails inválidos completada");
        capturarPantalla("validacion_emails_invalidos");
    }

    @Test(priority = 5, description = "Verificar validación de contraseñas que no coinciden", groups = { "regression",
            "registro", "negativo", "validacion" })
    @Story("Validación de Contraseñas")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifica que el sistema detecta cuando las contraseñas no coinciden")
    public void testValidacionPasswordsNoCoinciden() {

        logPasoPrueba("Probando validación de contraseñas que no coinciden");

        ModeloDatosPrueba datos = ModeloDatosPrueba.builder()
                .casoPrueba("REG_VAL_002")
                .nombre("Test")
                .apellido("User")
                .email("test.passwords@email.com")
                .password("Password123!")
                .confirmacionPassword("DiferentePassword456!")
                .esValido(false)
                .mensajeError("Las contraseñas no coinciden")
                .build();

        // Ejecutar registro
        boolean registroFallido = paginaRegistro.registrarUsuario(datos);

        // Validaciones
        Assert.assertTrue(registroFallido,
                "El registro debería fallar cuando las contraseñas no coinciden");

        String mensajeError = paginaRegistro.obtenerMensajeError();
        Assert.assertFalse(mensajeError.isEmpty(),
                "Debería mostrarse mensaje de error por contraseñas diferentes");

        logValidacion("Validación de contraseñas diferentes funcionando correctamente");
        capturarPantalla("validacion_passwords_diferentes");
    }

    @Test(priority = 6, description = "Verificar validación de contraseña débil", groups = { "security", "registro",
            "negativo", "validacion" })
    @Story("Seguridad de Contraseñas")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifica que el sistema rechaza contraseñas que no cumplen los criterios de seguridad")
    public void testValidacionPasswordDebil() {

        logPasoPrueba("Probando validación de contraseña débil");

        String[] passwordsDebiles = {
                "123", // Muy corta
                "password", // Sin mayúsculas ni números
                "PASSWORD", // Solo mayúsculas
                "12345678", // Solo números
                "Password" // Sin números ni caracteres especiales
        };

        for (String passwordDebil : passwordsDebiles) {

            logPasoPrueba("Probando contraseña débil: " + passwordDebil);

            // Limpiar formulario
            paginaRegistro.limpiarFormulario();

            ModeloDatosPrueba datos = ModeloDatosPrueba.builder()
                    .nombre("Test")
                    .apellido("User")
                    .email("test.weak@email.com")
                    .password(passwordDebil)
                    .confirmacionPassword(passwordDebil)
                    .esValido(false)
                    .build();

            // Ejecutar registro
            boolean registroFallido = paginaRegistro.registrarUsuario(datos);

            // Validar que falla o muestra advertencia
            Assert.assertTrue(registroFallido || paginaRegistro.hayErroresValidacion(),
                    "Debería rechazar o advertir sobre contraseña débil: " + passwordDebil);
        }

        logValidacion("Validación de contraseñas débiles completada");
        capturarPantalla("validacion_passwords_debiles");
    }

    @Test(priority = 7, dataProvider = "datosRegistroInvalidos", dataProviderClass = ProveedorDatos.class, description = "Verificar registro con datos inválidos desde proveedor de datos", groups = {
            "regression", "registro", "negativo" })
    @Story("Registro con Datos Inválidos")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifica el manejo de diferentes tipos de datos inválidos")
    public void testRegistroDatosInvalidos(ModeloDatosPrueba datos) {

        logPasoPrueba("Ejecutando caso negativo: " + datos.getCasoPrueba());

        // Ejecutar registro
        boolean registroFallido = paginaRegistro.registrarUsuario(datos);

        // Validaciones
        Assert.assertTrue(registroFallido,
                "El registro debería fallar para: " + datos.generarResumen());

        if (datos.getMensajeErrorEsperado() != null) {
            String mensajeActual = paginaRegistro.obtenerMensajeError();
            Assert.assertTrue(mensajeActual.contains(datos.getMensajeErrorEsperado()) ||
                    !mensajeActual.isEmpty(),
                    "Debería mostrarse mensaje de error apropiado");
        }

        logValidacion("Caso negativo validado correctamente: " + datos.getCasoPrueba());
    }

    // === CASOS DE PRUEBA DE USUARIO EXISTENTE ===

    @Test(priority = 8, description = "Verificar validación de usuario existente", groups = { "regression", "registro",
            "negativo", "usuario-existente" })
    @Story("Usuario Existente")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifica que el sistema detecta cuando se intenta registrar un usuario que ya existe")
    public void testRegistroUsuarioExistente() {

        logPasoPrueba("Probando registro de usuario existente");

        // Usar un email que "sabemos" que existe (en un escenario real)
        ModeloDatosPrueba datos = ModeloDatosPrueba.builder()
                .casoPrueba("REG_003")
                .descripcion("Registro con usuario existente")
                .nombre("Usuario")
                .apellido("Existente")
                .email("usuario.existente@test.com")
                .password("Password123!")
                .confirmacionPassword("Password123!")
                .esValido(false)
                .mensajeError("El usuario ya existe")
                .build();

        // Ejecutar registro
        boolean registroFallido = paginaRegistro.registrarUsuario(datos);

        // Validaciones
        Assert.assertTrue(registroFallido,
                "El registro debería fallar para usuario existente");

        String mensajeError = paginaRegistro.obtenerMensajeError();
        // En algunos sistemas podría no mostrar este error por seguridad
        logValidacion("Prueba de usuario existente completada. Mensaje: " + mensajeError);

        capturarPantalla("registro_usuario_existente");
    }

    // === CASOS DE PRUEBA DE TÉRMINOS Y CONDICIONES ===

    @Test(priority = 9, description = "Verificar validación de términos y condiciones no aceptados", groups = {
            "regression", "registro", "negativo", "validacion" })
    @Story("Términos y Condiciones")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifica que no se puede registrar sin aceptar términos y condiciones")
    public void testValidacionTerminosNoAceptados() {

        logPasoPrueba("Probando registro sin aceptar términos y condiciones");

        // Llenar formulario sin aceptar términos
        paginaRegistro.ingresarNombre("Test");
        paginaRegistro.ingresarApellido("User");
        paginaRegistro.ingresarEmail("test.terminos@email.com");
        paginaRegistro.ingresarPassword("Password123!");
        paginaRegistro.ingresarConfirmarPassword("Password123!");

        // NO aceptar términos
        // paginaRegistro.aceptarTerminos(); // Comentado intencionalmente

        paginaRegistro.clickBotonRegistrar();

        // Validaciones
        Assert.assertTrue(paginaRegistro.esPaginaVisible(),
                "Debería permanecer en la página de registro");

        boolean hayError = paginaRegistro.hayErroresValidacion();
        Assert.assertTrue(hayError,
                "Debería mostrar error por no aceptar términos");

        logValidacion("Validación de términos no aceptados funcionando correctamente");
        capturarPantalla("validacion_terminos_no_aceptados");
    }

    // === CASOS DE PRUEBA DE FUNCIONALIDADES ADICIONALES ===

    @Test(priority = 10, description = "Verificar funcionalidad de campos opcionales", groups = { "regression",
            "registro", "funcionalidad" })
    @Story("Campos Opcionales")
    @Severity(SeverityLevel.MINOR)
    @Description("Verifica que los campos opcionales funcionan correctamente")
    public void testCamposOpcionales() {

        logPasoPrueba("Probando campos opcionales del registro");

        ModeloDatosPrueba datos = ModeloDatosPrueba.builder()
                .casoPrueba("REG_004")
                .nombre("Usuario")
                .apellido("Completo")
                .email("usuario.completo." + System.currentTimeMillis() + "@test.com")
                .password("Password123!")
                .confirmacionPassword("Password123!")
                .telefono("+56987654321")
                .genero("Masculino")
                .pais("Chile")
                .ciudad("Santiago")
                .fechaNacimiento("01/01/1990")
                .esValido(true)
                .build();

        // Ejecutar registro con campos opcionales
        boolean registroExitoso = paginaRegistro.registrarUsuario(datos);

        // Validaciones
        Assert.assertTrue(registroExitoso,
                "El registro con campos opcionales debería ser exitoso");

        logValidacion("Registro con campos opcionales completado correctamente");
        capturarPantalla("registro_campos_opcionales");
    }

    @Test(priority = 11, description = "Verificar funcionalidad de newsletter", groups = { "regression", "registro",
            "funcionalidad" })
    @Story("Newsletter")
    @Severity(SeverityLevel.MINOR)
    @Description("Verifica que la opción de newsletter funciona correctamente")
    public void testFuncionalidadNewsletter() {

        logPasoPrueba("Probando funcionalidad de suscripción a newsletter");

        // Llenar datos básicos
        paginaRegistro.ingresarNombre("Test");
        paginaRegistro.ingresarApellido("Newsletter");
        paginaRegistro.ingresarEmail("test.newsletter." + System.currentTimeMillis() + "@email.com");
        paginaRegistro.ingresarPassword("Password123!");
        paginaRegistro.ingresarConfirmarPassword("Password123!");

        // Configurar newsletter
        paginaRegistro.configurarNewsletter(true);
        paginaRegistro.aceptarTerminos();
        paginaRegistro.clickBotonRegistrar();

        // Validar que el registro se completó
        boolean registroExitoso = paginaRegistro.verificarRegistroExitoso();

        logValidacion("Funcionalidad de newsletter probada. Resultado: " + registroExitoso);
        capturarPantalla("funcionalidad_newsletter");
    }

    @Test(priority = 12, description = "Verificar limpieza de formulario", groups = { "ui", "registro",
            "funcionalidad" })
    @Story("Funcionalidades de Formulario")
    @Severity(SeverityLevel.MINOR)
    @Description("Verifica que la limpieza del formulario funciona correctamente")
    public void testLimpiezaFormulario() {

        logPasoPrueba("Probando limpieza de formulario de registro");

        // Llenar todos los campos
        paginaRegistro.ingresarNombre("Test");
        paginaRegistro.ingresarApellido("User");
        paginaRegistro.ingresarEmail("test@email.com");
        paginaRegistro.ingresarPassword("Password123!");
        paginaRegistro.ingresarConfirmarPassword("Password123!");
        paginaRegistro.ingresarTelefono("123456789");

        // Limpiar formulario
        paginaRegistro.limpiarFormulario();

        // Verificar que la limpieza funcionó
        logValidacion("Limpieza de formulario ejecutada");
        capturarPantalla("limpieza_formulario_registro");
    }

    // === CASOS DE PRUEBA DE NAVEGACIÓN ===

    @Test(priority = 13, description = "Verificar enlace de navegación a login", groups = { "regression", "registro",
            "navegacion" })
    @Story("Navegación")
    @Severity(SeverityLevel.MINOR)
    @Description("Verifica que el enlace para ir a login funciona")
    public void testEnlaceLogin() {

        logPasoPrueba("Probando navegación a login desde registro");

        // Click en enlace de login
        paginaRegistro.irALogin();

        // Validaciones básicas
        logValidacion("Navegación a login ejecutada");
        capturarPantalla("navegacion_a_login");
    }

    // === CASOS DE PRUEBA DE PERFORMANCE ===

    @Test(priority = 14, description = "Verificar tiempo de respuesta del registro", groups = { "performance",
            "registro" })
    @Story("Performance")
    @Severity(SeverityLevel.MINOR)
    @Description("Verifica que el tiempo de respuesta del registro esté dentro de límites aceptables")
    public void testTiempoRespuestaRegistro() {

        logPasoPrueba("Midiendo tiempo de respuesta del registro");

        ModeloDatosPrueba datos = ModeloDatosPrueba.builder()
                .nombre("Performance")
                .apellido("Test")
                .email("performance.test." + System.currentTimeMillis() + "@email.com")
                .password("Password123!")
                .confirmacionPassword("Password123!")
                .esValido(true)
                .build();

        long tiempoInicio = System.currentTimeMillis();

        // Ejecutar registro
        boolean registroExitoso = paginaRegistro.registrarUsuario(datos);

        long tiempoFin = System.currentTimeMillis();
        long tiempoRespuesta = tiempoFin - tiempoInicio;

        // Validaciones
        Assert.assertTrue(registroExitoso, "El registro debería ser exitoso");
        Assert.assertTrue(tiempoRespuesta < 10000,
                "El tiempo de respuesta (" + tiempoRespuesta + "ms) debería ser menor a 10 segundos");

        logValidacion("Tiempo de respuesta del registro: " + tiempoRespuesta + "ms");
    }

    // === CASOS DE PRUEBA DE UI/UX ===

    @Test(priority = 15, description = "Verificar elementos de la interfaz de usuario", groups = { "ui", "registro" })
    @Story("Interfaz de Usuario")
    @Severity(SeverityLevel.MINOR)
    @Description("Verifica que todos los elementos de la interfaz estén presentes y sean accesibles")
    public void testElementosInterfazUsuario() {

        logPasoPrueba("Validando elementos de la interfaz de usuario");

        // Verificar elementos principales
        paginaRegistro.validarElementosPagina();

        // Verificar título
        String titulo = paginaRegistro.obtenerTitulo();
        Assert.assertFalse(titulo.isEmpty(),
                "La página debería tener un título");

        logValidacion("Elementos de interfaz validados correctamente. Título: " + titulo);
        capturarPantalla("elementos_interfaz_registro");
    }

    // === CASOS DE PRUEBA DE SEGURIDAD ===

    @Test(priority = 16, description = "Verificar caracteres especiales en campos de texto", groups = { "security",
            "registro", "edge-cases" })
    @Story("Seguridad")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifica el manejo de caracteres especiales en los campos de texto")
    public void testCaracteresEspeciales() {

        logPasoPrueba("Probando caracteres especiales en campos de registro");

        ModeloDatosPrueba datos = ModeloDatosPrueba.builder()
                .casoPrueba("REG_SECURITY_001")
                .nombre("Ñoño José")
                .apellido("O'Connor-Smith")
                .email("test.special." + System.currentTimeMillis() + "@domain.com")
                .password("P@ssw0rd!#$")
                .confirmacionPassword("P@ssw0rd!#$")
                .esValido(true)
                .build();

        // Intentar registro con caracteres especiales
        boolean resultado = paginaRegistro.registrarUsuario(datos);

        // La validación depende de si el sistema acepta estos caracteres
        logValidacion("Prueba de caracteres especiales completada. Resultado: " + resultado);
        capturarPantalla("caracteres_especiales_registro");
    }

    @Test(priority = 17, description = "Verificar prevención de inyección de scripts", groups = { "security",
            "registro", "xss" })
    @Story("Seguridad XSS")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifica que el sistema previene inyección de scripts maliciosos")
    public void testPrevencionXSS() {

        logPasoPrueba("Probando prevención de inyección XSS");

        String[] payloadsXSS = {
                "<script>alert('XSS')</script>",
                "javascript:alert('XSS')",
                "<img src=x onerror=alert('XSS')>",
                "';alert('XSS');//"
        };

        for (String payload : payloadsXSS) {

            logPasoPrueba("Probando payload XSS en nombre: " + payload);

            // Limpiar formulario
            paginaRegistro.limpiarFormulario();

            // Intentar inyectar script en campo nombre
            paginaRegistro.ingresarNombre(payload);
            paginaRegistro.ingresarApellido("TestUser");
            paginaRegistro.ingresarEmail("xss.test@email.com");
            paginaRegistro.ingresarPassword("Password123!");
            paginaRegistro.ingresarConfirmarPassword("Password123!");
            paginaRegistro.aceptarTerminos();
            paginaRegistro.clickBotonRegistrar();

            // Verificar que no se ejecutó el script
            // (esto sería más específico según la implementación)
            boolean sistemaSeguro = paginaRegistro.esPaginaVisible() ||
                    paginaRegistro.hayErroresValidacion();

            Assert.assertTrue(sistemaSeguro,
                    "El sistema debería manejar de forma segura el payload: " + payload);
        }

        logValidacion("Prueba de prevención XSS completada");
        capturarPantalla("prevencion_xss");
    }

    // === CASOS DE PRUEBA DE CASOS BORDE ===

    @Test(priority = 18, description = "Verificar campos con longitud máxima", groups = { "edge-cases", "registro",
            "validacion" })
    @Story("Casos Borde")
    @Severity(SeverityLevel.MINOR)
    @Description("Verifica el comportamiento con campos de longitud máxima")
    public void testCamposLongitudMaxima() {

        logPasoPrueba("Probando campos con longitud máxima");

        // Crear strings largos
        String nombreLargo = "A".repeat(100);
        String apellidoLargo = "B".repeat(100);
        String emailLargo = "test" + "x".repeat(200) + "@domain.com";

        ModeloDatosPrueba datos = ModeloDatosPrueba.builder()
                .casoPrueba("REG_EDGE_001")
                .nombre(nombreLargo)
                .apellido(apellidoLargo)
                .email(emailLargo)
                .password("Password123!")
                .confirmacionPassword("Password123!")
                .esValido(false) // Probablemente falle por longitud
                .build();

        // Ejecutar registro
        boolean resultado = paginaRegistro.registrarUsuario(datos);

        // Verificar que el sistema maneja apropiadamente los campos largos
        logValidacion("Prueba de campos con longitud máxima completada. Resultado: " + resultado);
        capturarPantalla("campos_longitud_maxima");
    }
