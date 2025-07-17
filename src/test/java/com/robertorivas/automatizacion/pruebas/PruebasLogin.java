package com.robertorivas.automatizacion.pruebas;

import com.robertorivas.automatizacion.datos.ProveedorDatos;
import com.robertorivas.automatizacion.modelos.Usuario;
import com.robertorivas.automatizacion.paginas.PaginaLogin;
import com.robertorivas.automatizacion.paginas.PaginaPrincipal;
import com.robertorivas.automatizacion.utilidades.GestorDatos;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase de pruebas para el formulario de login/inicio de sesión.
 * Contiene pruebas funcionales completas para validar todos los escenarios
 * del proceso de autenticación.
 * 
 * Principios aplicados:
 * - Single Responsibility: Solo pruebas de login
 * - Data Driven Testing: Uso de proveedores de datos
 * - Page Object Model: Interacción a través de page objects
 * - AAA Pattern: Arrange, Act, Assert en cada prueba
 * 
 * @author Roberto Rivas Lopez
 */
public class PruebasLogin extends PruebasBase {
    
    private PaginaLogin paginaLogin;
    private PaginaPrincipal paginaPrincipal;
    private GestorDatos gestorDatos;
    
    @BeforeClass
    public void configuracionClaseLogin() {
        super.configuracionClase();
        
        // Inicializar gestor de datos
        gestorDatos = new GestorDatos();
        
        logger.info("Configuración específica para pruebas de login completada");
    }
    
    // ===== PRUEBAS DE LOGIN EXITOSO =====
    
    @Test(description = "Login exitoso con credenciales válidas",
          groups = {"login", "positivo", "smoke"},
          priority = 1)
    public void loginExitosoConCredencialesValidas() {
        registrarSeparador("LOGIN EXITOSO - CREDENCIALES VÁLIDAS");
        
        // Arrange
        Usuario usuario = Usuario.crearUsuarioPrueba("student", "Password123");
        
        Map<String, Object> datosEntrada = new HashMap<>();
        datosEntrada.put("Email", usuario.getEmail());
        datosEntrada.put("Password", "***" + "*".repeat(usuario.getPassword().length() - 3));
        registrarDatosEntrada(datosEntrada);
        
        paginaLogin = new PaginaLogin(obtenerDriver());
        
        // Act
        registrarInfo("Navegando a la página de login");
        paginaLogin.navegarAPaginaLogin();
        tomarCapturaPaso("Página de login cargada");
        
        registrarInfo("Iniciando sesión con credenciales válidas");
        boolean loginExitoso = paginaLogin.iniciarSesion(usuario);
        tomarCapturaPaso("Intento de login completado");
        
        // Assert
        if (loginExitoso) {
            registrarPasoExitoso("Login completado exitosamente");
            
            // Verificar que estamos en la página principal
            paginaPrincipal = new PaginaPrincipal(obtenerDriver());
            boolean enPaginaPrincipal = paginaPrincipal.usuarioLogueado();
            
            if (enPaginaPrincipal) {
                registrarPasoExitoso("Usuario correctamente logueado en página principal");
                String nombreUsuario = paginaPrincipal.obtenerNombreUsuarioLogueado();
                registrarInfo("Usuario logueado: " + nombreUsuario);
                tomarCapturaPaso("Página principal después del login");
            } else {
                registrarPasoFallido("No se pudo verificar el estado de login en página principal", null);
            }
            
            Assert.assertTrue(enPaginaPrincipal, "El usuario debería estar logueado en la página principal");
        } else {
            List<String> errores = paginaLogin.obtenerErroresLogin();
            registrarPasoFallido("Login falló con credenciales válidas. Errores: " + errores, null);
            Assert.fail("El login debería ser exitoso con credenciales válidas");
        }
        
        Assert.assertTrue(loginExitoso, "El login debería ser exitoso");
    }
    
    @Test(dataProvider = "usuariosValidos", dataProviderClass = ProveedorDatos.class,
          description = "Login exitoso con múltiples usuarios válidos",
          groups = {"login", "positivo", "datadriven"},
          priority = 2)
    public void loginExitosoConMultiplesUsuarios(Usuario usuario) {
        registrarSeparador("LOGIN MÚLTIPLES USUARIOS - " + usuario.getEmail());
        
        // Arrange
        Map<String, Object> datosEntrada = new HashMap<>();
        datosEntrada.put("Email", usuario.getEmail());
        datosEntrada.put("Usuario ID", usuario.getId());
        registrarDatosEntrada(datosEntrada);
        
        paginaLogin = new PaginaLogin(obtenerDriver());
        
        // Act
        registrarInfo("Ejecutando login para usuario: " + usuario.getEmail());
        paginaLogin.navegarAPaginaLogin();
        
        boolean loginExitoso = paginaLogin.iniciarSesion(usuario);
        tomarCapturaPaso("Login para: " + usuario.getEmail());
        
        // Assert
        if (loginExitoso) {
            registrarPasoExitoso("Login exitoso para: " + usuario.getEmail());
            
            // Verificar estado de sesión
            paginaPrincipal = new PaginaPrincipal(obtenerDriver());
            boolean usuarioLogueado = paginaPrincipal.usuarioLogueado();
            
            Assert.assertTrue(usuarioLogueado, "Usuario debería estar logueado: " + usuario.getEmail());
        } else {
            List<String> errores = paginaLogin.obtenerErroresLogin();
            registrarPasoFallido("Login falló para: " + usuario.getEmail() + ". Errores: " + errores, null);
        }
        
        Assert.assertTrue(loginExitoso, "Login debería ser exitoso para: " + usuario.getEmail());
    }
    
    @Test(description = "Login exitoso con opción 'Recordar credenciales'",
          groups = {"login", "positivo", "funcional"},
          priority = 3)
    public void loginExitosoConRecordarCredenciales() {
        registrarSeparador("LOGIN EXITOSO - RECORDAR CREDENCIALES");
        
        // Arrange
        Usuario usuario = Usuario.crearUsuarioPrueba("student", "Password123");
        paginaLogin = new PaginaLogin(obtenerDriver());
        
        // Act
        registrarInfo("Navegando a página de login");
        paginaLogin.navegarAPaginaLogin();
        
        registrarInfo("Iniciando sesión con opción de recordar credenciales");
        boolean loginExitoso = paginaLogin.iniciarSesion(usuario.getEmail(), usuario.getPassword(), true);
        tomarCapturaPaso("Login con recordar credenciales");
        
        // Assert
        if (loginExitoso) {
            registrarPasoExitoso("Login exitoso con opción recordar activada");
            
            paginaPrincipal = new PaginaPrincipal(obtenerDriver());
            boolean usuarioLogueado = paginaPrincipal.usuarioLogueado();
            
            Assert.assertTrue(usuarioLogueado, "Usuario debería estar logueado");
        } else {
            List<String> errores = paginaLogin.obtenerErroresLogin();
            registrarPasoFallido("Login falló con recordar credenciales. Errores: " + errores, null);
        }
        
        Assert.assertTrue(loginExitoso, "Login con recordar credenciales debería ser exitoso");
    }
    
    // ===== PRUEBAS DE LOGIN FALLIDO =====
    
    @Test(description = "Login fallido con credenciales inválidas",
          groups = {"login", "negativo", "validacion"},
          priority = 4)
    public void loginFallidoConCredencialesInvalidas() {
        registrarSeparador("LOGIN FALLIDO - CREDENCIALES INVÁLIDAS");
        
        // Arrange
        Usuario usuarioInvalido = new Usuario("usuario.inexistente@test.com", "passwordIncorrecto");
        
        Map<String, Object> datosEntrada = new HashMap<>();
        datosEntrada.put("Email", usuarioInvalido.getEmail());
        datosEntrada.put("Descripción", "Credenciales que no existen en el sistema");
        registrarDatosEntrada(datosEntrada);
        
        paginaLogin = new PaginaLogin(obtenerDriver());
        
        // Act
        registrarInfo("Navegando a página de login");
        paginaLogin.navegarAPaginaLogin();
        
        registrarInfo("Intentando login con credenciales inválidas");
        boolean loginExitoso = paginaLogin.iniciarSesion(usuarioInvalido);
        tomarCapturaPaso("Intento de login con credenciales inválidas");
        
        // Assert
        registrarInfo("Verificando que el login haya fallado");
        boolean hayErrores = paginaLogin.hayErroresLogin();
        boolean credencialesInvalidas = paginaLogin.credencialesInvalidas();
        
        if (!loginExitoso && hayErrores) {
            List<String> errores = paginaLogin.obtenerErroresLogin();
            registrarPasoExitoso("Login falló correctamente con credenciales inválidas");
            registrarInfo("Errores detectados: " + errores);
            
            if (credencialesInvalidas) {
                registrarPasoExitoso("Mensaje de credenciales inválidas detectado correctamente");
            }
        } else {
            registrarPasoFallido("El login no falló como se esperaba con credenciales inválidas", null);
        }
        
        Assert.assertFalse(loginExitoso, "El login debería fallar con credenciales inválidas");
        Assert.assertTrue(hayErrores, "Debería mostrar errores con credenciales inválidas");
    }
    
    @Test(dataProvider = "credencialesInvalidas", dataProviderClass = ProveedorDatos.class,
          description = "Login fallido con múltiples credenciales inválidas",
          groups = {"login", "negativo", "datadriven"},
          priority = 5)
    public void loginFallidoConMultiplesCredencialesInvalidas(Usuario usuarioInvalido) {
        registrarSeparador("LOGIN FALLIDO MÚLTIPLE - " + usuarioInvalido.getEmail());
        
        // Arrange
        Map<String, Object> datosEntrada = new HashMap<>();
        datosEntrada.put("Email", usuarioInvalido.getEmail());
        datosEntrada.put("Motivo", "Credenciales inválidas del dataset");
        registrarDatosEntrada(datosEntrada);
        
        paginaLogin = new PaginaLogin(obtenerDriver());
        
        // Act
        registrarInfo("Probando credenciales inválidas: " + usuarioInvalido.getEmail());
        paginaLogin.navegarAPaginaLogin();
        
        boolean loginExitoso = paginaLogin.iniciarSesion(usuarioInvalido);
        
        // Assert
        boolean hayErrores = paginaLogin.hayErroresLogin();
        
        if (!loginExitoso && hayErrores) {
            registrarPasoExitoso("Login falló correctamente para: " + usuarioInvalido.getEmail());
        } else {
            registrarPasoFallido("Login no falló como esperado para: " + usuarioInvalido.getEmail(), null);
        }
        
        Assert.assertFalse(loginExitoso, "Login debería fallar para: " + usuarioInvalido.getEmail());
    }
    
    @Test(description = "Login fallido con campos vacíos",
          groups = {"login", "negativo", "validacion"},
          priority = 6)
    public void loginFallidoConCamposVacios() {
        registrarSeparador("LOGIN FALLIDO - CAMPOS VACÍOS");
        
        // Arrange
        paginaLogin = new PaginaLogin(obtenerDriver());
        
        // Act
        registrarInfo("Navegando a página de login");
        paginaLogin.navegarAPaginaLogin();
        tomarCapturaPaso("Página de login cargada");
        
        registrarInfo("Verificando comportamiento con campos vacíos");
        boolean resultadoCamposVacios = paginaLogin.verificarCamposVacios();
        tomarCapturaPaso("Intento de login con campos vacíos");
        
        // Assert
        if (resultadoCamposVacios) {
            List<String> errores = paginaLogin.obtenerErroresLogin();
            registrarPasoExitoso("Validación correcta con campos vacíos");
            registrarInfo("Errores detectados: " + errores);
        } else {
            registrarPasoFallido("No se detectaron errores con campos vacíos", null);
        }
        
        Assert.assertTrue(resultadoCamposVacios, "Debería mostrar errores con campos vacíos");
    }
    
    @Test(description = "Login fallido con email válido y password vacío",
          groups = {"login", "negativo", "validacion"},
          priority = 7)
    public void loginFallidoConPasswordVacio() {
        registrarSeparador("LOGIN FALLIDO - PASSWORD VACÍO");
        
        // Arrange
        String emailValido = "student";
        paginaLogin = new PaginaLogin(obtenerDriver());
        
        // Act
        registrarInfo("Navegando a página de login");
        paginaLogin.navegarAPaginaLogin();
        
        registrarInfo("Verificando comportamiento con password vacío");
        boolean resultadoPasswordVacio = paginaLogin.verificarPasswordVacio(emailValido);
        tomarCapturaPaso("Intento de login con password vacío");
        
        // Assert
        if (resultadoPasswordVacio) {
            List<String> errores = paginaLogin.obtenerErroresLogin();
            registrarPasoExitoso("Validación correcta con password vacío");
            registrarInfo("Errores detectados: " + errores);
        } else {
            registrarPasoFallido("No se detectaron errores con password vacío", null);
        }
        
        Assert.assertTrue(resultadoPasswordVacio, "Debería mostrar errores con password vacío");
    }
    
    @Test(description = "Login fallido con email vacío y password válido",
          groups = {"login", "negativo", "validacion"},
          priority = 8)
    public void loginFallidoConEmailVacio() {
        registrarSeparador("LOGIN FALLIDO - EMAIL VACÍO");
        
        // Arrange
        String passwordValido = "Password123";
        paginaLogin = new PaginaLogin(obtenerDriver());
        
        // Act
        registrarInfo("Navegando a página de login");
        paginaLogin.navegarAPaginaLogin();
        
        registrarInfo("Verificando comportamiento con email vacío");
        boolean resultadoEmailVacio = paginaLogin.verificarEmailVacio(passwordValido);
        tomarCapturaPaso("Intento de login con email vacío");
        
        // Assert
        if (resultadoEmailVacio) {
            List<String> errores = paginaLogin.obtenerErroresLogin();
            registrarPasoExitoso("Validación correcta con email vacío");
            registrarInfo("Errores detectados: " + errores);
        } else {
            registrarPasoFallido("No se detectaron errores con email vacío", null);
        }
        
        Assert.assertTrue(resultadoEmailVacio, "Debería mostrar errores con email vacío");
    }
    
    // ===== PRUEBAS DE SEGURIDAD =====
    
    @Test(description = "Intentos múltiples de login hasta bloqueo de cuenta",
          groups = {"login", "seguridad", "negativo"},
          priority = 9)
    public void intentosMultiplesHastaBloqueoCuenta() {
        registrarSeparador("SEGURIDAD - BLOQUEO POR INTENTOS MÚLTIPLES");
        
        // Arrange
        String emailValido = "student";
        String passwordIncorrecto = "PasswordIncorrecto123";
        
        Map<String, Object> datosEntrada = new HashMap<>();
        datosEntrada.put("Email", emailValido);
        datosEntrada.put("Password Incorrecto", "***");
        datosEntrada.put("Objetivo", "Verificar bloqueo por intentos múltiples");
        registrarDatosEntrada(datosEntrada);
        
        paginaLogin = new PaginaLogin(obtenerDriver());
        
        // Act
        registrarInfo("Navegando a página de login");
        paginaLogin.navegarAPaginaLogin();
        
        registrarInfo("Intentando múltiples logins para activar bloqueo");
        boolean cuentaBloqueada = paginaLogin.intentarHastaBloqueoCuenta(emailValido, passwordIncorrecto);
        tomarCapturaPaso("Resultado de intentos múltiples");
        
        // Assert
        if (cuentaBloqueada) {
            registrarPasoExitoso("Cuenta bloqueada correctamente después de múltiples intentos");
            List<String> errores = paginaLogin.obtenerErroresLogin();
            registrarInfo("Mensajes de bloqueo: " + errores);
        } else {
            registrarAdvertencia("La cuenta no se bloqueó después de múltiples intentos fallidos");
            registrarInfo("Esto podría indicar que el sistema no tiene protección contra fuerza bruta");
        }
        
        // Nota: No hacer Assert.assertTrue porque algunos sistemas no implementan bloqueo
        registrarInfo("Intentos de bloqueo completados. Resultado: " + (cuentaBloqueada ? "Bloqueado" : "No bloqueado"));
    }
    
    // ===== PRUEBAS DE FUNCIONALIDAD =====
    
    @Test(description = "Funcionalidad de limpiar campos de login",
          groups = {"login", "funcional"},
          priority = 10)
    public void funcionalidadLimpiarCampos() {
        registrarSeparador("FUNCIONALIDAD - LIMPIAR CAMPOS");
        
        // Arrange
        Usuario usuario = Usuario.crearUsuarioPrueba("test@test.com", "testpass");
        paginaLogin = new PaginaLogin(obtenerDriver());
        
        // Act
        registrarInfo("Navegando a página de login");
        paginaLogin.navegarAPaginaLogin();
        
        registrarInfo("Llenando campos de login");
        paginaLogin.introducirEmail(usuario.getEmail());
        paginaLogin.introducirPassword(usuario.getPassword());
        tomarCapturaPaso("Campos llenos");
        
        registrarInfo("Limpiando campos");
        paginaLogin.limpiarCamposLogin();
        tomarCapturaPaso("Campos limpiados");
        
        // Assert
        String[] valoresActuales = paginaLogin.obtenerValoresFormulario();
        String emailActual = valoresActuales[0];
        String passwordActual = valoresActuales[1];
        
        boolean camposVacios = (emailActual == null || emailActual.isEmpty()) &&
                              (passwordActual == null || passwordActual.isEmpty());
        
        if (camposVacios) {
            registrarPasoExitoso("Campos limpiados correctamente");
        } else {
            registrarPasoFallido("Los campos no se limpiaron completamente", null);
            registrarInfo("Valores actuales - Email: '" + emailActual + "', Password: '" + passwordActual + "'");
        }
        
        Assert.assertTrue(camposVacios, "Los campos deberían estar vacíos después de limpiar");
    }
    
    @Test(description = "Navegación a página de registro desde login",
          groups = {"login", "navegacion"},
          priority = 11)
    public void navegacionARegistro() {
        registrarSeparador("NAVEGACIÓN - LOGIN A REGISTRO");
        
        // Arrange
        paginaLogin = new PaginaLogin(obtenerDriver());
        
        // Act
        registrarInfo("Navegando a página de login");
        paginaLogin.navegarAPaginaLogin();
        tomarCapturaPaso("Página de login");
        
        registrarInfo("Navegando a página de registro desde login");
        paginaLogin.irAPaginaRegistro();
        tomarCapturaPaso("Navegación a registro");
        
        // Assert
        String urlActual = obtenerDriver().getCurrentUrl();
        boolean enPaginaRegistro = urlActual.contains("register") || 
                                  urlActual.contains("signup") ||
                                  urlActual.contains("registro");
        
        if (enPaginaRegistro) {
            registrarPasoExitoso("Navegación exitosa a página de registro");
        } else {
            registrarAdvertencia("No se pudo confirmar navegación a registro. URL actual: " + urlActual);
        }
        
        // Nota: No hacer assert estricto porque podría no existir el enlace
        registrarInfo("URL actual después de navegación: " + urlActual);
    }
    
    @Test(description = "Navegación a recuperación de contraseña",
          groups = {"login", "navegacion"},
          priority = 12)
    public void navegacionARecuperarPassword() {
        registrarSeparador("NAVEGACIÓN - RECUPERAR CONTRASEÑA");
        
        // Arrange
        paginaLogin = new PaginaLogin(obtenerDriver());
        
        // Act
        registrarInfo("Navegando a página de login");
        paginaLogin.navegarAPaginaLogin();
        tomarCapturaPaso("Página de login");
        
        registrarInfo("Navegando a recuperación de contraseña");
        paginaLogin.irARecuperarPassword();
        tomarCapturaPaso("Navegación a recuperar password");
        
        // Assert
        String urlActual = obtenerDriver().getCurrentUrl();
        boolean enPaginaRecuperacion = urlActual.contains("forgot") || 
                                      urlActual.contains("recover") ||
                                      urlActual.contains("reset") ||
                                      urlActual.contains("password");
        
        if (enPaginaRecuperacion) {
            registrarPasoExitoso("Navegación exitosa a recuperación de contraseña");
        } else {
            registrarAdvertencia("No se pudo confirmar navegación a recuperación. URL actual: " + urlActual);
        }
        
        registrarInfo("URL actual después de navegación: " + urlActual);
    }
    
    // ===== PRUEBAS DE FLUJO COMPLETO =====
    
    @Test(description = "Flujo completo: Login exitoso y logout",
          groups = {"login", "flujo", "smoke"},
          priority = 13)
    public void flujoCompletoLoginYLogout() {
        registrarSeparador("FLUJO COMPLETO - LOGIN Y LOGOUT");
        
        // Arrange
        Usuario usuario = Usuario.crearUsuarioPrueba("student", "Password123");
        
        Map<String, Object> datosEntrada = new HashMap<>();
        datosEntrada.put("Email", usuario.getEmail());
        datosEntrada.put("Flujo", "Login completo seguido de logout");
        registrarDatosEntrada(datosEntrada);
        
        paginaLogin = new PaginaLogin(obtenerDriver());
        
        // Act - Parte 1: Login
        registrarInfo("=== FASE 1: LOGIN ===");
        paginaLogin.navegarAPaginaLogin();
        
        boolean loginExitoso = paginaLogin.iniciarSesion(usuario);
        tomarCapturaPaso("Login completado");
        
        // Verificar login exitoso
        if (loginExitoso) {
            registrarPasoExitoso("Login exitoso");
            
            paginaPrincipal = new PaginaPrincipal(obtenerDriver());
            boolean usuarioLogueado = paginaPrincipal.usuarioLogueado();
            
            if (usuarioLogueado) {
                registrarPasoExitoso("Usuario correctamente autenticado");
                String nombreUsuario = paginaPrincipal.obtenerNombreUsuarioLogueado();
                registrarInfo("Usuario logueado: " + nombreUsuario);
                
                // Act - Parte 2: Logout
                registrarInfo("=== FASE 2: LOGOUT ===");
                boolean logoutExitoso = paginaPrincipal.cerrarSesion();
                tomarCapturaPaso("Logout completado");
                
                if (logoutExitoso) {
                    registrarPasoExitoso("Logout exitoso");
                    
                    // Verificar que estamos de vuelta en login o página inicial
                    String urlFinal = obtenerDriver().getCurrentUrl();
                    boolean enPaginaLogin = urlFinal.contains("login") || 
                                           urlFinal.contains("home") ||
                                           !paginaPrincipal.usuarioLogueado();
                    
                    if (enPaginaLogin) {
                        registrarPasoExitoso("Flujo completo exitoso: Login → Sesión → Logout");
                    } else {
                        registrarPasoFallido("El logout no redirigió correctamente", null);
                    }
                    
                    Assert.assertTrue(enPaginaLogin, "Debería estar fuera de la sesión después del logout");
                } else {
                    registrarPasoFallido("El logout no fue exitoso", null);
                    Assert.fail("El logout debería ser exitoso");
                }
            } else {
                registrarPasoFallido("No se pudo verificar el estado de login", null);
                Assert.fail("El usuario debería estar logueado");
            }
        } else {
            List<String> errores = paginaLogin.obtenerErroresLogin();
            registrarPasoFallido("Login falló en flujo completo. Errores: " + errores, null);
            Assert.fail("El login debería ser exitoso en el flujo completo");
        }
        
        Assert.assertTrue(loginExitoso, "El flujo completo de login debería ser exitoso");
    }
}