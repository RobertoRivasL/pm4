package com.automatizacion.proyecto.datos;

import java.util.regex.Pattern;

public class ModeloDatosPrueba {
    
    private String casoPrueba;
    private String descripcion;
    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private String confirmacionPassword;
    private String telefono;
    private boolean esValido = true;
    private boolean aceptarTerminos = true;
    private String resultadoEsperado;
    private String mensajeError;
    
    private static final Pattern PATRON_EMAIL = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    
    // Constructor vacío
    public ModeloDatosPrueba() {}
    
    // Constructor con parámetros básicos
    public ModeloDatosPrueba(String casoPrueba, String nombre, String email, String password) {
        this.casoPrueba = casoPrueba;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.confirmacionPassword = password;
    }
    
    // Builder estático
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private ModeloDatosPrueba modelo = new ModeloDatosPrueba();
        
        public Builder casoPrueba(String casoPrueba) { modelo.casoPrueba = casoPrueba; return this; }
        public Builder descripcion(String descripcion) { modelo.descripcion = descripcion; return this; }
        public Builder nombre(String nombre) { modelo.nombre = nombre; return this; }
        public Builder apellido(String apellido) { modelo.apellido = apellido; return this; }
        public Builder email(String email) { modelo.email = email; return this; }
        public Builder password(String password) { modelo.password = password; return this; }
        public Builder confirmacionPassword(String confirmacionPassword) { modelo.confirmacionPassword = confirmacionPassword; return this; }
        public Builder telefono(String telefono) { modelo.telefono = telefono; return this; }
        public Builder esValido(boolean esValido) { modelo.esValido = esValido; return this; }
        public Builder aceptarTerminos(boolean aceptarTerminos) { modelo.aceptarTerminos = aceptarTerminos; return this; }
        public Builder resultadoEsperado(String resultadoEsperado) { modelo.resultadoEsperado = resultadoEsperado; return this; }
        public Builder mensajeError(String mensajeError) { modelo.mensajeError = mensajeError; return this; }
        
        public ModeloDatosPrueba build() { return modelo; }
    }
    
    // Métodos de validación
    public boolean camposObligatoriosCompletos() {
        return nombre != null && !nombre.trim().isEmpty() &&
               email != null && !email.trim().isEmpty() &&
               password != null && !password.trim().isEmpty();
    }
    
    public boolean esEmailValido() {
        return email != null && PATRON_EMAIL.matcher(email.trim()).matches();
    }
    
    public boolean passwordsCoinciden() {
        return password != null && password.equals(confirmacionPassword);
    }
    
    // Getters y Setters
    public String getCasoPrueba() { return casoPrueba; }
    public void setCasoPrueba(String casoPrueba) { this.casoPrueba = casoPrueba; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getConfirmacionPassword() { return confirmacionPassword; }
    public void setConfirmacionPassword(String confirmacionPassword) { this.confirmacionPassword = confirmacionPassword; }
    
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    public boolean isEsValido() { return esValido; }
    public void setEsValido(boolean esValido) { this.esValido = esValido; }
    
    public boolean isAceptarTerminos() { return aceptarTerminos; }
    public void setAceptarTerminos(boolean aceptarTerminos) { this.aceptarTerminos = aceptarTerminos; }
    
    public String getResultadoEsperado() { return resultadoEsperado; }
    public void setResultadoEsperado(String resultadoEsperado) { this.resultadoEsperado = resultadoEsperado; }
    
    public String getMensajeError() { return mensajeError; }
    public void setMensajeError(String mensajeError) { this.mensajeError = mensajeError; }
}