package com.automatizacion.proyecto.paginas.interfaces;

import com.automatizacion.proyecto.datos.ModeloDatosPrueba;

public interface IPaginaRegistro extends IPaginaBase {
    void llenarFormularioCompleto(ModeloDatosPrueba datos);
    void ingresarNombre(String nombre);
    void ingresarApellido(String apellido);
    void ingresarEmail(String email);
    void ingresarPassword(String password);
    void ingresarConfirmarPassword(String confirmarPassword);
    void aceptarTerminos();
    void clickBotonRegistrar();
    boolean registrarUsuario(ModeloDatosPrueba datos);
    boolean verificarRegistroExitoso();
    boolean verificarRegistroFallido();
    String obtenerMensajeError();
}