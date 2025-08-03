package com.automatizacion.proyecto.datos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

/**
 * Modelo de datos para las pruebas de registro e inicio de sesión.
 * Utiliza Lombok para reducir el código boilerplate.
 * 
 * Principios aplicados:
 * - SRP: Solo representa datos de prueba
 * - Inmutabilidad: Campos finales cuando es apropiado
 * - Builder Pattern: Para creación flexible de instancias
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModeloDatosPrueba {
    
    // === IDENTIFICACIÓN DEL CASO DE PRUEBA ===
    private String casoPrueba;
    private String descripcion;
    private String categoria;
    
    // === DATOS PERSONALES ===
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String fechaNacimiento;
    
    // === DATOS DE CREDENCIALES ===
    private String password;
    private String confirmacionPassword;
    
    // === DATOS ADICIONALES ===
    private String genero;
    private String pais;
    private String ciudad;
    private String direccion;
    private String codigoPostal;
    
    // === CONFIGURACIÓN DE VALIDACIÓN ===
    private boolean esValido;
    private String mensajeError;
    private String tipoError;
    
    // === CONFIGURACIÓN DE PRUEBA ===
    private boolean aceptaTerminos;
    private boolean suscribirNewsletter;
    private int timeoutPersonalizado;
    
    // Patrones de validación
    private static final Pattern PATRON_EMAIL = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );
    
    private static final Pattern PATRON_PASSWORD_SEGURO = Pattern.compile(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"
    );
    
    private static final Pattern PATRON_TELEFONO = Pattern.compile(
        "^[+]?[0-9]{8,15}$"
    );
    
    // === MÉTODOS DE VALIDACIÓN ===
    
    /**
     * Verifica si todos los campos obligatorios están completos
     * @return true si los campos obligatorios están completos
     */
    public boolean camposObligatoriosCompletos() {
        return nombre != null && !nombre.trim().isEmpty() &&
               apellido != null && !apellido.trim().isEmpty() &&
               email != null && !email.trim().isEmpty() &&
               password != null && !password.trim().isEmpty();
    }
    
    /**
     * Verifica si el email tiene un formato válido
     * @return true si el email es válido
     */
    public boolean esEmailValido() {
        return email != null && PATRON_EMAIL.matcher(email).matches();
    }
    
    /**
     * Verifica si las contraseñas coinciden
     * @return true si las contraseñas coinciden
     */
    public boolean passwordsCoinciden() {
        if (password == null || confirmacionPassword == null) {
            return false;
        }
        return password.equals(confirmacionPassword);
    }
    
    /**
     * Verifica si la contraseña cumple criterios de seguridad
     * @return true si la contraseña es segura
     */
    public boolean esPasswordSeguro() {
        return password != null && PATRON_PASSWORD_SEGURO.matcher(password).matches();
    }
    
    /**
     * Verifica si el teléfono tiene formato válido
     * @return true si el teléfono es válido
     */
    public boolean esTelefonoValido() {
        return telefono == null || telefono.trim().isEmpty() || 
               PATRON_TELEFONO.matcher(telefono.replaceAll("\\s", "")).matches();
    }
    
    /**
     * Obtiene el nombre completo
     * @return nombre completo concatenado
     */
    public String obtenerNombreCompleto() {
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
     * Valida todos los criterios del modelo
     * @return true si todos los datos son válidos
     */
    public boolean validarTodosLosCriterios() {
        return camposObligatoriosCompletos() &&
               esEmailValido() &&
               passwordsCoinciden() &&
               esPasswordSeguro() &&
               esTelefonoValido();
    }
    
    /**
     * Obtiene una descripción de los errores de validación
     * @return descripción de errores
     */
    public String obtenerErroresValidacion() {
        StringBuilder errores = new StringBuilder();
        
        if (!camposObligatoriosCompletos()) {
            errores.append("Campos obligatorios incompletos. ");
        }
        
        if (email != null && !email.trim().isEmpty() && !esEmailValido()) {
            errores.append("Formato de email inválido. ");
        }
        
        if (password != null && !password.trim().isEmpty() && !esPasswordSeguro()) {
            errores.append("Contraseña no cumple criterios de seguridad. ");
        }
        
        if (!passwordsCoinciden()) {
            errores.append("Las contraseñas no coinciden. ");
        }
        
        if (!esTelefonoValido()) {
            errores.append("Formato de teléfono inválido. ");
        }
        
        return errores.toString().trim();
    }
    
    // === MÉTODOS DE UTILIDAD ===
    
    /**
     * Genera un email único basado en timestamp
     * @return email único
     */
    public String generarEmailUnico() {
        if (email != null && email.contains("@")) {
            String[] partes = email.split("@");
            return partes[0] + "." + System.currentTimeMillis() + "@" + partes[1];
        }
        return "test." + System.currentTimeMillis() + "@test.com";
    }
    
    /**
     * Crea una copia del modelo con email único
     * @return nueva instancia con email único
     */
    public ModeloDatosPrueba conEmailUnico() {
        return this.toBuilder()
                .email(generarEmailUnico())
                .build();
    }
    
    /**
     * Crea una copia marcada como inválida
     * @param razonError razón por la cual es inválido
     * @return nueva instancia marcada como inválida
     */
    public ModeloDatosPrueba comoInvalido(String razonError) {
        return this.toBuilder()
                .esValido(false)
                .mensajeError(razonError)
                .build();
    }
    
    /**
     * Crea una copia marcada como válida
     * @return nueva instancia marcada como válida
     */
    public ModeloDatosPrueba comoValido() {
        return this.toBuilder()
                .esValido(true)
                .mensajeError(null)
                .build();
    }
    
    // === MÉTODOS ESTÁTICOS DE CREACIÓN ===
    
    /**
     * Crea un modelo de datos básico válido
     * @return modelo de datos válido
     */
    public static ModeloDatosPrueba crearDatosValidos() {
        return ModeloDatosPrueba.builder()
                .casoPrueba("DATOS_VALIDOS")
                .descripcion("Datos válidos para prueba")
                .nombre("Usuario")
                .apellido("Prueba")
                .email("usuario.prueba@test.com")
                .password("Password123!")
                .confirmacionPassword("Password123!")
                .esValido(true)
                .aceptaTerminos(true)
                .build();
    }
    
    /**
     * Crea un modelo de datos inválido
     * @param tipoError tipo de error a simular
     * @return modelo de datos inválido
     */
    public static ModeloDatosPrueba crearDatosInvalidos(String tipoError) {
        ModeloDatosPruebaBuilder builder = ModeloDatosPrueba.builder()
                .casoPrueba("DATOS_INVALIDOS_" + tipoError)
                .descripcion("Datos inválidos para prueba: " + tipoError)
                .esValido(false)
                .mensajeError("Error de tipo: " + tipoError);
        
        switch (tipoError.toUpperCase()) {
            case "EMAIL_VACIO":
                builder.nombre("Usuario").apellido("Prueba")
                       .password("Password123!").confirmacionPassword("Password123!");
                break;
            case "EMAIL_INVALIDO":
                builder.nombre("Usuario").apellido("Prueba")
                       .email("email-invalido")
                       .password("Password123!").confirmacionPassword("Password123!");
                break;
            case "PASSWORD_VACIO":
                builder.nombre("Usuario").apellido("Prueba")
                       .email("usuario@test.com");
                break;
            case "PASSWORDS_DIFERENTES":
                builder.nombre("Usuario").apellido("Prueba")
                       .email("usuario@test.com")
                       .password("Password123!")
                       .confirmacionPassword("DiferentePassword123!");
                break;
            default:
                builder.nombre("").apellido("").email("").password("");
        }
        
        return builder.build();
    }
    
    @Override
    public String toString() {
        return String.format("ModeloDatosPrueba{casoPrueba='%s', descripcion='%s', " +
                           "nombre='%s', apellido='%s', email='%s', esValido=%s}",
                           casoPrueba, descripcion, nombre, apellido, 
                           email != null ? email.replaceAll("(.{3}).*(@.*)", "$1***$2") : null,
                           esValido);
    }
}