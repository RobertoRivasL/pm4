package com.automatizacion.proyecto.enums;

import java.util.Arrays;

/**
 * Enumeración que define los tipos de navegadores soportados
 * para la ejecución de pruebas automatizadas.
 * 
 * Implementa el principio de Abierto/Cerrado del SOLID permitiendo
 * fácil extensión de nuevos navegadores.
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
public enum TipoNavegador {
    
    /**
     * Google Chrome - Navegador predeterminado para pruebas
     */
    CHROME("Chrome", "chrome", ".exe"),
    
    /**
     * Mozilla Firefox - Navegador alternativo
     */
    FIREFOX("Firefox", "geckodriver", ".exe"),
    
    /**
     * Microsoft Edge - Navegador basado en Chromium
     */
    EDGE("Edge", "msedgedriver", ".exe"),
    
    /**
     * Safari - Navegador de Apple (solo macOS)
     */
    SAFARI("Safari", "safaridriver", "");
    
    private final String nombreCompleto;
    private final String nombreDriver;
    private final String extensionDriver;
    
    /**
     * Constructor del enum
     * @param nombreCompleto nombre completo del navegador para logs
     * @param nombreDriver nombre del driver ejecutable
     * @param extensionDriver extensión del driver (para Windows)
     */
    TipoNavegador(String nombreCompleto, String nombreDriver, String extensionDriver) {
        this.nombreCompleto = nombreCompleto;
        this.nombreDriver = nombreDriver;
        this.extensionDriver = extensionDriver;
    }
    
    /**
     * Obtiene el nombre completo del navegador
     * @return nombre completo del navegador
     */
    public String obtenerNombreCompleto() {
        return nombreCompleto;
    }
    
    /**
     * Obtiene el nombre del driver
     * @return nombre del driver ejecutable
     */
    public String obtenerNombreDriver() {
        return nombreDriver;
    }
    
    /**
     * Obtiene la extensión del driver
     * @return extensión del driver (principalmente para Windows)
     */
    public String obtenerExtensionDriver() {
        return extensionDriver;
    }
    
    /**
     * Obtiene el nombre completo del driver con extensión
     * @return nombre completo del driver
     */
    public String obtenerNombreDriverCompleto() {
        return nombreDriver + extensionDriver;
    }
    
    /**
     * Verifica si el navegador está soportado en el sistema operativo actual
     * @return true si está soportado, false en caso contrario
     */
    public boolean esSoportadoEnSistemaActual() {
        String sistemaOperativo = System.getProperty("os.name").toLowerCase();
        
        // Safari solo funciona en macOS
        if (this == SAFARI) {
            return sistemaOperativo.contains("mac");
        }
        
        // Los demás navegadores funcionan en Windows, Linux y macOS
        return true;
    }
    
    /**
     * Obtiene un TipoNavegador desde un string
     * @param nombre nombre del navegador como string
     * @return TipoNavegador correspondiente o CHROME por defecto
     */
    public static TipoNavegador desdeString(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return CHROME;
        }
        
        try {
            return TipoNavegador.valueOf(nombre.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            // Si no se encuentra el navegador, intentar por nombre completo
            for (TipoNavegador tipo : values()) {
                if (tipo.nombreCompleto.equalsIgnoreCase(nombre.trim())) {
                    return tipo;
                }
            }
            
            // Por defecto devolver Chrome
            return CHROME;
        }
    }
    
    /**
     * Obtiene todos los navegadores soportados en el sistema actual
     * @return array de navegadores soportados
     */
    public static TipoNavegador[] obtenerNavegadoresSoportados() {
        return Arrays.stream(values())
                .filter(TipoNavegador::esSoportadoEnSistemaActual)
                .toArray(TipoNavegador[]::new);
    }
    
    /**
     * Verifica si el navegador es compatible con modo headless
     * @return true si soporta modo headless
     */
    public boolean soportaModoHeadless() {
        // Safari no soporta modo headless
        return this != SAFARI;
    }
    
    /**
     * Obtiene la versión recomendada del driver
     * @return versión recomendada
     */
    public String obtenerVersionRecomendada() {
        switch (this) {
            case CHROME:
                return "120.0.0";
            case FIREFOX:
                return "0.33.0";
            case EDGE:
                return "120.0.0";
            case SAFARI:
                return "17.0";
            default:
                return "latest";
        }
    }
    
    /**
     * Obtiene el puerto por defecto para debugging
     * @return puerto de debugging
     */
    public int obtenerPuertoDebug() {
        switch (this) {
            case CHROME:
            case EDGE:
                return 9222;
            case FIREFOX:
                return 6000;
            case SAFARI:
                return 9999;
            default:
                return 9222;
        }
    }
    
    /**
     * Verifica si el navegador requiere configuración especial en Linux
     * @return true si requiere configuración especial
     */
    public boolean requiereConfiguracionLinux() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.contains("linux") && (this == CHROME || this == EDGE);
    }
    
    /**
     * Obtiene argumentos específicos para el navegador en modo headless
     * @return array de argumentos para modo headless
     */
    public String[] obtenerArgumentosHeadless() {
        switch (this) {
            case CHROME:
            case EDGE:
                return new String[]{"--headless=new", "--window-size=1920,1080", "--disable-gpu"};
            case FIREFOX:
                return new String[]{"--headless", "--width=1920", "--height=1080"};
            case SAFARI:
                return new String[]{}; // Safari no soporta headless
            default:
                return new String[]{"--headless"};
        }
    }
    
    /**
     * Obtiene la extensión de archivo de screenshot por defecto
     * @return extensión de archivo
     */
    public String obtenerExtensionScreenshot() {
        return ".png";
    }
    
    /**
     * Verifica si el navegador soporta descargas automáticas
     * @return true si soporta descargas automáticas
     */
    public boolean soportaDescargasAutomaticas() {
        return this != SAFARI; // Safari tiene limitaciones con descargas automáticas
    }
    
    @Override
    public String toString() {
        return String.format("%s (%s)", nombreCompleto, name());
    }
    
    /**
     * Representación detallada del navegador
     * @return información completa del navegador
     */
    public String toStringDetallado() {
        return String.format("Navegador: %s | Driver: %s | Versión: %s | Headless: %s | SO Actual: %s",
                nombreCompleto,
                obtenerNombreDriverCompleto(),
                obtenerVersionRecomendada(),
                soportaModoHeadless() ? "Sí" : "No",
                esSoportadoEnSistemaActual() ? "Soportado" : "No soportado");
    }
}