package com.automatizacion.proyecto.utilidades;

import com.automatizacion.proyecto.datos.ModeloDatosPrueba;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase utilitaria simplificada para leer datos de prueba.
 * Proporciona métodos para cargar datos de prueba para TestNG DataProvider.
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
public class LectorDatosPrueba {
    
    private static final Logger logger = LoggerFactory.getLogger(LectorDatosPrueba.class);
    
    // Constructor privado para clase utilitaria
    private LectorDatosPrueba() {
        throw new IllegalStateException("Clase utilitaria - no debe instanciarse");
    }
    
    /**
     * Obtiene datos de prueba para login válido.
     * 
     * @return Array bidimensional con datos de login válido
     */
    public static Object[][] obtenerDatosLoginValido() {
        logger.info("Generando datos de login válido");
        
        List<ModeloDatosPrueba> datos = new ArrayList<>();
        
        // Datos de login válido
        datos.add(ModeloDatosPrueba.loginValido("LOGIN_001", "admin@test.com", "Password123!"));
        datos.add(ModeloDatosPrueba.loginValido("LOGIN_002", "usuario@test.com", "SecurePass456!"));
        datos.add(ModeloDatosPrueba.loginValido("LOGIN_003", "qa.tester@test.com", "TestPass789!"));
        datos.add(ModeloDatosPrueba.loginValido("LOGIN_004", "roberto.rivas@test.com", "MyPassword2024!"));
        datos.add(ModeloDatosPrueba.loginValido("LOGIN_005", "test.user@gmail.com", "ValidPass123!"));
        
        return convertirAArray(datos);
    }
    
    /**
     * Obtiene datos de prueba para login inválido.
     * 
     * @return Array bidimensional con datos de login inválido
     */
    public static Object[][] obtenerDatosLoginInvalido() {
        logger.info("Generando datos de login inválido");
        
        List<ModeloDatosPrueba> datos = new ArrayList<>();
        
        // Datos de login inválido
        datos.add(ModeloDatosPrueba.loginInvalido("LOGIN_INV_001", "usuario.inexistente@test.com", "Password123!", "Usuario no encontrado"));
        datos.add(ModeloDatosPrueba.loginInvalido("LOGIN_INV_002", "admin@test.com", "WrongPassword", "Credenciales incorrectas"));
        datos.add(ModeloDatosPrueba.loginInvalido("LOGIN_INV_003", "", "Password123!", "Complete todos los campos"));
        datos.add(ModeloDatosPrueba.loginInvalido("LOGIN_INV_004", "usuario@test.com", "", "Complete todos los campos"));
        datos.add(ModeloDatosPrueba.loginInvalido("LOGIN_INV_005", "formato.invalido", "Password123!", "Ingrese un email válido"));
        
        return convertirAArray(datos);
    }
    
    /**
     * Obtiene datos de prueba para registro válido.
     * 
     * @return Array bidimensional con datos de registro válido
     */
    public static Object[][] obtenerDatosRegistroValido() {
        logger.info("Generando datos de registro válido");
        
        List<ModeloDatosPrueba> datos = new ArrayList<>();
        
        // Datos de registro válido
        datos.add(ModeloDatosPrueba.registroValido("REG_001", "Juan", "Pérez", "juan.perez@test.com", "Password123!"));
        datos.add(ModeloDatosPrueba.registroValido("REG_002", "María", "González", "maria.gonzalez@test.com", "SecurePass456!"));
        datos.add(ModeloDatosPrueba.registroValido("REG_003", "Carlos", "Rodríguez", "carlos.rodriguez@test.com", "MyPassword789!"));
        datos.add(ModeloDatosPrueba.registroValido("REG_004", "Ana", "López", "ana.lopez@gmail.com", "TestPass2024!"));
        datos.add(ModeloDatosPrueba.registroValido("REG_005", "Pedro", "Silva", "pedro.silva@yahoo.com", "AutoTest123!"));
        
        return convertirAArray(datos);
    }
    
    /**
     * Obtiene datos de prueba para registro inválido.
     * 
     * @return Array bidimensional con datos de registro inválido
     */
    public static Object[][] obtenerDatosRegistroInvalido() {
        logger.info("Generando datos de registro inválido");
        
        List<ModeloDatosPrueba> datos = new ArrayList<>();
        
        // Datos de registro inválido
        datos.add(ModeloDatosPrueba.registroInvalido("REG_INV_001", "", "López", "test1@email.com", "Password123!", "El nombre es obligatorio"));
        datos.add(ModeloDatosPrueba.registroInvalido("REG_INV_002", "Ana", "", "test2@email.com", "Password123!", "El apellido es obligatorio"));
        datos.add(ModeloDatosPrueba.registroInvalido("REG_INV_003", "Pedro", "Silva", "", "Password123!", "El email es obligatorio"));
        datos.add(ModeloDatosPrueba.registroInvalido("REG_INV_004", "Laura", "Morales", "laura@test.com", "", "La contraseña es obligatoria"));
        datos.add(ModeloDatosPrueba.registroInvalido("REG_INV_005", "Diego", "Fernández", "email.invalido", "Password123!", "Ingrese un email válido"));
        
        return convertirAArray(datos);
    }
    
    /**
     * Lee datos desde archivo CSV (método básico).
     * Si el archivo no existe, retorna datos de ejemplo.
     * 
     * @param nombreArchivo Nombre del archivo CSV
     * @return Array bidimensional con datos
     */
    public static Object[][] obtenerDatosCSVPrueba(String nombreArchivo) {
        logger.info("Intentando leer archivo CSV: {}", nombreArchivo);
        
        // Por ahora retorna datos de ejemplo
        // Más adelante se puede implementar lectura real de CSV
        
        if (nombreArchivo.contains("login_valido")) {
            return obtenerDatosLoginValido();
        } else if (nombreArchivo.contains("login_invalido")) {
            return obtenerDatosLoginInvalido();
        } else if (nombreArchivo.contains("registro_valido")) {
            return obtenerDatosRegistroValido();
        } else if (nombreArchivo.contains("registro_invalido")) {
            return obtenerDatosRegistroInvalido();
        } else {
            return crearDatosEjemplo();
        }
    }
    
    /**
     * Lee datos desde archivo Excel (método básico).
     * Si el archivo no existe, retorna datos de ejemplo.
     * 
     * @param nombreArchivo Nombre del archivo Excel
     * @param nombreHoja Nombre de la hoja
     * @return Array bidimensional con datos
     */
    public static Object[][] leerDatosExcel(String nombreArchivo, String nombreHoja) {
        logger.info("Intentando leer archivo Excel: {} - Hoja: {}", nombreArchivo, nombreHoja);
        
        // Por ahora retorna datos de ejemplo
        // Más adelante se puede implementar lectura real de Excel
        
        if (nombreHoja.toLowerCase().contains("login")) {
            return obtenerDatosLoginValido();
        } else if (nombreHoja.toLowerCase().contains("registro")) {
            return obtenerDatosRegistroValido();
        } else {
            return crearDatosEjemplo();
        }
    }
    
    /**
     * Obtiene datos combinados para todas las pruebas.
     * 
     * @return Array bidimensional con datos mixtos
     */
    public static Object[][] obtenerDatosTodosTipos() {
        logger.info("Generando datos combinados para todas las pruebas");
        
        List<ModeloDatosPrueba> datos = new ArrayList<>();
        
        // Agregar algunos de cada tipo
        datos.add(ModeloDatosPrueba.loginValido("LOGIN_001", "admin@test.com", "Password123!"));
        datos.add(ModeloDatosPrueba.registroValido("REG_001", "Juan", "Pérez", "juan.perez@test.com", "Password123!"));
        datos.add(ModeloDatosPrueba.loginInvalido("LOGIN_INV_001", "wrong@test.com", "WrongPass", "Credenciales incorrectas"));
        datos.add(ModeloDatosPrueba.registroInvalido("REG_INV_001", "", "López", "test@email.com", "Password123!", "El nombre es obligatorio"));
        
        return convertirAArray(datos);
    }
    
    /**
     * Crea datos de ejemplo cuando no se pueden leer archivos.
     * 
     * @return Array bidimensional con datos de ejemplo
     */
    private static Object[][] crearDatosEjemplo() {
        logger.info("Creando datos de ejemplo como fallback");
        
        List<ModeloDatosPrueba> datos = new ArrayList<>();
        
        // Datos básicos de ejemplo
        datos.add(ModeloDatosPrueba.loginValido("EJEMPLO_LOGIN", "usuario@test.com", "Password123!"));
        datos.add(ModeloDatosPrueba.registroValido("EJEMPLO_REG", "Usuario", "Ejemplo", "usuario.ejemplo@test.com", "Password123!"));
        
        return convertirAArray(datos);
    }
    
    /**
     * Convierte lista de ModeloDatosPrueba a array bidimensional para TestNG.
     * 
     * @param datos Lista de datos
     * @return Array bidimensional
     */
    private static Object[][] convertirAArray(List<ModeloDatosPrueba> datos) {
        if (datos == null || datos.isEmpty()) {
            logger.warn("Lista de datos vacía, retornando array mínimo");
            return new Object[][] {
                {ModeloDatosPrueba.loginValido("DEFAULT", "default@test.com", "Password123!")}
            };
        }
        
        Object[][] resultado = new Object[datos.size()][1];
        for (int i = 0; i < datos.size(); i++) {
            resultado[i][0] = datos.get(i);
        }
        
        logger.info("Convertidos {} registros a array para TestNG", datos.size());
        return resultado;
    }
    
    /**
     * Valida que los datos estén correctamente formateados.
     * 
     * @param datos Array de datos a validar
     * @return true si los datos son válidos
     */
    public static boolean validarDatos(Object[][] datos) {
        if (datos == null || datos.length == 0) {
            logger.warn("Array de datos es null o vacío");
            return false;
        }
        
        for (int i = 0; i < datos.length; i++) {
            if (datos[i].length == 0 || !(datos[i][0] instanceof ModeloDatosPrueba)) {
                logger.warn("Fila {} no contiene ModeloDatosPrueba válido", i);
                return false;
            }
        }
        
        logger.info("Validación de datos exitosa: {} registros válidos", datos.length);
        return true;
    }
    
    /**
     * Obtiene información sobre los datos cargados.
     * 
     * @param datos Array de datos
     * @return String con estadísticas
     */
    public static String obtenerEstadisticas(Object[][] datos) {
        if (datos == null || datos.length == 0) {
            return "No hay datos disponibles";
        }
        
        int total = datos.length;
        int login = 0;
        int registro = 0;
        int validos = 0;
        int invalidos = 0;
        
        for (Object[] fila : datos) {
            if (fila.length > 0 && fila[0] instanceof ModeloDatosPrueba) {
                ModeloDatosPrueba modelo = (ModeloDatosPrueba) fila[0];
                
                if (modelo.getCasoPrueba().startsWith("LOGIN")) {
                    login++;
                } else if (modelo.getCasoPrueba().startsWith("REG")) {
                    registro++;
                }
                
                if (modelo.isEsValido()) {
                    validos++;
                } else {
                    invalidos++;
                }
            }
        }
        
        return String.format("Total: %d | Login: %d | Registro: %d | Válidos: %d | Inválidos: %d", 
                           total, login, registro, validos, invalidos);
    }
}