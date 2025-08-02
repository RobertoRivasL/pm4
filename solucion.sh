#!/bin/bash

# =========================================================================
# Solución Inmediata - Corrección de Localizadores
# Autor: Roberto Rivas Lopez
# Descripción: Corrige los localizadores basándose en la página real
# =========================================================================

# Colores
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m'

echo -e "${CYAN}========================================${NC}"
echo -e "${CYAN} Solución Inmediata - Localizadores     ${NC}"
echo -e "${CYAN} Basado en inspección manual de página  ${NC}"
echo -e "${CYAN}========================================${NC}"
echo ""

# Verificar directorio
if [[ ! -f "pom.xml" ]]; then
    echo -e "${RED}❌ Error: No se encontró pom.xml${NC}"
    exit 1
fi

# Crear backup
BACKUP_DIR="backup_solucion_$(date +%Y%m%d_%H%M%S)"
echo -e "${YELLOW}📁 Creando backup...${NC}"
mkdir -p "$BACKUP_DIR"
cp -r src "$BACKUP_DIR/"
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

echo -e "${BLUE}🔧 Corrigiendo PaginaLogin con localizadores reales...${NC}"

# BASADO EN LA PÁGINA REAL DE practice.expandtesting.com/login
crear_archivo "src/test/java/com/automatizacion/proyecto/paginas/PaginaLogin.java" "package com.automatizacion.proyecto.paginas;

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
    private final By campoUsuario = By.id(\"username\");
    private final By campoPassword = By.id(\"password\");
    private final By botonLogin = By.tagName(\"button\");  // Solo hay un botón
    private final By mensajeFlash = By.id(\"flash\");
    private final By enlaceRegistro = By.linkText(\"here\");
    
    // Elementos para verificar que la página cargó
    private final By formularioLogin = By.tagName(\"form\");
    private final By contenedorPrincipal = By.className(\"container\");
    
    public PaginaLogin(WebDriver driver, EsperaExplicita espera) {
        super(driver, espera);
    }
    
    public void ingresarUsuario(String usuario) {
        System.out.println(\"🔤 Ingresando usuario: \" + usuario);
        WebElement campo = espera.esperarElementoVisible(campoUsuario);
        campo.clear();
        campo.sendKeys(usuario);
    }
    
    public void ingresarPassword(String password) {
        System.out.println(\"🔐 Ingresando password: \" + \"*\".repeat(password.length()));
        WebElement campo = espera.esperarElementoVisible(campoPassword);
        campo.clear();
        campo.sendKeys(password);
    }
    
    public void hacerClicLogin() {
        System.out.println(\"🖱️ Haciendo clic en botón Login\");
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
        System.out.println(\"\\n🚀 === INICIANDO LOGIN ===\");
        System.out.println(\"📄 URL actual: \" + driver.getCurrentUrl());
        
        ingresarUsuario(usuario);
        ingresarPassword(password);
        hacerClicLogin();
        
        System.out.println(\"📄 URL después del login: \" + driver.getCurrentUrl());
        System.out.println(\"✅ === LOGIN COMPLETADO ===\\n\");
    }
    
    public String obtenerMensajeFlash() {
        try {
            if (esElementoVisible(mensajeFlash)) {
                String mensaje = obtenerTextoElemento(mensajeFlash);
                System.out.println(\"💬 Mensaje flash encontrado: \" + mensaje);
                return mensaje;
            }
        } catch (Exception e) {
            System.out.println(\"❌ No se encontró mensaje flash\");
        }
        return \"\";
    }
    
    public void irARegistro() {
        if (esElementoVisible(enlaceRegistro)) {
            hacerClicElemento(enlaceRegistro);
        }
    }
    
    @Override
    public boolean esPaginaVisible() {
        try {
            System.out.println(\"🔍 Verificando visibilidad de página de login...\");
            System.out.println(\"   📄 URL: \" + driver.getCurrentUrl());
            System.out.println(\"   📋 Título: \" + driver.getTitle());
            
            // Verificar elementos básicos
            boolean campoUsuarioVisible = esElementoVisible(campoUsuario);
            boolean campoPasswordVisible = esElementoVisible(campoPassword);
            boolean botonVisible = esElementoVisible(botonLogin);
            
            System.out.println(\"   👤 Campo usuario visible: \" + campoUsuarioVisible);
            System.out.println(\"   🔐 Campo password visible: \" + campoPasswordVisible);
            System.out.println(\"   🔘 Botón visible: \" + botonVisible);
            
            boolean paginaVisible = campoUsuarioVisible && campoPasswordVisible && botonVisible;
            System.out.println(\"   ✅ Página visible: \" + paginaVisible);
            
            return paginaVisible;
            
        } catch (Exception e) {
            System.out.println(\"❌ Error verificando página: \" + e.getMessage());
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
}"

echo -e "${BLUE}🔧 Corrigiendo PaginaRegistro con localizadores reales...${NC}"

crear_archivo "src/test/java/com/automatizacion/proyecto/paginas/PaginaRegistro.java" "package com.automatizacion.proyecto.paginas;

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
    private final By campoUsuario = By.id(\"username\");
    private final By campoPassword = By.id(\"password\");
    private final By campoConfirmarPassword = By.id(\"confirmPassword\");
    private final By botonRegistrar = By.tagName(\"button\");  // Solo hay un botón
    private final By mensajeFlash = By.id(\"flash\");
    
    public PaginaRegistro(WebDriver driver, EsperaExplicita espera) {
        super(driver, espera);
    }
    
    public void ingresarUsuario(String usuario) {
        System.out.println(\"🔤 Ingresando usuario: \" + usuario);
        WebElement campo = espera.esperarElementoVisible(campoUsuario);
        campo.clear();
        campo.sendKeys(usuario);
    }
    
    public void ingresarPassword(String password) {
        System.out.println(\"🔐 Ingresando password\");
        WebElement campo = espera.esperarElementoVisible(campoPassword);
        campo.clear();
        campo.sendKeys(password);
    }
    
    public void ingresarConfirmarPassword(String password) {
        System.out.println(\"🔐 Confirmando password\");
        WebElement campo = espera.esperarElementoVisible(campoConfirmarPassword);
        campo.clear();
        campo.sendKeys(password);
    }
    
    public void hacerClicRegistrar() {
        System.out.println(\"🖱️ Haciendo clic en botón Registrar\");
        WebElement boton = espera.esperarElementoClickeable(botonRegistrar);
        boton.click();
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public void realizarRegistro(String usuario, String password) {
        System.out.println(\"\\n🚀 === INICIANDO REGISTRO ===\");
        System.out.println(\"📄 URL actual: \" + driver.getCurrentUrl());
        
        ingresarUsuario(usuario);
        ingresarPassword(password);
        ingresarConfirmarPassword(password);
        hacerClicRegistrar();
        
        System.out.println(\"📄 URL después del registro: \" + driver.getCurrentUrl());
        System.out.println(\"✅ === REGISTRO COMPLETADO ===\\n\");
    }
    
    public String obtenerMensajeFlash() {
        try {
            if (esElementoVisible(mensajeFlash)) {
                String mensaje = obtenerTextoElemento(mensajeFlash);
                System.out.println(\"💬 Mensaje flash: \" + mensaje);
                return mensaje;
            }
        } catch (Exception e) {
            System.out.println(\"❌ No se encontró mensaje flash\");
        }
        return \"\";
    }
    
    @Override
    public boolean esPaginaVisible() {
        try {
            System.out.println(\"🔍 Verificando visibilidad de página de registro...\");
            System.out.println(\"   📄 URL: \" + driver.getCurrentUrl());
            
            boolean usuarioVisible = esElementoVisible(campoUsuario);
            boolean passwordVisible = esElementoVisible(campoPassword);
            boolean confirmarVisible = esElementoVisible(campoConfirmarPassword);
            boolean botonVisible = esElementoVisible(botonRegistrar);
            
            System.out.println(\"   👤 Campo usuario: \" + usuarioVisible);
            System.out.println(\"   🔐 Campo password: \" + passwordVisible);
            System.out.println(\"   🔐 Confirmar password: \" + confirmarVisible);
            System.out.println(\"   🔘 Botón: \" + botonVisible);
            
            boolean paginaVisible = usuarioVisible && passwordVisible && confirmarVisible && botonVisible;
            System.out.println(\"   ✅ Página visible: \" + paginaVisible);
            
            return paginaVisible;
            
        } catch (Exception e) {
            System.out.println(\"❌ Error verificando página: \" + e.getMessage());
            return false;
        }
    }
    
    public String obtenerTitulo() {
        return driver.getTitle();
    }
    
    public boolean validarElementosPagina() {
        return esPaginaVisible();
    }
}"

echo -e "${BLUE}🔧 Actualizando PruebasLogin con casos más robustos...${NC}"

crear_archivo "src/test/java/com/automatizacion/proyecto/pruebas/PruebasLogin.java" "package com.automatizacion.proyecto.pruebas;

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
        System.out.println(\"\\n\" + \"=\".repeat(60));
        System.out.println(\"🔧 CONFIGURANDO PRUEBA DE LOGIN\");
        System.out.println(\"=\".repeat(60));
        
        String urlLogin = \"https://practice.expandtesting.com/login\";
        System.out.println(\"🌐 Navegando a: \" + urlLogin);
        
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
        System.out.println(\"✅ Página visible: \" + paginaVisible);
        
        if (!paginaVisible) {
            // Capturar pantalla para debug
            obtenerGestorCaptura().capturarPantalla(obtenerDriver(), \"error_pagina_no_visible\");
            System.out.println(\"📸 Captura guardada para debug\");
        }
        
        Assert.assertTrue(paginaVisible, \"La página de login debe estar visible\");
        System.out.println(\"✅ Configuración completada\\n\");
    }
    
    @Test(description = \"Login exitoso con credenciales válidas\")
    public void testLoginExitoso() {
        System.out.println(\"\\n🧪 TEST: Login Exitoso\");
        System.out.println(\"-\".repeat(30));
        
        // Usar credenciales de la página de práctica
        paginaLogin.realizarLogin(\"practice\", \"SuperSecretPassword!\");
        
        // Verificar resultado
        String urlActual = obtenerDriver().getCurrentUrl();
        System.out.println(\"📍 URL después del login: \" + urlActual);
        
        // Capturar pantalla del resultado
        obtenerGestorCaptura().capturarPantalla(obtenerDriver(), \"login_exitoso\");
        
        // Verificar que se redirigió a página segura
        boolean loginExitoso = urlActual.contains(\"secure\") || !urlActual.contains(\"login\");
        System.out.println(\"✅ Login exitoso: \" + loginExitoso);
        
        Assert.assertTrue(loginExitoso, \"Debería redirigir a área segura\");
    }
    
    @Test(description = \"Login con credenciales inválidas\")
    public void testLoginCredencialesInvalidas() {
        System.out.println(\"\\n🧪 TEST: Credenciales Inválidas\");
        System.out.println(\"-\".repeat(30));
        
        paginaLogin.realizarLogin(\"usuario_invalido\", \"password_invalido\");
        
        // Verificar mensaje de error
        String mensajeFlash = paginaLogin.obtenerMensajeFlash();
        String urlActual = obtenerDriver().getCurrentUrl();
        
        System.out.println(\"💬 Mensaje: \" + mensajeFlash);
        System.out.println(\"📍 URL: \" + urlActual);
        
        // Capturar pantalla del error
        obtenerGestorCaptura().capturarPantalla(obtenerDriver(), \"login_error\");
        
        // Verificar que NO se logueó
        boolean seQuedoEnLogin = urlActual.contains(\"login\");
        System.out.println(\"❌ Se quedó en login: \" + seQuedoEnLogin);
        
        Assert.assertTrue(seQuedoEnLogin || !mensajeFlash.isEmpty(), 
            \"Debería mostrar error o permanecer en login\");
    }
    
    @Test(description = \"Campos vacíos\")
    public void testCamposVacios() {
        System.out.println(\"\\n🧪 TEST: Campos Vacíos\");
        System.out.println(\"-\".repeat(30));
        
        paginaLogin.realizarLogin(\"\", \"\");
        
        String urlActual = obtenerDriver().getCurrentUrl();
        System.out.println(\"📍 URL: \" + urlActual);
        
        // Capturar pantalla
        obtenerGestorCaptura().capturarPantalla(obtenerDriver(), \"campos_vacios\");
        
        boolean seQuedoEnLogin = urlActual.contains(\"login\");
        Assert.assertTrue(seQuedoEnLogin, \"Debería permanecer en página de login\");
    }
    
    @Test(description = \"Verificar elementos de la página\")
    public void testElementosPagina() {
        System.out.println(\"\\n🧪 TEST: Elementos de Página\");
        System.out.println(\"-\".repeat(30));
        
        boolean elementosValidos = paginaLogin.validarElementosPagina();
        String titulo = paginaLogin.obtenerTitulo();
        
        System.out.println(\"📋 Título: \" + titulo);
        System.out.println(\"✅ Elementos válidos: \" + elementosValidos);
        
        // Capturar pantalla de la página
        obtenerGestorCaptura().capturarPantalla(obtenerDriver(), \"elementos_pagina\");
        
        Assert.assertTrue(elementosValidos, \"Todos los elementos deberían estar presentes\");
        Assert.assertTrue(titulo.toLowerCase().contains(\"login\"), \"Título debería contener 'login'\");
    }
}"

# Compilar y probar
echo ""
echo -e "${YELLOW}🔍 Compilando proyecto corregido...${NC}"

if mvn clean compile test-compile -q; then
    echo -e "${GREEN}✅ Compilación exitosa${NC}"
    echo ""
    echo -e "${YELLOW}🧪 Ejecutando prueba de login corregida...${NC}"
    
    if mvn test -Dtest=PruebasLogin#testLoginExitoso -q; then
        echo ""
        echo -e "${GREEN}🎉 ¡PRUEBA EXITOSA!${NC}"
        echo -e "${GREEN}✅ Los localizadores funcionan correctamente${NC}"
        echo ""
        echo -e "${BLUE}📸 Revisa las capturas generadas en: capturas/${NC}"
        
    else
        echo ""
        echo -e "${YELLOW}⚠️ Aún hay problemas, pero ahora con logging detallado${NC}"
        echo -e "${BLUE}📸 Revisa las capturas para debug en: capturas/${NC}"
    fi
    
else
    echo -e "${RED}❌ Error en compilación${NC}"
fi

echo ""
echo -e "${CYAN}========================================${NC}"
echo -e "${CYAN} SOLUCIÓN APLICADA                     ${NC}"
echo -e "${CYAN}========================================${NC}"
echo ""
echo -e "${YELLOW}📋 Cambios realizados:${NC}"
echo -e "1. ${GREEN}Localizadores simplificados y reales${NC}"
echo -e "2. ${GREEN}Logging detallado en cada paso${NC}"
echo -e "3. ${GREEN}Capturas automáticas para debug${NC}"
echo -e "4. ${GREEN}Validaciones más robustas${NC}"
echo ""
echo -e "${YELLOW}🔍 Para debug completo ejecuta:${NC}"
echo -e "${BLUE}mvn test -Dtest=PruebasLogin${NC}"