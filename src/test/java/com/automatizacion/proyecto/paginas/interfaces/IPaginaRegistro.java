package com.automatizacion.proyecto.paginas.interfaces;

import com.automatizacion.proyecto.datos.ModeloDatosPrueba;

/**
 * Interfaz que define el contrato para las páginas de registro.
 * Implementa el principio de Inversión de Dependencias (DIP) del SOLID.
 * 
 * Permite que las clases de prueba dependan de abstracciones
 * en lugar de implementaciones concretas.
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
public interface IPaginaRegistro {
    
    /**
     * Verifica si la página de registro está visible y cargada correctamente.
     * 
     * @return true si la página está visible, false en caso contrario
     */
    boolean esPaginaVisible();
    
    /**
     * Ingresa el nombre del usuario en el campo correspondiente.
     * 
     * @param nombre Nombre a ingresar
     * @throws RuntimeException si no se puede ingresar el nombre
     */
    void ingresarNombre(String nombre);
    
    /**
     * Ingresa el apellido del usuario en el campo correspondiente.
     * 
     * @param apellido Apellido a ingresar
     * @throws RuntimeException si no se puede ingresar el apellido
     */
    void ingresarApellido(String apellido);
    
    /**
     * Ingresa el email del usuario en el campo correspondiente.
     * 
     * @param email Email a ingresar
     * @throws RuntimeException si no se puede ingresar el email
     */
    void ingresarEmail(String email);
    
    /**
     * Ingresa la contraseña del usuario en el campo correspondiente.
     * 
     * @param password Contraseña a ingresar
     * @throws RuntimeException si no se puede ingresar la contraseña
     */
    void ingresarPassword(String password);
    
    /**
     * Ingresa la confirmación de contraseña en el campo correspondiente.
     * 
     * @param confirmarPassword Confirmación de contraseña a ingresar
     * @throws RuntimeException si no se puede ingresar la confirmación
     */
    void ingresarConfirmarPassword(String confirmarPassword);
    
    /**
     * Ingresa el teléfono del usuario en el campo correspondiente.
     * 
     * @param telefono Teléfono a ingresar (opcional)
     * @throws RuntimeException si no se puede ingresar el teléfono
     */
    void ingresarTelefono(String telefono);
    
    /**
     * Selecciona el género del usuario en el dropdown correspondiente.
     * 
     * @param genero Género a seleccionar
     * @throws RuntimeException si no se puede seleccionar el género
     */
    void seleccionarGenero(String genero);
    
    /**
     * Acepta los términos y condiciones marcando el checkbox.
     * 
     * @throws RuntimeException si no se pueden aceptar los términos
     */
    void aceptarTerminos();
    
    /**
     * Hace clic en el botón de registrar para enviar el formulario.
     * 
     * @throws RuntimeException si no se puede hacer clic en el botón
     */
    void clickBotonRegistrar();
    
    /**
     * Registra un usuario completo utilizando los datos proporcionados.
     * 
     * @param datos Modelo con todos los datos necesarios para el registro
     * @return true si el registro fue exitoso, false en caso contrario
     */
    boolean registrarUsuario(ModeloDatosPrueba datos);
    
    /**
     * Obtiene el mensaje de error mostrado en la página, si existe.
     * 
     * @return Texto del mensaje de error, cadena vacía si no hay error
     */
    String obtenerMensajeError();
    
    /**
     * Obtiene el mensaje de éxito mostrado en la página, si existe.
     * 
     * @return Texto del mensaje de éxito, cadena vacía si no hay mensaje
     */
    String obtenerMensajeExito();
    
    /**
     * Verifica si existen errores de validación en la página.
     * 
     * @return true si hay errores de validación, false en caso contrario
     */
    boolean hayErroresValidacion();
    
    /**
     * Limpia todos los campos del formulario.
     * 
     * @throws RuntimeException si no se puede limpiar el formulario
     */
    void limpiarFormulario();
}