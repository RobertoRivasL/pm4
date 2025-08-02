#!/bin/bash

# =========================================================================
# Script para Actualizar a la Página Correcta de Práctica
# Autor: Roberto Rivas Lopez
# Descripción: Actualiza URLs y localizadores para practice.expandtesting.com
# =========================================================================

# Colores
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m'

echo -e "${CYAN}========================================${NC}"
echo -e "${CYAN} Actualizando a Página Correcta         ${NC}"
echo -e "${CYAN} practice.expandtesting.com             ${NC}"
echo -e "${CYAN}========================================${NC}"
echo ""

# Verificar directorio
if [[ ! -f "pom.xml" ]]; then
    echo -e "${RED}❌ Error: No se encontró pom.xml${NC}"
    exit 1
fi

# Crear backup
BACKUP_DIR="backup_actualizacion_$(date +%Y%m%d_%H%M%S)"
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

echo -e "${BLUE}🔧 Actualizando configuración y páginas...${NC}"
echo ""

# 1. Actualizar config.properties
crear_archivo "src/test/resources/config.properties" "# Configuración del proyecto de automatización
# Autor: Roberto Rivas Lopez

# URL base de la aplicación - PÁGINA CORRECTA
url.base=https://practice.expandtesting.com/

# URLs específicas
url.login=https://practice.expandtesting.com/login
url.register=https://practice.expandtesting.com/register

# Credenciales de prueba válidas
usuario.valido=practice
password.valido=SuperSecretPassword!

# Configuración del navegador
navegador.tipo=CHROME
navegador.headless=false

# Timeouts (en segundos)
timeout.explicito=10
timeout.implicito=5

# Directorios
directorio.capturas=capturas
directorio.reportes=reportes

# Logging
log.nivel=INFO"

# 2. Actualizar PaginaLogin.java con localizadores correctos
crear_archivo "src/test/java/com/automatizacion/proyecto/paginas/PaginaLogin.java" "package com.automatizacion.proyecto.paginas;

import com.automatizacion.proyecto.utilidades.EsperaExplicita;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Página de Login para practice.expandtesting.com
 * @author Roberto Rivas Lopez
 */
public class PaginaLogin extends PaginaBase {
    
    // Localizadores para practice.expandtesting.com
    private final By campoUsuario = By.id(\"username\");
    private final By campoPassword = By.id(\"password\");
    private final By botonLogin = By.xpath(\"//button[@data-test='login-submit']\");
    private final By mensajeError = By.id(\"flash\");
    private final By enlaceRegistro = By.linkText(\"here\");
    private final By tituloLogin = By.xpath(\"//h1[contains(text(),'Test Login')]\");
    
    public PaginaLogin(WebDriver driver, EsperaExplicita espera) {
        super(driver, espera);
    }
    
    public void ingresarUsuario(String usuario) {
        ingresarTextoEnCampo(campoUsuario, usuario);
    }
    
    public void ingresarPassword(String password) {
        ingresarTextoEnCampo(campoPassword, password);
    }
    
    public void hacerClicLogin() {
        hacerClicElemento(botonLogin);
    }
    
    public void realizarLogin(String usuario, String password) {
        ingresarUsuario(usuario);
        ingresarPassword(password);
        hacerClicLogin();
    }
    
    public String obtenerMensajeError() {
        if (esElementoVisible(mensajeError)) {
            return obtenerTextoElemento(mensajeError);
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
        return esElementoVisible(campoUsuario) && 
               esElementoVisible(campoPassword) &&
               esElementoVisible(botonLogin);
    }
    
    public String obtenerTitulo() {
        if (esElementoVisible(tituloLogin)) {
            return obtenerTextoElemento(tituloLogin);
        }
        return driver.getTitle();
    }
    
    public boolean validarElementosPagina() {
        return esPaginaVisible() && esElementoVisible(tituloLogin);
    }
    
    public void limpiarFormulario() {
        if (esElementoVisible(campoUsuario)) {
            driver.findElement(campoUsuario).clear();
        }
        if (esElementoVisible(campoPassword)) {
            driver.findElement(campoPassword).clear();
        }
    }
}"

# 3. Crear PaginaRegistro.java correcta
crear_archivo "src/test/java/com/automatizacion/proyecto/paginas/PaginaRegistro.java" "package com.automatizacion.proyecto.paginas;

import com.automatizacion.proyecto.utilidades.EsperaExplicita;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Página de Registro para practice.expandtesting.com
 * @author Roberto Rivas Lopez
 */
public class PaginaRegistro extends PaginaBase {
    
    // Localizadores para practice.expandtesting.com/register
    private final By campoUsuario = By.id(\"username\");
    private final By campoPassword = By.id(\"password\");
    private final By campoConfirmarPassword = By.id(\"confirmPassword\");
    private final By botonRegistrar = By.xpath(\"//button[@data-test='register-submit']\");
    private final By mensajeError = By.id(\"flash\");
    private final By mensajeExito = By.xpath(\"//div[contains(@class,'alert-success')]\");
    private final By tituloRegistro = By.xpath(\"//h1[contains(text(),'Test Register')]\");
    
    public PaginaRegistro(WebDriver driver, EsperaExplicita espera) {
        super(driver, espera);
    }
    
    public void ingresarUsuario(String usuario) {
        ingresarTextoEnCampo(campoUsuario, usuario);
    }
    
    public void ingresarPassword(String password) {
        ingresarTextoEnCampo(campoPassword, password);
    }
    
    public void ingresarConfirmarPassword(String password) {
        ingresarTextoEnCampo(campoConfirmarPassword, password);
    }
    
    public void hacerClicRegistrar() {
        hacerClicElemento(botonRegistrar);
    }
    
    public void realizarRegistro(String usuario, String password) {
        ingresarUsuario(usuario);
        ingresarPassword(password);
        ingresarConfirmarPassword(password);
        hacerClicRegistrar();
    }
    
    public String obtenerMensajeError() {
        if (esElementoVisible(mensajeError)) {
            return obtenerTextoElemento(mensajeError);
        }
        return \"\";
    }
    
    public String obtenerMensajeExito() {
        if (esElementoVisible(mensajeExito)) {
            return obtenerTextoElemento(mensajeExito);
        }
        return \"\";
    }
    
    @Override
    public boolean esPaginaVisible() {
        return esElementoVisible(campoUsuario) && 
               esElementoVisible(campoPassword) &&
               esElementoVisible(campoConfirmarPassword) &&
               esElementoVisible(botonRegistrar);
    }
    
    public String obtenerTitulo() {
        if (esElementoVisible(tituloRegistro)) {
            return obtenerTextoElemento(tituloRegistro);
        }
        return driver.getTitle();
    }
    
    public boolean validarElementosPagina() {
        return esPaginaVisible() && esElementoVisible(tituloRegistro);
    }
    
    public void limpiarFormulario() {
        if (esElementoVisible(campoUsuario)) {
            driver.findElement(campoUsuario).clear();
        }
        if (esElementoVisible(campoPassword)) {
            driver.findElement(campoPassword).clear();
        }
        if (esElementoVisible(campoConfirmarPassword)) {
            driver.findElement(campoConfirmarPassword).clear();
        }
    }
}"

# 4. Actualizar PruebasLogin.java con casos realistas
crear_archivo "src/test/java/com/automatizacion/proyecto/pruebas/PruebasLogin.java" "package com.automatizacion.proyecto.pruebas;

import com.automatizacion.proyecto.base.BaseTest;
import com.automatizacion.proyecto.paginas.PaginaLogin;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Pruebas de funcionalidad de Login para practice.expandtesting.com
 * @author Roberto Rivas Lopez
 */
public class PruebasLogin extends BaseTest {
    
    private PaginaLogin paginaLogin;
    
    @BeforeMethod
    public void configurarPagina() {
        String urlLogin = configuracion.obtenerUrlBase() + \"login\";
        obtenerDriver().get(urlLogin);
        paginaLogin = new PaginaLogin(obtenerDriver(), obtenerEsperaExplicita());
        
        // Verificar que la página cargó correctamente
        Assert.assertTrue(paginaLogin.esPaginaVisible(), 
            \"La página de login no cargó correctamente\");
    }
    
    @Test(description = \"Verificar login exitoso con credenciales válidas\")
    public void testLoginExitoso() {
        // Usar credenciales válidas de practice.expandtesting.com
        paginaLogin.realizarLogin(\"practice\", \"SuperSecretPassword!\");
        
        // Verificar que se redirigió a página segura
        String urlActual = obtenerDriver().getCurrentUrl();
        Assert.assertTrue(urlActual.contains(\"secure\"), 
            \"No se redirigió a la página segura después del login exitoso\");
    }
    
    @Test(description = \"Verificar mensaje de error con credenciales inválidas\")
    public void testLoginCredencialesInvalidas() {
        paginaLogin.realizarLogin(\"usuario_invalido\", \"password_invalido\");
        
        String mensajeError = paginaLogin.obtenerMensajeError();
        Assert.assertFalse(mensajeError.isEmpty(), 
            \"Debería mostrar mensaje de error con credenciales inválidas\");
        Assert.assertTrue(mensajeError.contains(\"invalid\") || mensajeError.contains(\"incorrect\"),
            \"El mensaje de error debería indicar credenciales inválidas\");
    }
    
    @Test(description = \"Verificar campos obligatorios vacíos\")
    public void testCamposVacios() {
        // Intentar login sin credenciales
        paginaLogin.realizarLogin(\"\", \"\");
        
        // Verificar que permanece en la página de login
        Assert.assertTrue(paginaLogin.esPaginaVisible(), 
            \"Debería permanecer en la página de login con campos vacíos\");
    }
    
    @Test(description = \"Verificar login solo con usuario\")
    public void testSoloUsuario() {
        paginaLogin.realizarLogin(\"practice\", \"\");
        
        // Verificar que permanece en login o muestra error
        String urlActual = obtenerDriver().getCurrentUrl();
        Assert.assertTrue(urlActual.contains(\"login\"), 
            \"Debería permanecer en página de login\");
    }
    
    @Test(description = \"Verificar login solo con password\")
    public void testSoloPassword() {
        paginaLogin.realizarLogin(\"\", \"SuperSecretPassword!\");
        
        // Verificar que permanece en login
        String urlActual = obtenerDriver().getCurrentUrl();
        Assert.assertTrue(urlActual.contains(\"login\"), 
            \"Debería permanecer en página de login\");
    }
    
    @Test(description = \"Verificar elementos de la interfaz\")
    public void testElementosInterfaz() {
        Assert.assertTrue(paginaLogin.validarElementosPagina(), 
            \"Todos los elementos de la página deberían estar presentes\");
        
        String titulo = paginaLogin.obtenerTitulo();
        Assert.assertTrue(titulo.contains(\"Login\"), 
            \"El título debería contener 'Login'\");
    }
}"

# 5. Crear pruebas de registro
crear_archivo "src/test/java/com/automatizacion/proyecto/pruebas/PruebasRegistro.java" "package com.automatizacion.proyecto.pruebas;

import com.automatizacion.proyecto.base.BaseTest;
import com.automatizacion.proyecto.paginas.PaginaRegistro;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.util.Random;

/**
 * Pruebas de funcionalidad de Registro para practice.expandtesting.com
 * @author Roberto Rivas Lopez
 */
public class PruebasRegistro extends BaseTest {
    
    private PaginaRegistro paginaRegistro;
    private Random random = new Random();
    
    @BeforeMethod
    public void configurarPagina() {
        String urlRegistro = configuracion.obtenerUrlBase() + \"register\";
        obtenerDriver().get(urlRegistro);
        paginaRegistro = new PaginaRegistro(obtenerDriver(), obtenerEsperaExplicita());
        
        Assert.assertTrue(paginaRegistro.esPaginaVisible(), 
            \"La página de registro no cargó correctamente\");
    }
    
    @Test(description = \"Verificar registro exitoso con datos válidos\")
    public void testRegistroExitoso() {
        String usuarioUnico = \"usuario\" + System.currentTimeMillis();
        String password = \"Password123!\";
        
        paginaRegistro.realizarRegistro(usuarioUnico, password);
        
        // Verificar mensaje de éxito o redirección
        String urlActual = obtenerDriver().getCurrentUrl();
        String mensajeExito = paginaRegistro.obtenerMensajeExito();
        
        Assert.assertTrue(urlActual.contains(\"login\") || !mensajeExito.isEmpty(),
            \"Debería mostrar éxito o redirigir al login\");
    }
    
    @Test(description = \"Verificar error con contraseñas que no coinciden\")
    public void testPasswordsNoCoinciden() {
        String usuario = \"test\" + random.nextInt(1000);
        
        paginaRegistro.ingresarUsuario(usuario);
        paginaRegistro.ingresarPassword(\"Password123!\");
        paginaRegistro.ingresarConfirmarPassword(\"Password456!\");
        paginaRegistro.hacerClicRegistrar();
        
        // Verificar error
        String mensajeError = paginaRegistro.obtenerMensajeError();
        Assert.assertTrue(!mensajeError.isEmpty() || 
                         obtenerDriver().getCurrentUrl().contains(\"register\"),
            \"Debería mostrar error o permanecer en registro\");
    }
    
    @Test(description = \"Verificar campos obligatorios vacíos\")
    public void testCamposVacios() {
        paginaRegistro.realizarRegistro(\"\", \"\");
        
        Assert.assertTrue(paginaRegistro.esPaginaVisible(),
            \"Debería permanecer en página de registro con campos vacíos\");
    }
    
    @Test(description = \"Verificar elementos de la interfaz\")
    public void testElementosInterfaz() {
        Assert.assertTrue(paginaRegistro.validarElementosPagina(),
            \"Todos los elementos deberían estar presentes\");
        
        String titulo = paginaRegistro.obtenerTitulo();
        Assert.assertTrue(titulo.contains(\"Register\"),
            \"El título debería contener 'Register'\");
    }
}"

# Compilar y probar
echo ""
echo -e "${YELLOW}🔍 Compilando proyecto actualizado...${NC}"

if mvn clean compile test-compile -q; then
    echo -e "${GREEN}🎉 ¡PROYECTO ACTUALIZADO EXITOSAMENTE!${NC}"
    echo ""
    echo -e "${CYAN}========================================${NC}"
    echo -e "${CYAN} CONFIGURACIÓN ACTUALIZADA              ${NC}"
    echo -e "${CYAN}========================================${NC}"
    echo ""
    echo -e "${GREEN}✅ URL actualizada a: practice.expandtesting.com${NC}"
    echo -e "${GREEN}✅ Localizadores corregidos${NC}"
    echo -e "${GREEN}✅ Credenciales de prueba configuradas${NC}"
    echo -e "${GREEN}✅ Páginas de Login y Registro actualizadas${NC}"
    echo ""
    echo -e "${YELLOW}📋 Siguientes pasos:${NC}"
    echo -e "1. ${BLUE}mvn test${NC} - Ejecutar pruebas actualizadas"
    echo -e "2. Verificar que las pruebas pasen correctamente"
    echo -e "3. Agregar más casos según la rúbrica"
    
else
    echo -e "${RED}❌ Error en la compilación${NC}"
    echo -e "${YELLOW}💡 Backup disponible en: $BACKUP_DIR${NC}"
fi

echo ""
echo -e "${CYAN}========================================${NC}"
echo -e "${CYAN} ACTUALIZACIÓN COMPLETADA               ${NC}"
echo -e "${CYAN}========================================${NC}"