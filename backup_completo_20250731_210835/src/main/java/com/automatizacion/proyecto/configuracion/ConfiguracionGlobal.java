package com.automatizacion.proyecto.configuracion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Clase singleton responsable de la configuración global de la aplicación.
 * Gestiona la carga y acceso a propiedades de configuración desde archivos.
 * 
 * Implementa el patrón Singleton y sigue el principio de Responsabilidad Única.
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
public class ConfiguracionGlobal {

    private static final Logger logger = LoggerFactory.getLogger(ConfiguracionGlobal.class);
    private static final String ARCHIVO_CONFIG = "config.properties";

    private static ConfiguracionGlobal instancia;
    private final Properties propiedades;

    private ConfiguracionGlobal() {
        propiedades = new Properties();
        cargarPropiedades();
    }

    /**
     * Obtiene la instancia única de la configuración global
     * 
     * @return instancia singleton de ConfiguracionGlobal
     */
    public static synchronized ConfiguracionGlobal obtenerInstancia() {
        if (instancia == null) {
            instancia = new ConfiguracionGlobal();
        }
        return instancia;
    }

    /**
     * Carga las propiedades desde el archivo de configuración
     */
    private void cargarPropiedades() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(ARCHIVO_CONFIG)) {
            if (inputStream == null) {
                logger.warn("No se pudo encontrar el archivo de configuración: {}", ARCHIVO_CONFIG);
                cargarConfiguracionPorDefecto();
                return;
            }

            propiedades.load(inputStream);
            logger.info("Configuración cargada exitosamente desde: {}", ARCHIVO_CONFIG);

        } catch (IOException e) {
            logger.error("Error al cargar el archivo de configuración: {}", e.getMessage());
            cargarConfiguracionPorDefecto();
        }
    }

    ConfiguracionGlobal config = ConfiguracionGlobal.obtenerInstancia();
    String tipoNavegador = config.obtenerTipoNavegador();
    boolean headless = config.esNavegadorHeadless();

    /**
     * Carga configuración por defecto en caso de error
     */
    private void cargarConfiguracionPorDefecto() {
        propiedades.setProperty("url.base", "https://practice.expandtesting.com");
        propiedades.setProperty("navegador.tipo", "CHROME");
        propiedades.setProperty("navegador.headless", "false");
        propiedades.setProperty("timeout.implicito", "10");
        propiedades.setProperty("timeout.explicito", "15");
        propiedades.setProperty("timeout.carga.pagina", "30");
        propiedades.setProperty("capturas.directorio", "capturas");
        propiedades.setProperty("reportes.directorio", "reportes");
        propiedades.setProperty("datos.directorio", "src/test/resources/datos");

        logger.info("Configuración por defecto cargada");
    }

    /**
     * Obtiene una propiedad como String
     * 
     * @param clave clave de la propiedad
     * @return valor de la propiedad o cadena vacía si no existe
     */
    public String obtenerPropiedad(String clave) {
        return propiedades.getProperty(clave, "");
    }

    /**
     * Obtiene una propiedad como String con valor por defecto
     * 
     * @param clave           clave de la propiedad
     * @param valorPorDefecto valor por defecto si la clave no existe
     * @return valor de la propiedad o valor por defecto
     */
    public String obtenerPropiedad(String clave, String valorPorDefecto) {
        return propiedades.getProperty(clave, valorPorDefecto);
    }

    /**
     * Obtiene una propiedad como entero
     * 
     * @param clave           clave de la propiedad
     * @param valorPorDefecto valor por defecto si la clave no existe o no es válida
     * @return valor entero de la propiedad o valor por defecto
     */
    public int obtenerPropiedadEntero(String clave, int valorPorDefecto) {
        try {
            return Integer.parseInt(obtenerPropiedad(clave));
        } catch (NumberFormatException e) {
            logger.warn("No se pudo convertir la propiedad '{}' a entero. Usando valor por defecto: {}",
                    clave, valorPorDefecto);
            return valorPorDefecto;
        }
    }

    /**
     * Obtiene una propiedad como booleano
     * 
     * @param clave           clave de la propiedad
     * @param valorPorDefecto valor por defecto si la clave no existe
     * @return valor booleano de la propiedad o valor por defecto
     */
    public boolean obtenerPropiedadBooleana(String clave, boolean valorPorDefecto) {
        String valor = obtenerPropiedad(clave);
        if (valor.isEmpty()) {
            return valorPorDefecto;
        }
        return Boolean.parseBoolean(valor);
    }

    // Métodos de conveniencia para propiedades comunes

    public String obtenerUrlBase() {
        return obtenerPropiedad("url.base");
    }

    public String obtenerTipoNavegador() {
        return obtenerPropiedad("navegador.tipo", "CHROME");
    }

    public boolean esNavegadorHeadless() {
        return obtenerPropiedadBooleana("navegador.headless", false);
    }

    public int obtenerTimeoutImplicito() {
        return obtenerPropiedadEntero("timeout.implicito", 10);
    }

    public int obtenerTimeoutExplicito() {
        return obtenerPropiedadEntero("timeout.explicito", 15);
    }

    public int obtenerTimeoutCargaPagina() {
        return obtenerPropiedadEntero("timeout.carga.pagina", 30);
    }

    public String obtenerDirectorioCapturas() {
        return obtenerPropiedad("capturas.directorio", "capturas");
    }

    public String obtenerDirectorioReportes() {
        return obtenerPropiedad("reportes.directorio", "reportes");
    }

    public String obtenerDirectorioDatos() {
        return obtenerPropiedad("datos.directorio", "src/test/resources/datos");
    }

    /**
     * Establece una propiedad en tiempo de ejecución
     * 
     * @param clave clave de la propiedad
     * @param valor valor de la propiedad
     */
    public void establecerPropiedad(String clave, String valor) {
        propiedades.setProperty(clave, valor);
        logger.debug("Propiedad establecida: {} = {}", clave, valor);
    }

    /**
     * Verifica si una propiedad existe
     * 
     * @param clave clave de la propiedad
     * @return true si la propiedad existe, false en caso contrario
     */
    public boolean existePropiedad(String clave) {
        return propiedades.containsKey(clave);
    }
}
