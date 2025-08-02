package com.automatizacion.proyecto.paginas;

import com.automatizacion.proyecto.datos.ModeloDatosPrueba;
import com.automatizacion.proyecto.enums.TipoMensaje;
import com.automatizacion.proyecto.paginas.interfaces.IPaginaRegistro;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Página de registro para ExpandTesting implementando el patrón Page Object Model.
 * Encapsula todos los elementos y acciones relacionadas con el registro de usuarios.
 * 
 * Basada en la página real: https://practice.expandtesting.com/register
 * 
 * Implementa la interfaz IPaginaRegistro siguiendo el principio de
 * Inversión de Dependencias del SOLID.
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
public class PaginaRegistro extends PaginaBase implements IPaginaRegistro {
    
    private static final Logger logger = LoggerFactory.getLogger(PaginaRegistro.class);
    
    // === URL Y CONFIGURACIÓN ===
    private static final String URL_PAGINA = "https://practice.expandtesting.com/register";
    
    // === ELEMENTOS DEL FORMULARIO PRINCIPAL ===
    
    @FindBy(id = "username")
    private WebElement campoNombre;
    
    @FindBy(id = "password")
    private WebElement campoPassword;
    
    @FindBy(id = "confirmPassword")
    private WebElement campoConfirmarPassword;
    
    // === BOTONES Y CONTROLES ===
    
    @FindBy(xpath = "//button[contains(text(),'Register') or @type='submit']")
    private WebElement botonRegistrar;
    
    @FindBy(xpath = "//a[contains(text(),'Log in') or contains(@href,'login')]")
    private WebElement enlaceLogin;
    
    // === ELEMENTOS DE VALIDACIÓN Y MENSAJES ===
    
    @FindBy(xpath = "//form[contains(@class,'register') or contains(@id,'register')]")
    private WebElement formularioRegistro;
    
    @FindBy(xpath = "//div[contains(@class,'alert-success') or contains(@class,'success')]")
    private WebElement mensajeExito;
    
    @FindBy(xpath = "//div[contains(@class,'alert-danger') or contains(@class,'error')]")
    private WebElement mensajeError;
    
    @FindBy(xpath = "//div[contains(@class,'alert') or contains(@class,'message')]")
    private WebElement mensajeGeneral;
    
    // === ELEMENTOS DE CARGA ===
    
    @FindBy(xpath = "//div[contains(@class,'loading') or contains(@class,'spinner')]")
    private WebElement indicadorCarga;
    
    // === ELEMENTOS DE LA PÁGINA ===
    
    @FindBy(xpath = "//h1[contains(text(),'Register') or contains(text(),'Sign up')]")
    private WebElement tituloRegistro;
    
    @FindBy(xpath = "//main[contains(@class,'container') or contains(@class,'content')]")
    private WebElement contenidoPrincipal;
    
    /**
     * Constructor que inicializa la página con el driver
     * @param driver instancia de WebDriver
     */
    public PaginaRegistro(WebDriver driver) {
        super(driver);
        logger.debug(TipoMensaje.DEBUG.formatearMensaje("PaginaRegistro inicializada para ExpandTesting"));
    }
    
    // === IMPLEMENTACIÓN DE INTERFAZ IPaginaRegistro ===
    
    @Override
    public boolean esPaginaVisible() {
        try {
            // Verificar que estamos en la URL correcta
            String urlActual = driver.getCurrentUrl();
            if (!urlActual.contains("register")) {
                logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                    "URL actual no contiene 'register': " + urlActual));
            }
            
            // Verificar elementos clave
            return esperarElementoVisible(campoNombre, 10) && 
                   esperarElementoVisible(campoPassword, 5) &&
                   esperarElementoVisible(botonRegistrar, 5);
                   
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error verificando visibilidad de página registro: " + e.getMessage()));
            return false;
        }
    }
    
    @Override
    public void llenarFormularioCompleto(ModeloDatosPrueba datos) {
        try {
            logger.info(TipoMensaje.PASO_PRUEBA.formatearMensaje(
                "Llenando formulario completo con: " + datos.generarResumen()));
            
            // Campos obligatorios básicos
            if (datos.getNombre() != null) {
                ingresarNombre(datos.getNombre());
            }
            
            if (datos.getPassword() != null) {
                ingresarPassword(datos.getPassword());
            }
            
            if (datos.getConfirmacionPassword() != null) {
                ingresarConfirmarPassword(datos.getConfirmacionPassword());
            }
            
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "Formulario completado para: " + datos.getCasoPrueba()));
                
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error llenando formulario: " + e.getMessage()));
            throw new RuntimeException("Fallo al llenar formulario de registro", e);
        }
    }
    
    @Override
    public boolean registrarUsuario(ModeloDatosPrueba datos) {
        try {
            logger.info(TipoMensaje.PASO_PRUEBA.formatearMensaje(
                "Iniciando registro de usuario: " + datos.getEmail()));
            
            // Navegar a la página si no estamos allí
            if (!driver.getCurrentUrl().contains("register")) {
                navegarAPagina();
            }
            
            // Verificar que la página esté visible
            if (!esPaginaVisible()) {
                logger.error(TipoMensaje.ERROR.formatearMensaje(
                    "La página de registro no está visible"));
                return false;
            }
            
            // Llenar formulario
            llenarFormularioCompleto(datos);
            
            // Hacer click en registrar
            clickBotonRegistrar();
            
            // Esperar resultado
            return esperarYValidarResultado(datos);
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error durante el registro: " + e.getMessage()));
            return false;
        }
    }
    
    @Override
    public boolean validarElementosPagina() {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "Validando elementos de la página de registro"));
            
            boolean todosPresentes = true;
            
            // Validar campos obligatorios
            if (!esElementoPresente(campoNombre)) {
                logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("Campo nombre no presente"));
                todosPresentes = false;
            }
            
            if (!esElementoPresente(campoPassword)) {
                logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("Campo password no presente"));
                todosPresentes = false;
            }
            
            if (!esElementoPresente(campoConfirmarPassword)) {
                logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("Campo confirmar password no presente"));
                todosPresentes = false;
            }
            
            if (!esElementoPresente(botonRegistrar)) {
                logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("Botón registrar no presente"));
                todosPresentes = false;
            }
            
            return todosPresentes;
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error validando elementos: " + e.getMessage()));
            return false;
        }
    }
    
    // === MÉTODOS ESPECÍFICOS DE CAMPOS ===
    
    /**
     * Ingresa el nombre/username en el campo correspondiente
     * @param nombre nombre a ingresar
     */
    public void ingresarNombre(String nombre) {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Ingresando nombre: " + nombre));
            esperarElementoVisible(campoNombre, 5);
            limpiarYEscribir(campoNombre, nombre);
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error ingresando nombre: " + e.getMessage()));
            throw new RuntimeException("No se pudo ingresar el nombre", e);
        }
    }
    
    /**
     * Ingresa el apellido (si existe el campo)
     * @param apellido apellido a ingresar
     */
    public void ingresarApellido(String apellido) {
        // En ExpandTesting register page no hay campo apellido separado
        logger.debug(TipoMensaje.DEBUG.formatearMensaje(
            "Campo apellido no disponible en esta página, agregando al nombre"));
    }
    
    /**
     * Ingresa el email (reutiliza campo nombre si es necesario)
     * @param email email a ingresar
     */
    public void ingresarEmail(String email) {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Usando email como nombre: " + email));
            // En ExpandTesting, el campo username puede recibir email
            ingresarNombre(email);
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error ingresando email: " + e.getMessage()));
            throw new RuntimeException("No se pudo ingresar el email", e);
        }
    }
    
    /**
     * Ingresa la contraseña
     * @param password contraseña a ingresar
     */
    public void ingresarPassword(String password) {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Ingresando password"));
            esperarElementoVisible(campoPassword, 5);
            limpiarYEscribir(campoPassword, password);
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error ingresando password: " + e.getMessage()));
            throw new RuntimeException("No se pudo ingresar la contraseña", e);
        }
    }
    
    /**
     * Ingresa la confirmación de contraseña
     * @param confirmPassword confirmación de contraseña
     */
    public void ingresarConfirmarPassword(String confirmPassword) {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Ingresando confirmación de password"));
            esperarElementoVisible(campoConfirmarPassword, 5);
            limpiarYEscribir(campoConfirmarPassword, confirmPassword);
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error ingresando confirmación de password: " + e.getMessage()));
            throw new RuntimeException("No se pudo ingresar la confirmación de contraseña", e);
        }
    }
    
    /**
     * Ingresa el teléfono (no aplicable en esta página)
     * @param telefono teléfono a ingresar
     */
    public void ingresarTelefono(String telefono) {
        logger.debug(TipoMensaje.DEBUG.formatearMensaje(
            "Campo teléfono no disponible en esta página"));
    }
    
    // === MÉTODOS DE INTERACCIÓN ===
    
    /**
     * Hace click en el botón de registrar
     */
    public void clickBotonRegistrar() {
        try {
            logger.info(TipoMensaje.PASO_PRUEBA.formatearMensaje(
                "Haciendo click en botón registrar"));
            esperarElementoClickeable(botonRegistrar, 5);
            hacerClick(botonRegistrar);
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error haciendo click en registrar: " + e.getMessage()));
            throw new RuntimeException("No se pudo hacer click en el botón registrar", e);
        }
    }
    
    /**
     * Hace click en el enlace de login
     */
    public void clickEnlaceLogin() {
        try {
            logger.info(TipoMensaje.PASO_PRUEBA.formatearMensaje(
                "Haciendo click en enlace de login"));
            esperarElementoClickeable(enlaceLogin, 5);
            hacerClick(enlaceLogin);
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error haciendo click en enlace login: " + e.getMessage()));
            throw new RuntimeException("No se pudo hacer click en el enlace de login", e);
        }
    }
    
    // === MÉTODOS DE VALIDACIÓN ===
    
    /**
     * Verifica si el registro fue exitoso
     * @return true si el registro fue exitoso
     */
    public boolean verificarRegistroExitoso() {
        try {
            // Esperar un poco para que aparezca el resultado
            Thread.sleep(2000);
            
            // Verificar mensaje de éxito
            if (esElementoPresente(mensajeExito)) {
                String mensaje = obtenerTexto(mensajeExito);
                logger.info(TipoMensaje.EXITO.formatearMensaje(
                    "Registro exitoso - Mensaje: " + mensaje));
                return true;
            }
            
            // Verificar si hay redirección (cambio de URL)
            String urlActual = driver.getCurrentUrl();
            if (!urlActual.contains("register")) {
                logger.info(TipoMensaje.EXITO.formatearMensaje(
                    "Registro exitoso - Redirección a: " + urlActual));
                return true;
            }
            
            // Si llegamos aquí, probablemente hay un error
            return false;
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error verificando registro exitoso: " + e.getMessage()));
            return false;
        }
    }
    
    /**
     * Obtiene el mensaje de error si existe
     * @return mensaje de error o cadena vacía
     */
    public String obtenerMensajeError() {
        try {
            if (esElementoPresente(mensajeError)) {
                String mensaje = obtenerTexto(mensajeError);
                logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                    "Mensaje de error encontrado: " + mensaje));
                return mensaje;
            }
            
            if (esElementoPresente(mensajeGeneral)) {
                String mensaje = obtenerTexto(mensajeGeneral);
                if (mensaje.toLowerCase().contains("error") || 
                    mensaje.toLowerCase().contains("invalid") ||
                    mensaje.toLowerCase().contains("failed")) {
                    logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                        "Mensaje de error general: " + mensaje));
                    return mensaje;
                }
            }
            
            return "";
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error obteniendo mensaje de error: " + e.getMessage()));
            return "";
        }
    }
    
    /**
     * Verifica si hay errores de validación
     * @return true si hay errores
     */
    public boolean hayErroresValidacion() {
        return !obtenerMensajeError().isEmpty();
    }
    
    /**
     * Obtiene el título de la página
     * @return título de la página
     */
    public String obtenerTitulo() {
        try {
            String titulo = driver.getTitle();
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Título de página: " + titulo));
            return titulo;
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error obteniendo título: " + e.getMessage()));
            return "";
        }
    }
    
    // === MÉTODOS PRIVADOS DE APOYO ===
    
    /**
     * Navega a la página de registro
     */
    private void navegarAPagina() {
        try {
            logger.info(TipoMensaje.PASO_PRUEBA.formatearMensaje(
                "Navegando a página de registro: " + URL_PAGINA));
            driver.get(URL_PAGINA);
            
            // Esperar a que la página cargue
            esperarElementoVisible(campoNombre, 10);
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error navegando a página de registro: " + e.getMessage()));
            throw new RuntimeException("No se pudo navegar a la página de registro", e);
        }
    }
    
    /**
     * Espera y valida el resultado del registro
     * @param datos datos utilizados en el registro
     * @return true si el resultado es el esperado
     */
    private boolean esperarYValidarResultado(ModeloDatosPrueba datos) {
        try {
            // Esperar un momento para que se procese
            Thread.sleep(3000);
            
            boolean registroExitoso = verificarRegistroExitoso();
            String mensajeError = obtenerMensajeError();
            
            logger.info(TipoMensaje.INFORMATIVO.formatearMensaje(
                "Resultado registro - Exitoso: " + registroExitoso + 
                ", Error: " + mensajeError));
            
            // Si se esperaba éxito
            if (datos.isEsValido()) {
                return registroExitoso;
            } else {
                // Si se esperaba fallo
                return !registroExitoso || !mensajeError.isEmpty();
            }
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error esperando resultado: " + e.getMessage()));
            return false;
        }
    }
    
    // === MÉTODOS ADICIONALES PARA COMPATIBILIDAD ===
    
    /**
     * Configura newsletter (no aplicable)
     * @param suscribir si suscribirse o no
     */
    public void configurarNewsletter(boolean suscribir) {
        logger.debug(TipoMensaje.DEBUG.formatearMensaje(
            "Newsletter no disponible en esta página"));
    }
    
    /**
     * Acepta términos y condiciones (no aplicable)
     */
    public void aceptarTerminos() {
        logger.debug(TipoMensaje.DEBUG.formatearMensaje(
            "Términos y condiciones no requeridos en esta página"));
    }
    
    /**
     * Limpia el formulario (implementación básica)
     */
    public void limpiarFormulario() {
        try {
            logger.info(TipoMensaje.PASO_PRUEBA.formatearMensaje(
                "Limpiando formulario de registro"));
            
            if (esElementoPresente(campoNombre)) {
                campoNombre.clear();
            }
            
            if (esElementoPresente(campoPassword)) {
                campoPassword.clear();
            }
            
            if (esElementoPresente(campoConfirmarPassword)) {
                campoConfirmarPassword.clear();
            }
            
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                "Error limpiando formulario: " + e.getMessage()));
        }
    }
    
    /**
     * Obtiene la URL de la página
     * @return URL de la página de registro
     */
    public static String obtenerURL() {
        return URL_PAGINA;
    }
}