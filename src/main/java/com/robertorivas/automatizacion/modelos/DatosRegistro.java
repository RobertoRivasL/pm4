package com.robertorivas.automatizacion.modelos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Clase que encapsula todos los datos necesarios para el formulario de registro.
 * Extiende la funcionalidad de Usuario con campos específicos del registro.
 * 
 * Principios aplicados:
 * - Encapsulación: Datos privados con métodos de acceso controlado
 * - Validación: Validaciones específicas para el formulario de registro
 * - Single Responsibility: Maneja solo datos de registro
 * - Builder Pattern: Para construcción flexible de objetos
 * 
 * @author Roberto Rivas Lopez
 */
public class DatosRegistro {
    
    // Datos básicos del usuario
    private String email;
    private String password;
    private String confirmarPassword;
    
    // Datos personales
    private String nombre;
    private String apellido;
    private String telefono;
    private LocalDate fechaNacimiento;
    
    // Datos adicionales del formulario
    private String genero;
    private String pais;
    private String ciudad;
    private String codigoPostal;
    
    // Opciones del formulario
    private boolean aceptaTerminos;
    private boolean recibirNotificaciones;
    private boolean recibirPromociones;
    
    // Campos de validación
    private String captcha;
    private String codigoVerificacion;
    
    // Metadatos
    private TipoRegistro tipoRegistro;
    private List<String> erroresValidacion;
    
    public enum TipoRegistro {
        NORMAL("Normal"),
        PREMIUM("Premium"),
        EMPRESA("Empresa"),
        ESTUDIANTE("Estudiante");
        
        private final String descripcion;
        
        TipoRegistro(String descripcion) {
            this.descripcion = descripcion;
        }
        
        public String getDescripcion() {
            return descripcion;
        }
    }
    
    public enum Genero {
        MASCULINO("Masculino"),
        FEMENINO("Femenino"),
        OTRO("Otro"),
        PREFIERO_NO_DECIR("Prefiero no decir");
        
        private final String descripcion;
        
        Genero(String descripcion) {
            this.descripcion = descripcion;
        }
        
        public String getDescripcion() {
            return descripcion;
        }
    }
    
    /**
     * Constructor por defecto.
     */
    public DatosRegistro() {
        this.tipoRegistro = TipoRegistro.NORMAL;
        this.erroresValidacion = new ArrayList<>();
        this.aceptaTerminos = false;
        this.recibirNotificaciones = true;
        this.recibirPromociones = false;
    }
    
    /**
     * Constructor con datos básicos.
     */
    public DatosRegistro(String email, String password, String confirmarPassword) {
        this();
        this.email = email;
        this.password = password;
        this.confirmarPassword = confirmarPassword;
    }
    
    // Getters
    
    public String getEmail() {
        return email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public String getConfirmarPassword() {
        return confirmarPassword;
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
    
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }
    
    public String getGenero() {
        return genero;
    }
    
    public String getPais() {
        return pais;
    }
    
    public String getCiudad() {
        return ciudad;
    }
    
    public String getCodigoPostal() {
        return codigoPostal;
    }
    
    public boolean isAceptaTerminos() {
        return aceptaTerminos;
    }
    
    public boolean isRecibirNotificaciones() {
        return recibirNotificaciones;
    }
    
    public boolean isRecibirPromociones() {
        return recibirPromociones;
    }
    
    public String getCaptcha() {
        return captcha;
    }
    
    public String getCodigoVerificacion() {
        return codigoVerificacion;
    }
    
    public TipoRegistro getTipoRegistro() {
        return tipoRegistro;
    }
    
    public List<String> getErroresValidacion() {
        return new ArrayList<>(erroresValidacion);
    }
    
    // Setters con validación
    
    public DatosRegistro setEmail(String email) {
        this.email = email;
        return this;
    }
    
    public DatosRegistro setPassword(String password) {
        this.password = password;
        return this;
    }
    
    public DatosRegistro setConfirmarPassword(String confirmarPassword) {
        this.confirmarPassword = confirmarPassword;
        return this;
    }
    
    public DatosRegistro setNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }
    
    public DatosRegistro setApellido(String apellido) {
        this.apellido = apellido;
        return this;
    }
    
    public DatosRegistro setTelefono(String telefono) {
        this.telefono = telefono;
        return this;
    }
    
    public DatosRegistro setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
        return this;
    }
    
    public DatosRegistro setGenero(String genero) {
        this.genero = genero;
        return this;
    }
    
    public DatosRegistro setPais(String pais) {
        this.pais = pais;
        return this;
    }
    
    public DatosRegistro setCiudad(String ciudad) {
        this.ciudad = ciudad;
        return this;
    }
    
    public DatosRegistro setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
        return this;
    }
    
    public DatosRegistro setAceptaTerminos(boolean aceptaTerminos) {
        this.aceptaTerminos = aceptaTerminos;
        return this;
    }
    
    public DatosRegistro setRecibirNotificaciones(boolean recibirNotificaciones) {
        this.recibirNotificaciones = recibirNotificaciones;
        return this;
    }
    
    public DatosRegistro setRecibirPromociones(boolean recibirPromociones) {
        this.recibirPromociones = recibirPromociones;
        return this;
    }
    
    public DatosRegistro setCaptcha(String captcha) {
        this.captcha = captcha;
        return this;
    }
    
    public DatosRegistro setCodigoVerificacion(String codigoVerificacion) {
        this.codigoVerificacion = codigoVerificacion;
        return this;
    }
    
    public DatosRegistro setTipoRegistro(TipoRegistro tipoRegistro) {
        this.tipoRegistro = tipoRegistro != null ? tipoRegistro : TipoRegistro.NORMAL;
        return this;
    }
    
    // Métodos de validación
    
    /**
     * Valida todos los datos del formulario de registro.
     */
    public boolean validarDatos() {
        erroresValidacion.clear();
        
        validarEmail();
        validarPasswords();
        validarCamposObligatorios();
        validarTerminos();
        validarFechaNacimiento();
        
        return erroresValidacion.isEmpty();
    }
    
    private void validarEmail() {
        if (email == null || email.trim().isEmpty()) {
            erroresValidacion.add("El email es obligatorio");
        } else if (!Usuario.esEmailValido(email)) {
            erroresValidacion.add("El formato del email no es válido");
        }
    }
    
    private void validarPasswords() {
        if (password == null || password.isEmpty()) {
            erroresValidacion.add("La contraseña es obligatoria");
        } else if (!Usuario.esPasswordValido(password)) {
            erroresValidacion.add("La contraseña debe tener entre 6 y 50 caracteres");
        }
        
        if (confirmarPassword == null || confirmarPassword.isEmpty()) {
            erroresValidacion.add("La confirmación de contraseña es obligatoria");
        } else if (!Objects.equals(password, confirmarPassword)) {
            erroresValidacion.add("Las contraseñas no coinciden");
        }
    }
    
    private void validarCamposObligatorios() {
        if (nombre == null || nombre.trim().isEmpty()) {
            erroresValidacion.add("El nombre es obligatorio");
        }
        
        if (apellido == null || apellido.trim().isEmpty()) {
            erroresValidacion.add("El apellido es obligatorio");
        }
    }
    
    private void validarTerminos() {
        if (!aceptaTerminos) {
            erroresValidacion.add("Debe aceptar los términos y condiciones");
        }
    }
    
    private void validarFechaNacimiento() {
        if (fechaNacimiento != null) {
            LocalDate fechaMinima = LocalDate.now().minusYears(120);
            LocalDate fechaMaxima = LocalDate.now().minusYears(13);
            
            if (fechaNacimiento.isBefore(fechaMinima)) {
                erroresValidacion.add("La fecha de nacimiento no puede ser anterior a " + fechaMinima);
            }
            
            if (fechaNacimiento.isAfter(fechaMaxima)) {
                erroresValidacion.add("Debe ser mayor de 13 años para registrarse");
            }
        }
    }
    
    // Métodos de utilidad
    
    /**
     * Convierte los datos a un objeto Usuario.
     */
    public Usuario toUsuario() {
        if (!validarDatos()) {
            throw new IllegalStateException("Los datos de registro no son válidos: " + erroresValidacion);
        }
        
        Usuario usuario = new Usuario(email, password);
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setTelefono(telefono);
        
        return usuario;
    }
    
    /**
     * Obtiene el nombre completo.
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
     * Verifica si los datos son válidos para el registro.
     */
    public boolean esValidoParaRegistro() {
        return validarDatos() && aceptaTerminos;
    }
    
    /**
     * Limpia todos los errores de validación.
     */
    public void limpiarErrores() {
        erroresValidacion.clear();
    }
    
    /**
     * Agrega un error de validación personalizado.
     */
    public void agregarError(String error) {
        if (error != null && !error.trim().isEmpty()) {
            erroresValidacion.add(error.trim());
        }
    }
    
    /**
     * Verifica si hay errores de validación.
     */
    public boolean tieneErrores() {
        return !erroresValidacion.isEmpty();
    }
    
    /**
     * Obtiene el primer error de validación.
     */
    public String getPrimerError() {
        return erroresValidacion.isEmpty() ? null : erroresValidacion.get(0);
    }
    
    // Métodos Object
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DatosRegistro that = (DatosRegistro) o;
        return Objects.equals(email, that.email);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
    
    @Override
    public String toString() {
        return String.format("DatosRegistro{email='%s', nombre='%s', apellido='%s', tipoRegistro=%s, aceptaTerminos=%s}", 
                           email, nombre, apellido, tipoRegistro, aceptaTerminos);
    }
    
    // Clase Builder para construcción flexible
    
    public static class Builder {
        private final DatosRegistro datos;
        
        public Builder() {
            this.datos = new DatosRegistro();
        }
        
        public Builder email(String email) {
            datos.setEmail(email);
            return this;
        }
        
        public Builder password(String password) {
            datos.setPassword(password);
            return this;
        }
        
        public Builder confirmarPassword(String confirmarPassword) {
            datos.setConfirmarPassword(confirmarPassword);
            return this;
        }
        
        public Builder nombre(String nombre) {
            datos.setNombre(nombre);
            return this;
        }
        
        public Builder apellido(String apellido) {
            datos.setApellido(apellido);
            return this;
        }
        
        public Builder telefono(String telefono) {
            datos.setTelefono(telefono);
            return this;
        }
        
        public Builder fechaNacimiento(LocalDate fechaNacimiento) {
            datos.setFechaNacimiento(fechaNacimiento);
            return this;
        }
        
        public Builder genero(String genero) {
            datos.setGenero(genero);
            return this;
        }
        
        public Builder pais(String pais) {
            datos.setPais(pais);
            return this;
        }
        
        public Builder ciudad(String ciudad) {
            datos.setCiudad(ciudad);
            return this;
        }
        
        public Builder aceptarTerminos() {
            datos.setAceptaTerminos(true);
            return this;
        }
        
        public Builder recibirNotificaciones(boolean recibir) {
            datos.setRecibirNotificaciones(recibir);
            return this;
        }
        
        public Builder tipoRegistro(TipoRegistro tipo) {
            datos.setTipoRegistro(tipo);
            return this;
        }
        
        public DatosRegistro build() {
            return datos;
        }
    }
    
    // Métodos estáticos de fábrica
    
    /**
     * Crea datos de registro básicos para pruebas.
     */
    public static DatosRegistro crearDatosPrueba() {
        return new Builder()
                .email("test@testautomation.com")
                .password("password123")
                .confirmarPassword("password123")
                .nombre("Usuario")
                .apellido("Prueba")
                .aceptarTerminos()
                .build();
    }
    
    /**
     * Crea datos de registro válidos con información específica.
     */
    public static DatosRegistro crearDatosValidos(String email, String password, String nombre, String apellido) {
        return new Builder()
                .email(email)
                .password(password)
                .confirmarPassword(password)
                .nombre(nombre)
                .apellido(apellido)
                .aceptarTerminos()
                .build();
    }
    
    /**
     * Crea datos de registro inválidos para pruebas negativas.
     */
    public static DatosRegistro crearDatosInvalidos() {
        return new Builder()
                .email("email-invalido")
                .password("123")
                .confirmarPassword("456")
                .build();
    }
}