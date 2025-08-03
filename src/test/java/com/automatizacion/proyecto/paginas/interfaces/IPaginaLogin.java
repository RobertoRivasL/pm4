package com.automatizacion.proyecto.paginas.interfaces;

import com.automatizacion.proyecto.datos.ModeloDatosPrueba;

/**
 * Interfaz que define las operaciones específicas para la página de inicio de sesión.
 * Específicamente adaptada para practice.expandtesting.com.
 * 
 * Extiende IPaginaBase para heredar operaciones básicas e implementa
 * el principio de Segregación de Interfaces del SOLID.
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
public interface IPaginaLogin extends IPaginaBase {
    
    /**
     * Ingresa las credenciales de usuario (username y contraseña)
     * @param username nombre de usuario
     * @param password contraseña del usuario
     */
    void ingresarCredenciales(String username, String password);
    
    /**
     * Ingresa el username en el campo correspondiente
     * @param username nombre de usuario
     */
    void ingresarUsername(String username);
    
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
    
    /**
     * Obtiene el mensaje de éxito mostrado en la página
     * @return texto del mensaje de éxito, cadena vacía si no hay mensaje
     */
    String obtenerMensajeExito();
    
    /**
     * Limpia todos los campos del formulario
     */
    void limpiarFormulario();
    
    /**
     * Navega a la página de registro desde login
     */
    void irARegistro();
    
    /**
     * Verifica si el formulario está habilitado
     * @return true si el formulario está habilitado
     */
    boolean esFormularioHabilitado();
    
    /**
     * Verifica si el botón de login está habilitado
     * @return true si el botón está habilitado
     */
    boolean esBotonLoginHabilitado();
    
    /**
     * Obtiene las credenciales válidas para este sitio
     * @return ModeloDatosPrueba con credenciales válidas
     */
    ModeloDatosPrueba obtenerCredencialesValidas();
    
    /**
     * Realiza login con las credenciales válidas predeterminadas
     * @return true si el login fue exitoso
     */
    boolean loginConCredencialesValidas();
    
    /**
     * Valida que todos los elementos esperados estén presentes
     * @return true si todos los elementos están presentes
     */
    boolean validarElementosPagina();
    
    // === MÉTODOS OPCIONALES PARA COMPATIBILIDAD ===
    
    /**
     * Verifica si hay opción de "Recordarme" (no disponible en practice.expandtesting.com)
     * @return false para este sitio
     */
    default boolean tieneOpcionRecordarme() {
        return false;
    }
    
    /**
     * Marca opción "Recordarme" (no disponible en practice.expandtesting.com)
     * @param marcar valor a establecer
     */
    default void marcarRecordarme(boolean marcar) {
        // No implementado para este sitio
    }
    
    /**
     * Verifica si hay enlace "Olvidé mi contraseña" (no disponible en practice.expandtesting.com)
     * @return false para este sitio
     */
    default boolean tieneEnlaceOlvidoPassword() {
        return false;
    }
    
    /**
     * Click en enlace "Olvidé mi contraseña" (no disponible en practice.expandtesting.com)
     */
    default void clickOlvidoPassword() {
        // No implementado para este sitio
    }
    
    /**
     * Realiza logout si está disponible
     * @return true si el logout fue exitoso
     */
    boolean realizarLogout();
    
    /**
     * Verifica si el usuario está logueado
     * @return true si está logueado
     */
    boolean estaLogueado();
}