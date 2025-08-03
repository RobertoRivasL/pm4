package com.automatizacion.proyecto.datos;

/**
 * Modelo de datos para representar información de pruebas de registro y login.
 * Implementa el patrón Builder para facilitar la creación de instancias.
 * 
 * Sigue el principio de Responsabilidad Única encapsulando
 * únicamente los datos necesarios para las pruebas.
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
public class ModeloDatosPrueba {
    
    // Campos para identificación del caso de prueba
    private String casoPrueba;
    private String descripcion;
    
    // Campos para datos de usuario
    private String email;
    private String password;
    private String confirmarPassword;
    private String nombre;
    private String apellido;
    private String telefono;
    
    // Campos para información adicional
    private String genero;
    private String pais;
    private String ciudad;
    private String fechaNacimiento;
    
    // Campos para validación
    private boolean esValido;
    private String resultadoEsperado;
    private String mensajeError;
    
    // Constructor privado para usar con Builder
    private ModeloDatosPrueba(Builder builder) {
        this.casoPrueba = builder.casoPrueba;
        this.descripcion = builder.descripcion;
        this.email = builder.email;
        this.password = builder.password;
        this.confirmarPassword = builder.confirmarPassword;
        this.nombre = builder.nombre;
        this.apellido = builder.apellido;
        this.telefono = builder.telefono;
        this.genero = builder.genero;
        this.pais = builder.pais;
        this.ciudad = builder.ciudad;
        this.fechaNacimiento = builder.fechaNacimiento;
        this.esValido = builder.esValido;
        this.resultadoEsperado = builder.resultadoEsperado;
        this.mensajeError = builder.mensajeError;
    }
    
    // Constructor por defecto
    public ModeloDatosPrueba() {
    }
    
    // Getters
    public String getCasoPrueba() { return casoPrueba; }
    public String getDescripcion() { return descripcion; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getConfirmarPassword() { return confirmarPassword; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getTelefono() { return telefono; }
    public String getGenero() { return genero; }
    public String getPais() { return pais; }
    public String getCiudad() { return ciudad; }
    public String getFechaNacimiento() { return fechaNacimiento; }
    public boolean isEsValido() { return esValido; }
    public boolean getEsValido() { return esValido; } // Método alternativo
    public String getResultadoEsperado() { return resultadoEsperado; }
    public String getMensajeError() { return mensajeError; }
    
    // Setters
    public void setCasoPrueba(String casoPrueba) { this.casoPrueba = casoPrueba; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setConfirmarPassword(String confirmarPassword) { this.confirmarPassword = confirmarPassword; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setGenero(String genero) { this.genero = genero; }
    public void setPais(String pais) { this.pais = pais; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public void setEsValido(boolean esValido) { this.esValido = esValido; }
    public void setResultadoEsperado(String resultadoEsperado) { this.resultadoEsperado = resultadoEsperado; }
    public void setMensajeError(String mensajeError) { this.mensajeError = mensajeError; }
    
    /**
     * Método estático para crear un Builder
     */
    public static Builder builder() {
        return new Builder();
    }
    
    /**
     * Clase Builder para facilitar la creación de instancias
     */
    public static class Builder {
        private String casoPrueba;
        private String descripcion;
        private String email;
        private String password;
        private String confirmarPassword;
        private String nombre;
        private String apellido;
        private String telefono;
        private String genero;
        private String pais;
        private String ciudad;
        private String fechaNacimiento;
        private boolean esValido = true;
        private String resultadoEsperado;
        private String mensajeError;
        
        public Builder casoPrueba(String casoPrueba) {
            this.casoPrueba = casoPrueba;
            return this;
        }
        
        public Builder descripcion(String descripcion) {
            this.descripcion = descripcion;
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
        
        public Builder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }
        
        public Builder apellido(String apellido) {
            this.apellido = apellido;
            return this;
        }
        
        public Builder telefono(String telefono) {
            this.telefono = telefono;
            return this;
        }
        
        public Builder genero(String genero) {
            this.genero = genero;
            return this;
        }
        
        public Builder pais(String pais) {
            this.pais = pais;
            return this;
        }
        
        public Builder ciudad(String ciudad) {
            this.ciudad = ciudad;
            return this;
        }
        
        public Builder fechaNacimiento(String fechaNacimiento) {
            this.fechaNacimiento = fechaNacimiento;
            return this;
        }
        
        public Builder esValido(boolean esValido) {
            this.esValido = esValido;
            return this;
        }
        
        public Builder resultadoEsperado(String resultadoEsperado) {
            this.resultadoEsperado = resultadoEsperado;
            return this;
        }
        
        public Builder mensajeError(String mensajeError) {
            this.mensajeError = mensajeError;
            return this;
        }
        
        public ModeloDatosPrueba build() {
            return new ModeloDatosPrueba(this);
        }
    }
    
    /**
     * Valida si los datos son consistentes
     */
    public boolean sonDatosValidos() {
        // Validaciones básicas
        if (email == null || email.trim().isEmpty()) return false;
        if (password == null || password.trim().isEmpty()) return false;
        
        // Si hay confirmación de password, debe coincidir
        if (confirmarPassword != null && !password.equals(confirmarPassword)) return false;
        
        return true;
    }
    
    /**
     * Crea una copia de los datos actuales
     */
    public ModeloDatosPrueba copia() {
        return ModeloDatosPrueba.builder()
                .casoPrueba(this.casoPrueba)
                .descripcion(this.descripcion)
                .email(this.email)
                .password(this.password)
                .confirmarPassword(this.confirmarPassword)
                .nombre(this.nombre)
                .apellido(this.apellido)
                .telefono(this.telefono)
                .genero(this.genero)
                .pais(this.pais)
                .ciudad(this.ciudad)
                .fechaNacimiento(this.fechaNacimiento)
                .esValido(this.esValido)
                .resultadoEsperado(this.resultadoEsperado)
                .mensajeError(this.mensajeError)
                .build();
    }
    
    @Override
    public String toString() {
        return String.format("ModeloDatosPrueba{casoPrueba='%s', email='%s', esValido=%s}", 
                           casoPrueba, email, esValido);
    }
}