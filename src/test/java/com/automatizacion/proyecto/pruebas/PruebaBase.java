package com.automatizacion.proyecto.pruebas;

import com.automatizacion.proyecto.configuracion.ConfiguracionGlobal;
import com.automatizacion.proyecto.enums.TipoNavegador;
import com.automatizacion.proyecto.enums.TipoMensaje;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase base abstracta para todas las pruebas del proyecto.
 * Proporciona funcionalidades comunes y configuraciones base.
 * 
 * Implementa principios de herencia y reutilización de código
 * siguiendo las mejores prácticas de automatización.
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
public abstract class PruebaBase {
    
    private static final Logger logger = LoggerFactory.getLogger(PruebaBase.class);
    
    protected ConfiguracionGlobal configuracion;
    
    /**
     * Constructor que inicializa la configuración global
     */
    public PruebaBase() {
        this.configuracion = ConfiguracionGlobal.obtenerInstancia();
        logger.debug(TipoMensaje.DEBUG.formatearMensaje("PruebaBase inicializada"));
    }
    
    /**
     * Obtiene la configuración global
     * @return instancia de ConfiguracionGlobal
     */
    protected ConfiguracionGlobal obtenerConfiguracion() {
        return configuracion;
    }
    
    /**
     * Obtiene el tipo de navegador configurado
     * @return TipoNavegador configurado
     */
    protected TipoNavegador obtenerTipoNavegadorConfigurado() {
        String navegadorStr = configuracion.obtenerTipoNavegador();
        return TipoNavegador.desdeString(navegadorStr);
    }
    
    /**
     * Verifica si el modo headless está habilitado
     * @return true si está en modo headless
     */
    protected boolean esModoHeadless() {
        return configuracion.esNavegadorHeadless();
    }
    
    /**
     * Obtiene la URL base configurada
     * @return URL base
     */
    protected String obtenerUrlBase() {
        return configuracion.obtenerUrlBase();
    }
    
    /**
     * Obtiene el timeout implícito configurado
     * @return timeout en segundos
     */
    protected int obtenerTimeoutImplicito() {
        return configuracion.obtenerTimeoutImplicito();
    }
    
    /**
     * Obtiene el timeout explícito configurado
     * @return timeout en segundos
     */
    protected int obtenerTimeoutExplicito() {
        return configuracion.obtenerTimeoutExplicito();
    }
    
    /**
     * Obtiene el directorio de capturas configurado
     * @return directorio de capturas
     */
    protected String obtenerDirectorioCapturas() {
        return configuracion.obtenerDirectorioCapturas();
    }
    
    /**
     * Obtiene el directorio de reportes configurado
     * @return directorio de reportes
     */
    protected String obtenerDirectorioReportes() {
        return configuracion.obtenerDirectorioReportes();
    }
    
    /**
     * Obtiene el directorio de datos configurado
     * @return directorio de datos
     */
    protected String obtenerDirectorioDatos() {
        return configuracion.obtenerDirectorioDatos();
    }
    
    /**
     * Registra un mensaje informativo
     * @param mensaje mensaje a registrar
     */
    protected void logInfo(String mensaje) {
        logger.info(TipoMensaje.INFORMATIVO.formatearMensaje(mensaje));
    }
    
    /**
     * Registra un mensaje de debug
     * @param mensaje mensaje a registrar
     */
    protected void logDebug(String mensaje) {
        logger.debug(TipoMensaje.DEBUG.formatearMensaje(mensaje));
    }
    
    /**
     * Registra un mensaje de error
     * @param mensaje mensaje a registrar
     */
    protected void logError(String mensaje) {
        logger.error(TipoMensaje.ERROR.formatearMensaje(mensaje));
    }
    
    /**
     * Registra un mensaje de advertencia
     * @param mensaje mensaje a registrar
     */
    protected void logWarning(String mensaje) {
        logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(mensaje));
    }
    
    /**
     * Registra un mensaje de éxito
     * @param mensaje mensaje a registrar
     */
    protected void logExito(String mensaje) {
        logger.info(TipoMensaje.EXITO.formatearMensaje(mensaje));
    }
    
    /**
     * Registra un mensaje de configuración
     * @param mensaje mensaje a registrar
     */
    protected void logConfiguracion(String mensaje) {
        logger.info(TipoMensaje.CONFIGURACION.formatearMensaje(mensaje));
    }
    
    /**
     * Registra un mensaje de validación
     * @param mensaje mensaje a registrar
     */
    protected void logValidacion(String mensaje) {
        logger.info(TipoMensaje.VALIDACION.formatearMensaje(mensaje));
    }
    
    /**
     * Registra un paso de prueba
     * @param mensaje mensaje del paso
     */
    protected void logPasoPrueba(String mensaje) {
        logger.info(TipoMensaje.PASO_PRUEBA.formatearMensaje(mensaje));
    }
    
    /**
     * Método abstracto que debe ser implementado por las clases hijas
     * para definir la configuración específica de cada tipo de prueba
     */
    protected abstract void configuracionEspecifica();
    
    /**
     * Método de utilidad para pausas cortas en las pruebas
     * @param milisegundos tiempo a esperar
     */
    protected void esperarTiempo(int milisegundos) {
        try {
            Thread.sleep(milisegundos);
        } catch (InterruptedException e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("Interrupción durante espera"));
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Valida que la configuración esté correctamente cargada
     * @throws RuntimeException si la configuración no es válida
     */
    protected void validarConfiguracion() {
        if (configuracion == null) {
            throw new RuntimeException("La configuración global no está inicializada");
        }
        
        String urlBase = obtenerUrlBase();
        if (urlBase == null || urlBase.trim().isEmpty()) {
            throw new RuntimeException("La URL base no está configurada");
        }
        
        logValidacion("Configuración validada correctamente");
    }
    
    /**
     * Información del navegador configurado
     * @return información detallada del navegador
     */
    protected String obtenerInformacionNavegador() {
        TipoNavegador tipo = obtenerTipoNavegadorConfigurado();
        return String.format("Navegador: %s | Headless: %s | Soportado: %s",
                           tipo.obtenerNombreCompleto(),
                           esModoHeadless() ? "Sí" : "No",
                           tipo.esSoportadoEnSistemaActual() ? "Sí" : "No");
    }
}