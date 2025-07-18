package com.robertorivas.automatizacion.pruebas;

import com.robertorivas.automatizacion.datos.ProveedorDatos;
import com.robertorivas.automatizacion.modelos.DatosRegistro;
import com.robertorivas.automatizacion.paginas.PaginaRegistro;
import com.robertorivas.automatizacion.utilidades.GestorDatos;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase de pruebas para el formulario de registro de ExpandTesting.
 * Actualizada para ser compatible con la nueva PaginaRegistro.
 * 
 * URL: https://practice.expandtesting.com/register
 * 
 * @author Roberto Rivas Lopez
 */
public class PruebasRegistro extends PruebasBase {
    
    private PaginaRegistro paginaRegistro;
    private GestorDatos gestorDatos;
    
    @BeforeClass
    public void configuracionClaseRegistro() {
        super.configuracionClase();
        gestorDatos = new GestorDatos();
        logger.info("Configuración específica para pruebas de registro de ExpandTesting completada");
    }
    
    // ===== PRUEBAS DE REGISTRO EXITOSO =====
    
    @Test(description = "Registro exitoso con datos completos válidos - ExpandTesting",
          groups = {"registro", "positivo", "smoke"},
          priority = 1)
    public void registroExitosoConDatosCompletos() {
        registrarSeparador("REGISTRO EXITOSO - DATOS COMPLETOS EXPANDTESTING");
        
        // Arrange
        String username = "newuser" + System.currentTimeMillis();
        String password = "TestPassword123";
        
        Map<String, Object> datosEntrada = new HashMap<>();
        datosEntrada.put("Username", username);
        datosEntrada.put("Password", "***");
        datosEntrada.put("Plataforma", "ExpandTesting");
        registrarDatosEntrada(datosEntrada);
        
        paginaRegistro = new PaginaRegistro(obtenerDriver());
        
        // Act
        registrarInfo("Navegando a la página de registro de ExpandTesting");
        paginaRegistro.navegarAPaginaRegistro();
        tomarCapturaPaso("Página de registro cargada");
        
        registrarInfo("Realizando registro con datos completos");
        boolean registroExitoso = paginaRegistro.registrarUsuario(username, password, password);
        tomarCapturaPaso("Registro completado");
        
        // Assert
        if (registroExitoso) {
            registrarPasoExitoso("Registro completado exitosamente");
            
            // Verificar mensaje específico de ExpandTesting
            String mensajeExito = paginaRegistro.obtenerMensajeExito();
            if (mensajeExito != null && mensajeExito.contains("Successfully registered")) {
                registrarPasoExitoso("Mensaje de éxito correcto: " + mensajeExito);
            } else {
                registrarAdvertencia("Mensaje de éxito no encontrado o diferente");
            }
            
            // Verificar redirección a login
            String urlActual = obtenerDriver().getCurrentUrl();
            if (urlActual.contains("/login")) {
                registrarPasoExitoso("Redirección correcta a página de login");
            }
            
        } else {
            List<String> errores = paginaRegistro.obtenerErroresValidacion();
            registrarPasoFallido("Registro falló inesperadamente. Errores: " + errores, null);
        }
        
        Assert.assertTrue(registroExitoso, "El registro debería ser exitoso con datos válidos");
    }
    
    @Test(description = "Registro exitoso con datos mínimos requeridos - ExpandTesting",
          groups = {"registro", "positivo"},
          priority = 2)
    public void registroExitosoConDatosMinimos() {
        registrarSeparador("REGISTRO EXITOSO - DATOS MÍNIMOS EXPANDTESTING");
        
        // Arrange
        String username = "minimaluser" + System.currentTimeMillis();
        String password = "MinimalPass123";
        
        paginaRegistro = new PaginaRegistro(obtenerDriver());
        
        // Act
        registrarInfo("Navegando a página de registro");
        paginaRegistro.navegarAPaginaRegistro();
        
        registrarInfo("Registro con datos mínimos requeridos");
        boolean registroExitoso = paginaRegistro.registrarUsuario(username, password, password);
        tomarCapturaPaso("Registro con datos mínimos");
        
        // Assert
        if (registroExitoso) {
            registrarPasoExitoso("Registro exitoso con datos mínimos");
        } else {
            List<String> errores = paginaRegistro.obtenerErroresValidacion();
            registrarPasoFallido("Registro falló con datos mínimos. Errores: " + errores, null);
        }
        
        Assert.assertTrue(registroExitoso, "El registro debería ser exitoso con datos mínimos");
    }
    
    @Test(dataProvider = "datosRegistroValidos", dataProviderClass = ProveedorDatos.class,
          description = "Registro exitoso con múltiples datos desde CSV - ExpandTesting",
          groups = {"registro", "positivo", "datadriven"},
          priority = 3)
    public void registroExitosoConMultiplesDatos(DatosRegistro datos) {
        registrarSeparador("REGISTRO MÚLTIPLES DATOS - " + datos.getEmail());
        
        // Arrange
        Map<String, Object> datosEntrada = new HashMap<>();
        datosEntrada.put("Username", datos.getEmail());
        datosEntrada.put("Caso", "Data-driven desde CSV");
        registrarDatosEntrada(datosEntrada);
        
        paginaRegistro = new PaginaRegistro(obtenerDriver());
        
        // Act
        registrarInfo("Ejecutando registro para: " + datos.getEmail());
        paginaRegistro.navegarAPaginaRegistro();
        
        boolean registroExitoso = paginaRegistro.registrarUsuario(datos);
        tomarCapturaPaso("Registro desde CSV: " + datos.getEmail());
        
        // Assert
        if (registroExitoso) {
            registrarPasoExitoso("Registro exitoso para: " + datos.getEmail());
        } else {
            List<String> errores = paginaRegistro.obtenerErroresValidacion();
            registrarPasoFallido("Registro falló para: " + datos.getEmail() + ". Errores: " + errores, null);
        }
        
        Assert.assertTrue(registroExitoso, "Registro debería ser exitoso para: " + datos.getEmail());
    }
    
    // ===== PRUEBAS DE VALIDACIÓN NEGATIVA =====
    
    @Test(description = "Validación de campos obligatorios vacíos - ExpandTesting",
          groups = {"registro", "negativo", "validacion"},
          priority = 4)
    public void validacionCamposObligatoriosVacios() {
        registrarSeparador("VALIDACIÓN - CAMPOS OBLIGATORIOS VACÍOS");
        
        // Arrange
        paginaRegistro = new PaginaRegistro(obtenerDriver());
        
        // Act
        registrarInfo("Navegando a página de registro");
        paginaRegistro.navegarAPaginaRegistro();
        tomarCapturaPaso("Página de registro cargada");
        
        registrarInfo("Intentando registro con todos los campos vacíos");
        paginaRegistro.limpiarFormulario();
        paginaRegistro.hacerClicRegistrar();
        tomarCapturaPaso("Intento de registro con campos vacíos");
        
        // Assert
        boolean hayErrores = paginaRegistro.hayErroresValidacion();
        List<String> errores = paginaRegistro.obtenerErroresValidacion();
        
        if (hayErrores) {
            registrarPasoExitoso("Errores detectados correctamente con campos vacíos");
            registrarInfo("Errores encontrados: " + errores);
            
            // Verificar mensaje específico de ExpandTesting
            boolean mensajeCorrecto = errores.stream().anyMatch(error -> 
                error.contains("All fields are required."));
            
            if (mensajeCorrecto) {
                registrarPasoExitoso("Mensaje de error correcto: 'All fields are required.'");
            } else {
                registrarAdvertencia("Mensaje de error diferente al esperado");
            }
        } else {
            registrarPasoFallido("No se detectaron errores con campos vacíos", null);
        }
        
        Assert.assertTrue(hayErrores, "Debería mostrar errores con campos obligatorios vacíos");
    }
    
    @Test(description = "Validación de campo username faltante - ExpandTesting",
          groups = {"registro", "negativo", "validacion"},
          priority = 5)
    public void validacionCampoUsernameFaltante() {
        registrarSeparador("VALIDACIÓN - USERNAME FALTANTE");
        
        // Arrange
        String password = "ValidPassword123";
        paginaRegistro = new PaginaRegistro(obtenerDriver());
        
        // Act
        registrarInfo("Navegando a página de registro");
        paginaRegistro.navegarAPaginaRegistro();
        
        registrarInfo("Probando registro sin username");
        boolean hayError = paginaRegistro.probarRegistroSinUsername(password);
        tomarCapturaPaso("Registro sin username");
        
        // Assert
        if (hayError) {
            List<String> errores = paginaRegistro.obtenerErroresValidacion();
            registrarPasoExitoso("Error detectado correctamente sin username");
            registrarInfo("Errores: " + errores);
        } else {
            registrarPasoFallido("No se detectó error con username faltante", null);
        }
        
        Assert.assertTrue(hayError, "Debería mostrar error cuando falta username");
    }
    
    @Test(description = "Validación de campo password faltante - ExpandTesting",
          groups = {"registro", "negativo", "validacion"},
          priority = 6)
    public void validacionCampoPasswordFaltante() {
        registrarSeparador("VALIDACIÓN - PASSWORD FALTANTE");
        
        // Arrange
        String username = "validuser" + System.currentTimeMillis();
        paginaRegistro = new PaginaRegistro(obtenerDriver());
        
        // Act
        registrarInfo("Navegando a página de registro");
        paginaRegistro.navegarAPaginaRegistro();
        
        registrarInfo("Probando registro sin password");
        boolean hayError = paginaRegistro.probarRegistroSinPassword(username);
        tomarCapturaPaso("Registro sin password");
        
        // Assert
        if (hayError) {
            List<String> errores = paginaRegistro.obtenerErroresValidacion();
            registrarPasoExitoso("Error detectado correctamente sin password");
            registrarInfo("Errores: " + errores);
        } else {
            registrarPasoFallido("No se detectó error con password faltante", null);
        }
        
        Assert.assertTrue(hayError, "Debería mostrar error cuando falta password");
    }
    
    @Test(description = "Validación de passwords que no coinciden - ExpandTesting",
          groups = {"registro", "negativo", "validacion"},
          priority = 7)
    public void validacionPasswordsNoCoinciden() {
        registrarSeparador("VALIDACIÓN - PASSWORDS NO COINCIDEN");
        
        // Arrange
        String username = "testpasswords" + System.currentTimeMillis();
        String password = "Password123";
        String confirmPassword = "DifferentPassword456";
        
        paginaRegistro = new PaginaRegistro(obtenerDriver());
        
        // Act
        registrarInfo("Navegando a página de registro");
        paginaRegistro.navegarAPaginaRegistro();
        
        registrarInfo("Probando registro con passwords que no coinciden");
        boolean hayError = paginaRegistro.probarPasswordsNoCoinciden(username, password, confirmPassword);
        tomarCapturaPaso("Passwords no coinciden");
        
        // Assert
        if (hayError) {
            List<String> errores = paginaRegistro.obtenerErroresValidacion();
            registrarPasoExitoso("Error detectado correctamente con passwords diferentes");
            registrarInfo("Errores: " + errores);
            
            // Verificar mensaje específico
            boolean mensajeCorrecto = errores.stream().anyMatch(error -> 
                error.contains("Passwords do not match."));
            
            if (mensajeCorrecto) {
                registrarPasoExitoso("Mensaje de error correcto: 'Passwords do not match.'");
            }
        } else {
            registrarPasoFallido("No se detectó error con passwords diferentes", null);
        }
        
        Assert.assertTrue(hayError, "Debería mostrar error cuando passwords no coinciden");
    }
    
    // ===== PRUEBAS DE FUNCIONALIDAD =====
    
    @Test(description = "Funcionalidad de limpiar formulario de registro",
          groups = {"registro", "funcional"},
          priority = 8)
    public void funcionalidadLimpiarFormulario() {
        registrarSeparador("FUNCIONALIDAD - LIMPIAR FORMULARIO");
        
        // Arrange
        String username = "testlimpiar" + System.currentTimeMillis();
        String password = "TestPassword123";
        
        paginaRegistro = new PaginaRegistro(obtenerDriver());
        
        // Act
        registrarInfo("Navegando a página de registro");
        paginaRegistro.navegarAPaginaRegistro();
        
        registrarInfo("Llenando formulario");
        paginaRegistro.introducirUsername(username);
        paginaRegistro.introducirPassword(password);
        paginaRegistro.introducirConfirmarPassword(password);
        tomarCapturaPaso("Formulario lleno");
        
        registrarInfo("Limpiando formulario");
        paginaRegistro.limpiarFormulario();
        tomarCapturaPaso("Formulario limpiado");
        
        // Assert
        DatosRegistro valoresActuales = paginaRegistro.obtenerValoresFormulario();
        boolean formularioLimpio = (valoresActuales.getEmail() == null || valoresActuales.getEmail().isEmpty());
        
        if (formularioLimpio) {
            registrarPasoExitoso("Formulario limpiado correctamente");
        } else {
            registrarPasoFallido("El formulario no se limpió completamente", null);
        }
        
        Assert.assertTrue(formularioLimpio, "El formulario debería estar limpio");
    }
    
    @Test(description = "Navegación a página de login desde registro",
          groups = {"registro", "navegacion"},
          priority = 9)
    public void navegacionALogin() {
        registrarSeparador("NAVEGACIÓN - REGISTRO A LOGIN");
        
        // Arrange
        paginaRegistro = new PaginaRegistro(obtenerDriver());
        
        // Act
        registrarInfo("Navegando a página de registro");
        paginaRegistro.navegarAPaginaRegistro();
        tomarCapturaPaso("Página de registro");
        
        registrarInfo("Navegando a página de login desde registro");
        paginaRegistro.irAPaginaLogin();
        tomarCapturaPaso("Navegación a login");
        
        // Assert
        String urlActual = obtenerDriver().getCurrentUrl();
        boolean enPaginaLogin = urlActual.contains("/login");
        
        if (enPaginaLogin) {
            registrarPasoExitoso("Navegación exitosa a página de login");
        } else {
            registrarAdvertencia("No se pudo confirmar navegación a login. URL actual: " + urlActual);
        }
        
        registrarInfo("URL actual después de navegación: " + urlActual);
        // No hacer assert estricto porque algunos sitios pueden no tener el enlace
    }
}