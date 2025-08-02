package com.automatizacion.proyecto.paginas;

import com.automatizacion.proyecto.utilidades.EsperaExplicita;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Página de Login - LOCALIZADORES BASADOS EN IMÁGENES REALES
 * @author Roberto Rivas Lopez
 */
public class PaginaLogin extends PaginaBase {
    
    // *** LOCALIZADORES BASADOS EN LAS IMÁGENES REALES ***
    // Usando placeholders ya que no se ven IDs en la interfaz
    private final By campoUsuario = By.xpath("//input[@placeholder='Username']");
    private final By campoPassword = By.xpath("//input[@placeholder='Password']");
    private final By botonLogin = By.xpath("//button[contains(text(),'Login')]");
    
    // Elementos adicionales para verificación
    private final By mensajeFlash = By.id("flash");
    private final By contenedorLogin = By.xpath("//div[contains(text(),'Test Login page')]");
    
    public PaginaLogin(WebDriver driver, EsperaExplicita espera) {
        super(driver, espera);
    }
    
    public void ingresarUsuario(String usuario) {
        System.out.println("🔤 Ingresando usuario: " + usuario);
        try {
            WebElement campo = espera.esperarElementoVisible(campoUsuario);
            campo.clear();
            campo.sendKeys(usuario);
            System.out.println("   ✅ Usuario ingresado correctamente");
        } catch (Exception e) {
            System.out.println("   ❌ Error ingresando usuario: " + e.getMessage());
            obtenerGestorCaptura().capturarPantalla(driver, "error_usuario");
            throw e;
        }
    }
    
    public void ingresarPassword(String password) {
        System.out.println("🔐 Ingresando password: " + "*".repeat(password.length()));
        try {
            WebElement campo = espera.esperarElementoVisible(campoPassword);
            campo.clear();
            campo.sendKeys(password);
            System.out.println("   ✅ Password ingresado correctamente");
        } catch (Exception e) {
            System.out.println("   ❌ Error ingresando password: " + e.getMessage());
            obtenerGestorCaptura().capturarPantalla(driver, "error_password");
            throw e;
        }
    }
    
    public void hacerClicLogin() {
        System.out.println("🖱️ Haciendo clic en botón Login");
        try {
            WebElement boton = espera.esperarElementoClickeable(botonLogin);
            System.out.println("   📍 Botón encontrado: " + boton.getText());
            boton.click();
            System.out.println("   ✅ Clic realizado correctamente");
            
            // Esperar respuesta
            Thread.sleep(3000);
            
        } catch (Exception e) {
            System.out.println("   ❌ Error haciendo clic: " + e.getMessage());
            obtenerGestorCaptura().capturarPantalla(driver, "error_click_login");
            throw e;
        }
    }
    
    public void realizarLogin(String usuario, String password) {
        System.out.println("\n🚀 === INICIANDO LOGIN COMPLETO ===");
        System.out.println("📄 URL inicial: " + driver.getCurrentUrl());
        System.out.println("📋 Título inicial: " + driver.getTitle());
        
        // Capturar pantalla inicial
        obtenerGestorCaptura().capturarPantalla(driver, "login_inicio");
        
        try {
            ingresarUsuario(usuario);
            ingresarPassword(password);
            hacerClicLogin();
            
            System.out.println("📄 URL después del login: " + driver.getCurrentUrl());
            System.out.println("📋 Título después del login: " + driver.getTitle());
            
            // Capturar pantalla final
            obtenerGestorCaptura().capturarPantalla(driver, "login_resultado");
            
            System.out.println("✅ === LOGIN PROCESO COMPLETADO ===\n");
            
        } catch (Exception e) {
            System.out.println("❌ === ERROR EN LOGIN ===");
            System.out.println("Error: " + e.getMessage());
            obtenerGestorCaptura().capturarPantalla(driver, "login_error_general");
            throw e;
        }
    }
    
    public String obtenerMensajeFlash() {
        try {
            if (esElementoVisible(mensajeFlash)) {
                String mensaje = obtenerTextoElemento(mensajeFlash);
                System.out.println("💬 Mensaje flash: " + mensaje);
                return mensaje;
            }
        } catch (Exception e) {
            System.out.println("❌ No se encontró mensaje flash: " + e.getMessage());
        }
        return "";
    }
    
    @Override
    public boolean esPaginaVisible() {
        try {
            System.out.println("🔍 === VERIFICANDO VISIBILIDAD DE PÁGINA ===");
            System.out.println("   📄 URL: " + driver.getCurrentUrl());
            System.out.println("   📋 Título: " + driver.getTitle());
            
            // Verificar elementos uno por uno con logging detallado
            System.out.println("   🔍 Buscando campo usuario...");
            boolean usuarioVisible = esElementoVisible(campoUsuario);
            System.out.println("   👤 Campo usuario visible: " + usuarioVisible);
            
            System.out.println("   🔍 Buscando campo password...");
            boolean passwordVisible = esElementoVisible(campoPassword);
            System.out.println("   🔐 Campo password visible: " + passwordVisible);
            
            System.out.println("   🔍 Buscando botón login...");
            boolean botonVisible = esElementoVisible(botonLogin);
            System.out.println("   🔘 Botón login visible: " + botonVisible);
            
            boolean paginaVisible = usuarioVisible && passwordVisible && botonVisible;
            System.out.println("   ✅ PÁGINA VISIBLE: " + paginaVisible);
            
            if (!paginaVisible) {
                System.out.println("   📸 Capturando pantalla para debug...");
                obtenerGestorCaptura().capturarPantalla(driver, "pagina_no_visible_debug");
            }
            
            System.out.println("=== FIN VERIFICACIÓN ===\n");
            return paginaVisible;
            
        } catch (Exception e) {
            System.out.println("❌ Error verificando página: " + e.getMessage());
            obtenerGestorCaptura().capturarPantalla(driver, "error_verificacion");
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
    
    // Método adicional para debug
    public void debugElementos() {
        System.out.println("\n🐛 === DEBUG DE ELEMENTOS ===");
        
        try {
            // Buscar todos los inputs
            java.util.List<WebElement> inputs = driver.findElements(By.tagName("input"));
            System.out.println("📋 Total inputs encontrados: " + inputs.size());
            
            for (int i = 0; i < inputs.size(); i++) {
                WebElement input = inputs.get(i);
                String placeholder = input.getAttribute("placeholder");
                String type = input.getAttribute("type");
                System.out.println("   Input " + (i+1) + ": placeholder='" + placeholder + "', type='" + type + "'");
            }
            
            // Buscar todos los botones
            java.util.List<WebElement> buttons = driver.findElements(By.tagName("button"));
            System.out.println("🔘 Total botones encontrados: " + buttons.size());
            
            for (int i = 0; i < buttons.size(); i++) {
                WebElement button = buttons.get(i);
                String texto = button.getText();
                System.out.println("   Botón " + (i+1) + ": texto='" + texto + "'");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error en debug: " + e.getMessage());
        }
        
        System.out.println("=== FIN DEBUG ===\n");
    }
}
