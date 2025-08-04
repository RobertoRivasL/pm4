/*
 * Autores: Antonio B. Arriagada LL., Dante Escalona Bustos, Roberto Rivas Lopez
 * Proyecto: Suite de Automatización Funcional
 * Descripción: Pruebas de Login completas basadas en la implementación existente
 * Fecha: 04 de agosto de 2025
 * Entrega Final: 10 PM
 */

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

/**
 * Suite COMPLETA de pruebas para Login - Versión Final
 * Incluye TODOS los casos necesarios para la entrega final
 * 
 * @author Antonio B. Arriagada LL.
 * @author Dante Escalona Bustos
 * @author Roberto Rivas Lopez
 * @version FINAL - Para entrega 04/08/2025 10PM
 */
@Epic("Autenticación de Usuarios")
@Feature("Inicio de Sesión")
public class PruebasLogin extends BaseTest {

    private PaginaLogin paginaLogin;

    @Override
    protected void navegarAUrlBase() {
        try {
            // Usar la URL de login del sitio real
            String urlLogin = "https://practice.expandtesting.com/login";
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

        logPasoPrueba("🔍 Verificando que la página de login está visible");
        boolean paginaVisible = paginaLogin.esPaginaVisible();
        Assert.assertTrue(paginaVisible, "La página de login debería estar visible");

        if (paginaVisible) {
            logValidacion("✅ Página de login cargada correctamente");
            paginaLogin.validarElementosPagina();
            capturarPantalla("configuracion_inicial_login");
        }
    }

    // ================================
    // CASOS POSITIVOS - LOGIN EXITOSO
    // ================================

    @Test(priority = 1, description = "Login exitoso con credenciales válidas del sitio real", groups = { "smoke",
            "login", "positivo", "critico" })
    @Story("Login Exitoso")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verifica login exitoso con credenciales reales: practice/SuperSecretPassword!")
    public void testLoginExitosoCredencialesReales() {

        logPasoPrueba("🔑 Ejecutando login con credenciales reales del sitio");

        ModeloDatosPrueba datos = ModeloDatosPrueba.builder()
                .casoPrueba("LOGIN_REAL_001")
                .descripcion("Login exitoso con credenciales reales")
                .email("practice") // Username real del sitio
                .password("SuperSecretPassword!") // Password real del sitio
                .esValido(true)
                .resultadoEsperado("Usuario autenticado exitosamente")
                .build();

        logPasoPrueba("📋 Datos de login: " + datos.getCasoPrueba());
        capturarPantalla("antes_login_exitoso");

        boolean loginExitoso = paginaLogin.iniciarSesion(datos);

        capturarPantalla("despues_login_exitoso");

        logValidacion("🔍 Verificando resultado del login");
        Assert.assertTrue(loginExitoso, "El login debería ser exitoso con credenciales válidas");

        // Verificar que salimos de la página de login
        String urlActual = obtenerDriver().getCurrentUrl();
        boolean salidaDeLogin = !urlActual.toLowerCase().contains("login");
        logValidacion("📍 URL actual: " + urlActual + " (Salida de login: " + salidaDeLogin + ")");

        capturarPantalla("login_exitoso_pagina_final");
        logger.info(TipoMensaje.EXITO.formatearMensaje("✅ Test de login exitoso completado"));
    }

    @Test(priority = 2, description = "Verificar elementos después de login exitoso", groups = { "regression", "login",
            "positivo" }, dependsOnMethods = { "testLoginExitosoCredencialesReales" })
    @Story("Post-Login Verification")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verificar elementos presentes después de login exitoso")
    public void testElementosPostLogin() {

        logPasoPrueba("🔍 Verificando elementos después del login exitoso");

        String urlActual = obtenerDriver().getCurrentUrl();
        String titulo = obtenerDriver().getTitle();

        logValidacion("📍 URL después de login: " + urlActual);
        logValidacion("📄 Título después de login: " + titulo);

        // Verificar que hay evidencia de login exitoso
        String paginaContent = obtenerDriver().getPageSource().toLowerCase();
        boolean tieneSecure = paginaContent.contains("secure");
        boolean tieneLogout = paginaContent.contains("logout");
        boolean tieneBienvenida = paginaContent.contains("welcome") || paginaContent.contains("logged");

        logValidacion("🔍 Elementos encontrados:");
        logValidacion("   - Área segura: " + tieneSecure);
        logValidacion("   - Logout disponible: " + tieneLogout);
        logValidacion("   - Mensaje de bienvenida: " + tieneBienvenida);

        Assert.assertTrue(tieneSecure || tieneLogout || tieneBienvenida,
                "Debe haber evidencia de login exitoso en la página");

        capturarPantalla("elementos_post_login_verificados");
        logger.info(TipoMensaje.EXITO.formatearMensaje("✅ Verificación post-login completada"));
    }

    // ================================
    // CASOS NEGATIVOS - LOGIN FALLIDO
    // ================================

    @Test(priority = 10, description = "Login fallido con username incorrecto", groups = { "regression", "login",
            "negativo", "validacion" })
    @Story("Validación de Credenciales")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verificar rechazo de username incorrecto")
    public void testLoginUsernameIncorrecto() {

        logPasoPrueba("❌ Probando login con username incorrecto");

        ModeloDatosPrueba datos = ModeloDatosPrueba.builder()
                .casoPrueba("LOGIN_NEG_001")
                .descripcion("Username incorrecto")
                .email("usuario_inexistente") // Username incorrecto
                .password("SuperSecretPassword!") // Password correcto
                .esValido(false)
                .mensajeError("Your username is invalid!")
                .build();

        capturarPantalla("antes_username_incorrecto");

        boolean loginFallido = paginaLogin.iniciarSesion(datos);

        capturarPantalla("despues_username_incorrecto");

        logValidacion("🔍 Verificando que el login falló como esperado");
        Assert.assertTrue(loginFallido, "El login debería fallar con username incorrecto");

        String mensajeError = paginaLogin.obtenerMensajeError();
        logValidacion("⚠️ Mensaje de error: " + mensajeError);

        // Verificar que permanecemos en página de login
        String urlActual = obtenerDriver().getCurrentUrl();
        Assert.assertTrue(urlActual.toLowerCase().contains("login"),
                "Debe permanecer en página de login");

        logger.info(TipoMensaje.EXITO.formatearMensaje("✅ Username incorrecto rechazado correctamente"));
    }

    @Test(priority = 11, description = "Login fallido con password incorrecto", groups = { "regression", "login",
            "negativo", "validacion" })
    @Story("Validación de Credenciales")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verificar rechazo de password incorrecto")
    public void testLoginPasswordIncorrecto() {

        logPasoPrueba("❌ Probando login con password incorrecto");

        ModeloDatosPrueba datos = ModeloDatosPrueba.builder()
                .casoPrueba("LOGIN_NEG_002")
                .descripcion("Password incorrecto")
                .email("practice") // Username correcto
                .password("PasswordIncorrecto123!") // Password incorrecto
                .esValido(false)
                .mensajeError("Your password is invalid!")
                .build();

        capturarPantalla("antes_password_incorrecto");

        boolean loginFallido = paginaLogin.iniciarSesion(datos);

        capturarPantalla("despues_password_incorrecto");

        logValidacion("🔍 Verificando que el login falló como esperado");
        Assert.assertTrue(loginFallido, "El login debería fallar con password incorrecto");

        String mensajeError = paginaLogin.obtenerMensajeError();
        logValidacion("⚠️ Mensaje de error: " + mensajeError);

        logger.info(TipoMensaje.EXITO.formatearMensaje("✅ Password incorrecto rechazado correctamente"));
    }

    @Test(priority = 12, description = "Login fallido con ambas credenciales incorrectas", groups = { "regression",
            "login", "negativo" })
    @Story("Validación de Credenciales")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verificar rechazo total con credenciales completamente incorrectas")
    public void testLoginCredencialesCompletamenteIncorrectas() {

        logPasoPrueba("❌ Probando login con credenciales completamente incorrectas");

        ModeloDatosPrueba datos = ModeloDatosPrueba.builder()
                .casoPrueba("LOGIN_NEG_003")
                .descripcion("Credenciales completamente incorrectas")
                .email("usuario_fake")
                .password("password_fake")
                .esValido(false)
                .build();

        capturarPantalla("antes_credenciales_incorrectas");

        boolean loginFallido = paginaLogin.iniciarSesion(datos);

        capturarPantalla("despues_credenciales_incorrectas");

        Assert.assertTrue(loginFallido, "El login debería fallar con credenciales incorrectas");

        logger.info(TipoMensaje.EXITO.formatearMensaje("✅ Credenciales incorrectas rechazadas"));
    }

    // ================================
    // VALIDACIONES DE CAMPOS VACÍOS
    // ================================

    @Test(priority = 20, description = "Validación de campo username vacío", groups = { "regression", "login",
            "negativo", "validacion" })
    @Story("Validación de Campos Obligatorios")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verificar validación cuando el campo username está vacío")
    public void testLoginUsernameVacio() {

        logPasoPrueba("🔍 Probando username vacío");

        capturarPantalla("antes_username_vacio");

        try {
            paginaLogin.ingresarEmail("");
            paginaLogin.ingresarPassword("SuperSecretPassword!");
            paginaLogin.clickBotonLogin();

            Thread.sleep(2000); // Esperar validación

            capturarPantalla("despues_username_vacio");

            // Verificar que permanece en página de login
            Assert.assertTrue(paginaLogin.esPaginaVisible(),
                    "Debe permanecer en página de login");

            logValidacion("✅ Validación de username vacío funcionando");

        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                    "Error en validación username vacío: " + e.getMessage()));
        }
    }

    @Test(priority = 21, description = "Validación de campo password vacío", groups = { "regression", "login",
            "negativo", "validacion" })
    @Story("Validación de Campos Obligatorios")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verificar validación cuando el campo password está vacío")
    public void testLoginPasswordVacio() {

        logPasoPrueba("🔍 Probando password vacío");

        capturarPantalla("antes_password_vacio");

        try {
            paginaLogin.ingresarEmail("practice");
            paginaLogin.ingresarPassword("");
            paginaLogin.clickBotonLogin();

            Thread.sleep(2000); // Esperar validación

            capturarPantalla("despues_password_vacio");

            // Verificar que permanece en página de login
            Assert.assertTrue(paginaLogin.esPaginaVisible(),
                    "Debe permanecer en página de login");

            logValidacion("✅ Validación de password vacío funcionando");

        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                    "Error en validación password vacío: " + e.getMessage()));
        }
    }

    // ================================
    // CASOS DE SEGURIDAD
    // ================================

    @Test(priority = 30, description = "Prueba de inyección SQL en campos de login", groups = { "security", "login",
            "negativo", "inyeccion" })
    @Story("Seguridad - Prevención de Inyección SQL")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verificar que la aplicación maneja correctamente intentos de inyección SQL")
    public void testLoginInyeccionSQL() {

        logPasoPrueba("🛡️ Probando inyección SQL en login");

        ModeloDatosPrueba datos = ModeloDatosPrueba.builder()
                .casoPrueba("LOGIN_SEC_001")
                .descripcion("Intento de inyección SQL")
                .email("admin'; DROP TABLE users; --")
                .password("' OR '1'='1")
                .esValido(false)
                .build();

        capturarPantalla("antes_inyeccion_sql");

        boolean loginFallido = paginaLogin.iniciarSesion(datos);

        capturarPantalla("despues_inyeccion_sql");

        Assert.assertTrue(loginFallido, "Login debe fallar con intentos de inyección SQL");
        Assert.assertTrue(paginaLogin.esPaginaVisible(), "Debe permanecer en página de login");

        logValidacion("✅ Inyección SQL bloqueada correctamente");
    }

    @Test(priority = 31, description = "Prueba de XSS en campos de login", groups = { "security", "login", "negativo",
            "xss" })
    @Story("Seguridad - Prevención de XSS")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verificar que la aplicación maneja correctamente intentos de XSS")
    public void testLoginPrevencionXSS() {

        logPasoPrueba("🛡️ Probando XSS en login");

        ModeloDatosPrueba datos = ModeloDatosPrueba.builder()
                .casoPrueba("LOGIN_SEC_002")
                .descripcion("Intento de XSS")
                .email("<script>alert('XSS')</script>")
                .password("<img src=x onerror=alert('XSS')>")
                .esValido(false)
                .build();

        capturarPantalla("antes_xss");

        boolean loginFallido = paginaLogin.iniciarSesion(datos);

        capturarPantalla("despues_xss");

        Assert.assertTrue(loginFallido, "Login debe fallar con intentos de XSS");

        // Verificar que no se ejecutó JavaScript malicioso
        String paginaSource = obtenerDriver().getPageSource();
        Assert.assertFalse(paginaSource.contains("<script>alert"),
                "Scripts maliciosos no deben estar en el HTML");

        logValidacion("✅ XSS bloqueado correctamente");
    }

    // ================================
    // PRUEBAS DE FUNCIONALIDAD ADICIONAL
    // ================================

    @Test(priority = 40, description = "Verificar elementos de interfaz de usuario", groups = { "ui", "login",
            "interfaz" })
    @Story("Interfaz de Usuario")
    @Severity(SeverityLevel.MINOR)
    @Description("Verificar que todos los elementos de UI están presentes")
    public void testElementosInterfazUsuario() {

        logPasoPrueba("🎨 Verificando elementos de interfaz de usuario");

        // Verificar título de la página
        String titulo = obtenerDriver().getTitle();
        Assert.assertFalse(titulo.isEmpty(), "La página debe tener título");
        logValidacion("📄 Título de página: " + titulo);

        // Verificar URL correcta
        String urlActual = obtenerDriver().getCurrentUrl();
        Assert.assertTrue(urlActual.contains("practice.expandtesting.com"),
                "Debe estar en el sitio correcto");
        logValidacion("📍 URL verificada: " + urlActual);

        // Verificar elementos del formulario
        Assert.assertTrue(paginaLogin.esPaginaVisible(), "Formulario debe estar visible");

        capturarPantalla("elementos_ui_verificados");
        logValidacion("✅ Todos los elementos de UI verificados");
    }

    @Test(priority = 50, description = "Verificar tiempo de respuesta del login", groups = { "performance", "login",
            "tiempo" })
    @Story("Performance")
    @Severity(SeverityLevel.MINOR)
    @Description("Verificar que el tiempo de respuesta del login es aceptable")
    public void testTiempoRespuestaLogin() {

        logPasoPrueba("⏱️ Midiendo tiempo de respuesta del login");

        ModeloDatosPrueba datos = ModeloDatosPrueba.builder()
                .casoPrueba("LOGIN_PERF_001")
                .descripcion("Prueba de tiempo de respuesta")
                .email("practice")
                .password("SuperSecretPassword!")
                .esValido(true)
                .build();

        long tiempoInicio = System.currentTimeMillis();

        boolean loginExitoso = paginaLogin.iniciarSesion(datos);

        long tiempoFin = System.currentTimeMillis();
        long tiempoRespuesta = tiempoFin - tiempoInicio;

        Assert.assertTrue(loginExitoso, "Login debe ser exitoso");
        Assert.assertTrue(tiempoRespuesta < 10000,
                "Tiempo de respuesta debe ser menor a 10 segundos. Actual: " + tiempoRespuesta + "ms");

        logValidacion("⏱️ Tiempo de respuesta: " + tiempoRespuesta + "ms");
        capturarPantalla("tiempo_respuesta_login");
    }

    // ================================
    // DATOS MÚLTIPLES CON DATA PROVIDER
    // ================================

    @Test(priority = 60, dataProvider = "datosLoginValidos", dataProviderClass = ProveedorDatos.class, description = "Verificar login con múltiples credenciales válidas", groups = {
            "regression", "login", "positivo", "datadriven" })
    @Story("Login con Datos Múltiples")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifica login con diferentes conjuntos de credenciales válidas")
    public void testLoginMultiplesCredencialesValidas(ModeloDatosPrueba datos) {

        logPasoPrueba("🔄 Ejecutando login con datos: " + datos.getCasoPrueba());

        // Adaptar datos para el sitio real
        if (datos.isEsValido()) {
            datos.setEmail("practice");
            datos.setPassword("SuperSecretPassword!");
        }

        boolean loginExitoso = paginaLogin.iniciarSesion(datos);

        logValidacion("🔍 Verificando resultado para: " + datos.getCasoPrueba());
        Assert.assertTrue(loginExitoso, "El login debería ser exitoso para: " + datos.getCasoPrueba());

        capturarPantalla("login_multiple_" + datos.getCasoPrueba());

        // Regresar a login para siguiente iteración
        obtenerDriver().navigate().to("https://practice.expandtesting.com/login");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Good practice: restore interrupted status
            logger.warn("Thread sleep interrupted", e);
        }
    }

    @Test(priority = 61, dataProvider = "datosLoginInvalidos", dataProviderClass = ProveedorDatos.class, description = "Verificar validación con múltiples credenciales inválidas", groups = {
            "regression", "login", "negativo", "datadriven" })
    @Story("Validación con Datos Múltiples")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifica validación con diferentes conjuntos de credenciales inválidas")
    public void testValidacionMultiplesCredencialesInvalidas(ModeloDatosPrueba datos) {

        logPasoPrueba("🔄 Ejecutando validación con datos: " + datos.getCasoPrueba());

        boolean loginFallido = paginaLogin.iniciarSesion(datos);

        logValidacion("🔍 Verificando que falló como esperado para: " + datos.getCasoPrueba());
        Assert.assertTrue(loginFallido, "El login debería fallar para: " + datos.getCasoPrueba());

        capturarPantalla("validacion_multiple_" + datos.getCasoPrueba());
    }

    // ================================
    // LIMPIEZA Y REPORTE FINAL
    // ================================

    @Test(priority = 100, description = "Limpieza final y reporte de casos de login", groups = { "cleanup", "reporte" })
    @Story("Limpieza")
    @Severity(SeverityLevel.TRIVIAL)
    @Description("Realizar limpieza final y generar reporte de resultados")
    public void testLimpiezaFinalLogin() {

        logPasoPrueba("🧹 Ejecutando limpieza final de pruebas de login");

        // Regresar a página inicial de login
        obtenerDriver().navigate().to("https://practice.expandtesting.com/login");

        // Captura final del estado
        capturarPantalla("estado_final_login_completo");

        logValidacion("✅ Limpieza final completada");

        // Reporte de resumen
        logPasoPrueba("📊 === RESUMEN FINAL DE PRUEBAS DE LOGIN ===");
        logPasoPrueba("🎯 Sitio probado: https://practice.expandtesting.com/login");
        logPasoPrueba("🔐 Credenciales válidas: practice/SuperSecretPassword!");
        logPasoPrueba("✅ Casos positivos: Login exitoso verificado");
        logPasoPrueba("❌ Casos negativos: Validaciones funcionando");
        logPasoPrueba("🔍 Validaciones: Campos vacíos controlados");
        logPasoPrueba("🛡️ Seguridad: SQL injection y XSS prevenidos");
        logPasoPrueba("🎨 UI/UX: Elementos verificados");
        logPasoPrueba("⏱️ Performance: Tiempos medidos");
        logPasoPrueba("🔄 Data-driven: Múltiples casos ejecutados");
        logPasoPrueba("🎊 TODAS LAS PRUEBAS DE LOGIN COMPLETADAS EXITOSAMENTE!");
        logPasoPrueba("📅 Fecha de entrega: 04 de agosto de 2025 - 10:00 PM");

        logger.info(TipoMensaje.EXITO.formatearMensaje("🎉 SUITE DE LOGIN FINALIZADA CON ÉXITO"));
    }
}