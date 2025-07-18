package com.robertorivas.automatizacion.paginas;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Representa la página principal que se muestra después de un login exitoso.
 */
public class PaginaPrincipal extends PaginaBase {

    @FindBy(css = "a[href*='logout'], .logout")
    private WebElement enlaceLogout;

    @FindBy(css = ".profile-name, #userName")
    private WebElement etiquetaNombre;

    public PaginaPrincipal(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean estaPaginaCargada() {
        return estaElementoVisible(By.cssSelector("a[href*='logout'], .logout"));
    }

    @Override
    public void esperarCargaPagina() {
        buscarElementoVisible(By.cssSelector("a[href*='logout'], .logout"));
    }

    @Override
    public String obtenerTituloPagina() {
        return "Principal";
    }

    /** Verifica si el usuario está logueado. */
    public boolean usuarioLogueado() {
        return estaElementoVisible(By.cssSelector("a[href*='logout'], .logout"));
    }

    /** Obtiene el nombre del usuario logueado. */
    public String obtenerNombreUsuarioLogueado() {
        if (etiquetaNombre != null && etiquetaNombre.isDisplayed()) {
            return etiquetaNombre.getText().trim();
        }
        return "";
    }

    /** Cierra la sesión si es posible. */
    public boolean cerrarSesion() {
        if (enlaceLogout != null) {
            enlaceLogout.click();
            esperarCargaPaginaCompleta();
            return true;
        }
        return false;
    }
}
