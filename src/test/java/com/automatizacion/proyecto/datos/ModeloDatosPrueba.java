package com.automatizacion.proyecto.datos;

/**
 * Modelo de datos que encapsula toda la información necesaria para las pruebas.
 * Implementa el patrón Builder para facilitar la creación de objetos inmutables.
 * 
 * Principios SOLID aplicados:
 * - SRP: Responsabilidad única de encapsular datos de prueba
 * - OCP: Abierto para extensión, cerrado para modificación
 * - Encapsulación: Datos privados con getters públicos
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
public class ModeloDatosPrueba {
    
    // ===== CAMPOS PRINCIPALES =====
    private final String casoPrueba;
    private final String nombre;
    private final String apellido;
    private final String email;
    private final String password;
    private final String confirmarPassword;
    private final String telefono;
    private final String fechaNacimiento;
    private final String genero;
    private final boolean aceptarTerminos;
    private final boolean suscribirseNewsletter;
    private final boolean recordarme;
    private final boolean esValido;
    private final String mensajeError;
    private final String descripcion;
    
    // ===== CONSTRUCTOR PRIVADO =====
    private ModeloDatosPrueba(Builder builder) {
        this.casoPrueba = builder.casoPrueba;
        this.nombre = builder.nombre;
        this.apellido = builder.apellido;
        this.email = builder.email;
        this.password = builder.password;
        this.confirmarPassword = builder.confirmarPassword;
        this.telefono = builder.telefono;
        this.fechaNacimiento = builder.fechaNacimiento;
        this.genero = builder.genero;
        this.aceptarTerminos = builder.aceptarTerminos;
        this.suscribirseNewsletter = builder.suscribirseNewsletter;
        this.recordarme = builder.recordarme;
        this.esValido = builder.esValido;
        this.mensajeError = builder.mensajeError;
        this.descripcion = builder.descripcion;
    }
    
    // ===== GETTERS =====
    
    public String getCasoPrueba() {
        return casoPrueba;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public String getApellido() {
        return apellido;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public String getConfirmarPassword() {
        return confirmarPassword;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public String getFechaNacimiento() {
        return fechaNacimiento;
    }
    
    public String getGenero() {
        return genero;
    }
    
    public boolean isAceptarTerminos() {
        return aceptarTerminos;
    }
    
    public boolean isSuscribirseNewsletter() {
        return suscribirseNewsletter;
    }
    
    public boolean isRecordarme() {
        return recordarme;
    }
    
    public boolean isEsValido() {
        return esValido;
    }
    
    public String getMensajeError() {
        return mensajeError;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    // ===== MÉTODOS UTILITARIOS =====
    
    /**
     * Obtiene el nombre completo concatenando nombre y apellido.
     */
    public String getNombreCompleto() {
        if (nombre != null && apellido != null) {
            return nombre + " " + apellido;
        } else if (nombre != null) {
            return nombre;
        } else if (apellido != null) {
            return apellido;
        }
        return "";
    }
    
    /**
     * Verifica si los passwords coinciden.
     */
    public boolean passwordsCoinciden() {
        if (password == null || confirmarPassword == null) {
            return false;
        }
        return password.equals(confirmarPassword);
    }
    
    /**
     * Verifica si es un caso de prueba de registro válido.
     */
    public boolean esCasoRegistroValido() {
        return nombre != null && !nombre.trim().isEmpty() &&
               apellido != null && !apellido.trim().isEmpty() &&
               email != null && !email.trim().isEmpty() &&
               password != null && !password.trim().isEmpty() &&
               passwordsCoinciden() &&
               aceptarTerminos;
    }
    
    /**
     * Verifica si es un caso de prueba de login válido.
     */
    public boolean esCasoLoginValido() {
        return email != null && !email.trim().isEmpty() &&
               password != null && !password.trim().isEmpty();
    }
    
    @Override
    public String toString() {
        return "ModeloDatosPrueba{" +
                "casoPrueba='" + casoPrueba + '\'' +
                ", email='" + email + '\'' +
                ", nombre='" + getNombreCompleto() + '\'' +
                ", esValido=" + esValido +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
    
    // ===== BUILDER PATTERN =====
    
    /**
     * Método estático para crear un nuevo builder.
     */
    public static Builder builder() {
        return new Builder();
    }
    
    /**
     * Clase Builder para construcción fluida de objetos ModeloDatosPrueba.
     * Implementa el patrón Builder para facilitar la creación de objetos complejos.
     */
    public static class Builder {
        private String casoPrueba;
        private String nombre;
        private String apellido;
        private String email;
        private String password;
        private String confirmarPassword;
        private String telefono;
        private String fechaNacimiento;
        private String genero;
        private boolean aceptarTerminos = false;
        private boolean suscribirseNewsletter = false;
        private boolean recordarme = false;
        private boolean esValido = true;
        private String mensajeError;
        private String descripcion;
        
        public Builder casoPrueba(String casoPrueba) {
            this.casoPrueba = casoPrueba;
            return this;
        }
        
        public Builder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }
        
        public Builder apellido(String apellido) {
            this.apellido = apellido;
            return this;
        }
        
        public Builder email(String email) {
            this.email = email;
            return this;
        }
        
        public Builder password(String password) {
            this.password = password;
            return this;
        }
        
        public Builder confirmarPassword(String confirmarPassword) {
            this.confirmarPassword = confirmarPassword;
            return this;
        }
        
        public Builder telefono(String telefono) {
            this.telefono = telefono;
            return this;
        }
        
        public Builder fechaNacimiento(String fechaNacimiento) {
            this.fechaNacimiento = fechaNacimiento;
            return this;
        }
        
        public Builder genero(String genero) {
            this.genero = genero;
            return this;
        }
        
        public Builder aceptarTerminos(boolean aceptarTerminos) {
            this.aceptarTerminos = aceptarTerminos;
            return this;
        }
        
        public Builder suscribirseNewsletter(boolean suscribirseNewsletter) {
            this.suscribirseNewsletter = suscribirseNewsletter;
            return this;
        }
        
        public Builder recordarme(boolean recordarme) {
            this.recordarme = recordarme;
            return this;
        }
        
        public Builder esValido(boolean esValido) {
            this.esValido = esValido;
            return this;
        }
        
        public Builder mensajeError(String mensajeError) {
            this.mensajeError = mensajeError;
            return this;
        }
        
        public Builder descripcion(String descripcion) {
            this.descripcion = descripcion;
            return this;
        }
        
        /**
         * Método de conveniencia para casos de login.
         */
        public Builder datosLogin(String email, String password) {
            this.email = email;
            this.password = password;
            return this;
        }
        
        /**
         * Método de conveniencia para casos de registro básico.
         */
        public Builder datosRegistroBasico(String nombre, String apellido, String email, String password) {
            this.nombre = nombre;
            this.apellido = apellido;
            this.email = email;
            this.password = password;
            this.confirmarPassword = password;
            this.aceptarTerminos = true;
            return this;
        }
        
        /**
         * Método de conveniencia para casos de prueba inválidos.
         */
        public Builder casoInvalido(String mensajeError) {
            this.esValido = false;
            this.mensajeError = mensajeError;
            return this;
        }
        
        /**
         * Construye el objeto ModeloDatosPrueba final.
         */
        public ModeloDatosPrueba build() {
            // Validación básica antes de construir
            if (casoPrueba == null || casoPrueba.trim().isEmpty()) {
                throw new IllegalArgumentException("El caso de prueba es obligatorio");
            }
            
            return new ModeloDatosPrueba(this);
        }
    }
    
    // ===== MÉTODOS FACTORY PARA CASOS COMUNES =====
    
    /**
     * Crea un modelo para login válido.
     */
    public static ModeloDatosPrueba loginValido(String casoPrueba, String email, String password) {
        return builder()
                .casoPrueba(casoPrueba)
                .email(email)
                .password(password)
                .descripcion("Caso de login válido")
                .build();
    }
    
    /**
     * Crea un modelo para login inválido.
     */
    public static ModeloDatosPrueba loginInvalido(String casoPrueba, String email, String password, String mensajeError) {
        return builder()
                .casoPrueba(casoPrueba)
                .email(email)
                .password(password)
                .esValido(false)
                .mensajeError(mensajeError)
                .descripcion("Caso de login inválido")
                .build();
    }
    
    /**
     * Crea un modelo para registro válido.
     */
    public static ModeloDatosPrueba registroValido(String casoPrueba, String nombre, String apellido, String email, String password) {
        return builder()
                .casoPrueba(casoPrueba)
                .datosRegistroBasico(nombre, apellido, email, password)
                .descripcion("Caso de registro válido")
                .build();
    }
    
    /**
     * Crea un modelo para registro inválido.
     */
    public static ModeloDatosPrueba registroInvalido(String casoPrueba, String nombre, String apellido, String email, String password, String mensajeError) {
        return builder()
                .casoPrueba(casoPrueba)
                .nombre(nombre)
                .apellido(apellido)
                .email(email)
                .password(password)
                .confirmarPassword(password)
                .esValido(false)
                .mensajeError(mensajeError)
                .descripcion("Caso de registro inválido")
                .build();
    }
}