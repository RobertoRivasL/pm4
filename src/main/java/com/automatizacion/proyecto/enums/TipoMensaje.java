package main.java.com.automatizacion.proyecto.enums;

/**
 * Enumeración que define los tipos de mensajes utilizados
 * en la aplicación para categorizar logs, reportes y validaciones.
 * 
 * Facilita la clasificación y el manejo uniforme de mensajes
 * siguiendo el principio de Responsabilidad Única.
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
public enum TipoMensaje {
    
    /**
     * Mensaje informativo - Para logs generales y notificaciones
     */
    INFORMATIVO("INFO", "Información", "✓"),
    
    /**
     * Mensaje de advertencia - Para situaciones que requieren atención
     */
    ADVERTENCIA("WARN", "Advertencia", "⚠"),
    
    /**
     * Mensaje de error - Para errores que no detienen la ejecución
     */
    ERROR("ERROR", "Error", "✗"),
    
    /**
     * Mensaje crítico - Para errores que detienen la ejecución
     */
    CRITICO("CRITICAL", "Crítico", "🔥"),
    
    /**
     * Mensaje de éxito - Para operaciones exitosas
     */
    EXITO("SUCCESS", "Éxito", "✅"),
    
    /**
     * Mensaje de debug - Para información de depuración
     */
    DEBUG("DEBUG", "Depuración", "🔍"),
    
    /**
     * Mensaje de validación - Para resultados de validaciones
     */
    VALIDACION("VALIDATION", "Validación", "🔎"),
    
    /**
     * Mensaje de configuración - Para información de configuración
     */
    CONFIGURACION("CONFIG", "Configuración", "⚙"),
    
    /**
     * Mensaje de prueba - Para información específica de pruebas
     */
    PRUEBA("TEST", "Prueba", "🧪"),
    
    /**
     * Mensaje de paso de prueba - Para cada paso de una prueba
     */
    PASO_PRUEBA("STEP", "Paso", "➤");
    
    private final String codigo;
    private final String descripcion;
    private final String icono;
    
    /**
     * Constructor del enum
     * @param codigo código corto del tipo de mensaje
     * @param descripcion descripción legible del tipo
     * @param icono icono visual para el tipo de mensaje
     */
    TipoMensaje(String codigo, String descripcion, String icono) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.icono = icono;
    }
    
    /**
     * Obtiene el código del tipo de mensaje
     * @return código del mensaje
     */
    public String obtenerCodigo() {
        return codigo;
    }
    
    /**
     * Obtiene la descripción del tipo de mensaje
     * @return descripción legible del tipo
     */
    public String obtenerDescripcion() {
        return descripcion;
    }
    
    /**
     * Obtiene el icono del tipo de mensaje
     * @return icono visual del tipo
     */
    public String obtenerIcono() {
        return icono;
    }
    
    /**
     * Formatea un mensaje con el tipo correspondiente
     * @param mensaje mensaje a formatear
     * @return mensaje formateado con icono y tipo
     */
    public String formatearMensaje(String mensaje) {
        return String.format("%s [%s] %s", icono, descripcion.toUpperCase(), mensaje);
    }
    
    /**
     * Formatea un mensaje con timestamp
     * @param mensaje mensaje a formatear
     * @return mensaje formateado con timestamp, icono y tipo
     */
    public String formatearMensajeConTiempo(String mensaje) {
        return String.format("[%s] %s [%s] %s", 
                           java.time.LocalDateTime.now().format(
                               java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                           ),
                           icono, 
                           descripcion.toUpperCase(), 
                           mensaje);
    }
    
    /**
     * Verifica si el tipo de mensaje es de error (ERROR o CRITICO)
     * @return true si es un tipo de error
     */
    public boolean esError() {
        return this == ERROR || this == CRITICO;
    }
    
    /**
     * Verifica si el tipo de mensaje es positivo (EXITO o INFORMATIVO)
     * @return true si es un tipo positivo
     */
    public boolean esPositivo() {
        return this == EXITO || this == INFORMATIVO;
    }
    
    /**
     * Verifica si el tipo de mensaje requiere atención (ADVERTENCIA, ERROR, CRITICO)
     * @return true si requiere atención
     */
    public boolean requiereAtencion() {
        return this == ADVERTENCIA || this == ERROR || this == CRITICO;
    }
    
    /**
     * Obtiene el nivel de prioridad del mensaje (1=más alta, 5=más baja)
     * @return nivel de prioridad
     */
    public int obtenerNivelPrioridad() {
        switch (this) {
            case CRITICO:
                return 1;
            case ERROR:
                return 2;
            case ADVERTENCIA:
                return 3;
            case INFORMATIVO:
            case EXITO:
            case VALIDACION:
            case PRUEBA:
                return 4;
            case DEBUG:
            case CONFIGURACION:
            case PASO_PRUEBA:
            default:
                return 5;
        }
    }
    
    /**
     * Obtiene un TipoMensaje desde un string
     * @param tipo string del tipo de mensaje
     * @return TipoMensaje correspondiente o INFORMATIVO por defecto
     */
    public static TipoMensaje desdeString(String tipo) {
        if (tipo == null || tipo.trim().isEmpty()) {
            return INFORMATIVO;
        }
        
        try {
            return TipoMensaje.valueOf(tipo.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            // Intentar por código
            for (TipoMensaje tipoMensaje : values()) {
                if (tipoMensaje.codigo.equalsIgnoreCase(tipo.trim())) {
                    return tipoMensaje;
                }
            }
            
            // Por defecto devolver INFORMATIVO
            return INFORMATIVO;
        }
    }
    
    @Override
    public String toString() {
        return String.format("%s %s", icono, descripcion);
    }
}