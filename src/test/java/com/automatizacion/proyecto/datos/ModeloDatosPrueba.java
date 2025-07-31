package com.automatizacion.proyecto.datos;

import java.util.Objects;

/**
 * Modelo de datos para las pruebas de registro e inicio de sesión.
 * Encapsula toda la información necesaria para ejecutar casos de prueba.
 * 
 * Implementa el principio de Encapsulación y sirve como DTO (Data Transfer Object)
 * para transportar datos entre capas de la aplicación.
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
public class ModeloDatosPrueba {
    
    // Datos básicos del usuario
    private String nombre;
    private String apellido;
    private String nombreCompleto;
    private String email;
    private String password;
    private String confirmacionPassword;
    
    // Datos adicionales
    private String telefono;
    private String fechaNacimiento;
    private String genero;
    private String pais;
    private String ciudad;
    
    // Datos de validación
    private String casoPrueba;
    private String descripcionCaso;
    private boolean esValido;
    private String resultadoEsperado;
    private String mensajeErrorEsperado;
    
    // Datos para configuración de prueba
    private String navegador;
    private boolean requiereCaptura;
    private int tiempoEsperaPersonalizado;
    
    /**
     * Constructor por defecto
     */
    public ModeloDatosPrueba() {
        this.esValido = true;
        this.requiereCaptura = false;
        this.tiempoEsperaPersonalizado = 0;
    }
    
    /**
     * Constructor para datos básicos de login
     * @param email email del usuario
     * @param password contraseña del usuario
     */
    public ModeloDatosPrueba(String email, String password) {
        this();
        this.email = email;
        this.password = password;
    }
    
    /**
     * Constructor para datos básicos de registro
     * @param nombre nombre del usuario
     * @param apellido apellido del usuario
     * @param email email del usuario
     * @param password contraseña del usuario
     */
    public ModeloDatosPrueba(String nombre, String apellido, String email, String password) {
        this();
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.password = password;
        this.confirmacionPassword = password;
        this.nombreCompleto = nombre + " " + apellido;
    }
    
    /**
     * Constructor completo para casos de prueba
     * @param casoPrueba identificador del caso de prueba
     * @param descripcionCaso descripción del caso
     * @param esValido indica si los datos son válidos
     * @param resultadoEsperado resultado esperado de la prueba
     */
    public ModeloDatosPrueba(String casoPrueba, String descripcionCaso, boolean esValido, String resultadoEsperado) {
        this();
        this.casoPrueba = casoPrueba;
        this.descripcionCaso = descripcionCaso;
        this.esValido = esValido;
        this.resultadoEsperado = resultadoEsperado;
    }
    
    // === GETTERS Y SETTERS ===
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
        actualizarNombreCompleto();
    }
    
    public String getApellido() {
        return apellido;
    }
    
    public void setApellido(String apellido) {
        this.apellido = apellido;
        actualizarNombreCompleto();
    }
    
    public String getNombreCompleto() {
        return nombreCompleto;
    }
    
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getConfirmacionPassword() {
        return confirmacionPassword;
    }
    
    public void setConfirmacionPassword(String confirmacionPassword) {
        this.confirmacionPassword = confirmacionPassword;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public String getFechaNacimiento() {
        return fechaNacimiento;
    }
    
    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    
    public String getGenero() {
        return genero;
    }
    
    public void setGenero(String genero) {
        this.genero = genero;
    }
    
    public String getPais() {
        return pais;
    }
    
    public void setPais(String pais) {
        this.pais = pais;
    }
    
    public String getCiudad() {
        return ciudad;
    }
    
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
    
    public String getCasoPrueba() {
        return casoPrueba;
    }
    
    public void setCasoPrueba(String casoPrueba) {
        this.casoPrueba = casoPrueba;
    }
    
    public String getDescripcionCaso() {
        return descripcionCaso;
    }
    
    public void setDescripcionCaso(String descripcionCaso) {
        this.descripcionCaso = descripcionCaso;
    }
    
    public boolean isEsValido() {
        return esValido;
    }
    
    public void setEsValido(boolean esValido) {
        this.esValido = esValido;
    }
    
    public String getResultadoEsperado() {
        return resultadoEsperado;
    }
    
    public void setResultadoEsperado(String resultadoEsperado) {
        this.resultadoEsperado = resultadoEsperado;
    }
    
    public String getMensajeErrorEsperado() {
        return mensajeErrorEsperado;
    }
    
    public void setMensajeErrorEsperado(String mensajeErrorEsperado) {
        this.mensajeErrorEsperado = mensajeErrorEsperado;
    }
    
    public String getNavegador() {
        return navegador;
    }
    
    public void setNavegador(String navegador) {
        this.navegador = navegador;
    }
    
    public boolean isRequiereCaptura() {
        return requiereCaptura;
    }
    
    public void setRequiereCaptura(boolean requiereCaptura) {
        this.requiereCaptura = requiereCaptura;
    }
    
    public int getTiempoEsperaPersonalizado() {
        return tiempoEsperaPersonalizado;
    }
    
    public void setTiempoEsperaPersonalizado(int tiempoEsperaPersonalizado) {
        this.tiempoEsperaPersonalizado = tiempoEsperaPersonalizado;
    }
    
    // === MÉTODOS DE UTILIDAD ===
    
    /**
     * Actualiza el nombre completo cuando se modifican nombre o apellido
     */
    private void actualizarNombreCompleto() {
        if (nombre != null && apellido != null) {
            this.nombreCompleto = nombre.trim() + " " + apellido.trim();
        }
    }
    
    /**
     * Verifica si las contraseñas coinciden
     * @return true si coinciden o no hay confirmación
     */
    public boolean passwordsCoinciden() {
        if (confirmacionPassword == null) {
            return true; // No hay confirmación que validar
        }
        return Objects.equals(password, confirmacionPassword);
    }
    
    /**
     * Verifica si el email tiene un formato básicamente válido
     * @return true si el formato es válido
     */
    public boolean esEmailValido() {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return email.contains("@") && email.contains(".");
    }
    
    /**
     * Verifica si todos los campos obligatorios están completos
     * @return true si los campos obligatorios tienen valor
     */
    public boolean camposObligatoriosCompletos() {
        return esStringValido(nombre) && 
               esStringValido(apellido) && 
               esStringValido(email) && 
               esStringValido(password);
    }
    
    /**
     * Verifica si todos los campos de login están completos
     * @return true si email y password tienen valor
     */
    public boolean camposLoginCompletos() {
        return esStringValido(email) && esStringValido(password);
    }
    
    /**
     * Crea una copia de los datos para login (solo email y password)
     * @return nueva instancia con datos de login
     */
    public ModeloDatosPrueba extraerDatosLogin() {
        ModeloDatosPrueba datosLogin = new ModeloDatosPrueba(email, password);
        datosLogin.setCasoPrueba(casoPrueba);
        datosLogin.setDescripcionCaso(descripcionCaso);
        datosLogin.setEsValido(esValido);
        datosLogin.setResultadoEsperado(resultadoEsperado);
        datosLogin.setMensajeErrorEsperado(mensajeErrorEsperado);
        return datosLogin;
    }
    
    /**
     * Limpia datos sensibles (passwords)
     */
    public void limpiarDatosSensibles() {
        this.password = null;
        this.confirmacionPassword = null;
    }
    
    /**
     * Genera un resumen de los datos para logs (sin información sensible)
     * @return string con resumen de datos
     */
    public String generarResumen() {
        StringBuilder resumen = new StringBuilder();
        
        if (casoPrueba != null) {
            resumen.append("Caso: ").append(casoPrueba).append(" | ");
        }
        
        if (nombreCompleto != null) {
            resumen.append("Usuario: ").append(nombreCompleto).append(" | ");
        }
        
        if (email != null) {
            resumen.append("Email: ").append(enmascararEmail(email)).append(" | ");
        }
        
        resumen.append("Válido: ").append(esValido);
        
        if (resultadoEsperado != null) {
            resumen.append(" | Esperado: ").append(resultadoEsperado);
        }
        
        return resumen.toString();
    }
    
    /**
     * Enmascara un email para logs seguros
     * @param email email a enmascarar
     * @return email enmascarado
     */
    private String enmascararEmail(String email) {
        if (email == null || email.length() < 3) {
            return "***";
        }
        
        int posicionArroba = email.indexOf('@');
        if (posicionArroba <= 1) {
            return "***@" + email.substring(posicionArroba + 1);
        }
        
        return email.substring(0, 2) + "***@" + email.substring(posicionArroba + 1);
    }
    
    /**
     * Verifica si un string es válido (no null y no vacío)
     * @param valor string a verificar
     * @return true si es válido
     */
    private boolean esStringValido(String valor) {
        return valor != null && !valor.trim().isEmpty();
    }
    
    // === BUILDER PATTERN ===
    
    /**
     * Clase Builder para crear instancias de ModeloDatosPrueba de forma fluida
     */
    public static class Builder {
        private final ModeloDatosPrueba modelo;
        
        public Builder() {
            this.modelo = new ModeloDatosPrueba();
        }
        
        public Builder casoPrueba(String casoPrueba) {
            modelo.setCasoPrueba(casoPrueba);
            return this;
        }
        
        public Builder descripcion(String descripcion) {
            modelo.setDescripcionCaso(descripcion);
            return this;
        }
        
        public Builder nombre(String nombre) {
            modelo.setNombre(nombre);
            return this;
        }
        
        public Builder apellido(String apellido) {
            modelo.setApellido(apellido);
            return this;
        }
        
        public Builder email(String email) {
            modelo.setEmail(email);
            return this;
        }
        
        public Builder password(String password) {
            modelo.setPassword(password);
            return this;
        }
        
        public Builder confirmacionPassword(String confirmacionPassword) {
            modelo.setConfirmacionPassword(confirmacionPassword);
            return this;
        }
        
        public Builder telefono(String telefono) {
            modelo.setTelefono(telefono);
            return this;
        }
        
        public Builder esValido(boolean esValido) {
            modelo.setEsValido(esValido);
            return this;
        }
        
        public Builder resultadoEsperado(String resultado) {
            modelo.setResultadoEsperado(resultado);
            return this;
        }
        
        public Builder mensajeError(String mensajeError) {
            modelo.setMensajeErrorEsperado(mensajeError);
            return this;
        }
        
        public Builder navegador(String navegador) {
            modelo.setNavegador(navegador);
            return this;
        }
        
        public Builder requiereCaptura(boolean requiere) {
            modelo.setRequiereCaptura(requiere);
            return this;
        }
        
        public ModeloDatosPrueba build() {
            return modelo;
        }
    }
    
    /**
     * Crea un nuevo builder
     * @return nueva instancia de Builder
     */
    public static Builder builder() {
        return new Builder();
    }
    
    // === MÉTODOS ESTÁNDAR ===
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        ModeloDatosPrueba that = (ModeloDatosPrueba) o;
        
        return Objects.equals(email, that.email) &&
               Objects.equals(casoPrueba, that.casoPrueba);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(email, casoPrueba);
    }
    
    @Override
    public String toString() {
        return "ModeloDatosPrueba{" +
                "casoPrueba='" + casoPrueba + '\'' +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", email='" + enmascararEmail(email) + '\'' +
                ", esValido=" + esValido +
                ", resultadoEsperado='" + resultadoEsperado + '\'' +
                '}';
    }
}