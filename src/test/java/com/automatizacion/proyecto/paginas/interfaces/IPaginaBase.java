package com.automatizacion.proyecto.paginas.interfaces;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Interfaz base que define las operaciones comunes para todos los Page Objects.
 * Establece el contrato básico que deben cumplir todas las páginas.
 * 
 * Implementa el principio de Segregación de Interfaces del SOLID
 * proporcionando solo las operaciones básicas esenciales.
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
public interface IPaginaBase {
    
    /**
     * Verifica si la página está visible y cargada correctamente
     * 
     * @return true si la página está visible
     */
    boolean esPaginaVisible();
    
    /**
     * Obtiene el título de la página actual
     * 
     * @return título de la página
     */
    String obtenerTituloPagina();
    
    /**
     * Obtiene la URL actual de la página
     * 
     * @return URL actual
     */
    String obtenerUrlActual();
    
    /**
     * Navega a una URL específica
     * 
     * @param url URL de destino
     */
    void navegarA(String url);
    
    /**
     * Espera a que un elemento sea visible
     * 
     * @param localizador localizador del elemento
     * @param timeoutSegundos tiempo máximo de espera
     * @return WebElement visible
     */
    WebElement esperarElementoVisible(By localizador, int timeoutSegundos);
    
    /**
     * Espera a que un elemento sea clickeable
     * 
     * @param localizador localizador del elemento
     * @param timeoutSegundos tiempo máximo de espera
     * @return WebElement clickeable
     */
    WebElement esperarElementoClickeable(By localizador, int timeoutSegundos);
    
    /**
     * Verifica si un elemento está presente en la página
     * 
     * @param localizador localizador del elemento
     * @return true si el elemento está presente
     */
    boolean estaElementoPresente(By localizador);
    
    /**
     * Realiza click en un elemento de forma segura
     * 
     * @param elemento elemento sobre el cual hacer click
     * @return true si el click fue exitoso
     */
    boolean clickElementoSeguro(WebElement elemento);
    
    /**
     * Ingresa texto en un campo de forma segura
     * 
     * @param elemento campo donde ingresar el texto
     * @param texto texto a ingresar
     * @return true si el texto fue ingresado correctamente
     */
    boolean ingresarTextoSeguro(WebElement elemento, String texto);
    
    /**
     * Limpia un campo de texto
     * 
     * @param elemento campo a limpiar
     * @return true si se limpió correctamente
     */
    boolean limpiarCampo(WebElement elemento);
    
    /**
     * Obtiene el texto de un elemento
     * 
     * @param elemento elemento del cual obtener el texto
     * @return texto del elemento o cadena vacía si no se puede obtener
     */
    String obtenerTextoElemento(WebElement elemento);
    
    /**
     * Verifica si un elemento está habilitado
     * 
     * @param elemento elemento a verificar
     * @return true si el elemento está habilitado
     */
    boolean estaElementoHabilitado(WebElement elemento);
    
    /**
     * Verifica si un elemento está seleccionado
     * 
     * @param elemento elemento a verificar
     * @return true si el elemento está seleccionado
     */
    boolean estaElementoSeleccionado(WebElement elemento);
    
    /**
     * Toma una captura de pantalla de la página actual
     * 
     * @param nombreArchivo nombre del archivo de captura
     * @return ruta del archivo generado
     */
    String capturarPantalla(String nombreArchivo);
    
    /**
     * Hace scroll hasta un elemento específico
     * 
     * @param elemento elemento hacia el cual hacer scroll
     */
    void scrollHastaElemento(WebElement elemento);
    
    /**
     * Espera a que la página termine de cargar completamente
     */
    void esperarCargaCompleta();
}