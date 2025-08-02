package com.automatizacion.proyecto.pruebas;

import com.automatizacion.proyecto.base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import java.util.List;

/**
 * Inspector para encontrar los localizadores reales de la página
 * @author Roberto Rivas Lopez
 */
public class InspectorPagina extends BaseTest {
    
    @Test(description = "Inspeccionar página de login")
    public void inspeccionarLogin() {
        WebDriver driver = obtenerDriver();
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println("🔍 INSPECCIONANDO PÁGINA DE LOGIN");
        System.out.println("=".repeat(50) + "\n");
        
        // Ir a la página de login
        driver.get("https://practice.expandtesting.com/login");
        
        // Esperar un poco para que cargue
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Información básica
        System.out.println("📄 URL actual: " + driver.getCurrentUrl());
        System.out.println("📋 Título: " + driver.getTitle());
        System.out.println("📏 Tamaño de ventana: " + driver.manage().window().getSize());
        System.out.println();
        
        // Buscar campos de entrada (input)
        System.out.println("🔎 CAMPOS DE ENTRADA:");
        System.out.println("-".repeat(30));
        
        List<WebElement> inputs = driver.findElements(By.tagName("input"));
        for (int i = 0; i < inputs.size(); i++) {
            WebElement input = inputs.get(i);
            String id = input.getAttribute("id");
            String name = input.getAttribute("name");
            String type = input.getAttribute("type");
            String placeholder = input.getAttribute("placeholder");
            String className = input.getAttribute("class");
            
            System.out.println(String.format("Input %d:", i + 1));
            System.out.println("  ID: " + (id != null ? id : "(sin id)"));
            System.out.println("  Name: " + (name != null ? name : "(sin name)"));
            System.out.println("  Type: " + (type != null ? type : "(sin type)"));
            System.out.println("  Placeholder: " + (placeholder != null ? placeholder : "(sin placeholder)"));
            System.out.println("  Class: " + (className != null ? className : "(sin class)"));
            System.out.println("  Visible: " + input.isDisplayed());
            System.out.println("  Habilitado: " + input.isEnabled());
            System.out.println();
        }
        
        // Buscar botones
        System.out.println("🔘 BOTONES:");
        System.out.println("-".repeat(30));
        
        List<WebElement> buttons = driver.findElements(By.tagName("button"));
        for (int i = 0; i < buttons.size(); i++) {
            WebElement button = buttons.get(i);
            String id = button.getAttribute("id");
            String type = button.getAttribute("type");
            String className = button.getAttribute("class");
            String dataTest = button.getAttribute("data-test");
            String texto = button.getText();
            
            System.out.println(String.format("Botón %d:", i + 1));
            System.out.println("  ID: " + (id != null ? id : "(sin id)"));
            System.out.println("  Type: " + (type != null ? type : "(sin type)"));
            System.out.println("  Class: " + (className != null ? className : "(sin class)"));
            System.out.println("  Data-test: " + (dataTest != null ? dataTest : "(sin data-test)"));
            System.out.println("  Texto: " + (texto != null && !texto.isEmpty() ? texto : "(sin texto)"));
            System.out.println("  Visible: " + button.isDisplayed());
            System.out.println();
        }
        
        // Buscar enlaces
        System.out.println("🔗 ENLACES:");
        System.out.println("-".repeat(30));
        
        List<WebElement> links = driver.findElements(By.tagName("a"));
        for (int i = 0; i < links.size(); i++) {
            WebElement link = links.get(i);
            String href = link.getAttribute("href");
            String texto = link.getText();
            String id = link.getAttribute("id");
            
            if (href != null && !href.isEmpty() && texto != null && !texto.trim().isEmpty()) {
                System.out.println(String.format("Enlace %d:", i + 1));
                System.out.println("  Texto: " + texto.trim());
                System.out.println("  Href: " + href);
                System.out.println("  ID: " + (id != null ? id : "(sin id)"));
                System.out.println();
            }
        }
        
        // Buscar elementos con IDs importantes
        System.out.println("🆔 ELEMENTOS CON IDs RELEVANTES:");
        System.out.println("-".repeat(30));
        
        String[] idsImportantes = {"username", "password", "email", "login", "submit", "flash", "error", "message"};
        
        for (String id : idsImportantes) {
            try {
                WebElement elemento = driver.findElement(By.id(id));
                System.out.println("✅ Encontrado ID '" + id + "':");
                System.out.println("   Tag: " + elemento.getTagName());
                System.out.println("   Texto: " + elemento.getText());
                System.out.println("   Visible: " + elemento.isDisplayed());
                System.out.println();
            } catch (Exception e) {
                System.out.println("❌ No encontrado ID '" + id + "'");
            }
        }
        
        // Tomar captura para revisión manual
        obtenerGestorCaptura().capturarPantalla(driver, "inspeccion_login");
        System.out.println("📸 Captura guardada como: inspeccion_login.png");
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println("✅ INSPECCIÓN DE LOGIN COMPLETADA");
        System.out.println("=".repeat(50) + "\n");
    }
    
    @Test(description = "Inspeccionar página de registro", dependsOnMethods = "inspeccionarLogin")
    public void inspeccionarRegistro() {
        WebDriver driver = obtenerDriver();
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println("🔍 INSPECCIONANDO PÁGINA DE REGISTRO");
        System.out.println("=".repeat(50) + "\n");
        
        // Ir a la página de registro
        driver.get("https://practice.expandtesting.com/register");
        
        // Esperar un poco para que cargue
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Información básica
        System.out.println("📄 URL actual: " + driver.getCurrentUrl());
        System.out.println("📋 Título: " + driver.getTitle());
        System.out.println();
        
        // Buscar campos de entrada
        System.out.println("🔎 CAMPOS DE ENTRADA EN REGISTRO:");
        System.out.println("-".repeat(30));
        
        List<WebElement> inputs = driver.findElements(By.tagName("input"));
        for (int i = 0; i < inputs.size(); i++) {
            WebElement input = inputs.get(i);
            String id = input.getAttribute("id");
            String name = input.getAttribute("name");
            String type = input.getAttribute("type");
            String placeholder = input.getAttribute("placeholder");
            
            System.out.println(String.format("Input %d:", i + 1));
            System.out.println("  ID: " + (id != null ? id : "(sin id)"));
            System.out.println("  Name: " + (name != null ? name : "(sin name)"));
            System.out.println("  Type: " + (type != null ? type : "(sin type)"));
            System.out.println("  Placeholder: " + (placeholder != null ? placeholder : "(sin placeholder)"));
            System.out.println("  Visible: " + input.isDisplayed());
            System.out.println();
        }
        
        // Buscar botones en registro
        System.out.println("🔘 BOTONES EN REGISTRO:");
        System.out.println("-".repeat(30));
        
        List<WebElement> buttons = driver.findElements(By.tagName("button"));
        for (int i = 0; i < buttons.size(); i++) {
            WebElement button = buttons.get(i);
            String id = button.getAttribute("id");
            String dataTest = button.getAttribute("data-test");
            String texto = button.getText();
            
            System.out.println(String.format("Botón %d:", i + 1));
            System.out.println("  ID: " + (id != null ? id : "(sin id)"));
            System.out.println("  Data-test: " + (dataTest != null ? dataTest : "(sin data-test)"));
            System.out.println("  Texto: " + (texto != null ? texto : "(sin texto)"));
            System.out.println("  Visible: " + button.isDisplayed());
            System.out.println();
        }
        
        // Tomar captura de registro
        obtenerGestorCaptura().capturarPantalla(driver, "inspeccion_registro");
        System.out.println("📸 Captura guardada como: inspeccion_registro.png");
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println("✅ INSPECCIÓN DE REGISTRO COMPLETADA");
        System.out.println("=".repeat(50) + "\n");
    }
}
