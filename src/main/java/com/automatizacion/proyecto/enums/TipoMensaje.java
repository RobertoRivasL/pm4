package com.automatizacion.proyecto.enums;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Enumeración que define los tipos de mensajes utilizados
 * en la aplicación para categorizar logs, reportes y validaciones.
 * 
 * Facilita la clasificación y el manejo uniforme de mensajes
 * siguiendo el principio de Responsabilidad Única.
 * 
 * @author Antonio B. Arriagada LL. (anarriag@gmail.com)
 * @author Dante Escalona Bustos (Jacobo.bustos.22@gmail.com)
 * @author Roberto Rivas Lopez (umancl@gmail.com)
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
     * 
     * @param codigo      código corto del tipo de mensaje
     * @param descripcion descripción legible del tipo
     * @param icono       icono visual para el tipo de mensaje
     */
    TipoMensaje(String codigo, String descripcion, String icono) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.icono = icono;
    }

    /**
     * Obtiene el código del tipo de mensaje
     * 
     * @return código del mensaje
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Obtiene la descripción del tipo de mensaje
     * 
     * @return descripción del mensaje
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Obtiene el icono del tipo de mensaje
     * 
     * @return icono del mensaje
     */
    public String getIcono() {
        return icono;
    }

    /**
     * Formatea un mensaje con el formato estándar
     * 
     * @param mensaje mensaje a formatear
     * @return mensaje formateado con timestamp e icono
     */
    public String formatearMensaje(String mensaje) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return String.format("[%s] %s %s - %s", timestamp, icono, codigo, mensaje);
    }

    /**
     * Formatea un mensaje simple sin timestamp
     * 
     * @param mensaje mensaje a formatear
     * @return mensaje formateado solo con icono
     */
    public String formatearMensajeSimple(String mensaje) {
        return String.format("%s %s", icono, mensaje);
    }

    /**
     * Formatea un mensaje para consola con colores (si está soportado)
     * 
     * @param mensaje mensaje a formatear
     * @return mensaje formateado con colores ANSI
     */
    public String formatearMensajeConsola(String mensaje) {
        String colorCode = obtenerCodigoColor();
        String resetCode = "\u001B[0m";

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        return String.format("%s[%s] %s %s - %s%s",
                colorCode, timestamp, icono, codigo, mensaje, resetCode);
    }

    /**
     * Obtiene el código de color ANSI según el tipo de mensaje
     * 
     * @return código de color ANSI
     */
    private String obtenerCodigoColor() {
        return switch (this) {
            case ERROR, CRITICO -> "\u001B[31m"; // Rojo
            case ADVERTENCIA -> "\u001B[33m"; // Amarillo
            case EXITO -> "\u001B[32m"; // Verde
            case INFORMATIVO -> "\u001B[36m"; // Cian
            case DEBUG -> "\u001B[35m"; // Magenta
            case VALIDACION -> "\u001B[34m"; // Azul
            case CONFIGURACION -> "\u001B[37m"; // Blanco
            case PRUEBA -> "\u001B[96m"; // Cian claro
            case PASO_PRUEBA -> "\u001B[94m"; // Azul claro
        };
    }

    /**
     * Verifica si el tipo de mensaje es de nivel crítico
     * 
     * @return true si es crítico o error
     */
    public boolean esCritico() {
        return this == CRITICO || this == ERROR;
    }

    /**
     * Verifica si el tipo de mensaje es informativo
     * 
     * @return true si es informativo, éxito o debug
     */
    public boolean esInformativo() {
        return this == INFORMATIVO || this == EXITO || this == DEBUG;
    }

    /**
     * Obtiene el nivel de prioridad del mensaje (0-10, donde 10 es más crítico)
     * 
     * @return nivel de prioridad
     */
    public int obtenerNivelPrioridad() {
        return switch (this) {
            case CRITICO -> 10;
            case ERROR -> 8;
            case ADVERTENCIA -> 6;
            case VALIDACION -> 5;
            case PRUEBA, PASO_PRUEBA -> 4;
            case CONFIGURACION -> 3;
            case INFORMATIVO -> 2;
            case EXITO -> 1;
            case DEBUG -> 0;
        };
    }

    /**
     * Crea un mensaje de excepción formateado
     * 
     * @param mensaje   mensaje base
     * @param excepcion excepción asociada
     * @return mensaje formateado con información de la excepción
     */
    public String formatearMensajeConExcepcion(String mensaje, Exception excepcion) {
        StringBuilder sb = new StringBuilder();
        sb.append(formatearMensaje(mensaje));

        if (excepcion != null) {
            sb.append(" | Excepción: ").append(excepcion.getClass().getSimpleName());
            if (excepcion.getMessage() != null && !excepcion.getMessage().trim().isEmpty()) {
                sb.append(" - ").append(excepcion.getMessage());
            }
        }

        return sb.toString();
    }

    /**
     * Crea un mensaje con contexto adicional
     * 
     * @param mensaje  mensaje principal
     * @param contexto información de contexto
     * @return mensaje formateado con contexto
     */
    public String formatearMensajeConContexto(String mensaje, String contexto) {
        if (contexto == null || contexto.trim().isEmpty()) {
            return formatearMensaje(mensaje);
        }

        return formatearMensaje(mensaje + " [Contexto: " + contexto + "]");
    }

    /**
     * Obtiene todos los tipos de mensaje ordenados por prioridad
     * 
     * @return array de tipos ordenados por prioridad (mayor a menor)
     */
    public static TipoMensaje[] obtenerTiposOrdenadosPorPrioridad() {
        TipoMensaje[] tipos = values();
        java.util.Arrays.sort(tipos, (a, b) -> Integer.compare(b.obtenerNivelPrioridad(), a.obtenerNivelPrioridad()));
        return tipos;
    }

    /**
     * Busca un tipo de mensaje por su código
     * 
     * @param codigo código a buscar
     * @return TipoMensaje correspondiente o null si no se encuentra
     */
    public static TipoMensaje buscarPorCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            return null;
        }

        for (TipoMensaje tipo : values()) {
            if (tipo.codigo.equalsIgnoreCase(codigo.trim())) {
                return tipo;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return descripcion + " (" + codigo + ")";
    }

    /**
     * Verifica si el mensaje debe ser loggeado según el nivel
     * 
     * @param nivelMinimo nivel mínimo requerido
     * @return true si debe ser loggeado
     */
    public boolean debeLoggear(int nivelMinimo) {
        return this.obtenerNivelPrioridad() >= nivelMinimo;
    }

    /**
     * Obtiene el nivel de log de SLF4J correspondiente
     * 
     * @return nivel para SLF4J
     */
    public String obtenerNivelSLF4J() {
        return switch (this) {
            case CRITICO, ERROR -> "ERROR";
            case ADVERTENCIA -> "WARN";
            case DEBUG -> "DEBUG";
            case VALIDACION, PRUEBA, PASO_PRUEBA -> "INFO";
            default -> "INFO";
        };
    }
}