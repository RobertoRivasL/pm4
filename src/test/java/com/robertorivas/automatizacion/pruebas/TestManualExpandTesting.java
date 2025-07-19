package com.robertorivas.automatizacion.pruebas;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Test manual para diagnosticar exactamente qué está pasando con ExpandTesting.
 * Este test NO usa Page Objects, solo WebDriver directo para debugging.
 * 
 * @author Roberto Rivas Lopez
 */
public class TestManualExpandTesting extends PruebasBase {
    
    @Test(description = "Diagnóstico manual de ExpandTesting - Paso a paso",
          groups = {"debug", "manual"})
    public void diagnosticoManualExpandTesting() {
        registrarSeparador("DIAGNÓSTICO MANUAL - EXPANDTESTING");
        
        try {
            // Paso 1: Ir directamente a la página
            registrarInfo("=== PASO 1: NAVEGACIÓN DIRECTA ===");
            String urlLogin = "https://practice.expandtesting.com/login";
            registrarInfo("Navegando a: " + urlLogin);
            
            obtenerDriver().get(urlLogin);
            pausar(3000); // Dar tiempo para cargar
            
            String urlActual = obtenerDriver().getCurrentUrl();
            registrarInfo("URL actual después de navegación: " + urlActual);
            
            String titulo = obtenerDriver().getTitle();
            registrarInfo("Título de la página: " + titulo);
            
            tomarCapturaPaso("paso1_pagina_cargada");
            
            // Paso 2: Examinar el HTML de la página
            registrarInfo("=== PASO 2: ANÁLISIS DEL HTML ===");
            
            // Obtener todo el HTML para debug
            String htmlCompleto = obtenerDriver().getPageSource();
            registrarInfo("Tamaño del HTML: " + htmlCompleto.length() + " caracteres");
            
            // Buscar formularios
            List<WebElement> formularios = obtenerDriver().findElements(By.tagName("form"));
            registrarInfo("Formularios encontrados: " + formularios.size());
            
            // Buscar todos los inputs
            List<WebElement> inputs = obtenerDriver().findElements(By.tagName("input"));
            registrarInfo("Inputs encontrados: " + inputs.size());
            
            for (int i = 0; i < inputs.size(); i++) {
                WebElement input = inputs.get(i);
                String tipo = input.getAttribute("type");
                String nombre = input.getAttribute("name");
                String id = input.getAttribute("id");
                String placeholder = input.getAttribute("placeholder");
                registrarInfo(String.format("Input %d: type='%s', name='%s', id='%s', placeholder='%s'", 
                    i+1, tipo, nombre, id, placeholder));
            }
            
            // Buscar todos los botones
            List<WebElement> botones = obtenerDriver().findElements(By.tagName("button"));
            registrarInfo("Botones encontrados: " + botones.size());
            
            for (int i = 0; i < botones.size(); i++) {
                WebElement boton = botones.get(i);
                String tipo = boton.getAttribute("type");
                String texto = boton.getText();
                String clase = boton.getAttribute("class");
                registrarInfo(String.format("Botón %d: type='%s', text='%s', class='%s'", 
                    i+1, tipo, texto, clase));
            }
            
            // Paso 3: Probar selectores específicos
            registrarInfo("=== PASO 3: PROBAR SELECTORES ESPECÍFICOS ===");
            
            // Probar diferentes selectores para username
            String[] selectoresUsername = {
                "input[name='username']",
                "#username",
                "input[type='text']",
                "input[placeholder*='username']",
                "input[placeholder*='Username']",
                "input[id*='user']",
                "input[name*='user']"
            };
            
            WebElement campoUsername = null;
            for (String selector : selectoresUsername) {
                try {
                    List<WebElement> elementos = obtenerDriver().findElements(By.cssSelector(selector));
                    if (!elementos.isEmpty()) {
                        campoUsername = elementos.get(0);
                        registrarInfo("✅ Selector USERNAME funcionó: " + selector);
                        registrarInfo("   - Visible: " + campoUsername.isDisplayed());
                        registrarInfo("   - Habilitado: " + campoUsername.isEnabled());
                        break;
                    }
                } catch (Exception e) {
                    registrarInfo("❌ Selector USERNAME falló: " + selector + " - " + e.getMessage());
                }
            }
            
            // Probar diferentes selectores para password
            String[] selectoresPassword = {
                "input[name='password']",
                "#password",
                "input[type='password']",
                "input[placeholder*='password']",
                "input[placeholder*='Password']",
                "input[id*='pass']",
                "input[name*='pass']"
            };
            
            WebElement campoPassword = null;
            for (String selector : selectoresPassword) {
                try {
                    List<WebElement> elementos = obtenerDriver().findElements(By.cssSelector(selector));
                    if (!elementos.isEmpty()) {
                        campoPassword = elementos.get(0);
                        registrarInfo("✅ Selector PASSWORD funcionó: " + selector);
                        registrarInfo("   - Visible: " + campoPassword.isDisplayed());
                        registrarInfo("   - Habilitado: " + campoPassword.isEnabled());
                        break;
                    }
                } catch (Exception e) {
                    registrarInfo("❌ Selector PASSWORD falló: " + selector + " - " + e.getMessage());
                }
            }
            
            // Probar diferentes selectores para submit button
            String[] selectoresSubmit = {
                "button[type='submit']",
                "input[type='submit']",
                ".btn-primary",
                ".btn",
                "button:contains('Login')",
                "button:contains('Submit')",
                "[value='Login']"
            };
            
            WebElement botonSubmit = null;
            for (String selector : selectoresSubmit) {
                try {
                    List<WebElement> elementos = obtenerDriver().findElements(By.cssSelector(selector));
                    if (!elementos.isEmpty()) {
                        botonSubmit = elementos.get(0);
                        registrarInfo("✅ Selector SUBMIT funcionó: " + selector);
                        registrarInfo("   - Visible: " + botonSubmit.isDisplayed());
                        registrarInfo("   - Habilitado: " + botonSubmit.isEnabled());
                        registrarInfo("   - Texto: " + botonSubmit.getText());
                        break;
                    }
                } catch (Exception e) {
                    registrarInfo("❌ Selector SUBMIT falló: " + selector + " - " + e.getMessage());
                }
            }
            
            // Paso 4: Intentar login manual si encontramos los elementos
            if (campoUsername != null && campoPassword != null && botonSubmit != null) {
                registrarInfo("=== PASO 4: INTENTO DE LOGIN MANUAL ===");
                
                registrarInfo("Limpiando campo username...");
                campoUsername.clear();
                
                registrarInfo("Introduciendo username: practice");
                campoUsername.sendKeys("practice");
                
                registrarInfo("Limpiando campo password...");
                campoPassword.clear();
                
                registrarInfo("Introduciendo password: SuperSecretPassword!");
                campoPassword.sendKeys("SuperSecretPassword!");
                
                tomarCapturaPaso("paso4_antes_submit");
                
                registrarInfo("Haciendo clic en submit...");
                botonSubmit.click();
                
                // Esperar respuesta
                pausar(5000);
                
                String urlDespuesSubmit = obtenerDriver().getCurrentUrl();
                registrarInfo("URL después del submit: " + urlDespuesSubmit);
                
                tomarCapturaPaso("paso4_despues_submit");
                
                // Verificar si hay mensajes flash
                try {
                    WebElement flash = obtenerDriver().findElement(By.cssSelector("#flash"));
                    if (flash.isDisplayed()) {
                        String mensajeFlash = flash.getText();
                        registrarInfo("Mensaje flash encontrado: " + mensajeFlash);
                        
                        if (mensajeFlash.contains("You logged into a secure area!")) {
                            registrarPasoExitoso("¡LOGIN EXITOSO!");
                        } else {
                            registrarPasoFallido("Login falló. Mensaje: " + mensajeFlash, null);
                        }
                    }
                } catch (Exception e) {
                    registrarInfo("No se encontró mensaje flash: " + e.getMessage());
                }
                
                // Verificar si estamos en página segura
                if (urlDespuesSubmit.contains("/secure")) {
                    registrarPasoExitoso("¡Redirección a página segura exitosa!");
                } else {
                    registrarPasoFallido("No se redirigió a página segura", null);
                }
                
            } else {
                registrarPasoFallido("No se pudieron encontrar todos los elementos necesarios:", null);
                registrarInfo("- Username field: " + (campoUsername != null ? "✅" : "❌"));
                registrarInfo("- Password field: " + (campoPassword != null ? "✅" : "❌"));
                registrarInfo("- Submit button: " + (botonSubmit != null ? "✅" : "❌"));
            }
            
            // Paso 5: Examinar estructura DOM completa
            registrarInfo("=== PASO 5: ESTRUCTURA DOM COMPLETA ===");
            
            // Buscar por texto "Username" o "Password"
            String bodyText = obtenerDriver().findElement(By.tagName("body")).getText();
            registrarInfo("Texto de la página contiene 'Username': " + bodyText.contains("Username"));
            registrarInfo("Texto de la página contiene 'Password': " + bodyText.contains("Password"));
            registrarInfo("Texto de la página contiene 'Login': " + bodyText.contains("Login"));
            
            // Buscar elementos por texto
            try {
                WebElement loginHeading = obtenerDriver().findElement(By.xpath("//*[contains(text(), 'Login')]"));
                registrarInfo("Elemento con texto 'Login' encontrado: " + loginHeading.getTagName());
            } catch (Exception e) {
                registrarInfo("No se encontró elemento con texto 'Login'");
            }
            
            // Mostrar primeras líneas del HTML para análisis
            String htmlSnippet = htmlCompleto.length() > 1000 ? 
                htmlCompleto.substring(0, 1000) + "..." : htmlCompleto;
            registrarInfo("HTML snippet (primeros 1000 caracteres):");
            registrarInfo(htmlSnippet);
            
        } catch (Exception e) {
            registrarPasoFallido("Error durante diagnóstico manual", e);
            e.printStackTrace();
        }
    }
    
    @Test(description = "Test ultra básico - Solo abrir página",
          groups = {"debug", "basic"})
    public void testUltraBasico() {
        registrarSeparador("TEST ULTRA BÁSICO - SOLO ABRIR PÁGINA");
        
        try {
            registrarInfo("Abriendo página de ExpandTesting...");
            obtenerDriver().get("https://practice.expandtesting.com/login");
            
            pausar(2000);
            
            String url = obtenerDriver().getCurrentUrl();
            String titulo = obtenerDriver().getTitle();
            
            registrarInfo("URL: " + url);
            registrarInfo("Título: " + titulo);
            
            boolean paginaCargada = url.contains("expandtesting.com");
            
            if (paginaCargada) {
                registrarPasoExitoso("✅ Página de ExpandTesting cargada correctamente");
            } else {
                registrarPasoFallido("❌ Problema cargando página de ExpandTesting", null);
            }
            
            tomarCapturaPaso("ultra_basico_pagina");
            
        } catch (Exception e) {
            registrarPasoFallido("Error en test ultra básico", e);
        }
    }
}