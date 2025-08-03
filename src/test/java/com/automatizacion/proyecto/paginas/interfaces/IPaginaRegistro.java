package com.automatizacion.proyecto.paginas.interfaces;

import com.automatizacion.proyecto.datos.ModeloDatosPrueba;

/**
 * Interfaz que define las operaciones específicas para la página de registro de usuarios.
 * Específicamente adaptada para practice.expandtesting.com que tiene campos simplificados.
 * 
 * Extiende IPaginaBase para heredar operaciones básicas e implementa
 * el principio de Segregación de Interfaces del SOLID.
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
public interface IPaginaRegistro extends IPaginaBase {
    
    // === MÉTODOS PRINCIPALES DE REGISTRO ===
    
    /**
     * Llena todos los campos del formulario de registro
     * @param datos modelo con todos los datos necesarios para el registro
     */
    void llenarFormularioCompleto(ModeloDatosPrueba datos);
    
    /**
     * Ingresa el nombre de usuario (username) en el campo correspondiente
     * En practice.expandtesting.com este campo actúa como email/username
     * @param username nombre de usuario o email
     */
    void ingresarUsername(String username);
    
    /**
     * Ingresa la contraseña en el campo correspondiente
     * @param password contraseña del usuario
     */
    void ingresarPassword(String password);
    
    /**
     * Ingresa la confirmación de contraseña
     * @param confirmarPassword confirmación de la contraseña
     */
    void ingresarConfirmPassword(String confirmarPassword);
    
    /**
     * Hace click en el botón de registro
     */
    void clickBotonRegistro();
    
    /**
     * Ejecuta el proceso completo de registro de usuario
     * @param datos modelo con los datos de registro
     * @return true si el proceso se ejecutó correctamente
     */
    boolean registrarUsuario(ModeloDatosPrueba datos);
    
    // === MÉTODOS DE VALIDACIÓN Y ESTADO ===
    
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
    
    /**
     * Obtiene el mensaje de éxito mostrado en la página
     * @return texto del mensaje de éxito, cadena vacía si no hay mensaje
     */
    String obtenerMensajeExito();
    
    /**
     * Valida que todos los elementos esperados estén presentes
     * @return true si todos los elementos están presentes
     */
    boolean validarElementosPagina();
    
    // === MÉTODOS DE CONTROL DEL FORMULARIO ===
    
    /**
     * Limpia todos los campos del formulario
     */
    void limpiarFormulario();
    
    /**
     * Navega a la página de login desde registro
     */
    void irALogin();
    
    /**
     * Verifica si el formulario está habilitado
     * @return true si el formulario está habilitado
     */
    boolean esFormularioHabilitado();
    
    /**
     * Verifica si el botón de registro está habilitado
     * @return true si el botón está habilitado
     */
    boolean esBotonRegistroHabilitado();
    
    // === MÉTODOS OPCIONALES PARA COMPATIBILIDAD CON OTROS SITIOS ===
    
    /**
     * Ingresa el nombre (si está disponible en la página)
     * En practice.expandtesting.com este método no hace nada
     * @param nombre nombre del usuario
     */
    default void ingresarNombre(String nombre) {
        // Implementación por defecto vacía para compatibilidad
    }
    
    /**
     * Ingresa el apellido (si está disponible en la página)
     * En practice.expandtesting.com este método no hace nada
     * @param apellido apellido del usuario
     */
    default void ingresarApellido(String apellido) {
        // Implementación por defecto vacía para compatibilidad
    }
    
    /**
     * Ingresa el email (si es diferente del username)
     * En practice.expandtesting.com usa el campo username
     * @param email dirección de email
     */
    default void ingresarEmail(String email) {
        ingresarUsername(email); // Usa username como email
    }
    
    /**
     * Ingresa el teléfono (si está disponible)
     * En practice.expandtesting.com este método no hace nada
     * @param telefono número de teléfono
     */
    default void ingresarTelefono(String telefono) {
        // Implementación por defecto vacía para compatibilidad
    }
    
    /**
     * Acepta términos y condiciones (si están disponibles)
     * En practice.expandtesting.com este método no hace nada
     * @param aceptar true para aceptar, false para rechazar
     */
    default void aceptarTerminos(boolean aceptar) {
        // Implementación por defecto vacía para compatibilidad
    }
    
    /**
     * Suscribirse al newsletter (si está disponible)
     * En practice.expandtesting.com este método no hace nada
     * @param suscribir true para suscribirse, false para no suscribirse
     */
    default void suscribirNewsletter(boolean suscribir) {
        // Implementación por defecto vacía para compatibilidad
    }
    
    /**
     * Verifica si hay checkboxes de términos y condiciones
     * @return true si están presentes
     */
    default boolean tieneTerminosYCondiciones() {
        return false; // No hay términos en practice.expandtesting.com
    }
    
    /**
     * Verifica si hay opción de newsletter
     * @return true si está presente
     */
    default boolean tieneOpcionNewsletter() {
        return false; // No hay newsletter en practice.expandtesting.com
    }
    
    // === MÉTODOS ADICIONALES DE UTILIDAD ===
    
    /**
     * Obtiene texto de ayuda o instrucciones si están disponibles
     * @return texto de ayuda o cadena vacía
     */
    default String obtenerTextoAyuda() {
        return "Test Register page for Automation Testing Practice";
    }
    
    /**
     * Verifica si hay errores específicos en campos individuales
     * @param campo nombre del campo a verificar
     * @return mensaje de error del campo específico
     */
    default String obtenerErrorCampo(String campo) {
        return "";
    }
    
    /**
     * Valida formato de email en tiempo real
     * @param email email a validar
     * @return true si el formato es válido
     */
    default boolean validarFormatoEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        String patronEmail = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(patronEmail);
    }
    
    /**
     * Valida fortaleza de contraseña en tiempo real
     * @param password contraseña a validar
     * @return true si la contraseña es fuerte
     */
    default boolean validarFortalezaPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        boolean tieneMayuscula = password.matches(".*[A-Z].*");
        boolean tieneMinuscula = password.matches(".*[a-z].*");
        boolean tieneNumero = password.matches(".*\\d.*");
        boolean tieneEspecial = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
        
        return tieneMayuscula && tieneMinuscula && tieneNumero && tieneEspecial;
    }
    
    /**
     * Verifica si las contraseñas coinciden en tiempo real
     * @return true si las contraseñas coinciden
     */
    default boolean validarCoincidenciaPasswords() {
        return true; // Implementación específica en la página
    }
    
    /**
     * Genera datos de registro válidos únicos
     * @return ModeloDatosPrueba con datos únicos válidos
     */
    default ModeloDatosPrueba generarDatosRegistroValidos() {
        long timestamp = System.currentTimeMillis();
        
        return ModeloDatosPrueba.builder()
            .casoPrueba("AUTO_GENERATED_" + timestamp)
            .descripcion("Datos generados automáticamente")
            .email("user." + timestamp + "@test.com")
            .password("AutoPassword123!")
            .confirmacionPassword("AutoPassword123!")
            .esValido(true)
            .build();
    }
    
    /**
     * Limpia un campo específico
     * @param campo nombre del campo a limpiar
     */
    default void limpiarCampo(String campo) {
        // Implementación específica en la página
    }
    
    /**
     * Verifica si un campo específico está habilitado
     * @param campo nombre del campo a verificar
     * @return true si el campo está habilitado
     */
    default boolean esCampoHabilitado(String campo) {
        return true; // Implementación específica en la página
    }
    
    /**
     * Obtiene el valor actual de un campo
     * @param campo nombre del campo
     * @return valor actual del campo
     */
    default String obtenerValorCampo(String campo) {
        return ""; // Implementación específica en la página
    }
    
    /**
     * Verifica si un campo específico es obligatorio
     * @param campo nombre del campo
     * @return true si el campo es obligatorio
     */
    default boolean esCampoObligatorio(String campo) {
        // En practice.expandtesting.com, todos los campos son obligatorios
        return true;
    }
}