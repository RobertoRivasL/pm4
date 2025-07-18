package com.robertorivas.automatizacion.modelos;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Clase que representa un usuario del sistema.
 * VERSIÓN REFACTORIZADA: Código limpio, sin duplicaciones y funcional.
 * 
 * @author Roberto Rivas Lopez
 */
public class Usuario {
    
    // Patrones de validación optimizados
    private static final Pattern PATRON_EMAIL = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$"
    );
    
    // Patrón más flexible para usernames (permite "student", "admin", etc.)
    private static final Pattern PATRON_USERNAME = Pattern.compile(
        "^[A-Za-z0-9][A-Za-z0-9._-]*$"
    );
    
    private static final int LONGITUD_MINIMA_PASSWORD = 3; // Más flexible para pruebas
    private static final int LONGITUD_MAXIMA_PASSWORD = 100;
    
    // Campos del usuario
    private final String id;
    private String email; // Puede ser email completo o username simple
    private String password;
    private String nombre;
    private String apellido;
    private String telefono;
    private EstadoUsuario estado;
    private final LocalDateTime fechaCreacion;
    private LocalDateTime fechaUltimaModificacion;
    
    // Enum para estado del usuario
    public enum EstadoUsuario {
        ACTIVO("Activo"),
        INACTIVO("Inactivo"),
        BLOQUEADO("Bloqueado"),
        PENDIENTE_VERIFICACION("Pendiente de Verificación");
        
        private final String descripcion;
        
        EstadoUsuario(String descripcion) {
            this.descripcion = descripcion;
        }
        
        public String getDescripcion() {
            return descripcion;
        }
    }
    
    // ===== CONSTRUCTORES =====
    
    /**
     * Constructor principal con validaciones flexibles.
     */
    public Usuario(String id, String email, String password) {
        this.id = validarId(id);
        this.email = validarEmailOUsername(email);
        this.password = validarFormatoPassword(password);
        this.estado = EstadoUsuario.ACTIVO;
        this.fechaCreacion = LocalDateTime.now();
        this.fechaUltimaModificacion = LocalDateTime.now();
    }
    
    /**
     * Constructor con datos completos.
     */
    public Usuario(String id, String email, String password, String nombre, String apellido) {
        this(id, email, password);
        this.nombre = limpiarTexto(nombre);
        this.apellido = limpiarTexto(apellido);
    }
    
    /**
     * Constructor para pruebas (solo email y password).
     */
    public Usuario(String email, String password) {
        this(generarIdAleatorio(), email, password);
    }
    
    // ===== MÉTODOS DE VALIDACIÓN PRIVADOS =====
    
    private String validarId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo o vacío");
        }
        return id.trim();
    }
    
    /**
     * Valida email completo O username simple (SIN DUPLICACIÓN).
     */
    private String validarEmailOUsername(String emailOUsername) {
        if (emailOUsername == null || emailOUsername.trim().isEmpty()) {
            throw new IllegalArgumentException("El email/username no puede ser nulo o vacío");
        }
        
        String valor = emailOUsername.trim();
        
        // Si contiene @, debe ser un email válido
        if (valor.contains("@")) {
            if (PATRON_EMAIL.matcher(valor).matches()) {
                return valor.toLowerCase();
            } else {
                throw new IllegalArgumentException("El formato del email no es válido: " + emailOUsername);
            }
        }
        
        // Si no contiene @, debe ser un username válido
        if (PATRON_USERNAME.matcher(valor).matches() && valor.length() >= 2 && valor.length() <= 50) {
            return valor; // Mantener case original para usernames
        }
        
        throw new IllegalArgumentException("El formato del email/username no es válido: " + emailOUsername);
    }
    
    /**
     * Valida formato de password (renombrado para evitar conflicto).
     */
    private String validarFormatoPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede ser nula o vacía");
        }
        
        if (password.length() < LONGITUD_MINIMA_PASSWORD) {
            throw new IllegalArgumentException(
                String.format("La contraseña debe tener al menos %d caracteres", LONGITUD_MINIMA_PASSWORD)
            );
        }
        
        if (password.length() > LONGITUD_MAXIMA_PASSWORD) {
            throw new IllegalArgumentException(
                String.format("La contraseña no puede tener más de %d caracteres", LONGITUD_MAXIMA_PASSWORD)
            );
        }
        
        return password;
    }
    
    private String limpiarTexto(String texto) {
        return (texto != null && !texto.trim().isEmpty()) ? texto.trim() : null;
    }
    
    private static String generarIdAleatorio() {
        return "user_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
    }
    
    // ===== GETTERS =====
    
    public String getId() { return id; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getTelefono() { return telefono; }
    public EstadoUsuario getEstado() { return estado; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public LocalDateTime getFechaUltimaModificacion() { return fechaUltimaModificacion; }
    
    // ===== SETTERS CON VALIDACIÓN =====
    
    public void setEmail(String email) {
        this.email = validarEmailOUsername(email);
        this.fechaUltimaModificacion = LocalDateTime.now();
    }
    
    public void setPassword(String password) {
        this.password = validarFormatoPassword(password);
        this.fechaUltimaModificacion = LocalDateTime.now();
    }
    
    public void setNombre(String nombre) {
        this.nombre = limpiarTexto(nombre);
        this.fechaUltimaModificacion = LocalDateTime.now();
    }
    
    public void setApellido(String apellido) {
        this.apellido = limpiarTexto(apellido);
        this.fechaUltimaModificacion = LocalDateTime.now();
    }
    
    public void setTelefono(String telefono) {
        this.telefono = limpiarTexto(telefono);
        this.fechaUltimaModificacion = LocalDateTime.now();
    }
    
    public void setEstado(EstadoUsuario estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado del usuario no puede ser nulo");
        }
        this.estado = estado;
        this.fechaUltimaModificacion = LocalDateTime.now();
    }
    
    // ===== MÉTODOS DE UTILIDAD =====
    
    /**
     * Obtiene el nombre completo del usuario.
     */
    public String getNombreCompleto() {
        StringBuilder nombreCompleto = new StringBuilder();
        
        if (nombre != null && !nombre.isEmpty()) {
            nombreCompleto.append(nombre);
        }
        
        if (apellido != null && !apellido.isEmpty()) {
            if (nombreCompleto.length() > 0) {
                nombreCompleto.append(" ");
            }
            nombreCompleto.append(apellido);
        }
        
        return nombreCompleto.length() > 0 ? nombreCompleto.toString() : email;
    }
    
    /**
     * Compara passwords (renombrado para evitar conflicto con validación).
     */
    public boolean compararPassword(String passwordAValidar) {
        return this.password != null && this.password.equals(passwordAValidar);
    }
    
    /**
     * Método para compatibilidad con código existente.
     */
    public boolean validarPassword(String passwordAValidar) {
        return compararPassword(passwordAValidar);
    }
    
    /**
     * Verifica si el usuario está activo.
     */
    public boolean estaActivo() {
        return estado == EstadoUsuario.ACTIVO;
    }
    
    /**
     * Verifica si el usuario está bloqueado.
     */
    public boolean estaBloqueado() {
        return estado == EstadoUsuario.BLOQUEADO;
    }
    
    /**
     * Activa el usuario.
     */
    public void activar() {
        setEstado(EstadoUsuario.ACTIVO);
    }
    
    /**
     * Bloquea el usuario.
     */
    public void bloquear() {
        setEstado(EstadoUsuario.BLOQUEADO);
    }
    
    /**
     * Verifica si el identificador es un email completo.
     */
    public boolean esEmailCompleto() {
        return email != null && email.contains("@") && PATRON_EMAIL.matcher(email).matches();
    }
    
    /**
     * Verifica si el identificador es un username simple.
     */
    public boolean esUsernameSimple() {
        return email != null && !email.contains("@") && PATRON_USERNAME.matcher(email).matches();
    }
    
    /**
     * Crea una copia del usuario para pruebas.
     */
    public Usuario copiarParaPruebas() {
        Usuario copia = new Usuario(this.id + "_copia", this.email, "password_test");
        copia.setNombre(this.nombre);
        copia.setApellido(this.apellido);
        copia.setTelefono(this.telefono);
        copia.setEstado(this.estado);
        return copia;
    }
    
    // ===== MÉTODOS OBJECT =====
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id) && Objects.equals(email, usuario.email);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
    
    @Override
    public String toString() {
        return String.format("Usuario{id='%s', email='%s', nombre='%s', estado=%s}", 
                           id, email, getNombreCompleto(), estado);
    }
    
    /**
     * Convierte a string sin información sensible.
     */
    public String toStringSinPassword() {
        return String.format("Usuario{id='%s', email='%s', nombre='%s', estado=%s, fechaCreacion=%s}", 
                           id, email, getNombreCompleto(), estado, fechaCreacion);
    }
    
    // ===== MÉTODOS ESTÁTICOS DE UTILIDAD =====
    
    /**
     * Crea un usuario válido para pruebas.
     */
    public static Usuario crearUsuarioPrueba() {
        return new Usuario("student", "Password123");
    }
    
    /**
     * Crea un usuario con datos específicos para pruebas.
     */
    public static Usuario crearUsuarioPrueba(String emailOUsername, String password) {
        return new Usuario(emailOUsername, password);
    }
    
    /**
     * Valida si un email tiene formato válido.
     */
    public static boolean esEmailValido(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return email.contains("@") && PATRON_EMAIL.matcher(email.trim().toLowerCase()).matches();
    }
    
    /**
     * Valida si un username es válido.
     */
    public static boolean esUsernameValido(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        String valor = username.trim();
        return !valor.contains("@") && 
               PATRON_USERNAME.matcher(valor).matches() && 
               valor.length() >= 2 && 
               valor.length() <= 50;
    }
    
    /**
     * Valida si un email O username es válido.
     */
    public static boolean esEmailOUsernameValido(String emailOUsername) {
        return esEmailValido(emailOUsername) || esUsernameValido(emailOUsername);
    }
    
    /**
     * Valida si una contraseña cumple los requisitos mínimos.
     */
    public static boolean esPasswordValido(String password) {
        return password != null && 
               password.length() >= LONGITUD_MINIMA_PASSWORD && 
               password.length() <= LONGITUD_MAXIMA_PASSWORD;
    }
}