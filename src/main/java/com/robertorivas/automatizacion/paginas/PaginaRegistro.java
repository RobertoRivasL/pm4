package com.robertorivas.automatizacion.paginas;

import com.robertorivas.automatizacion.modelos.DatosRegistro;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.ArrayList;

/**
 * Page Object para la página de registro de usuarios.
 * Implementa el patrón POM para encapsular la interacción con el formulario de registro.
 * 
 * Principios aplicados:
 * - Page Object Model: Encapsula elementos y acciones de la página
 * - Single Responsibility: Solo maneja operaciones de registro
 * - Encapsulation: Elementos privados con métodos públicos de interacción
 * - DRY: Reutiliza funcionalidad de la clase base
 * 
 * @author Roberto Rivas Lopez
 */
public class PaginaRegistro extends PaginaBase {
    
    // ===== LOCALIZADORES DE ELEMENTOS =====
    
    // Campos del formulario de registro
    @FindBy(css = "input[name='username'], #username, input[type='email']")
    private WebElement campoEmail;
    
    @FindBy(css = "input[name='password'], #password")
    private WebElement campoPassword;
    
    @FindBy(css = "input[name='confirmPassword'], #confirmPassword, input[name='confirm_password']")
    private WebElement campoConfirmarPassword;
    
    @FindBy(css = "input[name='firstName'], #firstName, input[name='first_name']")
    private WebElement campoNombre;
    
    @FindBy(css = "input[name='lastName'], #lastName, input[name='last_name']")
    private WebElement campoApellido;
    
    @FindBy(css = "input[name='phone'], #phone, input[name='telephone']")
    private WebElement campoTelefono;
    
    // Dropdowns y selectores
    @FindBy(css = "select[name='gender'], #gender")
    private WebElement selectGenero;
    
    @FindBy(css = "select[name='country'], #country")
    private WebElement selectPais;
    
    @FindBy(css = "select[name='city'], #city")
    private WebElement selectCiudad;
    
    // Checkboxes
    @FindBy(css = "input[name='terms'], #terms, input[name='acceptTerms']")
    private WebElement checkboxTerminos;
    
    @FindBy(css = "input[name='newsletter'], #newsletter")
    private WebElement checkboxNotificaciones;
    
    @FindBy(css = "input[name='promotions'], #promotions")
    private WebElement checkboxPromociones;
    
    // Botones
    @FindBy(css = "button[type='submit'], #submit, .btn-register, input[type='submit']")
    private WebElement botonRegistrarse;
    
    @FindBy(css = ".btn-clear, #clear, button[name='clear']")
    private WebElement botonLimpiar;
    
    @FindBy(css = ".btn-cancel, #cancel, button[name='cancel']")
    private WebElement botonCancelar;
    
    // Mensajes y validaciones
    @FindBy(css = ".error-message, .alert-danger, #error, .field-error")
    private List<WebElement> mensajesError;
    
    @FindBy(css = ".success-message, .alert-success, #success")
    private WebElement mensajeExito;
    
    @FindBy(css = ".validation-message, .help-text")
    private List<WebElement> mensajesValidacion;
    
    // Enlaces y navegación
    @FindBy(css = "a[href*='login'], .link-login, #loginLink")
    private WebElement enlaceIniciarSesion;
    
    @FindBy(css = "a[href*='terms'], .link-terms")
    private WebElement enlaceTerminos;
    
    @FindBy(css = "a[href*='privacy'], .link-privacy")
    private WebElement enlacePrivacidad;
    
    // Localizadores dinámicos (usando By para flexibilidad)
    private static final By LOADER = By.cssSelector(".loader, .spinner, .loading");
    private static final By MODAL_CONFIRMACION = By.cssSelector(".modal, .dialog, .popup");
    private static final By CAPTCHA = By.cssSelector(".captcha, #captcha, .recaptcha");
    
    /**
     * Constructor de la página de registro.
     */
    public PaginaRegistro(WebDriver driver) {
        super(driver);
        logger.info("Inicializando página de registro");
    }
    
    // ===== MÉTODOS ABSTRACTOS IMPLEMENTADOS =====
    
    @Override
    public boolean estaPaginaCargada() {
        try {
            return estaElementoVisible(By.cssSelector("input[name='username'], #username, input[type='email']")) &&
                   estaElementoVisible(By.cssSelector("input[name='password'], #password")) &&
                   estaElementoVisible(By.cssSelector("button[type='submit'], #submit, .btn-register"));
        } catch (Exception e) {
            logger.debug("Error verificando si la página está cargada: {}", e.getMessage());
            return false;
        }
    }
    
    @Override
    public void esperarCargaPagina() {
        logger.debug("Esperando carga completa de la página de registro");
        
        // Esperar elementos principales del formulario
        buscarElementoVisible(By.cssSelector("input[name='username'], #username, input[type='email']"));
        buscarElementoVisible(By.cssSelector("input[name='password'], #password"));
        buscarElementoVisible(By.cssSelector("button[type='submit'], #submit, .btn-register"));
        
        // Esperar que desaparezca el loader si existe
        esperarQueElementoDesaparezca(LOADER, TIMEOUT_MEDIO);
        
        logger.info("Página de registro cargada completamente");
    }
    
    @Override
    public String obtenerTituloPagina() {
        return "Registro de Usuario";
    }
    
    // ===== MÉTODOS DE NAVEGACIÓN =====
    
    /**
     * Navega a la página de registro.
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
        hacerClic(By.cssSelector("a[href*='login'], .link-login, #loginLink"));
    }
    
    // ===== MÉTODOS DE INTERACCIÓN CON FORMULARIO =====
    
    /**
     * Llena el formulario de registro con datos completos.
     */
    public PaginaRegistro llenarFormularioRegistro(DatosRegistro datos) {
        logger.info("Llenando formulario de registro para: {}", datos.getEmail());
        
        llenarCamposBasicos(datos);
        llenarCamposPersonales(datos);
        llenarCamposOpcionales(datos);
        configurarOpciones(datos);
        
        logger.info("Formulario de registro completado");
        return this;
    }
    
    /**
     * Llena solo los campos básicos del formulario.
     */
    public PaginaRegistro llenarCamposBasicos(DatosRegistro datos) {
        if (datos.getEmail() != null) {
            introducirEmail(datos.getEmail());
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
     * Llena los campos personales del formulario.
     */
    public PaginaRegistro llenarCamposPersonales(DatosRegistro datos) {
        if (datos.getNombre() != null) {
            introducirNombre(datos.getNombre());
        }
        
        if (datos.getApellido() != null) {
            introducirApellido(datos.getApellido());
        }
        
        if (datos.getTelefono() != null) {
            introducirTelefono(datos.getTelefono());
        }
        
        return this;
    }
    
    /**
     * Llena los campos opcionales del formulario.
     */
    public PaginaRegistro llenarCamposOpcionales(DatosRegistro datos) {
        if (datos.getGenero() != null && estaElementoPresente(By.cssSelector("select[name='gender'], #gender"))) {
            seleccionarGenero(datos.getGenero());
        }
        
        if (datos.getPais() != null && estaElementoPresente(By.cssSelector("select[name='country'], #country"))) {
            seleccionarPais(datos.getPais());
        }
        
        if (datos.getCiudad() != null && estaElementoPresente(By.cssSelector("select[name='city'], #city"))) {
            seleccionarCiudad(datos.getCiudad());
        }
        
        return this;
    }
    
    /**
     * Configura las opciones del formulario (checkboxes).
     */
    public PaginaRegistro configurarOpciones(DatosRegistro datos) {
        if (datos.isAceptaTerminos()) {
            aceptarTerminos();
        }
        
        if (estaElementoPresente(By.cssSelector("input[name='newsletter'], #newsletter"))) {
            configurarNotificaciones(datos.isRecibirNotificaciones());
        }
        
        if (estaElementoPresente(By.cssSelector("input[name='promotions'], #promotions"))) {
            configurarPromociones(datos.isRecibirPromociones());
        }
        
        return this;
    }
    
    // ===== MÉTODOS DE CAMPOS INDIVIDUALES =====
    
    /**
     * Introduce el email en el campo correspondiente.
     */
    public PaginaRegistro introducirEmail(String email) {
        logger.debug("Introduciendo email: {}", email);
        introducirTexto(By.cssSelector("input[name='username'], #username, input[type='email']"), email);
        return this;
    }
    
    /**
     * Introduce la contraseña.
     */
    public PaginaRegistro introducirPassword(String password) {
        logger.debug("Introduciendo contraseña");
        introducirTexto(By.cssSelector("input[name='password'], #password"), password);
        return this;
    }
    
    /**
     * Introduce la confirmación de contraseña.
     */
    public PaginaRegistro introducirConfirmarPassword(String confirmarPassword) {
        logger.debug("Introduciendo confirmación de contraseña");
        if (estaElementoPresente(By.cssSelector("input[name='confirmPassword'], #confirmPassword"))) {
            introducirTexto(By.cssSelector("input[name='confirmPassword'], #confirmPassword"), confirmarPassword);
        }
        return this;
    }
    
    /**
     * Introduce el nombre.
     */
    public PaginaRegistro introducirNombre(String nombre) {
        logger.debug("Introduciendo nombre: {}", nombre);
        if (estaElementoPresente(By.cssSelector("input[name='firstName'], #firstName"))) {
            introducirTexto(By.cssSelector("input[name='firstName'], #firstName"), nombre);
        }
        return this;
    }
    
    /**
     * Introduce el apellido.
     */
    public PaginaRegistro introducirApellido(String apellido) {
        logger.debug("Introduciendo apellido: {}", apellido);
        if (estaElementoPresente(By.cssSelector("input[name='lastName'], #lastName"))) {
            introducirTexto(By.cssSelector("input[name='lastName'], #lastName"), apellido);
        }
        return this;
    }
    
    /**
     * Introduce el teléfono.
     */
    public PaginaRegistro introducirTelefono(String telefono) {
        logger.debug("Introduciendo teléfono: {}", telefono);
        if (estaElementoPresente(By.cssSelector("input[name='phone'], #phone"))) {
            introducirTexto(By.cssSelector("input[name='phone'], #phone"), telefono);
        }
        return this;
    }
    
    /**
     * Selecciona el género.
     */
    public PaginaRegistro seleccionarGenero(String genero) {
        logger.debug("Seleccionando género: {}", genero);
        seleccionarPorTexto(By.cssSelector("select[name='gender'], #gender"), genero);
        return this;
    }
    
    /**
     * Selecciona el país.
     */
    public PaginaRegistro seleccionarPais(String pais) {
        logger.debug("Seleccionando país: {}", pais);
        seleccionarPorTexto(By.cssSelector("select[name='country'], #country"), pais);
        return this;
    }
    
    /**
     * Selecciona la ciudad.
     */
    public PaginaRegistro seleccionarCiudad(String ciudad) {
        logger.debug("Seleccionando ciudad: {}", ciudad);
        seleccionarPorTexto(By.cssSelector("select[name='city'], #city"), ciudad);
        return this;
    }
    
    // ===== MÉTODOS DE CHECKBOXES =====
    
    /**
     * Acepta los términos y condiciones.
     */
    public PaginaRegistro aceptarTerminos() {
        logger.debug("Aceptando términos y condiciones");
        WebElement checkbox = buscarElementoClickeable(By.cssSelector("input[name='terms'], #terms"));
        if (!checkbox.isSelected()) {
            checkbox.click();
        }
        return this;
    }
    
    /**
     * Configura las notificaciones.
     */
    public PaginaRegistro configurarNotificaciones(boolean activar) {
        logger.debug("Configurando notificaciones: {}", activar);
        WebElement checkbox = buscarElementoClickeable(By.cssSelector("input[name='newsletter'], #newsletter"));
        if (checkbox.isSelected() != activar) {
            checkbox.click();
        }
        return this;
    }
    
    /**
     * Configura las promociones.
     */
    public PaginaRegistro configurarPromociones(boolean activar) {
        logger.debug("Configurando promociones: {}", activar);
        WebElement checkbox = buscarElementoClickeable(By.cssSelector("input[name='promotions'], #promotions"));
        if (checkbox.isSelected() != activar) {
            checkbox.click();
        }
        return this;
    }
    
    // ===== MÉTODOS DE ACCIONES =====
    
    /**
     * Envía el formulario de registro.
     */
    public void enviarFormulario() {
        logger.info("Enviando formulario de registro");
        hacerClic(By.cssSelector("button[type='submit'], #submit, .btn-register"));
        
        // Esperar que se procese el formulario
        esperarProcesamiento();
    }
    
    /**
     * Limpia todos los campos del formulario.
     */
    public PaginaRegistro limpiarFormulario() {
        logger.info("Limpiando formulario de registro");
        if (estaElementoPresente(By.cssSelector(".btn-clear, #clear"))) {
            hacerClic(By.cssSelector(".btn-clear, #clear"));
        } else {
            // Limpiar campos manualmente
            limpiarCamposManualmente();
        }
        return this;
    }
    
    /**
     * Cancela el registro.
     */
    public void cancelarRegistro() {
        logger.info("Cancelando registro");
        if (estaElementoPresente(By.cssSelector(".btn-cancel, #cancel"))) {
            hacerClic(By.cssSelector(".btn-cancel, #cancel"));
        }
    }
    
    /**
     * Limpia los campos manualmente.
     */
    private void limpiarCamposManualmente() {
        By[] campos = {
            By.cssSelector("input[name='username'], #username"),
            By.cssSelector("input[name='password'], #password"),
            By.cssSelector("input[name='confirmPassword'], #confirmPassword"),
            By.cssSelector("input[name='firstName'], #firstName"),
            By.cssSelector("input[name='lastName'], #lastName"),
            By.cssSelector("input[name='phone'], #phone")
        };
        
        for (By campo : campos) {
            if (estaElementoPresente(campo)) {
                WebElement elemento = driver.findElement(campo);
                elemento.clear();
            }
        }
    }
    
    // ===== MÉTODOS DE VALIDACIÓN =====
    
    /**
     * Verifica si hay errores de validación en el formulario.
     */
    public boolean hayErroresValidacion() {
        return !obtenerErroresValidacion().isEmpty();
    }
    
    /**
     * Obtiene todos los errores de validación visibles.
     */
    public List<String> obtenerErroresValidacion() {
        List<String> errores = new ArrayList<>();
        
        // Buscar mensajes de error generales
        List<WebElement> elementosError = driver.findElements(By.cssSelector(".error-message, .alert-danger, #error, .field-error"));
        for (WebElement elemento : elementosError) {
            if (elemento.isDisplayed()) {
                String textoError = elemento.getText().trim();
                if (!textoError.isEmpty()) {
                    errores.add(textoError);
                }
            }
        }
        
        return errores;
    }
    
    /**
     * Obtiene el mensaje de éxito si está visible.
     */
    public String obtenerMensajeExito() {
        if (estaElementoVisible(By.cssSelector(".success-message, .alert-success, #success"))) {
            return obtenerTexto(By.cssSelector(".success-message, .alert-success, #success"));
        }
        return null;
    }
    
    /**
     * Verifica si el registro fue exitoso.
     */
    public boolean registroExitoso() {
        return obtenerMensajeExito() != null || 
               estaEnUrl("success") || 
               estaEnUrl("welcome") ||
               estaEnUrl("login");
    }
    
    /**
     * Espera a que aparezca un mensaje de error específico.
     */
    public boolean esperarMensajeError(String mensajeEsperado) {
        try {
            espera.until(driver -> {
                List<String> errores = obtenerErroresValidacion();
                return errores.stream().anyMatch(error -> error.contains(mensajeEsperado));
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Espera el procesamiento del formulario.
     */
    private void esperarProcesamiento() {
        // Esperar que desaparezca el loader
        esperarQueElementoDesaparezca(LOADER, TIMEOUT_LARGO);
        
        // Esperar que aparezcan mensajes de error o éxito
        try {
            espera.until(driver -> 
                hayErroresValidacion() || 
                registroExitoso() ||
                estaElementoVisible(MODAL_CONFIRMACION)
            );
        } catch (Exception e) {
            logger.debug("Tiempo de espera agotado para procesamiento del formulario");
        }
    }
    
    // ===== MÉTODOS DE UTILIDAD =====
    
    /**
     * Obtiene todos los valores actuales del formulario.
     */
    public DatosRegistro obtenerValoresFormulario() {
        DatosRegistro datos = new DatosRegistro();
        
        try {
            if (estaElementoPresente(By.cssSelector("input[name='username'], #username"))) {
                datos.setEmail(obtenerAtributo(By.cssSelector("input[name='username'], #username"), "value"));
            }
            
            if (estaElementoPresente(By.cssSelector("input[name='firstName'], #firstName"))) {
                datos.setNombre(obtenerAtributo(By.cssSelector("input[name='firstName'], #firstName"), "value"));
            }
            
            if (estaElementoPresente(By.cssSelector("input[name='lastName'], #lastName"))) {
                datos.setApellido(obtenerAtributo(By.cssSelector("input[name='lastName'], #lastName"), "value"));
            }
            
            // Obtener estado de checkboxes
            if (estaElementoPresente(By.cssSelector("input[name='terms'], #terms"))) {
                WebElement checkbox = driver.findElement(By.cssSelector("input[name='terms'], #terms"));
                datos.setAceptaTerminos(checkbox.isSelected());
            }
            
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
            String email = obtenerAtributo(By.cssSelector("input[name='username'], #username"), "value");
            String password = obtenerAtributo(By.cssSelector("input[name='password'], #password"), "value");
            
            boolean camposBasicosLlenos = email != null && !email.isEmpty() && 
                                        password != null && !password.isEmpty();
            
            boolean terminosAceptados = true;
            if (estaElementoPresente(By.cssSelector("input[name='terms'], #terms"))) {
                WebElement checkbox = driver.findElement(By.cssSelector("input[name='terms'], #terms"));
                terminosAceptados = checkbox.isSelected();
            }
            
            return camposBasicosLlenos && terminosAceptados;
        } catch (Exception e) {
            logger.debug("Error verificando si el formulario está completo: {}", e.getMessage());
            return false;
        }
    }
}