package com.automatizacion.proyecto.enums;

public enum TipoMensaje {
    INFORMATIVO("INFO", "Información", "✓"),
    ADVERTENCIA("WARN", "Advertencia", "⚠"),
    ERROR("ERROR", "Error", "✗"),
    CRITICO("CRITICAL", "Crítico", "🔥"),
    EXITO("SUCCESS", "Éxito", "✅"),
    DEBUG("DEBUG", "Depuración", "🔍"),
    VALIDACION("VALIDATION", "Validación", "🔎"),
    CONFIGURACION("CONFIG", "Configuración", "⚙"),
    PRUEBA("TEST", "Prueba", "🧪"),
    PASO_PRUEBA("STEP", "Paso", "➤");
    
    private final String codigo;
    private final String descripcion;
    private final String icono;
    
    TipoMensaje(String codigo, String descripcion, String icono) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.icono = icono;
    }
    
    public String formatearMensaje(String mensaje) {
        return String.format("[%s] %s %s", codigo, icono, mensaje);
    }
    
    public String getCodigo() { return codigo; }
    public String getDescripcion() { return descripcion; }
    public String getIcono() { return icono; }
}