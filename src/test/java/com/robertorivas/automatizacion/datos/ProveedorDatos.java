package com.robertorivas.automatizacion.datos;

import com.robertorivas.automatizacion.modelos.DatosRegistro;
import com.robertorivas.automatizacion.modelos.Usuario;
import com.robertorivas.automatizacion.utilidades.GestorDatos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Proveedor centralizado de datos para las pruebas automatizadas.
 * Implementa el patrón Data Provider de TestNG para pruebas data-driven.
 * 
 * Principios aplicados:
 * - Single Responsibility: Solo provee datos para pruebas
 * - Data Driven Testing: Separa datos de lógica de pruebas
 * - Factory Pattern: Crea diferentes tipos de datos de prueba
 * - Iterator Pattern: Para manejo eficiente de grandes datasets
 * 
 * @author Roberto Rivas Lopez
 */
public class ProveedorDatos {
    
    private static final Logger logger = LoggerFactory.getLogger(ProveedorDatos.class);
    private static GestorDatos gestorDatos;
    
    // Inicialización estática del gestor de datos
    static {
        gestorDatos = new GestorDatos();
        logger.info("ProveedorDatos inicializado");
    }
    
    // ===== DATA PROVIDERS PARA DATOS DE REGISTRO =====
    
    /**
     * Proveedor de datos para registro válido desde archivo CSV.
     */
    @DataProvider(name = "datosRegistroValidos")
    public static Object[][] proveerDatosRegistroValidos() {
        logger.info("Cargando datos de registro válidos");
        
        try {
            List<DatosRegistro> datosRegistro = gestorDatos.leerDatosRegistro();
            
            if (datosRegistro.isEmpty()) {
                logger.warn("No se encontraron datos de registro en CSV, generando datos por defecto");
                datosRegistro = generarDatosRegistroValidosPorDefecto();
            }
            
            // Convertir lista a array bidimensional para TestNG
            Object[][] datos = new Object[datosRegistro.size()][1];
            for (int i = 0; i < datosRegistro.size(); i++) {
                datos[i][0] = datosRegistro.get(i);
            }
            
            logger.info("Se cargaron {} conjuntos de datos de registro válidos", datos.length);
            return datos;
            
        } catch (Exception e) {
            logger.error("Error cargando datos de registro válidos: {}", e.getMessage());
            return generarDatosRegistroValidosEmergencia();
        }
    }
    
    /**
     * Proveedor de datos para registro inválido.
     */
    @DataProvider(name = "datosRegistroInvalidos")
    public static Object[][] proveerDatosRegistroInvalidos() {
        logger.info("Generando datos de registro inválidos");
        
        List<DatosRegistro> datosInvalidos = new ArrayList<>();
        
        // Email inválido
        datosInvalidos.add(new DatosRegistro.Builder()
                .email("email-invalido-sin-arroba.com")
                .password("ValidPass123")
                .confirmarPassword("ValidPass123")
                .nombre("Test")
                .apellido("User")
                .aceptarTerminos()
                .build());
        
        // Contraseñas no coinciden
        datosInvalidos.add(new DatosRegistro.Builder()
                .email("test.passwords@testautomation.com")
                .password("Password123")
                .confirmarPassword("DiferentPassword456")
                .nombre("Test")
                .apellido("User")
                .aceptarTerminos()
                .build());
        
        // Contraseña muy corta
        datosInvalidos.add(new DatosRegistro.Builder()
                .email("test.shortpass@testautomation.com")
                .password("123")
                .confirmarPassword("123")
                .nombre("Test")
                .apellido("User")
                .aceptarTerminos()
                .build());
        
        // Sin nombre
        datosInvalidos.add(new DatosRegistro.Builder()
                .email("test.noname@testautomation.com")
                .password("ValidPass123")
                .confirmarPassword("ValidPass123")
                .nombre("")
                .apellido("User")
                .aceptarTerminos()
                .build());
        
        // Sin apellido
        datosInvalidos.add(new DatosRegistro.Builder()
                .email("test.nosurname@testautomation.com")
                .password("ValidPass123")
                .confirmarPassword("ValidPass123")
                .nombre("Test")
                .apellido("")
                .aceptarTerminos()
                .build());
        
        // Sin aceptar términos
        datosInvalidos.add(new DatosRegistro.Builder()
                .email("test.noterms@testautomation.com")
                .password("ValidPass123")
                .confirmarPassword("ValidPass123")
                .nombre("Test")
                .apellido("User")
                // No acepta términos
                .build());
        
        // Email vacío
        datosInvalidos.add(new DatosRegistro.Builder()
                .email("")
                .password("ValidPass123")
                .confirmarPassword("ValidPass123")
                .nombre("Test")
                .apellido("User")
                .aceptarTerminos()
                .build());
        
        // Password vacío
        datosInvalidos.add(new DatosRegistro.Builder()
                .email("test.nopass@testautomation.com")
                .password("")
                .confirmarPassword("")
                .nombre("Test")
                .apellido("User")
                .aceptarTerminos()
                .build());
        
        Object[][] datos = new Object[datosInvalidos.size()][1];
        for (int i = 0; i < datosInvalidos.size(); i++) {
            datos[i][0] = datosInvalidos.get(i);
        }
        
        logger.info("Se generaron {} conjuntos de datos de registro inválidos", datos.length);
        return datos;
    }
    
    /**
     * Proveedor de datos para diferentes tipos de registro.
     */
    @DataProvider(name = "tiposRegistro")
    public static Object[][] proveerTiposRegistro() {
        List<DatosRegistro> tiposRegistro = new ArrayList<>();
        
        // Registro Normal
        tiposRegistro.add(new DatosRegistro.Builder()
                .email("registro.normal@testautomation.com")
                .password("NormalPass123")
                .confirmarPassword("NormalPass123")
                .nombre("Usuario")
                .apellido("Normal")
                .tipoRegistro(DatosRegistro.TipoRegistro.NORMAL)
                .aceptarTerminos()
                .build());
        
        // Registro Premium
        tiposRegistro.add(new DatosRegistro.Builder()
                .email("registro.premium@testautomation.com")
                .password("PremiumPass123")
                .confirmarPassword("PremiumPass123")
                .nombre("Usuario")
                .apellido("Premium")
                .tipoRegistro(DatosRegistro.TipoRegistro.PREMIUM)
                .aceptarTerminos()
                .recibirNotificaciones(true)
                .build());
        
        // Registro Empresa
        tiposRegistro.add(new DatosRegistro.Builder()
                .email("registro.empresa@testautomation.com")
                .password("EmpresaPass123")
                .confirmarPassword("EmpresaPass123")
                .nombre("Usuario")
                .apellido("Empresa")
                .tipoRegistro(DatosRegistro.TipoRegistro.EMPRESA)
                .aceptarTerminos()
                .build());
        
        // Registro Estudiante
        tiposRegistro.add(new DatosRegistro.Builder()
                .email("registro.estudiante@testautomation.com")
                .password("EstudiantePass123")
                .confirmarPassword("EstudiantePass123")
                .nombre("Usuario")
                .apellido("Estudiante")
                .tipoRegistro(DatosRegistro.TipoRegistro.ESTUDIANTE)
                .aceptarTerminos()
                .build());
        
        Object[][] datos = new Object[tiposRegistro.size()][1];
        for (int i = 0; i < tiposRegistro.size(); i++) {
            datos[i][0] = tiposRegistro.get(i);
        }
        
        logger.info("Se generaron {} tipos de registro diferentes", datos.length);
        return datos;
    }
    
    // ===== DATA PROVIDERS PARA DATOS DE LOGIN =====
    
    /**
     * Proveedor de usuarios válidos para login desde archivo CSV.
     */
    @DataProvider(name = "usuariosValidos")
    public static Object[][] proveerUsuariosValidos() {
        logger.info("Cargando usuarios válidos para login");
        
        try {
            List<Usuario> usuarios = gestorDatos.leerDatosLogin();
            
            if (usuarios.isEmpty()) {
                logger.warn("No se encontraron usuarios válidos en CSV, generando usuarios por defecto");
                usuarios = generarUsuariosValidosPorDefecto();
            }
            
            Object[][] datos = new Object[usuarios.size()][1];
            for (int i = 0; i < usuarios.size(); i++) {
                datos[i][0] = usuarios.get(i);
            }
            
            logger.info("Se cargaron {} usuarios válidos", datos.length);
            return datos;
            
        } catch (Exception e) {
            logger.error("Error cargando usuarios válidos: {}", e.getMessage());
            return generarUsuariosValidosEmergencia();
        }
    }
    
    /**
     * Proveedor de credenciales inválidas desde archivo CSV.
     */
    @DataProvider(name = "credencialesInvalidas")
    public static Object[][] proveerCredencialesInvalidas() {
        logger.info("Cargando credenciales inválidas");
        
        try {
            List<Usuario> credencialesInvalidas = gestorDatos.leerCredencialesInvalidas();
            
            if (credencialesInvalidas.isEmpty()) {
                logger.warn("No se encontraron credenciales inválidas en CSV, generando por defecto");
                credencialesInvalidas = generarCredencialesInvalidasPorDefecto();
            }
            
            Object[][] datos = new Object[credencialesInvalidas.size()][1];
            for (int i = 0; i < credencialesInvalidas.size(); i++) {
                datos[i][0] = credencialesInvalidas.get(i);
            }
            
            logger.info("Se cargaron {} conjuntos de credenciales inválidas", datos.length);
            return datos;
            
        } catch (Exception e) {
            logger.error("Error cargando credenciales inválidas: {}", e.getMessage());
            return generarCredencialesInvalidasEmergencia();
        }
    }
    
    /**
     * Proveedor de combinaciones email/password para validaciones.
     */
    @DataProvider(name = "combinacionesLogin")
    public static Object[][] proveerCombinacionesLogin() {
        List<Object[]> combinaciones = new ArrayList<>();
        
        // Email válido, password válido
        combinaciones.add(new Object[]{"student", "Password123", true, "Login válido"});
        
        // Email válido, password inválido
        combinaciones.add(new Object[]{"student", "PasswordIncorrecto", false, "Password incorrecto"});
        
        // Email inválido, password válido
        combinaciones.add(new Object[]{"usuario.inexistente@test.com", "Password123", false, "Usuario inexistente"});
        
        // Email inválido, password inválido
        combinaciones.add(new Object[]{"usuario.inexistente@test.com", "PasswordIncorrecto", false, "Ambos inválidos"});
        
        // Email vacío, password válido
        combinaciones.add(new Object[]{"", "Password123", false, "Email vacío"});
        
        // Email válido, password vacío
        combinaciones.add(new Object[]{"student", "", false, "Password vacío"});
        
        // Ambos vacíos
        combinaciones.add(new Object[]{"", "", false, "Ambos vacíos"});
        
        // Email con formato inválido
        combinaciones.add(new Object[]{"email-sin-arroba.com", "Password123", false, "Email formato inválido"});
        
        // Password muy corto
        combinaciones.add(new Object[]{"student", "123", false, "Password muy corto"});
        
        Object[][] datos = combinaciones.toArray(new Object[0][]);
        
        logger.info("Se generaron {} combinaciones de login", datos.length);
        return datos;
    }
    
    // ===== DATA PROVIDERS CON ITERADORES (Para datasets grandes) =====
    
    /**
     * Proveedor con iterador para datasets grandes de registro.
     */
    @DataProvider(name = "datosRegistroGrandes")
    public static Iterator<Object[]> proveerDatosRegistroGrandes() {
        logger.info("Generando dataset grande de datos de registro");
        
        List<Object[]> datosGrandes = new ArrayList<>();
        
        // Generar 50 conjuntos de datos aleatorios
        List<DatosRegistro> datosAleatorios = gestorDatos.generarDatosRegistroAleatorios(50);
        
        for (DatosRegistro datos : datosAleatorios) {
            datosGrandes.add(new Object[]{datos});
        }
        
        logger.info("Dataset grande generado con {} registros", datosGrandes.size());
        return datosGrandes.iterator();
    }
    
    /**
     * Proveedor con iterador para datasets grandes de usuarios.
     */
    @DataProvider(name = "usuariosGrandes")
    public static Iterator<Object[]> proveerUsuariosGrandes() {
        logger.info("Generando dataset grande de usuarios");
        
        List<Object[]> usuariosGrandes = new ArrayList<>();
        
        // Generar 30 usuarios aleatorios
        List<Usuario> usuariosAleatorios = gestorDatos.generarUsuariosAleatorios(30);
        
        for (Usuario usuario : usuariosAleatorios) {
            usuariosGrandes.add(new Object[]{usuario});
        }
        
        logger.info("Dataset grande generado con {} usuarios", usuariosGrandes.size());
        return usuariosGrandes.iterator();
    }
    
    // ===== DATA PROVIDERS ESPECÍFICOS =====
    
    /**
     * Proveedor de datos para pruebas de cross-browser.
     */
    @DataProvider(name = "navegadores")
    public static Object[][] proveerNavegadores() {
        return new Object[][]{
            {"chrome"},
            {"firefox"},
            {"edge"}
        };
    }
    
    /**
     * Proveedor de datos para diferentes resoluciones de pantalla.
     */
    @DataProvider(name = "resoluciones")
    public static Object[][] proveerResoluciones() {
        return new Object[][]{
            {1920, 1080, "Full HD"},
            {1366, 768, "HD"},
            {1280, 720, "HD Ready"},
            {1024, 768, "XGA"}
        };
    }
    
    /**
     * Proveedor de formatos de email válidos e inválidos.
     */
    @DataProvider(name = "formatosEmail")
    public static Object[][] proveerFormatosEmail() {
        return new Object[][]{
            {"usuario@dominio.com", true, "Email válido estándar"},
            {"usuario.nombre@dominio.com", true, "Email con punto en nombre"},
            {"usuario+etiqueta@dominio.com", true, "Email con plus"},
            {"usuario@subdominio.dominio.com", true, "Email con subdominio"},
            {"123456@dominio.com", true, "Email numérico"},
            {"usuario@dominio-con-guion.com", true, "Dominio con guión"},
            
            {"usuario", false, "Sin arroba ni dominio"},
            {"@dominio.com", false, "Sin nombre de usuario"},
            {"usuario@", false, "Sin dominio"},
            {"usuario@@dominio.com", false, "Doble arroba"},
            {"usuario@dominio", false, "Sin extensión de dominio"},
            {"usuario con espacios@dominio.com", false, "Espacios en nombre"},
            {"usuario@dominio con espacios.com", false, "Espacios en dominio"},
            {"", false, "Email vacío"}
        };
    }
    
    // ===== MÉTODOS DE GENERACIÓN DE DATOS POR DEFECTO =====
    
    /**
     * Genera datos de registro válidos por defecto.
     */
    private static List<DatosRegistro> generarDatosRegistroValidosPorDefecto() {
        List<DatosRegistro> datos = new ArrayList<>();
        
        datos.add(new DatosRegistro.Builder()
                .email("usuario1@testautomation.com")
                .password("Password123")
                .confirmarPassword("Password123")
                .nombre("Juan")
                .apellido("Pérez")
                .telefono("+34600123456")
                .genero("Masculino")
                .pais("España")
                .ciudad("Madrid")
                .aceptarTerminos()
                .recibirNotificaciones(true)
                .build());
        
        datos.add(new DatosRegistro.Builder()
                .email("usuario2@testautomation.com")
                .password("SecurePass456")
                .confirmarPassword("SecurePass456")
                .nombre("María")
                .apellido("García")
                .telefono("+34600654321")
                .genero("Femenino")
                .pais("España")
                .ciudad("Barcelona")
                .aceptarTerminos()
                .recibirNotificaciones(false)
                .build());
        
        datos.add(new DatosRegistro.Builder()
                .email("usuario3@testautomation.com")
                .password("MyPassword789")
                .confirmarPassword("MyPassword789")
                .nombre("Carlos")
                .apellido("López")
                .aceptarTerminos()
                .build());
        
        return datos;
    }
    
    /**
     * Genera usuarios válidos por defecto.
     */
    private static List<Usuario> generarUsuariosValidosPorDefecto() {
        List<Usuario> usuarios = new ArrayList<>();
        
        usuarios.add(new Usuario("student", "Password123"));
        usuarios.add(new Usuario("admin", "admin123"));
        usuarios.add(new Usuario("test@testautomation.com", "testpass123"));
        usuarios.add(new Usuario("demo@demo.com", "demopass"));
        
        return usuarios;
    }
    
    /**
     * Genera credenciales inválidas por defecto.
     */
    private static List<Usuario> generarCredencialesInvalidasPorDefecto() {
        List<Usuario> credencialesInvalidas = new ArrayList<>();
        
        credencialesInvalidas.add(new Usuario("usuario.inexistente@test.com", "passwordIncorrecto"));
        credencialesInvalidas.add(new Usuario("admin", "passwordMalo"));
        credencialesInvalidas.add(new Usuario("email-malformado@", "password123"));
        credencialesInvalidas.add(new Usuario("test@test.com", "123"));
        credencialesInvalidas.add(new Usuario("", "password"));
        credencialesInvalidas.add(new Usuario("usuario@test.com", ""));
        
        return credencialesInvalidas;
    }
    
    // ===== MÉTODOS DE EMERGENCIA =====
    
    /**
     * Datos de emergencia para registro en caso de error.
     */
    private static Object[][] generarDatosRegistroValidosEmergencia() {
        DatosRegistro datosEmergencia = new DatosRegistro.Builder()
                .email("emergencia@testautomation.com")
                .password("Emergency123")
                .confirmarPassword("Emergency123")
                .nombre("Usuario")
                .apellido("Emergencia")
                .aceptarTerminos()
                .build();
        
        return new Object[][]{{datosEmergencia}};
    }
    
    /**
     * Usuarios de emergencia en caso de error.
     */
    private static Object[][] generarUsuariosValidosEmergencia() {
        Usuario usuarioEmergencia = new Usuario("student", "Password123");
        return new Object[][]{{usuarioEmergencia}};
    }
    
    /**
     * Credenciales inválidas de emergencia.
     */
    private static Object[][] generarCredencialesInvalidasEmergencia() {
        Usuario credencialesEmergencia = new Usuario("usuario.inexistente@test.com", "passwordIncorrecto");
        return new Object[][]{{credencialesEmergencia}};
    }
    
    // ===== MÉTODOS DE UTILIDAD =====
    
    /**
     * Obtiene estadísticas de los data providers.
     */
    public static String obtenerEstadisticas() {
        StringBuilder stats = new StringBuilder();
        stats.append("=== ESTADÍSTICAS DE DATA PROVIDERS ===\n");
        
        try {
            Object[][] datosRegistro = proveerDatosRegistroValidos();
            stats.append("- Datos de registro válidos: ").append(datosRegistro.length).append("\n");
            
            Object[][] datosRegistroInvalidos = proveerDatosRegistroInvalidos();
            stats.append("- Datos de registro inválidos: ").append(datosRegistroInvalidos.length).append("\n");
            
            Object[][] usuarios = proveerUsuariosValidos();
            stats.append("- Usuarios válidos: ").append(usuarios.length).append("\n");
            
            Object[][] credencialesInvalidas = proveerCredencialesInvalidas();
            stats.append("- Credenciales inválidas: ").append(credencialesInvalidas.length).append("\n");
            
            Object[][] combinaciones = proveerCombinacionesLogin();
            stats.append("- Combinaciones de login: ").append(combinaciones.length).append("\n");
            
            Object[][] navegadores = proveerNavegadores();
            stats.append("- Navegadores soportados: ").append(navegadores.length).append("\n");
            
        } catch (Exception e) {
            stats.append("Error obteniendo estadísticas: ").append(e.getMessage()).append("\n");
        }
        
        stats.append("=====================================");
        return stats.toString();
    }
    
    /**
     * Valida la integridad de todos los data providers.
     */
    public static boolean validarIntegridadDataProviders() {
        try {
            logger.info("Validando integridad de todos los data providers...");
            
            Object[][] datosRegistro = proveerDatosRegistroValidos();
            if (datosRegistro.length == 0) {
                logger.error("No hay datos de registro válidos");
                return false;
            }
            
            Object[][] usuarios = proveerUsuariosValidos();
            if (usuarios.length == 0) {
                logger.error("No hay usuarios válidos");
                return false;
            }
            
            Object[][] credencialesInvalidas = proveerCredencialesInvalidas();
            if (credencialesInvalidas.length == 0) {
                logger.error("No hay credenciales inválidas");
                return false;
            }
            
            // Validar que los datos no sean nulos
            for (Object[] registro : datosRegistro) {
                if (registro[0] == null) {
                    logger.error("Dato de registro nulo encontrado");
                    return false;
                }
            }
            
            for (Object[] usuario : usuarios) {
                if (usuario[0] == null) {
                    logger.error("Usuario nulo encontrado");
                    return false;
                }
            }
            
            logger.info("Validación de integridad completada exitosamente");
            return true;
            
        } catch (Exception e) {
            logger.error("Error durante validación de integridad: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Limpia el cache del gestor de datos.
     */
    public static void limpiarCache() {
        if (gestorDatos != null) {
            gestorDatos.limpiarCache();
            logger.info("Cache de data providers limpiado");
        }
    }
}