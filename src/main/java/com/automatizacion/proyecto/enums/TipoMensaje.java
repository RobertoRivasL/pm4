package com.automatizacion.proyecto.enums;

/**
 * Enumeración para tipos de mensajes de log.
 * Proporciona formateo consistente para diferentes tipos de mensajes.
 * 
 * @author Roberto Rivas Lopez
 */
public enum TipoMensaje {
    CONFIGURACION("[CONFIG]"),
    INFORMATIVO("[INFO]"),
    PASO_PRUEBA("[PASO]"),
    VALIDACION("[VALIDACION]"),
    ERROR("[ERROR]"),
    CRITICO("[CRITICO]"),
    DEBUG("[DEBUG]"),
    PRUEBA("[PRUEBA]"),
    ADVERTENCIA("[WARN]"),
    EXITO("[SUCCESS]");
    
    private final String prefijo;
    
    TipoMensaje(String prefijo) {
        this.prefijo = prefijo;
    }
    
    /**
     * Formatea un mensaje con el prefijo correspondiente
     * @param mensaje mensaje a formatear
     * @return mensaje formateado
     */
    public String formatearMensaje(String mensaje) {
        return String.format("%s %s", prefijo, mensaje);
    }
    
    /**
     * Formatea un mensaje con argumentos adicionales
     * @param template template del mensaje
     * @param argumentos argumentos para el template
     * @return mensaje formateado
     */
    public String formatearMensaje(String template, Object... argumentos) {
        String mensajeFormateado = String.format(template, argumentos);
        return formatearMensaje(mensajeFormateado);
    }
}