package com.automatizacion.proyecto.paginas;

import com.automatizacion.proyecto.utilidades.EsperaExplicita;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Página de Login - LOCALIZADORES REALES de practice.expandtesting.com
 * @author Roberto Rivas Lopez
 */
public class PaginaLogin extends PaginaBase {
    
    // *** LOCALIZADORES REALES VERIFICADOS ***
    private final By campoUsuario = By.id("username");
    private final By campoPassword = By.id("password");
    private final By botonLogin = By.tagName("button");  // Solo hay un botón
    private final By mensajeFlash = By.id("flash");
    private final By enlaceRegistro = By.linkText("here");
    
    // Elementos para verificar que la página cargó
    private final By formularioLogin = By.tagName("form");
    private final By contenedorPrincipal = By.className("container");
    
    public PaginaLogin(WebDriver driver, EsperaExplicita espera) {
        super(driver, espera);
    }
    
    public void ingresarUsuario(String usuario) {
        System.out.println("🔤 Ingresando usuario: " + usuario);
        WebElement campo = espera.esperarElementoVisible(campoUsuario);
        campo.clear();
        campo.sendKeys(usuario);
    }
    
    public void ingresarPassword(String password) {
        System.out.println("🔐 Ingresando password: " + "*".repeat(password.length()));
        WebElement campo = espera.esperarElementoVisible(campoPassword);
        campo.clear();
        campo.sendKeys(password);
    }
    
    public void hacerClicLogin() {
        System.out.println("🖱️ Haciendo clic en botón Login");
        WebElement boton = espera.esperarElementoClickeable(botonLogin);
        boton.click();
        
        // Esperar un poco después del clic
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public void realizarLogin(String usuario, String password) {
        System.out.println("\n🚀 === INICIANDO LOGIN ===");
        System.out.println("📄 URL actual: " + driver.getCurrentUrl());
        
        ingresarUsuario(usuario);
        ingresarPassword(password);
        hacerClicLogin();
        
        System.out.println("📄 URL después del login: " + driver.getCurrentUrl());
        System.out.println("✅ === LOGIN COMPLETADO ===\n");
    }
    
    public String obtenerMensajeFlash() {
        try {
            if (esElementoVisible(mensajeFlash)) {
                String mensaje = obtenerTextoElemento(mensajeFlash);
                System.out.println("💬 Mensaje flash encontrado: " + mensaje);
                return mensaje;
            }
        } catch (Exception e) {
            System.out.println("❌ No se encontró mensaje flash");
        }
        return "";
    }
    
    public void irARegistro() {
        if (esElementoVisible(enlaceRegistro)) {
            hacerClicElemento(enlaceRegistro);
        }
    }
    
    @Override
    public boolean esPaginaVisible() {
        try {
            System.out.println("🔍 Verificando visibilidad de página de login...");
            System.out.println("   📄 URL: " + driver.getCurrentUrl());
            System.out.println("   📋 Título: " + driver.getTitle());
            
            // Verificar elementos básicos
            boolean campoUsuarioVisible = esElementoVisible(campoUsuario);
            boolean campoPasswordVisible = esElementoVisible(campoPassword);
            boolean botonVisible = esElementoVisible(botonLogin);
            
            System.out.println("   👤 Campo usuario visible: " + campoUsuarioVisible);
            System.out.println("   🔐 Campo password visible: " + campoPasswordVisible);
            System.out.println("   🔘 Botón visible: " + botonVisible);
            
            boolean paginaVisible = campoUsuarioVisible && campoPasswordVisible && botonVisible;
            System.out.println("   ✅ Página visible: " + paginaVisible);
            
            return paginaVisible;
            
        } catch (Exception e) {
            System.out.println("❌ Error verificando página: " + e.getMessage());
            return false;
        }
    }
    
    public String obtenerTitulo() {
        return driver.getTitle();
    }
    
    public boolean validarElementosPagina() {
        return esPaginaVisible();
    }
    
    public void limpiarFormulario() {
        try {
            if (esElementoVisible(campoUsuario)) {
                driver.findElement(campoUsuario).clear();
            }
            if (esElementoVisible(campoPassword)) {
                driver.findElement(campoPassword).clear();
            }
        } catch (Exception e) {
            System.out.println("Error limpiando formulario: " + e.getMessage());
        }
    }
}
