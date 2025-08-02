package com.automatizacion.proyecto.pruebas;

import com.automatizacion.proyecto.base.BaseTest;
import com.automatizacion.proyecto.paginas.PaginaLogin;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Pruebas de Login - VERSIÓN CORREGIDA
 * @author Roberto Rivas Lopez
 */
public class PruebasLogin extends BaseTest {
    
    private PaginaLogin paginaLogin;
    
    @BeforeMethod
    public void configurarPagina() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🔧 CONFIGURANDO PRUEBA DE LOGIN");
        System.out.println("=".repeat(60));
        
        String urlLogin = "https://practice.expandtesting.com/login";
        System.out.println("🌐 Navegando a: " + urlLogin);
        
        obtenerDriver().get(urlLogin);
        
        // Esperar que la página cargue
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        paginaLogin = new PaginaLogin(obtenerDriver(), obtenerEsperaExplicita());
        
        // Verificar que la página cargó - CON LOGGING DETALLADO
        boolean paginaVisible = paginaLogin.esPaginaVisible();
        System.out.println("✅ Página visible: " + paginaVisible);
        
        if (!paginaVisible) {
            // Capturar pantalla para debug
            obtenerGestorCaptura().capturarPantalla(obtenerDriver(), "error_pagina_no_visible");
            System.out.println("📸 Captura guardada para debug");
        }
        
        Assert.assertTrue(paginaVisible, "La página de login debe estar visible");
        System.out.println("✅ Configuración completada\n");
    }
    
    @Test(description = "Login exitoso con credenciales válidas")
    public void testLoginExitoso() {
        System.out.println("\n🧪 TEST: Login Exitoso");
        System.out.println("-".repeat(30));
        
        // Usar credenciales de la página de práctica
        paginaLogin.realizarLogin("practice", "SuperSecretPassword!");
        
        // Verificar resultado
        String urlActual = obtenerDriver().getCurrentUrl();
        System.out.println("📍 URL después del login: " + urlActual);
        
        // Capturar pantalla del resultado
        obtenerGestorCaptura().capturarPantalla(obtenerDriver(), "login_exitoso");
        
        // Verificar que se redirigió a página segura
        boolean loginExitoso = urlActual.contains("secure") || !urlActual.contains("login");
        System.out.println("✅ Login exitoso: " + loginExitoso);
        
        Assert.assertTrue(loginExitoso, "Debería redirigir a área segura");
    }
    
    @Test(description = "Login con credenciales inválidas")
    public void testLoginCredencialesInvalidas() {
        System.out.println("\n🧪 TEST: Credenciales Inválidas");
        System.out.println("-".repeat(30));
        
        paginaLogin.realizarLogin("usuario_invalido", "password_invalido");
        
        // Verificar mensaje de error
        String mensajeFlash = paginaLogin.obtenerMensajeFlash();
        String urlActual = obtenerDriver().getCurrentUrl();
        
        System.out.println("💬 Mensaje: " + mensajeFlash);
        System.out.println("📍 URL: " + urlActual);
        
        // Capturar pantalla del error
        obtenerGestorCaptura().capturarPantalla(obtenerDriver(), "login_error");
        
        // Verificar que NO se logueó
        boolean seQuedoEnLogin = urlActual.contains("login");
        System.out.println("❌ Se quedó en login: " + seQuedoEnLogin);
        
        Assert.assertTrue(seQuedoEnLogin || !mensajeFlash.isEmpty(), 
            "Debería mostrar error o permanecer en login");
    }
    
    @Test(description = "Campos vacíos")
    public void testCamposVacios() {
        System.out.println("\n🧪 TEST: Campos Vacíos");
        System.out.println("-".repeat(30));
        
        paginaLogin.realizarLogin("", "");
        
        String urlActual = obtenerDriver().getCurrentUrl();
        System.out.println("📍 URL: " + urlActual);
        
        // Capturar pantalla
        obtenerGestorCaptura().capturarPantalla(obtenerDriver(), "campos_vacios");
        
        boolean seQuedoEnLogin = urlActual.contains("login");
        Assert.assertTrue(seQuedoEnLogin, "Debería permanecer en página de login");
    }
    
    @Test(description = "Verificar elementos de la página")
    public void testElementosPagina() {
        System.out.println("\n🧪 TEST: Elementos de Página");
        System.out.println("-".repeat(30));
        
        boolean elementosValidos = paginaLogin.validarElementosPagina();
        String titulo = paginaLogin.obtenerTitulo();
        
        System.out.println("📋 Título: " + titulo);
        System.out.println("✅ Elementos válidos: " + elementosValidos);
        
        // Capturar pantalla de la página
        obtenerGestorCaptura().capturarPantalla(obtenerDriver(), "elementos_pagina");
        
        Assert.assertTrue(elementosValidos, "Todos los elementos deberían estar presentes");
        Assert.assertTrue(titulo.toLowerCase().contains("login"), "Título debería contener 'login'");
    }
}
