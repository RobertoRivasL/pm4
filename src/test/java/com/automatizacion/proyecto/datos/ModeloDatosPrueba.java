package com.automatizacion.proyecto.datos;

/**
 * Modelo de datos para pruebas automatizadas.
 * Utiliza el patrón Builder para construcción flexible.
 * 
 * @author Roberto Rivas Lopez
 */
public class ModeloDatosPrueba {
    
    private String casoPrueba;
    private String descripcion;
    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private String confirmacionPassword;
    private String telefono;
    private String mensajeEsperado;
    private boolean esValido;
    
    // Constructor privado para forzar uso del Builder
    private ModeloDatosPrueba() {}
    
    // === GETTERS ===
    
    public String getCasoPrueba() { return casoPrueba; }
    public String getDescripcion() { return descripcion; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getConfirmacionPassword() { return confirmacionPassword; }
    public String getTelefono() { return telefono; }
    public String getMensajeEsperado() { return mensajeEsperado; }
    public boolean isEsValido() { return esValido; }
    
    // === MÉTODOS DE CONVENIENCIA ===
    
    /**
     * Obtiene el nombre completo concatenado
     * @return nombre + apellido
     */
    public String obtenerNombreCompleto() {
        return (nombre != null ? nombre : "") + " " + (apellido != null ? apellido : "");
    }
    
    // === MÉTODOS ESTÁTICOS DE FACTORY ===
    
    /**
     * Crea datos válidos por defecto
     * @return ModeloDatosPrueba con datos válidos
     */
    public static ModeloDatosPrueba crearDatosValidos() {
        return builder()
            .casoPrueba("DEFAULT_VALID")
            .descripcion("Datos válidos por defecto")
            .nombre("Usuario")
            .apellido("Prueba")
            .email("usuario.valido@test.com")
            .password("Password123!")
            .confirmacionPassword("Password123!")
            .telefono("123456789")
            .esValido(true)
            .build();
    }
    
    /**
     * Crea datos para login válido
     * @param casoPrueba identificador del caso
     * @param email email del usuario
     * @param password contraseña
     * @return ModeloDatosPrueba configurado para login
     */
    public static ModeloDatosPrueba loginValido(String casoPrueba, String email, String password) {
        return builder()
            .casoPrueba(casoPrueba)
            .descripcion("Login válido")
            .email(email)
            .password(password)
            .esValido(true)
            .build();
    }
    
    /**
     * Crea datos para login inválido
     * @param casoPrueba identificador del caso
     * @param email email del usuario
     * @param password contraseña
     * @param mensajeEsperado mensaje de error esperado
     * @return ModeloDatosPrueba configurado para login inválido
     */
    public static ModeloDatosPrueba loginInvalido(String casoPrueba, String email, String password, String mensajeEsperado) {
        return builder()
            .casoPrueba(casoPrueba)
            .descripcion("Login inválido")
            .email(email)
            .password(password)
            .mensajeEsperado(mensajeEsperado)
            .esValido(false)
            .build();
    }
    
    /**
     * Crea datos para registro válido
     * @param casoPrueba identificador del caso
     * @param nombre nombre del usuario
     * @param apellido apellido del usuario
     * @param email email del usuario
     * @param password contraseña
     * @return ModeloDatosPrueba configurado para registro
     */
    public static ModeloDatosPrueba registroValido(String casoPrueba, String nombre, String apellido, String email, String password) {
        return builder()
            .casoPrueba(casoPrueba)
            .descripcion("Registro válido")
            .nombre(nombre)
            .apellido(apellido)
            .email(email)
            .password(password)
            .confirmacionPassword(password)
            .telefono("123456789")
            .esValido(true)
            .build();
    }
    
    /**
     * Crea datos para registro inválido
     * @param casoPrueba identificador del caso
     * @param nombre nombre del usuario
     * @param apellido apellido del usuario
     * @param email email del usuario
     * @param password contraseña
     * @param mensajeEsperado mensaje de error esperado
     * @return ModeloDatosPrueba configurado para registro inválido
     */
    public static ModeloDatosPrueba registroInvalido(String casoPrueba, String nombre, String apellido, String email, String password, String mensajeEsperado) {
        return builder()
            .casoPrueba(casoPrueba)
            .descripcion("Registro inválido")
            .nombre(nombre)
            .apellido(apellido)
            .email(email)
            .password(password)
            .confirmacionPassword(password)
            .mensajeEsperado(mensajeEsperado)
            .esValido(false)
            .build();
    }
    
    // === BUILDER PATTERN ===
    
    /**
     * Obtiene una nueva instancia del Builder
     * @return Builder para construir ModeloDatosPrueba
     */
    public static Builder builder() {
        return new Builder();
    }
    
    /**
     * Clase Builder para construcción flexible de ModeloDatosPrueba
     */
    public static class Builder {
        private final ModeloDatosPrueba modelo = new ModeloDatosPrueba();
        
        public Builder casoPrueba(String casoPrueba) {
            modelo.casoPrueba = casoPrueba;
            return this;
        }
        
        public Builder descripcion(String descripcion) {
            modelo.descripcion = descripcion;
            return this;
        }
        
        public Builder nombre(String nombre) {
            modelo.nombre = nombre;
            return this;
        }
        
        public Builder apellido(String apellido) {
            modelo.apellido = apellido;
            return this;
        }
        
        public Builder email(String email) {
            modelo.email = email;
            return this;
        }
        
        public Builder password(String password) {
            modelo.password = password;
            return this;
        }
        
        public Builder confirmacionPassword(String confirmacionPassword) {
            modelo.confirmacionPassword = confirmacionPassword;
            return this;
        }
        
        public Builder telefono(String telefono) {
            modelo.telefono = telefono;
            return this;
        }
        
        public Builder mensajeEsperado(String mensajeEsperado) {
            modelo.mensajeEsperado = mensajeEsperado;
            return this;
        }
        
        public Builder esValido(boolean esValido) {
            modelo.esValido = esValido;
            return this;
        }
        
        /**
         * Construye la instancia final de ModeloDatosPrueba
         * @return ModeloDatosPrueba configurado
         */
        public ModeloDatosPrueba build() {
            // Validaciones básicas antes de construir
            if (modelo.casoPrueba == null || modelo.casoPrueba.trim().isEmpty()) {
                modelo.casoPrueba = "CASO_" + System.currentTimeMillis();
            }
            
            return modelo;
        }
    }
    
    // === MÉTODOS DE UTILIDAD ===
    
    @Override
    public String toString() {
        return String.format("ModeloDatosPrueba{casoPrueba='%s', email='%s', esValido=%s}", 
                           casoPrueba, email, esValido);
    }
}