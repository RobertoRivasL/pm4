package com.automatizacion.proyecto.paginas.interfaces;

import com.automatizacion.proyecto.datos.ModeloDatosPrueba;

/**
 * Interfaz que define el contrato para las páginas de login.
 * Implementa el principio de Inversión de Dependencias (DIP) del SOLID.
 * 
 * Permite que las clases de prueba dependan de abstracciones
 * en lugar de implementaciones concretas.
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
public interface IPaginaLogin {
    
    /**
     * Verifica si la página de login está visible y cargada correctamente.
     * 
     * @return true si la página está visible, false en caso contrario
     */
    boolean esPaginaVisible();
    
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
     * Marca o desmarca el checkbox de "Recordarme".
     * 
     * @param recordar true para marcar, false para desmarcar
     * @throws RuntimeException si no se puede manejar el checkbox
     */
    void marcarRecordarme(boolean recordar);
    
    /**
     * Hace clic en el botón de login para enviar el formulario.
     * 
     * @throws RuntimeException si no se puede hacer clic en el botón
     */
    void clickBotonLogin();
    
    /**
     * Hace clic en el enlace "¿Olvidaste tu contraseña?".
     * 
     * @throws RuntimeException si no se puede hacer clic en el enlace
     */
    void clickOlvidePassword();
    
    /**
     * Hace clic en el enlace "Crear cuenta nueva".
     * 
     * @throws RuntimeException si no se puede hacer clic en el enlace
     */
    void clickCrearCuenta();
    
    /**
     * Realiza el proceso completo de inicio de sesión con los datos proporcionados.
     * 
     * @param datos Modelo con las credenciales y configuraciones de login
     * @return true si el login fue exitoso, false en caso contrario
     */
    boolean iniciarSesion(ModeloDatosPrueba datos);
    
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
     * Verifica si la cuenta del usuario está bloqueada.
     * 
     * @return true si la cuenta está bloqueada, false en caso contrario
     */
    boolean esCuentaBloqueada();
    
    /**
     * Verifica si existen errores de validación en la página.
     * 
     * @return true si hay errores de validación, false en caso contrario
     */
    boolean hayErroresValidacion();
    
    /**
     * Verifica si el usuario está logueado exitosamente.
     * 
     * @return true si el usuario está logueado, false en caso contrario
     */
    boolean esUsuarioLogueado();
    
    /**
     * Limpia todos los campos del formulario de login.
     * 
     * @throws RuntimeException si no se pueden limpiar los campos
     */
    void limpiarCampos();
    
    /**
     * Cierra el modal de bloqueo si está presente en la página.
     * 
     * @throws RuntimeException si no se puede cerrar el modal
     */
    void cerrarModalBloqueo();
}