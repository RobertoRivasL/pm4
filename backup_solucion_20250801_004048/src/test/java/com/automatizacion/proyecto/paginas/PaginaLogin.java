package com.automatizacion.proyecto.paginas;

import com.automatizacion.proyecto.utilidades.EsperaExplicita;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Página de Login para practice.expandtesting.com
 * @author Roberto Rivas Lopez
 */
public class PaginaLogin extends PaginaBase {
    
    // Localizadores para practice.expandtesting.com
    private final By campoUsuario = By.id("username");
    private final By campoPassword = By.id("password");
    private final By botonLogin = By.xpath("//button[@data-test='login-submit']");
    private final By mensajeError = By.id("flash");
    private final By enlaceRegistro = By.linkText("here");
    private final By tituloLogin = By.xpath("//h1[contains(text(),'Test Login')]");
    
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
        return "";
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
}
