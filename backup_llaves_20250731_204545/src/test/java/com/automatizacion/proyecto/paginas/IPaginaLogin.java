package com.automatizacion.proyecto.paginas;

import com.automatizacion.proyecto.datos.ModeloDatosPrueba;

/**
 * Interfaz que define las operaciones específicas para la página de inicio de sesión.
 * Establece el contrato para todas las acciones relacionadas con el login.
 * 
 * Extiende IPaginaBase para heredar operaciones básicas e implementa
 * el principio de Segregación de Interfaces del SOLID.
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
public interface IPaginaLogin extends IPaginaBase {
    
    /**
     * Ingresa las credenciales de usuario (email y contraseña)
     * @param email dirección de correo electrónico
     * @param password contraseña del usuario
     */
    void ingresarCredenciales(String email, String password);
    
    /**
     * Ingresa el email en el campo correspondiente
     * @param email dirección de correo electrónico
     */
    void ingresarEmail(String email);
    
    /**
     * Ingresa la contraseña en el campo correspondiente
     * @param password contraseña del usuario
     */
    void ingresarPassword(String password);
    
    /**
     * Hace click en el botón de inicio de sesión
     */
    void clickBotonLogin();
    
    /**
     * Ejecuta el proceso completo de inicio de sesión
     * @param datos modelo con los datos de login
     * @return true si el proceso se ejecutó correctamente
     */
    boolean iniciarSesion(ModeloDatosPrueba datos);
    
    /**
     * Verifica si el login fue exitoso
     * @return true si el login fue exitoso
     */
    boolean verificarLoginExitoso();
    
    /**
     * Verifica si el login falló
     * @return true si el login falló
     */
    boolean verificarLoginFallido();
    
    /**
     * Obtiene el mensaje de error mostrado en la página
     * @return texto del mensaje de error, cadena vacía si no hay error
     */
    String obtenerMensajeError();
