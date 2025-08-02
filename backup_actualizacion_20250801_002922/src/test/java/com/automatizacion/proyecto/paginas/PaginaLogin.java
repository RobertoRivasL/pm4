package com.automatizacion.proyecto.paginas;

import com.automatizacion.proyecto.utilidades.EsperaExplicita;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Página de Login (Page Object Model)
 * @author Roberto Rivas Lopez
 */
public class PaginaLogin extends PaginaBase {
    
    // Localizadores
    private final By campoUsuario = By.name("username");
    private final By campoPassword = By.name("password");
    private final By botonLogin = By.xpath("//button[@type='submit']");
    private final By mensajeError = By.xpath("//p[contains(@class,'error')]");
    
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
    
    @Override
    public boolean esPaginaVisible() {
        return esElementoVisible(campoUsuario) && esElementoVisible(campoPassword);
    }
}
