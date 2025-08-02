#!/bin/bash

# =========================================================================
# Inspector Simple - Encontrar Localizadores Reales
# Autor: Roberto Rivas Lopez
# Descripción: Inspector muy simple para ver elementos reales
# =========================================================================

# Colores
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m'

echo -e "${CYAN}========================================${NC}"
echo -e "${CYAN} Inspector Simple de Elementos          ${NC}"
echo -e "${CYAN} practice.expandtesting.com             ${NC}"
echo -e "${CYAN}========================================${NC}"
echo ""

# Verificar directorio
if [[ ! -f "pom.xml" ]]; then
    echo -e "${RED}❌ Error: No se encontró pom.xml${NC}"
    exit 1
fi

# Crear inspector súper simple
mkdir -p src/test/java/com/automatizacion/proyecto/pruebas

cat > src/test/java/com/automatizacion/proyecto/pruebas/InspectorSimple.java << 'EOF'
package com.automatizacion.proyecto.pruebas;

import com.automatizacion.proyecto.base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import java.util.List;

/**
 * Inspector Simple para encontrar elementos reales
 * @author Roberto Rivas Lopez
 */
public class InspectorSimple extends BaseTest {
    
    @Test(description = "Inspeccionar página de login")
    public void inspeccionarPaginaLogin() {
        WebDriver driver = obtenerDriver();
        
        System.out.println("\n" + "=".repeat(80));
        System.out.println("🔍 INSPECTOR SIMPLE - PÁGINA DE LOGIN");
        System.out.println("=".repeat(80));
        
        // Ir a la página
        String url = "https://practice.expandtesting.com/login";
        System.out.println("🌐 Navegando a: " + url);
        driver.get(url);
        
        // Esperar 5 segundos para que cargue completamente
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Información básica
        System.out.println("\n📋 INFORMACIÓN BÁSICA:");
        System.out.println("   URL actual: " + driver.getCurrentUrl());
        System.out.println("   Título: " + driver.getTitle());
        System.out.println("   Tamaño ventana: " + driver.manage().window().getSize());
        
        // Capturar pantalla
        obtenerGestorCaptura().capturarPantalla(driver, "inspector_pagina_completa");
        System.out.println("   📸 Captura guardada: inspector_pagina_completa.png");
        
        // BUSCAR TODOS LOS INPUTS
        System.out.println("\n🔍 TODOS LOS INPUTS EN LA PÁGINA:");
        System.out.println("-".repeat(50));
        
        List<WebElement> inputs = driver.findElements(By.tagName("input"));
        System.out.println("   Total inputs encontrados: " + inputs.size());
        
        for (int i = 0; i < inputs.size(); i++) {
            WebElement input = inputs.get(i);
            try {
                String id = input.getAttribute("id");
                String name = input.getAttribute("name");
                String type = input.getAttribute("type");
                String placeholder = input.getAttribute("placeholder");
                boolean visible = input.isDisplayed();
                boolean enabled = input.isEnabled();
                
                System.out.println(String.format("   Input %d:", i + 1));
                System.out.println("      ID: " + (id != null && !id.isEmpty() ? id : "❌ SIN ID"));
                System.out.println("      Name: " + (name != null && !name.isEmpty() ? name : "❌ SIN NAME"));
                System.out.println("      Type: " + (type != null ? type : "❌ SIN TYPE"));
                System.out.println("      Placeholder: " + (placeholder != null ? placeholder : "❌ SIN PLACEHOLDER"));
                System.out.println("      Visible: " + visible);
                System.out.println("      Enabled: " + enabled);
                
                // Intentar diferentes localizadores
                if (id != null && !id.isEmpty()) {
                    System.out.println("      ✅ LOCALIZADOR: By.id(\"" + id + "\")");
                }
                if (name != null && !name.isEmpty()) {
                    System.out.println("      ✅ LOCALIZADOR: By.name(\"" + name + "\")");
                }
                
                System.out.println();
                
            } catch (Exception e) {
                System.out.println("   ❌ Error inspeccionando input " + (i + 1) + ": " + e.getMessage());
            }
        }
        
        // BUSCAR TODOS LOS BOTONES
        System.out.println("🔘 TODOS LOS BOTONES EN LA PÁGINA:");
        System.out.println("-".repeat(50));
        
        List<WebElement> buttons = driver.findElements(By.tagName("button"));
        System.out.println("   Total botones encontrados: " + buttons.size());
        
        for (int i = 0; i < buttons.size(); i++) {
            WebElement button = buttons.get(i);
            try {
                String id = button.getAttribute("id");
                String type = button.getAttribute("type");
                String className = button.getAttribute("class");
                String texto = button.getText();
                boolean visible = button.isDisplayed();
                
                System.out.println(String.format("   Botón %d:", i + 1));
                System.out.println("      ID: " + (id != null && !id.isEmpty() ? id : "❌ SIN ID"));
                System.out.println("      Type: " + (type != null ? type : "❌ SIN TYPE"));
                System.out.println("      Class: " + (className != null ? className : "❌ SIN CLASS"));
                System.out.println("      Texto: " + (texto != null && !texto.trim().isEmpty() ? "'" + texto.trim() + "'" : "❌ SIN TEXTO"));
                System.out.println("      Visible: " + visible);
                
                // Sugerir localizadores
                if (id != null && !id.isEmpty()) {
                    System.out.println("      ✅ LOCALIZADOR: By.id(\"" + id + "\")");
                }
                if (texto != null && !texto.trim().isEmpty()) {
                    System.out.println("      ✅ LOCALIZADOR: By.xpath(\"//button[text()='" + texto.trim() + "']\")");
                }
                
                System.out.println();
                
            } catch (Exception e) {
                System.out.println("   ❌ Error inspeccionando botón " + (i + 1) + ": " + e.getMessage());
            }
        }
        
        // PROBAR LOCALIZADORES ESPECÍFICOS
        System.out.println("🧪 PROBANDO LOCALIZADORES ESPECÍFICOS:");
        System.out.println("-".repeat(50));
        
        String[] localizadoresAPrueba = {
            "username", "password", "email", "user", "login", "submit"
        };
        
        for (String loc : localizadoresAPrueba) {
            try {
                WebElement elemento = driver.findElement(By.id(loc));
                System.out.println("   ✅ ENCONTRADO By.id(\"" + loc + "\") - Tag: " + elemento.getTagName() + ", Visible: " + elemento.isDisplayed());
            } catch (Exception e) {
                System.out.println("   ❌ NO ENCONTRADO By.id(\"" + loc + "\")");
            }
        }
        
        // BUSCAR FORMS
        System.out.println("\n📝 FORMULARIOS EN LA PÁGINA:");
        System.out.println("-".repeat(50));
        
        List<WebElement> forms = driver.findElements(By.tagName("form"));
        System.out.println("   Total formularios: " + forms.size());
        
        for (int i = 0; i < forms.size(); i++) {
            WebElement form = forms.get(i);
            String id = form.getAttribute("id");
            String action = form.getAttribute("action");
            String method = form.getAttribute("method");
            
            System.out.println("   Form " + (i + 1) + ":");
            System.out.println("      ID: " + (id != null ? id : "Sin ID"));
            System.out.println("      Action: " + (action != null ? action : "Sin action"));
            System.out.println("      Method: " + (method != null ? method : "Sin method"));
        }
        
        System.out.println("\n" + "=".repeat(80));
        System.out.println("✅ INSPECCIÓN COMPLETADA");
        System.out.println("=".repeat(80));
        System.out.println("📸 Revisa la captura: capturas/inspector_pagina_completa.png");
        System.out.println("🔍 Con esta información podremos corregir los localizadores");
        System.out.println("=".repeat(80));
    }
}
EOF

echo -e "${GREEN}✅ Inspector simple creado${NC}"

# Crear testng.xml específico para el inspector
cat > src/test/resources/inspector.xml << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<suite name="Inspector" verbose="1">
    <test name="InspectorSimple">
        <classes>
            <class name="com.automatizacion.proyecto.pruebas.InspectorSimple"/>
        </classes>
    </test>
</suite>
EOF

echo -e "${GREEN}✅ TestNG XML creado para inspector${NC}"
echo ""

echo -e "${YELLOW}🔍 Compilando y ejecutando inspector...${NC}"

if mvn clean compile test-compile -q; then
    echo -e "${GREEN}✅ Compilación exitosa${NC}"
    echo ""
    echo -e "${BLUE}🚀 Ejecutando inspector (esto tomará unos segundos)...${NC}"
    echo ""
    
    # Ejecutar inspector
    mvn test -Dtest=InspectorSimple -q
    
    echo ""
    echo -e "${CYAN}========================================${NC}"
    echo -e "${CYAN} INSPECTOR COMPLETADO                   ${NC}"
    echo -e "${CYAN}========================================${NC}"
    echo ""
    echo -e "${YELLOW}📋 Revisa el output anterior para ver:${NC}"
    echo -e "1. ${GREEN}Todos los inputs con sus IDs reales${NC}"
    echo -e "2. ${GREEN}Todos los botones con sus atributos${NC}"
    echo -e "3. ${GREEN}Localizadores sugeridos${NC}"
    echo -e "4. ${GREEN}Captura de pantalla guardada${NC}"
    echo ""
    echo -e "${YELLOW}📸 Captura guardada en:${NC}"
    echo -e "${BLUE}capturas/inspector_pagina_completa.png${NC}"
    echo ""
    echo -e "${YELLOW}🔧 Con esta información corregiremos PaginaLogin${NC}"
    
else
    echo -e "${RED}❌ Error en compilación${NC}"
    mvn clean compile test-compile
fi

echo ""
echo -e "${CYAN}========================================${NC}"
echo -e "${CYAN} INSPECCIÓN SIMPLE COMPLETADA           ${NC}"
echo -e "${CYAN}========================================${NC}"