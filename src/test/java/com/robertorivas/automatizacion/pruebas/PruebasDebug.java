package com.robertorivas.automatizacion.pruebas;

import com.robertorivas.automatizacion.modelos.Usuario;
import com.robertorivas.automatizacion.paginas.PaginaLogin;
import org.testng.annotations.Test;

/**
 * Clase de pruebas de depuración para verificar la conectividad con ExpandTesting.
 * 
 * @author Roberto Rivas Lopez
 */
public class PruebasDebug extends PruebasBase {
    
    @Test(description = "Debug: Verificar página de login de ExpandTesting",
          groups = {"debug", "smoke"})
    public void debugVerificarPaginaLogin() {
        registrarSeparador("DEBUG - VERIFICACIÓN PÁGINA LOGIN");
        
        // Crear página de login
        PaginaLogin paginaLogin = new PaginaLogin(obtenerDriver());
        
        // Navegar a la página
        registrarInfo("Navegando a ExpandTesting login...");
        paginaLogin.navegarAPaginaLogin();
        tomarCapturaPaso("pagina_login_cargada");
        
        // Verificar URL actual
        String urlActual = obtenerDriver().getCurrentUrl();
        registrarInfo("URL actual: " + urlActual);
        
        // Verificar título de la página
        String titulo = obtenerDriver().getTitle();
        registrarInfo("Título de la página: " + titulo);
        
        // Verificar que la página esté cargada
        boolean paginaCargada = paginaLogin.estaPaginaCargada();
        registrarInfo("Página cargada correctamente: " + paginaCargada);
        
        // Verificar formulario disponible
        boolean formularioDisponible = paginaLogin.formularioLoginDisponible();
        registrarInfo("Formulario de login disponible: " + formularioDisponible);
        
        // Loggear información detallada
        paginaLogin.logearInformacionLogin();
        
        // Verificar elementos específicos
        verificarElementosExpandTesting();
    }
    
    @Test(description = "Debug: Test de login paso a paso",
          groups = {"debug"},
          dependsOnMethods = {"debugVerificarPaginaLogin"})
    public void debugLoginPasoAPaso() {
        registrarSeparador("DEBUG - LOGIN PASO A PASO");
        
        PaginaLogin paginaLogin = new PaginaLogin(obtenerDriver());
        
        // Paso 1: Navegar
        registrarInfo("=== PASO 1: NAVEGACIÓN ===");
        paginaLogin.navegarAPaginaLogin();
        tomarCapturaPaso("paso1_navegacion");
        
        // Paso 2: Verificar página
        registrarInfo("=== PASO 2: VERIFICACIÓN DE PÁGINA ===");
        boolean paginaCargada = paginaLogin.estaPaginaCargada();
        registrarInfo("Página cargada: " + paginaCargada);
        
        if (!paginaCargada) {
            registrarPasoFallido("La página no se cargó correctamente", null);
            return;
        }
        
        // Paso 3: Limpiar campos
        registrarInfo("=== PASO 3: LIMPIAR CAMPOS ===");
        paginaLogin.limpiarCamposLogin();
        tomarCapturaPaso("paso3_campos_limpios");
        
        // Paso 4: Introducir username
        registrarInfo("=== PASO 4: INTRODUCIR USERNAME ===");
        paginaLogin.introducirUsername("practice");
        tomarCapturaPaso("paso4_username_introducido");
        
        // Verificar que se introdujo correctamente
        String[] valores = paginaLogin.obtenerValoresFormulario();
        registrarInfo("Username actual en el campo: '" + valores[0] + "'");
        
        // Paso 5: Introducir password
        registrarInfo("=== PASO 5: INTRODUCIR PASSWORD ===");
        paginaLogin.introducirPassword("SuperSecretPassword!");
        tomarCapturaPaso("paso5_password_introducido");
        
        // Verificar password (no podemos ver el valor por seguridad)
        valores = paginaLogin.obtenerValoresFormulario();
        registrarInfo("Password field exists: " + (valores[1] != null));
        
        // Paso 6: Hacer clic en login
        registrarInfo("=== PASO 6: HACER CLIC EN LOGIN ===");
        paginaLogin.hacerClicLogin();
        tomarCapturaPaso("paso6_despues_clic_login");
        
        // Esperar un momento para que la página responda
        pausar(3000);
        
        // Paso 7: Verificar resultado
        registrarInfo("=== PASO 7: VERIFICAR RESULTADO ===");
        String urlDespuesLogin = obtenerDriver().getCurrentUrl();
        registrarInfo("URL después del login: " + urlDespuesLogin);
        
        boolean loginExitoso = paginaLogin.verificarLoginExitoso();
        registrarInfo("Login exitoso: " + loginExitoso);
        
        boolean hayErrores = paginaLogin.hayErroresLogin();
        registrarInfo("Hay errores: " + hayErrores);
        
        if (hayErrores) {
            registrarInfo("Errores encontrados: " + paginaLogin.obtenerErroresLogin());
        }
        
        tomarCapturaPaso("paso7_resultado_final");
        
        // Información final
        paginaLogin.logearInformacionLogin();
    }
    
    @Test(description = "Debug: Verificar credenciales específicas",
          groups = {"debug"})
    public void debugVerificarCredenciales() {
        registrarSeparador("DEBUG - VERIFICAR CREDENCIALES ESPECÍFICAS");
        
        PaginaLogin paginaLogin = new PaginaLogin(obtenerDriver());
        
        // Test 1: Credenciales correctas de ExpandTesting
        registrarInfo("=== TEST 1: CREDENCIALES EXPANDTESTING ===");
        paginaLogin.navegarAPaginaLogin();
        
        boolean loginExitoso1 = paginaLogin.iniciarSesion("practice", "SuperSecretPassword!");
        registrarInfo("Login con practice/SuperSecretPassword!: " + loginExitoso1);
        tomarCapturaPaso("test1_expandtesting_credentials");
        
        if (!loginExitoso1) {
            registrarInfo("Errores: " + paginaLogin.obtenerErroresLogin());
        }
        
        // Volver a login para siguiente test
        paginaLogin.navegarAPaginaLogin();
        
        // Test 2: Credenciales incorrectas (las que estabas usando antes)
        registrarInfo("=== TEST 2: CREDENCIALES INCORRECTAS ===");
        boolean loginExitoso2 = paginaLogin.iniciarSesion("student", "Password123");
        registrarInfo("Login con student/Password123: " + loginExitoso2);
        tomarCapturaPaso("test2_wrong_credentials");
        
        if (!loginExitoso2) {
            registrarInfo("Errores (esperados): " + paginaLogin.obtenerErroresLogin());
        }
    }
    
    /**
     * Verifica elementos específicos de ExpandTesting
     */
    private void verificarElementosExpandTesting() {
        registrarInfo("=== VERIFICACIÓN DE ELEMENTOS ESPECÍFICOS ===");
        
        try {
            // Verificar campo username
            boolean campoUsername = obtenerDriver().findElement(
                org.openqa.selenium.By.cssSelector("input[name='username']")
            ).isDisplayed();
            registrarInfo("Campo username visible: " + campoUsername);
        } catch (Exception e) {
            registrarInfo("Campo username NO encontrado: " + e.getMessage());
        }
        
        try {
            // Verificar campo password
            boolean campoPassword = obtenerDriver().findElement(
                org.openqa.selenium.By.cssSelector("input[name='password']")
            ).isDisplayed();
            registrarInfo("Campo password visible: " + campoPassword);
        } catch (Exception e) {
            registrarInfo("Campo password NO encontrado: " + e.getMessage());
        }
        
        try {
            // Verificar botón submit
            boolean botonSubmit = obtenerDriver().findElement(
                org.openqa.selenium.By.cssSelector("button[type='submit']")
            ).isDisplayed();
            registrarInfo("Botón submit visible: " + botonSubmit);
        } catch (Exception e) {
            registrarInfo("Botón submit NO encontrado: " + e.getMessage());
        }
        
        // Verificar mensaje flash (para errores/éxito)
        try {
            boolean mensajeFlash = obtenerDriver().findElement(
                org.openqa.selenium.By.cssSelector("#flash")
            ).isDisplayed();
            registrarInfo("Mensaje flash visible: " + mensajeFlash);
        } catch (Exception e) {
            registrarInfo("Mensaje flash NO encontrado (normal si no hay mensajes)");
        }
    }
}