package com.automatizacion.proyecto.paginas.interfaces;

import com.automatizacion.proyecto.datos.ModeloDatosPrueba;

public interface IPaginaLogin extends IPaginaBase {
    void ingresarCredenciales(String email, String password);
    void ingresarEmail(String email);
    void ingresarPassword(String password);
    void clickBotonLogin();
    boolean iniciarSesion(ModeloDatosPrueba datos);
    boolean verificarLoginExitoso();
    boolean verificarLoginFallido();
    String obtenerMensajeError();
}