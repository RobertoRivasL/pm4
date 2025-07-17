package com.robertorivas.automatizacion.pruebas;

import com.robertorivas.automatizacion.modelos.DatosRegistro;
import com.robertorivas.automatizacion.modelos.Usuario;
import com.robertorivas.automatizacion.paginas.PaginaLogin;
import com.robertorivas.automatizacion.paginas.PaginaPrincipal;
import com.robertorivas.automatizacion.paginas.PaginaRegistro;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase de pruebas de integración end-to-end.
 * Valida flujos completos que involucran múltiples páginas y funcionalidades.
 * 
 * Principios aplicados:
 * - Integration Testing: Pruebas de múltiples componentes trabajando juntos
 * - End-to-End Testing: Flujos completos de usuario
 * - Real User Scenarios: Casos de uso reales
 * - Cross-Component Validation: Validación entre diferentes componentes
 * 
 * @author Roberto Rivas Lopez
 */
public class PruebasIntegracion extends PruebasBase {
    
    private PaginaRegistro paginaRegistro;
    private PaginaLogin paginaLogin;
    private PaginaPrincipal paginaPrincipal;
    
    @BeforeClass
    public void configuracionClaseIntegracion() {
        super.configuracionClase();
        logger.info("Configuración específica para pruebas de integración completada");
    }
    
    // ===== PRUEBAS DE FLUJO COMPLETO =====
    
    @Test(description = "Flujo completo: Registro → Login → Navegación → Logout",
          groups = {"integracion", "flujo", "e2e", "smoke"},
          priority = 1)
    public void flujoCompletoRegistroLoginLogout() {
        registrarSeparador("FLUJO COMPLETO E2E - REGISTRO → LOGIN → LOGOUT");
        
        // Arrange
        DatosRegistro datosRegistro = new DatosRegistro.Builder()
                .email("integracion.completo@testautomation.com")
                .password("IntegracionPass123")
                .confirmarPassword("IntegracionPass123")
                .nombre("Usuario")
                .apellido("Integracion")
                .telefono("+34600123789")
                .aceptarTerminos()
                .recibirNotificaciones(true)
                .build();
        
        Map<String, Object> datosEntrada = new HashMap<>();
        datosEntrada.put("Email", datosRegistro.getEmail());
        datosEntrada.put("Nombre Completo", datosRegistro.getNombreCompleto());
        datosEntrada.put("Flujo", "Registro completo seguido de login y logout");
        registrarDatosEntrada(datosEntrada);
        
        // Act & Assert - Fase 1: Registro
        registrarInfo("=== FASE 1: REGISTRO DE NUEVO USUARIO ===");
        paginaRegistro = new PaginaRegistro(obtenerDriver());
        
        paginaRegistro.navegarAPaginaRegistro();
        tomarCapturaPaso("Página de registro cargada");
        
        paginaRegistro.llenarFormularioRegistro(datosRegistro);
        tomarCapturaPaso("Formulario de registro completado");
        
        paginaRegistro.enviarFormulario();
        tomarCapturaPaso("Formulario de registro enviado");
        
        boolean registroExitoso = paginaRegistro.registroExitoso();
        if (registroExitoso) {
            registrarPasoExitoso("Registro de usuario completado exitosamente");
        } else {
            registrarPasoFallido("Falló el registro del usuario", null);
            Assert.fail("El registro debería ser exitoso para continuar con el flujo");
        }
        
        // Act & Assert - Fase 2: Login con credenciales recién creadas
        registrarInfo("=== FASE 2: LOGIN CON CREDENCIALES RECIÉN CREADAS ===");
        paginaLogin = new PaginaLogin(obtenerDriver());
        
        // Navegar a login (podría ser automático después del registro)
        String urlActual = obtenerDriver().getCurrentUrl();
        if (!urlActual.contains("login") && !urlActual.contains("logged-in")) {
            paginaLogin.navegarAPaginaLogin();
        }
        tomarCapturaPaso("Navegación a login después del registro");
        
        Usuario usuarioCreado = new Usuario(datosRegistro.getEmail(), datosRegistro.getPassword());
        usuarioCreado.setNombre(datosRegistro.getNombre());
        usuarioCreado.setApellido(datosRegistro.getApellido());
        
        boolean loginExitoso = paginaLogin.iniciarSesion(usuarioCreado);
        tomarCapturaPaso("Intento de login con credenciales recién creadas");
        
        if (loginExitoso) {
            registrarPasoExitoso("Login exitoso con credenciales del usuario recién registrado");
        } else {
            registrarPasoFallido("Falló el login con credenciales recién creadas", null);
        }
        
        // Act & Assert - Fase 3: Verificación en página principal
        registrarInfo("=== FASE 3: VERIFICACIÓN EN PÁGINA PRINCIPAL ===");
        paginaPrincipal = new PaginaPrincipal(obtenerDriver());
        
        boolean usuarioLogueado = paginaPrincipal.usuarioLogueado();
        if (usuarioLogueado) {
            registrarPasoExitoso("Usuario correctamente autenticado en página principal");
            
            String nombreUsuarioMostrado = paginaPrincipal.obtenerNombreUsuarioLogueado();
            registrarInfo("Nombre de usuario mostrado: " + nombreUsuarioMostrado);
            
            String mensajeBienvenida = paginaPrincipal.obtenerMensajeBienvenida();
            if (mensajeBienvenida != null) {
                registrarInfo("Mensaje de bienvenida: " + mensajeBienvenida);
            }
            
            tomarCapturaPaso("Usuario logueado en página principal");
        } else {
            registrarPasoFallido("No se pudo verificar el estado de login en página principal", null);
        }
        
        // Act & Assert - Fase 4: Logout
        registrarInfo("=== FASE 4: LOGOUT Y FINALIZACIÓN ===");
        boolean logoutExitoso = paginaPrincipal.cerrarSesion();
        tomarCapturaPaso("Proceso de logout");
        
        if (logoutExitoso) {
            registrarPasoExitoso("Logout completado exitosamente");
            
            // Verificar que estamos fuera de la sesión
            boolean fueraDeSesion = !paginaPrincipal.usuarioLogueado();
            if (fueraDeSesion) {
                registrarPasoExitoso("FLUJO COMPLETO E2E EXITOSO: Registro → Login → Navegación → Logout");
            } else {
                registrarPasoFallido("El usuario sigue logueado después del logout", null);
            }
            
            Assert.assertTrue(fueraDeSesion, "El usuario debería estar fuera de sesión después del logout");
        } else {
            registrarPasoFallido("Falló el proceso de logout", null);
        }
        
        // Assertions finales
        Assert.assertTrue(registroExitoso, "El registro debería ser exitoso");
        Assert.assertTrue(loginExitoso, "El login debería ser exitoso");
        Assert.assertTrue(usuarioLogueado, "El usuario debería estar logueado");
        Assert.assertTrue(logoutExitoso, "El logout debería ser exitoso");
    }
    
    @Test(description = "Flujo de navegación entre páginas principales",
          groups = {"integracion", "navegacion", "funcional"},
          priority = 2)
    public void flujoNavegacionEntrePaginas() {
        registrarSeparador("FLUJO DE NAVEGACIÓN - PÁGINAS PRINCIPALES");
        
        // Arrange
        Map<String, Object> datosEntrada = new HashMap<>();
        datosEntrada.put("Objetivo", "Verificar navegación fluida entre páginas principales");
        datosEntrada.put("Páginas", "Registro → Login → Registro → Login");
        registrarDatosEntrada(datosEntrada);
        
        // Act & Assert - Navegación Registro → Login
        registrarInfo("=== NAVEGACIÓN: REGISTRO → LOGIN ===");
        paginaRegistro = new PaginaRegistro(obtenerDriver());
        
        paginaRegistro.navegarAPaginaRegistro();
        tomarCapturaPaso("Página de registro inicial");
        
        boolean paginaRegistroCargada = paginaRegistro.estaPaginaCargada();
        Assert.assertTrue(paginaRegistroCargada, "La página de registro debería estar cargada");
        
        paginaRegistro.irAPaginaLogin();
        tomarCapturaPaso("Navegación de registro a login");
        
        // Verificar que estamos en login
        paginaLogin = new PaginaLogin(obtenerDriver());
        boolean paginaLoginCargada = paginaLogin.estaPaginaCargada();
        
        if (paginaLoginCargada) {
            registrarPasoExitoso("Navegación exitosa: Registro → Login");
        } else {
            registrarPasoFallido("Falló la navegación de registro a login", null);
        }
        
        // Act & Assert - Navegación Login → Registro
        registrarInfo("=== NAVEGACIÓN: LOGIN → REGISTRO ===");
        paginaLogin.irAPaginaRegistro();
        tomarCapturaPaso("Navegación de login a registro");
        
        // Verificar que estamos de vuelta en registro
        boolean deVueltaEnRegistro = paginaRegistro.estaPaginaCargada();
        
        if (deVueltaEnRegistro) {
            registrarPasoExitoso("Navegación exitosa: Login → Registro");
        } else {
            registrarAdvertencia("No se pudo confirmar navegación de login a registro");
        }
        
        // Act & Assert - Navegación a recuperar contraseña
        registrarInfo("=== NAVEGACIÓN: LOGIN → RECUPERAR CONTRASEÑA ===");
        paginaLogin = new PaginaLogin(obtenerDriver());
        paginaLogin.navegarAPaginaLogin();
        
        paginaLogin.irARecuperarPassword();
        tomarCapturaPaso("Navegación a recuperar contraseña");
        
        String urlRecuperacion = obtenerDriver().getCurrentUrl();
        boolean enPaginaRecuperacion = urlRecuperacion.contains("forgot") || 
                                      urlRecuperacion.contains("recover") ||
                                      urlRecuperacion.contains("reset");
        
        if (enPaginaRecuperacion) {
            registrarPasoExitoso("Navegación exitosa: Login → Recuperar Contraseña");
        } else {
            registrarAdvertencia("No se pudo confirmar navegación a recuperar contraseña. URL: " + urlRecuperacion);
        }
        
        registrarInfo("Flujo de navegación completado");
        
        // Assertions principales
        Assert.assertTrue(paginaRegistroCargada, "La página de registro debe cargar correctamente");
        Assert.assertTrue(paginaLoginCargada, "La página de login debe cargar correctamente");
    }
    
    @Test(description = "Validación de persistencia de datos entre páginas",
          groups = {"integracion", "datos", "validacion"},
          priority = 3)
    public void validacionPersistenciaDatos() {
        registrarSeparador("VALIDACIÓN - PERSISTENCIA DE DATOS");
        
        // Arrange
        DatosRegistro datosOriginales = new DatosRegistro.Builder()
                .email("persistencia@testautomation.com")
                .password("PersistPass123")
                .confirmarPassword("PersistPass123")
                .nombre("Usuario")
                .apellido("Persistencia")
                .build();
        
        registrarDatosEntrada(Map.of(
            "Email", datosOriginales.getEmail(),
            "Objetivo", "Verificar que los datos se mantienen correctamente entre navegaciones"
        ));
        
        // Act & Assert - Llenar formulario y navegar
        registrarInfo("=== LLENADO INICIAL DE FORMULARIO ===");
        paginaRegistro = new PaginaRegistro(obtenerDriver());
        
        paginaRegistro.navegarAPaginaRegistro();
        paginaRegistro.llenarCamposBasicos(datosOriginales);
        tomarCapturaPaso("Formulario parcialmente llenado");
        
        // Obtener valores actuales
        DatosRegistro datosActuales = paginaRegistro.obtenerValoresFormulario();
        
        // Verificar que los datos se guardaron
        boolean emailGuardado = datosOriginales.getEmail().equals(datosActuales.getEmail());
        
        if (emailGuardado) {
            registrarPasoExitoso("Los datos se mantuvieron correctamente en el formulario");
        } else {
            registrarPasoFallido("Los datos no se persistieron correctamente", null);
            registrarInfo("Email original: " + datosOriginales.getEmail());
            registrarInfo("Email actual: " + datosActuales.getEmail());
        }
        
        // Act & Assert - Limpiar y verificar
        registrarInfo("=== LIMPIEZA Y VERIFICACIÓN ===");
        paginaRegistro.limpiarFormulario();
        tomarCapturaPaso("Formulario limpiado");
        
        DatosRegistro datosLimpiados = paginaRegistro.obtenerValoresFormulario();
        boolean formularioLimpio = (datosLimpiados.getEmail() == null || datosLimpiados.getEmail().isEmpty());
        
        if (formularioLimpio) {
            registrarPasoExitoso("Formulario limpiado correctamente");
        } else {
            registrarPasoFallido("El formulario no se limpió completamente", null);
        }
        
        Assert.assertTrue(emailGuardado, "Los datos deberían persistir en el formulario");
        Assert.assertTrue(formularioLimpio, "El formulario debería limpiarse correctamente");
    }
    
    @Test(description = "Prueba de compatibilidad cross-browser básica",
          groups = {"integracion", "crossbrowser", "compatibilidad"},
          priority = 4)
    public void pruebaCompatibilidadBasica() {
        registrarSeparador("COMPATIBILIDAD CROSS-BROWSER");
        
        // Esta prueba valida que las páginas se cargan correctamente
        // La configuración cross-browser real se maneja en TestNG XML
        
        Map<String, Object> datosEntrada = new HashMap<>();
        datosEntrada.put("Navegador Actual", config.obtenerNavegadorDefecto());
        datosEntrada.put("Modo Headless", config.esNavegadorHeadless());
        datosEntrada.put("Objetivo", "Verificar compatibilidad básica del navegador");
        registrarDatosEntrada(datosEntrada);
        
        // Act & Assert - Cargar página de registro
        registrarInfo("=== VERIFICACIÓN DE PÁGINA DE REGISTRO ===");
        paginaRegistro = new PaginaRegistro(obtenerDriver());
        paginaRegistro.navegarAPaginaRegistro();
        tomarCapturaPaso("Página de registro en " + config.obtenerNavegadorDefecto());
        
        boolean registroCargado = paginaRegistro.estaPaginaCargada();
        String tituloRegistro = obtenerDriver().getTitle();
        
        if (registroCargado) {
            registrarPasoExitoso("Página de registro carga correctamente");
            registrarInfo("Título de página: " + tituloRegistro);
        } else {
            registrarPasoFallido("La página de registro no cargó correctamente", null);
        }
        
        // Act & Assert - Cargar página de login
        registrarInfo("=== VERIFICACIÓN DE PÁGINA DE LOGIN ===");
        paginaLogin = new PaginaLogin(obtenerDriver());
        paginaLogin.navegarAPaginaLogin();
        tomarCapturaPaso("Página de login en " + config.obtenerNavegadorDefecto());
        
        boolean loginCargado = paginaLogin.estaPaginaCargada();
        String tituloLogin = obtenerDriver().getTitle();
        
        if (loginCargado) {
            registrarPasoExitoso("Página de login carga correctamente");
            registrarInfo("Título de página: " + tituloLogin);
        } else {
            registrarPasoFallido("La página de login no cargó correctamente", null);
        }
        
        // Verificar que el formulario de login funciona básicamente
        registrarInfo("=== VERIFICACIÓN FUNCIONAL BÁSICA ===");
        boolean formularioDisponible = paginaLogin.formularioLoginDisponible();
        
        if (formularioDisponible) {
            registrarPasoExitoso("Formulario de login está disponible y funcional");
        } else {
            registrarPasoFallido("El formulario de login no está disponible", null);
        }
        
        // Assertions
        Assert.assertTrue(registroCargado, "La página de registro debe cargar en " + config.obtenerNavegadorDefecto());
        Assert.assertTrue(loginCargado, "La página de login debe cargar en " + config.obtenerNavegadorDefecto());
        Assert.assertTrue(formularioDisponible, "El formulario debe estar funcional en " + config.obtenerNavegadorDefecto());
        
        registrarInfo("Prueba de compatibilidad básica completada para: " + config.obtenerNavegadorDefecto());
    }
    
    @Test(description = "Flujo de manejo de errores y recuperación",
          groups = {"integracion", "errores", "robustez"},
          priority = 5)
    public void flujoManejoErroresYRecuperacion() {
        registrarSeparador("MANEJO DE ERRORES Y RECUPERACIÓN");
        
        // Este test valida que la aplicación maneja errores graciosamente
        // y permite recuperación después de errores
        
        registrarDatosEntrada(Map.of(
            "Objetivo", "Verificar manejo robusto de errores y capacidad de recuperación",
            "Escenarios", "Datos inválidos → Corrección → Éxito"
        ));
        
        // Act & Assert - Provocar error en registro
        registrarInfo("=== PROVOCAR ERROR EN REGISTRO ===");
        paginaRegistro = new PaginaRegistro(obtenerDriver());
        paginaRegistro.navegarAPaginaRegistro();
        
        // Datos inválidos que deberían provocar error
        DatosRegistro datosInvalidos = new DatosRegistro.Builder()
                .email("email-invalido-sin-arroba.com")
                .password("123") // Muy corto
                .confirmarPassword("456") // No coincide
                .nombre("") // Vacío
                .apellido("") // Vacío
                .build(); // Sin aceptar términos
        
        paginaRegistro.llenarFormularioRegistro(datosInvalidos);
        tomarCapturaPaso("Formulario con datos inválidos");
        
        paginaRegistro.enviarFormulario();
        tomarCapturaPaso("Intento de envío con datos inválidos");
        
        boolean hayErrores = paginaRegistro.hayErroresValidacion();
        if (hayErrores) {
            registrarPasoExitoso("Errores detectados correctamente con datos inválidos");
            registrarInfo("Errores encontrados: " + paginaRegistro.obtenerErroresValidacion());
        } else {
            registrarAdvertencia("No se detectaron errores con datos obviamente inválidos");
        }
        
        // Act & Assert - Recuperación con datos válidos
        registrarInfo("=== RECUPERACIÓN CON DATOS VÁLIDOS ===");
        paginaRegistro.limpiarFormulario();
        
        DatosRegistro datosValidos = new DatosRegistro.Builder()
                .email("recuperacion@testautomation.com")
                .password("RecuperacionPass123")
                .confirmarPassword("RecuperacionPass123")
                .nombre("Usuario")
                .apellido("Recuperacion")
                .aceptarTerminos()
                .build();
        
        paginaRegistro.llenarFormularioRegistro(datosValidos);
        tomarCapturaPaso("Formulario corregido con datos válidos");
        
        paginaRegistro.enviarFormulario();
        tomarCapturaPaso("Envío con datos válidos después de error");
        
        boolean registroExitoso = paginaRegistro.registroExitoso();
        if (registroExitoso) {
            registrarPasoExitoso("Recuperación exitosa: datos válidos después de error");
        } else {
            registrarPasoFallido("No se pudo recuperar después del error", null);
            registrarInfo("Errores persistentes: " + paginaRegistro.obtenerErroresValidacion());
        }
        
        // Assertions
        Assert.assertTrue(hayErrores || !paginaRegistro.obtenerErroresValidacion().isEmpty(), 
                         "Debería detectar errores con datos inválidos");
        
        registrarInfo("Flujo de manejo de errores y recuperación completado");
    }
}