package main.java.com.automatizacion.proyecto.enums;

/**
 * Enumeración que define los tipos de navegadores soportados
 * para la ejecución de pruebas automatizadas.
 * 
 * Implementa el principio de Abierto/Cerrado del SOLID permitiendo
 * fácil extensión de nuevos navegadores.
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
public enum TipoNavegador {
    
    /**
     * Google Chrome - Navegador predeterminado para pruebas
     */
    CHROME("Chrome", "chrome", ".exe"),
    
    /**
     * Mozilla Firefox - Navegador alternativo
     */
    FIREFOX("Firefox", "geckodriver", ".exe"),
    
    /**
     * Microsoft Edge - Navegador basado en Chromium
     */
    EDGE("Edge", "msedgedriver", ".exe"),
    
    /**
     * Safari - Navegador de Apple (solo macOS)
     */
    SAFARI("Safari", "safaridriver", "");
    
    private final String nombreCompleto;
    private final String nombreDriver;
    private final String extensionDriver;
    
    /**
     * Constructor del enum
     * @param nombreCompleto nombre completo del navegador para logs
     * @param nombreDriver nombre del driver ejecutable
     * @param extensionDriver extensión del driver (para Windows)
     */
    TipoNavegador(String nombreCompleto, String nombreDriver, String extensionDriver) {
        this.nombreCompleto = nombreCompleto;
        this.nombreDriver = nombreDriver;
        this.extensionDriver = extensionDriver;
    }
    
    /**
     * Obtiene el nombre completo del navegador
     * @return nombre completo del navegador
     */
    public String obtenerNombreCompleto() {
        return nombreCompleto;
    }
    
    /**
     * Obtiene el nombre del driver
     * @return nombre del driver ejecutable
     */
    public String obtenerNombreDriver() {
        return nombreDriver;
    }
    
    /**
     * Obtiene la extensión del driver
     * @return extensión del driver (principalmente para Windows)
     */
    public String obtenerExtensionDriver() {
        return extensionDriver;
    }
    
    /**
     * Obtiene el nombre completo del driver con extensión
     * @return nombre completo del driver
     */
    public String obtenerNombreDriverCompleto() {
        return nombreDriver + extensionDriver;
    }
    
    /**
     * Verifica si el navegador está soportado en el sistema operativo actual
     * @return true si está soportado, false en caso contrario
     */
    public boolean esSoportadoEnSistemaActual() {
        String sistemaOperativo = System.getProperty("os.name").toLowerCase();
        
        // Safari solo funciona en macOS
        if (this == SAFARI) {
            return sistemaOperativo.contains("mac");
        }
        
        // Los demás navegadores funcionan en Windows, Linux y macOS
        return true;
    }
    
    /**
     * Obtiene un TipoNavegador desde un string
     * @param nombre nombre del navegador como string
     * @return TipoNavegador correspondiente o CHROME por defecto
     */
    public static TipoNavegador desdeString(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return CHROME;
        }
        
        try {
            return TipoNavegador.valueOf(nombre.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            // Si no se encuentra el navegador, intentar por nombre completo
            for (TipoNavegador tipo : values()) {
                if (tipo.nombreCompleto.equalsIgnoreCase(nombre.trim())) {
                    return tipo;
                }
            }
            
            // Por defecto devolver Chrome
            return CHROME;
        }
    }
    
    /**
     * Obtiene todos los navegadores soportados en el sistema actual
     * @return array de navegadores soportados
     */
    public static TipoNavegador[] obtenerNavegadoresSoportados() {
        return java.util.Arrays.stream(values())
                .filter(TipoNavegador::esSoportadoEnSistemaActual)
                .toArray(TipoNavegador[]::new);
    }
    
    @Override
    public String toString() {
        return String.format("%s (%s)", nombreCompleto, name());
    }
}