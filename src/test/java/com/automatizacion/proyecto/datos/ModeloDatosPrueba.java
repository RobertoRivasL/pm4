package com.automatizacion.proyecto.datos;

/**
 * Modelo de datos para encapsular información de pruebas.
 * Representa los datos necesarios para ejecutar casos de prueba
 * de login y registro.
 * 
 * Implementa el patrón Data Transfer Object (DTO) y sigue
 * el principio de Encapsulación.
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
public class ModeloDatosPrueba {
    
    // === DATOS BÁSICOS ===
    private String casoPrueba;
    private String descripcion;
    private boolean esValido;
    private String resultadoEsperado;
    
    // === DATOS DE LOGIN ===
    private String email;
    private String usuario;
    private String password;
    private String confirmarPassword;
    
    // === DATOS DE REGISTRO ===
    private String nombre;
    private String apellido;
    private String telefono;
    private String fechaNacimiento;
    private String genero;
    private String pais;
    private String ciudad;
    
    // === CONFIGURACIÓN DE PRUEBA ===
    private boolean recordarme;
    private boolean aceptarTerminos;
    private boolean suscribirseNewsletter;
    
    // === VALIDACIONES Y MENSAJES ===
    private String mensajeError;
    private String mensajeExito;
    private String tipoValidacion;
    
    // === CONSTRUCTORS ===
    
    /**
     * Constructor por defecto
     */
    public ModeloDatosPrueba() {
        // Constructor vacío para flexibilidad
    }
    
    /**
     * Constructor para datos básicos de login
     * 
     * @param email email del usuario
     * @param password contraseña del usuario
     */
    public ModeloDatosPrueba(String email, String password) {
        this.email = email;
        this.password = password;
    }
    
    /**
     * Constructor completo para casos de prueba
     * 
     * @param casoPrueba identificador del caso
     * @param descripcion descripción del caso
     * @param email email del usuario
     * @param password contraseña del usuario
     * @param esValido si los datos son válidos
     * @param resultadoEsperado resultado esperado
     */
    public ModeloDatosPrueba(String casoPrueba, String descripcion, String email, 
                           String password, boolean esValido, String resultadoEsperado) {
        this.casoPrueba = casoPrueba;
        this.descripcion = descripcion;
        this.email = email;
        this.password = password;
        this.esValido = esValido;
        this.resultadoEsperado = resultadoEsperado;
    }
    
    // === MÉTODOS GETTER ===
    
    public String getCasoPrueba() {
        return casoPrueba;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public boolean isEsValido() {
        return esValido;
    }
    
    public String getResultadoEsperado() {
        return resultadoEsperado;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getUsuario() {
        return usuario;
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
    
    public String getFechaNacimiento() {
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
    
    public boolean isRecordarme() {
        return recordarme;
    }
    
    public boolean isAceptarTerminos() {
        return aceptarTerminos;
    }
    
    public boolean isSuscribirseNewsletter() {
        return suscribirseNewsletter;
    }
    
    public String getMensajeError() {
        return mensajeError;
    }
    
    public String getMensajeExito() {
        return mensajeExito;
    }
    
    public String getTipoValidacion() {
        return tipoValidacion;
    }
    
    // === MÉTODOS SETTER ===
    
    public void setCasoPrueba(String casoPrueba) {
        this.casoPrueba = casoPrueba;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public void setEsValido(boolean esValido) {
        this.esValido = esValido;
    }
    
    public void setResultadoEsperado(String resultadoEsperado) {
        this.resultadoEsperado = resultadoEsperado;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setConfirmarPassword(String confirmarPassword) {
        this.confirmarPassword = confirmarPassword;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    
    public void setGenero(String genero) {
        this.genero = genero;
    }
    
    public void setPais(String pais) {
        this.pais = pais;
    }
    
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
    
    public void setRecordarme(boolean recordarme) {
        this.recordarme = recordarme;
    }
    
    public void setAceptarTerminos(boolean aceptarTerminos) {
        this.aceptarTerminos = aceptarTerminos;
    }
    
    public void setSuscribirseNewsletter(boolean suscribirseNewsletter) {
        this.suscribirseNewsletter = suscribirseNewsletter;
    }
    
    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }
    
    public void setMensajeExito(String mensajeExito) {
        this.mensajeExito = mensajeExito;
    }
    
    public void setTipoValidacion(String tipoValidacion) {
        this.tipoValidacion = tipoValidacion;
    }
    
    // === MÉTODOS DE UTILIDAD ===
    
    /**
     * Verifica si los datos básicos de login están completos
     * 
     * @return true si email y password no están vacíos
     */
    public boolean tieneDatosLoginCompletos() {
        return email != null && !email.trim().isEmpty() &&
               password != null && !password.trim().isEmpty();
    }
    
    /**
     * Verifica si los datos de registro están completos
     * 
     * @return true si los campos obligatorios están llenos
     */
    public boolean tieneDatosRegistroCompletos() {
        return tieneDatosLoginCompletos() &&
               nombre != null && !nombre.trim().isEmpty() &&
               apellido != null && !apellido.trim().isEmpty() &&
               confirmarPassword != null && !confirmarPassword.trim().isEmpty();
    }
    
    /**
     * Verifica si las contraseñas coinciden
     * 
     * @return true si password y confirmarPassword son iguales
     */
    public boolean passwordsCoinciden() {
        if (password == null || confirmarPassword == null) {
            return false;
        }
        return password.equals(confirmarPassword);
    }
    
    /**
     * Genera un nombre completo combinando nombre y apellido
     * 
     * @return nombre completo o cadena vacía si faltan datos
     */
    public String obtenerNombreCompleto() {
        if (nombre == null && apellido == null) {
            return "";
        }
        
        StringBuilder nombreCompleto = new StringBuilder();
        if (nombre != null && !nombre.trim().isEmpty()) {
            nombreCompleto.append(nombre.trim());
        }
        if (apellido != null && !apellido.trim().isEmpty()) {
            if (nombreCompleto.length() > 0) {
                nombreCompleto.append(" ");
            }
            nombreCompleto.append(apellido.trim());
        }
        
        return nombreCompleto.toString();
    }
    
    /**
     * Crea una copia del modelo con datos limpios
     * 
     * @return nueva instancia con datos trimmed
     */
    public ModeloDatosPrueba crearCopiaLimpia() {
        ModeloDatosPrueba copia = new ModeloDatosPrueba();
        
        copia.casoPrueba = limpiarCadena(this.casoPrueba);
        copia.descripcion = limpiarCadena(this.descripcion);
        copia.email = limpiarCadena(this.email);
        copia.usuario = limpiarCadena(this.usuario);
        copia.password = this.password; // No limpiar passwords
        copia.confirmarPassword = this.confirmarPassword;
        copia.nombre = limpiarCadena(this.nombre);
        copia.apellido = limpiarCadena(this.apellido);
        copia.telefono = limpiarCadena(this.telefono);
        copia.fechaNacimiento = limpiarCadena(this.fechaNacimiento);
        copia.genero = limpiarCadena(this.genero);
        copia.pais = limpiarCadena(this.pais);
        copia.ciudad = limpiarCadena(this.ciudad);
        copia.mensajeError = limpiarCadena(this.mensajeError);
        copia.mensajeExito = limpiarCadena(this.mensajeExito);
        copia.tipoValidacion = limpiarCadena(this.tipoValidacion);
        copia.resultadoEsperado = limpiarCadena(this.resultadoEsperado);
        
        // Copiar booleanos
        copia.esValido = this.esValido;
        copia.recordarme = this.recordarme;
        copia.aceptarTerminos = this.aceptarTerminos;
        copia.suscribirseNewsletter = this.suscribirseNewsletter;
        
        return copia;
    }
    
    /**
     * Método auxiliar para limpiar cadenas
     * 
     * @param cadena cadena a limpiar
     * @return cadena limpia o null si era null
     */
    private String limpiarCadena(String cadena) {
        return cadena != null ? cadena.trim() : null;
    }
    
    // === MÉTODOS ESTÁNDAR ===
    
    @Override
    public String toString() {
        return "ModeloDatosPrueba{" +
                "casoPrueba='" + casoPrueba + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", email='" + email + '\'' +
                ", esValido=" + esValido +
                ", resultadoEsperado='" + resultadoEsperado + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        ModeloDatosPrueba that = (ModeloDatosPrueba) obj;
        
        return casoPrueba != null ? casoPrueba.equals(that.casoPrueba) : that.casoPrueba == null;
    }
    
    @Override
    public int hashCode() {
        return casoPrueba != null ? casoPrueba.hashCode() : 0;
    }
}