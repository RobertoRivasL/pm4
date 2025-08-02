package com.automatizacion.proyecto.paginas;

import com.automatizacion.proyecto.datos.ModeloDatosPrueba;
import com.automatizacion.proyecto.enums.TipoMensaje;
import com.automatizacion.proyecto.paginas.interfaces.IPaginaRegistro;
import com.automatizacion.proyecto.utilidades.GestorCapturaPantalla;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Página de registro implementando el patrón Page Object Model.
 * Encapsula todos los elementos y acciones relacionadas con el registro de usuarios.
 * 
 * Implementa la interfaz IPaginaRegistro siguiendo el principio de
 * Inversión de Dependencias del SOLID.
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
public class PaginaRegistro extends PaginaBase implements IPaginaRegistro {
    
    private static final Logger logger = LoggerFactory.getLogger(PaginaRegistro.class);
    
    // ===== LOCALIZADORES DE ELEMENTOS =====
    
    @FindBy(id = "nombre")
    private WebElement campoNombre;
    
    @FindBy(id = "apellido") 
    private WebElement campoApellido;
    
    @FindBy(id = "email")
    private WebElement campoEmail;
    
    @FindBy(id = "password")
    private WebElement campoPassword;
    
    @FindBy(id = "confirmarPassword")
    private WebElement campoConfirmarPassword;
    
    @FindBy(id = "telefono")
    private WebElement campoTelefono;
    
    @FindBy(id = "fechaNacimiento")
    private WebElement campoFechaNacimiento;
    
    @FindBy(id = "genero")
    private WebElement selectGenero;
    
    @FindBy(id = "terminosCondiciones")
    private WebElement checkboxTerminos;
    
    @FindBy(id = "newsletter")
    private WebElement checkboxNewsletter;
    
    @FindBy(id = "btnRegistrar")
    private WebElement botonRegistrar;
    
    @FindBy(id = "btnLimpiar")
    private WebElement botonLimpiar;
    
    @FindBy(xpath = "//div[@class='mensaje-error']")
    private WebElement mensajeError;
    
    @FindBy(xpath = "//div[@class='mensaje-exito']")
    private WebElement mensajeExito;
    
    @FindBy(xpath = "//span[@class='error-campo']")
    private WebElement errorCampo;
    
    // Localizadores como constantes para mejor mantenimiento
    private static final By FORMULARIO_REGISTRO = By.id("formularioRegistro");
    private static final By SPINNER_CARGA = By.className("spinner-loading");
    private static final By MODAL_CONFIRMACION = By.id("modalConfirmacion");
    
    /**
     * Constructor que inicializa la página de registro.
     * 
     * @param driver WebDriver para interactuar con la página
     */
    public PaginaRegistro(WebDriver driver) {
        super(driver);
        logger.info("Inicializando página de registro");
    }
    
    /**
     * Implementación del método de la interfaz para verificar si la página está visible.
     */
    @Override
    public boolean esPaginaVisible() {
        try {
            return esElementoVisible(FORMULARIO_REGISTRO) && 
                   campoNombre.isDisplayed() && 
                   campoEmail.isDisplayed();
        } catch (Exception e) {
            logger.warn("Error al verificar visibilidad de página de registro: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Ingresa el nombre en el campo correspondiente.
     */
    @Override
    public void ingresarNombre(String nombre) {
        try {
            espera.esperarElementoVisible(By.id("nombre"));
            limpiarEIngresarTexto(campoNombre, nombre);
            logger.debug("Nombre ingresado: {}", nombre);
        } catch (Exception e) {
            logger.error("Error al ingresar nombre: {}", e.getMessage());
            throw new RuntimeException("No se pudo ingresar el nombre", e);
        }
    }
    
    /**
     * Ingresa el apellido en el campo correspondiente.
     */
    @Override
    public void ingresarApellido(String apellido) {
        try {
            espera.esperarElementoVisible(By.id("apellido"));
            limpiarEIngresarTexto(campoApellido, apellido);
            logger.debug("Apellido ingresado: {}", apellido);
        } catch (Exception e) {
            logger.error("Error al ingresar apellido: {}", e.getMessage());
            throw new RuntimeException("No se pudo ingresar el apellido", e);
        }
    }
    
    /**
     * Ingresa el email en el campo correspondiente.
     */
    @Override
    public void ingresarEmail(String email) {
        try {
            espera.esperarElementoVisible(By.id("email"));
            limpiarEIngresarTexto(campoEmail, email);
            logger.debug("Email ingresado: {}", email);
        } catch (Exception e) {
            logger.error("Error al ingresar email: {}", e.getMessage());
            throw new RuntimeException("No se pudo ingresar el email", e);
        }
    }
    
    /**
     * Ingresa la contraseña en el campo correspondiente.
     */
    @Override
    public void ingresarPassword(String password) {
        try {
            espera.esperarElementoVisible(By.id("password"));
            limpiarEIngresarTexto(campoPassword, password);
            logger.debug("Password ingresado");
        } catch (Exception e) {
            logger.error("Error al ingresar password: {}", e.getMessage());
            throw new RuntimeException("No se pudo ingresar el password", e);
        }
    }
    
    /**
     * Ingresa la confirmación de contraseña.
     */
    @Override
    public void ingresarConfirmarPassword(String confirmarPassword) {
        try {
            espera.esperarElementoVisible(By.id("confirmarPassword"));
            limpiarEIngresarTexto(campoConfirmarPassword, confirmarPassword);
            logger.debug("Confirmación de password ingresada");
        } catch (Exception e) {
            logger.error("Error al ingresar confirmación de password: {}", e.getMessage());
            throw new RuntimeException("No se pudo ingresar la confirmación de password", e);
        }
    }
    
    /**
     * Ingresa el teléfono en el campo correspondiente.
     */
    @Override
    public void ingresarTelefono(String telefono) {
        try {
            if (telefono != null && !telefono.trim().isEmpty()) {
                espera.esperarElementoVisible(By.id("telefono"));
                limpiarEIngresarTexto(campoTelefono, telefono);
                logger.debug("Teléfono ingresado: {}", telefono);
            }
        } catch (Exception e) {
            logger.error("Error al ingresar teléfono: {}", e.getMessage());
            throw new RuntimeException("No se pudo ingresar el teléfono", e);
        }
    }
    
    /**
     * Selecciona el género del dropdown.
     */
    @Override
    public void seleccionarGenero(String genero) {
        try {
            if (genero != null && !genero.trim().isEmpty()) {
                espera.esperarElementoVisible(By.id("genero"));
                Select selectElemento = new Select(selectGenero);
                selectElemento.selectByVisibleText(genero);
                logger.debug("Género seleccionado: {}", genero);
            }
        } catch (Exception e) {
            logger.error("Error al seleccionar género: {}", e.getMessage());
            throw new RuntimeException("No se pudo seleccionar el género", e);
        }
    }
    
    /**
     * Acepta los términos y condiciones.
     */
    @Override
    public void aceptarTerminos() {
        try {
            espera.esperarElementoClickeable(By.id("terminosCondiciones"));
            if (!checkboxTerminos.isSelected()) {
                hacerClicRobusto(checkboxTerminos);
                logger.debug("Términos y condiciones aceptados");
            }
        } catch (Exception e) {
            logger.error("Error al aceptar términos: {}", e.getMessage());
            throw new RuntimeException("No se pudieron aceptar los términos", e);
        }
    }
    
    /**
     * Hace clic en el botón de registrar.
     */
    @Override
    public void clickBotonRegistrar() {
        try {
            espera.esperarElementoClickeable(By.id("btnRegistrar"));
            hacerClicRobusto(botonRegistrar);
            
            // Esperar a que la página procese el registro
            espera.esperarInvisibilidadDelElemento(SPINNER_CARGA);
            logger.info("Click en botón registrar ejecutado");
            
        } catch (Exception e) {
            logger.error("Error al hacer click en registrar: {}", e.getMessage());
            throw new RuntimeException("No se pudo hacer click en registrar", e);
        }
    }
    
    /**
     * Registra un usuario utilizando el modelo de datos de prueba.
     */
    @Override
    public boolean registrarUsuario(ModeloDatosPrueba datos) {
        try {
            logger.info("Iniciando registro de usuario: {}", datos.getEmail());
            
            // Verificar que la página esté visible
            if (!esPaginaVisible()) {
                throw new RuntimeException("La página de registro no está visible");
            }
            
            // Llenar formulario con los datos
            llenarFormulario(datos);
            
            // Capturar pantalla antes del submit
            GestorCapturaPantalla.capturarPantalla(driver, "antes_registro_" + datos.getCasoPrueba());
            
            // Hacer click en registrar
            clickBotonRegistrar();
            
            // Verificar resultado
            boolean registroExitoso = verificarRegistroExitoso();
            
            // Capturar pantalla después del resultado
            GestorCapturaPantalla.capturarPantalla(driver, 
                "despues_registro_" + datos.getCasoPrueba() + 
                (registroExitoso ? "_exitoso" : "_fallido"));
            
            logger.info("Registro completado. Exitoso: {}", registroExitoso);
            return registroExitoso;
            
        } catch (Exception e) {
            logger.error("Error durante el registro: {}", e.getMessage());
            GestorCapturaPantalla.capturarPantalla(driver, "error_registro_" + datos.getCasoPrueba());
            return false;
        }
    }
    
    /**
     * Obtiene el mensaje de error si existe.
     */
    @Override
    public String obtenerMensajeError() {
        try {
            if (esElementoVisible(By.xpath("//div[@class='mensaje-error']"))) {
                return mensajeError.getText().trim();
            }
            if (esElementoVisible(By.xpath("//span[@class='error-campo']"))) {
                return errorCampo.getText().trim();
            }
            return "";
        } catch (Exception e) {
            logger.warn("No se pudo obtener mensaje de error: {}", e.getMessage());
            return "";
        }
    }
    
    /**
     * Obtiene el mensaje de éxito si existe.
     */
    @Override
    public String obtenerMensajeExito() {
        try {
            if (esElementoVisible(By.xpath("//div[@class='mensaje-exito']"))) {
                return mensajeExito.getText().trim();
            }
            return "";
        } catch (Exception e) {
            logger.warn("No se pudo obtener mensaje de éxito: {}", e.getMessage());
            return "";
        }
    }
    
    /**
     * Verifica si hay errores de validación en la página.
     */
    @Override
    public boolean hayErroresValidacion() {
        try {
            return esElementoVisible(By.xpath("//div[@class='mensaje-error']")) ||
                   esElementoVisible(By.xpath("//span[@class='error-campo']")) ||
                   esElementoVisible(By.xpath("//*[contains(@class,'error')]"));
        } catch (Exception e) {
            logger.warn("Error al verificar errores de validación: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Limpia el formulario haciendo click en el botón limpiar.
     */
    @Override
    public void limpiarFormulario() {
        try {
            if (botonLimpiar.isDisplayed()) {
                hacerClicRobusto(botonLimpiar);
                logger.debug("Formulario limpiado");
            }
        } catch (Exception e) {
            logger.warn("Error al limpiar formulario: {}", e.getMessage());
        }
    }
    
    // ===== MÉTODOS PRIVADOS DE APOYO =====
    
    /**
     * Llena el formulario con los datos del modelo.
     */
    private void llenarFormulario(ModeloDatosPrueba datos) {
        if (datos.getNombre() != null) ingresarNombre(datos.getNombre());
        if (datos.getApellido() != null) ingresarApellido(datos.getApellido());
        if (datos.getEmail() != null) ingresarEmail(datos.getEmail());
        if (datos.getPassword() != null) ingresarPassword(datos.getPassword());
        if (datos.getConfirmarPassword() != null) ingresarConfirmarPassword(datos.getConfirmarPassword());
        if (datos.getTelefono() != null) ingresarTelefono(datos.getTelefono());
        if (datos.getGenero() != null) seleccionarGenero(datos.getGenero());
        
        // Aceptar términos si es requerido
        if (datos.isAceptarTerminos()) {
            aceptarTerminos();
        }
    }
    
    /**
     * Verifica si el registro fue exitoso.
     */
    private boolean verificarRegistroExitoso() {
        try {
            // Verificar si aparece mensaje de éxito
            if (esElementoVisible(By.xpath("//div[@class='mensaje-exito']"))) {
                return true;
            }
            
            // Verificar si redirige a página de éxito o login
            String urlActual = obtenerUrlActual();
            if (urlActual.contains("registro-exitoso") || urlActual.contains("login")) {
                return true;
            }
            
            // Verificar si aparece modal de confirmación
            if (esElementoVisible(MODAL_CONFIRMACION)) {
                return true;
            }
            
            // Si hay errores, el registro falló
            return !hayErroresValidacion();
            
        } catch (Exception e) {
            logger.warn("Error al verificar registro exitoso: {}", e.getMessage());
            return false;
        }
    }
}