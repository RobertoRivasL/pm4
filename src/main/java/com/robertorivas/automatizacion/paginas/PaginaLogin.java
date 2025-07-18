package com.robertorivas.automatizacion.paginas;

import com.robertorivas.automatizacion.configuracion.ConfiguracionPruebas;
import com.robertorivas.automatizacion.modelos.Usuario;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.List;

/**
 * Page Object para la página de inicio de sesión.
 */
public class PaginaLogin extends PaginaBase {

    // Localizadores básicos
    @FindBy(css = "input[name='username'], #username")
    private WebElement campoEmail;

    @FindBy(css = "input[name='password'], #password")
    private WebElement campoPassword;

    @FindBy(css = "button[type='submit'], #submit")
    private WebElement botonIngresar;

    @FindBy(css = "a[href*='register'], .link-register")
    private WebElement enlaceRegistro;

    @FindBy(css = "a[href*='forgot'], .link-forgot")
    private WebElement enlaceRecuperar;

    @FindBy(css = ".error-message, .alert-danger, #error")
    private List<WebElement> mensajesError;

    public PaginaLogin(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean estaPaginaCargada() {
        return estaElementoVisible(By.cssSelector("input[name='username'], #username"));
    }

    @Override
    public void esperarCargaPagina() {
        buscarElementoVisible(By.cssSelector("input[name='username'], #username"));
    }

    @Override
    public String obtenerTituloPagina() {
        return "Login";
    }

    /** Navega a la URL de login. */
    public PaginaLogin navegarAPaginaLogin() {
        String url = ConfiguracionPruebas.obtenerInstancia().obtenerUrlLogin();
        navegarA(url);
        return this;
    }

    /** Introduce el email. */
    public PaginaLogin introducirEmail(String email) {
        introducirTexto(By.cssSelector("input[name='username'], #username"), email);
        return this;
    }

    /** Introduce la contraseña. */
    public PaginaLogin introducirPassword(String password) {
        introducirTexto(By.cssSelector("input[name='password'], #password"), password);
        return this;
    }

    /** Envía el formulario. */
    public void enviarFormulario() {
        hacerClic(By.cssSelector("button[type='submit'], #submit"));
        esperarCargaPaginaCompleta();
    }

    /** Inicia sesión con un usuario. */
    public boolean iniciarSesion(Usuario usuario) {
        introducirEmail(usuario.getEmail());
        introducirPassword(usuario.getPassword());
        enviarFormulario();
        return !hayErroresLogin();
    }

    /** Inicia sesión con credenciales explícitas. */
    public boolean iniciarSesion(String email, String password, boolean enviar) {
        introducirEmail(email);
        introducirPassword(password);
        if (enviar) {
            enviarFormulario();
            return !hayErroresLogin();
        }
        return true;
    }

    /** Obtiene los mensajes de error actuales. */
    public List<String> obtenerErroresLogin() {
        List<String> errores = new ArrayList<>();
        for (WebElement elem : mensajesError) {
            if (elem.isDisplayed()) {
                String texto = elem.getText().trim();
                if (!texto.isEmpty()) {
                    errores.add(texto);
                }
            }
        }
        return errores;
    }

    /** Indica si existen errores visibles. */
    public boolean hayErroresLogin() {
        return !obtenerErroresLogin().isEmpty();
    }

    /** Intenta iniciar sesión varias veces para simular bloqueo de cuenta. */
    public boolean intentarHastaBloqueoCuenta(String email, String passwordIncorrecto) {
        for (int i = 0; i < 3; i++) {
            iniciarSesion(email, passwordIncorrecto, true);
            if (credencialesInvalidas()) {
                return true;
            }
        }
        return false;
    }

    /** Verifica si se muestra mensaje de credenciales inválidas. */
    public boolean credencialesInvalidas() {
        return obtenerErroresLogin().stream()
                .anyMatch(m -> m.toLowerCase().contains("invalid") || m.toLowerCase().contains("incorrect"));
    }

    /** Verifica comportamiento con campos vacíos. */
    public boolean verificarCamposVacios() {
        limpiarCamposLogin();
        enviarFormulario();
        return hayErroresLogin();
    }

    /** Verifica comportamiento con password vacío. */
    public boolean verificarPasswordVacio(String emailValido) {
        introducirEmail(emailValido);
        introducirPassword("");
        enviarFormulario();
        return hayErroresLogin();
    }

    /** Verifica comportamiento con email vacío. */
    public boolean verificarEmailVacio(String passwordValido) {
        introducirEmail("");
        introducirPassword(passwordValido);
        enviarFormulario();
        return hayErroresLogin();
    }

    /** Limpia los campos del formulario. */
    public void limpiarCamposLogin() {
        if (campoEmail != null) campoEmail.clear();
        if (campoPassword != null) campoPassword.clear();
    }

    /** Obtiene valores actuales de email y password. */
    public String[] obtenerValoresFormulario() {
        String email = campoEmail != null ? campoEmail.getAttribute("value") : "";
        String password = campoPassword != null ? campoPassword.getAttribute("value") : "";
        return new String[]{email, password};
    }

    /** Navega a la página de registro. */
    public void irAPaginaRegistro() {
        if (enlaceRegistro != null) {
            enlaceRegistro.click();
            esperarCargaPaginaCompleta();
        }
    }

    /** Navega a la página de recuperación de contraseña. */
    public void irARecuperarPassword() {
        if (enlaceRecuperar != null) {
            enlaceRecuperar.click();
            esperarCargaPaginaCompleta();
        }
    }
}
