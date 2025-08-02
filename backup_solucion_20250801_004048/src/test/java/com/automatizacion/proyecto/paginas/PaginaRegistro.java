package com.automatizacion.proyecto.paginas;

import com.automatizacion.proyecto.utilidades.EsperaExplicita;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Página de Registro para practice.expandtesting.com
 * @author Roberto Rivas Lopez
 */
public class PaginaRegistro extends PaginaBase {
    
    // Localizadores para practice.expandtesting.com/register
    private final By campoUsuario = By.id("username");
    private final By campoPassword = By.id("password");
    private final By campoConfirmarPassword = By.id("confirmPassword");
    private final By botonRegistrar = By.xpath("//button[@data-test='register-submit']");
    private final By mensajeError = By.id("flash");
    private final By mensajeExito = By.xpath("//div[contains(@class,'alert-success')]");
    private final By tituloRegistro = By.xpath("//h1[contains(text(),'Test Register')]");
    
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
        return "";
    }
    
    public String obtenerMensajeExito() {
        if (esElementoVisible(mensajeExito)) {
            return obtenerTextoElemento(mensajeExito);
        }
        return "";
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
}
