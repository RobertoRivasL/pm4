package com.automatizacion.proyecto.paginas.interfaces;

public interface IPaginaBase {
    boolean esPaginaVisible();
    String obtenerTituloPagina();
    boolean esperarCargaPagina(int timeoutSegundos);
    void navegarAtras();
    void actualizarPagina();
}