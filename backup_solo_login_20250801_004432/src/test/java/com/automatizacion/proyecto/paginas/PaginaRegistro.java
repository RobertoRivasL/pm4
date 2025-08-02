package com.automatizacion.proyecto.paginas;

import com.automatizacion.proyecto.utilidades.EsperaExplicita;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Página de Registro - LOCALIZADORES REALES de practice.expandtesting.com
 * @author Roberto Rivas Lopez
 */
public class PaginaRegistro extends PaginaBase {
    
    // *** LOCALIZADORES REALES VERIFICADOS ***
    private final By campoUsuario = By.id("username");
    private final By campoPassword = By.id("password");
    private final By campoConfirmarPassword = By.id("confirmPassword");
    private final By botonRegistrar = By.tagName("button");  // Solo hay un botón
    private final By mensajeFlash = By.id("flash");
    
    public PaginaRegistro(WebDriver driver, EsperaExplicita espera) {
        super(driver, espera);
    }
    
    public void ingresarUsuario(String usuario) {
        System.out.println("🔤 Ingresando usuario: " + usuario);
        WebElement campo = espera.esperarElementoVisible(campoUsuario);
        campo.clear();
        campo.sendKeys(usuario);
    }
    
    public void ingresarPassword(String password) {
        System.out.println("🔐 Ingresando password");
        WebElement campo = espera.esperarElementoVisible(campoPassword);
        campo.clear();
        campo.sendKeys(password);
    }
    
    public void ingresarConfirmarPassword(String password) {
        System.out.println("🔐 Confirmando password");
        WebElement campo = espera.esperarElementoVisible(campoConfirmarPassword);
        campo.clear();
        campo.sendKeys(password);
    }
    
    public void hacerClicRegistrar() {
        System.out.println("🖱️ Haciendo clic en botón Registrar");
        WebElement boton = espera.esperarElementoClickeable(botonRegistrar);
        boton.click();
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public void realizarRegistro(String usuario, String password) {
        System.out.println("\n🚀 === INICIANDO REGISTRO ===");
        System.out.println("📄 URL actual: " + driver.getCurrentUrl());
        
        ingresarUsuario(usuario);
        ingresarPassword(password);
        ingresarConfirmarPassword(password);
        hacerClicRegistrar();
        
        System.out.println("📄 URL después del registro: " + driver.getCurrentUrl());
        System.out.println("✅ === REGISTRO COMPLETADO ===\n");
    }
    
    public String obtenerMensajeFlash() {
        try {
            if (esElementoVisible(mensajeFlash)) {
                String mensaje = obtenerTextoElemento(mensajeFlash);
                System.out.println("💬 Mensaje flash: " + mensaje);
                return mensaje;
            }
        } catch (Exception e) {
            System.out.println("❌ No se encontró mensaje flash");
        }
        return "";
    }
    
    @Override
    public boolean esPaginaVisible() {
        try {
            System.out.println("🔍 Verificando visibilidad de página de registro...");
            System.out.println("   📄 URL: " + driver.getCurrentUrl());
            
            boolean usuarioVisible = esElementoVisible(campoUsuario);
            boolean passwordVisible = esElementoVisible(campoPassword);
            boolean confirmarVisible = esElementoVisible(campoConfirmarPassword);
            boolean botonVisible = esElementoVisible(botonRegistrar);
            
            System.out.println("   👤 Campo usuario: " + usuarioVisible);
            System.out.println("   🔐 Campo password: " + passwordVisible);
            System.out.println("   🔐 Confirmar password: " + confirmarVisible);
            System.out.println("   🔘 Botón: " + botonVisible);
            
            boolean paginaVisible = usuarioVisible && passwordVisible && confirmarVisible && botonVisible;
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
}
