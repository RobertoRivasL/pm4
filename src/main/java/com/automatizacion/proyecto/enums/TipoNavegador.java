package com.automatizacion.proyecto.enums;

/**
 * Enumeración para los tipos de navegadores soportados
 * Cumple con requerimiento ABP: Soporte multi-navegador
 */
public enum TipoNavegador {
    CHROME("chrome"),
    FIREFOX("firefox"),
    EDGE("edge");
    
    private final String nombre;
    
    TipoNavegador(String nombre) {
        this.nombre = nombre;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public static TipoNavegador desdeString(String navegador) {
        if (navegador == null) return CHROME;
        
        switch (navegador.toUpperCase()) {
            case "FIREFOX":
                return FIREFOX;
            case "EDGE":
                return EDGE;
            default:
                return CHROME;
        }
    }
}