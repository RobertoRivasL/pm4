package com.automatizacion.proyecto.paginas.interfaces;

import com.automatizacion.proyecto.datos.ModeloDatosPrueba;

/**
 * Interfaz para la página de login.
 * Define el contrato para las operaciones de inicio de sesión.
 * 
 * @author Roberto Rivas Lopez
 */
public interface IPaginaLogin {
    
    /**
     * Realiza el login con los datos proporcionados
     * @param datos datos de login
     * @return true si el login fue exitoso
     */
    boolean realizarLogin(ModeloDatosPrueba datos);
    
    /**
     * Ingresa el email en el campo correspondiente
     * @param email email a ingresar
     */
    void ingresarEmail(String email);
    
    /**
     * Ingresa la contraseña en el campo correspondiente
     * @param password contraseña a ingresar
     */
    void ingresarPassword(String password);
    
    /**
     * Hace clic en el botón de login
     */
    void clickBotonLogin();
    
    /**
     * Limpia los campos del formulario
     */
    void limpiarFormulario();
    
    /**
     * Navega a la página de registro
     */
    void irARegistro();
    
    /**
     * Verifica si hay mensajes de error
     * @return true si hay errores
     */
    boolean hayMensajesError();
    
    /**
     * Obtiene el mensaje de error actual
     * @return texto del mensaje de error
     */
    String obtenerMensajeError();
    
    /**
     * Verifica si el login fue exitoso
     * @return true si el login fue exitoso
     */
    boolean esLoginExitoso();
}