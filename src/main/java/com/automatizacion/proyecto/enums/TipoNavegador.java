package com.automatizacion.proyecto.enums;

/**
 * Enumeraci?n para los tipos de navegadores soportados.
 * Implementa m?todos de conversi?n y validaci?n.
 * 
 * @author Roberto Rivas Lopez
 */
public enum TipoNavegador {
    CHROME("chrome"),
    FIREFOX("firefox"),
    EDGE("edge"),
    SAFARI("safari");
    
    private final String nombre;
    
    TipoNavegador(String nombre) {
        this.nombre = nombre;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    /**
     * Convierte un string a TipoNavegador
     * @param navegador string del navegador
     * @return TipoNavegador correspondiente
     * @throws IllegalArgumentException si el navegador no es v?lido
     */
    public static TipoNavegador desdeString(String navegador) {
        if (navegador == null || navegador.trim().isEmpty()) {
            return CHROME; // Valor por defecto
        }
        
        String navegadorLimpio = navegador.trim().toLowerCase();
        
        for (TipoNavegador tipo : values()) {
            if (tipo.nombre.equalsIgnoreCase(navegadorLimpio) || 
                tipo.name().equalsIgnoreCase(navegadorLimpio)) {
                return tipo;
            }
        }
        
        throw new IllegalArgumentException("Tipo de navegador no soportado: " + navegador);
    }
    
    /**
     * Verifica si un navegador es v?lido
     * @param navegador string a validar
     * @return true si es v?lido
     */
    public static boolean esValido(String navegador) {
        try {
            desdeString(navegador);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}