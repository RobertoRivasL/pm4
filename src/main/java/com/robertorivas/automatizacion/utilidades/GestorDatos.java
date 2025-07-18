package com.robertorivas.automatizacion.utilidades;

import com.opencsv.CSVReader;
import com.robertorivas.automatizacion.configuracion.ConfiguracionPruebas;
import com.robertorivas.automatizacion.modelos.DatosRegistro;
import com.robertorivas.automatizacion.modelos.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
