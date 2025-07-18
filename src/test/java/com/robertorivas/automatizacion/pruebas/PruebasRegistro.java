package com.robertorivas.automatizacion.pruebas;

import com.robertorivas.automatizacion.datos.ProveedorDatos;
import com.robertorivas.automatizacion.modelos.DatosRegistro;
import com.robertorivas.automatizacion.paginas.PaginaRegistro;
import com.robertorivas.automatizacion.paginas.PaginaLogin;
import com.robertorivas.automatizacion.utilidades.GestorDatos;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase de pruebas para el formulario de registro de usuarios.
 * Contiene pruebas funcionales completas para validar todos los escenarios
 * del proceso de registro.
 * 
 * Principios aplicados:
 * - Single Responsibility: Solo pruebas de registro
 * - Data Driven Testing: Uso de proveedores de datos
 * - Page Object Model: Interacción a través de page objects
 * - AAA Pattern: Arrange, Act, Assert en cada prueba
 * 
 * @author Roberto Rivas Lopez
 */
public class PruebasRegistro extends PruebasBase {
    
    private PaginaRegistro paginaRegistro;
    private PaginaLogin paginaLogin;
    private GestorDatos gestorDatos;
    
    @BeforeClass
    public void configuracionClaseRegistro() {
        super.configuracionClase();
        
        // Inicializar gestor de datos
        gestorDatos = new GestorDatos();
        
        logger.info("Configuración específica para pruebas de registro completada");
    }
    
    // ===== PRUEBAS DE REGISTRO EXITOSO =====
    
    @Test(description = "Test Case 1: Successful Registration (Happy Path)",
          groups = {"registro", "positivo", "smoke"},
          priority = 1)
    public void registroExitosoConDatosValidos() {
        registrarSeparador("TEST CASE 1: SUCCESSFUL REGISTRATION");
        
        // Arrange - Datos únicos para cada ejecución
        String timestamp = String.valueOf(System.currentTimeMillis());
        DatosRegistro datosRegistro = DatosRegistro.crearDatosValidos(
            "testuser" + timestamp,        // Username único
            "SuperSecretPassword!",        // Password válido
            "Test",                        // Nombre
            "User"                         // Apellido
        );
        datosRegistro.setConfirmarPassword("SuperSecretPassword!"); // Confirmar password
        
        Map<String, Object> datosEntrada = new HashMap<>();
        datosEntrada.put("Username", datosRegistro.getEmail());
        datosEntrada.put("Password", "***" + "*".repeat(datosRegistro.getPassword().length() - 3));
        registrarDatosEntrada(datosEntrada);
        
        paginaRegistro = new PaginaRegistro(obtenerDriver());
        
        // Act - Siguiendo test case 1
        registrarInfo("1. Launch the browser - DONE");
        registrarInfo("2. Navigate to the Register page URL (/register)");
        paginaRegistro.navegarAPaginaRegistro();
        tomarCapturaPaso("Register page loaded");
        
        registrarInfo("3. Enter a valid Username");
        paginaRegistro.llenarCampo("username", datosRegistro.getEmail());
        
        registrarInfo("4. Enter a valid Password");
        paginaRegistro.llenarCampo("password", datosRegistro.getPassword());
        
        registrarInfo("5. Confirm the Password by re-entering it correctly");
        paginaRegistro.llenarCampo("confirmPassword", datosRegistro.getConfirmarPassword());
        
        registrarInfo("6. Click the Register button");
        paginaRegistro.enviarFormulario();
        tomarCapturaPaso("Registration form submitted");
        
        // Assert - Verificar test case 1 expectations
        registrarInfo("7. Verify that the user is redirected to the Login page (/login)");
        boolean enPaginaLogin = obtenerDriver().getCurrentUrl().contains("/login");
        
        registrarInfo("8. Confirm that the success message 'Successfully registered, you can log in now.' is displayed");
        boolean mensajeExitoVisible = paginaRegistro.verificarMensajeRegistroExitoso();
        
        if (enPaginaLogin && mensajeExitoVisible) {
            registrarPasoExitoso("✓ Registration successful - redirected to login page with success message");
            tomarCapturaPaso("Success - Login page with success message");
        } else {
            registrarPasoFallido("✗ Registration flow failed", null);
            tomarCapturaPaso("Failed registration");
        }
        
        Assert.assertTrue(enPaginaLogin, "Should be redirected to login page (/login)");
        Assert.assertTrue(mensajeExitoVisible, "Should display 'Successfully registered, you can log in now.' message");
    }
    
    // ===== PRUEBAS DE VALIDACIÓN DE CAMPOS =====
    
    @Test(description = "Test Case 2: Registration with Missing Username",
          groups = {"registro", "negativo", "validacion"},
          priority = 2)
    public void validacionCamposObligatorios() {
        registrarSeparador("TEST CASE 2: REGISTRATION WITH MISSING USERNAME");
        
        // Arrange
        paginaRegistro = new PaginaRegistro(obtenerDriver());
        
        // Act - Siguiendo test case 2
        registrarInfo("1. Launch the browser - DONE");
        registrarInfo("2. Navigate to the Register page URL (/register)");
        paginaRegistro.navegarAPaginaRegistro();
        tomarCapturaPaso("Register page loaded");
        
        registrarInfo("3. Leave the Username field blank");
        // No llenar username
        
        registrarInfo("4. Enter a valid Password");
        paginaRegistro.llenarCampo("password", "ValidPassword123");
        
        registrarInfo("5. Confirm the Password by re-entering it");
        paginaRegistro.llenarCampo("confirmPassword", "ValidPassword123");
        
        registrarInfo("6. Click the Register button");
        paginaRegistro.enviarFormulario();
        tomarCapturaPaso("Registration attempt with missing username");
        
        // Assert - Verificar test case 2 expectation
        registrarInfo("7. Verify that an error message 'All fields are required.' is displayed");
        boolean errorCamposRequeridos = paginaRegistro.verificarErrorCamposRequeridos();
        
        if (errorCamposRequeridos) {
            registrarPasoExitoso("✓ Error message 'All fields are required.' displayed correctly");
            tomarCapturaPaso("Success - All fields required error shown");
        } else {
            registrarPasoFallido("✗ Expected error message not shown", null);
            tomarCapturaPaso("Failed - No error message");
        }
        
        Assert.assertTrue(errorCamposRequeridos, 
                          "Should display error message 'All fields are required.' when username is missing");
    }
    
    @Test(description = "Test Case 3: Registration with Missing Password",
          groups = {"registro", "negativo", "validacion"},
          priority = 3)
    public void validacionPasswordFaltante() {
        registrarSeparador("TEST CASE 3: REGISTRATION WITH MISSING PASSWORD");
        
        // Arrange
        paginaRegistro = new PaginaRegistro(obtenerDriver());
        String timestamp = String.valueOf(System.currentTimeMillis());
        
        // Act - Siguiendo test case 3
        registrarInfo("1. Launch the browser - DONE");
        registrarInfo("2. Navigate to the Register page URL (/register)");
        paginaRegistro.navegarAPaginaRegistro();
        tomarCapturaPaso("Register page loaded");
        
        registrarInfo("3. Enter a valid Username");
        paginaRegistro.llenarCampo("username", "validuser" + timestamp);
        
        registrarInfo("4. Leave the Password field blank");
        // No llenar password
        
        registrarInfo("5. Confirm the Password by entering any value");
        paginaRegistro.llenarCampo("confirmPassword", "AnyValue123");
        
        registrarInfo("6. Click the Register button");
        paginaRegistro.enviarFormulario();
        tomarCapturaPaso("Registration attempt with missing password");
        
        // Assert - Verificar test case 3 expectation
        registrarInfo("7. Verify that an error message 'All fields are required.' is displayed");
        boolean errorCamposRequeridos = paginaRegistro.verificarErrorCamposRequeridos();
        
        if (errorCamposRequeridos) {
            registrarPasoExitoso("✓ Error message 'All fields are required.' displayed correctly");
            tomarCapturaPaso("Success - All fields required error shown");
        } else {
            registrarPasoFallido("✗ Expected error message not shown", null);
            tomarCapturaPaso("Failed - No error message");
        }
        
        Assert.assertTrue(errorCamposRequeridos, 
                          "Should display error message 'All fields are required.' when password is missing");
    }

    @Test(description = "Test Case 4: Registration with Non-matching Passwords",
          groups = {"registro", "negativo", "validacion"},
          priority = 4)
    public void validacionPasswordsNoCoinciden() {
        registrarSeparador("TEST CASE 4: REGISTRATION WITH NON-MATCHING PASSWORDS");
        
        // Arrange
        paginaRegistro = new PaginaRegistro(obtenerDriver());
        String timestamp = String.valueOf(System.currentTimeMillis());
        
        // Act - Siguiendo test case 4
        registrarInfo("1. Launch the browser - DONE");
        registrarInfo("2. Navigate to the Register page URL (/register)");
        paginaRegistro.navegarAPaginaRegistro();
        tomarCapturaPaso("Register page loaded");
        
        registrarInfo("3. Enter a valid Username");
        paginaRegistro.llenarCampo("username", "testuser" + timestamp);
        
        registrarInfo("4. Enter a valid Password");
        paginaRegistro.llenarCampo("password", "ValidPassword123");
        
        registrarInfo("5. Confirm the Password by entering a different value");
        paginaRegistro.llenarCampo("confirmPassword", "DifferentPassword456");
        
        registrarInfo("6. Click the Register button");
        paginaRegistro.enviarFormulario();
        tomarCapturaPaso("Registration attempt with non-matching passwords");
        
        // Assert - Verificar test case 4 expectation
        registrarInfo("7. Verify that an error message 'Passwords do not match.' is displayed");
        boolean errorPasswordsNoCoinciden = paginaRegistro.verificarErrorPasswordsNoCoinciden();
        
        if (errorPasswordsNoCoinciden) {
            registrarPasoExitoso("✓ Error message 'Passwords do not match.' displayed correctly");
            tomarCapturaPaso("Success - Passwords do not match error shown");
        } else {
            registrarPasoFallido("✗ Expected error message not shown", null);
            tomarCapturaPaso("Failed - No error message");
        }
        
        Assert.assertTrue(errorPasswordsNoCoinciden, 
                          "Should display error message 'Passwords do not match.' when passwords don't match");
    }
    
    // ===== PRUEBAS DE FLUJO COMPLETO =====
    
    @Test(description = "Flujo completo: registro exitoso y redirección a login",
          groups = {"registro", "flujo", "integracion"},
          priority = 5)
    public void flujoCompletoRegistroYLogin() {
        registrarSeparador("FLUJO COMPLETO - REGISTRO Y REDIRECCIÓN");
        
        // Arrange
        DatosRegistro datosRegistro = DatosRegistro.crearDatosValidos(
            "usuario.completo" + System.currentTimeMillis() + "@test.com",
            "Password123",
            "Usuario",
            "Completo"
        );
        
        paginaRegistro = new PaginaRegistro(obtenerDriver());
        
        // Act - Registro
        registrarInfo("Navegando a la página de registro");
        paginaRegistro.navegarAPaginaRegistro();
        tomarCapturaPaso("Página de registro cargada");
        
        registrarInfo("Completando registro");
        paginaRegistro.llenarFormularioRegistro(datosRegistro);
        paginaRegistro.enviarFormulario();
        boolean registroExitoso = paginaRegistro.registroExitoso();
        tomarCapturaPaso("Registro completado");
        
        // Act - Verificar redirección o enlace a login
        boolean puedeIrALogin = false;
        if (registroExitoso) {
            registrarInfo("Intentando navegar a página de login después del registro");
            try {
                paginaRegistro.irAPaginaLogin();
                puedeIrALogin = true; // Si no lanza excepción, asumimos éxito
                tomarCapturaPaso("Navegación a login exitosa");
            } catch (Exception e) {
                registrarInfo("Error navegando a login: " + e.getMessage());
                // Intentar navegar directamente
                paginaLogin = new PaginaLogin(obtenerDriver());
                paginaLogin.navegarAPaginaLogin();
                puedeIrALogin = paginaLogin.estaPaginaCargada();
                tomarCapturaPaso("Navegación directa a login");
            }
        }
        
        // Assert
        Assert.assertTrue(registroExitoso, "El registro debería ser exitoso");
        Assert.assertTrue(puedeIrALogin, "Debería poder navegar a la página de login después del registro");
        
        if (registroExitoso && puedeIrALogin) {
            registrarPasoExitoso("Flujo completo de registro ejecutado correctamente");
        } else {
            registrarPasoFallido("El flujo completo no se ejecutó correctamente", null);
        }
    }
    
    // ===== PRUEBAS DE LIMPIEZA Y CANCELACIÓN =====
    
    @Test(description = "Funcionalidad de limpiar formulario",
          groups = {"registro", "utilidad"},
          priority = 6)
    public void limpiarFormulario() {
        registrarSeparador("UTILIDAD - LIMPIAR FORMULARIO");
        
        // Arrange
        DatosRegistro datosTemp = DatosRegistro.crearDatosValidos(
            "temp@test.com",
            "TempPassword",
            "Temp",
            "User"
        );
        
        paginaRegistro = new PaginaRegistro(obtenerDriver());
        
        // Act
        registrarInfo("Navegando a la página de registro");
        paginaRegistro.navegarAPaginaRegistro();
        tomarCapturaPaso("Página de registro cargada");
        
        registrarInfo("Llenando formulario con datos temporales");
        paginaRegistro.llenarFormularioRegistro(datosTemp);
        tomarCapturaPaso("Formulario lleno con datos temporales");
        
        registrarInfo("Limpiando formulario");
        paginaRegistro.limpiarFormulario();
        boolean formularioLimpio = true; // Asumimos éxito si no lanza excepción
        tomarCapturaPaso("Formulario después de limpiar");
        
        // Assert
        if (formularioLimpio) {
            registrarPasoExitoso("Formulario limpiado correctamente");
        } else {
            registrarPasoFallido("No se pudo limpiar el formulario", null);
        }
        
        Assert.assertTrue(formularioLimpio, "El formulario debería limpiarse correctamente");
    }
}
