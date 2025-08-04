package com.automatizacion.proyecto.pruebas;

import com.automatizacion.proyecto.base.BaseTest;
import com.automatizacion.proyecto.datos.ModeloDatosPrueba;
import com.automatizacion.proyecto.datos.ProveedorDatos;
import com.automatizacion.proyecto.enums.TipoMensaje;
import com.automatizacion.proyecto.paginas.PaginaRegistro;
import io.qameta.allure.*;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.openqa.selenium.By;

/**
 * Clase de pruebas para la funcionalidad de registro de usuarios.
 * Contiene todos los casos de prueba relacionados con el registro.
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
@Epic("Gestión de Usuarios")
@Feature("Registro de Usuarios")
public class PruebasRegistro extends BaseTest {
    
    private PaginaRegistro paginaRegistro;
    
    @BeforeMethod(alwaysRun = true)
    public void configuracionEspecificaRegistro() {
        paginaRegistro = new PaginaRegistro(obtenerDriver());
        
        logPasoPrueba("Verificando que la página de registro está visible");
        boolean paginaVisible = paginaRegistro.esPaginaVisible();
        Assert.assertTrue(paginaVisible, "La página de registro debería estar visible");
        
        if (paginaVisible) {
            logValidacion("Página de registro cargada correctamente");
            paginaRegistro.validarElementosPagina();
        }
    }
    
    @Test(priority = 1, 
          description = "Verificar registro exitoso con datos válidos",
          groups = {"smoke", "registro", "positivo"})
    @Story("Registro Exitoso")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verifica que un usuario puede registrarse correctamente con datos válidos")
    public void testRegistroExitoso() {
        
        logPasoPrueba("Preparando datos válidos para registro exitoso");
        
        ModeloDatosPrueba datos = ModeloDatosPrueba.builder()
                .casoPrueba("REG_001")
                .descripcion("Registro exitoso con datos válidos")
                .nombre("Roberto")
                .email("roberto.rivas." + System.currentTimeMillis() + "@test.com")
                .password("Password123!")
                .confirmacionPassword("Password123!")
                .esValido(true)
                .aceptarTerminos(true)
                .resultadoEsperado("Usuario registrado exitosamente")
                .build();
        
        logPasoPrueba("Ejecutando proceso de registro con datos: " + datos.getCasoPrueba());
        
        boolean registroExitoso = paginaRegistro.registrarUsuario(datos);
        
        logValidacion("Verificando resultado del registro");
        Assert.assertTrue(registroExitoso, "El registro debería ser exitoso con datos válidos");
        
        logValidacion("Verificando que no hay errores en la página");
        Assert.assertFalse(paginaRegistro.hayErroresValidacion(), "No debería haber errores de validación");
        
        capturarPantalla("registro_exitoso");
        logger.info(TipoMensaje.EXITO.formatearMensaje("Test de registro exitoso completado"));
    }
    
    @Test(priority = 2,
          description = "Verificar validación de email inválido",
          groups = {"regression", "registro", "negativo", "validacion"})
    @Story("Validación de Email")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifica que el sistema rechaza emails con formato inválido")
    public void testValidacionEmailInvalido() {
        
        logPasoPrueba("Preparando datos con email inválido");
        
        ModeloDatosPrueba datos = ModeloDatosPrueba.builder()
                .casoPrueba("REG_002")
                .descripcion("Validación de email inválido")
                .nombre("Usuario")
                .email("email-invalido-sin-arroba")
                .password("Password123!")
                .confirmacionPassword("Password123!")
                .esValido(false)
                .aceptarTerminos(true)
                .mensajeError("Please enter a valid email address")
                .build();
        
        logPasoPrueba("Ejecutando registro con email inválido");
        
        boolean registroFallido = paginaRegistro.registrarUsuario(datos);
        
        logValidacion("Verificando que el registro falló como esperado");
        Assert.assertTrue(registroFallido, "El registro debería fallar con email inválido");
        
        String mensajeError = paginaRegistro.obtenerMensajeError();
        logValidacion("Mensaje de error obtenido: " + mensajeError);
        
        capturarPantalla("validacion_email_invalido");
        logger.info(TipoMensaje.EXITO.formatearMensaje("Test de validación de email inválido completado"));
    }
    
    @Test(priority = 3,
          description = "Verificar validación de contraseñas que no coinciden",
          groups = {"regression", "registro", "negativo", "validacion"})
    @Story("Validación de Contraseñas")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifica que el sistema detecta cuando las contraseñas no coinciden")
    public void testValidacionPasswordsNoCoinciden() {
        
        logPasoPrueba("Preparando datos con contraseñas diferentes");
        
        ModeloDatosPrueba datos = ModeloDatosPrueba.builder()
                .casoPrueba("REG_003")
                .descripcion("Validación de contraseñas diferentes")
                .nombre("Usuario")
                .email("usuario.passwords." + System.currentTimeMillis() + "@test.com")
                .password("Password123!")
                .confirmacionPassword("DiferentePassword456!")
                .esValido(false)
                .aceptarTerminos(true)
                .mensajeError("Passwords do not match")
                .build();
        
        logPasoPrueba("Ejecutando registro con contraseñas diferentes");
        
        boolean registroFallido = paginaRegistro.registrarUsuario(datos);
        
        logValidacion("Verificando que el registro falló por contraseñas diferentes");
        Assert.assertTrue(registroFallido, "El registro debería fallar cuando las contraseñas no coinciden");
        
        String mensajeError = paginaRegistro.obtenerMensajeError();
        logValidacion("Mensaje de error obtenido: " + mensajeError);
        
        capturarPantalla("validacion_passwords_diferentes");
        logger.info(TipoMensaje.EXITO.formatearMensaje("Test de validación de contraseñas diferentes completado"));
    }
    
    @Test(priority = 4,
          description = "Verificar validación de campos obligatorios vacíos",
          groups = {"regression", "registro", "negativo", "validacion"})
    @Story("Validación de Campos Obligatorios")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifica que el sistema requiere todos los campos obligatorios")
    public void testValidacionCamposVacios() {
        
        logPasoPrueba("Preparando datos con campos vacíos");
        
        ModeloDatosPrueba datos = ModeloDatosPrueba.builder()
                .casoPrueba("REG_004")
                .descripcion("Validación de campos obligatorios vacíos")
                .nombre("")
                .email("")
                .password("")
                .confirmacionPassword("")
                .esValido(false)
                .aceptarTerminos(false)
                .mensajeError("Please fill out this field")
                .build();
        
        logPasoPrueba("Ejecutando registro con campos vacíos");
        
        boolean registroFallido = paginaRegistro.registrarUsuario(datos);
        
        logValidacion("Verificando que el registro falló por campos vacíos");
        Assert.assertTrue(registroFallido, "El registro debería fallar con campos obligatorios vacíos");
        
        capturarPantalla("validacion_campos_vacios");
        logger.info(TipoMensaje.EXITO.formatearMensaje("Test de validación de campos vacíos completado"));
    }
    
    @Test(priority = 5,
          description = "Verificar elementos de la página de registro",
          groups = {"smoke", "registro", "ui"})
    @Story("Elementos de UI")
    @Severity(SeverityLevel.MINOR)
    @Description("Verifica que todos los elementos necesarios están presentes en la página")
    public void testElementosPaginaRegistro() {
        
        logPasoPrueba("Verificando elementos básicos de la página");
        
        Assert.assertTrue(paginaRegistro.esPaginaVisible(), "La página de registro debe estar visible");
        
        logValidacion("Validando elementos específicos del formulario");
        paginaRegistro.validarElementosPagina();
        
        String titulo = paginaRegistro.obtenerTituloPagina();
        logValidacion("Título de página obtenido: " + titulo);
        Assert.assertFalse(titulo.isEmpty(), "El título de la página no debe estar vacío");
        
        capturarPantalla("elementos_pagina_registro");
        logger.info(TipoMensaje.EXITO.formatearMensaje("Test de elementos de página completado"));
    }
    
    @Test(priority = 6,
          dataProvider = "datosRegistroValidos", 
          dataProviderClass = ProveedorDatos.class,
          description = "Verificar registro con múltiples datos válidos",
          groups = {"regression", "registro", "positivo", "datadriven"})
    @Story("Registro con Datos Múltiples")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifica registro con diferentes conjuntos de datos válidos")
    public void testRegistroMultiplesDatosValidos(ModeloDatosPrueba datos) {
        
        logPasoPrueba("Ejecutando registro con datos: " + datos.getCasoPrueba());
        
        boolean registroExitoso = paginaRegistro.registrarUsuario(datos);
        
        logValidacion("Verificando resultado del registro para: " + datos.getCasoPrueba());
        Assert.assertTrue(registroExitoso, "El registro debería ser exitoso para: " + datos.getCasoPrueba());
        
        capturarPantalla("registro_multiple_" + datos.getCasoPrueba());
    }
    
    @Test(priority = 7,
          dataProvider = "datosRegistroInvalidos", 
          dataProviderClass = ProveedorDatos.class,
          description = "Verificar validación con múltiples datos inválidos",
          groups = {"regression", "registro", "negativo", "datadriven"})
    @Story("Validación con Datos Múltiples")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifica validación con diferentes conjuntos de datos inválidos")
    public void testValidacionMultiplesDatosInvalidos(ModeloDatosPrueba datos) {
        
        logPasoPrueba("Ejecutando validación con datos: " + datos.getCasoPrueba());
        
        boolean registroFallido = paginaRegistro.registrarUsuario(datos);
        
        logValidacion("Verificando que falló como esperado para: " + datos.getCasoPrueba());
        Assert.assertTrue(registroFallido, "El registro debería fallar para: " + datos.getCasoPrueba());
        
        capturarPantalla("validacion_multiple_" + datos.getCasoPrueba());
    }
    
    // Método auxiliar para evitar errores de compilación con By
    private void verificarElementoPorId(String id) {
        try {
            driver.findElement(By.id(id));
            logValidacion("Elemento encontrado: " + id);
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("Elemento no encontrado: " + id));
        }
    }
}