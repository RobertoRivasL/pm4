package com.automatizacion.proyecto.datos;

import com.automatizacion.proyecto.configuracion.ConfiguracionGlobal;
import com.automatizacion.proyecto.enums.TipoMensaje;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Proveedor de datos para las pruebas automatizadas.
 * Maneja la carga de datos desde diferentes fuentes (CSV, Excel, generados).
 * 
 * Principios aplicados:
 * - SRP: Solo se encarga de proveer datos para pruebas
 * - DRY: Centraliza la lógica de carga de datos
 * - Abstracción: Oculta la complejidad de diferentes fuentes de datos
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
public class ProveedorDatos {
    
    private static final Logger logger = LoggerFactory.getLogger(ProveedorDatos.class);
    private static final ConfiguracionGlobal configuracion = ConfiguracionGlobal.obtenerInstancia();
    
    // Rutas de archivos de datos
    private static final String RUTA_DATOS_CSV = "src/test/resources/datos/usuarios.csv";
    private static final String RUTA_DATOS_EXCEL = "src/test/resources/datos/datos_prueba.xlsx";
    
    // === DATA PROVIDERS PARA REGISTRO ===
    
    /**
     * Proporciona datos válidos para pruebas de registro exitoso
     * @return array de objetos ModeloDatosPrueba
     */
    @DataProvider(name = "datosRegistroValidos")
    public static Object[][] obtenerDatosRegistroValidos() {
        logger.info(TipoMensaje.INFORMATIVO.formatearMensaje("Generando datos válidos para registro"));
        
        List<ModeloDatosPrueba> datosValidos = Arrays.asList(
            ModeloDatosPrueba.builder()
                .casoPrueba("REG_VALIDO_001")
                .descripcion("Registro con datos básicos válidos")
                .nombre("Usuario")
                .apellido("Prueba")
                .email("usuario.prueba." + System.currentTimeMillis() + "@test.com")
                .password("Password123!")
                .confirmacionPassword("Password123!")
                .esValido(true)
                .build(),
                
            ModeloDatosPrueba.builder()
                .casoPrueba("REG_VALIDO_002")
                .descripcion("Registro con email complejo")
                .nombre("Roberto")
                .apellido("Rivas")
                .email("roberto.rivas.test+" + System.currentTimeMillis() + "@practice.com")
                .password("SuperSecure123#")
                .confirmacionPassword("SuperSecure123#")
                .esValido(true)
                .build(),
                
            ModeloDatosPrueba.builder()
                .casoPrueba("REG_VALIDO_003")
                .descripcion("Registro con caracteres especiales válidos")
                .nombre("Test-User")
                .apellido("Practice'Test")
                .email("special.chars." + System.currentTimeMillis() + "@test-domain.co.uk")
                .password("MyP@ssw0rd!")
                .confirmacionPassword("MyP@ssw0rd!")
                .esValido(true)
                .build()
        );
        
        return convertirListaAArray(datosValidos);
    }
    
    /**
     * Proporciona datos inválidos para pruebas de registro negativas
     * @return array de objetos ModeloDatosPrueba
     */
    @DataProvider(name = "datosRegistroInvalidos")
    public static Object[][] obtenerDatosRegistroInvalidos() {
        logger.info(TipoMensaje.INFORMATIVO.formatearMensaje("Generando datos inválidos para registro"));
        
        List<ModeloDatosPrueba> datosInvalidos = Arrays.asList(
            // Email vacío
            ModeloDatosPrueba.builder()
                .casoPrueba("REG_INVALIDO_001")
                .descripcion("Registro con email vacío")
                .nombre("Usuario")
                .apellido("Test")
                .email("")
                .password("Password123!")
                .confirmacionPassword("Password123!")
                .esValido(false)
                .mensajeError("Email es obligatorio")
                .build(),
                
            // Email inválido
            ModeloDatosPrueba.builder()
                .casoPrueba("REG_INVALIDO_002")
                .descripcion("Registro con email inválido")
                .nombre("Usuario")
                .apellido("Test")
                .email("email-invalido")
                .password("Password123!")
                .confirmacionPassword("Password123!")
                .esValido(false)
                .mensajeError("Formato de email inválido")
                .build(),
                
            // Contraseñas no coinciden
            ModeloDatosPrueba.builder()
                .casoPrueba("REG_INVALIDO_003")
                .descripcion("Contraseñas diferentes")
                .nombre("Usuario")
                .apellido("Test")
                .email("test." + System.currentTimeMillis() + "@test.com")
                .password("Password123!")
                .confirmacionPassword("DiferentePassword456!")
                .esValido(false)
                .mensajeError("Las contraseñas no coinciden")
                .build(),
                
            // Contraseña débil
            ModeloDatosPrueba.builder()
                .casoPrueba("REG_INVALIDO_004")
                .descripcion("Contraseña débil")
                .nombre("Usuario")
                .apellido("Test")
                .email("weak.password." + System.currentTimeMillis() + "@test.com")
                .password("123")
                .confirmacionPassword("123")
                .esValido(false)
                .mensajeError("Contraseña muy débil")
                .build(),
                
            // Campos vacíos
            ModeloDatosPrueba.builder()
                .casoPrueba("REG_INVALIDO_005")
                .descripcion("Todos los campos vacíos")
                .nombre("")
                .apellido("")
                .email("")
                .password("")
                .confirmacionPassword("")
                .esValido(false)
                .mensajeError("Campos obligatorios vacíos")
                .build()
        );
        
        return convertirListaAArray(datosInvalidos);
    }
    
    // === DATA PROVIDERS PARA LOGIN ===
    
    /**
     * Proporciona datos válidos para pruebas de login exitoso
     * @return array de objetos ModeloDatosPrueba
     */
    @DataProvider(name = "datosLoginValidos")
    public static Object[][] obtenerDatosLoginValidos() {
        logger.info(TipoMensaje.INFORMATIVO.formatearMensaje("Generando datos válidos para login"));
        
        List<ModeloDatosPrueba> datosValidos = Arrays.asList(
            // Credenciales válidas según practice.expandtesting.com
            ModeloDatosPrueba.builder()
                .casoPrueba("LOGIN_VALIDO_001")
                .descripcion("Login con credenciales válidas principales")
                .email("practice")
                .password("SuperSecretPassword!")
                .esValido(true)
                .build(),
                
            // Casos adicionales si hubiera múltiples usuarios válidos
            ModeloDatosPrueba.builder()
                .casoPrueba("LOGIN_VALIDO_002")
                .descripcion("Login case sensitive")
                .email("practice")
                .password("SuperSecretPassword!")
                .esValido(true)
                .build()
        );
        
        return convertirListaAArray(datosValidos);
    }
    
    /**
     * Proporciona datos inválidos para pruebas de login negativas
     * @return array de objetos ModeloDatosPrueba
     */
    @DataProvider(name = "datosLoginInvalidos")
    public static Object[][] obtenerDatosLoginInvalidos() {
        logger.info(TipoMensaje.INFORMATIVO.formatearMensaje("Generando datos inválidos para login"));
        
        List<ModeloDatosPrueba> datosInvalidos = Arrays.asList(
            // Usuario incorrecto
            ModeloDatosPrueba.builder()
                .casoPrueba("LOGIN_INVALIDO_001")
                .descripcion("Usuario inexistente")
                .email("usuario_inexistente")
                .password("SuperSecretPassword!")
                .esValido(false)
                .mensajeError("Credenciales inválidas")
                .build(),
                
            // Contraseña incorrecta
            ModeloDatosPrueba.builder()
                .casoPrueba("LOGIN_INVALIDO_002")
                .descripcion("Contraseña incorrecta")
                .email("practice")
                .password("PasswordIncorrecto123!")
                .esValido(false)
                .mensajeError("Credenciales inválidas")
                .build(),
                
            // Usuario vacío
            ModeloDatosPrueba.builder()
                .casoPrueba("LOGIN_INVALIDO_003")
                .descripcion("Usuario vacío")
                .email("")
                .password("SuperSecretPassword!")
                .esValido(false)
                .mensajeError("Usuario es obligatorio")
                .build(),
                
            // Contraseña vacía
            ModeloDatosPrueba.builder()
                .casoPrueba("LOGIN_INVALIDO_004")
                .descripcion("Contraseña vacía")
                .email("practice")
                .password("")
                .esValido(false)
                .mensajeError("Contraseña es obligatoria")
                .build(),
                
            // Ambos campos vacíos
            ModeloDatosPrueba.builder()
                .casoPrueba("LOGIN_INVALIDO_005")
                .descripcion("Ambos campos vacíos")
                .email("")
                .password("")
                .esValido(false)
                .mensajeError("Campos obligatorios vacíos")
                .build(),
                
            // Caracteres especiales maliciosos
            ModeloDatosPrueba.builder()
                .casoPrueba("LOGIN_INVALIDO_006")
                .descripcion("Intento de inyección SQL")
                .email("'; DROP TABLE users; --")
                .password("SuperSecretPassword!")
                .esValido(false)
                .mensajeError("Caracteres no válidos")
                .build()
        );
        
        return convertirListaAArray(datosInvalidos);
    }
    
    // === DATA PROVIDERS DESDE ARCHIVOS EXTERNOS ===
    
    /**
     * Carga datos desde archivo CSV
     * @return array de objetos ModeloDatosPrueba desde CSV
     */
    @DataProvider(name = "datosDesdeCSV")
    public static Object[][] obtenerDatosDesdeCSV() {
        logger.info(TipoMensaje.INFORMATIVO.formatearMensaje("Cargando datos desde CSV: " + RUTA_DATOS_CSV));
        
        List<ModeloDatosPrueba> datos = new ArrayList<>();
        
        try {
            Path rutaArchivo = Paths.get(RUTA_DATOS_CSV);
            
            // Crear archivo de ejemplo si no existe
            if (!Files.exists(rutaArchivo)) {
                crearArchivoCSVEjemplo(rutaArchivo);
            }
            
            List<String> lineas = Files.readAllLines(rutaArchivo);
            
            // Saltar header (primera línea)
            for (int i = 1; i < lineas.size(); i++) {
                String linea = lineas.get(i).trim();
                if (!linea.isEmpty()) {
                    ModeloDatosPrueba dato = parsearLineaCSV(linea, i);
                    if (dato != null) {
                        datos.add(dato);
                    }
                }
            }
            
            logger.info(TipoMensaje.EXITO.formatearMensaje("Cargados " + datos.size() + " registros desde CSV"));
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error cargando CSV: " + e.getMessage()));
            
            // Fallback: retornar datos por defecto
            datos = Arrays.asList(ModeloDatosPrueba.crearDatosValidos());
        }
        
        return convertirListaAArray(datos);
    }
    
    /**
     * Carga datos desde archivo Excel
     * @return array de objetos ModeloDatosPrueba desde Excel
     */
    @DataProvider(name = "datosDesdeExcel")
    public static Object[][] obtenerDatosDesdeExcel() {
        logger.info(TipoMensaje.INFORMATIVO.formatearMensaje("Cargando datos desde Excel: " + RUTA_DATOS_EXCEL));
        
        List<ModeloDatosPrueba> datos = new ArrayList<>();
        
        try {
            Path rutaArchivo = Paths.get(RUTA_DATOS_EXCEL);
            
            // Crear archivo de ejemplo si no existe
            if (!Files.exists(rutaArchivo)) {
                crearArchivoExcelEjemplo(rutaArchivo);
            }
            
            try (FileInputStream fis = new FileInputStream(rutaArchivo.toFile());
                 Workbook workbook = new XSSFWorkbook(fis)) {
                
                Sheet sheet = workbook.getSheetAt(0);
                
                // Iterar filas (saltar header)
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row fila = sheet.getRow(i);
                    if (fila != null) {
                        ModeloDatosPrueba dato = parsearFilaExcel(fila, i);
                        if (dato != null) {
                            datos.add(dato);
                        }
                    }
                }
            }
            
            logger.info(TipoMensaje.EXITO.formatearMensaje("Cargados " + datos.size() + " registros desde Excel"));
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error cargando Excel: " + e.getMessage()));
            
            // Fallback: retornar datos por defecto
            datos = Arrays.asList(ModeloDatosPrueba.crearDatosValidos());
        }
        
        return convertirListaAArray(datos);
    }
    
    // === DATA PROVIDERS COMBINADOS ===
    
    /**
     * Combina todos los datos válidos de diferentes fuentes
     * @return array de objetos ModeloDatosPrueba combinados
     */
    @DataProvider(name = "todosDatosValidos")
    public static Object[][] obtenerTodosDatosValidos() {
        logger.info(TipoMensaje.INFORMATIVO.formatearMensaje("Combinando todos los datos válidos"));
        
        List<ModeloDatosPrueba> todosLosDatos = new ArrayList<>();
        
        // Agregar datos de registro válidos
        Object[][] datosRegistro = obtenerDatosRegistroValidos();
        for (Object[] fila : datosRegistro) {
            todosLosDatos.add((ModeloDatosPrueba) fila[0]);
        }
        
        // Agregar datos de login válidos
        Object[][] datosLogin = obtenerDatosLoginValidos();
        for (Object[] fila : datosLogin) {
            todosLosDatos.add((ModeloDatosPrueba) fila[0]);
        }
        
        return convertirListaAArray(todosLosDatos);
    }
    
    /**
     * Proporciona datos específicos para pruebas de seguridad
     * @return array de objetos ModeloDatosPrueba para seguridad
     */
    @DataProvider(name = "datosSeguridad")
    public static Object[][] obtenerDatosSeguridad() {
        logger.info(TipoMensaje.INFORMATIVO.formatearMensaje("Generando datos para pruebas de seguridad"));
        
        List<ModeloDatosPrueba> datosSeguridad = Arrays.asList(
            // XSS Attempts
            ModeloDatosPrueba.builder()
                .casoPrueba("SEC_001")
                .descripcion("Intento XSS en email")
                .email("<script>alert('XSS')</script>@test.com")
                .password("Password123!")
                .confirmacionPassword("Password123!")
                .esValido(false)
                .mensajeError("Caracteres no permitidos")
                .build(),
                
            // SQL Injection
            ModeloDatosPrueba.builder()
                .casoPrueba("SEC_002")
                .descripcion("Intento SQL Injection")
                .email("admin'; DROP TABLE users; --")
                .password("Password123!")
                .esValido(false)
                .mensajeError("Caracteres no válidos")
                .build(),
                
            // Contraseñas comunes
            ModeloDatosPrueba.builder()
                .casoPrueba("SEC_003")
                .descripcion("Contraseña muy común")
                .email("test.security@test.com")
                .password("123456")
                .confirmacionPassword("123456")
                .esValido(false)
                .mensajeError("Contraseña muy débil")
                .build()
        );
        
        return convertirListaAArray(datosSeguridad);
    }
    
    // === MÉTODOS PRIVADOS DE APOYO ===
    
    /**
     * Convierte una lista de ModeloDatosPrueba a array bidimensional para TestNG
     * @param lista lista a convertir
     * @return array bidimensional
     */
    private static Object[][] convertirListaAArray(List<ModeloDatosPrueba> lista) {
        Object[][] array = new Object[lista.size()][1];
        for (int i = 0; i < lista.size(); i++) {
            array[i][0] = lista.get(i);
        }
        return array;
    }
    
    /**
     * Parsea una línea CSV y la convierte a ModeloDatosPrueba
     * @param linea línea CSV
     * @param numeroLinea número de línea para logging
     * @return ModeloDatosPrueba o null si hay error
     */
    private static ModeloDatosPrueba parsearLineaCSV(String linea, int numeroLinea) {
        try {
            String[] campos = linea.split(",");
            
            // Formato CSV esperado: casoPrueba,descripcion,email,password,confirmPassword,esValido
            if (campos.length >= 6) {
                return ModeloDatosPrueba.builder()
                    .casoPrueba(campos[0].trim())
                    .descripcion(campos[1].trim())
                    .email(campos[2].trim())
                    .password(campos[3].trim())
                    .confirmacionPassword(campos[4].trim())
                    .esValido(Boolean.parseBoolean(campos[5].trim()))
                    .build();
            }
            
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                "Error parseando línea " + numeroLinea + " del CSV: " + e.getMessage()));
        }
        
        return null;
    }
    
    /**
     * Parsea una fila Excel y la convierte a ModeloDatosPrueba
     * @param fila fila Excel
     * @param numeroFila número de fila para logging
     * @return ModeloDatosPrueba o null si hay error
     */
    private static ModeloDatosPrueba parsearFilaExcel(Row fila, int numeroFila) {
        try {
            return ModeloDatosPrueba.builder()
                .casoPrueba(obtenerValorCelda(fila.getCell(0)))
                .descripcion(obtenerValorCelda(fila.getCell(1)))
                .email(obtenerValorCelda(fila.getCell(2)))
                .password(obtenerValorCelda(fila.getCell(3)))
                .confirmacionPassword(obtenerValorCelda(fila.getCell(4)))
                .esValido(Boolean.parseBoolean(obtenerValorCelda(fila.getCell(5))))
                .build();
                
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                "Error parseando fila " + numeroFila + " del Excel: " + e.getMessage()));
            return null;
        }
    }
    
    /**
     * Obtiene el valor de una celda Excel como String
     * @param celda celda Excel
     * @return valor como String
     */
    private static String obtenerValorCelda(Cell celda) {
        if (celda == null) {
            return "";
        }
        
        switch (celda.getCellType()) {
            case STRING:
                return celda.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf((long) celda.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(celda.getBooleanCellValue());
            default:
                return "";
        }
    }
    
    /**
     * Crea un archivo CSV de ejemplo si no existe
     * @param rutaArchivo ruta donde crear el archivo
     */
    private static void crearArchivoCSVEjemplo(Path rutaArchivo) {
        try {
            // Crear directorio si no existe
            Files.createDirectories(rutaArchivo.getParent());
            
            List<String> lineas = Arrays.asList(
                "casoPrueba,descripcion,email,password,confirmPassword,esValido",
                "CSV_001,Usuario válido desde CSV,csv.user1@test.com,Password123!,Password123!,true",
                "CSV_002,Usuario válido 2 desde CSV,csv.user2@test.com,SecurePass456#,SecurePass456#,true",
                "CSV_003,Email inválido desde CSV,email-invalido,Password123!,Password123!,false",
                "CSV_004,Contraseñas diferentes desde CSV,csv.user3@test.com,Password123!,DiferentePass456!,false"
            );
            
            Files.write(rutaArchivo, lineas);
            
            logger.info(TipoMensaje.CONFIGURACION.formatearMensaje(
                "Archivo CSV de ejemplo creado: " + rutaArchivo));
                
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error creando archivo CSV de ejemplo: " + e.getMessage()));
        }
    }
    
    /**
     * Crea un archivo Excel de ejemplo si no existe
     * @param rutaArchivo ruta donde crear el archivo
     */
    private static void crearArchivoExcelEjemplo(Path rutaArchivo) {
        try {
            // Crear directorio si no existe
            Files.createDirectories(rutaArchivo.getParent());
            
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("DatosPrueba");
                
                // Header
                Row header = sheet.createRow(0);
                header.createCell(0).setCellValue("casoPrueba");
                header.createCell(1).setCellValue("descripcion");
                header.createCell(2).setCellValue("email");
                header.createCell(3).setCellValue("password");
                header.createCell(4).setCellValue("confirmPassword");
                header.createCell(5).setCellValue("esValido");
                
                // Datos de ejemplo
                String[][] datosEjemplo = {
                    {"EXCEL_001", "Usuario válido desde Excel", "excel.user1@test.com", "Password123!", "Password123!", "true"},
                    {"EXCEL_002", "Usuario válido 2 desde Excel", "excel.user2@test.com", "SecurePass456#", "SecurePass456#", "true"},
                    {"EXCEL_003", "Email inválido desde Excel", "email-invalido", "Password123!", "Password123!", "false"},
                    {"EXCEL_004", "Contraseñas diferentes desde Excel", "excel.user3@test.com", "Password123!", "DiferentePass456!", "false"}
                };
                
                for (int i = 0; i < datosEjemplo.length; i++) {
                    Row fila = sheet.createRow(i + 1);
                    for (int j = 0; j < datosEjemplo[i].length; j++) {
                        if (j == 5) { // Campo booleano
                            fila.createCell(j).setCellValue(Boolean.parseBoolean(datosEjemplo[i][j]));
                        } else {
                            fila.createCell(j).setCellValue(datosEjemplo[i][j]);
                        }
                    }
                }
                
                // Auto-ajustar columnas
                for (int i = 0; i < 6; i++) {
                    sheet.autoSizeColumn(i);
                }
                
                // Guardar archivo
                try (FileOutputStream fos = new FileOutputStream(rutaArchivo.toFile())) {
                    workbook.write(fos);
                }
            }
            
            logger.info(TipoMensaje.CONFIGURACION.formatearMensaje(
                "Archivo Excel de ejemplo creado: " + rutaArchivo));
                
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error creando archivo Excel de ejemplo: " + e.getMessage()));
        }
    }
}