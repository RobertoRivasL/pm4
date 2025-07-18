package com.robertorivas.automatizacion.paginas;

import com.robertorivas.automatizacion.modelos.DatosRegistro;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.List;

/**
 * Page Object para la página de registro de ExpandTesting.
 * Implementa el patrón POM para encapsular la interacción con el formulario de registro.
 * 
 * URL: https://practice.expandtesting.com/register
 * 
 * Casos de prueba soportados:
 * - Registro exitoso con username y password válidos
 * - Error con campos faltantes: "All fields are required."
 * - Error con passwords no coincidentes: "Passwords do not match."
 * - Redirección a /login con mensaje: "Successfully registered, you can log in now."
 * 
 * @author Roberto Rivas Lopez
 */
public class PaginaRegistro extends PaginaBase {
    
    // ===== LOCALIZADORES ESPECÍFICOS PARA EXPANDTESTING =====
    
    // Campos del formulario de registro
    @FindBy(css = "input[name='username'], #username")
    private WebElement campoUsername;
    
    @FindBy(css = "input[name='password'], #password")
    private WebElement campoPassword;
    
    @FindBy(css = "input[name='confirmPassword'], #confirmPassword")
    private WebElement campoConfirmarPassword;
    
    // Botones
    @FindBy(css = "button[type='submit'], .btn-primary, input[value='Register']")
    private WebElement botonRegistrar;
    
    @FindBy(css = ".btn-clear, #clear, button[name='clear']")
    private WebElement botonLimpiar;
    
    // Mensajes y alertas específicos de ExpandTesting
    @FindBy(css = "#flash, .alert-danger, .error")
    private WebElement mensajeError;
    
    @FindBy(css = "#flash, .alert-success, .success")
    private WebElement mensajeExito;
    
    // Enlaces de navegación
    @FindBy(css = "a[href='/login'], a[href*='login']")
    private WebElement enlaceLogin;
    
    // Localizadores dinámicos
    private static final By LOADER = By.cssSelector(".loader, .spinner, .loading");
    private static final By MENSAJE_FLASH = By.cssSelector("#flash");
    
    /**
     * Constructor de la página de registro.
     */
    public PaginaRegistro(WebDriver driver) {
        super(driver);
        logger.info("Inicializando página de registro de ExpandTesting");
    }
    
    // ===== MÉTODOS ABSTRACTOS IMPLEMENTADOS =====
    
    @Override
    public boolean estaPaginaCargada() {
        try {
            return estaElementoVisible(By.cssSelector("input[name='username']")) &&
                   estaElementoVisible(By.cssSelector("input[name='password']")) &&
                   estaElementoVisible(By.cssSelector("input[name='confirmPassword']")) &&
                   estaElementoVisible(By.cssSelector("button[type='submit'], .btn-primary")) &&
                   obtenerUrlActual().contains("/register");
        } catch (Exception e) {
            logger.debug("Error verificando si la página de registro está cargada: {}", e.getMessage());
            return false;
        }
    }
    
    @Override
    public void esperarCargaPagina() {
        logger.debug("Esperando carga completa de la página de registro de ExpandTesting");
        
        // Esperar elementos principales del formulario
        buscarElementoVisible(By.cssSelector("input[name='username']"));
        buscarElementoVisible(By.cssSelector("input[name='password']"));
        buscarElementoVisible(By.cssSelector("input[name='confirmPassword']"));
        buscarElementoVisible(By.cssSelector("button[type='submit'], .btn-primary"));
        
        // Esperar que desaparezca el loader si existe
        esperarQueElementoDesaparezca(LOADER, TIMEOUT_MEDIO);
        
        logger.info("Página de registro de ExpandTesting cargada completamente");
    }
    
    @Override
    public String obtenerTituloPagina() {
        return "Register - ExpandTesting";
    }
    
    // ===== MÉTODOS DE NAVEGACIÓN =====
    
    /**
     * Navega a la página de registro de ExpandTesting.
     */
    public PaginaRegistro navegarAPaginaRegistro() {
        String urlRegistro = config.obtenerUrlRegistro();
        navegarA(urlRegistro);
        esperarCargaPagina();
        return this;
    }
    
    /**
     * Navega a la página de login desde el enlace.
     */
    public void irAPaginaLogin() {
        logger.info("Navegando a página de login desde registro");
        if (estaElementoPresente(By.cssSelector("a[href='/login'], a[href*='login']"))) {
            hacerClic(By.cssSelector("a[href='/login'], a[href*='login']"));
        } else {
            // Navegación directa si no hay enlace
            navegarA(config.obtenerUrlBase() + "/login");
        }
    }
    
    // ===== MÉTODOS DE INTERACCIÓN CON FORMULARIO =====
    
    /**
     * Introduce el username en el campo correspondiente.
     */
    public PaginaRegistro introducirUsername(String username) {
        logger.debug("Introduciendo username: {}", username);
        introducirTexto(By.cssSelector("input[name='username']"), username);
        return this;
    }
    
    /**
     * Introduce la contraseña.
     */
    public PaginaRegistro introducirPassword(String password) {
        logger.debug("Introduciendo contraseña");
        introducirTexto(By.cssSelector("input[name='password']"), password);
        return this;
    }
    
    /**
     * Introduce la confirmación de contraseña.
     */
    public PaginaRegistro introducirConfirmarPassword(String confirmarPassword) {
        logger.debug("Introduciendo confirmación de contraseña");
        introducirTexto(By.cssSelector("input[name='confirmPassword']"), confirmarPassword);
        return this;
    }
    
    /**
     * Introduce el email (alias para username para compatibilidad).
     */
    public PaginaRegistro introducirEmail(String email) {
        return introducirUsername(email);
    }
    
    /**
     * Introduce el nombre (para compatibilidad, aunque ExpandTesting no lo usa).
     */
    public PaginaRegistro introducirNombre(String nombre) {
        // ExpandTesting no tiene campo nombre separado
        logger.debug("Nota: ExpandTesting no tiene campo nombre separado");
        return this;
    }
    
    /**
     * Introduce el apellido (para compatibilidad, aunque ExpandTesting no lo usa).
     */
    public PaginaRegistro introducirApellido(String apellido) {
        // ExpandTesting no tiene campo apellido separado
        logger.debug("Nota: ExpandTesting no tiene campo apellido separado");
        return this;
    }
    
    /**
     * Acepta términos (para compatibilidad, aunque ExpandTesting no lo requiere).
     */
    public PaginaRegistro aceptarTerminos() {
        // ExpandTesting no tiene checkbox de términos
        logger.debug("Nota: ExpandTesting no requiere aceptar términos");
        return this;
    }
    
    /**
     * Hace clic en el botón de registro.
     */
    public void hacerClicRegistrar() {
        logger.info("Haciendo clic en botón de registro");
        hacerClic(By.cssSelector("button[type='submit'], .btn-primary"));
        
        // Esperar respuesta del servidor
        esperarRespuestaRegistro();
    }
    
    /**
     * Limpia todos los campos del formulario.
     */
    public PaginaRegistro limpiarFormulario() {
        logger.info("Limpiando formulario de registro");
        
        if (estaElementoPresente(By.cssSelector("input[name='username']"))) {
            WebElement username = driver.findElement(By.cssSelector("input[name='username']"));
            username.clear();
        }
        
        if (estaElementoPresente(By.cssSelector("input[name='password']"))) {
            WebElement password = driver.findElement(By.cssSelector("input[name='password']"));
            password.clear();
        }
        
        if (estaElementoPresente(By.cssSelector("input[name='confirmPassword']"))) {
            WebElement confirmPassword = driver.findElement(By.cssSelector("input[name='confirmPassword']"));
            confirmPassword.clear();
        }
        
        return this;
    }
    
    // ===== MÉTODOS DE REGISTRO PRINCIPAL =====
    
    /**
     * Llena el formulario de registro con datos completos (adaptado para ExpandTesting).
     */
    public PaginaRegistro llenarFormularioRegistro(DatosRegistro datos) {
        logger.info("Llenando formulario de registro para: {}", datos.getEmail());
        
        llenarCamposBasicos(datos);
        // ExpandTesting solo requiere username, password y confirmPassword
        
        logger.info("Formulario de registro completado para ExpandTesting");
        return this;
    }
    
    /**
     * Llena solo los campos básicos requeridos por ExpandTesting.
     */
    public PaginaRegistro llenarCamposBasicos(DatosRegistro datos) {
        if (datos.getEmail() != null) {
            introducirUsername(datos.getEmail());
        }
        
        if (datos.getPassword() != null) {
            introducirPassword(datos.getPassword());
        }
        
        if (datos.getConfirmarPassword() != null) {
            introducirConfirmarPassword(datos.getConfirmarPassword());
        }
        
        return this;
    }
    
    /**
     * Realiza el proceso completo de registro.
     */
    public boolean registrarUsuario(String username, String password, String confirmPassword) {
        logger.info("Iniciando registro para usuario: {}", username);
        
        try {
            // Limpiar campos antes de llenar
            limpiarFormulario();
            
            // Llenar formulario
            introducirUsername(username);
            introducirPassword(password);
            introducirConfirmarPassword(confirmPassword);
            
            // Enviar formulario
            hacerClicRegistrar();
            
            // Verificar resultado
            return verificarRegistroExitoso();
            
        } catch (Exception e) {
            logger.error("Error durante el proceso de registro: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Realiza el proceso completo de registro con DatosRegistro.
     */
    public boolean registrarUsuario(DatosRegistro datos) {
        return registrarUsuario(datos.getEmail(), datos.getPassword(), datos.getConfirmarPassword());
    }
    
    /**
     * Envía el formulario de registro.
     */
    public void enviarFormulario() {
        hacerClicRegistrar();
    }
    
    // ===== MÉTODOS DE VALIDACIÓN Y VERIFICACIÓN =====
    
    /**
     * Verifica si el registro fue exitoso.
     */
    public boolean verificarRegistroExitoso() {
        try {
            // Opción 1: Verificar redirección a página de login
            String urlActual = obtenerUrlActual();
            if (urlActual.contains("/login")) {
                logger.info("Registro exitoso - Redirigido a página de login");
                return true;
            }
            
            // Opción 2: Verificar mensaje de éxito
            if (estaElementoVisible(MENSAJE_FLASH)) {
                String mensajeFlash = obtenerTexto(MENSAJE_FLASH);
                if (mensajeFlash.contains("Successfully registered, you can log in now.")) {
                    logger.info("Registro exitoso - Mensaje de éxito detectado");
                    return true;
                }
            }
            
            // Opción 3: Verificar ausencia de errores y cambio de página
            if (!hayErroresValidacion() && !urlActual.contains("/register")) {
                logger.info("Registro exitoso - Sin errores y fuera de página de registro");
                return true;
            }
            
            return false;
            
        } catch (Exception e) {
            logger.error("Error verificando registro exitoso: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Verifica si el registro fue exitoso (alias para compatibilidad).
     */
    public boolean registroExitoso() {
        return verificarRegistroExitoso();
    }
    
    /**
     * Verifica si hay errores de validación.
     */
    public boolean hayErroresValidacion() {
        try {
            if (estaElementoVisible(MENSAJE_FLASH)) {
                String mensaje = obtenerTexto(MENSAJE_FLASH);
                return mensaje.contains("All fields are required.") || 
                       mensaje.contains("Passwords do not match.") ||
                       mensaje.contains("error") ||
                       mensaje.contains("Error");
            }
            return false;
        } catch (Exception e) {
            logger.debug("Error verificando errores de validación: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Obtiene todos los errores de validación.
     */
    public List<String> obtenerErroresValidacion() {
        List<String> errores = new ArrayList<>();
        
        try {
            if (estaElementoVisible(MENSAJE_FLASH)) {
                String mensaje = obtenerTexto(MENSAJE_FLASH);
                if (!mensaje.isEmpty() && (mensaje.contains("error") || mensaje.contains("Error") || 
                    mensaje.contains("required") || mensaje.contains("match"))) {
                    errores.add(mensaje);
                }
            }
        } catch (Exception e) {
            logger.debug("Error obteniendo errores de validación: {}", e.getMessage());
        }
        
        return errores;
    }
    
    /**
     * Obtiene el mensaje de éxito si está visible.
     */
    public String obtenerMensajeExito() {
        try {
            if (estaElementoVisible(MENSAJE_FLASH)) {
                String mensaje = obtenerTexto(MENSAJE_FLASH);
                if (mensaje.contains("Successfully registered")) {
                    return mensaje;
                }
            }
            return null;
        } catch (Exception e) {
            logger.debug("Error obteniendo mensaje de éxito: {}", e.getMessage());
            return null;
        }
    }
    
    // ===== MÉTODOS DE CASOS DE PRUEBA ESPECÍFICOS =====
    
    /**
     * Prueba de registro con username faltante.
     */
    public boolean probarRegistroSinUsername(String password) {
        logger.info("Probando registro sin username");
        
        limpiarFormulario();
        // Dejar username vacío
        introducirPassword(password);
        introducirConfirmarPassword(password);
        hacerClicRegistrar();
        
        return verificarErrorCamposRequeridos();
    }
    
    /**
     * Prueba de registro con password faltante.
     */
    public boolean probarRegistroSinPassword(String username) {
        logger.info("Probando registro sin password");
        
        limpiarFormulario();
        introducirUsername(username);
        // Dejar passwords vacíos
        hacerClicRegistrar();
        
        return verificarErrorCamposRequeridos();
    }
    
    /**
     * Prueba de registro con passwords que no coinciden.
     */
    public boolean probarPasswordsNoCoinciden(String username, String password, String confirmPassword) {
        logger.info("Probando registro con passwords que no coinciden");
        
        limpiarFormulario();
        introducirUsername(username);
        introducirPassword(password);
        introducirConfirmarPassword(confirmPassword);
        hacerClicRegistrar();
        
        return verificarErrorPasswordsNoCoinciden();
    }
    
    /**
     * Verifica error de campos requeridos.
     */
    private boolean verificarErrorCamposRequeridos() {
        List<String> errores = obtenerErroresValidacion();
        return errores.stream().anyMatch(error -> error.contains("All fields are required."));
    }
    
    /**
     * Verifica error de passwords que no coinciden.
     */
    private boolean verificarErrorPasswordsNoCoinciden() {
        List<String> errores = obtenerErroresValidacion();
        return errores.stream().anyMatch(error -> error.contains("Passwords do not match."));
    }
    
    // ===== MÉTODOS DE UTILIDAD =====
    
    /**
     * Obtiene todos los valores actuales del formulario.
     */
    public DatosRegistro obtenerValoresFormulario() {
        DatosRegistro datos = new DatosRegistro();
        
        try {
            if (estaElementoPresente(By.cssSelector("input[name='username']"))) {
                datos.setEmail(obtenerAtributo(By.cssSelector("input[name='username']"), "value"));
            }
            
            // Nota: No obtenemos passwords por seguridad
            
        } catch (Exception e) {
            logger.warn("Error obteniendo valores del formulario: {}", e.getMessage());
        }
        
        return datos;
    }
    
    /**
     * Verifica si el formulario está completamente lleno.
     */
    public boolean formularioCompleto() {
        try {
            String username = obtenerAtributo(By.cssSelector("input[name='username']"), "value");
            String password = obtenerAtributo(By.cssSelector("input[name='password']"), "value");
            String confirmPassword = obtenerAtributo(By.cssSelector("input[name='confirmPassword']"), "value");
            
            return username != null && !username.isEmpty() && 
                   password != null && !password.isEmpty() &&
                   confirmPassword != null && !confirmPassword.isEmpty();
        } catch (Exception e) {
            logger.debug("Error verificando si el formulario está completo: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Verifica si el formulario de registro está disponible.
     */
    public boolean formularioRegistroDisponible() {
        try {
            return estaElementoVisible(By.cssSelector("input[name='username']")) &&
                   estaElementoVisible(By.cssSelector("input[name='password']")) &&
                   estaElementoVisible(By.cssSelector("input[name='confirmPassword']")) &&
                   estaElementoVisible(By.cssSelector("button[type='submit'], .btn-primary"));
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Espera la respuesta del servidor después del registro.
     */
    private void esperarRespuestaRegistro() {
        try {
            // Esperar que aparezca mensaje de éxito, error o redirección
            espera.until(driver -> {
                String urlActual = driver.getCurrentUrl();
                boolean hayMensaje = estaElementoVisible(MENSAJE_FLASH);
                boolean huboRedireccion = !urlActual.endsWith("/register");
                
                return hayMensaje || huboRedireccion;
            });
        } catch (Exception e) {
            logger.debug("Timeout esperando respuesta de registro");
        }
    }
    
    /**
     * Obtiene información de debug de la página actual.
     */
    public void logearInformacionRegistro() {
        logger.debug("=== INFORMACIÓN DE REGISTRO ===");
        logger.debug("URL actual: {}", obtenerUrlActual());
        logger.debug("Página cargada: {}", estaPaginaCargada());
        logger.debug("Formulario disponible: {}", formularioRegistroDisponible());
        logger.debug("Formulario completo: {}", formularioCompleto());
        logger.debug("Hay errores: {}", hayErroresValidacion());
        
        if (hayErroresValidacion()) {
            logger.debug("Errores: {}", obtenerErroresValidacion());
        }
        
        String mensajeExito = obtenerMensajeExito();
        if (mensajeExito != null) {
            logger.debug("Mensaje de éxito: {}", mensajeExito);
        }
        
        logger.debug("===============================");
    }
}