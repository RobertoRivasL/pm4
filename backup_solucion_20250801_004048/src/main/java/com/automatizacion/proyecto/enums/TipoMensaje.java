package com.automatizacion.proyecto.enums;

/**
 * Enumeración de tipos de mensajes para logging
 * @author Roberto Rivas Lopez
 */
public enum TipoMensaje {
    INFORMATIVO("INFO", "✓"),
    ADVERTENCIA("WARN", "⚠"),
    ERROR("ERROR", "✗"),
    CRITICO("CRITICAL", "🔥"),
    EXITO("SUCCESS", "✅"),
    DEBUG("DEBUG", "🔍"),
    VALIDACION("VALIDATION", "🔎"),
    CONFIGURACION("CONFIG", "⚙"),
    PRUEBA("TEST", "🧪"),
    PASO_PRUEBA("STEP", "➤");
    
    private final String codigo;
    private final String icono;
    
    TipoMensaje(String codigo, String icono) {
        this.codigo = codigo;
        this.icono = icono;
    }
    
    public String obtenerCodigo() {
        return codigo;
    }
    
    public String obtenerIcono() {
        return icono;
    }
    
    public String formatearMensaje(String mensaje) {
        return String.format("[%s] %s %s", codigo, icono, mensaje);
    }
}
