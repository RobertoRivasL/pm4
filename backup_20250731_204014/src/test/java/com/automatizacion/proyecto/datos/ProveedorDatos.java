package test.java.com.automatizacion.proyecto.datos;

import com.automatizacion.proyecto.datos.ModeloDatosPrueba;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.DataProvider;
import java.io.*;
import java.util.*;

/**
 * Proveedor centralizado de datos para las pruebas.
 * Implementa diferentes fuentes de datos: CSV, Excel, generación dinámica.
 * 
 * Principios aplicados:
 * - SRP: Solo se encarga de proveer datos de prueba
 * - Strategy: Diferentes estrategias para obtener datos
 * - Factory: Creación de diferentes tipos de datos
 * 
 * @author Roberto Rivas Lopez
 */
public class ProveedorDatos {
    
    private static final Random random = new Random();
    private static final String RUTA_DATOS = "src/test/resources/datos/";
    
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
    
    /**
     * Proveedor de datos para login válido
     */
    @DataProvider(name = "datosLoginValidos")
    public static Object[][] credencialesLoginValidas() {
        return new Object[][] {
            {crearDatosLogin("LOGIN_001", "Login exitoso con credenciales válidas", 
                "usuario.valido@test.com", "Password123!", true)},
            {crearDatosLogin("LOGIN_002", "Login con email válido alternativo", 
                "admin@test.com", "AdminPass456#", true)},
            {crearDatosLogin("LOGIN_003", "Login con usuario de prueba", 
                "qa.tester@test.com", "QATest789$", true)},
            {crearDatosLogin("LOGIN_004", "Usuario con caracteres especiales", 
                "test.special@tést.com", "Spëcïal123!", true)},
            {crearDatosLogin("LOGIN_005", "Email con formato complejo", 
                "test+tag@sub-domain.co.uk", "TestPass123#", true)}
        };
    }
    
    /**
     * Proveedor de datos para login inválido
     */
    @DataProvider(name = "datosLoginInvalidos")
    public static Object[][] credencialesLoginInvalidas() {
        return new Object[][] {
            {crearDatosLogin("LOGIN_INV_001", "Credenciales inválidas - email incorrecto",
                "usuario.incorrecto@test.com", "Password123!", false)},
            {crearDatosLogin("LOGIN_INV_002", "Credenciales inválidas - password incorrecto",
                "usuario.valido@test.com", "PasswordIncorrecto", false)},
            {crearDatosLogin("LOGIN_INV_003", "Email vacío",
                "", "Password123!", false)},
            {crearDatosLogin("LOGIN_INV_004", "Password vacío",
                "usuario.valido@test.com", "", false)},
            {crearDatosLogin("LOGIN_INV_005", "Ambos campos vacíos",
                "", "", false)},
            {crearDatosLogin("LOGIN_INV_006", "Email con formato inválido",
                "email-sin-arroba.com", "Password123!", false)},
            {crearDatosLogin("LOGIN_INV_007", "Email con espacios",
                "usuario con espacios@test.com", "Password123!", false)},
            {crearDatosLogin("LOGIN_INV_008", "Password muy corta",
                "usuario.valido@test.com", "123", false)},
            {crearDatosLogin("LOGIN_INV_009", "Inyección SQL en email",
                "admin'OR'1'='1", "Password123!", false)},
            {crearDatosLogin("LOGIN_INV_010", "Intento de XSS en password",
                "usuario.test@test.com", "<script>alert('xss')</script>", false)}
        };
    }
    
    /**
     * Proveedor de datos para registro válido
     */
    @DataProvider(name = "datosRegistroValidos")
    public static Object[][] datosRegistroValidos() {
        return new Object[][] {
            {crearDatosRegistro("REG_001", "Usuario completo válido", "Roberto", "Rivas",
                generarEmailUnico(), "Password123!", "Password123!", "+56912345678", true)},
            {crearDatosRegistro("REG_002", "Usuario mínimo válido", "Ana", "García",
                generarEmailUnico(), "SecurePass456#", "SecurePass456#", "", true)},
            {crearDatosRegistro("REG_003", "Usuario con caracteres especiales", "José María", "Pérez-González",
                generarEmailUnico(), "StrongPwd789$", "StrongPwd789$", "+56987654321", true)},
            {crearDatosRegistro("REG_004", "Usuario internacional", "John", "Smith",
                generarEmailUnico(), "MySecure2024!", "MySecure2024!", "+1234567890", true)},
            {crearDatosRegistro("REG_005", "Usuario con email especial", "Test User", "Apellido-Compuesto",
                generarEmailUnico(), "TestPass123#", "TestPass123#", "+44123456789", true)}
        };
    }
    
    /**
     * Proveedor de datos para registro inválido
     */
    @DataProvider(name = "datosRegistroInvalidos")
    public static Object[][] datosRegistroInvalidos() {
        return new Object[][] {
            {crearDatosRegistro("REG_INV_001", "Nombre vacío", "", "García",
                "vacio.nombre@test.com", "Password123!", "Password123!", "+56912345678", false)},
            {crearDatosRegistro("REG_INV_002", "Apellido vacío", "Roberto", "",
                "vacio.apellido@test.com", "Password123!", "Password123!", "+56912345678", false)},
            {crearDatosRegistro("REG_INV_003", "Email inválido", "Roberto", "Rivas",
                "email-sin-arroba.com", "Password123!", "Password123!", "+56912345678", false)},
            {crearDatosRegistro("REG_INV_004", "Contraseñas no coinciden", "Roberto", "Rivas",
                "no.coinciden@test.com", "Password123!", "DiferentePass456!", "+56912345678", false)},
            {crearDatosRegistro("REG_INV_005", "Contraseña débil", "Roberto", "Rivas",
                "password.debil@test.com", "123", "123", "+56912345678", false)},
            {crearDatosRegistro("REG_INV_006", "Todos los campos vacíos", "", "",
                "", "", "", "", false)},
            {crearDatosRegistro("REG_INV_007", "Email con espacios", "Roberto", "Rivas",
                "email con espacios@test.com", "Password123!", "Password123!", "+56912345678", false)},
            {crearDatosRegistro("REG_INV_008", "Inyección XSS en nombre", "<script>alert('XSS')</script>", "Rivas",
                "xss.test@test.com", "Password123!", "Password123!", "+56912345678", false)},
            {crearDatosRegistro("REG_INV_009", "Longitud excesiva en nombre", "A".repeat(50), "Apellido",
                "longitud.maxima@test.com", "Password123!", "Password123!", "+56912345678", false)},
            {crearDatosRegistro("REG_INV_010", "Teléfono inválido", "Roberto", "Rivas",
                "telefono.invalido@test.com", "Password123!", "Password123!", "telefono-invalido", false)}
        };
    }
    
    /**
     * Crea datos de login
     */
    private static ModeloDatosPrueba crearDatosLogin(String casoPrueba, String descripcion, 
                                                   String email, String password, boolean esValido) {
        return ModeloDatosPrueba.builder()
                .casoPrueba(casoPrueba)
                .descripcion(descripcion)
                .email(email)
                .password(password)
                .esValido(esValido)
                .resultadoEsperado(esValido ? "Login exitoso" : "Login rechazado")
                .mensajeError(esValido ? "" : "Credenciales incorrectas")
                .build();
    }
    
    /**
     * Crea datos de registro
     */
    private static ModeloDatosPrueba crearDatosRegistro(String casoPrueba, String descripcion,
                                                      String nombre, String apellido, String email,
                                                      String password, String confirmPassword,
                                                      String telefono, boolean esValido) {
        return ModeloDatosPrueba.builder()
                .casoPrueba(casoPrueba)
                .descripcion(descripcion)
                .nombre(nombre)
                .apellido(apellido)
                .email(email)
                .password(password)
                .confirmacionPassword(confirmPassword)
                .telefono(telefono)
                .esValido(esValido)
                .resultadoEsperado(esValido ? "Registro exitoso" : "Error de validación")
                .mensajeError(esValido ? "" : "Datos inválidos")
                .build();
    }
    
    /**
     * Genera un email único para evitar conflictos.
     */
    public static String generarEmailUnico() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String nombre = obtenerElementoAleatorio(NOMBRES);
        String dominio = obtenerElementoAleatorio(DOMINIOS_EMAIL);
        return String.format("%s.test.%s@%s", 
                           nombre.toLowerCase(), 
                           timestamp.substring(timestamp.length() - 6), 
                           dominio);
    }
    
    /**
     * Obtiene un elemento aleatorio de un array
     */
    private static String obtenerElementoAleatorio(String[] array) {
        return array[random.nextInt(array.length)];
    }
    
    /**
     * Genera una contraseña segura.
     */
    public static String generarContrasenaSegura() {
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
     * Lee datos desde archivo CSV
     */
    public static Object[][] leerDatosCSV(String nombreArchivo) {
        List<Object[]> datos = new ArrayList<>();
        String rutaArchivo = RUTA_DATOS + nombreArchivo;
        
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            boolean primeraLinea = true;
            
            while ((linea = br.readLine()) != null) {
                // Saltar header si es la primera línea
                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }
                
                String[] valores = procesarLineaCSV(linea);
                if (valores.length > 0 && !esLineaVacia(valores)) {
                    ModeloDatosPrueba dato = mapearCSVAModelo(valores);
                    datos.add(new Object[]{dato});
                }
            }
            
        } catch (IOException e) {
            System.err.println("Error al leer archivo CSV: " + e.getMessage());
            // Retornar datos por defecto en caso de error
            return credencialesLoginInvalidas();
        }
        
        return datos.toArray(new Object[0][]);
    }
    
    /**
     * Procesa una línea CSV manejando comillas y comas.
     */
    private static String[] procesarLineaCSV(String linea) {
        List<String> campos = new ArrayList<>();
        StringBuilder campoActual = new StringBuilder();
        boolean dentroComillas = false;
        
        for (int i = 0; i < linea.length(); i++) {
            char c = linea.charAt(i);
            
            if (c == '"') {
                dentroComillas = !dentroComillas;
            } else if (c == ',' && !dentroComillas) {
                campos.add(campoActual.toString().trim());
                campoActual = new StringBuilder();
            } else {
                campoActual.append(c);
            }
        }
        
        campos.add(campoActual.toString().trim());
        return campos.toArray(new String[0]);
    }
    
    /**
     * Mapea valores CSV a un modelo de datos de prueba
     */
    private static ModeloDatosPrueba mapearCSVAModelo(String[] valores) {
        // Estructura esperada del CSV:
        // caso_prueba,descripcion,email,password,es_valido,resultado_esperado,mensaje_error
        
        if (valores.length >= 5) {
            return ModeloDatosPrueba.builder()
                    .casoPrueba(limpiarValor(valores[0]))
                    .descripcion(limpiarValor(valores[1]))
                    .email(limpiarValor(valores[2]))
                    .password(limpiarValor(valores[3]))
                    .esValido(Boolean.parseBoolean(limpiarValor(valores[4])))
                    .resultadoEsperado(valores.length > 5 ? limpiarValor(valores[5]) : "")
                    .mensajeError(valores.length > 6 ? limpiarValor(valores[6]) : "")
                    .build();
        }
        
        // Datos por defecto si el CSV no tiene la estructura esperada
        return ModeloDatosPrueba.builder()
                .casoPrueba("CSV_ERROR")
                .descripcion("Error procesando CSV")
                .email("error@test.com")
                .password("ErrorPassword123!")
                .esValido(false)
                .build();
    }
    
    /**
     * Limpia un valor CSV removiendo comillas
     */
    private static String limpiarValor(String valor) {
        if (valor == null) return "";
        valor = valor.trim();
        if (valor.startsWith("\"") && valor.endsWith("\"")) {
            valor = valor.substring(1, valor.length() - 1);
        }
        return valor;
    }
    
    /**
     * Verifica si una línea está vacía.
     */
    private static boolean esLineaVacia(String[] valores) {
        if (valores == null || valores.length == 0) {
            return true;
        }
        
        for (String valor : valores) {
            if (valor != null && !valor.trim().isEmpty()) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Crea archivo CSV de ejemplo con datos de prueba.
     */
    public static void crearArchivoCSVEjemplo() {
        String rutaArchivo = RUTA_DATOS + "credenciales_ejemplo.csv";
        
        try {
            // Crear directorio si no existe
            File directorio = new File(RUTA_DATOS);
            if (!directorio.exists()) {
                directorio.mkdirs();
            }
            
            try (PrintWriter writer = new PrintWriter(new FileWriter(rutaArchivo))) {
                writer.println("caso_prueba,descripcion,email,password,es_valido,resultado_esperado,mensaje_error");
                writer.println("LOGIN_001,\"Login exitoso con credenciales válidas\",usuario.valido@test.com,Password123!,true,\"Login exitoso y redirección\",");
                writer.println("LOGIN_002,\"Login con email válido alternativo\",admin@test.com,AdminPass456#,true,\"Login exitoso\",");
                writer.println("LOGIN_003,\"Credenciales inválidas - email incorrecto\",usuario.incorrecto@test.com,Password123!,false,\"Login rechazado\",\"Email no encontrado\"");
                writer.println("LOGIN_004,\"Credenciales inválidas - password incorrecto\",usuario.valido@test.com,PasswordIncorrecto,false,\"Login rechazado\",\"Contraseña incorrecta\"");
                writer.println("LOGIN_005,\"Email vacío\",,Password123!,false,\"Error de validación\",\"Email es obligatorio\"");
            }
            
            System.out.println("Archivo CSV de ejemplo creado: " + rutaArchivo);
            
        } catch (IOException e) {
            System.err.println("Error al crear archivo CSV: " + e.getMessage());
        }
    }
}