package com.robertorivas.automatizacion.utilidades;

<<<<<<< HEAD
=======
import com.opencsv.CSVReader;
>>>>>>> 6997292b2d22485ff45fed1f08040976dfcfd0b3
import com.robertorivas.automatizacion.configuracion.ConfiguracionPruebas;
import com.robertorivas.automatizacion.modelos.DatosRegistro;
import com.robertorivas.automatizacion.modelos.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

<<<<<<< HEAD
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Gestor centralizado para el manejo de datos de pruebas.
 * Incluye lectura de archivos CSV, generación de datos aleatorios y cache.
 * 
 * Principios aplicados:
 * - Single Responsibility: Solo maneja operaciones de datos
 * - Caching: Cache para mejorar performance
 * - Factory Pattern: Genera diferentes tipos de datos
 * - Error Handling: Manejo robusto de errores de I/O
 * 
 * @author Roberto Rivas Lopez
 */
public class GestorDatos {
    
    private static final Logger logger = LoggerFactory.getLogger(GestorDatos.class);
    
    private final ConfiguracionPruebas config;
    private final Map<String, List<?>> cache;
    private final Random random;
    
    // Datos base para generación aleatoria
    private static final String[] NOMBRES = {
        "Juan", "María", "Carlos", "Ana", "Luis", "Isabel", "Miguel", "Laura",
        "David", "Carmen", "Antonio", "Pilar", "Francisco", "Rosa", "Manuel",
        "Teresa", "Ángel", "Dolores", "José", "Mónica", "Pedro", "Cristina"
    };
    
    private static final String[] APELLIDOS = {
        "García", "Rodríguez", "González", "Fernández", "López", "Martínez",
        "Sánchez", "Pérez", "Gómez", "Martín", "Jiménez", "Ruiz", "Hernández",
        "Díaz", "Moreno", "Muñoz", "Álvarez", "Romero", "Alonso", "Gutiérrez"
    };
    
    private static final String[] GENEROS = {"Masculino", "Femenino", "Otro"};
    
    private static final String[] PAISES = {
        "España", "México", "Argentina", "Colombia", "Chile", "Perú",
        "Venezuela", "Ecuador", "Bolivia", "Uruguay", "Paraguay"
    };
    
    private static final String[] CIUDADES_ESPANA = {
        "Madrid", "Barcelona", "Valencia", "Sevilla", "Zaragoza", "Málaga",
        "Murcia", "Palma", "Las Palmas", "Bilbao", "Alicante", "Córdoba"
    };
    
    /**
     * Constructor del gestor de datos.
     */
    public GestorDatos() {
        this.config = ConfiguracionPruebas.obtenerInstancia();
        this.cache = new ConcurrentHashMap<>();
        this.random = new Random(System.currentTimeMillis());
        
        logger.info("GestorDatos inicializado");
    }
    
    // ===== MÉTODOS DE LECTURA DE CSV =====
    
    /**
     * Lee datos de registro desde archivo CSV.
     */
    @SuppressWarnings("unchecked")
    public List<DatosRegistro> leerDatosRegistro() {
        String cacheKey = "datos_registro";
        
        if (cache.containsKey(cacheKey)) {
            logger.debug("Retornando datos de registro desde cache");
            return (List<DatosRegistro>) cache.get(cacheKey);
        }
        
        List<DatosRegistro> datosRegistro = new ArrayList<>();
        String nombreArchivo = config.obtenerArchivoUsuariosRegistro();
        
        try {
            logger.info("Leyendo datos de registro desde: {}", nombreArchivo);
            
            List<String[]> lineas = leerArchivoCSV(nombreArchivo);
            
            if (lineas.isEmpty()) {
                logger.warn("No se encontraron datos en {}", nombreArchivo);
                return datosRegistro;
            }
            
            // Obtener headers (primera línea)
            String[] headers = lineas.get(0);
            logger.debug("Headers CSV: {}", Arrays.toString(headers));
            
            // Procesar datos (resto de líneas)
            for (int i = 1; i < lineas.size(); i++) {
                String[] valores = lineas.get(i);
                
                try {
                    DatosRegistro datos = crearDatosRegistroDesdeCSV(headers, valores);
                    if (datos != null) {
                        datosRegistro.add(datos);
                    }
                } catch (Exception e) {
                    logger.warn("Error procesando línea {}: {}", i + 1, e.getMessage());
                }
            }
            
            cache.put(cacheKey, datosRegistro);
            logger.info("Se cargaron {} registros de datos de registro", datosRegistro.size());
            
        } catch (Exception e) {
            logger.error("Error leyendo datos de registro: {}", e.getMessage());
        }
        
        return datosRegistro;
    }
    
    /**
     * Lee datos de login desde archivo CSV.
     */
    @SuppressWarnings("unchecked")
    public List<Usuario> leerDatosLogin() {
        String cacheKey = "datos_login";
        
        if (cache.containsKey(cacheKey)) {
            logger.debug("Retornando datos de login desde cache");
            return (List<Usuario>) cache.get(cacheKey);
        }
        
        List<Usuario> usuarios = new ArrayList<>();
        String nombreArchivo = config.obtenerArchivoUsuariosLogin();
        
        try {
            logger.info("Leyendo datos de login desde: {}", nombreArchivo);
            
            List<String[]> lineas = leerArchivoCSV(nombreArchivo);
            
            if (lineas.isEmpty()) {
                logger.warn("No se encontraron datos en {}", nombreArchivo);
                return usuarios;
            }
            
            // Obtener headers
            String[] headers = lineas.get(0);
            
            // Procesar datos
            for (int i = 1; i < lineas.size(); i++) {
                String[] valores = lineas.get(i);
                
                try {
                    Usuario usuario = crearUsuarioDesdeCSV(headers, valores);
                    if (usuario != null) {
                        usuarios.add(usuario);
                    }
                } catch (Exception e) {
                    logger.warn("Error procesando línea {}: {}", i + 1, e.getMessage());
                }
            }
            
            cache.put(cacheKey, usuarios);
            logger.info("Se cargaron {} usuarios válidos", usuarios.size());
            
        } catch (Exception e) {
            logger.error("Error leyendo datos de login: {}", e.getMessage());
        }
        
        return usuarios;
    }
    
    /**
     * Lee credenciales inválidas desde archivo CSV.
     */
    @SuppressWarnings("unchecked")
    public List<Usuario> leerCredencialesInvalidas() {
        String cacheKey = "credenciales_invalidas";
        
        if (cache.containsKey(cacheKey)) {
            logger.debug("Retornando credenciales inválidas desde cache");
            return (List<Usuario>) cache.get(cacheKey);
        }
        
        List<Usuario> credencialesInvalidas = new ArrayList<>();
        String nombreArchivo = config.obtenerArchivoCredencialesInvalidas();
        
        try {
            logger.info("Leyendo credenciales inválidas desde: {}", nombreArchivo);
            
            List<String[]> lineas = leerArchivoCSV(nombreArchivo);
            
            if (lineas.isEmpty()) {
                logger.warn("No se encontraron datos en {}", nombreArchivo);
                return credencialesInvalidas;
            }
            
            // Obtener headers
            String[] headers = lineas.get(0);
            
            // Procesar datos (permitir usuarios inválidos)
            for (int i = 1; i < lineas.size(); i++) {
                String[] valores = lineas.get(i);
                
                try {
                    // Para credenciales inválidas, ser más permisivo
                    Usuario usuario = crearUsuarioInvalidoDesdeCSV(headers, valores);
                    if (usuario != null) {
                        credencialesInvalidas.add(usuario);
                    }
                } catch (Exception e) {
                    logger.debug("Error procesando credencial inválida línea {}: {}", i + 1, e.getMessage());
                }
            }
            
            cache.put(cacheKey, credencialesInvalidas);
            logger.info("Se cargaron {} credenciales inválidas", credencialesInvalidas.size());
            
        } catch (Exception e) {
            logger.error("Error leyendo credenciales inválidas: {}", e.getMessage());
        }
        
        return credencialesInvalidas;
    }
    
    // ===== MÉTODOS DE GENERACIÓN ALEATORIA =====
    
    /**
     * Genera datos de registro aleatorios.
     */
    public List<DatosRegistro> generarDatosRegistroAleatorios(int cantidad) {
        logger.info("Generando {} datos de registro aleatorios", cantidad);
        
        List<DatosRegistro> datosAleatorios = new ArrayList<>();
        
        for (int i = 0; i < cantidad; i++) {
            DatosRegistro datos = new DatosRegistro.Builder()
                    .email(generarEmailAleatorio())
                    .password(generarPasswordAleatorio())
                    .confirmarPassword("") // Se establecerá igual al password
                    .nombre(NOMBRES[random.nextInt(NOMBRES.length)])
                    .apellido(APELLIDOS[random.nextInt(APELLIDOS.length)])
                    .telefono(generarTelefonoAleatorio())
                    .genero(GENEROS[random.nextInt(GENEROS.length)])
                    .pais(PAISES[random.nextInt(PAISES.length)])
                    .ciudad(CIUDADES_ESPANA[random.nextInt(CIUDADES_ESPANA.length)])
                    .aceptarTerminos()
                    .recibirNotificaciones(random.nextBoolean())
                    .tipoRegistro(DatosRegistro.TipoRegistro.values()[random.nextInt(DatosRegistro.TipoRegistro.values().length)])
                    .build();
            
            // Establecer confirmación de password igual al password
            datos.setConfirmarPassword(datos.getPassword());
            
            datosAleatorios.add(datos);
        }
        
        logger.info("Datos de registro aleatorios generados: {}", cantidad);
        return datosAleatorios;
    }
    
    /**
     * Genera usuarios aleatorios.
     */
    public List<Usuario> generarUsuariosAleatorios(int cantidad) {
        logger.info("Generando {} usuarios aleatorios", cantidad);
        
        List<Usuario> usuariosAleatorios = new ArrayList<>();
        
        for (int i = 0; i < cantidad; i++) {
            String email = generarEmailAleatorio();
            String password = generarPasswordAleatorio();
            String nombre = NOMBRES[random.nextInt(NOMBRES.length)];
            String apellido = APELLIDOS[random.nextInt(APELLIDOS.length)];
            
            Usuario usuario = new Usuario(email, password);
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setTelefono(generarTelefonoAleatorio());
            
            usuariosAleatorios.add(usuario);
        }
        
        logger.info("Usuarios aleatorios generados: {}", cantidad);
        return usuariosAleatorios;
    }
    
    // ===== MÉTODOS PRIVADOS DE UTILIDAD =====
    
    /**
     * Lee un archivo CSV y retorna las líneas como arrays de strings.
     */
    private List<String[]> leerArchivoCSV(String nombreArchivo) throws IOException {
        List<String[]> lineas = new ArrayList<>();
        
        // Intentar leer desde classpath primero
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("datos/" + nombreArchivo)) {
            if (inputStream != null) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                    String linea;
                    while ((linea = reader.readLine()) != null) {
                        if (!linea.trim().isEmpty()) {
                            String[] valores = parsearLineaCSV(linea);
                            lineas.add(valores);
                        }
                    }
                }
                logger.debug("Archivo CSV leído desde classpath: {}", nombreArchivo);
            } else {
                // Intentar leer desde sistema de archivos
                Path rutaArchivo = config.obtenerRutaDatos().resolve(nombreArchivo);
                if (Files.exists(rutaArchivo)) {
                    try (BufferedReader reader = Files.newBufferedReader(rutaArchivo, StandardCharsets.UTF_8)) {
                        String linea;
                        while ((linea = reader.readLine()) != null) {
                            if (!linea.trim().isEmpty()) {
                                String[] valores = parsearLineaCSV(linea);
                                lineas.add(valores);
                            }
                        }
                    }
                    logger.debug("Archivo CSV leído desde sistema de archivos: {}", rutaArchivo);
                } else {
                    logger.warn("Archivo CSV no encontrado: {}", nombreArchivo);
                }
            }
        }
        
        return lineas;
    }
    
    /**
     * Parsea una línea CSV considerando comas dentro de comillas.
     */
    private String[] parsearLineaCSV(String linea) {
        List<String> valores = new ArrayList<>();
        boolean dentroComillas = false;
        StringBuilder valor = new StringBuilder();
        
        for (int i = 0; i < linea.length(); i++) {
            char c = linea.charAt(i);
            
            if (c == '"') {
                dentroComillas = !dentroComillas;
            } else if (c == ',' && !dentroComillas) {
                valores.add(valor.toString().trim());
                valor = new StringBuilder();
            } else {
                valor.append(c);
            }
        }
        
        valores.add(valor.toString().trim());
        return valores.toArray(new String[0]);
    }
    
    /**
     * Crea un DatosRegistro desde una línea CSV.
     */
    private DatosRegistro crearDatosRegistroDesdeCSV(String[] headers, String[] valores) {
        Map<String, String> mapa = crearMapaCSV(headers, valores);
        
        DatosRegistro.Builder builder = new DatosRegistro.Builder();
        
        // Campos obligatorios
        String email = mapa.get("email");
        String password = mapa.get("password");
        String confirmarPassword = mapa.get("confirmarPassword");
        String nombre = mapa.get("nombre");
        String apellido = mapa.get("apellido");
        
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            logger.debug("Saltando registro con email o password vacío");
            return null;
        }
        
        builder.email(email)
               .password(password)
               .confirmarPassword(confirmarPassword != null ? confirmarPassword : password)
               .nombre(nombre != null ? nombre : "")
               .apellido(apellido != null ? apellido : "");
        
        // Campos opcionales
        if (mapa.containsKey("telefono")) {
            builder.telefono(mapa.get("telefono"));
        }
        if (mapa.containsKey("genero")) {
            builder.genero(mapa.get("genero"));
        }
        if (mapa.containsKey("pais")) {
            builder.pais(mapa.get("pais"));
        }
        if (mapa.containsKey("ciudad")) {
            builder.ciudad(mapa.get("ciudad"));
        }
        
        // Opciones booleanas
        if ("true".equalsIgnoreCase(mapa.get("aceptaTerminos"))) {
            builder.aceptarTerminos();
        }
        if (mapa.containsKey("notificaciones")) {
            builder.recibirNotificaciones(Boolean.parseBoolean(mapa.get("notificaciones")));
        }
        
        // Tipo de registro
        if (mapa.containsKey("tipo")) {
            try {
                DatosRegistro.TipoRegistro tipo = DatosRegistro.TipoRegistro.valueOf(mapa.get("tipo"));
                builder.tipoRegistro(tipo);
            } catch (Exception e) {
                logger.debug("Tipo de registro inválido: {}", mapa.get("tipo"));
            }
        }
        
        return builder.build();
    }
    
    /**
     * Crea un Usuario desde una línea CSV.
     */
    private Usuario crearUsuarioDesdeCSV(String[] headers, String[] valores) {
        Map<String, String> mapa = crearMapaCSV(headers, valores);
        
        String email = mapa.get("email");
        String password = mapa.get("password");
        
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            logger.debug("Saltando usuario con email o password vacío");
            return null;
        }
        
        Usuario usuario = new Usuario(email, password);
        
        // Campos opcionales
        if (mapa.containsKey("nombre")) {
            usuario.setNombre(mapa.get("nombre"));
        }
        if (mapa.containsKey("apellido")) {
            usuario.setApellido(mapa.get("apellido"));
        }
        if (mapa.containsKey("telefono")) {
            usuario.setTelefono(mapa.get("telefono"));
        }
        
        return usuario;
    }
    
    /**
     * Crea un Usuario inválido desde CSV (más permisivo para pruebas negativas).
     */
    private Usuario crearUsuarioInvalidoDesdeCSV(String[] headers, String[] valores) {
        Map<String, String> mapa = crearMapaCSV(headers, valores);
        
        String email = mapa.get("email");
        String password = mapa.get("password");
        
        // Para usuarios inválidos, permitir valores vacíos o nulos
        email = email != null ? email : "";
        password = password != null ? password : "";
        
        try {
            // Crear usuario incluso con datos inválidos
            if (email.isEmpty() && password.isEmpty()) {
                return new Usuario("email_vacio@test.com", "password_vacio");
            } else if (email.isEmpty()) {
                return new Usuario("email_vacio@test.com", password);
            } else if (password.isEmpty()) {
                return new Usuario(email, "password_vacio");
            } else {
                return new Usuario(email, password);
            }
        } catch (Exception e) {
            // Si falla la validación, crear un usuario con datos básicos
            return new Usuario("usuario_invalido@test.com", "password_invalido");
        }
    }
    
    /**
     * Crea un mapa de headers -> valores.
     */
    private Map<String, String> crearMapaCSV(String[] headers, String[] valores) {
        Map<String, String> mapa = new HashMap<>();
        
        for (int i = 0; i < headers.length && i < valores.length; i++) {
            String header = headers[i].trim();
            String valor = valores[i].trim();
            
            // Limpiar comillas si las hay
            if (valor.startsWith("\"") && valor.endsWith("\"") && valor.length() > 1) {
                valor = valor.substring(1, valor.length() - 1);
            }
            
            mapa.put(header, valor);
        }
        
        return mapa;
    }
    
    /**
     * Genera un email aleatorio.
     */
    private String generarEmailAleatorio() {
        String[] dominios = {"testautomation.com", "test.com", "example.com", "demo.com"};
        String prefijo = "test" + random.nextInt(10000);
        String dominio = dominios[random.nextInt(dominios.length)];
        return prefijo + "@" + dominio;
    }
    
    /**
     * Genera una contraseña aleatoria.
     */
    private String generarPasswordAleatorio() {
        String[] passwords = {
            "Password123", "SecurePass456", "TestPass789", "DemoPass321",
            "AutoPass555", "ValidPass888", "StrongPass999", "SafePass111"
        };
        return passwords[random.nextInt(passwords.length)];
    }
    
    /**
     * Genera un teléfono aleatorio español.
     */
    private String generarTelefonoAleatorio() {
        return "+34" + (600 + random.nextInt(100)) + String.format("%06d", random.nextInt(1000000));
    }
    
    // ===== MÉTODOS PÚBLICOS DE UTILIDAD =====
    
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
    public Map<String, Integer> obtenerEstadisticasCache() {
        Map<String, Integer> estadisticas = new HashMap<>();
        
        cache.forEach((clave, lista) -> {
            estadisticas.put(clave, lista.size());
        });
        
        return estadisticas;
    }
    
    /**
     * Valida que los archivos de datos existan.
     */
    public boolean validarArchivos() {
        String[] archivos = {
            config.obtenerArchivoUsuariosRegistro(),
            config.obtenerArchivoUsuariosLogin(),
            config.obtenerArchivoCredencialesInvalidas()
        };
        
        boolean todosExisten = true;
        
        for (String archivo : archivos) {
            boolean existe = getClass().getClassLoader().getResource("datos/" + archivo) != null ||
                           Files.exists(config.obtenerRutaDatos().resolve(archivo));
            
            if (!existe) {
                logger.warn("Archivo no encontrado: {}", archivo);
                todosExisten = false;
            } else {
                logger.debug("Archivo encontrado: {}", archivo);
            }
        }
        
        return todosExisten;
    }
}
=======
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Utilidad para la carga de datos desde archivos CSV.
 * Mantiene los datos en memoria para evitar lecturas repetidas.
 */
public class GestorDatos {

    private static final Logger logger = LoggerFactory.getLogger(GestorDatos.class);
    private final ConfiguracionPruebas config;

    private List<DatosRegistro> cacheRegistro;
    private List<Usuario> cacheUsuarios;
    private List<Usuario> cacheCredencialesInvalidas;

    public GestorDatos() {
        this.config = ConfiguracionPruebas.obtenerInstancia();
    }

    /** Carga los datos de registro desde CSV. */
    public List<DatosRegistro> leerDatosRegistro() {
        if (cacheRegistro == null) {
            cacheRegistro = leerDatosRegistroDesdeCsv();
        }
        return cacheRegistro;
    }

    private List<DatosRegistro> leerDatosRegistroDesdeCsv() {
        List<DatosRegistro> lista = new ArrayList<>();
        Path ruta = config.obtenerRutaDatos().resolve(config.obtenerArchivoUsuariosRegistro());
        if (!Files.exists(ruta)) {
            logger.warn("Archivo de datos de registro no encontrado: {}", ruta);
            return lista;
        }
        try (CSVReader reader = new CSVReader(new InputStreamReader(Files.newInputStream(ruta)))) {
            String[] fila;
            boolean encabezados = true;
            while ((fila = reader.readNext()) != null) {
                if (encabezados) {
                    encabezados = false;
                    continue;
                }
                DatosRegistro datos = new DatosRegistro.Builder()
                        .email(fila[0])
                        .password(fila[1])
                        .confirmarPassword(fila[2])
                        .nombre(fila[3])
                        .apellido(fila[4])
                        .telefono(fila[5])
                        .genero(fila[6])
                        .pais(fila[7])
                        .ciudad(fila[8])
                        .aceptarTerminos()
                        .build();
                lista.add(datos);
            }
        } catch (IOException e) {
            logger.error("Error leyendo datos de registro", e);
        }
        return lista;
    }

    /** Carga los usuarios válidos para login. */
    public List<Usuario> leerUsuariosLogin() {
        if (cacheUsuarios == null) {
            cacheUsuarios = leerUsuariosDesdeCsv(config.obtenerArchivoUsuariosLogin());
        }
        return cacheUsuarios;
    }

    /** Carga las credenciales inválidas para pruebas negativas. */
    public List<Usuario> leerCredencialesInvalidas() {
        if (cacheCredencialesInvalidas == null) {
            cacheCredencialesInvalidas = leerUsuariosDesdeCsv(config.obtenerArchivoCredencialesInvalidas());
        }
        return cacheCredencialesInvalidas;
    }

    private List<Usuario> leerUsuariosDesdeCsv(String nombreArchivo) {
        List<Usuario> lista = new ArrayList<>();
        Path ruta = config.obtenerRutaDatos().resolve(nombreArchivo);
        if (!Files.exists(ruta)) {
            logger.warn("Archivo de usuarios no encontrado: {}", ruta);
            return lista;
        }
        try (CSVReader reader = new CSVReader(new InputStreamReader(Files.newInputStream(ruta)))) {
            String[] fila;
            boolean encabezados = true;
            while ((fila = reader.readNext()) != null) {
                if (encabezados) {
                    encabezados = false;
                    continue;
                }
                Usuario usuario = new Usuario(fila[0], fila[1]);
                if (fila.length > 2) {
                    usuario.setNombre(fila[2]);
                }
                if (fila.length > 3) {
                    usuario.setApellido(fila[3]);
                }
                if (fila.length > 4) {
                    usuario.setTelefono(fila[4]);
                }
                lista.add(usuario);
            }
        } catch (IOException e) {
            logger.error("Error leyendo archivo {}", nombreArchivo, e);
        }
        return lista;
    }

    /** Limpia el contenido cacheado. */
    public void limpiarCache() {
        cacheRegistro = null;
        cacheUsuarios = null;
        cacheCredencialesInvalidas = null;
    }
}
>>>>>>> 6997292b2d22485ff45fed1f08040976dfcfd0b3
