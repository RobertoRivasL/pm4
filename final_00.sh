#!/bin/bash

# =========================================================================
# Corrección Final - Localizadores Basados en las Imágenes Reales
# Autor: Roberto Rivas Lopez
# Descripción: Usar los localizadores correctos según las imágenes
# =========================================================================

# Colores
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m'

echo -e "${CYAN}========================================${NC}"
echo -e "${CYAN} Corrección Final - Localizadores Reales${NC}"
echo -e "${CYAN} Basado en imágenes de la página        ${NC}"
echo -e "${CYAN}========================================${NC}"
echo ""

# Verificar directorio
if [[ ! -f "pom.xml" ]]; then
    echo -e "${RED}❌ Error: No se encontró pom.xml${NC}"
    exit 1
fi

# Crear backup
BACKUP_DIR="backup_final_$(date +%Y%m%d_%H%M%S)"
echo -e "${YELLOW}📁 Creando backup...${NC}"
mkdir -p "$BACKUP_DIR"
cp -r src "$BACKUP_DIR/" 2>/dev/null
echo -e "${GREEN}✅ Backup creado en: $BACKUP_DIR${NC}"
echo ""

# Función para crear archivo
crear_archivo() {
    local ruta="$1"
    local contenido="$2"
    
    mkdir -p "$(dirname "$ruta")"
    echo "$contenido" > "$ruta"
    echo -e "${GREEN}✅ Actualizado: $(basename "$ruta")${NC}"
}

echo -e "${BLUE}🔧 Creando PaginaLogin con localizadores REALES...${NC}"

# PaginaLogin.java con localizadores basados en las imágenes
crear_archivo "src/test/java/com/automatizacion/proyecto/paginas/PaginaLogin.java" "package com.automatizacion.proyecto.paginas;

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
    private final By campoUsuario = By.xpath(\"//input[@placeholder='Username']\");
    private final By campoPassword = By.xpath(\"//input[@placeholder='Password']\");
    private final By botonLogin = By.xpath(\"//button[contains(text(),'Login')]\");
    
    // Elementos adicionales para verificación
    private final By mensajeFlash = By.id(\"flash\");
    private final By contenedorLogin = By.xpath(\"//div[contains(text(),'Test Login page')]\");
    
    public PaginaLogin(WebDriver driver, EsperaExplicita espera) {
        super(driver, espera);
    }
    
    public void ingresarUsuario(String usuario) {
        System.out.println(\"🔤 Ingresando usuario: \" + usuario);
        try {
            WebElement campo = espera.esperarElementoVisible(campoUsuario);
            campo.clear();
            campo.sendKeys(usuario);
            System.out.println(\"   ✅ Usuario ingresado correctamente\");
        } catch (Exception e) {
            System.out.println(\"   ❌ Error ingresando usuario: \" + e.getMessage());
            obtenerGestorCaptura().capturarPantalla(driver, \"error_usuario\");
            throw e;
        }
    }
    
    public void ingresarPassword(String password) {
        System.out.println(\"🔐 Ingresando password: \" + \"*\".repeat(password.length()));
        try {
            WebElement campo = espera.esperarElementoVisible(campoPassword);
            campo.clear();
            campo.sendKeys(password);
            System.out.println(\"   ✅ Password ingresado correctamente\");
        } catch (Exception e) {
            System.out.println(\"   ❌ Error ingresando password: \" + e.getMessage());
            obtenerGestorCaptura().capturarPantalla(driver, \"error_password\");
            throw e;
        }
    }
    
    public void hacerClicLogin() {
        System.out.println(\"🖱️ Haciendo clic en botón Login\");
        try {
            WebElement boton = espera.esperarElementoClickeable(botonLogin);
            System.out.println(\"   📍 Botón encontrado: \" + boton.getText());
            boton.click();
            System.out.println(\"   ✅ Clic realizado correctamente\");
            
            // Esperar respuesta
            Thread.sleep(3000);
            
        } catch (Exception e) {
            System.out.println(\"   ❌ Error haciendo clic: \" + e.getMessage());
            obtenerGestorCaptura().capturarPantalla(driver, \"error_click_login\");
            throw e;
        }
    }
    
    public void realizarLogin(String usuario, String password) {
        System.out.println(\"\\n🚀 === INICIANDO LOGIN COMPLETO ===\");
        System.out.println(\"📄 URL inicial: \" + driver.getCurrentUrl());
        System.out.println(\"📋 Título inicial: \" + driver.getTitle());
        
        // Capturar pantalla inicial
        obtenerGestorCaptura().capturarPantalla(driver, \"login_inicio\");
        
        try {
            ingresarUsuario(usuario);
            ingresarPassword(password);
            hacerClicLogin();
            
            System.out.println(\"📄 URL después del login: \" + driver.getCurrentUrl());
            System.out.println(\"📋 Título después del login: \" + driver.getTitle());
            
            // Capturar pantalla final
            obtenerGestorCaptura().capturarPantalla(driver, \"login_resultado\");
            
            System.out.println(\"✅ === LOGIN PROCESO COMPLETADO ===\\n\");
            
        } catch (Exception e) {
            System.out.println(\"❌ === ERROR EN LOGIN ===\");
            System.out.println(\"Error: \" + e.getMessage());
            obtenerGestorCaptura().capturarPantalla(driver, \"login_error_general\");
            throw e;
        }
    }
    
    public String obtenerMensajeFlash() {
        try {
            if (esElementoVisible(mensajeFlash)) {
                String mensaje = obtenerTextoElemento(mensajeFlash);
                System.out.println(\"💬 Mensaje flash: \" + mensaje);
                return mensaje;
            }
        } catch (Exception e) {
            System.out.println(\"❌ No se encontró mensaje flash: \" + e.getMessage());
        }
        return \"\";
    }
    
    @Override
    public boolean esPaginaVisible() {
        try {
            System.out.println(\"🔍 === VERIFICANDO VISIBILIDAD DE PÁGINA ===\");
            System.out.println(\"   📄 URL: \" + driver.getCurrentUrl());
            System.out.println(\"   📋 Título: \" + driver.getTitle());
            
            // Verificar elementos uno por uno con logging detallado
            System.out.println(\"   🔍 Buscando campo usuario...\");
            boolean usuarioVisible = esElementoVisible(campoUsuario);
            System.out.println(\"   👤 Campo usuario visible: \" + usuarioVisible);
            
            System.out.println(\"   🔍 Buscando campo password...\");
            boolean passwordVisible = esElementoVisible(campoPassword);
            System.out.println(\"   🔐 Campo password visible: \" + passwordVisible);
            
            System.out.println(\"   🔍 Buscando botón login...\");
            boolean botonVisible = esElementoVisible(botonLogin);
            System.out.println(\"   🔘 Botón login visible: \" + botonVisible);
            
            boolean paginaVisible = usuarioVisible && passwordVisible && botonVisible;
            System.out.println(\"   ✅ PÁGINA VISIBLE: \" + paginaVisible);
            
            if (!paginaVisible) {
                System.out.println(\"   📸 Capturando pantalla para debug...\");
                obtenerGestorCaptura().capturarPantalla(driver, \"pagina_no_visible_debug\");
            }
            
            System.out.println(\"=== FIN VERIFICACIÓN ===\\n\");
            return paginaVisible;
            
        } catch (Exception e) {
            System.out.println(\"❌ Error verificando página: \" + e.getMessage());
            obtenerGestorCaptura().capturarPantalla(driver, \"error_verificacion\");
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
            System.out.println(\"Error limpiando formulario: \" + e.getMessage());
        }
    }
    
    // Método adicional para debug
    public void debugElementos() {
        System.out.println(\"\\n🐛 === DEBUG DE ELEMENTOS ===\");
        
        try {
            // Buscar todos los inputs
            java.util.List<WebElement> inputs = driver.findElements(By.tagName(\"input\"));
            System.out.println(\"📋 Total inputs encontrados: \" + inputs.size());
            
            for (int i = 0; i < inputs.size(); i++) {
                WebElement input = inputs.get(i);
                String placeholder = input.getAttribute(\"placeholder\");
                String type = input.getAttribute(\"type\");
                System.out.println(\"   Input \" + (i+1) + \": placeholder='\" + placeholder + \"', type='\" + type + \"'\");
            }
            
            // Buscar todos los botones
            java.util.List<WebElement> buttons = driver.findElements(By.tagName(\"button\"));
            System.out.println(\"🔘 Total botones encontrados: \" + buttons.size());
            
            for (int i = 0; i < buttons.size(); i++) {
                WebElement button = buttons.get(i);
                String texto = button.getText();
                System.out.println(\"   Botón \" + (i+1) + \": texto='\" + texto + \"'\");
            }
            
        } catch (Exception e) {
            System.out.println(\"❌ Error en debug: \" + e.getMessage());
        }
        
        System.out.println(\"=== FIN DEBUG ===\\n\");
    }
}"

echo -e "${BLUE}🔧 Actualizando PruebasLogin con debug mejorado...${NC}"

# PruebasLogin.java con debug completo
crear_archivo "src/test/java/com/automatizacion/proyecto/pruebas/PruebasLogin.java" "package com.automatizacion.proyecto.pruebas;

import com.automatizacion.proyecto.base.BaseTest;
import com.automatizacion.proyecto.paginas.PaginaLogin;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Pruebas de Login - VERSIÓN CON DEBUG COMPLETO
 * @author Roberto Rivas Lopez
 */
public class PruebasLogin extends BaseTest {
    
    private PaginaLogin paginaLogin;
    
    @BeforeMethod
    public void configurarPagina() {
        System.out.println(\"\\n\" + \"=\".repeat(80));
        System.out.println(\"🔧 CONFIGURANDO PRUEBA DE LOGIN - VERSIÓN DEBUG\");
        System.out.println(\"=\".repeat(80));
        
        String urlLogin = \"https://practice.expandtesting.com/login\";
        System.out.println(\"🌐 Navegando a: \" + urlLogin);
        
        obtenerDriver().get(urlLogin);
        
        // Esperar que la página cargue completamente
        System.out.println(\"⏳ Esperando 5 segundos para carga completa...\");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        paginaLogin = new PaginaLogin(obtenerDriver(), obtenerEsperaExplicita());
        
        // Debug de elementos antes de verificar
        paginaLogin.debugElementos();
        
        // Verificar que la página cargó
        boolean paginaVisible = paginaLogin.esPaginaVisible();
        System.out.println(\"🎯 RESULTADO VERIFICACIÓN: \" + paginaVisible);
        
        if (!paginaVisible) {
            System.out.println(\"❌ PÁGINA NO VISIBLE - CAPTURANDO EVIDENCIA\");
            obtenerGestorCaptura().capturarPantalla(obtenerDriver(), \"configuracion_error\");
        }
        
        Assert.assertTrue(paginaVisible, \"La página de login debe estar visible después de la carga\");
        System.out.println(\"✅ Configuración completada exitosamente\\n\");
    }
    
    @Test(description = \"Login exitoso con credenciales válidas - VERSIÓN DEBUG\")
    public void testLoginExitoso() {
        System.out.println(\"\\n🧪 === TEST: LOGIN EXITOSO (DEBUG) ===\");
        System.out.println(\"-\".repeat(50));
        
        // Usar credenciales conocidas de la página
        paginaLogin.realizarLogin(\"practice\", \"SuperSecretPassword!\");
        
        // Análisis del resultado
        String urlActual = obtenerDriver().getCurrentUrl();
        String tituloActual = obtenerDriver().getTitle();
        
        System.out.println(\"📊 ANÁLISIS DEL RESULTADO:\");
        System.out.println(\"   📍 URL actual: \" + urlActual);
        System.out.println(\"   📋 Título actual: \" + tituloActual);
        
        // Verificar diferentes indicadores de éxito
        boolean contieneSecure = urlActual.contains(\"secure\");
        boolean noContieneLogin = !urlActual.contains(\"login\");
        boolean cambioUrl = !urlActual.equals(\"https://practice.expandtesting.com/login\");
        
        System.out.println(\"   ✅ Contiene 'secure': \" + contieneSecure);
        System.out.println(\"   ✅ No contiene 'login': \" + noContieneLogin);
        System.out.println(\"   ✅ Cambió de URL: \" + cambioUrl);
        
        boolean loginExitoso = contieneSecure || cambioUrl;
        System.out.println(\"   🎯 LOGIN EXITOSO: \" + loginExitoso);
        
        // Capturar pantalla final
        obtenerGestorCaptura().capturarPantalla(obtenerDriver(), \"test_login_exitoso_final\");
        
        Assert.assertTrue(loginExitoso, \"El login debería ser exitoso - URL: \" + urlActual);
        System.out.println(\"✅ === TEST LOGIN EXITOSO COMPLETADO ===\\n\");
    }
    
    @Test(description = \"Debug completo de elementos\")
    public void testDebugElementos() {
        System.out.println(\"\\n🐛 === TEST: DEBUG COMPLETO ===\");
        System.out.println(\"-\".repeat(50));
        
        paginaLogin.debugElementos();
        
        // Capturar pantalla para análisis visual
        obtenerGestorCaptura().capturarPantalla(obtenerDriver(), \"debug_elementos\");
        
        System.out.println(\"📸 Captura guardada para análisis visual\");
        System.out.println(\"✅ === DEBUG COMPLETADO ===\\n\");
        
        // Siempre pasa - es solo para debug
        Assert.assertTrue(true, \"Test de debug siempre pasa\");
    }
}"

echo ""
echo -e "${YELLOW}🔍 Compilando con localizadores corregidos...${NC}"

if mvn clean compile test-compile -q; then
    echo -e "${GREEN}✅ Compilación exitosa${NC}"
    echo ""
    echo -e "${BLUE}🧪 Ejecutando test de debug...${NC}"
    
    # Ejecutar solo el test de debug primero
    mvn test -Dtest=PruebasLogin#testDebugElementos -q
    
    echo ""
    echo -e "${BLUE}🧪 Ahora ejecutando test de login...${NC}"
    
    # Ejecutar test de login
    mvn test -Dtest=PruebasLogin#testLoginExitoso -q
    
else
    echo -e "${RED}❌ Error en compilación${NC}"
    mvn clean compile test-compile
fi

echo ""
echo -e "${CYAN}========================================${NC}"
echo -e "${CYAN} CORRECCIÓN FINAL APLICADA              ${NC}"
echo -e "${CYAN}========================================${NC}"
echo ""
echo -e "${YELLOW}📋 Cambios realizados:${NC}"
echo -e "1. ${GREEN}Localizadores basados en placeholders${NC}"
echo -e "2. ${GREEN}XPath para elementos sin ID${NC}"
echo -e "3. ${GREEN}Debug completo de elementos${NC}"
echo -e "4. ${GREEN}Capturas en cada paso${NC}"
echo -e "5. ${GREEN}Logging detallado de todo el proceso${NC}"
echo ""
echo -e "${YELLOW}📸 Revisa las capturas en: capturas/${NC}"
echo -e "${YELLOW}🔍 El debug te mostrará los elementos reales${NC}"