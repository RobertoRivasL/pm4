package com.automatizacion.proyecto.configuracion;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuración global del proyecto (Singleton)
 * @author Roberto Rivas Lopez
 */
public class ConfiguracionGlobal {
    private static ConfiguracionGlobal instancia;
    private Properties propiedades;
    
    private ConfiguracionGlobal() {
        cargarPropiedades();
    }
    
    public static ConfiguracionGlobal obtenerInstancia() {
        if (instancia == null) {
            instancia = new ConfiguracionGlobal();
        }
        return instancia;
    }
    
    private void cargarPropiedades() {
        propiedades = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                propiedades.load(input);
            }
        } catch (IOException e) {
            // Valores por defecto
            propiedades.setProperty("url.base", "https://opensource-demo.orangehrmlive.com/");
            propiedades.setProperty("navegador.tipo", "CHROME");
            propiedades.setProperty("navegador.headless", "false");
            propiedades.setProperty("timeout.explicito", "10");
            propiedades.setProperty("directorio.capturas", "capturas");
        }
    }
    
    public String obtenerUrlBase() {
        return propiedades.getProperty("url.base");
    }
    
    public String obtenerTipoNavegador() {
        return propiedades.getProperty("navegador.tipo");
    }
    
    public boolean esNavegadorHeadless() {
        return Boolean.parseBoolean(propiedades.getProperty("navegador.headless"));
    }
    
    public int obtenerTimeoutExplicito() {
        return Integer.parseInt(propiedades.getProperty("timeout.explicito"));
    }
    
    public String obtenerDirectorioCapturas() {
        return propiedades.getProperty("directorio.capturas");
    }
}
