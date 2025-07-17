package com.robertorivas.automatizacion.modelos;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Clase que representa un usuario del sistema.
 * 
 * Principios aplicados:
 * - Encapsulación: Campos privados con métodos de acceso
 * - Validación: Validaciones en setters y constructor
 * - Inmutabilidad: Campos final donde es apropiado
 * - Single Responsibility: Solo maneja datos de usuario
 * 
 * @author Roberto Rivas Lopez
 */
public class Usuario {
    
    // Patrones de validación
    private static final Pattern PATRON_EMAIL = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$"
    );
    private static final int LONGITUD_MINIMA_PASSWORD = 6;
    private static final int LONGITUD_MAXIMA_PASSWORD = 50;
    
    // Campos del usuario
    private final String id;
    private String email;
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
    
    /**
     * Constructor principal con validaciones.
     */
    public Usuario(String id, String email, String password) {
        this.id = validarId(id);
        this.email = validarEmail(email);
        this.password = validarPassword(password);
        this.estado = EstadoUsuario.ACTIVO;
        this.fechaCreacion = LocalDateTime.now();
        this.fechaUltimaModificacion = LocalDateTime.now();
    }
    
    /**
     * Constructor con datos completos.
     */
    public Usuario(String id, String email, String password, String nombre, String apellido) {
        this(id, email, password);
        this.nombre = validarTexto(nombre, "nombre");
        this.apellido = validarTexto(apellido, "apellido");
    }
    
    /**
     * Constructor para pruebas básicas (solo email y password).
     */
    public Usuario(String email, String password) {
        this(generarIdAleatorio(), email, password);
    }
    
    // Métodos de validación privados
    
    private String validarId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo o vacío");
        }
        return id.trim();
    }
    
    private String validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede ser nulo o vacío");
        }
        
        String emailLimpio = email.trim().toLowerCase();
        if (!PATRON_EMAIL.matcher(emailLimpio).matches()) {
            throw new IllegalArgumentException("El formato del email no es válido: " + email);
        }
        
        return emailLimpio;
    }
    
    private String validarPassword(String password) {
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
    
    private String validarTexto(String texto, String nombreCampo) {
        if (texto != null && !texto.trim().isEmpty()) {
            return texto.trim();
        }
        return null; // Permitir campos opcionales nulos
    }
    
    private static String generarIdAleatorio() {
        return "user_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
    }
    
    // Getters
    
    public String getId() {
        return id;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public String getApellido() {
        return apellido;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public EstadoUsuario getEstado() {
        return estado;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public LocalDateTime getFechaUltimaModificacion() {
        return fechaUltimaModificacion;
    }
    
    // Setters con validación
    
    public void setEmail(String email) {
        this.email = validarEmail(email);
        this.fechaUltimaModificacion = LocalDateTime.now();
    }
    
    public void setPassword(String password) {
        this.password = validarPassword(password);
        this.fechaUltimaModificacion = LocalDateTime.now();
    }
    
    public void setNombre(String nombre) {
        this.nombre = validarTexto(nombre, "nombre");
        this.fechaUltimaModificacion = LocalDateTime.now();
    }
    
    public void setApellido(String apellido) {
        this.apellido = validarTexto(apellido, "apellido");
        this.fechaUltimaModificacion = LocalDateTime.now();
    }
    
    public void setTelefono(String telefono) {
        this.telefono = validarTexto(telefono, "telefono");
        this.fechaUltimaModificacion = LocalDateTime.now();
    }
    
    public void setEstado(EstadoUsuario estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado del usuario no puede ser nulo");
        }
        this.estado = estado;
        this.fechaUltimaModificacion = LocalDateTime.now();
    }
    
    // Métodos de utilidad
    
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
     * Valida si la contraseña proporcionada coincide.
     */
    public boolean validarPassword(String passwordAValidar) {
        return this.password != null && this.password.equals(passwordAValidar);
    }
    
    /**
     * Crea una copia del usuario para pruebas (sin exponer la contraseña real).
     */
    public Usuario copiarParaPruebas() {
        Usuario copia = new Usuario(this.id + "_copia", this.email, "password_test");
        copia.setNombre(this.nombre);
        copia.setApellido(this.apellido);
        copia.setTelefono(this.telefono);
        copia.setEstado(this.estado);
        return copia;
    }
    
    // Métodos Object
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id) && 
               Objects.equals(email, usuario.email);
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
     * Convierte a string sin información sensible (para logs).
     */
    public String toStringSinPassword() {
        return String.format("Usuario{id='%s', email='%s', nombre='%s', estado=%s, fechaCreacion=%s}", 
                           id, email, getNombreCompleto(), estado, fechaCreacion);
    }
    
    // Métodos estáticos de utilidad
    
    /**
     * Crea un usuario válido para pruebas.
     */
    public static Usuario crearUsuarioPrueba() {
        return new Usuario(
            "test@testautomation.com", 
            "password123"
        );
    }
    
    /**
     * Crea un usuario con datos específicos para pruebas.
     */
    public static Usuario crearUsuarioPrueba(String email, String password) {
        return new Usuario(email, password);
    }
    
    /**
     * Valida si un email tiene formato válido.
     */
    public static boolean esEmailValido(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return PATRON_EMAIL.matcher(email.trim().toLowerCase()).matches();
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
