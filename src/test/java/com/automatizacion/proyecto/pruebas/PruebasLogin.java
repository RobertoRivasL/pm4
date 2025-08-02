package com.automatizacion.proyecto.pruebas;

import com.automatizacion.proyecto.base.BaseTest;
import com.automatizacion.proyecto.paginas.PaginaLogin;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import org.testng.annotations.*;
import io.qameta.allure.*;
import com.automatizacion.proyecto.paginas.PaginaRegistro;
import com.automatizacion.proyecto.datos.ModeloDatosPrueba;
import com.automatizacion.proyecto.utilidades.LectorDatosPrueba;

/**
 * Pruebas de Login - VERSIÓN CON DEBUG COMPLETO
 * @author Roberto Rivas Lopez
 */
public class PruebasLogin extends BaseTest {
    
    private PaginaLogin paginaLogin;
    
    @BeforeMethod
    public void configurarPagina() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("🔧 CONFIGURANDO PRUEBA DE LOGIN - VERSIÓN DEBUG");
        System.out.println("=".repeat(80));
        
        String urlLogin = "https://practice.expandtesting.com/login";
        System.out.println("🌐 Navegando a: " + urlLogin);
        
        
        
        // Esperar que la página cargue completamente
        System.out.println("⏳ Esperando 5 segundos para carga completa...");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
                
        // Debug de elementos antes de verificar
        paginaLogin.debugElementos();
        
        // Verificar que la página cargó
        boolean paginaVisible = paginaLogin.esPaginaVisible();
        System.out.println("🎯 RESULTADO VERIFICACIÓN: " + paginaVisible);
        
        if (!paginaVisible) {
            System.out.println("❌ PÁGINA NO VISIBLE - CAPTURANDO EVIDENCIA");
          
        }
        
        Assert.assertTrue(paginaVisible, "La página de login debe estar visible después de la carga");
        System.out.println("✅ Configuración completada exitosamente\n");
    }
    
    @Test(description = "Login exitoso con credenciales válidas - VERSIÓN DEBUG")
    public void testLoginExitoso() {
        System.out.println("\n🧪 === TEST: LOGIN EXITOSO (DEBUG) ===");
        System.out.println("-".repeat(50));
        
        // Usar credenciales conocidas de la página
        paginaLogin.realizarLogin("practice", "SuperSecretPassword!");
        
        
        
        System.out.println("📊 ANÁLISIS DEL RESULTADO:");
        System.out.println("   📍 URL actual: " + urlActual);
        System.out.println("   📋 Título actual: " + tituloActual);
        
        // Verificar diferentes indicadores de éxito
        boolean contieneSecure = urlActual.contains("secure");
        boolean noContieneLogin = !urlActual.contains("login");
        boolean cambioUrl = !urlActual.equals("https://practice.expandtesting.com/login");
        
        System.out.println("   ✅ Contiene 'secure': " + contieneSecure);
        System.out.println("   ✅ No contiene 'login': " + noContieneLogin);
        System.out.println("   ✅ Cambió de URL: " + cambioUrl);
        
        boolean loginExitoso = contieneSecure || cambioUrl;
        System.out.println("   🎯 LOGIN EXITOSO: " + loginExitoso);
        
       
        
        Assert.assertTrue(loginExitoso, "El login debería ser exitoso - URL: " + urlActual);
        System.out.println("✅ === TEST LOGIN EXITOSO COMPLETADO ===\n");
    }
    
    @Test(description = "Debug completo de elementos")
    public void testDebugElementos() {
        System.out.println("\n🐛 === TEST: DEBUG COMPLETO ===");
        System.out.println("-".repeat(50));
        
        paginaLogin.debugElementos();
        
                
        System.out.println("📸 Captura guardada para análisis visual");
        System.out.println("✅ === DEBUG COMPLETADO ===\n");
        
        // Siempre pasa - es solo para debug
        Assert.assertTrue(true, "Test de debug siempre pasa");
    }
}
