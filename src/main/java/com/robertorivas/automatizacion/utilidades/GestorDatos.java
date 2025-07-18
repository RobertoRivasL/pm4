package com.robertorivas.automatizacion.utilidades;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import com.robertorivas.automatizacion.configuracion.ConfiguracionPruebas;
import com.robertorivas.automatizacion.modelos.DatosRegistro;
import com.robertorivas.automatizacion.modelos.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Gestor centralizado para el manejo de datos de prueba.
 * Responsable de leer, escribir y generar datos desde/hacia archivos CSV.
 * 
 * Principios aplicados:
 * - Single Responsibility: Solo maneja operaciones de datos
 * - Caching: Cache para mejorar performance
 * - Thread Safety: Operaciones thread-safe
 * - Error Handling: Manejo robusto de errores
 * 
 * @author Roberto Rivas Lopez
 */
public class GestorDatos {
    
    private static final Logger logger = LoggerFactory.getLogger(GestorDatos.class);
    
    // Configuración y cache
    private final ConfiguracionPruebas config;
    private final Map<String, List<?>> cache;
    private final Random random;
    
    // Constantes para generación de datos
    private static final String[] NOMBRES = {
        "Juan", "María", "Carlos", "Ana", "Luis", "Carmen", "Miguel", "Laura",
        "David", "Isabel", "Francisco", "Pilar", "Antonio", "Rosa", "Manuel"
    };
    
    private static final String[] APELLIDOS = {
        "García", "Rodríguez", "González", "Fernández", "López", "Martínez",
        "Sánchez", "Pérez", "Gómez", "Martín", "Jiménez", "Ruiz", "Hernández"
    };
    
    private static final String[] DOMINIOS_EMAIL = {
        "@testautomation.com", "@expandtesting.com", "@qa.test", "@selenium.test",
        "@webdriver.test", "@automation.com", "@testing.org"
    };
    
    private static final String[] PREFIJOS_USERNAME = {
        "test", "user", "qa", "auto", "selenium", "demo", "practice", "expand"
    };
    
    /**
     * Constructor del gestor de datos.
     */
    public GestorDatos() {
        this.config = ConfiguracionPruebas.obtenerInstancia();
        this.cache = new ConcurrentHashMap<>();
        this.random = new Random();
        
        logger.info("GestorDatos inicializado");
    }
    
    // ===== MÉTODOS PARA LEER DATOS DE LOGIN =====
    
    /**
     * Lee usuarios válidos para login desde CSV.
     */
    public List<Usuario> leerDatosLogin() {
        String cacheKey = "usuarios_login";
        
        @SuppressWarnings("unchecked")
        List<Usuario> usuariosCache = (List<Usuario>) cache.get(cacheKey);
        if (usuariosCache != null) {
            logger.debug("Devolviendo usuarios de login desde cache");
            return new ArrayList<>(usuariosCache);
        }
        
        List<Usuario> usuarios = new ArrayList<>();
        String archivoLogin = config.obtenerArchivoUsuariosLogin();
        
        try {
            Path rutaArchivo = obtenerRutaArchivo(archivoLogin);
            
            if (!Files.exists(rutaArchivo)) {
                logger.warn("Archivo de login no encontrado: {}. Intentando archivo alternativo", rutaArchivo);
                rutaArchivo = obtenerRutaArchivo("usuarios_login_expandtesting.csv");
                
                if (!Files.exists(rutaArchivo)) {
                    logger.warn("Archivo alternativo tampoco existe, generando datos por defecto");
                    return generarUsuariosLoginPorDefecto();
                }
            }
            
            try (CSVReader reader = new CSVReaderBuilder(Files.newBufferedReader(rutaArchivo))
                    .withSkipLines(1)
                    .build()) {
                
                List<String[]> records = reader.readAll();
                
                for (String[] record : records) {
                    if (record.length >= 2) {
                        String username = limpiarCampo(record[0]);
                        String password = limpiarCampo(record[1]);
                        
                        if (!username.isEmpty() && !password.isEmpty()) {
                            Usuario usuario = new Usuario(username, password);
                            
                            if (record.length > 2) usuario.setNombre(limpiarCampo(record[2]));
                            if (record.length > 3) usuario.setApellido(limpiarCampo(record[3]));
                            if (record.length > 4) usuario.setTelefono(limpiarCampo(record[4]));
                            
                            usuarios.add(usuario);
                        }
                    }
                }
                
                logger.info("Cargados {} usuarios válidos para login desde: {}", usuarios.size(), rutaArchivo);
                
            }
            
        } catch (Exception e) {
            logger.error("Error leyendo datos de login: {}", e.getMessage());
            return generarUsuariosLoginPorDefecto();
        }
        
        // Guardar en cache
        cache.put(cacheKey, new ArrayList<>(usuarios));
        
        return usuarios;
    }
    
    /**
     * Lee credenciales inválidas desde CSV.
     */
    public List<Usuario> leerCredencialesInvalidas() {
        String cacheKey = "credenciales_invalidas";
        
        @SuppressWarnings("unchecked")
        List<Usuario> credencialesCache = (List<Usuario>) cache.get(cacheKey);
        if (credencialesCache != null) {
            logger.debug("Devolviendo credenciales inválidas desde cache");
            return new ArrayList<>(credencialesCache);
        }
        
        List<Usuario> credencialesInvalidas = new ArrayList<>();
        String archivoCredenciales = config.obtenerArchivoCredencialesInvalidas();
        
        try {
            Path rutaArchivo = obtenerRutaArchivo(archivoCredenciales);
            
            if (!Files.exists(rutaArchivo)) {
                logger.warn("Archivo de credenciales inválidas no encontrado. Intentando archivo alternativo");
                rutaArchivo = obtenerRutaArchivo("credenciales_invalidas_expandtesting.csv");
                
                if (!Files.exists(rutaArchivo)) {
                    logger.warn("Archivo alternativo tampoco existe, generando datos por defecto");
                    return generarCredencialesInvalidasPorDefecto();
                }
            }
            
            try (CSVReader reader = new CSVReaderBuilder(Files.newBufferedReader(rutaArchivo))
                    .withSkipLines(1)
                    .build()) {
                
                List<String[]> records = reader.readAll();
                
                for (String[] record : records) {
                    if (record.length >= 2) {
                        String username = limpiarCampo(record[0]);
                        String password = limpiarCampo(record[1]);
                        
                        // Para credenciales inválidas permitimos campos vacíos
                        Usuario usuario = new Usuario(
                            username.isEmpty() ? "usuario_vacio_" + System.currentTimeMillis() : username,
                            password.isEmpty() ? "password_vacio" : password
                        );
                        
                        credencialesInvalidas.add(usuario);
                    }
                }
                
                logger.info("Cargadas {} credenciales inválidas desde: {}", credencialesInvalidas.size(), rutaArchivo);
                
            }
            
        } catch (Exception e) {
            logger.error("Error leyendo credenciales inválidas: {}", e.getMessage());
            return generarCredencialesInvalidasPorDefecto();
        }
        
        // Guardar en cache
        cache.put(cacheKey, new ArrayList<>(credencialesInvalidas));
        
        return credencialesInvalidas;
    }
    
    // ===== MÉTODOS PARA LEER DATOS DE REGISTRO =====
    
    /**
     * Lee datos de registro desde CSV.
     */
    public List<DatosRegistro> leerDatosRegistro() {
        String cacheKey = "datos_registro";
        
        @SuppressWarnings("unchecked")
        List<DatosRegistro> datosCache = (List<DatosRegistro>) cache.get(cacheKey);
        if (datosCache != null) {
            logger.debug("Devolviendo datos de registro desde cache");
            return new ArrayList<>(datosCache);
        }
        
        List<DatosRegistro> datosRegistro = new ArrayList<>();
        String archivoRegistro = config.obtenerArchivoUsuariosRegistro();
        
        try {
            Path rutaArchivo = obtenerRutaArchivo(archivoRegistro);
            
            if (!Files.exists(rutaArchivo)) {
                logger.warn("Archivo de registro no encontrado. Intentando archivo alternativo");
                rutaArchivo = obtenerRutaArchivo("usuarios_registro_expandtesting.csv");
                
                if (!Files.exists(rutaArchivo)) {
                    logger.warn("Archivo alternativo tampoco existe, generando datos por defecto");
                    return generarDatosRegistroPorDefecto();
                }
            }
            
            try (CSVReader reader = new CSVReaderBuilder(Files.newBufferedReader(rutaArchivo))
                    .withSkipLines(1)
                    .build()) {
                
                List<String[]> records = reader.readAll();
                
                for (String[] record : records) {
                    if (record.length >= 3) {
                        DatosRegistro datos = new DatosRegistro.Builder()
                                .email(limpiarCampo(record[0]))
                                .password(limpiarCampo(record[1]))
                                .confirmarPassword(limpiarCampo(record[2]))
                                .build();
                        
                        // Campos opcionales
                        if (record.length > 3) datos.setNombre(limpiarCampo(record[3]));
                        if (record.length > 4) datos.setApellido(limpiarCampo(record[4]));
                        if (record.length > 5) datos.setTelefono(limpiarCampo(record[5]));
                        if (record.length > 6) datos.setGenero(limpiarCampo(record[6]));
                        if (record.length > 7) datos.setPais(limpiarCampo(record[7]));
                        if (record.length > 8) datos.setCiudad(limpiarCampo(record[8]));
                        
                        // Configuraciones boolean
                        if (record.length > 9) {
                            datos.setAceptaTerminos(Boolean.parseBoolean(limpiarCampo(record[9])));
                        } else {
                            datos.setAceptaTerminos(true); // Por defecto true
                        }
                        
                        if (record.length > 10) {
                            datos.setRecibirNotificaciones(Boolean.parseBoolean(limpiarCampo(record[10])));
                        }
                        
                        if (record.length > 11) {
                            String tipo = limpiarCampo(record[11]);
                            if (!tipo.isEmpty()) {
                                try {
                                    datos.setTipoRegistro(DatosRegistro.TipoRegistro.valueOf(tipo.toUpperCase()));
                                } catch (IllegalArgumentException e) {
                                    logger.warn("Tipo de registro inválido: {}", tipo);
                                }
                            }
                        }
                        
                        datosRegistro.add(datos);
                    }
                }
                
                logger.info("Cargados {} conjuntos de datos de registro desde: {}", datosRegistro.size(), rutaArchivo);
                
            }
            
        } catch (Exception e) {
            logger.error("Error leyendo datos de registro: {}", e.getMessage());
            return generarDatosRegistroPorDefecto();
        }
        
        // Guardar en cache
        cache.put(cacheKey, new ArrayList<>(datosRegistro));
        
        return datosRegistro;
    }
    
    // ===== MÉTODOS DE GENERACIÓN DE DATOS ALEATORIOS =====
    
    /**
     * Genera usuarios aleatorios para pruebas.
     */
    public List<Usuario> generarUsuariosAleatorios(int cantidad) {
        logger.info("Generando {} usuarios aleatorios", cantidad);
        
        List<Usuario> usuarios = new ArrayList<>();
        
        for (int i = 0; i < cantidad; i++) {
            String username = generarUsernameAleatorio();
            String password = generarPasswordAleatorio();
            String nombre = NOMBRES[random.nextInt(NOMBRES.length)];
            String apellido = APELLIDOS[random.nextInt(APELLIDOS.length)];
            
            Usuario usuario = new Usuario(username, password);
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setTelefono(generarTelefonoAleatorio());
            
            usuarios.add(usuario);
        }
        
        return usuarios;
    }
    
    /**
     * Genera datos de registro aleatorios.
     */
    public List<DatosRegistro> generarDatosRegistroAleatorios(int cantidad) {
        logger.info("Generando {} conjuntos de datos de registro aleatorios", cantidad);
        
        List<DatosRegistro> datosRegistro = new ArrayList<>();
        
        for (int i = 0; i < cantidad; i++) {
            String email = generarEmailAleatorio();
            String password = generarPasswordAleatorio();
            String nombre = NOMBRES[random.nextInt(NOMBRES.length)];
            String apellido = APELLIDOS[random.nextInt(APELLIDOS.length)];
            
            DatosRegistro datos = new DatosRegistro.Builder()
                    .email(email)
                    .password(password)
                    .confirmarPassword(password)
                    .nombre(nombre)
                    .apellido(apellido)
                    .telefono(generarTelefonoAleatorio())
                    .genero(random.nextBoolean() ? "Masculino" : "Femenino")
                    .pais("España")
                    .ciudad("Madrid")
                    .aceptarTerminos()
                    .recibirNotificaciones(random.nextBoolean())
                    .build();
            
            datosRegistro.add(datos);
        }
        
        return datosRegistro;
    }
    
    // ===== MÉTODOS DE ESCRITURA =====
    
    /**
     * Guarda usuarios en archivo CSV.
     */
    public boolean guardarUsuarios(List<Usuario> usuarios, String nombreArchivo) {
        try {
            Path rutaArchivo = config.obtenerRutaDatos().resolve(nombreArchivo);
            
            try (CSVWriter writer = new CSVWriter(Files.newBufferedWriter(rutaArchivo))) {
                // Escribir header
                writer.writeNext(new String[]{"email", "password", "nombre", "apellido", "telefono"});
                
                // Escribir datos
                for (Usuario usuario : usuarios) {
                    writer.writeNext(new String[]{
                        usuario.getEmail(),
                        usuario.getPassword(),
                        usuario.getNombre() != null ? usuario.getNombre() : "",
                        usuario.getApellido() != null ? usuario.getApellido() : "",
                        usuario.getTelefono() != null ? usuario.getTelefono() : ""
                    });
                }
            }
            
            logger.info("Guardados {} usuarios en: {}", usuarios.size(), rutaArchivo);
            return true;
            
        } catch (Exception e) {
            logger.error("Error guardando usuarios: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Guarda datos de registro en archivo CSV.
     */
    public boolean guardarDatosRegistro(List<DatosRegistro> datosRegistro, String nombreArchivo) {
        try {
            Path rutaArchivo = config.obtenerRutaDatos().resolve(nombreArchivo);
            
            try (CSVWriter writer = new CSVWriter(Files.newBufferedWriter(rutaArchivo))) {
                // Escribir header
                writer.writeNext(new String[]{
                    "email", "password", "confirmarPassword", "nombre", "apellido", 
                    "telefono", "genero", "pais", "ciudad", "aceptaTerminos", 
                    "notificaciones", "tipo"
                });
                
                // Escribir datos
                for (DatosRegistro datos : datosRegistro) {
                    writer.writeNext(new String[]{
                        datos.getEmail(),
                        datos.getPassword(),
                        datos.getConfirmarPassword(),
                        datos.getNombre() != null ? datos.getNombre() : "",
                        datos.getApellido() != null ? datos.getApellido() : "",
                        datos.getTelefono() != null ? datos.getTelefono() : "",
                        datos.getGenero() != null ? datos.getGenero() : "",
                        datos.getPais() != null ? datos.getPais() : "",
                        datos.getCiudad() != null ? datos.getCiudad() : "",
                        String.valueOf(datos.isAceptaTerminos()),
                        String.valueOf(datos.isRecibirNotificaciones()),
                        datos.getTipoRegistro() != null ? datos.getTipoRegistro().name() : ""
                    });
                }
            }
            
            logger.info("Guardados {} conjuntos de datos de registro en: {}", datosRegistro.size(), rutaArchivo);
            return true;
            
        } catch (Exception e) {
            logger.error("Error guardando datos de registro: {}", e.getMessage());
            return false;
        }
    }
    
    // ===== MÉTODOS DE UTILIDAD PRIVADOS =====
    
    /**
     * Obtiene la ruta completa de un archivo de datos.
     */
    private Path obtenerRutaArchivo(String nombreArchivo) {
        return config.obtenerRutaDatos().resolve(nombreArchivo);
    }
    
    /**
     * Limpia un campo CSV eliminando espacios y comillas.
     */
    private String limpiarCampo(String campo) {
        if (campo == null) return "";
        return campo.trim().replaceAll("^\"|\"$", "");
    }
    
    /**
     * Genera un username aleatorio.
     */
    private String generarUsernameAleatorio() {
        String prefijo = PREFIJOS_USERNAME[random.nextInt(PREFIJOS_USERNAME.length)];
        int numero = random.nextInt(9999) + 1;
        return prefijo + numero;
    }
    
    /**
     * Genera un email aleatorio.
     */
    private String generarEmailAleatorio() {
        String username = generarUsernameAleatorio();
        String dominio = DOMINIOS_EMAIL[random.nextInt(DOMINIOS_EMAIL.length)];
        return username + dominio;
    }
    
    /**
     * Genera un password aleatorio.
     */
    private String generarPasswordAleatorio() {
        String[] palabras = {"Test", "Pass", "Auto", "QA", "Selenium"};
        String palabra = palabras[random.nextInt(palabras.length)];
        int numero = random.nextInt(999) + 100;
        return palabra + numero;
    }
    
    /**
     * Genera un teléfono aleatorio.
     */
    private String generarTelefonoAleatorio() {
        return "+34" + (600000000 + random.nextInt(99999999));
    }
    
    // ===== MÉTODOS DE DATOS POR DEFECTO =====
    
    /**
     * Genera usuarios de login por defecto para ExpandTesting.
     */
    private List<Usuario> generarUsuariosLoginPorDefecto() {
        List<Usuario> usuarios = new ArrayList<>();
        
        // Usuario principal de ExpandTesting
        Usuario usuarioPrincipal = new Usuario("practice", "SuperSecretPassword!");
        usuarioPrincipal.setNombre("Practice");
        usuarioPrincipal.setApellido("User");
        usuarios.add(usuarioPrincipal);
        
        // Usuarios adicionales para testing
        for (int i = 1; i <= 5; i++) {
            Usuario usuario = new Usuario("practice", "SuperSecretPassword!");
            usuario.setNombre("Test" + i);
            usuario.setApellido("User");
            usuarios.add(usuario);
        }
        
        logger.info("Generados {} usuarios de login por defecto para ExpandTesting", usuarios.size());
        return usuarios;
    }
    
    /**
     * Genera credenciales inválidas por defecto para ExpandTesting.
     */
    private List<Usuario> generarCredencialesInvalidasPorDefecto() {
        List<Usuario> credencialesInvalidas = new ArrayList<>();
        
        // Username inválido
        credencialesInvalidas.add(new Usuario("wrongUser", "SuperSecretPassword!"));
        credencialesInvalidas.add(new Usuario("invalidUser", "SuperSecretPassword!"));
        
        // Password inválido
        credencialesInvalidas.add(new Usuario("practice", "WrongPassword"));
        credencialesInvalidas.add(new Usuario("practice", "InvalidPass123"));
        
        // Campos vacíos
        credencialesInvalidas.add(new Usuario("", "SuperSecretPassword!"));
        credencialesInvalidas.add(new Usuario("practice", ""));
        credencialesInvalidas.add(new Usuario("", ""));
        
        logger.info("Generadas {} credenciales inválidas por defecto para ExpandTesting", credencialesInvalidas.size());
        return credencialesInvalidas;
    }
    
    /**
     * Genera datos de registro por defecto para ExpandTesting.
     */
    private List<DatosRegistro> generarDatosRegistroPorDefecto() {
        List<DatosRegistro> datosRegistro = new ArrayList<>();
        
        // Registros válidos
        for (int i = 1; i <= 5; i++) {
            DatosRegistro datos = new DatosRegistro.Builder()
                    .email("testuser" + String.format("%03d", i) + "@expandtesting.com")
                    .password("TestPassword" + (100 + i))
                    .confirmarPassword("TestPassword" + (100 + i))
                    .nombre("Test" + i)
                    .apellido("User")
                    .telefono("+34600" + String.format("%06d", i))
                    .aceptarTerminos()
                    .build();
            
            datosRegistro.add(datos);
        }
        
        // Registros inválidos para testing negativo
        datosRegistro.add(new DatosRegistro.Builder()
                .email("") // Email vacío
                .password("ValidPassword123")
                .confirmarPassword("ValidPassword123")
                .build());
        
        datosRegistro.add(new DatosRegistro.Builder()
                .email("validuser@test.com")
                .password("") // Password vacío
                .confirmarPassword("")
                .build());
        
        datosRegistro.add(new DatosRegistro.Builder()
                .email("validuser@test.com")
                .password("Password123")
                .confirmarPassword("DifferentPassword456") // Passwords no coinciden
                .build());
        
        logger.info("Generados {} conjuntos de datos de registro por defecto para ExpandTesting", datosRegistro.size());
        return datosRegistro;
    }
    
    // ===== MÉTODOS DE GESTIÓN DE CACHE =====
    
    /**
     * Limpia el cache de datos.
     */
    public void limpiarCache() {
        cache.clear();
        logger.info("Cache de datos limpiado");
    }
    
    /**
     * Obtiene estadísticas del cache.
     */
    public String obtenerEstadisticasCache() {
        StringBuilder stats = new StringBuilder();
        stats.append("=== ESTADÍSTICAS DE CACHE ===\n");
        stats.append("Entradas en cache: ").append(cache.size()).append("\n");
        
        for (Map.Entry<String, List<?>> entry : cache.entrySet()) {
            stats.append("- ").append(entry.getKey()).append(": ")
                 .append(entry.getValue().size()).append(" elementos\n");
        }
        
        stats.append("============================");
        return stats.toString();
    }
    
    /**
     * Verifica la integridad de todos los archivos de datos.
     */
    public boolean verificarIntegridadArchivos() {
        boolean todosValidos = true;
        
        String[] archivos = {
            config.obtenerArchivoUsuariosLogin(),
            config.obtenerArchivoUsuariosRegistro(),
            config.obtenerArchivoCredencialesInvalidas()
        };
        
        for (String archivo : archivos) {
            Path rutaArchivo = obtenerRutaArchivo(archivo);
            if (!Files.exists(rutaArchivo)) {
                logger.warn("Archivo no encontrado: {}", rutaArchivo);
                todosValidos = false;
            } else {
                logger.debug("Archivo válido: {}", rutaArchivo);
            }
        }
        
        return todosValidos;
    }
}