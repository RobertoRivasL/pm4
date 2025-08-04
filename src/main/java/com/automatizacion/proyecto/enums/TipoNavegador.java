package com.automatizacion.proyecto.enums;

public enum TipoNavegador {
    CHROME("chrome", "Google Chrome"),
    FIREFOX("firefox", "Mozilla Firefox"),
    EDGE("edge", "Microsoft Edge"),
    SAFARI("safari", "Apple Safari");
    
    private final String codigo;
    private final String descripcion;
    
    TipoNavegador(String codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }
    
    public String getCodigo() { return codigo; }
    public String getDescripcion() { return descripcion; }
}