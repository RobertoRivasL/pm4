package com.automatizacion.proyecto.paginas;

import com.automatizacion.proyecto.datos.ModeloDatosPrueba;

/**
 * Interfaz que define las operaciones específicas para la página de registro de usuarios.
 * Establece el contrato para todas las acciones relacionadas con el registro.
 * 
 * Extiende IPaginaBase para heredar operaciones básicas e implementa
 * el principio de Segregación de Interfaces del SOLID.
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
public interface IPaginaRegistro extends IPaginaBase {
    
    /**
     * Llena todos los campos del formulario de registro
     * @param datos modelo con todos los datos necesarios para el registro
     */
    void llenarFormularioCompleto(ModeloDatosPrueba datos);
    
    /**
     * Ingresa el nombre en el campo correspondiente
     * @param nombre nombre del usuario
     */
    void ingresarNombre(String nombre);
    
    /**
     * Ingresa el apellido en el campo correspondiente
     * @param apellido apellido del usuario
     */
    void ingresarApellido(String apellido);
    
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
     * Ingresa la confirmación de contraseña
     * @param confirmarPassword confirmación de la contraseña
     */
    void ingresarConfirmarPassword(String confirmarPassword);
    
    /**
     * Acepta los términos y condiciones
     */
    void aceptarTerminos();
    
    /**
     * Hace click en el botón de registro
     */
    void clickBotonRegistrar();
    
    /**
     * Ejecuta el proceso completo de registro de usuario
     * @param datos modelo con los datos de registro
     * @return true si el proceso se ejecutó correctamente
     */
    boolean registrarUsuario(ModeloDatosPrueba datos);
    
    /**
     * Verifica si el registro fue exitoso
     * @return true si el registro fue exitoso
     */
    boolean verificarRegistroExitoso();
    
    /**
     * Verifica si el registro falló
     * @return true si el registro falló
     */
    boolean verificarRegistroFallido();
    
    /**
     * Obtiene el mensaje de error mostrado en la página
     * @return texto del mensaje de error, cadena vacía si no hay error
     */
    String obtenerMensajeError();
}
