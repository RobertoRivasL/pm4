package com.automatizacion.proyecto.enums;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Enumeración que define los tipos de mensajes para logging y reporting.
 * Proporciona formateo consistente para diferentes tipos de mensajes.
 * 
 * Principios aplicados:
 * - SRP: Solo maneja tipos y formateo de mensajes
 * - DRY: Evita repetición en el formateo de logs
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
public enum TipoMensaje {
    
    INFORMATIVO("INFO", "ℹ️", "#2196F3"),
    ADVERTENCIA("WARN", "⚠️", "#FF9800"),
    ERROR("ERROR", "❌", "#F44336"),
    EXITO("SUCCESS", "✅", "#4CAF50"),
    PRUEBA("TEST", "🧪", "#9C27B0"),
    CONFIGURACION("CONFIG", "⚙️", "#607D8B"),
    VALIDACION("VALID", "✓", "#8BC34A"),
    PASO_PRUEBA("STEP", "👉", "#3F51B5"),
    RESULTADO("RESULT", "📊", "#FF5722"),
    DEBUG("DEBUG", "🔍", "#795548");
    
    private final String nivel;
    private final String icono;
    private final String color;
    private static final DateTimeFormatter FORMATO_FECHA = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Constructor de la enumeración
     * @param nivel nivel del mensaje
     * @param icono icono asociado al mensaje
     * @param color color en formato hexadecimal
     */
    TipoMensaje(String nivel, String icono, String color) {
        this.nivel = nivel;
        this.icono = icono;
        this.color = color;
    }
    
    /**
     * Formatea un mensaje con timestamp y prefijo del tipo
     * @param mensaje mensaje a formatear
     * @return mensaje formateado
     */
    public String formatearMensaje(String mensaje) {
        return String.format("[%s] %s [%s] %s", 
            LocalDateTime.now().format(FORMATO_FECHA),
            icono,
            nivel,
            mensaje
        );
    }
    
    /**
     * Formatea un mensaje con información adicional de contexto
     * @param mensaje mensaje principal
     * @param contexto información de contexto
     * @return mensaje formateado con contexto
     */
    public String formatearMensajeConContexto(String mensaje, String contexto) {
        return String.format("[%s] %s [%s] %s | Contexto: %s", 
            LocalDateTime.now().format(FORMATO_FECHA),
            icono,
            nivel,
            mensaje,
            contexto
        );
    }
    
    /**
     * Formatea un mensaje para reportes HTML
     * @param mensaje mensaje a formatear
     * @return mensaje formateado para HTML
     */
    public String formatearParaHTML(String mensaje) {
        return String.format(
            "<span style='color: %s;'><strong>%s [%s]</strong> %s</span>",
            color,
            icono,
            nivel,
            mensaje
        );
    }
    
    /**
     * Crea un separador visual para logs
     * @param titulo título del separador
     * @return separador formateado
     */
    public String crearSeparador(String titulo) {
        StringBuilder separador = new StringBuilder();
        String linea = "=".repeat(60);
        
        separador.append(formatearMensaje(linea));
        separador.append("\n");
        separador.append(formatearMensaje(String.format("    %s", titulo)));
        separador.append("\n");
        separador.append(formatearMensaje(linea));
        
        return separador.toString();
    }
    
    /**
     * Formatea un mensaje de progreso con porcentaje
     * @param mensaje mensaje base
     * @param actual valor actual
     * @param total valor total
     * @return mensaje de progreso formateado
     */
    public String formatearProgreso(String mensaje, int actual, int total) {
        double porcentaje = total > 0 ? (actual * 100.0) / total : 0.0;
        return formatearMensaje(
            String.format("%s - Progreso: %d/%d (%.1f%%)", 
                mensaje, actual, total, porcentaje)
        );
    }
    
    /**
     * Formatea un mensaje para indicar duración de operación
     * @param mensaje mensaje base
     * @param inicioTiempo tiempo de inicio en milisegundos
     * @return mensaje con duración formateado
     */
    public String formatearConDuracion(String mensaje, long inicioTiempo) {
        long duracion = System.currentTimeMillis() - inicioTiempo;
        return formatearMensaje(
            String.format("%s - Duración: %d ms", mensaje, duracion)
        );
    }
    
    /**
     * Getters para acceso a propiedades
     */
    public String getNivel() {
        return nivel;
    }
    
    public String getIcono() {
        return icono;
    }
    
    public String getColor() {
        return color;
    }
    
    @Override
    public String toString() {
        return String.format("%s %s", icono, nivel);
    }
}