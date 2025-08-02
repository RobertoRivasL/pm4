package com.automatizacion.proyecto.base;

/**
 * Interfaz base que define las operaciones comunes para todas las páginas.
 * Establece el contrato básico que deben cumplir todas las páginas del sistema.
 * 
 * Implementa el principio de Segregación de Interfaces del SOLID,
 * proporcionando solo las operaciones esenciales.
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
public interface IPaginaBase {
    
    /**
     * Verifica si la página actual está visible y cargada correctamente
     * @return true si la página está visible, false en caso contrario
     */
    boolean esPaginaVisible();
    
    /**
     * Obtiene el título de la página actual
     * @return título de la página
     */
    String obtenerTituloPagina();
    
    /**
     * Espera a que la página se cargue completamente
     * @param timeoutSegundos tiempo máximo de espera en segundos
     * @return true si la página se cargó correctamente
     */
    boolean esperarCargaPagina(int timeoutSegundos);
    
    /**
     * Navega hacia atrás en el historial del navegador
     */
    void navegarAtras();
    
    /**
     * Actualiza/recarga la página actual
     */
    void actualizarPagina();
