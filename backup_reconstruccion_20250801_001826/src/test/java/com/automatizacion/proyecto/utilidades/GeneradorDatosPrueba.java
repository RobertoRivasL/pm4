package com.automatizacion.proyecto.utilidades;

import com.automatizacion.proyecto.datos.ModeloDatosPrueba;
import com.automatizacion.proyecto.enums.TipoMensaje;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Clase utilitaria para generar datos de prueba de forma dinámica y aleatoria.
 * Proporciona métodos para crear diferentes tipos de datos de prueba válidos e inválidos.
 * 
 * Implementa el principio de Responsabilidad Única y encapsula
 * toda la lógica de generación de datos de prueba.
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
public class GeneradorDatosPrueba {
    
    private static final Logger logger = LoggerFactory.getLogger(GeneradorDatosPrueba.class);
    private static final SecureRandom random = new SecureRandom();
    
    // Arrays de datos para generación aleatoria
    private static final String[] NOMBRES = {
        "Roberto", "Ana", "Carlos", "María", "José", "Carmen", "Luis", "Isabel",
        "Miguel", "Patricia", "Antonio", "Rosa", "Francisco", "Laura", "Jesús",
        "Pilar", "Manuel", "Mercedes", "David", "Dolores", "Pedro", "Francisca"
    };
    
    private static final String[] APELLIDOS = {
        "García", "González", "Rodríguez", "Fernández", "López", "Martínez",
        "Sánchez", "Pérez", "Gómez", "Martín", "Jiménez", "Ruiz", "Hernández",
        "Díaz", "Moreno", "Álvarez", "Muñoz", "Romero", "Alonso", "Gutiérrez"
    };
    
    private static final String[] DOMINIOS_EMAIL = {
        "gmail.com", "yahoo.com", "hotmail.com", "outlook.com", "test.com",
        "example.com", "mail.com", "email.com", "correo.com", "demo.com"
    };
    
    private static final String[] PAISES = {
        "Chile", "Argentina", "México", "España", "Colombia", "Perú",
        "Venezuela", "Ecuador", "Bolivia", "Uruguay", "Paraguay"
    };
    
    private static final String[] CIUDADES = {
        "Santiago", "Buenos Aires", "Ciudad de México", "Madrid", "Bogotá",
        "Lima", "Caracas", "Quito", "La Paz", "Montevideo", "Asunción"
    };
    
    private static final String[] GENEROS = {"Masculino", "Femenino", "Otro", "Prefiero no decir"};
    
    // Constructores privados para clase utilitaria
    private GeneradorDatosPrueba() {
        throw new IllegalStateException("Clase utilitaria - no debe ser instanciada");
    }
    
    // === MÉTODOS PÚBLICOS DE GENERACIÓN ===
    
    /**
     * Genera un usuario válido con datos aleatorios
     * @return ModeloDatosPrueba con datos válidos
     */
    public static ModeloDatosPrueba generarUsuarioValido() {
        return generarUsuarioValido("USR_" + System.currentTimeMillis());
    }
    
    /**
     * Genera un usuario válido con un caso de prueba específico
     * @param casoPrueba identificador del caso de prueba
     * @return ModeloDatosPrueba con datos válidos
     */
    public static ModeloDatosPrueba generarUsuarioValido(String casoPrueba) {
        String nombre = obtenerElementoAleatorio(NOMBRES);
        String apellido = obtenerElementoAleatorio(APELLIDOS);
        String email = generarEmailUnico(nombre, apellido);
        String password = generarPasswordSeguro();
        
        return ModeloDatosPrueba.builder()
                .casoPrueba(casoPrueba)
                .descripcion("Usuario válido generado automáticamente")
                .nombre(nombre)
                .apellido(apellido)
                .email(email)
                .password(password)
                .confirmacionPassword(password)
                .telefono(generarTelefono())
                .genero(obtenerElementoAleatorio(GENEROS))
                .pais(obtenerElementoAleatorio(PAISES))
                .ciudad(obtenerElementoAleatorio(CIUDADES))
                .fechaNacimiento(generarFechaNacimiento())
                .esValido(true)
                .resultadoEsperado("Registro exitoso")
                .build();
    }
    
    /**
     * Genera múltiples usuarios válidos
     * @param cantidad número de usuarios a generar
     * @return lista de ModeloDatosPrueba con datos válidos
     */
    public static List<ModeloDatosPrueba> generarUsuariosValidos(int cantidad) {
        List<ModeloDatosPrueba> usuarios = new ArrayList<>();
        
        for (int i = 1; i <= cantidad; i++) {
            String casoPrueba = String.format("USR_VALID_%03d", i);
            usuarios.add(generarUsuarioValido(casoPrueba));
        }
        
        logger.info(TipoMensaje.INFORMATIVO.formatearMensaje(
            "Generados " + cantidad + " usuarios válidos"));
        
        return usuarios;
    }
    
    /**
     * Genera un usuario con datos inválidos para pruebas negativas
     * @param tipoError tipo de error a simular
     * @return ModeloDatosPrueba con datos inválidos
     */
    public static ModeloDatosPrueba generarUsuarioInvalido(TipoErrorDatos tipoError) {
        String casoPrueba = "USR_INVALID_" + tipoError.name();
        ModeloDatosPrueba.Builder builder = ModeloDatosPrueba.builder()
                .casoPrueba(casoPrueba)
                .esValido(false);
        
        switch (tipoError) {
            case NOMBRE_VACIO:
                return builder
                        .descripcion("Usuario con nombre vacío")
                        .nombre("")
                        .apellido(obtenerElementoAleatorio(APELLIDOS))
                        .email(generarEmailUnico("test", "user"))
                        .password(generarPasswordSeguro())
                        .confirmacionPassword(generarPasswordSeguro())
                        .mensajeError("El nombre es obligatorio")
                        .build();
                        
            case EMAIL_INVALIDO:
                return builder
                        .descripcion("Usuario con email inválido")
                        .nombre(obtenerElementoAleatorio(NOMBRES))
                        .apellido(obtenerElementoAleatorio(APELLIDOS))
                        .email(generarEmailInvalido())
                        .password(generarPasswordSeguro())
                        .confirmacionPassword(generarPasswordSeguro())
                        .mensajeError("Formato de email inválido")
                        .build();
                        
            case PASSWORDS_NO_COINCIDEN:
                return builder
                        .descripcion("Usuario con contraseñas diferentes")
                        .nombre(obtenerElementoAleatorio(NOMBRES))
                        .apellido(obtenerElementoAleatorio(APELLIDOS))
                        .email(generarEmailUnico("test", "user"))
                        .password(generarPasswordSeguro())
                        .confirmacionPassword(generarPasswordSeguro())
                        .mensajeError("Las contraseñas no coinciden")
                        .build();
                        
            case PASSWORD_DEBIL:
                return builder
                        .descripcion("Usuario con contraseña débil")
                        .nombre(obtenerElementoAleatorio(NOMBRES))
                        .apellido(obtenerElementoAleatorio(APELLIDOS))
                        .email(generarEmailUnico("test", "user"))
                        .password(generarPasswordDebil())
                        .confirmacionPassword(generarPasswordDebil())
                        .mensajeError("La contraseña no cumple los requisitos")
                        .build();
                        
            case CAMPOS_VACIOS:
                return builder
                        .descripcion("Usuario con campos obligatorios vacíos")
                        .nombre("")
                        .apellido("")
                        .email("")
                        .password("")
                        .confirmacionPassword("")
                        .mensajeError("Campos obligatorios vacíos")
                        .build();
                        
            default:
                return generarUsuarioValido(casoPrueba);
        }
    }
    
    /**
     * Genera datos específicos para pruebas de login
     * @param esValido indica si los datos deben ser válidos o inválidos
     * @return ModeloDatosPrueba configurado para login
     */
    public static ModeloDatosPrueba generarDatosLogin(boolean esValido) {
        if (esValido) {
            return ModeloDatosPrueba.builder()
                    .casoPrueba("LOGIN_VALID_" + System.currentTimeMillis())
                    .descripcion("Datos de login válidos")
                    .email("usuario.login@test.com")
                    .password("Password123!")
                    .esValido(true)
                    .resultadoEsperado("Login exitoso")
                    .build();
        } else {
            return ModeloDatosPrueba.builder()
                    .casoPrueba("LOGIN_INVALID_" + System.currentTimeMillis())
                    .descripcion("Datos de login inválidos")
                    .email("usuario.invalido@test.com")
                    .password("PasswordIncorrecto")
                    .esValido(false)
                    .mensajeError("Credenciales incorrectas")
                    .build();
        }
    }
    
    // === MÉTODOS PRIVADOS DE GENERACIÓN ===
    
    /**
     * Genera un email único combinando nombre, apellido y timestamp
     * @param nombre nombre base para el email
     * @param apellido apellido base para el email
     * @return email único
     */
    private static String generarEmailUnico(String nombre, String apellido) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String dominio = obtenerElementoAleatorio(DOMINIOS_EMAIL);
        
        return String.format("%s.%s.%s@%s", 
                           nombre.toLowerCase(), 
                           apellido.toLowerCase(), 
                           timestamp.substring(timestamp.length() - 6), 
                           dominio);
    }
    
    /**
     * Genera un email con formato inválido para pruebas negativas
     * @return email con formato inválido
     */
    private static String generarEmailInvalido() {
        String[] emailsInvalidos = {
            "email-sin-arroba.com",
            "email@sin-punto",
            "email@.com",
            "@dominio.com",
            "email espacios@dominio.com",
            "email..doble.punto@dominio.com",
            "email@dominio..com"
        };
        
        return obtenerElementoAleatorio(emailsInvalidos);
    }
    
    /**
     * Genera una contraseña segura que cumple todos los criterios
     * @return contraseña segura
     */
    private static String generarPasswordSeguro() {
        String[] passwordsSeguras = {
            "Password123!",
            "SecurePass456#",
            "StrongPwd789$",
            "MySecure2024!",
            "TestPass123#",
            "AutoTest456$",
            "QAPassword789!",
            "TestData2024#"
        };
        
        return obtenerElementoAleatorio(passwordsSeguras);
    }
    
    /**
     * Genera una contraseña débil para pruebas negativas
     * @return contraseña que no cumple criterios de seguridad
     */
    private static String generarPasswordDebil() {
        String[] passwordsDebiles = {
            "123",              // Muy corta
            "password",         // Solo minúsculas
            "PASSWORD",         // Solo mayúsculas
            "12345678",         // Solo números
            "Password",         // Sin números ni símbolos
            "pass123",          // Sin mayúsculas ni símbolos
            "PASS123",          // Sin minúsculas ni símbolos
            "abc"               // Muy corta
        };
        
        return obtenerElementoAleatorio(passwordsDebiles);
    }
    
    /**
     * Genera un número de teléfono chileno válido
     * @return número de teléfono
     */
    private static String generarTelefono() {
        // Generar teléfono móvil chileno: +569XXXXXXXX
        int numero = 10000000 + random.nextInt(90000000);
        return "+569" + numero;
    }
    
    /**
     * Genera una fecha de nacimiento aleatoria para un adulto
     * @return fecha en formato dd/MM/yyyy
     */
    private static String generarFechaNacimiento() {
        // Generar fecha entre 18 y 80 años atrás
        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaMinima = fechaActual.minusYears(80);
        LocalDate fechaMaxima = fechaActual.minusYears(18);
        
        long diasDiferencia = fechaMaxima.toEpochDay() - fechaMinima.toEpochDay();
        long diasAleatorios = Math.abs(random.nextLong()) % diasDiferencia;
        
        LocalDate fechaNacimiento = fechaMinima.plusDays(diasAleatorios);
        
        return fechaNacimiento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
    
    /**
     * Obtiene un elemento aleatorio de un array
     * @param array array de elementos
     * @return elemento aleatorio
     */
    private static String obtenerElementoAleatorio(String[] array) {
        return array[random.nextInt(array.length)];
    }
    
    /**
     * Genera datos específicos para pruebas de seguridad
     * @return ModeloDatosPrueba con datos para pruebas de seguridad
     */
    public static ModeloDatosPrueba generarDatosSeguridadXSS() {
        return ModeloDatosPrueba.builder()
                .casoPrueba("SEC_XSS_001")
                .descripcion("Datos con payload XSS")
                .nombre("<script>alert('XSS')</script>")
                .apellido("javascript:alert('XSS')")
                .email("xss.test@security.com")
                .password("Password123!")
                .confirmacionPassword("Password123!")
                .esValido(false)
                .mensajeError("Caracteres no permitidos")
                .build();
    }
    
    /**
     * Genera datos con caracteres especiales válidos
     * @return ModeloDatosPrueba con caracteres especiales
     */
    public static ModeloDatosPrueba generarDatosCaracteresEspeciales() {
        return ModeloDatosPrueba.builder()
                .casoPrueba("CHAR_ESP_001")
                .descripcion("Datos con caracteres especiales válidos")
                .nombre("José María")
                .apellido("Pérez-González")
                .email("jose.maria." + System.currentTimeMillis() + "@test.com")
                .password("P@ssw0rd!#$")
                .confirmacionPassword("P@ssw0rd!#$")
                .esValido(true)
                .build();
    }
    
    /**
     * Enumeración que define los tipos de errores para generar datos inválidos
     */
    public enum TipoErrorDatos {
        NOMBRE_VACIO("Nombre vacío"),
        APELLIDO_VACIO("Apellido vacío"),
        EMAIL_VACIO("Email vacío"),
        EMAIL_INVALIDO("Email con formato inválido"),
        PASSWORD_VACIO("Password vacío"),
        PASSWORD_DEBIL("Password que no cumple criterios"),
        PASSWORDS_NO_COINCIDEN("Contraseñas diferentes"),
        CAMPOS_VACIOS("Todos los campos obligatorios vacíos"),
        CARACTERES_ESPECIALES("Caracteres especiales no permitidos"),
        LONGITUD_EXCESIVA("Campos exceden longitud máxima");
        
        private final String descripcion;
        
        TipoErrorDatos(String descripcion) {
            this.descripcion = descripcion;
        }
        
        public String getDescripcion() {
            return descripcion;
        }
    }
}
