package com.automatizacion.proyecto.configuracion;

import com.automatizacion.proyecto.enums.TipoMensaje;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfiguracionGlobal {
    
    private static final Logger logger = LoggerFactory.getLogger(ConfiguracionGlobal.class);
    private static volatile ConfiguracionGlobal instancia;
    private final Properties propiedades;
    
    public static final String URL_BASE = "url.base";
    public static final String URL_LOGIN = "url.login";
    public static final String URL_REGISTRO = "url.registro";
    public static final String NAVEGADOR_TIPO = "navegador.tipo";
    public static final String NAVEGADOR_HEADLESS = "navegador.headless";
    public static final String TIMEOUT_EXPLICITO = "timeout.explicito";
    public static final String TIMEOUT_IMPLICITO = "timeout.implicito";
    
    private ConfiguracionGlobal() {
        this.propiedades = new Properties();
        cargarConfiguraciones();
    }
    
    public static ConfiguracionGlobal obtenerInstancia() {
        if (instancia == null) {
            synchronized (ConfiguracionGlobal.class) {
                if (instancia == null) {
                    instancia = new ConfiguracionGlobal();
                }
            }
        }
        return instancia;
    }
    
    private void cargarConfiguraciones() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                propiedades.load(input);
                logger.info(TipoMensaje.CONFIGURACION.formatearMensaje("Configuraciones cargadas"));
            } else {
                cargarConfiguracionesPorDefecto();
            }
        } catch (IOException e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("Error cargando config.properties, usando valores por defecto"));
            cargarConfiguracionesPorDefecto();
        }
    }
    
    private void cargarConfiguracionesPorDefecto() {
        propiedades.setProperty(URL_BASE, "https://practice.expandtesting.com");
        propiedades.setProperty(URL_LOGIN, "/login");
        propiedades.setProperty(URL_REGISTRO, "/register");
        propiedades.setProperty(NAVEGADOR_TIPO, "CHROME");
        propiedades.setProperty(NAVEGADOR_HEADLESS, "false");
        propiedades.setProperty(TIMEOUT_EXPLICITO, "10");
        propiedades.setProperty(TIMEOUT_IMPLICITO, "10");
    }
    
    public String obtenerPropiedad(String clave) {
        return propiedades.getProperty(clave);
    }
    
    public String obtenerPropiedad(String clave, String valorPorDefecto) {
        return propiedades.getProperty(clave, valorPorDefecto);
    }
    
    public int obtenerPropiedadInt(String clave, int valorPorDefecto) {
        try {
            String valor = obtenerPropiedad(clave);
            return valor != null ? Integer.parseInt(valor.trim()) : valorPorDefecto;
        } catch (NumberFormatException e) {
            return valorPorDefecto;
        }
    }
    
    public boolean obtenerPropiedadBoolean(String clave, boolean valorPorDefecto) {
        String valor = obtenerPropiedad(clave);
        return valor != null ? Boolean.parseBoolean(valor.trim()) : valorPorDefecto;
    }
    
    public String obtenerUrlBase() {
        return obtenerPropiedad(URL_BASE, "https://practice.expandtesting.com");
    }
    
    public String obtenerUrlLogin() {
        return obtenerUrlBase() + obtenerPropiedad(URL_LOGIN, "/login");
    }
    
    public String obtenerUrlRegistro() {
        return obtenerUrlBase() + obtenerPropiedad(URL_REGISTRO, "/register");
    }
    
    public String obtenerInformacionEstado() {
        return String.format("Config[URL: %s, Navegador: %s, Timeout: %s]", 
                           obtenerUrlBase(), 
                           obtenerPropiedad(NAVEGADOR_TIPO, "CHROME"),
                           obtenerPropiedad(TIMEOUT_EXPLICITO, "10"));
    }
}