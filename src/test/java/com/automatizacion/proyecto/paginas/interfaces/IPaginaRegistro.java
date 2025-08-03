package com.automatizacion.proyecto.paginas.interfaces;

import com.automatizacion.proyecto.datos.ModeloDatosPrueba;

/**
 * Interfaz para la página de registro.
 * Define el contrato para las operaciones de registro.
 * 
 * @author Roberto Rivas Lopez
 */
public interface IPaginaRegistro {
    
    /**
     * Registra un usuario con los datos proporcionados
     * @param datos datos del usuario a registrar
     * @return true si el registro fue exitoso
     */
    boolean registrarUsuario(ModeloDatosPrueba datos);
    
    /**
     * Ingresa el nombre en el campo correspondiente
     * @param nombre nombre a ingresar
     */
    void ingresarNombre(String nombre);
    
    /**
     * Ingresa el apellido en el campo correspondiente
     * @param apellido apellido a ingresar
     */
    void ingresarApellido(String apellido);
    
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
     * Ingresa la confirmación de contraseña
     * @param confirmacionPassword confirmación a ingresar
     */
    void ingresarConfirmarPassword(String confirmacionPassword);
    
    /**
     * Hace clic en el botón de registro
     */
    void clickBotonRegistro();
    
    /**
     * Limpia todos los campos del formulario
     */
    void limpiarFormulario();
    
    /**
     * Navega a la página de login
     */
    void irALogin();
    
    /**
     * Verifica si hay mensajes de error visibles
     * @return true si hay errores
     */
    boolean hayMensajesError();
    
    /**
     * Obtiene el texto del mensaje de error
     * @return texto del mensaje de error
     */
    String obtenerMensajeError();
}