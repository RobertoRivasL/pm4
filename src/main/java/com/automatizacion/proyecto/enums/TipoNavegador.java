package com.automatizacion.proyecto.enums;

/**
 * Enumeración que define los tipos de navegadores soportados.
 * Facilita la configuración y selección de navegadores para las pruebas.
 * 
 * Principios aplicados:
 * - SRP: Solo define tipos de navegadores
 * - OCP: Fácil extensión para nuevos navegadores
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
public enum TipoNavegador {
    
    CHROME("Chrome", "chrome", "googlechrome"),
    FIREFOX("Firefox", "firefox", "mozilla firefox"),
    EDGE("Edge", "edge", "microsoftedge"),
    SAFARI("Safari", "safari", "safari");
    
    private final String nombreMostrar;
    private final String nombreConfiguracion;
    private final String nombreDriver;
    
    /**
     * Constructor de la enumeración
     * @param nombreMostrar nombre para mostrar en logs y reportes
     * @param nombreConfiguracion nombre usado en archivos de configuración
     * @param nombreDriver nombre usado para el driver
     */
    TipoNavegador(String nombreMostrar, String nombreConfiguracion, String nombreDriver) {
        this.nombreMostrar = nombreMostrar;
        this.nombreConfiguracion = nombreConfiguracion;
        this.nombreDriver = nombreDriver;
    }
    
    /**
     * Obtiene el nombre para mostrar del navegador
     * @return nombre para mostrar
     */
    public String getNombreMostrar() {
        return nombreMostrar;
    }
    
    /**
     * Obtiene el nombre de configuración del navegador
     * @return nombre de configuración
     */
    public String getNombreConfiguracion() {
        return nombreConfiguracion;
    }
    
    /**
     * Obtiene el nombre del driver del navegador
     * @return nombre del driver
     */
    public String getNombreDriver() {
        return nombreDriver;
    }
    
    /**
     * Convierte una cadena a tipo de navegador
     * @param nombre nombre del navegador
     * @return tipo de navegador correspondiente
     * @throws IllegalArgumentException si el navegador no es soportado
     */
    public static TipoNavegador desdeString(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return CHROME; // Navegador por defecto
        }
        
        String nombreLimpio = nombre.trim().toUpperCase();
        
        try {
            return TipoNavegador.valueOf(nombreLimpio);
        } catch (IllegalArgumentException e) {
            // Intentar búsqueda por nombre de configuración
            for (TipoNavegador tipo : values()) {
                if (tipo.getNombreConfiguracion().equalsIgnoreCase(nombre) ||
                    tipo.getNombreMostrar().equalsIgnoreCase(nombre) ||
                    tipo.getNombreDriver().equalsIgnoreCase(nombre)) {
                    return tipo;
                }
            }
            
            throw new IllegalArgumentException(
                "Navegador no soportado: " + nombre + 
                ". Navegadores disponibles: " + java.util.Arrays.toString(values())
            );
        }
    }
    
    /**
     * Verifica si el navegador es soportado en el sistema operativo actual
     * @return true si el navegador es soportado
     */
    public boolean esSoportadoEnSO() {
        String sistemaOperativo = System.getProperty("os.n").toLowerCase();
        
        // Safari solo está disponible en macOS
        if (this == SAFARI) {
            return sistemaOperativo.contains("mac");
        }
        
        // Los demás navegadores están disponibles en todos los SO principales
        return sistemaOperativo.contains("win") || 
               sistemaOperativo.contains("mac") || 
               sistemaOperativo.contains("nix") || 
               sistemaOperativo.contains("nux");
    }
    
    /**
     * Obtiene el navegador por defecto del sistema
     * @return navegador por defecto basado en el SO
     */
    public static TipoNavegador obtenerNavegadorPorDefecto() {
        String sistemaOperativo = System.getProperty("os.n").toLowerCase();
        
        if (sistemaOperativo.contains("mac")) {
            return SAFARI;
        } else if (sistemaOperativo.contains("win")) {
            return EDGE;
        } else {
            return FIREFOX; // Para sistemas Linux
        }
    }
    
    @Override
    public String toString() {
        return nombreMostrar;
    }
}