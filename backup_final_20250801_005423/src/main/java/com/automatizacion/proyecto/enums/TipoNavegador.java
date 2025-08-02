package com.automatizacion.proyecto.enums;

/**
 * Enumeración de tipos de navegadores soportados
 * @author Roberto Rivas Lopez
 */
public enum TipoNavegador {
    CHROME("Chrome", "chrome"),
    FIREFOX("Firefox", "geckodriver"),
    EDGE("Edge", "msedgedriver");
    
    private final String nombreCompleto;
    private final String nombreDriver;
    
    TipoNavegador(String nombreCompleto, String nombreDriver) {
        this.nombreCompleto = nombreCompleto;
        this.nombreDriver = nombreDriver;
    }
    
    public String obtenerNombreCompleto() {
        return nombreCompleto;
    }
    
    public String obtenerNombreDriver() {
        return nombreDriver;
    }
    
    public static TipoNavegador desdeString(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return CHROME;
        }
        
        try {
            return TipoNavegador.valueOf(nombre.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            return CHROME;
        }
    }
}
